package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import com.revrobotics.RelativeEncoder; 
import frc.robot.Constants;
import frc.robot.motors.CANSparkMax4237;


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

    /**
     * makes a new motor
     */
    private final CANSparkMax4237 motor = new CANSparkMax4237(Constants.Shuttle.MOTOR_PORT, Constants.Shuttle.MOTOR_CAN_BUS, "Shuttle Motor");
    // private final CANSparkMax shuttleMotor = new CANSparkMax(Constants.Shuttle.SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    
    private final double GEAR_RATIO = 3.0 / 20.0; // Roller spins three times, the motor spins 20 times
    private final double ROLLER_CIRCUMFRENCE_INCHES = Math.PI * 2.25; 

    private class PeriodicData
    {
 
        private double motorSpeed = 0.0;
        private double position = 0.0;

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

        motor.setupFactoryDefaults();

        // creates a limit switch for the shuttle motor
        
        configMotor();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public double getPosition()
    {
        return periodicData.position;
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.position = motor.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        motor.set(periodicData.motorSpeed);
    }

    @Override
    public void periodic()
    {
        //System.out.println("Encoder = " + motor.getPosition());
        
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
    public void stop()
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
        motor.setupBrakeMode();
        motor.setupForwardSoftLimit(3.0 * ROLLER_CIRCUMFRENCE_INCHES, true);
        motor.setupReverseSoftLimit(-3.0 * ROLLER_CIRCUMFRENCE_INCHES, true);

        // motor.enableSoftLimit(SoftLimitDirection.kForward, true); // kForward is upward movement
        // motor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled); // kReverse is downward movement
        // motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        //motor.enableSoftLimit(SoftLimitDirection.kForward, true);
    
        motor.setupForwardHardLimitSwitch(true, true);
        motor.setupReverseHardLimitSwitch(true, true);

        motor.setupPositionConversionFactor(ROLLER_CIRCUMFRENCE_INCHES * GEAR_RATIO);
    }

    /**
     * Resets the Encoder value to 0.
     */
    public void resetEncoder()
    {
        motor.setPosition(0.0);
    }
}
