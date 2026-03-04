package frc.robot.Commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Vision.Vision;

public class DriveToNearestYellow extends Command {

    // ---------- TUNER CONSTANTS ----------
    // <O> <O>
    private static final double DRIVE_SPEED_MPS = 1.5; // meters per second max
    private static final double TURN_SPEED_RAD_S = 1.2; // radians per second max

    // Stop driving forward when within this distance (meters).
    private static final double DISTANCE_TOLERANCE_METERS = 0.15;
    // Stop turning when the horizontal angle error is within this value (degrees).
    private static final double ANGLE_TOLERANCE_DEG = 2.0;
    private static final double CAMERA_OFFSET_X_METERS = 0.0; // Tune later
    // --------------------------------------

    // PID Controllers — kP and kD need to be tuned, leave kI at 0
    private final PIDController forwardPID = new PIDController(1.0, 0, 0.10); // distance (m) → m/s
    private final PIDController lateralPID = new PIDController(1.5, 0, 0.05); // lateral offset (m) → m/s
    private final PIDController rotationalPID = new PIDController(0.05, 0, 0.01); // target_x (deg) → rad/s

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
    public void initialize() {
        forwardPID.reset();
        lateralPID.reset();
        rotationalPID.reset(); // resets just in case
        drivetrain.setControl(idleRequest);
    }

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

        double forwardMPS = MathUtil.clamp(forwardPID.calculate(distance, 0), 0.05, DRIVE_SPEED_MPS);
        double lateralMPS = MathUtil.clamp(lateralPID.calculate(robotX, 0), -0.5, 0.5);
        double rotationRadS = MathUtil.clamp(rotationalPID.calculate(tx, 0), -TURN_SPEED_RAD_S, TURN_SPEED_RAD_S);

        drivetrain.setControl(
                driveRequest
                        .withVelocityX(forwardMPS)
                        .withVelocityY(lateralMPS)
                        .withRotationalRate(rotationRadS));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(idleRequest);
    }

    @Override
    public boolean isFinished() {
        if (!vision.hasTarget())
            return false;

        double distance = vision.getNearestDistanceMeters();
        if (distance < 0)
            return false; // invalid distance, not done yet

        boolean closeEnough = distance <= DISTANCE_TOLERANCE_METERS;
        boolean aimedEnough = Math.abs(vision.getNearestTX()) <= ANGLE_TOLERANCE_DEG;

        return closeEnough && aimedEnough;
    }

    // Clamp method for keeping a value within 2 values.
    // lol realized MathUtil alr has this method
    // private double clamp(double value, double min, double max) {
    //     return Math.max(min, Math.min(max, value));
    // }
}