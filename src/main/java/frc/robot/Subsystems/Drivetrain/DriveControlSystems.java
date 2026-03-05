package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ForwardPerspectiveValue;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;
import com.ctre.phoenix6.swerve.SwerveModule;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.Interpolator;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Subsystems.Shooter;
import frc.robot.generated.TunerConstants;

import static edu.wpi.first.units.Units.Radians;

public class DriveControlSystems {

    private double deadbandFactor = 0.8; // higher is more linear joystick controls

    private final ProfiledPIDController thetaController = new ProfiledPIDController(
            2.2, 1.2, 0, new TrapezoidProfile.Constraints(Constants.MaxAngularVelocity, Constants.MaxAngularRate), 0.02);

    private static CommandSwerveDrivetrain s_Swerve;
    private static Shooter s_Shooter;

    Boolean mode_AlignToGoal = false;
    Alliance alliance;
    Translation2d targetGoal;
    double targetHeading;
    double ffMinRadius = 0.2, ffMaxRadius = 1.2;

    public DriveControlSystems() {
        s_Swerve = CommandSwerveDrivetrain.getInstance();
        s_Shooter = Shooter.getInstance();

        thetaController.enableContinuousInput(-Math.PI, Math.PI);  

        alliance = DriverStation.getAlliance().get();
    }

     // =======---===[ ⚙ Joystick processing ]===---========
    public SwerveRequest drive(double driverLY, double driverLX, double driverRX){
        driverLX = scaledDeadBand(driverLX) * Constants.MaxSpeed;
        driverLY = scaledDeadBand(driverLY) * Constants.MaxSpeed;
        driverRX = scaledDeadBand(driverRX) * Constants.MaxAngularRate;

        if (alliance.equals(Alliance.Blue)) {
            driverLX *= -1;
            driverLY *= -1;
        }

        if (mode_AlignToGoal) {
            return new SwerveRequest.FieldCentric()
                .withVelocityX(driverLY)
                .withVelocityY(-driverLX)
                .withRotationalRate(autoAimToGoal());
        }

        return new SwerveRequest.FieldCentricFacingAngle()
        .withVelocityX(driverLY)
        .withVelocityY(-driverLX)
        .withTargetRateFeedforward(driverRX)
        .withForwardPerspective(ForwardPerspectiveValue.BlueAlliance)
        .withDriveRequestType(com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType.Velocity)
        .withSteerRequestType(com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType.MotionMagicExpo)
        .withDesaturateWheelSpeeds(true);

        // return new SwerveRequest.RobotCentric()
        // .withVelocityX(driverLY)
        // .withVelocityY(driverLX)
        // .withRotationalRate(driverRX);
    }

    private double autoAimToGoal() {

        Pose2d currentPose = s_Swerve.getState().Pose;

        double currentDistance = currentPose.getTranslation().getDistance(targetGoal);
        
        targetHeading = Math.atan2(
            (targetGoal.getY() - currentPose.getY()), (targetGoal.getX() - currentPose.getX())); 

        double ffScaler = MathUtil.clamp(
                (currentDistance - ffMinRadius) / (ffMaxRadius - ffMinRadius),
                0.0,
                1.0);

        // Calculate theta speed
        double thetaVelocity = thetaController.getSetpoint().velocity * ffScaler
                + thetaController.calculate(
                        currentPose.getRotation().getRadians(), targetHeading);
        
        return thetaVelocity;
    }

    private double scaledDeadBand(double input) {
        if(Math.abs(input) < Constants.stickDeadband) 
            return 0;
        else
            return (deadbandFactor * Math.pow(input, 3)) + (1 - deadbandFactor) * input;
    }

    // mode changing

    public void turnOnAutoAim() {
        mode_AlignToGoal = true;
        
        var state = s_Swerve.getState();

        targetGoal = alliance.equals(Alliance.Blue)
         ? Constants.FieldConstants.blueGoal.toTranslation2d() : Constants.FieldConstants.redGoal.toTranslation2d();

        thetaController.reset(state.Pose.getRotation().getRadians(),
            state.Speeds.omegaRadiansPerSecond);
    }
}
