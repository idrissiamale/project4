package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.helpers.HelperClass;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

public class FareCalculatorService {
    private static HelperClass helper = new HelperClass();
    private TicketDAO ticketDAO = new TicketDAO();


    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inTime = ticket.getInTime().getTime();
        double outTime = ticket.getOutTime().getTime();








        //TODO: Some tests are failing here. Need to check if this logic is correct

        double durationInMinutes = helper.getMinutes(outTime, inTime); // j'ai transformé les milliseconds en minutes
        double duration = helper.getHours(durationInMinutes); // j'ai divisé les minutes par 60 pour obtenir des heures. Ainsi pour convertir 45mn en heure
                                            // je fais 45/60 ce qui me donne 0,75. Mais je ne comprends alors que 0,75*1,5€ = 1,125€ j'obtiens 0€ sur les tests.



        int numberOfVisits = ticketDAO.countVehicleRegNumber("");
        if(numberOfVisits > 0) {
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }


        if (duration < 0.5) {
            ticket.setPrice(duration * Fare.UNDER_THIRTY_MINUTES);
        }




    }
}