package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import com.revrobotics.RelativeEncoder; 
import frc.robot.Constants;
import frc.robot.motors.CANSparkMax4237;


/**
 *This is shuttle / transfer from intake to shooter
 *Creates a new shuttle
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


    //makes a new motor, can spark max instantiation
    private final CANSparkMax4237 motor = new CANSparkMax4237(Constants.Shuttle.MOTOR_PORT, Constants.Shuttle.MOTOR_CAN_BUS, "Shuttle Motor");
    // private final CANSparkMax shuttleMotor = new CANSparkMax(Constants.Shuttle.SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    
    private final double GEAR_RATIO = 3.0 / 20.0; // Roller spins three times, the motor spins 20 times
    private final double ROLLER_CIRCUMFRENCE_INCHES = Math.PI * 2.25; 
    private final double SHUTTLE_SOFT_LIMIT = 3.00;

    private class PeriodicData
    {
 
        private double motorSpeed = 0.0;
        private double position = 0.0;

        // OUTPUTS

    }

    private boolean reset = false;

    private final PeriodicData periodicData = new PeriodicData();
    
    

    /** 
     * Creates a new subsystem for a shuttle. 
     */
    public Shuttle()
    {
        super("Shuttle");
        System.out.println("  Constructor Started: " + fullClassName);

        motor.setupFactoryDefaults();

        
        configMotor();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * sets a soft limit, hard limit, brake mode and conversion factor for the Shuttle motor
     */
    private void configMotor()
    {
        motor.setupCoastMode();
        motor.setupForwardSoftLimit(SHUTTLE_SOFT_LIMIT * ROLLER_CIRCUMFRENCE_INCHES, true);
        motor.setupReverseSoftLimit(-SHUTTLE_SOFT_LIMIT * ROLLER_CIRCUMFRENCE_INCHES, true);

        // motor.enableSoftLimit(SoftLimitDirection.kForward, true); // kForward is upward movement
        // motor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled); // kReverse is downward movement
        // motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        //motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        
        // creates a limit switch for the shuttle motor
        motor.setupForwardHardLimitSwitch(true, true);
        motor.setupReverseHardLimitSwitch(true, true);

        motor.setupPositionConversionFactor(ROLLER_CIRCUMFRENCE_INCHES * GEAR_RATIO);
    }

    public double getPosition()
    {
        return periodicData.position;
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
        periodicData.motorSpeed = 0.1;
    }

    
    /**
     * Turns on the motor to move the ring down and away from the shooter
     */
    public void moveDownward()
    {
        periodicData.motorSpeed = -0.1;
    }

    /**
     * Resets the Encoder value to 0.
     */
    public void resetEncoder()
    {
        reset = true;
    }

    public Command moveUpwardCommand()
    {
        return Commands.runOnce(() -> moveUpward(), this).withName("Move Downward");
    }

    public Command moveDownwardCommand()
    {
        return Commands.runOnce(() -> moveDownward(), this).withName("Move Downward");
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stop(), this).withName("Stop");
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
        
        if (reset)
        {
            motor.setPosition(0.0);
        }
        
        reset = false;
        
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

    @Override
    public String toString()
    {
        return("Encoder = " + motor.getPosition());
    }

}
