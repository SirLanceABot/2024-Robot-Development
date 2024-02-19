package frc.robot.sensors;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.GyroTrimConfigs;
import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.Pigeon2FeaturesConfigs;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
/*
 * This class creates gyro which is used for rotation 
 */
public class Gyro4237 extends Sensor4237
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
        // Inputs
        private double yaw;
        private double pitch;
        private double roll;
        private Rotation2d rotation2d;

        // Outputs
    }

    public static final double RESET_GYRO_DELAY = 0.1;

    public final double BLUE_LEFT_STARTING_YAW = 60.0;
    public final double BLUE_MIDDLE_STARTING_YAW = 0.0;
    public final double BLUE_RIGHT_STARTING_YAW = -60.0;
    public final double RED_LEFT_STARTING_YAW = 120.0;
    public final double RED_MIDDLE_STARTING_YAW = 180.0;
    public final double RED_RIGHT_STARTING_YAW = -120.0;

    // private final WPI_Pigeon2 gyro = new WPI_Pigeon2(Constants.Gyro.PIGEON_ID, Constants.Gyro.PIGEON_CAN_BUS);
    private final Pigeon2 gyro = new Pigeon2(Constants.Gyro.PIGEON_PORT, Constants.Gyro.PIGEON_CAN_BUS);
    private ResetState resetState = ResetState.kDone;
    private Timer timer = new Timer();

    private final PeriodicData periodicData = new PeriodicData();

    public Gyro4237()
    {
        super("Gyro4237");
        System.out.println("  Constructor Started:  " + fullClassName);

        configGyro();
        periodicData.rotation2d = gyro.getRotation2d();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configGyro()
    {
        Pigeon2Configuration configs = new Pigeon2Configuration();
        MountPoseConfigs mountPoseConfigs = new MountPoseConfigs();
        Pigeon2FeaturesConfigs featuresConfigs = new Pigeon2FeaturesConfigs();
        GyroTrimConfigs gyroTrimConfigs = new GyroTrimConfigs();

        StatusCode statusCode;
        int count = 0;

        do
        {
            // Reset to factory defaults
            statusCode = gyro.getConfigurator().apply(configs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);

        mountPoseConfigs.withMountPoseRoll(0.0);
        mountPoseConfigs.withMountPosePitch(0.0);
        mountPoseConfigs.withMountPoseYaw(0.0);
        configs.withMountPose(mountPoseConfigs);

        featuresConfigs.withDisableNoMotionCalibration(false);
        featuresConfigs.withDisableTemperatureCompensation(false);
        featuresConfigs.withEnableCompass(false);
        configs.withPigeon2Features(featuresConfigs);

        gyroTrimConfigs.withGyroScalarX(0.0);
        gyroTrimConfigs.withGyroScalarY(0.0);
        gyroTrimConfigs.withGyroScalarZ(0.0);

        count = 0;
        do
        {
            // Apply gyro configurations
            statusCode = gyro.getConfigurator().apply(configs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);

        // gyro.configFactoryDefault();
        // gyro.configMountPose(Constants.Gyro.FORWARD_AXIS, Constants.Gyro.UP_AXIS); //forward axis and up axis
        gyro.reset();
        Timer.delay(0.5);
        // gyro.setYaw(0.0);  // 2022 robot started with front facing away from the driver station, 2023 will not
        // Timer.delay(0.5);
    }

    /**
     * Goes through reset process to eventually get yaw to zero degrees
     */
    public void reset()
    {
        resetState = ResetState.kStart;
    }

    /**
     * Wrapper for Pigeon2 setYaw() method
     * @param newValue Value to set to. Units are in deg.
     */
    public void setYaw(double newValue)
    {
        gyro.setYaw(newValue);
    }

    public double getRoll()
    {
        return periodicData.roll; // x-axis
    }

    public double getPitch()
    {
        return periodicData.pitch; // y-axis
    }

    public double getYaw()
    {
        return periodicData.yaw; // z-axis
    }

    public Rotation2d getRotation2d()
    {
        return periodicData.rotation2d;
    }

    @Override
    public void readPeriodicInputs()
    {
        if(resetState == ResetState.kDone)
        {
            periodicData.yaw = gyro.getYaw().getValueAsDouble();
            periodicData.pitch = gyro.getPitch().getValueAsDouble();
            periodicData.roll = gyro.getRoll().getValueAsDouble();

            periodicData.rotation2d = gyro.getRotation2d();
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        
    }

    @Override
    public void runPeriodicTask()
    {
        switch(resetState)
        {
            case kStart:
                gyro.reset();
                timer.reset();
                timer.start();
                gyro.setYaw(0.0);
                resetState = ResetState.kTry;
                break;
            case kTry:
                if(timer.hasElapsed(RESET_GYRO_DELAY))
                    resetState = ResetState.kDone;
                break;
            case kDone:
                break;
        }
    }

    @Override
    public String toString()
    {
        return String.format("Gyro %f \n", periodicData.yaw);
    }

    
}
