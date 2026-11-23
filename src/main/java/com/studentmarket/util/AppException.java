package com.studentmarket.util;


public class AppException extends RuntimeException {
    public AppException(String pesan) {
        super(pesan);
    }

    public AppException(String pesan, Throwable penyebab) {
        super(pesan, penyebab);
    }
}
