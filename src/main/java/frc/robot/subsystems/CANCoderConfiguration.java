package frc.robot.subsystems;

enum SensorVelocityMeasPeriod {Period_100Ms}; 
enum AbsoluteSensorRange {Unsigned_0_to_360};
enum SensorInitializationStrategy {BootToAbsolutePosition};
enum SensorTimeBase {PerSecond};

public class CANCoderConfiguration 
{
    SensorVelocityMeasPeriod velocityMeasurementPeriod;
    int velocityMeasurementWindow;
    AbsoluteSensorRange absoluteSensorRange;
    boolean sensorDirection;
    SensorInitializationStrategy initializationStrategy ;
    double sensorCoefficient;
    String unitString;
    SensorTimeBase sensorTimeBase;
    int customParam0 = 0;
    int customParam1 = 0;
    double magnetOffsetDegrees;

    public CANCoderConfiguration()
    {
        System.out.println("ERROR ERROR ERROR - using fake CANCoderConfiguration class");
    }
}
