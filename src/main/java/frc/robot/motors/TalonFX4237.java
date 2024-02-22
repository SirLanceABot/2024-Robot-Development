package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.Slot2Configs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.ForwardLimitSourceValue;
import com.ctre.phoenix6.signals.ForwardLimitTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitSourceValue;
import com.ctre.phoenix6.signals.ReverseLimitTypeValue;
import com.ctre.phoenix6.spns.SpnValue;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DriverStation;


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

    @FunctionalInterface
    private interface Function
    {
        public abstract StatusCode apply();
    }

    private final TalonFX motor;
    private final PositionVoltage positionVoltage;
    private final VelocityVoltage velocityVoltage;
    private final String motorControllerName;
    
    private StringLogEntry motorLogEntry;
    private final int SETUP_ATTEMPT_LIMIT = 5;
    private int setupErrorCount = 0;

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

        this.motorControllerName = motorControllerName;
        motorLogEntry = new StringLogEntry(log, "/motors/setup", "Setup");
        motor = new TalonFX(deviceId, canbus);
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        motor.getConfigurator().refresh(feedbackConfigs);
        feedbackConfigs.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        motor.getConfigurator().apply(feedbackConfigs);
        positionVoltage = new PositionVoltage(0.0);
        velocityVoltage = new VelocityVoltage(0);
        clearStickyFaults();
        setupFactoryDefaults();

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + motorControllerName);
    }

    /** 
     * Check the motor controller for an error and print a message.
     * @param message The message to print
     */
    private void setup(Function func, String message)
    {
        StatusCode errorCode = StatusCode.OK;
        int attemptCount = 0;
        String logMessage = "";
        
        do
        {
            errorCode = func.apply();
            logMessage = motorControllerName + " : " + message + " " + errorCode;

            if(errorCode == StatusCode.OK)
                System.out.println(">> >> " + logMessage);
            else
                DriverStation.reportWarning(logMessage, true);
            motorLogEntry.append(logMessage);
            attemptCount++;
        }
        while(errorCode != StatusCode.OK && attemptCount < SETUP_ATTEMPT_LIMIT);

        setupErrorCount += (attemptCount - 1);
    }

    /**
     * Clear all sticky faults.
     */
    public void clearStickyFaults()
    {
        setup(() -> motor.clearStickyFaults(), "Clear Sticky Faults");
    }

    /**
     * Reset to the factory defaults.
     */
    public void setupFactoryDefaults()
    {
        setup(() -> motor.getConfigurator().apply(new TalonFXConfiguration()), "Setup Factory Defaults");
    }

    public void setupRemoteCANCoder(int remoteSensorId)
    {
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        motor.getConfigurator().refresh(feedbackConfigs);
        feedbackConfigs.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
        feedbackConfigs.FeedbackRemoteSensorID = remoteSensorId;
        setup(() -> motor.getConfigurator().apply(feedbackConfigs), "Setup Remote CANCoder");
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
        setup(() -> motor.getConfigurator().apply(motorOutputConfigs), "Setup Brake Mode");
    }

    /**
     * Sets the idle/neutral mode to coast mode.
     */
    public void setupCoastMode()
    {
        MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        motor.getConfigurator().refresh(motorOutputConfigs);
        motorOutputConfigs.NeutralMode = NeutralModeValue.Coast;
        setup(() -> motor.getConfigurator().apply(motorOutputConfigs), "Setup Coast Mode");
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
        setup(() -> motor.getConfigurator().apply(softLimitSwitchConfigs), "Setup Forward Soft Limit");
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
        setup(() -> motor.getConfigurator().apply(softLimitSwitchConfigs), "Setup Reverse Soft Limit");
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
        setup(() -> motor.getConfigurator().apply(hardwareLimitSwitchConfigs), "Setup Forward Hard Limit");
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
        setup(() -> motor.getConfigurator().apply(hardwareLimitSwitchConfigs), "Setup Reverse Hard Limit");
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

        currentLimitsConfigs.SupplyCurrentLimit = currentLimit;
        currentLimitsConfigs.SupplyCurrentThreshold = currentThreshold;
        currentLimitsConfigs.SupplyTimeThreshold = timeThreshold;
        setup(() -> motor.getConfigurator().apply(currentLimitsConfigs), "Setup Current Limit");
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
        setup(() -> motor.getConfigurator().apply(openLoopRampsConfigs), "Setup Open Loop Ramp Rate");
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
        setup(() -> motor.getConfigurator().apply(voltageConfigs), "Setup Voltage Compensation");
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
        setup(() -> motor.getConfigurator().apply(feedbackConfigs), "Setup Position Conversion Factor");
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
        setup(() -> motor.getConfigurator().apply(feedbackConfigs), "Setup Velocity Conversion Factor");
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
            setup(() -> motor.getConfigurator().apply(slotConfigs), "Setup PID Controller"); 
        }
    }

    /**
     * Set the PID controls for the motor.
     * @param kP The Proportional constant
     * @param kI The Integral constant
     * @param kD The Derivative constant
     */
    public void setupPIDController(int slotId, double kP, double kI, double kD, double kS, double kV)
    {
        if(slotId >= 0 && slotId <= 2)
        {
            SlotConfigs slotConfigs = new SlotConfigs();
            motor.getConfigurator().refresh(slotConfigs);
            slotConfigs.SlotNumber = slotId;
            slotConfigs.kP = kP;
            slotConfigs.kI = kI;
            slotConfigs.kD = kD;
            slotConfigs.kS = kS;
            slotConfigs.kV = kV;
            setup(() -> motor.getConfigurator().apply(slotConfigs), "Setup PID Controller"); 
        }
    }

    public double[] getPID(int slotId)
    {
        double[] pid = {0.0, 0.0, 0.0};
        if(slotId >= 0 && slotId <= 2)
        {
            SlotConfigs slotConfigs = new SlotConfigs();
            switch(slotId)
            {
                case 0:
                    motor.getConfigurator().refresh(Slot0Configs.from(slotConfigs));
                    break;
                case 1:
                    motor.getConfigurator().refresh(Slot1Configs.from(slotConfigs));
                    break;
                case 2:
                    motor.getConfigurator().refresh(Slot2Configs.from(slotConfigs));
                    break;
            }     
                
            pid[0] = slotConfigs.kP;
            pid[1] = slotConfigs.kI;
            pid[2] = slotConfigs.kD;
        }
        return pid;
    }

    /**
     * Sets a motor to be a follower of another motor.
     * Setting the power of the leader, also sets the power of the follower.
     * @param leaderId The id of the leader motor on the can bus
     * @param isInverted True to invert the motor so it runs opposite of the leader
     */
    public void setupFollower(int leaderId, boolean isInverted)
    {
        setup(() -> motor.setControl(new Follower(leaderId, isInverted)), "Setup Follower");
    }

    /**
     * Logs the sticky faults
     */
    public void logStickyFaults()
    {
        // int faults = motor.getStickyFaultField().getValue();
        int faultsCount = 0;
        motorLogEntry = new StringLogEntry(log, "/motors/faults", "Faults");

        if(motor.getStickyFault_BootDuringEnable().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_BootDuringEnable);
            faultsCount++;
        }
        if(motor.getStickyFault_BridgeBrownout().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_BridgeBrownout);
            faultsCount++;
        }
        if(motor.getStickyFault_DeviceTemp().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_DeviceTemp);
            faultsCount++;
        }
        if(motor.getStickyFault_ForwardHardLimit().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_ForwardHardLimit);
            faultsCount++;
        }
        if(motor.getStickyFault_ForwardSoftLimit().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_ForwardSoftLimit);
            faultsCount++;
        }
        if(motor.getStickyFault_FusedSensorOutOfSync().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_FusedSensorOutOfSync);
            faultsCount++;
        }
        if(motor.getStickyFault_Hardware().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_Hardware);
            faultsCount++;
        }
        if(motor.getStickyFault_MissingDifferentialFX().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_MissingDifferentialFX);
            faultsCount++;
        }
        if(motor.getStickyFault_OverSupplyV().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_OverSupplyV);
            faultsCount++;
        }
        if(motor.getStickyFault_RemoteSensorDataInvalid().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_RemoteSensorPosOverflow);
            faultsCount++;
        }
        if(motor.getStickyFault_RemoteSensorPosOverflow().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_RemoteSensorPosOverflow);
            faultsCount++;
        }
        if(motor.getStickyFault_RemoteSensorReset().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_RemoteSensorReset);
            faultsCount++;
        }
        if(motor.getStickyFault_ReverseHardLimit().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_ReverseHardLimit);
            faultsCount++;
        }
        if(motor.getStickyFault_ReverseSoftLimit().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_ReverseSoftLimit);
            faultsCount++;
        }
        if(motor.getStickyFault_StatorCurrLimit().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_StatorCurrLimit);
            faultsCount++;
        }
        if(motor.getStickyFault_SupplyCurrLimit().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_SupplyCurrLimit);
            faultsCount++;
        }
        if(motor.getStickyFault_Undervoltage().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_Undervoltage);
            faultsCount++;
        }
        if(motor.getStickyFault_UnlicensedFeatureInUse().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_UnlicensedFeatureInUse);
            faultsCount++;
        }
        if(motor.getStickyFault_UnstableSupplyV().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_UnstableSupplyV);
            faultsCount++;
        }
        if(motor.getStickyFault_UsingFusedCANcoderWhileUnlicensed().getValue())
        {
            motorLogEntry.append(motorControllerName + " : " + SpnValue.StickyFault_TALONFX_UsingFusedCCWhileUnlicensed);
            faultsCount++;
        }

        if(faultsCount == 0)
            motorLogEntry.append(motorControllerName + " : No Sticky Faults");
    }

    /**
     * Move the motor to a position using PID control.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @param position The position to move the motor to
     */
    public void setControlPosition(double position)
    {
        motor.setControl(positionVoltage.withPosition(position));
    }

    /**
     * Spin the motor to a velocity using PID control.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @param velocity The velocity to spin the motor at
     */
    public void setControlVelocity(double velocity)
    {
        motor.setControl(velocityVoltage.withVelocity(velocity));
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
     * Units are RPS by default, but can be changed using the conversion factor.
     * @return The velocity of the encoder
     */    
    public double getVelocity()
    {
        return motor.getVelocity().getValueAsDouble();
    }

    /**
     * Get the applied motor voltage (in volts).
     * @return The voltage
     */    
    public double getMotorVoltage()
    {
        return motor.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public void stopMotor()
    {
        set(0.0);
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
        feed();
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

}
