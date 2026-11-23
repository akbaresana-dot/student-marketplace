package com.studentmarket;

import com.studentmarket.gui.LoginFrame;
import com.studentmarket.util.Koneksi;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class StudentMarketApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // pakai tampilan default Swing
        }
        SwingUtilities.invokeLater(() -> {
            try (Connection c = Koneksi.get()) {
                // koneksi berhasil
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Tidak dapat terhubung ke database:\n" + e.getMessage()
                        + "\n\nPeriksa application.properties, pastikan MySQL berjalan\n"
                        + "dan database/schema.sql sudah dijalankan.",
                        "Koneksi Gagal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            new LoginFrame().setVisible(true);
        });
    }
}
