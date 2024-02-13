// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.AmpAssist;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Pivot;
import frc.robot.commands.Commands4237;
import frc.robot.controls.DriverButtonBindings;
import frc.robot.controls.DriverController;
import frc.robot.controls.OperatorButtonBindings;
import frc.robot.controls.OperatorController;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Proximity;
import frc.robot.shuffleboard.MainShuffleboard;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private boolean useFullRobot            = false;

    private boolean useAmpAssist            = false;
    private boolean useCandle               = false;
    private boolean useClimb                = false;
    private boolean useDrivetrain           = false;
    private boolean useFlywheel             = false;
    private boolean useIndex                = false;
    private boolean useIntake               = false;
    private boolean useIntakePositioning    = false;
    private boolean usePivot                = false;
    private boolean usePoseEstimator        = false;
    private boolean useShuttle              = false;

    private boolean useCameraOne            = false;
    private boolean useCameraTwo            = false;
    private boolean useCameraThree          = false;
    private boolean useCameraFour           = false;
    private boolean useGyro                 = false;
    private boolean useAllProximity         = false;
    private boolean useCompressor           = false;
    private boolean usePneumaticHub         = false;
    private boolean useExampleSubsystem     = false;

    private boolean useMainShuffleboard     = false;

    private boolean useBindings             = false;
    private boolean useDriverController     = false;
    private boolean useOperatorController   = false;


    public final boolean fullRobot;

    public final AmpAssist ampAssist;
    public final Candle4237 candle;
    public final Climb climb;
    public final Drivetrain drivetrain;
    public final Flywheel flywheel;
    public final Index index;
    public final Intake intake;
    public final IntakePositioning intakePositioning;
    public final Pivot pivot;
    public final Shuttle shuttle;
    public final Compressor compressor;
    public final PneumaticHub pneumaticHub;

    public final Camera[] cameraArray = new Camera[4];
    public final Gyro4237 gyro;
    public final Proximity firstShuttleProximity;
    public final Proximity secondShuttleProximity;
    public final Proximity indexProximity;
    public final Proximity indexWheelsProximity;
    public final ExampleSubsystem exampleSubsystem;

    public final MainShuffleboard mainShuffleboard;

    public final DriverButtonBindings driverButtonBindings;
    public final DriverController driverController;
    public final OperatorButtonBindings operatorButtonBindings;
    public final OperatorController operatorController;
    
    private CommandSchedulerLog schedulerLog = null;


    /** 
     * The container for the robot. Contains subsystems, OI devices, and commands.
     * Use the default modifier so that new objects can only be constructed in the same package.
     */
    RobotContainer()
    {
        // Create the needed subsystems
        fullRobot               = (useFullRobot);

        gyro                    = (useFullRobot || useGyro)                 ? new Gyro4237()                                            : null;	
        drivetrain              = (useFullRobot || useDrivetrain)           ? new Drivetrain(gyro, cameraArray, usePoseEstimator)       : null;

        ampAssist               = (useFullRobot || useAmpAssist)            ? new AmpAssist()                                           : null;
        candle                  = (useFullRobot || useCandle)               ? new Candle4237()                                          : null;
        climb                   = (useFullRobot || useClimb)                ? new Climb()                                               : null;
        flywheel                = (useFullRobot || useFlywheel)             ? new Flywheel()                                            : null;
        index                   = (useFullRobot || useIndex)                ? new Index()                                               : null;
        intake                  = (useFullRobot || useIntake)               ? new Intake()                                              : null;
        intakePositioning       = (useFullRobot || useIntakePositioning)    ? new IntakePositioning()                                   : null;
        pivot                   = (useFullRobot || usePivot)                ? new Pivot()                                               : null;
        shuttle                 = (useFullRobot || useShuttle)              ? new Shuttle()                                             : null;
        
        cameraArray[0]          = (useFullRobot || useCameraOne)            ? new Camera("limelight-one")                               : null;
        cameraArray[1]          = (useFullRobot || useCameraTwo)            ? new Camera("limelight-two")                               : null;
        cameraArray[2]          = (useFullRobot || useCameraThree)          ? new Camera("limelight-three")                             : null;
        cameraArray[3]          = (useFullRobot || useCameraFour)           ? new Camera("limelight-four")                              : null;
        firstShuttleProximity   = (useFullRobot || useAllProximity)         ? new Proximity(Constants.Proximity.FIRST_SHUTTLE_PORT)     : null;
        secondShuttleProximity  = (useFullRobot || useAllProximity)         ? new Proximity(Constants.Proximity.SECOND_SHUTTLE_PORT)    : null;
        indexProximity          = (useFullRobot || useAllProximity)         ? new Proximity(Constants.Proximity.MIDDLE_INDEX_PORT)      : null;
        indexWheelsProximity    = (useFullRobot || useAllProximity)         ? new Proximity(Constants.Proximity.INDEX_WHEELS_PORT)      : null;
        compressor              = (useFullRobot || useCompressor)           ? new Compressor(PneumaticsModuleType.REVPH)                : null;
        pneumaticHub            = (useFullRobot || usePneumaticHub)         ? new PneumaticHub(1)                                       : null;

        exampleSubsystem        = (useExampleSubsystem)                     ? new ExampleSubsystem()                                    : null;

        // DO NOT MOVE THIS STATEMENT
        // This statement must be after the subsystems have been instantiated
        //   and must be before the button bindings.
        Commands4237.setRobotContainer(this);

        mainShuffleboard        = (useFullRobot || useMainShuffleboard)     ? new MainShuffleboard(this)                                : null;

        driverController        = (useFullRobot || useDriverController)     ? new DriverController(Constants.Controller.DRIVER)         : null;
        driverButtonBindings    = (useFullRobot || useBindings)             ? new DriverButtonBindings(this)                            : null;
        operatorController      = (useFullRobot || useOperatorController)   ? new OperatorController(Constants.Controller.OPERATOR)     : null;
        operatorButtonBindings  = (useFullRobot || useBindings)             ? new OperatorButtonBindings(this)                          : null;

        configLog();
        registerNamedCommands();
    }

    public void configLog()
    {
        boolean useConsole = false;
        boolean useDataLog = true;
        boolean useShuffleBoardLog = false;

        schedulerLog = new CommandSchedulerLog(useConsole, useDataLog, useShuffleBoardLog);
        schedulerLog.logCommandInitialize();
        schedulerLog.logCommandInterrupt();
        schedulerLog.logCommandFinish();
        schedulerLog.logCommandExecute();  // Generates a lot of output
    }

    public void configCompressor()
    {
        // compressor.enableAnalog(60.0, 90.0);
        pneumaticHub.enableCompressorAnalog(60.0, 90.0);
    }

    public void resetRobot()
    {
        if(gyro != null)
        {
            gyro.reset();
        }
    }

    public void stopRobot()
    {
        if(drivetrain != null)
        {
            drivetrain.stopMotors();
        }
    }

    public void registerNamedCommands()
    {
        NamedCommands.registerCommand("intake", Commands.print("IntakeCommand"));
        NamedCommands.registerCommand("transfer", Commands.print("TransferCommand"));
        NamedCommands.registerCommand("shoot", Commands.print("ShootCommand"));
    }
}

