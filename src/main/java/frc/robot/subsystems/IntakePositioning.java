package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
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
        kUp(Value.kForward, Value.kReverse),
        kDown(Value.kReverse, Value.kForward),
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
        private boolean isIntakeUp;
        private boolean isIntakeDown;
        // private double pressure;

        // OUTPUTS
        // private IntakePosition intakePosition = IntakePosition.kUp;
    }

    private PeriodicData periodicData = new PeriodicData();

    private final DoubleSolenoid extendSolenoid = new DoubleSolenoid(
        Constants.IntakePositioning.PCM_PORT, PneumaticsModuleType.REVPH, 
        Constants.IntakePositioning.EXTEND_ACTIVE_PORT, Constants.IntakePositioning.EXTEND_FLOAT_PORT);
    private final DoubleSolenoid retractSolenoid = new DoubleSolenoid(
        Constants.IntakePositioning.PCM_PORT, PneumaticsModuleType.REVPH,
        Constants.IntakePositioning.RETRACT_ACTIVE_PORT, Constants.IntakePositioning.RETRACT_FLOAT_PORT);

    private final DigitalInput intakeDownSensor = new DigitalInput(Constants.IntakePositioning.INTAKE_DOWN_SENSOR);
    private final DigitalInput intakeUpSensor = new DigitalInput(Constants.IntakePositioning.INTAKE_UP_SENSOR);

    // private final PneumaticHub pneumaticHub = new PneumaticHub(Constants.IntakePositioning.PCM_PORT);

    /** 
     * Creates a new IntakePositioning. 
     */
    public IntakePositioning()
    {
        super("Intake Positioning");
        System.out.println("  Constructor Started:  " + fullClassName);
        setDefaultCommand(moveUpCommand());
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // public void configPneumaticHub()
    // {

    // }

    // public double getPressure()
    // {
    //     return periodicData.pressure;
    // }

    public void moveUp()
    {
        // periodicData.intakePosition = IntakePosition.kDown;
        extendSolenoid.set(IntakePosition.kUp.extendValue);
        retractSolenoid.set(IntakePosition.kUp.retractValue);
    }

    public void moveDown()
    {
        // periodicData.intakePosition = IntakePosition.kUp;
        extendSolenoid.set(IntakePosition.kDown.extendValue);
        retractSolenoid.set(IntakePosition.kDown.retractValue);

    }

    public void floating()
    {
        // periodicData.intakePosition = IntakePosition.kFloat;
        extendSolenoid.set(IntakePosition.kFloat.extendValue);
        retractSolenoid.set(IntakePosition.kFloat.retractValue);
    }

    public boolean isIntakeDown()
    {
        return periodicData.isIntakeDown;
    }

    public boolean isIntakeUp()
    {
        return periodicData.isIntakeUp;
    }

     public BooleanSupplier isIntakeDownSupplier()
    {
        return () -> isIntakeDown();
    }

    public Command moveUpCommand()
    {
        return Commands.runOnce(() -> moveUp(), this).withName("Move Up");
    }

    public Command moveDownCommand()
    {
        return Commands.runOnce(() -> moveDown(), this).withName("Move Down");
    }

    public Command floatingCommand()
    {
        return Commands.runOnce(() -> floating(), this).withName("Floating");
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.isIntakeUp = !intakeUpSensor.get();
        periodicData.isIntakeDown = !intakeDownSensor.get();
        // periodicData.pressure = pneumaticHub.getPressure(0);
    }

    @Override
    public void writePeriodicOutputs()
    {
        // extendSolenoid.set(periodicData.intakePosition.extendValue);
        // retractSolenoid.set(periodicData.intakePosition.retractValue);
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
        return "Is Intake Up: " + periodicData.isIntakeUp;
    }
}