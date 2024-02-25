package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.commands.Commands4237;


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

        // Shooting after flywheel up to speed
        // aButtonTrigger.onTrue(Commands4237.shootCommand( () -> 0.0));
        aButtonTrigger.onTrue(Commands4237.samsEpicShootCommand());
    }

    private void configBButton()
    {
        // B Button
        BooleanSupplier bButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kB);
        Trigger bButtonTrigger = new Trigger(bButton);

        // Picking up from the front
        bButtonTrigger.onTrue(Commands4237.intakeFromFloorFront());
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
            xButtonTrigger.onTrue(Commands.runOnce( () -> robotContainer.drivetrain.lockWheels()));
        }
    }

    private void configYButton()
    {
        // Y Button
        BooleanSupplier yButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kY);
        Trigger yButtonTrigger = new Trigger(yButton);

        yButtonTrigger.onTrue(Commands4237.intakeFromFloorBack());
    }

    private void configLeftBumper()
    {
        //Left Bumper
        BooleanSupplier leftBumper = robotContainer.driverController.getButtonSupplier(Xbox.Button.kLeftBumper);
        Trigger leftBumperTrigger = new Trigger(leftBumper);

        if(true)
        {
            leftBumperTrigger.onTrue(Commands4237.shootFromSubWooferCommand());
        }
    }

    private void configRightBumper()
    {
        // Right Bumper
        BooleanSupplier rightBumper = robotContainer.driverController.getButtonSupplier(Xbox.Button.kRightBumper);
        Trigger rightBumperTrigger = new Trigger(rightBumper);

        if(robotContainer.drivetrain != null)
        {
            rightBumperTrigger.toggleOnTrue(Commands.runOnce(() -> scaleFactor = scaleFactor < 1.0 ? 1.0 : 0.5));
            // rightBumperTrigger.onFalse(Commands.runOnce(() -> scaleFactor = 1.0));
        }
    }

    private void configBackButton()
    {
        // Back Button
        BooleanSupplier backButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kBack);
        Trigger backButtonTrigger = new Trigger(backButton);

        if(true)
        {}
    }

    private void configStartButton()
    {
        // Start Button
        BooleanSupplier startButton = robotContainer.driverController.getButtonSupplier(Xbox.Button.kStart);
        Trigger startButtonTrigger = new Trigger(startButton);

        if(true)
        {}
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

        if(true)
        {
            // leftTriggerTrigger.onTrue(robotContainer.pivot.moveUp());
        }
    }

    private void configRightTrigger()
    {
        //Right trigger 
        BooleanSupplier rightTrigger = robotContainer.driverController.getButtonSupplier(Xbox.Button.kRightTrigger);
        Trigger rightTriggerTrigger = new Trigger(rightTrigger);

        if(true)
        {}
    }

    private void configDpadUp()
    {
        // Dpad down button
        BooleanSupplier dPadUp = robotContainer.driverController.getDpadSupplier(Xbox.Dpad.kUp);
        Trigger dPadUpTrigger = new Trigger(dPadUp);

        if(robotContainer.intakePositioning != null)
        {
            dPadUpTrigger.onTrue(robotContainer.intakePositioning.moveUpCommand());
        }
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
