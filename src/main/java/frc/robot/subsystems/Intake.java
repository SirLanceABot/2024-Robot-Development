package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.CANSparkMax4237;

/**
 * Use this class as a template to create other subsystems.
 */
public class Intake extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum Direction
    {
        kForward(0.1), kBackward(-0.1), kOff(0.0);

        public final double value;
        
        private Direction(double value)
        {
            this.value = value;
        }
    }

    public enum Action
    {
        kPickup, kEject;
    }

    private class PeriodicData
    {
        // INPUTS
        private double topIntakePosition = 0.0;
        private double bottomIntakePosition = 0.0;

        // OUTPUTS
        private double topIntakeSpeed = 0.0;
        private double bottomIntakeSpeed = 0.0;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final CANSparkMax4237 topMotor = new CANSparkMax4237(3, Constants.Intake.TOP_MOTOR_CAN_BUS, "intakeTopMotor");
    private final CANSparkMax4237 bottomMotor = new CANSparkMax4237(5, Constants.Intake.BOTTOM_MOTOR_CAN_BUS, "intakeBottomMotor");
    private RelativeEncoder topEncoder;
    private RelativeEncoder bottomEncoder;

    /** 
     * Creates a new Intake. 
     */
    public Intake()
    {
        super("Intake");
        System.out.println("  Constructor Started:  " + fullClassName);

        configCANSparkMax();
        
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configCANSparkMax()
    {
        // Factory Defaults
        topMotor.setupFactoryDefaults();
        bottomMotor.setupFactoryDefaults();
        // Do Not Invert Motor Direction
        topMotor.setupInverted(false); // test later
        bottomMotor.setupInverted(true); // test later
        // Set Brake Mode
        topMotor.setupBrakeMode();
        bottomMotor.setupBrakeMode();

        // topMotor.setPosition(0.0);
        // bottomMotor.setPosition(0.0);
    }

    public double getTopPosition()
    {
        return periodicData.topIntakePosition;
    }

    public double getBottomPosition()
    {
        return periodicData.bottomIntakePosition;
    }

    public double getTopSpeed()
    {
        return periodicData.topIntakeSpeed;
    }

    public double getBottomSpeed()
    {
        return periodicData.bottomIntakeSpeed;
    }

    public void pickupFront()
    {
        periodicData.topIntakeSpeed = 0.1;
        periodicData.bottomIntakeSpeed = 0.1;
    }

    public void ejectFront()
    {
        periodicData.topIntakeSpeed = -0.1;
        periodicData.bottomIntakeSpeed = -0.1;
    }

    public void pickupBack()
    {
        periodicData.topIntakeSpeed = 0.1;
        periodicData.bottomIntakeSpeed = -0.1;
    }

    public void ejectBack()
    {
        periodicData.topIntakeSpeed = -0.1;
        periodicData.bottomIntakeSpeed = 0.1;
    }

    public void stop()
    {
        periodicData.topIntakeSpeed = 0.0;
        periodicData.bottomIntakeSpeed = 0.0;
    }

    public void in(double speed)
    {
        periodicData.topIntakeSpeed = speed;
        periodicData.bottomIntakeSpeed = speed;
    }

    public Command pickupFrontCommand()
    {
        return Commands.runOnce(() -> pickupFront(), this);
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stop(), this);
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.topIntakePosition = topMotor.getPosition();
        periodicData.bottomIntakePosition = bottomMotor.getPosition();
    }

    @Override
    public void writePeriodicOutputs()
    {
        topMotor.set(periodicData.topIntakeSpeed);
        bottomMotor.set(periodicData.bottomIntakeSpeed);
    }

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }

    @Override
    public String toString()
    {
        return "Current Top Intake Position: " + periodicData.topIntakePosition + " Current Bottom Intake Position: " + periodicData.bottomIntakePosition;
    }
}
