package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Candle4237.LedStatus;

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
    // private final Joystick bradysJoystick;
    private final Proximity proximity;


    // *** CLASS CONSTRUCTOR ***
    public BradyTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        candle = new Candle4237();
        // bradysJoystick = new Joystick(0);
        proximity = new Proximity();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {

    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        //candle.setRGBFade();
        // if(bradysJoystick.getRawButton(1)) //A
        // {
        //     candle.setGreen(false);
        // }
        // else if(bradysJoystick.getRawButton(2)) //B
        // {
        //     candle.setRed(false);
        // }
        // else if(bradysJoystick.getRawButton(3)) //X
        // {
        //     candle.setBlue(false);
        // }
        // else if(bradysJoystick.getRawButton(4)) //Y
        // {
        //     candle.setLarson();
        // }
        // else if(bradysJoystick.getRawButton(6)) //RB
        // {
        //     candle.setOff();
        // }

        // if(proximity.getIsDetected())
        // {
        //     candle.setGreen(false);
        // }
        // else
        // {
        //     candle.setRed(false);
        // }

        if(proximity.isDetected())
        {
            System.out.println("True");
        }
        else
        {
            System.out.println("False");
        }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        // candle.setOff();
    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
