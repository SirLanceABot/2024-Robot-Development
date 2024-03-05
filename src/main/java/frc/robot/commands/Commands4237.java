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
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Candle4237.LEDColor;

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
        if(robotContainer.intake != null && robotContainer.shuttle != null && robotContainer.index != null && robotContainer.indexWheelsProximity != null && robotContainer.firstShuttleProximity != null)
        {
            return
            Commands.either(
                Commands.none(),

                // robotContainer.candle.setYellowCommand()
                setCandleCommand(LEDColor.kYellow)
                .andThen(
                    robotContainer.intakePositioning.moveDownCommand())
                .andThen(
                    Commands.waitUntil(robotContainer.intakePositioning.isIntakeDownSupplier()).withTimeout(0.5))
                .andThen(
                    robotContainer.intakePositioning.floatingCommand())
                // .andThen(
                //     robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE))
                .andThen(
                    Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier())
                    .deadlineWith(
                        robotContainer.intake.pickupFrontCommand(),
                        robotContainer.shuttle.moveUpwardCommand(),
                        robotContainer.index.acceptNoteFromShuttleCommand(),
                        // robotContainer.candle.setGreenCommand()
                        setCandleCommand(LEDColor.kGreen)
                            .onlyIf(
                                robotContainer.firstShuttleProximity.isDetectedSupplier())
                            .repeatedly()))
                .andThen(
                    Commands.parallel(
                        robotContainer.index.stopCommand(),
                        robotContainer.intakePositioning.moveUpCommand(),
                        robotContainer.intake.stopCommand(),
                        robotContainer.shuttle.stopCommand()))
                        // robotContainer.candle.setGreenCommand())
                .handleInterrupt( () -> setCandleDefaultRed())
                .withName("Intake From Floor Front"),

                robotContainer.indexWheelsProximity.isDetectedSupplier());
                // robotContainer.index.stopCommand()
                // .alongWith(
                //     robotContainer.intakePositioning.moveUpCommand(),
                //     robotContainer.intake.stopCommand(),
                //     robotContainer.shuttle.stopCommand()))
                    
                    
            // robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)
            // .alongWith(
            //     robotContainer.intakePositioning.moveDownCommand(),
            //     robotContainer.shuttle.moveUpwardCommand(),
            //     robotContainer.index.acceptNoteFromShuttleCommand(),
            //     robotContainer.intake.pickupFrontCommand())
            // .andThen(
            //     robotContainer.intakePositioning.floatingCommand())
            // .andThen(
            //     Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier()))
            // .andThen(
            //     robotContainer.intake.stopCommand()
            //     .alongWith(
            //         robotContainer.shuttle.stopCommand(),
            //         robotContainer.index.stopCommand(),
            //         robotContainer.intakePositioning.moveUpCommand()))
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
            setCandleCommand(LEDColor.kYellow)
            .andThen(
                robotContainer.intakePositioning.moveDownCommand())
            .andThen(
                Commands.waitUntil(robotContainer.intakePositioning.isIntakeDownSupplier()).withTimeout(0.5))
            .andThen(
                robotContainer.intakePositioning.floatingCommand())
            // .andThen(
            //     robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE))
            .andThen(
                Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier())
                .deadlineWith(
                    robotContainer.intake.pickupBackCommand(),
                    robotContainer.shuttle.moveUpwardCommand(),
                    robotContainer.index.acceptNoteFromShuttleCommand()))
            .andThen(
                Commands.parallel(
                    robotContainer.index.stopCommand(),
                    robotContainer.intakePositioning.moveUpCommand(),
                    robotContainer.intake.stopCommand(),
                    robotContainer.shuttle.stopCommand(),
                    // robotContainer.candle.setGreenCommand()))
                    setCandleCommand(LEDColor.kGreen)))
            .handleInterrupt( () -> setCandleDefaultRed())
            .withName("Intake From Floor Back");

            // robotContainer.candle.setYellowCommand()
            // robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)
            // .alongWith(
            //     robotContainer.intakePositioning.moveDownCommand(),
            //     robotContainer.shuttle.moveUpwardCommand(),
            //     robotContainer.index.acceptNoteFromShuttleCommand(),
            //     robotContainer.intake.pickupBackCommand())
            // .andThen(
            //     robotContainer.intakePositioning.floatingCommand())
            // .andThen(
            //     Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier()))
            // .andThen(
            //     robotContainer.intake.stopCommand()
            //     .alongWith(
            //         robotContainer.intakePositioning.moveUpCommand(),
            //         robotContainer.shuttle.stopCommand(),
            //         robotContainer.index.stopCommand()))
            // //     robotContainer.candle.setGreenCommand())
            // .withName("Intake From Floor Back");
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

    public static Command burpNoteCommand()
    {
        if(robotContainer.flywheel != null)
        {
            return
            // robotContainer.candle.setPurpleCommand()
            setCandleCommand(LEDColor.kPurple)
            .andThen(
                robotContainer.flywheel.shootCommand(() -> 10.0))
                .withTimeout(3.0)
            // .andThen(
            //     Commands.waitSeconds(3.0))
            .andThen(
                Commands.parallel(
                    robotContainer.flywheel.stopCommand(),
                    // robotContainer.candle.setRedCommand()))
                    setCandleCommand(LEDColor.kRed)))
            .withName("Burp Note");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command intakeFromSource()
    {
        
        if(robotContainer.flywheel != null && robotContainer.indexWheelsProximity != null)
        { 
            return
            // robotContainer.candle.setYellowCommand()
            setCandleCommand(LEDColor.kYellow)
            .andThen(
            Commands.waitSeconds(1.0)
            .deadlineWith(
                Commands.parallel(
                    robotContainer.flywheel.intakeCommand(),
                    robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.INTAKE_FROM_SOURCE_ANGLE))))
                // robotContainer.flywheel.intakeCommand()
                // .alongWith(
                //     robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.INTAKE_FROM_SOURCE_ANGLE)))
                // robotContainer.index.intakeCommand(),
                // robotContainer.shuttle.moveDownwardCommand())
            .andThen(
                Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier()))
            .andThen(
                Commands.waitSeconds(1.0))
            .andThen(
                Commands.parallel(
                    robotContainer.flywheel.stopCommand(),
                    // robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    // robotContainer.candle.setGreenCommand()))
                    setCandleCommand(LEDColor.kGreen)))
                // robotContainer.flywheel.stopCommand()
                // .alongWith(
                //     robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE)))
            // .andThen(
                // robotContainer.candle.setGreenCommand())
            .handleInterrupt( () -> setCandleDefaultRed())
            .withName("Intake From Source");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command getFlywheelToSpeedCommand(double speed)
    {
        if(robotContainer.flywheel != null)
        {
            return
            // robotContainer.candle.setBlueCommand()
            setCandleCommand(LEDColor.kBlue)
            .andThen(
                robotContainer.flywheel.shootCommand(() -> speed))
            .handleInterrupt( () -> setCandleDefaultRed())
            .withName("Get Flywheel To Speed");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command setAngleToCorrectSpeakerCommand()
    {
        double distance, angle = 0.0;
        // distance = (Math.round((distance * 39.37 / 12.0)));
        
        if(robotContainer.drivetrain != null && robotContainer.pivot != null)
        {
            // if(robotContainer.isBlueAlliance)
            // {
            //     distance = robotContainer.drivetrain.getDistanceToBlueSpeaker();
            // }
            // else
            // {
            //     distance = robotContainer.drivetrain.getDistanceToRedSpeaker();
            // }

            // distance = distance * 3.2808; // meters to feet
            // angle = robotContainer.pivot.calculateAngleFromDistance(distance);

            // return
            // robotContainer.pivot.setAngleCommand(angle)
            // .withName("Move Pivot");

            return
            Commands.either(
                robotContainer.pivot.setAngleCommand(() -> (
                    robotContainer.pivot.calculateAngleFromDistance(() -> (
                        robotContainer.drivetrain.getDistanceToBlueSpeaker() * 3.2808)))),

                robotContainer.pivot.setAngleCommand(() -> (
                    robotContainer.pivot.calculateAngleFromDistance(() -> (
                        robotContainer.drivetrain.getDistanceToRedSpeaker() * 3.2808)))),

                robotContainer.isBlueAllianceSupplier());
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command resetPivotAngleCommand()
    {
        if(robotContainer.pivot != null)
        {
            return
            Commands.waitSeconds(2.0)
            .deadlineWith(
                robotContainer.pivot.moveDownCommand())
            .andThen(
                robotContainer.pivot.stopCommand())
            .andThen(
                robotContainer.pivot.resetMotorEncoderCommand());
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command setFlywheelToCorrectVelocityCommand()
    {
        DoubleSupplier distance;
        DoubleSupplier velocity;
        // distance = (Math.round((distance * 39.37 / 12.0)));
        
        if(robotContainer.drivetrain != null && robotContainer.flywheel != null)
        {
            // if(robotContainer.isBlueAlliance)
            // {
            //     distance = () -> (robotContainer.drivetrain.getDistanceToBlueSpeaker() * 3.2808);
            // }
            // else
            // {
            //     distance = () -> (robotContainer.drivetrain.getDistanceToRedSpeaker() * 3.2808);
            // }

            // // distance = () -> (distance.getAsDouble() * 3.2808); // meters to feet
            // velocity = () -> (robotContainer.flywheel.calculateSpeedFromDistance(distance.getAsDouble()));

            // return
            // robotContainer.flywheel.shootCommand(velocity)
            // .withName("Set Flywheel Speed To Correct Velocity Command");
            return
            Commands.either(
                robotContainer.flywheel.shootCommand( () -> (
                    robotContainer.flywheel.calculateSpeedFromDistance(() -> (
                        robotContainer.drivetrain.getDistanceToBlueSpeaker() * 3.2808)))),

                robotContainer.flywheel.shootCommand( () -> (
                    robotContainer.flywheel.calculateSpeedFromDistance(() -> (
                        robotContainer.drivetrain.getDistanceToRedSpeaker() * 3.2808)))),

                robotContainer.isBlueAllianceSupplier());

        }
        else
        {
            return Commands.none();
        }
    }

    public static Command rotateToSpeakerCommand()
    {
        if(robotContainer.drivetrain !=null)
        {
            // if(robotContainer.isBlueAllianceSupplier().getAsBoolean())
            // if(true)
            // {
            //     return 
            //     robotContainer.drivetrain.rotateToBlueSpeakerCommand()
            //     .until(
            //         robotContainer.drivetrain.isAlignedWithBlueSpeaker())
            //     .withTimeout(1.0)
            //     .withName("Rotate to Blue Speaker");
            // }
            // else
            // {
            //     return 
            //     robotContainer.drivetrain.rotateToRedSpeakerCommand()
            //     .until(
            //         robotContainer.drivetrain.isAlignedWithRedSpeaker())
            //     .withTimeout(1.0)
            //     .withName("Rotate to Red Speaker");
            // }
            return
            Commands.either(
                robotContainer.drivetrain.rotateToBlueSpeakerCommand()
                .until(
                    robotContainer.drivetrain.isAlignedWithBlueSpeaker())
                .withTimeout(2.0)
                .withName("Rotate to Blue Speaker"),

                robotContainer.drivetrain.rotateToRedSpeakerCommand()
                .until(
                    robotContainer.drivetrain.isAlignedWithRedSpeaker())
                .withTimeout(2.0)
                .withName("Rotate to Red Speaker"),

                robotContainer.isBlueAllianceSupplier());
        }
        else
        {
            return Commands.none();
        }
    }

    // public static Command shootCommand()
    // {
    //     if(robotContainer.drivetrain != null && robotContainer.pivot != null && robotContainer.index != null && robotContainer.flywheel != null && robotContainer.candle != null)
    //     {
    //         return 
    //         robotContainer.candle.setPurpleCommand()
    //         .alongWith(
    //             shootToSpeakerAngleCommand(),
    //             rotateToSpeakerCommand())
    //             // robotContainer.drivetrain.rotateToRedSpeakerCommand())  // need to figure out which alliance we are on
    //         .andThen(
    //             robotContainer.index.feedNoteToFlywheelCommand(() -> 80.0))
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
            // Commands.waitSeconds(2.0)
            // rotateToSpeakerCommand()
            // .andThen(
            // robotContainer.candle.setPurpleCommand()
            setCandleCommand(LEDColor.kPurple)
            .andThen(
                Commands.waitUntil(() -> (robotContainer.pivot.isAtAngle(64.5).getAsBoolean() && 
                                        robotContainer.flywheel.isAtSpeed(65.0).getAsBoolean()))
                                        .withTimeout(1.0)
                // .alongWith(
                //     Commands.waitUntil(robotContainer.flywheel.isAtSpeed(80.0)))
                .deadlineWith(
                    robotContainer.flywheel.shootCommand(() -> 65.0),
                    robotContainer.pivot.setAngleCommand(() -> 64.5)))
            // .andThen(
            //     Commands.print("Hello Woodard"))      
            .andThen(
                Commands.waitSeconds(0.5)
                .deadlineWith(
                    robotContainer.index.feedNoteToFlywheelCommand()))
            .andThen(
                Commands.parallel(
                    robotContainer.flywheel.stopCommand(),
                    // robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    robotContainer.index.stopCommand(),
                    // robotContainer.candle.setRedCommand()))
                    setCandleCommand(LEDColor.kRed)))
                // robotContainer.flywheel.stopCommand()
                // .alongWith(
                //     robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                //     robotContainer.index.stopCommand()))
            .withName("Subwoofer Shoot");
                    // robotContainer.pivot.setAngleCommand(32.0)));
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command podiumShootCommand()
    {
        if(robotContainer.pivot != null && robotContainer.index  != null && robotContainer.flywheel != null && robotContainer.drivetrain != null)
        {
            return
            // Commands.waitSeconds(1.0)
            
            // .andThen(
            // robotContainer.candle.setPurpleCommand()
            setCandleCommand(LEDColor.kPurple)
            .andThen(
                Commands.parallel(
                // rotateToSpeakerCommand(),

                    Commands.waitUntil(() -> (robotContainer.pivot.isAtAngle(48.0).getAsBoolean() && 
                                                robotContainer.flywheel.isAtSpeed(55.0).getAsBoolean()))
                                                .withTimeout(1.0)
                    .deadlineWith(
                        setFlywheelToCorrectVelocityCommand(),
                        setAngleToCorrectSpeakerCommand())))
                    
                    // robotContainer.flywheel.shootCommand(() -> 65.0),
                    // robotContainer.pivot.setAngleCommand(() -> 48)))
            // .andThen(
            //     Commands.print("Past wait sam doesnt like it"))
                
            .andThen(
                Commands.waitSeconds(0.5)
                .deadlineWith(
                    robotContainer.index.feedNoteToFlywheelCommand()))
                    // .until(() -> !robotContainer.indexWheelsProximity.isDetectedSupplier().getAsBoolean())
            // .andThen(
            //     Commands.waitSeconds(1.0))
            .andThen(
                Commands.parallel(
                    robotContainer.flywheel.stopCommand(),
                    // robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    robotContainer.index.stopCommand(),
                    // robotContainer.candle.setRedCommand()))
                    setCandleCommand(LEDColor.kRed)))
                // robotContainer.flywheel.stopCommand()
                // .alongWith(
                //     robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                //     robotContainer.index.stopCommand()))
            .withName("Podium Shoot");
                    // robotContainer.pivot.setAngleCommand(32.0)));
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command extendClimbToChainCommand()
    {
        if(robotContainer.climb != null && robotContainer.intakePositioning != null && robotContainer.pivot != null)
        {
            return
            Commands.parallel(
                // robotContainer.candle.setRainbowCommand(),
                setCandleCommand(LEDColor.kRainbow),
                robotContainer.climb.extendCommand(),
                robotContainer.intakePositioning.moveUpCommand(),
                robotContainer.pivot.setAngleCommand( () -> 25.0));
                // Commands.run(() -> robotContainer.candle.setRainbow()));
        }
        else
        {
            return
            Commands.none();
        }
    }

    // public static Command shootToAmpCommand()
    // {
    //     if(robotContainer.pivot != null && robotContainer.flywheel != null && robotContainer.index != null)
    //     {
    //         return
    //         Commands.waitSeconds(1.0)
    //                 .deadlineWith(
    //                         robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.SHOOT_TO_AMP_ANGLE),
    //                         getFlywheelToSpeedCommand(17.0),
    //                         robotContainer.ampAssist.extendCommand()) 
    //                 .andThen(
    //                         robotContainer.index.feedNoteToFlywheelCommand(() -> 80.0)) //TODO: Check speed
    //                 .andThen(
    //                         Commands.waitSeconds(1.0))
    //                 .andThen(
    //                         robotContainer.flywheel.stopCommand()
    //                         .alongWith(
    //                                 robotContainer.index.stopCommand(),
    //                                 robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE))) 
    //                 .withName("Shoot To Amp");
    //         }
    //     else
    //         {
    //         return Commands.none();
    //         }
    // }

    public static Command extendAmpCommand()
    {
        if(robotContainer.pivot != null && robotContainer.ampAssist != null)
        {
            return
            setCandleCommand(LEDColor.kPurple)
            .andThen(
                Commands.waitUntil(() -> robotContainer.pivot.isAtAngle(robotContainer.pivot.classConstants.SHOOT_TO_AMP_ANGLE).getAsBoolean())
                                    .withTimeout(1.0)
                .deadlineWith(
                    robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.SHOOT_TO_AMP_ANGLE)))
            .andThen(
                robotContainer.ampAssist.extendCommand())
            .withName("Extend AmpAssist to Shoot Command");
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
            setCandleCommand(LEDColor.kPurple)
            .andThen(
                Commands.waitUntil(() -> (robotContainer.flywheel.isAtSpeed(17.0).getAsBoolean()))
                                        .withTimeout(1.0)
                .deadlineWith(
                    robotContainer.flywheel.shootCommand(() -> 17.0)))
            .andThen(
                Commands.waitSeconds(0.5)
                .deadlineWith(
                    robotContainer.index.feedNoteToFlywheelAmpCommand()))
            .andThen(
                Commands.parallel(
                    robotContainer.flywheel.stopCommand(),
                    // robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    robotContainer.index.stopCommand()))
                    // setCandleCommand(LEDColor.kRed)))
            .withName("Shoot to amp Command");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command retractAmpCommand()
    {
        if(robotContainer.pivot != null && robotContainer.ampAssist != null)
        {
            return
            // setCandleCommand(LEDColor.kPurple)
            // .andThen(
                robotContainer.ampAssist.retractCommand()
                .alongWith(
                    Commands.waitSeconds(0.5))
            .andThen(
                Commands.waitUntil(() -> robotContainer.pivot.isAtAngle(robotContainer.pivot.classConstants.DEFAULT_ANGLE).getAsBoolean())
                                        .withTimeout(1.0)
                .deadlineWith(
                    robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    setCandleCommand(LEDColor.kRed)))

            .withName("Extend AmpAssist to Shoot Command");
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command moveAmpAssistCommand()
    {
        if(robotContainer.ampAssist != null)
        {
            return
            Commands.either(
                extendAmpCommand(), 
                retractAmpCommand(), 
                robotContainer.ampAssist.isAmpAssistRetracted())
            .withName("Move AmpAssist Command");
        }
        else
        {
            return Commands.none();
        }
        
    }

    public static Command autonomousShootCommand()
    {
        if(robotContainer.pivot != null && robotContainer.index  != null && robotContainer.flywheel != null && robotContainer.drivetrain != null && robotContainer.indexWheelsProximity != null)
        {
            return
            // Commands.waitSeconds(1.0)
            // rotateToSpeakerCommand()
            // .andThen(
            Commands.either(
            // robotContainer.candle.setPurpleCommand()
            setCandleCommand(LEDColor.kPurple)
            .andThen(
                Commands.waitUntil(() -> (robotContainer.pivot.isAtAngle(48.0).getAsBoolean() && 
                                        robotContainer.flywheel.isAtSpeed(60.0).getAsBoolean()))
                                                .withTimeout(1.0)
                .deadlineWith(
                    robotContainer.flywheel.shootCommand(() -> 60.0),
                    robotContainer.pivot.setAngleCommand(() -> 48.0)))
            .andThen(
                Commands.waitSeconds(0.5)
                .deadlineWith(
                    robotContainer.index.feedNoteToFlywheelCommand()))
                    // .until(() -> !robotContainer.indexWheelsProximity.isDetectedSupplier().getAsBoolean())
            // .andThen(
            //     Commands.waitSeconds(1.0))
            .andThen(
                Commands.parallel(
                    robotContainer.flywheel.stopCommand(),
                    // robotContainer.pivot.setAngleCommand(() -> robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                    robotContainer.index.stopCommand(),
                    // robotContainer.candle.setRedCommand()))
                    setCandleCommand(LEDColor.kRed))),
                // robotContainer.flywheel.stopCommand()
                // .alongWith(
                //     robotContainer.pivot.setAngleCommand(robotContainer.pivot.classConstants.DEFAULT_ANGLE),
                //     robotContainer.index.stopCommand()))
                
                Commands.none(),

                robotContainer.indexWheelsProximity.isDetectedSupplier()
            )
            .withName("Autonomous Shoot");
                    // robotContainer.pivot.setAngleCommand(32.0)));

        }
        else
        {
            return Commands.none();
        }
    }

    public static Command autonomousFinishIntakeCommand()
    {
        if(robotContainer.intake != null && robotContainer.shuttle != null && robotContainer.index != null && robotContainer.indexWheelsProximity != null && robotContainer.firstShuttleProximity != null)
        {
            return
            Commands.either(
                Commands.none(),

            // robotContainer.candle.setYellowCommand()
            setCandleCommand(LEDColor.kYellow)
            .andThen(
                Commands.waitUntil(robotContainer.indexWheelsProximity.isDetectedSupplier())
                .deadlineWith(
                    robotContainer.intake.pickupFrontCommand(),
                    robotContainer.shuttle.moveUpwardCommand(),
                    robotContainer.index.acceptNoteFromShuttleCommand(),
                    // robotContainer.candle.setGreenCommand()
                    setCandleCommand(LEDColor.kGreen)
                        .onlyIf(
                        robotContainer.firstShuttleProximity.isDetectedSupplier())
                        .repeatedly()
                    ))
            .andThen(
                Commands.parallel(
                    robotContainer.index.stopCommand(),
                    robotContainer.shuttle.stopCommand()))
                    // robotContainer.candle.setGreenCommand()))
            .withTimeout(1.5)
            .withName("Autonomous Finish Intake"),

            robotContainer.indexWheelsProximity.isDetectedSupplier());
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command resetGyroCommand()
    {
        if(robotContainer.gyro !=null)
        {
            return
            Commands.either(
                robotContainer.gyro.setYawBlueCommand(),
                robotContainer.gyro.setYawRedCommand(), 
                robotContainer.isBlueAllianceSupplier());
        }
        else
        {
            return Commands.none();
        }
    }

    public static Command setCandleCommand(LEDColor ledColor)
    {
        if(robotContainer.candle != null)
        {
            switch(ledColor)
            {
                case kRed:
                    return robotContainer.candle.setRedCommand();

                case kYellow:
                    return robotContainer.candle.setYellowCommand();

                case kGreen:
                    return robotContainer.candle.setGreenCommand();

                case kBlue:
                    return robotContainer.candle.setBlueCommand();

                case kPurple:
                    return robotContainer.candle.setPurpleCommand();

                case kWhite:
                    return robotContainer.candle.setWhiteCommand();
                    
                case kRainbow:
                    return robotContainer.candle.setRainbowCommand();

                case kOff:
                    return robotContainer.candle.stopCommand();

                default:
                    return Commands.none();
            }
        }
        else
        {
            return 
            Commands.none();
        }
        // return
        // Commands.either(
        //     robotContainer.candle.setColorCommand(ledColor),

        //     Commands.none(),

        //     () -> (robotContainer.candle != null));
    }

    private static void setCandleDefaultRed()
    {
        if(robotContainer.candle != null)
        {
            robotContainer.candle.setRed(false);
        }
        else
        {
            // do nothing
        }
    } 
}