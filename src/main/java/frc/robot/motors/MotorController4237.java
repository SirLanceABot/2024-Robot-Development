package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * This abstract class defines the abstract methods that all motor controllers have.
 */
public abstract class MotorController4237 extends MotorSafety implements MotorController
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** ABSTRACT METHODS ***
    // These must be defined in any subclass that extends this class.
    public abstract void setupFactoryDefaults();
    public abstract void setupPeriodicFramePeriod(int frameNumber, int periodMs);
    public abstract void setupInverted(boolean isInverted);
    public abstract void setupBrakeMode();
    public abstract void setupCoastMode();
    public abstract void setupForwardSoftLimit(double limit, boolean isEnabled);
    public abstract void setupReverseSoftLimit(double limit, boolean isEnabled);
    public abstract void setupForwardHardLimitSwitch(boolean isEnabled, boolean isNormallyOpen);
    public abstract void setupReverseHardLimitSwitch(boolean isEnabled, boolean isNormallyOpen);
    public abstract void setupCurrentLimit(double currentLimit, double currentThreshold, double timeThreshold);
    public abstract void setupOpenLoopRampRate(double rampRateSeconds);
    public abstract void setupVoltageCompensation(double voltageCompensation);
    public abstract void setupPositionConversionFactor(double factor);
    public abstract void setupVelocityConversionFactor(double factor);

    public abstract void setPosition(double position);
    public abstract double getPosition();
    public abstract double getVelocity();


    /** 
     * The constructor enables the Watchdog for the motor by default.
     * @param motorControllerName The name of the motor controller, or the mechanism it belongs to
     */
    MotorController4237(String motorControllerName)
    {
        System.out.println("  Constructor Started:  " + fullClassName  + " >> " + motorControllerName);
        
        setSafetyEnabled(true);
        
        System.out.println("  Constructor Finished: " + fullClassName + " >> " + motorControllerName);
    }
}
