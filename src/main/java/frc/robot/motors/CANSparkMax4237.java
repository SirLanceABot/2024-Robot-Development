package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringEntry;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;

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

    @FunctionalInterface
    private interface Function
    {
        public abstract REVLibError doAction();
    }

    private final CANSparkMax motor;
    private final RelativeEncoder encoder;
    private SparkAbsoluteEncoder sparkAbsoluteEncoder = null;
    private SparkLimitSwitch forwardLimitSwitch = null;
    private SparkLimitSwitch reverseLimitSwitch = null;
    private SparkPIDController sparkPIDController = null;
    private final String motorControllerName;

    private final NetworkTable canSparkMaxTable;    
    private StringEntry strEntry;
    // private StringLogEntry motorLogEntry;

    private final int SETUP_ATTEMPT_LIMIT = 5;
    private int setupErrorCount = 0;



    /**
     * Creates a CANSparkMax on the CANbus with a brushless motor (Neo550 or Neo1650).
     * Defaults to using the built-in encoder sensor (RelativeEncoder).
     * @param deviceId The id number of the device on the CANbus
     * @param canbus The name of the CANbus (ex. "rio" is the default name of the roboRIO CANbus)
     * @param motorControllerName The name describing the purpose of this motor controller
     */
    public CANSparkMax4237(int deviceId, String canbus, String motorControllerName)
    {
        super(motorControllerName);

        System.out.println("  Constructor Started:  " + fullClassName + " >> " + motorControllerName);

        this.motorControllerName = motorControllerName;

        canSparkMaxTable = NetworkTableInstance.getDefault().getTable(Constants.NETWORK_TABLE_NAME);
        strEntry = canSparkMaxTable.getStringTopic("Motors/Setup").getEntry("");
        // strEntry.setDefault("");
        // motorLogEntry = new StringLogEntry(log, "/motors/setup", "Setup");

        motor = new CANSparkMax(deviceId, CANSparkLowLevel.MotorType.kBrushless);
        encoder = motor.getEncoder();
        sparkPIDController = motor.getPIDController();
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
        REVLibError errorCode = REVLibError.kOk;
        int attemptCount = 0;
        String logMessage = "";
        
        do
        {
            errorCode = func.doAction();
            logMessage = motorControllerName + " : " + message + " " + errorCode;

            if(errorCode == REVLibError.kOk)
                System.out.println(">> >> " + logMessage);
            else
                DriverStation.reportWarning(logMessage, false);
            
            strEntry.set(logMessage);
            // motorLogEntry.append(logMessage);
            attemptCount++;
        }
        while(errorCode != REVLibError.kOk && attemptCount < SETUP_ATTEMPT_LIMIT);

        setupErrorCount += (attemptCount - 1);
    }

    public void burnFlash()
    {
        motor.burnFlash();
    }

    /**
     * Clear all sticky faults.
     */
    public void clearStickyFaults()
    {
        setup(() -> motor.clearFaults(), "Clear Sticky Faults");
    }

    /**
     * Reset to the factory defaults.
     */
    public void setupFactoryDefaults()
    {
        setup(() -> motor.restoreFactoryDefaults(false), "Setup Factory Defaults");
    }

    public void setupRemoteCANCoder(int remoteSensorId)
    {
        sparkAbsoluteEncoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.fromId(remoteSensorId));
    }

    /**
     * Set the Periodic Frame Period.
     * <p>Defaults: Status0 - 10ms, Status1 - 20ms, Status2 - 20ms, Status3 - 50ms, Status4 - 20ms, Status5 - 200ms, Status6 - 200ms, Status7 - 250ms
     * @param frameNumber The frame number to set
     * @param periodMs The time period in milliseconds
     */
    public void setupPeriodicFramePeriod(int frameNumber, int periodMs)
    {
        switch(frameNumber)
        {
            case 0:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0, periodMs), "Setup Periodic Frame Period 0");
                break;
            case 1:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1, periodMs), "Setup Periodic Frame Period 1");
                break;
            case 2:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2, periodMs), "Setup Periodic Frame Period 2");
                break;
            case 3:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus3, periodMs), "Setup Periodic Frame Period 3");
                break;
            case 4:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus4, periodMs), "Setup Periodic Frame Period 4");
                break;
            case 5:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus5, periodMs), "Setup Periodic Frame Period 5");
                break;
            case 6:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus6, periodMs), "Setup Periodic Frame Period 6");
                break;
            case 7:
                setup(() -> motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus7, periodMs), "Setup Periodic Frame Period 7");
                break;
            default:
                System.out.println("ERROR - Invalid Status Frame Period");
                break;
        }
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
        setup(() -> motor.setIdleMode(IdleMode.kBrake), "Setup Brake Mode");
    }
    
    /**
     * Sets the idle/neutral mode to coast mode.
     */
    public void setupCoastMode()
    {
        setup(() -> motor.setIdleMode(IdleMode.kCoast), "Setup Coast Mode");
    }

    /**
     * Set the forward soft limit.
     * @param limit The forward soft limit value
     * @param isEnabled True to enable the forward soft limit
     */
    public void setupForwardSoftLimit(double limit, boolean isEnabled)
    {
        setup(() -> motor.setSoftLimit(SoftLimitDirection.kForward, (float) limit), "Setup Forward Soft Limit");
        setup(() -> motor.enableSoftLimit(SoftLimitDirection.kForward, isEnabled), "Enable Forward Soft Limit");
    }

    /**
     * Set the reverse soft limit.
     * @param limit The reverse soft limit value
     * @param isEnabled True to enable the reverse soft limit
     */
    public void setupReverseSoftLimit(double limit, boolean isEnabled)
    {
        setup(() -> motor.setSoftLimit(SoftLimitDirection.kReverse, (float) limit), "Setup Reverse Soft Limit");
        setup(() -> motor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled), "Enable Reverse Soft Limit");
    }

    /**
     * Enable or disable the forward hard limit switch.
     * @param isEnabled True to enable the hard limit switch
     */
    public void setupForwardHardLimitSwitch(boolean isEnabled, boolean isNormallyOpen)
    {
        if(isNormallyOpen)
            forwardLimitSwitch = motor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        else
            forwardLimitSwitch = motor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyClosed);
        setup(() -> forwardLimitSwitch.enableLimitSwitch(isEnabled), "Setup Forward Hard Limit");
    }

    /**
     * Enable or disable the reverse hard limit switch.
     * @param isEnabled True to enable the hard limit switch
     */
    public void setupReverseHardLimitSwitch(boolean isEnabled, boolean isNormallyOpen)
    {
        if(isNormallyOpen)
            reverseLimitSwitch = motor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        else
            reverseLimitSwitch = motor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        setup(() -> reverseLimitSwitch.enableLimitSwitch(isEnabled), "Setup Reverse Hard Limit");
    }

    /**
     * Set the current limits of the motor.
     * @param currentLimit The current limit in Amps
     * @param currentThreshold The max current limit in Amps
     * @param timeThreshold The time threshold in Seconds
     */
    public void setupCurrentLimit(double currentLimit, double currentThreshold, double timeThreshold)
    {
        setup(() -> motor.setSmartCurrentLimit((int) currentLimit), "Setup Smart Current Limit");
        setup(() -> motor.setSecondaryCurrentLimit(currentThreshold, (int) (timeThreshold / 0.00005) ), "Setup Secondary Current Limit");
    }

    /**
     * Set the maximum rate at which the motor output can change.
     * @param rampRateSeconds Time in seconds to go from 0 to full throttle
     */
    public void setupOpenLoopRampRate(double rampRateSeconds)
    {
        setup(() -> motor.setOpenLoopRampRate(rampRateSeconds), "Setup Open Loop Ramp Rate");
    }

    /**
     * Sets the voltage compensation for the motor controller. Use the battery voltage.
     * @param voltageCompensation The nominal voltage to compensate to
     */
    public void setupVoltageCompensation(double voltageCompensation)
    {
        setup(() -> motor.enableVoltageCompensation(voltageCompensation), "Setup Voltage Compensation");
    }

    /**
     * Set the conversion factor to convert from sensor position to mechanism position.
     * @param factor The conversion factor to multiply by
     */
    public void setupPositionConversionFactor(double factor)
    {
        if(sparkAbsoluteEncoder == null)
            setup(() -> encoder.setPositionConversionFactor(factor), "Setup Position Conversion Factor");
        else
            setup(() -> sparkAbsoluteEncoder.setPositionConversionFactor(factor), "Setup Position Conversion Factor");
    }

    /**
     * Set the conversion factor to convert from sensor velocity to mechanism velocity.
     * @param factor The conversion factor to multiply by
     */
    public void setupVelocityConversionFactor(double factor)
    {
        if(sparkAbsoluteEncoder == null)
            setup(() -> encoder.setVelocityConversionFactor(factor), "Setup Velocity Conversion Factor");
        else
            setup(() -> sparkAbsoluteEncoder.setVelocityConversionFactor(factor), "Setup Velocity Conversion Factor");
    }

    /**
     * Set the PID controls for the motor.
     * @param kP The Proportional gain constant
     * @param kI The Integral gain constant
     * @param kD The Derivative gain constant
     */
    public void setupPIDController(int slotId, double kP, double kI, double kD)
    {
        if(slotId >= 0 && slotId <= 3)
        {
            // set PID coefficients
            setup(() -> sparkPIDController.setP(kP, slotId), "Setup PIDController(kP)");
            setup(() -> sparkPIDController.setI(kI, slotId), "Setup PIDController(kI)");
            setup(() -> sparkPIDController.setD(kD, slotId), "Setup PIDController(kD)");
        }

        // pidController.setIZone(kIz);
        // pidController.setFF(kFF);
        // pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    public double[] getPID(int slotId)
    {
        double[] pid = {0.0, 0.0, 0.0};

        if(slotId >= 0 && slotId <= 3)
        {
            pid[0] = sparkPIDController.getP(slotId);
            pid[1] = sparkPIDController.getI(slotId);
            pid[2] = sparkPIDController.getD(slotId);
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
        setup(() -> motor.follow(CANSparkBase.ExternalFollower.kFollowerSpark, leaderId, isInverted), "Setup Follower");
    }

    /**
     * @return true if limit switch is pressed
     */
    public boolean isForwardLimitSwitchPressed()
    {
        return forwardLimitSwitch.isPressed();
    }

    /**
     * @return true if limit switch is pressed
     */
    public boolean isReverseLimitSwitchPressed()
    {
        return reverseLimitSwitch.isPressed();
    }

    /**
     * Logs the sticky faults
     */
    public void logStickyFaults()
    {
        int faults = motor.getStickyFaults();
        strEntry = canSparkMaxTable.getStringTopic("Motors/Faults").getEntry("");
        // motorLogEntry = new StringLogEntry(log, "/motors/faults", "Faults");

        if(faults > 0)
        {
            for(int i = 0; i < 16; i++)
            {
                if((faults & (1 << i)) > 0)
                    strEntry.set(motorControllerName + " : " + CANSparkBase.FaultID.fromId(i));
                    // motorLogEntry.append(motorControllerName + " : " + CANSparkBase.FaultID.fromId(i));
            }
        }
        else
            strEntry.set(motorControllerName + " : No Sticky Faults");
            // motorLogEntry.append(motorControllerName + " : No Sticky Faults");

        clearStickyFaults();
    }

    /**
     * Move the motor to a position using PID control.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @param position The position to move the motor to
     */
    public void setControlPosition(double position)
    {
        sparkPIDController.setReference(position, CANSparkBase.ControlType.kPosition);
    }

    /**
     * Spin the motor to a velocity using PID control.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @param velocity The velocity to spin the motor at
     */
    public void setControlVelocity(double velocity)
    {
        sparkPIDController.setReference(velocity, CANSparkBase.ControlType.kVelocity);
    }

    /**
     * Set the position of the encoder.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @param position The position of the encoder
     */
    public void setPosition(double position)
    {
        encoder.setPosition(position);
    }

    /**
     * Get the position of the encoder.
     * Units are rotations by default, but can be changed using the conversion factor.
     * @return The position of the encoder
     */
    public double getPosition()
    {
        if(sparkAbsoluteEncoder == null)
            return encoder.getPosition();
        else
            return sparkAbsoluteEncoder.getPosition();
    }

    /**
     * Get the velocity of the encoder.
     * Units are RPMs by default, but can be changed using the conversion factor.
     * @return The velocity of the encoder
     */    
    public double getVelocity()
    {
        if(sparkAbsoluteEncoder == null)
            return encoder.getVelocity();
        else
            return sparkAbsoluteEncoder.getVelocity();
    }

    /**
     * Get the applied motor voltage (in volts).
     * @return The voltage
     */    
    public double getMotorVoltage()
    {
        return motor.getBusVoltage();
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
        motor.setVoltage(outputVolts);
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
