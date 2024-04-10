// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// import java.io.IOException;
import java.lang.invoke.MethodHandles;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.RobotController;
// import frc.robot.subsystems.DrivetrainConfig;
// import frc.robot.subsystems.SwerveModuleConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    
    public static final String NETWORK_TABLE_NAME = "Team4237";
    public static final String ADVANTAGE_SCOPE_TABLE_NAME = "ASTable";

    // These are the names of the CAN bus set on the roboRIO and CANivore
    public static final String CANIVORE = "CANivore";
    public static final String ROBORIO  = "rio";

    public static final double MAX_BATTERY_VOLTAGE          = 12.0;
    public static final double END_OF_MATCH_BATTERY_VOLTAGE = 11.5; // This is the estimated voltage at the end of each match, used in subsystems with setVoltage()

    // Set the name of the robot
    private static final String comment = RobotController.getComments();
    private static String robotName4237 = "";
    // public static double DRIVETRAIN_WHEELBASE_METERS  ; 
    // public static double DRIVETRAIN_TRACKWIDTH_METERS ; 

    static
    {
        System.out.println("Loading: " + fullClassName);
        //// start get roboRIO comment
        /*
        roboRIO dashboard reads:
        2023 Robot

        prints from here:
        The roboRIO comment is >PRETTY_HOSTNAME="2023 Robot"
        <
        Notice an extra free control character that made a new line
        */
        // final Path commentPath = Path.of("/etc/machine-info");
        // String comment = "";
        // try 
        // {  
        //     comment = Files.readString(commentPath);
        //     System.out.println("The roboRIO comment is >" + comment + "<");
        // } 
        // catch (IOException e) 
        // {
        //     // Couldn't read the file
        //     System.out.println(e);
        // }

        // final String comment = RobotController.getComments();
        System.out.println("The roboRIO comment is >" + comment + "<");
      
        // Use the comment variable to decide what to do
        
        if (comment.contains("2024 Robot"))
        {
            robotName4237 = "2024 Robot";
        }
        else if(comment.contains("2023 Robot"))
        {
            robotName4237 = "2023 Robot";
        }
        else if (comment.contains("2022 Robot"))
        { 
            robotName4237 = "2022 Robot";
        }
        else 
        {
            System.out.println("Unknown Robot " + comment);
        }

        System.out.println("Robot:" + robotName4237);
        // end get roboRIO comment
    }

    public static final String ROBOT_NAME_4237 = robotName4237;

    public static class AmpAssist
    {
        public static final int SOLENOID_PORT       = 1;
        public static final int OUT_POSITION        = 5;
        public static final int IN_POSITION         = 4;

        // public static final String MOTOR_CAN_BUS    = ROBORIO;
        // public static final int OUT_SOFT_LIMIT      = 4237;
        // public static final int IN_SOFT_LIMIT       = 0;
    }

    public static class Camera
    {
        public static final String CAMERA_1 = "limelight-one"; // Inside robot, on the right
        public static final String CAMERA_2 = "limelight-two";  // Inside robot, on the left
        public static final String CAMERA_3 = "limelight-three";    // Inside robot, on shooter side (back)
        public static final String CAMERA_4 = "limelight-four";     // Inside robot, on intake side (front)

        public static final String BOT_POSE = "botpose_wpiblue";

        public static final String CAMERA_1_BOT_POSE = CAMERA_1 + "/" + BOT_POSE;
        public static final String CAMERA_2_BOT_POSE = CAMERA_2 + "/" + BOT_POSE;
        public static final String CAMERA_3_BOT_POSE = CAMERA_3 + "/" + BOT_POSE;
        public static final String CAMERA_4_BOT_POSE = CAMERA_4 + "/" + BOT_POSE;
    }

    public static class Candle
    {
        public static final int PORT            = 1;
        public static final String CAN_BUS      = CANIVORE;
    }

    public static class Climb
    {
        public static final int LEFT_MOTOR_PORT          = 13;
        public static final int RIGHT_MOTOR_PORT         = 14;
        public static final String LEFT_MOTOR_CAN_BUS    = ROBORIO;
        public static final String RIGHT_MOTOR_CAN_BUS   = ROBORIO;
    }

    public static class Controller
    {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }

    public static class Drivetrain
    {
        // The CAN bus to use depends on the robot
        private static String cancoderCanBus = "";
        private static String motorCanBus = "";

        static
        {
            if(robotName4237.equals("2024 Robot"))
            {
                cancoderCanBus = CANIVORE;
                motorCanBus = ROBORIO;
            }
            else if(robotName4237.equals("2023 Robot"))
            {
                cancoderCanBus = CANIVORE;
                motorCanBus = CANIVORE;
            }
            else if(robotName4237.equals("2022 Robot"))
            {
                cancoderCanBus = CANIVORE;
                motorCanBus = ROBORIO;
            }
            else
            {
                System.out.println("Unknown Robot " + robotName4237);
            }
        }

        public static final int FRONT_LEFT_DRIVE_PORT       = 7;
        public static final int FRONT_LEFT_ENCODER_PORT     = 8;  
        public static final int FRONT_LEFT_TURN_PORT        = 9;  

        public static final int FRONT_RIGHT_DRIVE_PORT      = 10;
        public static final int FRONT_RIGHT_ENCODER_PORT    = 11;  
        public static final int FRONT_RIGHT_TURN_PORT       = 12;  

        public static final int BACK_LEFT_DRIVE_PORT        = 4; 
        public static final int BACK_LEFT_ENCODER_PORT      = 5; 
        public static final int BACK_LEFT_TURN_PORT         = 6;  

        public static final int BACK_RIGHT_DRIVE_PORT       = 1; 
        public static final int BACK_RIGHT_ENCODER_PORT     = 2; 
        public static final int BACK_RIGHT_TURN_PORT        = 3;

        public static final String CANCODER_CAN_BUS         = cancoderCanBus;
        public static final String MOTOR_CAN_BUS            = motorCanBus;
    }

    public static class DrivetrainConstants
    {
        public static final double INCHES_TO_METERS = 0.0254;

        // The Wheelbase, Trackwidth, and Encoder Resolution to use depends on the robot
        private static double drivetrainWheelbaseMeters = 0.0;   // Front to Back
        private static double drivetrainTrackwidthMeters = 0.0;  // Side to Side
        private static int driveMotorEncoderResolution = 0;      // Falcon500 = 2048, Neo1650 = 42
        
        static
        {
            System.out.println("Loading: " + MethodHandles.lookup().lookupClass().getCanonicalName());

            if(robotName4237.equals("2024 Robot"))
            {
                drivetrainWheelbaseMeters   = 23.5 * INCHES_TO_METERS; // Front to back
                drivetrainTrackwidthMeters  = 23.5 * INCHES_TO_METERS; // Side to side
                driveMotorEncoderResolution = 1; // Neo1650
            }
            else if(robotName4237.equals("2023 Robot"))
            {
                drivetrainWheelbaseMeters   = 27.44 * INCHES_TO_METERS; // Front to back
                drivetrainTrackwidthMeters  = 19.50 * INCHES_TO_METERS; // Side to side
                driveMotorEncoderResolution = 2048;  // Falcon500
            }
            else if(robotName4237.equals("2022 Robot"))
            {
                drivetrainWheelbaseMeters   = 23.5 * INCHES_TO_METERS; // Front to back
                drivetrainTrackwidthMeters  = 23.5 * INCHES_TO_METERS; // Side to side
                driveMotorEncoderResolution = 1;  //42 // Neo1650
            }
            else
            {
                System.out.println("Unknown Robot " + robotName4237);
            }
        }  

        public static final double DRIVETRAIN_WHEELBASE_METERS  = drivetrainWheelbaseMeters;   // Front to back
        public static final double DRIVETRAIN_TRACKWIDTH_METERS = drivetrainTrackwidthMeters; // Side to side
        public static final int DRIVE_MOTOR_ENCODER_RESOLUTION  = driveMotorEncoderResolution;

        public static final double MAX_MODULE_TURN_SPEED        = 1080.0; // degrees per second, this is 3.0 rev/sec, used to be 1980 and 5.5 rev/sec
        public static final double MAX_MODULE_TURN_ACCELERATION = 1728.0; // degrees per second per second, this is 4.8 rev/sec^2, used to be 17280 and 48 rev/sec^2
        

        public static final double DRIVE_MOTOR_GEAR_RATIO = 6.75; // L1: 8.14;
        public static final double WHEEL_RADIUS_METERS = 2.0 * INCHES_TO_METERS;
        public static final double DRIVE_ENCODER_RATE_TO_METERS_PER_SEC = 
            ((10.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
        public static final double DRIVE_ENCODER_POSITION_TO_METERS =
            ((1.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
        public static final double MAX_DRIVE_SPEED = 10.0; //4.4; // meters per second

        public static final double X_ACCELERATION_RATE_LIMT = 10.0;
        public static final double X_DECELERATION_RATE_LIMT = 10.0;
        public static final double Y_ACCELERATION_RATE_LIMT = 10.0;
        public static final double Y_DECELERATION_RATE_LIMT = 10.0;
    }

    // public static class DrivetrainSetup
    // {
    //     static
    //     {
    //         System.out.println("Loading: " + MethodHandles.lookup().lookupClass().getCanonicalName());
    //     }

    //     public static final DrivetrainConfig DRIVETRAIN_DATA = new DrivetrainConfig(
    //         SwerveModuleSetup.FRONT_LEFT, SwerveModuleSetup.FRONT_RIGHT, SwerveModuleSetup.BACK_LEFT, SwerveModuleSetup.BACK_RIGHT);
    // }

    public static class Flywheel
    {
        public static final int MOTOR_PORT          = 51;
        public static final String MOTOR_CAN_BUS    = CANIVORE;
    }

    public static class Gyro 
    {
        public static final int PIGEON_PORT         = 0;
        public static final String PIGEON_CAN_BUS   = CANIVORE;

        // public static final AxisDirection FORWARD_AXIS = AxisDirection.PositiveX;
        // public static final AxisDirection UP_AXIS = AxisDirection.PositiveZ;
    }

    public static class Index
    {
        public static final int MOTOR_PORT          = 52;
        public static final String MOTOR_CAN_BUS    = CANIVORE;
    }

    public static class Intake
    {
        public static final int TOP_MOTOR_PORT           = 15;
        public static final int BOTTOM_MOTOR_PORT        = 16;
        public static final String TOP_MOTOR_CAN_BUS     = ROBORIO;
        public static final String BOTTOM_MOTOR_CAN_BUS  = ROBORIO;
    }

    public static class IntakePositioning
    {
        public static final int PCM_PORT                = 1;
        public static final int EXTEND_ACTIVE_PORT      = 0;
        public static final int EXTEND_FLOAT_PORT       = 1;
        public static final int RETRACT_ACTIVE_PORT     = 2;
        public static final int RETRACT_FLOAT_PORT      = 3;

        public static final int INTAKE_DOWN_SENSOR      = 6;
        public static final int INTAKE_UP_SENSOR        = 7;
    }

    public static class Pivot
    {
        public static final int MOTOR_PORT          = 53; //Will be changed to 53
        public static final String MOTOR_CAN_BUS    = CANIVORE;
        public static final int CANCODER_PORT       = 20;
        public static final String CANCODER_CAN_BUS = CANIVORE;

        // public static final double DEFAULT_ANGLE = 30.0;
        // public static final double INTAKE_FROM_SOURCE_ANGLE = 50.0; 
        // public static final double SHOOT_TO_AMP_ANGLE = 59.5;       
    }

    public static class PowerDistributionHub
    {
        public static final int PDH_PORT    = 1;
        public static final String CAN_BUS  = ROBORIO;
    }

    public static class Proximity
    {
        public static final int FIRST_SHUTTLE_PORT  = 1;
        public static final int SECOND_SHUTTLE_PORT = 9;
        public static final int MIDDLE_INDEX_PORT   = 8;
        public static final int INDEX_WHEELS_PORT   = 0; // using this proximity
    }

    public enum ShootingPosition
    {
        kSpeakerBase, kPodium, kRandomPosition, kToAmp, kOff;
    }

    public static class Shuttle
    {
        public static final int MOTOR_PORT          = 17;
        public static final String MOTOR_CAN_BUS    = ROBORIO;
    }

    public static class SwerveModuleSetup
    {
        // The CANcoder offset to use depends on the robot
        private static double frontLeftEncoderOffset;
        private static double frontRightEncoderOffset;
        private static double backLeftEncoderOffset;
        private static double backRightEncoderOffset;

        static
        {
            System.out.println("Loading: " + MethodHandles.lookup().lookupClass().getCanonicalName());

            if(robotName4237.equals("2024 Robot"))
            {
                frontLeftEncoderOffset  = -0.29296875; // Berrien Springs 0.269531;
                frontRightEncoderOffset = -0.18798828125; // pre state -0.194580078125; // Berrien Springs -0.142822;
                backLeftEncoderOffset   = -0.976807;//-0.675048828125; // Berrien Springs 0.022461;
                backRightEncoderOffset  = -0.75732421875;  // Berrien Springs 0.043213;
            }
            else if(robotName4237.equals("2023 Robot"))
            {
                frontLeftEncoderOffset   = 0.413818;  //-209.883; 
                frontRightEncoderOffset  = -0.477051; //-171.562;  //-133.330; changed at state 
                backLeftEncoderOffset    = -0.052979; //-18.809; 
                backRightEncoderOffset   = 0.044922;  //-342.422; 
            }
            else if(robotName4237.equals("2022 Robot"))
            {
                frontLeftEncoderOffset  = -0.282715;//-102.129; //-338.730;
                frontRightEncoderOffset = -0.374756;//-135.088; //-287.578;
                backLeftEncoderOffset   = -0.979736;//-352.529; //-348.75;
                backRightEncoderOffset  = -0.041260;//-15.205;  //-103.271;
            }
            else 
            {
                System.out.println("Unknown Robot " + robotName4237);
            }
        }

        public static final double FRONT_LEFT_ENCODER_OFFSET  = frontLeftEncoderOffset;
        public static final double FRONT_RIGHT_ENCODER_OFFSET = frontRightEncoderOffset;
        public static final double BACK_LEFT_ENCODER_OFFSET   = backLeftEncoderOffset;
        public static final double BACK_RIGHT_ENCODER_OFFSET  = backRightEncoderOffset;


        // private static final Translation2d FRONT_LEFT_LOCATION = new Translation2d(DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        // private static final Translation2d FRONT_RIGHT_LOCATION = new Translation2d(DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, -DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        // private static final Translation2d BACK_LEFT_LOCATION = new Translation2d(-DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        // private static final Translation2d BACK_RIGHT_LOCATION = new Translation2d(-DrivetrainConstants.DRIVETRAIN_WHEELBASE_METERS / 2, -DrivetrainConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
       
        // public static final SwerveModuleConfig FRONT_LEFT = new SwerveModuleConfig(
        //     "Front Left", FRONT_LEFT_LOCATION, Drivetrain.FRONT_LEFT_DRIVE, true, Drivetrain.FRONT_LEFT_ENCODER, FRONT_LEFT_ENCODER_OFFSET, Drivetrain.FRONT_LEFT_TURN);
        // public static final SwerveModuleConfig FRONT_RIGHT = new SwerveModuleConfig(
        //     "Front Right", FRONT_RIGHT_LOCATION, Drivetrain.FRONT_RIGHT_DRIVE, false, Drivetrain.FRONT_RIGHT_ENCODER, FRONT_RIGHT_ENCODER_OFFSET, Drivetrain.FRONT_RIGHT_TURN);
        // public static final SwerveModuleConfig BACK_LEFT = new SwerveModuleConfig(
        //     "Back Left", BACK_LEFT_LOCATION, Drivetrain.BACK_LEFT_DRIVE, true, Drivetrain.BACK_LEFT_ENCODER, BACK_LEFT_ENCODER_OFFSET, Drivetrain.BACK_LEFT_TURN);
        // public static final SwerveModuleConfig BACK_RIGHT = new SwerveModuleConfig(
        //     "Back Right", BACK_RIGHT_LOCATION, Drivetrain.BACK_RIGHT_DRIVE, false, Drivetrain.BACK_RIGHT_ENCODER, BACK_RIGHT_ENCODER_OFFSET, Drivetrain.BACK_RIGHT_TURN); 
    }
}
