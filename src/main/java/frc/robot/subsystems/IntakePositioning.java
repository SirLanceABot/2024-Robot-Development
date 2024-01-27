package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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
        kUp(Value.kReverse), kDown(Value.kForward), kOff(Value.kOff);

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

    private final DoubleSolenoid solenoid = new DoubleSolenoid(Constants.IntakePositioning.SOLENOID_PORT, PneumaticsModuleType.CTREPCM, UP_POSITION, DOWN_POSITION);
    public static final int UP_POSITION                = 0;
    public static final int DOWN_POSITION              = 1;

    /** 
     * Creates a new IntakePositioning. 
     */
    public IntakePositioning()
    {
        super("Intake Positioning");
        System.out.println("  Constructor Started:  " + fullClassName);
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public void extend()
    {
        periodicData.intakePosition = IntakePosition.kDown;
    }

    public void retract()
    {
        periodicData.intakePosition = IntakePosition.kUp;
    }

    public Command extendCommand()
    {
        return Commands.runOnce(() -> extend(), this);
    }

    @Override
    public void readPeriodicInputs()
    {
        
    }

    @Override
    public void writePeriodicOutputs()
    {
        solenoid.set(periodicData.intakePosition.value);
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
        return "Intake Position: " + periodicData.intakePosition;
    }
}