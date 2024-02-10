package frc.robot.subsystems;
// final
import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfigurator;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

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

import edu.wpi.first.wpilibj2.command.button.Trigger;


/**
 * This class creates a Pivot to set the angle of the flywheel.
 */
public class Pivot extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

     // *** INNER ENUMS and INNER CLASSES ***
    // Put all inner enums and inner classes here
    private class PeriodicData
    {
        // INPUTS


        private double currentRotationalPosition;
        private boolean isToggleSwitchActive = false;
        private boolean isPIDSet;
        //private boolean isManualOverride;

        // OUTPUTS
        private double setSpeed = 0.0;
   }

    private class MyConstants
    {
        //for PID
        public double kP = 140.0;
        public double kI = 0.0;
        public double kD = 0.0;
        public double setPoint = 0.0;
        public int slotId = 0;

        //for manually moving Pivot
        public final double MOTOR_SPEED_DOWN = 0.07;
        public final double MOTOR_SPEED_UP = 0.07;

        //soft limits
        public final double FORWARD_SOFT_LIMIT = 0.152; //0.33
        public final double REVERSE_SOFT_LIMIT = 0.055;
    }
    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder pivotAngle = new CANcoder(Constants.Pivot.CANCODER_PORT, Constants.Pivot.MOTOR_CAN_BUS);
    private final PeriodicData periodicData = new PeriodicData();  
    private final MyConstants myConstants = new MyConstants();
    private PIDController PIDcontroller = new PIDController(myConstants.kP, myConstants.kI, myConstants.kD);

    // private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);
    

     // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Pivot. 
     */
    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);

        configPivotMotor();
        configCANcoder();
        System.out.println("  Constructor Finished: " + fullClassName);
        LiveWindow.setEnabled(true);
        LiveWindow.enableTelemetry(this.PIDcontroller);
    }

     // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here
    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setupBrakeMode();
        motor.setPosition(0.0);
        motor.setupRemoteCANCoder(Constants.Pivot.CANCODER_PORT);
        motor.setupPIDController(myConstants.slotId, myConstants.kP, myConstants.kI, myConstants.kD);
        
        // Soft Limits
        motor.setupForwardSoftLimit(myConstants.FORWARD_SOFT_LIMIT, true);
        motor.setupReverseSoftLimit(myConstants.REVERSE_SOFT_LIMIT, true);

    }

    private void configCANcoder()
    {
        CANcoderConfiguration canCoderConfig = new CANcoderConfiguration();
        pivotAngle.getConfigurator().refresh(canCoderConfig);
        canCoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        canCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        canCoderConfig.MagnetSensor.MagnetOffset = -0.668213; //BW 2/9/2024
        pivotAngle.getConfigurator().apply(canCoderConfig);
        motor.setPosition(pivotAngle.getAbsolutePosition().getValueAsDouble() * 200.0);
    }

    public void moveUp()
    {
        periodicData.setSpeed = myConstants.MOTOR_SPEED_UP;
    }

    public void moveDown()
    {
        periodicData.setSpeed = -myConstants.MOTOR_SPEED_DOWN;
    }

    public void stopMotor()
    {
        periodicData.setSpeed = 0.0;
    }

     // public void switchOverride()
     // {
     //     periodicData.isManualOverride = !periodicData.isManualOverride;
     // }

    public double getAngle()
    {
        //periodicData.current position returns a value in rotations of the CANcoder
        return periodicData.currentRotationalPosition * 360.0;
    }

    public double getPosition()
    {
        return periodicData.currentRotationalPosition;
    }

    // public void resetCANcoder()
    // {
    //     pivotAngle.setPosition(0.0);
    // }

    // public void setArtificialPosition(double degrees)
    // {
    //     pivotAngle.setPosition(degrees);
    // }

    // public void humanIntake()
    // {
    //     motor.setControl(Constants.Pivot.DEFAULT_ANGLE / 360.0);
    // }

    // public void subwooferShotAngle()
    // {
    //     motor.setControl(Constants.Pivot.SHOOT_FROM_SUBWOOFER_ANGLE / 360.0);
    // }

    private void setAngle(double degrees)
    { 
        //setAngle using CANcoder
        if(degrees >= 7.0 && degrees <= 67.0)
        {
            motor.setControl(degrees / 360.0);
        }
        else
        {
            System.out.println("error");
            stopMotor();
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

    public Command setAngleCommand(double angle)
    {
        return Commands.runOnce(() -> setAngle(angle)).withName("Set Angle");
    }

    public Command tunePID()
    {
        return Commands.runOnce(() -> motor.setupPIDController(myConstants.slotId, PIDcontroller.getP(), PIDcontroller.getI(), PIDcontroller.getD())).withName("setK's")
            .andThen(
                Commands.runOnce(() -> SmartDashboard.putBoolean("Activate PID", SmartDashboard.getBoolean("Activate PID", false)))).withName("reset switch")
            .andThen(
                Commands.run(() -> setAngle(PIDcontroller.getSetpoint()))).withName("control to " + PIDcontroller.getSetpoint() + " degrees");
            // .finallyDo(
            //     this.motor::stopMotor).withName("stop tuning");
    }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void readPeriodicInputs()
    {
        // //Using Rotary Encoder
        // //periodicData.currentAngle = 360.0 * rotaryEncoder.getAbsolutePosition();

        //Using CANcoder
        periodicData.currentRotationalPosition = pivotAngle.getAbsolutePosition().getValueAsDouble();

        // For Testing
        // Changes the PID values to the values displayed on the PID widget
        myConstants.kP = PIDcontroller.getP();
        myConstants.kI = PIDcontroller.getI();
        myConstants.kD = PIDcontroller.getD();
        myConstants.setPoint = PIDcontroller.getSetpoint();
        
        //variable to decide if the PID should be reset
        //periodicData.isPIDSet = !(myConstants.kP != PIDcontroller.getP() || myConstants.kI != PIDcontroller.getI() || myConstants.kD != PIDcontroller.getD() || myConstants.setPoint != PIDcontroller.getSetpoint());

        //Returns current state of the toggle switch
        //periodicData.isToggleSwitchActive = SmartDashboard.getBoolean("Activate PID", !periodicData.isToggleSwitchActive);

        

    }

    @Override
    public void writePeriodicOutputs()
    {
        //Displays the pivot's current angle
        SmartDashboard.putNumber("currentAngle", getAngle());

        //For Testing
        //Displays any changed values on the PID controller widget and sets the correct values to the PID controller
        //Go to ShuffleBoard - Sources - NetworkTables to insert widget 
        // if(periodicData.isPIDSet == false)
        // {
            PIDcontroller.setP(myConstants.kP);
            PIDcontroller.setI(myConstants.kI);
            PIDcontroller.setD(myConstants.kD);
            PIDcontroller.setSetpoint(myConstants.setPoint);
            motor.setupPIDController(myConstants.slotId, myConstants.kP, myConstants.kI, myConstants.kD);
        // }

        // Toggle switch to use the PID controller widget (use toggle-button under Show as...)
        //SmartDashboard.putBoolean("Activate PID", periodicData.isToggleSwitchActive);

        //For activating setPoint using the toggle switch
        // if(periodicData.isToggleSwitchActive)
        // {
            setAngle(myConstants.setPoint);
        // }
        // else
        // {
        //     stopMotor();
        // }

        //For competition
        // if(periodicData.isManualOverride)
        // {
        //     motor.set(periodicData.setSpeed);
        // }
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

    @Override
    public String toString()
    {
        return "";
    }
}
