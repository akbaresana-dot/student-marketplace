package com.studentmarket.gui;

import com.studentmarket.model.DaftarFavorit;
import com.studentmarket.model.Listing;
import com.studentmarket.service.Sesi;
import com.studentmarket.util.Format;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class FavoritPanel extends JPanel implements Segarkan {

    private final DefaultTableModel model = GuiUtil.modelTabel("Judul", "Kategori", "Harga", "Penjual");
    private final JTable tabel = new JTable(model);

    public FavoritPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setRowHeight(22);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JButton btnHapus = new JButton("Hapus dari Favorit");
        JPanel bawah = new JPanel();
        bawah.add(btnHapus);
        add(bawah, BorderLayout.SOUTH);

        btnHapus.addActionListener(e -> hapusTerpilih());
    }

    @Override
    public void segarkan() {
        DaftarFavorit favorit = Sesi.getFavorit();
        model.setRowCount(0);
        for (Listing l : favorit.getDaftar()) {
            model.addRow(new Object[]{l.getJudul(), l.getKategori(),
                Format.rupiah(l.getHarga()), l.getPenjual().getNama()});
        }
    }

    private void hapusTerpilih() {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            GuiUtil.info(this, "Pilih salah satu barang terlebih dahulu.");
            return;
        }
        Listing l = Sesi.getFavorit().getDaftar().get(row);
        Sesi.getFavorit().hapus(l);
        segarkan();
    }
}
