package frc.robot.Subsystems.Vision;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LimeLight extends SubsystemBase {
    private static LimeLight instance;

    public static LimeLight getInstance() {
        if (instance == null) {
            instance = new LimeLight();
        }
        return instance;
    }

    private final NetworkTable limelightTable;
    private final String limelightName;

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
        LedMode(int value) { this.value = value; }
    }

    public enum CamMode {
        VISION(0),
        DRIVER(1);

        public final int value;
        CamMode(int value) { this.value = value; }
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
        // Update cached values each loop
        hasTarget = limelightTable.getEntry("tv").getDouble(0) == 1;
        tx = limelightTable.getEntry("tx").getDouble(0);
        ty = limelightTable.getEntry("ty").getDouble(0);
        ta = limelightTable.getEntry("ta").getDouble(0);
        tl = limelightTable.getEntry("tl").getDouble(0);
        tid = (int) limelightTable.getEntry("tid").getDouble(-1);

        // Get robot pose based on alliance
        String poseKey = DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue
            ? "botpose_wpiblue"
            : "botpose_wpired";
        botpose = limelightTable.getEntry(poseKey).getDoubleArray(new double[7]);
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
            Rotation2d.fromDegrees(botpose[5])
        );
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
                Math.toRadians(botpose[5])
            )
        );
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
            goal.getX() - robotPose.getX()
        );
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
