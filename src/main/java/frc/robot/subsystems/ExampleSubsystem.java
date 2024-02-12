package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.util.sendable.SendableRegistry;
import frc.robot.Constants;
import frc.robot.motors.TalonFX4237;

/**
 * Use this class as a template to create other subsystems.
 */
public class ExampleSubsystem extends Subsystem4237
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

        // OUTPUTS
        private double speed;
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final TalonFX4237 motor1 = new TalonFX4237(4, Constants.ROBORIO, "Motor 1");
    private final TalonFX4237 motor2 = new TalonFX4237(12, Constants.ROBORIO, "Motor 2");


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public ExampleSubsystem()
    {
        super("Example Subsystem");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();

        SendableRegistry.addLW(this, "Example Subsystem", "MY Subsystem");
        addChild("Motor 1", motor1);
        addChild("Motor 2", motor2);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configMotors()
    {
        motor1.setupFactoryDefaults();
        motor2.setupFactoryDefaults();
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void set(double speed)
    {
        periodicData.speed = speed;
    }

    public void stop()
    {
        periodicData.speed = 0.0;
    }


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here
    @Override
    public void readPeriodicInputs()
    {

    }

    @Override
    public void writePeriodicOutputs()
    {
        motor1.set(periodicData.speed);
        motor2.set(periodicData.speed);
    }

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }

    @Override
    public String toString()
    {
        return "";
    }
}
