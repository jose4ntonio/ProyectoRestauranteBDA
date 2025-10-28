package DAOs;

import Entidades.Ingrediente;
import Persistencia.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class IngredienteDAO {

    public boolean agregarIngrediente(Ingrediente i) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(i);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public boolean actualizarIngrediente(Ingrediente i) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(i);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public boolean eliminarIngrediente(int idIngrediente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Ingrediente ref = em.find(Ingrediente.class, idIngrediente);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
            return ref != null;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally { em.close(); }
    }

    public Ingrediente obtenerIngrediente(int idIngrediente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Ingrediente.class, idIngrediente);
        } finally { em.close(); }
    }

    public List<Ingrediente> listarIngredientes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Ingrediente> q = em.createQuery(
                    "SELECT i FROM Ingrediente i ORDER BY i.idIngrediente DESC", Ingrediente.class);
            return q.getResultList();
        } finally { em.close(); }
    }
}
