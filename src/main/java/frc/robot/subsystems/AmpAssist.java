package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.RelativeEncoder;

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
    
    private class PeriodicData
    {
        // INPUTS
        private double ampAssistPosition = 0.0;

        // OUTPUTS
        private double ampAssistSpeed = 0.0;
    }

    private PeriodicData periodicData = new PeriodicData();

    private final CANSparkMax4237 motor = new CANSparkMax4237(Constants.AmpAssist.MOTOR_PORT, Constants.AmpAssist.MOTOR_CAN_BUS, "ampAssistMotor");
    private RelativeEncoder encoder;

    /** 
     * Creates a new AmpAssist. 
     */
    public AmpAssist()
    {
        super("Amp Assist");
        System.out.println("  Constructor Started:  " + fullClassName);

        configCANSparkMax();
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configCANSparkMax()
    {
        // Restore factory Defaults
        motor.setupFactoryDefaults();

        // Do Not invert Motor
        motor.setupInverted(false);

        motor.setupForwardSoftLimit(-75, true);
        motor.setupReverseSoftLimit(-100, true);

        encoder.setPosition(0.0);
    }

    public double getAmpAssistPosition()
    {
        return periodicData.ampAssistPosition;
    }

    public void moveOut()
    {
        periodicData.ampAssistSpeed = 0.1;
    }

    public void moveIn()
    {
        periodicData.ampAssistSpeed = -0.1;
    }

    public void off()
    {
        periodicData.ampAssistSpeed = 0.0;
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.ampAssistPosition = motor.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        motor.set(periodicData.ampAssistSpeed);
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
