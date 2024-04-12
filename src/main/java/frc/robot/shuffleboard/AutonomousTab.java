package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;

// import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.commands.AutoCommandList;
// import frc.robot.RobotContainer;

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
    // Create the Autonomous Command List that will be scheduled to run during autonomousInit()
    // private Command autonomousCommand = null;
    // private RobotContainer robotContainer;
    
    
  
    // Create the Box objects
    private SendableChooser<AutonomousTabData.StartingSide> startingSideBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.ContainingPreload> containingPreloadBox = new SendableChooser<>();
    //private SendableChooser<AutonomousTabData.ScorePreload> scorePreloadBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.DriveOutOfStartZone> driveOutOfStartZoneBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.ShootDelay> shootDelayBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.DriveDelay> driveDelayBox = new SendableChooser<>();
    // private SendableChooser<AutonomousTabData.PickupSecondNote> pickupNotesBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ScoreExtraNotes> scoreExtraNotesBox = new SendableChooser<>();
    private static SendableChooser<AutonomousTabData.StagePositioning> stageBox = new SendableChooser<>();
    private static SendableChooser<AutonomousTabData.SitPretty> sitPrettyBox = new SendableChooser<>();


    

    private GenericEntry successfulDownload;
    private GenericEntry doesAutonomousExist;
    private GenericEntry errorMessageBox;
    private GenericEntry autonomousNameBox;
    

    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();

 
    private ButtonState previousButtonState = ButtonState.kStillReleased;
    // private boolean isDataValid = true;
    // private boolean isAutoValid = true;
    // private String errorMessage = "No Errors";
    public String autonomousName = "";

    // *** CLASS CONSTRUCTOR ***
    AutonomousTab()
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        // this.robotContainer = robotContainer;
        createStartingSideBox();
        createSitPrettyBox();
        createStageBox();
        createScoreMoreNotesBox();
        // createScorePreloadBox();
        // createContainingPreloadBox();
        // createDriveOutOfStartZoneBox();
        // createShootDelayBox();
        // createDriveDelayBox();
        // createPickupNotesBox();

    
        
        createSendDataButton();
        successfulDownload = createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        doesAutonomousExist = createDoesAutonomousExistBox();
        doesAutonomousExist.setBoolean(false);

        errorMessageBox = createErrorMessageBox();
        autonomousNameBox = createAutonomousNameBox();
    
        

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // *** CLASS & INSTANCE METHODS ***

    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createStartingSideBox()
    {
        //create and name the Box
        SendableRegistry.add(startingSideBox, "Starting Side");
        SendableRegistry.setName(startingSideBox, "Starting Side");
        
        //add options to  Box
        startingSideBox.setDefaultOption("Amp", AutonomousTabData.StartingSide.kAmp);
        startingSideBox.addOption("Sub", AutonomousTabData.StartingSide.kSub);
        startingSideBox.addOption("Source", AutonomousTabData.StartingSide.kSource);

        //put the widget on the shuffleboard
        autonomousTab.add(startingSideBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 1)
            .withSize(5, 3);
    }

    {
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
    // private void createShootDelayBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(shootDelayBox, "Shoot Delay (Sec)");
    //     SendableRegistry.setName(shootDelayBox, "Shoot Delay (Sec)");
        
    //     //add options to  Box
    //     shootDelayBox.setDefaultOption("Zero", AutonomousTabData.ShootDelay.k0);
    //     // shootDelayBox.addOption("One", AutonomousTabData.ShootDelay.k1);
    //     // shootDelayBox.addOption("Two", AutonomousTabData.ShootDelay.k2);
    //     shootDelayBox.addOption("Three", AutonomousTabData.ShootDelay.k3);
    //     // shootDelayBox.addOption("Four", AutonomousTabData.ShootDelay.k4);
    //     // shootDelayBox.addOption("Five", AutonomousTabData.ShootDelay.k5);

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(shootDelayBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(11, 1)
    //         .withSize(5, 3);
    // }

    /**
    * <b>Drive Delay</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private void createDriveDelayBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(driveDelayBox, "Drive Delay (Sec)");
    //     SendableRegistry.setName(driveDelayBox, "Drive Delay (Sec)");
        
    //     //add options to  Box
    //     driveDelayBox.setDefaultOption("Zero", AutonomousTabData.DriveDelay.k0);
    //     // driveDelayBox.addOption("One", AutonomousTabData.DriveDelay.k1);
    //     // driveDelayBox.addOption("Two", AutonomousTabData.DriveDelay.k2);
    //     driveDelayBox.addOption("Three", AutonomousTabData.DriveDelay.k3);
    //     // driveDelayBox.addOption("Four", AutonomousTabData.DriveDelay.k4);
    //     // driveDelayBox.addOption("Five", AutonomousTabData.DriveDelay.k5);

    //     //put the widget on the shuffleboard
    //     autonomousTab.add(driveDelayBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(6, 1)
    //         .withSize(5, 3);
    // }

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

    }
    private void createScoreMoreNotesBox()
    {
        //create and name the Box
        SendableRegistry.add(scoreExtraNotesBox, "Score How Many Extra Notes?");
        SendableRegistry.setName(scoreExtraNotesBox, "Score How Many Extra Notes?");
        
        //add options to  Box
        scoreExtraNotesBox.addOption("0", AutonomousTabData.ScoreExtraNotes.k0);
        scoreExtraNotesBox.setDefaultOption("1", AutonomousTabData.ScoreExtraNotes.k1);
        scoreExtraNotesBox.addOption("2", AutonomousTabData.ScoreExtraNotes.k2);
        scoreExtraNotesBox.addOption("3", AutonomousTabData.ScoreExtraNotes.k3);
        scoreExtraNotesBox.addOption("4", AutonomousTabData.ScoreExtraNotes.k4);
        

        //put the widget on the shuffleboard
        autonomousTab.add(scoreExtraNotesBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 1)
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
            .withPosition(11, 1)
            .withSize(3, 3);
    }

    private void createStageBox()
    {
        //create and name the box
        SendableRegistry.add(stageBox,"Stage Positioning");
        SendableRegistry.setName(stageBox,"Stage  Positioning");

        //add options to Box
        stageBox.setDefaultOption("None", AutonomousTabData.StagePositioning.kNone);
        // stageBox.addOption("Around Stage", AutonomousTabData.StagePositioning.kAroundStage);
        stageBox.addOption("Through Stage", AutonomousTabData.StagePositioning.kThroughStage);

        //put the widget on the shuffleboard
        autonomousTab.add(stageBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1,4)
            .withSize(8, 3);
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
        
        return autonomousTab.add("Is Data Valid?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(19, 1)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private GenericEntry createDoesAutonomousExistBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();
    
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        return autonomousTab.add("Does Autonomous Exist", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(19, 10)
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
             .withPosition(1, 7)
             .withSize(18, 3)
             .getEntry();
    }

    private GenericEntry createAutonomousNameBox()
    {
         return autonomousTab.add("Autonomous Name", "")
             .withWidget(BuiltInWidgets.kTextView)
             .withPosition(1, 10)
             .withSize(18, 3)
             .getEntry(); 

    }

    
    private void updateAutonomousTabData()
    {
        autonomousTabData.startingSide = startingSideBox.getSelected();
        autonomousTabData.sitPretty = sitPrettyBox.getSelected();
        autonomousTabData.stagePositioning = stageBox.getSelected();
        autonomousTabData.scoreExtraNotes = scoreExtraNotesBox.getSelected();
        // autonomousTabData.driveOutOfStartZone = driveOutOfStartZoneBox.getSelected();
        // autonomousTabData.containingPreload = containingPreloadBox.getSelected();
        //autonomousTabData.scorePreload = scorePreloadBox.getSelected();
        // autonomousTabData.driveDelay = driveDelayBox.getSelected();
        // autonomousTabData.shootDelay = shootDelayBox.getSelected();
        // autonomousTabData.pickupSecondNote = pickupNotesBox.getSelected();
        // autonomousTabData.scoreExtraNotes = scoreExtraNotesBox.getSelected();
        // autonomousTabData.sitPretty = sitPrettyBox.getSelected();
        // autonomousTabData.stagePositioning = stageBox.getSelected();
        autonomousTabData.createPathPlannerStringAndCommand();
    }

    // private void createPathPlannerString()
    // {
    //     AutoCommandList.pathPlannerString = "";
    //     AutoCommandList.pathPlannerString += autonomousTabData.startingSide;
    //     AutoCommandList.pathPlannerString += autonomousTabData.sitPretty;
    //     if (!doNothing())
    //     {
    //         AutoCommandList.pathPlannerString += autonomousTabData.scoreExtraNotes;
    //     }
    // }

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
                    //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
                }
                break;

            case kPressed:
                // errorMessage = "";
                updateAutonomousTabData();
                // updateIsDataValidAndErrorMessage();
                if(autonomousTabData.isDataValid())
                {
                    successfulDownload.setBoolean(true);
                    // updateAutonomousTabData();
                    // updateIsAutoValid();
                    if(autonomousTabData.isAutoValid())
                    {
                        doesAutonomousExist.setBoolean(true);
                    }
                    else
                    {
                        doesAutonomousExist.setBoolean(false);
                    }
                    
                    isNewData = true;
                    
                    
                    // errorMessageBox.setString(errorMessage);
                    //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
                    
                }
                else
                {
                    doesAutonomousExist.setBoolean(false);
                    successfulDownload.setBoolean(false);
                    
                    // errorMessageBox.setString(errorMessage);
                    //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
                }
                errorMessageBox.setString(autonomousTabData.getErrorMessage());
                DriverStation.reportWarning(autonomousTabData.getErrorMessage(), false);
                autonomousNameBox.setString(autonomousTabData.getPathPlannerString());
                previousButtonState = ButtonState.kStillPressed;
                break;

            case kStillPressed:
                if(!isSendDataButtonPressed)
                {
                    previousButtonState = ButtonState.kReleased;
                }
                break;

            case kReleased:
                successfulDownload.setBoolean(false);
                previousButtonState = ButtonState.kStillReleased;
                break;
        }

        return isNewData;
    }

    // public AutonomousTabData getAutonomousTabData()
    // {
    //     return autonomousTabData;
    // }

    // public AutoCommandList getAutoCommandList()
    // {
    //     return autoCommandList;
    // }

    // public void updateIsAutoValid()
    // {
    //     String msgAuto = "";
    //     boolean autoExists = true;

    //     // autonomousCommand = new AutoCommandList(robotContainer, autonomousTabData);
    //     autonomousTabData.createPathPlannerStringAndCommand();
    //     autonomousCommand = autonomousTabData.getPathPlannerCommand();

    //     // if(!AutoBuilder.getAllAutoNames().contains(AutoCommandList.pathPlannerString))
    //     if(!AutoBuilder.getAllAutoNames().contains(autonomousTabData.getPathPlannerString()))
    //     {
    //         // add(AutoBuilder.buildAuto(AutoCommandList.pathPlannerString));
    //         autoExists = false;
    //         //doesAutonomousExist.setBoolean(false);

    //         msgAuto = " [Selected Autonomous Path does NOT exist ]\n";
            
    //     }

    //     if(!autoExists)
    //         errorMessage += msgAuto;

    //     isAutoValid = autoExists;

    // }

    public Command getAutonomousCommand()
    {
        return autonomousTabData.getPathPlannerCommand();
    }   

    public AutonomousTabData.StartingSide getStartingSide()
    {
        return autonomousTabData.startingSide;
    }

//     public void updateIsDataValidAndErrorMessage()
//     {
//         //errorMessage = "";
//         String msgValid = "";
//         boolean isValid = true;
//         //autonomousNameBox.setString(AutoCommandList.pathPlannerString);
        
//         // boolean isContainingPreload = (containingPreloadBox.getSelected() == AutonomousTabData.ContainingPreload.kYes);
//         // boolean isScorePreload = (scorePreloadBox.getSelected() == AutonomousTabData.ScorePreload.kYes);
//         // boolean isShootDelay = 
//         // (shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k0 ||
//         // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k1 ||
//         // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k2 ||
//         //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k3 );
//         //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k4 ||
//         //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k5 )
//         // boolean isPickupSecondNote = (pickupNotesBox.getSelected() == AutonomousTabData.PickupSecondNote.kYes);
//         boolean isScoreMoreNotes = 
//         (//scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k0 ||
//          scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k1 ||
//          scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k2 ||
//          scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k3)||
//          scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k4;
//         // boolean isDriveDelay = 
//         //  (driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k0 ||
//         // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k1 ||
//         // //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k2 ||
//         //  driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k3 );
//         //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k4 ||
//         //  shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k5 )
//         boolean isStartingLocation =
//         (startingSideBox.getSelected() == AutonomousTabData.StartingSide.kAmp ||
//         startingSideBox.getSelected() == AutonomousTabData.StartingSide.kSub ||
//         startingSideBox.getSelected() == AutonomousTabData.StartingSide.kSource);

//         boolean isSitPretty =
//         (sitPrettyBox.getSelected() == AutonomousTabData.SitPretty.kYes);
        
//         // boolean isOverDelay = 
//         // (shootDelayBox.getSelected() == AutonomousTabData.ShootDelay.k3 && 
//         // driveDelayBox.getSelected() == AutonomousTabData.DriveDelay.k3);

//         boolean isScoreManyMoreNotes = 
//         (scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k3 || 
//         scoreExtraNotesBox.getSelected() == AutonomousTabData.ScoreExtraNotes.k2 );

//         // boolean isStage = 
//         // (stageBox.getSelected() == AutonomousTabData.StagePositioning.kThroughStage || 
//         // stageBox.getSelected() == AutonomousTabData.StagePositioning.kAroundStage );

//         boolean isNoStage = 
//         (stageBox.getSelected() == AutonomousTabData.StagePositioning.kNone);

//         boolean isNotSub = 
//         (startingSideBox.getSelected() == AutonomousTabData.StartingSide.kSource ||
//         startingSideBox.getSelected() == AutonomousTabData.StartingSide.kAmp);

//         // if(!isContainingPreload && isScorePreload) :)
//         // {
//         //     isValid = false;
            
//         //     msg += "[ Not Possible ] - Cannot Score without containing Preload \n";

//         // }

//         // if(!isPickupSecondNote && isScoreSecondNote)
//         // {
//         //     isValid = false;
            
//         //     msg += "[ Not Possible ] - Cannot Score Second Note without Picking It up \n";

//         // }

        
//         // if(isShootDelay && isScorePreload) :)
//         // {
//         //     isValid = false;
            
//         //     msg += "[ Not Possible ] - Cannot Score without containing Preload \n";

//         // }


//         // if(isScoreManyMoreNotes && isOverDelay) 
//         // {
//         //     isValid = false;
            
//         //     msg += "[Not Possible] - Cannot score 2+ extra Notes with double 3 second delays \n";

//         // }

//         if(isSitPretty && isScoreMoreNotes)
//         {
//             isValid = false;

//             msgValid = " [Backup Option Selected] - Cannot complete any other tasks \n";
//         }

// //         if(isNotSub && isStage)
// //         {
// //             isValid = false;
// // //:)
// //             msgValid = " [Stage Not Available] - Non-Sub Start selected \n";
// //         }

        
        

//         // Do NOT remove any of the remaining code
//         // Check if the selections are valid
//         if(!isValid)
//             errorMessage += msgValid;

        
        
//         // Displays either "No Errors" or the error messages
        
//         //autonomousNameBox.setString(" ");

//         //autonomousNameBox.setString(AutoCommandList.pathPlannerString);

//         // 
//         isDataValid = isValid;
        

//     }   

    // public static boolean doNothing()
    // {
    //     boolean doNothing =
    //     (sitPrettyBox.getSelected() == AutonomousTabData.SitPretty.kYes);
        
    //     return doNothing;
    // }

    // public static boolean useStage()
    // {
    //     boolean useStage =
    //      (stageBox.getSelected() == AutonomousTabData.StagePositioning.kNone);
    //         //stageBox.getSelected() == AutonomousTabData.StagePositioning.kThroughStage) ||
    //     // (stageBox.getSelected() == AutonomousTabData.StagePositioning.kAroundStage ;
        
    //     return useStage;
    // }
    

}
