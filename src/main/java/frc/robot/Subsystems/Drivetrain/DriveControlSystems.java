package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ForwardPerspectiveValue;
import com.ctre.phoenix6.swerve.SwerveModule;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.Interpolator;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.generated.TunerConstants;

import static edu.wpi.first.units.Units.Radians;

public class DriveControlSystems {

    private double deadbandFactor = 0.8; // higher is more linear joystick controls

    PIDController pidHeading = new PIDController(0, 0, 0);

    private static DriveControlSystems controlSystems;

     // =======---===[ ⚙ Joystick processing ]===---========
    public SwerveRequest drive(double driverLY, double driverLX, double driverRX){
        driverLX = scaledDeadBand(driverLX) * Constants.MaxSpeed;
        driverLY = scaledDeadBand(driverLY) * Constants.MaxSpeed;
        driverRX = scaledDeadBand(driverRX) * Constants.MaxAngularRate;

        // if(Constants.alliance == Alliance.Red){
        //     driverLY = driverLY * -1;
        //     driverLX = driverLX * -1;
        // }

        // return new SwerveRequest.RobotCentric()
        // .withVelocityX(driverLY)
        // .withVelocityY(driverLX)
        // .withRotationalRate(driverRX);
        return new SwerveRequest.FieldCentricFacingAngle()
        .withVelocityX(driverLY)
        .withVelocityY(-driverLX)
        .withTargetRateFeedforward(driverRX)
        .withForwardPerspective(ForwardPerspectiveValue.BlueAlliance)
        .withDriveRequestType(com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType.Velocity)
        .withSteerRequestType(com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType.MotionMagicExpo)
        .withDesaturateWheelSpeeds(true);
    }

    public double scaledDeadBand(double input) {
        if(Math.abs(input) < Constants.stickDeadband) 
            return 0;
        else
            return (deadbandFactor * Math.pow(input, 3)) + (1 - deadbandFactor) * input;
    }
}
