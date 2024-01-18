package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import frc.robot.Constants;
import frc.robot.subsystems.PoseEstimator;
import frc.robot.motors.TalonFX4237;

/**
 * Use this class as a template to create other subsystems.
 * 
 */
public class Flywheel extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // test
    
    public enum ResetState
    {
        kStart, kTry, kDone;
    }

    private class PeriodicData
    {
        // INPUTS
        // private double currentDistance;
        private double currentSpeed;

        // OUTPUTS
       
        private double flywheelSpeed;
        // private DoubleLogEntry currentDistanceEntry;
        private DoubleLogEntry currentVelocityEntry;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final TalonFX4237 motor = new TalonFX4237(Constants.Flywheel.MOTOR_PORT, Constants.Flywheel.MOTOR_CAN_BUS, "flywheelMotor");
    private RelativeEncoder encoder;

    private ResetState resetState = ResetState.kDone;
    private Pose2d fieldLocation;

    /** 
     * Creates a new Shooter. 
     */
    public Flywheel()
    {
        super("Shooter");
        System.out.println("  Constructor Started:  " + fullClassName);
        configTalonFX();
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setupCoastMode();

    }

    public void resetEncoder()
    {
        resetState = ResetState.kStart;
    }

    public double getVelocity()
    {
        return motor.getVelocity();
        
    }

    public double getPosition()
    {
        return motor.getPosition();
    }

    public void setVelocity()
    {

    }


    public void shoot()
    {
        periodicData.flywheelSpeed = 0.1;
    }


    public void intake()
    {
        periodicData.flywheelSpeed = -0.1;
    }

    public void turnOff()
    {
        periodicData.flywheelSpeed = 0.0;
    }


    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {
        motor.set(periodicData.flywheelSpeed);
    }

    @Override
    public String toString()
    {
        return "Current Shooter Speed: " + periodicData.flywheelSpeed;
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
