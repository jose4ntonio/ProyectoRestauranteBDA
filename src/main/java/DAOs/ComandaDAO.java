
package DAOs;

import ConexionBD.ConexionBD;
import Entidades.Comanda;
import Entidades.DetalleComanda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO {

    public boolean agregarComanda(Comanda c, List<DetalleComanda> detalles) {
        
        String sqlComanda = "INSERT INTO comanda (folio, fecha, estado, total, mesa, clienteFrecuente) VALUES (?,?,?,?,?,?)";
        String sqlDetalle = "INSERT INTO detalle_comanda (idComanda, idProducto, cantidad, precioUnitario, totalProducto, comentarios) VALUES (?,?,?,?,?,?)";
        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) throw new SQLException("Conexión nula.");

            con.setAutoCommit(false);
            int idGenerado;

            try (PreparedStatement ps = con.prepareStatement(sqlComanda, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getFolio());
                ps.setTimestamp(2, new Timestamp(c.getFecha().getTime()));
                ps.setString(3, c.getEstado());
                ps.setDouble(4, c.getTotal());
                ps.setString(5, c.getMesa());
                ps.setString(6, c.getClienteFrecuente());
                if (ps.executeUpdate() == 0) throw new SQLException("No se insertó la comanda.");

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("No se obtuvo idComanda.");
                    idGenerado = rs.getInt(1);
                }
            }

            // insertar detalles
            try (PreparedStatement psd = con.prepareStatement(sqlDetalle)) {
                for (DetalleComanda d : detalles) {
                    psd.setInt(1, idGenerado);
                    psd.setInt(2, d.getIdProducto());
                    psd.setInt(3, d.getCantidad());
                    psd.setDouble(4, d.getPrecioUnitario());
                    psd.setDouble(5, d.getTotalProducto());
                    psd.setString(6, d.getComentarios());
                    psd.addBatch();
                }
                psd.executeBatch();
            }

            con.commit();
            c.setIdComanda(idGenerado);
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizarComanda(Comanda c) {
        String sql = "UPDATE comanda SET folio=?, fecha=?, estado=?, total=?, mesa=?, clienteFrecuente=? WHERE idComanda=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) throw new SQLException("Conexión nula.");
            ps.setString(1, c.getFolio());
            ps.setTimestamp(2, new Timestamp(c.getFecha().getTime()));
            ps.setString(3, c.getEstado());
            ps.setDouble(4, c.getTotal());
            ps.setString(5, c.getMesa());
            ps.setString(6, c.getClienteFrecuente());
            ps.setInt(7, c.getIdComanda());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizarEstado(int idComanda, String estado) {
        String sql = "UPDATE comanda SET estado=? WHERE idComanda=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) throw new SQLException("Conexión nula.");
            ps.setString(1, estado);
            ps.setInt(2, idComanda);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Comanda obtenerComanda(int idComanda) {
        String sql = "SELECT * FROM comanda WHERE idComanda=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) throw new SQLException("Conexión nula.");
            ps.setInt(1, idComanda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Comanda c = new Comanda();
                    c.setIdComanda(rs.getInt("idComanda"));
                    c.setFolio(rs.getString("folio"));
                    c.setFecha(rs.getTimestamp("fecha"));
                    c.setEstado(rs.getString("estado"));
                    c.setTotal(rs.getDouble("total"));
                    c.setMesa(rs.getString("mesa"));
                    c.setClienteFrecuente(rs.getString("clienteFrecuente"));
                    return c;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Comanda> listarComandas() {
        List<Comanda> out = new ArrayList<>();
        String sql = "SELECT * FROM comanda ORDER BY fecha DESC";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (con == null) throw new SQLException("Conexión nula.");
            while (rs.next()) {
                Comanda c = new Comanda();
                c.setIdComanda(rs.getInt("idComanda"));
                c.setFolio(rs.getString("folio"));
                c.setFecha(rs.getTimestamp("fecha"));
                c.setEstado(rs.getString("estado"));
                c.setTotal(rs.getDouble("total"));
                c.setMesa(rs.getString("mesa"));
                c.setClienteFrecuente(rs.getString("clienteFrecuente"));
                out.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public boolean eliminarComanda(int idComanda) {
        String sql = "DELETE FROM comanda WHERE idComanda=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) throw new SQLException("Conexión nula.");
            ps.setInt(1, idComanda);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
