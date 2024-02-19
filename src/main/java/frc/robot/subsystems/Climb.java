package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch;

import frc.robot.Constants;
import frc.robot.motors.CANSparkMax4237;
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
        kRobot(ROBOT_ENCODER_POSITION),
        kOverride(-4237.0);

        public final double value;
        private TargetPosition(double value)
        {
            this.value = value;
        }
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
        private double motorSpeed = 0.0;
        // private double rightMotorSpeed = 0.0;
    }

    private PeriodicData periodicData = new PeriodicData();
    private final CANSparkMax4237 leftLeadMotor = new CANSparkMax4237(Constants.Climb.LEFT_MOTOR_PORT, Constants.Climb.LEFT_MOTOR_CAN_BUS, "leftMotor");
    private final CANSparkMax4237 rightFollowMotor = new CANSparkMax4237(Constants.Climb.RIGHT_MOTOR_PORT, Constants.Climb.RIGHT_MOTOR_CAN_BUS, "rightMotor");
    private TargetPosition targetPosition = TargetPosition.kOverride;
    private OverrideMode overrideMode = OverrideMode.kNotMoving;
    // private RelativeEncoder leftMotorEncoder;
    // private RelativeEncoder rightMotorEncoder;

    private final double LEFT_MOTOR_FORWARD_SOFT_LIMIT       = 200.0;
    private final double LEFT_MOTOR_REVERSE_SOFT_LIMIT       = 0.0;
    private final double RIGHT_MOTOR_FORWARD_SOFT_LIMIT      = 40.0;
    private final double RIGHT_MOTOR_REVERSE_SOFT_LIMIT      = 0.0;

    private static final double CHAIN_ENCODER_POSITION              = 30.0;
    private static final double ROBOT_ENCODER_POSITION        = 5.0;

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

    private final double encoderResetTolerance = 3;

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
        leftLeadMotor.setupFactoryDefaults();
        rightFollowMotor.setupFactoryDefaults();
        leftLeadMotor.setupBrakeMode();
        rightFollowMotor.setupBrakeMode();
        leftLeadMotor.setupInverted(false);
        rightFollowMotor.setupInverted(false);
        // leftLeadMotor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        // rightFollowMotor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        leftLeadMotor.setPosition(0.0);
        rightFollowMotor.setPosition(0.0);
        rightFollowMotor.setupFollower(Constants.Climb.LEFT_MOTOR_PORT, true);

        leftLeadMotor.setupForwardSoftLimit(LEFT_MOTOR_FORWARD_SOFT_LIMIT, true);
        leftLeadMotor.setupReverseSoftLimit(LEFT_MOTOR_REVERSE_SOFT_LIMIT, true);
        leftLeadMotor.setupForwardHardLimitSwitch(true, true);
        leftLeadMotor.setupReverseHardLimitSwitch(true, true);
        // rightFollowMotor.setupForwardSoftLimit(RIGHT_MOTOR_FORWARD_SOFT_LIMIT, true);
        
        // rightFollowMotor.setupReverseSoftLimit(RIGHT_MOTOR_REVERSE_SOFT_LIMIT, true);
    }

    public double getLeftPosition()
    {
        return periodicData.currentLeftPosition;
        
    }

    public double getRightPosition()
    {
        return periodicData.currentRightPosition;
    }

    public void extendClimb(double speed)
    {
        
        targetPosition = TargetPosition.kOverride;
        overrideMode = OverrideMode.kMoving;
        periodicData.motorSpeed = speed;
    }

    // public void extendRight(double speed)
    // {
    //     targetPosition = TargetPosition.kOverride;
    //     overrideMode = OverrideMode.kMoving;
    //     periodicData.rightMotorSpeed = speed;
    // }

    public void retractClimb(double speed)
    {
        targetPosition = TargetPosition.kOverride;
        overrideMode = OverrideMode.kMoving;
        periodicData.motorSpeed = -Math.abs(speed);
    }

    // public void retractRight(double speed)
    // {
    //     targetPosition = TargetPosition.kOverride;
    //     overrideMode = OverrideMode.kMoving;
    //     periodicData.rightMotorSpeed = speed;
    // }

    // public void extendClimb(double speed)
    // {
    //     extendLeft(speed);
    //     // extendRight(speed);
    // }

    // public void retractClimb(double speed)
    // {
    //     retractLeft(speed);
    //     // retractRight(speed);
    // }

    public void holdClimb()
    {
        periodicData.motorSpeed = 0.05;
        // periodicData.rightMotorSpeed = 0.05;
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
        // periodicData.rightMotorSpeed = 0.0;
    }

    public void moveToChain()
    {
        targetPosition = TargetPosition.kChain;
    }

    public void moveToOuterRobot()
    {
        targetPosition = TargetPosition.kRobot;
    }

    private void moveToSetPosition(TargetPosition targetPosition)
    {
        if(targetPosition == TargetPosition.kOverride)
        {
            leftLeadMotor.set(periodicData.motorSpeed);
            // rightFollowMotor.set(periodicData.leftMotorSpeed);
        }
        else
        {
            if((targetPosition.value - 1.0) > leftLeadMotor.getPosition())
            {
                leftLeadMotor.set(0.05);
                // rightFollowMotor.set(0.05);        
            }
            else if((targetPosition.value + 1.0) < leftLeadMotor.getPosition())
            {
                leftLeadMotor.set(-0.05);
                // rightFollowMotor.set(-0.05);
            }
            else
            {
                leftLeadMotor.set(0.0);
                // rightFollowMotor.set(0.0); 
            }
            // leftLeadMotor.setControlPosition(targetPosition.value);
        }
    }

    // public Command extendLeftClimbCommand(double speed)
    // {
    //     return Commands.startEnd( () -> extendClimb(speed), () -> stop(), this);
    // }

    // public Command retractLeftClimbCommand(double speed)
    // {
    //     return Commands.startEnd( () -> retractClimb(speed), () -> stop(), this);
    // }

    // public Command extendRightClimbCommand(double speed)
    // {
    //     return Commands.startEnd( () -> extendRight(speed), () -> stop(), this);
    // }

    // public Command retractRightClimbCommand(double speed)
    // {
    //     return Commands.startEnd( () -> retractRight(speed), () -> stop(), this);
    // }

    public Command extendClimbCommand(double speed)
    {
        return Commands.runEnd( () -> extendClimb(speed), () -> stop(), this).withName("Extend Climb");
    }

    public Command retractClimbCommand(double speed)
    {
        return Commands.runEnd( () -> retractClimb(speed), () -> stop(), this).withName("Retract Climb");
    }

    public Command moveToPositionCommand(TargetPosition targetPosition)
    {
        return Commands.runOnce( () -> moveToChain(), this).withName("Move To Position");
    }

    public Command stopClimbCommand()
    {
        return Commands.runOnce( () -> stop(), this).withName("Stop Climb");
    }

    @Override
    public void readPeriodicInputs()
    {

        
        periodicData.currentLeftPosition = leftLeadMotor.getPosition();
        periodicData.currentRightPosition = rightFollowMotor.getPosition();
        // getPosit();
        // periodicData.currentLeftPosition = leftMotor.getPosition();
        // periodicData.currentRightPosition = rightMotor.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        // moveToSetPosition(targetPosition);
        leftLeadMotor.set(periodicData.motorSpeed);

        if(leftLeadMotor.isReverseLimitSwitchPressed() && Math.abs(periodicData.currentLeftPosition) > encoderResetTolerance)
        {
            leftLeadMotor.setPosition(0.0);
        }

        if(leftLeadMotor.isReverseLimitSwitchPressed() && Math.abs(periodicData.currentRightPosition) > encoderResetTolerance)
        {
            rightFollowMotor.setPosition(0.0);
        }
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
        return "Current Encoder Position: " + getLeftPosition() + "\n" + "Current Encoder PositionV2: " + leftLeadMotor.getPosition();

    }

}
