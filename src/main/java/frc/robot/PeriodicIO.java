package frc.robot;

import java.util.ArrayList;

/**
 * This interface is implemented for every system on the robot that uses periodic inputs and outputs.
 * Every class must call the <b>registerPeriodicIO()</b> method to add the system to the array list for periodic inputs and outputs.
 */
public interface PeriodicIO
{
    // *** CLASS & INSTANCE VARIABLES ***
    public final static ArrayList<PeriodicIO> allPeriodicIO = new ArrayList<PeriodicIO>();

    // Abstract methods to override in subclasses
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();
    
    /**
     * Default method to register periodic inputs and outputs
     */
    public default void registerPeriodicIO()
    {
        allPeriodicIO.add(this);
    }

    /**
     * Static method to periodically update all of the systems in the array list.
     * Call this method from the robotPeriodic() method in the Robot class.
     */
    public static void readInputs()
    {
        for(PeriodicIO periodicIO : allPeriodicIO)
            periodicIO.readPeriodicInputs();
    }

    /**
     * Static method to periodically update all of the systems in the array list.
     * Call this method from the robotPeriodic() method in the Robot class.
     */
    public static void writeOutputs()
    {
        for(PeriodicIO periodicIO : allPeriodicIO)
            periodicIO.writePeriodicOutputs();
    }
}
