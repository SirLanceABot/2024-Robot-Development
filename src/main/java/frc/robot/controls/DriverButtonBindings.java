package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.commands.Commands4237;
import frc.robot.subsystems.Drivetrain.LockTarget;


// ------------------------------------------------------------------------------------------
// COMMAND EXAMPLES
// ------------------------------------------------------------------------------------------
// 
// Here are other options ways to create "Suppliers"
// DoubleSupplier leftYAxis =  () -> { return driverController.getRawAxis(Xbox.Axis.kLeftY) * 2.0; };
// DoubleSupplier leftXAxis =  () -> { return driverController.getRawAxis(Xbox.Axis.kLeftX) * 2.0; };
// DoubleSupplier rightXAxis = () -> { return driverController.getRawAxis(Xbox.Axis.kRightX) * 2.0; };
// BooleanSupplier aButton =   () -> { return driverController.getRawButton(Xbox.Button.kA); };
//
// ------------------------------------------------------------------------------------------
//
// Here are 4 ways to perform the "LockWheels" command
// Press the X button to lock the wheels, unlock when the driver moves left or right axis
// 
// Option 1
// xButtonTrigger.onTrue( new RunCommand( () -> drivetrain.lockWheels(), drivetrain )
//						.until(driverController.tryingToMoveRobot()) );
//
// Option 2
// xButtonTrigger.onTrue(new LockWheels(drivetrain)
// 						.until(driverController.tryingToMoveRobot()));
//
// Option 3
// xButtonTrigger.onTrue(new FunctionalCommand(
// 		() -> {}, 								// onInit
// 		() -> { drivetrain.lockWheels(); }, 	// onExec
// 		(interrupted) -> {}, 					// onEnd
// 		driverController.tryingToMoveRobot(),	// isFinished
// 		drivetrain ) );							// requirements
// 
// Option 4
// xButtonTrigger.onTrue( run( () -> drivetrain.lockWheels() )	//run(drivetrain::lockWheels)
// 						.until(driverController.tryingToMoveRobot()) );
//


