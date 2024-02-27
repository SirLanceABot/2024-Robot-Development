package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;

public class CANcoder4237 extends Sensor4237
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

    private class PeriodicData
    {
        private double relativePosition = 0.0;
        private double absolutePosition = 0.0;
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private final String cancoderName;
    private final CANcoder cancoder;
    private final PeriodicData periodicData;

    final static DataLog log = DataLogManager.getLog();
    private StringLogEntry cancoderLogEntry;
    private DoubleLogEntry doubleLogEntry;

    private final int SETUP_ATTEMPT_LIMIT = 5;
    private int setupErrorCount = 0;

    private boolean logPeriodicData = false;


    public CANcoder4237(int deviceID, String canbus, String cancoderName)
    {
        super(cancoderName);
        System.out.println("  Constructor Started:  " + fullClassName + " >> " + cancoderName);

        this.cancoderName = cancoderName;
        cancoderLogEntry = new StringLogEntry(log, "/cancoders/setup", "Setup");
        doubleLogEntry = new DoubleLogEntry(log, "cancoders/" + cancoderName, "Degrees");

        cancoder = new CANcoder(deviceID);
        periodicData = new PeriodicData();
        
        System.out.println("  Constructor Finished: " + fullClassName + " >> " + cancoderName);
    }

    /** 
     * Check the CANCoder for an error and print a message.
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
            logMessage = cancoderName + " : " + message + " " + errorCode;

            if(errorCode == StatusCode.OK)
                System.out.println(">> >> " + logMessage);
            else
                DriverStation.reportWarning(logMessage, true);
            cancoderLogEntry.append(logMessage);
            attemptCount++;
        }
        while(errorCode != StatusCode.OK && attemptCount < SETUP_ATTEMPT_LIMIT);

        setupErrorCount += (attemptCount - 1);
    }
    
    public void setupFactoryDefaults()
    {
        setup(() -> cancoder.getConfigurator().apply(new CANcoderConfiguration()), "Setup Factory Defaults");
    }

    public void setupAbsoluteSensorRange_0To1()
    {
        MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        cancoder.getConfigurator().refresh(magnetSensorConfigs);

        magnetSensorConfigs.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        setup(() -> cancoder.getConfigurator().apply(magnetSensorConfigs), "Setup AbsoluteSensorRange_0To1");
    }

    public void setupAbsoluteSensorRange_PlusMinusHalf()
    {
        MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        cancoder.getConfigurator().refresh(magnetSensorConfigs);

        magnetSensorConfigs.AbsoluteSensorRange = AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
        setup(() -> cancoder.getConfigurator().apply(magnetSensorConfigs), "Setup AbsoluteSensorRange_PlusMinusHalf");
    }

    public void setupMagnetOffset(double magnetOffset)
    {
        MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        cancoder.getConfigurator().refresh(magnetSensorConfigs);

        magnetSensorConfigs.MagnetOffset = magnetOffset;
        setup(() -> cancoder.getConfigurator().apply(magnetSensorConfigs), "Setup MagnetOffset_" + magnetOffset);
    }

    public void setupSensorDirection_ClockwisePositive()
    {
        MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        cancoder.getConfigurator().refresh(magnetSensorConfigs);

        magnetSensorConfigs.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        setup(() -> cancoder.getConfigurator().apply(magnetSensorConfigs), "Setup SensorDirection_ClockwisePositive");
    }

    public void setupSensorDirection_CounterClockwisePositive()
    {
        MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        cancoder.getConfigurator().refresh(magnetSensorConfigs);

        magnetSensorConfigs.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        setup(() -> cancoder.getConfigurator().apply(magnetSensorConfigs), "Setup SensorDirection_CounterClockwisePositive");
    }

    public double getPosition()
    {
        return periodicData.relativePosition;
    }

    public void setPosition(double position)
    {
        cancoder.setPosition(position);
    }

    public double getAbsolutePosition()
    {
        return periodicData.absolutePosition;
    }

    public void logPeriodicData(boolean isEnabled)
    {
        logPeriodicData = isEnabled;
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.relativePosition = cancoder.getPosition().getValueAsDouble();
        periodicData.absolutePosition = cancoder.getAbsolutePosition().getValueAsDouble();
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(logPeriodicData)
            doubleLogEntry.append(periodicData.absolutePosition);
    }

    @Override
    public void runPeriodicTask()
    {

    }

    @Override
    public String toString()
    {
        CANcoderConfiguration cancoderConfiguration = new CANcoderConfiguration();
        cancoder.getConfigurator().refresh(cancoderConfiguration);

        return cancoderConfiguration.serialize();
    }
}