package DAOs;

import Entidades.Producto;
import Persistencia.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProductoDAO {

    public boolean agregarProducto(Producto p) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean actualizarProducto(Producto p) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean eliminarProducto(int idProducto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Producto ref = em.find(Producto.class, idProducto);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
            return ref != null;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public Producto obtenerProducto(int idProducto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Producto.class, idProducto);
        } finally {
            em.close();
        }
    }

    public List<Producto> listarProductos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Producto> q = em.createQuery(
                    "SELECT p FROM Producto p ORDER BY p.idProducto DESC", Producto.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    
    public List<Producto> buscarPorNombreLike(String texto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Producto> q = em.createQuery(
                    "SELECT p FROM Producto p " +
                    "WHERE LOWER(p.nombre) LIKE LOWER(:t) " +
                    "ORDER BY p.nombre", Producto.class);
            q.setParameter("t", "%" + texto + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // Útil para validar duplicado exacto
    public Producto buscarPorNombreExacto(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Producto> q = em.createQuery(
                    "SELECT p FROM Producto p WHERE LOWER(p.nombre) = LOWER(:n)", Producto.class);
            q.setParameter("n", nombre);
            return q.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}
