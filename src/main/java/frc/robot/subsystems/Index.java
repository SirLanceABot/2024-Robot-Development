package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.TalonFX4237;

/**
 * This class creates an index that feeds notes to the flywheel.
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

    public enum Direction1
    {
        kToFlywheel, kFromShuttle;
    }
    
    private final class PeriodicData
    {
        // INPUTS
        private double currentPosition;
        private double currentVelocity;

        // OUTPUTS
        private double motorSpeed;
        private double encoderPosition;
        // private DoubleLogEntry positiDoubleLogEntry;
    }

    private PeriodicData periodicData = new PeriodicData();
    private final TalonFX4237 motor = new TalonFX4237(Constants.Index.MOTOR_PORT, Constants.Index.MOTOR_CAN_BUS, "indexMotor");
    public static final double CURRENT_LIMIT                       = 10.0;
    public static final double CURRENT_THRESHOLD                   = 10.0;
    public static final double TIME_THRESHOLD                      = 10.0;

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
        // motor.setupCurrentLimit(getPosition(), getVelocity(), getPosition());
        motor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
    }

    public void resetEncoder()
    {
        motor.setPosition(0.0);
    }

    public void acceptNote()
    {
        periodicData.motorSpeed = 0.1;
    }

    public void feedNote(double speed)
    {
        periodicData.motorSpeed = speed;
    }


    public void reverse()
    {
        periodicData.motorSpeed = -0.1;
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
    }

    public double getPosition()
    {
        return periodicData.currentPosition;
    }

    public void setPosition(double position)
    {
        periodicData.encoderPosition = position;
    }

    public double getVelocity()
    {
        return periodicData.currentVelocity;
    }

    public void setVelocity(double speed)
    {
        periodicData.motorSpeed = speed;
    }

    public Command acceptNoteCommand()
    {
        return Commands.runOnce(() -> acceptNote(), this);
    }

    public Command feedNoteCommand(double speed)
    {
        return Commands.runOnce(() -> feedNote(speed), this);
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stop(), this);
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.currentPosition = motor.getPosition();
        periodicData.currentVelocity = motor.getVelocity();
    }

    @Override
    public void writePeriodicOutputs()
    {
        motor.set(periodicData.motorSpeed);
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
        return "Current Index Speed: " + periodicData.motorSpeed;
    }
}
