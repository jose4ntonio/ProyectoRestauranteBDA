/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Reportes extends JFrame {

    private JLabel lblTitulo, lblDesde, lblHasta, lblTipoReporte;
    private JComboBox<String> cbxTipoReporte;
    private JTable tablaReportes;
    private JButton btnGenerar, btnExportar, btnVolver;
    private JSpinner spnDesde, spnHasta;

    public Reportes() {
        initComponents();
        setTitle("Reportes del Restaurante - Restaurante ITSON");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TÍTULO =====
        lblTitulo = new JLabel("GENERADOR DE REPORTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // ===== PANEL SUPERIOR (Filtros) =====
        JPanel panelFiltros = new JPanel(new GridLayout(2, 4, 10, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Parámetros del reporte"));
        panelFiltros.setBackground(Color.WHITE);

        lblDesde = new JLabel("Desde:");
        lblHasta = new JLabel("Hasta:");
        lblTipoReporte = new JLabel("Tipo de Reporte:");

        spnDesde = new JSpinner(new SpinnerDateModel());
        spnHasta = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorDesde = new JSpinner.DateEditor(spnDesde, "dd/MM/yyyy");
        JSpinner.DateEditor editorHasta = new JSpinner.DateEditor(spnHasta, "dd/MM/yyyy");
        spnDesde.setEditor(editorDesde);
        spnHasta.setEditor(editorHasta);

        cbxTipoReporte = new JComboBox<>(new String[]{
            "Ventas por día",
            "Productos más vendidos",
            "Ingresos por categoría",
            "Clientes frecuentes"
        });

        btnGenerar = new JButton("Generar");
        btnExportar = new JButton("Exportar");

        panelFiltros.add(lblDesde);
        panelFiltros.add(spnDesde);
        panelFiltros.add(lblHasta);
        panelFiltros.add(spnHasta);
        panelFiltros.add(lblTipoReporte);
        panelFiltros.add(cbxTipoReporte);
        panelFiltros.add(btnGenerar);
        panelFiltros.add(btnExportar);

        panel.add(panelFiltros, BorderLayout.NORTH);

        // ===== TABLA DE RESULTADOS =====
        tablaReportes = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Columna 1", "Columna 2", "Columna 3", "Columna 4"}
        ));
        tablaReportes.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaReportes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Resultados del reporte"));

        panel.add(scrollTabla, BorderLayout.CENTER);

        // ===== BOTÓN VOLVER =====
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        panelVolver.add(btnVolver);

        panel.add(panelVolver, BorderLayout.SOUTH);

        // ===== CONFIGURACIÓN GENERAL =====
        add(panel);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Reportes().setVisible(true));
    }
}
