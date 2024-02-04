package frc.robot.motors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * This abstract class defines the abstract methods that all motor controllers have.
 * Every motor controller will automatically have the Watchdog enabled.
 */
public abstract class MotorController4237 extends MotorSafety implements MotorController, Sendable
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Enables the Watchdog for the motor by default.
     * Can be disabled by calling setSafetyEnabled(false).
     * @param motorControllerName The name of the motor controller/mechanism, for debugging purposes
     */
    MotorController4237(String motorControllerName)
    {
        System.out.println("  Constructor Started:  " + fullClassName  + " >> " + motorControllerName);
        
        // Enable the Watchdog for the motor
        setSafetyEnabled(true);
        
        System.out.println("  Constructor Finished: " + fullClassName + " >> " + motorControllerName);
    }


    // *** ABSTRACT METHODS ***
    // These methods must be defined in any subclass that extends this class
    public abstract void clearStickyFaults();
    public abstract void setupFactoryDefaults();
    public abstract void setupRemoteCANCoder(int remoteSensorId);
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
    public abstract void setupPIDController(int slotId, double kP, double kI, double kD);
    public abstract void setupFollower(int leaderId, boolean isInverted);

    public abstract void setControl(double position);
    public abstract void setPosition(double position);
    public abstract double getPosition();
    public abstract double getVelocity();
}
