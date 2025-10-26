/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
