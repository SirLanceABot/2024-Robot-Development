package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.MotorSafety;

public abstract class MotorController4237 extends MotorSafety
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum BrakeCoastMode { kBrake, kCoast };

    public abstract void resetFactoryDefaults();
    public abstract void setInverted(boolean isInverted);
    public abstract void setBrakeCoastMode(BrakeCoastMode mode);
    public abstract void setForwardSoftLimit(double limit, boolean isEnabled);
    public abstract void setReverseSoftLimit(double limit, boolean isEnabled);
    public abstract void setCurrentLimit(int limit);

    private final int deviceId;
    private final String canbus;
    private final String description;

    public MotorController4237(int deviceId, String canbus, String description)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.deviceId = deviceId;
        this.canbus = canbus;
        this.description = description;

        System.out.println("  Constructor Finished: " + fullClassName);
    }
}
