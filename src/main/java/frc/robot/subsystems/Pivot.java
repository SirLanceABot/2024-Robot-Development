package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;

import frc.robot.motors.TalonFX4237;
import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private class MyConstants
    {
        public double kP = 17.0;
        public double kI = 10.0;
        public double kD = 0.0;
        public double setPoint = 0.0;
        public int slotId = 0;
    }

    private class PeriodicData
    {
        // INPUTS
    
        private double currentPosition;
        // private double currentAngle;
        private boolean isActive;
        private boolean isPIDSet;

        // OUTPUTS

        private double motorSpeed;

    }
    

    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder pivotAngle = new CANcoder(Constants.Pivot.CAN_CODER_PORT, Constants.Pivot.MOTOR_CAN_BUS);
    private PeriodicData periodicData = new PeriodicData();  
    private MyConstants myConstants = new MyConstants();
    private PIDController PIDcontroller = new PIDController(myConstants.kP, myConstants.kI, myConstants.kD);
    // private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);
    


    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);

        configPivotMotor();
        System.out.println("  Constructor Finished: " + fullClassName);
        LiveWindow.setEnabled(true);
        LiveWindow.enableTelemetry(this.PIDcontroller);
    }

    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setupBrakeMode();
        motor.setPosition(0.0);
        motor.setupRemoteCANCoder(20);
        motor.setupPIDController(myConstants.slotId, myConstants.kP, myConstants.kI, myConstants.kD);
        
        
        // Soft Limits
        motor.setupForwardSoftLimit(0.25, true);
        motor.setupReverseSoftLimit(0.0, true);

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
        var currentAngle = periodicData.currentPosition * 360.0;
        return currentAngle;
    }

    public double getPosition()
    {
        
        return periodicData.currentPosition;
    }

    public void resetCANcoder()
    {
        pivotAngle.setPosition(0.0);
    }

    public void setArtificialPosition(double degrees)
    {
        pivotAngle.setPosition(degrees);
    }

    public void humanIntake()
    {
        motor.setControl(Constants.Pivot.DEFAULT_ANGLE / 360.0);
    }

    public void subwooferShotAngle()
    {
        motor.setControl(Constants.Pivot.SHOOT_FROM_SUBWOOFER_ANGLE / 360.0);
    }

    public void setAngle(double degrees)
    { 
        //setAngle using CANcoder
        if(degrees >= 0.0 && degrees <= 90.0)
        {
            motor.setControl(degrees / 360.0);
        }
        else
        {
            PIDcontroller.setSetpoint(404.0);
        }

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

        //Using CANcoder
        periodicData.currentPosition = pivotAngle.getPosition().getValueAsDouble();
        
        // Decides if the PID controller should be setup again
        if(myConstants.kP != PIDcontroller.getP() || myConstants.kI != PIDcontroller.getI() || myConstants.kD != PIDcontroller.getD() || myConstants.setPoint != PIDcontroller.getSetpoint())
        {
            periodicData.isPIDSet = false;
        }
        else
        {
            periodicData.isPIDSet = true;
        }

        //Changes the PID values to the values displayed on the PID widget
        myConstants.kP = PIDcontroller.getP();
        myConstants.kI = PIDcontroller.getI();
        myConstants.kD = PIDcontroller.getD();
        myConstants.setPoint = PIDcontroller.getSetpoint();

        //For activating PID values using toggle switch
        if(periodicData.isActive)
        {
            if(periodicData.isPIDSet == false)
                motor.setupPIDController(myConstants.slotId, myConstants.kP, myConstants.kI, myConstants.kD);
            setAngle(myConstants.setPoint);
        }
        else
        {
            stop();
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        //Displays any changed values on the PID controller widget
        //Go to ShuffleBoard - Sources - NetworkTables to insert widget
        PIDcontroller.setP(myConstants.kP);
        PIDcontroller.setI(myConstants.kI);
        PIDcontroller.setD(myConstants.kD);
        PIDcontroller.setSetpoint(myConstants.setPoint);   

        //Returns current state of the toggle switch
        periodicData.isActive = SmartDashboard.getBoolean("Activate PID", !periodicData.isActive);

        //Toggle switch to use the PID controller widget (use toggle-button under Show as...)
        SmartDashboard.putBoolean("Activate PID", periodicData.isActive);

        //Displays the pivot's current angle
        SmartDashboard.putNumber("currentAngle", getAngle());
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
