package com.studentmarket.model;

import com.studentmarket.util.Format;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Listing implements DapatDijual {

    private static final double PERSEN_ADMIN = 0.03;   

    private int id;
    private User penjual;                                        
    private String kategori;                                     
    private String judul;
    private String deskripsi;
    private double harga;
    private StatusListing status;
    private Timestamp createdAt;
    private final List<ListingImage> gambar = new ArrayList<>(); 

    public Listing(String kategori, int id, User penjual, String judul, String deskripsi,
                   double harga, StatusListing status, Timestamp createdAt) {
        this.id = id;
        setKategori(kategori);
        setPenjual(penjual);
        setJudul(judul);
        setDeskripsi(deskripsi);
        setHarga(harga);
        this.status = (status == null) ? StatusListing.TERSEDIA : status;
        this.createdAt = createdAt;
    }

    
    public Listing(String kategori, User penjual, String judul, String deskripsi, double harga) {
        this(kategori, 0, penjual, judul, deskripsi, harga, StatusListing.TERSEDIA, null);
    }

    
    public double hitungBiayaAdmin() {
        return harga * PERSEN_ADMIN;
    }

    
    @Override
    public boolean isTersedia() {
        return status == StatusListing.TERSEDIA;
    }

    @Override
    public void tandaiTerjual() {
        this.status = StatusListing.TERJUAL;
    }

   
    public double hitungTotalBayar() {
        return harga + hitungBiayaAdmin();
    }

   
    public void tambahGambar(int idGambar, String url, int urutan) {
        gambar.add(new ListingImage(idGambar, this.id, url, urutan));
    }

    
    public void tambahGambar(String url) {
        tambahGambar(0, url, gambar.size() + 1);
    }

    public void bersihkanGambar() {
        gambar.clear();
    }

    public List<ListingImage> getGambar() {
        return Collections.unmodifiableList(gambar);
    }

    public String tampilkanInfo() {
        return judul
                + "\nKategori     : " + getKategori()
                + "\nHarga        : " + Format.rupiah(harga)
                + "\nBiaya admin  : " + Format.rupiah(hitungBiayaAdmin())
                + "\nTotal bayar  : " + Format.rupiah(hitungTotalBayar())
                + "\nStatus       : " + status
                + "\nPenjual      : " + penjual.getNama()
                + "\n\n" + deskripsi;
    }


    public int getId() { return id; }
    public String getKategori() { return kategori; }
    public User getPenjual() { return penjual; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public double getHarga() { return harga; }
    public StatusListing getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }

    
    public void setKategori(String kategori) {
        String k = (kategori == null) ? "" : kategori.trim().toUpperCase();
        if (!k.equals("ELEKTRONIK") && !k.equals("BUKU") && !k.equals("LAINNYA")) {
            throw new IllegalArgumentException("Kategori harus ELEKTRONIK, BUKU, atau LAINNYA");
        }
        this.kategori = k;
    }

    public void setPenjual(User penjual) {
        if (penjual == null) {
            throw new IllegalArgumentException("Penjual tidak boleh kosong");
        }
        this.penjual = penjual;
    }

    public void setJudul(String judul) {
        if (judul == null || judul.trim().isEmpty()) {
            throw new IllegalArgumentException("Judul barang wajib diisi");
        }
        if (judul.trim().length() > 150) {
            throw new IllegalArgumentException("Judul maksimal 150 karakter");
        }
        this.judul = judul.trim();
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = (deskripsi == null) ? "" : deskripsi.trim();
    }

    public void setHarga(double harga) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih besar dari 0");
        }
        this.harga = harga;
    }

    @Override
    public String toString() {
        return judul;
    }
}
