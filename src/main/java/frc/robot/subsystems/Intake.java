package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;

/**
 * Use this class as a template to create other subsystems.
 */
public class Intake extends Subsystem4237
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
        private double intakePosition = 0.0;

        // OUTPUTS
        private double intakeSpeed = 0.0;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final CANSparkMax intakeMotor = new CANSparkMax(Constants.Intake.INTAKE_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder intakeEncoder;

    /** 
     * Creates a new Intake. 
     */
    public Intake()
    {
        super("Intake");
        System.out.println("  Constructor Started:  " + fullClassName);

        configCANSparkMax();
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configCANSparkMax()
    {
        // Factory Defaults
        intakeMotor.restoreFactoryDefaults();
        // Do Not Invert Motor Direction
        intakeMotor.setInverted(false); // test later

        intakeEncoder = intakeMotor.getEncoder();
    }

    public double getIntakePosition()
    {
        return periodicData.intakePosition;
    }

    public double getIntakeSpeed()
    {
        return periodicData.intakeSpeed;
    }

    public void intakeForward()
    {
        periodicData.intakeSpeed = 0.3;
    }

    public void intakeOff()
    {
        periodicData.intakeSpeed = 0.0;
    }

    public void intakeReverse()
    {
        periodicData.intakeSpeed = -0.3;
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.intakePosition = intakeEncoder.getPosition();
    }

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

    @Override
    public String toString()
    {
        return "Current Intake Position: " + periodicData.intakePosition;
    }
}
