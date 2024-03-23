package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.sql.Driver;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;


/** Represents a WPILib SwerveDrivePoseEstimator. */
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
    //private final Field2d field = new Field2d();

    // custom network table to make pose readable for AdvantageScope
    private NetworkTable ASTable;// = NetworkTableInstance.getDefault().getTable("ASTable");
    private final double[] blueSpeakerCoords = {0.076, 5.45};   // bad y {0.076, 5.547868};
    private final double[] redSpeakerCoords = {16.465042, 5.45};    // bad y {16.465042, 5.547868};
    private final double[] blueAmpZoneCoords = {1.0, 7.5};
    private final double[] redAmpZoneCoords = {15.5, 7.5};
    private final double[] fieldDimensions = {16.542, 8.211};

    private Matrix<N3, N1> visionStdDevs;
    private Matrix<N3, N1> stateStdDevs;

    private final double MAX_TARGET_DISTANCE = 5.0; // meters

    private int totalTagCount = 0;

    private class PeriodicData
    {
        // INPUTS
        private Rotation2d gyroRotation;
        private SwerveModulePosition[] swerveModulePositions;

        // OUTPUTS
        private Pose2d estimatedPose = new Pose2d();
        private Pose2d poseForAS;

        private DoubleArrayEntry poseEstimaterEntry;

    }

    private final PeriodicData periodicData = new PeriodicData();
    private final double[] defaultValues = {0.0, 0.0, 0.0};

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

        ASTable = NetworkTableInstance.getDefault().getTable(Constants.ADVANTAGE_SCOPE_TABLE_NAME);
        periodicData.poseEstimaterEntry = ASTable.getDoubleArrayTopic("PoseEstimator").getEntry(defaultValues);

        double[] doubleArray = {0, 0, 0};
        visionStdDevs = new Matrix<N3, N1>(Nat.N3(), Nat.N1(), doubleArray);
        stateStdDevs = new Matrix<N3, N1>(Nat.N3(), Nat.N1(), doubleArray);

        configStdDevs();

        if(drivetrain != null && gyro != null)
        {
            // poseEstimator = new SwerveDrivePoseEstimator(
            //     drivetrain.getKinematics(),
            //     gyro.getRotation2d(),
            //     drivetrain.getSwerveModulePositions(),
            //     drivetrain.getPose());

            poseEstimator = new SwerveDrivePoseEstimator(
                drivetrain.getKinematics(),
                gyro.getRotation2d(),
                drivetrain.getSwerveModulePositions(),
                drivetrain.getPose(),
                stateStdDevs,
                visionStdDevs);
        }
        else
        {
            poseEstimator = null;
        }

        //SmartDashboard.putData("Field",field);

        System.out.println(fullClassName + " : Constructor Finished");
    }

    public void configStdDevs()
    {
        stateStdDevs.set(0, 0, 0.1);    // x (meters)           default = 0.1
        stateStdDevs.set(1, 0, 0.1);    // y (meters)           default = 0.1
        stateStdDevs.set(2, 0, 0.05);   // heading (radians)    default = 0.1

        visionStdDevs.set(0, 0, 0.9);   // x (meters)           default = 0.9
        visionStdDevs.set(1, 0, 0.9);   // y (meters)           default = 0.9
        visionStdDevs.set(2, 0, 0.95);  // heading (radians)    default = 0.9
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

    /**
     * @return Angle nessecary for shooter to face the center of the blue speaker (degrees)
     */
    public double getAngleToBlueSpeaker()
    {
        double xPose = periodicData.estimatedPose.getX();
        double yPose = periodicData.estimatedPose.getY();
        double deltaX = Math.abs(blueSpeakerCoords[0] - xPose);
        double deltaY = Math.abs(blueSpeakerCoords[1] - yPose);
        double angleRads = Math.atan2(deltaY, deltaX);
        if(yPose > blueSpeakerCoords[1])
        {
            return Math.toDegrees(angleRads);
        }
        else if(yPose < blueSpeakerCoords[1])
        {
            return -Math.toDegrees(angleRads);
        }
        else
        {
            return 0.0;
        }
    }

    /**
     * @return Angle nessecary for shooter to face the center of the red speaker (degrees)
     */
    public double getAngleToRedSpeaker()
    {   
        double xPose = periodicData.estimatedPose.getX();
        double yPose = periodicData.estimatedPose.getY();
        double deltaX = Math.abs(redSpeakerCoords[0] - xPose);
        double deltaY = Math.abs(redSpeakerCoords[1] - yPose);
        double angleRads = Math.atan2(deltaY, deltaX);
        if(yPose > redSpeakerCoords[1])
        {
            return -Math.toDegrees(angleRads);
        }
        else if(yPose < redSpeakerCoords[1])
        {
            return Math.toDegrees(angleRads);
        }
        else
        {
            return 0.0;
        }
    }

    public double getAngleToBlueAmpZone()
    {
        double xPose = periodicData.estimatedPose.getX();
        double yPose = periodicData.estimatedPose.getY();
        double deltaX = Math.abs(blueAmpZoneCoords[0] - xPose);
        double deltaY = Math.abs(blueAmpZoneCoords[1] - yPose);
        double angleRads = Math.atan2(deltaY, deltaX);
        if(yPose > blueAmpZoneCoords[1])
        {
            return Math.toDegrees(angleRads);
        }
        else if(yPose < blueAmpZoneCoords[1])
        {
            return -Math.toDegrees(angleRads);
        }
        else
        {
            return 0.0;
        }
    }

    public double getAngleToRedAmpZone()
    {
        double xPose = periodicData.estimatedPose.getX();
        double yPose = periodicData.estimatedPose.getY();
        double deltaX = Math.abs(redAmpZoneCoords[0] - xPose);
        double deltaY = Math.abs(redAmpZoneCoords[1] - yPose);
        double angleRads = Math.atan2(deltaY, deltaX);
        if(yPose > redAmpZoneCoords[1])
        {
            return -Math.toDegrees(angleRads);
        }
        else if(yPose < redAmpZoneCoords[1])
        {
            return Math.toDegrees(angleRads);
        }
        else
        {
            return 0.0;
        }
    }

    /**
     * @return Distance to blue speaker in meters
     */
    public double getDistanceToBlueSpeaker()
    {
        Translation2d speakerTranslation = new Translation2d(blueSpeakerCoords[0], blueSpeakerCoords[1]);
        Translation2d robotTranslation = periodicData.estimatedPose.getTranslation();
        return robotTranslation.getDistance(speakerTranslation);
    }

    /**
     * @return Distance to red speaker in meters
     */
    public double getDistanceToRedSpeaker()
    {
        Translation2d speakerTranslation = new Translation2d(redSpeakerCoords[0], redSpeakerCoords[1]);
        Translation2d robotTranslation = periodicData.estimatedPose.getTranslation();
        return robotTranslation.getDistance(speakerTranslation);
    }

    /**
     * @return Distance to blue amp zone in meters
     */
    public double getDistanceToBlueAmpZone()
    {
        Translation2d ampZoneTranslation = new Translation2d(blueAmpZoneCoords[0], blueAmpZoneCoords[1]);
        Translation2d robotTranslation = periodicData.estimatedPose.getTranslation();
        return robotTranslation.getDistance(ampZoneTranslation);
    }

    /**
     * @return Distance to blue amp zone in meters
     */
    public double getDistanceToRedAmpZone()
    {
        Translation2d ampZoneTranslation = new Translation2d(redAmpZoneCoords[0], redAmpZoneCoords[1]);
        Translation2d robotTranslation = periodicData.estimatedPose.getTranslation();
        return robotTranslation.getDistance(ampZoneTranslation);
    }

    public void resetPosition(Rotation2d gyroAngle, SwerveModulePosition[] modulePositions, Pose2d newPose)
    {
        poseEstimator.resetPosition(gyroAngle, modulePositions, newPose);
    }

    public boolean isPoseCloseToCurrent(Pose2d pose)
    {
        // if pose is close to the current pose (within 3 meters)
        if(pose.getTranslation().getDistance(periodicData.estimatedPose.getTranslation()) < 3.0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isPoseInsideField(Pose2d pose)
    {
        // if the x component of the pose is within the field, and the y component is in the field
        // 1 meter buffer given
        if((pose.getX() > -1.0 && pose.getX() < fieldDimensions[0] + 1.0) && (pose.getY() > -1.0 && pose.getY() < fieldDimensions[1] + 1.0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Checks to see if the given pose is close enough to the current pose and if the given pose is actually inside the field
     * @param pose
     * @return true if pose is valid
     */
    public boolean isPoseValid(Pose2d pose)
    {
        if(isPoseCloseToCurrent(pose) && isPoseInsideField(pose))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void readPeriodicInputs()
    {
        totalTagCount = 0;
        if(drivetrain != null && gyro != null)
        {
            periodicData.gyroRotation = gyro.getRotation2d();
            periodicData.swerveModulePositions = drivetrain.getSwerveModulePositions();
            
            // update pose estimator with drivetrain encoders (odometry part)
            periodicData.estimatedPose = poseEstimator.update(periodicData.gyroRotation, periodicData.swerveModulePositions);
        }

        for(Camera camera : cameraArray)
        {
            // Pose2d visionPose = camera.getBotPoseBlue().toPose2d();

            if(camera != null)
            {
                if(camera.isTargetFound() && camera.getAverageDistanceFromTarget() < MAX_TARGET_DISTANCE)
                {
                    Pose2d visionPose = camera.getBotPoseBlue().toPose2d();

                    if(DriverStation.isDisabled())
                    {
                        poseEstimator.addVisionMeasurement(
                                camera.getBotPoseBlue().toPose2d(), 
                                Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000),
                                visionStdDevs.times(camera.getAverageDistanceFromTarget()));
                    }

                    if(isPoseValid(visionPose))
                    {
                        totalTagCount += camera.getTagCount();
                        if(DriverStation.isAutonomousEnabled())
                        {
                            poseEstimator.addVisionMeasurement(
                                camera.getBotPoseBlue().toPose2d(), 
                                Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000),
                                visionStdDevs.times(2 * camera.getAverageDistanceFromTarget()));
                        }
                        else
                        {
                            poseEstimator.addVisionMeasurement(
                                camera.getBotPoseBlue().toPose2d(), 
                                Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000),
                                visionStdDevs.times(camera.getAverageDistanceFromTarget()));
                        }
                    }
                    // if(DriverStation.isTeleopEnabled() && isPoseCloseToPrevious(visionPose))
                    // if(DriverStation.isTeleopEnabled())
                    // {
                    //     poseEstimator.addVisionMeasurement(
                    //         camera.getBotPoseBlue().toPose2d(), 
                    //         Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000),
                    //         visionStdDevs.times(camera.getAverageDistanceFromTarget()));
                    // }
                }
            }
            // if(!DriverStation.isAutonomousEnabled() && camera != null && camera.isTargetFound() && camera.getAverageDistanceFromTarget() < MAX_TARGET_DISTANCE)
            // {
            //     // update pose estimator with limelight data (vision part)
            //     // poseEstimator.addVisionMeasurement(
            //     //     camera.getBotPoseBlue().toPose2d(), 
            //     //     Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000));
                
            //     poseEstimator.addVisionMeasurement(
            //         camera.getBotPoseBlue().toPose2d(), 
            //         Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000),
            //         visionStdDevs.times(camera.getAverageDistanceFromTarget()));
            // }
            // else if(DriverStation.isAutonomousEnabled() && camera != null && camera.isTargetFound() && camera.getAverageDistanceFromTarget() < MAX_TARGET_DISTANCE)
            // {
            //     poseEstimator.addVisionMeasurement(
            //         camera.getBotPoseBlue().toPose2d(), 
            //         Timer.getFPGATimestamp() - (camera.getTotalLatencyBlue() / 1000),
            //         visionStdDevs.times(2 * camera.getAverageDistanceFromTarget()));
            // }
        }

        if(totalTagCount >= 3 && DriverStation.isTeleopEnabled())
        {
            drivetrain.resetOdometryOnly(poseEstimator.getEstimatedPosition());
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
            periodicData.poseEstimaterEntry.set(pose);
            // ASTable.getEntry("poseEstimator").setDoubleArray(pose);

            //field.setRobotPose(getEstimatedPose());
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

    @Override
    public String toString()
    {
        return "Estimated Pose: " + getEstimatedPose();
    }
}
