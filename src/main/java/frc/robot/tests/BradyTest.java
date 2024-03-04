package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.PoseEstimator;
import frc.robot.subsystems.Candle4237.LEDColor;

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
    private final Joystick bradysController;
    // private final Proximity proximity;
    // private final Drivetrain drivetrain;
    // private final Gyro4237 gyro;
    // private final Camera[] cameraArray;
    // private final PoseEstimator poseEstimator;
    // private final DigitalInput digitalInput;


    // *** CLASS CONSTRUCTOR ***
    public BradyTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // this.drivetrain = robotContainer.drivetrain;
        // this.gyro = robotContainer.gyro;
        // this.cameraArray = robotContainer.cameraArray;
        this.candle = robotContainer.candle;
        bradysController = new Joystick(0);
        // proximity = new Proximity(9);
        // digitalInput = new DigitalInput(9);

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
        if(bradysController.getRawButton(1)) //A
        {
            candle.setGreenCommand().schedule();
        }
        else if(bradysController.getRawButton(2)) //B
        {
            // candle.setRedAndBlue();
            candle.setWhiteCommand().schedule();
        }
        else if(bradysController.getRawButton(3)) //X
        {
            candle.setBlueCommand().schedule();
        }
        else if(bradysController.getRawButton(4)) //Y
        {
            candle.setYellowCommand().schedule();
        }
        else if(bradysController.getRawButton(5)) //LB 
        {
            candle.setPurpleCommand().schedule();
        }
        else if(bradysController.getRawButton(6)) //RB
        {
            // candle.setPurple(false);
            candle.setRainbowCommand().schedule();
            // candle.setRGBFade();
        }
        // else
        // {
        //     candle.setRedCommand().schedule();
        // }

        // if(proximity.getIsDetected())
        // {
        //     candle.setGreen(false);
        // }
        // else
        // {
        //     candle.setRed(false);
        // }

        // if(proximity.isDetected())
        // {
        //     System.out.println("True");
        // }
        // else
        // {
        //     System.out.println("False");
        // }

        // System.out.println(drivetrain.getAngleToBlueSpeaker());

            // System.out.println(digitalInput.get());

    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        candle.stop();
    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
