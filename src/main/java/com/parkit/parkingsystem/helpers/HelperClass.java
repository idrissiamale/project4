package com.parkit.parkingsystem.helpers;

public class HelperClass {
    public static double getMinutes (double end, double start) {
        double durationInMinutes = (end - start)/60000;
        return durationInMinutes;
    }

    public static double getHours (double minutes) {
        double hours = minutes/60;
        return hours;
    }
}
