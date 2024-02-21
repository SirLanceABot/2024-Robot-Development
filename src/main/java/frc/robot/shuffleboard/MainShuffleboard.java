package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import frc.robot.PeriodicIO;
import frc.robot.RobotContainer;

public class MainShuffleboard implements PeriodicIO
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private boolean useAutonomousTab            = true;
    private boolean useCameraTab                = false;
    private boolean useSensorTab                = false;
    private boolean useDriverControllerTab      = false;
    private boolean useOperatorControllerTab    = false;
    
    public final AutonomousTab autonomousTab;
    public final CameraTab cameraTab;
    public final SensorTab sensorTab;
    // public final DriverTab driverTab;
    public final DriverControllerTab driverControllerTab;
    public final OperatorControllerTab operatorControllerTab;
    

    // *** CLASS CONSTRUCTOR ***
    public MainShuffleboard(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        boolean useFullRobot = robotContainer.fullRobot; 
        useDriverControllerTab = useDriverControllerTab && robotContainer.driverController != null;
        useOperatorControllerTab = useOperatorControllerTab && robotContainer.operatorController != null;

        autonomousTab           = (useFullRobot || useAutonomousTab)    ? new AutonomousTab()                                           : null;
        cameraTab               = (useFullRobot || useCameraTab)        ? new CameraTab()                                               : null;
        sensorTab               = (useSensorTab)                        ? new SensorTab(robotContainer)                                 : null;                                           
        driverControllerTab     = (useDriverControllerTab)              ? new DriverControllerTab(robotContainer.driverController)      : null;
        operatorControllerTab   = (useOperatorControllerTab)            ? new OperatorControllerTab(robotContainer.operatorController)  : null;

        registerPeriodicIO();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    
    // *** CLASS & INSTANCE METHODS ***
    //-------------------------------------------------------------------//
    // DRIVER CONTROLLER TAB
    public void setDriverControllerSettings()
    {
        if(driverControllerTab != null)
            driverControllerTab.setDriverControllerAxisSettings();
    }

    //-------------------------------------------------------------------//
    // OPERATOR CONTROLLER TAB
    public void setOperatorControllerSettings()
    {
        if(operatorControllerTab != null)
            operatorControllerTab.setOperatorControllerAxisSettings();
    }

    //-------------------------------------------------------------------//
    // CAMERA TAB
    public void setCameras()
    {
        if(cameraTab != null)
        {
            cameraTab.updateCameraTab();
        }
    }

    //-------------------------------------------------------------------//
    // SENSOR TAB
    public void setSensors()
    {
        if(sensorTab != null)
            sensorTab.updateSensorData();
    }

    //-------------------------------------------------------------------//
    // DRIVER TAB
    // public void setDriver()
    // {
    //     if(driverTab != null)
    //         driverTab.updateEncoderData();
    // }

    // Driver Tab



    @Override
    public void readPeriodicInputs()
    {

    }

    @Override
    public void writePeriodicOutputs()
    {

    }
}
