package frc.robot.Commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Vision.Vision;

public class DriveToNearestYellow extends Command {

    // ---------- TUNER CONSTANTS ----------
    private static final double DRIVE_SPEED_MPS = 1.5; // meters per second at full gain
    private static final double TURN_SPEED_RAD_S = 1.2; // radians per second at full gain
    private static final double kP_DRIVE = 1.0; //Tune later
    private static final double kP_TURN = 0.05; //Tune later
    /** Stop driving forward when within this distance (meters). */
    private static final double DISTANCE_TOLERANCE_METERS = 0.15;
    //Stop turning when the horizontal angle error is within this value (degrees).
    private static final double ANGLE_TOLERANCE_DEG = 2.0;
    private static final double CAMERA_OFFSET_X_METERS = 0.0; //Tune later
    // --------------------------------------

    private final Vision vision;
    private final CommandSwerveDrivetrain drivetrain;

    private final SwerveRequest.RobotCentric driveRequest = new SwerveRequest.RobotCentric();
    private final SwerveRequest.Idle idleRequest = new SwerveRequest.Idle();

    public DriveToNearestYellow(Vision vision, CommandSwerveDrivetrain drivetrain) {
        this.vision = vision;
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() { drivetrain.setControl(idleRequest); }

   @Override
    public void execute() {
        if (!vision.hasTarget()) {
            drivetrain.setControl(idleRequest);
            return;
        }

        double tx = vision.getNearestTX();
        double distance = vision.getNearestDistanceMeters();

        double angleRad = Math.toRadians(tx);
        double robotX = (distance * Math.sin(angleRad)) + CAMERA_OFFSET_X_METERS;

        double forwardMPS  = clamp(distance * kP_DRIVE, 0.05, DRIVE_SPEED_MPS);
        double lateralMPS  = clamp(-robotX * 1.5, -0.5, 0.5);
        double rotationRadPS = clamp(-tx * kP_TURN, -TURN_SPEED_RAD_S, TURN_SPEED_RAD_S);

        drivetrain.setControl(
            driveRequest
                .withVelocityX(forwardMPS)
                .withVelocityY(lateralMPS)
                .withRotationalRate(rotationRadPS));
}

    @Override
    public void end(boolean interrupted) { drivetrain.setControl(idleRequest); }

    @Override
    public boolean isFinished() {
        if (!vision.hasTarget()) return false;

        boolean closeEnough = vision.getNearestDistanceMeters() <= DISTANCE_TOLERANCE_METERS;
        boolean aimedEnough = Math.abs(vision.getNearestTX()) <= ANGLE_TOLERANCE_DEG;

        return closeEnough && aimedEnough;
    }

    // Clamp method for keeping a value within 2 values.
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}