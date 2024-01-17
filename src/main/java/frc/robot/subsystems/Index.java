package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import frc.robot.Constants;

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

        // OUTPUTS
        private double motorSpeed;
    }

    private PeriodicData periodicData = new PeriodicData();
    private final int IndexMotorPort = Constants.Index.INDEX_MOTOR_PORT;
    private final TalonFX indexMotor = new TalonFX(1);
    private RelativeEncoder indexEncoder;

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
        // Factory Defaults
        // outerShooterMotor.config
        indexMotor.setInverted(false);
        indexMotor.setNeutralMode(NeutralModeValue.Coast);
        // indexEncoder = indexMotor.getEncoder();
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

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {
        indexMotor.set(periodicData.motorSpeed);
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
