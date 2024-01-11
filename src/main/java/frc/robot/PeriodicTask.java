package frc.robot;

import java.util.ArrayList;

/**
 * This interface is implemented for every system on the robot (excluding SubsystemBase) that runs periodic tasks.
 * Every class must call the <b>registerPeriodicTask()</b> method to add the system to the array list for periodic inputs and outputs.
 */
public interface PeriodicTask
{
    // *** CLASS & INSTANCE VARIABLES ***
    public final static ArrayList<PeriodicTask> allPeriodicTasks = new ArrayList<PeriodicTask>();

    // Abstract methods to override in subclasses
    public abstract void runPeriodicTask();
    
    /**
     * Default method to register periodic inputs and outputs
     */
    public default void registerPeriodicTask()
    {
        allPeriodicTasks.add(this);
    }

    /**
     * Static method to periodically update all of the systems in the array list.
     * Call this method from the robotPeriodic() method in the Robot class.
     */
    public static void runTasks()
    {
        for(PeriodicTask periodicTask : allPeriodicTasks)
            periodicTask.runPeriodicTask();
    }
}
