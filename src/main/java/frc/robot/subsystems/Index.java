package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        private double kP = SmartDashboard.getNumber("kP", 0.0);
        private double kI = SmartDashboard.getNumber("kI", 0.0);
        private double kD = SmartDashboard.getNumber("kD", 0.0);

        // OUTPUTS
        private double motorSpeed;
        private double encoderPosition;
        // private DoubleLogEntry positiDoubleLogEntry;
    }

    public static final double CURRENT_LIMIT                       = 10.0;
    public static final double CURRENT_THRESHOLD                   = 10.0;
    public static final double TIME_THRESHOLD                      = 10.0;
    public static final double ROLLER_RADIUS                       = 1.125; // inches

    // Gear ratio is 2.0 / 3.0
    private PeriodicData periodicData = new PeriodicData();
    private final TalonFX4237 motor = new TalonFX4237(Constants.Index.MOTOR_PORT, Constants.Index.MOTOR_CAN_BUS, "indexMotor");
    private PIDController PIDcontroller = new PIDController(periodicData.kP, periodicData.kI, periodicData.kD);
    

    /** 
     * Creates a new Index. 
     */
    public Index()
    {
        super("Index");
        System.out.println("  Constructor Started:  " + fullClassName);
        configTalonFX();
        SmartDashboard.putNumber("kP", periodicData.kP);
        SmartDashboard.putNumber("kI", periodicData.kI);
        SmartDashboard.putNumber("kD", periodicData.kD);

        SmartDashboard.putNumber("Velocity", 0.0);

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        motor.setupCoastMode();
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        // motor.setupCurrentLimit(getPosition(), getVelocity(), getPosition());
        // motor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        motor.setupPIDController(0, periodicData.kP, periodicData.kI, periodicData.kD);
        motor.setupVelocityConversionFactor(2 * Math.PI * ROLLER_RADIUS * (1.0 / 60.0) * 0.0833); // converts rpm to ft/s

    }

    public void resetEncoder()
    {
        motor.setPosition(0.0);
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

    public void acceptNoteFromShuttle()
    {
        periodicData.motorSpeed = 0.1;
    }

    public void feedNoteToFlywheel(double speed)
    {
        periodicData.motorSpeed = speed;
    }


    public void intake()
    {
        periodicData.motorSpeed = -0.1;
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
    }

    public Command acceptNoteFromShuttleCommand()
    {
        return Commands.runOnce(() -> acceptNoteFromShuttle(), this).withName("Accept Note From Shuttle");
    }

    public Command feedNoteToFlywheelCommand(double speed)
    {
        return Commands.runEnd(() -> feedNoteToFlywheel(speed), () -> stop(), this).withName("Feed Note To Flywheel");
    }

    public Command intakeCommand()
    {
        return Commands.runOnce(() -> intake(), this).withName("Intake");
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stop(), this).withName("Stop");
    }

    //Returns speed in feet per minute
    // public double convertToSurfaceAreaSpeed(double speed)
    // {
    //     return speed * 6380 * 2.5 * Math.PI;
    // }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.currentPosition = motor.getPosition();
        periodicData.currentVelocity = motor.getVelocity();

        periodicData.kP = SmartDashboard.getNumber("kP", 0.0);
        periodicData.kI = SmartDashboard.getNumber("kI", 0.0);
        periodicData.kD = SmartDashboard.getNumber("kD", 0.0);

    }

    @Override
    public void writePeriodicOutputs()
    {
        // motor.setControl(periodicData.motorSpeed);
        SmartDashboard.putNumber("currentVelocity", getVelocity());
        PIDcontroller.setP(periodicData.kP);
        PIDcontroller.setI(periodicData.kI);
        PIDcontroller.setD(periodicData.kD);
        // PIDcontroller.setSetpoint(setPoint);
            
        motor.setControlVelocity(periodicData.motorSpeed);
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
