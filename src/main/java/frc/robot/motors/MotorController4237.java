package frc.robot.motors;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;


/**
 * This abstract class defines the abstract methods that all motor controllers have.
 * Every motor controller will automatically have the Watchdog enabled.
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


    // *** CLASS and INSTANCE VARAIBLES ***
    // These varaibles are class and instance variables
    final static DataLog log = DataLogManager.getLog();
    private final static ArrayList<MotorController4237> allMotorControllers4237 = new ArrayList<MotorController4237>();


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

        // Register this motor in order to log sticky faults at the end of the match
        registerMotorController4237();
        
        System.out.println("  Constructor Finished: " + fullClassName + " >> " + motorControllerName);
    }

    
    // *** METHODS ***
    // Put all methods methods here

    /**
     * Register the motor controller in the array list
     */
    private void registerMotorController4237()
    {
        allMotorControllers4237.add(this);
    }

    /**
     * Static method to log sticky faults of the motor controllers in the array list.
     * Call this method from the telopExit() method in the Robot class.
     */
    public static void logAllStickyFaults()
    {
        for(MotorController4237 motorController4237 : allMotorControllers4237)
            motorController4237.logStickyFaults();
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

    public abstract double[] getPID(int slotId);
    public abstract void logStickyFaults();

    public abstract void setControlPosition(double position);
    public abstract void setControlVelocity(double velocity);
    public abstract void setPosition(double position);
    public abstract double getPosition();
    public abstract double getVelocity();
    public abstract double getMotorVoltage();
}
