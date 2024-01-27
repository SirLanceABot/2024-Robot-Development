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

    private class Cam
    {
        private Pose3d pose;
        private double totalLatency;
        private boolean isTargetFound;
    }


    private class PeriodicData
    {
        // INPUTS
        private Rotation2d gyroRotation;
        private SwerveModulePosition[] swerveModulePositions;
        private Optional<Alliance> alliance;

        private Cam cam1;
        private Cam cam2;
        private Cam cam3;
        private Cam cam4;

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
        // this.cam1 = cam1;
        // this.cam2 = cam2;
        // this.cam3 = cam3;
        // this.cam4 = cam4;

        if(drivetrain != null && gyro != null)
        {
            poseEstimator = new SwerveDrivePoseEstimator(
                drivetrain.getKinematics(),
                gyro.getRotation2d(),
                drivetrain.getSwerveModulePositions(),
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
        double deltaX = blueSpeakerCoords[0] - periodicData.estimatedPose.getX();
        deltaX = Math.abs(deltaX);
        double deltaY = blueSpeakerCoords[1] - periodicData.estimatedPose.getY();
        deltaY = Math.abs(deltaY);
        double angleRads = Math.atan2(deltaX, deltaY);
        // double angleRads = Math.asin(deltaY / deltaX);
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

        
        
        periodicData.alliance = DriverStation.getAlliance();

        for(Camera camera : cameraArray)
        {
            if(camera != null && camera.isTargetFound())
            {
                // update pose esitmator with limelight data (vision part)
                poseEstimator.addVisionMeasurement(
                    camera.getBotPoseWPIBlue().toPose2d(), 
                    Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000));
            }
        }
        
        // if(cam1 != null)
        // {
        //     periodicData.cam1.pose = cam1.getBotPose(periodicData.alliance);
        //     periodicData.cam1.totalLatency = cam1.getTotalLatency(periodicData.alliance);
        //     periodicData.cam1.isTargetFound = cam1.isTargetFound();
        // }
    
        
        // if(cam2 != null)
        // {
        //     periodicData.cam2.pose = cam2.getBotPose(periodicData.alliance);
        //     periodicData.cam2.totalLatency = cam2.getTotalLatency(periodicData.alliance);
        //     periodicData.cam2.isTargetFound = cam2.isTargetFound();
        // }

        // if(cam3 != null)
        // {
        //     periodicData.cam3.pose = cam3.getBotPose(periodicData.alliance);
        //     periodicData.cam3.totalLatency = cam3.getTotalLatency(periodicData.alliance);
        //     periodicData.cam3.isTargetFound = cam3.isTargetFound();
        // }

        // if(cam4 != null)
        // {
        //     periodicData.cam4.pose = cam4.getBotPose(periodicData.alliance);
        //     periodicData.cam4.totalLatency = cam4.getTotalLatency(periodicData.alliance);
        //     periodicData.cam4.isTargetFound = cam4.isTargetFound();
        // }
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(poseEstimator != null && drivetrain != null && gyro != null)
        {
            // update pose estimator with drivetrain encoders (odometry part)
            // periodicData.estimatedPose = poseEstimator.update(periodicData.gyroRotation, periodicData.swerveModulePositions);

            // if(cam1 != null && periodicData.cam1.isTargetFound)
            // {
            //     // update pose esitmator with limelight data (vision part)
            //     poseEstimator.addVisionMeasurement(
            //         periodicData.cam1.pose.toPose2d(), 
            //         Timer.getFPGATimestamp() - (periodicData.cam1.totalLatency / 1000));
            // }

            // if(cam2 != null && periodicData.cam2.isTargetFound)
            // {
            //     // update pose esitmator with limelight-two data (vision part)
            //     poseEstimator.addVisionMeasurement(
            //         periodicData.cam2.pose.toPose2d(), 
            //         Timer.getFPGATimestamp() - (periodicData.cam2.totalLatency / 1000));
            // }

            // if(cam3 != null && periodicData.cam3.isTargetFound)
            // {
            //     // update pose esitmator with limelight-three data (vision part)
            //     poseEstimator.addVisionMeasurement(
            //         periodicData.cam3.pose.toPose2d(), 
            //         Timer.getFPGATimestamp() - (periodicData.cam3.totalLatency / 1000));
            // }

            // if(cam4 != null && periodicData.cam4.isTargetFound)
            // {
            //     // update pose esitmator with limelight-four data (vision part)
            //     poseEstimator.addVisionMeasurement(
            //         periodicData.cam4.pose.toPose2d(), 
            //         Timer.getFPGATimestamp() - (periodicData.cam4.totalLatency / 1000));
            // }

            periodicData.estimatedPose = poseEstimator.getEstimatedPosition();
            periodicData.poseForAS = poseEstimator.getEstimatedPosition(); // variable for testing in AdvantageScope

            // put the pose onto the NT so AdvantageScope can read it
            ASTable.getEntry("poseEstimator").setDoubleArray(Camera.toQuaternions(periodicData.poseForAS));
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
