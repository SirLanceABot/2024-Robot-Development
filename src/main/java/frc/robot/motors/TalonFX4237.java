package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.ForwardLimitSourceValue;
import com.ctre.phoenix6.signals.ForwardLimitTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitSourceValue;
import com.ctre.phoenix6.signals.ReverseLimitTypeValue;

import edu.wpi.first.util.sendable.SendableBuilder;


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
    private final PositionVoltage positionVoltage;
    private final String motorControllerName;

    /**
     * Creates a TalonFX on the CANbus with a brushless motor (Falcon500).
     * Defaults to using the built-in encoder sensor (RotorSensor).
     * @param deviceId The id number of the device on the CANbus
     * @param canbus The name of the CANbus (ex. "rio" is the default name of the roboRIO CANbus)
     * @param motorControllerName The name describing the purpose of this motor controller
     */
    public TalonFX4237(int deviceId, String canbus, String motorControllerName)
    {
        super(motorControllerName);

        System.out.println("  Constructor Started:  " + fullClassName + " >> " + motorControllerName);

        motor = new TalonFX(deviceId, canbus);
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        motor.getConfigurator().refresh(feedbackConfigs);
        feedbackConfigs.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        motor.getConfigurator().apply(feedbackConfigs);
        this.motorControllerName = motorControllerName;
        positionVoltage = new PositionVoltage(0.0);

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + motorControllerName);
    }

    /**
     * Reset to the factory defaults.
     */
    public void setupFactoryDefaults()
    {
        motor.getConfigurator().apply(new TalonFXConfiguration());
    }

    public void setupRemoteCANCoder(int remoteSensorId)
    {
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        motor.getConfigurator().refresh(feedbackConfigs);
        feedbackConfigs.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
        feedbackConfigs.FeedbackRemoteSensorID = remoteSensorId;
        motor.getConfigurator().apply(feedbackConfigs);
    }

    /**
     * Set the Periodic Frame Period.
     * @param frameNumber The frame number to set
     * @param periodMs The time period in milliseconds
     */
    public void setupPeriodicFramePeriod(int frameNumber, int periodMs)
    {
        // FIXME
    }

    /**
     * Invert the direction of the motor controller.
     * @param isInverted True to invert the motor controller
     */
    public void setupInverted(boolean isInverted)
    {
        motor.setInverted(isInverted);
    }

    /**
     * Sets the idle/neutral mode to brake mode.
     */
    public void setupBrakeMode()
    {
        MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        motor.getConfigurator().refresh(motorOutputConfigs);
        motorOutputConfigs.NeutralMode = NeutralModeValue.Brake;
        motor.getConfigurator().apply(motorOutputConfigs);
    }

    /**
     * Sets the idle/neutral mode to coast mode.
     */
    public void setupCoastMode()
    {
        MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        motor.getConfigurator().refresh(motorOutputConfigs);
        motorOutputConfigs.NeutralMode = NeutralModeValue.Coast;
        motor.getConfigurator().apply(motorOutputConfigs);
    }

    /**
     * Set the forward soft limit.
     * @param limit The forward soft limit value
     * @param isEnabled True to enable the forward soft limit
     */
    public void setupForwardSoftLimit(double limit, boolean isEnabled)
    {
        SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        motor.getConfigurator().refresh(softLimitSwitchConfigs);
        softLimitSwitchConfigs.ForwardSoftLimitThreshold = limit;
        softLimitSwitchConfigs.ForwardSoftLimitEnable = isEnabled;
        motor.getConfigurator().apply(softLimitSwitchConfigs);
    }

    /**
     * Set the reverse soft limit.
     * @param limit The reverse soft limit value
     * @param isEnabled True to enable the reverse soft limit
     */
    public void setupReverseSoftLimit(double limit, boolean isEnabled)
    {
        SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        motor.getConfigurator().refresh(softLimitSwitchConfigs);

        softLimitSwitchConfigs.ReverseSoftLimitThreshold = limit;
        softLimitSwitchConfigs.ReverseSoftLimitEnable = isEnabled;
        motor.getConfigurator().apply(softLimitSwitchConfigs);
    }

    /**
     * Enable or disable the forward hard limit switch.
     * @param isEnabled True to enable the hard limit switch
     */
    public void setupForwardHardLimitSwitch(boolean isEnabled, boolean isNormallyOpen)
    {
        HardwareLimitSwitchConfigs hardwareLimitSwitchConfigs = new HardwareLimitSwitchConfigs();
        motor.getConfigurator().refresh(hardwareLimitSwitchConfigs);

        hardwareLimitSwitchConfigs.ForwardLimitSource = ForwardLimitSourceValue.LimitSwitchPin;
        if(isNormallyOpen)
            hardwareLimitSwitchConfigs.ForwardLimitType = ForwardLimitTypeValue.NormallyOpen;
        else
            hardwareLimitSwitchConfigs.ForwardLimitType = ForwardLimitTypeValue.NormallyClosed;
        hardwareLimitSwitchConfigs.ForwardLimitEnable = isEnabled;
        motor.getConfigurator().apply(hardwareLimitSwitchConfigs);
    }

    /**
     * Enable or disable the reverse hard limit switch.
     * @param isEnabled True to enable the hard limit switch
     */
    public void setupReverseHardLimitSwitch(boolean isEnabled, boolean isNormallyOpen)
    {
        HardwareLimitSwitchConfigs hardwareLimitSwitchConfigs = new HardwareLimitSwitchConfigs();
        motor.getConfigurator().refresh(hardwareLimitSwitchConfigs);

        hardwareLimitSwitchConfigs.ReverseLimitSource = ReverseLimitSourceValue.LimitSwitchPin;
        if(isNormallyOpen)
            hardwareLimitSwitchConfigs.ReverseLimitType = ReverseLimitTypeValue.NormallyOpen;
        else
            hardwareLimitSwitchConfigs.ReverseLimitType = ReverseLimitTypeValue.NormallyClosed;
        hardwareLimitSwitchConfigs.ReverseLimitEnable = isEnabled;
        motor.getConfigurator().apply(hardwareLimitSwitchConfigs);
    }

    /**
     * Set the current limits of the motor.
     * @param currentLimit The current limit in Amps
     * @param currentThreshold The max current limit in Amps
     * @param timeThreshold The time threshold in Seconds
     */
    public void setupCurrentLimit(double currentLimit, double currentThreshold, double timeThreshold)
    {
        CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        motor.getConfigurator().refresh(currentLimitsConfigs);

        currentLimitsConfigs.withSupplyCurrentLimit(currentLimit);
        currentLimitsConfigs.withSupplyCurrentThreshold(currentThreshold);
        currentLimitsConfigs.withSupplyTimeThreshold(timeThreshold);
        motor.getConfigurator().apply(currentLimitsConfigs);
    }

    /**
     * Set the maximum rate at which the motor output can change.
     * @param rampRateSeconds Time in seconds to go from 0 to full throttle
     */
    public void setupOpenLoopRampRate(double rampRateSeconds)
    {
        OpenLoopRampsConfigs openLoopRampsConfigs = new OpenLoopRampsConfigs();
        motor.getConfigurator().refresh(openLoopRampsConfigs);

        openLoopRampsConfigs.DutyCycleOpenLoopRampPeriod = rampRateSeconds;
        motor.getConfigurator().apply(openLoopRampsConfigs);
    }

    /**
     * Sets the voltage compensation for the motor controller. Use the battery voltage.
     * @param voltageCompensation The nominal voltage to compensate to
     */
    public void setupVoltageCompensation(double voltageCompensation)
    {
        VoltageConfigs voltageConfigs = new VoltageConfigs();
        motor.getConfigurator().refresh(voltageConfigs);

        voltageConfigs.PeakForwardVoltage = voltageCompensation;
        voltageConfigs.PeakReverseVoltage = -voltageCompensation;
        motor.getConfigurator().apply(voltageConfigs);
    }

    /**
     * Set the conversion factor to convert from sensor rotations to mechanism output.
     * @param factor The conversion factor to multiply by
     */
    public void setupPositionConversionFactor(double factor)
    {
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        motor.getConfigurator().refresh(feedbackConfigs);

        feedbackConfigs.SensorToMechanismRatio = factor;
        motor.getConfigurator().apply(feedbackConfigs);
    }

    /**
     * Set the conversion factor to convert from sensor velocity to mechanism velocity.
     * @param factor The conversion factor to multiply by
     */
    public void setupVelocityConversionFactor(double factor)
    {
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        motor.getConfigurator().refresh(feedbackConfigs);

        feedbackConfigs.SensorToMechanismRatio = factor;
        motor.getConfigurator().apply(feedbackConfigs);
    }

    /**
     * Set the PID controls for the motor.
     * @param kP The Proportional constant
     * @param kI The Integral constant
     * @param kD The Derivative constant
     */
    public void setupPIDController(int slotId, double kP, double kI, double kD)
    {
        if(slotId >= 0 && slotId <= 2)
        {
            SlotConfigs slotConfigs = new SlotConfigs();
            motor.getConfigurator().refresh(slotConfigs);
            slotConfigs.SlotNumber = slotId;
            slotConfigs.kP = kP;
            slotConfigs.kI = kI;
            slotConfigs.kD = kD;
            motor.getConfigurator().apply(slotConfigs); 
        }
    }

    /**
     * Set the position of the encoder.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @param position The position of the encoder
     */
    public void setPosition(double position)
    {
        motor.setPosition(position);
    }

    public void setControl(double position)
    {
        motor.setControl(positionVoltage.withPosition(position));
    }

    /**
     * Get the position of the encoder.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @return The position of the encoder
     */
    public double getPosition()
    {
        return motor.getPosition().getValueAsDouble();
    }

    /**
     * Get the velocity of the encoder.
     * Units are RPMs by default, but can be changed using the conversion factor.
     * @return The velocity of the encoder
     */    
    public double getVelocity()
    {
        return motor.getVelocity().getValueAsDouble() * 60.0;
    }

    @Override
    public void stopMotor()
    {
        set(0.0);
        feed();
    }

    @Override
    public String getDescription()
    {
        return motorControllerName;
    }

    @Override
    public void set(double speed)
    {
        motor.set(speed);
        feed();
    }

    @Override
    public void setVoltage(double outputVolts) 
    {
        super.setVoltage(outputVolts);
    }

    @Override
    public double get()
    {
        return motor.get();
    }

    /**
     * @deprecated Use <b>setupInverted()</b> instead
     */
    @Override
    public void setInverted(boolean isInverted)
    {
        setupInverted(isInverted);
    }

    @Override
    public boolean getInverted()
    {
        return motor.getInverted();
    }

    @Override
    public void disable()
    {
        motor.disable();
    }
    
    @Override
    public void initSendable(SendableBuilder builder) 
    {
        builder.setSmartDashboardType("MotorController");
        builder.setActuator(true);
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Value", this::get, this::set);
    }
}
