package com.studentmarket.service;

import com.studentmarket.model.Listing;
import com.studentmarket.model.ListingImage;
import com.studentmarket.model.StatusListing;
import com.studentmarket.model.User;
import com.studentmarket.repository.ListingRepository;
import com.studentmarket.util.AppException;
import com.studentmarket.util.Koneksi;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


public class ListingService {

    private final ListingRepository repo = new ListingRepository();

    public List<Listing> feed(String keyword, String kategori) {
        return repo.feed(keyword, kategori);
    }

    public List<Listing> barangSaya(User u) {
        return repo.milikUser(u.getId());
    }

    public List<Listing> semuaBarang() {
        return repo.semua();
    }

    public void tambah(User penjual, String kategori, String judul, String deskripsi, String hargaTeks) {
        double harga = parseHarga(hargaTeks);
        Listing l = new Listing(kategori, 0, penjual, judul, deskripsi, harga, StatusListing.TERSEDIA, null);
        repo.simpan(l);
    }

   
    public void ubah(Listing lama, String kategori, String judul, String deskripsi, String hargaTeks) {
        pastikanBolehKelola(lama);
        double harga = parseHarga(hargaTeks);
        Listing baru = new Listing(kategori, lama.getId(), lama.getPenjual(), judul, deskripsi,
                harga, lama.getStatus(), lama.getCreatedAt());
        repo.update(baru);
    }

    public void tandaiTerjual(Listing l) {
        pastikanBolehKelola(l);
        l.tandaiTerjual();                                   
        repo.updateStatus(l.getId(), l.getStatus());
    }

    public void hapus(Listing l) {
        pastikanBolehKelola(l);
        muatGambar(l);
        repo.hapus(l.getId());
        for (ListingImage g : l.getGambar()) {               
            try {
                Files.deleteIfExists(fileGambar(g).toPath());
            } catch (IOException ignored) {
            }
        }
    }

    public void muatGambar(Listing l) {
        repo.muatGambar(l);
    }

    public File fileGambar(ListingImage g) {
        return Paths.get(Koneksi.getUploadDir()).resolve(g.getUrl()).toFile();
    }

    public void tambahFoto(Listing l, File sumber) {
        pastikanBolehKelola(l);
        String nama = sumber.getName().toLowerCase();
        String ext = nama.contains(".") ? nama.substring(nama.lastIndexOf('.') + 1) : "";
        if (!ext.matches("jpg|jpeg|png|gif")) {
            throw new AppException("Format foto harus JPG, PNG, atau GIF");
        }
        if (sumber.length() > 5L * 1024 * 1024) {
            throw new AppException("Ukuran foto maksimal 5 MB");
        }
        try {
            Path dir = Paths.get(Koneksi.getUploadDir());
            Files.createDirectories(dir);
            String namaBaru = "listing" + l.getId() + "_" + System.currentTimeMillis() + "." + ext;
            Files.copy(sumber.toPath(), dir.resolve(namaBaru), StandardCopyOption.REPLACE_EXISTING);
            int urutan = repo.hitungGambar(l.getId()) + 1;
            int idBaru = repo.tambahGambar(l.getId(), namaBaru, urutan);
            l.tambahGambar(idBaru, namaBaru, urutan);
        } catch (IOException e) {
            throw new AppException("Gagal menyimpan file foto: " + e.getMessage(), e);
        }
    }

    
    private void pastikanBolehKelola(Listing l) {
        User saya = Sesi.getUser();
        if (saya == null || !(saya.isAdmin() || saya.getId() == l.getPenjual().getId())) {
            throw new AppException("Anda tidak berhak mengelola barang ini");
        }
    }

    private double parseHarga(String teks) {
        String digit = (teks == null) ? "" : teks.replaceAll("[^0-9]", "");
        if (digit.isEmpty()) {
            throw new AppException("Harga wajib diisi (angka)");
        }
        if (digit.length() > 12) {
            throw new AppException("Harga terlalu besar");
        }
        return Double.parseDouble(digit);
    }
}
