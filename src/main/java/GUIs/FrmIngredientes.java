
package GUIs;

import Negocio.IngredienteService;
import Entidades.Ingrediente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmIngredientes extends JFrame {

    // ===== Service =====
    private final IngredienteService ingredienteService = new IngredienteService();

    // ===== Componentes =====
    private JLabel lblTitulo, lblNombre, lblUnidad, lblStock, lblDelta;
    private JTextField txtNombre, txtUnidad, txtStock, txtDelta, txtBuscar;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnAjustar, btnBuscar, btnVolver;
    private JTable tablaIngredientes;

    public FrmIngredientes() {
        initComponents();
        setTitle("Gestión de Ingredientes - Restaurante ITSON");
        setLocationRelativeTo(null);
        hookEventos();
        cargarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== Título =====
        lblTitulo = new JLabel("GESTIÓN DE INGREDIENTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 90, 90));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== Centro =====
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        // Formulario
        JPanel form = new JPanel(new GridLayout(2, 6, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Datos del Ingrediente"));

        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();

        lblUnidad = new JLabel("Unidad:");
        txtUnidad = new JTextField(); // ej. kg, g, l, ml, pza

        lblStock = new JLabel("Stock:");
        txtStock = new JTextField(); // numérico

        lblDelta = new JLabel("Delta (±):");
        txtDelta = new JTextField(); // para ajustar stock

        form.add(lblNombre); form.add(txtNombre);
        form.add(lblUnidad); form.add(txtUnidad);
        form.add(lblStock);  form.add(txtStock);
        form.add(lblDelta);  form.add(txtDelta);
        // relleno para cuadrar celdas
        form.add(new JLabel()); form.add(new JLabel());

        centro.add(form);

        // Botones CRUD + Ajuste
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar  = new JButton("Eliminar");
        btnLimpiar   = new JButton("Limpiar");
        btnAjustar   = new JButton("Ajustar Stock ");

        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnAjustar);
        botones.add(btnLimpiar);

        centro.add(Box.createVerticalStrut(8));
        centro.add(botones);

        // Buscar
        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscar.setBorder(BorderFactory.createTitledBorder("Buscar Ingredientes"));
        txtBuscar = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        buscar.add(new JLabel("Nombre o Unidad:"));
        buscar.add(txtBuscar);
        buscar.add(btnBuscar);

        centro.add(Box.createVerticalStrut(8));
        centro.add(buscar);

        // Tabla
        tablaIngredientes = new JTable(new DefaultTableModel(
                new Object[][]{}, new String[]{"ID", "Nombre", "Unidad", "Stock"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaIngredientes.setRowHeight(24);
        JScrollPane sp = new JScrollPane(tablaIngredientes);
        sp.setBorder(BorderFactory.createTitledBorder("Ingredientes Registrados"));

        centro.add(Box.createVerticalStrut(8));
        centro.add(sp);

        // Volver
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        pie.add(btnVolver);

        centro.add(Box.createVerticalStrut(8));
        centro.add(pie);

        panel.add(centro, BorderLayout.CENTER);

        add(panel);
        setSize(900, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* ===================== Eventos ===================== */

    private void hookEventos() {
        btnRegistrar.addActionListener(e -> onRegistrar());
        btnActualizar.addActionListener(e -> onActualizar());
        btnEliminar.addActionListener(e -> onEliminar());
        btnAjustar.addActionListener(e -> onAjustarStock());
        btnLimpiar.addActionListener(e -> limpiarForm());
        btnBuscar.addActionListener(e -> onBuscar());
        btnVolver.addActionListener(e -> dispose());

        tablaIngredientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) pasarFilaAFormulario();
        });
    }

    /* ===================== Acciones ===================== */

    private void onRegistrar() {
        try {
            Ingrediente i = leerFormularioAlta();
            ingredienteService.crearIngrediente(i);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Ingrediente registrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onActualizar() {
        try {
            int row = tablaIngredientes.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un ingrediente en la tabla.");
            int id = (int) tablaIngredientes.getValueAt(row, 0);

            Ingrediente i = ingredienteService.obtenerIngrediente(id);
            if (i == null) throw new IllegalStateException("El ingrediente ya no existe.");

            i.setNombre(txtNombre.getText().trim());
            i.setUnidad(txtUnidad.getText().trim());
            i.setStock(parseDouble(txtStock.getText().trim(), "Stock"));

            ingredienteService.actualizarIngrediente(i);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Ingrediente actualizado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        try {
            int row = tablaIngredientes.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un ingrediente en la tabla.");
            int id = (int) tablaIngredientes.getValueAt(row, 0);

            int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar ingrediente " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            ingredienteService.eliminarIngrediente(id);
            cargarTabla();
            limpiarForm();
            JOptionPane.showMessageDialog(this, "Ingrediente eliminado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAjustarStock() {
        try {
            int row = tablaIngredientes.getSelectedRow();
            if (row < 0) throw new IllegalStateException("Selecciona un ingrediente en la tabla.");
            int id = (int) tablaIngredientes.getValueAt(row, 0);

            double delta = parseDouble(txtDelta.getText().trim(), "Delta");
            boolean ok = ingredienteService.ajustarStock(id, delta);
            if (!ok) throw new RuntimeException("No se pudo ajustar el stock.");

            cargarTabla();
            txtDelta.setText("");
            JOptionPane.showMessageDialog(this, "Stock ajustado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuscar() {
        String q = txtBuscar.getText().trim().toLowerCase();
        List<Ingrediente> lista = ingredienteService.listarIngredientes();

        DefaultTableModel m = (DefaultTableModel) tablaIngredientes.getModel();
        m.setRowCount(0);
        for (Ingrediente i : lista) {
            boolean match = i.getNombre().toLowerCase().contains(q)
                    || (i.getUnidad() != null && i.getUnidad().toLowerCase().contains(q));
            if (q.isEmpty() || match) {
                m.addRow(new Object[]{ i.getIdIngrediente(), i.getNombre(), i.getUnidad(), i.getStock() });
            }
        }
    }

    /* ===================== Utilidades GUI ===================== */

    private void cargarTabla() {
        DefaultTableModel m = (DefaultTableModel) tablaIngredientes.getModel();
        m.setRowCount(0);
        for (Ingrediente i : ingredienteService.listarIngredientes()) {
            m.addRow(new Object[]{ i.getIdIngrediente(), i.getNombre(), i.getUnidad(), i.getStock() });
        }
    }

    private void pasarFilaAFormulario() {
        int row = tablaIngredientes.getSelectedRow();
        if (row < 0) return;
        txtNombre.setText(String.valueOf(tablaIngredientes.getValueAt(row, 1)));
        txtUnidad.setText(String.valueOf(tablaIngredientes.getValueAt(row, 2)));
        txtStock.setText(String.valueOf(tablaIngredientes.getValueAt(row, 3)));
    }

    private void limpiarForm() {
        tablaIngredientes.clearSelection();
        txtNombre.setText("");
        txtUnidad.setText("");
        txtStock.setText("");
        txtDelta.setText("");
        txtBuscar.setText("");
        txtNombre.requestFocus();
    }

    /* ===================== Helpers de dominio ===================== */

    private Ingrediente leerFormularioAlta() {
        String nombre = txtNombre.getText().trim();
        String unidad = txtUnidad.getText().trim();
        double stock = parseDouble(txtStock.getText().trim(), "Stock");

        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (unidad.isEmpty()) throw new IllegalArgumentException("La unidad es obligatoria.");
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");

        Ingrediente i = new Ingrediente();
        i.setNombre(nombre);
        i.setUnidad(unidad);
        i.setStock(stock);
        return i;
    }

    private double parseDouble(String s, String etiqueta) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(etiqueta + " es obligatorio.");
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(etiqueta + " inválido (usa números, e.g., 1.5).");
        }
    }

    /* ===================== Main para pruebas aisladas ===================== */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmIngredientes().setVisible(true));
    }
}
