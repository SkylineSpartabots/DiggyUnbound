package frc.robot;

import org.opencv.core.Point;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation3d;

public class Constants {
    public static final class HardwarePorts {
        public static final int intake = 20;

        public static final int indexer= 20;

        public static final int climbL = 14;
        public static final int climbR = 4; // We should have 2 arms eventually

        public static final int convayerL = 20;
        public static final int convayerR = 20;

        public static final int shooterLT = 0;
        public static final int shooterLB = 0;
        public static final int shooterRT = 0;
        public static final int shooterRB = 0;

    }
    
    public static final double stickDeadband = 0.075;
    public static final double triggerDeadzone = 0.2;

    public static double MaxSpeed = 6; //can be lowered during testing
    public static double MaxAcceleration = 1.2542976; //can be lowered during testing
    public static double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity
    public static double MaxAngularVelocity = 2 * Math.PI;

    public static double shooterRadiusM = 0.0508;
    public static double shooterAngleRad = 0.40143;
    
    public static final class FieldConstants {
        public static Translation3d blueGoal = new Translation3d(4.6256,4.0345, 1.8288);
    }

}
