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
        kLeft("Left"),
        kMiddle("Middle"),
        kRight("Right");

        private final String name;

        private StartingLocation(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    // public static enum ContainingPreload
    // {
    //     kYes, kNo;
    // }

    // public static enum DriveOutOfStartZone
    // {
    //     kYes, kNo;
    // }

    public static enum ScorePreload
    {
        kYes("Yes"), 
        kNo("No");

        private final String name;

        private ScorePreload(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static enum ShootDelay
    {
        k0("ShootDelay0"), 
        k3("ShootDelay3"), 
        k5("ShootDelay5");

        private final String name;

        private ShootDelay(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static enum DriveDelay
    {
        k0("DriveDelay0"), 
        k3("DriveDelay3"), 
        k5("DriveDelay5");

        private final String name;

        private DriveDelay(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    } 

    // public static enum PickupSecondNote
    // {
    //     kYes, kNo;
    // }

    public static enum ScoreMoreNotes
    {
        k0("ScoreMoreNotes0"), 
        k1("ScoreMoreNotes1"), 
        k2("ScoreMoreNotes"), 
        k3("ScoreMoreNotes");

        private final String name;

        private ScoreMoreNotes(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    //-------------------------------------------------------------------//

    // IMPORTANT: Any variables added here must be initialized in the copy constructor below
    public StartingLocation startingLocation = StartingLocation.kMiddle;
    // public DriveOutOfStartZone driveOutOfStartZone = DriveOutOfStartZone.kYes;
    // public ContainingPreload containingPreload = ContainingPreload.kYes;
    public ScorePreload scorePreload = ScorePreload.kYes;
    public ShootDelay shootDelay = ShootDelay.k0;
    public DriveDelay driveDelay = DriveDelay.k0;
    // public PickupSecondNote pickupSecondNote = PickupSecondNote.kYes;
    public ScoreMoreNotes scoreMoreNotes = ScoreMoreNotes.k0;
  

    // Default constructor
    public AutonomousTabData()
    {}

    // Copy Constructor
    public AutonomousTabData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        // driveOutOfStartZone = atd.driveOutOfStartZone;
        // containingPreload = atd.containingPreload;
        scorePreload = atd.scorePreload;
        shootDelay = atd.shootDelay;
        driveDelay = atd.driveDelay;
        // pickupSecondNote = atd.pickupSecondNote;
        scoreMoreNotes = atd.scoreMoreNotes;

    }

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location           : " + startingLocation   + "\n";
        // str += "Drive Out Of Start Zone     : " + driveOutOfStartZone  + "\n";
        // str += "Containing Preload          : " + containingPreload + "\n";
        str += "Score Preload               : " + scorePreload  + "\n";  
        str += "Shoot Delay                 : " + shootDelay + "\n";   
        str += "Drive Delay                 : " + driveDelay + "\n";  
        // str += "Pickup Second Note          : " + pickupSecondNote + "\n";
        str += "Score Second Note           : " + scoreMoreNotes + "\n";

        return str;
    }
}
