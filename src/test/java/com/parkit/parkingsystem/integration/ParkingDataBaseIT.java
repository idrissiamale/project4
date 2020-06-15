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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingService parkingService;
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
    public void testUpdateParking() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpot.setAvailable(false);

        parkingSpotDAO.updateParking(parkingSpot);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }

    @Test
    public void testSaveTicket() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        String vehiculeRegNumber = "ABCDEF";
        Date inTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehiculeRegNumber);
        ticket.setPrice(0);

        ticketDAO.saveTicket(ticket);
        assertNotNull(ticketDAO.getTicket(vehiculeRegNumber));
    }

    @Test
    public void testParkingACar() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        String vehiculeRegNumber = "ABCDEF";
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehiculeRegNumber);
        ticket.setPrice(0);

        ticketDAO.saveTicket(ticket);
        assertNotNull(ticketDAO.getTicket(vehiculeRegNumber));
    }

    @Test
    public void testParkingACar2() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        parkingService.processIncomingVehicle();
        assertEquals(1, ticketDAO.countVehicleRegNumber("ABCDEF"));
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }


    @Test
    public void testParkingLotExit2() {
        testParkingACar();
        ticket = ticketDAO.getTicket("ABCDEF");
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.calculateFare(ticket);

        ticketDAO.updateTicket(ticket);
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
        assertEquals(1, ticketDAO.countVehicleRegNumber("ABCDEF"));
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.calculateFare(ticket);

        parkingService.processExitingVehicle();
        assertTrue(ticketDAO.updateTicket(ticket));
        assertEquals(1, ticketDAO.countVehicleRegNumber("ABCDEF"));
    }
}
