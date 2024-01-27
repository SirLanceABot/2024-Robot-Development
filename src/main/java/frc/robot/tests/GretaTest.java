package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shuttle;

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
    private final Shuttle shuttleSuzie;
    private final Pivot pivotPriscilla;
    private final Flywheel flyWheelFiona;
    private final Joystick joystick = new Joystick(0); 
    
//     // *** CLASS CONSTRUCTOR ***
    public GretaTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        shuttleSuzie = robotContainer.shuttle;
        pivotPriscilla = robotContainer.pivot;
        flyWheelFiona = robotContainer.flywheel;

        System.out.println("  Constructor Finished: " + fullClassName);
    }
    
//     /**
//      * This method runs one time before the periodic() method.
//      */
    public void init()
    {}

//     /**
//      * This method runs periodically (every 20ms).
//      */
    public void periodic()
    {
//         if (joystick.getRawButton(1))
//         {
//             shuttleSuzie.moveUpward();
//             // pivotPriscilla.moveUp(0.35);
//             flyWheelFiona.shoot(0.35);
//         }
//         else if (joystick.getRawButton(2))
//         {
//             shuttleSuzie.moveDownward();
//             // pivotPriscilla.moveDown(-0.35);
//         }
//         else
//         {
//             shuttleSuzie.off();
//             pivotPriscilla.stop();
//             flyWheelFiona.Stop();
//         }
        
//         if (joystick.getRawButton(3))
//         {
//             shuttleSuzie.resetEncoder();
//             pivotPriscilla.resetEncoder();
//             flyWheelFiona.resetEncoder();
//         }

//         robotContainer.mainShuffleboard.sensorTab.updateEncoderData();
    }
    
//     /**
//      * This method runs one time after the periodic() method.
//      */
    public void exit()
    {}

//     // *** METHODS ***
//     // Put any additional methods here.

    
}
