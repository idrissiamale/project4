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

    public static double getTheRoundToThreeDecimalPlaces(double n) {
        BigDecimal bd = new BigDecimal(n);
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_DOWN);
        n = bd.doubleValue();
        return n;
    }
}
