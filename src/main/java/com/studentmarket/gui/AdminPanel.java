package com.studentmarket.gui;

import com.studentmarket.model.Listing;
import com.studentmarket.model.StatusUser;
import com.studentmarket.model.User;
import com.studentmarket.service.ListingService;
import com.studentmarket.service.UserService;
import com.studentmarket.util.Format;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class AdminPanel extends JPanel implements Segarkan {

    private final UserService userService = new UserService();
    private final ListingService listingService = new ListingService();
    private List<User> pengguna = new ArrayList<>();
    private List<Listing> barang = new ArrayList<>();

    private final DefaultTableModel modelUser =
            GuiUtil.modelTabel("Email", "Nama", "NIM", "Role", "Status", "Hak akses");
    private final DefaultTableModel modelBarang =
            GuiUtil.modelTabel("Judul", "Kategori", "Penjual", "Harga", "Status");
    private final JTable tabelUser = new JTable(modelUser);
    private final JTable tabelBarang = new JTable(modelBarang);

    public AdminPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tabelUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnAktif = new JButton("Aktifkan");
        JButton btnNonaktif = new JButton("Nonaktifkan");
        JPanel tombolUser = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombolUser.add(btnAktif);
        tombolUser.add(btnNonaktif);
        JPanel panelUser = new JPanel(new BorderLayout());
        panelUser.add(tombolUser, BorderLayout.NORTH);
        panelUser.add(new JScrollPane(tabelUser), BorderLayout.CENTER);

        JButton btnHapus = new JButton("Hapus Barang");
        JPanel tombolBarang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombolBarang.add(btnHapus);
        JPanel panelBarang = new JPanel(new BorderLayout());
        panelBarang.add(tombolBarang, BorderLayout.NORTH);
        panelBarang.add(new JScrollPane(tabelBarang), BorderLayout.CENTER);

        JTabbedPane sub = new JTabbedPane();
        sub.addTab("Pengguna", panelUser);
        sub.addTab("Semua Barang", panelBarang);
        add(sub, BorderLayout.CENTER);

        btnAktif.addActionListener(e -> ubahStatus(StatusUser.AKTIF));
        btnNonaktif.addActionListener(e -> ubahStatus(StatusUser.NONAKTIF));
        btnHapus.addActionListener(e -> hapusBarang());
    }

    @Override
    public void segarkan() {
        try {
            pengguna = userService.semuaPengguna();
            modelUser.setRowCount(0);
            for (User u : pengguna) {
                modelUser.addRow(new Object[]{u.getEmail(), u.getNama(), u.getNim(),
                    u.getRole(), u.getStatus(), u.getHakAkses()});
            }
            barang = listingService.semuaBarang();
            modelBarang.setRowCount(0);
            for (Listing l : barang) {
                modelBarang.addRow(new Object[]{l.getJudul(), l.getKategori(), l.getPenjual().getNama(),
                    Format.rupiah(l.getHarga()), l.getStatus()});
            }
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }

    private void ubahStatus(StatusUser status) {
        int row = tabelUser.getSelectedRow();
        if (row < 0 || row >= pengguna.size()) {
            GuiUtil.info(this, "Pilih pengguna terlebih dahulu.");
            return;
        }
        try {
            userService.ubahStatus(pengguna.get(row), status);
            segarkan();
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }

    private void hapusBarang() {
        int row = tabelBarang.getSelectedRow();
        if (row < 0 || row >= barang.size()) {
            GuiUtil.info(this, "Pilih barang terlebih dahulu.");
            return;
        }
        Listing l = barang.get(row);
        if (JOptionPane.showConfirmDialog(this, "Hapus \"" + l.getJudul() + "\"?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                listingService.hapus(l);
                segarkan();
            } catch (RuntimeException ex) {
                GuiUtil.error(this, ex);
            }
        }
    }
}
