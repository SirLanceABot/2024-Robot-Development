package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Flywheel;
// import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;

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
    private final Flywheel flywheel;
    private final Index index;
    private final Joystick joystick = new Joystick(0);


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        flywheel = this.robotContainer.flywheel;
        index = this.robotContainer.index;

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
            System.out.println("Accept Note");
            // flywheel.shoot();
            index.acceptNote();
        }
        else if(joystick.getRawButton(2))
        {
            System.out.println("Feed Note");
            // flywheel.intake();
            index.feedNote();
        }
        else if(joystick.getRawButton(3))
        {
            System.out.println("Reverse Index");
            index.reverse();
        }
        else 
        {
            System.out.println("Off");
            // flywheel.turnOff();
            index.turnOff();
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
