package com.studentmarket.model;

import java.sql.Timestamp;

public class Mahasiswa extends User {

    public Mahasiswa(int id, String email, String password, String nama,
                     String nim, String noHp, StatusUser status, Timestamp createdAt) {
        super(id, email, password, nama, nim, noHp, status, createdAt);   // super(...)
    }

    public Mahasiswa(String email, String password, String nama, String nim, String noHp) {
        super(email, password, nama, nim, noHp);
    }

    @Override
    public Role getRole() {
        return Role.USER;
    }

    @Override
    public String getHakAkses() {
        return "Pengguna: melihat feed, mengelola barang milik sendiri";
    }

  
    @Override
    public String tampilkanInfo() {
        return super.tampilkanInfo() + " | NIM: " + getNim();
    }
}
