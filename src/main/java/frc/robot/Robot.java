// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Subsystems.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Intake;

//LED stuff
import com.lumynlabs.connection.usb.USBPort;
import com.lumynlabs.devices.ConnectorXAnimate;
import com.lumynlabs.domain.config.ConfigBuilder;
import com.lumynlabs.domain.config.LumynDeviceConfig;
import com.lumynlabs.domain.config.NetworkType;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.units.Units;
import com.lumynlabs.domain.led.MatrixTextScrollDirection;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private CommandSwerveDrivetrain drivetrain;

  //LEDs
  private ConnectorXAnimate hades_leds = new ConnectorXAnimate();
  
  public Robot() {
    drivetrain = CommandSwerveDrivetrain.getInstance(); 

    System.out.println(SignalLogger.setPath("/media/sdb1/ctre-logs/"));
    SignalLogger.enableAutoLogging(false);
    System.out.println("gurt");
    // System.out.println(SignalLogger.start());

    m_robotContainer = new RobotContainer();
    // Intake.getInstance();
  }

  @Override
  public void robotInit() {
    //---LED---
    boolean isConnected = hades_leds.Connect(USBPort.kUSB1);
    System.out.println("ConnectorX connected: " + isConnected);
    //--Assigning leftward scroll of text: "2976"--
    hades_leds.leds.SetText("2976").ForZone("1")
      .WithColor(new Color(new Color8Bit(1, 130, 36)))
      .WithDirection(MatrixTextScrollDirection.Left)
      .WithDelay(Units.Milliseconds.of(70))
      .RunOnce(false);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
