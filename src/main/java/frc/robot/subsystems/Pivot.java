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
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
        // private boolean isToggleSwitchActive;
        // private boolean isPIDSet;

        // OUTPUTS
        private boolean isBadAngle;
   }

    private class MyConstants
    {
        //for PID
        private double kP = 140.0;
        private double kI = 0.0;
        private double kD = 0.0;
        // private double setPoint = 0.0;
        private int slotId = 0;

        //Used to get the correct currentPosition
        private final double currentRotationalPositionOffset = 0.011667;
        // private final double motorPositionConversionFactor = 0.002778;

        //limits
        private final double FORWARD_SOFT_LIMIT = 0.2473;
        private final double REVERSE_SOFT_LIMIT = 0.0;
        private final double MAX_PIVOT_ANGLE = 77.0;
        private final double MINIMUM_PIVOT_ANGLE = 12.0; 

        //for manually moving Pivot
        private final double MOTOR_SPEED_DOWN = 0.07;
        private final double MOTOR_SPEED_UP = 0.07;
    }
    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder pivotAngle = new CANcoder(Constants.Pivot.CANCODER_PORT, Constants.Pivot.MOTOR_CAN_BUS); 
    private final PeriodicData periodicData = new PeriodicData();
    private final MyConstants myConstants = new MyConstants();
    private PIDController PIDcontroller = new PIDController(myConstants.kP, myConstants.kI, myConstants.kD);
    private final InterpolatingDoubleTreeMap shotMap = new InterpolatingDoubleTreeMap();

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
        configShotMap();
        setAngleDefaultCommand();
       
        System.out.println(" Construction Finished: " + fullClassName);
    }

    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here
    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setupBrakeMode();
        // motor.setupPositionConversionFactor(myConstants.motorPositionConversionFactor);
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
        // pivotAngle.setPosition(0.0);
        motor.setPosition(pivotAngle.getAbsolutePosition().getValueAsDouble() * 200.0);
    }

    public void setAngleDefaultCommand()
    {
        this.setDefaultCommand(this.setAngleCommand(30.0));
    }

    private void configShotMap()
    {
        // first value is distance from speaker in feet, second value is the pivot angle in degrees
        shotMap.put(3.0, 60.6);
        shotMap.put(4.0, 53.1);
        shotMap.put(5.0, 46.8);
        shotMap.put(6.0, 41.6);
        shotMap.put(7.0, 37.3);
        shotMap.put(8.0, 33.7);
        shotMap.put(9.0, 30.65);
        shotMap.put(10.0, 28.1);
        shotMap.put(11.0, 25.9);
        shotMap.put(12.0, 24.0);
        shotMap.put(13.0, 22.3);
        shotMap.put(14.0, 20.9);
        shotMap.put(15.0, 19.6);

    }

    public void moveUp()
    {
        motor.set(myConstants.MOTOR_SPEED_UP);
    }

    public void moveDown()
    {
        motor.set(-myConstants.MOTOR_SPEED_DOWN);
    }

    public void stopMotor()
    {
        motor.set(0.0);

    }

    public double getAngle()
    {
        // returns an angular value of the CANcoder
        return getPosition() * 360.0;
    }

    public double getPosition()
    {
        //returns the position of the CANcoder in rotations
        return periodicData.currentRotationalPosition;
    }
    
    private void setAngle(double degrees)
    { 
        //setAngle using CANcoder
        if(degrees >= myConstants.MINIMUM_PIVOT_ANGLE && degrees <= myConstants.MAX_PIVOT_ANGLE)
        {
            motor.setControlPosition(degrees / 360.0);
        }
        else
        {
            periodicData.isBadAngle = true;
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

    /**
     * 
     * @param distance (ft)
     * @return angle pivot should move to
     */
    public double calculateAngleFromDistance(double distance)
    {
        return shotMap.get(distance);
    }

    public Command setAngleCommand(double angle)
    {
        return Commands.runOnce(() -> setAngle(angle), this).withName("Set Angle");
    }

    public Command tunePID()
    {
        return Commands.runOnce(() -> motor.setupPIDController(myConstants.slotId, PIDcontroller.getP(), PIDcontroller.getI(), PIDcontroller.getD())).withName("setK's")
            .andThen(
                Commands.runOnce(() -> SmartDashboard.putBoolean("Activate PID", SmartDashboard.getBoolean("Activate PID", false)))).withName("reset switch")
            .andThen(
                Commands.runOnce(() -> setAngle(PIDcontroller.getSetpoint()))).withName("control to " + PIDcontroller.getSetpoint() + " degrees")
            .finallyDo(
                this.motor::stopMotor).withName("stop tuning");
    }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void readPeriodicInputs()
    {
        //Using CANcoder
        periodicData.currentRotationalPosition = pivotAngle.getAbsolutePosition().getValueAsDouble() + myConstants.currentRotationalPositionOffset;

        // For Testing
        // Changes the PID values to the values displayed on the PID widget
        // myConstants.kP = PIDcontroller.getP();
        // myConstants.kI = PIDcontroller.getI();
        // myConstants.kD = PIDcontroller.getD();
        // myConstants.setPoint = PIDcontroller.getSetpoint();
        
        // //variable to decide if the PID should be reset
        // periodicData.isPIDSet = (myConstants.kP != PIDcontroller.getP() || myConstants.kI != PIDcontroller.getI() || myConstants.kD != PIDcontroller.getD() || myConstants.setPoint != PIDcontroller.getSetpoint());

        // //Returns current state of the toggle switch
        // periodicData.isToggleSwitchActive = SmartDashboard.getBoolean("Activate PID", !periodicData.isToggleSwitchActive);
    }

    @Override
    public void writePeriodicOutputs()
    {
        //Stops the pivot if the angle for setAngle is out of range
        if(periodicData.isBadAngle)
        {
            stopMotor();
            System.out.println("Angle is out of Range");
            periodicData.isBadAngle = false;
        }

        //Displays the pivot's current angle
        //SmartDashboard.putNumber("currentAngle", getAngle());

        //For Testing
        //Displays any changed values on the PID controller widget and sets the correct values to the PID controller
        //Go to ShuffleBoard - Sources - NetworkTables to insert widget 
        // if(periodicData.isPIDSet == false)
        // {
        //     PIDcontroller.setP(myConstants.kP);
        //     PIDcontroller.setI(myConstants.kI);
        //     PIDcontroller.setD(myConstants.kD);
        //     PIDcontroller.setSetpoint(myConstants.setPoint);
        //     motor.setupPIDController(myConstants.slotId, myConstants.kP, myConstants.kI, myConstants.kD);
        // }

        // // Toggle switch to use the PID controller widget (use toggle-button under Show as...)
        // SmartDashboard.putBoolean("Activate PID", periodicData.isToggleSwitchActive);

        // //For activating setPoint using the toggle switch
        // if(periodicData.isToggleSwitchActive)
        // {
        //     setAngle(myConstants.setPoint);
        // }
        // else
        // {
        //     stopMotor();
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
        return "current pivot angle = " + getAngle();
    }
}
