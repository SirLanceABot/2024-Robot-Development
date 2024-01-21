// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.Action;
import frc.robot.subsystems.Intake.Direction;

/** 
 * An example command that uses an example subsystem. 
 */
public class UseIntakeRollers extends Command 
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
    private final Direction direction;
    private final Action action;


    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public UseIntakeRollers(Intake intake, Action action, Direction direction) 
    {
        this.intake = intake;
        this.action = action;
        this.direction = direction;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(this.intake != null)
        {
            addRequirements(this.intake);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(intake != null)
        {
            if(action == Action.kPickup && direction == Direction.kForward)
            {
                intake.pickupFront();
            }
            else if(action == Action.kPickup && direction == Direction.kBackward)
            {
                intake.pickupBack();
            }
            else if(action == Action.kEject && direction == Direction.kForward)
            {
                intake.ejectFront();
            }
            else if(action == Action.kEject && direction == Direction.kBackward)
            {
                intake.ejectBack();
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return false;
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
