// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Intake;


public class RobotContainer {

  private CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
  public final CommandXboxController driver = new CommandXboxController(0);
  private Intake intake = Intake.getInstance();


  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
        drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
        drivetrain.applyRequest(() -> drive(-driver.getLeftY(), -driver.getLeftX(), -driver.getRightX())) // Drive counterclockwise with negative X (left)
    );
    driver.a().onTrue(intake.setIntakeState(Intake.IntakeStates.ON));
    driver.b().onTrue(intake.setIntakeState(Intake.IntakeStates.OFF));
    driver.x().onTrue(intake.setIntakeState(Intake.IntakeStates.REVERSE));
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
