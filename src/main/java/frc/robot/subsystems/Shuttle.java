package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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
    
    private final double GEAR_RATIO = 1; // Roller spins three times, the motor spins 20 times
    private final double ROLLER_CIRCUMFERENCE_INCHES = 2.316194; 
    private final double SHUTTLE_SOFT_LIMIT = 3.00;
    // private final double END_OF_MATCH_VOLTAGE = 11.5;
    // private final double PERCENT_VOLTAGE = 0.8;
    // private final double VOLTAGE = PERCENT_VOLTAGE * END_OF_MATCH_VOLTAGE;
    private final double DEFAULT_SPEED = 1.0;


    private class PeriodicData
    {
 
        // private double motorSpeed = 0.0;
        private double position = 0.0;
        // private double motorVoltage = 0.0;

        // OUTPUTS

    }

    // private boolean reset = false;

    private final PeriodicData periodicData = new PeriodicData();
    
    

    /** 
     * Creates a new subsystem for a shuttle. 
     */
    public Shuttle()
    {
        super("Shuttle");
        System.out.println("  Constructor Started: " + fullClassName);
        
        configMotor();
        setDefaultCommand(stopCommand());

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * sets a soft limit, hard limit, brake mode and conversion factor for the Shuttle motor
     */
    private void configMotor()
    {
        motor.setupFactoryDefaults();
        motor.setupCoastMode();
        motor.setupForwardSoftLimit(SHUTTLE_SOFT_LIMIT * ROLLER_CIRCUMFERENCE_INCHES, false);
        motor.setupReverseSoftLimit(-SHUTTLE_SOFT_LIMIT * ROLLER_CIRCUMFERENCE_INCHES, false);

        // motor.enableSoftLimit(SoftLimitDirection.kForward, true); // kForward is upward movement
        // motor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled); // kReverse is downward movement
        // motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        //motor.enableSoftLimit(SoftLimitDirection.kForward, true);
        
        // creates a limit switch for the shuttle motor
        motor.setupForwardHardLimitSwitch(false, true);
        motor.setupReverseHardLimitSwitch(false, true);

        motor.setupPositionConversionFactor(ROLLER_CIRCUMFERENCE_INCHES * GEAR_RATIO);

        motor.setSafetyEnabled(false);
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
        // periodicData.motorSpeed = 0.0;
        // periodicData.motorVoltage = 0.0;
        set(0.0);
    }

    

    /** 
     * Turns on the motor to move the ring up and towards the shooter
     */
    public void moveUpward()
    {
        // periodicData.motorSpeed = 0.9;
        // periodicData.motorVoltage = VOLTAGE;
        // periodicData.motorSpeed = DEFAULT_SPEED;
        setVoltage(DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    
    /**
     * Turns on the motor to move the ring down and away from the shooter
     */
    public void moveDownward()
    {
        // periodicData.motorSpeed = -0.1;
        // periodicData.motorVoltage = -VOLTAGE;
        // periodicData.motorSpeed = -DEFAULT_SPEED;
        setVoltage(-DEFAULT_SPEED * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    }

    /**
     * Resets the Encoder value to 0.
     */
    public void resetEncoder()
    {
        // reset = true;
        motor.setPosition(0.0);

    }

    /**
     * Private method which calls motor.set()
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     */
    public void set(double speed)
    {
        motor.set(speed);
    }

    /**
     * Private method which calls motor.setVoltage()
     * @param voltage The voltage to output
     */
    private void setVoltage(double voltage)
    {
        motor.setVoltage(voltage);
    }

    public Command moveUpwardCommand()
    {
        return Commands.run(() -> moveUpward(), this).withName("Move Upward");
    }

    public Command moveDownwardCommand()
    {
        return Commands.run(() -> moveDownward(), this).withName("Move Downward");
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
        // motor.set(periodicData.motorSpeed);
        // motor.setVoltage(periodicData.motorVoltage);
        // motor.setVoltage(periodicData.motorSpeed * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
        
        // if (reset)
        // {
        //     motor.setPosition(0.0);
        //     reset = false;
        // }
         
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
