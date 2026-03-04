package frc.robot.Subsystems;

import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;

public class RobotState {

    private static RobotState instance;

    CommandSwerveDrivetrain drivetrain;

    public static RobotState getInstance() {
        if (instance == null) {
            instance = new RobotState();
        }
        return instance;
    }

    public RobotState() {
        drivetrain = CommandSwerveDrivetrain.getInstance();
    }


    public void updateAirtime() {
        
    }
}
