package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.RobotContainer;
// import frc.robot.motors.CANSparkMax4237;
import frc.robot.motors.TalonFX4237;

/**
 * Test class for JWood
 */
public class JWoodTest implements Test
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
    // Put all inner enums and inner classes here



    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final RobotContainer robotContainer;
    private final TalonFX4237 mc;
    private final Joystick joystick;
    // private TalonFX talon;

    private boolean isInverted = false;
    private boolean isBrake = true;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    public JWoodTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        mc = new TalonFX4237(1, Constants.ROBORIO, "JWoodTest Motor");
        joystick = new Joystick(0);
        configMotor();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    public void configMotor()
    {
        mc.setupFactoryDefaults();
        mc.setupBrakeMode();
        mc.setupInverted(isInverted);
        mc.setupForwardSoftLimit(50, true);
        mc.setupReverseSoftLimit(0, true);
    }

    // public void configTalonFX()
    // {
    //     talon = new TalonFX(1);
    //     AudioConfigs audioConfigs = new AudioConfigs();
    //     talon.getConfigurator().refresh(audioConfigs);
    //     audioConfigs.AllowMusicDurDisable = true;
    //     talon.getConfigurator().apply(audioConfigs);
        
    //     Orchestra orchestra = new Orchestra();

    //     // Add a single device to the orchestra
    //     orchestra.addInstrument(talon);

    //     // Attempt to load the chrp
    //     orchestra.loadMusic("starwars.chrp");
    //     orchestra.play();
    // }


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    /**
     * This method runs one time before the periodic() method.
     */
    @Override
    public void init()
    {}

    /**
     * This method runs periodically (every 20ms).
     */
    @Override
    public void periodic()
    {
        if(joystick.getRawButton(1))
        {
            mc.set(0.1);
            System.out.println(mc.getPosition());
        }
        else if(joystick.getRawButton(2))
        {
            mc.set(-0.1);
            System.out.println(mc.getPosition());
        }
        else
            mc.set(0.0);

        if(joystick.getRawButtonPressed(3))
        {
            isInverted = !isInverted;
            mc.setupInverted(isInverted);
        }

        if(joystick.getRawButtonPressed(4))
        {
            isBrake = !isBrake;
            if(isBrake)
                mc.setupBrakeMode();
            else
                mc.setupCoastMode();
        }

        if(joystick.getRawButtonPressed(7))
            mc.setPosition(0.0);

    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    @Override
    public void exit()
    {}
}
