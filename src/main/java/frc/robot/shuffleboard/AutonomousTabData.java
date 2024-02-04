package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

public class AutonomousTabData 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    //-------------------------------------------------------------------//

    public static enum StartingLocation
    {
        kLeft, kMiddle, kRight;
    }

    public static enum ContainingPreload
    {
        kYes, kNo;
    }

    public static enum DriveOutOfStartZone
    {
        kYes, kNo;
    }

    public static enum ScorePreload
    {
        kYes, kNo;
    }

    public static enum ShootDelay
    {
        k0, k1, k2, k3, k4, k5;
    }

    public static enum DriveDelay
    {
        k0, k1, k2, k3, k4, k5;
    }

    public static enum PickupSecondNote
    {
        kYes, kNo;
    }

    public static enum ScoreSecondNote
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    // IMPORTANT: Any variables added here must be initialized in the copy constructor below
    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public DriveOutOfStartZone driveOutOfStartZone = DriveOutOfStartZone.kYes;
    public ContainingPreload containingPreload = ContainingPreload.kYes;
    public ScorePreload scorePreload = ScorePreload.kYes;
    public ShootDelay shootDelay = ShootDelay.k0;
    public DriveDelay driveDelay = DriveDelay.k0;
    public PickupSecondNote pickupSecondNote = PickupSecondNote.kYes;
    public ScoreSecondNote scoreSecondNote = ScoreSecondNote.kYes;
  

    // Default constructor
    public AutonomousTabData()
    {}

    // Copy Constructor
    public AutonomousTabData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        driveOutOfStartZone = atd.driveOutOfStartZone;
        containingPreload = atd.containingPreload;
        scorePreload = atd.scorePreload;
        shootDelay = atd.shootDelay;
        driveDelay = atd.driveDelay;
        pickupSecondNote = atd.pickupSecondNote;
        scoreSecondNote = atd.scoreSecondNote;

    }

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location           : " + startingLocation   + "\n";
        str += "Drive Out Of Start Zone     : " + driveOutOfStartZone  + "\n";
        str += "Containing Preload          : " + containingPreload + "\n";
        str += "Score Preload               : " + scorePreload  + "\n";  
        str += "Shoot Delay                 : " + shootDelay + "\n";   
        str += "Drive Delay                 : " + driveDelay + "\n";  
        str += "Pickup Second Note          : " + pickupSecondNote + "\n";
        str += "Score Second Note           : " + scoreSecondNote + "\n";

        return str;
    }
}
