package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;

import frc.robot.motors.TalonFX4237;
import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;



public class Pivot extends Subsystem4237
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class MyConstants
    {
        public double kP = 17.0;
        public double kD = 0.0;
        public double kI = 0.0;
        public int slotId = 0;

        public double iZone = 0.0;
        public double setPoint = 00;
        public boolean haveSetpoint = false;
        public boolean continuous = false;
        public double minimumInput = 0.0;
        public double maximumInput = 0.0;
        public double measurement = 0.0;
        public double positionError = 0.0;
        public double prevError = 0.0;
        public double period = 0.0;
        public double velocityError = 0.0;
    }

    private class PeriodicData
    {
        // INPUTS
    
        private double currentPosition;
        private double currentAngle;

        // OUTPUTS

        private double motorSpeed;

    }
    

    private final TalonFX4237 motor = new TalonFX4237(Constants.Pivot.MOTOR_PORT, Constants.Pivot.MOTOR_CAN_BUS, "pivotMotor");
    private final CANcoder pivotAngle = new CANcoder(20, Constants.Pivot.MOTOR_CAN_BUS);
    private PeriodicData periodicData = new PeriodicData();  
    private MyConstants myConstants = new MyConstants();
    private PIDController PIDcontroller;
    // private AnalogEncoder rotaryEncoder = new AnalogEncoder(3);

    private double position = 0.0;



    public Pivot()
    {
        super("Pivot");
        System.out.println("  Constructor Started:  " + fullClassName);

        configPivotMotor();
        SendableRegistry.addLW(this, "Pivot", "PID");
        System.out.println("  Constructor Finished: " + fullClassName);
    }

    private void configPivotMotor()
    {
        // Factory Defaults
        motor.setupFactoryDefaults();
        motor.setupInverted(false);
        motor.setupBrakeMode();
        motor.setPosition(0.0);
        //motor.setupPositionConversionFactor(0.00048828);
        motor.setupRemoteCANCoder(20);
        motor.setupPIDController(myConstants.slotId, myConstants.kP, myConstants.kI, myConstants.kD);
        


        // Soft Limits
        motor.setupForwardSoftLimit(6144.0, false);
        motor.setupReverseSoftLimit(-6144.0, false);
    }

    public void moveUp()
    {
        periodicData.motorSpeed = Constants.Pivot.MOTOR_SPEED;
        motor.set(periodicData.motorSpeed);
    }

    public void moveDown()
    {
        periodicData.motorSpeed = -Constants.Pivot.MOTOR_SPEED;
        motor.set(periodicData.motorSpeed);
    }

    public void stop()
    {
        periodicData.motorSpeed = 0.0;
        motor.set(periodicData.motorSpeed);
    }

    public double getAngle()
    {
        return periodicData.currentAngle;
    }

    public double getPosition()
    {
        return periodicData.currentPosition;
    }

    public void resetEncoder()
    {
        // motor.setPosition(0.0);
        pivotAngle.setPosition(0.0);
    }

    public void setPosition(double degrees)
    {
        position = degrees;
    }

    public void setAngle(double degrees)
    { 
            motor.setControl(degrees / 360);
        
        //setAngle using FalconFX encoder
        // if(periodicData.currentAngle > (degrees + 5))
        // {
        //     moveDown();
        // }
        // else if(periodicData.currentAngle < (degrees - 5))
        // {
        //     moveUp();
        // }
        // else
        // {
        //     stop();
        // }

        //setAngle using rotary encoder
        // if(periodicData.currentAngle > (degrees + 2))
        // {
        //     moveDown(speed);
        // }
        // else if(periodicData.currentAngle < (degrees - 2))
        // {
        //     moveUp(speed);
        // } 
        // else
        // {
        //     stop();
        // }
    }

    public Command movePivotCommand(double angle)
    {
        return Commands.runOnce(() -> setAngle(angle));
    }

    @Override
    public void readPeriodicInputs()
    {
        //Using Rotary Encoder
        //periodicData.currentAngle = 360.0 * rotaryEncoder.getAbsolutePosition();

        //Using TalonFX encoder
        periodicData.currentPosition = pivotAngle.getPosition().getValueAsDouble();
        periodicData.currentAngle = periodicData.currentPosition * 360.0;
    }

    @Override
    public void writePeriodicOutputs()
    {
        //System.out.println("currentAngle = " + periodicData.currentAngle); 
        //System.out.println("currentPosition = " + motor.getPosition());
        
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


        /**
     * Sets the Proportional coefficient of the PID controller gain.
     *
     * @param kp The proportional coefficient. Must be &gt;= 0.
     */
    public void setP(double kp) 
    {
         myConstants.kP = kp;
    }

    /**
     * Sets the Integral coefficient of the PID controller gain.
     *
     * @param ki The integral coefficient. Must be &gt;= 0.
     */
    public void setI(double ki) 
    {
        myConstants.kI = ki;
    }

    /**
     * Sets the Differential coefficient of the PID controller gain.
     *
     * @param kd The differential coefficient. Must be &gt;= 0.
     */
    public void setD(double kd) 
    {
        myConstants.kD = kd;
    }

        /**
     * Get the Proportional coefficient.
     *
     * @return proportional coefficient
     */
    public double getP() 
    {
        return myConstants.kP;
    }

    /**
     * Get the Integral coefficient.
     *
     * @return integral coefficient
     */
    public double getI() 
    {
        return myConstants.kI;
    }

    /**
     * Get the Differential coefficient.
     *
     * @return differential coefficient
     */
    public double getD() 
    {
        return myConstants.kD;
    }

        /**
     * Get the IZone range.
     *
     * @return Maximum magnitude of error to allow integral control.
     */
    public double getIZone() 
    {
        return myConstants.iZone;
    }

        /**
     * Sets the IZone range. When the absolute value of the position error is greater than IZone, the
     * total accumulated error will reset to zero, disabling integral gain until the absolute value of
     * the position error is less than IZone. This is used to prevent integral windup. Must be
     * non-negative. Passing a value of zero will effectively disable integral gain. Passing a value
     * of {@link Double#POSITIVE_INFINITY} disables IZone functionality.
     *
     * @param iZone Maximum magnitude of error to allow integral control.
     * @throws IllegalArgumentException if iZone &lt; 0
     */
    public void setIZone(double iZone) 
    {
        if (iZone < 0) 
        {
            throw new IllegalArgumentException("IZone must be a non-negative number!");
        }
        myConstants.iZone = iZone;
    }

        /**
     * Returns the current setpoint of the PIDController.
     *
     * @return The current setpoint.
     */
    public double getSetpoint() 
    {
        return myConstants.setPoint;
    }

        /**
     * Sets the setpoint for the PIDController.
     *
     * @param setpoint The desired setpoint.
     */
    public void setSetpoint(double setpoint)
    {
        myConstants.setPoint = setpoint;
        myConstants.haveSetpoint = true;

        if (myConstants.continuous) {
        double errorBound = (myConstants.maximumInput - myConstants.minimumInput) / 2.0;
        myConstants.positionError = MathUtil.inputModulus(myConstants.setPoint - myConstants.measurement, -errorBound, errorBound);
        } else {
        myConstants.positionError = myConstants.setPoint - myConstants.measurement;
        }

        myConstants.velocityError = (myConstants.positionError - myConstants.prevError) / myConstants.period;
    }

    @Override
    public void initSendable(SendableBuilder builder) 
    {
        builder.setSmartDashboardType("PIDController");
        builder.addDoubleProperty("p", this::getP, this::setP);
        builder.addDoubleProperty("i", this::getI, this::setI);
        builder.addDoubleProperty("d", this::getD, this::setD);
        builder.addDoubleProperty(
            "izone",
            this::getIZone,
            (double toSet) -> {
            try {
                setIZone(toSet);
            } catch (IllegalArgumentException e) {
                MathSharedStore.reportError("IZone must be a non-negative number!", e.getStackTrace());
            }
            });
        builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
    }
}
