// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Shuttle;
// import frc.robot.subsystems.IntakePositioning.IntakePosition;

/** 
 * An example command that uses an example subsystem. 
 */
public class RetractIntake extends Command 
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
    private final Shuttle shuttle;
    private final Intake intake;
    private final IntakePositioning intakePositioning;

    /**
     * Creates a new RetractIntake.
     *
     * @param subsystem The subsystem used by this command.
     */
    public RetractIntake(Shuttle shuttle, Intake intake, IntakePositioning intakePositioning)
    {
        this.shuttle = shuttle;
        this.intake = intake;
        this.intakePositioning = intakePositioning;
        // Use addRequirements() here to declare subsystem dependencies.
        if(shuttle != null && intake != null && intakePositioning != null)
        {
            addRequirements(this.shuttle);
            addRequirements(this.intake);
            addRequirements(this.intakePositioning);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {}

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
