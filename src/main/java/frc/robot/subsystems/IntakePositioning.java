package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;

/**
 * Creates an intake positioning which moves the intake
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
        kUp(Value.kReverse, Value.kForward), 
        kDown(Value.kForward, Value.kReverse), 
        kFloat(Value.kReverse, Value.kReverse);

        public final Value extendValue;
        public final Value retractValue;


        private IntakePosition(Value extendValue, Value retractValue)
        {
            this.extendValue = extendValue;
            this.retractValue = retractValue;
        }
    }
    
    private final class PeriodicData
    {
        // INPUTS

        // OUTPUTS
        private IntakePosition intakePosition = IntakePosition.kUp;
    }

    private PeriodicData periodicData = new PeriodicData();

    private final DoubleSolenoid extendSolenoid = new DoubleSolenoid(
        Constants.IntakePositioning.PCM_PORT, PneumaticsModuleType.REVPH, 
        Constants.IntakePositioning.EXTEND_ACTIVE_PORT, Constants.IntakePositioning.EXTEND_FLOAT_PORT);
    private final DoubleSolenoid retractSolenoid = new DoubleSolenoid(
        Constants.IntakePositioning.PCM_PORT, PneumaticsModuleType.REVPH,
        Constants.IntakePositioning.RETRACT_ACTIVE_PORT, Constants.IntakePositioning.RETRACT_FLOAT_PORT);

    /** 
     * Creates a new IntakePositioning. 
     */
    public IntakePositioning()
    {
        super("Intake Positioning");
        System.out.println("  Constructor Started:  " + fullClassName);
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public void moveUp()
    {
        periodicData.intakePosition = IntakePosition.kDown;
    }

    public void moveDown()
    {
        periodicData.intakePosition = IntakePosition.kUp;
    }

    public void floating()
    {
        periodicData.intakePosition = IntakePosition.kFloat;
    }

    public Command moveUpCommand()
    {
        return Commands.runOnce(() -> moveUp(), this);
    }

    public Command moveDownCommand()
    {
        return Commands.runOnce(() -> moveDown(), this);
    }

    public Command floatingCommand()
    {
        return Commands.runOnce(() -> floating(), this);
    }

    @Override
    public void readPeriodicInputs()
    {
        
    }

    @Override
    public void writePeriodicOutputs()
    {
        extendSolenoid.set(periodicData.intakePosition.extendValue);
        retractSolenoid.set(periodicData.intakePosition.retractValue);
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