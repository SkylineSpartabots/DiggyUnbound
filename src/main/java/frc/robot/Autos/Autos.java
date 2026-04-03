// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Autos;

import java.io.SequenceInputStream;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Optional;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ForwardPerspectiveValue;

import choreo.Choreo;
import choreo.auto.AutoTrajectory;
import choreo.trajectory.SwerveSample;
import choreo.trajectory.Trajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.Commands.CommandFactory;
import frc.robot.Commands.Automation.JiggleBallsDrivetrain;
import frc.robot.Commands.Automation.JiggleBallsDrivetrainNoRolla;
import frc.robot.Commands.Indexer.SetIndexer;
import frc.robot.Commands.Intake.SetIntake;
import frc.robot.Commands.Pivot.ForcePivot;
import frc.robot.Commands.Shooter.RampShooterWithDistance;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Indexer.IndexerStates;
import frc.robot.Subsystems.Intake.IntakeStates;

public final class Autos {
    static Timer path_time = new Timer();

    public static Command getAutoCommand(AutoPath autoPath) {
        return autoPath.autoCommand;
    }

    public static Command depo_simple() {
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("depo_simple");
        return new SequentialCommandGroup(
                new ForcePivot(),
                CommandFactory.IntakeBallsON(),
                new SequentialCommandGroup(
                        new WaitCommand(3.3),
                        CommandFactory.IntakeBallsOFF()).alongWith(new FollowChoreoTrajectory(traj)),

                CommandFactory.AutoAimShoot().raceWith(new WaitCommand(6))
            );
    }

    public static Command mid() {
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("mid");
        return new SequentialCommandGroup(
                new WaitCommand(2.5),
                new ForcePivot(),
                CommandFactory.ShootAtDistance()
        ).alongWith(new FollowChoreoTrajectory(traj));
    }

    public static Command mid_to_depo() {
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("mid_to_depo");
        return new SequentialCommandGroup(
                new WaitCommand(2),
                new ForcePivot(), //2.5
                new SetIntake(IntakeStates.ON),
                new WaitCommand(3), //5.5
                CommandFactory.ShootAtDistance()
        ).alongWith(new FollowChoreoTrajectory(traj));
    }

    public static Command mid_right() {
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("mid_right");
        return new SequentialCommandGroup(
                new WaitCommand(2.25),
                new ForcePivot(),
                CommandFactory.ShootAtDistance()
        ).alongWith(new FollowChoreoTrajectory(traj));
    }

    public static Command mid_tuning() {
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("mid_tuning");
        return new SequentialCommandGroup(
            new FollowChoreoTrajectory(traj)
            // CommandFactory.IntakeBallsON(),
            // new WaitCommand(1),
            // CommandFactory.IntakeBallsOFF()
        );


    }

    public static Command trench_right_right_mid_chill() {
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("trench_right_right_mid_chill");
        return new SequentialCommandGroup(
            new SequentialCommandGroup(
                new WaitCommand(0.65),
                new ForcePivot(),
                CommandFactory.IntakeBallsON(),
                        new WaitCommand(2.2),
                        CommandFactory.IntakeBallsOFF()).alongWith(new FollowChoreoTrajectory(traj)),
                        CommandFactory.AutoAimShoot().raceWith(new WaitCommand(6)));
    }
                    
    public static Command trench_left_left_mid_chill() {
        // Timer timer = new Timer();
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("trench_left_left_mid_chill");
        return new SequentialCommandGroup(

                new SequentialCommandGroup(
                    new WaitCommand(0.8),
                    // Commands.runOnce(timer::restart),
                    // new WaitUntilCommand(() -> timer.hasElapsed(0.8)),
                    new ForcePivot(), //1.3
                    CommandFactory.IntakeBallsON(),
                    new WaitCommand(2.6), //
                    // new WaitUntilCommand(() -> timer.hasElapsed(4.4)),
                    CommandFactory.IntakeBallsOFF()
                    ).alongWith(new FollowChoreoTrajectory(traj)),

                CommandFactory.AutoAimShoot(),
                new JiggleBallsDrivetrainNoRolla()
            );
    }

    public enum AutoPath {
        trench_right_right_mid_chill("trench_right_right_mid_chill", trench_right_right_mid_chill());

        String name;
        Command autoCommand;

        private AutoPath(String a, Command autoCommand) {
            name = a;
            this.autoCommand = autoCommand;
        }
    }

}
