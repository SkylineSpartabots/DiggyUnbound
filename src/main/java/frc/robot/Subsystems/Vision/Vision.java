package frc.robot.Subsystems.Vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Vision.LimelightHelpers.LimelightResults;
import frc.robot.Subsystems.Vision.LimelightHelpers.LimelightTarget_Detector;

public class Vision extends SubsystemBase {
    private static Vision instance;

    // Camera name to be configured in the Limelight web interface
    private final String limelightName = "limelight";

    // **** FILL THESE OUT ****:
    private static final double limelightHeightMeters = 0.0; // height of limelight lens above floor
    private static final double limelightMountAngleDeg = 0.0; // tilt angle of limelight from horizontal (deg)
    private static final double targetHeightMeters = 0.075; // height of target center above floor
    private static final double minimumConfidence = 0.75; // 0.0–1.0, skip detections below this threshold

    private LimelightTarget_Detector nearestTarget;
    private double nearestDistanceMeters;

    public static Vision getInstance() {
        if (instance == null)
            instance = new Vision();
        return instance;
    }

    public Vision() {}

    @Override
    public void periodic() {
        updateNearestTarget();
        //Shuffleboard for testing
        SmartDashboard.putBoolean("Vision/HasTarget", hasTarget());
        SmartDashboard.putNumber("Vision/NearestDistanceM", nearestDistanceMeters);
        SmartDashboard.putNumber("Vision/NearestTX", getNearestTX());
        SmartDashboard.putNumber("Vision/NearestTY", getNearestTY());
        SmartDashboard.putNumber("Vision/TargetCount", getTargetCount());
    }

    private void updateNearestTarget() {
        nearestTarget = null;
        nearestDistanceMeters = -1;

        LimelightResults results = LimelightHelpers.getLatestResults(limelightName);
        LimelightTarget_Detector[] targets = results.targets_Detector;

        if (targets == null || targets.length == 0)
            return;
        double minDistance = Double.MAX_VALUE;

        for (LimelightTarget_Detector target : targets) {
            if (target.confidence < minimumConfidence)
                continue; // skip weak detections
            double distance = calculateDistance(target.ty);
            if (distance > 0 && distance < minDistance) {
                minDistance = distance;
                nearestTarget = target;
                nearestDistanceMeters = distance;
            }
        }
    }

    /**
     * <§ CLAUDE MADE THE METADATA BELOW §>
     * Estimates the ground-plane distance to a target using the standard limelight
     * trigonometry formula:
     * distance = (targetHeight - cameraHeight) / tan(mountAngle + ty)
     *
     * @param ty Vertical angle to target from limelight crosshair (degrees,
     *           positive = up)
     * @return Estimated distance in meters, or -1 if the geometry is invalid
     */
    private double calculateDistance(double ty) {
        double angleRad = Math.toRadians(limelightMountAngleDeg + ty);
        if (Math.abs(angleRad) < 1e-6)
            return -1; // avoid divide-by-zero
        double heightDiff = targetHeightMeters - limelightHeightMeters;
        double distance = heightDiff / Math.tan(angleRad);
        return distance > 0 ? distance : -1;
    }

    public boolean hasTarget() {
        return nearestTarget != null;
    }

    public double getNearestTX() {
        return nearestTarget != null ? nearestTarget.tx : 0.0;
    }

    public double getNearestTY() {
        return nearestTarget != null ? nearestTarget.ty : 0.0;
    }

    public double getNearestDistanceMeters() {
        return nearestDistanceMeters;
    }

    public int getTargetCount() {
        LimelightResults results = LimelightHelpers.getLatestResults(limelightName);
        LimelightTarget_Detector[] targets = results.targets_Detector;
        return targets != null ? targets.length : 0;
    }
}
