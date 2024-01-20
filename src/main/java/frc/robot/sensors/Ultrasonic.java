package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Use this class as a template to create other sensors.
 */
public class Ultrasonic extends Sensor4237
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
        private double sensorVoltage;


        // OUTPUTS
        private double filteredMeasurement;
        private double sensorDistance;
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final AnalogInput sensor = new AnalogInput(1);

    private final MedianFilter m_filter = new MedianFilter(5);
    private boolean skipChecker = false;
    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Ultrasonic sensor. 
     */
    public Ultrasonic()
    {   
        super("Ultrasonic");
        System.out.println("  Constructor Started:  " + fullClassName);

        periodicData.sensorDistance = calculateDistance();
        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here
    private double calculateDistance()
    {
        var voltSensor = sensor.getVoltage();
        SmartDashboard.putNumber("Sensor Voltage", voltSensor);
        var voltsPerCM = edu.wpi.first.wpilibj.RobotController.getVoltage5V()/5.12;
        SmartDashboard.putNumber("RoboRio Voltage", voltsPerCM);
        periodicData.sensorVoltage = voltSensor/voltsPerCM/30.48;
        periodicData.filteredMeasurement = m_filter.calculate(periodicData.sensorVoltage);
        return periodicData.filteredMeasurement; 
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public double getDistance()
    {
        periodicData.sensorDistance = calculateDistance();
        return periodicData.sensorDistance;
    }
    

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void readPeriodicInputs() 
    {
        periodicData.sensorValue = sensor.getAverageVoltage();
        if(!skipChecker)
        {
            double temp = calculateDistance();
            SmartDashboard.putNumber("Temp Distance", temp);
            if(temp < 12.0)
            {
                periodicData.sensorDistance = temp;
                skipChecker = !skipChecker;
            }
        }
        else
        {
            skipChecker = !skipChecker;
        }
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
        return String.format("Ultrasonic %f \n", periodicData.sensorDistance);
    }
}