package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

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
    
    private class PeriodicIO
    {
        // INPUTS

        // OUTPUTS

    }

    private PeriodicIO periodicIO = new PeriodicIO();

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public ExampleSubsystem()
    {}

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {}

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
}
