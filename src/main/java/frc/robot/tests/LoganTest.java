package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.AmpAssist;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake.Direction;
import frc.robot.sensors.Proximity;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.commands.IntakeFromFloor;

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
    private double topEncoderPosition;
    private double bottomEncoderPosition;
    private Intake intake;
    private AmpAssist ampAssist;
    private IntakePositioning intakePositioning;
    private Shuttle shuttle;
    private Index index;
    private Direction direction;
    private Proximity secondShuttleProximity;
    private Proximity indexProximity;


    // *** CLASS CONSTRUCTOR ***
    public LoganTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        intake = robotContainer.intake;
        ampAssist = robotContainer.ampAssist;
        intakePositioning = robotContainer.intakePositioning;
        shuttle = robotContainer.shuttle;
        index = robotContainer.index;
        secondShuttleProximity = robotContainer.secondShuttleProximity;
        indexProximity = robotContainer.indexProximity;

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
        //MOTORS
        // if(joystick.getRawButton(1)) // A button
        // {
        //     topEncoderPosition = intake.getTopPosition();
        //     bottomEncoderPosition = intake.getBottomPosition();
        //     System.out.println("Pickup Front.  Top Encoder Position: " + topEncoderPosition);
        //     System.out.println("Bottom Encoder Position: " + bottomEncoderPosition);
        //     intake.pickupFront();
        // }
        // else if(joystick.getRawButton(2)) // B button
        // {
        //     topEncoderPosition = intake.getTopPosition();
        //     bottomEncoderPosition = intake.getBottomPosition();
        //     System.out.println("Pickup Back.  Top Encoder Position: " + topEncoderPosition);
        //     System.out.println("Bottom Encoder Position: " + bottomEncoderPosition);
        //     intake.pickupBack();
        // }
        // else if(joystick.getRawButton(3)) // X button
        // {
        //     topEncoderPosition = intake.getTopPosition();
        //     bottomEncoderPosition = intake.getBottomPosition();
        //     System.out.println("Eject Front.  Top Encoder Position: " + topEncoderPosition);
        //     System.out.println("Bottom Encoder Position: " + bottomEncoderPosition);
        //     intake.ejectFront();
        // }
        // else if(joystick.getRawButton(4)) // Y button
        // {
        //     topEncoderPosition = intake.getTopPosition();
        //     bottomEncoderPosition = intake.getBottomPosition();
        //     System.out.println("Eject Back.  Top Encoder Position: " + topEncoderPosition);
        //     System.out.println("Bottom Encoder Position: " + bottomEncoderPosition);
        //     intake.ejectBack();
        // }
        // else
        // {
        //     System.out.println("Off.  Top Encoder Position: " + topEncoderPosition);
        //     System.out.println("Bottom Encoder Position: " + bottomEncoderPosition);
        //     intake.off();
        // }

        //PNEUMATICS
    //     if(joystick.getRawButton(1)) // A button
    //     {
    //         System.out.println("Out.");
    //         ampAssist.extend();
    //     }
        
    //     if(joystick.getRawButton(2)) // B button
    //     {
    //         System.out.println("In.");
    //         ampAssist.retract();
    //     }

        if(joystick.getRawButton(1))
        {
            new IntakeFromFloor(intake, intakePositioning, shuttle, index, direction, secondShuttleProximity, indexProximity);
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
