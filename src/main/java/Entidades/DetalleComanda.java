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

    // Muchas filas de detalle pertenecen a una comanda
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idComanda", nullable = false)
    private Comanda comanda;

    // Cada detalle apunta a un producto existente
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    @Column(nullable = false)
    private double totalProducto;

    @Column(length = 255)
    private String comentarios;

    public DetalleComanda() {}

    public DetalleComanda(Comanda comanda, Producto producto, int cantidad, double precioUnitario, String comentarios) {
        this.comanda = comanda;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.comentarios = comentarios;
    }

    /* ================== Ciclo de vida ================== */
    @PrePersist
    @PreUpdate
    private void calcularTotal() {
        if (cantidad < 0) cantidad = 0;
        if (precioUnitario < 0) precioUnitario = 0;
        this.totalProducto = Math.round((cantidad * precioUnitario) * 100.0) / 100.0;
    }

    /* ================== Getters/Setters ================== */
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public Comanda getComanda() { return comanda; }
    public void setComanda(Comanda comanda) { this.comanda = comanda; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

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
                ", producto=" + (producto != null ? producto.getNombre() : "null") +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", totalProducto=" + totalProducto +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
