package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.commands.SwerveDrive;


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


public class OperatorButtonBindings 
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
   

    // *** CLASS CONSTRUCTOR ***
    public OperatorButtonBindings(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        
        if(robotContainer.driverController != null)
        {
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
            configDpad();
            configRumble();
            configDefaultCommands();
        }
    }
    
    private void configAButton()
    {
        // A Button
        BooleanSupplier aButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kA);
        Trigger aButtonTrigger = new Trigger(aButton);

        if(robotContainer.drivetrain != null)
        {}
    }

    private void configBButton()
    {
        // B Button
        BooleanSupplier bButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kB);
        Trigger bButtonTrigger = new Trigger(bButton);

        if(true)
        {}
    }

    private void configXButton()
    {
        // X Button
        BooleanSupplier xButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kX);
        Trigger xButtonTrigger = new Trigger(xButton);

        if(true)
        {}
    }

    private void configYButton()
    {
        // Y Button
        BooleanSupplier yButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kY);
        Trigger yButtonTrigger = new Trigger(yButton);

        if(true)
        {}
    }

    private void configLeftBumper()
    {
        //Left Bumper
        BooleanSupplier leftBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftBumper);
        Trigger leftBumperTrigger = new Trigger(leftBumper);

        if(true)
        {}
    }

    private void configRightBumper()
    {
        // Right Bumper
        BooleanSupplier rightBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightBumper);
        Trigger rightBumperTrigger = new Trigger(rightBumper);

        if(true)
        {}
    }

    private void configBackButton()
    {
        // Back Button
        BooleanSupplier backButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kBack);
        Trigger backButtonTrigger = new Trigger(backButton);

        if(true)
        {}
    }

    private void configStartButton()
    {
        // Start Button
        BooleanSupplier startButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kStart);
        Trigger startButtonTrigger = new Trigger(startButton);

        if(true)
        {}
    }

    private void configLeftStick()
    {
        // Left Stick Button
        BooleanSupplier leftStickButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftStick);
        Trigger leftStickButtonTrigger = new Trigger(leftStickButton);

        if(true)
        {}
    }

    private void configRightStick()
    {
        // Left Stick Button
        BooleanSupplier rightStickButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightStick);
        Trigger rightStickButtonTrigger = new Trigger(rightStickButton);

        if(true)
        {}
    }

    private void configLeftTrigger()
    {
        //Left trigger 
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        if(true)
        {}
    }

    private void configRightTrigger()
    {
        //Right trigger 
        BooleanSupplier rightTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
        Trigger rightTriggerTrigger = new Trigger(rightTrigger);

        if(true)
        {}
    }

    private void configDpad()
    {
        // Dpad down button
        BooleanSupplier dPadDown = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kDown);
        Trigger dPadDownTrigger = new Trigger(dPadDown);

        if(true)
        {}
    }

    private void configRumble()
    {
        // Rumble
    }

    private void configDefaultCommands()
    {
        // Default Commands

    }
}
