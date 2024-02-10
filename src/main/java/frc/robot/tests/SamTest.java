package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Pivot;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Shuttle;

public class SamTest implements Test
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
    // private final Proximity prox1;
    // private final Proximity prox2;
    // private final Candle4237 candle;
    private final Shuttle shuttle;
    private final Index index;
    private final Pivot pivot;
    private final Flywheel flywheel;
    private final Joystick joystick;

    // *** CLASS CONSTRUCTOR ***
    public SamTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // prox1 = new Proximity(8);
        // prox2 = new Proximity(9);
        // candle = new Candle4237();
        joystick = new Joystick(0);
        this.shuttle = robotContainer.shuttle;
        this.index = robotContainer.index;
        this.pivot = robotContainer.pivot;
        this.flywheel = robotContainer.flywheel;

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
        // if(prox1.isDetected() && prox2.isDetected())
        // {
        //     candle.setGreen(false);
        // }
        // else if(prox1.isDetected() || prox2.isDetected())
        // {
        //     candle.setRed(false);
        // }
        // else
        // {
        //     candle.stop();
        // }

        // if(joystick.getRawButton(3))    //X
        // {
        //     flywheel.intake();
        //     index.intake();
        //     // shuttle.moveDownward();
        //     // flywheel.shoot(0.5);
        // }


        if(joystick.getRawButton(1))    //A
        {
            // flywheel.intake();
            // index.intake();
            // shuttle.moveDownward();
            flywheel.shoot(80);
            // index.setVelocity(1);
        }
        else
        {
            // flywheel.stop();
            // index.stop();
            // shuttle.stop();
            // pivot.stopMotor();
        }
        if(joystick.getRawButton(2))   //B
        {
            index.setVelocity(1);
        }
        // else if(joystick.getRawButton(4))    //Y
        // // {
        // // //     flywheel.shoot(0.1);
        // // //     index.setVelocity(0.1);
        // // //     shuttle.moveUpward();
        // // }
        else
        {
        //     // flywheel.stop();
            index.stop();
        //     // shuttle.stop();
        //     // pivot.stopMotor();
        }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        // candle.stop();
        flywheel.stop();
        index.stop();
        shuttle.stop();
        // pivot.stopMotor();
    }

    // *** METHODS ***
    // Put any additional methods here.

        
}
