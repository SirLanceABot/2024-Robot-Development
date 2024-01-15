package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.Constants;



public class Pivot extends Subsystem4237
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    public enum MovementState
    {
        kNotMoving, kMoving
    }

    private class PeriodicIO
    {
        // INPUTS
    
        private double currentPosition;
        private double currentVelocity;



        // OUTPUTS



    }

    private final CANSparkMax pivotMotor = new CANSparkMax(Constants.Pivot.PIVOT_MOTOR_PORT, MotorType.kBrushless);

    private PeriodicIO periodicIO = new PeriodicIO();

    private RelativeEncoder relativeEncoder;

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);

        configPivotMotor();
        relativeEncoder.setPosition(Constants.Pivot.STARTING_ENCODER_POSITION);
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configPivotMotor()
    {
        // Factory Defaults
        pivotMotor.restoreFactoryDefaults();

    }

    @Override
    public void readPeriodicInputs()
    {
        periodicIO.currentPosition = relativeEncoder.getPosition();
        periodicIO.currentVelocity = relativeEncoder.getVelocity();
    }

    @Override
    public void writePeriodicOutputs()
    {

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
}
