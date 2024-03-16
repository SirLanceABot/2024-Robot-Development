package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.commands.Commands4237;
import frc.robot.motors.CANSparkMax4237;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Pivot;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Proximity;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.Climb.TargetPosition;

public class SamTest implements Test
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    // private final Proximity prox1;
    // private final Proximity prox2;
    // private final Candle4237 candle;
    // private final Shuttle shuttle;
    // private final Index index;
    // private final Pivot pivot;
    // private final Flywheel flywheel;
    // private final Intake intake;
    private final Joystick joystick;
    // private final Drivetrain drivetrain;
    // private final Gyro4237 gyro;
    // private final Boolean isBlueAlliance;
    // private final Climb climb;
    private final CANSparkMax4237 swerveModule = new CANSparkMax4237(10, "rio", "swerve module");

    // *** CLASS CONSTRUCTOR ***
    public SamTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // prox1 = new Proximity(8);
        // prox2 = new Proximity(9);
        // candle = new Candle4237();
        joystick = new Joystick(0);
        // this.shuttle = robotContainer.shuttle;
        // this.index = robotContainer.index;
        // this.pivot = robotContainer.pivot;
        // this.flywheel = robotContainer.flywheel;
        // this.intake = robotContainer.intake;
        // this.drivetrain = robotContainer.drivetrain;
        // this.gyro = robotContainer.gyro;
        // this.isBlueAlliance = robotContainer.isBlueAlliance;
        // this.climb = robotContainer.climb;

        // SmartDashboard.putNumber("kP", 0.0);
        // SmartDashboard.putNumber("kI", 0.0);
        // SmartDashboard.putNumber("kD", 0.0);

        // SmartDashboard.putNumber("Left Encoder Value:", 0.0);
        // SmartDashboard.putNumber("Right Encoder Value:", 0.0);

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        // p = SmartDashboard.getNumber("kP", 0.0);
        // i = SmartDashboard.getNumber("kI", 0.0);
        // d = SmartDashboard.getNumber("kD", 0.0);
        // drivetrain.setPID(p, i, d);
        // System.out.println("P: " + p + " I: " + i + " D: " + d);
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // SmartDashboard.putNumber("Left Encoder Value:", climb.getLeftPosition());
        // SmartDashboard.putNumber("Right Encoder Value:", climb.getRightPosition());
        // if(prox1.isDetected() && prox2.isDetected())
        // {
        //     candle.setGreen(false);
        // }
        // else if(prox1.isDetected() || prox2.isDetected())
        // {
        //     candle.setRed(false);
        // }
        // else
        // {
        //     candle.stop();
        // }

        // if(joystick.getRawButton(3))    //X
        // {
        //     flywheel.intake();
        //     index.intake();
        //     // shuttle.moveDownward();
        //     // flywheel.shoot(0.5);
        // }

        // SmartDashboard.putNumber("Current Yaw", robotContainer.gyro.getYaw());
        if(joystick.getRawButton(1))    //A
        {
            swerveModule.set(1.0);
            // Commands4237.rotateToSpeakerCommand().schedule();
            // flywheel.intake();
            // index.intake();
            // shuttle.moveDownward();
            // flywheel.shoot(80);
            // index.setVelocity(1);
            // intake.pickupFront();
            // while(!drivetrain.isAlligned(drivetrain.getAngleToBlueSpeaker()).getAsBoolean())
            // {
            
            // drivetrain.rotateToBlueSpeaker();
                // System.out.println(drivetrain.isAlligned(drivetrain.getAngleToBlueSpeaker()).getAsBoolean());
            // }
            // climb.extend(0.1);
            // climb.moveToSetPosition(TargetPosition.kChain);
        }
        else if(joystick.getRawButton(2))   //B
        {
            // System.out.println(robotContainer.drivetrain.getAngleToRedSpeaker());
            // climb.retract(0.1);
            // climb.moveToSetPosition(TargetPosition.kRobot);
        }
        else if(joystick.getRawButton(3))   //X
        {
            // System.out.println(robotContainer.drivetrain.getAngleToBlueSpeaker());
        }
        else
        {
            swerveModule.set(0);
            // climb.stop();
        }

        // System.out.println(robotContainer.isBlueAlliance);
        // SmartDashboard.putNumber("Gyro Yaw", gyro.getYaw());
        // else if(joystick.getRawButton(2))   //B
        // {
        //     // flywheel.stop();
        //     // index.stop();
        //     // shuttle.stop();
        //     // pivot.stopMotor();
        //     intake.pickupBack();
            // System.out.println(drivetrain.getAngleToBlueSpeaker());
        // }
        // else
        // {
        //     intake.stop();
        // }
        // if(joystick.getRawButton(2))   //B
        // {
        //     // index.setVelocity(1);
        //     intake.pickupFront();
        // }
        // // else if(joystick.getRawButton(4))    //Y
        // // // {
        // // // //     flywheel.shoot(0.1);
        // // // //     index.setVelocity(0.1);
        // // // //     shuttle.moveUpward();
        // // // }
        // else
        // {
        // //     // flywheel.stop();
        //     index.stop();
        // //     // shuttle.stop();
        // //     // pivot.stopMotor();
        // }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        // candle.stop();
        // flywheel.stop();
        // index.stop();
        // shuttle.stop();
        // pivot.stopMotor();
        // intake.stop();
        // climb.stop();
    }

    // *** METHODS ***
    // Put any additional methods here.

        
}
