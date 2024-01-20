package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import frc.robot.motors.TalonFX4237;
import edu.wpi.first.math.controller.BangBangController;
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

    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");

    private PeriodicData periodicData = new PeriodicData();
    private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);

    private BangBangController controller = new BangBangController();



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
        motor.setPosition(0.0);
        motor.setupPositionConversionFactor(0.00048828);

        // Soft Limits
        motor.setupForwardSoftLimit(6144.0, true);
        motor.setupReverseSoftLimit(-6144.0, true);
    }

    public void moveUp(double speed)
    {
        periodicData.motorSpeed = speed;
        // motor.set(periodicData.motorSpeed);
    }

    public void moveDown(double speed)
    {
        periodicData.motorSpeed = -speed;
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
    }

    public double returnAngle()
    {
        // return 360.0 * rotaryEncoder.getAbsolutePosition();
        return periodicData.currentAngle;
    }

    public void setAngle(double degrees, double speed)
    {
        //setAngle using FalconFX encoder
        if(periodicData.currentAngle > (degrees + 16))
        {
            moveDown(speed);
        }
        else if(periodicData.currentAngle < (degrees - 16))
        {
            moveUp(speed);
        }
        else
        {
            stop();
        }


        //setAngle using rotary encoder
        // if(periodicData.currentAngle > (degrees + 2))
        // {
        //     moveDown(0.25);
        // }
        // else if(periodicData.currentAngle < (degrees - 2))
        // {
        //     moveUp(0.25);
        // } 
        // else
        // {
        //     stopPivot();
        // }
    }

    @Override
    public void readPeriodicInputs()
    {
        //Using Rotary Encoder
        //periodicData.currentAngle = 360.0 * rotaryEncoder.getAbsolutePosition();

        //Using TalonFX encoder
        periodicData.currentPosition = motor.getPosition();
        periodicData.currentAngle = periodicData.currentPosition / Constants.Pivot.ENCODER_TICKS_PER_DEGREE;
    }

    @Override
    public void writePeriodicOutputs()
    {
        //System.out.println("currentAngle = " + periodicData.currentAngle); 
        System.out.println("currentPosition = " + motor.getPosition());
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
