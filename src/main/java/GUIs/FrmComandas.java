
package GUIs;

import Negocio.ComandaService;
import Negocio.ProductoService;
import Entidades.Comanda;
import Entidades.DetalleComanda;
import Entidades.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GUI de Comandas:
 *  - Genera folio y fecha automáticamente
 *  - Permite agregar/modificar/eliminar productos (cantidad, precio, comentarios)
 *  - Recalcula total en cada cambio
 *  - Guarda comanda + detalles via ComandaService
 *  - Cambia estado a ENTREGADA / CANCELADA
 */
public class FrmComandas extends JFrame {

    // ===== Services =====
    private final ComandaService comandaService = new ComandaService();
    private final ProductoService productoService = new ProductoService();

    // ===== Componentes =====
    private JLabel lblTitulo;
    private JLabel lblFolio, lblFecha, lblEstado, lblMesa, lblCliente, lblTotal;
    private JTextField txtFolio, txtFecha, txtTotal, txtBuscarCliente, txtBuscarProducto;
    private JComboBox<String> cbxEstado, cbxMesa;
    private JButton btnBuscarCliente, btnAsociarCliente;
    private JButton btnBuscarProducto, btnAgregar, btnModificar, btnEliminar;
    private JButton btnGuardar, btnEntregar, btnCancelar, btnVolver;
    private JTable tablaProductos;

    // ===== Estado local =====
    private Integer idComandaCreada = null; // por si luego quieres actualizar por id

