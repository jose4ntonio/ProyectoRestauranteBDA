package DAOs;

import ConexionBD.ConexionBD;
import Entidades.Cliente;
import Persistencia.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    public List<Cliente> buscar(String q) {
        List<Cliente> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM cliente
            WHERE LOWER(nombre)   LIKE ?
               OR LOWER(apellido) LIKE ?
               OR LOWER(telefono) LIKE ?
               OR LOWER(correo)   LIKE ?
        """;

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + q.toLowerCase() + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            ps.setString(4, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("idCliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreo(rs.getString("correo"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setTipo(rs.getString("tipo"));
                    lista.add(c);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
