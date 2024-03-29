package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Represents a Limelight to track AprilTags. */
public class Camera extends Sensor4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public class PeriodicData
    {
        //INPUTS
        
        // Entry variables named with LL convention (not camelcase)
        private NetworkTableEntry ta;
        private NetworkTableEntry tv;
        private NetworkTableEntry botpose_wpiblue;
        private NetworkTableEntry botpose_wpired;
        private NetworkTableEntry camerapose_targetspace;

        // Our class variables named with our convention (yes camelcase)
        private double targetSize;
        private boolean isTargetFound;
        private double[] botPoseWPIBlue;
        private double[] botPoseWPIRed;
        private double[] cameraPoseInTargetSpace;

        // private Matrix<N3, N1> measurementStdDevs;

        private DoubleArrayEntry dblArrayEntry;

    }

    public static final int TRANSLATION_X_METERS_INDEX = 0;
    public static final int TRANSLATION_Y_METERS_INDEX = 1;
    public static final int TRANSLATION_Z_METERS_INDEX = 2;
    public static final int ROTATION_ROLL_DEGREES_INDEX = 3;
    public static final int ROTATION_PITCH_DEGREES_INDEX = 4;
    public static final int ROTATION_YAW_DEGREES_INDEX = 5;
    public static final int TOTAL_LATENCY_INDEX = 6;
    public static final int TAG_COUNT_INDEX = 7;
    public static final int AVERAGE_TAG_DISTANCE_FROM_CAMERA_INDEX = 9;
    

    private final PeriodicData periodicData = new PeriodicData();
    private double[] poseForAS = {0.0, 0.0, 0.0};
    private NetworkTable cameraTable;

    private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable(Constants.ADVANTAGE_SCOPE_TABLE_NAME); // custom table for AdvantageScope testing
    private double[] defaultArray = {0.0, 0.0, 0.0};

    public Camera(String cameraName)
    {   
        super("Camera");
        System.out.println("  Constructor Started:  " + fullClassName + " >> " + cameraName);

        // Assign the Network Table variable in the constructor so the camName parameter can be used
        cameraTable = NetworkTableInstance.getDefault().getTable(cameraName);   // official limelight table

        periodicData.dblArrayEntry = ASTable.getDoubleArrayTopic(cameraName).getEntry(defaultArray);

        periodicData.ta = cameraTable.getEntry("ta");
        periodicData.tv = cameraTable.getEntry("tv");
        periodicData.botpose_wpiblue = cameraTable.getEntry("botpose_wpiblue");
        periodicData.botpose_wpired = cameraTable.getEntry("botpose_wpired");
        periodicData.camerapose_targetspace = cameraTable.getEntry("camerapose_targetspace");


        System.out.println("  Constructor Started:  " + fullClassName + " >> " + cameraName);
    }

    /**
     * 
     * @return size of target as percentage of total FOV it takes up
     */
    public double getTargetSize()
    {
        return periodicData.targetSize;
    }

    /** @return false if no target is found, true if target is found */
    public boolean isTargetFound()
    {
        return periodicData.isTargetFound;
    }

    // converts the double array from NT into Pose3d
    public Pose3d toPose3d(double[] poseArray)
    {
        return new Pose3d(
            new Translation3d(
                poseArray[TRANSLATION_X_METERS_INDEX],
                poseArray[TRANSLATION_Y_METERS_INDEX],
                poseArray[TRANSLATION_Z_METERS_INDEX]
                ),
            new Rotation3d(
                Units.degreesToRadians(poseArray[ROTATION_ROLL_DEGREES_INDEX]),
                Units.degreesToRadians(poseArray[ROTATION_PITCH_DEGREES_INDEX]),
                Units.degreesToRadians(poseArray[ROTATION_YAW_DEGREES_INDEX])
                )
        );
    }

    // converts Pose3d to Quaternions for AdvantageScope usage
    public static double[] toQuaternions(Pose3d pose)
    {
        return new double[] {
            pose.getTranslation().getX(), pose.getTranslation().getY(), pose.getTranslation().getZ(),
            pose.getRotation().getQuaternion().getW(), pose.getRotation().getQuaternion().getX(),
            pose.getRotation().getQuaternion().getY(), pose.getRotation().getQuaternion().getZ()
        };
    }

    // converts Pose2d to Quaternions for AdvantageScope usage
    public static double[] toQuaternions(Pose2d pose)
    {
        return new double[] {
            pose.getTranslation().getX(), pose.getTranslation().getY(),
            pose.getRotation().getRadians()
        };
    }

    /** @return the robot pose on the field (double[]) blue driverstration origin*/
    public Pose3d getBotPoseBlue()
    {
        return toPose3d(periodicData.botPoseWPIBlue);
    }

    /** @return the robot pose on the field (double[]) red driverstration origin*/
    public Pose3d getBotPoseRed()
    {
        return toPose3d(periodicData.botPoseWPIRed);
    }

    /** @return the total latency from WPIBlue botpose measurements (double)*/
    public double getTotalLatencyBlue()
    {
        return periodicData.botPoseWPIBlue[TOTAL_LATENCY_INDEX];
    }

    /** @return the total latency from WPIRed botpose measurements (double)*/
    public double getTotalLatencyRed()
    {
        return periodicData.botPoseWPIRed[TOTAL_LATENCY_INDEX];
    }

    public int getTagCount()
    {
        return (int) periodicData.botPoseWPIBlue[TAG_COUNT_INDEX];
    }

    /** @return the camera pose in the cooridnate plane of the target */
    public Pose3d getCameraPoseInTargetSpace()
    {
        return toPose3d(periodicData.cameraPoseInTargetSpace);
    }

    /** @return the distance between to Pose3ds in meters */
    public double getDistanceBetweenPose3ds(Pose3d pose1, Pose3d pose2)
    {
        Translation3d translation1 = pose1.getTranslation();
        Translation3d translation2 = pose2.getTranslation();

        return translation1.getDistance(translation2);
    }

    /** Calculates the distance to the target with custom math. NOT the same as getDistanceFromTarget() */
    /** @return the distance between the camera and the target in meters */
    public double calculateDistanceFromTarget()
    {
        return getDistanceBetweenPose3ds(toPose3d(periodicData.cameraPoseInTargetSpace), new Pose3d());
    }

    /** Gets the average distance to the target that the LL calculates and provides in botpose array */
    /** @return the average distance between the camera and the target in meters */
    public double getAverageDistanceFromTarget()
    {
        return periodicData.botPoseWPIBlue[AVERAGE_TAG_DISTANCE_FROM_CAMERA_INDEX];
    }

    // public Matrix<N3, N1> setMeasurementStdDevs()
    // {
    //     // defaults
    //     double x = 0.9;
    //     double y = 0.9;
    //     double heading = 0.9;

    //     // code to scale std dev based on distance (or size) of tag

    //     periodicData.measurementStdDevs.set(0, 0, x);
    //     periodicData.measurementStdDevs.set(1, 0, y);
    //     periodicData.measurementStdDevs.set(2, 0, heading);

    //     return periodicData.measurementStdDevs;
    // }

    // public Matrix<N3, N1> getMeasurementStdDevs()
    // {
    //     return periodicData.measurementStdDevs;
    // }

    @Override
    public void readPeriodicInputs() 
    {
        periodicData.targetSize = periodicData.ta.getDouble(0.0);
        periodicData.isTargetFound = periodicData.tv.getDouble(0.0) == 1.0;
        periodicData.botPoseWPIBlue = periodicData.botpose_wpiblue.getDoubleArray(new double[11]);
        periodicData.botPoseWPIRed = periodicData.botpose_wpired.getDoubleArray(new double[11]);
        periodicData.cameraPoseInTargetSpace = periodicData.camerapose_targetspace.getDoubleArray(new double[6]);
        // periodicData.measurementStdDevs = setMeasurementStdDevs();
    }

    @Override
    public void writePeriodicOutputs() 
    {
        // LL publishes a 3D pose in a weird format, so to make it readable
        // in AS we need to create our own double array and publish that
        poseForAS[0] = periodicData.botPoseWPIBlue[0];
        poseForAS[1] = periodicData.botPoseWPIBlue[1];
        poseForAS[2] = periodicData.botPoseWPIBlue[5];
        // SmartDashboard.putNumber("Distance", getDistanceFromTarget());
        // put the pose from LL onto the Network Table so AdvantageScope can read it
        // ASTable.getEntry(cameraName).setDoubleArray(poseForAS);
        periodicData.dblArrayEntry.set(poseForAS);
    }

    @Override
    public void runPeriodicTask()
    {
    }
}
