package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.motors.CANSparkMax4237;

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

    private final CANSparkMax4237 pivotMotor = new CANSparkMax4237(3, "rio", "pivotMotor");

    private PeriodicIO periodicIO = new PeriodicIO();
    private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);


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
        pivotMotor.setupFactoryDefaults();


        // Soft Limits
        pivotMotor.setupForwardSoftLimit(10000.0, true);
        pivotMotor.setupReverseSoftLimit(-10000.0, true);

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
        return (360.0 * rotaryEncoder.getAbsolutePosition());
    }

    public void setAngle(double degrees)
    {
        do
        {
           if(periodicIO.currentAngle > degrees)
            {
                moveDown(0.5);
            }

            if(periodicIO.currentAngle < degrees)
            {
                moveUp(0.5);
            } 
        }
        while(periodicIO.currentAngle > (degrees - 2.0) || periodicIO.currentAngle < (degrees + 2.0));

        stopPivot();
    }

    // public void convertAngleToEncoderValue(double degrees)
    // {

    // }

    @Override
    public void readPeriodicInputs()
    {
        periodicIO.currentAngle = returnPivotAngle();
    }

    @Override
    public void writePeriodicOutputs()
    {
        System.out.println("currentAngle = " + periodicIO.currentAngle); 
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
