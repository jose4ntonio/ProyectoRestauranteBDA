
package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {


    private static final String URL = "jdbc:mysql://localhost:3306/restaurante_itson?useSSL=true&serverTimezone=UTC";
    private static final String USER = "Donato"; // Cambia por tu usuario
    private static final String PASSWORD = "809865"; // Cambia por tu contraseña

    private static Connection conexion;

     public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            // Asegura autocommit ON si tu DAO no maneja transacciones manuales
            con.setAutoCommit(true);
            System.out.println("[DB] Conectado: " + con.getMetaData().getURL());
            return con;
        } catch (SQLException e) {
            System.err.println("[DB] Error al conectar: " + e.getMessage());
            return null;
        }
    }
    
//    // Método para obtener la conexión
//    public static Connection getConnection() {
//        if (conexion == null) {
//            try {
//                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
//                System.out.println("Conexión a la base de datos establecida correctamente.");
//            } catch (SQLException e) {
//                System.err.println("Error al conectar a la base de datos: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//        return conexion;
//    }

    // Método para cerrar la conexión
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Prueba de conexión
    public static void main(String[] args) {
        Connection con = ConexionBD.getConnection();
        if (con != null) {
            System.out.println("¡Base de datos lista para usarse!");
            ConexionBD.cerrarConexion();
        }
    }
}
