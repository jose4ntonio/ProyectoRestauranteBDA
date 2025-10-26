/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import javax.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProducto;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @Column(length = 50)
    private String tipo; // Ejemplo: "Platillo", "Bebida", "Postre"

    // ===== Constructores =====
    public Producto() {
    }

    public Producto(String nombre, double precio, String tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
    }

    // ===== Getters y Setters =====
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
