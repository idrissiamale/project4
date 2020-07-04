package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import java.util.Date;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingService parkingService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private Ticket ticket;
    private Date inTime;
    private Date outTime;

    @Mock
    private static InputReaderUtil inputReaderUtil;


    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        ticket = new Ticket();
        inTime = new Date();
        outTime = new Date();
        dataBasePrepareService.clearDataBaseEntries();
    }


    @Test
    public void checkForACarThatATicketIsSavedInDataBase() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();

        assertNotNull(ticketDAO.getTicket("ABCDEF"));
        assertNotNull(ticket.getParkingSpot());
        assertNotNull(ticket.getVehicleRegNumber());
        assertNotNull(ticket.getPrice());
        assertNotNull(ticket.getInTime());
    }

    @Test
    public void checkForABikeThatATicketIsSavedInDataBase() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(2);
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();

        assertNotNull(ticketDAO.getTicket("ABCDEF"));
        assertNotNull(ticket.getParkingSpot());
        assertNotNull(ticket.getVehicleRegNumber());
        assertNotNull(ticket.getPrice());
        assertNotNull(ticket.getInTime());
    }

    @Test
    public void checkForACarThatParkingTableIsUpdatedWithAvailability() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingSpot.setAvailable(false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
        assertFalse(ticket.getParkingSpot().isAvailable());
    }

    @Test
    public void checkForABikeThatParkingTableIsUpdatedWithAvailability() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(2);
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingSpot.setAvailable(false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
        assertFalse(ticket.getParkingSpot().isAvailable());
    }

    //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

    @Test
    public void parkingACar() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();
    }

    @Test
    public void testParkingLotExitOfACar() {
        parkingACar();
        ticketDAO.getTicket("ABCDEF");
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();

        assertTrue(ticketDAO.updateTicket(ticket));
        assertNotNull(ticket.getOutTime());
        assertNotNull(ticket.getPrice());
    }

    @Test
    public void parkingABike() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();
    }

    @Test
    public void testParkingLotExitOfABike() {
        parkingABike();
        ticketDAO.getTicket("ABCDEF");
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();

        assertTrue(ticketDAO.updateTicket(ticket));
        assertNotNull(ticket.getOutTime());
        assertNotNull(ticket.getPrice());
    }

    @Test
    public void testParkingACarWithOneHourParkingTime() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        ticketDAO.saveTicket(ticket);
    }

    @Test
    public void testParkingLotExitOfACarAfterOneHourParkingTime() {
        testParkingACarWithOneHourParkingTime();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticketDAO.getTicket("ABCDEF");
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();

        assertTrue(ticketDAO.updateTicket(ticket));
        assertEquals(outTime, ticket.getOutTime());
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void testParkingABikeWithOneHourParkingTime() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        ticketDAO.saveTicket(ticket);
    }

    @Test
    public void testParkingLotExitOfABikeAfterOneHourParkingTime() {
        testParkingABikeWithOneHourParkingTime();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticketDAO.getTicket("ABCDEF");
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();

        assertTrue(ticketDAO.updateTicket(ticket));
        assertEquals(outTime, ticket.getOutTime());
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }
    //TODO: check that the fare generated and out time are populated correctly in the database
}
