package com.studentmarket.repository;

import com.studentmarket.model.Admin;
import com.studentmarket.model.Mahasiswa;
import com.studentmarket.model.Role;
import com.studentmarket.model.StatusUser;
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


public class UserRepository {

    private static final String KOLOM =
            "id, email, password, nama, nim, no_hp, role, status, created_at";


    public static User map(ResultSet rs, String p) throws SQLException {
        Role role = Role.valueOf(rs.getString(p + "role"));
        int id = rs.getInt(p + "id");
        String email = rs.getString(p + "email");
        String pass = rs.getString(p + "password");
        String nama = rs.getString(p + "nama");
        String nim = rs.getString(p + "nim");
        String noHp = rs.getString(p + "no_hp");
        StatusUser st = StatusUser.valueOf(rs.getString(p + "status"));
        java.sql.Timestamp dibuat = rs.getTimestamp(p + "created_at");
        if (role == Role.ADMIN) {
            return new Admin(id, email, pass, nama, nim, noHp, st, dibuat);
        }
        return new Mahasiswa(id, email, pass, nama, nim, noHp, st, dibuat);
    }

    public User cariByEmail(String email) {
        String sql = "SELECT " + KOLOM + " FROM users WHERE email = ?";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs, "") : null;
            }
        } catch (SQLException e) {
            throw new AppException("Gagal membaca data pengguna: " + e.getMessage(), e);
        }
    }

    public List<User> semua() {
        String sql = "SELECT " + KOLOM + " FROM users ORDER BY created_at DESC";
        List<User> hasil = new ArrayList<>();
        try (Connection c = Koneksi.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                hasil.add(map(rs, ""));
            }
            return hasil;
        } catch (SQLException e) {
            throw new AppException("Gagal membaca daftar pengguna: " + e.getMessage(), e);
        }
    }

    public int simpan(User u) {
        String sql = "INSERT INTO users (email, password, nama, nim, no_hp, role, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNama());
            ps.setString(4, u.getNim());
            ps.setString(5, u.getNoHp());
            ps.setString(6, u.getRole().name());
            ps.setString(7, u.getStatus().name());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                return k.next() ? k.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AppException("Gagal menyimpan pengguna: " + e.getMessage(), e);
        }
    }

    public void updateProfil(User u) {
        String sql = "UPDATE users SET nama = ?, nim = ?, no_hp = ?, password = ? WHERE id = ?";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNama());
            ps.setString(2, u.getNim());
            ps.setString(3, u.getNoHp());
            ps.setString(4, u.getPassword());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Gagal memperbarui profil: " + e.getMessage(), e);
        }
    }

    public void updateStatus(int id, StatusUser status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (Connection c = Koneksi.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Gagal mengubah status pengguna: " + e.getMessage(), e);
        }
    }
}
