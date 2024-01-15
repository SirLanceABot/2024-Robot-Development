package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TalonFX4237 extends MotorController4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final TalonFX motor;
    private final String description;

    public TalonFX4237(int deviceId, String canbus, String description)
    {
        super(deviceId, canbus, description);

        System.out.println("  Constructor Started:  " + fullClassName);

        motor = new TalonFX(deviceId, canbus);
        this.description = description;

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * Reset the factory defaults.
     */
    public void resetFactoryDefaults()
    {
        motor.getConfigurator().apply(new TalonFXConfiguration());
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
        MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        motor.getConfigurator().refresh(motorOutputConfigs);
        if(mode == BrakeCoastMode.kBrake)
            motorOutputConfigs.NeutralMode = NeutralModeValue.Brake;
        else
            motorOutputConfigs.NeutralMode = NeutralModeValue.Coast;
        motor.getConfigurator().apply(motorOutputConfigs);
    }

    /**
     * Set the forward soft limit.
     */
    public void setForwardSoftLimit(double limit, boolean isEnabled)
    {
        SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        motor.getConfigurator().refresh(softLimitSwitchConfigs);
        softLimitSwitchConfigs.withForwardSoftLimitThreshold(limit);
        softLimitSwitchConfigs.withForwardSoftLimitEnable(isEnabled);
        motor.getConfigurator().apply(softLimitSwitchConfigs);
    }

    /**
     * Set the reverse soft limit.
     */
    public void setReverseSoftLimit(double limit, boolean isEnabled)
    {
        SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        motor.getConfigurator().refresh(softLimitSwitchConfigs);
        softLimitSwitchConfigs.withReverseSoftLimitThreshold(limit);
        softLimitSwitchConfigs.withReverseSoftLimitEnable(isEnabled);
        motor.getConfigurator().apply(softLimitSwitchConfigs);
    }


    public void setCurrentLimit(int limit)
    {

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
