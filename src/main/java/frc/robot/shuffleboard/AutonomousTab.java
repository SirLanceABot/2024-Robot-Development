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
    // private SendableChooser<AutonomousTabData.ContainingPreload> containingPreloadBox = new SendableChooser<>();
    //private SendableChooser<AutonomousTabData.ScorePreload> scorePreloadBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.DriveOutOfStartZone> driveOutOfStartZoneBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ShootDelay> shootDelayBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.DriveDelay> driveDelayBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.PickupSecondNote> pickupNotesBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ScoreMoreNotes> scoreMoreNotesBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.SitPretty> sitPrettyBox = new SendableChooser<>();

    

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
        // createScorePreloadBox();
        // createContainingPreloadBox();
        // createDriveOutOfStartZoneBox();
        createShootDelayBox();
        createDriveDelayBox();
        // createPickupNotesBox();
        createScoreMoreNotesBox();
        createSitPrettyBox();
    
        
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
            .withPosition(1, 1)
            .withSize(5, 3);
    }

    /**
    * <b>Drive Out of Start Zone box</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private void createDriveOutOfStartZoneBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(driveOutOfStartZoneBox, "Drive Out Of Start Zone?");
    //     SendableRegistry.setName(driveOutOfStartZoneBox, "Drive Out Of Start Zone?");
        
    //     //add options to  Box
    //     driveOutOfStartZoneBox.addOption("No", AutonomousTabData.DriveOutOfStartZone.kNo);
    //     driveOutOfStartZoneBox.setDefaultOption("Yes", AutonomousTabData.DriveOutOfStartZone.kYes);
        

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(driveOutOfStartZoneBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(6, 1)
    //         .withSize(4, 3);
    // }

    /**
    * <b>Containing Preload</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private void createContainingPreloadBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(containingPreloadBox, "Containing Preload?");
    //     SendableRegistry.setName(containingPreloadBox, "Containing Preload?");

    //     //add options to Box
    //     containingPreloadBox.addOption("No", AutonomousTabData.ContainingPreload.kNo);
    //     containingPreloadBox.setDefaultOption("Yes", AutonomousTabData.ContainingPreload.kYes);
        
    //     //put the widget on the shuffleboard
    //     autonomousTab.add(containingPreloadBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(1,5)
    //         .withSize(4, 3);
    // }

    /**
    * <b>Score Preload</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private void createScorePreloadBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(scorePreloadBox, "Score Preload?");
    //     SendableRegistry.setName(scorePreloadBox, "Score Preload?");
        
    //     //add options to  Box
    //     scorePreloadBox.addOption("No", AutonomousTabData.ScorePreload.kNo);
    //     scorePreloadBox.setDefaultOption("Yes", AutonomousTabData.ScorePreload.kYes);
        

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(scorePreloadBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(1, 5)
    //         .withSize(5, 3);
    // }

    /**
    * <b>Shoot Delay</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createShootDelayBox()
    {
        //create and name the Box
        SendableRegistry.add(shootDelayBox, "Shoot Delay");
        SendableRegistry.setName(shootDelayBox, "Shoot Delay");
        
        //add options to  Box
        shootDelayBox.setDefaultOption("Zero", AutonomousTabData.ShootDelay.k0);
        // shootDelayBox.addOption("One", AutonomousTabData.ShootDelay.k1);
        // shootDelayBox.addOption("Two", AutonomousTabData.ShootDelay.k2);
        shootDelayBox.addOption("Three", AutonomousTabData.ShootDelay.k3);
        // shootDelayBox.addOption("Four", AutonomousTabData.ShootDelay.k4);
        // shootDelayBox.addOption("Five", AutonomousTabData.ShootDelay.k5);

        //put the widget on the shuffleboard
        autonomousTab.add(shootDelayBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(11, 1)
            .withSize(5, 3);
    }

    /**
    * <b>Drive Delay</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createDriveDelayBox()
    {
        //create and name the Box
        SendableRegistry.add(driveDelayBox, "Drive Delay");
        SendableRegistry.setName(driveDelayBox, "Drive Delay");
        
        //add options to  Box
        driveDelayBox.setDefaultOption("Zero", AutonomousTabData.DriveDelay.k0);
        // driveDelayBox.addOption("One", AutonomousTabData.DriveDelay.k1);
        // driveDelayBox.addOption("Two", AutonomousTabData.DriveDelay.k2);
        driveDelayBox.addOption("Three", AutonomousTabData.DriveDelay.k3);
        // driveDelayBox.addOption("Four", AutonomousTabData.DriveDelay.k4);
        // driveDelayBox.addOption("Five", AutonomousTabData.DriveDelay.k5);

        //put the widget on the shuffleboard
        autonomousTab.add(driveDelayBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 1)
            .withSize(5, 3);
    }

    // private void createPickupNotesBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(pickupNotesBox, "Pickup Second Note?");
    //     SendableRegistry.setName(pickupNotesBox, "Pickup Second Note?");
        
    //     //add options to  Box
    //     pickupNotesBox.addOption("No", AutonomousTabData.PickupSecondNote.kNo);
    //     pickupNotesBox.setDefaultOption("Yes", AutonomousTabData.PickupSecondNote.kYes);
        

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(pickupNotesBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(21, 1)
    //         .withSize(4, 3);
    // }


    private void createScoreMoreNotesBox()
    {
        //create and name the Box
        SendableRegistry.add(scoreMoreNotesBox, "Score How Many Extra Notes?");
        SendableRegistry.setName(scoreMoreNotesBox, "Score How Many Extra Notes?");
        
        //add options to  Box
        scoreMoreNotesBox.addOption("0", AutonomousTabData.ScoreMoreNotes.k0);
        scoreMoreNotesBox.setDefaultOption("1", AutonomousTabData.ScoreMoreNotes.k1);
        scoreMoreNotesBox.addOption("2",AutonomousTabData.ScoreMoreNotes.k2);
        scoreMoreNotesBox.addOption("3", AutonomousTabData.ScoreMoreNotes.k3);
        

        //put the widget on the shuffleboard
        autonomousTab.add(scoreMoreNotesBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 5)
            .withSize(5, 3);
    }

    private void createSitPrettyBox()
    {
        //create and name the Box
        SendableRegistry.add(sitPrettyBox, "Sit Pretty");
        SendableRegistry.setName(sitPrettyBox, "Sit Pretty");
        
        //add options to  Box
        sitPrettyBox.setDefaultOption("No", AutonomousTabData.SitPretty.kNo);
        sitPrettyBox.addOption("Yes", AutonomousTabData.SitPretty.kYes);

        //put the widget on the shuffleboard
        autonomousTab.add(sitPrettyBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 5)
            .withSize(5, 3);
    }

    /**
     * <b>Send Data</b> Button
     * <p>Create an entry in the Network Table and add the Button to the Shuffleboard Tab
     */
    private void createSendDataButton()
    {
        SendableRegistry.add(sendDataButton, "Send Data");
        SendableRegistry.setName(sendDataButton, "Send Data");

        sendDataButton.setDefaultOption("Yes", true);
        sendDataButton.addOption("No", false);

        autonomousTab.add(sendDataButton)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(19, 5)
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
             .withPosition(19, 1)
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
             .withPosition(1, 10)
             .withSize(22, 3)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.startingLocation = startingLocationBox.getSelected();
        // autonomousTabData.driveOutOfStartZone = driveOutOfStartZoneBox.getSelected();
        // autonomousTabData.containingPreload = containingPreloadBox.getSelected();
        //autonomousTabData.scorePreload = scorePreloadBox.getSelected();
        autonomousTabData.driveDelay = driveDelayBox.getSelected();
        autonomousTabData.shootDelay = shootDelayBox.getSelected();
        // autonomousTabData.pickupSecondNote = pickupNotesBox.getSelected();
        autonomousTabData.scoreMoreNotes = scoreMoreNotesBox.getSelected();
        
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
        
        // boolean isContainingPreload = (containingPreloadBox.getSelected() == AutonomousTabData.ContainingPreload.kYes);
        // boolean isScorePreload = (scorePreloadBox.getSelected() == AutonomousTabData.ScorePreload.kYes);
        boolean isShootDelay = 
        //shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k0 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k1 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k2 ||
        ( shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k3 );
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k4 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k5 )
        // boolean isPickupSecondNote = (pickupNotesBox.getSelected() == AutonomousTabData.PickupSecondNote.kYes);
        boolean isScoreMoreNotes = 
        (scoreMoreNotesBox.getSelected() == AutonomousTabData.ScoreMoreNotes.k0 ||
         scoreMoreNotesBox.getSelected() == AutonomousTabData.ScoreMoreNotes.k1 ||
         scoreMoreNotesBox.getSelected() == AutonomousTabData.ScoreMoreNotes.k2 ||
         scoreMoreNotesBox.getSelected() == AutonomousTabData.ScoreMoreNotes.k3);
        boolean isDriveDelay = 
        // (driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k0 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k1 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k2 ||
         (driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k3 );
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k4 ||
        //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k5 )
    



        // if(!isContainingPreload && isScorePreload) :)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ] - Cannot Score without containing Preload \n";

        // }

        // if(!isPickupSecondNote && isScoreSecondNote)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ] - Cannot Score Second Note without Picking It up \n";

        // }

        
        // if(isShootDelay && isScorePreload) :)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ] - Cannot Score without containing Preload \n";

        // }

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
