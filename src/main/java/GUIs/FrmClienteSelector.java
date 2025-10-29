package GUIs;

import Entidades.Cliente;
import Negocio.ClienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Selector modal de clientes frecuentes.
 * - Buscar por nombre, apellido, teléfono o correo (contiene).
 * - Doble clic o botón "Seleccionar" para elegir.
 * - Usa ClienteService.buscar(String) -> ClienteDAO.buscar(String).
 */
public class FrmClienteSelector extends JDialog {

    private final ClienteService clienteService = new ClienteService();

    private JTextField txtFiltro;
    private JButton btnBuscar, btnSeleccionar, btnCerrar, btnLimpiar;
    private JTable tblClientes;
    private DefaultTableModel modelo;

    private List<Cliente> resultados = new ArrayList<>();
    private Cliente seleccionado;

    public FrmClienteSelector(Frame owner) {
        super(owner, "Seleccionar cliente", true);
        initComponents();
        setLocationRelativeTo(owner);
    }

    public FrmClienteSelector(Dialog owner) {
        super(owner, "Seleccionar cliente", true);
        initComponents();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 420);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ====== ENCABEZADO / BUSCADOR ======
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        top.add(new JLabel("Buscar (nombre, apellido, teléfono o correo):"));
        txtFiltro = new JTextField(28);
        top.add(txtFiltro);
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");
        top.add(btnBuscar);
        top.add(btnLimpiar);
        root.add(top, BorderLayout.NORTH);

        // ====== TABLA ======
        modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Apellido", "Teléfono", "Correo", "Tipo"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblClientes = new JTable(modelo);
        tblClientes.setRowHeight(24);
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        root.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        // ====== BOTONERA ======
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnSeleccionar = new JButton("Seleccionar");
        btnCerrar = new JButton("Cerrar");
        bottom.add(btnSeleccionar);
        bottom.add(btnCerrar);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);

        // ==== Eventos ====
        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnLimpiar.addActionListener(e -> {
            txtFiltro.setText("");
            cargarTabla(new ArrayList<>());
        });
        btnCerrar.addActionListener(e -> dispose());

        btnSeleccionar.addActionListener(e -> {
            int idx = tblClientes.getSelectedRow();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla.");
                return;
            }
            seleccionado = resultados.get(idx);
            dispose();
        });

        // Doble clic para seleccionar
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tblClientes.getSelectedRow() >= 0) {
                    int idx = tblClientes.getSelectedRow();
                    seleccionado = resultados.get(idx);
                    dispose();
                }
            }
        });
    }

    private void realizarBusqueda() {
        String q = txtFiltro.getText().trim();
        try {
            resultados = clienteService.buscar(q);
            cargarTabla(resultados);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(List<Cliente> data) {
        modelo.setRowCount(0);
        for (Cliente c : data) {
            modelo.addRow(new Object[]{
                    c.getIdCliente(),
                    safe(c.getNombre()),
                    safe(c.getApellido()),
                    safe(c.getTelefono()),
                    safe(c.getCorreo()),
                    safe(c.getTipo())
            });
        }
    }

    private String safe(String s) { return s == null ? "" : s; }

    /** Devuelve el cliente elegido (o null si se cerró sin elegir). */
    public Cliente getSeleccionado() { return seleccionado; }

    /** Helper estático por si prefieres un one-liner. */
    public static Cliente seleccionar(Frame parent) {
        FrmClienteSelector dlg = new FrmClienteSelector(parent);
        dlg.setVisible(true);
        return dlg.getSeleccionado();
    }
}
