package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import frc.robot.Constants;
import frc.robot.controls.AdaptiveSlewRateLimiter;
import frc.robot.sensors.Gyro4237;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;


/** Represents a swerve drive style drivetrain. */
public class Drivetrain extends Subsystem4237 
{

    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    /**
     * define all the inputs to be read at once
     * define all the outputs to be written at once
     */
    private class PeriodicIO 
    {
        // INPUTS
        private double xSpeed;
        private double ySpeed;
        private double turn;
        private boolean fieldRelative;
        private SwerveModulePosition frontLeftPosition;
        private SwerveModulePosition frontRightPosition;
        private SwerveModulePosition backLeftPosition;
        private SwerveModulePosition backRightPosition;


        DoubleLogEntry flsLogEntry;
        DoubleLogEntry frsLogEntry;
        DoubleLogEntry blsLogEntry;
        DoubleLogEntry brsLogEntry;
        DoubleLogEntry fldLogEntry;
        DoubleLogEntry frdLogEntry;
        DoubleLogEntry bldLogEntry;
        DoubleLogEntry brdLogEntry;
        
        // OUTPUTS
        private ChassisSpeeds chassisSpeeds;
        private SwerveModuleState[] swerveModuleStates;
        private SwerveDriveOdometry odometry;
    }

    private enum DriveMode
    {
        kDrive, kLockwheels, kStop, kArcadeDrive;
    }

    public enum ArcadeDriveDirection
    {
        kStraight(0.0), kStrafe(90.0);

        public double value;
        
        private ArcadeDriveDirection(double value)
        {
            this.value =  value;
        }

    }

    // *** CLASS & INSTANCE VARIABLES ***
    // private static final Translation2d frontLeftLocation = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    // private static final Translation2d frontRightLocation = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    // private static final Translation2d backLeftLocation = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    // private static final Translation2d backRightLocation = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);

    private final SwerveModule frontLeft;// = new SwerveModule(Port.Module.FRONT_LEFT);
    private final SwerveModule frontRight;// = new SwerveModule(Port.Module.FRONT_RIGHT);
    private final SwerveModule backLeft;// = new SwerveModule(Port.Module.BACK_LEFT);
    private final SwerveModule backRight;// = new SwerveModule(Port.Module.BACK_RIGHT);

    private final Gyro4237 gyro; //Pigeon2
    private boolean useDataLog = true;
    private final DataLog log;
    private final SwerveDriveKinematics kinematics;

    private final AdaptiveSlewRateLimiter adaptiveXRateLimiter = new AdaptiveSlewRateLimiter(Constants.DrivetrainConstants.X_ACCELERATION_RATE_LIMT, Constants.DrivetrainConstants.X_DECELERATION_RATE_LIMT);
    private final AdaptiveSlewRateLimiter adaptiveYRateLimiter = new AdaptiveSlewRateLimiter(Constants.DrivetrainConstants.Y_ACCELERATION_RATE_LIMT, Constants.DrivetrainConstants.Y_DECELERATION_RATE_LIMT);


    // TODO: Make final by setting to an initial stopped state
    //private SwerveModuleState[] previousSwerveModuleStates = null;
    private DriveMode driveMode = DriveMode.kDrive;
    private boolean resetEncoders = false;
    private boolean resetOdometry = false;

    private PeriodicIO periodicIO;
    
