package com.studentmarket.service;

import com.studentmarket.model.Mahasiswa;
import com.studentmarket.model.StatusUser;
import com.studentmarket.model.User;
import com.studentmarket.repository.UserRepository;
import com.studentmarket.util.AppException;
import com.studentmarket.util.Keamanan;
import java.util.List;

public class UserService {

    private final UserRepository repo = new UserRepository();

    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new AppException("Email dan password wajib diisi");
        }
        User u = repo.cariByEmail(email.trim().toLowerCase());
        if (u == null || !u.getPassword().equals(Keamanan.hash(password))) {
            throw new AppException("Email atau password salah");
        }
        if (u.getStatus() == StatusUser.NONAKTIF) {
            throw new AppException("Akun Anda dinonaktifkan. Hubungi admin.");
        }
        return u;
    }

    public void daftar(String email, String password, String nama, String nim, String noHp) {
        if (password == null || password.length() < 6) {
            throw new AppException("Password minimal 6 karakter");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new AppException("Nama wajib diisi");
        }
       
        User baru = new Mahasiswa(email, Keamanan.hash(password), nama, nim, noHp);
        if (repo.cariByEmail(baru.getEmail()) != null) {
            throw new AppException("Email sudah terdaftar");
        }
        repo.simpan(baru);
    }

    public void simpanProfil(User u, String nama, String nim, String noHp, String passwordBaru) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new AppException("Nama wajib diisi");
        }
        u.setNama(nama);
        u.setNim(nim);
        u.setNoHp(noHp);
        if (passwordBaru != null && !passwordBaru.isEmpty()) {
            if (passwordBaru.length() < 6) {
                throw new AppException("Password baru minimal 6 karakter");
            }
            u.setPassword(Keamanan.hash(passwordBaru));
        }
        repo.updateProfil(u);
    }

    public List<User> semuaPengguna() {
        return repo.semua();
    }

    public void ubahStatus(User target, StatusUser status) {
        User saya = Sesi.getUser();
        if (saya == null || !saya.isAdmin()) {
            throw new AppException("Hanya admin yang boleh mengubah status pengguna");
        }
        if (target.getId() == saya.getId()) {
            throw new AppException("Anda tidak dapat mengubah status akun sendiri");
        }
        repo.updateStatus(target.getId(), status);
        target.setStatus(status);
    }
}
