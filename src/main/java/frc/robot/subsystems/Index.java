package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

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

        

        // OUTPUTS
        // private double motorSpeed;
        private double encoderPosition;
        // private DoubleLogEntry positiDoubleLogEntry;
    }

    private final double CURRENT_LIMIT                       = 30.0;
    private final double CURRENT_THRESHOLD                   = 35.0;
    private final double TIME_THRESHOLD                      = 0.5;

    private final double ROLLER_DIAMETER_FEET = 2.02 / 12.0; // feet
    private final double GEAR_RATIO = 3.0 / 2.0;
    // private final double MINUTES_TO_SECONDS = 1.0 / 60.0;
    private final double RPS_TO_FPS = GEAR_RATIO * (1.0 / Math.PI) * ( 1.0 / ROLLER_DIAMETER_FEET); // 2.865

    private final double kP = 0.3;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kS = 0.13;
    private final double kV = 0.01;


    // Gear ratio is 2.0 / 3.0
    private PeriodicData periodicData = new PeriodicData();
    private final TalonFX4237 motor = new TalonFX4237(Constants.Index.MOTOR_PORT, Constants.Index.MOTOR_CAN_BUS, "indexMotor");
    // private PIDController PIDcontroller = new PIDController(periodicData.kP, periodicData.kI, periodicData.kD);
    private boolean tunePID = true;
    

    /** 
     * Creates a new Index. 
     */
    public Index()
    {
        super("Index");
        System.out.println("  Constructor Started:  " + fullClassName);
        configTalonFX();
        if(tunePID)
        {
            // SmartDashboard.putNumber("kP", periodicData.kP);
            // SmartDashboard.putNumber("kI", periodicData.kI);
            // SmartDashboard.putNumber("kD", periodicData.kD);

            // SmartDashboard.putNumber("Velocity", 0.0);
        }
        
        setDefaultCommand(stopCommand());
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        motor.setupCoastMode();
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setSafetyEnabled(false);
        // motor.setupCurrentLimit(getPosition(), getVelocity(), getPosition());
        // motor.setupCurrentLimit(CURRENT_LIMIT, CURRENT_THRESHOLD, TIME_THRESHOLD);
        motor.setupPIDController(0, kP, kI, kD, kS, kV);
        // motor.setupVelocityConversionFactor(Math.PI * ROLLER_DIAMETER_FEET * (1.0 / 60.0) * 0.0833); // converts rpm to ft/s
        motor.setupVelocityConversionFactor(RPS_TO_FPS);

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
        // periodicData.motorSpeed = speed;
    }

    public void acceptNoteFromShuttle()
    {
        //periodicData.motorSpeed = 10.0;
        setControlVelocity(5.0);
    }

    public void acceptNoteFromShuttleSlowly()
    {
        //periodicData.motorSpeed = 10.0;
        setControlVelocity(1.0);
    }

    public void feedNoteToFlywheel()
    {
        // System.out.println("Feeding note");
        // periodicData.motorSpeed = 80.0;
        setControlVelocity(80.0);
    }

    public void feedNoteToFlywheelAmp()
    {
        setControlVelocity(8.0);
    }

    public void ejectNote()
    {
        // periodicData.motorSpeed = -10;
        setControlVelocity(-10.0);
    }

    public void intakeNote()
    {
        //periodicData.motorSpeed = -0.1;
        setControlVelocity(-10.0);
    }

    public void stop()
    {
        // periodicData.motorSpeed = 0.0;
        set(0.0);
    }

    private void setControlVelocity(double velocity)
    {
        motor.setControlVelocity(velocity);
    }

    private void set(double speed)
    {
        motor.set(speed);
    }

    public Command acceptNoteFromShuttleCommand()
    {
        // return Commands.runOnce(() -> motor.setVoltage(0.2 * Constants.END_OF_MATCH_BATTERY_VOLTAGE), this).withName("Accept Note From Shuttle");
        // return Commands.runOnce(() -> motor.set(0.9), this).withName("Accept Note From Shuttle");
        return Commands.run(() -> acceptNoteFromShuttle(), this).withName("Accept Note From Shuttle");
    }

    public Command acceptNoteFromShuttleSlowlyCommand()
    {
        // return Commands.runOnce(() -> motor.setVoltage(0.2 * Constants.END_OF_MATCH_BATTERY_VOLTAGE), this).withName("Accept Note From Shuttle");
        // return Commands.runOnce(() -> motor.set(0.9), this).withName("Accept Note From Shuttle");
        return Commands.run(() -> acceptNoteFromShuttleSlowly(), this).withName("Accept Note From Shuttle Slowly");
    }

    public Command feedNoteToFlywheelCommand()
    {
        return Commands.run(() -> feedNoteToFlywheel(), this).withName("Feed Note To Flywheel");
    }

    public Command feedNoteToFlywheelAmpCommand()
    {
        return Commands.run(() -> feedNoteToFlywheelAmp(), this).withName("Feed Note To Flywheel Amp");
    }

    public Command ejectNoteCommand()
    {
        return Commands.run(() -> ejectNote(), this).withName("Eject Note");
    }

    public Command intakeNoteCommand()
    {
        return Commands.run(() -> intakeNote(), this).withName("Intake Note");
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

        // if(tunePID)
        // {
            // periodicData.kP = SmartDashboard.getNumber("kP", 0.0);
            // periodicData.kI = SmartDashboard.getNumber("kI", 0.0);
            // periodicData.kD = SmartDashboard.getNumber("kD", 0.0);
        // }
        

    }

    @Override
    public void writePeriodicOutputs()
    {
        // motor.setControl(periodicData.motorSpeed);
        SmartDashboard.putNumber("currentVelocity", getVelocity());
        // if(tunePID)
        // {
            // PIDcontroller.setP(periodicData.kP);
            // PIDcontroller.setI(periodicData.kI);
            // PIDcontroller.setD(periodicData.kD);
        // }
        
        // PIDcontroller.setSetpoint(setPoint);
            
        // motor.setControlVelocity(periodicData.motorSpeed);
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
        return "Current Index Speed: " + periodicData.currentVelocity;
    }
}