    // *** CLASS CONSTRUCTOR ***
    public Drivetrain(Gyro4237 gyro, DataLog log)//, DriverController driverController)
    {
        DrivetrainConfig dd = Constants.DrivetrainSetup.DRIVETRAIN_DATA;
        // super();  // call the RobotDriveBase constructor
        // setSafetyEnabled(false);
  
  
        periodicIO = new PeriodicIO(); // all the periodic I/O appear here
        
        this.gyro = gyro;
        this.log = log;
        if(log == null)
        {
            useDataLog = false;
        }

        if(useDataLog)
        {
            logEncodersInit();
        }




        frontLeft = new SwerveModule(dd.frontLeftSwerveModule);
        frontRight = new SwerveModule(dd.frontRightSwerveModule);
        backLeft = new SwerveModule(dd.backLeftSwerveModule);
        backRight = new SwerveModule(dd.backRightSwerveModule);

        // gyro = new WPI_Pigeon2(Port.Sensor.PIGEON, Port.Motor.CAN_BUS);

        kinematics = new SwerveDriveKinematics(
            dd.frontLeftSwerveModule.moduleLocation,
            dd.frontRightSwerveModule.moduleLocation,
            dd.backLeftSwerveModule.moduleLocation,
            dd.backRightSwerveModule.moduleLocation);

        periodicIO.odometry = new SwerveDriveOdometry(
            kinematics, 
            gyro.getRotation2d(),
            new SwerveModulePosition[] 
            {
                frontLeft.getPosition(),
                frontRight.getPosition(),
                backLeft.getPosition(),
                backRight.getPosition()
            });

        // setSafetyEnabled(true);
    }

    // *** CLASS & INSTANCE METHODS ***
    //FIXME Is this used?
    // public void configOpenLoopRamp(double seconds)
    // {
    //     frontLeft.configOpenLoopRamp(seconds);
    //     frontRight.configOpenLoopRamp(seconds);
    //     backLeft.configOpenLoopRamp(seconds);
    //     backRight.configOpenLoopRamp(seconds);
    // }

    
    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed Speed of the robot in the x direction (forward).
     * @param ySpeed Speed of the robot in the y direction (sideways).
     * @param turn Angular rate of the robot.
     * @param fieldRelative Whether the provided x and y speeds are relative to the field.
     */
    @SuppressWarnings("ParameterName")
    public void drive(double xSpeed, double ySpeed, double turn, boolean fieldRelative)
    {
        driveMode = DriveMode.kDrive;
        // updateOdometry();

        if(Math.abs(xSpeed) < 0.04)
            xSpeed = 0.0;
        if(Math.abs(ySpeed) < 0.04)
            ySpeed = 0.0;
        if(Math.abs(turn) < 0.04)
            turn = 0.0;    
        // periodicIO.xSpeed = xSpeed;
        // periodicIO.ySpeed = ySpeed;

        periodicIO.xSpeed = adaptiveXRateLimiter.calculate(xSpeed);
        periodicIO.ySpeed = adaptiveYRateLimiter.calculate(ySpeed);

        periodicIO.turn = turn;
        periodicIO.fieldRelative = fieldRelative;

        //ChassisSpeeds chassisSpeeds;
        //SwerveModuleState[] swerveModuleStates;

        // if(fieldRelative)
        //     chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, turn, gyro.getRotation2d());
        // else
        //     chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turn);
        
        // swerveModuleStates = kinematics.toSwerveModuleStates(periodicIO.chassisSpeeds);
        // SwerveDriveKinematics.desaturateWheelSpeeds(periodicIO.swerveModuleStates, Constant.MAX_DRIVE_SPEED);
        // printDesiredStates(swerveModuleStates);
      
        // frontLeft.setDesiredState(periodicIO.swerveModuleStates[0]);
        // frontRight.setDesiredState(periodicIO.swerveModuleStates[1]);
        // backLeft.setDesiredState(periodicIO.swerveModuleStates[2]);
        // backRight.setDesiredState(periodicIO.swerveModuleStates[3]);

        // previousSwerveModuleStates = periodicIO.swerveModuleStates;

        // feedWatchdog();
    }
    
    
    /**
     * Rotate swerve modules to an X shape to hopefully prevent being pushed 
     */
    @SuppressWarnings("ParameterName")
    public void lockWheels()
    {
        driveMode = DriveMode.kLockwheels;
       
        //updateOdometry();
        
        // SwerveModuleState[] swerveModuleStates = new SwerveModuleState[4];
        
        // // TODO: Check that this works
        // swerveModuleStates[0] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
        // swerveModuleStates[1] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
        // swerveModuleStates[2] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
        // swerveModuleStates[3] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));

        // SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constant.MAX_DRIVE_SPEED);
        // printDesiredStates(swerveModuleStates);

        // frontLeft.setDesiredState(swerveModuleStates[0]);
        // frontRight.setDesiredState(swerveModuleStates[1]);
        // backLeft.setDesiredState(swerveModuleStates[2]);
        // backRight.setDesiredState(swerveModuleStates[3]);

        //previousSwerveModuleStates = swerveModuleStates;

        //feedWatchdog();
    }



