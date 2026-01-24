// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Subsystems.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Intake;


public class RobotContainer {

  /* controller setup */
  public final CommandXboxController driver = new CommandXboxController(0); // Driver joystick
  public final CommandXboxController operator = new CommandXboxController(1);

  private CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
  private Intake intake = Intake.getInstance();
  

  /* driver buttons */
  private final Trigger driverBack = driver.back();
  private final Trigger driverStart = driver.start();
  private final Trigger driverA = driver.a();
  private final Trigger driverB = driver.b();
  private final Trigger driverX = driver.x();
  private final Trigger driverY = driver.y();
  private final Trigger driverRightBumper = driver.rightBumper();
  private final Trigger driverLeftBumper = driver.rightBumper();
  private final Trigger driverLeftTrigger = driver.leftTrigger();
  private final Trigger driverRightTrigger = driver.rightTrigger();
  private final Trigger driverDpadUp = driver.povUp();
  private final Trigger driverDpadDown = driver.povDown();
  private final Trigger driverDpadLeft = driver.povLeft();
  private final Trigger driverDpadRight = driver.povRight();


  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {

      drivetrain.setDefaultCommand( // Drivetrain will execute t  his command periodically
          drivetrain.applyRequest(() -> drive(-driver.getLeftY(), -driver.getLeftX(), -driver.getRightX())) // Drive counterclockwise with negative X (left)
      );

      driverBack.onTrue(new InstantCommand(() -> drivetrain.resetPose(new Pose2d())));

      driverLeftTrigger.onTrue(new InstantCommand(() -> intake.setSpeed(-0.5)));
      driverLeftTrigger.onFalse(new InstantCommand(() -> intake.setSpeed(0)));
      
  }

  public SwerveRequest drive(double driverLY, double driverLX, double driverRX) {
      return new SwerveRequest.FieldCentric()
      .withVelocityX(driverLX * 5)
      .withVelocityY(driverLY * 5)
      .withRotationalRate(driverRX *5);
  }

  public Command getAutonomousCommand() {
      return Commands.print("No autonomous command configured");
  }

}
