// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Constants.Candle;
import frc.robot.sensors.Gyro4237;
import frc.robot.subsystems.AmpAssist;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shuttle;
import frc.robot.shuffleboard.AutonomousTabData;


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
    private final Intake intake;
    private final Climb climb;
    private final AmpAssist ampAssist;
    private final Flywheel flywheel;
    private final Index index;
    private final Pivot pivot;
    private final Candle4237 candle;
    private final Shuttle shuttle;

    private String commandString = "\n***** AUTONOMOUS COMMAND LIST *****\n";
    private String pathPlannerString = "";
    
    // *** CLASS CONSTRUCTOR ***
    public AutoCommandList(RobotContainer robotContainer, AutonomousTabData autonomousTabData)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.autonomousTabData = robotContainer.mainShuffleboard.autonomousTab.getAutonomousTabData();
        this.gyro = robotContainer.gyro;
        this.drivetrain = robotContainer.drivetrain;
        this.intake = robotContainer.intake;
        this.climb = robotContainer.climb;
        this.ampAssist = robotContainer.ampAssist;
        this.flywheel = robotContainer.flywheel;
        this.index = robotContainer.index;
        this.pivot = robotContainer.pivot;
        this.candle = robotContainer.candle;
        this.shuttle = robotContainer.shuttle;


        // this.autonomousTabData = autonomousTabData;


        build();

        System.out.println(this);
        
        System.out.println("  Constructor Finished:  " + fullClassName);
    }


    // *** CLASS & INSTANCE METHODS ***

    /**
     * Builds the Autonomous Command List based on the Autonomous Tab selections
     */
    private void build()
    {
        // if(drivetrain != null)
        //     add(new StopDrive(drivetrain));

        pathPlannerString += autonomousTabData.startingSide;
        //pathPlannerString += autonomousTabData.scorePreload;
        pathPlannerString += autonomousTabData.shootDelay;
        pathPlannerString += autonomousTabData.driveDelay;
        pathPlannerString += autonomousTabData.scoreExtraNotes;
        pathPlannerString += autonomousTabData.sitPretty;
        System.out.println(pathPlannerString);
        
    }


    /**
     * Adds the command to the command group as well as the command string to display
     * @param command The command to add to the command group
     */
    private void add(Command command)
    {
        addCommands(command);
        commandString += command + "\n";
    }

    @Override
    public String toString()
    {
        return commandString;
    }


}