    /** Updates the field relative position of the robot. */
    
    // private void updateOdometry()
    // {
    //     //FIXME do we need to put this into periodic methods
    //     // periodicIO.odometry.update(
    //     //     gyro.getRotation2d(),
    //     //     new SwerveModulePosition[] 
    //     //     {
    //     //         frontLeft.getPosition(),
    //     //         frontRight.getPosition(),
    //     //         backLeft.getPosition(),
    //     //         backRight.getPosition()
    //     //     });
        
    //     System.out.format( "pose: X:%f Y:%f degrees %f\n"
    //     // a POSE has a TRANSLATION and a ROTATION
    //     // POSE can return directly the X and Y of the TRANSLATION but not the Degrees, Radians,
    //     // or trig functions of the ROTATION
    //     // pose: X:-0.565898 Y:-0.273620 degrees 137.436218
    //         ,periodicIO.odometry.getPoseMeters().getX()
    //         ,periodicIO.odometry.getPoseMeters().getY()
    //         ,periodicIO.odometry.getPoseMeters().getRotation().getDegrees()
    //     );
        
    // }
    


    public Translation2d getCurrentTranslation()
    {
        return periodicIO.odometry.getPoseMeters().getTranslation();
    }

    public double getDistanceDrivenMeters(Translation2d startingPosition)
    {
        return periodicIO.odometry.getPoseMeters().getTranslation().getDistance(startingPosition);
    }

    public void resetEncoders()
    {
        resetEncoders = true;
        // frontLeft.resetEncoders();
        // frontRight.resetEncoders();
        // backLeft.resetEncoders();
        // backRight.resetEncoders();
    }

    public void resetOdometry()
    {
        resetOdometry = true;
    }

    //@Override
    public void stopMotor()
    {
        driveMode = DriveMode.kStop;
        // frontLeft.stopModule();
        // frontRight.stopModule();
        // backLeft.stopModule();
        // backRight.stopModule();
        //feedWatchdog();
    }

    public void resetSlewRateLimiter()
    {
        adaptiveXRateLimiter.reset(0.0);
        adaptiveYRateLimiter.reset(0.0);
    }


    @Override
    public void readPeriodicInputs()
    {
        if(DriverStation.isAutonomousEnabled())
        {
            periodicIO.frontLeftPosition = frontLeft.getPosition();
            periodicIO.frontRightPosition =  frontRight.getPosition();
            periodicIO.backLeftPosition = backLeft.getPosition();
            periodicIO.backRightPosition = backRight.getPosition();
        }

    }

    @Override
    public void periodic()
    {
        switch (driveMode)
        {
            case kDrive:
        
                if(periodicIO.fieldRelative)
                    periodicIO.chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(periodicIO.xSpeed, periodicIO.ySpeed, periodicIO.turn, gyro.getRotation2d());
                else
                    periodicIO.chassisSpeeds = new ChassisSpeeds(periodicIO.xSpeed, periodicIO.ySpeed, periodicIO.turn);
                
                periodicIO.swerveModuleStates = kinematics.toSwerveModuleStates(periodicIO.chassisSpeeds);

                SwerveDriveKinematics.desaturateWheelSpeeds(periodicIO.swerveModuleStates, Constants.DrivetrainConstants.MAX_DRIVE_SPEED);
                break;

            case kLockwheels:

                periodicIO.swerveModuleStates = new SwerveModuleState[4];
            
                
                periodicIO.swerveModuleStates[0] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
                periodicIO.swerveModuleStates[1] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
                periodicIO.swerveModuleStates[2] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
                periodicIO.swerveModuleStates[3] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
                //System.out.println("drivetrain.Lockwheels");
                break;

            case kArcadeDrive:
                SwerveDriveKinematics.desaturateWheelSpeeds(periodicIO.swerveModuleStates, Constants.DrivetrainConstants.MAX_DRIVE_SPEED);
                break;

            case kStop:
                //No calculations to do
                break;

            
        }
    }


