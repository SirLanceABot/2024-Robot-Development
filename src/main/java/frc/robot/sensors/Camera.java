package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

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
        NetworkTableEntry tv;
        NetworkTableEntry botpose_wpiblue;
        NetworkTableEntry botpose_wpired;

        // Our class variables named with our convention (yes camelcase)
        private boolean isTargetFound;
        private double[] botPoseWPIBlue;
        private double[] botPoseWPIRed;
    }

    private PeriodicData periodicData;
    private Pose3d poseForAS;
    private NetworkTable cameraTable;

    private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable("ASTable"); // custom table for AdvantageScope testing


    public Camera(String camName)
    {   
        super("Camera");
        System.out.println("  Constructor Started:  " + fullClassName + " >> " + camName);

        periodicData = new PeriodicData();

        // Assign the Network Table variable in the constructor so the camName parameter can be used
        cameraTable = NetworkTableInstance.getDefault().getTable(camName);   // official limelight table

        periodicData.tv = cameraTable.getEntry("tv");
        periodicData.botpose_wpiblue = cameraTable.getEntry("botpose_wpiblue");
        periodicData.botpose_wpired = cameraTable.getEntry("botpose_wpired");


        System.out.println("  Constructor Started:  " + fullClassName + " >> " + camName);
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
                poseArray[Constants.Camera.translationXMetersIndex],
                poseArray[Constants.Camera.translationYMetersIndex],
                poseArray[Constants.Camera.translationZMetersIndex]
                ),
            new Rotation3d(
                Units.degreesToRadians(poseArray[Constants.Camera.rotationRollDegreesIndex]),
                Units.degreesToRadians(poseArray[Constants.Camera.rotationPitchDegreesIndex]),
                Units.degreesToRadians(poseArray[Constants.Camera.rotationYawDegreesIndex])
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
    public Pose3d getBotPoseWPIBlue()
    {
        return toPose3d(periodicData.botPoseWPIBlue);
    }

    /** @return the robot pose on the field (double[]) red driverstration origin*/
    public Pose3d getBotPoseWPIRed()
    {
        return toPose3d(periodicData.botPoseWPIRed);
    }

    /** @return the robot pose on the field (double[]) red driverstration origin*/
    /** @param allianceColor the allaince color */
    public Pose3d getBotPose(DriverStation.Alliance allianceColor)
    {
        if(allianceColor == DriverStation.Alliance.Red)
        {
            return toPose3d(periodicData.botPoseWPIRed);
        }
        else if(allianceColor == DriverStation.Alliance.Blue)
        {
            return toPose3d(periodicData.botPoseWPIBlue);
        }
        else
        {
            return null;
        }
            
    }

    /** @return the total latency from WPIBlue botpose measurements (double)*/
    public double getTotalLatencyBlue()
    {
        return periodicData.botPoseWPIBlue[Constants.Camera.totalLatencyIndex];
    }

    /** @return the total latency from WPIRed botpose measurements (double)*/
    public double getTotalLatencyRed()
    {
        return periodicData.botPoseWPIRed[Constants.Camera.totalLatencyIndex];
    }

    /** @return the total latency from botpose measurements (double)*/
    /** @param allianceColor the allaince color */
    public double getTotalLatency(DriverStation.Alliance allianceColor)
    {
        if(allianceColor == DriverStation.Alliance.Red)
        {
            return periodicData.botPoseWPIRed[Constants.Camera.totalLatencyIndex];
        }
        else if(allianceColor == DriverStation.Alliance.Blue)
        {
            return periodicData.botPoseWPIBlue[Constants.Camera.totalLatencyIndex];
        }
        else
        {
            return 0.0;
        }
            
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicData.isTargetFound = periodicData.tv.getDouble(0.0) == 1.0;
        periodicData.botPoseWPIBlue = periodicData.botpose_wpiblue.getDoubleArray(new double[7]);
        periodicData.botPoseWPIRed = periodicData.botpose_wpired.getDoubleArray(new double[7]);
    }

    @Override
    public void writePeriodicOutputs() 
    {
        poseForAS = toPose3d(periodicData.botPoseWPIBlue);    // variable for testing in AdvantageScope

        // put the pose from LL onto the Network Table so AdvantageScope can read it
        ASTable.getEntry("robotpose").setDoubleArray(Camera.toQuaternions(poseForAS));
    }

    @Override
    public void runPeriodicTask()
    {
    }
}
