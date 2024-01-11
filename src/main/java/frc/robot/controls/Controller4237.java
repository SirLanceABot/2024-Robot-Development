package frc.robot.controls;

import java.lang.invoke.MethodHandles;

import frc.robot.PeriodicIO;
import frc.robot.PeriodicTask;

/**
 * This abstract class will be extended for every subsystem on the robot. 
 * Every subsystem will automatically be added to the array list for periodic inputs and outputs.
 */
abstract class Controller4237 implements PeriodicIO, PeriodicTask
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
    Controller4237()
    {
        super();

        System.out.println("  Constructor Started:  " + fullClassName);

        // Register this subsystem in the array list for periodic inputs and outputs.
        registerPeriodicIO();
        registerPeriodicTask();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // Abstract methods to override in subclasses
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();
    public abstract void runPeriodicTask();
}
