// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.button.Trigger;
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
import frc.robot.subsystems.PoseEstimator;
import frc.robot.subsystems.Shooter;
// import frc.robot.subsystems.Shooter;
import frc.robot.controls.DriverButtonBindings;
import frc.robot.controls.DriverController;
import frc.robot.controls.OperatorButtonBindings;
import frc.robot.controls.OperatorController;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Proximity;
import frc.robot.sensors.Ultrasonic;
import frc.robot.shuffleboard.MainShuffleboard;
import frc.robot.controls.Xbox;

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
    
    private boolean useFullRobot			= false;

    private boolean useExampleSubsystem		= false;
    private boolean useGyro					= true;
    private boolean useDrivetrain   		= true;
    private boolean useFlywheel             = false;
    private boolean useIntake               = false;
    private boolean useAmpAssist            = false; 
    private boolean usePivot                = false;
    private boolean useIntakePositioning    = false;
    private boolean useShuttle              = false;
    private boolean useClimb                = false;
    private boolean useIndex                = false;
    private boolean useUltrasonic           = false;
    private boolean useShooter              = false;
    private boolean useCandle               = false;
    private boolean useProximity            = false;
    
    private boolean useCameraOne            = false;
    private boolean useCameraTwo            = false;
    private boolean useCameraThree          = false;
    private boolean useCameraFour           = false;
    private boolean usePoseEstimator        = false;

    private boolean useMainShuffleboard		= false;    
    private boolean useDriverController		= true;
    private boolean useOperatorController 	= false;
    private boolean useBindings				= true;

    private boolean useDataLog				= false;


    public final boolean fullRobot;

    public final ExampleSubsystem exampleSubsystem;
    public final Gyro4237 gyro;
    public final Drivetrain drivetrain;
    public final Compressor compressor;
    public final Flywheel flywheel;
    public final Intake intake;
    public final AmpAssist ampAssist;
    public final Pivot pivot;
    public final IntakePositioning intakePositioning;
    public final Shuttle shuttle;
    public final Climb climb;
    public final Index index;
    public final Ultrasonic ultrasonic;
    public final Shooter shooter;
    public final Candle4237 candle;
    public final Proximity firstShuttleProximity;
    public final Proximity secondShuttleProximity;
    public final Proximity indexProximity;

    public final Camera[] cameraArray = new Camera[4];

    public final MainShuffleboard mainShuffleboard;
    public final DriverController driverController;
    public final OperatorController operatorController;
    public final DriverButtonBindings driverButtonBindings;
    public final OperatorButtonBindings operatorButtonBindings;

    public DataLog log = null;
    public SchedulerLog schedulerLog;


    /** 
     * The container for the robot. Contains subsystems, OI devices, and commands.
     * Use the default modifier so that new objects can only be constructed in the same package.
     */
    RobotContainer()
    {
        // Create the needed subsystems
        fullRobot 			    = (useFullRobot);

        exampleSubsystem 	    = (useExampleSubsystem)						? new ExampleSubsystem() 							    	: null;
        gyro 				    = (useFullRobot || useGyro)					? new Gyro4237()									    	: null;	
        drivetrain 			    = (useFullRobot || useDrivetrain) 			? new Drivetrain(gyro, log, cameraArray, usePoseEstimator)  : null;
        compressor			    = (true)                                    ? new Compressor(0, PneumaticsModuleType.CTREPCM)    : null;
        intake                  = (useIntake)                               ? new Intake()                                              : null;
        ampAssist               = (useAmpAssist)                            ? new AmpAssist()                                           : null;
        pivot                   = (usePivot)                                ? new Pivot()                                               : null;
        intakePositioning       = (useIntakePositioning)                    ? new IntakePositioning()                                   : null;
        shuttle                 = (useShuttle)                              ? new Shuttle()                                             : null;
        climb                   = (useClimb)                                ? new Climb()                                               : null;
        index                   = (useIndex)                                ? new Index()                                               : null;
        ultrasonic              = (useUltrasonic)                           ? new Ultrasonic()                                          : null;
        flywheel                = (useFullRobot || useFlywheel)             ? new Flywheel()                                            : null;
        shooter                 = (useFullRobot || useShooter)              ? new Shooter(pivot, index, flywheel)                       : null;
        candle                  = (useFullRobot || useCandle)               ? new Candle4237()                                          : null;
        firstShuttleProximity   = (useFullRobot || useProximity)            ? new Proximity(9)                         : null;
        secondShuttleProximity  = (useFullRobot || useProximity)            ? new Proximity(8)                         : null;
        indexProximity          = (useFullRobot || useProximity)            ? new Proximity(7)                         : null;

        cameraArray[0]          = (useFullRobot || useCameraOne)            ? new Camera("limelight-one")                       : null;
        cameraArray[1]          = (useFullRobot || useCameraTwo)            ? new Camera("limelight-two")                       : null;
        cameraArray[2]          = (useFullRobot || useCameraThree)          ? new Camera("limelight-three")                     : null;
        cameraArray[3]          = (useFullRobot || useCameraFour)           ? new Camera("limelight-four")                      : null;

        

        mainShuffleboard 	    = (useFullRobot || useMainShuffleboard)		? new MainShuffleboard(this)						    	: null;
        driverController 	    = (useFullRobot || useDriverController) 	? new DriverController(Constants.Controller.DRIVER)     	: null;
        driverButtonBindings	= (useFullRobot || useBindings) 			? new DriverButtonBindings(this) 					    	: null;

        operatorController 	    = (useFullRobot || useOperatorController)   ? new OperatorController(Constants.Controller.OPERATOR) 	: null;
        operatorButtonBindings	= (useFullRobot || useBindings) 			? new OperatorButtonBindings(this) 	    					: null;


        if(useFullRobot || useDataLog)
        {
            DataLogManager.start();
            log = DataLogManager.getLog();
            schedulerLog = new SchedulerLog();
        }
        else
            schedulerLog = null;

        // Configure the trigger bindings
        // if(useFullRobot || useBindings)
        //     configureBindings();
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
}

