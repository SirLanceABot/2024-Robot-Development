package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
// import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Pivot;
import frc.robot.sensors.Gyro4237;
import frc.robot.RobotContainer;
// import frc.robot.Constants.Pivot;
import frc.robot.subsystems.Shuttle;

public class SensorTab
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private ShuffleboardTab sensorTab = Shuffleboard.getTab("Sensor");
    private Gyro4237 gyro;
    private Drivetrain drivetrain;
    private Shuttle shuttle;
    private Pivot pivot;
    private Flywheel flyWheel;
    // private Index index;

    private GenericEntry gyroBox;
    private GenericEntry flsEncoderBox;
    private GenericEntry frsEncoderBox;
    private GenericEntry blsEncoderBox;
    private GenericEntry brsEncoderBox;
    private GenericEntry shuttleEncoderBox;
    private GenericEntry pivotEncoderBox;
    private GenericEntry flyWheelEncoderBox;
    // private GenericEntry indexEncoderBox;

    // *** CLASS CONSTRUCTOR ***
    SensorTab(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.drivetrain = robotContainer.drivetrain;
        this.gyro = robotContainer.gyro;
        this.shuttle = robotContainer.shuttle;
        this.pivot = robotContainer.pivot;
        this.flyWheel = robotContainer.flywheel;
        // this.index = robotContainer.index;

        if(drivetrain != null)
        {
            flsEncoderBox = createFrontLeftTurnEncoderBox();
            frsEncoderBox = createFrontRightTurnEncoderBox();
            blsEncoderBox = createBackLeftTurnEncoderBox();
            brsEncoderBox = createBackRightTurnEncoderBox();
        }

        if(gyro != null)
        {
            gyroBox = createGyroBox();
        }

        if(shuttle != null)
        {
            shuttleEncoderBox = createShuttleEncoderBox();
        }
        
        if(pivot != null)
        {
            pivotEncoderBox = createPivotEncoderBox();
        }

        if(flyWheel != null)
        {
            flyWheelEncoderBox = createFlyWheelEncoderBox();
        }

        // // if(index != null)
        // {
        //     // indexEncoderBox = createIndexEncoderBox();
        // }

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private GenericEntry createFrontLeftTurnEncoderBox()
    {
        return sensorTab.add("Front Left Turn Encoder", drivetrain.fls())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 2)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createFrontRightTurnEncoderBox()
    {
        return sensorTab.add("Front Right Turn Encoder", drivetrain.frs())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 4)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createBackLeftTurnEncoderBox()
    {
        return sensorTab.add("Back Left Turn Encoder", drivetrain.bls())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 6)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createBackRightTurnEncoderBox()
    {
        return sensorTab.add("Back Right Turn Encoder", drivetrain.brs())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 8)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createGyroBox()
    {
        return sensorTab.add("Gyro", gyro.getPitch())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(1, 9)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createShuttleEncoderBox()
    {
        return sensorTab.add("Shuttle", shuttle.getPosition())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(8, 5)  // sets position of widget
        .withSize(3, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createPivotEncoderBox()
    {
        return sensorTab.add("Pivot", pivot.getAngle())
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(8,7) // sets position of widget
        .withSize(3,2)  // sets size of widget
        .getEntry();
    }

    private GenericEntry createFlyWheelEncoderBox()
    {
        return sensorTab.add("Fly Wheel", flyWheel.getPosition())
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(8,10) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    public void updateEncoderData()
    {
        if(drivetrain != null)
        {
            flsEncoderBox.setDouble(drivetrain.fls());
            frsEncoderBox.setDouble(drivetrain.frs());
            blsEncoderBox.setDouble(drivetrain.bls());
            brsEncoderBox.setDouble(drivetrain.brs());
        }

        if(gyro != null)
        {
            gyroBox.setDouble(gyro.getPitch());
        }

        if(shuttle != null)
        {
            shuttleEncoderBox.setDouble(round(shuttle.getPosition(), 3));
        }

        if(pivot != null)
        {
            pivotEncoderBox.setDouble(round(pivot.getAngle(), 3));
        }

        if(flyWheel != null)
        {
            flyWheelEncoderBox.setDouble(round(flyWheel.getPosition(),3));
        }
    }

    public double round(double value, int digits)
    {
        double x = Math.pow(10.0, digits);
        return Math.round(value * x) / x;
    }
}