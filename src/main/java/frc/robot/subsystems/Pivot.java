package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogEncoder;
import frc.robot.Constants;



public class Pivot extends Subsystem4237
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class PeriodicIO
    {
        // INPUTS
    
        private double currentPosition;
        private double currentAngle;

        // OUTPUTS

        private double motorSpeed;

    }

    private final CANSparkMax pivotMotor = new CANSparkMax(3, MotorType.kBrushless);

    private PeriodicIO periodicIO = new PeriodicIO();
    private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);

    public static final double STARTING_ENCODER_POSITION    = 0.0;
    public static final float ENCODER_UPWARD_SOFT_LIMIT    = 4237.0f;
    public static final float ENCODER_DOWNWARD_SOFT_LIMIT  = -4237.0f;

    //private RelativeEncoder relativeEncoder;
    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);

        configPivotMotor();
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configPivotMotor()
    {
        // Factory Defaults
        pivotMotor.restoreFactoryDefaults();


        // Soft Limits
        pivotMotor.setSoftLimit(SoftLimitDirection.kForward, ENCODER_UPWARD_SOFT_LIMIT);
        pivotMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
        pivotMotor.setSoftLimit(SoftLimitDirection.kReverse, ENCODER_DOWNWARD_SOFT_LIMIT);
        pivotMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);

    }

    public void moveUp(double speed)
    {
        periodicIO.motorSpeed = speed;
        pivotMotor.set(periodicIO.motorSpeed);
    }

    public void moveDown(double speed)
    {
        periodicIO.motorSpeed = -speed;
        pivotMotor.set(periodicIO.motorSpeed);
    }

    public void stopPivot()
    {
        periodicIO.motorSpeed = 0.0;
        pivotMotor.set(periodicIO.motorSpeed); 
    }

    public double returnPivotAngle()
    {
        return (360 * rotaryEncoder.getAbsolutePosition());
    }

    public void setAngle(double degrees)
    {
    
        do
        {
           if(returnPivotAngle() > degrees)
            {
                moveDown(0.5);
            }

            if(returnPivotAngle() < degrees)
            {
                moveUp(0.5);
            } 
        }
        while(returnPivotAngle() > (degrees - 2.0) || returnPivotAngle() < (degrees + 2.0));

        stopPivot();
    }

    @Override
    public void readPeriodicInputs()
    {

    }

    @Override
    public void writePeriodicOutputs()
    {
        //Temporary
        System.out.println("rotaryEncoder = " + 360 * rotaryEncoder.getAbsolutePosition());
        // System.out.println("getDistancePerRotation()" + rotaryEncoder.getDistancePerRotation());
        // System.out.println("getDistance()" + rotaryEncoder.getDistance());
        
    }

    @Override
    public void periodic()
    {

        periodicIO.currentAngle = returnPivotAngle();
        // This method will be called once per scheduler run
        
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}
