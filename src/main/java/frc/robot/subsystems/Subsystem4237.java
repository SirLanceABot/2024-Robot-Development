package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.PeriodicIO;

/**
 * This abstract class will be extended for every subsystem on the robot. 
 * Every subsystem will automatically be added to the array list for periodic inputs and outputs.
 */
abstract class Subsystem4237 extends SubsystemBase implements PeriodicIO
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
     * Registers the subsystem for PeriodicIO.
     * @param subsystemName The name of the subsystem, for debugging purposes
     */
    Subsystem4237(String subsystemName)
    {
        super();

        System.out.println("  Constructor Started:  " + fullClassName + " >> " + subsystemName);

        // Register this sensor in the array list to get periodic input and output
        registerPeriodicIO();

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + subsystemName);
    }
    

    // *** ABSTRACT METHODS ***
    // These methods must be defined in any subclass that extends this class
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();
}
