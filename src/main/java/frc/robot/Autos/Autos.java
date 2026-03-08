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
import frc.robot.Constants;
import frc.robot.Commands.Factories.CommandFactory;
import frc.robot.Commands.Indexer.SetIndexer;
import frc.robot.Commands.Intake.SetIntake;
import frc.robot.Commands.Pivot.ForcePivot;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Indexer.IndexerStates;
import frc.robot.Subsystems.Intake.IntakeStates;



public final class Autos {
    Timer path_time = new Timer();

    public static Command getAutoCommand(AutoPath autoPath) {
        return autoPath.autoCommand;
    }
    
    public static Command depo_simple(){
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("depo_simple");
        return new SequentialCommandGroup(

            new ForcePivot(),
            CommandFactory.IntakeBallsON(),

            new SequentialCommandGroup(
                new WaitCommand(3.3),
                CommandFactory.IntakeBallsOFF()
            ).alongWith(new FollowChoreoTrajectory(traj)),

            CommandFactory.AutoAimShoot().raceWith(new WaitCommand(6))
        );
    }

    public static Command mid(){
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("mid");
        System.out.println(traj);
        return new SequentialCommandGroup(
            new FollowChoreoTrajectory(traj),
            new ForcePivot(),
            CommandFactory.LobAtMeter(1.5).raceWith(new WaitCommand(5))
            );

    }

    public static Command trench_right_right_mid_chill(){
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("trench_right_right_mid_chill");
        return new SequentialCommandGroup(
            new SequentialCommandGroup(
                new WaitCommand(0.65),
                new ForcePivot(),
                CommandFactory.IntakeBallsON(),
                new WaitCommand(2.2),
                CommandFactory.IntakeBallsOFF()
            ).alongWith(new FollowChoreoTrajectory(traj)),
            CommandFactory.AutoAimShoot().raceWith(new WaitCommand(6))
        );
    }
   
    /*
     * Enum for the different autos. Contains a name and a mechCommands array. The mechCommands array contains
     * all the commands that the mechanisms will use (stuff that is unrelated to the drivetrain). These commands
     * will be executed in the order they are in the array during the auto path. Refer to runAutoCommand(AutoType auto).
     */
    public enum AutoPath {
        //when writing enums, if you want multiple mechCommands to run before the next path, put them in a sequential command group
        //if you want those mechCommands to run in parallel, put them in a parallelCommandGroup
        //if you want to run a mechCommand or mechCommandGroup in parallel with a path, create a boolean array with true values corresponding to the mechCommands you want to run in parallel.
        
        depo_pimple("depo_simple", depo_simple()),
        mid("mid", mid()),
        trench_right_right_mid_chill("trench_right_right_mid_chill", trench_right_right_mid_chill());

        String name;
        Command autoCommand;

        private AutoPath(String a, Command autoCommand) {
            name = a;
            this.autoCommand = autoCommand;
        }
    }
    
}
