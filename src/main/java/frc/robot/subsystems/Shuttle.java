package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkLowLevel.MotorType;


import frc.robot.Constants;
import frc.robot.PeriodicIO;

/**
 *This is shuttle / transfer from intake to shooter
 */
public class Shuttle extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // private final CANSparkMax shuttleMotor = new CANSparkMax(Constants.Shuttle.SHUTTLE_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax shuttleMotor = new CANSparkMax(3, MotorType.kBrushless);

    private SparkLimitSwitch forwardLimit;
    private SparkLimitSwitch reverseLimit;

    private class PeriodicData
    {
 
        private double shuttleMotorSpeed = 0.0;

        // OUTPUTS

    }

    private PeriodicData periodicData = new PeriodicData();

    /** 
     * Creates a new subsystem for a shuttle. 
     */
    public Shuttle()
    {
        super("Shuttle");
        System.out.println("  Constructor Started: " + fullClassName);

        shuttleMotor.restoreFactoryDefaults();
        forwardLimit = shuttleMotor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        forwardLimit.enableLimitSwitch(true);
        reverseLimit = shuttleMotor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        reverseLimit.enableLimitSwitch(true);

        shuttleMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 15);
        shuttleMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, 0);

        shuttleMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        shuttleMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);

    
        
        System.out.println("  Constructor Finished: " + fullClassName);
    
    }

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {
        shuttleMotor.set(periodicData.shuttleMotorSpeed);
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
   
    /**
     * shuts off motor
     */
    public void off()
    {
        periodicData.shuttleMotorSpeed = 0.0;
    }

    /** 
     * Turns the motor on 
     * @param speed (double)*/
    public void forward()
    {
        periodicData.shuttleMotorSpeed = 0.1;
    }

    public void reverse()
    {
        periodicData.shuttleMotorSpeed = -0.1;
    }

}
