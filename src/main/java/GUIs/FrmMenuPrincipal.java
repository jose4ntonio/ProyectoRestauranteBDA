package GUIs;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

/**
 * Menú principal del sistema Restaurante ITSON.
 * Abre: Productos, Ingredientes, Clientes, Comandas y Reportes.
 */
public class FrmMenuPrincipal extends JFrame {

    private JButton btnProductos;
    private JButton btnIngredientes;
    private JButton btnClientes;
    private JButton btnComandas;
    private JButton btnReportes;
    private JButton btnSalir;

    public FrmMenuPrincipal() {
        initComponents();
        hookEventos();
        setTitle("Restaurante ITSON - Menú Principal");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);

        // ====== Barra de menú ======
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        miSalir.addActionListener(e -> salir());
        menuArchivo.add(miSalir);

        JMenu menuModulos = new JMenu("Módulos");
        JMenuItem miProductos = new JMenuItem("Productos");
        miProductos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        miProductos.addActionListener(e -> abrirProductos());

        JMenuItem miIngredientes = new JMenuItem("Ingredientes");
        miIngredientes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        miIngredientes.addActionListener(e -> abrirIngredientes());

        JMenuItem miClientes = new JMenuItem("Clientes");
        miClientes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        miClientes.addActionListener(e -> abrirClientes());

        JMenuItem miComandas = new JMenuItem("Comandas");
        miComandas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));
        miComandas.addActionListener(e -> abrirComandas());

        JMenuItem miReportes = new JMenuItem("Reportes");
        miReportes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        miReportes.addActionListener(e -> abrirReportes());

        menuModulos.add(miProductos);
        menuModulos.add(miIngredientes);
        menuModulos.add(miClientes);
        menuModulos.add(miComandas);
        menuModulos.add(miReportes);

        menuBar.add(menuArchivo);
        menuBar.add(menuModulos);
        setJMenuBar(menuBar);

        // ====== Encabezado ======
        JLabel lblTitulo = new JLabel("SISTEMA DE RESTAURANTE - ITSON", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(240, 244, 248));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ====== Botones grandes ======
        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        btnProductos = crearBoton("Productos");
        btnIngredientes = crearBoton("Ingredientes");
        btnClientes = crearBoton("Clientes");
        btnComandas = crearBoton("Comandas");
        btnReportes = crearBoton("Reportes");
        btnSalir = crearBoton("Salir");

        panelBotones.add(btnProductos);
        panelBotones.add(btnIngredientes);
        panelBotones.add(btnClientes);
        panelBotones.add(btnComandas);
        panelBotones.add(btnReportes);
        panelBotones.add(btnSalir);

        // ====== Pie ======
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel copy = new JLabel("© Restaurante ITSON");
        copy.setForeground(new Color(100, 100, 100));
        pie.add(copy);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        // ====== Layout raíz ======
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.add(lblTitulo, BorderLayout.NORTH);
        root.add(panelBotones, BorderLayout.CENTER);
        root.add(pie, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JButton crearBoton(String texto) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        return b;
    }

    private void hookEventos() {
        btnProductos.addActionListener(e -> abrirProductos());
        btnIngredientes.addActionListener(e -> abrirIngredientes());
        btnClientes.addActionListener(e -> abrirClientes());
        btnComandas.addActionListener(e -> abrirComandas());
        btnReportes.addActionListener(e -> abrirReportes());
        btnSalir.addActionListener(e -> salir());
    }

    /* ======= Acciones ======= */

    private void abrirProductos() {
        try {
            new FrmProductos().setVisible(true);
        } catch (Throwable t) {
            mostrarError("No se pudo abrir FrmProductos", t);
        }
    }

    private void abrirIngredientes() {
        try {
            new FrmIngredientes().setVisible(true);
        } catch (Throwable t) {
            mostrarError("No se pudo abrir FrmIngredientes", t);
        }
    }

    private void abrirClientes() {
        try {
            new FrmClientes().setVisible(true);
        } catch (Throwable t) {
            mostrarError("No se pudo abrir FrmClientes", t);
        }
    }

    private void abrirComandas() {
        try {
            new FrmComandas().setVisible(true);
        } catch (Throwable t) {
            mostrarError("No se pudo abrir FrmComandas", t);
        }
    }

    private void abrirReportes() {
        try {
            new FrmReportes().setVisible(true);
        } catch (Throwable t) {
            mostrarError("No se pudo abrir FrmReportes", t);
        }
    }

    private void salir() {
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Deseas salir del sistema?", "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) System.exit(0);
    }

    private void mostrarError(String msg, Throwable t) {
        JOptionPane.showMessageDialog(this, msg + "\n" + t.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        t.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmMenuPrincipal().setVisible(true));
    }
}
