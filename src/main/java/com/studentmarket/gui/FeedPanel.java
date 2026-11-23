package com.studentmarket.gui;

import com.studentmarket.model.Listing;
import com.studentmarket.model.ListingImage;
import com.studentmarket.service.ListingService;
import com.studentmarket.service.Sesi;
import com.studentmarket.util.Format;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


public class FeedPanel extends JPanel implements Segarkan {

    private final ListingService service = new ListingService();
    private List<Listing> data = new ArrayList<>();

    private final JTextField txtCari = new JTextField(18);
    private final JComboBox<String> cbKategori =
            new JComboBox<>(new String[]{"SEMUA", "ELEKTRONIK", "BUKU", "LAINNYA"});
    private final DefaultTableModel model = GuiUtil.modelTabel("Judul", "Kategori", "Harga", "Penjual");
    private final JTable tabel = new JTable(model);
    private final JTextArea txtDetail = new JTextArea();
    private final JLabel lblFoto = new JLabel("Pilih barang", SwingConstants.CENTER);

    public FeedPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton btnCari = new JButton("Cari");
        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        atas.add(new JLabel("Cari:"));
        atas.add(txtCari);
        atas.add(new JLabel("Kategori:"));
        atas.add(cbKategori);
        atas.add(btnCari);
        add(atas, BorderLayout.NORTH);

        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setRowHeight(22);

        lblFoto.setPreferredSize(new Dimension(240, 240));
        lblFoto.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
        txtDetail.setEditable(false);
        txtDetail.setLineWrap(true);
        txtDetail.setWrapStyleWord(true);
        txtDetail.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel kanan = new JPanel(new BorderLayout(6, 6));
        kanan.add(lblFoto, BorderLayout.NORTH);
        kanan.add(new JScrollPane(txtDetail), BorderLayout.CENTER);
        JButton btnFavorit = new JButton("\u2665 Tambah ke Favorit");
        kanan.add(btnFavorit, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tabel), kanan);
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);

        btnFavorit.addActionListener(e -> {
            Listing l = terpilih();
            if (l == null) {
                return;
            }
            if (l.getPenjual().getId() == Sesi.getUser().getId()) {
                GuiUtil.info(this, "Ini barang milik Anda sendiri.");
            } else if (Sesi.getFavorit().tambah(l)) {
                GuiUtil.info(this, "Ditambahkan ke Favorit.");
            } else {
                GuiUtil.info(this, "Barang ini sudah ada di Favorit.");
            }
        });
        btnCari.addActionListener(e -> segarkan());
        txtCari.addActionListener(e -> segarkan());
        cbKategori.addActionListener(e -> segarkan());
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tampilDetail();
            }
        });
    }

    @Override
    public void segarkan() {
        try {
            data = service.feed(txtCari.getText(), (String) cbKategori.getSelectedItem());
            model.setRowCount(0);
            for (Listing l : data) {
                model.addRow(new Object[]{l.getJudul(), l.getKategori(),
                    Format.rupiah(l.getHarga()), l.getPenjual().getNama()});
            }
            kosongkanDetail();
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

    private void kosongkanDetail() {
        txtDetail.setText("");
        lblFoto.setIcon(null);
        lblFoto.setText("Pilih barang");
    }

    private void tampilDetail() {
        int row = tabel.getSelectedRow();
        if (row < 0 || row >= data.size()) {
            return;
        }
        try {
            Listing l = data.get(row);
            service.muatGambar(l);
            txtDetail.setText(l.tampilkanInfo() + "\n\nKontak penjual: " + l.getPenjual().getNoHp());
            txtDetail.setCaretPosition(0);
            tampilFoto(l);
        } catch (RuntimeException ex) {
            GuiUtil.error(this, ex);
        }
    }

    private void tampilFoto(Listing l) {
        if (l.getGambar().isEmpty()) {
            lblFoto.setIcon(null);
            lblFoto.setText("Tidak ada foto");
            return;
        }
        ListingImage g = l.getGambar().get(0);
        File f = service.fileGambar(g);
        ImageIcon ikon = new ImageIcon(f.getAbsolutePath());
        if (ikon.getIconWidth() <= 0) {
            lblFoto.setIcon(null);
            lblFoto.setText("File foto tidak ditemukan");
            return;
        }
        double skala = Math.min(240.0 / ikon.getIconWidth(), 240.0 / ikon.getIconHeight());
        Image kecil = ikon.getImage().getScaledInstance(
                (int) (ikon.getIconWidth() * skala), (int) (ikon.getIconHeight() * skala), Image.SCALE_SMOOTH);
        lblFoto.setText("");
        lblFoto.setIcon(new ImageIcon(kecil));
    }
}
