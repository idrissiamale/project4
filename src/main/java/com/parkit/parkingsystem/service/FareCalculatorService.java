package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.helpers.HelperClass;
import com.parkit.parkingsystem.model.Ticket;

/**
 * The system calculates fare based on the parking time and the vehicule type (car/bike).
 */
public class FareCalculatorService {
    private static HelperClass helper = new HelperClass();
    private static TicketDAO ticketDAO = new TicketDAO();

    public FareCalculatorService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    /**
     * Calculation of the parking fees.
     *
     * @param ticket, the ticket with the parking informations of the user.
     * @throws IllegalArgumentException, if the out time provided is incorrect.
     */
    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double durationInMinutes = helper.getMinutes(ticket.getOutTime().getTime(), ticket.getInTime().getTime());
        double duration = helper.getHours(durationInMinutes);
        String vehiculeRegNumber = ticket.getVehicleRegNumber();
        int numberOfVisits = ticketDAO.countVehicleRegNumber(vehiculeRegNumber);

        if (numberOfVisits > 1) {
            ticket.setPrice(this.getPriceWithDiscount(ticket.getParkingSpot().getParkingType(), duration));
        } else {
            ticket.setPrice(this.getPriceWithoutDiscount(ticket.getParkingSpot().getParkingType(), duration));
        }

        if (duration < 0.5) {
            ticket.setPrice(this.getPriceFree30MinParking(duration));
        }
    }

    /**
     * Calculation of the parking fees with discount.
     *
     * @param parkingType, it refers to the vehicule type (car/bike).
     * @param duration,    parking time. It's the elapsed time, given in hours, between parking entry and exit.
     * @return the price calculated based on the parking time, the vehicule type and the 5% discount.
     */
    private double getPriceWithDiscount(ParkingType parkingType, double duration) {
        switch (parkingType) {
            case CAR: {
                return helper.getTheRoundToThreeDecimalPlaces(duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
            }
            case BIKE: {
                return helper.getTheRoundToThreeDecimalPlaces(duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    /**
     * Calculation of the parking fees without discount.
     *
     * @param parkingType, it refers to the vehicule type (car/bike).
     * @param duration,    parking time. It's the elapsed time, given in hours, between parking entry and exit.
     * @return the price calculated based on the parking time and the vehicule type.
     */
    private double getPriceWithoutDiscount(ParkingType parkingType, double duration) {
        switch (parkingType) {
            case CAR: {
                return helper.getTheRoundToThreeDecimalPlaces(duration * Fare.CAR_RATE_PER_HOUR);
            }
            case BIKE: {
                return helper.getTheRoundToThreeDecimalPlaces(duration * Fare.BIKE_RATE_PER_HOUR);
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    /**
     * Free fees for parking under thirty minutes.
     *
     * @param duration, parking time. It's the elapsed time, given in hours, between parking entry and exit.
     * @return free parking fees under thirty minutes.
     */
    private double getPriceFree30MinParking(double duration) {
        return duration * Fare.UNDER_THIRTY_MINUTES;
    }
}
