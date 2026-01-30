package frc.robot;

public class Constants {
    public static final class HardwarePorts {
        public static final int intakeID = 20;
    }
    
    public static final double stickDeadband = 0.075;
    public static final double triggerDeadzone = 0.2;

    public static double MaxSpeed = 6; //can be lowered during testing
    public static double MaxAcceleration = 1.2542976; //can be lowered during testing
    public static double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity
    public static double MaxAngularVelocity = 2 * Math.PI;
}
