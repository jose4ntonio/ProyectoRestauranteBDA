/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmComandas extends JFrame {

    // Declaración de componentes
    private JLabel lblTitulo;
    private JLabel lblFolio, lblFecha, lblEstado, lblMesa, lblCliente, lblTotal;
    private JTextField txtFolio, txtFecha, txtTotal, txtBuscarCliente, txtBuscarProducto;
    private JComboBox<String> cbxEstado, cbxMesa;
    private JButton btnBuscarCliente, btnAsociarCliente;
    private JButton btnBuscarProducto, btnAgregar, btnModificar, btnEliminar;
    private JButton btnGuardar, btnEntregar, btnCancelar, btnVolver;
    private JTable tablaProductos;

    public FrmComandas() {
        initComponents();
        setTitle("Gestión de Comandas - Restaurante ITSON");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TÍTULO SUPERIOR =====
        lblTitulo = new JLabel("SISTEMA DE COMANDAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== PANEL CENTRAL =====
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // ----- DATOS DE LA COMANDA -----
        JPanel panelDatos = new JPanel(new GridLayout(3, 4, 10, 10));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos de la Comanda"));

        lblFolio = new JLabel("Folio:");
        txtFolio = new JTextField();
        txtFolio.setEditable(false);

        lblFecha = new JLabel("Fecha:");
        txtFecha = new JTextField();
        txtFecha.setEditable(false);

        lblEstado = new JLabel("Estado:");
        cbxEstado = new JComboBox<>(new String[]{"Abierta", "Entregada", "Cancelada"});

        lblMesa = new JLabel("Mesa:");
        cbxMesa = new JComboBox<>();

        lblCliente = new JLabel("Cliente frecuente:");
        txtBuscarCliente = new JTextField();
        btnBuscarCliente = new JButton("Buscar");
        btnAsociarCliente = new JButton("Asociar");

        // Añadir componentes al panel de datos
        panelDatos.add(lblFolio);
        panelDatos.add(txtFolio);
        panelDatos.add(lblFecha);
        panelDatos.add(txtFecha);

        panelDatos.add(lblEstado);
        panelDatos.add(cbxEstado);
        panelDatos.add(lblMesa);
        panelDatos.add(cbxMesa);

        panelDatos.add(lblCliente);
        panelDatos.add(txtBuscarCliente);
        panelDatos.add(btnBuscarCliente);
        panelDatos.add(btnAsociarCliente);

        panelCentro.add(panelDatos);

        // ----- PRODUCTOS EN LA COMANDA -----
        JPanel panelProductos = new JPanel(new BorderLayout(5, 5));
        panelProductos.setBorder(BorderFactory.createTitledBorder("Productos en la Comanda"));

        JPanel panelBuscarProducto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscarProducto.add(new JLabel("Buscar producto:"));
        txtBuscarProducto = new JTextField(25);
        btnBuscarProducto = new JButton("Buscar");
        panelBuscarProducto.add(txtBuscarProducto);
        panelBuscarProducto.add(btnBuscarProducto);

        panelProductos.add(panelBuscarProducto, BorderLayout.NORTH);

        // Tabla vacía
        tablaProductos = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"#", "Producto", "Cantidad", "Precio", "Total", "Comentarios"}
        ));
        panelProductos.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Botones CRUD
        JPanel panelBotonesCRUD = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar producto");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        panelBotonesCRUD.add(btnAgregar);
        panelBotonesCRUD.add(btnModificar);
        panelBotonesCRUD.add(btnEliminar);

        panelProductos.add(panelBotonesCRUD, BorderLayout.SOUTH);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelProductos);

        // ----- TOTAL Y BOTONES PRINCIPALES -----
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        lblTotal = new JLabel("Total: $");
        txtTotal = new JTextField(10);
        txtTotal.setEditable(false);
        txtTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnGuardar = new JButton("Guardar");
        btnEntregar = new JButton("Marcar como Entregada");
        btnCancelar = new JButton("Cancelar");
        btnVolver = new JButton("Volver");

        panelTotal.add(lblTotal);
        panelTotal.add(txtTotal);
        panelTotal.add(btnGuardar);
        panelTotal.add(btnEntregar);
        panelTotal.add(btnCancelar);
        panelTotal.add(btnVolver);

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelTotal);

        panel.add(panelCentro, BorderLayout.CENTER);

        // Configuración general del frame
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmComandas().setVisible(true));
    }
}
