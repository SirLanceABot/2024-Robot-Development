package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import frc.robot.PeriodicIO;
import frc.robot.PeriodicTask;

/**
 * This abstract class will be extended for every sensor on the robot. 
 * Every sensor will automatically be added to the array list for periodic inputs and outputs.
 */
abstract class Sensor4237 implements PeriodicIO, PeriodicTask
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS CONSTRUCTOR ***
    Sensor4237(String sensorName)
    {
        super();

        System.out.println("  Constructor Started:  " + fullClassName + " >> " + sensorName);

        // Register this subsystem in the array list for periodic inputs and outputs.
        registerPeriodicIO();
        registerPeriodicTask();

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + sensorName);
    }

    // Abstract methods to override in subclasses
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();
    public abstract void runPeriodicTask();
}
