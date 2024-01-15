package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import frc.robot.Constants;
import frc.robot.subsystems.PoseEstimator;

/**
 * Use this class as a template to create other subsystems.
 */
public class Shooter extends Subsystem4237
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

    private class PeriodicData
    {
        // INPUTS
        private double currentDistance;
        private double currentVelocity;

        // OUTPUTS
       
        private double outerMotorSpeed;
        private DoubleLogEntry currentDistanceEntry;
        private DoubleLogEntry currentVelocityEntry;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final int OuterShooterMotorPort = Constants.Shooter.OUTER_SHOOTER_MOTOR_PORT;
    private final TalonFX outerShooterMotor = new TalonFX(OuterShooterMotorPort);
    private RelativeEncoder outerShooterEncoder;
    private RelativeEncoder innerShooterEncoder;

    private ResetState resetState = ResetState.kDone;
    private Pose2d fieldLocation;
    private final PoseEstimator poseEstimator;

    /** 
     * Creates a new Shooter. 
     */
    public Shooter(PoseEstimator poseEstimator)
    {
        super("Shooter");
        System.out.println("  Constructor Started:  " + fullClassName);
        this.poseEstimator = poseEstimator;
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configTalonFX()
    {
        // Factory Defaults
        // outerShooterMotor.config
        outerShooterMotor.setInverted(false);
        // innerShooterMotor.setInverted(false);
        // outerShooterMotor.setNeutralMode(NeutralModeValue.Coast);
        // innerShooterMotor.setNeutralMode(NeutralModeValue.Coast);
        // outerShooterEncoder = outerShooterMotor.getEncoder();
        // innerShooterEncoder = innerShooterMotor.getEncoder();
    }

    public void resetEncoder()
    {
        resetState = ResetState.kStart;
    }

    public void calculateDistance()
    {
        fieldLocation = poseEstimator.getEstimatedPose();
    }

    public double getVelocity()
    {
        return periodicData.currentVelocity;
        
    }

    public double getDistance()
    {
        return periodicData.currentDistance;
    }

    public void forwardInnerMotors()
    {
        // periodicData.innerMotorSpeed = 0.1;
    }


    public void reverseInnerMotors()
    {
        // periodicData.innerMotorSpeed = -0.1;
    }

    public void forwardOuterMotors()
    {
        periodicData.outerMotorSpeed = 0.1;
    }


    public void reverseOuterMotors()
    {
        periodicData.outerMotorSpeed = -0.1;
    }

    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {}

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
