package com.studentmarket.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class Format {

    private Format() {
    }

    /** 1500000 -> "Rp 1.500.000" */
    public static String rupiah(double nilai) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setGroupingSeparator('.');
        s.setDecimalSeparator(',');
        return "Rp " + new DecimalFormat("#,##0", s).format(nilai);
    }
}
