package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import javax.lang.model.util.ElementScanner14;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.AmpAssist;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake.Direction;
import frc.robot.sensors.Proximity;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.commands.Commands4237;
import frc.robot.subsystems.Climb;
import frc.robot.controls.Xbox;

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
    private final Joystick joystick = new Joystick(3);
    // private double topEncoderPosition;
    // private double bottomEncoderPosition;
    // private Intake intake;
    private AmpAssist ampAssist;
    // private IntakePositioning intakePositioning;
    // private Shuttle shuttle;
    // private Index index;
    // private Climb climb;
    // private Direction direction;
    // private Proximity secondShuttleProximity;
    // private Proximity indexProximity;


    // *** CLASS CONSTRUCTOR ***
    public LoganTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // intake = robotContainer.intake;
        ampAssist = robotContainer.ampAssist;
        // intakePositioning = robotContainer.intakePositioning;
        // shuttle = robotContainer.shuttle;
        // index = robotContainer.index;
        // climb = robotContainer.climb;
        // secondShuttleProximity = robotContainer.secondShuttleProximity;
        // indexProximity = robotContainer.indexProximity;

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
        // System.out.println("Pressure: " + robotContainer.intakePositioning.getPressure());
        //MOTORS
        if(joystick.getRawButton(1)) // A button
        {
            // Commands4237.intakeFromFloorFront().schedule();
        //     // configAButton();
            // intakePositioning.moveUp();
            // System.out.println("Intake is Up: " + intakePositioning.isIntakeUp());
            // ampAssist.extendCommand().schedule();
            // System.out.println("Extend");
            // robotContainer.climb.raise(0.25);
            robotContainer.intake.pickupFrontCommand().schedule();
            robotContainer.shuttle.moveUpwardCommand().schedule();
            // robotContainer.intakePositioning.moveDownCommand().schedule();
        }
        else if(joystick.getRawButton(2)) // B
        {
            // Commands4237.shootFromSubWooferCommand().schedule();
            // ampAssist.retractCommand().schedule();
            // System.out.println("Retract");
            // robotContainer.climb.lower(-0.25);
            robotContainer.intake.pickupBackCommand().schedule();
            robotContainer.shuttle.moveUpwardCommand().schedule();
        }
        else if(joystick.getRawButton(3))
        {
            robotContainer.intakePositioning.moveDownCommand().schedule();
        }
        // else if(joystick.getRawButton(3))
        // {
        //     // robotContainer.climb.resetEncoders();
        // }
        else
        {
            // robotContainer.climb.stop();
            robotContainer.intake.stopCommand().schedule();
            robotContainer.shuttle.stopCommand().schedule();
        }
        // SmartDashboard.putNumber("Climb Encoder Position:", robotContainer.climb.getLeftPosition());
        // else if(joystick.getRawButton(2))
        // {
        //     climb.retract(0.4);
        // }
        // else
        // {
        //     climb.stop();
        // }
        // if(joystick.getRawButton(3)) // X
        // {
        //     // climb.resetEncoder();
        // }
        // else if(joystick.getRawButton(2))
        // {
        //     intakePositioning.moveDown();
        //     System.out.println("Intake is Down: " + intakePositioning.isIntakeDown());
        // }
        // else if(joystick.getRawButton(3))
        // {
        //     intakePositioning.floating();
        //     System.out.println("Intake is Down: " + intakePositioning.isIntakeDown());
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

    

    }

    
    
    /** 
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    // *** METHODS ***
    // Put any additional methods here.

    private void configAButton()
    {
        // A Button
        BooleanSupplier aButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kA);
        Trigger aButtonTrigger = new Trigger(aButton);

        // aButtonTrigger.onTrue();
    }        
}
