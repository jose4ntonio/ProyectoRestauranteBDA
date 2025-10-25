/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmIngredientes extends JFrame {

    // Componentes
    private JLabel lblTitulo, lblNombre, lblUnidad, lblCantidad;
    private JTextField txtNombre, txtCantidad, txtBuscar;
    private JComboBox<String> cbxUnidad;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnVolver;
    private JTable tablaIngredientes;

    public FrmIngredientes() {
        initComponents();
        setTitle("Gestión de Ingredientes - Restaurante ITSON");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TÍTULO =====
        lblTitulo = new JLabel("GESTIÓN DE INGREDIENTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== PANEL CENTRAL =====
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // ----- FORMULARIO -----
        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Ingrediente"));

        lblNombre = new JLabel("Nombre del ingrediente:");
        txtNombre = new JTextField();

        lblUnidad = new JLabel("Unidad de medida:");
        cbxUnidad = new JComboBox<>(new String[]{"Piezas", "Gramos", "Mililitros"});

        lblCantidad = new JLabel("Cantidad actual (stock):");
        txtCantidad = new JTextField();

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblUnidad);
        panelFormulario.add(cbxUnidad);
        panelFormulario.add(lblCantidad);
        panelFormulario.add(txtCantidad);

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
        panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar Ingredientes"));
        txtBuscar = new JTextField(25);
        JButton btnBuscar = new JButton("Buscar");
        panelBuscar.add(new JLabel("Nombre o Unidad:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBuscar);

        // ----- TABLA DE INGREDIENTES -----
        tablaIngredientes = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Unidad", "Cantidad Disponible"}
        ));
        tablaIngredientes.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaIngredientes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Ingredientes Registrados"));

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
        SwingUtilities.invokeLater(() -> new FrmIngredientes().setVisible(true));
    }
}
