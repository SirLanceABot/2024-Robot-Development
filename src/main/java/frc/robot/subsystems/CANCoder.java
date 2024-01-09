package frc.robot.subsystems;

public class CANCoder 
{
    public CANCoder(int deviceNumber, String canbus)
    {
        System.out.println("ERROR ERROR ERROR - using fake CANCoder class");
    }

    public ErrorCode configAllSettings(CANCoderConfiguration allConfigs)
    {
        ErrorCode errorCode = null;
        return errorCode;
    }

    public ErrorCode setPosition(double newPosition)
    {
        ErrorCode errorCode = null;
        return errorCode;
    }

    public double getAbsolutePosition()
    {
        return 0.0;
    }


}
