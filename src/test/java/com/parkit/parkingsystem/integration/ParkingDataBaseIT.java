package com.parkit.parkingsystem.integration;

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
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;


    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
    }

    @AfterAll
    private static void tearDown(){
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();
    }




    @Test
    public void testParkingACar() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpot.setAvailable(false);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        parkingService.processIncomingVehicle();

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    public void testParkingACar1() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpot.setAvailable(false);
        parkingSpotDAO.updateParking(parkingSpot);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);


        parkingService.processIncomingVehicle();

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
        assertFalse(ticketDAO.saveTicket(ticket));
        assertNotNull(ticketDAO.getTicket("ABCDEF"));
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    public void testParkingLotExit() throws Exception {
        testParkingACar1();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Date outTime = new Date();
        ticket.getInTime();
        ticket.setOutTime(outTime);
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.calculateFare(ticket);
        parkingService.processExitingVehicle();
        assertTrue(ticketDAO.updateTicket(ticket));
        //TODO: check that the fare generated and out time are populated correctly in the database
    }
}
