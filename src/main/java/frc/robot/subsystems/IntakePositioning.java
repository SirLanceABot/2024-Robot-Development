package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
        kUp(Value.kForward), kDown(Value.kReverse), kOff(Value.kOff);

        public final Value value;

        private IntakePosition(Value value)
        {
            this.value = value;
        }
    }
    
    private class PeriodicData
    {
        // INPUTS

        // OUTPUTS
        private IntakePosition intakePosition = IntakePosition.kOff;

    }

    private PeriodicData periodicData = new PeriodicData();

    // private final DoubleSolenoid intakeSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, );

    /** 
     * Creates a new IntakePositioning. 
     */
    public IntakePositioning()
    {
        super("Intake Positioning");
        System.out.println("  Constructor Started:  " + fullClassName);
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public void dropIntake()
    {

    }

    public void raiseIntake()
    {
       
    }

    @Override
    public void readPeriodicInputs()
    {
        
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