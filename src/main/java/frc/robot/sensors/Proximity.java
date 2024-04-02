package frc.robot.sensors;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

/**Represents a Sharp infrared proximity switch. */
public class Proximity extends Sensor4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class PeriodicData
    {
        // INPUTS
        private boolean value;
        private boolean isDetected;

        // OUTPUTS
    }

    private final DigitalInput proximity;
    private PeriodicData periodicData = new PeriodicData();
    
    public Proximity(int digitalInputPort)
    {   
        super("Proximity");
        System.out.println("  Constructor Started:  " + fullClassName);

        proximity = new DigitalInput(digitalInputPort);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method returns true if sensor detects something.
     */
    public boolean isDetected()
    {
        return periodicData.isDetected;
    }

    /**
     * This method gets the raw value from the proximity sensor. Use isDetected instead.
     */
    public boolean getValue()
    {
        return periodicData.value;
    }

    public BooleanSupplier isDetectedSupplier()
    {
        return () -> isDetected();
    }

    public BooleanSupplier isNotDetectedSupplier()
    {
        return () -> !isDetected();
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicData.value = proximity.get();
        periodicData.isDetected = !periodicData.value;
    }

    @Override
    public void writePeriodicOutputs() 
    {
        SmartDashboard.putBoolean("isDetected", periodicData.isDetected);
    }

    @Override
    public void runPeriodicTask()
    {

    }

    @Override
    public String toString()
    {
        return "Is detected: " + isDetected();
    }
}