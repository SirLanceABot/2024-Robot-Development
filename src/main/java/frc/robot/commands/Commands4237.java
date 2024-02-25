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


    public static Command intakeFromFloorFront()
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
        if(robotContainer.intake != null && robotContainer.shuttle != null && robotContainer.index != null && robotContainer.indexWheelsProximity != null)
        {
            return
            // robotContainer.candle.setYellowCommand()
            robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)
            .alongWith(
                robotContainer.intakePositioning.moveDownCommand(),
                robotContainer.shuttle.moveUpwardCommand(),
                robotContainer.index.acceptNoteFromShuttleCommand(),
                robotContainer.intake.pickupFrontCommand())
            .andThen(
                robotContainer.intakePositioning.floatingCommand())
            .andThen(
                Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.intake.stopCommand()
                .alongWith(
                    robotContainer.shuttle.stopCommand(),
                    robotContainer.index.stopCommand(),
                    robotContainer.intakePositioning.moveUpCommand()))
                // .alongWith(
                //     robotContainer.intakePositioning.moveUpCommand()))
            // .andThen(
            //     Commands.waitUntil(robotContainer.indexProximity.isDetectedSupplier()))
            // .andThen(
            //     robotContainer.shuttle.stopCommand()
            //     .alongWith(
            //         robotContainer.index.stopCommand()))
            // // .andThen(
            //     robotContainer.candle.setGreenCommand())
            .withName("Intake From Floor Front");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command intakeFromFloorBack()
    {
        if(robotContainer.intake != null && robotContainer.shuttle != null && robotContainer.index != null && robotContainer.indexWheelsProximity != null)
        {
            return
            // robotContainer.candle.setYellowCommand()
            robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)
            .alongWith(
                robotContainer.intakePositioning.moveDownCommand(),
                robotContainer.shuttle.moveUpwardCommand(),
                robotContainer.index.acceptNoteFromShuttleCommand(),
                robotContainer.intake.pickupBackCommand())
            .andThen(
                robotContainer.intakePositioning.floatingCommand())
            .andThen(
                Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier()))
            .andThen(
                robotContainer.intake.stopCommand()
                .alongWith(
                    robotContainer.intakePositioning.moveUpCommand(),
                    robotContainer.shuttle.stopCommand(),
                    robotContainer.index.stopCommand()))
            //     robotContainer.candle.setGreenCommand())
            .withName("Intake From Floor Back");
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Command ejectNote()
    // {
    //     if(robotContainer.index != null && robotContainer.shuttle != null && robotContainer.intake != null && robotContainer.intakePositioning != null)
    //     {
    //         return
    //         robotContainer.candle.setYellowCommand()
    //         .alongWith(
    //             robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE),
    //             robotContainer.intakePositioning.moveDownCommand(),
    //             robotContainer.index.ejectCommand(),
    //             robotContainer.shuttle.moveDownwardCommand(),
    //             robotContainer.intake.ejectCommand())
    //         .andThen(
    //             Commands.waitSeconds(5.0))
    //         .andThen(
    //             robotContainer.index.stopCommand()
    //             .alongWith(
    //                 robotContainer.shuttle.stopCommand(),
    //                 robotContainer.intake.stopCommand(),
    //                 robotContainer.intakePositioning.moveUpCommand()))
    //         .withName("Eject Note");
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    public static Command intakeFromSource()
    {
        
        if(robotContainer.flywheel != null && robotContainer.indexWheelsProximity != null)
        { 
            return
            // robotContainer.candle.setYellowCommand()
            // .alongWith(
            Commands.waitSeconds(1.0)
            .deadlineWith(
                robotContainer.flywheel.intakeCommand()
                .alongWith(
                    robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.INTAKE_FROM_SOURCE_ANGLE)))
                // robotContainer.index.intakeCommand(),
                // robotContainer.shuttle.moveDownwardCommand())
            .andThen(
                Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier()))
            .andThen(
                Commands.waitSeconds(1.0))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)))
            // .andThen(
                // robotContainer.candle.setGreenCommand())   
            .withName("Intake From Source");
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Command getFlywheelToSpeedCommand(double speed)
    // {
    //     if(robotContainer.flywheel != null && robotContainer.candle != null)
    //     {
    //         return
    //         robotContainer.candle.setBlueCommand()
    //         .alongWith(
    //             robotContainer.flywheel.shootCommand(() -> speed))
    //         .withName("Get Flywheel To Speed");
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    // public static Command shootToSpeakerAngleCommand()
    // {
    //     double distance, angle = 0.0;
    //     if(robotContainer.isBlueAlliance)
    //     {
    //         distance = robotContainer.drivetrain.getDistanceToBlueSpeaker();
    //     }
    //     else
    //     {
    //         distance = robotContainer.drivetrain.getDistanceToRedSpeaker();
    //     }

    //     // distance = (Math.round((distance * 39.37 / 12.0)));
    //     distance = distance * 3.28; // meters to feet
    //     angle = robotContainer.pivot.calculateAngleFromDistance(distance);

    //     if(robotContainer.drivetrain != null && robotContainer.pivot != null)
    //     {
    //         return
    //         robotContainer.pivot.setAngleCommand(angle)
    //         .withName("Move Pivot");
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    // public static Command shootCommand(DoubleSupplier rotateAngle)
    // {
    //     if(robotContainer.drivetrain != null && robotContainer.pivot != null && robotContainer.index != null && robotContainer.flywheel != null && robotContainer.candle != null)
    //     {
    //         return 
    //         robotContainer.candle.setPurpleCommand()
    //         .alongWith(
    //             shootToSpeakerAngleCommand(),
    //             robotContainer.drivetrain.rotateToRedSpeakerCommand())  // need to figure out which alliance we are on
    //         .andThen(
    //             robotContainer.index.feedNoteToFlywheelCommand(() -> 50.0))
    //         .andThen(
    //             Commands.waitSeconds(1.0))
    //         .andThen(
    //             robotContainer.flywheel.stopCommand()
    //             .alongWith(
    //                 robotContainer.index.stopCommand(),
    //             robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)))
    //         .andThen(
    //             robotContainer.candle.setRedCommand())
    //         .withName("Shoot");
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    public static Command shootFromSubWooferCommand()
    {
        if(robotContainer.pivot != null && robotContainer.index  != null && robotContainer.flywheel != null)
        {
            return
            Commands.waitSeconds(1.0)
            .deadlineWith(
                robotContainer.flywheel.shootCommand(() -> 80.0),
                robotContainer.pivot.setAngleCommand(65.0))
            .andThen(
                Commands.print("Past wait sam doesnt like it"))
            .andThen(
                robotContainer.index.feedNoteToFlywheelCommand(() -> 80.0))
            .andThen(
                Commands.waitSeconds(1.0))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    robotContainer.index.stopCommand()));
                    // robotContainer.pivot.setAngleCommand(32.0)));
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command samsEpicShootCommand()
    {
        if(robotContainer.pivot != null && robotContainer.index  != null && robotContainer.flywheel != null)
        {
            return
            Commands.waitSeconds(1.0)
            .deadlineWith(
                robotContainer.flywheel.shootCommand(() -> 60.0),
                robotContainer.pivot.setAngleCommand(48.0))
            .andThen(
                Commands.print("Past wait sam doesnt like it"))
            .andThen(
                robotContainer.index.feedNoteToFlywheelCommand(() -> 80.0))
            .andThen(
                Commands.waitSeconds(1.0))
            .andThen(
                robotContainer.flywheel.stopCommand()
                .alongWith(
                    robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    robotContainer.index.stopCommand()));
                    // robotContainer.pivot.setAngleCommand(32.0)));
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Command shootToAmpCommand()
    // {
    //     if(robotContainer.pivot != null && robotContainer.flywheel != null && robotContainer.index != null)
    //     {
    //         return
    //         Commands.waitSeconds(1.0)
    //         .deadlineWith(
    //             robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.SHOOT_TO_AMP_ANGLE),
    //             getFlywheelToSpeedCommand(17.0),
    //             robotContainer.ampAssist.extendCommand()) 
    //         .andThen(
    //             robotContainer.index.feedNoteToFlywheelCommand(() -> 80.0)) //TODO: Check speed
    //         .andThen(
    //             Commands.waitSeconds(1.0))
    //         .andThen(
    //             robotContainer.flywheel.stopCommand()
    //             .alongWith(
    //                 robotContainer.index.stopCommand(),
    //                 robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE))) 
    //         .withName("Shoot To Amp");
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }

    // public static Command autonomousShootCommand()
    // {
    //     if(robotContainer.pivot != null && robotContainer.index != null && robotContainer.flywheel != null)
    //     {
    //         return
    //         Commands.waitSeconds(1.0)
    //         .deadlineWith(
    //             robotContainer.flywheel.shootCommand(() -> 80.0),
    //             shootToSpeakerAngleCommand())
    //         .andThen(
    //             robotContainer.index.feedNoteToFlywheelCommand(() -> 80.0))
    //         .andThen(
    //             Commands.waitSeconds(1.0))
    //         .andThen(
    //             robotContainer.flywheel.stopCommand()
    //             .alongWith(
    //                 robotContainer.index.stopCommand(),
    //                 robotContainer.pivot.setAngleCommand(32.0)));
    //     }
    //     else
    //     {
    //         return Commands.none();
    //     }
    // }
}
