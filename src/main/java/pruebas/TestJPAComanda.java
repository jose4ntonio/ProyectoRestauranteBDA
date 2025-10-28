package Pruebas;

import Entidades.Comanda;
import Entidades.DetalleComanda;
import Persistencia.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.Date;

public class TestJPAComanda {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE GUARDADO CON JPA ===");

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // === Crear la comanda ===
            Comanda c = new Comanda();
            c.setFolio("OB-" + new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()));
            c.setFecha(new Date());
            c.setEstado("ABIERTA");
            c.setMesa("MESA-01");
            c.setClienteFrecuente("Juan Pérez");

            // === Crear los detalles ===
            DetalleComanda d1 = new DetalleComanda();
            d1.setIdProducto(1);
            d1.setCantidad(2);
            d1.setPrecioUnitario(25.0);
            d1.setTotalProducto(50.0);
            d1.setComentarios("Sin azúcar");

            DetalleComanda d2 = new DetalleComanda();
            d2.setIdProducto(2);
            d2.setCantidad(1);
            d2.setPrecioUnitario(120.0);
            d2.setTotalProducto(120.0);
            d2.setComentarios("Término 3/4");

            // === Enlazar los detalles a la comanda ===
            c.addDetalle(d1);
            c.addDetalle(d2);

            // Calcular total
            double total = d1.getTotalProducto() + d2.getTotalProducto();
            c.setTotal(total);

            // === Persistir (por cascade, guarda detalles también) ===
            em.persist(c);

            em.getTransaction().commit();

            System.out.println("✅ Comanda guardada con éxito!");
            System.out.println("ID Comanda: " + c.getIdComanda());
            System.out.println("Detalles: " + c.getDetalles().size());

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
            JPAUtil.shutdown();
        }
    }
}
