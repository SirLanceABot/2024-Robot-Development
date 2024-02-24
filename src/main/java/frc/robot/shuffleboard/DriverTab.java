package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.IntakePositioning;
import frc.robot.subsystems.Pivot;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Proximity;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shuttle;
import frc.robot.subsystems.Intake;
import frc.robot.sensors.Proximity;

public class DriverTab
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
   // private static PoseEstimator poseEstimator = new PoseEstimator<>(null, null, null, null);
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private ShuffleboardTab driverTab = Shuffleboard.getTab("Driver");
    private final Field2d field = new Field2d();
    private Drivetrain drivetrain;
    private Pivot pivot;
    private Intake intake;
    private IntakePositioning intakePositioning;
    //private PoseEstimator poseEstimator;

    private String intakeString;
    
    private GenericEntry pivotAngleBox;
    private GenericEntry intakeStatusBox;
 

    // *** CLASS CONSTRUCTOR ***
    DriverTab(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        createFieldBox();
        // createPivotAngleBox();
        // createIntakeStatusBox();
    
        this.pivot = robotContainer.pivot;
        this.intake = robotContainer.intake;
        this.intakePositioning = robotContainer.intakePositioning;
        this.drivetrain = robotContainer.drivetrain;
        

        
        if(pivot != null)
        {
            pivotAngleBox = createPivotAngleBox();
        }

        if(intakePositioning != null)
        {
            intakeStatusBox = createIntakeStatusBox();
        }



        System.out.println("  Constructor Finished: " + fullClassName);
    }
 
    private void createFieldBox()
    {
        field.setRobotPose(drivetrain.getEstimatedPose());
        driverTab.add("Field", field);
        // SmartDashboard.putData("Field", field);
    }

    private GenericEntry createPivotAngleBox()
    {
        return driverTab.add("Pivot Angle", round(pivot.getCANCoderAngle(),3))
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(1,2) // sets position of widget
        .withSize(4,2)  // sets size of widget
        .getEntry();
    }


    private GenericEntry createIntakeStatusBox()
    {
    
        if(intakePositioning.isIntakeUp())
        {
            intakeString = "Up";
        }
        else if (intakePositioning.isIntakeDown())
        {
            intakeString = "Down";
        }

        return driverTab.add("Intake Position: ", intakeString)
        .withWidget(BuiltInWidgets.kTextView) //specifies type of widget: "kTextView"
        .withPosition(5,8) // sets position of widget
        .withSize(4,2)  // sets size of widget
        .getEntry();
            
    }

    
    public void updateData()
    {

        // if(pivot != null)
        // {
        //     pivotAngleBox.setDouble(round(pivot.getCANCoderAngle(), 3));
        // }

        if(intake != null)
        {
            intakeStatusBox.setString(intakeString);
        }
     
    }

    public double round(double value, int digits)
    {
        double x = Math.pow(10.0, digits);
        return Math.round(value * x) / x;
    }
}