    @Override
    public void writePeriodicOutputs()
    {
        if (DriverStation.isDisabled() && resetOdometry)
        {
            periodicIO.odometry.resetPosition(
                new Rotation2d(), /*zero*/
                new SwerveModulePosition[]
                {/*zeros distance, angle*/
                    new SwerveModulePosition(),
                    new SwerveModulePosition(),
                    new SwerveModulePosition(),
                    new SwerveModulePosition()
                },
                new Pose2d(/*zeros facing X*/));
            resetOdometry = false;
        }
        else if (DriverStation.isAutonomousEnabled())
        {
            periodicIO.odometry.update(
                gyro.getRotation2d(),
                new SwerveModulePosition[] 
                {
                    periodicIO.frontLeftPosition,
                    periodicIO.frontRightPosition,
                    periodicIO.backLeftPosition,
                    periodicIO.backRightPosition
                });

                // System.out.println(gyro.getYaw());
        }

        if (resetEncoders)
        {   //FIXME do we need to add a time delay to reset the encoders?
            frontLeft.stopModule();
            frontRight.stopModule();
            backLeft.stopModule();
            backRight.stopModule();

            frontLeft.resetEncoders();
            frontRight.resetEncoders();
            backLeft.resetEncoders();
            backRight.resetEncoders();
        }
        else if (driveMode == DriveMode.kStop)
        {
            frontLeft.stopModule();
            frontRight.stopModule();
            backLeft.stopModule();
            backRight.stopModule();
        }
        else 
        {
            frontLeft.setDesiredState(periodicIO.swerveModuleStates[0]);
            frontRight.setDesiredState(periodicIO.swerveModuleStates[1]);
            backLeft.setDesiredState(periodicIO.swerveModuleStates[2]);
            backRight.setDesiredState(periodicIO.swerveModuleStates[3]);
        }
        

        feedWatchdog();
    }

    public void feedWatchdog() 
    {
        frontLeft.feed();
        backLeft.feed();
        frontRight.feed();
        backRight.feed();
    }


    /**
   * datalog
   */

   void logEncodersInit()
   {

    String EncoderName = new String("/SwerveEncoders/"); // make a prefix tree structure for the ultrasonic data
    // f front; b back; r right; l left; s steer; d drive
    periodicIO.flsLogEntry = new DoubleLogEntry(log, EncoderName+"fls", "RawCounts");
    periodicIO.frsLogEntry = new DoubleLogEntry(log, EncoderName+"frs", "RawCounts");
    periodicIO.blsLogEntry = new DoubleLogEntry(log, EncoderName+"bls", "RawCounts");
    periodicIO.brsLogEntry = new DoubleLogEntry(log, EncoderName+"brs", "RawCounts");
    periodicIO.fldLogEntry = new DoubleLogEntry(log, EncoderName+"fld", "RawCounts");
    periodicIO.frdLogEntry = new DoubleLogEntry(log, EncoderName+"frd", "RawCounts");
    periodicIO.bldLogEntry = new DoubleLogEntry(log, EncoderName+"bld", "RawCounts");
    periodicIO.brdLogEntry = new DoubleLogEntry(log, EncoderName+"brd", "RawCounts");
   }

