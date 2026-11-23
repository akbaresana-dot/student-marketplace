package com.studentmarket.gui;

import com.studentmarket.model.User;
import com.studentmarket.service.Sesi;
import com.studentmarket.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

    private final UserService userService = new UserService();
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);

    public LoginFrame() {
        super("Student Marketplace - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel judul = new JLabel("Student Marketplace", SwingConstants.CENTER);
        judul.setFont(judul.getFont().deriveFont(java.awt.Font.BOLD, 22f));
        judul.setBorder(BorderFactory.createEmptyBorder(16, 8, 8, 8));

        JPanel form = new JPanel(new GridBagLayout());
        GuiUtil.baris(form, 0, "Email", txtEmail);
        GuiUtil.baris(form, 1, "Password", txtPassword);

        JButton btnLogin = new JButton("Login");
        JButton btnDaftar = new JButton("Daftar");
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tombol.add(btnLogin);
        tombol.add(btnDaftar);

        btnLogin.addActionListener(e -> login());
        btnDaftar.addActionListener(e -> new RegisterDialog(this).setVisible(true));
        txtPassword.addActionListener(e -> login());
        getRootPane().setDefaultButton(btnLogin);

        setLayout(new BorderLayout());
        add(judul, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(tombol, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void login() {
        try {
            User u = userService.login(txtEmail.getText(), new String(txtPassword.getPassword()));
            Sesi.masuk(u);
            new MainFrame().setVisible(true);
            dispose();
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }
}
