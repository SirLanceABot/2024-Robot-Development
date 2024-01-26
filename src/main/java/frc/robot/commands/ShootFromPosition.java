// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ShootingPosition;
import frc.robot.subsystems.AmpAssist;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Pivot;
// import frc.robot.subsystems.Shuttle;
// import frc.robot.subsystems.PoseEstimator;

/** 
 * An example command that uses an example subsystem. 
 */
public class ShootFromPosition extends SequentialCommandGroup 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Flywheel flywheel;
    private final Index index;
    private final Pivot pivot;
    private final AmpAssist ampAssist;
    private final ShootingPosition shootingPosition;
    // private final PoseEstimator poseEstimator;
    private final Drivetrain drivetrain;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public ShootFromPosition(Flywheel flywheel, Index index, Pivot pivot, AmpAssist ampAssist, Drivetrain drivetrain, ShootingPosition shootingPosition) 
    {
        this.flywheel = flywheel;
        this.index = index;
        this.pivot = pivot;
        this.ampAssist = ampAssist;
        this.shootingPosition = shootingPosition;
        // this.poseEstimator = poseEstimator;
        this.drivetrain = drivetrain;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(flywheel != null && index != null && pivot != null && ampAssist != null && drivetrain != null)
        {
            addRequirements(this.flywheel);
            addRequirements(this.index);
            addRequirements(this.pivot);
            addRequirements(this.ampAssist);
            // addRequirements(this.poseEstimator);
            addRequirements(this.drivetrain);

            build();
        }
    }

    // // Called when the command is initially scheduled.
    // @Override
    // public void initialize()
    // {}

    // // Called every time the scheduler runs while the command is scheduled.
    // @Override
    // public void execute()
    // {}

    // // Called once the command ends or is interrupted.
    // @Override
    // public void end(boolean interrupted)
    // {}

    // // Returns true when the command should end.
    // @Override
    // public boolean isFinished() 
    // {
    //     return false;
    // }
    
    private void build()
    {
        switch(shootingPosition)
        {
            case kSpeakerBase:
                addCommands(new ParallelCommandGroup(
                (new InstantCommand( () -> pivot.setAngle(45, 0.02))),
                (new InstantCommand( () -> flywheel.shoot(0.4))),
                (new InstantCommand( () -> drivetrain.rotateForShooting()))
                ));
                addCommands(new InstantCommand( () -> index.feedNote(0.4)));
                break;

            case kPodium:
                addCommands(new ParallelCommandGroup(
                (new InstantCommand( () -> pivot.setAngle(45, 0.02))),
                (new InstantCommand( () -> flywheel.shoot(0.4))),
                (new InstantCommand( () -> drivetrain.rotateForShooting()))
                ));
                addCommands(new InstantCommand( () -> index.feedNote(0.4)));
                break;

            case kRandomPosition:
                addCommands(new ParallelCommandGroup(
                (new InstantCommand( () -> pivot.setAngle(45, 0.02))),
                (new InstantCommand( () -> flywheel.shoot(0.4))),
                (new InstantCommand( () -> drivetrain.rotateForShooting()))
                ));
                addCommands(new InstantCommand( () -> index.feedNote(0.4)));
                break;

            case kToAmp:
                addCommands(new ParallelCommandGroup(
                (new InstantCommand( () -> pivot.setAngle(45, 0.02))),
                (new InstantCommand( () -> flywheel.shoot(0.4))),
                (new InstantCommand( () -> ampAssist.extend()))
                ));
                addCommands(new InstantCommand( () -> index.feedNote(0.4)));
                break;

            case kOff:
                addCommands(new ParallelCommandGroup(
                (new InstantCommand( () -> pivot.setAngle(45, 0.02))),
                (new InstantCommand( () -> flywheel.shoot(0.0))),
                (new InstantCommand( () -> ampAssist.retract()))
                ));
                addCommands(new InstantCommand( () -> index.feedNote(0.0)));
                break;
            

            
        }
    }
    
    @Override
    public boolean runsWhenDisabled()
    {
        return false;
    }

    @Override
    public String toString()
    {
        String str = this.getClass().getSimpleName();
        return String.format("Command: %s( )", str);
    }
}
