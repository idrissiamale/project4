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

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
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
        ParkingSpot parkingSpot;
        try {
            parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        assertEquals(parkingSpot, nextParkingNumberIfAvailable);
    }

    @Test
    public void getNextParkingNumberForABikeWhenItSAvailable() {
        ParkingSpot parkingSpot;
        try {
            parkingSpot = new ParkingSpot(2, ParkingType.BIKE, false);
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        ParkingSpot nextParkingNumberIfAvailable = parkingService.getNextParkingNumberIfAvailable();

        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        assertEquals(parkingSpot, nextParkingNumberIfAvailable);
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

    @Test
    public void verifyThatInputReaderUtilReadSelectionInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
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
    public void testThatParkingSpotDAOGetNextAvailableSLotInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
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
    public void testWhenTheParkingIsFullThatParkingSpotDAOGetNextAvailableSLotInProcessIncomingMethodIsCalled() {
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
    public void testThatInputReaderUtilReadVehicleRegistrationNumberInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
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
    public void testWhenItSARecurringUserThatTicketDAOCountVehicleRegNumberInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
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
    public void testWhenItSANewUserThatTicketDAOCountVehiculeRegNumberInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(0);
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
    public void testThatParkingSpotDAOUpdateParkingInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
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
    public void testThatTicketDAOSaveTicketInProcessIncomingVehicleMethodIsCalled() {
        try {
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setInTime(inTime);
            when(inputReaderUtil.readSelection()).thenReturn(1, 2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.countVehicleRegNumber("ABCDEF")).thenReturn(1);
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
    public void testThatInputReaderUtilReadVehicleRegistrationNumberInProcessExitingVehicleMethodIsCalled() {
        try {
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
    public void testThatTicketDAOGetTicketInProcessExitingVehicleMethodIsCalled() {
        try {
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
    public void testThatTicketDAOUpdateTicketInProcessExitingVehicleMethodIsCalled() {
        try {
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
    public void testThatWhenTicketDAOUpdateTicketIsFalseInProcessExitingVehicleMethodWeGetAnErrorMessage() {
        try {
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
    public void testThatParkingSpotDAOUpdateParkingInProcessExitingVehicleMethodIsCalled() {
        try {
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