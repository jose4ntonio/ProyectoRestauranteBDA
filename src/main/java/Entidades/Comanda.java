/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comanda")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // ===== Constructores =====
    public Comanda() {
    }

    public Comanda(String folio, Date fecha, String estado, double total, String mesa, String clienteFrecuente) {
        this.folio = folio;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.mesa = mesa;
        this.clienteFrecuente = clienteFrecuente;
    }

    // ===== Getters y Setters =====
    public int getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getClienteFrecuente() {
        return clienteFrecuente;
    }

    public void setClienteFrecuente(String clienteFrecuente) {
        this.clienteFrecuente = clienteFrecuente;
    }

    // ===== toString =====
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
                '}';
    }
}
