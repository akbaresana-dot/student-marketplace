package com.studentmarket.gui;

import com.studentmarket.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterDialog extends JDialog {

    private final UserService userService = new UserService();
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JTextField txtNama = new JTextField(20);
    private final JTextField txtNim = new JTextField(20);
    private final JTextField txtNoHp = new JTextField(20);

    public RegisterDialog(Frame owner) {
        super(owner, "Daftar Akun Baru", true);

        JPanel form = new JPanel(new GridBagLayout());
        GuiUtil.baris(form, 0, "Email", txtEmail);
        GuiUtil.baris(form, 1, "Password (min. 6)", txtPassword);
        GuiUtil.baris(form, 2, "Nama", txtNama);
        GuiUtil.baris(form, 3, "NIM", txtNim);
        GuiUtil.baris(form, 4, "No HP", txtNoHp);

        JButton btnSimpan = new JButton("Daftar");
        JButton btnBatal = new JButton("Batal");
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tombol.add(btnSimpan);
        tombol.add(btnBatal);

        btnSimpan.addActionListener(e -> simpan());
        btnBatal.addActionListener(e -> dispose());

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(tombol, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void simpan() {
        try {
            userService.daftar(txtEmail.getText(), new String(txtPassword.getPassword()),
                    txtNama.getText(), txtNim.getText(), txtNoHp.getText());
            GuiUtil.info(this, "Pendaftaran berhasil. Silakan login.");
            dispose();
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }
}
