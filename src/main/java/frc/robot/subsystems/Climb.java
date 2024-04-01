package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        kOverride(4237);

        public final double value;
        private TargetPosition(double value)
        {
            this.value = value;
        }
    }
    
    private final class PeriodicData
    {
        // INPUTS
        private double currentLeftPosition = 0.0;
        private double currentRightPosition = 0.0;
       

        // OUTPUTS
        private DoubleLogEntry positionEntry;
        private DoubleLogEntry velocityEntry;
        // private double motorSpeed = 0.0;
        // private double rightMotorSpeed = 0.0;
    }

    private PeriodicData periodicData = new PeriodicData();
    private final CANSparkMax4237 leadMotor = new CANSparkMax4237(Constants.Climb.RIGHT_MOTOR_PORT, Constants.Climb.RIGHT_MOTOR_CAN_BUS, "rightMotor");
    private final CANSparkMax4237 followMotor = new CANSparkMax4237(Constants.Climb.LEFT_MOTOR_PORT, Constants.Climb.LEFT_MOTOR_CAN_BUS, "leftMotor");
    // private TargetPosition targetPosition = TargetPosition.kOverride;
    // private RelativeEncoder leftMotorEncoder;
    // private RelativeEncoder rightMotorEncoder;

    private final double FORWARD_SOFT_LIMIT       = 47.0;
    private final double REVERSE_SOFT_LIMIT       = 0.0;
    // private final double RIGHT_MOTOR_FORWARD_SOFT_LIMIT      = 40.0;
    // private final double RIGHT_MOTOR_REVERSE_SOFT_LIMIT      = 0.0;

    private static final double CHAIN_ENCODER_POSITION       = 45.0;
    private static final double ROBOT_ENCODER_POSITION       = 1.0;//5.0;

    private final double DEFAULT_SPEED = 0.4;
    private final double HOLDING_SPEED = 0.0;

    private final double CURRENT_LIMIT                       = 50.0;
    private final double CURRENT_THRESHOLD                   = 55.0;
    private final double TIME_THRESHOLD                      = 0.25;

    // private final double kP = 0.00003;
    // private final double kI = 0.0; // 0.0001
    // private final double kD = 0.0;
    // private final double kIz = 0.0;
    // private final double kFF = 0.0;
    // private final double kMaxOutput = 0.7;
    // private final double kMinOutput = -0.7;
    // private final double kRobotMaxOutput = 0.7;
    // private final double kRobotMinOutput = -0.7;

    private final double POSITION_TOLERANCE = 1.0;

    /** 
     * Creates a new Climb. 
     */
    public Climb()
    {
        super("Climb");
        System.out.println("  Constructor Started:  " + fullClassName);
        configMotors();
        setDefaultCommand(stopCommand());
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configMotors()
    {
        leadMotor.setupFactoryDefaults();
        followMotor.setupFactoryDefaults();
        leadMotor.setupBrakeMode();
        followMotor.setupBrakeMode();
        leadMotor.setupInverted(true);
        followMotor.setupInverted(true);
        leadMotor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        followMotor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        leadMotor.setPosition(0.0);
        followMotor.setPosition(0.0);
        followMotor.setupFollower(Constants.Climb.RIGHT_MOTOR_PORT, true);

        leadMotor.setupForwardSoftLimit(FORWARD_SOFT_LIMIT, true);
        leadMotor.setupReverseSoftLimit(REVERSE_SOFT_LIMIT, false);
        leadMotor.setupForwardHardLimitSwitch(true, true);
        leadMotor.setupReverseHardLimitSwitch(true, true);
        // rightFollowMotor.setupForwardSoftLimit(RIGHT_MOTOR_FORWARD_SOFT_LIMIT, true);
        
        // rightFollowMotor.setupReverseSoftLimit(RIGHT_MOTOR_REVERSE_SOFT_LIMIT, true);
        followMotor.setSafetyEnabled(false);
    }

    public double getLeftPosition()
    {
        return periodicData.currentLeftPosition;
        
    }

    public double getRightPosition()
    {
        return periodicData.currentRightPosition;
    }

    public void raise(double speed)
    {
        
        // targetPosition = TargetPosition.kOverride;
        // periodicData.motorSpeed = speed;
        setVoltage(Math.abs(speed) * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    public void raise()
    {
        
        // targetPosition = TargetPosition.kOverride;
        // periodicData.motorSpeed = DEFAULT_SPEED;
        setVoltage(DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    // public void extendRight(double speed)
    // {
    //     targetPosition = TargetPosition.kOverride;
    //     overrideMode = OverrideMode.kMoving;
    //     periodicData.rightMotorSpeed = speed;
    // }

    public void lower(double speed)
    {
        // targetPosition = TargetPosition.kOverride;
        // periodicData.motorSpeed = -Math.abs(speed);
        setVoltage(-Math.abs(speed) * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    public void lower()
    {
        // targetPosition = TargetPosition.kOverride;
        // periodicData.motorSpeed = -DEFAULT_SPEED;
        setVoltage(-DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    public void resetEncoders()
    {
        leadMotor.setPosition(0.0);
        followMotor.setPosition(0.0);
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

    public void hold()
    {
        // periodicData.motorSpeed = 0.05;
        // periodicData.rightMotorSpeed = 0.05;
        setVoltage(0.05 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    public void stop()
    {
        // periodicData.motorSpeed = 0.0;
        // periodicData.rightMotorSpeed = 0.0;
        set(0.0);
    }

    public void moveToChain()
    {
        // targetPosition = TargetPosition.kChain;
        if((TargetPosition.kChain.value - POSITION_TOLERANCE) > leadMotor.getPosition())
        {
            setVoltage(DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE); //0.3
            // rightFollowMotor.set(0.05);
        }
        else if((TargetPosition.kChain.value + POSITION_TOLERANCE) < leadMotor.getPosition())
        {
            setVoltage(-DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE); //-0.3
            // rightFollowMotor.set(-0.05);
        }
        else
        {
            set(0.0);
            // rightFollowMotor.set(0.0); 
        }
    }

    public void moveToRobot()
    {
        if((TargetPosition.kRobot.value - POSITION_TOLERANCE) > leadMotor.getPosition())
        {
            setVoltage(DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE); //0.3
            // rightFollowMotor.set(0.05);
        }
        else if((TargetPosition.kRobot.value + POSITION_TOLERANCE) < leadMotor.getPosition())
        {
            setVoltage(-DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE); //-0.3
            // rightFollowMotor.set(-0.05);
        }
        else
        {
            set(0.0);
            // rightFollowMotor.set(0.0); 
        }
        // targetPosition = TargetPosition.kRobot;
        // if((TargetPosition.kRobot.value - 1.0) > leadMotor.getPosition())
        //     {
        //         leadMotor.set(0.1); //0.3
        //         // rightFollowMotor.set(0.05);
        //     }
        //     else if((targetPosition.value + 1.0) < leadMotor.getPosition())
        //     {
        //         leadMotor.set(-0.1); //-0.3
        //         // rightFollowMotor.set(-0.05);
        //     }
        //     else
        //     {
        //         leadMotor.set(0.0);
        //         // rightFollowMotor.set(0.0); 
        //     }
    }

    // public void moveToSetPosition(TargetPosition targetPosition)
    // {
    //     this.targetPosition = targetPosition;
    //     // if(targetPosition == TargetPosition.kOverride)
    //     // {
    //     //     leftLeadMotor.set(periodicData.motorSpeed);
    //     //     // rightFollowMotor.set(periodicData.leftMotorSpeed);
    //     // }
    //     // else
    //     // {
    //         if((targetPosition.value - POSITION_TOLERANCE) > leadMotor.getPosition())
    //         {
    //             leadMotor.setVoltage(DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE); //0.3
    //             // rightFollowMotor.set(0.05);
    //         }
    //         else if((targetPosition.value + POSITION_TOLERANCE) < leadMotor.getPosition())
    //         {
    //             leadMotor.setVoltage(-DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE); //-0.3
    //             // rightFollowMotor.set(-0.05);
    //         }
    //         else
    //         {
    //             leadMotor.set(0.0);
    //             // rightFollowMotor.set(0.0); 
    //         }
    //     //     // leftLeadMotor.setControlPosition(targetPosition.value);
    //     // }
    // }

    private void set(double speed)
    {
        leadMotor.set(speed);
    }

    private void setVoltage(double voltage)
    {
        leadMotor.setVoltage(voltage);
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

    // /**
    //  * Command to extend climb at a desired speed
    //  * @parm Desired speed
    //  */
    // public Command extendCommand(double speed)
    // {
    //     return Commands.runEnd( () -> extend(speed), () -> stop(), this).withName("Extend Climb");
    // }

    // /**
    //  * Command to retract climb at a desired speed
    //  * @parm Desired speed
    //  */
    // public Command retractCommand(double speed)
    // {
    //     return Commands.runEnd( () -> retract(speed), () -> stop(), this).withName("Retract Climb");
    // }

    /**
     * Command to extend climb at the default speed
     */
    public Command raiseCommand()
    {
        return Commands.run( () -> raise(), this).withName("Raise Climb");
    }

    /**
     * Command to retract climb at the default speed
     */
    public Command lowerCommand()
    {
        return Commands.run( () -> lower(), this).withName("Lower Climb");
    }

    public Command moveToChainCommand()
    {
        return Commands.run( () -> moveToChain(), this).withName("Move to Chain");
    }

    public Command moveToRobotCommand()
    {
        return Commands.run( () -> moveToRobot(), this).withName("Move to Robot");
    }

    // public Command moveToPositionCommand(TargetPosition targetPosition)
    // {
    //     return Commands.runOnce( () -> moveToSetPosition(targetPosition), this).withName("Move To Position");
    // }

    public Command stopCommand()
    {
        return Commands.runOnce( () -> stop(), this).withName("Stop Climb");
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.currentLeftPosition = leadMotor.getPosition();
        periodicData.currentRightPosition = followMotor.getPosition();
        // getPosit();
        // periodicData.currentLeftPosition = leftMotor.getPosition();
        // periodicData.currentRightPosition = rightMotor.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        // if(targetPosition == TargetPosition.kOverride)  // if we are in override mode, just set the speed to whatever was given
        // {
        //     // leftLeadMotor.set(periodicData.motorSpeed);
        //     leadMotor.setVoltage(periodicData.motorSpeed * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
        // }
        // else    // if we are NOT in override mode (meaning we want to move to a set position), move to that set position
        // {
        //     if((targetPosition.value - POSITION_TOLERANCE) > leadMotor.getPosition())
        //     {
        //         // leftLeadMotor.set(DEFAULT_SPEED);
        //         leadMotor.setVoltage(DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
        //     }
        //     else if((targetPosition.value + POSITION_TOLERANCE) < leadMotor.getPosition())
        //     {
        //         // leftLeadMotor.set(-DEFAULT_SPEED);
        //         leadMotor.setVoltage(-DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
        //     }
        //     else
        //     {
        //         // leftLeadMotor.set(0.0);
        //         leadMotor.setVoltage(0.0);
        //     }
        // }
        SmartDashboard.putNumber("Current Right Climb Position", getRightPosition());

        if(leadMotor.isReverseLimitSwitchPressed() && Math.abs(periodicData.currentLeftPosition) > POSITION_TOLERANCE)
        {
            leadMotor.setPosition(0.0);
        }

        if(leadMotor.isReverseLimitSwitchPressed() && Math.abs(periodicData.currentRightPosition) > POSITION_TOLERANCE)
        {
            followMotor.setPosition(0.0);
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
        return "Current Encoder Position: " + getLeftPosition() + "\n" + "Current Encoder PositionV2: " + leadMotor.getPosition();
    }

}
