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
        kLeft("Starting Location: Left -- "),
        kMiddle("Starting Location: Middle -- "),
        kRight("Starting Location: Right -- ");

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

    // public static enum ScorePreload
    // {
    //     kYes("Score Preload: Yes -- "), 
    //     kNo("Score Preload: No -- ");

    //     private final String name;

    //     private ScorePreload(String name)
    //     {
    //         this.name = name;
    //     }

    //     @Override
    //     public String toString()
    //     {
    //         return name;
    //     }
    // }

    public static enum ShootDelay
    {
        k0("Shoot Delay: 0 Sec -- "), 
        k3("Shoot Delay: 3 Secs -- "); 


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
        k0("Drive Delay: 0 Sec -- "), 
        k3("Drive Delay: 3 Sec -- ");

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
        k0("ScoreMoreNotes: 0 -- "), 
        k1("ScoreMoreNotes: 1 -- "), 
        k2("ScoreMoreNotes: 2 -- "), 
        k3("ScoreMoreNotes: 3 -- ");

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

    public static enum SitPretty
    {
        kYes("Do Nothing"), 
        kNo("Run Autonomous"); 


        private final String name;

        private SitPretty(String name)
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
    // public ScorePreload scorePreload = ScorePreload.kYes;
    public ShootDelay shootDelay = ShootDelay.k0;
    public DriveDelay driveDelay = DriveDelay.k0;
    // public PickupSecondNote pickupSecondNote = PickupSecondNote.kYes;
    public ScoreMoreNotes scoreMoreNotes = ScoreMoreNotes.k0;
    public SitPretty sitPretty = SitPretty.kNo;
  

    // Default constructor
    public AutonomousTabData()
    {}

    // Copy Constructor
    public AutonomousTabData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        // driveOutOfStartZone = atd.driveOutOfStartZone;
        // containingPreload = atd.containingPreload;
        // scorePreload = atd.scorePreload;
        shootDelay = atd.shootDelay;
        driveDelay = atd.driveDelay;
        // pickupSecondNote = atd.pickupSecondNote;
        scoreMoreNotes = atd.scoreMoreNotes;
        sitPretty = atd.sitPretty;

    }

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";


        str += "Starting Location              : " + startingLocation   + "\n";
        // str += "Drive Out Of Start Zone     : " + driveOutOfStartZone  + "\n";
        // str += "Containing Preload          : " + containingPreload + "\n";
        // str += "Score Preload               : " + scorePreload  + "\n";  
        str += "Shoot Delay                    : " + shootDelay + "\n";   
        str += "Drive Delay                    : " + driveDelay + "\n";  
        // str += "Pickup Second Note          : " + pickupSecondNote + "\n";
        str += "Score More Notes               : " + scoreMoreNotes + "\n";
//FIXME IF STATEMENT
        str += "Sit Pretty                     : " + sitPretty + "\n";


        return str;
    }
}
