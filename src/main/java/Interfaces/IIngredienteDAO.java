
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
