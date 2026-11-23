package com.studentmarket.gui;

import com.studentmarket.model.Listing;
import com.studentmarket.service.ListingService;
import com.studentmarket.service.Sesi;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ListingFormDialog extends JDialog {

    private final ListingService service = new ListingService();
    private final Listing lama;
    private final JComboBox<String> cbKategori =
            new JComboBox<>(new String[]{"ELEKTRONIK", "BUKU", "LAINNYA"});
    private final JTextField txtJudul = new JTextField(24);
    private final JTextArea txtDeskripsi = new JTextArea(4, 24);
    private final JTextField txtHarga = new JTextField(24);

    public ListingFormDialog(Frame owner, Listing lama) {
        super(owner, lama == null ? "Tambah Barang" : "Ubah Barang", true);
        this.lama = lama;

        if (lama != null) {
            cbKategori.setSelectedItem(lama.getKategori());
            txtJudul.setText(lama.getJudul());
            txtDeskripsi.setText(lama.getDeskripsi());
            txtHarga.setText(String.valueOf((long) lama.getHarga()));
        }
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GuiUtil.baris(form, 0, "Kategori", cbKategori);
        GuiUtil.baris(form, 1, "Judul", txtJudul);
        GuiUtil.baris(form, 2, "Deskripsi", new JScrollPane(txtDeskripsi));
        GuiUtil.baris(form, 3, "Harga (Rp)", txtHarga);

        JButton btnSimpan = new JButton("Simpan");
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
        String kategori = (String) cbKategori.getSelectedItem();
        try {
            if (lama == null) {
                service.tambah(Sesi.getUser(), kategori, txtJudul.getText(),
                        txtDeskripsi.getText(), txtHarga.getText());
            } else {
                service.ubah(lama, kategori, txtJudul.getText(),
                        txtDeskripsi.getText(), txtHarga.getText());
            }
            dispose();
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);       // dialog tetap terbuka agar input tidak hilang
        }
    }
}
