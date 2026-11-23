package com.studentmarket.gui;

import com.studentmarket.model.Listing;
import com.studentmarket.service.ListingService;
import com.studentmarket.service.Sesi;
import com.studentmarket.util.Format;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class BarangSayaPanel extends JPanel implements Segarkan {

    private final ListingService service = new ListingService();
    private List<Listing> data = new ArrayList<>();
    private final DefaultTableModel model = GuiUtil.modelTabel("Judul", "Kategori", "Harga", "Status");
    private final JTable tabel = new JTable(model);

    public BarangSayaPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setRowHeight(22);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnTerjual = new JButton("Tandai Terjual");
        JButton btnFoto = new JButton("Tambah Foto");
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombol.add(btnTambah);
        tombol.add(btnUbah);
        tombol.add(btnHapus);
        tombol.add(btnTerjual);
        tombol.add(btnFoto);
        add(tombol, BorderLayout.NORTH);

        btnTambah.addActionListener(e -> tambah());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnTerjual.addActionListener(e -> terjual());
        btnFoto.addActionListener(e -> foto());
    }

    private Frame induk() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    @Override
    public void segarkan() {
        try {
            data = service.barangSaya(Sesi.getUser());
            model.setRowCount(0);
            for (Listing l : data) {
                model.addRow(new Object[]{l.getJudul(), l.getKategori(),
                    Format.rupiah(l.getHarga()), l.getStatus()});
            }
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }

    private Listing terpilih() {
        int row = tabel.getSelectedRow();
        if (row < 0 || row >= data.size()) {
            GuiUtil.info(this, "Pilih salah satu barang terlebih dahulu.");
            return null;
        }
        return data.get(row);
    }

    private void tambah() {
        new ListingFormDialog(induk(), null).setVisible(true);
        segarkan();
    }

    private void ubah() {
        Listing l = terpilih();
        if (l == null) {
            return;
        }
        new ListingFormDialog(induk(), l).setVisible(true);
        segarkan();
    }

    private void hapus() {
        Listing l = terpilih();
        if (l == null) {
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus \"" + l.getJudul() + "\" beserta fotonya?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                service.hapus(l);
                segarkan();
            } catch (RuntimeException ex) {
                GuiUtil.error(this, ex);
            }
        }
    }

    private void terjual() {
        Listing l = terpilih();
        if (l == null) {
            return;
        }
        try {
            if (!l.isTersedia()) {
                GuiUtil.info(this, "Barang ini sudah berstatus TERJUAL.");
                return;
            }
            service.tandaiTerjual(l);
            segarkan();
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }

    private void foto() {
        Listing l = terpilih();
        if (l == null) {
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Gambar (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                service.tambahFoto(l, f);
                GuiUtil.info(this, "Foto berhasil ditambahkan.");
            } catch (RuntimeException ex) {
                GuiUtil.error(this, ex);
            }
        }
    }
}
