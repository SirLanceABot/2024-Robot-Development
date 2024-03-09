package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.TalonFX4237;
import frc.robot.sensors.CANcoder4237;


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

    @FunctionalInterface
    private interface Function
    {
        public abstract StatusCode apply();
    }

    private class PeriodicData
    {
        // INPUTS
        private double canCoderAbsolutePosition;
        private double canCoderRotationalPosition;
        private double motorEncoderRotationalPosition;
        // private boolean isToggleSwitchActive;
        // private boolean isPIDSet;

        // OUTPUTS
        private boolean isBadAngle = false;
   }

    public class ClassConstants
    {
        //for PID
        private double kP = 0.5; //140.0;
        private double kI = 0.0;
        private double kD = 0.0;
        // private double setPoint = 0.0;
        private int slotId = 0;

        //limits
        private final double FORWARD_SOFT_LIMIT = 64.0; //66 degrees is the top
        private final double REVERSE_SOFT_LIMIT = 27.0; //22 degrees is the bottom *add 2 to the limit for correct value
        //TODONT don't use this
        private final double MAGNET_OFFSET = -0.21624856; // updated 3/5/24   -0.54925655555; // updated 3/4/24   -0.71478456;

        public final double DEFAULT_ANGLE = 32.0;
        public final double INTAKE_FROM_SOURCE_ANGLE = 60.0;   //TODO: Check angle
        public final double SHOOT_TO_AMP_ANGLE = 60.5;
        public final double ANGLE_TOLERANCE = 0.3;

        //for manually moving Pivot
        private final double MOTOR_SPEED = 0.1;
    }
    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***

    final static DataLog log = DataLogManager.getLog();
    private StringLogEntry cancoderLogEntry;
    private DoubleLogEntry doubleLogEntry;

    private String canCoderName = "pivotCANcoder";
    private final int SETUP_ATTEMPT_LIMIT = 5;
    private int setupErrorCount = 0;

    private boolean logPeriodicData = false;

    // Put all class variables and instance variables here
    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder pivotAngle = new CANcoder(Constants.Pivot.CANCODER_PORT, Constants.Pivot.CANCODER_CAN_BUS);
    private final PeriodicData periodicData = new PeriodicData();
    public final ClassConstants classConstants = new ClassConstants();
    private PIDController PIDcontroller = new PIDController(classConstants.kP, classConstants.kI, classConstants.kD);
    private final InterpolatingDoubleTreeMap angleShotMap = new InterpolatingDoubleTreeMap();

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

        cancoderLogEntry = new StringLogEntry(log, "/cancoders/setup", "Setup");
        doubleLogEntry = new DoubleLogEntry(log, "cancoders/" + canCoderName, "Degrees");

        configCANcoder();
        configPivotMotor();
        configShotMap();

        // setDefaultCommand(setAngleCommand(() -> classConstants.DEFAULT_ANGLE));
        // periodicData.canCoderRotationalPosition = pivotAngle.getPosition().getValueAsDouble();
        // periodicData.motorEncoderRotationalPosition = motor.getPosition();
        // motor.setPosition(periodicData.canCoderRotationalPosition);
       
        System.out.println(" Construction Finished: " + fullClassName);
    }

    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configCANcoder()
    {
        CANcoderConfiguration canCoderConfig = new CANcoderConfiguration();

        // setup(() -> pivotAngle.getConfigurator().apply(canCoderConfig), "Setup Factory Defaults");
        setup(() -> pivotAngle.getConfigurator().refresh(canCoderConfig), "Refresh CANcoder");
        canCoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        canCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        // canCoderConfig.MagnetSensor.MagnetOffset = classConstants.MAGNET_OFFSET;
        setup(() -> pivotAngle.getConfigurator().apply(canCoderConfig), "Setup CANcoder");
    }

    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(true);
        motor.setupBrakeMode();
        motor.setupPositionConversionFactor(200.0 / 360.0);
        // motor.setupRemoteCANCoder(Constants.Pivot.CANCODER_PORT);
        motor.setSafetyEnabled(false);
        // motor.setPosition(fPart(pivotAngle.getPosition().getValueAsDouble()) * 200);
        // motor.setPosition(21.0);
        motor.setPosition((pivotAngle.getAbsolutePosition().getValueAsDouble() + classConstants.MAGNET_OFFSET) * 360.0);


        motor.setupPIDController(classConstants.slotId, classConstants.kP, classConstants.kI, classConstants.kD);
        
        // Soft Limits
        motor.setupForwardSoftLimit(classConstants.FORWARD_SOFT_LIMIT, true);
        motor.setupReverseSoftLimit(classConstants.REVERSE_SOFT_LIMIT, true);

        //Hard Limits
        motor.setupForwardHardLimitSwitch(true, true);
        motor.setupReverseHardLimitSwitch(true, true);
    }

     /** 
     * Check the CANCoder for an error and print a message.
     * @param message The message to print
     */
    private void setup(Function func, String message)
    {
        StatusCode errorCode = StatusCode.OK;
        int attemptCount = 0;
        String logMessage = "";
        
        do
        {
            errorCode = func.apply();
            logMessage = canCoderName + " : " + message + " " + errorCode;

            if(errorCode == StatusCode.OK)
                System.out.println(">> >> " + logMessage);
            else
                DriverStation.reportWarning(logMessage, true);
            cancoderLogEntry.append(logMessage);
            attemptCount++;
        }
        while(errorCode != StatusCode.OK && attemptCount < SETUP_ATTEMPT_LIMIT);

        setupErrorCount += (attemptCount - 1);
    }

    private void configShotMap()
    {
        // first value is distance from speaker in feet, second value is the pivot angle in degrees
        // These are for the calculated (poseEstimator) distances
        // angleShotMap.put(4.0, 64.0);
        // angleShotMap.put(5.0, 62.8);
        // angleShotMap.put(6.0, 56.7);
        // angleShotMap.put(7.0, 52.5);
        // angleShotMap.put(8.0, 49.9);
        // angleShotMap.put(9.0, 47.6);
        // angleShotMap.put(10.0, 44.0);
        // angleShotMap.put(11.0, 42.2);
        // angleShotMap.put(12.0, 41.3);
        // angleShotMap.put(13.0, 40.65);
        // angleShotMap.put(14.0, 39.64);
        // angleShotMap.put(15.0, 39.1);
        
        // These are for the real world distance
        angleShotMap.put(4.5, 64.0);
        angleShotMap.put(5.5, 62.8);
        angleShotMap.put(6.5, 56.7);
        angleShotMap.put(7.5, 52.5);
        angleShotMap.put(8.5, 49.9);
        angleShotMap.put(9.5, 47.6);
        angleShotMap.put(10.5, 44.0);
        angleShotMap.put(11.5, 42.2);
        angleShotMap.put(12.5, 41.3);
        angleShotMap.put(13.5, 40.65);
        angleShotMap.put(14.5, 39.64);
        angleShotMap.put(15.5, 39.1);

        // angleShotMap.put(5.0, 64.0);
        // angleShotMap.put(6.0, 62.8);
        // angleShotMap.put(7.0, 56.7);
        // angleShotMap.put(8.0, 52.5);
        // angleShotMap.put(9.0, 49.9);
        // angleShotMap.put(10.0, 47.6);
        // angleShotMap.put(11.0, 44.0);
        // angleShotMap.put(12.0, 42.2);
        // angleShotMap.put(13.0, 41.3);
        // angleShotMap.put(14.0, 40.65);
        // angleShotMap.put(15.0, 39.64);
        // angleShotMap.put(16.0, 39.1);
    }

    public void moveUp()
    {
        motor.set(classConstants.MOTOR_SPEED);
    }

    public void moveDown()
    {
        motor.set(-classConstants.MOTOR_SPEED);
    }

    public void stop()
    {
        motor.set(0.0);
    }

    public double getCANCoderAngle()
    {
        // returns an angular value of the CANcoder
        return periodicData.canCoderAbsolutePosition * 360.0;
    }

    public double getCANCoderPosition()
    {
        //returns the position of the CANcoder in rotations
        return periodicData.canCoderRotationalPosition;
    }

    public double getMotorEncoderPosition()
    {
        return periodicData.motorEncoderRotationalPosition;
    }
    
    public void setAngle(double degrees)
    { 
        //setAngle using CANcoder
        degrees = MathUtil.clamp(degrees, classConstants.REVERSE_SOFT_LIMIT, classConstants.FORWARD_SOFT_LIMIT);
        motor.setControlPosition(degrees);

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

    public BooleanSupplier isAtAngle(double targetAngle)
    {
        return () ->
        {
            double position = motor.getPosition();
            // System.out.println("Position: " + motor.getPosition());
            boolean isAtAngle;
            if(position > targetAngle - classConstants.ANGLE_TOLERANCE && position < targetAngle + classConstants.ANGLE_TOLERANCE)
            {
                isAtAngle = true;
            }
            else
            {
                isAtAngle = false;
            }
            return isAtAngle;
        };
    }

    /**
     * 
     * @param distance (ft)
     * @return angle pivot should move to
     */
    public double calculateAngleFromDistance(DoubleSupplier distance)
    {
        System.out.println(angleShotMap.get(distance.getAsDouble()));
        return angleShotMap.get(distance.getAsDouble());
    }

    public Command setAngleCommand(DoubleSupplier angle)
    {
        return Commands.runOnce(() -> setAngle(angle.getAsDouble()), this).withName("Set Angle");
    }

    public Command moveDownCommand()
    {
        return Commands.run(() -> moveDown(), this);
    }

    public Command resetMotorEncoderCommand()
    {
        return Commands.runOnce(() -> motor.setPosition(classConstants.REVERSE_SOFT_LIMIT), this).withName("Reset Pivot Position");
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stop());
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
        periodicData.canCoderAbsolutePosition = pivotAngle.getAbsolutePosition().getValueAsDouble() + classConstants.MAGNET_OFFSET;
        periodicData.canCoderRotationalPosition = pivotAngle.getPosition().getValueAsDouble() + classConstants.MAGNET_OFFSET;
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
        if(logPeriodicData)
        {
            doubleLogEntry.append(periodicData.canCoderAbsolutePosition);
            doubleLogEntry.append(periodicData.motorEncoderRotationalPosition);
        }

        if(periodicData.isBadAngle)
        {
            stop();
            System.out.println("Angle is out of Range");
            periodicData.isBadAngle = false;
        }

        //All included in tunePID() command

        //Displays the pivot's current angle
        SmartDashboard.putNumber("Pivot Angle:", getCANCoderAngle());
        SmartDashboard.putNumber("Pivot Cancoder Rotational Position:", getCANCoderPosition());
        SmartDashboard.putNumber("Pivot Motor Position:", getMotorEncoderPosition());



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
