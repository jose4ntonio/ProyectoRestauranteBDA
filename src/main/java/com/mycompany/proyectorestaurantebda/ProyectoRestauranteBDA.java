
package com.mycompany.proyectorestaurantebda;

import GUIs.FrmMenu;
import ConexionBD.ConexionBD;

import javax.swing.*;
import java.util.Locale;
import java.util.TimeZone;

public class ProyectoRestauranteBDA {

    public static void main(String[] args) {

        // --- Zona horaria y locale (reporteo/fechas coherentes con Hermosillo) ---
        TimeZone.setDefault(TimeZone.getTimeZone("America/Hermosillo"));
        Locale.setDefault(new Locale("es", "MX"));

        // --- Look & Feel (Nimbus si está disponible) ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equalsIgnoreCase(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // --- Hook de cierre para soltar la conexión JDBC si quedó abierta ---
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { ConexionBD.cerrarConexion(); } catch (Exception ignored) {}
        }));

        // --- Lanzar la UI en el EDT ---
        SwingUtilities.invokeLater(() -> {
            try {
                FrmMenu menu = new FrmMenu();
                menu.setVisible(true);
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error iniciando la aplicación: " + t.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });

    }
}