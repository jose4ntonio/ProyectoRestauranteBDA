package Pruebas;

import Entidades.Cliente;
import Entidades.Comanda;
import Entidades.DetalleComanda;
import Entidades.Producto;
import Entidades.Ingrediente;
import Negocio.ClienteService;
import Negocio.ComandaService;
import Negocio.ProductoService;
import Negocio.IngredienteService;
import Persistencia.JPAUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Prueba integral del proyecto Restaurante con JPA.
 * Recorre CRUD de Producto, Cliente, Ingrediente, y flujo de Comandas con detalles.
 */
public class TestEndToEnd {

    private static final ProductoService productoService = new ProductoService();
    private static final ClienteService clienteService   = new ClienteService();
    private static final IngredienteService ingrService  = new IngredienteService();
    private static final ComandaService comandaService   = new ComandaService();

    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("=== PRUEBA INTEGRAL RESTAURANTE (JPA) ===");

        try {
            // 0) Preparación: limpiar datos de prueba previos (si tu Servicio/DAO no tiene deleteAll, borrar por ID existentes)
            //    Para no borrar tu base real, aquí solo haremos "best effort": si existen con mismos nombres, los reutilizamos.

            // 1) PRODUCTOS — crear / listar / obtener / actualizar
            System.out.println("\n-- PRODUCTOS --");
            Producto cafe = ensureProducto("Café Americano", 25.0, "BEBIDA");
            Producto torta = ensureProducto("Torta de Jamón", 55.0, "PLATILLO");
            Producto pastel = ensureProducto("Rebanada de Pastel", 42.0, "POSTRE");

            listarProductos();

            // actualizar un producto
            cafe.setPrecio(27.0);
            productoService.actualizarProducto(cafe);
            System.out.println("Actualizado: " + cafe);

            // 2) CLIENTES — crear / listar / actualizar
            System.out.println("\n-- CLIENTES --");
            Cliente ana = ensureCliente("Ana", "Lopez", "6441234567", "ana@example.com", "Col. Centro", "VIP");
            Cliente bob = ensureCliente("Bob", "Martinez", "6449876543", "bob@example.com", "Zona Norte", "GENERAL");
            listarClientes();

            // 3) INGREDIENTES — crear / listar / ajustar stock
            System.out.println("\n-- INGREDIENTES --");
            Ingrediente cafeMolido = ensureIngrediente("Café molido", "KG", 2.0);
            Ingrediente pan = ensureIngrediente("Pan telera", "PZA", 20);
            Ingrediente jamon = ensureIngrediente("Jamón pavo", "KG", 1.0);
            listarIngredientes();

            // entradas/salidas de inventario
            ingrService.ajustarStock(cafeMolido.getIdIngrediente(), +1.5); // entran 1.5 kg
            ingrService.ajustarStock(pan.getIdIngrediente(), -2);          // salen 2 piezas
            listarIngredientes();

            // 4) COMANDA — construir detalles y persistir
            System.out.println("\n-- COMANDA --");
            List<DetalleComanda> detalles = new ArrayList<>();

            // Detalle 1: 2 cafés
            DetalleComanda d1 = new DetalleComanda();
            d1.setProducto(cafe);                            // <== IMPORTANTE: tu entidad DetalleComanda debe tener setProducto(Producto)
            d1.setCantidad(2);
            d1.setPrecioUnitario(cafe.getPrecio());          // congela precio del momento
            d1.setTotalProducto(d1.getCantidad() * d1.getPrecioUnitario());
            d1.setComentarios("Sin azúcar");
            detalles.add(d1);

            // Detalle 2: 1 torta
            DetalleComanda d2 = new DetalleComanda();
            d2.setProducto(torta);
            d2.setCantidad(1);
            d2.setPrecioUnitario(torta.getPrecio());
            d2.setTotalProducto(d2.getCantidad() * d2.getPrecioUnitario());
            d2.setComentarios("Con jalapeño");
            detalles.add(d2);

            // Prepara la comanda (folio, fecha, total y estado) y valida
            String mesa = "M-07";
            String clienteFrecuente = ana.getNombre() + " " + ana.getApellido();
            Comanda com = comandaService.prepararComanda(mesa, clienteFrecuente, detalles, "ABIERTA");

            // Persistir (tu ComandaDAO, llamado por el service, debe guardar comanda y detalles en una sola transacción)
            boolean ok = comandaService.crearComandaConDetalles(com, detalles);
            System.out.println("Comanda creada? " + ok + " | folio=" + com.getFolio() + " total=$" + com.getTotal());

            // 5) LISTAR COMANDAS DEL DÍA
            List<Comanda> comandasHoy = comandaService.listarComandas();
            double totalHoy = 0.0;
            System.out.println("\nComandas registradas:");
            for (Comanda c : comandasHoy) {
                System.out.printf("- %s  %s  mesa=%s  cliente=%s  estado=%s  total=$%.2f%n",
                        DF.format(c.getFecha()), c.getFolio(), nullSafe(c.getMesa()),
                        nullSafe(c.getClienteFrecuente()), c.getEstado(), c.getTotal());
                totalHoy += c.getTotal();
            }
            System.out.printf("Total del día: $%.2f%n", totalHoy);

            // 6) OPERACIONES EXTRA — actualizar estado de la comanda a ENTREGADA
            com.setEstado("ENTREGADA");
            comandaService.actualizarComanda(com);
            System.out.println("Comanda marcada como ENTREGADA: " + com.getFolio());

            // 7) ELIMINACIONES DE PRUEBA (opcional)
            // productoService.eliminarProducto(pastel.getIdProducto());
            // clienteService.eliminarCliente(bob.getIdCliente());
            // ingrService.eliminarIngrediente(pan.getIdIngrediente());
            // System.out.println("Eliminaciones de prueba realizadas.");

            System.out.println("\n=== PRUEBA COMPLETA OK ===");
        } catch (Exception ex) {
            System.err.println("FALLO EN PRUEBA: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Cierre ordenado de la EMF
            JPAUtil.shutdown();
        }
    }

