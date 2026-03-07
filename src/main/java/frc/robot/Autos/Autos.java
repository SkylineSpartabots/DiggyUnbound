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

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Commands.Indexer.SetIndexer;
import frc.robot.Commands.Intake.SetIntake;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Indexer.IndexerStates;
import frc.robot.Subsystems.Intake.IntakeStates;



public final class Autos {
    private static CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();   

    private static final PIDController xController = new PIDController(5, 1, 0);
    private static final PIDController yController = new PIDController(5, 1, 0);
    private static final PIDController thetaController = new PIDController(0.1325, 0, 0);

    public static Command getAutoCommand(AutoPath autoPath) {
        return TestingAuto();
    }
    
    public static Command TestingAuto(){
        // Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("TestingAuto");
        
        // Optional<Trajectory<SwerveSample>> part1 = traj.get().getSplit(0);
        // Optional<Trajectory<SwerveSample>> part2 = traj.get().getSplit(1);

        return new SequentialCommandGroup(

            // new FollowChoreoTrajectory(part1.get()),
            // new SetIndexer(IndexerStates.ON),
            // new FollowChoreoTrajectory(part2.get()),
            // new SetIndexer(IndexerStates.OFF)
        );
    }

        public static Command TuningAuto(){
        // Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("TuningTest");
        
        // Optional<Trajectory<SwerveSample>> part1 = traj.get().getSplit(0);
        // Optional<Trajectory<SwerveSample>> part2 = traj.get().getSplit(1);
        // Optional<Trajectory<SwerveSample>> part3 = traj.get().getSplit(2);

        // AutoTrajectory autoTraj = Choreo.loadTrajectory("testing_auto").get();
        return new SequentialCommandGroup(
            // new FollowChoreoTrajectory(part1.get()),
            // new SetIntake(IntakeStates.ON),
            // new FollowChoreoTrajectory(part2.get()),
            // new SetIndexer(IndexerStates.OFF)
        );
    }

    public static Command Depo_Simple(){
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("depo_simple");
        return new SequentialCommandGroup(
            new InstantCommand(() -> {

            })
            // new FollowChoreoTrajectory(traj.get())
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
        
        TestingAuto("TestingAuto", TestingAuto()),
        Depo_Simple("Depo_Simple", Depo_Simple());

        String name;
        Command autoCommand;

        private AutoPath(String a, Command autoCommand) {
            name = a;
            this.autoCommand = autoCommand;
        }
    }
    
}
