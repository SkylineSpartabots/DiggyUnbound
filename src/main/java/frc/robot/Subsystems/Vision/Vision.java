package frc.robot.Subsystems.Vision;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.RobotState;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;

public class Vision extends SubsystemBase{
    private static Vision instance;

    CommandSwerveDrivetrain s_Swerve;
    RobotState robotState;
    // LimeLightHelpers does not need an object.

    public static Vision getInstance() {
        if (instance == null) instance = new Vision();
        return instance;
    }

    public Vision() {
        s_Swerve = CommandSwerveDrivetrain.getInstance();
        robotState = RobotState.getInstance();
    }
    

}
