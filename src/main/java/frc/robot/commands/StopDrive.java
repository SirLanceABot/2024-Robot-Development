// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class StopDrive extends Command
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private Drivetrain drivetrain;

    public StopDrive(Drivetrain drivetrain)
    {
        this.drivetrain = drivetrain;

        if(this.drivetrain != null)
            addRequirements(this.drivetrain);
    }

    @Override
    public void initialize()
    {
        if(drivetrain != null)
            drivetrain.stopMotors();
    }

    @Override
    public void execute()
    {} 

    @Override 
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {
        return true;
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
