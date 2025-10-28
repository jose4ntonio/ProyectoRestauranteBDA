
package GUIs;

import Negocio.ProductoService;
import Entidades.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmProductos extends JFrame {

    // ===== Service =====
    private final ProductoService productoService = new ProductoService();

    // ===== Componentes =====
    private JLabel lblTitulo, lblNombre, lblPrecio, lblCategoria;
    private JTextField txtNombre, txtPrecio, txtBuscar;
    private JComboBox<String> cbxCategoria;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnVolver, btnBuscar;
    private JTable tablaProductos;

    public FrmProductos() {
        initComponents();
        setTitle("Gestión de Productos - Restaurante ITSON");
        setLocationRelativeTo(null);

        hookEventos();   // listeners
        cargarTabla();   // datos iniciales
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TÍTULO =====
        lblTitulo = new JLabel("GESTIÓN DE PRODUCTOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== PANEL CENTRAL =====
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // ----- FORMULARIO -----
        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        lblNombre = new JLabel("Nombre del producto:");
        txtNombre = new JTextField();

        lblCategoria = new JLabel("Categoría:");
        cbxCategoria = new JComboBox<>(new String[]{"Comida", "Bebida", "Postre", "Otro"});

        lblPrecio = new JLabel("Precio ($):");
        txtPrecio = new JTextField();

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblCategoria);
        panelFormulario.add(cbxCategoria);
        panelFormulario.add(lblPrecio);
        panelFormulario.add(txtPrecio);

        // relleno para cuadrar GridLayout
        panelFormulario.add(new JLabel());
        panelFormulario.add(new JLabel());

        panelCentro.add(panelFormulario);

        // ----- BOTONES CRUD -----
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBotones);

        // ----- BUSCADOR -----
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar Productos"));
        txtBuscar = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        panelBuscar.add(new JLabel("Nombre o Categoría:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBuscar);

        // ----- TABLA DE PRODUCTOS -----
        tablaProductos = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Categoría", "Precio"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });
        tablaProductos.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos Registrados"));

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(scrollTabla);

        // ----- BOTÓN VOLVER -----
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        panelVolver.add(btnVolver);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelVolver);

        panel.add(panelCentro, BorderLayout.CENTER);

        // Configuración general
        add(panel);
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* ===================== Eventos ===================== */

    private void hookEventos() {
        btnRegistrar.addActionListener(e -> onRegistrar());
        btnActualizar.addActionListener(e -> onActualizar());
        btnEliminar.addActionListener(e -> onEliminar());
        btnLimpiar.addActionListener(e -> limpiarForm());
        btnBuscar.addActionListener(e -> onBuscar());

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) pasarFilaAFormulario();
        });

        btnVolver.addActionListener(e -> dispose());
    }

    /* ===================== Acciones ===================== */

    private void onRegistrar() {
        try {
            Producto p = leerFormulario();
            productoService.crearProducto(p);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Producto registrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        try {
            int row = tablaProductos.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un producto en la tabla.");

            int id = (int) tablaProductos.getValueAt(row, 0);
            Producto p = productoService.obtenerProducto(id);
            if (p == null) throw new IllegalStateException("El producto ya no existe.");

            // Sobrescribe con lo del formulario
            p.setNombre(txtNombre.getText().trim());
            p.setTipo((String) cbxCategoria.getSelectedItem());
            p.setPrecio(parsePrecio(txtPrecio.getText().trim()));

            productoService.actualizarProducto(p);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Producto actualizado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        try {
            int row = tablaProductos.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un producto en la tabla.");

            int id = (int) tablaProductos.getValueAt(row, 0);
            int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar producto " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            productoService.eliminarProducto(id);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Producto eliminado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuscar() {
        String q = txtBuscar.getText().trim().toLowerCase();
        List<Producto> productos = productoService.listarProductos();

        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        m.setRowCount(0);

        for (Producto p : productos) {
            boolean match = p.getNombre().toLowerCase().contains(q)
                    || (p.getTipo() != null && p.getTipo().toLowerCase().contains(q));
            if (q.isEmpty() || match) {
                m.addRow(new Object[]{
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getTipo(),
                        p.getPrecio()
                });
            }
        }
    }

    /* ===================== Utilidades GUI ===================== */

    private void cargarTabla() {
        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        m.setRowCount(0);
        for (Producto p : productoService.listarProductos()) {
            m.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getTipo(),
                    p.getPrecio()
            });
        }
    }

    private void pasarFilaAFormulario() {
        int row = tablaProductos.getSelectedRow();
        if (row < 0) return;
        txtNombre.setText(String.valueOf(tablaProductos.getValueAt(row, 1)));
        cbxCategoria.setSelectedItem(String.valueOf(tablaProductos.getValueAt(row, 2)));
        txtPrecio.setText(String.valueOf(tablaProductos.getValueAt(row, 3)));
    }

    private void limpiarForm() {
        tablaProductos.clearSelection();
        txtNombre.setText("");
        cbxCategoria.setSelectedIndex(0);
        txtPrecio.setText("");
        txtBuscar.setText("");
        txtNombre.requestFocus();
    }

    /* ===================== Helpers de dominio ===================== */

    private Producto leerFormulario() {
        String nombre = txtNombre.getText().trim();
        String tipo = (String) cbxCategoria.getSelectedItem();
        double precio = parsePrecio(txtPrecio.getText().trim());

        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setTipo(tipo);
        p.setPrecio(precio);
        return p;
    }

    private double parsePrecio(String s) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException("El precio es obligatorio.");
        try {
            double v = Double.parseDouble(s);
            if (v < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
            return v;
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Precio inválido (usa números, e.g., 120.50).");
        }
    }

    /* ===================== Main para pruebas aisladas ===================== */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmProductos().setVisible(true));
    }
}
