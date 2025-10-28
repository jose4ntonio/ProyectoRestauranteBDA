package Negocio;

import ConexionBD.ConexionBD; 
import DAOs.ComandaDAO;
import Entidades.Comanda;
import Entidades.DetalleComanda;

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
 */
public class ComandaService {

    private final ComandaDAO comandaDAO = new ComandaDAO();

    /** Crea el folio con la forma OB-YYYYMMDD-XXX consultando el consecutivo del día. */
    public String generarFolio() {
        String fecha = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefijo = "OB-" + fecha + "-";
        int consecutivo = 1;

        String sql = "SELECT COUNT(*) AS total FROM comanda WHERE folio LIKE ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) consecutivo = rs.getInt("total") + 1;
            }
        } catch (SQLException e) {
            // si falla la conexión, no reventamos el folio
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
        if (detalles == null || detalles.isEmpty()) return 0.0;
        double total = 0.0;
        for (DetalleComanda d : detalles) {
            total += (d.getTotalProducto() > 0)
                    ? d.getTotalProducto()
                    : d.getCantidad() * d.getPrecioUnitario();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Valida que la comanda tenga los campos mínimos para persistir.
     * - Mesa no vacía
     * - Estado válido
     * - Al menos un detalle con cantidad > 0 y producto asignado
     */
    public void validarComanda(Comanda c, List<DetalleComanda> detalles) {
        if (c == null) throw new IllegalArgumentException("Comanda nula.");
        if (c.getMesa() == null || c.getMesa().isBlank())
            throw new IllegalArgumentException("Debes indicar la mesa.");
        if (detalles == null || detalles.isEmpty())
            throw new IllegalArgumentException("La comanda debe tener al menos un producto.");

        for (DetalleComanda d : detalles) {
            // **CAMBIO clave**: ya no existe getIdProducto(); ahora es getProducto()
            if (d.getProducto() == null || d.getProducto().getIdProducto() <= 0)
                throw new IllegalArgumentException("Detalle con producto inválido.");
            if (d.getCantidad() <= 0)
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            if (d.getPrecioUnitario() < 0)
                throw new IllegalArgumentException("Precio unitario no puede ser negativo.");
        }
    }

    /** Construye una comanda lista para persistir. */
    public Comanda prepararComanda(String mesa, String clienteFrecuente,
                                   List<DetalleComanda> detalles, String estado) {
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

    // **CAMBIO de firma**: usa List<DetalleComanda> (no fully-qualified raro)
    public boolean crearComandaConDetalles(Comanda c, List<DetalleComanda> detalles) {
        validarComanda(c, detalles);
        if (!"ABIERTA".equalsIgnoreCase(c.getEstado()))
            throw new IllegalStateException("Solo se pueden registrar nuevas comandas en estado ABIERTA.");

        // Enlazar la relación bidireccional antes de persistir (para cascade)
        if (c.getDetalles() != null) {
            c.getDetalles().clear();
        }
        for (DetalleComanda d : detalles) {
            c.addDetalle(d);         // <-- ESTE método debe existir en Comanda (ver nota abajo)
        }

        // recalcular por si hubo cambios
        c.setTotal(calcularTotal(detalles));

        return comandaDAO.agregarComanda(c);
    }

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
