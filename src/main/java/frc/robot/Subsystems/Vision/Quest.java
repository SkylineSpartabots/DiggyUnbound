package frc.robot.Subsystems.Vision;


import com.ctre.phoenix6.swerve.jni.SwerveJNI.DriveState;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.DriverStation;
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
    private Transform3d ROBOT_TO_QUEST = new Transform3d(new Translation3d(11.97, -9.964, 5.04), new Rotation3d(0,0,180));
    
    CommandSwerveDrivetrain drivetrain;
    QuestNav questNav;

    public Quest() {
        questNav = new QuestNav();
        drivetrain = CommandSwerveDrivetrain.getInstance();
    }

    Matrix<N3, N1> QUESTNAV_STD_DEVS =
        VecBuilder.fill(
            0.02,// Trust down to 2cm in X direction 0.2
            0.02, // Trust down to 2cm in Y direction 0.2
            999 // Trust down to 2 degrees rotational 0.035
        );


    public void anchorQuest(Pose3d robotPose){
        questNav.setPose(robotPose.transformBy(ROBOT_TO_QUEST));
    }

PoseFrame[] questFrames;

@Override
public void periodic() {

    questNav.commandPeriodic();

    // Get the latest pose data frames from the Quest
    questFrames = questNav.getAllUnreadPoseFrames();

    // System.out.println(questNav.isTracking() + " " + questNav.isConnected());
    // Loop over the pose data frames and send them to the pose estimator
    for (PoseFrame questFrame : questFrames) {
        // Make sure the Quest was tracking the pose for this frame
        if (questNav.isTracking() && questNav.isConnected() && DriverStation.isEnabled()) {
            // Get the pose of the Quest
            Pose3d questPose = questFrame.questPose3d();
            // Get timestamp for when the data was sent
            double timestamp = questFrame.dataTimestamp();

            // Transform by the mount pose to get your robot pose
            Pose3d robotPose = questPose.transformBy(ROBOT_TO_QUEST.inverse());

            // You can put some sort of filtering here if you would like!
            // System.out.println(robotPose.toPose2d());
            // Add the measurement to our estimator
            System.out.println(robotPose.toPose2d().toString());
            drivetrain.addVisionMeasurement(robotPose.toPose2d(), timestamp, QUESTNAV_STD_DEVS);
        }
    }
}
}
