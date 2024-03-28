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

    public static enum StartingSide
    {
        kAmp("StartingSide_Amp -- "),
        kSub("StartingSide_Sub -- "),
        kSource("StartingSide_Source -- ");

        private final String name;

        private StartingSide(String name)
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

    // public static enum ShootDelay
    // {
    //     k0("ShootDelay_0 -- "), 
    //     k3("ShootDelay_3 -- "); 


    //     private final String name;

    //     private ShootDelay(String name)
    //     {
    //         this.name = name;
    //     }

    //     @Override
    //     public String toString()
    //     {
    //         return name;
    //     }
    // }

    // public static enum DriveDelay
    // {
    //     k0("DriveDelay_0 -- "), 
    //     k3("DriveDelay_3 -- ");

    //     private final String name;

    //     private DriveDelay(String name)
    //     {
    //         this.name = name;
    //     }

    //     @Override
    //     public String toString()
    //     {
    //         return name;
    //     }
    // } 

    // public static enum PickupSecondNote
    // {
    //     kYes, kNo;
    // }

    public static enum ScoreExtraNotes
    {
        k0(" -- ScoreExtraNotes_0"), 
        k1(" -- ScoreExtraNotes_1"), 
        k2(" -- ScoreExtraNotes_2"), 
        k3(" -- ScoreExtraNotes_3"),
        k4(" -- ScoreExtraNotes_4");

        private final String name;

        private ScoreExtraNotes(String name)
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
        kYes("Do_Nothing"), 
        kNo("Run_Autonomous"); 


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

    public static enum Stage
    {
        kNone("None"),
        kAroundStage("Around_Stage"),
        kThroughStage("Through_Stage");
        

        private final String name;

        private Stage(String name)
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
    public StartingSide startingSide = StartingSide.kSub;
    // public DriveOutOfStartZone driveOutOfStartZone = DriveOutOfStartZone.kYes;
    // public ContainingPreload containingPreload = ContainingPreload.kYes;
    // public ScorePreload scorePreload = ScorePreload.kYes;
    // public ShootDelay shootDelay = ShootDelay.k0;
    // public DriveDelay driveDelay = DriveDelay.k0;
    // public PickupSecondNote pickupSecondNote = PickupSecondNote.kYes;
    public ScoreExtraNotes scoreExtraNotes = ScoreExtraNotes.k0;
    public SitPretty sitPretty = SitPretty.kNo;
  

    // Default constructor
    public AutonomousTabData()
    {}

    // Copy Constructor
    public AutonomousTabData(AutonomousTabData atd)
    {
        startingSide = atd.startingSide;
        // driveOutOfStartZone = atd.driveOutOfStartZone;
        // containingPreload = atd.containingPreload;
        // scorePreload = atd.scorePreload;
        // shootDelay = atd.shootDelay;
        // driveDelay = atd.driveDelay;
        // pickupSecondNote = atd.pickupSecondNote;
        scoreExtraNotes = atd.scoreExtraNotes;
        sitPretty = atd.sitPretty;

    }

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";


        str += "Starting Side             : " + startingSide   + "\n";
        // str += "Drive Out Of Start Zone     : " + driveOutOfStartZone  + "\n";
        // str += "Containing Preload          : " + containingPreload + "\n";
        // str += "Score Preload               : " + scorePreload  + "\n";  
        // str += "Shoot Delay                    : " + shootDelay + "\n";   
        // str += "Drive Delay                    : " + driveDelay + "\n";  
        // str += "Pickup Second Note          : " + pickupSecondNote + "\n";
        str += "Score Extra Notes               : " + scoreExtraNotes + "\n";
//FIXME IF STATEMENT
        str += "Sit Pretty                     : " + sitPretty + "\n";


        return str;
    }
}
