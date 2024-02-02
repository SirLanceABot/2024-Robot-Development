package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.util.sendable.SendableBuilder;

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
    private final RelativeEncoder encoder;
    private SparkLimitSwitch forwardLimitSwitch;
    private SparkLimitSwitch reverseLimitSwitch;
    private SparkPIDController pidController;
    private final String motorControllerName;

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
        
        motor = new CANSparkMax(deviceId, CANSparkLowLevel.MotorType.kBrushless);
        encoder = motor.getEncoder();
        pidController = motor.getPIDController();
        this.motorControllerName = motorControllerName;

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + motorControllerName);
    }

    /**
     * Reset to the factory defaults.
     */
    public void setupFactoryDefaults()
    {
        motor.restoreFactoryDefaults(false);
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
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0, periodMs);
                break;
            case 1:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1, periodMs);
                break;
            case 2:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2, periodMs);
                break;
            case 3:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus3, periodMs);
                break;
            case 4:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus4, periodMs);
                break;
            case 5:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus5, periodMs);
                break;
            case 6:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus6, periodMs);
                break;
            case 7:
                motor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus7, periodMs);
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
        motor.setIdleMode(IdleMode.kBrake);
    }
    
    /**
     * Sets the idle/neutral mode to coast mode.
     */
    public void setupCoastMode()
    {
        motor.setIdleMode(IdleMode.kCoast);
    }

    /**
     * Set the forward soft limit.
     * @param limit The forward soft limit value
     * @param isEnabled True to enable the forward soft limit
     */
    public void setupForwardSoftLimit(double limit, boolean isEnabled)
    {
        motor.setSoftLimit(SoftLimitDirection.kForward, (float) limit);
        motor.enableSoftLimit(SoftLimitDirection.kForward, isEnabled);
    }

    /**
     * Set the reverse soft limit.
     * @param limit The reverse soft limit value
     * @param isEnabled True to enable the reverse soft limit
     */
    public void setupReverseSoftLimit(double limit, boolean isEnabled)
    {
        motor.setSoftLimit(SoftLimitDirection.kReverse, (float) limit);
        motor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled);
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
        forwardLimitSwitch.enableLimitSwitch(isEnabled);
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
        reverseLimitSwitch.enableLimitSwitch(isEnabled);
    }

    /**
     * Set the current limits of the motor.
     * @param currentLimit The current limit in Amps
     * @param currentThreshold The max current limit in Amps
     * @param timeThreshold The time threshold in Seconds
     */
    public void setupCurrentLimit(double currentLimit, double currentThreshold, double timeThreshold)
    {
        motor.setSmartCurrentLimit((int) currentLimit);
        motor.setSecondaryCurrentLimit(currentThreshold, (int) (timeThreshold / 0.00005) );
    }

    /**
     * Set the maximum rate at which the motor output can change.
     * @param rampRateSeconds Time in seconds to go from 0 to full throttle
     */
    public void setupOpenLoopRampRate(double rampRateSeconds)
    {
        motor.setOpenLoopRampRate(rampRateSeconds);
    }

    /**
     * Sets the voltage compensation for the motor controller. Use the battery voltage.
     * @param voltageCompensation The nominal voltage to compensate to
     */
    public void setupVoltageCompensation(double voltageCompensation)
    {
        motor.enableVoltageCompensation(voltageCompensation);
    }

    /**
     * Set the conversion factor to convert from sensor position to mechanism position.
     * @param factor The conversion factor to multiply by
     */
    public void setupPositionConversionFactor(double factor)
    {
        encoder.setPositionConversionFactor(factor);
    }

    /**
     * Set the conversion factor to convert from sensor velocity to mechanism velocity.
     * @param factor The conversion factor to multiply by
     */
    public void setupVelocityConversionFactor(double factor)
    {
        encoder.setVelocityConversionFactor(factor);
    }

    /**
     * Set the PID controls for the motor.
     * @param kP The Proportional constant
     * @param kI The Integral constant
     * @param kD The Derivative constant
     */
    public void setupPIDController(int slotId, double kP, double kI, double kD)
    {
        if(slotId >= 0 && slotId <= 3)
        {
            // set PID coefficients
            pidController.setP(kP, slotId);
            pidController.setI(kI, slotId);
            pidController.setD(kD, slotId);            
        }

        // pidController.setIZone(kIz);
        // pidController.setFF(kFF);
        // pidController.setOutputRange(kMinOutput, kMaxOutput);
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
        return encoder.getPosition();
    }

    /**
     * Get the velocity of the encoder.
     * Units are RPMs by default, but can be changed using the conversion factor.
     * @return The velocity of the encoder
     */    
    public double getVelocity()
    {
        return encoder.getVelocity();
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
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Value", this::get, this::set);
    }
}
