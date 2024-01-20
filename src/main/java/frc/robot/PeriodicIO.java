package frc.robot;

import java.util.ArrayList;

/**
 * This interface is implemented for every system on the robot that uses periodic inputs and outputs.
 * Every class must call the <b>registerPeriodicIO()</b> method to add the system to the array list for periodic inputs and outputs.
 */
public interface PeriodicIO
{
    // *** STATIC CONSTANTS ***
    // Put all constants here - automatically "public final static"
    public final static ArrayList<PeriodicIO> allPeriodicIO = new ArrayList<PeriodicIO>();


    // *** STATIC METHODS ***
    // Put all static methods here - automatically "public"

    /**
     * Static method to periodically update all of the systems in the array list.
     * Call this method from the robotPeriodic() method in the Robot class.
     */
    public static void readAllPeriodicInputs()
    {
        for(PeriodicIO periodicIO : allPeriodicIO)
            periodicIO.readPeriodicInputs();
    }

    /**
     * Static method to periodically update all of the systems in the array list.
     * Call this method from the robotPeriodic() method in the Robot class.
     */
    public static void writeAllPeriodicOutputs()
    {
        for(PeriodicIO periodicIO : allPeriodicIO)
            periodicIO.writePeriodicOutputs();
    }


    // *** DEFAULT METHODS ***
    // Put all default methods methods here - automatically "public"

    /**
     * Default method to register periodic inputs and outputs
     */
    public default void registerPeriodicIO()
    {
        allPeriodicIO.add(this);
    }
    

    // *** ABSTRACT METHODS ***
    // Put all abstract methods here - automatically "public abstract"
    // These methods must be defined in any subclass that implements this interface
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();
}
