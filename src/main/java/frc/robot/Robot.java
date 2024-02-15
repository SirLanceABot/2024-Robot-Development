// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutoCommandList;
import frc.robot.motors.MotorController4237;
import frc.robot.shuffleboard.AutonomousTabData;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
        try {
            Class.forName("frc.robot.Constants"); // load and static initializers
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } 
    }

    
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final RobotContainer robotContainer = new RobotContainer();
    private AutonomousTabData autonomousTabData = null;
    private Command autonomousCommand = null;
    private TestMode testMode = null;
    

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * This class determines the actions of the robot, depending on the mode and state of the robot.
     * Use the default modifier so that new objects can only be constructed in the same package.
     */
    Robot()
    {}

    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    /**
     * This method runs when the robot first starts up.
     */
    @Override
    public void robotInit()
    {
        System.out.println("Robot Init");

        DataLogManager.start();

        // enableLiveWindowInTest(true);
    }

    /**
     * This method runs periodically (20ms) while the robot is powered on.
     */
    @Override
    public void robotPeriodic()
    {
        // Update all of the periodic inputs.
        PeriodicIO.readAllPeriodicInputs();
        
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
        
        // Run periodic tasks
        PeriodicTask.runAllPeriodicTasks();

        // Update all of the periodic outputs.
        PeriodicIO.writeAllPeriodicOutputs();
    }

    /**
     * This method runs one time after the driver station connects.
     */
    @Override
    public void driverStationConnected()
    {}

    /**
     * This method runs one time when the robot enters disabled mode.
     */
    @Override
    public void disabledInit()
    {
        System.out.println("Disabled Mode");
        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
        System.gc();
    }

    /**
     * This method runs periodically (20ms) during disabled mode.
     */
    @Override
    public void disabledPeriodic()
    {
        if(robotContainer.mainShuffleboard != null &&
            robotContainer.mainShuffleboard.autonomousTab != null)
        {
            // Check if there is new data on the Autonomous Tab (Send Data button is pressed)
            boolean isNewData = robotContainer.mainShuffleboard.autonomousTab.isNewData();

            if (isNewData)
            {
                // Create a copy of the Autonomous Tab Data that is on the Autonomous Tab
                autonomousTabData = new AutonomousTabData(robotContainer.mainShuffleboard.autonomousTab.getAutonomousTabData());
                System.out.println(autonomousTabData);
                
                // Create the Autonomous Command List that will be scheduled to run during autonomousInit()
                autonomousCommand = new AutoCommandList(robotContainer, autonomousTabData);

                // Reset the gyro, encoders, and any other sensors
                robotContainer.resetRobot();
            }
        }
    }

    /**
     * This method runs one time when the robot exits disabled mode.
     */
    @Override
    public void disabledExit()
    {
        if(robotContainer.mainShuffleboard != null)
        {
            if(robotContainer.driverController != null)
            {
                robotContainer.mainShuffleboard.setDriverControllerSettings();
            }
            if(robotContainer.operatorController != null)
            {
                robotContainer.mainShuffleboard.setOperatorControllerSettings();
            }
        }
    }

    /**
     * This method runs one time when the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit()
    {
        System.out.println("Autonomous Mode");

        // robotContainer.drivetrain.followPathCommand("Test")
        //     .withName("Follow Path Command")
        //     .schedule();
        
        // new PathPlannerAuto("3 Piece Auto").schedule();

        if(autonomousCommand != null)
        {
            autonomousCommand.schedule();
        }
    }

    /**
     * This method runs periodically (20ms) during autonomous mode.
     */
    @Override
    public void autonomousPeriodic()
    {}

        /**
     * This method runs one time when the robot exits autonomous mode.
     */
    @Override
    public void autonomousExit()
    {
        robotContainer.stopRobot();
    }

    /**
     * This method runs one time when the robot enters teleop mode.
     */
    @Override
    public void teleopInit()
    {
        System.out.println("Teleop Mode");

        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if(autonomousCommand != null)
        {
            autonomousCommand.cancel();
            autonomousCommand = null;
            autonomousTabData = null;
        }
    }

    /**
     * This method runs periodically (20ms) during teleop mode.
     */
    @Override
    public void teleopPeriodic()
    {
        if(!DriverStation.isFMSAttached())
        {
            if(robotContainer.mainShuffleboard != null && robotContainer.mainShuffleboard.sensorTab != null)
            {
                robotContainer.mainShuffleboard.sensorTab.updateEncoderData();
            }
        }
    }

    /**
     * This method runs one time when the robot exits teleop mode.
     */
    @Override
    public void teleopExit()
    {
        robotContainer.stopRobot();

        // Log all sticky faults.
        MotorController4237.logAllStickyFaults();
    }

    /**
     * This method runs one time when the robot enters test mode.
     */
    @Override
    public void testInit()
    {
        System.out.println("Test Mode");

        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();

        // Create a TestMode object to test one team members code.
        testMode = new TestMode(robotContainer);

        // pidTunerTab = new PIDTunerTab();

        testMode.init();
    }

    /**
     * This method runs periodically (20ms) during test mode.
     */
    @Override
    public void testPeriodic()
    {
        if(robotContainer.mainShuffleboard != null && robotContainer.mainShuffleboard.sensorTab != null)
        {
            robotContainer.mainShuffleboard.sensorTab.updateEncoderData();
        }
        testMode.periodic();
    }

    /**
     * This method runs one time when the robot exits test mode.
     */
    @Override
    public void testExit()
    {
        testMode.exit();

        // Set the TestMode object to null so that garbage collection will remove the object.
        testMode = null;
        
        robotContainer.stopRobot();
    }

    /**
     * This method runs one time when the robot enters simulation mode.
     */
    @Override
    public void simulationInit()
    {
        System.out.println("Simulation Mode");
    }

    /**
     * This method runs periodically (20ms) during simulation mode.
     */
    @Override
    public void simulationPeriodic()
    {}
}
