/**
 * sample Sensor-like class that easily gets an example value
 * and displays it
 */

package frc.robot.sensors;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    private class PeriodicData
    {
        // INPUTS
        private double x;

        // OUTPUTS
    }

    private PeriodicData periodicData;

    public ExampleSensor()
    {   
        super("Example Sensor");
        System.out.println("  Constructor Started:  " + fullClassName);

        periodicData = new PeriodicData();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /** example getter
     * Usage periodicIO.getX()
    * @return X
    */
    public double getX()
    {
        return periodicData.x;
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicData.x = 3.14159;
    }

    @Override
    public void writePeriodicOutputs() 
    {
        SmartDashboard.putNumber("Pi", periodicData.x);
    }

    @Override
    public void runPeriodicTask()
    {

    }

    @Override
    public String toString()
    {
        return null;
    }
}