package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;

import frc.robot.Constants;
import frc.robot.motors.TalonFX4237;

/**
 * This class creates the climb
 */
public class Climb extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum TargetPosition
    {
        kChain(CHAIN_ENCODER_POSITION),
        kInnerRobot(INNER_ROBOT_ENCODER_POSITION),
        kOuterRobot(OUTER_ROBOT_ENCODER_POSITION),
        kOverride(-4237.0);

        public final double value;
        private TargetPosition(double value)
        {
            this.value = value;
        }
    }

    public enum ResetState
    {
        kStart, kTry, kDone;
    }

    private enum LimitSwitchState
    {
        kPressed, kStillPressed, kReleased, kStillReleased;
    }

    private enum OverrideMode
    {
        kNotMoving, kMoving;
    }
    
    private final class PeriodicData
    {
        // INPUTS
        private double currentLeftPosition = 0.0;
        private double currentRightPosition = 0.0;
       

        // OUTPUTS
        private DoubleLogEntry positionEntry;
        private DoubleLogEntry velocityEntry;
        private double motorSpeed;
    }

    private PeriodicData periodicData = new PeriodicData();
    private final TalonFX4237 leftMotor = new TalonFX4237(Constants.Climb.LEFT_MOTOR_PORT, Constants.Climb.LEFT_MOTOR_CAN_BUS, "leftMotor");
    private final TalonFX4237 rightMotor = new TalonFX4237(Constants.Climb.RIGHT_MOTOR_PORT, Constants.Climb.RIGHT_MOTOR_CAN_BUS, "rightMotor");
    private TargetPosition targetPosition = TargetPosition.kOverride;
    private OverrideMode overrideMode = OverrideMode.kNotMoving;
    // private RelativeEncoder leftMotorEncoder;
    // private RelativeEncoder rightMotorEncoder;

    private final double LEFT_MOTOR_FORWARD_SOFT_LIMIT       = 80.0;
    private final double LEFT_MOTOR_REVERSE_SOFT_LIMIT       = 0.0;
    private final double RIGHT_MOTOR_FORWARD_SOFT_LIMIT      = 80.0;
    private final double RIGHT_MOTOR_REVERSE_SOFT_LIMIT      = 0.0;

    private static final double CHAIN_ENCODER_POSITION              = 60.0;
    private static final double OUTER_ROBOT_ENCODER_POSITION        = 30.0;
    private static final double INNER_ROBOT_ENCODER_POSITION        = 20.0;

    private final double CURRENT_LIMIT                       = 10.0;
    private final double CURRENT_THRESHOLD                   = 10.0;
    private final double TIME_THRESHOLD                      = 10.0;

    private final double kP = 0.00003;
    private final double kI = 0.0; // 0.0001
    private final double kD = 0.0;
    private final double kIz = 0.0;
    private final double kFF = 0.0;
    private final double kMaxOutput = 0.7;
    private final double kMinOutput = -0.7;
    private final double kRobotMaxOutput = 0.7;
    private final double kRobotMinOutput = -0.7;

    private LimitSwitchState reverseLSState = LimitSwitchState.kStillReleased;
    private TargetPosition position = TargetPosition.kOverride;
    private ResetState resetState = ResetState.kDone;


    
    

    /** 
     * Creates a new Climb. 
     */
    public Climb()
    {
        super("Climb");
        System.out.println("  Constructor Started:  " + fullClassName);
        configMotors();

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configMotors()
    {
        leftMotor.setupBrakeMode();
        rightMotor.setupBrakeMode();
        leftMotor.setupFactoryDefaults();
        rightMotor.setupFactoryDefaults();
        leftMotor.setupInverted(false);
        rightMotor.setupInverted(false);
        leftMotor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        rightMotor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        leftMotor.setPosition(0.0);
        rightMotor.setPosition(0.0);

        leftMotor.setupForwardSoftLimit(LEFT_MOTOR_FORWARD_SOFT_LIMIT, true);
        leftMotor.setupReverseSoftLimit(LEFT_MOTOR_REVERSE_SOFT_LIMIT, true);
        rightMotor.setupForwardSoftLimit(RIGHT_MOTOR_FORWARD_SOFT_LIMIT, true);
        rightMotor.setupReverseSoftLimit(RIGHT_MOTOR_REVERSE_SOFT_LIMIT, true);

        // rightMotor.follow(leftMotor);
    }

    public void resetEncoder()
    {
        resetState = ResetState.kStart;
    }

    public void extend()
    {
        
        targetPosition = TargetPosition.kOverride;
        overrideMode = OverrideMode.kMoving;
        periodicData.motorSpeed = 0.2;
    }

    public void retract()
    {
        targetPosition = TargetPosition.kOverride;
        overrideMode = OverrideMode.kMoving;
        periodicData.motorSpeed = -0.2;
    }

    public void holdClimb()
    {
        periodicData.motorSpeed = -0.05;
    }

    public void moveToChain()
    {
        targetPosition = TargetPosition.kChain;
    }

    public void moveToOuterRobot()
    {
        targetPosition = TargetPosition.kOuterRobot;
    }

    public void moveToInnerRobot()
    {
        targetPosition = TargetPosition.kInnerRobot;
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
    }

    public void setLeftAndRightPosiiton(TargetPosition position)
    {
        if(position.value > leftMotor.getPosition())
        {
            leftMotor.set(0.05);
            rightMotor.set(0.05);        
        }
        else
        {
            leftMotor.set(0.0);
            rightMotor.set(0.0); 
        }
        // if(position.value < leftMotor.getPosition() && (Math.abs(position.value - leftMotor.getPosition()) > 5))
        // {
        //     leftMotor.set(-0.05);
        //     rightMotor.set(-0.05);        
        // }
    }

    public double getLeftPosition()
    {
        return periodicData.currentLeftPosition;
        
    }

    public double getRightPosition()
    {
        return periodicData.currentRightPosition;
    }

    public Command extendClimbCommand()
    {
        return Commands.startEnd( () -> extend(), () -> stop(), this);
    }

    public Command retractClimbCommand()
    {
        return Commands.startEnd( () -> retract(), () -> stop(), this);
    }

    public Command stopClimbCommand()
    {
        return Commands.runOnce( () -> stop(), this);
    }

    @Override
    public void readPeriodicInputs()
    {

        
        periodicData.currentLeftPosition = leftMotor.getPosition();
        periodicData.currentRightPosition = rightMotor.getPosition();
        // getPosit();
        // periodicData.currentLeftPosition = leftMotor.getPosition();
        // periodicData.currentRightPosition = rightMotor.getPosition();

        // if(periodicData.currentLeftPosition == periodicData.currentRightPosition)
        // {
        //     boolean isReverseLimitSwitchPressed = reverseLimitSwitch.isPressed();

        //     switch(reverseLSState)
        //     {
        //         case kStillReleased:
        //             if(isReverseLimitSwitchPressed)
        //             {
        //                 resetState = ResetState.kStart;
        //                 reverseLSState = LimitSwitchState.kPressed;
        //             }
        //             break;
        //         case kPressed:
        //             if(isReverseLimitSwitchPressed)
        //                 reverseLSState = LimitSwitchState.kStillPressed;
        //             else
        //                 reverseLSState = LimitSwitchState.kReleased;
        //             break;
        //         case kStillPressed:
        //             if(!isReverseLimitSwitchPressed)
        //                 reverseLSState = LimitSwitchState.kReleased;
        //             break;
        //         case kReleased:
        //             if(!isReverseLimitSwitchPressed)
        //                 reverseLSState = LimitSwitchState.kStillReleased;
        //             else
        //                 reverseLSState = LimitSwitchState.kPressed;
        //             break;
        //     }
        // }
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(targetPosition == TargetPosition.kOverride)
        {
            leftMotor.set(periodicData.motorSpeed);
            rightMotor.set(periodicData.motorSpeed);
        }
        else
        {
            if(targetPosition.value > leftMotor.getPosition())
            {
                leftMotor.set(0.05);
                rightMotor.set(0.05);        
            }
            else
            {
                leftMotor.set(0.0);
                rightMotor.set(0.0); 
            }
        }
        

        // switch(resetState)
        // {
        //     case kDone:
        //         if(position == Position.kOverride)
        //         {
        //             leftMotor.set(periodicData.motorSpeed);
        //             rightMotor.set(periodicData.motorSpeed);
        //         }
        //         else if(position == Position.kRobot)
        //         {
        //             // pidController.
        //         }
        // }
    }

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }

    @Override
    public String toString()
    {
        return "Current Encoder Position: " + getLeftPosition() + "\n" + "Current Encoder PositionV2: " + leftMotor.getPosition();

    }

}
