package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import frc.robot.subsystems.Intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;

public class LoganTest implements Test
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
    // private final CANSparkMax intakeMotor = new CANSparkMax(3, MotorType.kBrushless);
    // private RelativeEncoder intakeEncoder;
    private final Joystick joystick = new Joystick(1);
    private double encoderPosition;
    private Intake intake;


    // *** CLASS CONSTRUCTOR ***
    public LoganTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        intake = this.robotContainer.intake;

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
            encoderPosition = intake.getBottomIntakePosition();
            System.out.println("In.  Encoder Position: " + encoderPosition);
            intake.pickupFront();
        }
        else if(joystick.getRawButton(2))
        {
            encoderPosition = intake.getBottomIntakePosition();
            System.out.println("Out.  Encoder Position: " + encoderPosition);
            intake.pickupBack();
        }
        else
        {
            encoderPosition = intake.getBottomIntakePosition();
            System.out.println("Stopped.  Encoder Position: " + encoderPosition);
            intake.off();
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
