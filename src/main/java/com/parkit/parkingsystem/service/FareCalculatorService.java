package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Long inTime = ticket.getInTime().getTime();
        Long outTime = ticket.getOutTime().getTime();






        //TODO: Some tests are failing here. Need to check if this logic is correct

        Long durationMinutes = ((outTime - inTime)/ 60000); // j'ai transformé les milliseconds en minutes

        Long duration = (durationMinutes/60); // j'ai divisé les minutes par 60 pour obtenir des heures. Ainsi pour convertir 45mn en heure
                                            // je fais 45/60 ce qui me donne 0,75. Mais je ne comprends alors que 0,75*1,5€ = 1,125€ j'obtiens 0€ sur les tests.



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



    }
}