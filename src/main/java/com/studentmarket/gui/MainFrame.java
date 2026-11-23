package com.studentmarket.gui;

import com.studentmarket.model.User;
import com.studentmarket.service.Sesi;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    public MainFrame() {
        User user = Sesi.getUser();
        // Polimorfisme: user bisa Mahasiswa atau Admin, getRole() memberi hasil berbeda
        setTitle("Student Marketplace - " + user.getNama() + " [" + user.getRole() + "]");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Feed", new FeedPanel());
        tabs.addTab("Favorit", new FavoritPanel());
        tabs.addTab("Barang Saya", new BarangSayaPanel());
        tabs.addTab("Profil", new ProfilPanel());
        if (user.isAdmin()) {
            tabs.addTab("Admin", new AdminPanel());
        }
        tabs.addChangeListener(e -> {
            Component c = tabs.getSelectedComponent();
            if (c instanceof Segarkan) {
                ((Segarkan) c).segarkan();
            }
        });
        add(tabs);

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Akun");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem keluar = new JMenuItem("Keluar Aplikasi");
        logout.addActionListener(e -> {
            Sesi.keluar();
            dispose();
            new LoginFrame().setVisible(true);
        });
        keluar.addActionListener(e -> System.exit(0));
        menu.add(logout);
        menu.add(keluar);
        bar.add(menu);
        setJMenuBar(bar);

        ((Segarkan) tabs.getComponentAt(0)).segarkan();
    }
}
