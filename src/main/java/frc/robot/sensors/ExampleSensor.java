package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Use this class as a template to create other sensors.
 */
public class ExampleSensor extends Sensor4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    // Put all inner enums and inner classes here
    private class PeriodicData
    {
        // INPUTS
        private double sensorValue;

        // OUTPUTS
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final AnalogInput sensor = new AnalogInput(0);


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public ExampleSensor()
    {   
        super("Example Sensor");
        System.out.println("  Constructor Started:  " + fullClassName);


        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public double getSensorValue()
    {
        return periodicData.sensorValue;
    }
    

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void readPeriodicInputs() 
    {
        periodicData.sensorValue = sensor.getAverageVoltage();
    }

    @Override
    public void writePeriodicOutputs() 
    {
        SmartDashboard.putNumber("Analog Input", periodicData.sensorValue);
    }

    @Override
    public void runPeriodicTask()
    {

    }

    @Override
    public String toString()
    {
        return "";
    }
}