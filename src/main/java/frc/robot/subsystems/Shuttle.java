package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import com.revrobotics.RelativeEncoder; 
import frc.robot.Constants;


/**
 *This is shuttle / transfer from intake to shooter
 */
public class Shuttle extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // private final CANSparkMax shuttleMotor = new CANSparkMax(Constants.Shuttle.SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax motor = new CANSparkMax(Constants.Shuttle.MOTOR_PORT, MotorType.kBrushless);

    private final SparkLimitSwitch upwardLimit;
    private final SparkLimitSwitch downwardLimit;
    private final RelativeEncoder encoder;
    private final double GEAR_RATIO = 3.0 / 20.0; // Roller spins three times, the motor spins 20 times
    private final double ROLLER_CIRCUMFRENCE_INCHES = Math.PI * 2.25; 
    private class PeriodicData
    {
 
        private double motorSpeed = 0.0;

        // OUTPUTS

    }

    private PeriodicData periodicData = new PeriodicData();
    
    // creates shuttle encoder
    

    /** 
     * Creates a new subsystem for a shuttle. 
     */
    public Shuttle()
    {
        super("Shuttle");
        System.out.println("  Constructor Started: " + fullClassName);

        motor.restoreFactoryDefaults();

        // creates a limit switch for the shuttle motor
        upwardLimit = motor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        downwardLimit = motor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);

        encoder = motor.getEncoder();

        configMotor();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {
        motor.set(periodicData.motorSpeed);
    }

    @Override
    public void periodic()
    {
        System.out.println("Encoder = " + encoder.getPosition());
        
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
   
    /**
     * shuts off motor
     */
    public void off()
    {
        periodicData.motorSpeed = 0.0;
    }

    /** 
     * Turns on the motor to move the ring up and towards the shooter
     */
    public void moveUpward()
    {
        periodicData.motorSpeed = 0.02;
    }

    
    /**
     * Turns on the motor to move the ring down and away from the shooter
     */
    public void moveDownward()
    {
        periodicData.motorSpeed = -0.02;
    }


    /**
     * sets a soft limit, hard limit, brake mode and conversion factor for the Shuttle motor
     */
    private void configMotor()
    {
        motor.setIdleMode(IdleMode.kBrake);
        motor.setSoftLimit(SoftLimitDirection.kForward, (float) (3.0 * ROLLER_CIRCUMFRENCE_INCHES)); // kForward is upward movement
        motor.setSoftLimit(SoftLimitDirection.kReverse, (float) (-3.0 * ROLLER_CIRCUMFRENCE_INCHES)); // kReverse is downward movement

        motor.enableSoftLimit(SoftLimitDirection.kForward, true); // kForward is upward movement
        motor.enableSoftLimit(SoftLimitDirection.kReverse, true); // kReverse is downward movement
    
        upwardLimit.enableLimitSwitch(true);
        downwardLimit.enableLimitSwitch(true);

        encoder.setPositionConversionFactor(ROLLER_CIRCUMFRENCE_INCHES * GEAR_RATIO);
    }

    /**
     * Resets the Encoder value to 0.
     */
    public void resetEncoder()
    {
        encoder.setPosition(0.0);
    }
}
