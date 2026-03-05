// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Drivetrain.DriveControlSystems;
import frc.robot.Subsystems.Indexer.IndexerStates;
import frc.robot.Subsystems.Intake.IntakeStates;
import frc.robot.Subsystems.Pivot.PivotStates;
import frc.robot.Commands.SetShooter;
import frc.robot.Commands.Factories.CommandFactory;
import frc.robot.Subsystems.Climb;
import frc.robot.Subsystems.Conveyor;
import frc.robot.Subsystems.Indexer;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.Pivot;
import frc.robot.Subsystems.Shooter;
import frc.robot.Subsystems.Conveyor.ConveyorStates;

public class RobotContainer {

    private CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
    private Indexer indexer = Indexer.getInstance();
    private Intake intake = Intake.getInstance();

    private Conveyor conveyor = Conveyor.getInstance();
    private Shooter shooter = Shooter.getInstance();

    private Pivot pivot = Pivot.getInstance();
    private Climb climb = Climb.getInstance();

    private DriveControlSystems control = DriveControlSystems.getInstance();
    public final CommandXboxController driver = new CommandXboxController(0);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        /* DT bindings */
        drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
            drivetrain.applyRequest(
                () -> control.drive(
                    -driver.getLeftY(), 
                    -driver.getLeftX(), 
                    -driver.getRightX()
                )
            ) // Drive counterclockwise with negative X (left)
        );
        
        driver.a().onTrue(allOff()); // all off
        // driver.b().onTrue(intake.setState(IntakeStates.ON)); // intake 
        driver.x().onTrue(new InstantCommand(() -> shooter.setVelocity(75)));
        // driver.y().onTrue(indexer.setState(IndexerStates.ON));

        // driver.y().onTrue(new InstantCommand(() -> indexer.setVoltage(5)));

        /* Sysid Bindings IGNORE TS */

        // driver.povLeft().onTrue(new InstantCommand(() -> SignalLogger.start()));
        // driver.povRight().onTrue(new InstantCommand(() -> SignalLogger.stop()));

        // driver.x().whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // driver.b().whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // driver.y().whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // driver.a().whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }

    public Command allOff() {
        return new ParallelCommandGroup(
            intake.setState(IntakeStates.OFF),
            conveyor.setState(ConveyorStates.OFF),
            indexer.setState(IndexerStates.OFF),
            new InstantCommand(() -> shooter.setVoltage(0))
        );
    }

}
