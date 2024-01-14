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

    public static enum PlayPreload
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    // IMPORTANT: Any variables added here must be initialized in the copy constructor below
    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public ContainingPreload containingPreload = ContainingPreload.kYes;
    public PlayPreload playPreload = PlayPreload.kYes;

    // Default constructor
    public AutonomousTabData()
    {}

    // Copy Constructor
    public AutonomousTabData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        containingPreload = atd.containingPreload;
        playPreload = atd.playPreload;
    }

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location           : " + startingLocation   + "\n";
        str += "Containing Preload          : " + containingPreload + "\n";
        str += "Play Preload                : " + playPreload  + "\n";

        return str;
    }
}
