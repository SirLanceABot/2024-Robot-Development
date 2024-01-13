package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import frc.robot.controls.DriverController;
import frc.robot.controls.Xbox;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class DriverControllerTab 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    private class AxisObjects
    {
        private GenericEntry deadzoneEntry;
        private GenericEntry minOutputEntry;
        private GenericEntry maxOutputEntry;
        private SendableChooser<Boolean> isFlipped = new SendableChooser<>();
        private SendableChooser<DriverController.AxisScale> axisScaleComboBox = new SendableChooser<>();
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private final AxisObjects leftXObjects = new AxisObjects();
    private final AxisObjects leftYObjects = new AxisObjects();
    private final AxisObjects rightXObjects = new AxisObjects();
    private final AxisObjects rightYObjects = new AxisObjects();
    private final AxisObjects leftTriggerObjects = new AxisObjects();
    private final AxisObjects rightTriggerObjects = new AxisObjects();

    // private final AxisObjects rightYObjects = new AxisObjects();

    private final DriverController driverController;
    private ShuffleboardTab driverControllerTab = Shuffleboard.getTab("Driver Controller");


    // *** CLASS CONSTRUCTOR ***
    DriverControllerTab( DriverController driverController)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.driverController = driverController;
        initDriverControllerTab();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // *** CLASS & INSTANCE METHODS ***
    private void initDriverControllerTab()
    {
        createAxisWidgets(Xbox.Axis.kLeftX, "Left X", leftXObjects, 0);
        createAxisWidgets(Xbox.Axis.kLeftY, "Left Y", leftYObjects, 5);
        createAxisWidgets(Xbox.Axis.kRightX, "Right X", rightXObjects, 10);
        createAxisWidgets(Xbox.Axis.kRightY, "Right Y", rightYObjects, 15);
        createAxisWidgets(Xbox.Axis.kLeftTrigger, "Left Trigger", leftTriggerObjects, 20);
        createAxisWidgets(Xbox.Axis.kRightTrigger, "Right Trigger", rightTriggerObjects, 24);
        // createAxisWidgets(DriverController.DriverAxisAction.kRightY, "", rightYObjects, 15);
    }

    private void createAxisWidgets(Xbox.Axis axis, String name, AxisObjects axisObjects, int column)
    {
        int row = 0;
        int width = 4;
        int height = 2;

        // Get the current axis settings on the Driver Controller for the given axis
        DriverController.AxisSettings axisSettings = driverController.new AxisSettings();
        axisSettings = driverController.getAxisSettings(axis);

        // Create the text box to set the deadzone of the axis
        axisObjects.deadzoneEntry = createTextBox(name + " Deadzone", Double.toString(axisSettings.axisDeadzone), column, row, width, height);
        
        //Create the text box to set the min output of the axis
        row += 2;
        axisObjects.minOutputEntry = createTextBox(name + " Min Output", Double.toString(axisSettings.axisMinOutput), column, row, width, height);

        // Create the text box to set the max output of the axis
        row += 2;
        axisObjects.maxOutputEntry = createTextBox(name + " Max Output", Double.toString(axisSettings.axisMaxOutput), column, row, width, height);

        // Create the button to flip the axis (swap negative and positive)
        row += 2;
        createSplitButtonChooser(axisObjects.isFlipped, name + " Is Flipped", axisSettings.axisIsFlipped, column, row, width, height);

        // Create the combo box to set the axis scale
        row += 3;
        createComboBox(axisObjects.axisScaleComboBox, name + " Axis Scale", axisSettings.axisScale, column, row, width, height);
    }

    /**
    * Create a <b>Text Box</b>
    * <p>Create an entry in the Network Table and add the Text Box to the Shuffleboard Tab
    */
    private GenericEntry createTextBox(String title, String defaultValue, int column, int row, int width, int height)
    {
        return driverControllerTab.add(title, defaultValue)
            .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
            .withPosition(column, row)  // sets position of widget
            .withSize(width, height)    // sets size of widget
            .getEntry();
    }

    /**
    * Create a <b>Combo Box</b>
    * <p>Create an entry in the Network Table and add the Combo Box to the Shuffleboard Tab
    */
    private void createComboBox(SendableChooser<DriverController.AxisScale> comboBox, String title, DriverController.AxisScale defaultValue, int column, int row, int width, int height)
    {
        SendableRegistry.add(comboBox, title);
        SendableRegistry.setName(comboBox, title);

        for(DriverController.AxisScale axisScale: DriverController.AxisScale.values())
        {
            if(axisScale == defaultValue)
            {
                comboBox.setDefaultOption(axisScale.toString(), axisScale);
            }
            else
            {
                comboBox.addOption(axisScale.toString(), axisScale);
            }
        }

        driverControllerTab.add(comboBox)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(column, row)
            .withSize(width, height);
    }

    private void createSplitButtonChooser(SendableChooser<Boolean> splitButtonChooser, String title, boolean defaultValue, int column, int row, int width, int height)
    {
        SendableRegistry.add(splitButtonChooser, title);
        SendableRegistry.setName(splitButtonChooser, title);

        splitButtonChooser.setDefaultOption((defaultValue ? "Yes" : "No"), defaultValue);
        splitButtonChooser.addOption((!defaultValue ? "Yes" : "No"), !defaultValue);

        driverControllerTab.add(splitButtonChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(column, row)
            .withSize(width, height);
    }

    private DriverController.AxisSettings getAxisSettingsFromShuffleboard(AxisObjects axisObjects)
    {
        DriverController.AxisSettings axisSettings = driverController.new AxisSettings();

        // These MUST be read from the shuffleboard as a String, then converted to a Double because it is a "Text Box"
        axisSettings.axisDeadzone = Double.valueOf(axisObjects.deadzoneEntry.getString("0.1"));
        axisSettings.axisMinOutput = Double.valueOf(axisObjects.minOutputEntry.getString("0.0"));
        axisSettings.axisMaxOutput = Double.valueOf(axisObjects.maxOutputEntry.getString("1.0"));
        
        axisSettings.axisIsFlipped = axisObjects.isFlipped.getSelected();
        axisSettings.axisScale = axisObjects.axisScaleComboBox.getSelected();

        // // System.out.println(axisSettings);
        return axisSettings;
    }

    public void setDriverControllerAxisSettings()
    {
        DriverController.AxisSettings axisSettings = driverController.new AxisSettings();

        axisSettings = getAxisSettingsFromShuffleboard(leftXObjects);
        driverController.setAxisSettings(Xbox.Axis.kLeftX, axisSettings);

        axisSettings = getAxisSettingsFromShuffleboard(leftYObjects);
        driverController.setAxisSettings(Xbox.Axis.kLeftY, axisSettings);

        axisSettings = getAxisSettingsFromShuffleboard(rightXObjects);
        driverController.setAxisSettings(Xbox.Axis.kRightX, axisSettings);

        axisSettings = getAxisSettingsFromShuffleboard(rightYObjects);
        driverController.setAxisSettings(DriverController.Axis.kRightY, axisSettings);

        axisSettings = getAxisSettingsFromShuffleboard(leftTriggerObjects);
        driverController.setAxisSettings(DriverController.Axis.kLeftTrigger, axisSettings);

        axisSettings = getAxisSettingsFromShuffleboard(rightTriggerObjects);
        driverController.setAxisSettings(DriverController.Axis.kRightTrigger, axisSettings);
    }
}
