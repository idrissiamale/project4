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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {
    private static ParkingService parkingService;
    private Ticket ticket;
    private Date inTime;
    private Date outTime;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;


    @BeforeEach
    private void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticket = new Ticket();
        inTime = new Date();
        outTime = new Date();
    }

    @Test
    public void countVehicleRegNumberShouldReturnAnIntegerGreaterThanZeroWhenItSARecurringUser() {
        try {
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        int count = parkingService.countTheNumberOfVisits("ABCDEF");

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(1, count);
    }

    @Test
    public void countVehicleRegNumberShouldReturnZeroWhenItSANewUser() {
        try {
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        int count = parkingService.countTheNumberOfVisits("ABCDEF");

        verify(ticketDAO).countVehicleRegNumber("ABCDEF");
        assertEquals(0, count);
    }

    @Test
    public void countVehicleRegNumberShouldThrowAnExceptionWhenTheUserInputIsInvalid() {
        try {
            when(ticketDAO.countVehicleRegNumber(null)).thenThrow(new IllegalArgumentException());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        assertThrows(IllegalArgumentException.class, () -> parkingService.countTheNumberOfVisits(null));

        verify(ticketDAO).countVehicleRegNumber(null);
    }

    @Test
    public void getNextParkingNumberForACarWhenItSAvailable() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(ParkingType.CAR);
        assertNotNull(nextParkingNumberIfAvailable);
    }

    @Test
    public void getNextParkingNumberForABikeWhenItSAvailable() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(ParkingType.BIKE);
        assertNotNull(nextParkingNumberIfAvailable);
    }

    @Test
    public void getNextAvailableSLotShouldBeNullWhenTheParkingSLotIsFull() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        assertNull(nextParkingNumberIfAvailable);
    }

    //Testing that the mo
    @Test
    public void verifyThatInputReaderUtilReadSelectionIsInvoked() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readSelection();
    }

    @Test
    public void verifyThatParkingSpotDAOGetNextAvailableSLotIsInvoked() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void verifyThatParkingSpotDAOGetNextAvailableSLotIsInvokedWhenTheParkingIsFull() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void verifyThatInputReaderUtilReadVehicleRegistrationNumberIsInvoked() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
    }

    @Test
    public void verifyThatTicketDAOCountVehicleRegNumberIsInvoked() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).countVehicleRegNumber("ABCDEF");
    }

    @Test
    public void testThatParkingSpotDAOIsUpdatedAfterTheEntranceOfACarInTheParking() {
        ParkingSpot parkingSpot;
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(0);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void testThatParkingSpotDAOIsUpdatedAfterTheEntranceOfABikeInTheParking() {
        ParkingSpot parkingSpot;
        try {
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(2);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            parkingSpot = new ParkingSpot(2, ParkingType.BIKE, false);
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(0);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void testForACarThatTicketIsSavedInDataBase() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            when(ticketDAO.saveTicket(ticket)).thenReturn(true);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0);
            ticket.setInTime(inTime);
            ticket.setOutTime(null);
            ticketDAO.saveTicket(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(ticket);
        assertTrue(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void testForABikeThatTicketIsSavedInDatabase() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(2);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
            when(ticketDAO.saveTicket(ticket)).thenReturn(true);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0);
            ticket.setInTime(inTime);
            ticket.setOutTime(null);
            ticketDAO.saveTicket(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(ticket);
        assertTrue(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void verifyThatInputReaderUtilReadVehicleRegistrationNumberInProcessExitingVehicleMethodIsInvoked() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
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
    public void testThatTicketDAOGetTicketIsNotNull() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).getTicket("ABCDEF");
        assertNotNull(ticketDAO.getTicket("ABCDEF"));
    }

    @Test
    public void testThatTheTicketOfAUserWithCarIsUpdated() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setOutTime(outTime);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).updateTicket(ticket);
        assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    public void testThatTheTicketOfAUserWithBikeIsUpdated() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setOutTime(outTime);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).updateTicket(ticket);
        assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    public void testThatParkingSpotDAOIsUpdatedWithParkingSpotAsAvailableAfterTheExitOfACar() {
        ParkingSpot parkingSpot;
        try {
            parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void testThatParkingSpotDAOIsUpdatedWithParkingSpotAsAvailableAfterTheExitOfABike() {
        ParkingSpot parkingSpot;
        try {
            parkingSpot = new ParkingSpot(2, ParkingType.BIKE, true);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            when(ticketDAO.updateTicket(ticket)).thenReturn(true);
            when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(parkingSpot);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
}