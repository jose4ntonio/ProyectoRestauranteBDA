
package GUIs;

import Negocio.ComandaService;
import Entidades.Comanda;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// iText 5
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class FrmReportes extends JFrame {

    private final ComandaService comandaService = new ComandaService();

    private JTabbedPane tabs;

    // Tab Comandas
    private JTextField txtDesde, txtHasta;
    private JButton btnFiltrarComandas, btnExportarComandas;
    private JTable tblComandas;
    private JLabel lblTotalVentas;

    // Tab Clientes
    private JTextField txtNombreCliente;
    private JSpinner spMinVisitas;
    private JButton btnFiltrarClientes, btnExportarClientes;
    private JTable tblClientes;

    private final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat DFDT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat HOUR = new SimpleDateFormat("HH:mm:ss");

    public FrmReportes() {
        initComponents();
        setTitle("Reportes - Restaurante ITSON");
        setLocationRelativeTo(null);
        hookEventos();

        String hoy = DF.format(new Date());
        txtDesde.setText(hoy);
        txtHasta.setText(hoy);

        cargarComandas();
        cargarClientesFrecuentes();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel titulo = new JLabel("MÓDULO DE REPORTES", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 70, 120));
        root.add(titulo, BorderLayout.NORTH);

        tabs = new JTabbedPane();

        // ===== TAB COMANDAS =====
        JPanel tabComandas = new JPanel(new BorderLayout(8, 8));

        JPanel filtros1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros1.setBorder(BorderFactory.createTitledBorder("Filtro por fecha (yyyy-MM-dd)"));
        filtros1.add(new JLabel("Desde:"));
        txtDesde = new JTextField(10);
        filtros1.add(txtDesde);
        filtros1.add(new JLabel("Hasta:"));
        txtHasta = new JTextField(10);
        filtros1.add(txtHasta);
        btnFiltrarComandas = new JButton("Buscar");
        filtros1.add(btnFiltrarComandas);
        btnExportarComandas = new JButton("Exportar PDF");
        filtros1.add(btnExportarComandas);
        tabComandas.add(filtros1, BorderLayout.NORTH);

        tblComandas = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Fecha", "Hora", "Mesa", "Estado", "Cliente", "Total"}
        ));
        tblComandas.setRowHeight(24);
        tabComandas.add(new JScrollPane(tblComandas), BorderLayout.CENTER);

        JPanel pie1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalVentas = new JLabel("Total ventas: $0.00");
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pie1.add(lblTotalVentas);
        tabComandas.add(pie1, BorderLayout.SOUTH);

        tabs.addTab("Reporte de Comandas", tabComandas);

        // ===== TAB CLIENTES =====
        JPanel tabClientes = new JPanel(new BorderLayout(8, 8));

        JPanel filtros2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros2.setBorder(BorderFactory.createTitledBorder("Filtros de clientes"));
        filtros2.add(new JLabel("Nombre (contiene):"));
        txtNombreCliente = new JTextField(18);
        filtros2.add(txtNombreCliente);
        filtros2.add(new JLabel("Mínimo de visitas:"));
        spMinVisitas = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
        filtros2.add(spMinVisitas);
        btnFiltrarClientes = new JButton("Buscar");
        filtros2.add(btnFiltrarClientes);
        btnExportarClientes = new JButton("Exportar PDF");
        filtros2.add(btnExportarClientes);

        tabClientes.add(filtros2, BorderLayout.NORTH);

        tblClientes = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Cliente", "Visitas", "Total gastado", "Puntos", "Última visita"}
        ));
        tblClientes.setRowHeight(24);
        tabClientes.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        tabs.addTab("Reporte de Clientes", tabClientes);
        root.add(tabs, BorderLayout.CENTER);
        
        // ===== BOTONES INFERIORES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnVolver = new JButton("Volver");
        
        panelBotones.add(btnVolver);
        root.add(panelBotones, BorderLayout.SOUTH);
        
        // Acción del botón Volver
        btnVolver.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            new FrmMenuPrincipal().setVisible(true); // Abre el menú principal
        });

        setContentPane(root);
    }

    private void hookEventos() {
        btnFiltrarComandas.addActionListener(e -> cargarComandas());
        btnExportarComandas.addActionListener(e -> exportarTablaPDF(tblComandas, "Reporte de Comandas"));
        btnFiltrarClientes.addActionListener(e -> cargarClientesFrecuentes());
        btnExportarClientes.addActionListener(e -> exportarTablaPDF(tblClientes, "Reporte de Clientes Frecuentes"));
    }

    // ==== Comandas ====
    private void cargarComandas() {
        Date desde = parseDate(txtDesde.getText().trim());
        Date hasta = parseDate(txtHasta.getText().trim());
        if (desde == null || hasta == null) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa yyyy-MM-dd");
            return;
        }
        Date hastaInc = finDeDia(hasta);
        List<Comanda> todas = comandaService.listarComandas();
        List<Comanda> filtradas = new ArrayList<>();
        for (Comanda c : todas) {
            Date f = c.getFecha();
            if (f != null && !f.before(desde) && !f.after(hastaInc)) filtradas.add(c);
        }

        DefaultTableModel m = (DefaultTableModel) tblComandas.getModel();
        m.setRowCount(0);
        double total = 0;
        for (Comanda c : filtradas) {
            String fecha = DF.format(c.getFecha());
            String hora = HOUR.format(c.getFecha());
            String mesa = c.getMesa() != null ? c.getMesa() : "";
            String cliente = c.getClienteFrecuente() != null ? c.getClienteFrecuente() : "";
            String estado = c.getEstado() != null ? c.getEstado() : "";
            total += c.getTotal();
            m.addRow(new Object[]{fecha, hora, mesa, estado, cliente, redondear(c.getTotal())});
        }
        lblTotalVentas.setText(String.format("Total ventas: $%.2f", total));
    }

    // ==== Clientes ====
    private void cargarClientesFrecuentes() {
        String filtroNombre = txtNombreCliente.getText().trim().toLowerCase(Locale.ROOT);
        int minVisitas = (Integer) spMinVisitas.getValue();
        List<Comanda> all = comandaService.listarComandas();

        Map<String, ClienteAgg> mapa = new HashMap<>();
        for (Comanda c : all) {
            String cli = c.getClienteFrecuente();
            if (cli == null || cli.isBlank()) continue;
            ClienteAgg agg = mapa.getOrDefault(cli, new ClienteAgg(cli));
            agg.visitas++;
            agg.totalGastado += c.getTotal();
            if (agg.ultima == null || c.getFecha().after(agg.ultima))
                agg.ultima = c.getFecha();
            mapa.put(cli, agg);
        }

        DefaultTableModel m = (DefaultTableModel) tblClientes.getModel();
        m.setRowCount(0);
        for (ClienteAgg a : mapa.values()) {
            boolean matchNom = filtroNombre.isBlank() || a.nombre.toLowerCase().contains(filtroNombre);
            if (matchNom && a.visitas >= minVisitas) {
                double puntos = a.totalGastado / 20.0;
                m.addRow(new Object[]{
                        a.nombre, a.visitas, redondear(a.totalGastado),
                        redondear(puntos), DFDT.format(a.ultima)
                });
            }
        }
    }

    // ==== Utilidades ====
    private Date parseDate(String s) {
        try { return DF.parse(s); } catch (ParseException e) { return null; }
    }

    private Date finDeDia(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    private double redondear(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // ==== Exportar a PDF ====
    private void exportarTablaPDF(JTable tabla, String titulo) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File(titulo.replace(" ", "_") + ".pdf"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        java.io.File f = fc.getSelectedFile();
        try {
            Document doc = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(f));
            doc.open();

            com.itextpdf.text.Font fTitulo = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fMeta = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);

            Paragraph head = new Paragraph(titulo + "\n", fTitulo);
            head.setAlignment(Element.ALIGN_CENTER);
            doc.add(head);

            String fechaGen = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            Paragraph meta = new Paragraph("Generado: " + fechaGen + "\n\n", fMeta);
            meta.setAlignment(Element.ALIGN_RIGHT);
            doc.add(meta);

            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
            pdfTable.setWidthPercentage(100);

            for (int c = 0; c < model.getColumnCount(); c++) {
                PdfPCell cell = new PdfPCell(new Phrase(model.getColumnName(c)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(230, 230, 230));
                pdfTable.addCell(cell);
            }

            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    Object val = model.getValueAt(r, c);
                    PdfPCell cell = new PdfPCell(new Phrase(val != null ? String.valueOf(val) : ""));
                    cell.setHorizontalAlignment(c == model.getColumnCount() - 1 ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                    pdfTable.addCell(cell);
                }
            }

            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    Phrase p = new Phrase("Página " + writer.getPageNumber(),
                            new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9,
                                    com.itextpdf.text.Font.ITALIC));
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT, p, document.right(), document.bottom() - 10, 0);
                }
            });

            doc.add(pdfTable);
            doc.close();
            JOptionPane.showMessageDialog(this, "PDF exportado: " + f.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error exportando PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==== Estructura auxiliar ====
    static class ClienteAgg {
        String nombre;
        int visitas;
        double totalGastado;
        Date ultima;
        ClienteAgg(String n) { nombre = n; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmReportes().setVisible(true));
    }
}
