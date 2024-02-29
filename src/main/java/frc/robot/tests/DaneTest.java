package frc.robot.tests;

import frc.robot.subsystems.Pivot;
import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner14;

import frc.robot.RobotContainer;
import frc.robot.controls.Xbox;
//import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DaneTest implements Test
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
    private final Pivot pivot;
    private final Joystick joystick = new Joystick(0);


    // *** CLASS CONSTRUCTOR ***
    public DaneTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        pivot = this.robotContainer.pivot;
        

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
        if(joystick.getRawButton(1))
        {
            pivot.setAngle(50.0);
            // pivot.setAngleCommand(50.0);
            // pivot.moveUp();
        }
        else if(joystick.getRawButton(2))
        {
            pivot.setAngle(30.0);
            // pivot.setAngleCommand(30.0);
            // pivot.moveDown();
        }
        else
        {
            pivot.stop();
        }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        pivot.stop();
    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
