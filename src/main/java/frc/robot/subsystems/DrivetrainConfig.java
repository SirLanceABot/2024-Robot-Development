package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

public class DrivetrainConfig
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public final SwerveModuleConfig frontLeftSwerveModule;
    public final SwerveModuleConfig frontRightSwerveModule;
    public final SwerveModuleConfig backLeftSwerveModule;
    public final SwerveModuleConfig backRightSwerveModule;

    public DrivetrainConfig(SwerveModuleConfig frontLeftSwerveModule,
                          SwerveModuleConfig frontRightSwerveModule,
                          SwerveModuleConfig backLeftSwerveModule,
                          SwerveModuleConfig backRightSwerveModule)
    {
        this.frontLeftSwerveModule = frontLeftSwerveModule;
        this.frontRightSwerveModule = frontRightSwerveModule;
        this.backLeftSwerveModule = backLeftSwerveModule;
        this.backRightSwerveModule = backRightSwerveModule;
    }
}
