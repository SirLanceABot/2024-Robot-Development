package frc.robot.sensors;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.sensors.Pigeon2.AxisDirection;

public class WPI_Pigeon2 
{
    public WPI_Pigeon2(int deviceNumber, String canbus)
    {
        System.out.println("ERROR ERROR ERROR - using fake WPI_Pigeon2 class");
    }
    
    public Rotation2d getRotation2d()
    {
        return new Rotation2d();
    }

    public void configFactoryDefault()
    {}

    public void configMountPose(AxisDirection forward, AxisDirection up)
    {}

    public void reset()
    {}

    public void setYaw(double angleDeg)
    {}

    public double getYaw()
    {
        return 0.0;
    }

    public double getPitch()
    {
        return 0.0;
    }

    public double getRoll()
    {
        return 0.0;
    }

}
