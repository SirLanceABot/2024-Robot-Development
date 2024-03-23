package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

// import com.ctre.phoenix6.Orchestra;
// import com.ctre.phoenix6.configs.AudioConfigs;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
// import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.controls.Xbox;
// import frc.robot.motors.CANSparkMax4237;
// import frc.robot.motors.TalonFX4237;
import frc.robot.motors.CANSparkMax4237;
import frc.robot.motors.MotorController4237;
import frc.robot.motors.TalonFX4237;
import frc.robot.subsystems.ExampleSubsystem;

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
    // private final ExampleSubsystem exampleSubsystem;
    // private final TalonFX4237 mc;
    private final Joystick joystick;
    // private TalonFX talon;
    // private TalonFX4237 motor1 = new TalonFX4237(4, "rio", "motor4");
    // private TalonFX4237 motor2 = new TalonFX4237(12, "rio", "motor12");
    private CANSparkMax4237 sparkMotor = new CANSparkMax4237(3, "rio", "Spark Motor");

    // private boolean isInverted = false;
    // private boolean isBrake = true;


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /**
     * Use this class to test your code using Test mode
     * @param robotContainer The container of all robot components
     */
    public JWoodTest(RobotContainer robotContainer)
    {
        System.out.println("  Constructor Started:  " + fullClassName);

        this.robotContainer = robotContainer;
        // this.exampleSubsystem = robotContainer.exampleSubsystem;

        joystick = new Joystick(0);
        
        // configMotor(motor1);
        // configMotor(motor2);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    public void configMotor(MotorController4237 mc)
    {
        mc.setupFactoryDefaults();
        mc.setupCoastMode();
        // mc.setupPIDController(0, 2.5, 0, 0);
        // mc.setupBrakeMode();
        // mc.setupInverted(isInverted);
        // mc.setupForwardSoftLimit(50, true);
        // mc.setupReverseSoftLimit(0, true);
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
            sparkMotor.set(0.1);
            // motor1.set(0.1);
            // motor2.set(0.1);
            // exampleSubsystem.set(0.1);
        }
        else if(joystick.getRawButton(2))
        {
            sparkMotor.set(-0.1);
            // motor1.set(-0.1);
            // motor2.set(-0.1);
            // exampleSubsystem.set(-0.1);
        }
        else
        {
            sparkMotor.set(0.0);
            // motor1.set(0.0);
            // motor2.set(0.0);
            // exampleSubsystem.stop();
        }

        // if(joystick.getRawButtonPressed(3))
        // {
        //     isInverted = !isInverted;
        //     mc.setupInverted(isInverted);
        // }

        // if(joystick.getRawButtonPressed(4))
        // {
        //     isBrake = !isBrake;
        //     if(isBrake)
        //         mc.setupBrakeMode();
        //     else
        //         mc.setupCoastMode();
        // }

        // if(joystick.getRawButtonPressed(7))
        //     mc.setPosition(0.0);

    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    @Override
    public void exit()
    {}
}
