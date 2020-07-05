package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import static com.parkit.parkingsystem.helpers.HelperClass.getHours;
import static com.parkit.parkingsystem.helpers.HelperClass.getTheRoundToThreeDecimalPlaces;

/**
 * The system calculates fare based on the parking time and the vehicle type (car/bike).
 */
public class FareCalculatorService {
    private TicketDAO ticketDAO;

    public FareCalculatorService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    /**
     * Calculation of the parking fees.
     *
     * @param ticket, the ticket with the parking information of the user.
     * @throws IllegalArgumentException, if the out time provided is incorrect.
     * @see TicketDAO
     */
    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double duration = getHours(ticket.getOutTime().getTime(), ticket.getInTime().getTime());
        String vehicleRegNumber = ticket.getVehicleRegNumber();
        int numberOfVisits = ticketDAO.countVehicleRegNumber(vehicleRegNumber);

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
     * @param parkingType, the parking type based on the user's vehicle (car or bike).
     * @param duration,    parking time. It's the elapsed time, given in hours, between parking entry and exit.
     * @return the price calculated based on the parking time, the vehicle type and the 5% discount.
     */
    private double getPriceWithDiscount(ParkingType parkingType, double duration) {
        switch (parkingType) {
            case CAR: {
                return getTheRoundToThreeDecimalPlaces(duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
            }
            case BIKE: {
                return getTheRoundToThreeDecimalPlaces(duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    /**
     * Calculation of the parking fees without discount.
     *
     * @param parkingType, the parking type based on the user's vehicle (car or bike).
     * @param duration,    parking time. It's the elapsed time, given in hours, between parking entry and exit.
     * @return the price calculated based on the parking time and the vehicle type.
     */
    private double getPriceWithoutDiscount(ParkingType parkingType, double duration) {
        switch (parkingType) {
            case CAR: {
                return getTheRoundToThreeDecimalPlaces(duration * Fare.CAR_RATE_PER_HOUR);
            }
            case BIKE: {
                return getTheRoundToThreeDecimalPlaces(duration * Fare.BIKE_RATE_PER_HOUR);
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
