package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.motors.TalonFX4237;
import frc.robot.subsystems.Pivot;

public class MatthewTest implements Test
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
    private final TalonFX4237 talonFX = new TalonFX4237(4, "rio", "matthew'sMotor");
    private final Pivot pivot;


    // *** CLASS CONSTRUCTOR ***
    public MatthewTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);
    
        this.robotContainer = robotContainer;
        this.pivot = robotContainer.pivot;
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
        pivot.tunePID();
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }

    // *** METHODS ***
    // Put any additional methods here.

        
}
