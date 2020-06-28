package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.helpers.HelperClass;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private static TicketDAO ticketDAO = new TicketDAO();
    private static HelperClass helper = new HelperClass();
    private Ticket ticket;
    private Date inTime;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService(ticketDAO);
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        inTime = new Date();
    }

    @Test
    public void calculateFareCarWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.75 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.75 * Fare.BIKE_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTimeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(24 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTimeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(24 * Fare.BIKE_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithThirtyMinutesParkingTimeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.5 * Fare.CAR_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithThirtyMinutesParkingTimeWithoutDiscount() {
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("FGHIJK");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.5 * Fare.BIKE_RATE_PER_HOUR)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals(helper.getTheRoundToThreeDecimalPlaces(Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals(helper.getTheRoundToThreeDecimalPlaces(Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTimeWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.75 * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeWithDiscount() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.75 * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTimeWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(24 * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTimeWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(24 * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithThirtyMinutesParkingTimeWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.5 * Fare.CAR_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithThirtyMinutesParkingTimeWithDiscount() {
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        fareCalculatorService.calculateFare(ticket);

        assertEquals((helper.getTheRoundToThreeDecimalPlaces(0.5 * Fare.BIKE_RATE_PER_HOUR * Fare.FIVE_PERCENT_DISCOUNT)), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
        inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));
        Date outTime = new Date();
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
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.25 * Fare.UNDER_THIRTY_MINUTES), ticket.getPrice());
    }

    @Test
    public void calculateFareUnkownType() {
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareCarWithFutureInTime() {
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
}
