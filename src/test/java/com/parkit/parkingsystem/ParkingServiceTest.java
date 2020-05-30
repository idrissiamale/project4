package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;


    @Test
    public void testOfABCDEFvehicleRegNumber() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);

        int count = parkingService.countTheNumberOfVisits("ABCDEF");

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(9, count);
    }


    @Test
    public void testOfNullVehicleRegNumberException() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(ticketDAO.countVehicleRegNumber(null)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> parkingService.countTheNumberOfVisits(null));

        verify(ticketDAO).countVehicleRegNumber(null);
    }

    @Test
    public void testOfNonExistentVehicleRegNumber() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(ticketDAO.countVehicleRegNumber("FGHIJK")).thenReturn(0);

        int count = parkingService.countTheNumberOfVisits("FGHIJK");

        verify(ticketDAO).countVehicleRegNumber("FGHIJK");
        assertEquals(0, count);
    }

    @Test
    public void getVehiculeTypeCar(){
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        ParkingType parkingType = parkingService.getVehichleType();
        assertEquals(parkingType.CAR, parkingType);
    }

    @Test
    public void getVehiculeTypeBike(){
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        when(inputReaderUtil.readSelection()).thenReturn(2);

        ParkingType parkingType = parkingService.getVehichleType();
        assertEquals(parkingType.BIKE, parkingType);
    }

    @Test
    public void getVehiculeTypeError(){
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        when(inputReaderUtil.readSelection()).thenReturn(3).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () ->parkingService.getVehichleType());
    }





    @Test
    public void testGetNextAvailableSLotMethod() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        ParkingSpot available = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        assertEquals(parkingSpot, available);
    }

    @Test
    public void testGetNextAvailableSLotMethodBike() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        ParkingSpot available = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        assertEquals(parkingSpot, available);
    }

    @Test
    public void checkThatTheParkingSLotIsFull() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        when(inputReaderUtil.readSelection()).thenReturn(1).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);

        ParkingSpot available = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        assertEquals(null , available);
    }



    @Test
    public void processIncomingVehicleTestRecurringUser() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readSelection();
    }

    @Test
    public void processIncomingVehicleTestRecurringUser1() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void processIncomingVehicleTestRecurringUser2() throws Exception {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
    }

    @Test
    public void processIncomingVehicleTestRecurringUser3() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).countVehicleRegNumber(anyString());
    }

    @Test
    public void processIncomingVehicleTestRecurringUser4() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processIncomingVehicleTestRecurringUser5() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(9);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncomingVehicleWhenTheParkingIsFull() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));

    }

    @Test
    public void processIncomingVehicleForANewUser() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("FGHIJK");
            ticket.setInTime(inTime);


            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("FGHIJK");
            when(ticketDAO.countVehicleRegNumber("FGHIJK")).thenReturn(0);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).countVehicleRegNumber(anyString());
    }

    @Test
    public void processExitingVehicleTestGetVehiculeRegNumber() throws Exception {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
    }

    @Test
    public void processExitingVehicleTestGetTicket() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
    }





    @Test
    public void processExitingVehicleTestUpdateTicketTrue() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    }

    @Test
    public void processExitingVehicleTestUpdateTicketFalse() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    }

    @Test
    public void processExitingVehicleTestUpdateParkingSpot() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }
}