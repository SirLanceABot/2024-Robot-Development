package frc.robot.controls;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;


public class DriverController extends Xbox
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    

    // *** INNER ENUMS and INNER CLASSES ***
    private class PeriodicData
    {
        private double[] axis = new double[6];
        private boolean[] button = new boolean[14];  // skip 0 and 11
        private Dpad dpad;
    }

    private PeriodicData periodicData = new PeriodicData();

    // *** CLASS CONSTRUCTOR ***
    public DriverController(int port)
    {
        super(port);

        System.out.println("  Constructor Started:  " + fullClassName);

        registerPeriodicIO();
        configureAxes();
        createRumbleEvents();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    // *** CLASS & INSTANCE METHODS *** 

    public void createRumbleEvents()
    {
        createRumbleEvent(30.0, 2.0, 0.75, 0.75);
        createRumbleEvent(10.0, 1.0, 1.0, 1.0);
        createRumbleEvent(5.0, 0.25, 1.0, 1.0);
        createRumbleEvent(4.0, 0.25, 1.0, 1.0);
        createRumbleEvent(3.0, 0.25, 1.0, 1.0);
        createRumbleEvent(2.0, 0.25, 1.0, 1.0);
        createRumbleEvent(1.0, 0.25, 1.0, 1.0);
    }

    public void configureAxes()
    {
        setAxisSettings(Axis.kLeftX, 0.05, 0.0, 5.0, true, AxisScale.kCubed);
        setAxisSettings(Axis.kLeftY, 0.05, 0.0, 5.0, true, AxisScale.kCubed);
        setAxisSettings(Axis.kLeftTrigger, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightTrigger, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightX, 0.1, 0.0, 5.0, true, AxisScale.kSquared);
        setAxisSettings(Axis.kRightY, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
    }

    public DoubleSupplier getAxisSupplier(Axis axis)
    {
        return getAxisSupplier(axis, 1.0);
    }

    public DoubleSupplier getAxisSupplier(Axis axis, double scaleFactor)
    {
        return () -> periodicData.axis[axis.value] * scaleFactor;
    }

    public DoubleSupplier getAxisSupplier(Axis axis, DoubleSupplier scaleFactorSupplier)
    {
        return () -> periodicData.axis[axis.value] * scaleFactorSupplier.getAsDouble();
    }

    public BooleanSupplier getButtonSupplier(Button button)
    {
        return () -> periodicData.button[button.value];
    }

    public BooleanSupplier getDpadSupplier(Dpad dpad)
    {
        return () -> periodicData.dpad == dpad;
    }

    public BooleanSupplier movingLeftOrRightAxes()
    {
        return () -> {
            boolean leftMove = Math.abs(periodicData.axis[Xbox.Axis.kLeftX.value]) > 0.0 
                || Math.abs(periodicData.axis[Xbox.Axis.kLeftY.value]) > 0.0;
            boolean rightMove = Math.abs(periodicData.axis[Xbox.Axis.kRightX.value]) > 0.0 
                || Math.abs(periodicData.axis[Xbox.Axis.kRightY.value]) > 0.0;
            return leftMove || rightMove;};
    }

    @Override
    public void readPeriodicInputs()
    {
        for(int a = 0; a <= 5; a++)
            periodicData.axis[a] = getRawAxis(a);

        for(int b = 1; b <= 13; b++)
        {
            if(b != 11)
                periodicData.button[b] = getRawButton(b);
        }

        periodicData.dpad = getDpad();
    }

    @Override
    public void writePeriodicOutputs()
    {

    }

    @Override
    public void runPeriodicTask()
    {
        checkRumbleEvent();
    }

    @Override
    public String toString()
    {
        String str = "";

        return str;
    }
}
