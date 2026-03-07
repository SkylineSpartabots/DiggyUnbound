package frc.robot.Commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Subsystems.Shooter;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;

public class RampShooterWithDistance extends Command {
    Shooter s_Shooter;
    CommandSwerveDrivetrain s_Swerve;
    Timer timer = new Timer();

    Translation3d targetGoal;
    
    public RampShooterWithDistance() {
        s_Shooter = Shooter.getInstance();
        s_Swerve = CommandSwerveDrivetrain.getInstance();

        addRequirements(s_Shooter);
    }

    @Override
    public void initialize() {
    targetGoal = DriverStation.getAlliance().equals(Alliance.Blue)
        ? Constants.FieldConstants.blueGoal : Constants.FieldConstants.redGoal;
    }

    double g = 9.81;
    Transform2d ROBOT_TO_SHOOTER = new Transform2d(-0.1778, 0, new Rotation2d());

    @Override
    public void execute() {
        Translation2d currPose = s_Swerve.getState().Pose.transformBy(ROBOT_TO_SHOOTER).getTranslation();

        // double h = Constants.FieldConstants.blueGoal.getZ() - Constants.shooterHeightM; //need shooter height
        // // double d = 3;
        double d = currPose.getDistance(Constants.FieldConstants.blueGoal.toTranslation2d());
        // // System.out.println("dist " + d);

        // double v = Math.sqrt( (g * d * d) / 
        //     (
        //     2 * Math.pow( Math.cos(Constants.shooterAngleRad), 2 ) *
        //     (d * Math.tan(Constants.shooterAngleRad) - h)
        //     ));


        // System.out.println(v);
        // SmartDashboard.putNumber("Aimtime", d / (v * Math.cos(Constants.shooterAngleRad)));
        // s_Shooter.setExitVelocity(Math.min(100, v)); // free speed max 106.3 rps

        double v = 3.786* d * d + -5.907*d + 38.02;

        s_Shooter.updateAirtime(
            d / (v * Math.cos(Constants.shooterAngleRad)) // seconds
        );

        // s_Shooter.setExitVelocity(Math.min(100, v)); // free speed max 106.3 rps
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
