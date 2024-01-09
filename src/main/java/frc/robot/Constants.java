// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

// FIXME uncomment
// import com.ctre.phoenix.sensors.Pigeon2.AxisDirection;
import frc.robot.sensors.Pigeon2.AxisDirection;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.DrivetrainConfig;
import frc.robot.subsystems.SwerveModuleConfig;

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
    private static String Robot4237 = "";

    public static double DRIVETRAIN_WHEELBASE_METERS  ; 
    public static double DRIVETRAIN_TRACKWIDTH_METERS ; 

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
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
        final Path commentPath = Path.of("/etc/machine-info");
        String comment = "";
        try 
        {  
            comment = Files.readString(commentPath);
            System.out.println("The roboRIO comment is >" + comment + "<");
        } 
        catch (IOException e) 
        {
            // Couldn't read the file
            System.out.println(e);
        }
      
        // Use the comment variable to decide what to do
        if(comment.contains("2023 Robot"))
        {
            Robot4237 = "2023 Robot";
            Constants.DRIVETRAIN_WHEELBASE_METERS =  27.44 * DrivetrainConstants.INCHES_TO_METERS; // 23.5 Front to back
            Constants.DRIVETRAIN_TRACKWIDTH_METERS = 19.50 * DrivetrainConstants.INCHES_TO_METERS; // 23.5 // Side to side
            
        }
        else if (comment.contains("2022 Robot"))
        { 
            Robot4237 = "2022 Robot";
            Constants.DRIVETRAIN_WHEELBASE_METERS =  23.5 * DrivetrainConstants.INCHES_TO_METERS; // Front to back
            Constants.DRIVETRAIN_TRACKWIDTH_METERS = 23.5 * DrivetrainConstants.INCHES_TO_METERS; // Side to side
        }
        else 
        {
            System.out.println("Unknown Robot " + comment);
        }

        System.out.println("Robot:" + Robot4237);
        // end get roboRIO comment

    }
    
    private static final String CANIVORE = "CANivore";
    private static final String ROBORIO = "rio";
    
    // public static class Subsystem
    // {
    //     public static final int GATHERER_MOTOR_PORT     = 25;
    //     public static final int ARM_MOTOR_PORT          = 4;
    //     public static final int SHOULDER_MOTOR_PORT     = 5;

    //     public static final String CAN_BUS = ROBORIO;
    // }

    // public static class Shoulder
    // {
    //     public static final float ENCODER_FORWARD_SOFT_LIMIT = 380000.0f;
    //     public static final float ENCODER_REVERSE_SOFT_LIMIT = -6000.0f;

    //     public static final double HIGH_CONE = 215000.0; //375000 big single suction //340000 double small suction // 205000
    //     public static final double HIGH_CONE_LOCK = 195000.0;
    //     public static final double HIGH_CUBE = 218000.0;
    //     public static final double MIDDLE_CONE = 176000.0; //300000 big single suction //297000 double small suction
    //     public static final double MIDDLE_CUBE = 205000.0;
    //     public static final double LOW_CONE = 55000.0;
    //     public static final double LOW_CUBE = 55000.0;
    //     public static final double GATHER = 0.0;
    //     // public static final double SHOULDER_READY_TO_GRAB = 
    //     public static final double SUCTION_CONE = 38912.2;
    //     public static final double SHOULDER_READY_TO_CLAMP = 168639.2;
    //     public static final double CLAMP = 682.0;
    //     public static final double READY_TO_PICK_UP = 104740.0;
    //     public static final double STARTING_POSITION = 32000.0; //20000.0; //42000 old
    //     public static final double SUBSTATION = 230000.0;//330000 old // 225000
    //     public static final double LIMELIGHT = 135000.0;
    // }

    // public static class Arm
    // {
    //     public static final float ENCODER_FORWARD_SOFT_LIMIT = 995000.0f;
    //     public static final float ENCODER_REVERSE_SOFT_LIMIT = -6000.0f;

    //     public static final double HIGH_CONE = 912000.0;
    //     public static final double HIGH_CUBE = 912000.0;
    //     public static final double MIDDLE_CONE = 250000.0;
    //     public static final double MIDDLE_CUBE = 438000.0;
    //     public static final double LOW_CONE = 161000.0;
    //     public static final double LOW_CUBE = 161000.0;
    //     public static final double GATHER = 0.0;
    //     public static final double ARM_READY_TO_GRAB = 161595.3;
    //     public static final double ARM_READY_TO_SUCTION = 165000.0;
    //     // public static final double ARM_SUCTION_CONE = 
    //     // public static final double ARM_READY_TO_CLAMP = 
    //     public static final double CLAMP = 292.6;
    //     public static final double READY_TO_PICK_UP = 0.0;
    //     public static final double STARTING_POSITION = 0.0;
    //     public static final double SUBSTATION = 500000.0; //360000 //319000 old
    // }

    // public static class Grabber
    // {
    //     public static final int WRIST_UP = 1;
    //     public static final int WRIST_DOWN = 0;
    //     public static final int VACUUM_CAN_ID = 1;
    //     public static final double VOLTAGE_SCALE_FACTOR = 5.0 / 3.3; // Sensor based on 5-volt system

    //     //Vacuum Top
    //     public static final int GRABBER_MOTOR_TOP_PORT          = 2;
    //     public static final double MAX_PRESSURE_TOP             = 0.76; //0.7
    //     public static final double MAX_SPEED_TOP                = -0.5; //-0.5;
    //     // public static final double AS_FAST_AS_POSSIBLE_TOP      = -1.000;
    //     public static final double TARGET_PRESSURE_TOP          = 0.71; // = -12.21625 psi
    //     public static final double GAME_PIECE_ACQUIRED_TOP       = 1.1;
    //     public static final double MAINTAIN_SPEED_LIMIT_TOP     = -0.5; //-0.5; //RUN POWER TOP

    //     //Vacuum Bottom
    //     public static final int GRABBER_MOTOR_BOTTOM_PORT       = 3;
    //     public static final double MAX_PRESSURE_BOTTOM          = 0.84; //0.7;
    //     public static final double MAX_SPEED_BOTTOM             = -0.5; //-0.5;
    //     // public static final double AS_FAST_AS_POSSIBLE_BOTTOM   = -1.000;
    //     public static final double TARGET_PRESSURE_BOTTOM       = 0.79; // = -11.34625 psi
    //     public static final double GAME_PIECE_ACQUIRED_BOTTOM    = 1.1;
    //     public static final double MAINTAIN_SPEED_LIMIT_BOTTOM  = -0.5; //-0.5; //RUN POWER BOTTOM
    // }

    public static class Drivetrain
    {
        private static final int FRONT_LEFT_DRIVE       = 7;
        private static final int FRONT_LEFT_ENCODER     = 8;  
        private static final int FRONT_LEFT_TURN        = 9;  

        private static final int FRONT_RIGHT_DRIVE      = 10;
        private static final int FRONT_RIGHT_ENCODER    = 11;  
        private static final int FRONT_RIGHT_TURN       = 12;  

        private static final int BACK_LEFT_DRIVE        = 4; 
        private static final int BACK_LEFT_ENCODER      = 5; 
        private static final int BACK_LEFT_TURN         = 6;  

        private static final int BACK_RIGHT_DRIVE       = 1; 
        private static final int BACK_RIGHT_ENCODER     = 2; 
        private static final int BACK_RIGHT_TURN        = 3;

        public static final String CANCODER_CAN_BUS = CANIVORE;
        public static final String MOTOR_CAN_BUS = ROBORIO;
    }

    public static class PowerDistributionHub
    {
        public static final int PDH_CAN_ID              = 1;

        public static final String CAN_BUS = ROBORIO;
    }

    // public static class Candle
    // {
    //     public static final int CANDLE_PORT = 1;
    //     public static final String CAN_BUS = ROBORIO;
    // }

    public static class Gyro 
    {
        public static final int PIGEON_ID = 0;

        public static final AxisDirection FORWARD_AXIS = AxisDirection.PositiveX;
        public static final AxisDirection UP_AXIS = AxisDirection.PositiveZ;

        public static final double RESET_GYRO_DELAY = 0.1;
        public static final String PIGEON_CAN_BUS = CANIVORE;
    }

    public static class Controller
    {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }

    public static class DrivetrainConstants
    {
        static
        {
        System.out.println("Loading: " + MethodHandles.lookup().lookupClass().getCanonicalName());
        }       
        public static final double INCHES_TO_METERS = 0.0254;
        // public static final double DRIVETRAIN_WHEELBASE_METERS =  27.44 * INCHES_TO_METERS; // 23.5 Front to back
        // public static final double DRIVETRAIN_TRACKWIDTH_METERS = 19.50 * INCHES_TO_METERS; // 23.5 // Side to side
        public static final double MAX_MODULE_TURN_SPEED = 1080.0; // degrees per second, this is 3.0 rev/sec, used to be 1980 and 5.5 rev/sec
        public static final double MAX_MODULE_TURN_ACCELERATION = 1728.0; // degrees per second per second, this is 4.8 rev/sec^2, used to be 17280 and 48 rev/sec^2
        public static final double MAX_BATTERY_VOLTAGE = 12.0;
        public static final int DRIVE_MOTOR_ENCODER_RESOLUTION = 42;
        public static final double DRIVE_MOTOR_GEAR_RATIO = 8.14;
        public static final double WHEEL_RADIUS_METERS = 2.0 * INCHES_TO_METERS;
        public static final double DRIVE_ENCODER_RATE_TO_METERS_PER_SEC = 
        ((10.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
        public static final double DRIVE_ENCODER_POSITION_TO_METERS =
        ((1.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
        public static final double MAX_DRIVE_SPEED = 4.4; // meters per second

        public static double DRIVETRAIN_WHEELBASE_METERS = Constants.DRIVETRAIN_WHEELBASE_METERS  ; // 23.5 Front to back
        public static double DRIVETRAIN_TRACKWIDTH_METERS = Constants.DRIVETRAIN_TRACKWIDTH_METERS ; // 23.5 // Side to side

        public static final double X_ACCELERATION_RATE_LIMT = 10.0;
        public static final double X_DECELERATION_RATE_LIMT = 10.0;
        public static final double Y_ACCELERATION_RATE_LIMT = 10.0;
        public static final double Y_DECELERATION_RATE_LIMT = 10.0;

    }

    public static class SwerveModuleSetup
    {
        private static double FRONT_LEFT_ENCODER_OFFSET   ;
        private static double FRONT_RIGHT_ENCODER_OFFSET  ;
        private static double BACK_LEFT_ENCODER_OFFSET    ;
        private static double BACK_RIGHT_ENCODER_OFFSET   ;

        private static final Translation2d FRONT_LEFT_LOCATION = new Translation2d(Constants.DRIVETRAIN_WHEELBASE_METERS / 2, Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d FRONT_RIGHT_LOCATION = new Translation2d(Constants.DRIVETRAIN_WHEELBASE_METERS / 2, -Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_LEFT_LOCATION = new Translation2d(-Constants.DRIVETRAIN_WHEELBASE_METERS / 2, Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_RIGHT_LOCATION = new Translation2d(-Constants.DRIVETRAIN_WHEELBASE_METERS / 2, -Constants.DRIVETRAIN_TRACKWIDTH_METERS / 2);
       
        static
        {
            
            System.out.println("Loading: " + MethodHandles.lookup().lookupClass().getCanonicalName());

            if(Robot4237.equals("2023 Robot"))
            {
                FRONT_LEFT_ENCODER_OFFSET   = -209.883; 
                FRONT_RIGHT_ENCODER_OFFSET  = -171.562; //-133.330; changed at state 
                BACK_LEFT_ENCODER_OFFSET    = -18.809; 
                BACK_RIGHT_ENCODER_OFFSET   = -342.422; 
            }
            else if (Robot4237.equals("2022 Robot"))
            {
                FRONT_LEFT_ENCODER_OFFSET   = -102.129; //-338.730;
                FRONT_RIGHT_ENCODER_OFFSET  = -135.088; //-287.578;
                BACK_LEFT_ENCODER_OFFSET    = -352.529; //-348.75;
                BACK_RIGHT_ENCODER_OFFSET   = -15.205;  //-103.271;
            }
            else 
            {
                System.out.println("Unknown Robot " + Robot4237);
            }
       
            
        }
        
            public static final SwerveModuleConfig FRONT_LEFT = new SwerveModuleConfig(
                "Front Left", FRONT_LEFT_LOCATION, Drivetrain.FRONT_LEFT_DRIVE, true, Drivetrain.FRONT_LEFT_ENCODER, FRONT_LEFT_ENCODER_OFFSET, Drivetrain.FRONT_LEFT_TURN);
            public static final SwerveModuleConfig FRONT_RIGHT = new SwerveModuleConfig(
                "Front Right", FRONT_RIGHT_LOCATION, Drivetrain.FRONT_RIGHT_DRIVE, false, Drivetrain.FRONT_RIGHT_ENCODER, FRONT_RIGHT_ENCODER_OFFSET, Drivetrain.FRONT_RIGHT_TURN);
            public static final SwerveModuleConfig BACK_LEFT = new SwerveModuleConfig(
                "Back Left", BACK_LEFT_LOCATION, Drivetrain.BACK_LEFT_DRIVE, true, Drivetrain.BACK_LEFT_ENCODER, BACK_LEFT_ENCODER_OFFSET, Drivetrain.BACK_LEFT_TURN);
            public static final SwerveModuleConfig BACK_RIGHT = new SwerveModuleConfig(
                "Back Right", BACK_RIGHT_LOCATION, Drivetrain.BACK_RIGHT_DRIVE, false, Drivetrain.BACK_RIGHT_ENCODER, BACK_RIGHT_ENCODER_OFFSET, Drivetrain.BACK_RIGHT_TURN);
        // public static final SwerveModuleData FRONT_LEFT = new SwerveModuleData("Front Left", 7, true, 8, -167.255859375, 9);
        // public static final SwerveModuleData FRONT_RIGHT = new SwerveModuleData("Front Right", 10, false, 11, -305.947265625, 12);
        // public static final SwerveModuleData BACK_LEFT = new SwerveModuleData("Back Left", 4, true, 5, -348.75, 6);
        // public static final SwerveModuleData BACK_RIGHT = new SwerveModuleData("Back Right", 1, false, 2, -101.953125, 3);

        //  private static final double FRONT_LEFT_ENCODER_OFFSET   = -209.883; //-338.730;
        //  private static final double FRONT_RIGHT_ENCODER_OFFSET  = -133.330; //-287.578;
        //  private static final double BACK_LEFT_ENCODER_OFFSET    = -18.809;  //-348.75;
        //  private static final double BACK_RIGHT_ENCODER_OFFSET   = -342.422; //-103.271;

        // private static double FRONT_LEFT_ENCODER_OFFSET = Constants.FRONT_LEFT_ENCODER_OFFSET    ;
        // private static double FRONT_RIGHT_ENCODER_OFFSET = Constants.FRONT_RIGHT_ENCODER_OFFSET  ;
        // private static double BACK_LEFT_ENCODER_OFFSET = Constants.BACK_LEFT_ENCODER_OFFSET      ;
        // private static double BACK_RIGHT_ENCODER_OFFSET = Constants.BACK_RIGHT_ENCODER_OFFSET    ;
        
    }

    // public static class Vacuum
    // {
    //     public static final VacuumPumpConfig TOP = new VacuumPumpConfig(
    //         "Top", Grabber.GRABBER_MOTOR_TOP_PORT, Grabber.MAX_PRESSURE_TOP, Grabber.MAX_SPEED_TOP, Grabber.AS_FAST_AS_POSSIBLE_TOP, Grabber.TARGET_PRESSURE_TOP, 
    //         Grabber.MAINTAIN_SPEED_LIMIT_TOP);
    //         public static final VacuumPumpConfig BOTTOM = new VacuumPumpConfig(
    //         "Bottom", Grabber.GRABBER_MOTOR_BOTTOM_PORT, Grabber.MAX_PRESSURE_BOTTOM, Grabber.MAX_SPEED_BOTTOM, Grabber.AS_FAST_AS_POSSIBLE_BOTTOM, Grabber.TARGET_PRESSURE_BOTTOM,
    //          Grabber.MAINTAIN_SPEED_LIMIT_BOTTOM);
    // }

    public static class DrivetrainSetup
    {
        static
        {
        System.out.println("Loading: " + MethodHandles.lookup().lookupClass().getCanonicalName());
        }
        public static final DrivetrainConfig DRIVETRAIN_DATA = new DrivetrainConfig(
            SwerveModuleSetup.FRONT_LEFT, SwerveModuleSetup.FRONT_RIGHT, SwerveModuleSetup.BACK_LEFT, SwerveModuleSetup.BACK_RIGHT);
    }

    // public enum TargetPosition
    // {
    //     kGather(Constants.Shoulder.GATHER, Constants.Arm.GATHER),
    //     kLowCone(Constants.Shoulder.LOW_CONE, Constants.Arm.LOW_CONE),
    //     kLowCube(Constants.Shoulder.LOW_CUBE, Constants.Arm.LOW_CUBE),
    //     kMiddleCone(Constants.Shoulder.MIDDLE_CONE, Constants.Arm.MIDDLE_CONE),
    //     kMiddleCube(Constants.Shoulder.MIDDLE_CUBE, Constants.Arm.MIDDLE_CUBE),
    //     kHighCone(Constants.Shoulder.HIGH_CONE, Constants.Arm.HIGH_CONE),
    //     kHighConeLock(Constants.Shoulder.HIGH_CONE_LOCK, Constants.Arm.HIGH_CONE),
    //     kHighCube(Constants.Shoulder.HIGH_CUBE, Constants.Arm.HIGH_CUBE),
    //     kClamp(Constants.Shoulder.CLAMP, Constants.Arm.CLAMP),
    //     kStartingPosition(Constants.Shoulder.STARTING_POSITION, Constants.Arm.STARTING_POSITION),
    //     kSubstation(Constants.Shoulder.SUBSTATION, Constants.Arm.SUBSTATION),
    //     kLimelight(Constants.Shoulder.LIMELIGHT, Constants.Arm.GATHER),
    //     kOverride(-4237, -4237),

    //     kArmReadyToClamp(Constants.Shoulder.READY_TO_PICK_UP, Constants.Arm.ARM_READY_TO_SUCTION),
    //     kSuctionCone(Constants.Shoulder.SUCTION_CONE, Constants.Arm.ARM_READY_TO_GRAB),
    //     kShoulderReadyToClamp(Constants.Shoulder.SHOULDER_READY_TO_CLAMP, Constants.Arm.ARM_READY_TO_GRAB),
    //     kReadyToPickUp(Constants.Shoulder.READY_TO_PICK_UP, Constants.Arm.READY_TO_PICK_UP);
        
     
    //     public final double shoulder;
    //     public final double arm;

    //     private TargetPosition(double shoulder, double arm)
    //     {
    //         this.shoulder = shoulder;
    //         this.arm = arm;
    //     }
    // }

    // public enum SuctionState
    // {
    //     kOn, kOff;
    // }
    
}
