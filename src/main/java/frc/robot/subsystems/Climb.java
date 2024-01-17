package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.RelativeEncoder;

import frc.robot.Constants;

/**
 * Use this class as a template to create other subsystems.
 */
public class Climb extends Subsystem4237
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
    private int leftMotorPort = Constants.Climb.LEFT_MOTOR_PORT;
    private int rightMotorPort = Constants.Climb.RIGHT_MOTOR_PORT;
    private final TalonFX leftMotor = new TalonFX(leftMotorPort);
    private final TalonFX rightMotor = new TalonFX(rightMotorPort);
    private RelativeEncoder leftMotorEncoder;
    private RelativeEncoder rightMotorEncoder;
    
    

    /** 
     * Creates a new Climb. 
     */
    public Climb()
    {
        super("Climb");
        System.out.println("  Constructor Started:  " + fullClassName);

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        leftMotor.setInverted(false);
        rightMotor.setInverted(false);
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
