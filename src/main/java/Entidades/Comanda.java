package Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "comanda")
public class Comanda implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idComanda;

    @Column(nullable = false)
    private String folio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false)
    private double total;

    @Column(length = 100)
    private String mesa;

    @Column(length = 100)
    private String clienteFrecuente;

    /*  RELACIÓN con DetalleComanda (cascade = ALL)
        orphanRemoval: si quitas un detalle de la lista y haces merge, se elimina en BD. */
    @OneToMany(mappedBy = "comanda",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<DetalleComanda> detalles = new ArrayList<>();

    public Comanda() {}

    public void addDetalle(DetalleComanda d) {
        d.setComanda(this);
        this.detalles.add(d);
    }

    public void removeDetalle(DetalleComanda d) {
        d.setComanda(null);
        this.detalles.remove(d);
    }

    // ==== getters/setters ====
    public int getIdComanda() { return idComanda; }
    public void setIdComanda(int idComanda) { this.idComanda = idComanda; }
    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getMesa() { return mesa; }
    public void setMesa(String mesa) { this.mesa = mesa; }
    public String getClienteFrecuente() { return clienteFrecuente; }
    public void setClienteFrecuente(String clienteFrecuente) { this.clienteFrecuente = clienteFrecuente; }
    public List<DetalleComanda> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleComanda> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "Comanda{" +
                "idComanda=" + idComanda +
                ", folio='" + folio + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                ", mesa='" + mesa + '\'' +
                ", clienteFrecuente='" + clienteFrecuente + '\'' +
                ", detalles=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
