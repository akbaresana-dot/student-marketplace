package com.studentmarket.model;

import java.util.ArrayList;
import java.util.List;

public class DaftarFavorit {

    private User pemilik;                                  
    private List<Listing> daftar = new ArrayList<>();      

    public DaftarFavorit(User pemilik) {
        this.pemilik = pemilik;
    }

   
    public boolean tambah(Listing barang) {
        if (ada(barang)) {
            return false;
        }
        daftar.add(barang);
        return true;
    }

   
    public void hapus(Listing barang) {
        for (int i = 0; i < daftar.size(); i++) {
            if (daftar.get(i).getId() == barang.getId()) {
                daftar.remove(i);
                return;
            }
        }
    }

    
    public boolean ada(Listing barang) {
        for (Listing l : daftar) {
            if (l.getId() == barang.getId()) {
                return true;
            }
        }
        return false;
    }

    public int jumlah() {
        return daftar.size();
    }

    public User getPemilik() {
        return pemilik;
    }

    public List<Listing> getDaftar() {
        return daftar;
    }
}
