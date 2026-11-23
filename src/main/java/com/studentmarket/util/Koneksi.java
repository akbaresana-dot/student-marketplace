package com.studentmarket.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Koneksi {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Koneksi.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IllegalStateException("File application.properties tidak ditemukan");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Gagal membaca application.properties", e);
        }
    }

    private Koneksi() {
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password", ""));
    }

    public static String getUploadDir() {
        return PROPS.getProperty("app.upload-dir", "uploads");
    }
}
