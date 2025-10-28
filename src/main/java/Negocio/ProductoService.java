
package Negocio;

import DAOs.ProductoDAO;
import Entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Lógica de negocio para productos del menú.
 * - Valida nombre, precio y tipo.
 * - Evita duplicados de nombre.
 * - Orquesta las operaciones con ProductoDAO.
 * - Incluye búsqueda por nombre para integrarse con FrmComandas.
 */
public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();

    /* ==================== CRUD ==================== */

    public boolean crearProducto(Producto p) {
        normalizarCampos(p);
        validarProducto(p, false);

        // Evitar duplicado por nombre (validación amable)
        for (Producto existente : listarProductos()) {
            if (existente.getNombre().equalsIgnoreCase(p.getNombre())) {
                throw new IllegalArgumentException("Ya existe un producto con el nombre: " + p.getNombre());
            }
        }
        return productoDAO.agregarProducto(p);
    }

    public boolean actualizarProducto(Producto p) {
        if (p == null || p.getIdProducto() <= 0)
            throw new IllegalArgumentException("ID de producto inválido.");

        normalizarCampos(p);
        validarProducto(p, true);

        // Verificar que el nombre (si cambió) no choque con otro
        for (Producto existente : listarProductos()) {
            if (existente.getIdProducto() != p.getIdProducto()
                    && existente.getNombre().equalsIgnoreCase(p.getNombre())) {
                throw new IllegalArgumentException("Ya existe otro producto con el nombre: " + p.getNombre());
            }
        }
        return productoDAO.actualizarProducto(p);
    }

    public boolean eliminarProducto(int idProducto) {
        if (idProducto <= 0) throw new IllegalArgumentException("ID de producto inválido.");
        return productoDAO.eliminarProducto(idProducto);
    }

    public Producto obtenerProducto(int idProducto) {
        if (idProducto <= 0) throw new IllegalArgumentException("ID de producto inválido.");
        return productoDAO.obtenerProducto(idProducto);
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = productoDAO.listarProductos();
        return lista != null ? lista : new ArrayList<>();
    }

    /* ==================== Búsquedas / Helpers ==================== */

    /**
     * Búsqueda exacta por nombre (case-insensitive).
     * Útil para FrmComandas al reconstruir el idProducto desde el nombre mostrado en la tabla.
     */
    public Producto buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return null;
        String n = nombre.trim();
        for (Producto p : listarProductos()) {
            if (p.getNombre() != null && p.getNombre().equalsIgnoreCase(n)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Búsqueda parcial por nombre (contiene, case-insensitive).
     * Útil para buscadores/auto-complete.
     */
    public List<Producto> buscarPorNombreLike(String q) {
        List<Producto> out = new ArrayList<>();
        if (q == null) return out;
        String needle = q.trim().toLowerCase(Locale.ROOT);
        if (needle.isBlank()) return listarProductos();
        for (Producto p : listarProductos()) {
            String nm = p.getNombre() != null ? p.getNombre().toLowerCase(Locale.ROOT) : "";
            if (nm.contains(needle)) out.add(p);
        }
        return out;
    }

    /** Lista productos filtrando por tipo (PLATILLO/BEBIDA/POSTRE/OTRO, etc.). */
    public List<Producto> listarPorTipo(String tipo) {
        List<Producto> out = new ArrayList<>();
        if (tipo == null || tipo.isBlank()) return out;
        String t = tipo.trim().toLowerCase(Locale.ROOT);
        for (Producto p : listarProductos()) {
            String pt = p.getTipo() != null ? p.getTipo().toLowerCase(Locale.ROOT) : "";
            if (pt.equals(t)) out.add(p);
        }
        return out;
    }

    /** ¿Existe ya un producto con ese nombre? (case-insensitive) */
    public boolean existeNombre(String nombre) {
        return buscarPorNombre(nombre) != null;
    }

    /** Cambiar precio de un producto por id (válida no-negativo). */
    public boolean actualizarPrecio(int idProducto, double nuevoPrecio) {
        if (idProducto <= 0) throw new IllegalArgumentException("ID inválido.");
        if (nuevoPrecio < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
        Producto p = obtenerProducto(idProducto);
        if (p == null) throw new IllegalArgumentException("Producto no encontrado.");
        p.setPrecio(nuevoPrecio);
        return actualizarProducto(p);
    }

    /* ==================== Validaciones ==================== */

    private void validarProducto(Producto p, boolean esUpdate) {
        if (p == null) throw new IllegalArgumentException("Producto nulo.");
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (p.getNombre().length() > 100)
            throw new IllegalArgumentException("El nombre supera 100 caracteres.");
        if (p.getPrecio() < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        if (p.getTipo() == null || p.getTipo().isBlank())
            throw new IllegalArgumentException("El tipo de producto es obligatorio.");

        // Tipos sugeridos (normalización no bloqueante)
        String tipo = p.getTipo().trim().toUpperCase(Locale.ROOT);
        if (tipo.equals("PLATILLO") || tipo.equals("BEBIDA") || tipo.equals("POSTRE") || tipo.equals("OTRO")) {
            p.setTipo(tipo);
        } else {
            // Si pones "Comida", "Snack", etc., no bloqueamos; solo recortamos espacios.
            p.setTipo(p.getTipo().trim());
        }
    }

    private void normalizarCampos(Producto p) {
        if (p == null) return;
        if (p.getNombre() != null) p.setNombre(p.getNombre().trim());
        if (p.getTipo() != null)   p.setTipo(p.getTipo().trim());
        // precio se valida; no hay normalización
    }
}
