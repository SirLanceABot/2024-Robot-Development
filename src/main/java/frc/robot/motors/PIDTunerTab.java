package frc.robot.motors;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.PeriodicIO;

public class PIDTunerTab implements PeriodicIO, AutoCloseable
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private enum SwitchState
    {
        kOn, kStillOn, kOff, kStillOff
    }

    private enum PIDControlType
    {
        kNone, kPosition, kVelocity;
    }

    private final class PeriodicData
    {
        // INPUTS
        private MotorController4237 motor = null;
        private MotorController4237 prevMotor = null;
        private PIDControlType pidControlType = PIDControlType.kNone;
        private int pidSlotID = 0;
        private int prevPIDSlotId = 0;
        private double setpoint = 0.0;

        private SwitchState switchState = SwitchState.kOff;

        // OUTPUTS
        private double value = 0.0;
    }

    // *** CLASS and INSTANCE VARAIBLES ***
    // These varaibles are class and instance variables
    private ShuffleboardTab pidTunerTab = Shuffleboard.getTab("PIDTuner");

    private SendableChooser<MotorController4237> motorBox = new SendableChooser<>();
    private SendableChooser<PIDControlType> pidControlTypeBox = new SendableChooser<>();
    private SendableChooser<Integer> pidSlotIDBox = new SendableChooser<>();
    // private SendableChooser<PIDController> pidControllerBox0 = new SendableChooser<>();
    // private GenericEntry pidControllerBox;
    // private SendableChooser<Boolean> setPIDBox = new SendableChooser<>();
    private ShuffleboardLayout pidTunerLayout;
    private GenericEntry kpBox;
    private GenericEntry kiBox;
    private GenericEntry kdBox;
    private GenericEntry setpointBox;
    
    private GenericEntry setPIDBox;
    private GenericEntry valueBox;
    // private PIDController pidController = new PIDController(0, 0, 0);

    
    private PeriodicData periodicData = new PeriodicData();


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * 
     */
    public PIDTunerTab()
    {
        System.out.println("  Constructor Started:  " + fullClassName);
        
        createObjects();
        registerPeriodicIO();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void createObjects()
    {
        createMotorBox();
        createControlTypeBox();
        createPIDSlotBox();
        // createPIDControllerBox();
        // pidControllerBox = createPIDControllerBox();
        createPIDControllerBox();
        kpBox = createKpBox();
        kiBox = createKiBox();
        kdBox = createKdBox();
        setpointBox = createSetpointBox();

        setPIDBox = createSetPIDBox();
        valueBox = createValueBox();

        setPIDBox.setBoolean(false);
    }

    /**
    * <b>Motor</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createMotorBox()
    {
        //create and name the Box
        SendableRegistry.add(motorBox, "Motor");
        SendableRegistry.setName(motorBox, "Motor");
        
        //add options to the Box
        motorBox.setDefaultOption("None", null);

        // for(MotorController4237 pmc : MotorController4237.pidMotorControllers4237)
        // {
        //     motorBox.addOption(pmc.getDescription(), pmc);
        // }

        //put the widget on the shuffleboard
        pidTunerTab.add(motorBox)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(1, 1)
            .withSize(4, 2);
    }

    /**
    * <b>PID Slot</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createControlTypeBox()
    {
        //create and name the Box
        SendableRegistry.add(pidControlTypeBox, "Control Type");
        SendableRegistry.setName(pidControlTypeBox, "Control Type");
        
        //add options to the Box
        pidControlTypeBox.setDefaultOption("None", PIDControlType.kNone);
        pidControlTypeBox.addOption("Position", PIDControlType.kPosition);
        pidControlTypeBox.addOption("Velocity", PIDControlType.kVelocity);

        //put the widget on the shuffleboard
        pidTunerTab.add(pidControlTypeBox)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(1, 5)
            .withSize(4, 2);
    }

    /**
    * <b>PID Slot</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPIDSlotBox()
    {
        //create and name the Box
        SendableRegistry.add(pidSlotIDBox, "PID Slot");
        SendableRegistry.setName(pidSlotIDBox, "PID Slot");
        
        //add options to the Box
        pidSlotIDBox.setDefaultOption("0", 0);
        pidSlotIDBox.addOption("1", 1);
        pidSlotIDBox.addOption("2", 2);
        pidSlotIDBox.addOption("2", 2);

        //put the widget on the shuffleboard
        pidTunerTab.add(pidSlotIDBox)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(1, 9)
            .withSize(4, 2);
    }

    /**
    * <b>PID Controller</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPIDControllerBox()
    {
        pidTunerLayout = pidTunerTab.getLayout("PID Tuner", BuiltInLayouts.kList)
            .withPosition(6, 1)
            .withSize(6, 6)
            .withProperties(Map.of("Label position", "LEFT"));
    }

    private GenericEntry createKpBox()
    {
        return pidTunerLayout.add("P = ", 0.0)
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();
    }  

    private GenericEntry createKiBox()
    {
        return pidTunerLayout.add("I = ", 0.0)
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();
    }

    private GenericEntry createKdBox()
    {
        return pidTunerLayout.add("D = ", 0.0)
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();
    }

    private GenericEntry createSetpointBox()
    {
        return pidTunerLayout.add("Setpoint = ", 0.0)
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();
    } 


    /**
     * <b>Set PID</b> Box
     * <p>Create an entry in the Network Table and add the Button to the Shuffleboard Tab
     */
    private GenericEntry createSetPIDBox()
    {
        return pidTunerTab.add("Set PID", false)
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .withPosition(13, 1)
            .withSize(4, 2)
            .getEntry();
    }

    private GenericEntry createValueBox()
    {
        return pidTunerTab.add("Value", 0.0)
            .withWidget(BuiltInWidgets.kTextView)
            .withPosition(13, 5)
            .withSize(4, 2)
            .getEntry();
    }


    private void updateSetPIDSwitch()
    {
        // boolean isSwitchOn = setPIDBox.getSelected();
        boolean isSwitchOn = setPIDBox.getBoolean(false);

        switch(periodicData.switchState)
        {
            case kStillOff:
                if(isSwitchOn)
                {
                    periodicData.switchState = SwitchState.kOn;
                }
                break;

            case kOn:
                periodicData.switchState = SwitchState.kStillOn;
                break;

            case kStillOn:
                if(!isSwitchOn)
                {
                    periodicData.switchState = SwitchState.kOff;
                }
                break;

            case kOff:
                periodicData.switchState = SwitchState.kStillOff;
                break;
        }
    }

    private double round(double value, int digits)
    {
        double x = Math.pow(10.0, digits);
        return Math.round(value * x) / x;
    }

    @Override
    public void readPeriodicInputs()
    {
        // Get the current switch position
        updateSetPIDSwitch();
        
        // Check if the switch is off
        if(periodicData.switchState == SwitchState.kOff || periodicData.switchState == SwitchState.kStillOff)
        {
            // Get the current motor and slot
            periodicData.motor = motorBox.getSelected();
            periodicData.pidControlType = pidControlTypeBox.getSelected();
            periodicData.pidSlotID = pidSlotIDBox.getSelected();
        }
        else  // if the the switch is on
        {
            // Get the current value
            if(periodicData.motor != null)
            {
                if(periodicData.pidControlType == PIDControlType.kPosition)
                    periodicData.value = motorBox.getSelected().getPosition();
                else
                    periodicData.value = motorBox.getSelected().getVelocity();
            }

            // System.out.println("Selected = " + motorBox.getSelected() + " " + pidSlotBox.getSelected());
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(periodicData.motor != null && periodicData.pidControlType != PIDControlType.kNone)
        {
            switch(periodicData.switchState)
            {
                case kOn:
                    double kP = kpBox.getDouble(0.0);
                    double kI = kiBox.getDouble(0.0);
                    double kD = kdBox.getDouble(0.0);
                    periodicData.setpoint = setpointBox.getDouble(0.0);

                    motorBox.getSelected().setupPIDController(periodicData.pidSlotID, kP, kI, kD);
                    valueBox.setDouble(round(periodicData.value, 3));
                    break;

                case kStillOn:
                    if(periodicData.pidControlType == PIDControlType.kPosition)
                        motorBox.getSelected().setControlPosition(periodicData.setpoint);
                    else
                        motorBox.getSelected().setControlVelocity(periodicData.setpoint);
                    valueBox.setDouble(round(periodicData.value, 3));
                    break;
                
                case kOff:
                    motorBox.getSelected().stopMotor();
                    break;

                case kStillOff:
                    if(periodicData.motor != periodicData.prevMotor || periodicData.pidSlotID != periodicData.prevPIDSlotId)
                        valueBox.setDouble(round(periodicData.value, 3));
                    break;
            }
        }

        periodicData.prevMotor = periodicData.motor;
        periodicData.prevPIDSlotId = periodicData.pidSlotID;
    }

    @Override
    public void close()
    {
        motorBox.close();
        pidControlTypeBox.close();
        pidSlotIDBox.close();
        kpBox.close();
        kiBox.close();
        kdBox.close();
        setpointBox.close();
        setPIDBox.close();
        valueBox.close();
    }
}
