package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathHolonomic;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

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
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.SwerveModuleSetup;
import frc.robot.Constants.TeamColor;
import frc.robot.controls.AdaptiveSlewRateLimiter;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;


/** Represents a swerve drive style drivetrain. */
public class Drivetrain extends Subsystem4237 
{
    // This string gets the full name of the class, including the package name
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
    private class PeriodicData 
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


        private DoubleLogEntry fltLogEntry;
        private DoubleLogEntry frtLogEntry;
        private DoubleLogEntry bltLogEntry;
        private DoubleLogEntry brtLogEntry;
        private DoubleLogEntry fldLogEntry;
        private DoubleLogEntry frdLogEntry;
        private DoubleLogEntry bldLogEntry;
        private DoubleLogEntry brdLogEntry;
        
        // OUTPUTS
        private ChassisSpeeds chassisSpeeds;
        private SwerveModuleState[] swerveModuleStates;
        private SwerveModuleState[] targetStatesPP;
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
    private final Gyro4237 gyro; //Pigeon2
    private final Camera[] cameraArray;
    private boolean usePoseEstimator;
    private boolean useDataLog = true;
    private final DataLog log;
    private final SwerveDriveKinematics kinematics;
    private final PoseEstimator poseEstimator;

    private final AdaptiveSlewRateLimiter adaptiveXRateLimiter = new AdaptiveSlewRateLimiter(DrivetrainConstants.X_ACCELERATION_RATE_LIMT, DrivetrainConstants.X_DECELERATION_RATE_LIMT);
    private final AdaptiveSlewRateLimiter adaptiveYRateLimiter = new AdaptiveSlewRateLimiter(DrivetrainConstants.Y_ACCELERATION_RATE_LIMT, DrivetrainConstants.Y_DECELERATION_RATE_LIMT);

