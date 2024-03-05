package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;

/**
 * This class is used to operate the Amp Assist which uses pnuematics to control a bar.
 */
public class AmpAssist extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum AmpAssistPosition
    {
        kRetract(Value.kForward), kExtend(Value.kReverse);

        public final Value value;

        private AmpAssistPosition(Value value)
        {
            this.value = value;
        }
    }
    
    private final class PeriodicData
    {
        // INPUTS
        

        // OUTPUTS
        private AmpAssistPosition ampAssistCurrentPosition = AmpAssistPosition.kRetract;
    }

    private PeriodicData periodicData = new PeriodicData();

    private final DoubleSolenoid solenoid = new DoubleSolenoid(Constants.AmpAssist.SOLENOID_PORT,PneumaticsModuleType.REVPH, Constants.AmpAssist.OUT_POSITION, Constants.AmpAssist.IN_POSITION);
    

    /** 
     * Creates a new AmpAssist. 
     */
    public AmpAssist()
    {
        super("Amp Assist");
        System.out.println("  Constructor Started:  " + fullClassName);
        // setDefaultCommand(retractCommand());
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // public AmpAssistPosition getPosition()
    // {
    //     return periodicData.ampAssistPosition;
    // }

    public BooleanSupplier isAmpAssistRetracted()
    {
        return () ->
        {
            if(periodicData.ampAssistCurrentPosition == AmpAssistPosition.kRetract)
            {
                return true;
            }
            else
            {
                return false;
            }
        };
    }

    public void extend()
    {
        periodicData.ampAssistCurrentPosition = AmpAssistPosition.kExtend;
        solenoid.set(AmpAssistPosition.kExtend.value);
    }

    public void retract()
    {
        periodicData.ampAssistCurrentPosition = AmpAssistPosition.kRetract;
        solenoid.set(AmpAssistPosition.kRetract.value);
    }

    public Command extendCommand()
    {
        return Commands.runOnce(() -> extend(), this);
    }

    public Command retractCommand()
    {
        return Commands.runOnce(() -> retract(), this);
    }

    // public Command ampAssistCommand(AmpAssistPosition ampAssistPosition)
    // {
    //     if(ampAssistPosition == AmpAssistPosition.kRetract || ampAssistPosition == AmpAssistPosition.kOff)
    //     {
    //         return Commands.runOnce( () -> extend(), this);
    //     }
    //     else if(ampAssistPosition == AmpAssistPosition.kExtend)
    //     {
    //         return Commands.runOnce( () -> retract(), this);
    //     }
    //     else
    //     {
    //         return Commands.runOnce( () -> stop(), this);
    //     }

    //     // return this.runOnce( () -> extend());
    // }

   

    @Override
    public void readPeriodicInputs()
    {
        
    }

    @Override
    public void writePeriodicOutputs()
    {
        // solenoid.set(periodicData.ampAssistPosition.value);
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
        return "Current AmpAssist Position: " + periodicData.ampAssistCurrentPosition;
    }
}
