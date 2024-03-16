// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class SwerveDrive extends Command
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
    private final Drivetrain drivetrain;
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier turn;
    private boolean fieldRelative;
    private boolean useSlewRateLimiter;
    
    public SwerveDrive(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn, boolean fieldRelative, boolean useSlewRateLimiter)
    {
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.turn = turn;
        this.fieldRelative = fieldRelative;
        this.useSlewRateLimiter = useSlewRateLimiter;

        if(this.drivetrain != null)
            addRequirements(this.drivetrain);
    }

    @Override 
    public void initialize()
    {}

    @Override
    public void execute()
    {
        if(drivetrain != null)
            drivetrain.drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), turn.getAsDouble(), fieldRelative, useSlewRateLimiter);
    }

    @Override
    public void end(boolean interrupted)
    {}

    @Override
    public boolean runsWhenDisabled()
    {
        return false;
    }

    @Override
    public boolean isFinished()
    {
        if(drivetrain != null)
            return false;
        else
            return true;
    }

    @Override
    public String toString()
    {
        String str = this.getClass().getSimpleName();
        return String.format("Command: %s( )", str);
    }
}