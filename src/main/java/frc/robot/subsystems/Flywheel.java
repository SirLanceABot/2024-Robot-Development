package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.PoseEstimator;
import frc.robot.motors.TalonFX4237;

/**
 *This class creates a flywheel which is the wheels that shoots the notes.
 */
public class Flywheel extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    
    public enum ResetState
    {
        kStart, kTry, kDone;
    }

    public enum Action
    {
        kShoot, kIntake;
    }

    private final class PeriodicData
    {
        // INPUTS
        // private double currentDistance;
        private double currentVelocity;
        private double RPMSpeed = 0.0;

        private DoubleEntry velocityEntry;


        // private double kP = SmartDashboard.getNumber("kP", 0.1);
        // private double kI = SmartDashboard.getNumber("kI", 0.0);
        // private double kD = SmartDashboard.getNumber("kD", 0.0);

        // OUTPUTS
       
        // private double flywheelSpeed;
        // private DoubleLogEntry currentDistanceEntry;
        // private DoubleLogEntry currentVelocityEntry;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final TalonFX4237 motor = new TalonFX4237(Constants.Flywheel.MOTOR_PORT, Constants.Flywheel.MOTOR_CAN_BUS, "flywheelMotor");
    // private RelativeEncoder encoder;
    // BangBangController controller = new BangBangController();
    private final InterpolatingDoubleTreeMap speakerVelocityShotMap = new InterpolatingDoubleTreeMap();
    private final InterpolatingDoubleTreeMap ampVelocityPassMap = new InterpolatingDoubleTreeMap();

    private ResetState resetState = ResetState.kDone;

    private final double ROLLER_DIAMETER_FEET = 4.0 / 12.0; // feet
    private final double GEAR_RATIO = 3.0 / 2.0;
    // private final double MINUTES_TO_SECONDS = 1.0 / 60.0;
    // Will need to be changed to look better possibly
    private final double RPS_TO_FPS = GEAR_RATIO * ( 1.0 / Math.PI) * (1.0 / ROLLER_DIAMETER_FEET); // 1.432
    // private boolean tunePID = false;

    public final double DEAULT_SPEED_TOLERANCE = 1.0;

    // -10 ft/s for intaking
    private final double kP = 0.45;
    private final double kI = 0.0;
    private final double kD = 0.001;
    private final double kS = 0.13;
    private final double kV = 0.15;

    private final NetworkTable velocityNetworkTable;    

    // private PIDController PIDcontroller = new PIDController(kP, kI, kD);

    // kS is equal to 0.013 in dutycycles(* 12 for volts)
    

    // private final double kIz = 0.0;
    // private final double kFF = 0.0;
    // private final double kShootMaxOutput = 0.0;
    // private final double kShootMinOutput = 0.0;
    // private final double kIntakeMaxOutput = 0.0;
    // private final double kIntakeMinOutput = 0.0;

    // private final double ROLLER_RADIUS = 2.0;   //inches


    /** 
     * Creates a new flywheel. 
     */
    public Flywheel()
    {
        super("Flywheel");
        System.out.println("  Constructor Started:  " + fullClassName);
        configTalonFX();
        configShotMap();
        configPassMap();


        velocityNetworkTable = NetworkTableInstance.getDefault().getTable(Constants.NETWORK_TABLE_NAME);
        periodicData.velocityEntry = velocityNetworkTable.getDoubleTopic("FlywheelVelocity").getEntry(0.0);
        // if(tunePID)
        // {
        //     SmartDashboard.putNumber("kP", periodicData.kP);
        //     SmartDashboard.putNumber("kI", periodicData.kI);
        //     SmartDashboard.putNumber("kD", periodicData.kD);

        //     SmartDashboard.putNumber("Velocity", 0.0);
        // }
        // setDefaultCommand(stopCommand());
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        motor.setupFactoryDefaults();
        motor.setupInverted(true);
        motor.setupCoastMode();
        motor.setSafetyEnabled(false);
        motor.setupPIDController(0, kP, kI, kD, kS, kV);
        // motor.setupPIDController(0, periodicData.kP, periodicData.kI, periodicData.kD);
        // motor.setupVelocityConversionFactor(2 * Math.PI * ROLLER_RADIUS * (1.0 / 60.0) * 0.833); // converts rpm to ft/s
        motor.setupVelocityConversionFactor(RPS_TO_FPS); // 0.69813111
        // motor.setupCurrentLimit(30, 35, 0.5);


        // motor.config_kP(0, kP);

    }

    //
    private void configShotMap()
    {
        // first value is distance from speaker in feet, second value is the pivot angle in degrees
        // These are for the calculated (poseEstimator) distances
        // speedShotMap.put(4.0, 65.0);
        // speedShotMap.put(5.0, 65.0);
        // speedShotMap.put(6.0, 65.0);
        // speedShotMap.put(7.0, 65.0);
        // speedShotMap.put(8.0, 60.0);
        // speedShotMap.put(9.0, 60.0);
        // speedShotMap.put(10.0, 60.0);
        // speedShotMap.put(11.0, 60.0);
        // speedShotMap.put(12.0, 55.0);
        // speedShotMap.put(13.0, 55.0);
        // speedShotMap.put(14.0, 55.0);
        // speedShotMap.put(15.0, 55.0);

        // These are for the real world distance
        speakerVelocityShotMap.put(4.5, 65.0);
        speakerVelocityShotMap.put(5.5, 65.0);
        speakerVelocityShotMap.put(6.5, 65.0);
        speakerVelocityShotMap.put(7.5, 65.0);
        speakerVelocityShotMap.put(8.5, 60.0);
        speakerVelocityShotMap.put(9.5, 60.0);
        speakerVelocityShotMap.put(10.5, 60.0);
        speakerVelocityShotMap.put(11.5, 60.0);
        speakerVelocityShotMap.put(12.5, 55.0);
        speakerVelocityShotMap.put(13.5, 55.0);
        speakerVelocityShotMap.put(14.5, 55.0);
        speakerVelocityShotMap.put(15.5, 55.0);
    }

    private void configPassMap()
    {
        ampVelocityPassMap.put(27.0, 47.0);
        ampVelocityPassMap.put(32.0, 50.0);
        ampVelocityPassMap.put(37.0, 53.0);
    }



    public void resetEncoder()
    {
        resetState = ResetState.kStart;
    }

    public double getVelocity()
    {
        return periodicData.currentVelocity;
        
    }

    public double getPosition()
    {
        return motor.getPosition();
    }

    public void shoot(double speed)
    {
        // periodicData.flywheelSpeed = speed;
        setControlVelocity(speed);
    }


    public void intake()
    {
        // periodicData.flywheelSpeed = -0.1;
        setControlVelocity(-10.0);
    }

    public void stop()
    {
        // periodicData.flywheelSpeed = 0.0;
        set(0.0);
    }

    private void setControlVelocity(double speed)
    {
        motor.setControlVelocity(speed);
    }

    private void set(double speed)
    {
        motor.set(speed);
    }

    public BooleanSupplier isAtSpeed(double targetSpeed)
    {
        // return () ->
        // {
        //     double speed = motor.getVelocity();

        //     // System.out.println("Velocity: " + speed);
        //     // System.out.println("Voltage: " + motor.getMotorVoltage());
        //     // System.out.println("Supply Voltage" + motor.getMotorSupplyVoltage());
        //     boolean isAtSpeed;
        //     if(speed > targetSpeed - SPEED_TOLERANCE && speed < targetSpeed + SPEED_TOLERANCE)
        //     {
        //         isAtSpeed = true;
        //         System.out.println("Flywheel Got To Speed");
        //     }
        //     else
        //     {
        //         isAtSpeed = false;
        //     }
        //     return isAtSpeed;
        // };

        return isAtSpeed(targetSpeed, DEAULT_SPEED_TOLERANCE);
    }

    public BooleanSupplier isAtSpeed(double targetSpeed, double speedTolerance)
    {
        return () ->
        {
            double speed = motor.getVelocity();

            // System.out.println("Velocity: " + speed);
            // System.out.println("Voltage: " + motor.getMotorVoltage());
            // System.out.println("Supply Voltage" + motor.getMotorSupplyVoltage());
            boolean isAtSpeed;
            if(speed > targetSpeed - speedTolerance && speed < targetSpeed + speedTolerance)
            {
                isAtSpeed = true;
                // System.out.println("Flywheel Got To Speed");
            }
            else
            {
                isAtSpeed = false;
            }
            return isAtSpeed;
        };
    }

    public double calculateShootVelocityFromDistance(DoubleSupplier distance)
    {
        // System.out.println(speedShotMap.get(distance.getAsDouble() * 3.2808));
        return speakerVelocityShotMap.get(distance.getAsDouble() * 3.2808);
    }

    public double calculatePassVelocityFromDistance(DoubleSupplier distance)
    {
        // System.out.println(speedShotMap.get(distance.getAsDouble() * 3.2808));
        return ampVelocityPassMap.get(distance.getAsDouble() * 3.2808);
    }

    

    public Command shootCommand(DoubleSupplier speed)
    {
        return Commands.run(() -> shoot(speed.getAsDouble()), this).withName("Shoot");
    }
    

    public Command shootSpeakerCommand()
    {
        return Commands.run(() -> shoot(0.6), this).withName("Shoot Speaker");
    }

    public Command shootAmpCommand()
    {
        return Commands.run(() -> shoot(0.2), this).withName("Shoot Amp");
    }

    public Command intakeCommand()
    {
        return Commands.run( () -> intake(), this).withName("Intake");
    }

    public Command stopCommand()
    {
        return Commands.runOnce( () -> stop(), this).withName("Stop");
    }

    // public double speedConversion(double speed)
    // {
    //     return speed * 6380 * 4.0 * Math.PI;
    // }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.currentVelocity = motor.getVelocity();
        // periodicData.RPMSpeed = speedConversion(periodicData.flywheelSpeed);

        // if(tunePID)
        // {
        //     periodicData.kP = SmartDashboard.getNumber("kP", 0.0);
        //     periodicData.kI = SmartDashboard.getNumber("kI", 0.0);
        //     periodicData.kD = SmartDashboard.getNumber("kD", 0.0);
        // }
        
    }

    @Override
    public void writePeriodicOutputs()
    {
        // if(periodicData.currentVelocity < periodicData.flywheelSpeed)
        // {
        //     motor.set(periodicData.flywheelSpeed);
        // }
        // else if(periodicData.currentVelocity >= periodicData.flywheelSpeed)
        // {
        //     motor.set(0.0);
        // }

        // // Done to fix the bang bang controller
        // if(periodicData.RPMSpeed <= 0.0)
        // {
        //     motor.set(periodicData.flywheelSpeed);
        // }
        // else
        // {
        //     motor.set(controller.calculate(periodicData.currentVelocity, periodicData.RPMSpeed));
        // }

        periodicData.velocityEntry.set(getVelocity());

        // motor.setControlVelocity(periodicData.flywheelSpeed);
        SmartDashboard.putNumber("Velocity", periodicData.currentVelocity);

        // if(tunePID)
        // {
        //     PIDcontroller.setP(periodicData.kP);
        //     PIDcontroller.setI(periodicData.kI);
        //     PIDcontroller.setD(periodicData.kD);
        // }
        

        // motor.set(periodicData.flywheelSpeed);
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
        return "Current Shooter Speed: " + periodicData.currentVelocity;
    }
}
