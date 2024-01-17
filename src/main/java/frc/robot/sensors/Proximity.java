package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    DigitalInput proximity = new DigitalInput(9);
    private PeriodicData periodicData;

    public Proximity()
    {   
        super("Proximity");
        System.out.println("  Constructor Started:  " + fullClassName);

        periodicData = new PeriodicData();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method returns true if sensor detects something.
     */
    public boolean getIsDetected()
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
        return null;
    }
}