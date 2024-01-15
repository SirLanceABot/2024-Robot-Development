package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
// import com.revrobotics.CANSparkMax;

import frc.robot.Constants;

/**
 * Use this class as a template to create other subsystems.
 */
public class Shooter extends Subsystem4237
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

        // OUTPUTS

    }

    private final int OuterShooterMotorPort = Constants.Shooter.OUTER_SHOOTER_MOTOR_PORT;
    private final int InnerShooterMotorPort = Constants.Shooter.INNER_SHOOTER_MOTOR_PORT;

    // private final TalonFX shooterMotor;

    private PeriodicData periodicData = new PeriodicData();

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Shooter()
    {
        super("Shooter");
        System.out.println("  Constructor Started:  " + fullClassName);

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {}

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
