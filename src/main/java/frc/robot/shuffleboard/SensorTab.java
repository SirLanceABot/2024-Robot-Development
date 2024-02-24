package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
// import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Pivot;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Proximity;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.Intake;
import frc.robot.sensors.Proximity;

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
    private Flywheel flywheel;
    private Index index;
    private Intake intake;
    private Climb climb;
    private Proximity firstShuttleProximity;
    private Proximity secondShuttleProximity;
    private Proximity indexProximity;
    private Proximity indexWheelsProximity;

    private GenericEntry gyroBox;
    private GenericEntry fltEncoderBox;
    private GenericEntry frtEncoderBox;
    private GenericEntry bltEncoderBox;
    private GenericEntry brtEncoderBox;
    private GenericEntry shuttleEncoderBox;
    private GenericEntry pivotEncoderBox;
    private GenericEntry flywheelEncoderBox;
    private GenericEntry indexEncoderBox;
    private GenericEntry topIntakeEncoderBox;
    private GenericEntry bottomIntakeEncoderBox;
    private GenericEntry rightClimbEncoderBox;
    private GenericEntry leftClimbEncoderBox;
    private GenericEntry firstShuttleProximityBox;
    private GenericEntry secondShuttleProximityBox;
    private GenericEntry indexProximityBox;
    private GenericEntry indexWheelsProximityBox;
    private GenericEntry flyWheelVelocityBox;
    private GenericEntry indexVelocityBox;

    // *** CLASS CONSTRUCTOR ***
    SensorTab(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.drivetrain = robotContainer.drivetrain;
        this.gyro = robotContainer.gyro;
        this.shuttle = robotContainer.shuttle;
        this.pivot = robotContainer.pivot;
        this.flywheel = robotContainer.flywheel;
        this.index = robotContainer.index;
        this.intake = robotContainer.intake;
        this.climb = robotContainer.climb;
        // this.firstShuttleProximity = robotContainer.firstShuttleProximity;
        // this.secondShuttleProximity = robotContainer.secondShuttleProximity;
        // this.indexProximity = robotContainer.indexProximity;
        this.indexWheelsProximity = robotContainer.indexWheelsProximity; 
        

        if(drivetrain != null)
        {
            fltEncoderBox = createFrontLeftTurnEncoderBox();
            frtEncoderBox = createFrontRightTurnEncoderBox();
            bltEncoderBox = createBackLeftTurnEncoderBox();
            brtEncoderBox = createBackRightTurnEncoderBox();
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

        if(flywheel != null)
        {
            flywheelEncoderBox = createFlywheelEncoderBox();
            flyWheelVelocityBox = createFlywheelVelocityBox();

            
        }

        if(index != null)
        {
            indexEncoderBox = createIndexEncoderBox();
            indexVelocityBox = createIndexVelocityBox();
        }

        if(intake != null)
        {
            topIntakeEncoderBox = createTopIntakeEncoderBox();
            bottomIntakeEncoderBox = createBottomIntakeEncoderBox();
        }

        if(climb != null)
        {
            rightClimbEncoderBox = createRightClimbEncoderBox();
            leftClimbEncoderBox = createLeftClimbEncoderBox();
        }

        if(firstShuttleProximity != null)
        {

            firstShuttleProximityBox = createFirstShuttleProximitySensorBox();

        }

        if(secondShuttleProximity != null)
        {
            secondShuttleProximityBox = createSecondShuttleProximitySensorBox();
        }

        if(indexProximity != null)
        {
            indexProximityBox = createIndexProximitySensorBox();
        }

        if(indexWheelsProximity != null)
        {
            indexWheelsProximityBox = createIndexWheelsProximityBox();
        }

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private GenericEntry createFrontLeftTurnEncoderBox()
    {
        return sensorTab.add("Front Left Turn Encoder", round(drivetrain.flt(),3))
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 2)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createFrontRightTurnEncoderBox()
    {
        return sensorTab.add("Front Right Turn Encoder", round(drivetrain.frt(),3))
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 4)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createBackLeftTurnEncoderBox()
    {
        return sensorTab.add("Back Left Turn Encoder", round(drivetrain.blt(),3))
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 6)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createBackRightTurnEncoderBox()
    {
        return sensorTab.add("Back Right Turn Encoder", round(drivetrain.brt(),3))
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 8)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createGyroBox()
    {
        return sensorTab.add("Gyro", gyro.getPitch())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(3, 10)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createShuttleEncoderBox()
    {
        return sensorTab.add("Shuttle", round(shuttle.getPosition(),3))
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(1, 1)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createPivotEncoderBox()
    {
        return sensorTab.add("Pivot", round(pivot.getCANCoderAngle(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(1,11) // sets position of widget
        .withSize(4,2)  // sets size of widget
        .getEntry();
    }

    private GenericEntry createFlywheelEncoderBox()
    {
        return sensorTab.add("Fly Wheel", round(flywheel.getPosition(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(13,1) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    private GenericEntry createIndexEncoderBox()
    {
        return sensorTab.add("Index", round(index.getPosition(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(5,1) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    private GenericEntry createTopIntakeEncoderBox()
    {
        return sensorTab.add("Intake Top", round(intake.getTopPosition(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(9,1) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    private GenericEntry createBottomIntakeEncoderBox()
    {
        return sensorTab.add("Intake Bottom", round(intake.getBottomPosition(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(9,3) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    private GenericEntry createRightClimbEncoderBox()
    {
        return sensorTab.add("Right Climb", round(climb.getRightPosition(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(18,5) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    private GenericEntry createLeftClimbEncoderBox()
    {
        return sensorTab.add("Left Climb", round(climb.getLeftPosition(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(18,8) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }
    
    private GenericEntry createFirstShuttleProximitySensorBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();

        booleanBoxProperties.put("Color when detected", "Lime");
        booleanBoxProperties.put("Color when not detected", "Red");

        return sensorTab.add("First Shuttle Detected?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(1, 3)
             .withSize(4, 3)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private GenericEntry createSecondShuttleProximitySensorBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();

        booleanBoxProperties.put("Color when detected", "Lime");
        booleanBoxProperties.put("Color when detected", "Red");

        return sensorTab.add("Second Shuttle Detected?", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(1, 6)
            .withSize(4,3)
            .withProperties(booleanBoxProperties)
            .getEntry();
    }

    private GenericEntry createIndexProximitySensorBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();

        booleanBoxProperties.put("Color when detected", "Lime");
        booleanBoxProperties.put("Color when detected", "Red");
        
        return sensorTab.add("Index Detected?", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(5, 3)
            .withSize(4,3)
            .withProperties(booleanBoxProperties)
            .getEntry();
    }

    private GenericEntry createIndexWheelsProximityBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();

        booleanBoxProperties.put("Color when detected", "Lime");
        booleanBoxProperties.put("Color when detected","Red");

        return sensorTab.add("Index Wheels Detected?", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(5,6)
            .withSize(4,3)
            .withProperties(booleanBoxProperties)
            .getEntry();
    }

    private GenericEntry createFlywheelVelocityBox()
    {
        return sensorTab.add("Flywheel Velocity", round(flywheel.getVelocity(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(13,4) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    private GenericEntry createIndexVelocityBox()
    {
        return sensorTab.add("Index Velocity", round(index.getVelocity(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(5,9) // sets position of widget
        .withSize(4,2) //sets size of widget
        .getEntry();
    }

    public void updateSensorData()
    {
        if(drivetrain != null)
        {
            fltEncoderBox.setDouble(round(drivetrain.flt(),3));
            frtEncoderBox.setDouble(round(drivetrain.frt(),3));
            bltEncoderBox.setDouble(round(drivetrain.blt(),3));
            brtEncoderBox.setDouble(round(drivetrain.brt(),3));
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
            pivotEncoderBox.setDouble(round(pivot.getCANCoderAngle(), 3));
        }

        if(flywheel != null)
        {
            flywheelEncoderBox.setDouble(round(flywheel.getPosition(),3));
        }

        if(index != null)
        {
            indexEncoderBox.setDouble(round(index.getPosition(),3));
        }

        if(intake != null)
        {
            topIntakeEncoderBox.setDouble(round(intake.getTopPosition(),3));
            bottomIntakeEncoderBox.setDouble(round(intake.getBottomPosition(),3));
        }

        if(climb != null)
        {
            leftClimbEncoderBox.setDouble(round(climb.getLeftPosition(),3));
            rightClimbEncoderBox.setDouble(round(climb.getRightPosition(),3));
        }

        if(firstShuttleProximity != null)
        {
            if(firstShuttleProximity.isDetected())
            {
                firstShuttleProximityBox.setBoolean(true);
            }
            else
            {
                firstShuttleProximityBox.setBoolean(false);
            }
        }

        if(secondShuttleProximity != null)
        {
            if(secondShuttleProximity.isDetected())
            {
                secondShuttleProximityBox.setBoolean(true);
            }
            else
            {
                secondShuttleProximityBox.setBoolean(false);
            }
        }

        if(indexProximity != null)
        {
            if(indexProximity.isDetected())
            {
                indexProximityBox.setBoolean(true);

            }
            else
            {
                indexProximityBox.setBoolean(false);
            }
        }
        if(indexWheelsProximity != null)
        {
            if(indexWheelsProximity.isDetected())
            {
                indexWheelsProximityBox.setBoolean(true);
            }
            else
            {
                indexWheelsProximityBox.setBoolean(false);
            }
        }
    }

    public double round(double value, int digits)
    {
        double x = Math.pow(10.0, digits);
        return Math.round(value * x) / x;
    }
}