/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import ConexionBD.ConexionBD;
import Entidades.Ingrediente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO {

    public boolean agregarIngrediente(Ingrediente i) {
        String sql = "INSERT INTO ingrediente(nombre, unidad, stock) VALUES(?,?,?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNombre());
            ps.setString(2, i.getUnidad());
            ps.setDouble(3, i.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarIngrediente(Ingrediente i) {
        String sql = "UPDATE ingrediente SET nombre=?, unidad=?, stock=? WHERE idIngrediente=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNombre());
            ps.setString(2, i.getUnidad());
            ps.setDouble(3, i.getStock());
            ps.setInt(4, i.getIdIngrediente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarIngrediente(int idIngrediente) {
        String sql = "DELETE FROM ingrediente WHERE idIngrediente=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idIngrediente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ingrediente obtenerIngrediente(int idIngrediente) {
        String sql = "SELECT * FROM ingrediente WHERE idIngrediente=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idIngrediente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ingrediente i = new Ingrediente(
                        rs.getString("nombre"),
                        rs.getString("unidad"),
                        rs.getDouble("stock")
                );
                i.setIdIngrediente(rs.getInt("idIngrediente"));
                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ingrediente> listarIngredientes() {
        List<Ingrediente> lista = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente";
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Ingrediente i = new Ingrediente(
                        rs.getString("nombre"),
                        rs.getString("unidad"),
                        rs.getDouble("stock")
                );
                i.setIdIngrediente(rs.getInt("idIngrediente"));
                lista.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
