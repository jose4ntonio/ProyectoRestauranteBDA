package DAOs;

import Entidades.Cliente;
import Persistencia.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClienteDAO {

    public boolean agregarCliente(Cliente c) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public boolean actualizarCliente(Cliente c) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(c);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public boolean eliminarCliente(int idCliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Cliente ref = em.find(Cliente.class, idCliente);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
            return ref != null;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public Cliente obtenerCliente(int idCliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cliente.class, idCliente);
        } finally { em.close(); }
    }

    public List<Cliente> listarClientes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cliente> q = em.createQuery(
                    "SELECT c FROM Cliente c ORDER BY c.idCliente DESC", Cliente.class);
            return q.getResultList();
        } finally { em.close(); }
    }

    // (Opcional) Búsqueda por nombre/apellido
    public List<Cliente> buscarPorNombreLike(String texto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cliente> q = em.createQuery(
                    "SELECT c FROM Cliente c " +
                    "WHERE LOWER(CONCAT(c.nombre,' ',COALESCE(c.apellido,''))) LIKE LOWER(:t) " +
                    "ORDER BY c.nombre", Cliente.class);
            q.setParameter("t", "%" + texto + "%");
            return q.getResultList();
        } finally { em.close(); }
    }
}
