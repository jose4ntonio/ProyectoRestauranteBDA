
package Negocio;

import DAOs.ClienteDAO;
import Entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Lógica de negocio para clientes frecuentes.
 * - Valida nombre/apellido/telefono/correo/tipo.
 * - Evita duplicado de correo si aplica.
 */
public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private static final Pattern EMAIL_RX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public boolean crearCliente(Cliente c) {
        validarCliente(c, false);

        // Si hay correo, evitar duplicados amables
        if (c.getCorreo() != null && !c.getCorreo().isBlank()) {
            for (Cliente ex : listarClientes()) {
                if (ex.getCorreo() != null
                        && ex.getCorreo().equalsIgnoreCase(c.getCorreo())) {
                    throw new IllegalArgumentException("Ya existe un cliente con ese correo.");
                }
            }
        }
        return clienteDAO.agregarCliente(c);
    }

    public boolean actualizarCliente(Cliente c) {
        if (c == null || c.getIdCliente() <= 0)
            throw new IllegalArgumentException("ID de cliente inválido.");
        validarCliente(c, true);

        if (c.getCorreo() != null && !c.getCorreo().isBlank()) {
            for (Cliente ex : listarClientes()) {
                if (ex.getIdCliente() != c.getIdCliente()
                        && ex.getCorreo() != null
                        && ex.getCorreo().equalsIgnoreCase(c.getCorreo())) {
                    throw new IllegalArgumentException("Otro cliente ya usa ese correo.");
                }
            }
        }
        return clienteDAO.actualizarCliente(c);
    }

    public boolean eliminarCliente(int idCliente) {
        if (idCliente <= 0) throw new IllegalArgumentException("ID de cliente inválido.");
        return clienteDAO.eliminarCliente(idCliente);
    }

    public Cliente obtenerCliente(int idCliente) {
        if (idCliente <= 0) throw new IllegalArgumentException("ID de cliente inválido.");
        return clienteDAO.obtenerCliente(idCliente);
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = clienteDAO.listarClientes();
        return lista != null ? lista : new ArrayList<>();
    }

    /* ==================== Validaciones ==================== */

    private void validarCliente(Cliente c, boolean esUpdate) {
        if (c == null) throw new IllegalArgumentException("Cliente nulo.");
        if (c.getNombre() == null || c.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (c.getApellido() == null || c.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido es obligatorio.");
        if (c.getTelefono() == null || c.getTelefono().isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio.");

        if (c.getCorreo() != null && !c.getCorreo().isBlank()
                && !EMAIL_RX.matcher(c.getCorreo()).matches()) {
            throw new IllegalArgumentException("Formato de correo inválido.");
        }
        if (c.getTipo() == null || c.getTipo().isBlank())
            c.setTipo("GENERAL");
    }
}
