package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Flywheel;
// import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Climb.TargetPosition;
// import frc.robot.commands.ExtendClimb;
import frc.robot.controls.Xbox;
// import frc.robot.controls.OperatorController
import frc.robot.sensors.Ultrasonic;

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
    private final Pivot pivot;
    // private final Climb climb;
    // private final Ultrasonic ultrasonic;
    private final Joystick joystick = new Joystick(0);
    // private CANSparkMax motor = new CANSparkMax(0, MotorType.kBrushless);
    // private CANSparkMax motor1 = new CANSparkMax(1, MotorType.kBrushless);
    // private BooleanSupplier false1 = () -> false;
    // BooleanSupplier buttonA = operatorController.getRawButton(Xbox.Button.kA);
    // Trigger trigger = new Trigger(true);


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        flywheel = this.robotContainer.flywheel;
        index = this.robotContainer.index;
        pivot = this.robotContainer.pivot;
        // climb = this.robotContainer.climb;
        // ultrasonic = this.robotContainer.ultrasonic;

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        // climb.setLeftAndRightPosiiton(TargetPosition.kChain);
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // motor.follow(motor1);
        // configLeftTrigger();
        // configRightTrigger();
        // configBackButton();
        // configLeftBumper();
        // configRightBumper();
        // System.out.println("Velocity: " + climb.getLeftPosition());
        // System.out.println("Left Position: " + climb.getLeftPosition() + "   Right Position: " + climb.getRightPosition());
        // System.out.println("distance = " + ultrasonic.getDistance());
        if(joystick.getRawButton(1))
        {
            System.out.println("Chain Position");
            pivot.moveDown();
            // System.out.println("Encoder Position" + climb.getLeftPosition());
            // flywheel.shoot(0.7);
            // index.feedNoteToFlywheel(0.9);
            // climb.moveToChain();
        }
        else if(joystick.getRawButton(2))
        {
            System.out.println("Robot Position");
            // System.out.println("Encoder Position" + climb.getLeftPosition());
            // flywheel.intake();
            // index.feedNoteToFlywheel(0.1);
            // climb.moveToInnerRobot();
        }
        else if(joystick.getRawButton(3))
        {
            System.out.println("Raise Climb");
            // System.out.println("Encoder Position" + climb.getLeftPosition());
            // index.reverse();
            // climb.extend();
        }
        else if(joystick.getRawButton(4))
        {
            System.out.println("Lower Climb");
            pivot.moveUp();
            // System.out.println("Encoder Position" + climb.getLeftPosition());
            // index.reverse();
            // climb.retract();
        }
        else 
        {
            System.out.println("Off");
            // System.out.println("Encoder Position" + climb.getLeftPosition());
            // flywheel.stop();
            // index.stop();
            // climb.off();
            pivot.stop();
        }

    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    
    //this is the equalibrium ratio for shooter motor powers
    private void configLeftTrigger()
    {
        //Left trigger 
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        if(true)
        {
            // leftTriggerTrigger.whileTrue(index.feedNoteToFlywheelCommand(1.0).alongWith(flywheel.shootCommand(0.625)));
            // leftTriggerTrigger.onFalse(index.stopCommand().alongWith(flywheel.stopCommand()));
        }
    }
    
    //this is for 10% extra power to the flywheel from the equalibrium
    private void configRightTrigger()
    {
        //Left trigger 
        BooleanSupplier rightTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
        Trigger rightTriggerTrigger = new Trigger(rightTrigger);

        if(true)
        {
            // rightTriggerTrigger.whileTrue(index.feedNoteToFlywheelCommand(1.0).alongWith(flywheel.shootCommand(0.725)));
            // rightTriggerTrigger.onFalse(index.stopCommand().alongWith(flywheel.stopCommand()));
        }
    }

    private void configBackButton()
    {
        // Back Button
        BooleanSupplier backButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kBack);
        Trigger backButtonTrigger = new Trigger(backButton);

        
            // backButtonTrigger.whileTrue(climb.moveToPositionCommand(TargetPosition.kChain));

        // backButtonTrigger.onTrue(shootCommand(0.5, () -> 0.0));
    }

    private void configLeftBumper()
    {
        // Back Button
        BooleanSupplier leftBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftBumper);
        Trigger leftBumperTrigger = new Trigger(leftBumper);

        if(true)
        {
            // leftBumperTrigger.whileTrue(climb.retractClimbCommand(-0.4));
            // leftBumperTrigger.onFalse(climb.stopClimbCommand());

        }

        // backButtonTrigger.onTrue(shootCommand(0.5, () -> 0.0));
    }

    private void configRightBumper()
    {
        // Back Button
        BooleanSupplier rightBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightBumper);
        Trigger rightBumperTrigger = new Trigger(rightBumper);

        if(true)
        {
            
            // rightBumperTrigger.onFalse(climb.stopClimbCommand());

            // rightBumperTrigger.whileTrue(climb.extendClimbCommand(0.4));
            // rightBumperTrigger.onFalse(climb.stopClimbCommand());

        }

        // backButtonTrigger.onTrue(shootCommand(0.5, () -> 0.0));
    }

    

    // *** METHODS ***
    // Put any additional methods here.

        
}
