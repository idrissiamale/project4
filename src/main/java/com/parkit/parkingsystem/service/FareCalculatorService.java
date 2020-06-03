package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.helpers.HelperClass;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    private static HelperClass helper = new HelperClass();
    public TicketDAO ticketDAO = new TicketDAO();

    public void calculateFare(Ticket ticket, String vehiculeRegNumber) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double durationInMinutes = helper.getMinutes(ticket.getOutTime().getTime(), ticket.getInTime().getTime());
        double duration = helper.getHours(durationInMinutes);

        int numberOfVisits = ticketDAO.countVehicleRegNumber(vehiculeRegNumber);
        if (numberOfVisits > 0) {
            ticket.setPrice(this.getPriceWithDiscount(ticket.getParkingSpot().getParkingType(), duration));
        } else {
            ticket.setPrice(this.getPriceWithoutDiscount(ticket.getParkingSpot().getParkingType(), duration));
        }

        if (duration < 0.5) {
            ticket.setPrice(this.getPriceFree30MinParking(duration));
        }
    }

    private double getPriceWithDiscount(ParkingType parkingType, double duration) {
        switch (parkingType) {
            case CAR: {
                return duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT;
            }
            case BIKE: {
                return duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    private double getPriceWithoutDiscount(ParkingType parkingType, double duration) {
        switch (parkingType) {
            case CAR: {
                return duration * Fare.CAR_RATE_PER_HOUR;
            }
            case BIKE: {
                return duration * Fare.BIKE_RATE_PER_HOUR;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    private double getPriceFree30MinParking(double duration) {
        return duration * Fare.UNDER_THIRTY_MINUTES;
    }
}
