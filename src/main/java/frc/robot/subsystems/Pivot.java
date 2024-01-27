package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;

import frc.robot.motors.TalonFX4237;
import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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
    private final CANcoder pivotAngle = new CANcoder(20, Constants.Pivot.MOTOR_CAN_BUS);
    private PeriodicData periodicData = new PeriodicData();
    
    // private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);

    //PID values
    private final double kP = 1.0;
    private final double kD = 0.0;
    private final double kI = 0.0;
    private final int slotId = 0;

    private double position = 0.0;



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
        //motor.setupPositionConversionFactor(0.00048828);
        motor.setupRemoteCANCoder(20);
        motor.setupPIDController(slotId, kP, kI, kD);
        


        // Soft Limits
        motor.setupForwardSoftLimit(6144.0, false);
        motor.setupReverseSoftLimit(-6144.0, false);
    }

    public void moveUp()
    {
        periodicData.motorSpeed = Constants.Pivot.MOTOR_SPEED;
        motor.set(periodicData.motorSpeed);
    }

    public void moveDown()
    {
        periodicData.motorSpeed = -Constants.Pivot.MOTOR_SPEED;
        motor.set(periodicData.motorSpeed);
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
        motor.set(periodicData.motorSpeed);
    }

    public double getAngle()
    {
        return periodicData.currentAngle;
    }

    public double getPosition()
    {
        return periodicData.currentPosition;
    }

    public void resetEncoder()
    {
        // motor.setPosition(0.0);
        pivotAngle.setPosition(0.0);
    }

    public void setPosition(double degrees)
    {
        position = degrees;
    }

    

    public void setAngle(double degrees)
    { 
            motor.setControl(degrees / 360);
        
        //setAngle using FalconFX encoder
        // if(periodicData.currentAngle > (degrees + 5))
        // {
        //     moveDown();
        // }
        // else if(periodicData.currentAngle < (degrees - 5))
        // {
        //     moveUp();
        // }
        // else
        // {
        //     stop();
        // }

        //setAngle using rotary encoder
        // if(periodicData.currentAngle > (degrees + 2))
        // {
        //     moveDown(speed);
        // }
        // else if(periodicData.currentAngle < (degrees - 2))
        // {
        //     moveUp(speed);
        // } 
        // else
        // {
        //     stop();
        // }
    }

    public Command movePivotCommand(double angle)
    {
        return Commands.runOnce(() -> setAngle(angle));
    }

    @Override
    public void readPeriodicInputs()
    {
        //Using Rotary Encoder
        //periodicData.currentAngle = 360.0 * rotaryEncoder.getAbsolutePosition();

        //Using TalonFX encoder
        periodicData.currentPosition = pivotAngle.getPosition().getValueAsDouble();
        periodicData.currentAngle = periodicData.currentPosition * 360.0;
    }

    @Override
    public void writePeriodicOutputs()
    {
        System.out.println("currentAngle = " + periodicData.currentAngle); 
        //System.out.println("currentPosition = " + motor.getPosition());
        
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
