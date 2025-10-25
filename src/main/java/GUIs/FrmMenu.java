/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUIs;

import javax.swing.*;
import java.awt.*;

public class FrmMenu extends JFrame {

    private JButton btnComandas, btnProductos, btnIngredientes, btnClientes, btnReportes, btnSalir;
    private JLabel lblTitulo;

    public FrmMenu() {
        initComponents();
        setTitle("Menú Principal - Restaurante ITSON");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TÍTULO =====
        lblTitulo = new JLabel("SISTEMA DE RESTAURANTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== PANEL DE BOTONES =====
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setLayout(new GridLayout(3, 2, 20, 20)); // 3 filas x 2 columnas, espacio entre botones

        // Botones de navegación
        btnComandas = new JButton("Comandas");
        btnProductos = new JButton("Productos");
        btnIngredientes = new JButton("Ingredientes");
        btnClientes = new JButton("Clientes");
        btnReportes = new JButton("Reportes");
        btnSalir = new JButton("Salir");

        // Estilo de los botones
        JButton[] botones = {btnComandas, btnProductos, btnIngredientes, btnClientes, btnReportes, btnSalir};
        for (JButton b : botones) {
            b.setFont(new Font("Segoe UI", Font.BOLD, 18));
            b.setBackground(new Color(90, 0, 0));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(new Color(60, 0, 0), 2));
        }

        // Agregar botones al panel
        panelBotones.add(btnComandas);
        panelBotones.add(btnProductos);
        panelBotones.add(btnIngredientes);
        panelBotones.add(btnClientes);
        panelBotones.add(btnReportes);
        panelBotones.add(btnSalir);

        panel.add(panelBotones, BorderLayout.CENTER);

        // ===== ACCIONES DE BOTONES =====
        btnComandas.addActionListener(e -> {
            FrmComandas frm = new FrmComandas();
            frm.setVisible(true);
            this.dispose();
        });

        btnProductos.addActionListener(e -> {
            FrmProductos frm = new FrmProductos();
            frm.setVisible(true);
            this.dispose();
        });

        btnIngredientes.addActionListener(e -> {
            FrmIngredientes frm = new FrmIngredientes();
            frm.setVisible(true);
            this.dispose();
        });

        btnClientes.addActionListener(e -> {
            FrmClientes frm = new FrmClientes();
            frm.setVisible(true);
            this.dispose();
        });

        btnReportes.addActionListener(e -> {
            Reportes frm = new Reportes();
            frm.setVisible(true);
            this.dispose();
        });

        btnSalir.addActionListener(e -> System.exit(0));

        // Configuración general del frame
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmMenu().setVisible(true));
    }
}
