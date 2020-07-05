package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Parking system. Process with incoming and exiting vehicles.
 */
public class ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private TicketDAO ticketDAO;

    private FareCalculatorService fareCalculatorService;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.fareCalculatorService = new FareCalculatorService(ticketDAO);
    }

    /**
     * Process incoming vehicles and issuing of parking tickets.
     *
     * @see ParkingSpotDAO
     * @see TicketDAO
     */
    public void processIncomingVehicle() {
        
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehichleRegNumber();
                countTheNumberOfVisits(vehicleRegNumber);
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false

                Date inTime = new Date();
                Ticket ticket = new Ticket();
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                //ticket.setId(ticketID);
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
            }

    }

    /**
     * The system gets the vehicle registration number entered by the user.
     *
     * @return the user input.
     * @see InputReaderUtil
     */
    private String getVehichleRegNumber() {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * It counts the vehicle registration number entered by the user to determine whether he's a new or a recurring user.
     *
     * @param vehicleRegNumber, it refers to the vehicle registration number entered by the user.
     * @return the number of visits. If it's greater than zero, it means that the vehicle registration number
     * exists in our database and that the user is a recurring user.
     * @see TicketDAO
     */
    public int countTheNumberOfVisits(String vehicleRegNumber) {
        int numberOfVisits = ticketDAO.countVehicleRegNumber(vehicleRegNumber);
        if (numberOfVisits > 0) {
            System.out.println("Welcome back ! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
        }
        return numberOfVisits;
    }

    /**
     * The system searches for an available parking spot based on the vehicle type selected by the user
     * and displays the parking number if it's available.
     *
     * @return the available parking spot.
     * @throws Exception if the parking lots are full.
     * @see ParkingSpotDAO
     */
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehichleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            logger.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * The system gets the vehicle type (car or bike) selected by the user.
     *
     * @return the user selection.
     * @throws IllegalArgumentException if the input entered is invalid and does not match any vehicle type of the menu.
     * @see InputReaderUtil
     */
    private ParkingType getVehichleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     * Process exiting vehicles and calculation of the parking fares.
     *
     * @see TicketDAO
     * @see FareCalculatorService
     * @see ParkingSpotDAO
     */
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehichleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
        }
    }
}