    private final Translation2d frontLeftLocation = new Translation2d(DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    private final Translation2d frontRightLocation = new Translation2d(DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, -DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    private final Translation2d backLeftLocation = new Translation2d(-DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    private final Translation2d backRightLocation = new Translation2d(-DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, -DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    
    private final SwerveModuleConfig frontLeftSwerveModule = new SwerveModuleConfig(
        "Front Left", frontLeftLocation, Constants.Drivetrain.FRONT_LEFT_DRIVE, true, Constants.Drivetrain.FRONT_LEFT_ENCODER, SwerveModuleSetup.FRONT_LEFT_ENCODER_OFFSET, Constants.Drivetrain.FRONT_LEFT_TURN);
    private final SwerveModuleConfig frontRightSwerveModule = new SwerveModuleConfig(
        "Front Right", frontRightLocation, Constants.Drivetrain.FRONT_RIGHT_DRIVE, false, Constants.Drivetrain.FRONT_RIGHT_ENCODER, SwerveModuleSetup.FRONT_RIGHT_ENCODER_OFFSET, Constants.Drivetrain.FRONT_RIGHT_TURN);
    private final SwerveModuleConfig backLeftSwerveModule = new SwerveModuleConfig(
        "Back Left", backLeftLocation, Constants.Drivetrain.BACK_LEFT_DRIVE, true, Constants.Drivetrain.BACK_LEFT_ENCODER, SwerveModuleSetup.BACK_LEFT_ENCODER_OFFSET, Constants.Drivetrain.BACK_LEFT_TURN);
    private final SwerveModuleConfig backRightSwerveModule = new SwerveModuleConfig(
        "Back Right", backRightLocation, Constants.Drivetrain.BACK_RIGHT_DRIVE, false, Constants.Drivetrain.BACK_RIGHT_ENCODER, SwerveModuleSetup.BACK_RIGHT_ENCODER_OFFSET, Constants.Drivetrain.BACK_RIGHT_TURN); 
    
    private final SwerveModule frontLeft;
    private final SwerveModule frontRight;
    private final SwerveModule backLeft;
    private final SwerveModule backRight;
    // private final Module frontLeft;
    // private final Module frontRight;
    // private final Module backLeft;
    // private final Module backRight;

    // TODO: Make final by setting to an initial stopped state
    //private SwerveModuleState[] previousSwerveModuleStates = null;
    private DriveMode driveMode = DriveMode.kDrive;
    private boolean resetEncoders = false;
    private boolean resetOdometry = false;

    private double shootingAngle = 0.0;
    private TeamColor teamColor = TeamColor.kBlue;

    private PeriodicData periodicData;
    
    // *** CLASS CONSTRUCTOR ***
    public Drivetrain(Gyro4237 gyro, DataLog log, Camera[] cameraArray, boolean usePoseEstimator)//, DriverController driverController)
    {
        super("Drivetrain");
        System.out.println("  Constructor Started:  " + fullClassName);

        // DrivetrainConfig dd = Constants.DrivetrainSetup.DRIVETRAIN_DATA;
        // super();  // call the RobotDriveBase constructor
        // setSafetyEnabled(false);
  
  
        periodicData = new PeriodicData(); // all the periodic I/O appear here
        
        this.gyro = gyro;
        this.cameraArray = cameraArray;
        this.log = log;
        this.usePoseEstimator = usePoseEstimator;
        if(log == null)
        {
            useDataLog = false;
        }

        if(useDataLog)
        {
            logEncodersInit();
        }




        // frontLeft = new SwerveModule(dd.frontLeftSwerveModule);
        // frontRight = new SwerveModule(dd.frontRightSwerveModule);
        // backLeft = new SwerveModule(dd.backLeftSwerveModule);
        // backRight = new SwerveModule(dd.backRightSwerveModule);
        // frontLeft = new SwerveModule(FRONT_LEFT_SWERVE_MODULE);
        // frontRight = new SwerveModule(FRONT_RIGHT_SWERVE_MODULE);
        // backLeft = new SwerveModule(BACK_LEFT_SWERVE_MODULE);
        // backRight = new SwerveModule(BACK_RIGHT_SWERVE_MODULE);

        // if(Constants.ROBOT_NAME_4237.equals("2023 Robot"))
        // {
        //     frontLeft = new SwerveModule2023(FRONT_LEFT_SWERVE_MODULE);
        //     frontRight = new SwerveModule2023(FRONT_RIGHT_SWERVE_MODULE);
        //     backLeft = new SwerveModule2023(BACK_LEFT_SWERVE_MODULE);
        //     backRight = new SwerveModule2023(BACK_RIGHT_SWERVE_MODULE);
        // }
        // else if(Constants.ROBOT_NAME_4237.equals("2022 Robot"))
        // {
        //     frontLeft = new SwerveModule2022(FRONT_LEFT_SWERVE_MODULE);
        //     frontRight = new SwerveModule2022(FRONT_RIGHT_SWERVE_MODULE);
        //     backLeft = new SwerveModule2022(BACK_LEFT_SWERVE_MODULE);
        //     backRight = new SwerveModule2022(BACK_RIGHT_SWERVE_MODULE);
        // }
        // else
        // {
        //     System.out.println("Unknown Robot " + Constants.ROBOT_NAME_4237);
        //     frontLeft = null;
        //     frontRight = null;
        //     backLeft = null;
        //     backRight = null;
        // }
        
        
        frontLeft = new SwerveModule(frontLeftSwerveModule);
        frontRight = new SwerveModule(frontRightSwerveModule);
        backLeft = new SwerveModule(backLeftSwerveModule);
        backRight = new SwerveModule(backRightSwerveModule);

        // gyro = new WPI_Pigeon2(Port.Sensor.PIGEON, Port.Motor.CAN_BUS);

        // kinematics = new SwerveDriveKinematics(
        //     dd.frontLeftSwerveModule.moduleLocation,
        //     dd.frontRightSwerveModule.moduleLocation,
        //     dd.backLeftSwerveModule.moduleLocation,
        //     dd.backRightSwerveModule.moduleLocation);

        kinematics = new SwerveDriveKinematics(
            frontLeftLocation,
            frontRightLocation,
            backLeftLocation,
            backRightLocation);

        periodicData.odometry = new SwerveDriveOdometry(
            kinematics, 
            gyro.getRotation2d(),
            new SwerveModulePosition[] 
            {
                frontLeft.getPosition(),
                frontRight.getPosition(),
                backLeft.getPosition(),
                backRight.getPosition()
            });

        
        poseEstimator = (usePoseEstimator) ? new PoseEstimator(this, gyro, cameraArray) : null;

        // setSafetyEnabled(true);

        // configureAutoBuilder();

        System.out.println("  Constructor Finished: " + fullClassName);
    }

    public void configureAutoBuilder()
    {
        AutoBuilder.configureHolonomic(
                this::getPose, // Robot pose supplier
                this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getRobotRelativeSpeedsForPP, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                this::driveRobotRelative, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
                new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
                        Constants.DrivetrainConstants.MAX_DRIVE_SPEED, // Max module speed, in m/s
                        0.417, // Drive base radius in meters. Distance from robot center to furthest module.
                        new ReplanningConfig() // Default path replanning config. See the API for the options here
                ),
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this // Reference to this subsystem to set requirements
        );
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
        periodicData.xSpeed = xSpeed;
        periodicData.ySpeed = ySpeed;

        // commented out for driver practice/tryouts
        // periodicData.xSpeed = adaptiveXRateLimiter.calculate(xSpeed);
        // periodicData.ySpeed = adaptiveYRateLimiter.calculate(ySpeed);

        periodicData.turn = turn;
        periodicData.fieldRelative = fieldRelative;

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
    
    public Pose2d getPose()
    {
        return periodicData.odometry.getPoseMeters();
    }

    public void rotateForShooting()
    {
        // if(teamColor == TeamColor.kRed)
            // shootingAngle = poseEstimator.getAngleToRedSpeaker();

        // if(teamColor == TeamColor.kBlue)
            // shootingAngle = poseEstimator.getAngleToBlueSpeaker();

        drive(0.0, 0.0, shootingAngle, false);
    }

    public SwerveModulePosition[] getSwerveModulePositions()
    {
        return new SwerveModulePosition[] 
            {
                frontLeft.getPosition(),
                frontRight.getPosition(),
                backLeft.getPosition(),
                backRight.getPosition()
            };
    }

    public SwerveDriveKinematics getKinematics()
    {
        return kinematics;
    }

    public Translation2d getCurrentTranslation()
    {
        return periodicData.odometry.getPoseMeters().getTranslation();
    }

    public double getDistanceDrivenMeters(Translation2d startingPosition)
    {
        return periodicData.odometry.getPoseMeters().getTranslation().getDistance(startingPosition);
    }

    public ChassisSpeeds getRobotRelativeSpeedsForPP()
    {
        SwerveModuleState[] moduleStates = new SwerveModuleState[] 
            {
                frontLeft.getState(),
                frontRight.getState(),
                backLeft.getState(),
                backRight.getState()
            }; 
        
        return kinematics.toChassisSpeeds(moduleStates);

        // return periodicData.chassisSpeeds;

        // System.out.println(new ChassisSpeeds(periodicData.xSpeed, periodicData.ySpeed, periodicData.turn));
        // return new ChassisSpeeds(periodicData.xSpeed, periodicData.ySpeed, periodicData.turn);
    }

    public double getAngleToBlueSpeaker()
    {
        return poseEstimator.getAngleToBlueSpeaker();
    }

    public void driveRobotRelative(ChassisSpeeds chassisSpeeds)
    {
        // ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(chassisSpeeds, 0.02);
        // System.out.println(targetSpeeds);
        // periodicData.targetStatesPP = kinematics.toSwerveModuleStates(targetSpeeds);
        // SwerveDriveKinematics.desaturateWheelSpeeds(periodicData.targetStatesPP, Constants.DrivetrainConstants.MAX_DRIVE_SPEED);
        // System.out.println(periodicData.targetStatesPP);

        // frontLeft.setDesiredState(targetStates[0]);
        // frontRight.setDesiredState(targetStates[1]);
        // backLeft.setDesiredState(targetStates[2]);
        // backRight.setDesiredState(targetStates[3]);
        
        System.out.println(chassisSpeeds);
        driveMode = DriveMode.kDrive;
        periodicData.fieldRelative = false;
        // periodicData.chassisSpeeds = chassisSpeeds;
        periodicData.swerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(periodicData.swerveModuleStates, Constants.DrivetrainConstants.MAX_DRIVE_SPEED);
        // System.out.println(periodicData.swerveModuleStates);
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

    public void resetPose(Pose2d newPose)
    {
        periodicData.odometry.resetPosition(
            gyro.getRotation2d(), 
            new SwerveModulePosition[] 
            {
                frontLeft.getPosition(),
                frontRight.getPosition(),
                backLeft.getPosition(),
                backRight.getPosition()
            }, 
            newPose);
    }

    //@Override
    public void stopMotors()
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

    public Command driveCommand(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn, DoubleSupplier scale)
    {
        return this.runOnce(
            () -> drive(
                xSpeed.getAsDouble() * scale.getAsDouble(),
                ySpeed.getAsDouble() * scale.getAsDouble(),
                turn.getAsDouble(), 
                true));
    }

    @Override
    public void readPeriodicInputs()
    {
        if(DriverStation.isAutonomousEnabled())
        {
            periodicData.frontLeftPosition = frontLeft.getPosition();
            periodicData.frontRightPosition =  frontRight.getPosition();
            periodicData.backLeftPosition = backLeft.getPosition();
            periodicData.backRightPosition = backRight.getPosition();
        }

    }

    @Override
    public void periodic()
    {
        switch (driveMode)
        {
            case kDrive:

                // System.out.println(periodicData.chassisSpeeds);

                if(periodicData.fieldRelative)
                    periodicData.chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(periodicData.xSpeed, periodicData.ySpeed, periodicData.turn, gyro.getRotation2d());
                else
                    periodicData.chassisSpeeds = new ChassisSpeeds(periodicData.xSpeed, periodicData.ySpeed, periodicData.turn);

                // System.out.println(periodicData.chassisSpeeds);

                periodicData.swerveModuleStates = kinematics.toSwerveModuleStates(periodicData.chassisSpeeds);

                // System.out.println(periodicData.swerveModuleStates[0] + "   "
                // + periodicData.swerveModuleStates[1] + "   "
                // + periodicData.swerveModuleStates[2] + "   "
                // + periodicData.swerveModuleStates[3]);

                SwerveDriveKinematics.desaturateWheelSpeeds(periodicData.swerveModuleStates, Constants.DrivetrainConstants.MAX_DRIVE_SPEED);
                break;

            case kLockwheels:

                periodicData.swerveModuleStates = new SwerveModuleState[4];
            
                
                periodicData.swerveModuleStates[0] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
                periodicData.swerveModuleStates[1] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
                periodicData.swerveModuleStates[2] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(135));
                periodicData.swerveModuleStates[3] = new SwerveModuleState(0.0, Rotation2d.fromDegrees(45));
                //System.out.println("drivetrain.Lockwheels");
                break;

            case kArcadeDrive:
                SwerveDriveKinematics.desaturateWheelSpeeds(periodicData.swerveModuleStates, Constants.DrivetrainConstants.MAX_DRIVE_SPEED);
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
            periodicData.odometry.resetPosition(
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
            periodicData.odometry.update(
                gyro.getRotation2d(),
                new SwerveModulePosition[] 
                {
                    periodicData.frontLeftPosition,
                    periodicData.frontRightPosition,
                    periodicData.backLeftPosition,
                    periodicData.backRightPosition
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
            // System.out.println(periodicData.swerveModuleStates[0] + "   "
            // + periodicData.swerveModuleStates[1] + "   "
            // + periodicData.swerveModuleStates[2] + "   "
            // + periodicData.swerveModuleStates[3]);
            frontLeft.setDesiredState(periodicData.swerveModuleStates[0]);
            frontRight.setDesiredState(periodicData.swerveModuleStates[1]);
            backLeft.setDesiredState(periodicData.swerveModuleStates[2]);
            backRight.setDesiredState(periodicData.swerveModuleStates[3]);
            // frontLeft.setDesiredState(periodicData.targetStatesPP[0]);
            // frontRight.setDesiredState(periodicData.targetStatesPP[1]);
            // backLeft.setDesiredState(periodicData.targetStatesPP[2]);
            // backRight.setDesiredState(periodicData.targetStatesPP[3]);

            
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
    periodicData.fltLogEntry = new DoubleLogEntry(log, EncoderName+"flt", "RawCounts");
    periodicData.frtLogEntry = new DoubleLogEntry(log, EncoderName+"frt", "RawCounts");
    periodicData.bltLogEntry = new DoubleLogEntry(log, EncoderName+"blt", "RawCounts");
    periodicData.brtLogEntry = new DoubleLogEntry(log, EncoderName+"brt", "RawCounts");
    periodicData.fldLogEntry = new DoubleLogEntry(log, EncoderName+"fld", "RawCounts");
    periodicData.frdLogEntry = new DoubleLogEntry(log, EncoderName+"frd", "RawCounts");
    periodicData.bldLogEntry = new DoubleLogEntry(log, EncoderName+"bld", "RawCounts");
    periodicData.brdLogEntry = new DoubleLogEntry(log, EncoderName+"brd", "RawCounts");
   }

  void logEncoders()
  {
    periodicData.fltLogEntry.append(frontLeft.getTurningEncoderPosition());
    periodicData.frtLogEntry.append(frontRight.getTurningEncoderPosition());
    periodicData.bltLogEntry.append(backLeft.getTurningEncoderPosition());
    periodicData.brtLogEntry.append(backRight.getTurningEncoderPosition());
    periodicData.fldLogEntry.append(frontLeft.getDrivingEncoderRate());
    periodicData.frdLogEntry.append(frontRight.getDrivingEncoderRate());
    periodicData.bldLogEntry.append(backLeft.getDrivingEncoderRate());
    periodicData.brdLogEntry.append(backRight.getDrivingEncoderRate());
  }

  public double flt()
  {
    return frontLeft.getTurningEncoderPosition();
  }

  public double frt()
  {
    return frontRight.getTurningEncoderPosition();
  }

  public double blt()
  {
    return backLeft.getTurningEncoderPosition();
  }

  public double brt()
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

        periodicData.swerveModuleStates = new SwerveModuleState[4];
        // double m_maxOutput = 2.;
        double maxOutput = Math.abs(xSpeed);
        
        //  assuming fl, fr, bl, br
        periodicData.swerveModuleStates[0] = new SwerveModuleState(speeds.left * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        periodicData.swerveModuleStates[1] = new SwerveModuleState(speeds.right * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        periodicData.swerveModuleStates[2] = new SwerveModuleState(speeds.left * maxOutput, Rotation2d.fromDegrees(moduleAngle));
        periodicData.swerveModuleStates[3] = new SwerveModuleState(speeds.right * maxOutput, Rotation2d.fromDegrees(moduleAngle));
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

    public Command followPathCommand(String pathName) {
        PathPlannerPath path = PathPlannerPath.fromPathFile(pathName);

        resetPose(path.getPreviewStartingHolonomicPose());

        return new FollowPathHolonomic(
                path,
                poseEstimator::getEstimatedPose, // Robot pose supplier
                this::getRobotRelativeSpeedsForPP, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                this::driveRobotRelative, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
                new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
                        Constants.DrivetrainConstants.MAX_DRIVE_SPEED, // Max module speed, in m/s
                        0.417, // Drive base radius in meters. Distance from robot center to furthest module.
                        new ReplanningConfig() // Default path replanning config. See the API for the options here
                ),
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this // Reference to this subsystem to set requirements
        );
    }


}