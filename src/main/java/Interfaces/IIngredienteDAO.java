/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Entidades.Ingrediente;
import java.util.List;

public interface IIngredienteDAO {

    boolean agregarIngrediente(Ingrediente i);

    boolean actualizarIngrediente(Ingrediente i);

    boolean eliminarIngrediente(int idIngrediente);

    Ingrediente obtenerIngrediente(int idIngrediente);

    List<Ingrediente> listarIngredientes();
}
