package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

public class CANSparkMax4237 extends MotorController4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final CANSparkMax motor;
    private final String description;

    public CANSparkMax4237(int deviceId, String canbus, String description)
    {
        super(deviceId, canbus, description);

        System.out.println("  Constructor Started:  " + fullClassName);
        
        motor = new CANSparkMax(deviceId, CANSparkLowLevel.MotorType.kBrushless);
        this.description = description;

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * Reset the factory defaults.
     */
    public void resetFactoryDefaults()
    {
        motor.restoreFactoryDefaults(false);
    }

    /**
     * Invert the direction of the motor controller.
     */
    public void setInverted(boolean isInverted)
    {
        motor.setInverted(isInverted); 
    }

    /**
     * Sets the mode to brake or coast.
     */
    public void setBrakeCoastMode(BrakeCoastMode mode)
    {
        if(mode == BrakeCoastMode.kBrake)
            motor.setIdleMode(IdleMode.kBrake);
        else
            motor.setIdleMode(IdleMode.kCoast);
    }

    /**
     * Set the forward soft limit.
     */
    public void setForwardSoftLimit(double limit, boolean isEnabled)
    {
        motor.setSoftLimit(SoftLimitDirection.kForward, (float) limit);
        motor.enableSoftLimit(SoftLimitDirection.kForward, isEnabled);
    }

    /**
     * Set the reverse soft limit.
     */
    public void setReverseSoftLimit(double limit, boolean isEnabled)
    {
        motor.setSoftLimit(SoftLimitDirection.kReverse, (float) limit);
        motor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled);
    }

    public void setCurrentLimit(int limit)
    {
        motor.setSmartCurrentLimit(limit);
        // motor.setSecondaryCurrentLimit(limit, limit)
    }

    @Override
    public void stopMotor()
    {
        motor.set(0.0);
    }

    @Override
    public String getDescription()
    {
        return description;
    }
}
