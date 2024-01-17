package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

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
        private double topIntakePosition = 0.0;
        private double bottomIntakePosition = 0.0;

        // OUTPUTS
        private double topIntakeSpeed = 0.0;
        private double bottomIntakeSpeed = 0.0;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final CANSparkMax topIntakeMotor = new CANSparkMax(3, MotorType.kBrushless);
    private final CANSparkMax bottomIntakeMotor = new CANSparkMax(3, MotorType.kBrushless);
    // private RelativeEncoder topIntakeEncoder;
    private RelativeEncoder bottomIntakeEncoder;

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
        // topIntakeMotor.restoreFactoryDefaults();
        bottomIntakeMotor.restoreFactoryDefaults();
        // Do Not Invert Motor Direction
        // topIntakeMotor.setInverted(false); // test later
        bottomIntakeMotor.setInverted(true); // test later

        // topIntakeEncoder = topIntakeMotor.getEncoder();
        bottomIntakeEncoder = bottomIntakeMotor.getEncoder();
    }

    public double getTopIntakePosition()
    {
        return periodicData.topIntakePosition;
    }

    public double getBottomIntakePosition()
    {
        return periodicData.bottomIntakePosition;
    }

    public double getTopIntakeSpeed()
    {
        return periodicData.topIntakeSpeed;
    }

    public double getBottomIntakeSpeed()
    {
        return periodicData.bottomIntakeSpeed;
    }

    public void forward()
    {
        periodicData.topIntakeSpeed = 0.1;
        periodicData.bottomIntakeSpeed = 0.1;
    }

    public void off()
    {
        periodicData.topIntakeSpeed = 0.0;
        periodicData.bottomIntakeSpeed = 0.0;
    }

    public void reverse()
    {
        periodicData.topIntakeSpeed = -0.1;
        periodicData.bottomIntakeSpeed = -0.1;
    }

    public void on(double speed)
    {
        periodicData.topIntakeSpeed = speed;
        periodicData.bottomIntakeSpeed = speed;
    }

    @Override
    public void readPeriodicInputs()
    {
        // periodicData.topIntakePosition = topIntakeEncoder.getPosition();
        periodicData.bottomIntakePosition = bottomIntakeEncoder.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        // topIntakeMotor.set(periodicData.topIntakeSpeed);
        bottomIntakeMotor.set(periodicData.bottomIntakeSpeed);
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
        return "Current Top Intake Position: " + periodicData.topIntakePosition + " Current Bottom Intake Position: " + periodicData.bottomIntakePosition;
    }
}
