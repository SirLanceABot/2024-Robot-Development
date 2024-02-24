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
        private double canCoderRotationalPosition;
        private double motorEncoderRotationalPosition;
        // private boolean isToggleSwitchActive;
        // private boolean isPIDSet;

        // OUTPUTS
        private boolean isBadAngle = false;
   }

    public class PivotConstants
    {
        //for PID
        private double kP = 140.0;
        private double kI = 0.0;
        private double kD = 0.0;
        // private double setPoint = 0.0;
        private int slotId = 0;

        //limits
        private final double FORWARD_SOFT_LIMIT = (61.0 / 360.0); //66 degrees is the top
        private final double REVERSE_SOFT_LIMIT = (27.0 / 360.0); //22 degrees is the bottom
        private final double MAX_PIVOT_ANGLE = 61.0;
        private final double MINIMUM_PIVOT_ANGLE = 27.0;

        public final double DEFAULT_ANGLE = 32.0;
        public final double INTAKE_FROM_SOURCE_ANGLE = 50.0;   //TODO: Check angle
        public final double SHOOT_TO_AMP_ANGLE = 59.5;

        //for manually moving Pivot
        private final double MOTOR_SPEED = 0.1;
    }
    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder pivotAngle = new CANcoder(Constants.Pivot.CANCODER_PORT, Constants.Pivot.CANCODER_CAN_BUS);
    private final PeriodicData periodicData = new PeriodicData();
    public final PivotConstants pivotConstants = new PivotConstants();
    private PIDController PIDcontroller = new PIDController(pivotConstants.kP, pivotConstants.kI, pivotConstants.kD);
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

        configCANcoder();
        configPivotMotor();
        configShotMap();
        // setAngleDefaultCommand();

        periodicData.canCoderRotationalPosition = pivotAngle.getAbsolutePosition().getValueAsDouble();
        // periodicData.motorEncoderRotationalPosition = motor.getPosition();
        // motor.setPosition(periodicData.canCoderRotationalPosition);
       
        System.out.println(" Construction Finished: " + fullClassName);
    }

    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configCANcoder()
    {
        CANcoderConfiguration canCoderConfig = new CANcoderConfiguration();
        pivotAngle.getConfigurator().refresh(canCoderConfig);
        canCoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        canCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        canCoderConfig.MagnetSensor.MagnetOffset = -0.63079956;
        pivotAngle.getConfigurator().apply(canCoderConfig);

        pivotAngle.setPosition(pivotAngle.getAbsolutePosition().getValueAsDouble());
    }

    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(true);
        motor.setupBrakeMode();
        // motor.setupPositionConversionFactor(1.0 / 360.0);
        motor.setupRemoteCANCoder(Constants.Pivot.CANCODER_PORT);

        motor.setupPIDController(pivotConstants.slotId, pivotConstants.kP, pivotConstants.kI, pivotConstants.kD);
        
        // Soft Limits
        motor.setupForwardSoftLimit(pivotConstants.FORWARD_SOFT_LIMIT, true);
        motor.setupReverseSoftLimit(pivotConstants.REVERSE_SOFT_LIMIT, true);

        //Hard Limits
        motor.setupForwardHardLimitSwitch(true, true);
        motor.setupReverseHardLimitSwitch(true, true);
    }

    public void setAngleDefaultCommand()
    {
        // this.setDefaultCommand(this.setAngleCommand(30.0));
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
        motor.set(pivotConstants.MOTOR_SPEED);
    }

    public void moveDown()
    {
        motor.set(-pivotConstants.MOTOR_SPEED);
    }

    public void stop()
    {
        motor.set(0.0);
    }

    public double getCANCoderAngle()
    {
        // returns an angular value of the CANcoder
        return getCANCoderPosition() * 360.0;
    }

    public double getCANCoderPosition()
    {
        //returns the position of the CANcoder in rotations
        return periodicData.canCoderRotationalPosition;
    }

    private double getMotorEncoderPosition()
    {
        return periodicData.motorEncoderRotationalPosition;
    }
    
    private void setAngle(double degrees)
    { 
        //setAngle using CANcoder
        if(degrees >= pivotConstants.MINIMUM_PIVOT_ANGLE && degrees <= pivotConstants.MAX_PIVOT_ANGLE)
        {
            motor.setControlPosition(degrees / 360.0);
        }
        else
        {
            periodicData.isBadAngle = true;
        }

        //setAngle using FalconFX encoder
        // if(getAngle() > (degrees + 2))
        // {
        //     moveDown();
        // }
        // else if(getAngle() < (degrees - 2))
        // {
        //     moveUp();
        // }
        // else
        // {
        //     stopMotor();
        // }

        //setAngle using rotary encoder
        // if(getAngle() > (degrees + 1))
        // {
        //     moveDown();
        // }
        // else if(getAngle() < (degrees - 1))
        // {
        //     moveUp();
        // } 
        // else
        // {
        //     stopMotor();
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

    // public Command tunePID()
    // {
    //     return Commands.runOnce(() -> motor.setupPIDController(myConstants.slotId, PIDcontroller.getP(), PIDcontroller.getI(), PIDcontroller.getD())).withName("setK's")
    //         .andThen(
    //             Commands.runOnce(() -> SmartDashboard.putBoolean("Activate PID", SmartDashboard.getBoolean("Activate PID", false)))).withName("reset switch")
    //         .andThen(
    //             Commands.runOnce(() -> setAngle(PIDcontroller.getSetpoint()))).withName("control to " + PIDcontroller.getSetpoint() + " degrees")
    //         .finallyDo(
    //             this.motor::stopMotor).withName("stop tuning");
    // }

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void readPeriodicInputs()
    {
        //Using CANcoder
        periodicData.canCoderRotationalPosition = pivotAngle.getAbsolutePosition().getValueAsDouble();

        periodicData.motorEncoderRotationalPosition = motor.getPosition();

        //All included in tunePID() command

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
            stop();
            System.out.println("Angle is out of Range");
            periodicData.isBadAngle = false;
        }

        //All included in tunePID() command

        //Displays the pivot's current angle
        SmartDashboard.putNumber("pivotAngle.currentAngle", getCANCoderAngle());
        SmartDashboard.putNumber("pivotAngle.currentPosition", getCANCoderPosition());
        SmartDashboard.putNumber("motor.getPosition = ", periodicData.motorEncoderRotationalPosition);

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
        return "current pivot angle = " + getCANCoderAngle();
    }
}
