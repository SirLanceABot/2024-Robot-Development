// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.AmpAssist;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Pivot;
// import frc.robot.subsystems.Shuttle;

/** 
 * An example command that uses an example subsystem. 
 */
public class ShootToAmp extends SequentialCommandGroup 
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

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public ShootToAmp(Flywheel flywheel, Index index, Pivot pivot, AmpAssist ampAssist) 
    {
        this.flywheel = flywheel;
        this.index = index;
        this.pivot = pivot;
        this.ampAssist = ampAssist;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(flywheel != null && index != null && pivot != null && ampAssist != null)
        {
            addRequirements(this.flywheel);
            addRequirements(this.index);
            addRequirements(this.pivot);
            addRequirements(this.ampAssist);

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

        addCommands(new ParallelCommandGroup(
        (new InstantCommand( () -> pivot.setAngle(45))),
        (new InstantCommand( () -> flywheel.shoot(0.6)))
        ));

        // addCommands(new InstantCommands)
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
