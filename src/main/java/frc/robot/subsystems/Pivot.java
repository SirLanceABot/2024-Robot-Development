package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxLimitSwitch;
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
        private double currentAngle;
        private double currentVelocity;



        // OUTPUTS



    }

    private final CANSparkMax pivotMotor = new CANSparkMax(Constants.Pivot.PIVOT_MOTOR_PORT, MotorType.kBrushless);

    private PeriodicIO periodicIO = new PeriodicIO();

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);

        configPortMotor();
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configPortMotor()
    {
        // Factory Defaults
        pivotMotor.restoreFactoryDefaults();

    }

    @Override
    public void readPeriodicInputs()
    {
        
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
