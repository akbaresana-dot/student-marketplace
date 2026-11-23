package com.studentmarket.gui;

import com.studentmarket.model.User;
import com.studentmarket.service.Sesi;
import com.studentmarket.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ProfilPanel extends JPanel implements Segarkan {

    private final UserService service = new UserService();
    private final JLabel lblEmail = new JLabel();
    private final JLabel lblRole = new JLabel();
    private final JLabel lblStatus = new JLabel();
    private final JTextField txtNama = new JTextField(24);
    private final JTextField txtNim = new JTextField(24);
    private final JTextField txtNoHp = new JTextField(24);
    private final JPasswordField txtPasswordBaru = new JPasswordField(24);

    public ProfilPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel form = new JPanel(new GridBagLayout());
        GuiUtil.baris(form, 0, "Email", lblEmail);
        GuiUtil.baris(form, 1, "Hak akses", lblRole);
        GuiUtil.baris(form, 2, "Status", lblStatus);
        GuiUtil.baris(form, 3, "Nama", txtNama);
        GuiUtil.baris(form, 4, "NIM", txtNim);
        GuiUtil.baris(form, 5, "No HP", txtNoHp);
        GuiUtil.baris(form, 6, "Password baru", txtPasswordBaru);

        JButton btnSimpan = new JButton("Simpan Perubahan");
        btnSimpan.addActionListener(e -> simpan());
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bawah.add(btnSimpan);
        bawah.add(new JLabel("(kosongkan password jika tidak ingin mengganti)"));

        JPanel isi = new JPanel(new BorderLayout());
        isi.add(form, BorderLayout.NORTH);
        isi.add(bawah, BorderLayout.CENTER);
        add(isi, BorderLayout.NORTH);
        segarkan();
    }

    @Override
    public void segarkan() {
        User u = Sesi.getUser();
        lblEmail.setText(u.getEmail());
        lblRole.setText(u.getHakAkses());       
        lblStatus.setText(u.getStatus().name());
        txtNama.setText(u.getNama());
        txtNim.setText(u.getNim());
        txtNoHp.setText(u.getNoHp());
        txtPasswordBaru.setText("");
    }

    private void simpan() {
        try {
            service.simpanProfil(Sesi.getUser(), txtNama.getText(), txtNim.getText(),
                    txtNoHp.getText(), new String(txtPasswordBaru.getPassword()));
            txtPasswordBaru.setText("");
            GuiUtil.info(this, "Profil berhasil diperbarui.");
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
            segarkan();
        }
    }
}
