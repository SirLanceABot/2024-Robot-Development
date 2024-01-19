package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import frc.robot.Constants;
import frc.robot.motors.TalonFX4237;

/**
 * Use this class as a template to create other subsystems.
 */
public class Index extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    private class PeriodicData
    {
        // INPUTS
        private double currentPosition;
        private double currentSpeed;

        // OUTPUTS
        private double motorSpeed;
        private double encoderPosition;
        private DoubleLogEntry positiDoubleLogEntry;
    }

    private PeriodicData periodicData = new PeriodicData();
    private final TalonFX4237 motor = new TalonFX4237(Constants.Index.MOTOR_PORT, Constants.Index.MOTOR_CAN_BUS, "indexMotor");

    /** 
     * Creates a new Index. 
     */
    public Index()
    {
        super("Index");
        System.out.println("  Constructor Started:  " + fullClassName);
        configTalonFX();

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        motor.setupCoastMode();
        motor.setupFactoryDefaults();
        motor.setupInverted(true);
        motor.setupCurrentLimit(getPosition(), getVelocity(), getPosition());
    }

    public void acceptNote()
    {
        periodicData.motorSpeed = 0.1;
    }

    public void feedNote()
    {
        periodicData.motorSpeed = 0.25;
    }


    public void reverse()
    {
        periodicData.motorSpeed = -0.1;
    }

    public void turnOff()
    {
        periodicData.motorSpeed = 0.0;
    }

    public double getPosition()
    {
        return motor.getPosition();
    }

    public void setPosition(double position)
    {
        periodicData.encoderPosition = position;
    }

    public double getVelocity()
    {
        return motor.getVelocity();
    }

    public void setVelocity(double speed)
    {
        periodicData.motorSpeed = speed;
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
    public String toString()
    {
        return "Current Index Speed: " + periodicData.motorSpeed;
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
