// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Intake;


public class RobotContainer {

  /* controller setup */
  public final CommandXboxController driver = new CommandXboxController(0); // Driver joystick
  public final CommandXboxController operator = new CommandXboxController(1);

  private CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
  private Intake intake = Intake.getInstance();
  

  /* driver buttons */
  private final Trigger driverLeftTrigger = driver.leftTrigger();





  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {

      drivetrain.setDefaultCommand( // Drivetrain will execute t  his command periodically
          drivetrain.applyRequest(() -> drive(-driver.getLeftY(), -driver.getLeftX(), -driver.getRightX())) // Drive counterclockwise with negative X (left)
      );

      driver.leftBumper().whileTrue(new InstantCommand(() -> intake.setSpeed(20)));
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
