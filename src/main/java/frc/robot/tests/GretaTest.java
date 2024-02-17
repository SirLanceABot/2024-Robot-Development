package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shuttle;
import frc.robot.sensors.Proximity;

public class GretaTest implements Test
{
//     // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

//     // *** STATIC INITIALIZATION BLOCK ***
//     // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
  

//     // *** CLASS & INSTANCE VARIABLES ***
//     // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    // private final Shuttle shuttleSuzie;
    // private final Pivot pivotPriscilla;
    // private final Flywheel flyWheelFiona;
    // private final Index indexIzzy;
    // private final Intake intakeIrene;
    // private final Climb climbCindy;
    // private final Proximity firstShuttleProximity;
    // private final Proximity secondShuttleProximity;
    // private final Proximity indexProximity;
    // private final Proximity indexWheelsProximity;
    
    private final Joystick joystick = new Joystick(0); 
    
//     // *** CLASS CONSTRUCTOR ***
    public GretaTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // shuttleSuzie = robotContainer.shuttle;
        // pivotPriscilla = robotContainer.pivot;
        // flyWheelFiona = robotContainer.flywheel;
        // indexIzzy = robotContainer.index;
        // intakeIrene = robotContainer.intake;
        // climbCindy = robotContainer.climb;
        // firstShuttleProximity = robotContainer.firstShuttleProximity;
        // secondShuttleProximity = robotContainer.secondShuttleProximity;
        // indexProximity = robotContainer.indexProximity;
        // indexWheelsProximity = robotContainer.indexWheelsProximity;
    

        System.out.println("  Constructor Finished: " + fullClassName);
    }
    
//     /**
//      * This method runs one time before the periodic() method.
//      */
    public void init()
    {
        String string = "Hello";
        System.out.println(string);
        System.out.println(string + " World");
    }

//     /**
//      * This method runs periodically (every 20ms).
//      */
    public void periodic()
    {
        // if (joystick.getRawButton(1))
        {
            // shuttleSuzie.moveUpward();
            // pivotPriscilla.moveUp();
            // flyWheelFiona.shoot(0.35);
            // indexIzzy.acceptNoteFromShuttle();
            // climbCindy.holdClimb();
            // intakeIrene.ejectFront();
        }
        // else if (joystick.getRawButton(2))
        {
            // shuttleSuzie.moveDownward();
            // pivotPriscilla.moveDown();
            // indexIzzy.intake();
            // //climbCindy.retract();
            // intakeIrene.ejectBack();
        }

        // else
        {
            // shuttleSuzie.stop();
            // pivotPriscilla.stop();
            // flyWheelFiona.stop();
            // indexIzzy.stop();
            // intakeIrene.stop();
            // climbCindy.stop();
        }
        
        // if (joystick.getRawButton(3))
        {
            // shuttleSuzie.resetEncoder();
            // pivotPriscilla.resetCANcoder();
            // flyWheelFiona.resetEncoder();
            // climbCindy.resetEncoder();
        }

        // robotContainer.mainShuffleboard.sensorTab.updateEncoderData();
    }
    
//     /**
//      * This method runs one time after the periodic() method.
//      */
    public void exit()
    {}

//     // *** METHODS ***
//     // Put any additional methods here.

    
}
