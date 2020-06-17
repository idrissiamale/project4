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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingService parkingService;
    private static FareCalculatorService fareCalculatorService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private Ticket ticket;

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
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void checkThatATicketIsSavedInDataBase() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        Date inTime = new Date();
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
        assertNotNull(ticket.getPrice());
    }

    @Test
    public void checkThatParkingTableIsUpdatedWithAvailability() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingSpot.setAvailable(false);
        Date inTime = new Date();
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
    public void testParkingACar() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        Date inTime = new Date();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        parkingService.processIncomingVehicle();

        assertNotNull(ticketDAO.getTicket("ABCDEF"));
    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        ticket = ticketDAO.getTicket("ABCDEF");
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();

        assertTrue(ticketDAO.updateTicket(ticket));
        assertEquals(outTime, ticket.getOutTime());
        assertEquals(Fare.UNDER_THIRTY_MINUTES, ticket.getPrice());
    }

    @Test
    public void testParkingACarOfANewUserWithOneHourParkingTime() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        System.out.println("toto" + ticketDAO.countVehicleRegNumber("ABCDEF"));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        String vehiculeRegNumber = "ABCDEF";
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehiculeRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);

        ticketDAO.saveTicket(ticket);

        assertNotNull(ticketDAO.getTicket(vehiculeRegNumber));
    }

    @Test
    public void testParkingLotExitOfANewUserAfterOneHourParkingTime() {
        testParkingACarOfANewUserWithOneHourParkingTime();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticket = ticketDAO.getTicket("ABCDEF");
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();

        assertTrue(ticketDAO.updateTicket(ticket));
    }
    //TODO: check that the fare generated and out time are populated correctly in the database
}
