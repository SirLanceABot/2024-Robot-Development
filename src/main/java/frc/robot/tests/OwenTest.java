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
    // private final Flywheel flywheel;
    // private final Index index;
    private final Climb climb;
    // private final Ultrasonic ultrasonic;
    // private final Joystick joystick = new Joystick(0);
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
        // flywheel = this.robotContainer.flywheel;
        // index = this.robotContainer.index;
        climb = this.robotContainer.climb;
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
        configLeftTrigger();
        configRightTrigger();
        configBackButton();
        System.out.println("Left Position: " + climb.getLeftPosition() + "   Right Position: " + climb.getRightPosition());
        // System.out.println("distance = " + ultrasonic.getDistance());
        // if(joystick.getRawButton(1))
        // {
        //     System.out.println("Chain Position");
        //     System.out.println("Encoder Position" + climb.getLeftPosition());
        //     // flywheel.shoot();
        //     // index.acceptNote();
        //     climb.moveToChain();
        // }
        // else if(joystick.getRawButton(2))
        // {
        //     System.out.println("Robot Position");
        //     System.out.println("Encoder Position" + climb.getLeftPosition());
        //     // flywheel.intake();
        //     // index.feedNote();
        //     climb.moveToInnerRobot();
        // }
        // else if(joystick.getRawButton(3))
        // {
        //     System.out.println("Raise Climb");
        //     System.out.println("Encoder Position" + climb.getLeftPosition());
        //     // index.reverse();
        //     climb.extend();
        // }
        // else if(joystick.getRawButton(4))
        // {
        //     System.out.println("Lower Climb");
        //     System.out.println("Encoder Position" + climb.getLeftPosition());
        //     // index.reverse();
        //     climb.retract();
        // }
        // else 
        // {
        //     System.out.println("Off");
        //     System.out.println("Encoder Position" + climb.getLeftPosition());
        //     // flywheel.turnOff();
        //     // index.turnOff();
        //     climb.off();
        // }

    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    

    private void configLeftTrigger()
    {
        //Left trigger 
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        if(true)
        {
            leftTriggerTrigger.whileTrue(climb.extendClimbCommand(0.1));
            leftTriggerTrigger.onFalse(climb.stopClimbCommand());
            // leftTriggerTrigger.onFalse(climb.stopClimbCommand());
        }
    }

    private void configRightTrigger()
    {
        //Left trigger 
        BooleanSupplier rightTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
        Trigger rightTriggerTrigger = new Trigger(rightTrigger);

        if(true)
        {
            rightTriggerTrigger.whileTrue(climb.retractClimbCommand(-0.1));
            rightTriggerTrigger.onFalse(climb.stopClimbCommand());
            // rightTriggerTrigger.onFalse(climb.stopClimbCommand());
        }
    }

    private void configBackButton()
    {
        // Back Button
        BooleanSupplier backButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kBack);
        Trigger backButtonTrigger = new Trigger(backButton);

        if(true)
        {
            backButtonTrigger.onTrue(climb.moveToPositionCommand(TargetPosition.kChain));
            // backButtonTrigger.onFalse(climb.stopClimbCommand());
        }

        // backButtonTrigger.onTrue(shootCommand(0.5, () -> 0.0));
    }

    

    // *** METHODS ***
    // Put any additional methods here.

        
}
