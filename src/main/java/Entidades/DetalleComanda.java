
package Entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "detalle_comanda")
public class DetalleComanda implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetalleComanda;

    @Column(nullable = false)
    private int idComanda;

    @Column(nullable = false)
    private int idProducto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    @Column(nullable = false)
    private double totalProducto;

    @Column(length = 200)
    private String comentarios; // Ej. "Sin picante", "Término medio", etc.

    // ===== Constructores =====
    public DetalleComanda() {
    }

    public DetalleComanda(int idComanda, int idProducto, int cantidad, double precioUnitario, double totalProducto, String comentarios) {
        this.idComanda = idComanda;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.totalProducto = totalProducto;
        this.comentarios = comentarios;
    }

    // ===== Getters y Setters =====
    public int getIdDetalleComanda() {
        return idDetalleComanda;
    }

    public void setIdDetalleComanda(int idDetalleComanda) {
        this.idDetalleComanda = idDetalleComanda;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getTotalProducto() {
        return totalProducto;
    }

    public void setTotalProducto(double totalProducto) {
        this.totalProducto = totalProducto;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "DetalleComanda{" +
                "idDetalleComanda=" + idDetalleComanda +
                ", idComanda=" + idComanda +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", totalProducto=" + totalProducto +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
