// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Optional;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ForwardPerspectiveValue;

import choreo.Choreo;
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
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;



public final class Autos {
    private static CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();   

    private static final PIDController thetaController = new PIDController(3, 1.4, 0); //tune?
    private static final PIDController xController = new PIDController(5, 1, 0);
    private static final PIDController yController = new PIDController(5, 1, 0);



    public static Command getAutoCommand(AutoPath autoPath) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                    
                ), 
                autoPath.autoCommand
                );
    }

    public static Command FollowChoreoTrajectory(SwerveSample sample) {
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        return Commands.run(() -> {
            // Get the current pose of the robot
            Pose2d pose = drivetrain.getState().Pose;

            // Generate the next speeds for the robot
            ChassisSpeeds speeds = new ChassisSpeeds(
                sample.vx + xController.calculate(pose.getX(), sample.x),
                sample.vy + yController.calculate(pose.getY(), sample.y),
                sample.omega + thetaController.calculate(pose.getRotation().getRadians(), sample.heading)
            );

            // Apply the generated speeds
            drivetrain.setControl(
                new SwerveRequest.FieldCentric()
                .withVelocityX(speeds.vxMetersPerSecond)
                .withVelocityY(speeds.vyMetersPerSecond)
                .withRotationalRate(speeds.omegaRadiansPerSecond)
                .withForwardPerspective(ForwardPerspectiveValue.BlueAlliance)
                .withDriveRequestType(com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType.Velocity)
                .withSteerRequestType(com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType.MotionMagicExpo)
                .withDesaturateWheelSpeeds(true)
            );
        }, drivetrain);
    }
        // Trajectory<SwerveSample> traj = path;
        // thetaController.reset();
        // xController.reset();
        // yController.reset();

        
        // SmartDashboard.putNumber("Start pose x", traj.getInitialPose().getX());
        // Command swerveCommand = Choreo.choreoSwerveCommand(
        //         traj,
        //         drivetrain::getPose,
        //         xController,
        //         yController,
        //         thetaController,
        //         (ChassisSpeeds speeds) -> drivetrain.setControl(drive.withSpeeds(speeds)),
        //         // () -> {return false;},
        //         // (ChassisSpeeds speeds) -> s_Swerve.applyRequest(() -> drive.withSpeeds(speeds)),
        //         () -> {
        //             Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        //             return alliance.isPresent() && alliance.get() == Alliance.Red;
        //         },
        //         drivetrain);

        // return swerveCommand;
    // }
    //FIXME: IF THE AUTO MOVEMENTS ARE ONE MOVEMENT EARLY, THEN JUST INCREASE TRAJECTORY #s BY ONE OR CHANGE IN CHOREO
    
    public static Command LeftTrenchNearSide(){
        Optional<Trajectory<SwerveSample>> traj = Choreo.loadTrajectory("LeftTrenchNearSide");
        return new SequentialCommandGroup(
            new InstantCommand(() -> {
                System.out.println("yea we're cookedd lmaooooo");
            }),

            FollowChoreoTrajectory(traj.get().sampleAt(0, false).get())
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
        
        LeftTrenchNearSide("LeftTrenchNearSide", LeftTrenchNearSide() );

        String name;
        Command autoCommand;

        private AutoPath(String a, Command autoCommand) {
            name = a;
            this.autoCommand = autoCommand;
        }
    }
    
}
