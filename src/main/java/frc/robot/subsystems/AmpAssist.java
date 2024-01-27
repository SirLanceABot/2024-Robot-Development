package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.motors.CANSparkMax4237;

/**
 * Use this class as a template to create other subsystems.
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
        kIn(Value.kForward), kOut(Value.kReverse), kOff(Value.kOff);

        public final Value value;

        private AmpAssistPosition(Value value)
        {
            this.value = value;
        }
    }
    
    private class PeriodicData
    {
        // INPUTS
        

        // OUTPUTS
        private AmpAssistPosition ampAssistPosition = AmpAssistPosition.kOff;
    }

    private PeriodicData periodicData = new PeriodicData();

    private final DoubleSolenoid solenoid = new DoubleSolenoid(Constants.AmpAssist.SOLENOID_PORT,PneumaticsModuleType.CTREPCM, OUT_POSITION, IN_POSITION);
    public static final int OUT_POSITION             = 0;
    public static final int IN_POSITION              = 1;

    /** 
     * Creates a new AmpAssist. 
     */
    public AmpAssist()
    {
        super("Amp Assist");
        System.out.println("  Constructor Started:  " + fullClassName);
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public void extend()
    {
        periodicData.ampAssistPosition = AmpAssistPosition.kOut;
    }

    public void retract()
    {
        periodicData.ampAssistPosition = AmpAssistPosition.kIn;
    }
    public void stop()
    {
        periodicData.ampAssistPosition = AmpAssistPosition.kOff;
    }

    public Command climbCommand(AmpAssistPosition ampAssistPosition)
    {
        if(ampAssistPosition == AmpAssistPosition.kIn || ampAssistPosition == AmpAssistPosition.kOff)
        {
            return this.runOnce( () -> extend());
        }
        else if(ampAssistPosition == AmpAssistPosition.kOut)
        {
            return this.runOnce( () -> retract());
        }
        else
        {
            return this.runOnce( () -> stop());
        }

        // return this.runOnce( () -> extend());
    }

    // public Command shootCommand(double angle)
    // {
    //     return this.runOnce( new ParallelCommandGroup(
    //     () -> flywheel.shoot(0.4),
    //     () -> drivetrain.rotateForShooting(),
    //     () -> pivot.setAngle(angle, 0.02),
    //     this.waitUntil(flywheel.getVelocity() == 0.4),
    //     () -> index.feedNote(0.4)
    //     ));
    // }

    @Override
    public void readPeriodicInputs()
    {
        
    }

    @Override
    public void writePeriodicOutputs()
    {
        solenoid.set(periodicData.ampAssistPosition.value);
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
        return "Current AmpAssist Position: " + periodicData.ampAssistPosition;
    }
}
