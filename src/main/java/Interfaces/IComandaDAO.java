
package Interfaces;

import Entidades.Comanda;
import Entidades.DetalleComanda;
import java.util.List;

public interface IComandaDAO {

    boolean agregarComanda(Comanda c, List<DetalleComanda> detalles);

    List<Comanda> listarComandas();

    Comanda obtenerComanda(int idComanda);

    boolean actualizarComanda(Comanda c);

    boolean eliminarComanda(int idComanda);
}
