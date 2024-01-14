package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public interface Module 
{
    public abstract SwerveModulePosition getPosition();
    public abstract void stopModule();
    public abstract void resetEncoders();
    public abstract void setDesiredState(SwerveModuleState desiredState);
    public abstract double getTurningEncoderPosition();
    public abstract double getDrivingEncoderRate();
    public abstract void feed();
}
