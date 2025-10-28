
package Entidades;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "mesa")
public class Mesa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMesa;

    @Column(nullable = false, length = 50)
    private String ubicacion; // Ejemplo: "Terraza", "Ventana", "General"

    @Column(nullable = false)
    private int capacidad;

    // ===== Constructores =====
    public Mesa() {
    }

    public Mesa(String ubicacion, int capacidad) {
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    // ===== Getters y Setters =====
    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "idMesa=" + idMesa +
                ", ubicacion='" + ubicacion + '\'' +
                ", capacidad=" + capacidad +
                '}';
    }
}

