package frc.robot.Subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Quest extends SubsystemBase {
    private static Quest instance;

    public static Quest getInstance() {
        if(instance == null) {
            instance = new Quest();
        }
        return instance;
    }

    // QuestNav questNav = new QuestNav();

    @Override
    public void periodic() {

    }
}
