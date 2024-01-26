// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.Intake.Direction;
import frc.robot.subsystems.Index.Direction1;
import frc.robot.sensors.Proximity;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

/** 
 * An example command that uses an example subsystem. 
 */
public class IntakeFromFloor extends SequentialCommandGroup 
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
    private final Intake intake;
    private final IntakePositioning intakePositioning;
    private final Shuttle shuttle;
    private final Index index;
    private final Direction direction;
    private final Proximity secondShuttleProximity;
    private final Proximity indexProximity;
    SequentialCommandGroup group = new SequentialCommandGroup(this);

    /**
     * Creates a new IntakeFromFloor.
     *
     * @param subsystem The subsystem used by this command.
     */ 
    public IntakeFromFloor(Intake intake, IntakePositioning intakePositioning, Shuttle shuttle, Index index, Direction direction, Proximity secondShuttleProximity, Proximity indexProximity) 
    {
        this.intake = intake;
        this.intakePositioning = intakePositioning;
        this.shuttle = shuttle;
        this.index = index;
        this.direction = direction;
        this.secondShuttleProximity = secondShuttleProximity;
        this.indexProximity = indexProximity;
        // Use addRequirements() here to declare subsystem dependencies.
        if(intake != null && intakePositioning != null && shuttle != null && index != null)
        {
            addRequirements(this.intake);
            addRequirements(this.intakePositioning);
            addRequirements(this.shuttle);
            addRequirements(this.index);

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
    // {
    //     addCommands(new ParallelCommandGroup(
    // }

    // Called once the command ends or is interrupted.
    // @Override
    // public void end(boolean interrupted)
    // {}

    // Returns true when the command should end.
    // @Override
    // public boolean isFinished() 
    // {
    //     return false;
    // }

    private void build()
    {
        addCommands( new ParallelCommandGroup(
                     new InstantCommand(() -> intake.pickupFront()),
                     new InstantCommand(() -> intakePositioning.extend()),
                     new InstantCommand(() -> shuttle.moveUpward()),
                     new InstantCommand(() -> index.acceptNote())));
        addCommands( new WaitUntilCommand(() -> secondShuttleProximity.isDetected()));
        addCommands( new ParallelCommandGroup(
                     new InstantCommand(() -> intake.off()),
                     new InstantCommand(() -> intakePositioning.retract())));
        addCommands( new WaitUntilCommand(() -> indexProximity.isDetected()));
        addCommands( new ParallelCommandGroup(
                     new InstantCommand(() -> shuttle.off()),
                     new InstantCommand(() -> index.turnOff())));
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
