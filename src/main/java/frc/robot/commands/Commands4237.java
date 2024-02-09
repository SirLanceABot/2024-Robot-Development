package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import javax.lang.model.util.ElementScanner14;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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
    private static InterpolatingDoubleTreeMap map = new InterpolatingDoubleTreeMap();

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
                robotContainer.index.acceptNoteFromShuttleCommand())
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
                robotContainer.shuttle.moveDownwardCommand())
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

    public static Command movePivotCommand()
    {
        double distance, angle = 0.0;
        Optional<Alliance> color = DriverStation.getAlliance();

        // first value is distance from speaker in feet, second value is the pivot angle in degrees
        map.put(3.0, 60.6);
        map.put(4.0, 53.1);
        map.put(5.0, 46.8);
        map.put(6.0, 41.6);
        map.put(7.0, 37.3);
        map.put(8.0, 33.7);
        map.put(9.0, 30.65);
        map.put(10.0, 28.1);
        map.put(11.0, 25.9);
        map.put(12.0, 24.0);
        map.put(13.0, 22.3);
        map.put(14.0, 20.9);
        map.put(15.0, 19.6);

        if(color.get() == Alliance.Red)
        {
            distance = robotContainer.drivetrain.getDistanceToRedSpeaker();
        }
        else
        {
            distance = robotContainer.drivetrain.getDistanceToBlueSpeaker();
        }

        distance = (Math.round((distance * 39.37 / 12.0)));
        angle = map.get(distance);

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
                movePivotCommand(),
                robotContainer.drivetrain.rotateForShootingCommand())
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
                robotContainer.pivot.setAngleCommand(60)
                .alongWith(
                    robotContainer.index.feedNoteToFlywheelCommand(0.2)))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.index.feedNoteToFlywheelCommand(0.0),
                    robotContainer.pivot.setAngleCommand(45)))
            .withName("Shoot To Amp");
            
        }
        else
        {
            return Commands.none();
        }
    }
}
