package com.studentmarket.model;

import java.sql.Timestamp;

public abstract class User {

    private int id;
    private String email;
    private String password;
    private String nama;
    private String nim;
    private String noHp;
    private StatusUser status;
    private Timestamp createdAt;

    protected User(int id, String email, String password, String nama,
                   String nim, String noHp, StatusUser status, Timestamp createdAt) {
        this.id = id;
        setEmail(email);
        setPassword(password);
        setNama(nama);
        setNim(nim);
        setNoHp(noHp);
        setStatus(status);
        this.createdAt = createdAt;
    }

        protected User(String email, String password, String nama, String nim, String noHp) {
        this(0, email, password, nama, nim, noHp, StatusUser.AKTIF, null);
    }

    
    public abstract Role getRole();

    public abstract String getHakAkses();

    
    public boolean isAdmin() {
        return getRole() == Role.ADMIN;
    }

    public String tampilkanInfo() {
        return nama + " (" + email + ")";
    }

    
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public String getNoHp() { return noHp; }
    public StatusUser getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }

    
    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
        if (email.trim().length() > 100) {
            throw new IllegalArgumentException("Email maksimal 100 karakter");
        }
        this.email = email.trim().toLowerCase();
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }
        this.password = password;
    }

    public void setNama(String nama) {
        String n = (nama == null) ? "" : nama.trim();
        if (n.length() > 100) {
            throw new IllegalArgumentException("Nama maksimal 100 karakter");
        }
        this.nama = n;
    }

    public void setNim(String nim) {
        String n = (nim == null) ? "" : nim.trim();
        if (n.length() > 20) {
            throw new IllegalArgumentException("NIM maksimal 20 karakter");
        }
        this.nim = n;
    }

    public void setNoHp(String noHp) {
        String n = (noHp == null) ? "" : noHp.trim();
        if (n.length() > 20) {
            throw new IllegalArgumentException("No HP maksimal 20 karakter");
        }
        for (int i = 0; i < n.length(); i++) {
            if ("0123456789+- ".indexOf(n.charAt(i)) < 0) {
                throw new IllegalArgumentException("No HP hanya boleh berisi angka");
            }
        }
        this.noHp = n;
    }

    public void setStatus(StatusUser status) {
        if (status == null) {
            throw new IllegalArgumentException("Status tidak boleh kosong");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return tampilkanInfo();
    }
}
