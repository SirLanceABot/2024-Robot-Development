package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Candle4237;

public class BradyTest implements Test
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    private final Candle4237 candle;


    // *** CLASS CONSTRUCTOR ***
    public BradyTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        candle = new Candle4237();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {}

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        candle.signalRed();
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        candle.signalOff();
    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
