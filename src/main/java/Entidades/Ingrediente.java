/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import javax.persistence.*;

@Entity
@Table(name = "ingrediente")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idIngrediente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20, nullable = false)
    private String unidad; // Ejemplo: "piezas", "gramos", "ml"

    @Column(nullable = false)
    private double stock;

    // ===== Constructores =====
    public Ingrediente() {
    }

    public Ingrediente(String nombre, String unidad, double stock) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.stock = stock;
    }

    // ===== Getters y Setters =====
    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "idIngrediente=" + idIngrediente +
                ", nombre='" + nombre + '\'' +
                ", unidad='" + unidad + '\'' +
                ", stock=" + stock +
                '}';
    }
}
