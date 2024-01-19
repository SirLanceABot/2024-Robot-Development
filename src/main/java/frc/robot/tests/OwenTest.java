package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Flywheel;
// import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Climb.TargetPosition;

public class OwenTest implements Test
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
    // private final Flywheel flywheel;
    // private final Index index;
    private final Climb climb;
    private final Joystick joystick = new Joystick(0);


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // flywheel = this.robotContainer.flywheel;
        // index = this.robotContainer.index;
        climb = this.robotContainer.climb;

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        climb.setLeftAndRightPosiiton(TargetPosition.kChain);
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if(joystick.getRawButton(1))
        {
            System.out.println("raise climb");
            System.out.println("Encoder Position" + climb.getLeftPosition());
            // flywheel.shoot();
            // index.acceptNote();
            climb.extendClimb();
        }
        else if(joystick.getRawButton(2))
        {
            System.out.println("lower climb");
            System.out.println("Encoder Position" + climb.getLeftPosition());
            // flywheel.intake();
            // index.feedNote();
            climb.retractClimb();
        }
        else if(joystick.getRawButton(3))
        {
            System.out.println("hold climb");
            System.out.println("Encoder Position" + climb.getLeftPosition());
            // index.reverse();
            climb.holdClimb();
        }
        else 
        {
            System.out.println("Off");
            System.out.println("Encoder Position" + climb.getLeftPosition());
            // flywheel.turnOff();
            // index.turnOff();
            climb.off();
        }

    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    // *** METHODS ***
    // Put any additional methods here.

        
}
