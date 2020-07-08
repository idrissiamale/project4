package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.parkit.parkingsystem.helpers.HelperClass.getTheRoundToThreeDecimalPlaces;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;

    @Mock
    private static TicketDAO ticketDAO;

    private Ticket ticket;
    private Date inTime;
    private Date outTime;

    @BeforeEach
    private void setUpPerTest() {
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        ticket = new Ticket();
        inTime = new Date();
        outTime = new Date();
    }

    ////Tests which check that normal parking fees (without discount) based on the parking time and the vehicle type are correctly calculated.
    @Test
    public void calculateFareCarWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.75 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.75 * Fare.BIKE_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTimeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(24 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTimeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(24 * Fare.BIKE_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithThirtyMinutesParkingTimeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
            outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.5 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithThirtyMinutesParkingTimeWithoutDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
            outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.5 * Fare.BIKE_RATE_PER_HOUR)), ticket.getPrice());
    }

    //Tests which check that parking fees with discount and based on the parking time and the vehicle type are correctly calculated.
    @Test
    public void calculateFareCarWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(getTheRoundToThreeDecimalPlaces(Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(getTheRoundToThreeDecimalPlaces(Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.75 * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.75 * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTimeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(24 * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTimeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(24 * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithThirtyMinutesParkingTimeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
            outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.5 * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithThirtyMinutesParkingTimeWithDiscount() {
        try {
            inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
            outTime = new Date();
            ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        fareCalculatorService.calculateFare(ticket);

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals((getTheRoundToThreeDecimalPlaces(0.5 * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    //
    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
        inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.25 * Fare.UNDER_THIRTY_MINUTES), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThanThirtyMinutesParkingTime() {
        inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.25 * Fare.UNDER_THIRTY_MINUTES), ticket.getPrice());
    }

    //Tests which check that exceptions are thrown when the vehicle type is an unknown type or the out time provided is incorrect.
    @Test
    public void calculateFareUnknownType() {
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareCarWithFutureInTime() {
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
}
