/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmClientes extends JFrame {

    // Componentes
    private JLabel lblTitulo, lblNombre, lblTelefono, lblCorreo;
    private JTextField txtNombre, txtTelefono, txtCorreo, txtBuscar;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnVolver;
    private JTable tablaClientes;

    public FrmClientes() {
        initComponents();
        setTitle("Gestión de Clientes Frecuentes - Restaurante ITSON");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TÍTULO =====
        lblTitulo = new JLabel("GESTIÓN DE CLIENTES FRECUENTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== PANEL CENTRAL =====
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // ----- FORMULARIO DE CLIENTE -----
        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        lblNombre = new JLabel("Nombre completo:");
        txtNombre = new JTextField();

        lblTelefono = new JLabel("Teléfono:");
        txtTelefono = new JTextField();

        lblCorreo = new JLabel("Correo electrónico:");
        txtCorreo = new JTextField();

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblTelefono);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(lblCorreo);
        panelFormulario.add(txtCorreo);

        panelCentro.add(panelFormulario);

        // ----- BOTONES DE ACCIÓN -----
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
        panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar Clientes"));
        txtBuscar = new JTextField(25);
        JButton btnBuscar = new JButton("Buscar");
        panelBuscar.add(new JLabel("Nombre / Teléfono / Correo:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBuscar);

        // ----- TABLA DE CLIENTES -----
        tablaClientes = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Teléfono", "Correo", "Fecha Registro", "Visitas", "Total Gastado", "Puntos"}
        ));
        tablaClientes.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Clientes Registrados"));

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(scrollTabla);

        // ----- BOTÓN VOLVER -----
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        panelVolver.add(btnVolver);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelVolver);

        panel.add(panelCentro, BorderLayout.CENTER);

        // Configuración final del frame
        add(panel);
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmClientes().setVisible(true));
    }
    
    
    
}
