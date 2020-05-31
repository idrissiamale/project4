package com.parkit.parkingsystem;


import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;



public class InteractiveShellTest {

    private static InteractiveShell interactiveShell;
    private InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        interactiveShell = new InteractiveShell();
    }

    @BeforeEach
    private void setUpPerTest() {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    public void getVehiculeTypeCar(){


    }
}
