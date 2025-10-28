
package GUIs;

import Negocio.ClienteService;
import Entidades.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmClientes extends JFrame {

    // ===== Service =====
    private final ClienteService clienteService = new ClienteService();

    // ===== Componentes =====
    private JLabel lblTitulo, lblNombre, lblApellido, lblTelefono, lblCorreo, lblDireccion, lblTipo;
    private JTextField txtNombre, txtApellido, txtTelefono, txtCorreo, txtBuscar;
    private JTextArea txtDireccion;
    private JComboBox<String> cbxTipo;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar, btnVolver;
    private JTable tablaClientes;

    public FrmClientes() {
        initComponents();
        setTitle("Gestión de Clientes Frecuentes - Restaurante ITSON");
        setLocationRelativeTo(null);

        hookEventos();
        cargarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== Título =====
        lblTitulo = new JLabel("GESTIÓN DE CLIENTES FRECUENTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(60, 80, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== Centro =====
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        // Formulario
        JPanel form = new JPanel(new GridLayout(4, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();

        lblApellido = new JLabel("Apellido:");
        txtApellido = new JTextField();

        lblTelefono = new JLabel("Teléfono:");
        txtTelefono = new JTextField();

        lblCorreo = new JLabel("Correo:");
        txtCorreo = new JTextField();

        lblDireccion = new JLabel("Dirección:");
        txtDireccion = new JTextArea(3, 20);
        txtDireccion.setLineWrap(true);
        txtDireccion.setWrapStyleWord(true);
        JScrollPane spDir = new JScrollPane(txtDireccion);

        lblTipo = new JLabel("Tipo:");
        cbxTipo = new JComboBox<>(new String[]{"GENERAL", "VIP", "EMPRESA", "OTRO"});

        // fila 1
        form.add(lblNombre);   form.add(txtNombre);
        form.add(lblApellido); form.add(txtApellido);
        // fila 2
        form.add(lblTelefono); form.add(txtTelefono);
        form.add(lblCorreo);   form.add(txtCorreo);
        // fila 3
        form.add(lblDireccion); form.add(spDir);
        form.add(lblTipo);      form.add(cbxTipo);
        // fila 4 (relleno)
        form.add(new JLabel()); form.add(new JLabel());
        form.add(new JLabel()); form.add(new JLabel());

        centro.add(form);

        // Botones CRUD
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar  = new JButton("Eliminar");
        btnLimpiar   = new JButton("Limpiar");
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        centro.add(Box.createVerticalStrut(8));
        centro.add(botones);

        // Buscar
        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscar.setBorder(BorderFactory.createTitledBorder("Buscar Clientes"));
        txtBuscar = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        buscar.add(new JLabel("Nombre / Apellido / Correo / Tipo:"));
        buscar.add(txtBuscar);
        buscar.add(btnBuscar);

        centro.add(Box.createVerticalStrut(8));
        centro.add(buscar);

        // Tabla
        tablaClientes = new JTable(new DefaultTableModel(
                new Object[][]{}, new String[]{"ID", "Nombre", "Apellido", "Teléfono", "Correo", "Dirección", "Tipo"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaClientes.setRowHeight(24);
        JScrollPane spTabla = new JScrollPane(tablaClientes);
        spTabla.setBorder(BorderFactory.createTitledBorder("Clientes Registrados"));

        centro.add(Box.createVerticalStrut(8));
        centro.add(spTabla);

        // Volver
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        pie.add(btnVolver);

        centro.add(Box.createVerticalStrut(8));
        centro.add(pie);

        panel.add(centro, BorderLayout.CENTER);

        add(panel);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* ===================== Eventos ===================== */

    private void hookEventos() {
        btnRegistrar.addActionListener(e -> onRegistrar());
        btnActualizar.addActionListener(e -> onActualizar());
        btnEliminar.addActionListener(e -> onEliminar());
        btnLimpiar.addActionListener(e -> limpiarForm());
        btnBuscar.addActionListener(e -> onBuscar());
        btnVolver.addActionListener(e -> dispose());

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) pasarFilaAFormulario();
        });
    }

    /* ===================== Acciones ===================== */

    private void onRegistrar() {
        try {
            Cliente c = leerFormularioAlta();
            clienteService.crearCliente(c);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Cliente registrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        try {
            int row = tablaClientes.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un cliente en la tabla.");
            int id = (int) tablaClientes.getValueAt(row, 0);

            Cliente c = clienteService.obtenerCliente(id);
            if (c == null) throw new IllegalStateException("El cliente ya no existe.");

            c.setNombre(txtNombre.getText().trim());
            c.setApellido(txtApellido.getText().trim());
            c.setTelefono(txtTelefono.getText().trim());
            c.setCorreo(txtCorreo.getText().trim());
            c.setDireccion(txtDireccion.getText().trim());
            c.setTipo((String) cbxTipo.getSelectedItem());

            clienteService.actualizarCliente(c);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Cliente actualizado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        try {
            int row = tablaClientes.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un cliente en la tabla.");
            int id = (int) tablaClientes.getValueAt(row, 0);

            int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar cliente " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            clienteService.eliminarCliente(id);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuscar() {
        String q = txtBuscar.getText().trim().toLowerCase();
        List<Cliente> lista = clienteService.listarClientes();

        DefaultTableModel m = (DefaultTableModel) tablaClientes.getModel();
        m.setRowCount(0);

        for (Cliente c : lista) {
            boolean match =
                    c.getNombre().toLowerCase().contains(q) ||
                    c.getApellido().toLowerCase().contains(q) ||
                    (c.getCorreo() != null && c.getCorreo().toLowerCase().contains(q)) ||
                    (c.getTipo() != null && c.getTipo().toLowerCase().contains(q));

            if (q.isEmpty() || match) {
                m.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre(),
                        c.getApellido(),
                        c.getTelefono(),
                        c.getCorreo() != null ? c.getCorreo() : "",
                        c.getDireccion() != null ? c.getDireccion() : "",
                        c.getTipo() != null ? c.getTipo() : "GENERAL"
                });
            }
        }
    }

    /* ===================== Utilidades GUI ===================== */

    private void cargarTabla() {
        DefaultTableModel m = (DefaultTableModel) tablaClientes.getModel();
        m.setRowCount(0);

        for (Cliente c : clienteService.listarClientes()) {
            m.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getTelefono(),
                    c.getCorreo() != null ? c.getCorreo() : "",
                    c.getDireccion() != null ? c.getDireccion() : "",
                    c.getTipo() != null ? c.getTipo() : "GENERAL"
            });
        }
    }

    private void pasarFilaAFormulario() {
        int row = tablaClientes.getSelectedRow();
        if (row < 0) return;

        txtNombre.setText(String.valueOf(tablaClientes.getValueAt(row, 1)));
        txtApellido.setText(String.valueOf(tablaClientes.getValueAt(row, 2)));
        txtTelefono.setText(String.valueOf(tablaClientes.getValueAt(row, 3)));

        Object correo = tablaClientes.getValueAt(row, 4);
        txtCorreo.setText(correo != null ? correo.toString() : "");

        Object direccion = tablaClientes.getValueAt(row, 5);
        txtDireccion.setText(direccion != null ? direccion.toString() : "");

        Object tipo = tablaClientes.getValueAt(row, 6);
        cbxTipo.setSelectedItem(tipo != null ? tipo.toString() : "GENERAL");
    }

    private void limpiarForm() {
        tablaClientes.clearSelection();
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        cbxTipo.setSelectedIndex(0);
        txtBuscar.setText("");
        txtNombre.requestFocus();
    }

    /* ===================== Helpers de dominio ===================== */

    private Cliente leerFormularioAlta() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cbxTipo.getSelectedItem();

        Cliente c = new Cliente(nombre, apellido, telefono, correo, direccion, tipo);
        // ClienteService.validarCliente aplicará validaciones (nombre, apellido, teléfono, email, tipo).
        return c;
    }

    /* ===================== Main para pruebas aisladas ===================== */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmClientes().setVisible(true));
    }
}