    /* ======================= Helpers dominio ======================= */

    private static Producto ensureProducto(String nombre, double precio, String tipo) {
        for (Producto p : safe(productoService.listarProductos())) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                // actualizar precio si cambió
                if (Double.compare(p.getPrecio(), precio) != 0 || !p.getTipo().equalsIgnoreCase(tipo)) {
                    p.setPrecio(precio);
                    p.setTipo(tipo);
                    productoService.actualizarProducto(p);
                }
                System.out.println("Reusado producto: " + p);
                return p;
            }
        }
        Producto nuevo = new Producto(nombre, precio, tipo);
        productoService.crearProducto(nuevo);
        // recargar para tener ID
        for (Producto p : productoService.listarProductos()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        throw new IllegalStateException("No pude asegurar producto: " + nombre);
    }
    
    /* ======================= Métodos de listado para consola ======================= */

private static void listarProductos() {
    System.out.println("Listado de productos:");
    productoService.listarProductos().forEach(p ->
        System.out.printf(" - [#%d] %s | $%.2f | tipo=%s%n",
                p.getIdProducto(), p.getNombre(), p.getPrecio(), p.getTipo())
    );
}

private static void listarClientes() {
    System.out.println("Listado de clientes:");
    clienteService.listarClientes().forEach(c ->
        System.out.printf(" - [#%d] %s %s | %s | tipo=%s%n",
                c.getIdCliente(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getTipo())
    );
}

private static void listarIngredientes() {
    System.out.println("Listado de ingredientes:");
    ingrService.listarIngredientes().forEach(i ->
        System.out.printf(" - [#%d] %s | %.2f %s%n",
                i.getIdIngrediente(), i.getNombre(), i.getStock(), i.getUnidad())
    );
}


    private static Cliente ensureCliente(String nom, String ape, String tel, String mail, String dir, String tipo) {
        for (Cliente c : safe(clienteService.listarClientes())) {
            if (mail != null && !mail.isBlank() && mail.equalsIgnoreCase(c.getCorreo())) {
                // actualizar datos básicos si cambiaron
                boolean cambiado = false;
                if (!Objects.equals(c.getNombre(), nom)) { c.setNombre(nom); cambiado = true; }
                if (!Objects.equals(c.getApellido(), ape)) { c.setApellido(ape); cambiado = true; }
                if (!Objects.equals(c.getTelefono(), tel)) { c.setTelefono(tel); cambiado = true; }
                if (!Objects.equals(c.getDireccion(), dir)) { c.setDireccion(dir); cambiado = true; }
                if (!Objects.equals(c.getTipo(), tipo)) { c.setTipo(tipo); cambiado = true; }
                if (cambiado) clienteService.actualizarCliente(c);
                System.out.println("Reusado cliente: " + c);
                return c;
            }
        }
        Cliente nuevo = new Cliente(nom, ape, tel, mail, dir, tipo);
        clienteService.crearCliente(nuevo);
        for (Cliente c : clienteService.listarClientes()) {
            if (mail != null && mail.equalsIgnoreCase(c.getCorreo())) return c;
        }
        // Si no tiene correo único, intenta por nombre+tel
        for (Cliente c : clienteService.listarClientes()) {
            if (c.getNombre().equalsIgnoreCase(nom) && c.getTelefono().equalsIgnoreCase(tel)) return c;
        }
        throw new IllegalStateException("No pude asegurar cliente: " + nom);
    }

    private static Ingrediente ensureIngrediente(String nombre, String unidad, double stock) {
        for (Ingrediente i : safe(ingrService.listarIngredientes())) {
            if (i.getNombre().equalsIgnoreCase(nombre) && i.getUnidad().equalsIgnoreCase(unidad)) {
                // normaliza stock mínimo 0
                if (i.getStock() < 0) { i.setStock(0); ingrService.actualizarIngrediente(i); }
                System.out.println("Reusado ingrediente: " + i.getNombre() + " (" + i.getUnidad() + "), stock=" + i.getStock());
                return i;
            }
        }
        Ingrediente nuevo = new Ingrediente(nombre, unidad, stock);
        ingrService.crearIngrediente(nuevo);
        for (Ingrediente i : ingrService.listarIngredientes()) {
            if (i.getNombre().equalsIgnoreCase(nombre) && i.getUnidad().equalsIgnoreCase(unidad)) return i;
        }
        throw new IllegalStateException("No pude asegurar ingrediente: " + nombre);
    }

    /* ======================= Helpers utilitarios ======================= */

    private static <T> List<T> safe(List<T> l) {
        return l != null ? l : Collections.emptyList();
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }
}
