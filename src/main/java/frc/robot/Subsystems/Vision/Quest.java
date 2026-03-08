package frc.robot.Subsystems.Vision;


import com.ctre.phoenix6.swerve.jni.SwerveJNI.DriveState;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import gg.questnav.questnav.PoseFrame;
import gg.questnav.questnav.QuestNav;

public class Quest extends SubsystemBase {
    private static Quest instance;

    public static Quest getInstance() {
        if(instance == null) {
            instance = new Quest();
        }
        return instance;
    }

    private boolean questEnabled = true;

    public Transform3d ROBOT_TO_QUEST = new Transform3d(new Translation3d(0.30404, -0.25309, 0.19152), new Rotation3d(0,0,180));
    
    // private Transform3d QUEST_TO_FIELD = new Transform3d();

    CommandSwerveDrivetrain drivetrain;
    static QuestNav questNav;

    public Quest() {
        questNav = new QuestNav();
        drivetrain = CommandSwerveDrivetrain.getInstance();
    }

    // public static QuestNav getQuest() {
    //     if(questNav == null) {
    //         questNav = new QuestNav();
    //     }
    //     return questNav;
    // }

    Matrix<N3, N1> QUESTNAV_STD_DEVS =
        VecBuilder.fill(
            0.02,// Trust down to 2cm in X direction 0.2
            0.02, // Trust down to 2cm in Y direction 0.2
            0.035 // Trust down to 2 degrees rotational 0.035
        );

    PoseFrame[] questFrames;

    public void anchorQuest(Pose3d robotPose){
        // questNav.setPose(robotPose);
        // questFrames = questNav.getAllUnreadPoseFrames();
        // if (questFrames.length > 1 && questNav.isTracking() && questNav.isConnected()) {
        //     // System.out.println(questFrames[questFrames.length - 1].questPose3d());
        //     // System.out.println("robot " + robotPose.toString());
        //     // QUEST_TO_FIELD = new Transform3d(questFrames[questFrames.length - 1].questPose3d(), robotPose);
        //     // System.out.println("qtf " + QUEST_TO_FIELD.toString());
        // }

        questNav.setPose(robotPose.transformBy(ROBOT_TO_QUEST));
    }

    public void updateQuest() {
        questFrames = questNav.getAllUnreadPoseFrames();

        if (questFrames.length > 1 && questNav.isTracking() && questNav.isConnected()) {
            PoseFrame frame = questFrames[questFrames.length - 1];
            Pose3d questPose = frame.questPose3d();
            double timestamp = frame.dataTimestamp();

            // System.out.println(QUEST_TO_FIELD.toString());
            Pose3d robotPose = questPose.transformBy(ROBOT_TO_QUEST.inverse());

            // System.out.println(robotPose.toPose2d().toString());
            SmartDashboard.putString("Quest Pose", robotPose.toPose2d().toString());
            // System.out.println(robotPose.toString());
            drivetrain.addVisionMeasurement(robotPose.toPose2d(), timestamp, QUESTNAV_STD_DEVS);
        }
    }

    public void toggleQuestEnabled() {
        questEnabled = !questEnabled;
    }

    @Override
    public void periodic() {

        questNav.commandPeriodic();

        SmartDashboard.putBoolean("Quest Enabled", questEnabled);

        if (DriverStation.isEnabled() && questEnabled) {
            updateQuest();
        }
        
    }
}
