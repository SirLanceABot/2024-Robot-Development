package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

// FIXME uncomment this next line
// import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;

// knock knock

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


    private class PeriodicIO
    {
        // Inputs
        private double yaw;
        private double pitch;
        private double roll;
        private Rotation2d rotation2d;

        // Outputs
    }

    private final WPI_Pigeon2 gyro = new WPI_Pigeon2(Constants.Gyro.PIGEON_ID, Constants.Gyro.PIGEON_CAN_BUS);
    private ResetState resetState = ResetState.kDone;
    private Timer timer = new Timer();

    private final PeriodicIO periodicIO = new PeriodicIO();

    public Gyro4237()
    {
        //reset();
        initPigeon();
        // periodicIO.angle = gyro.getYaw();
        periodicIO.rotation2d = gyro.getRotation2d();

    }

    public void initPigeon()
    {
        gyro.configFactoryDefault();
        gyro.configMountPose(Constants.Gyro.FORWARD_AXIS, Constants.Gyro.UP_AXIS); //forward axis and up axis
        gyro.reset();
        Timer.delay(0.5);
        gyro.setYaw(180.0);  // 2022 robot started with front facing away from the driver station, 2023 will not
        Timer.delay(0.5);
    }

    public void reset()
    {
        resetState = ResetState.kStart;
        // gyro.reset();
    }

    public double getRoll()
    {
        return periodicIO.roll; // x-axis
    }

    public double getPitch()
    {
        return periodicIO.pitch; // y-axis
    }

    public double getYaw()
    {
        return periodicIO.yaw; // z-axis
    }

    public Rotation2d getRotation2d()
    {
        return periodicIO.rotation2d;
        // return gyro.getRotation2d();
    }

    @Override
    public void readPeriodicInputs()
    {
        if (resetState == ResetState.kDone)
        {
            periodicIO.yaw = gyro.getYaw();
            periodicIO.pitch = gyro.getPitch();
            periodicIO.roll = gyro.getRoll();

            periodicIO.rotation2d = gyro.getRotation2d();
        }

        
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(resetState == ResetState.kStart)
        {
            gyro.reset();
            timer.reset();
            timer.start();
            gyro.setYaw(180.0);
            resetState = ResetState.kTry;
        }
        else if (resetState == ResetState.kTry && timer.hasElapsed(Constants.Gyro.RESET_GYRO_DELAY))
            resetState = ResetState.kDone;

        // System.out.println(periodicIO.angle + "   " + periodicIO.rotation2d.getDegrees());
    }

    @Override
    public String toString()
    {
        return String.format("Gyro %f \n", periodicIO.yaw);
    }

    
}
