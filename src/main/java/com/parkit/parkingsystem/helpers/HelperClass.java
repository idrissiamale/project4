package com.parkit.parkingsystem.helpers;

import java.math.BigDecimal;

/**
 * A class whose methods are used to convert parking time into hours and to round fees to three decimal places.
 */
public class HelperClass {
    /**
     * Helper method which permits to convert the elapsed time into minutes.
     *
     * @param end,   it refers to the ending time.
     * @param start, it refers to the starting time.
     * @return durationInMinutes which is the difference between the ending time and the starting time in minutes.
     * @see com.parkit.parkingsystem.service.FareCalculatorService
     */
    public static double getMinutes(double end, double start) {
        double durationInMinutes = (end - start) / 60000;
        return durationInMinutes;
    }

    /**
     * Helper method which permits to convert the elapsed time into hours.
     *
     * @param minutes, it refers to the time in minutes.
     * @return hours, it returns the time in hours.
     * @see com.parkit.parkingsystem.service.FareCalculatorService
     */
    public static double getHours(double minutes) {
        double hours = minutes / 60;
        return hours;
    }

    /**
     * Helper method which permits to round a number to three decimal places.
     *
     * @param n, the number to round.
     * @return n, it returns the number rounded to three decimal places.
     * @see com.parkit.parkingsystem.service.FareCalculatorService
     */
    public static double getTheRoundToThreeDecimalPlaces(double n) {
        BigDecimal bd = new BigDecimal(n);
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_DOWN);
        n = bd.doubleValue();
        return n;
    }
}
