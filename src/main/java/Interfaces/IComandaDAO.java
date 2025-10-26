/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