    public FrmComandas() {
        initComponents();
        setTitle("Gestión de Comandas - Restaurante ITSON");
        setLocationRelativeTo(null);

        hookEventos();
        prepararNuevaComanda(); // folio + fecha + estado inicial + limpiar tabla
        cargarMesas();          // carga inicial de mesas (ajusta según tu BD)
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // TÍTULO
        lblTitulo = new JLabel("SISTEMA DE COMANDAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(90, 0, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // ===== DATOS COMANDA =====
        JPanel panelDatos = new JPanel(new GridLayout(3, 4, 10, 10));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos de la Comanda"));

        lblFolio = new JLabel("Folio:");
        txtFolio = new JTextField(); txtFolio.setEditable(false);

        lblFecha = new JLabel("Fecha:");
        txtFecha = new JTextField(); txtFecha.setEditable(false);

        lblEstado = new JLabel("Estado:");
        cbxEstado = new JComboBox<>(new String[]{"ABIERTA", "ENTREGADA", "CANCELADA"});

        lblMesa = new JLabel("Mesa:");
        cbxMesa = new JComboBox<>(); // se llenará con cargarMesas()

        lblCliente = new JLabel("Cliente frecuente:");
        txtBuscarCliente = new JTextField();
        btnBuscarCliente = new JButton("Buscar");
        btnAsociarCliente = new JButton("Asociar");

        panelDatos.add(lblFolio);  panelDatos.add(txtFolio);
        panelDatos.add(lblFecha);  panelDatos.add(txtFecha);

        panelDatos.add(lblEstado); panelDatos.add(cbxEstado);
        panelDatos.add(lblMesa);   panelDatos.add(cbxMesa);

        panelDatos.add(lblCliente);        panelDatos.add(txtBuscarCliente);
        panelDatos.add(btnBuscarCliente);  panelDatos.add(btnAsociarCliente);

        panelCentro.add(panelDatos);

        // ===== PRODUCTOS =====
        JPanel panelProductos = new JPanel(new BorderLayout(5, 5));
        panelProductos.setBorder(BorderFactory.createTitledBorder("Productos en la Comanda"));

        JPanel panelBuscarProducto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscarProducto.add(new JLabel("Buscar producto:"));
        txtBuscarProducto = new JTextField(25);
        btnBuscarProducto = new JButton("Buscar");
        panelBuscarProducto.add(txtBuscarProducto);
        panelBuscarProducto.add(btnBuscarProducto);

        panelProductos.add(panelBuscarProducto, BorderLayout.NORTH);

        tablaProductos = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"#", "Producto", "Cantidad", "Precio", "Total", "Comentarios"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 2 -> Integer.class;
                    case 3, 4 -> Double.class;
                    default -> String.class;
                };
            }
        });
        tablaProductos.setRowHeight(24);
        panelProductos.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

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

        // ===== TOTAL + ACCIONES =====
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

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
    }

    /* ===================== Hook de eventos ===================== */

    private void hookEventos() {
        btnBuscarProducto.addActionListener(e -> onBuscarProducto());
        btnAgregar.addActionListener(e -> onAgregarProducto());
        btnModificar.addActionListener(e -> onModificarLinea());
        btnEliminar.addActionListener(e -> onEliminarLinea());

        btnGuardar.addActionListener(e -> onGuardarComanda());
        btnEntregar.addActionListener(e -> onMarcarEstado("ENTREGADA"));
        btnCancelar.addActionListener(e -> onMarcarEstado("CANCELADA"));
        btnVolver.addActionListener(e -> dispose());

        // Cliente (búsqueda/asociación ligera; aquí solo guardamos el texto)
        btnBuscarCliente.addActionListener(e -> onBuscarCliente());
        btnAsociarCliente.addActionListener(e -> onAsociarCliente());
        cbxEstado.addActionListener(e -> bloquearCapturaSegunEstado());
    }
    
    private void bloquearCapturaSegunEstado() {
        String estado = String.valueOf(cbxEstado.getSelectedItem());
        boolean editable = "ABIERTA".equalsIgnoreCase(estado);
        btnAgregar.setEnabled(editable);
        btnModificar.setEnabled(editable);
        btnEliminar.setEnabled(editable);
        btnGuardar.setEnabled(editable);
        txtBuscarProducto.setEnabled(editable);
    }

    /* ===================== Preparación ===================== */

    private void prepararNuevaComanda() {
        txtFolio.setText(comandaService.generarFolio());
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        cbxEstado.setSelectedItem("ABIERTA");
        txtBuscarCliente.setText("");
        limpiarTabla();
        recalcularTotal();
        idComandaCreada = null;
    }

    private void cargarMesas() {
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    for (int i = 1; i <= 20; i++) {
        model.addElement(String.format("MESA-%02d", i));
    }
    cbxMesa.setModel(model);
    cbxMesa.setSelectedIndex(0);
}

    /* ===================== Productos (líneas) ===================== */

    private void onBuscarProducto() {
        // Busca por texto o muestra lista para elegir
        String q = txtBuscarProducto.getText().trim().toLowerCase();
        List<Producto> productos = productoService.listarProductos();
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : productos) {
            if (q.isBlank()
                || p.getNombre().toLowerCase().contains(q)
                || (p.getTipo() != null && p.getTipo().toLowerCase().contains(q))) {
                filtrados.add(p);
            }
        }
        if (filtrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron productos para: " + q);
            return;
        }
        Producto elegido = seleccionarProducto(filtrados);
        if (elegido != null) {
            // Prefill un renglón con cantidad 1
            agregarLinea(elegido, 1, elegido.getPrecio(), "");
        }
    }

    private Producto seleccionarProducto(List<Producto> opciones) {
        String[] nombres = opciones.stream().map(Producto::getNombre).toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(
                this, "Selecciona un producto:",
                "Productos", JOptionPane.PLAIN_MESSAGE, null,
                nombres, nombres[0]);
        if (sel == null) return null;
        return opciones.stream()
                .filter(p -> p.getNombre().equals(sel))
                .findFirst().orElse(null);
    }

    private void onAgregarProducto() {
        // Permite agregar producto con diálogo (elige producto + cantidad + comentarios)
        List<Producto> productos = productoService.listarProductos();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en catálogo.");
            return;
        }
        Producto p = seleccionarProducto(productos);
        if (p == null) return;

        String sCant = JOptionPane.showInputDialog(this, "Cantidad:", "1");
        if (sCant == null) return;
        int cant;
        try {
            cant = Integer.parseInt(sCant.trim());
            if (cant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }

        String comentarios = JOptionPane.showInputDialog(this, "Comentarios (opcional):", "");
        if (comentarios == null) comentarios = "";

        agregarLinea(p, cant, p.getPrecio(), comentarios);
    }

    private void agregarLinea(Producto p, int cantidad, double precioUnit, String comentarios) {
        double total = redondear(precioUnit * cantidad);
        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        int num = m.getRowCount() + 1;
        m.addRow(new Object[]{ num, p.getNombre(), cantidad, precioUnit, total, comentarios });
        recalcularTotal();
    }

    private void onModificarLinea() {
        int row = tablaProductos.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona una línea para modificar."); return; }

        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        String prod = String.valueOf(m.getValueAt(row, 1));
        int cantidadActual = (int) m.getValueAt(row, 2);
        double precioActual = (double) m.getValueAt(row, 3);
        String comentariosActuales = String.valueOf(m.getValueAt(row, 5));

        String sCant = JOptionPane.showInputDialog(this, "Cantidad:", cantidadActual);
        if (sCant == null) return;
        int cant;
        try {
            cant = Integer.parseInt(sCant.trim());
            if (cant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }

        String sPrecio = JOptionPane.showInputDialog(this, "Precio unitario:", precioActual);
        if (sPrecio == null) return;
        double precio;
        try {
            precio = Double.parseDouble(sPrecio.trim());
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            return;
        }

        String comentarios = JOptionPane.showInputDialog(this, "Comentarios:", comentariosActuales);
        if (comentarios == null) comentarios = comentariosActuales;

        m.setValueAt(cant, row, 2);
        m.setValueAt(precio, row, 3);
        m.setValueAt(redondear(cant * precio), row, 4);
        m.setValueAt(comentarios, row, 5);

        recalcularTotal();
    }

    private void onEliminarLinea() {
        int row = tablaProductos.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona una línea para eliminar."); return; }

        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        m.removeRow(row);

        // Re-numerar la columna "#"
        for (int i = 0; i < m.getRowCount(); i++) {
            m.setValueAt(i + 1, i, 0);
        }
        recalcularTotal();
    }

    private void limpiarTabla() {
        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        m.setRowCount(0);
    }

    private void recalcularTotal() {
        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        double s = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            Object v = m.getValueAt(i, 4);
            if (v instanceof Number) s += ((Number) v).doubleValue();
        }
        txtTotal.setText(String.format("%.2f", redondear(s)));
    }

    private double redondear(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    /* ===================== Guardar / Estados ===================== */

    private void onGuardarComanda() {
        try {
            // Armar detalles desde la tabla
            List<DetalleComanda> detalles = construirDetallesDesdeTabla();
            if (detalles.isEmpty())
                throw new IllegalArgumentException("La comanda debe tener al menos un producto.");

            // Datos cabecera
            String mesa = (String) cbxMesa.getSelectedItem();
            if (mesa == null || mesa.isBlank())
                throw new IllegalArgumentException("Selecciona una mesa.");

            String cliente = txtBuscarCliente.getText().trim();
            String estado = (String) cbxEstado.getSelectedItem();

            // Preparar comanda (folio, fecha, total)
            Comanda c = comandaService.prepararComanda(
                    mesa,
                    cliente.isBlank() ? null : cliente,
                    detalles,
                    estado
            );

            // Persistir (cabecera + detalles) vía Service
            boolean ok = comandaService.crearComandaConDetalles(c, detalles);
            if (!ok) throw new RuntimeException("No se pudo guardar la comanda.");

            idComandaCreada = c.getIdComanda(); // si el DAO setea el ID (dependiendo de tu implementación)
            JOptionPane.showMessageDialog(this, "Comanda guardada con folio: " + c.getFolio());
            prepararNuevaComanda();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<DetalleComanda> construirDetallesDesdeTabla() {
        List<DetalleComanda> detalles = new ArrayList<>();
        DefaultTableModel m = (DefaultTableModel) tablaProductos.getModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            String nombreProd = String.valueOf(m.getValueAt(i, 1));
            int cantidad = (int) m.getValueAt(i, 2);
            double precio = (double) m.getValueAt(i, 3);
            double total = (double) m.getValueAt(i, 4);
            String comentarios = String.valueOf(m.getValueAt(i, 5));

            // Necesitamos el idProducto, lo resolvemos por nombre:
            Producto p = productoService.buscarPorNombre(nombreProd);
            if (p == null) {
                // fallback: intenta listar y encontrar por equalsIgnoreCase
                for (Producto pp : productoService.listarProductos()) {
                    if (pp.getNombre().equalsIgnoreCase(nombreProd)) { p = pp; break; }
                }
            }
            if (p == null) throw new IllegalStateException("Producto no encontrado: " + nombreProd);

            DetalleComanda d = new DetalleComanda();
            d.setIdProducto(p.getIdProducto());
            d.setCantidad(cantidad);
            d.setPrecioUnitario(precio);
            d.setTotalProducto(total);
            d.setComentarios(comentarios);
            detalles.add(d);
        }
        return detalles;
    }

    private void onMarcarEstado(String nuevoEstado) {
        try {
            // Si quieres operar sobre la comanda recién creada, deberías tener su ID.
            if (idComandaCreada == null) {
                // Permitir marcar estado "local" antes de guardar: solo actualiza combo
                cbxEstado.setSelectedItem(nuevoEstado);
                JOptionPane.showMessageDialog(this, "Estado marcado en la captura: " + nuevoEstado);
                return;
            }
            Comanda c = comandaService.obtenerComanda(idComandaCreada);
            if (c == null) throw new IllegalStateException("Comanda no encontrada.");
            c.setEstado(nuevoEstado);
            boolean ok = comandaService.actualizarComanda(c);
            if (!ok) throw new RuntimeException("No se pudo actualizar estado.");
            JOptionPane.showMessageDialog(this, "Comanda " + nuevoEstado + ".");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ===================== Cliente (ligero) ===================== */

    private void onBuscarCliente() {
        // Aquí podrías abrir un diálogo que liste clientes frecuentes (ClienteService)
        // Por ahora, simulamos que el texto ya es el filtro y lo dejamos en el campo.
        JOptionPane.showMessageDialog(this, "Búsqueda de cliente por texto.\nIntegra aquí tu FrmClientes o un selector.");
    }

    private void onAsociarCliente() {
        // De momento solo mantiene el texto como "clienteFrecuente" asociado
        JOptionPane.showMessageDialog(this, "Cliente asociado: " + txtBuscarCliente.getText().trim());
    }

    /* ===================== Main ===================== */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmComandas().setVisible(true));
    }
}
