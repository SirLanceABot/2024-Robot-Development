package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

// import com.ctre.phoenix.ErrorCode;
// import com.ctre.phoenix.sensors.AbsoluteSensorRange;
// import com.ctre.phoenix.sensors.CANCoder;
// import com.ctre.phoenix.sensors.SensorInitializationStrategy;
// import com.ctre.phoenix.sensors.SensorTimeBase;
// import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
// import com.ctre.phoenix.motorcontrol.can.TalonFX;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;

import frc.robot.Constants;


class SwerveModule2023 extends RobotDriveBase implements Module
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private final String moduleName;

    private final TalonFX driveMotor;
    // private final CANSparkMax driveMotor;
    // private final RelativeEncoder driveMotorEncoder;

    private final boolean driveMotorInverted;
    private final CANcoder turnEncoder;
    private final double turnEncoderOffset;

    private final TalonFX turnMotor;
    // private final CANSparkMax turnMotor;
    // private final RelativeEncoder turnMotorEncoder;

    private final PIDController drivePIDController = new PIDController(3.5, 0, 0.09); // volts / (m/s) for P and volts / (m/s^2) for D
    // private final PIDController turningPIDController = new PIDController(1.0, 0, 0);

    private final ProfiledPIDController turningPIDController =
        new ProfiledPIDController(
            0.0789, 0.0, 0.000877,
            // FIXME Changing radians to degrees, divided by 57 roughly
            // 4.5, 0.0, 0.05, //4.5, 0.0, 0.05,
            new TrapezoidProfile.Constraints(Constants.DrivetrainConstants.MAX_MODULE_TURN_SPEED, Constants.DrivetrainConstants.MAX_MODULE_TURN_ACCELERATION));

    //FIXME: Gains are for example purposes only - must be determined for your own robot!
    //First parameter is static gain (how much voltage it takes to move)
    //Second parameters is veloctiy gain (how much additional speed you get per volt)
    
    private final SimpleMotorFeedforward driveFeedforward = new SimpleMotorFeedforward(0.165, 2.1, 0.0);
    
    // TODO Changing radians to degrees, Changed volts per radian to volts per degree by multiplying by 2 pi and dividing by 360
    private final SimpleMotorFeedforward turnFeedforward = new SimpleMotorFeedforward(0.0035, 0.0052, 0.0);
    // private final SimpleMotorFeedforward turnFeedforward = new SimpleMotorFeedforward(0.2, 0.3, 0.0);


    // *** CLASS CONSTRUCTOR ***
    /**
     * Constructs a SwerveModule.
     *
     * @param smc SwerveModuleData for making SwerveModules
     */
    SwerveModule2023(SwerveModuleConfig smc)
    {
        moduleName = smc.moduleName;
        System.out.println("  Constructor Started:  " + fullClassName + " >> " + moduleName);

        driveMotor = new TalonFX(smc.driveMotorChannel, Constants.Drivetrain.MOTOR_CAN_BUS);
        // driveMotor = new CANSparkMax(smd.driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        // driveMotorEncoder = driveMotor.getEncoder();
        driveMotorInverted = smc.driveMotorInverted;
        turnEncoder = new CANcoder(smc.turnEncoderChannel, Constants.Drivetrain.CANCODER_CAN_BUS);  
        turnEncoderOffset = smc.turnEncoderOffset;
        turnMotor = new TalonFX(smc.turnMotorChannel, Constants.Drivetrain.MOTOR_CAN_BUS);
        // turnMotor = new CANSparkMax(smd.turnMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        // turnMotorEncoder = turnMotor.getEncoder();

        configDriveMotor();
        configCANCoder();
        configTurnMotor(); // Do not invert any of the turning motors

        // TODO: Cleanup old commented out code

        // resetTurningMotorEncoder();

        // Set the distance per pulse for the drive encoder. We can simply use the
        // distance traveled for one rotation of the wheel divided by the encoder
        // resolution.
        // driveEncoder.setDistancePerPulse(2 * Math.PI * kWheelRadius / kEncoderResolution);

        // Set the distance (in this case, angle) per pulse for the turning encoder.
        // This is the the angle through an entire rotation (2 * wpi::math::pi)
        // divided by the encoder resolution.
        // turningEncoder.setDistancePerPulse(2 * Math.PI / kEncoderResolution);

        // Limit the PID Controller's input range between -pi and pi and set the input
        // to be continuous.
        // FIXME Changing radians to degrees, replaced PI with 180
        turningPIDController.enableContinuousInput(-180, 180);

        System.out.println("  Constructor Finished: " + fullClassName + " >> " + moduleName);
    }


    // *** CLASS & INSTANCE METHODS ***
    private void configDriveMotor()
    {
        // Set factory defaults
        TalonFXConfiguration talonFXConfigs = new TalonFXConfiguration();
        StatusCode statusCode;
        int count = 0;

        do
        {
            // Reset to factory defaults
            statusCode = driveMotor.getConfigurator().apply(talonFXConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);

        // driveMotor.restoreFactoryDefaults(false);

        // FIXME 1/14
        // driveMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        // driveMotor.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2, 20);
        
        // Invert the motor and Set brake mode
        MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        if(driveMotorInverted)
            motorOutputConfigs.withInverted(InvertedValue.Clockwise_Positive);
        else
            motorOutputConfigs.withInverted(InvertedValue.CounterClockwise_Positive);
        motorOutputConfigs.withNeutralMode(NeutralModeValue.Brake);
        motorOutputConfigs.withDutyCycleNeutralDeadband(0.001);
        talonFXConfigs.withMotorOutput(motorOutputConfigs);
        // driveMotor.setInverted(driveMotorInverted); 
        // driveMotor.setNeutralMode(NeutralModeValue.Brake);
        // driveMotor.setIdleMode(IdleMode.kBrake);

        // Set soft limits
        SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        softLimitSwitchConfigs.withForwardSoftLimitThreshold(0.0);
        softLimitSwitchConfigs.withForwardSoftLimitEnable(false);
        softLimitSwitchConfigs.withReverseSoftLimitThreshold(0.0);
        softLimitSwitchConfigs.withReverseSoftLimitEnable(false);
        talonFXConfigs.withSoftwareLimitSwitch(softLimitSwitchConfigs);
        // driveMotor.setSoftLimit(SoftLimitDirection.kForward, 0.0f);
        // driveMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        // driveMotor.setSoftLimit(SoftLimitDirection.kReverse, 0.0f);
        // driveMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        
        // Set the Feedback sensor and Conversion factor
        // driveMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        feedbackConfigs.withFeedbackSensorSource(FeedbackSensorSourceValue.RotorSensor);
        // feedbackConfigs.withRotorToSensorRatio(??);  // FIXME do we need this?
        feedbackConfigs.withSensorToMechanismRatio(Constants.DrivetrainConstants.DRIVE_ENCODER_POSITION_TO_METERS);
        talonFXConfigs.withFeedback(feedbackConfigs);
        // driveMotorEncoder.setPositionConversionFactor(Constants.DrivetrainConstants.DRIVE_ENCODER_POSITION_TO_METERS);
        // driveMotorEncoder.setVelocityConversionFactor(Constants.DrivetrainConstants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC);
        
        // Set current limits
        // driveMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 20, 25, 1.0));
        // driveMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 10, 15, 0.5));
        // driveMotor.setSmartCurrentLimit(40);
        CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        currentLimitsConfigs.withSupplyCurrentLimit(40.0);
        currentLimitsConfigs.withSupplyCurrentThreshold(60.0);
        currentLimitsConfigs.withSupplyTimeThreshold(0.5);
        talonFXConfigs.withCurrentLimits(currentLimitsConfigs);

        // Set open loop ramp rate
        // driveMotor.configOpenloopRamp(openLoopRamp);
        // driveMotor.setOpenLoopRampRate(openLoopRamp);

        //TODO driveMotor.configNeutralDeadband(0.001);

        // Set voltage compensation
        VoltageConfigs voltageConfigs = new VoltageConfigs();
        voltageConfigs.withPeakForwardVoltage(Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE);
        voltageConfigs.withPeakReverseVoltage(Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE);
        talonFXConfigs.withVoltage(voltageConfigs);

        count = 0;
        do
        {
            // Apply TalonFX configurations
            statusCode = driveMotor.getConfigurator().apply(talonFXConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);

        // driveMotor.enableVoltageCompensation(Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE);
        
       

        // System.out.println("DriveTalon");

        // Setup PID through TalonFX
        // Old values were 3.5, 0, 0.09
        // volts / (m/s) for P
        // volts / (m/s^2) for D
        // kP Converting v / (process unit) to volts
        // Assuming kI is 0
        // kD Converting v / (rate of change of process unit per time base) to volts

        // Old before pdf explanation?
        // final double kPTalonFX = (3.5 * 10) / Constants.DRIVE_ENCODER_RATE_IN_MOTOR_TICKS_PER_100MS;
        // // 10th of a second to 50th of a second or something
        // final double kDTalonFX = (0.09 * (10 * 5)) / Constants.DRIVE_ENCODER_RATE_IN_MOTOR_TICKS_PER_100MS;

        /*
        // Cancelled
        // TODO: Move these constants somewhere else or remove calculations
        final double kPTalonFX = 200 * (3.5 / (1 / Constants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC));
        final double kITalonFX = 0.0;
        // 10th of a second to 50th of a second or something
        final double kDTalonFX = (0.09 * (0.2 / (1 / Constants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC)));

        // Probably the wrong number but is my backup
        // final double kFTalonFX = (1023 * 0.75) / (2.1 / (1 / Constants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC));
        // Was 2.1 originally
        final double kFTalonFX = (3 / (1 / Constants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC));

        // TODO: Finish moving PID to TalonFX
        driveMotor.config_kP(0, kPTalonFX);
        driveMotor.config_kI(0, kITalonFX);
        driveMotor.config_kD(0, kDTalonFX);

        driveMotor.config_kF(0, kFTalonFX);
        */
    }

    private void configTurnMotor()
    {
        // Set factory defaults
        TalonFXConfiguration talonFXConfigs = new TalonFXConfiguration();
        StatusCode statusCode;
        int count = 0;

        do
        {
            // Reset the factory defaults
            statusCode = turnMotor.getConfigurator().apply(talonFXConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);

        // turnMotor.restoreFactoryDefaults(false);

        // Invert the motor and Set brake mode
        MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        motorOutputConfigs.withInverted(InvertedValue.CounterClockwise_Positive);
        motorOutputConfigs.withNeutralMode(NeutralModeValue.Brake);
        motorOutputConfigs.withDutyCycleNeutralDeadband(0.001);
        talonFXConfigs.withMotorOutput(motorOutputConfigs);
        // turnMotor.setInverted(false);
        // turnMotor.setNeutralMode(NeutralModeValue.Brake);
        // turnMotor.setIdleMode(IdleMode.kBrake);

        // Set soft limits
        SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        softLimitSwitchConfigs.withForwardSoftLimitThreshold(0.0);
        softLimitSwitchConfigs.withForwardSoftLimitEnable(false);
        softLimitSwitchConfigs.withReverseSoftLimitThreshold(0.0);
        softLimitSwitchConfigs.withReverseSoftLimitEnable(false);
        talonFXConfigs.withSoftwareLimitSwitch(softLimitSwitchConfigs);
        // turnMotor.setSoftLimit(SoftLimitDirection.kForward, 0.0f);
        // turnMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        // turnMotor.setSoftLimit(SoftLimitDirection.kReverse, 0.0f);
        // turnMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Set the feedback sensor
        // turnMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        feedbackConfigs.withFeedbackSensorSource(FeedbackSensorSourceValue.RotorSensor);
        talonFXConfigs.withFeedback(feedbackConfigs);
        
        // Set current limits
        // turnMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 20, 25, 1.0));
        // turnMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 10, 15, 0.5));
        // turnMotor.setSmartCurrentLimit(40);
        CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        currentLimitsConfigs.withSupplyCurrentLimit(40.0);
        currentLimitsConfigs.withSupplyCurrentThreshold(60.0);
        currentLimitsConfigs.withSupplyTimeThreshold(0.5);
        talonFXConfigs.withCurrentLimits(currentLimitsConfigs);
        
        // Set open loop ramp rate
        // turnMotor.configOpenloopRamp(openLoopRamp);
        // turnMotor.setOpenLoopRampRate(openLoopRamp);
        
        // TODO turnMotor.configNeutralDeadband(0.001);

        // Set voltage compensation
        VoltageConfigs voltageConfigs = new VoltageConfigs();
        voltageConfigs.withPeakForwardVoltage(Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE);
        voltageConfigs.withPeakReverseVoltage(Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE);
        talonFXConfigs.withVoltage(voltageConfigs);

        count = 0;
        do
        {
            // Apply TalonFX configurations
            statusCode = turnMotor.getConfigurator().apply(talonFXConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);

        // turnMotor.enableVoltageCompensation(Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE);

        // TODO Set the conversion factors
        // turnMotorEncoder.setPositionConversionFactor(?);
        // turnMotorEncoder.setVelocityConversionFactor(?);

        // System.out.println("configTurnTalon");
    }

    private void configCANCoder()
    {
        // TODO Add all the config settings
        /**
         * Confgure the CANCoders
         */
        CANcoderConfiguration CANcoderConfigs = new CANcoderConfiguration();
        
        StatusCode statusCode;
        int count = 0;

        do
        {
            // Reset to factory defaults
            statusCode = turnEncoder.getConfigurator().apply(CANcoderConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);
        
        MagnetSensorConfigs magnetSensorConfigs = new MagnetSensorConfigs();
        
        magnetSensorConfigs.withAbsoluteSensorRange(AbsoluteSensorRangeValue.Unsigned_0To1);
        magnetSensorConfigs.withMagnetOffset(turnEncoderOffset);
        magnetSensorConfigs.withSensorDirection(SensorDirectionValue.CounterClockwise_Positive);


        CANcoderConfigs.withMagnetSensor(magnetSensorConfigs);

        count = 0;
        do
        {
            // Reset to factory defaults
            statusCode = turnEncoder.getConfigurator().apply(CANcoderConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);


        // // Configurations all have in common
        // CANcoderConfigs.velocityMeasurementPeriod = SensorVelocityMeasPeriod.Period_100Ms;
        // CANcoderConfigs.velocityMeasurementWindow = 64;
        // CANcoderConfigs.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        // CANcoderConfigs.sensorDirection = false; // CCW
        // CANcoderConfigs.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition; // error on get "On boot up, set position to zero.";
        // // CANCoderConfigs.sensorCoefficient = 0.0015339776873588562; // 4096 ticks to radians
        // CANcoderConfigs.sensorCoefficient = 0.087890625; // 4096 ticks to degrees
        // CANcoderConfigs.unitString = "degrees";
        // CANcoderConfigs.sensorTimeBase = SensorTimeBase.PerSecond;
        // CANcoderConfigs.customParam0 = 0;
        // CANcoderConfigs.customParam1 = 0;

        // // Individual Settings
        // // System.out.println(moduleName);
        // // System.out.println("setStatusFramePeriod " + turnEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, (int)(steerAdjustPeriod*1000.*.8)));
        // CANcoderConfigs.magnetOffsetDegrees = turnEncoderOffset;

        // errorCode = turnEncoder.configAllSettings(CANcoderConfigs);
        // // System.out.println("configAllSettings " + errorCode);
        // // System.out.println(CANCoderConfigs.toString());

        // // When deploy code set the integrated encoder to the absolute encoder on the CANCoder
        // turnEncoder.setPosition(turnEncoder.getAbsolutePosition());
    }

    public void configOpenLoopRamp(double seconds)
    {   
        StatusCode statusCode;
        int count = 0;

        OpenLoopRampsConfigs openLoopRampsConfigs = new OpenLoopRampsConfigs();
        openLoopRampsConfigs.withDutyCycleOpenLoopRampPeriod(seconds);
        
        do
        {
            // Reset the factory defaults
            statusCode = turnMotor.getConfigurator().apply(openLoopRampsConfigs);
            count++;
        }
        while(!statusCode.isOK() && count < 5);
        // driveMotor.configOpenloopRamp(seconds);
        // driveMotor.setOpenLoopRampRate(seconds);
    }

    /**
     * Returns the current state of the module.
     *
     * @return The current state of the module.
     */
    public SwerveModuleState getState()
    {
        // FIXME Changing radians to degrees
        return new SwerveModuleState(getDrivingEncoderRate(), Rotation2d.fromDegrees(getTurningEncoderPosition()));
        // return new SwerveModuleState(getDrivingEncoderRate(), new Rotation2d(getTurningEncoderPosition())); // Using radian line
    }

    /**
     * Sets the desired state for the module.
     *
     * @param desiredState Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState desiredState)
    {
        // Optimize the reference state to avoid spinning further than 90 degrees
        // FIXME Changing radians to degrees
        SwerveModuleState state =
            SwerveModuleState.optimize(desiredState, Rotation2d.fromDegrees(getTurningEncoderPosition()));
            
        // SwerveModuleState state =
        // SwerveModuleState.optimize(desiredState, new Rotation2d(getTurningEncoderPosition()));

        double driveP = SmartDashboard.getNumber("Drive P", 0.0);
        double driveD = SmartDashboard.getNumber("Drive D", 0.0);
        drivePIDController.setP(driveP);
        drivePIDController.setD(driveD);

        // Calculate the drive output from the drive PID controller.
        final double driveOutput =
            drivePIDController.calculate(getDrivingEncoderRate(), state.speedMetersPerSecond);

        final double driveFeedforwardValue = driveFeedforward.calculate(state.speedMetersPerSecond);

        // Calculate the turning motor output from the turning PID controller.

        // double p = SmartDashboard.getNumber("Turn P", 0.0);
        // double d = SmartDashboard.getNumber("Turn D", 0.0);
        // turningPIDController.setP(p);
        // turningPIDController.setD(d);
        // FIXME Changing radians to degrees, changed PID to take degrees
        final double turnOutput = turningPIDController.calculate(getTurningEncoderPosition(), state.angle.getDegrees());

        // final double turnOutput =
            // turningPIDController.calculate(Math.toRadians(getTurningEncoderPosition()), state.angle.getRadians());

        // FIXME Changing radians to degrees, Changed volts per radian to volts per degree for kS, kV, kA
        final double turnFeedforwardValue =
            turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);

        //FIXME Convert to Talon FX
        var normalizedDriveVoltage = normalizeVoltage(driveOutput + driveFeedforwardValue);
        var normalizedTurnVoltage = normalizeVoltage(turnOutput + turnFeedforwardValue);

        // TODO Is this the same as output
        // driveMotor.set(ControlMode.PercentOutput, normalizedDriveVoltage);
        driveMotor.set(normalizedDriveVoltage);

        // Used for running PIDF on TalonFX
        // var ticksPer100MS = state.speedMetersPerSecond * (1 / Constants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC);
        // System.out.println("M/S: " + state.speedMetersPerSecond + ", Ticks/100MS: " + ticksPer100MS);
        // driveMotor.set(ControlMode.Velocity, ticksPer100MS);
        // getDrivingEncoderRate();

        // turnMotor.set(ControlMode.PercentOutput, normalizedTurnVoltage);
        turnMotor.set(normalizedTurnVoltage);


        // FIXME Changing radians to degrees
        // SmartDashboard.putNumber(moduleName + " Optimized Angle Radians", state.angle.getRadians());
        // SmartDashboard.putNumber(moduleName + " Optimized Angle Degrees", state.angle.getDegrees());
        // SmartDashboard.putNumber(moduleName + " Optimized Speed", state.speedMetersPerSecond);
        // SmartDashboard.putNumber(moduleName + " Turn Output", turnOutput);
        // SmartDashboard.putNumber(moduleName + " Turn Feedforward", turnFeedforwardValue);
        // SmartDashboard.putNumber(moduleName + " Normalized Turn Percent", normalizedTurnVoltage);
        // SmartDashboard.putNumber(moduleName + " Drive Output", driveOutput);
        // SmartDashboard.putNumber(moduleName + " Drive Feedforward", driveFeedforwardValue);
        // SmartDashboard.putNumber(moduleName + " Normalized Drive Percent", normalizedDriveVoltage);
        // SmartDashboard.putNumber(moduleName + " Drive Encoder Rate", getDrivingEncoderRate());
    }

    public double getDrivingEncoderRate()
    {
        // FIXME Changing drivePID, changed nothing yet and probably do not need to because this will still return ticks / 100 ms
       
        // double velocity = driveMotor.getSelectedSensorVelocity() * Constants.DrivetrainConstants.DRIVE_ENCODER_RATE_TO_METERS_PER_SEC;
        double velocity = driveMotor.getVelocity().getValueAsDouble();
        // double velocity = driveMotorEncoder.getVelocity();
        
        // FIXME Units conversion?
        // System.out.println(driveMotor.getDeviceID() + " " + velocity);
        return velocity;
    }

    public double getDrivingEncoderPosition()
    {
        // double position = driveMotor.getSelectedSensorPosition() * Constants.DrivetrainConstants.DRIVE_ENCODER_POSITION_TO_METERS;
        double position = driveMotor.getPosition().getValueAsDouble();
        // double position = driveMotorEncoder.getPosition();
        
        return position;
    }

    public double getTurningEncoderPosition()
    {
        // FIXME Changing radians to degrees included making encoder return degrees
        // Used the Phoenix tuner to change the return value to radians
        return turnEncoder.getAbsolutePosition().getValueAsDouble() * 360.0;
        // Reset factory default in Phoenix Tuner X to make the 0 go forward 
        // while wheel bolts facing in, then save, then get absolute value and put in enum
        // return turningEncoder.getAbsolutePosition() - turningEncoderOffset; 
    }

    /**
     * Normalizes voltage from -1 to 1 using current battery voltage
     * 
     * @param outputVolts
     * @return normalizedVoltage
     */
    public static double normalizeVoltage(double outputVolts)
    {
        return MathUtil.clamp(outputVolts / Constants.DrivetrainConstants.MAX_BATTERY_VOLTAGE, -1.0, 1.0); //RobotController.getBatteryVoltage();
    }

    public void resetEncoders()
    {
        driveMotor.setPosition(0.0);
        // driveMotorEncoder.setPosition(0.0);

        turnMotor.setPosition(0.0);
        // turnMotorEncoder.setPosition(0.0);
        
        turnEncoder.setPosition(turnEncoder.getAbsolutePosition().getValueAsDouble());
    }

    public double getDriveMotorPosition()
    {
        return driveMotor.getPosition().getValueAsDouble();
        // return driveMotorEncoder.getPosition();
    }

    public void stopModule()
    {
        // driveMotor.set(ControlMode.PercentOutput, 0.0);
        driveMotor.set(0.0);

        // turnMotor.set(ControlMode.PercentOutput, 0.0);
        turnMotor.set(0.0);
    }


    /**
   * Returns the current position of the module.
   *
   * @return The current position of the module.
   */
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        getDrivingEncoderPosition(), Rotation2d.fromDegrees(getTurningEncoderPosition()));
  }

  @Override
  public String getDescription()
  {
      return "Swerve " + moduleName;
  }

  @Override
  public void stopMotor()
  {
    stopModule();
  }
    
}
