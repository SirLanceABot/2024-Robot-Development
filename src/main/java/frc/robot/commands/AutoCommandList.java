// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.AutonomousTabData;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

public class AutoCommandList extends SequentialCommandGroup
{
    //This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***



    // *** CLASS & INSTANCE VARIABLES ***
    private AutonomousTabData autonomousTabData;
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private String commandString = "\n***** AUTONOMOUS COMMAND LIST *****\n";

    
    // *** CLASS CONSTRUCTOR ***
    public AutoCommandList(RobotContainer robotContainer)
    {
        this.autonomousTabData = robotContainer.mainShuffleboard.autonomousTab.getAutonomousTabData();
        this.drivetrain = robotContainer.drivetrain;
        this.gyro = robotContainer.gyro;

        build();

        System.out.println(this);
    }


    // *** CLASS & INSTANCE METHODS ***
    private void build()
    {
        add(new StopDrive(drivetrain));
    }


    // ***** Use these methods in AutonomousMode to execute the AutonomousCommandList
    private void add(Command command)
    {
        addCommands(command);
        commandString += command + "\n";
    }

    public String toString()
    {
        return commandString;
    }
}
