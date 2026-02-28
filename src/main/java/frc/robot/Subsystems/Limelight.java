package frc.robot.Subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    private static Limelight instance;

    public static Limelight getInstance() {
        if(instance == null) instance = new Limelight();
        return instance;
    }
    
}
