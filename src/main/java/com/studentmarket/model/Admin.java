package com.studentmarket.model;

import java.sql.Timestamp;


public class Admin extends User {

    public Admin(int id, String email, String password, String nama,
                 String nim, String noHp, StatusUser status, Timestamp createdAt) {
        super(id, email, password, nama, nim, noHp, status, createdAt);
    }

    public Admin(String email, String password, String nama, String nim, String noHp) {
        super(email, password, nama, nim, noHp);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    @Override
    public String getHakAkses() {
        return "Admin: mengelola semua pengguna dan semua barang";
    }

    @Override
    public String tampilkanInfo() {
        return "[ADMIN] " + super.tampilkanInfo();
    }
}