public class DriverButtonBindings 
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
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    private final double CRAWL_SPEED = 0.4;
    private final double WALK_SPEED = 0.65;
    private final double RUN_SPEED = 1.0;

    private double scaleFactor = 0.5;
    private DoubleSupplier leftYAxis;
    private DoubleSupplier leftXAxis;
    private DoubleSupplier rightXAxis;
    private DoubleSupplier scaleFactorSupplier;

    // *** CLASS CONSTRUCTOR ***
    public DriverButtonBindings(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        
        if(robotContainer.driverController != null)
        {
            scaleFactorSupplier = () -> scaleFactor;
            leftYAxis = robotContainer.driverController.getAxisSupplier(Xbox.Axis.kLeftY, scaleFactorSupplier);
            leftXAxis = robotContainer.driverController.getAxisSupplier(Xbox.Axis.kLeftX, scaleFactorSupplier);
            rightXAxis = robotContainer.driverController.getAxisSupplier(Xbox.Axis.kRightX);
            
            configAButton();
            configBButton();
            configXButton();
            configYButton();
            configLeftBumper();
            configRightBumper();
            configBackButton();
            configStartButton();
            configLeftTrigger();
            configRightTrigger();
            configLeftStick();
            configRightStick();
            configDpadUp();
            configDpadDown();
            configRumble();
            configDefaultCommands();
        }

        System.out.println("  Constructor Finished: " + fullClassName);
    }
    
    private void configAButton()
    {
        // A Button
        BooleanSupplier aButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kA);
        Trigger aButtonTrigger = new Trigger(aButton);

        aButtonTrigger.whileTrue(robotContainer.drivetrain.lockRotationToAmpZoneCommand(leftYAxis, leftXAxis, scaleFactorSupplier));

        // aButtonTrigger.onTrue(Commands4237.driveToAmpCommand());

        // Shooting after flywheel up to speed
        // aButtonTrigger.onTrue(Commands4237.shootCommand( () -> 0.0));
    }

    private void configBButton()
    {
        // B Button
        BooleanSupplier bButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kB);
        Trigger bButtonTrigger = new Trigger(bButton);

        // bButtonTrigger.toggleOnTrue(Commands4237.intakeFromFloorBack());
        // bButtonTrigger.whileTrue(robotContainer.drivetrain.lockRotationToSourceCommand(leftYAxis, leftXAxis, scaleFactorSupplier));
         bButtonTrigger.whileTrue(robotContainer.drivetrain.lockRotationToPositionCommand(leftYAxis, leftXAxis, scaleFactorSupplier, LockTarget.kRightChain));

        // Picking up from the front
        // if(bButton.getAsBoolean())
        // {
        //     System.out.println("b button pressed");
        //     Commands4237.intakeFromFloorFront();
        // }
    }

    private void configXButton()
    {
        // X Button
        BooleanSupplier xButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kX);
        Trigger xButtonTrigger = new Trigger(xButton);

        // Picking up from the back
        if(robotContainer.drivetrain != null)
        {
            // xButtonTrigger.onTrue(Commands.runOnce( () -> robotContainer.drivetrain.lockWheels()));
             xButtonTrigger.whileTrue(robotContainer.drivetrain.lockRotationToPositionCommand(leftYAxis, leftXAxis, scaleFactorSupplier, LockTarget.kLeftChain));
        }
    }

    private void configYButton()
    {
        // Y Button
        BooleanSupplier yButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kY);
        Trigger yButtonTrigger = new Trigger(yButton);

        // yButtonTrigger.onTrue(Commands4237.resetGyroCommand());
        if(robotContainer.drivetrain != null)
        {
        //    yButtonTrigger.whileTrue(robotContainer.drivetrain.lockRotationToAmpZoneCommand(leftYAxis, leftXAxis, scaleFactorSupplier));
             yButtonTrigger.whileTrue(robotContainer.drivetrain.lockRotationToPositionCommand(leftYAxis, leftXAxis, scaleFactorSupplier, LockTarget.kBackChain));
        }
    }

    private void configLeftBumper()
    {
        //Left Bumper
        BooleanSupplier leftBumper = robotContainer.driverController.getButtonSupplier(Xbox.Button.kLeftBumper);
        Trigger leftBumperTrigger = new Trigger(leftBumper);

        if(robotContainer.drivetrain != null)
        {
            leftBumperTrigger.onTrue(Commands.runOnce(() -> scaleFactor = (scaleFactor > (CRAWL_SPEED + WALK_SPEED) / 2.0) ? CRAWL_SPEED : RUN_SPEED));
        }

        
    }

    private void configRightBumper()
    {
        // Right Bumper
        BooleanSupplier rightBumper = robotContainer.driverController.getButtonSupplier(Xbox.Button.kRightBumper);
        Trigger rightBumperTrigger = new Trigger(rightBumper);

        if(true)
        {
            rightBumperTrigger.toggleOnTrue(Commands4237.intakeFromFloorFront());
            // rightBumperTrigger.onFalse(Commands.runOnce(() -> scaleFactor = 1.0));
        }
    }

    private void configBackButton()
    {
        // Back Button
        BooleanSupplier backButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kBack);
        Trigger backButtonTrigger = new Trigger(backButton);

        // backButtonTrigger.toggleOnTrue(Commands4237.intakeFromFloorBack());
        if(robotContainer.drivetrain != null)
        {
            backButtonTrigger.onTrue(robotContainer.drivetrain.resetSwerveConfigCommand()).debounce(0.25);
        }
    }

    private void configStartButton()
    {
        // Start Button
        BooleanSupplier startButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kStart);
        Trigger startButtonTrigger = new Trigger(startButton);

        // if(robotContainer.pivot != null)
        // {
        //     startButtonTrigger.onTrue(robotContainer.pivot.resetAngleCommand());
        // }

        if(true)
        {
            startButtonTrigger.onTrue(Commands4237.resetGyroCommand()).debounce(0.25);
        }
    }

    private void configLeftStick()
    {
        // Left Stick Button
        BooleanSupplier leftStickButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kLeftStick);
        Trigger leftStickButtonTrigger = new Trigger(leftStickButton);

        if(true)
        {}
    }

    private void configRightStick()
    {
        // Left Stick Button
        BooleanSupplier rightStickButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kRightStick);
        Trigger rightStickButtonTrigger = new Trigger(rightStickButton);

        if(true)
        {}
    }

    private void configLeftTrigger()
    {
        //Left trigger 
        BooleanSupplier leftTrigger = robotContainer.driverController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        leftTriggerTrigger.onTrue(Commands.runOnce(() -> scaleFactor = (scaleFactor > (WALK_SPEED + RUN_SPEED) / 2.0) ? WALK_SPEED : RUN_SPEED));
    }

    private void configRightTrigger()
    {
        //Right trigger 
        BooleanSupplier rightTrigger = robotContainer.driverController.getButtonSupplier(Xbox.Button.kRightTrigger);
        Trigger rightTriggerTrigger = new Trigger(rightTrigger);

        rightTriggerTrigger.toggleOnTrue(Commands4237.intakeFromSource());
    }

    private void configDpadUp()
    {
        // Dpad down button
        BooleanSupplier dPadUp = robotContainer.driverController.getDpadSupplier(Xbox.Dpad.kUp);
        Trigger dPadUpTrigger = new Trigger(dPadUp);

        // dPadUpTrigger.onTrue(Commands4237.rotateToSpeakerCommand());
        dPadUpTrigger.whileTrue(robotContainer.drivetrain.lockRotationToSourceCommand(leftYAxis, leftXAxis, scaleFactorSupplier));

    }

    private void configDpadDown()
    {
        BooleanSupplier dPadDown = robotContainer.driverController.getDpadSupplier(Xbox.Dpad.kDown);
        Trigger dPadDownTrigger = new Trigger(dPadDown);

        // dPadDownTrigger.onTrue(Commands4237.shootFromAnywhereCommand());
    }

    private void configRumble()
    {
        // Rumble
    }

    private void configDefaultCommands()
    {
        // Axis, driving and rotating
        // DoubleSupplier leftYAxis = robotContainer.driverController.getAxisSupplier(Xbox.Axis.kLeftY);
        // DoubleSupplier leftXAxis = robotContainer.driverController.getAxisSupplier(Xbox.Axis.kLeftX);
        // DoubleSupplier rightXAxis = robotContainer.driverController.getAxisSupplier(Xbox.Axis.kRightX);
        
        // Default Commands
        if(robotContainer.drivetrain != null)
        {
            robotContainer.drivetrain.setDefaultCommand(robotContainer.drivetrain.driveCommand(leftYAxis, leftXAxis, rightXAxis, scaleFactorSupplier));
            // robotContainer.drivetrain.setDefaultCommand(new SwerveDrive(robotContainer.drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
        }
    }
}
