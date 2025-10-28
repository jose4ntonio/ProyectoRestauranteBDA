
package Entidades;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCliente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column(length = 15, unique = true)
    private String telefono;

    @Column(length = 150, unique = true)
    private String correo;

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String tipo; // Ejemplo: "Frecuente", "Nuevo", "VIP"

    // ===== Constructores =====
    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String telefono, String correo, String direccion, String tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.tipo = tipo;
    }

    // ===== Getters y Setters =====
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "Cliente{"
                + "idCliente=" + idCliente
                + ", nombre='" + nombre + '\''
                + ", apellido='" + apellido + '\''
                + ", telefono='" + telefono + '\''
                + ", correo='" + correo + '\''
                + ", direccion='" + direccion + '\''
                + ", tipo='" + tipo + '\''
                + '}';
    }
}
 