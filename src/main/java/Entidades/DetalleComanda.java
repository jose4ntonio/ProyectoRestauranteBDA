package Entidades;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_comanda")
public class DetalleComanda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetalle;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idComanda")
    private Comanda comanda;

    @Column(name = "idProducto", nullable = false)
    private int idProducto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    @Column(nullable = false)
    private double totalProducto;

    @Column(length = 255)
    private String comentarios;

    // === Getters y Setters ===
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public Comanda getComanda() { return comanda; }
    public void setComanda(Comanda comanda) { this.comanda = comanda; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getTotalProducto() { return totalProducto; }
    public void setTotalProducto(double totalProducto) { this.totalProducto = totalProducto; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    @Override
    public String toString() {
        return "DetalleComanda{" +
                "idDetalle=" + idDetalle +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", totalProducto=" + totalProducto +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