  void logEncoders()
  {
    periodicIO.flsLogEntry.append(frontLeft.getTurningEncoderPosition());
    periodicIO.frsLogEntry.append(frontRight.getTurningEncoderPosition());
    periodicIO.blsLogEntry.append(backLeft.getTurningEncoderPosition());
    periodicIO.brsLogEntry.append(backRight.getTurningEncoderPosition());
    periodicIO.fldLogEntry.append(frontLeft.getDrivingEncoderRate());
    periodicIO.frdLogEntry.append(frontRight.getDrivingEncoderRate());
    periodicIO.bldLogEntry.append(backLeft.getDrivingEncoderRate());
    periodicIO.brdLogEntry.append(backRight.getDrivingEncoderRate());
  }

  public double fls()
  {
    return frontLeft.getTurningEncoderPosition();
  }

  public double frs()
  {
    return frontRight.getTurningEncoderPosition();
  }

  public double bls()
  {
    return backLeft.getTurningEncoderPosition();
  }

  public double brs()
  {
    return backRight.getTurningEncoderPosition();
  }


/**
 * drive with wheels fixed aligned to chassis
 * turning is accomplished by left and right wheels differing speeds
 * @param xSpeed robot speed -1 to +1
 * @param rotation angle of wheels and chassis -1 to +1
 */
    public void arcadeDrive(double xSpeed, double rotation, double moduleAngle)
    {
        driveMode = DriveMode.kArcadeDrive;

        //TEST THIS
        if(Math.abs(rotation) > 0.3)    //clamps rotation at -0.3 to 0.3
        {
            rotation = Math.copySign(0.3, rotation);
        }

        WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(xSpeed, rotation, false);

        periodicIO.swerveModuleStates = new SwerveModuleState[4];
        // double m_maxOutput = 2.;
        double maxOutput = Math.abs(xSpeed);
        
        //  assuming fl, fr, bl, br
        periodicIO.swerveModuleStates[0] = new SwerveModuleState(speeds.left * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        periodicIO.swerveModuleStates[1] = new SwerveModuleState(speeds.right * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        periodicIO.swerveModuleStates[2] = new SwerveModuleState(speeds.left * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        periodicIO.swerveModuleStates[3] = new SwerveModuleState(speeds.right * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        // periodicIO.swerveModuleStates[0] = new SwerveModuleState(speeds.left * maxOutput, Rotation2d.fromDegrees(0));
        // periodicIO.swerveModuleStates[1] = new SwerveModuleState(speeds.right * maxOutput, Rotation2d.fromDegrees(0));
        // periodicIO.swerveModuleStates[2] = new SwerveModuleState(speeds.left * maxOutput, Rotation2d.fromDegrees(0));
        // periodicIO.swerveModuleStates[3] = new SwerveModuleState(speeds.right * maxOutput, Rotation2d.fromDegrees(0));
        
    }

    /**
     * Drive a "straight" distance in meters
     * 
     * @param startingPosition of the robot
     * @param velocity in meters per second (+ forward, - reverse)
     * @param distanceToDrive in meters
     * @return true when drive is complete
     */
    // public boolean driveStraight(Translation2d startingPosition, double velocity, double distanceToDrive)
    // {
    //     boolean isDone = false;
    //     double distanceDriven = odometry.getPoseMeters().getTranslation().getDistance(startingPosition);
        
    //     updateOdometry();

    //     if(Math.abs(distanceDriven) < Math.abs(distanceToDrive))
    //     {
    //         drive(velocity, 0.0, 0.0, false);
    //     }
    //     else
    //     {
    //         stopMotor();
    //         isDone = true;
    //         // System.out.println("Dist (meters) = " + distanceDriven);
    //     }

    //     return isDone;
    // }

    /**
     * Drive a "vector" distance in meters
     * 
     * @param startingPosition of the robot
     * @param velocity in meters per second (+ forward, - reverse)
     * @param distanceToDriveX in meters
     * @param distanceToDriveY in meters
     * @return true when drive is complete
     */
    // public boolean driveVector(Translation2d startingPosition, double velocity, double distanceToDriveX, double distanceToDriveY)
    // {
    //     boolean isDone = false;

    //     double distanceToDrive = Math.sqrt(distanceToDriveX * distanceToDriveX + distanceToDriveY * distanceToDriveY);

    //     double velocityX = velocity * distanceToDriveX / distanceToDrive;
    //     double velocityY = velocity * distanceToDriveY / distanceToDrive;

    //     double distanceDriven = odometry.getPoseMeters().getTranslation().getDistance(startingPosition);

    //     double distanceToNearestEndpoint = Math.min(distanceDriven, distanceToDrive - distanceDriven);
    //     double maxVelocity = velocity;
    //     double minVelocity = 0.75;

    //     if (distanceToNearestEndpoint < 1.0)
    //     {
    //         velocityX *= distanceToNearestEndpoint / 1.0 * (maxVelocity - minVelocity) + minVelocity;
    //         velocityY *= distanceToNearestEndpoint / 1.0 * (maxVelocity - minVelocity) + minVelocity;
    //         System.out.println("DRIVE SPEED" + distanceToNearestEndpoint / 1.0 * (maxVelocity - minVelocity) + minVelocity);
    //     }

    //     updateOdometry();

    //     if(Math.abs(distanceDriven) < Math.abs(distanceToDrive))
    //     {
    //         drive(velocityX, velocityY, 0.0, true);
    //     }
    //     else
    //     {
    //         stopMotor();
    //         isDone = true;
    //         // System.out.println("Dist (meters) = " + distanceDriven);
    //     }

    //     return isDone;
    // }

    /**
     * Turn to an angle in degrees
     * 
     * @param minAngularVelocity the robot can turn
     * @param maxAngularVelocity the robot can turn
     * @param desiredAngle in degrees to turn to
     * @param angleThreshold in degrees
     * @return true when turn is complete
     */
    // public boolean turnToAngle(double minAngularVelocity, double maxAngularVelocity, double desiredAngle, double angleThreshold)
    // {
    //     boolean isDone = false;
    //     // double currentAngle = odometry.getPoseMeters().getRotation().getDegrees();
    //     double currentAngle = gyro.getYaw();
    //     double angleToTurn = (desiredAngle - currentAngle) % 360;

    //     if (angleToTurn <= -180.0)
    //     {
    //         angleToTurn += 360.0;
    //     }
    //     else if (angleToTurn > 180.0)
    //     {
    //         angleToTurn -= 360.0;
    //     }

    //     System.out.println("ANGLE TO TURN: " + angleToTurn);
        
    //     updateOdometry();

    //     if(!isAtAngle(desiredAngle, angleThreshold))
    //     {
    //         //proportion of how close the speed will be to the max speed from the min speed, so it doesn't exceed the max speed
    //         double turnSpeedProportion = angleToTurn / 30.0;

    //         if (Math.abs(turnSpeedProportion) > 1.0)
    //         {
    //             turnSpeedProportion = Math.signum(turnSpeedProportion);
    //         }

    //         // Use calculateTurnRotation
    //         drive(0.0, 0.0, turnSpeedProportion * (maxAngularVelocity - minAngularVelocity) + minAngularVelocity * Math.signum(angleToTurn), true);
    //     }
    //     else
    //     {
    //         stopMotor();
    //         isDone = true;
    //         // System.out.println("Angle turned (degrees) = ");
    //     }

    //     return isDone;
    //}

    /**
     * Drive a "vector" distance in meters and rotate to desired angle
     * 
     * @param startingPosition of the robot
     * @param velocity in meters per second (+ forward, - reverse)
     * @param distanceToDriveX in meters
     * @param distanceToDriveY in meters
     * @param desiredAngle in degrees in navigatioanl position
     * @return true when drive is complete
     */
    // public boolean driveVectorAndTurnToAngle(Translation2d startingPosition, double velocity, double distanceToDriveX, double distanceToDriveY, double minAngularVelocity, double maxAngularVelocity, double desiredAngle, double angleThreshold)
    // {
    //     boolean isDone = false;

    //     double distanceToDrive = Math.sqrt(distanceToDriveX * distanceToDriveX + distanceToDriveY * distanceToDriveY);

    //     double velocityX = velocity * distanceToDriveX / distanceToDrive;
    //     double velocityY = velocity * distanceToDriveY / distanceToDrive;

    //     double distanceDriven = odometry.getPoseMeters().getTranslation().getDistance(startingPosition);

    //     double distanceToNearestEndpoint = Math.min(distanceDriven, distanceToDrive - distanceDriven);
    //     double maxVelocity = velocity;
    //     double minVelocity = 0.75;

    //     if (distanceToNearestEndpoint < 1.0)
    //     {
    //         velocityX *= distanceToNearestEndpoint / 1.0 * (maxVelocity - minVelocity) + minVelocity;
    //         velocityY *= distanceToNearestEndpoint / 1.0 * (maxVelocity - minVelocity) + minVelocity;
    //         System.out.println("DRIVE SPEED" + distanceToNearestEndpoint / 1.0 * (maxVelocity - minVelocity) + minVelocity);
    //     }

    //     updateOdometry();

    //     if(Math.abs(distanceDriven) < Math.abs(distanceToDrive) && !isAtAngle(desiredAngle, angleThreshold))
    //     {
    //         drive(velocityX, velocityY, calculateTurnRotation(minAngularVelocity, maxAngularVelocity, desiredAngle, angleThreshold), true);
    //     }
    //     else
    //     {
    //         stopMotor();
    //         isDone = true;
    //         // System.out.println("Dist (meters) = " + distanceDriven);
    //     }

    //     return isDone;
    // }

    // public double calculateTurnRotation(double minAngularVelocity, double maxAngularVelocity, double desiredAngle, double angleThreshold)
    // {
    //     // double currentAngle = odometry.getPoseMeters().getRotation().getDegrees();
    //     double currentAngle = gyro.getYaw();
    //     double angleToTurn = (desiredAngle - currentAngle) % 360;
    //     double angularVelocity;

    //     if (angleToTurn <= -180.0)
    //     {
    //         angleToTurn += 360.0;
    //     }
    //     else if (angleToTurn > 180.0)
    //     {
    //         angleToTurn -= 360.0;
    //     }

    //     System.out.println("ANGLE TO TURN: " + angleToTurn);

    //     if(!isAtAngle(desiredAngle, angleThreshold))
    //     {
    //         //proportion of how close the speed will be to the max speed from the min speed, so it doesn't exceed the max speed
    //         double turnSpeedProportion = angleToTurn / 30.0;

    //         if (Math.abs(turnSpeedProportion) > 1.0)
    //         {
    //             turnSpeedProportion = Math.signum(turnSpeedProportion);
    //         }

    //         angularVelocity = turnSpeedProportion * (maxAngularVelocity - minAngularVelocity) + minAngularVelocity * Math.signum(angleToTurn);
    //     }
    //     else
    //     {
    //         angularVelocity = 0.0;
    //         // stopMotor();
    //         // isDone = true;
    //         // System.out.println("Angle turned (degrees) = ");
    //     }

    //     return angularVelocity;
    // }

    // public boolean isAtAngle(double desiredAngle, double angleThreshold)
    // {
    //     double currentAngle = gyro.getYaw();
    //     double angleToTurn = (currentAngle - desiredAngle) % 360.0;

    //     if (angleToTurn <= -180.0)
    //     {
    //         angleToTurn += 360.0;
    //     }
    //     else if (angleToTurn > 180.0)
    //     {
    //         angleToTurn -= 360.0;
    //     }
    //     System.out.println("CURRENT ANGLE: " + currentAngle);
    //     System.out.println("DESIRED ANGLE: " + desiredAngle);

    //     return (Math.abs(angleToTurn) < angleThreshold);
    // }


}