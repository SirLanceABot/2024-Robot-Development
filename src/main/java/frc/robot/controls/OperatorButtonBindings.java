package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.Constants.ShootingPosition;
import frc.robot.subsystems.AmpAssist.AmpAssistPosition;
import frc.robot.subsystems.Candle4237.LEDColor;
import frc.robot.subsystems.Climb.TargetPosition;
import frc.robot.commands.Commands4237;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


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
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        
        if(robotContainer.operatorController != null)
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
            configDpadDown();
            configDpadUp();
            configDpadLeft();
            configDpadRight();
            configLeftTriggerAndDpadDown();
            configLeftTriggerAndDpadUp();
            configLeftTriggerAndXButton();
            configLeftTriggerAndYButton();
            configRumble();
            configDefaultCommands();
        }

        System.out.println("  Constructor Finished: " + fullClassName);
    }
    
    private void configAButton()
    {
        // A Button
        BooleanSupplier aButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kA);
        Trigger aButtonTrigger = new Trigger(aButton);

        aButtonTrigger.onTrue(Commands4237.shootFromAnywhereCommand());
    }

    private void configBButton()
    {
        // B Button
        BooleanSupplier bButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kB);
        Trigger bButtonTrigger = new Trigger(bButton);

        // if(robotContainer.intakePositioning != null)
        // {
        //     bButtonTrigger.onTrue(robotContainer.intakePositioning.moveUpCommand());
        // }

        bButtonTrigger.onTrue(Commands4237.passToAmpZoneCommand());
    }

    private void configXButton()
    {
        // X Button
        BooleanSupplier xButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kX);
        Trigger xButtonTrigger = new Trigger(xButton);

        // xButtonTrigger.onTrue(Commands4237.ejectNote());
        xButtonTrigger.onTrue(Commands4237.getFlywheelToSpeedCommand(55.0));
    }

    private void configYButton()
    {
        // Y Button
        BooleanSupplier yButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kY);
        Trigger yButtonTrigger = new Trigger(yButton);

        yButtonTrigger.onTrue(Commands4237.shootFromSubWooferCommand());
    }

    private void configLeftBumper()
    {
        //Left Bumper
        BooleanSupplier leftBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftBumper);
        Trigger leftBumperTrigger = new Trigger(leftBumper);

        leftBumperTrigger.onTrue(Commands4237.moveAmpAssistCommand());
    }

    private void configRightBumper()
    {
        // Right Bumper
        BooleanSupplier rightBumper = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightBumper);
        Trigger rightBumperTrigger = new Trigger(rightBumper);

        rightBumperTrigger.onTrue(Commands4237.shootToAmpCommand());
    }

    private void configBackButton()
    {
        // Back Button
        BooleanSupplier backButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kBack);
        Trigger backButtonTrigger = new Trigger(backButton);

        backButtonTrigger.onTrue(Commands4237.resetPivotToCANCoderAngleCommand());

        // backButtonTrigger.onTrue(Commands4237.ejectNote());
    }

    private void configStartButton()
    {
        // Start Button
        BooleanSupplier startButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kStart);
        Trigger startButtonTrigger = new Trigger(startButton);

        startButtonTrigger.onTrue(Commands4237.burpNoteCommand());
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

        // leftTriggerTrigger.onTrue(Commands4237.cancelCommands()); 
    }

    private void configRightTrigger()
    {
        //Right trigger 
        BooleanSupplier rightTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
        Trigger rightTriggerTrigger = new Trigger(rightTrigger);

        rightTriggerTrigger.whileTrue(Commands4237.setCandleCommand(LEDColor.kWhite));
    }

    private void configDpadDown()
    {
        // Dpad down button
        BooleanSupplier dPadDown = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kDown);
        Trigger dPadDownTrigger = new Trigger(dPadDown);

        if(robotContainer.climb != null)
        {
            // dPadDownTrigger.whileTrue(robotContainer.climb.retractCommand(-0.2));
            dPadDownTrigger.onTrue(robotContainer.climb.moveToRobotCommand());
        }
    }

    private void configDpadUp()
    {
        // Dpad down button
        BooleanSupplier dPadUp = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kUp);
        Trigger dPadUpTrigger = new Trigger(dPadUp);

            // dPadUpTrigger.whileTrue(robotContainer.climb.extendCommand(0.2));
            // dPadUpTrigger.onTrue(robotContainer.climb.moveToChainCommand());
        dPadUpTrigger.onTrue(Commands4237.raiseClimbToChainCommand());
    }

    private void configDpadLeft()
    {
        // Dpad down button
        BooleanSupplier dPadLeft = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kLeft);
        Trigger dPadLeftTrigger = new Trigger(dPadLeft);

        // dPadLeftTrigger.onTrue(Commands4237.moveAmpAssistCommand());
    }

    private void configDpadRight()
    {
        // Dpad down button
        BooleanSupplier dPadRight = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kRight);
        Trigger dPadRightTrigger = new Trigger(dPadRight);

        // if(robotContainer.ampAssist != null)
        // {
        //     dPadRightTrigger.onTrue(robotContainer.ampAssist.ampAssistCommand(AmpAssistPosition.kOut));
        // }
    }

    private void configLeftTriggerAndDpadDown()
    {
        //Left trigger
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        // Dpad down button
        BooleanSupplier dPadDown = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kDown);
        Trigger dPadDownTrigger = new Trigger(dPadDown);

        // Left trigger and Dpad down button combination
        Trigger leftTriggerAndDpadDownTrigger = leftTriggerTrigger.and(dPadDownTrigger);

        if(robotContainer.climb != null)
        {
            leftTriggerAndDpadDownTrigger.whileTrue(robotContainer.climb.lowerCommand());
        }
    }

    private void configLeftTriggerAndDpadUp()
    {
        //Left trigger
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        // Dpad up button
        BooleanSupplier dPadUp = robotContainer.operatorController.getDpadSupplier(Xbox.Dpad.kUp);
        Trigger dPadUpTrigger = new Trigger(dPadUp);

        // Left trigger and Dpad up button combination
        Trigger leftTriggerAndDpadUpTrigger = leftTriggerTrigger.and(dPadUpTrigger);

        if(robotContainer.climb != null)
        {
            leftTriggerAndDpadUpTrigger.whileTrue(robotContainer.climb.raiseCommand());
        }
    }

    private void configLeftTriggerAndXButton()
    {
        // Left Trigger
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);

        BooleanSupplier xButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kX);
        Trigger xButtonTrigger = new Trigger(xButton);

        Trigger leftTriggerAndXButtonTrigger = leftTriggerTrigger.and(xButtonTrigger);

        if(robotContainer.flywheel != null)
        {
            leftTriggerAndXButtonTrigger.onTrue(robotContainer.flywheel.stopCommand().andThen(Commands4237.setCandleCommand(LEDColor.kRed)));
        }
    }

    private void configLeftTriggerAndYButton()
    {
        // Y Button
        BooleanSupplier yButton = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kY);
        Trigger yButtonTrigger = new Trigger(yButton);

        //Left trigger 
        BooleanSupplier leftTrigger = robotContainer.operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
        Trigger leftTriggerTrigger = new Trigger(leftTrigger);  
        
        //Left trigger and Y button combination
        // Trigger leftTriggerAndYButtonTrigger = leftTriggerTrigger.and(yButtonTrigger);

        // leftTriggerAndYButtonTrigger.onTrue(robotContainer.intakePositioning.moveUpCommand());
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
