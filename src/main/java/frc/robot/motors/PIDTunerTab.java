package frc.robot.motors;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
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

    private final class PeriodicData
    {
        // INPUTS
        private MotorController4237 currentMotor = null;
        private int currentSlot = 0;
        private SwitchState switchState = SwitchState.kOff;

        // OUTPUTS
        private double value = 0.0;
    }

    // *** CLASS and INSTANCE VARAIBLES ***
    // These varaibles are class and instance variables
    private ShuffleboardTab pidTunerTab = Shuffleboard.getTab("PIDTuner");

    private SendableChooser<MotorController4237> motorBox = new SendableChooser<>();
    private SendableChooser<Integer> pidSlotBox = new SendableChooser<>();
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
    private PIDController pidController = new PIDController(0, 0, 0);

    
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

        for(MotorController4237 pmc : MotorController4237.pidMotorControllers4237)
        {
            motorBox.addOption(pmc.getDescription(), pmc);
        }

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
    private void createPIDSlotBox()
    {
        //create and name the Box
        SendableRegistry.add(pidSlotBox, "PID Slot");
        SendableRegistry.setName(pidSlotBox, "PID Slot");
        
        //add options to the Box
        pidSlotBox.setDefaultOption("0", 0);
        pidSlotBox.addOption("1", 1);
        pidSlotBox.addOption("2", 2);
        pidSlotBox.addOption("2", 2);

        //put the widget on the shuffleboard
        pidTunerTab.add(pidSlotBox)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(1, 5)
            .withSize(4, 2);
    }

    /**
    * <b>PID Controller</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    // private void createPIDControllerBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(pidControllerBox0, "PID Controller");
    //     SendableRegistry.setName(pidControllerBox0, "PID Controller");

    //     //put the widget on the shuffleboard
    //     pidTunerTab.add(pidControllerBox0)
    //         .withWidget(BuiltInWidgets.kPIDController)
    //         .withPosition(6, 1)
    //         .withSize(4, 6);
    // }

    // private GenericEntry createPIDControllerBox()
    // {
    //     return pidTunerTab.add("PID Controller", "0, 0, 0, 0")
    //         .withWidget(BuiltInWidgets.kPIDController)
    //         .withPosition(6, 1)
    //         .withSize(4, 6)
    //         .getEntry("PIDController");
    // }

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

    private void createPIDControllerBox()
    {
        pidTunerLayout = pidTunerTab.getLayout("PID Tuner", BuiltInLayouts.kList)
                            .withPosition(6, 1)
                            .withSize(4, 6)
                            .withProperties(Map.of("Label position", "LEFT"));

        

    }

    /**
     * <b>Set PID</b> Box
     * <p>Create an entry in the Network Table and add the Button to the Shuffleboard Tab
     */
    // private void createSetPIDBox()
    // {
    //     SendableRegistry.add(setPIDBox, "Set PID");
    //     SendableRegistry.setName(setPIDBox, "Set PID");

    //     setPIDBox.setDefaultOption("No", false);
    //     setPIDBox.addOption("Yes", true);

    //     pidTunerTab.add(setPIDBox)
    //         .withWidget(BuiltInWidgets.kComboBoxChooser)
    //         .withPosition(11, 1)
    //         .withSize(4, 2);
    // }

    private GenericEntry createSetPIDBox()
    {
        return pidTunerTab.add("Set PID", false)
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .withPosition(11, 1)
            .withSize(4, 2)
            .getEntry();
    }

    private GenericEntry createValueBox()
    {
        return pidTunerTab.add("Value", 0.0)
            .withWidget(BuiltInWidgets.kTextView)
            .withPosition(11, 5)
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
            periodicData.currentMotor = motorBox.getSelected();
            periodicData.currentSlot = pidSlotBox.getSelected();
        }
        else  // if the the switch is on
        {
            // Get the current value
            if(periodicData.currentMotor != null)
                periodicData.value = motorBox.getSelected().getPosition();

            // System.out.println("Selected = " + motorBox.getSelected() + " " + pidSlotBox.getSelected());
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(periodicData.currentMotor != null)
        {
            double setpoint = setpointBox.getDouble(0.0);
            switch(periodicData.switchState)
            {
                case kOn:
                    // double kP = pidControllerBox0.getSelected().getP();
                    // double kI = pidControllerBox.getSelected().getI();
                    // double kD = pidControllerBox.getSelected().getD();

                    double kP = kpBox.getDouble(0.0);
                    double kI = kiBox.getDouble(0.0);
                    double kD = kdBox.getDouble(0.0);

                    motorBox.getSelected().setupPIDController(periodicData.currentSlot, kP, kI, kD);
                    motorBox.getSelected().setControl(setpoint);
                    valueBox.setDouble(round(periodicData.value, 3));
                    break;

                case kStillOn:
                    motorBox.getSelected().setControl(setpoint);
                    valueBox.setDouble(round(periodicData.value, 3));
                    break;
                
                case kOff:
                    motorBox.getSelected().stopMotor();
                    break;

                case kStillOff:
                    // if(periodicData.currentMotor != previousMotor || periodicData.currentSlot != previousSlot)
                    // {
                    //     double[] pid = periodicData.currentMotor.getPID(periodicData.currentSlot);

                    //     pidControllerBox.getSelected().setPID(pid[0], pid[1], pid[2]);
                    //     pidControllerBox.getSelected().setSetpoint(0.0);
                    //     valueBox.setDouble(round(periodicData.currentMotor.getPosition(), 3));

                    //     previousMotor = periodicData.currentMotor;
                    //     previousSlot = periodicData.currentSlot;
                    // }
                    break;
            }
        }
    }

    @Override
    public void close()
    {
        motorBox.close();
        pidSlotBox.close();
        // pidControllerBox.close();
        kpBox.close();
        kiBox.close();
        kdBox.close();
        setpointBox.close();
        setPIDBox.close();
        valueBox.close();
    }
}
