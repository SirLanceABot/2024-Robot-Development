package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
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

    public class ClassConstants
    {
        //for PID
        private double kP = 0.5; //140.0;
        private double kI = 0.0;
        private double kD = 0.0;
        // private double setPoint = 0.0;
        private int slotId = 0;

        //limits
        private final double FORWARD_SOFT_LIMIT = 64.0; //(65.0 / 360.0); //66 degrees is the top
        private final double REVERSE_SOFT_LIMIT = 25.0; //(27.0 / 360.0); //22 degrees is the bottom
        private final double MAX_PIVOT_ANGLE = 65.0;
        private final double MINIMUM_PIVOT_ANGLE = 24.0;
        private final double MAGNET_OFFSET = -0.5538955;
        private final double RELATIVE_ENCODER_OFFEST = 21.0;


        public final double DEFAULT_ANGLE = 32.0;
        public final double INTAKE_FROM_SOURCE_ANGLE = 60.0;   //TODO: Check angle
        public final double SHOOT_TO_AMP_ANGLE = 59.5;
        public final double ANGLE_TOLERANCE = 0.3;

        //for manually moving Pivot
        private final double MOTOR_SPEED = 0.1;
    }
    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder4237 pivotAngle = new CANcoder4237(Constants.Pivot.CANCODER_PORT, Constants.Pivot.CANCODER_CAN_BUS, "pivot CANCoder");
    private final PeriodicData periodicData = new PeriodicData();
    public final ClassConstants classConstants = new ClassConstants();
    private PIDController PIDcontroller = new PIDController(classConstants.kP, classConstants.kI, classConstants.kD);
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

        resetRelativeEncoder();
        // setAngleDefaultCommand();

        periodicData.canCoderRotationalPosition = pivotAngle.getAbsolutePosition();//.getValueAsDouble();
        // periodicData.motorEncoderRotationalPosition = motor.getPosition();
        // motor.setPosition(periodicData.canCoderRotationalPosition);
       
        System.out.println(" Construction Finished: " + fullClassName);
    }

    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configCANcoder()
    {
        pivotAngle.logPeriodicData(true);

        pivotAngle.setupAbsoluteSensorRange_0To1();
        pivotAngle.setupMagnetOffset(classConstants.MAGNET_OFFSET);
        pivotAngle.setupSensorDirection_ClockwisePositive();
        Timer.delay(3.0);

        System.out.println(pivotAngle);

        pivotAngle.setPosition(pivotAngle.getAbsolutePosition());
        Timer.delay(1.0);


        // CANcoderConfiguration canCoderConfigs = new CANcoderConfiguration();
        // pivotAngle.getConfigurator().apply(canCoderConfigs);

        // MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        // magnetSensorConfigs.withAbsoluteSensorRange(AbsoluteSensorRangeValue.Unsigned_0To1);
        // magnetSensorConfigs.withMagnetOffset(classConstants.MAGNET_OFFSET);
        // magnetSensorConfigs.withSensorDirection(SensorDirectionValue.Clockwise_Positive);

        // canCoderConfigs.withMagnetSensor(magnetSensorConfigs);

        // pivotAngle.getConfigurator().apply(canCoderConfigs);
        // pivotAngle.getConfigurator().refresh(canCoderConfig);
        // canCoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        // canCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        // canCoderConfig.MagnetSensor.MagnetOffset = classConstants.MAGNET_OFFSET;
        // pivotAngle.getConfigurator().apply(canCoderConfig);

        // pivotAngle.setPosition(pivotAngle.getAbsolutePosition().getValueAsDouble());

    }

    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(true);
        motor.setupBrakeMode();
        // motor.setupPositionConversionFactor(1.0 / 360.0);
        motor.setupPositionConversionFactor(200.0 / 360.0);
        // motor.setupRemoteCANCoder(Constants.Pivot.CANCODER_PORT);
        motor.setSafetyEnabled(false);

        motor.setupPIDController(classConstants.slotId, classConstants.kP, classConstants.kI, classConstants.kD);
        
        // Soft Limits
        motor.setupForwardSoftLimit(classConstants.FORWARD_SOFT_LIMIT, true);
        motor.setupReverseSoftLimit(classConstants.REVERSE_SOFT_LIMIT, true);

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

    public void resetRelativeEncoder()
    {
        motor.setPosition(classConstants.RELATIVE_ENCODER_OFFEST);
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
    
    public void setAngle(double degrees)
    { 
        //setAngle using CANcoder
        if(degrees >= classConstants.MINIMUM_PIVOT_ANGLE && degrees <= classConstants.MAX_PIVOT_ANGLE)
        {
            motor.setControlPosition(degrees);
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
    public double calculateAngleFromDistance(double distance)
    {
        return shotMap.get(distance);
    }

    public Command setAngleCommand(double angle)
    {
        return Commands.runOnce(() -> setAngle(angle), this).withName("Set Angle");
    }

    public Command resetAngleCommand()
    {
        return Commands.runOnce(() -> resetRelativeEncoder(), this).withName("Reset Pivot Angle");
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
        periodicData.canCoderRotationalPosition = pivotAngle.getAbsolutePosition();//.getValueAsDouble();

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

        // System.out.println("Motor position = " + motor.getPosition());

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
