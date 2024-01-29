package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;


/**
 * Use this class as a template to create other subsystems.
 */
public class PoseEstimator extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    private final Gyro4237 gyro;
    private final Drivetrain drivetrain;
    private final Camera[] cameraArray;

    private final SwerveDrivePoseEstimator poseEstimator;
    // private final Camera cam1;
    // private final Camera cam2;
    // private final Camera cam3;
    // private final Camera cam4;

    // custom network table to make pose readable for AdvantageScope
    private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable("ASTable");
    private double[] blueSpeakerCoords = {0.0, 5.55};
    private double[] redSpeakerCoords = {16.54, 5.55};

    private class PeriodicData
    {
        // INPUTS
        private Rotation2d gyroRotation;
        private SwerveModulePosition[] swerveModulePositions;
        // private Optional<Alliance> alliance;

        // OUTPUTS
        private Pose2d estimatedPose;
        private Pose2d poseForAS;

    }

    private PeriodicData periodicData = new PeriodicData();

    /** 
     * Creates a new PoseEstimator. 
     */
    public PoseEstimator(Drivetrain drivetrain, Gyro4237 gyro, Camera[] cameraArray)
    {
        super("PoseEstimator");
        System.out.println(fullClassName + " : Constructor Started");

        this.gyro = gyro;
        this.drivetrain = drivetrain;
        this.cameraArray = cameraArray;

        if(drivetrain != null && gyro != null)
        {
            poseEstimator = new SwerveDrivePoseEstimator(
                drivetrain.getKinematics(),
                gyro.getRotation2d(),
                drivetrain.getSwerveModulePositions(),
                // new Pose2d());
                drivetrain.getPose());
        }
        else
        {
            poseEstimator = null;
        }

        System.out.println(fullClassName + " : Constructor Finished");
    }
    
    /** @return the estimated pose (Pose2d)*/
    public Pose2d getEstimatedPose() 
    {
        if(poseEstimator != null)
        {
            return periodicData.estimatedPose;
        }
        else
        {
            return new Pose2d();
        }
        
    }

    public double getAngleToBlueSpeaker()
    {
        double deltaX = Math.abs(blueSpeakerCoords[0] - periodicData.estimatedPose.getX());
        double deltaY = Math.abs(blueSpeakerCoords[1] - periodicData.estimatedPose.getY());
        double angleRads = Math.atan2(deltaY, deltaX);
        if(periodicData.estimatedPose.getY() > blueSpeakerCoords[1])
        {
            return Math.toDegrees(angleRads);
        }
        else if(periodicData.estimatedPose.getY() < blueSpeakerCoords[1])
        {
            return -Math.toDegrees(angleRads);
        }
        else
        {
            return 0.0;
        }
    }

    public double getAngleToRedSpeaker()
    {
        double deltaX = redSpeakerCoords[0] - periodicData.estimatedPose.getX();
        deltaX = Math.abs(deltaX);
        double deltaY = redSpeakerCoords[1] - periodicData.estimatedPose.getY();
        deltaY = Math.abs(deltaY);
        double angleRads = Math.asin(deltaY / deltaX);
        return Math.toDegrees(angleRads);
    }

    @Override
    public void readPeriodicInputs()
    {
        if(drivetrain != null && gyro != null)
        {
            periodicData.gyroRotation = gyro.getRotation2d();
            periodicData.swerveModulePositions = drivetrain.getSwerveModulePositions();
            
            // update pose estimator with drivetrain encoders (odometry part)
            periodicData.estimatedPose = poseEstimator.update(periodicData.gyroRotation, periodicData.swerveModulePositions);
        }

        // periodicData.alliance = DriverStation.getAlliance();

        for(Camera camera : cameraArray)
        {
            if(camera != null && camera.isTargetFound())
            {
                // update pose esitmator with limelight data (vision part)
                poseEstimator.addVisionMeasurement(
                    camera.getBotPoseBlue().toPose2d(), 
                    Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000));
            }
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(poseEstimator != null && drivetrain != null && gyro != null)
        {
            periodicData.estimatedPose = poseEstimator.getEstimatedPosition();
            periodicData.poseForAS = poseEstimator.getEstimatedPosition(); // variable for testing in AdvantageScope

            // put the pose onto the NT so AdvantageScope can read it
            // ASTable.getEntry("poseEstimator").setDoubleArray(Camera.toQuaternions(periodicData.poseForAS));
            double[] pose = {
                periodicData.poseForAS.getX(), periodicData.poseForAS.getY(), periodicData.poseForAS.getRotation().getDegrees()
            };
            ASTable.getEntry("poseEstimator").setDoubleArray(pose);

        }
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
