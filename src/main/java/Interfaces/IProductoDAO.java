
package Interfaces;

import Entidades.Producto;
import java.util.List;

public interface IProductoDAO {

    boolean agregarProducto(Producto p);

    boolean actualizarProducto(Producto p);

    boolean eliminarProducto(int idProducto);

    Producto obtenerProducto(int idProducto);

    List<Producto> listarProductos();
}
