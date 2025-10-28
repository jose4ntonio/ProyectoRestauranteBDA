
package Interfaces;

import Entidades.Cliente;
import java.util.List;

public interface IClienteDAO {

    boolean agregarCliente(Cliente c);

    boolean actualizarCliente(Cliente c);

    boolean eliminarCliente(int idCliente);

    Cliente obtenerCliente(int idCliente);

    List<Cliente> listarClientes();
}
