package com.studentmarket.service;

import com.studentmarket.model.DaftarFavorit;
import com.studentmarket.model.User;


public final class Sesi {

    private static User user;
    private static DaftarFavorit favorit;   

    private Sesi() {
    }

    public static User getUser() {
        return user;
    }

    public static DaftarFavorit getFavorit() {
        return favorit;
    }

    public static void masuk(User u) {
        user = u;
        favorit = new DaftarFavorit(u);      
    }

    public static void keluar() {
        user = null;
        favorit = null;
    }
}
