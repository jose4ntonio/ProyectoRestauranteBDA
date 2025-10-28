
package Negocio;

import DAOs.IngredienteDAO;
import Entidades.Ingrediente;
import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de negocio para ingredientes (inventario).
 * - Valida nombre, unidad y stock.
 * - Evita duplicados por (nombre, unidad).
 * - Expone operación de ajuste de stock.
 */
public class IngredienteService {

    private final IngredienteDAO ingredienteDAO = new IngredienteDAO();

    public boolean crearIngrediente(Ingrediente i) {
        validarIngrediente(i, false);

        // Evitar duplicado nombre+unidad (validación amable)
        for (Ingrediente ex : listarIngredientes()) {
            if (ex.getNombre().equalsIgnoreCase(i.getNombre())
                    && ex.getUnidad().equalsIgnoreCase(i.getUnidad())) {
                throw new IllegalArgumentException("Ya existe un ingrediente '" + i.getNombre()
                        + "' con unidad '" + i.getUnidad() + "'.");
            }
        }
        return ingredienteDAO.agregarIngrediente(i);
    }

    public boolean actualizarIngrediente(Ingrediente i) {
        if (i == null || i.getIdIngrediente() <= 0)
            throw new IllegalArgumentException("ID de ingrediente inválido.");
        validarIngrediente(i, true);

        // Evitar duplicado nombre+unidad con otro registro
        for (Ingrediente ex : listarIngredientes()) {
            if (ex.getIdIngrediente() != i.getIdIngrediente()
                    && ex.getNombre().equalsIgnoreCase(i.getNombre())
                    && ex.getUnidad().equalsIgnoreCase(i.getUnidad())) {
                throw new IllegalArgumentException("Otro ingrediente con mismo nombre/unidad ya existe.");
            }
        }
        return ingredienteDAO.actualizarIngrediente(i);
    }

    public boolean eliminarIngrediente(int idIngrediente) {
        if (idIngrediente <= 0) throw new IllegalArgumentException("ID de ingrediente inválido.");
        return ingredienteDAO.eliminarIngrediente(idIngrediente);
    }

    public Ingrediente obtenerIngrediente(int idIngrediente) {
        if (idIngrediente <= 0) throw new IllegalArgumentException("ID de ingrediente inválido.");
        return ingredienteDAO.obtenerIngrediente(idIngrediente);
    }

    public List<Ingrediente> listarIngredientes() {
        List<Ingrediente> lista = ingredienteDAO.listarIngredientes();
        return lista != null ? lista : new ArrayList<>();
    }

    /**
     * Ajusta (suma/resta) stock de un ingrediente.
     * @param idIngrediente ID
     * @param delta cantidad a sumar (negativa para restar)
     */
    public boolean ajustarStock(int idIngrediente, double delta) {
        Ingrediente i = obtenerIngrediente(idIngrediente);
        if (i == null) throw new IllegalArgumentException("Ingrediente no encontrado.");
        double nuevo = i.getStock() + delta;
        if (nuevo < 0) throw new IllegalArgumentException("Stock no puede quedar negativo.");
        i.setStock(nuevo);
        return ingredienteDAO.actualizarIngrediente(i);
    }

    /* ==================== Validaciones ==================== */

    private void validarIngrediente(Ingrediente i, boolean esUpdate) {
        if (i == null) throw new IllegalArgumentException("Ingrediente nulo.");
        if (i.getNombre() == null || i.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (i.getUnidad() == null || i.getUnidad().isBlank())
            throw new IllegalArgumentException("La unidad es obligatoria.");
        if (i.getStock() < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo.");
    }
}

