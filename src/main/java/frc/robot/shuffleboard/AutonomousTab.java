package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.util.sendable.SendableRegistry;


public class AutonomousTab 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private enum ButtonState
    {
        kPressed, kStillPressed, kReleased, kStillReleased
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Create a Shuffleboard Tab
    private ShuffleboardTab autonomousTab = Shuffleboard.getTab("Autonomous");

    private final AutonomousTabData autonomousTabData = new AutonomousTabData();
  
    // Create the Box objects
    private SendableChooser<AutonomousTabData.StartingLocation> startingLocationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ContainingPreload> containingPreloadBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.PlayPreload> playPreloadBox = new SendableChooser<>();
    private GenericEntry successfulDownload;
    private GenericEntry errorMessageBox;

    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    private ButtonState previousButtonState = ButtonState.kStillReleased;
    private boolean isDataValid = true;
    private String errorMessage = "No Errors";

    // *** CLASS CONSTRUCTOR ***
    AutonomousTab()
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        createStartingLocationBox();
        createPlayPreloadBox();
        createContainingPreloadBox();
        
        createSendDataButton();
        successfulDownload = createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        errorMessageBox = createErrorMessageBox();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // *** CLASS & INSTANCE METHODS ***

    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createStartingLocationBox()
    {
        //create and name the Box
        SendableRegistry.add(startingLocationBox, "Starting Location");
        SendableRegistry.setName(startingLocationBox, "Starting Location");
        
        //add options to  Box
        startingLocationBox.setDefaultOption("Left", AutonomousTabData.StartingLocation.kLeft);
        startingLocationBox.addOption("Middle", AutonomousTabData.StartingLocation.kMiddle);
        startingLocationBox.addOption("Right", AutonomousTabData.StartingLocation.kRight);

        //put the widget on the shuffleboard
        autonomousTab.add(startingLocationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 3)
            .withSize(5, 2);
    }

    /**
    * <b>Containing Preload</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createContainingPreloadBox()
    {
        //create and name the Box
        SendableRegistry.add(containingPreloadBox, "Containing Preload?");
        SendableRegistry.setName(containingPreloadBox, "Containing Preload?");

        //add options to Box
        containingPreloadBox.addOption("No", AutonomousTabData.ContainingPreload.kNo);
        containingPreloadBox.setDefaultOption("Yes", AutonomousTabData.ContainingPreload.kYes);
        
        //put the widget on the shuffleboard
        autonomousTab.add(containingPreloadBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(10, 0)
            .withSize(4, 3);
    }

    /**
    * <b>Play Preload</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPlayPreloadBox()
    {
        //create and name the Box
        SendableRegistry.add(playPreloadBox, "Play Preload?");
        SendableRegistry.setName(playPreloadBox, "Play Preload?");
        
        //add options to  Box
        playPreloadBox.addOption("No", AutonomousTabData.PlayPreload.kNo);
        playPreloadBox.setDefaultOption("Yes", AutonomousTabData.PlayPreload.kYes);
        

        //put the widget on the shuffleboard
        autonomousTab.add(playPreloadBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(14, 0)
            .withSize(4, 3);
    }

    /**
     * <b>Send Data</b> Button
     * <p>Create an entry in the Network Table and add the Button to the Shuffleboard Tab
     */
    private void createSendDataButton()
    {
        SendableRegistry.add(sendDataButton, "Send Data");
        SendableRegistry.setName(sendDataButton, "Send Data");

        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);

        autonomousTab.add(sendDataButton)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(20, 8)
            .withSize(4, 4);
    }

    /**
    * <b>Successful Download</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private GenericEntry createSuccessfulDownloadBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();
    
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        return autonomousTab.add("Successful Download?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(24, 8)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    /**
    * <b>Error Message</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private GenericEntry createErrorMessageBox()
    {
         return autonomousTab.add("Error Messages", "No Errors")
             .withWidget(BuiltInWidgets.kTextView)
             .withPosition(0, 10)
             .withSize(20, 2)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.startingLocation = startingLocationBox.getSelected();
        autonomousTabData.containingPreload = containingPreloadBox.getSelected();
        autonomousTabData.playPreload = playPreloadBox.getSelected();
    }

    // FIXME check this again
    public boolean isNewData()
    {
        boolean isNewData = false;
        boolean isSendDataButtonPressed = sendDataButton.getSelected();

        switch(previousButtonState)
        {
            case kStillReleased:
                if(isSendDataButtonPressed)
                {
                    previousButtonState = ButtonState.kPressed;
                }
                break;

            case kPressed:
                updateIsDataValidAndErrorMessage();
                if(isDataValid)
                {
                    successfulDownload.setBoolean(true);
                    updateAutonomousTabData();
                    isNewData = true;
                    // errorMessageBox.setString(errorMessage);
                }
                else
                {
                    successfulDownload.setBoolean(false);
                    DriverStation.reportWarning(errorMessage, false);
                    // errorMessageBox.setString(errorMessage);
                }
                previousButtonState = ButtonState.kStillPressed;
                break;

            case kStillPressed:
                if(!isSendDataButtonPressed)
                {
                    previousButtonState = ButtonState.kReleased;
                }
                break;

            case kReleased:
                previousButtonState = ButtonState.kStillReleased;
                break;
        }

        return isNewData;
    }

    public AutonomousTabData getAutonomousTabData()
    {
        return autonomousTabData;
    }

    public void updateIsDataValidAndErrorMessage()
    {
        errorMessage = "No Errors";
        String msg = "";
        boolean isValid = true;
        
        boolean isContainingPreload = (containingPreloadBox.getSelected() == AutonomousTabData.ContainingPreload.kYes);
        boolean isPlayPreload = (playPreloadBox.getSelected() == AutonomousTabData.PlayPreload.kYes);
        
        if(!isContainingPreload && isPlayPreload)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        // Do NOT remove any of the remaining code
        // Check if the selections are valid
        if(!isValid)
            errorMessage = msg;
        
        // Displays either "No Errors" or the error messages
        errorMessageBox.setString(errorMessage);

        // 
        isDataValid = isValid;

    }   
}
