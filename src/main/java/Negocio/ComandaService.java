
package Negocio;

import ConexionBD.ConexionBD;
import DAOs.ComandaDAO;
import Entidades.Comanda;
import Entidades.DetalleComanda;
import Entidades.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Lógica de negocio para Comandas.
 * - Genera folio OB-YYYYMMDD-XXX.
 * - Valida que haya ≥ 1 producto en captura.
 * - Calcula total acumulado.
 * - Normaliza estado (ABIERTA/ENTREGADA/CANCELADA).
 *
 * NOTA: Este servicio delega la persistencia a ComandaDAO (que debes implementar
 * con una sola transacción para comanda + detalles). Si quieres, te paso ese DAO.
 */
public class ComandaService {

    private final ComandaDAO comandaDAO = new ComandaDAO();

    /** Crea el folio con la forma OB-YYYYMMDD-XXX consultando el consecutivo del día. */
    public String generarFolio() {
        String fecha = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefijo = "OB-" + fecha + "-";
        int consecutivo = 1;

        // Contar cuántas comandas existen hoy para asignar el siguiente consecutivo.
        String sql = "SELECT COUNT(*) AS total FROM comanda WHERE folio LIKE ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) consecutivo = rs.getInt("total") + 1;
            }
        } catch (SQLException e) {
            // Si hay problema de conexión, igual devolvemos un folio válido con 001
            consecutivo = Math.max(1, consecutivo);
        }
        return prefijo + String.format("%03d", consecutivo);
    }

    /** Normaliza el estado a valores válidos. */
    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) return "ABIERTA";
        String e = estado.trim().toUpperCase(Locale.ROOT);
        if (e.equals("ABIERTA") || e.equals("ENTREGADA") || e.equals("CANCELADA")) return e;
        return "ABIERTA";
    }

    /** Calcula el total acumulado a partir de los detalles. */
    public double calcularTotal(List<DetalleComanda> detalles) {
        if (detalles == null || detalles.isEmpty())
            return 0.0;
        double total = 0.0;
        for (DetalleComanda d : detalles) {
            // Si ya viene totalProducto, úsalo; si no, calcula a partir de cantidad * precioUnitario.
            if (d.getTotalProducto() > 0) {
                total += d.getTotalProducto();
            } else {
                total += d.getCantidad() * d.getPrecioUnitario();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Valida que la comanda tenga los campos mínimos para persistir.
     * - Mesa (string) no vacía
     * - Estado válido
     * - Al menos un detalle con cantidad > 0
     */
    public void validarComanda(Comanda c, List<DetalleComanda> detalles) {
        if (c == null) throw new IllegalArgumentException("Comanda nula.");
        if (c.getMesa() == null || c.getMesa().isBlank())
            throw new IllegalArgumentException("Debes indicar la mesa.");
        if (detalles == null || detalles.isEmpty())
            throw new IllegalArgumentException("La comanda debe tener al menos un producto.");
        for (DetalleComanda d : detalles) {
            if (d.getIdProducto() <= 0)
                throw new IllegalArgumentException("Detalle con producto inválido.");
            if (d.getCantidad() <= 0)
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            if (d.getPrecioUnitario() < 0)
                throw new IllegalArgumentException("Precio unitario no puede ser negativo.");
        }
    }

    /**
     * Construye una comanda lista para persistir (folio, fecha, total, estado normalizado).
     */
    public Comanda prepararComanda(String mesa, String clienteFrecuente, List<DetalleComanda> detalles, String estado) {
        if (mesa == null || mesa.isBlank())
            throw new IllegalArgumentException("La mesa es obligatoria.");
        if (detalles == null || detalles.isEmpty())
            throw new IllegalArgumentException("Se requiere al menos un producto.");

        Comanda c = new Comanda();
        c.setFolio(generarFolio());
        c.setFecha(new Date());
        c.setMesa(mesa.trim());
        c.setClienteFrecuente(clienteFrecuente != null ? clienteFrecuente.trim() : null);
        c.setEstado(normalizarEstado(estado));
        c.setTotal(calcularTotal(detalles));

        validarComanda(c, detalles);
        return c;
    }

    /* ==================== Delegación al DAO ==================== */

    public boolean crearComandaConDetalles(Comanda c, java.util.List<Entidades.DetalleComanda> detalles) {
    validarComanda(c, detalles);
    if (!"ABIERTA".equalsIgnoreCase(c.getEstado()))
        throw new IllegalStateException("Solo se pueden registrar nuevas comandas en estado ABIERTA.");

    // ENLAZAR detalles -> comanda (necesario para cascade)
    c.getDetalles().clear();
    for (Entidades.DetalleComanda d : detalles) {
        c.addDetalle(d); // setComanda + add a la lista
    }

    // total ya calculado antes (o recalcula aquí)
    return comandaDAO.agregarComanda(c);
}

    // Los demás métodos son pasarelas hacia el DAO (listar, obtener, actualizar, eliminar)
    public List<Comanda> listarComandas() { return comandaDAO.listarComandas(); }

    public Comanda obtenerComanda(int idComanda) {
        if (idComanda <= 0) throw new IllegalArgumentException("ID de comanda inválido.");
        return comandaDAO.obtenerComanda(idComanda);
    }

    public boolean actualizarComanda(Comanda c) {
        if (c == null || c.getIdComanda() <= 0) throw new IllegalArgumentException("Comanda inválida.");
        c.setEstado(normalizarEstado(c.getEstado()));
        return comandaDAO.actualizarComanda(c);
    }

    public boolean eliminarComanda(int idComanda) {
        if (idComanda <= 0) throw new IllegalArgumentException("ID de comanda inválido.");
        return comandaDAO.eliminarComanda(idComanda);
    }
}
