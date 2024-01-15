package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;

/**
 * Use this class as a template to create other subsystems.
 */
public class IntakePositioning extends Subsystem4237
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

    private PeriodicData periodicData = new PeriodicData();

    private final CANSparkMax intakePositioningMotor = new CANSparkMax(Constants.IntakePositioning.INTAKE_POSITIONING_MOTOR_PORT, MotorType.kBrushless);
    private RelativeEncoder intakePositioningEncoder;

    /** 
     * Creates a new IntakePositioning. 
     */
    public IntakePositioning()
    {
        super("Intake Positioning");
        System.out.println("  Constructor Started:  " + fullClassName);

        // configCANSparkMax();
        
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