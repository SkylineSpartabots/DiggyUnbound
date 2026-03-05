package frc.robot.Subsystems.Vision;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;

public class LimeLight extends SubsystemBase {
    private static LimeLight instance;
    private static CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();

    Matrix<N3, N1> LIMELIGHT_STD_DEVS = VecBuilder.fill(
            0.02, // Trust down to 2cm in X direction
            0.02, // Trust down to 2cm in Y direction
            0.035 // Trust down to 2 degrees rotational
    );

    public static LimeLight getInstance() {
        if (instance == null) {
            instance = new LimeLight();
        }
        return instance;
    }

    private final NetworkTable limelightTable;
    private final String limelightName;

    private final Pigeon2 pigeon = drivetrain.getPigeon2();

    // Cached values
    private boolean hasTarget;
    private double tx; // Horizontal offset from crosshair to target (-27 to 27 degrees)
    private double ty; // Vertical offset from crosshair to target (-20.5 to 20.5 degrees)
    private double ta; // Target area (0% to 100% of image)
    private double tl; // Pipeline latency (ms)
    private int tid; // AprilTag ID
    private double[] botpose; // Robot pose in field space

    public enum LedMode {
        PIPELINE(0),
        OFF(1),
        BLINK(2),
        ON(3);

        public final int value;

        LedMode(int value) {
            this.value = value;
        }
    }

    public enum CamMode {
        VISION(0),
        DRIVER(1);

        public final int value;

        CamMode(int value) {
            this.value = value;
        }
    }

    public LimeLight() {
        this("limelight");
    }

    public LimeLight(String name) {
        this.limelightName = name;
        this.limelightTable = NetworkTableInstance.getDefault().getTable(name);
    }

    @Override
    public void periodic() {
        updateLimelight();
    }

    public void updateLimelight() {

        LimelightHelpers.SetRobotOrientation(limelightName, 
        // might be better to use something else not hte pigeon. maybe ask the quest or the general robo pose container
            pigeon.getYaw().getValueAsDouble(),
            pigeon.getAngularVelocityZDevice(true).getValueAsDouble(), 
            pigeon.getPitch().getValueAsDouble(), 
            pigeon.getAngularVelocityYDevice(true).getValueAsDouble(), 
            pigeon.getRoll().getValueAsDouble(), 
            pigeon.getAngularVelocityXWorld(true).getValueAsDouble() );
        
        PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(limelightName);

        if (isValidEstimate(mt2)) {
            drivetrain.addVisionMeasurement(mt2.pose, mt2.timestampSeconds, LIMELIGHT_STD_DEVS);
        }
    }

    public boolean isValidEstimate(PoseEstimate estimate) {
        // boolean valid = false;

// mt2
        if (Math.abs(pigeon.getAngularVelocityZDevice().getValueAsDouble()) >= 360 || estimate.tagCount == 0) // choose some max angular accel in degree per sec
            return false;

            // mt1
        // if (estimate.tagCount == 1 && estimate.rawFiducials.length == 1) {
        //     if (estimate.rawFiducials[0].ambiguity > .7) {
        //         valid = false;
        //     }
        //     if (estimate.rawFiducials[0].distToCamera > 3) { // not sure what this measure is in porlly meters
        //         valid = false;
        //     }
        // }
        // if (estimate.tagCount == 0) {
        //     valid = false;
        // }

        return true;
    }

    // Basic targeting data
    public boolean hasTarget() {
        return hasTarget;
    }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }

    public double getTa() {
        return ta;
    }

    public double getLatencyMs() {
        return tl;
    }

    public int getTagId() {
        return tid;
    }

    // Pose estimation
    public Pose2d getBotPose2d() {
        if (botpose == null || botpose.length < 6) {
            return new Pose2d();
        }
        return new Pose2d(
                botpose[0],
                botpose[1],
                Rotation2d.fromDegrees(botpose[5]));
    }

    public Pose3d getBotPose3d() {
        if (botpose == null || botpose.length < 6) {
            return new Pose3d();
        }
        return new Pose3d(
                new Translation3d(botpose[0], botpose[1], botpose[2]),
                new Rotation3d(
                        Math.toRadians(botpose[3]),
                        Math.toRadians(botpose[4]),
                        Math.toRadians(botpose[5])));
    }

    public double getPoseLatencySeconds() {
        if (botpose == null || botpose.length < 7) {
            return 0;
        }
        return (tl + botpose[6]) / 1000.0; // Convert total latency to seconds
    }

    // Distance calculations
    public double getDistanceToGoal() {
        Pose2d robotPose = getBotPose2d();
        Translation3d goal = DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue
                ? Constants.FieldConstants.blueGoal
                : Constants.FieldConstants.redGoal;

        return robotPose.getTranslation().getDistance(goal.toTranslation2d());
    }

    public double getAngleToGoal() {
        Pose2d robotPose = getBotPose2d();
        Translation3d goal = DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue
                ? Constants.FieldConstants.blueGoal
                : Constants.FieldConstants.redGoal;

        return Math.atan2(
                goal.getY() - robotPose.getY(),
                goal.getX() - robotPose.getX());
    }

    // Settings
    public void setLedMode(LedMode mode) {
        limelightTable.getEntry("ledMode").setNumber(mode.value);
    }

    public void setCamMode(CamMode mode) {
        limelightTable.getEntry("camMode").setNumber(mode.value);
    }

    public void setPipeline(int pipeline) {
        limelightTable.getEntry("pipeline").setNumber(pipeline);
    }

    public int getCurrentPipeline() {
        return (int) limelightTable.getEntry("getpipe").getDouble(0);
    }

    // Utility
    public boolean isPoseValid() {
        return hasTarget && botpose != null && botpose.length >= 6
                && (botpose[0] != 0 || botpose[1] != 0);
    }

    public String getLimelightName() {
        return limelightName;
    }
}
