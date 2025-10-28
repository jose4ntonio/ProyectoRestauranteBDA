
package Negocio;

import Entidades.Mesa;

/**
 * Lógica de negocio para mesa.
 * En este proyecto no existe un MesaDAO, así que esta clase provee
 * validaciones/comodines para cuando la GUI capture datos de mesas.
 *
 * Si más adelante agregas un DAO para mesa, aquí es donde se orquesta.
 */
public class MesaService {

    public void validarMesa(Mesa m) {
        if (m == null) throw new IllegalArgumentException("Mesa nula.");
        if (m.getUbicacion() == null || m.getUbicacion().isBlank())
            throw new IllegalArgumentException("La ubicación es obligatoria.");
        if (m.getCapacidad() <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
    }

    /**
     * Genera una etiqueta legible para mostrar una mesa en la GUI.
     * Ej: "Mesa #12 · Terraza · 4 pax"
     */
    public String etiquetaMesa(Mesa m) {
        validarMesa(m);
        return "Mesa #" + m.getIdMesa() + " · " + m.getUbicacion() + " · " + m.getCapacidad() + " pax";
    }
}
