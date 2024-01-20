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

    private class PeriodicData
    {
        // INPUTS
    
        private double currentPosition;
        private double currentAngle;

        // OUTPUTS

        private double motorSpeed;

    }

    private final CANSparkMax4237 motor = new CANSparkMax4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");

    private PeriodicData periodicData = new PeriodicData();
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
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setupBrakeMode();


        // Soft Limits
        motor.setupForwardSoftLimit(10000.0, true);
        motor.setupReverseSoftLimit(-10000.0, true);

    }

    public void moveUp(double speed)
    {
        periodicData.motorSpeed = speed;
        // motor.set(periodicData.motorSpeed);
    }

    public void moveDown(double speed)
    {
        periodicData.motorSpeed = -speed;
        // motor.set(periodicData.motorSpeed);
    }

    public void stopPivot()
    {
        periodicData.motorSpeed = 0.0;
        // motor.set(periodicData.motorSpeed); 
    }

    public double returnPivotAngle()
    {
        return (360.0 * rotaryEncoder.getAbsolutePosition());
    }

    public void setAngle(double degrees)
    {
        do
        {
           if(periodicData.currentAngle > degrees)
            {
                moveDown(0.5);
            }

            if(periodicData.currentAngle < degrees)
            {
                moveUp(0.5);
            } 
        }
        while(periodicData.currentAngle > (degrees - 2.0) || periodicData.currentAngle < (degrees + 2.0));

        stopPivot();
    }

    // public void convertAngleToEncoderValue(double degrees)
    // {

    // }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.currentAngle = returnPivotAngle();
    }

    @Override
    public void writePeriodicOutputs()
    {
        System.out.println("currentAngle = " + periodicData.currentAngle); 
        motor.set(periodicData.motorSpeed);
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
