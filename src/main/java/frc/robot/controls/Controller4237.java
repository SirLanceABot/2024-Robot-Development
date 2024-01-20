package frc.robot.controls;

import java.lang.invoke.MethodHandles;

import frc.robot.PeriodicIO;
import frc.robot.PeriodicTask;

/**
 * This abstract class will be extended for every controller. 
 * Every controller will automatically be added to the array list for periodic inputs, outputs, and tasks.
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


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    
    /**
     * Registers the controller for PeriodicIO and PeriodicTask.
     * @param controllerName
     */
    Controller4237(String controllerName)
    {
        super();

        System.out.println("  Constructor Started:  " + fullClassName + " >> " + controllerName);

        // Register this sensor in the array list to get periodic input and output
        registerPeriodicIO();

        // Register this controller in the array list to run periodic tasks
        registerPeriodicTask();

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + controllerName);
    }


    // *** ABSTRACT METHODS ***
    // These methods must be defined in any subclass that extends this class
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();
    public abstract void runPeriodicTask();
}
