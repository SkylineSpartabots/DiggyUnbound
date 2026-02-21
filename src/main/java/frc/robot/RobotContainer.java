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
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Drivetrain.DriveControlSystems;
import frc.robot.Subsystems.Indexer;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.Shooter;


public class RobotContainer {

    private CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
    private Indexer indexer = Indexer.getInstance();
    private Intake intake = Intake.getInstance();
    private Shooter shooter = Shooter.getInstance();


    private DriveControlSystems control = new DriveControlSystems();
    public final CommandXboxController driver = new CommandXboxController(0);

    private double speed = 0; // just for testing

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

        /* shitty bindings for lazy testing lmao */
        driver.a().onTrue(new InstantCommand(() -> {
            shooter.setPercent(0);
            speed = 0;
        }));

        driver.y().onTrue(new InstantCommand(() -> {
            shooter.setPercent(speed + 0.1);
            speed += 0.1;
        }));

        driver.x().onTrue(new InstantCommand(() -> {
            shooter.setPercent(speed - 0.1);
            speed -= 0.1;
        }));

        /* Sysid Bindings IGNORE TS */

        // driver.leftBumper().onTrue(Commands.runOnce(SignalLogger::start));
        // driver.rightBumper().onTrue(Commands.runOnce(SignalLogger::stop));

        // driver.x().whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // driver.b().whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // driver.y().whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // driver.a().whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));



    }

    public SwerveRequest drive(double driverLY, double driverLX, double driverRX) {
        return new SwerveRequest.FieldCentric()
            .withVelocityX(driverLX)
            .withVelocityY(driverLY)
            .withRotationalRate(driverRX);
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }

}
