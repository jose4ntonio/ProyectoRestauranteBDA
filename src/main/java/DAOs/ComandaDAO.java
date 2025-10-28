package DAOs;

import Entidades.Comanda;
import Persistencia.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ComandaDAO {

    public boolean agregarComanda(Comanda c) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c); // Detalles se guardan por cascade
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

    public List<Comanda> listarComandas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Comanda> q = em.createQuery(
                "SELECT c FROM Comanda c ORDER BY c.fecha DESC", Comanda.class);
            return q.getResultList();
        } finally { em.close(); }
    }

    public Comanda obtenerComanda(int idComanda) {
        EntityManager em = JPAUtil.getEntityManager();
        try { return em.find(Comanda.class, idComanda); }
        finally { em.close(); }
    }

    public boolean actualizarComanda(Comanda c) {
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

    public boolean eliminarComanda(int idComanda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Comanda ref = em.find(Comanda.class, idComanda);
            if (ref != null) em.remove(ref); // elimina también los detalles por orphanRemoval
            em.getTransaction().commit();
            return ref != null;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }
}
