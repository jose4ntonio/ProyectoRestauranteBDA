/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmProductos extends JFrame {

    // Componentes
    private JLabel lblTitulo, lblNombre, lblPrecio, lblCategoria, lblDescripcion;
    private JTextField txtNombre, txtPrecio, txtBuscar;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbxCategoria;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnVolver;
    private JTable tablaProductos;

    public FrmProductos() {
        initComponents();
        setTitle("Gestión de Productos - Restaurante ITSON");
        setLocationRelativeTo(null);
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
        JPanel panelFormulario = new JPanel(new GridLayout(3, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        lblNombre = new JLabel("Nombre del producto:");
        txtNombre = new JTextField();

        lblCategoria = new JLabel("Categoría:");
        cbxCategoria = new JComboBox<>(new String[]{"Comida", "Bebida", "Postre", "Otro"});

        lblPrecio = new JLabel("Precio ($):");
        txtPrecio = new JTextField();

        lblDescripcion = new JLabel("Descripción:");
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblCategoria);
        panelFormulario.add(cbxCategoria);
        panelFormulario.add(lblPrecio);
        panelFormulario.add(txtPrecio);
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(scrollDesc);

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
        JButton btnBuscar = new JButton("Buscar");
        panelBuscar.add(new JLabel("Nombre o Categoría:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBuscar);

        // ----- TABLA DE PRODUCTOS -----
        tablaProductos = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Categoría", "Precio", "Descripción"}
        ));
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
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmProductos().setVisible(true));
    }
}
