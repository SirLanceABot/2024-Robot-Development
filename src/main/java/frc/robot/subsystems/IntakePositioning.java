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

    public enum IntakePosition
    {
        kUP(Constants.IntakePositioning.INTAKE_UP_POSITION), kDOWN(Constants.IntakePositioning.INTAKE_DOWN_POSITION);

        public final double intakePositioning;

        private IntakePosition(double intakePositioning)
        {
            this.intakePositioning = intakePositioning;
        }
    }
    
    private class PeriodicData
    {
        // INPUTS
        private double intakePositioningPosition = 0.0;

        // OUTPUTS
        private double intakePositioningSpeed = 0.0;

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

        configCANSparkMax();
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configCANSparkMax()
    {
        // Factory Defaults
        intakePositioningMotor.restoreFactoryDefaults();
        // Do Not Invert Motor Direction
        intakePositioningMotor.setInverted(false); // test later

        intakePositioningEncoder = intakePositioningMotor.getEncoder();
    }

    public double getIntakePositioningPosition()
    {
        return periodicData.intakePositioningPosition;
    }

    public double getInakePositioningSpeed()
    {
        return periodicData.intakePositioningSpeed;
    }

    public void dropIntake()
    {
        periodicData.intakePositioningSpeed = 0.1;
    }

    public void raiseIntake()
    {
        periodicData.intakePositioningSpeed = -0.1;
    }

    public void intakePositioningOff()
    {
        periodicData.intakePositioningSpeed = 0.0;
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.intakePositioningPosition = intakePositioningEncoder.getPosition();
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
}