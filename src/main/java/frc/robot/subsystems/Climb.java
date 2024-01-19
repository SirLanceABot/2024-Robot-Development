package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.util.datalog.DoubleLogEntry;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;

import frc.robot.Constants;
import frc.robot.motors.TalonFX4237;

/**
 * Use this class as a template to create other subsystems.
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

    public enum Position
    {
        kChain(CHAIN_ENCODER_POSITION),
        kRobot(ROBOT_ENCODER_POSITION),
        kOverride(-4237);

        public final double value;
        private Position(double value)
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
    
    private class PeriodicData
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
    // private RelativeEncoder leftMotorEncoder;
    // private RelativeEncoder rightMotorEncoder;

    public static final int LEFT_MOTOR_FORWARD_SOFT_LIMIT       = 10000;
    public static final int LEFT_MOTOR_REVERSE_SOFT_LIMIT       = 0;
    public static final int RIGHT_MOTOR_FORWARD_SOFT_LIMIT      = 10000;
    public static final int RIGHT_MOTOR_REVERSE_SOFT_LIMIT      = 0;

    public static final int CHAIN_ENCODER_POSITION              = 10000;
    public static final int ROBOT_ENCODER_POSITION              = 10000;

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
    private Position position = Position.kOverride;
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

        leftMotor.setupForwardSoftLimit(LEFT_MOTOR_FORWARD_SOFT_LIMIT, true);
        leftMotor.setupReverseSoftLimit(LEFT_MOTOR_REVERSE_SOFT_LIMIT, true);
        rightMotor.setupForwardSoftLimit(RIGHT_MOTOR_FORWARD_SOFT_LIMIT, true);
        rightMotor.setupReverseSoftLimit(RIGHT_MOTOR_REVERSE_SOFT_LIMIT, true);
    }

    public void resetEncoder()
    {
        resetState = ResetState.kStart;
    }

    public void raiseClimb()
    {
        periodicData.motorSpeed = 0.2;
    }

    public void lowerClimb()
    {
        periodicData.motorSpeed = -0.2;
    }

    public void holdClimb()
    {
        periodicData.motorSpeed = -0.05;
    }

    public void off()
    {
        periodicData.motorSpeed = 0.0;
    }

    public void getPosit()
    {
        periodicData.currentLeftPosition = leftMotor.getPosition();
        periodicData.currentRightPosition = rightMotor.getPosition();
    }

    @Override
    public void readPeriodicInputs()
    {

        getPosit();
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
        leftMotor.set(periodicData.motorSpeed);
        rightMotor.set(periodicData.motorSpeed);

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
    public String toString()
    {
        return "Current Encoder Position: " + periodicData.currentLeftPosition + "\n" + "Current Encoder PositionV2: " + leftMotor.getPosition();

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
}
