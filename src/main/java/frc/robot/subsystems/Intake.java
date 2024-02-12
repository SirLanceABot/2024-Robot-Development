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
 * create intake which pick up notes
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

    private final class PeriodicData
    {
        // INPUTS
        private double topIntakePosition = 0.0;
        private double bottomIntakePosition = 0.0;
        private double topIntakeVelocity;
        private double bottomIntakeVelocity;

        // OUTPUTS
        private double topIntakeSpeed = 0.0;
        private double bottomIntakeSpeed = 0.0;

    }

    private PeriodicData periodicData = new PeriodicData();

    private final CANSparkMax4237 topMotor = new CANSparkMax4237(Constants.Intake.TOP_MOTOR_PORT, Constants.Intake.TOP_MOTOR_CAN_BUS, "intakeTopMotor");
    private final CANSparkMax4237 bottomMotor = new CANSparkMax4237(Constants.Intake.BOTTOM_MOTOR_PORT, Constants.Intake.BOTTOM_MOTOR_CAN_BUS, "intakeBottomMotor");
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
        bottomMotor.setupCoastMode();
        topMotor.setupPIDController(0, 17.0, 0.0, 0.0);
        bottomMotor.setupPIDController(0, 17.0, 0.0, 0.0);

        topMotor.setupVelocityConversionFactor(getBottomPosition());

        topMotor.setupCurrentLimit(30.0, 35.0, 0.5);

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
        periodicData.topIntakeSpeed = 0.8;
        periodicData.bottomIntakeSpeed = 0.8;
    }

    public void ejectFront()
    {
        periodicData.topIntakeSpeed = -0.8;
        periodicData.bottomIntakeSpeed = -0.8;
    }

    public void pickupBack()
    {
        periodicData.topIntakeSpeed = 0.8;
        periodicData.bottomIntakeSpeed = -0.8;
    }

    public void ejectBack()
    {
        periodicData.topIntakeSpeed = -0.8;
        periodicData.bottomIntakeSpeed = 0.8;
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

    public double getTopVelocity()
    {
        return periodicData.topIntakeVelocity;
    }

    public double getBottomVelocity()
    {
        return periodicData.bottomIntakeVelocity;
    }

    public Command pickupCommand()
    {
        return Commands.runOnce(() -> pickupFront(), this).withName("Pickup");
    }

    public Command ejectCommand()
    {
        return Commands.runOnce(() -> pickupBack(), this).withName("Eject");
    }

    public Command stopCommand()
    {
        return Commands.runOnce(() -> stop(), this).withName("Stop");
    }

    @Override
    public void readPeriodicInputs()
    {
        periodicData.topIntakePosition = topMotor.getPosition();
        periodicData.bottomIntakePosition = bottomMotor.getPosition();
        periodicData.topIntakeVelocity = topMotor.getVelocity();
        periodicData.bottomIntakeVelocity = bottomMotor.getVelocity();
    }

    @Override
    public void writePeriodicOutputs()
    {
        // topMotor.set(periodicData.topIntakeSpeed);
        // bottomMotor.set(periodicData.bottomIntakeSpeed);

        topMotor.setControlVelocity(periodicData.topIntakeSpeed);
        bottomMotor.setControlVelocity(periodicData.bottomIntakeSpeed);
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
