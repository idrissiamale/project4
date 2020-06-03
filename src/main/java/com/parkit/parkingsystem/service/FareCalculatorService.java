package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.helpers.HelperClass;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

public class FareCalculatorService {
    private static HelperClass helper = new HelperClass();
    public TicketDAO ticketDAO = new TicketDAO();

    private double inTime;
    private double outTime;
    private double durationInMinutes;
    private double duration;
    private int numberOfVisits;



    public void calculateFare(Ticket ticket, String vehiculeRegNumber) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        inTime = ticket.getInTime().getTime();
        outTime = ticket.getOutTime().getTime();

        durationInMinutes = helper.getMinutes(outTime, inTime);
        duration = helper.getHours(durationInMinutes);

        numberOfVisits = ticketDAO.countVehicleRegNumber(vehiculeRegNumber);
        if(numberOfVisits > 0) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }

        if (duration < 0.5) {
            ticket.setPrice(duration * Fare.UNDER_THIRTY_MINUTES);
        }

    }



    public int checkIfItIsARecurringUser(Ticket ticket, String vehiculeRegNumber) {
        int numberOfVisits = 0;
        numberOfVisits = ticketDAO.countVehicleRegNumber(vehiculeRegNumber);
        if(numberOfVisits > 0) {
            calculateFareForARecurringUser(ticket);
        } else {
            calculateFare(ticket, vehiculeRegNumber);
        }
        return numberOfVisits;
    }

    public void calculateFareForARecurringUser(Ticket ticket) {
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}