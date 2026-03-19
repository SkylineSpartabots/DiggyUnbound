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
import frc.robot.Subsystems.Vision.Quest;
import frc.robot.Commands.CommandFactory;
import frc.robot.Commands.Automation.AlignToGoal;
import frc.robot.Commands.Automation.JiggleBallsDrivetrain;
import frc.robot.Commands.Convayor.SetConveyor;
import frc.robot.Commands.Indexer.SetIndexer;
import frc.robot.Commands.Intake.SetIntake;
import frc.robot.Commands.Pivot.ForcePivot;
import frc.robot.Commands.Shooter.SetShooter;
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
    public final CommandXboxController opp = new CommandXboxController(1);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        /* DT bindings */

        drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
            drivetrain.applyRequest(
                () -> control.drive(
                    driver.getLeftY(), 
                    driver.getLeftX(), 
                    -driver.getRightX()
                )
            ) // Drive counterclockwise with negative X (left)
        );
        
        // final bindings -----------------------------------------------

        // driver.leftBumper().onTrue(CommandFactory.IntakeBallsON()); // top buttons
        // driver.rightBumper().onTrue(CommandFactory.IntakeBallsOFF());

        // driver.leftTrigger().onTrue(new InstantCommand(() -> control.turnOnAutoAim())); //bottom buttons
        // driver.rightTrigger().onTrue(new InstantCommand(() -> control.turnOffAutoAim()));

        // driver.x().onTrue(CommandFactory.ShootAtDistance());

        // driver.y().onTrue(new ForcePivot());
        // driver.y().onTrue(new ForcePivot(3));
        
        // driver.a().onTrue(CommandFactory.AllOff());

        // driver.povUp().onTrue(CommandFactory.LobAtMeter(2));
        // driver.povLeft().onTrue(CommandFactory.LobAtMeter(3));
        // driver.povRight().onTrue(CommandFactory.LobAtMeter(4));
        // driver.povDown().onTrue(new JiggleBallsDrivetrain(driver));

        // testing bindings -----------------------------------------------
        
        driver.b().onTrue(new InstantCommand(() -> intake.setVelocity(35))); // intake 
        driver.a().onTrue(new InstantCommand(() -> intake.setVoltage(0))); // intake
        
        driver.a().onTrue(allOff()); // intake

        // driver.x().onTrue(new SetIntake(IntakeStates.ON)); // intake 
        // driver.b().onTrue(new SetIntake(IntakeStates.OFF)); // intake 

        driver.povLeft().onTrue(new SetConveyor(ConveyorStates.ON)); // intake 
        driver.povRight().onTrue(new SetConveyor(ConveyorStates.OFF)); // intake 






        // driver.povUp().onTrue(CommandFactory.LobAtRps(25)); // intake 
        // driver.povLeft().onTrue(CommandFactory.LobAtRps(50)); // intake 
        // driver.povRight().onTrue(CommandFactory.LobAtRps(75)); // intake 
        // driver.povDown().onTrue(CommandFactory.LobAtRps(90)); // intake 

        // driver.x().onTrue(chud2());
        // driver.povUp().onTrue(new InstantCommand(() -> control.turnOnAutoAim()));
        // driver.povDown().onTrue(new InstantCommand(() -> control.turnOffAutoAim()));

        // driver.start().onTrue(new InstantCommand(() -> drivetrain.resetOdo()));
    
        // driver.x().onTrue(new InstantCommand(() -> shooter.setVelocity(75)));

        // driver.povLeft().onTrue(CommandFactory.AutoAimShoot());

        // driver.y().onTrue(new InstantCommand(() -> conveyor.setVoltage(7)));

        /* Sysid Bindings IGNORE TS */

        // driver.povLeft().onTrue(new InstantCommand(() -> SignalLogger.start()));
        // driver.povRight().onTrue(new InstantCommand(() -> SignalLogger.stop()));

        // driver.x().whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // driver.b().whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // driver.y().whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // driver.a().whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
    }

    public Command allOff() {
        return new ParallelCommandGroup(
            intake.setState(IntakeStates.OFF),
            conveyor.setState(ConveyorStates.OFF),
            indexer.setState(IndexerStates.OFF),
            new InstantCommand(() -> shooter.setVoltage(0))
        );
    }

    public Command chud() {
        return new ParallelCommandGroup(
            conveyor.setState(ConveyorStates.ON),
            indexer.setState(IndexerStates.ON),
            new InstantCommand(() -> shooter.setVelocity(15))
        );
    }

    public Command chud2() {
        return new ParallelCommandGroup(
            conveyor.setState(ConveyorStates.ON),
            intake.setState(IntakeStates.ON)
        );
    }

}
