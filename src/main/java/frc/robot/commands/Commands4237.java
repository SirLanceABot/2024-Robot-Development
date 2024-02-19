package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;
import javax.lang.model.util.ElementScanner14;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
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

    // Initialize pivotAngleMap
   


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here
    private Commands4237(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public static void setRobotContainer(RobotContainer robotContainer)
    {
        if(Commands4237.robotContainer == null)
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
                robotContainer.intakePositioning.moveDownCommand(),
                robotContainer.intake.pickupCommand(),
                robotContainer.shuttle.moveUpwardCommand(),
                robotContainer.index.acceptNoteFromShuttleCommand(),
                robotContainer.pivot.setAngleCommand(Constants.Pivot.DEFAULT_ANGLE))
            .andThen(
                robotContainer.intakePositioning.floatingCommand())
            .andThen(
                Commands.waitUntil(robotContainer.secondShuttleProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.intake.stopCommand()
                .alongWith(
                    robotContainer.intakePositioning.moveUpCommand()))
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
        
        if(robotContainer.flywheel != null && robotContainer.index != null && robotContainer.shuttle != null && robotContainer.indexProximity != null && robotContainer.secondShuttleProximity != null)
        {
            return
            robotContainer.candle.setYellowCommand()
            .alongWith(
                robotContainer.flywheel.intakeCommand(),
                robotContainer.index.intakeCommand(),
                robotContainer.shuttle.moveDownwardCommand(),
                robotContainer.pivot.setAngleCommand(Constants.Pivot.INTAKE_FROM_SOURCE_ANGLE))
            .andThen(
                Commands.waitUntil(robotContainer.secondShuttleProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.index.stopCommand(),
                    robotContainer.shuttle.stopCommand()))
            .andThen(
                robotContainer.index.acceptNoteFromShuttleCommand()
                .alongWith(
                    robotContainer.shuttle.moveUpwardCommand()))
            .andThen(
                Commands.waitUntil(robotContainer.indexProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.index.stopCommand()
                .alongWith(
                    robotContainer.shuttle. stopCommand(),
                    robotContainer.pivot.setAngleCommand(Constants.Pivot.DEFAULT_ANGLE)))
            .andThen(
                robotContainer.candle.setGreenCommand())   
            .withName("Intake From Source");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command getFlywheelToSpeedCommand(double speed)
    {
        if(robotContainer.flywheel != null && robotContainer.candle != null)
        {
            return
            robotContainer.candle.setBlueCommand()
            .alongWith(
                robotContainer.flywheel.shootCommand(speed))
            .withName("Get Flywheel To Speed");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command shootToSpeakerAngleCommand()
    {
        double distance, angle = 0.0;
        if(robotContainer.isBlueAlliance)
        {
            distance = robotContainer.drivetrain.getDistanceToBlueSpeaker();
        }
        else
        {
            distance = robotContainer.drivetrain.getDistanceToRedSpeaker();
        }

        // distance = (Math.round((distance * 39.37 / 12.0)));
        distance = distance * 3.28; // meters to feet
        angle = robotContainer.pivot.calculateAngleFromDistance(distance);

        if(robotContainer.drivetrain != null && robotContainer.pivot != null)
        {
            return
            robotContainer.pivot.setAngleCommand(angle)
            .withName("Move Pivot");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command shootCommand(DoubleSupplier rotateAngle)
    {
        if(robotContainer.drivetrain != null && robotContainer.pivot != null && robotContainer.index != null && robotContainer.flywheel != null && robotContainer.candle != null)
        {
            return 
            robotContainer.candle.setPurpleCommand()
            .alongWith(
                shootToSpeakerAngleCommand(),
                robotContainer.drivetrain.rotateToRedSpeakerCommand())  // need to figure out which alliance we are on
            .andThen(
                robotContainer.index.feedNoteToFlywheelCommand(0.5))
            .andThen(
                Commands.waitSeconds(1.0))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.index.stopCommand()))
            .andThen(
                robotContainer.pivot.setAngleCommand(Constants.Pivot.DEFAULT_ANGLE))
            .andThen(
                robotContainer.candle.setRedCommand())
            .withName("Shoot");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command shootToAmpCommand()
    {
        if(robotContainer.pivot != null && robotContainer.flywheel != null && robotContainer.index != null)
        {
            return
            getFlywheelToSpeedCommand(0.2)
            .andThen(
                robotContainer.pivot.setAngleCommand(Constants.Pivot.SHOOT_TO_AMP_ANGLE)
                .alongWith(
                    robotContainer.index.feedNoteToFlywheelCommand(0.2)))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.index.feedNoteToFlywheelCommand(0.0),
                    robotContainer.pivot.setAngleCommand(Constants.Pivot.DEFAULT_ANGLE)))
            .withName("Shoot To Amp");
        }
        else
        {
            return Commands.none();
        }
    }
}
