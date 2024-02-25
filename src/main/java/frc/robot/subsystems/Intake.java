package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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


        // private double kP = SmartDashboard.getNumber("kP", 0.0);
        // private double kI = SmartDashboard.getNumber("kI", 0.0);
        // private double kD = SmartDashboard.getNumber("kD", 0.0);

    }

    private PeriodicData periodicData = new PeriodicData();

    private final double GEAR_RATIO = 1.0 / 25.0;
    private final double WHEEL_DIAMETER_FEET = 2.25 / 12.0;
    private final double MINUTES_TO_SECONDS = 1.0 / 60.0;
    private final double RPM_TO_FPS = GEAR_RATIO * MINUTES_TO_SECONDS * Math.PI * WHEEL_DIAMETER_FEET;
    // private final double PERCENT_VOLTAGE = 0.9;
    // private final double VOLTAGE = PERCENT_VOLTAGE * Constants.END_OF_MATCH_BATTERY_VOLTAGE;
    private final double DEFAULT_SPEED = 0.9;

    private final CANSparkMax4237 topMotor = new CANSparkMax4237(Constants.Intake.TOP_MOTOR_PORT, Constants.Intake.TOP_MOTOR_CAN_BUS, "intakeTopMotor");
    private final CANSparkMax4237 bottomMotor = new CANSparkMax4237(Constants.Intake.BOTTOM_MOTOR_PORT, Constants.Intake.BOTTOM_MOTOR_CAN_BUS, "intakeBottomMotor");
    // private PIDController PIDcontroller = new PIDController(periodicData.kP, periodicData.kI, periodicData.kD);
    // private RelativeEncoder topEncoder;
    // private RelativeEncoder bottomEncoder;
    // private boolean tunePID = true;

    /** 
     * Creates a new Intake. 
     */
    public Intake()
    {
        super("Intake");
        System.out.println("  Constructor Started:  " + fullClassName);

        configCANSparkMax();

        // if(tunePID)
        // {
        //     SmartDashboard.putNumber("currentVelocity", getTopVelocity());
        //     SmartDashboard.putNumber("kP", periodicData.kP);
        //     SmartDashboard.putNumber("kI", periodicData.kI);
        //     SmartDashboard.putNumber("kD", periodicData.kD);
        // }

        // SmartDashboard.putNumber("Velocity", 0.0);
        
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
        // Set Coast Mode
        topMotor.setupCoastMode();
        bottomMotor.setupCoastMode();
        // topMotor.setupPIDController(0, periodicData.kP, periodicData.kI, periodicData.kD);
        // bottomMotor.setupPIDController(0, 17.0, 0.0, 0.0);

        topMotor.setupVelocityConversionFactor(RPM_TO_FPS);

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
        periodicData.topIntakeSpeed = DEFAULT_SPEED; //VOLTAGE;
        periodicData.bottomIntakeSpeed = DEFAULT_SPEED; //VOLTAGE;
    }

    public void ejectFront()
    {
        periodicData.topIntakeSpeed = -DEFAULT_SPEED; //-VOLTAGE;
        periodicData.bottomIntakeSpeed = -DEFAULT_SPEED; //-VOLTAGE;
    }

    public void pickupBack()
    {
        periodicData.topIntakeSpeed = DEFAULT_SPEED + 0.1; //VOLTAGE;
        periodicData.bottomIntakeSpeed = -DEFAULT_SPEED; //-VOLTAGE;
    }

    public void ejectBack()
    {
        periodicData.topIntakeSpeed = -DEFAULT_SPEED; //-VOLTAGE;
        periodicData.bottomIntakeSpeed = DEFAULT_SPEED; //VOLTAGE;
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

    public Command pickupFrontCommand()
    {
        return Commands.runOnce(() -> pickupFront(), this).withName("Pickup Front");
    }

    public Command pickupBackCommand()
    {
        return Commands.runOnce(() -> pickupBack(), this).withName("Pickup Back");
    }

    public Command ejectCommand()
    {
        return Commands.runOnce(() -> ejectFront(), this).withName("Eject");
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

        // if(tunePID)
        // {
        //     periodicData.kP = SmartDashboard.getNumber("kP", 0.0);
        //     periodicData.kI = SmartDashboard.getNumber("kI", 0.0);
        //     periodicData.kD = SmartDashboard.getNumber("kD", 0.0);
        // }
    }

    @Override
    public void writePeriodicOutputs()
    {
        topMotor.setVoltage(periodicData.topIntakeSpeed * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
        bottomMotor.setVoltage(periodicData.bottomIntakeSpeed * Constants.END_OF_MATCH_BATTERY_VOLTAGE);

        // if(tunePID)
        // {
        //     PIDcontroller.setP(periodicData.kP);
        //     PIDcontroller.setI(periodicData.kI);
        //     PIDcontroller.setD(periodicData.kD);
        // }

        // topMotor.setControlVelocity(2.0);
        // bottomMotor.setControlVelocity(periodicData.bottomIntakeSpeed);
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
