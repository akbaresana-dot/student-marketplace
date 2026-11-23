package com.studentmarket.repository;

import com.studentmarket.model.Listing;
import com.studentmarket.model.StatusListing;
import com.studentmarket.model.User;
import com.studentmarket.util.AppException;
import com.studentmarket.util.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListingRepository {

    private static final String DASAR =
            "SELECT l.id, l.judul, l.deskripsi, l.harga, l.kategori, l.status, l.created_at, "
            + "u.id AS u_id, u.email AS u_email, u.password AS u_password, u.nama AS u_nama, "
            + "u.nim AS u_nim, u.no_hp AS u_no_hp, u.role AS u_role, u.status AS u_status, "
            + "u.created_at AS u_created_at "
            + "FROM listing l JOIN users u ON u.id = l.user_id ";

    private List<Listing> ambil(String where, Object... param) {
        String sql = DASAR + where + " ORDER BY l.created_at DESC, l.id DESC";
        List<Listing> hasil = new ArrayList<>();
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User penjual = UserRepository.map(rs, "u_");
                    hasil.add(new Listing(
                            rs.getString("kategori"),
                            rs.getInt("id"),
                            penjual,
                            rs.getString("judul"),
                            rs.getString("deskripsi"),
                            rs.getDouble("harga"),
                            StatusListing.valueOf(rs.getString("status")),
                            rs.getTimestamp("created_at")));
                }
            }
            return hasil;
        } catch (SQLException e) {
            throw new AppException("Gagal membaca data barang: " + e.getMessage(), e);
        }
    }

   
    public List<Listing> feed(String keyword, String kategori) {
        String like = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        String where = "WHERE l.status = 'TERSEDIA' AND u.status = 'AKTIF' "
                + "AND (l.judul LIKE ? OR l.deskripsi LIKE ?) ";
        if (kategori != null && !kategori.isEmpty() && !"SEMUA".equals(kategori)) {
            return ambil(where + "AND l.kategori = ?", like, like, kategori);
        }
        return ambil(where, like, like);
    }

    public List<Listing> milikUser(int userId) {
        return ambil("WHERE l.user_id = ?", userId);
    }

    public List<Listing> semua() {
        return ambil("");
    }

    public int simpan(Listing l) {
        String sql = "INSERT INTO listing (user_id, judul, deskripsi, harga, kategori, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, l.getPenjual().getId());
            ps.setString(2, l.getJudul());
            ps.setString(3, l.getDeskripsi());
            ps.setDouble(4, l.getHarga());
            ps.setString(5, l.getKategori());
            ps.setString(6, l.getStatus().name());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                return k.next() ? k.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AppException("Gagal menyimpan barang: " + e.getMessage(), e);
        }
    }

    public void update(Listing l) {
        String sql = "UPDATE listing SET judul = ?, deskripsi = ?, harga = ?, kategori = ? WHERE id = ?";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getJudul());
            ps.setString(2, l.getDeskripsi());
            ps.setDouble(3, l.getHarga());
            ps.setString(4, l.getKategori());
            ps.setInt(5, l.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Gagal memperbarui barang: " + e.getMessage(), e);
        }
    }

    public void updateStatus(int id, StatusListing status) {
        String sql = "UPDATE listing SET status = ? WHERE id = ?";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Gagal mengubah status barang: " + e.getMessage(), e);
        }
    }

    
    public void hapus(int id) {
        try (Connection c = Koneksi.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM listing WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Gagal menghapus barang: " + e.getMessage(), e);
        }
    }

    
    public void muatGambar(Listing l) {
        String sql = "SELECT id, url, urutan FROM listing_image WHERE listing_id = ? ORDER BY urutan, id";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, l.getId());
            l.bersihkanGambar();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.tambahGambar(rs.getInt("id"), rs.getString("url"), rs.getInt("urutan"));
                }
            }
        } catch (SQLException e) {
            throw new AppException("Gagal membaca foto barang: " + e.getMessage(), e);
        }
    }

    public int hitungGambar(int listingId) {
        String sql = "SELECT COUNT(*) FROM listing_image WHERE listing_id = ?";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AppException("Gagal menghitung foto: " + e.getMessage(), e);
        }
    }

    public int tambahGambar(int listingId, String url, int urutan) {
        String sql = "INSERT INTO listing_image (listing_id, url, urutan) VALUES (?, ?, ?)";
        try (Connection c = Koneksi.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, listingId);
            ps.setString(2, url);
            ps.setInt(3, urutan);
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                return k.next() ? k.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AppException("Gagal menyimpan foto: " + e.getMessage(), e);
        }
    }
}
