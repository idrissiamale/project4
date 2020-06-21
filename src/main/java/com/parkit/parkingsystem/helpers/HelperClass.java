package com.parkit.parkingsystem.helpers;

import java.math.BigDecimal;

public class HelperClass {
    public static double getMinutes(double end, double start) {
        double durationInMinutes = (end - start) / 60000;
        return durationInMinutes;
    }

    public static double getHours(double minutes) {
        double hours = minutes / 60;
        return hours;
    }

    public static double arrondiDecimales(double a) {
        BigDecimal bd = new BigDecimal(a);
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_DOWN);
        a = bd.doubleValue();
        return a;
    }
}
