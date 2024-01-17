package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.util.datalog.DoubleLogEntry;

import com.revrobotics.RelativeEncoder;

import frc.robot.Constants;

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
    
    private class PeriodicData
    {
        // INPUTS
        private double currentPosition;
        private double currentVelocity;

        // OUTPUTS
        private DoubleLogEntry positiLogEntry;
        private DoubleLogEntry velocityLogEntry;
        private double motorSpeed;
    }

    private PeriodicData periodicData = new PeriodicData();
    private int leftMotorPort = Constants.Climb.LEFT_MOTOR_PORT;
    private int rightMotorPort = Constants.Climb.RIGHT_MOTOR_PORT;
    private final TalonFX leftMotor = new TalonFX(leftMotorPort);
    private final TalonFX rightMotor = new TalonFX(rightMotorPort);
    private RelativeEncoder leftMotorEncoder;
    private RelativeEncoder rightMotorEncoder;
    
    

    /** 
     * Creates a new Climb. 
     */
    public Climb()
    {
        super("Climb");
        System.out.println("  Constructor Started:  " + fullClassName);

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        leftMotor.setInverted(false);
        rightMotor.setInverted(false);
        leftMotor.setNeutralMode(NeutralModeValue.Brake);
        rightMotor.setNeutralMode(NeutralModeValue.Brake);

        // rightMaster.configForwardSoftLimitThreshold(10000, 0);
        // rightMaster.configReverseSoftLimitThreshold(-10000, 0);
        // rightMaster.configForwardSoftLimitEnable(true, 0);
        // rightMaster.configReverseSoftLimitEnable(true, 0);

        // leftMotor.configForwardSoftLimitThreshold(Constants.Climb.LEFT_MOTOR_UPPER_SOFT_LIMIT, 0);



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

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {
        leftMotor.set(periodicData.motorSpeed);
        rightMotor.set(periodicData.motorSpeed);
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
