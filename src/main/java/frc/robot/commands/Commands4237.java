package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public final class Commands4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    // Put all inner enums and inner classes here



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private static RobotContainer robotContainer = null;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private Commands4237(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public static void setRobotContainer(RobotContainer robotContainer)
    {
        if(robotContainer == null)
            Commands4237.robotContainer = robotContainer;
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    // public static Command exampleCommand()
    // {
    //     if(subsystems != null)
    //     {
    //         return someCompoundCommand;
    //     }
    //     else
    //         return Commands.none();
    // }


    public static Command intakeFromFloor()
    {
        // SequentialCommandGroup scg = new SequentialCommandGroup();
        // // Command command = new SequentialCommandGroup();

        // scg.addCommands(Commands.runOnce(() -> robotContainer.intake.pickupFront(), robotContainer.intake));
        // scg.addCommands(Commands.runOnce(() -> robotContainer.intakePositioning.extend(), robotContainer.intakePositioning));
        // scg.addCommands(Commands.runOnce(() -> robotContainer.shuttle.moveUpward(), robotContainer.shuttle));
        // ParallelCommandGroup pcg = new ParallelCommandGroup();

        // pcg.addCommands(Commands.runOnce(() -> robotContainer.intake.pickupFront(), robotContainer.intake));
        // pcg.addCommands(Commands.runOnce(() -> robotContainer.intakePositioning.extend(), robotContainer.intakePositioning));
        // pcg.addCommands(Commands.runOnce(() -> robotContainer.shuttle.moveUpward(), robotContainer.shuttle));

        // return
        // Commands.runOnce(() -> robotContainer.intakePositioning.extend(),  robotContainer.intakePositioning)
        //     .alongWith(
        //         Commands.runOnce(() -> robotContainer.intake.pickupFront(), robotContainer.intake),
        //         Commands.runOnce(() -> robotContainer.shuttle.moveUpward(), robotContainer.shuttle),
        //         Commands.runOnce(() -> robotContainer.index.acceptNote(), robotContainer.index))
        //     .andThen(
        //         Commands.waitUntil(() -> robotContainer.secondShuttleProximity.isDetected()))
        //     .andThen(
        //         Commands.runOnce(() -> robotContainer.intake.stop(), robotContainer.intake)
        //         .alongWith(
        //             Commands.runOnce(() -> robotContainer.intakePositioning.retract(), robotContainer.intakePositioning)));

        //TODO: Add timeout in case of sensor failure until we can test live
        if(robotContainer.intake != null && robotContainer.intakePositioning != null && robotContainer.shuttle != null && robotContainer.index != null && robotContainer.secondShuttleProximity != null && robotContainer.indexProximity != null)
        {
            return
            robotContainer.candle.setYellowCommand()
            .alongWith(
                robotContainer.intakePositioning.moveUpCommand(),
                robotContainer.intake.pickupFrontCommand(),
                robotContainer.shuttle.moveUpwardCommand(),
                robotContainer.index.acceptNoteFromShuttleCommand())
            .andThen(
                robotContainer.intakePositioning.floatingCommand())
            .andThen(
                Commands.waitUntil(robotContainer.secondShuttleProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.intake.stopCommand()
                .alongWith(
                    robotContainer.intakePositioning.moveDownCommand()))
            .andThen(
                Commands.waitUntil(robotContainer.indexProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.shuttle.stopCommand()
                .alongWith(
                    robotContainer.index.stopCommand()))
            .andThen(
                robotContainer.candle.setGreenCommand())
            .withName("Intake From Floor");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command intakeFromSource()
    {
        if(robotContainer.flywheel != null && robotContainer.index != null && robotContainer.indexWheelsProximity != null)
        {
            return
            robotContainer.candle.setYellowCommand()
            .alongWith(
                robotContainer.flywheel.intakeCommand(),
                robotContainer.index.intakeCommand(),
                robotContainer.shuttle.moveDownwardCommand())
            .andThen(
                Commands.waitUntil(robotContainer.secondShuttleProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.index.stopCommand()),
                    robotContainer.shuttle.stopCommand())
            .andThen(
                robotContainer.index.acceptNoteFromShuttleCommand())
                .alongWith(
                    robotContainer.shuttle.moveUpwardCommand())
            .andThen(
                Commands.waitUntil(robotContainer.indexProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.index.stopCommand()
                .alongWith(
                    robotContainer.shuttle. stopCommand()))
            .andThen(
                robotContainer.candle.setGreenCommand())   
            .withName("Intake From Source");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command getFlywheelToSpeedCommand()
    {
        if(robotContainer.flywheel != null && robotContainer.candle != null)
        {
            return
            robotContainer.candle.setBlueCommand()
            .alongWith(
                robotContainer.flywheel.shootCommand(0.5));
        }
        else
        {
            return Commands.none();
        }
        
    }

    public static Command shootCommand(double pivotAngle, DoubleSupplier rotateAngle)
    {
        if(robotContainer.drivetrain != null && robotContainer.pivot != null && robotContainer.index != null && robotContainer.flywheel != null && robotContainer.candle != null)
        {
            return 
            robotContainer.candle.setPurpleCommand()
            .alongWith(
                robotContainer.pivot.movePivotCommand(pivotAngle),
                robotContainer.drivetrain.driveCommand(() -> 0.0, () -> 0.0, rotateAngle, () -> 0.0))
            .andThen(
                robotContainer.index.feedNoteToFlywheelCommand(0.5))
            .andThen(
                Commands.waitSeconds(1.0))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.index.stopCommand()))
            .andThen(
                robotContainer.pivot.movePivotCommand(Constants.Pivot.DEFAULT_ANGLE))
            .andThen(
                robotContainer.candle.setRedCommand())
            .withName("Shoot");
        }
        else
        {
            return Commands.none();
        }
    }
}
