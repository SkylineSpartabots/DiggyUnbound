// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.signals.UpdateModeValue;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;
import frc.robot.Subsystems.Vision.LimeLight;
import frc.robot.Subsystems.Vision.Quest;
import frc.robot.Autos.Autos;
import frc.robot.Autos.Autos.AutoPath;
import frc.robot.Subsystems.Climb;
import frc.robot.Subsystems.Conveyor;
import frc.robot.Subsystems.Indexer;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.Pivot;
import frc.robot.Subsystems.Shooter;

public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

    private final RobotContainer m_robotContainer;
    private CommandSwerveDrivetrain drivetrain;
    private LimeLight limeLight;
    
    SendableChooser<Autos.AutoPath> autoChooser = new SendableChooser<Autos.AutoPath>();
    
    public Robot() {
        drivetrain = CommandSwerveDrivetrain.getInstance(); 
        limeLight = LimeLight.getInstance();
        Indexer.getInstance();
        Intake.getInstance();
        Conveyor.getInstance();
        Shooter.getInstance();
        Pivot.getInstance();
        Climb.getInstance();
        Quest.getInstance();

        // autoChooser.setDefaultOption("depo_simple", AutoPath.depo_pimple);
        autoChooser.setDefaultOption("mid", AutoPath.mid);
        // autoChooser.setDefaultOption("trench_right_right_mid_chill", AutoPath.trench_right_right_mid_chill);
        SmartDashboard.putData("Auto choices", autoChooser);
        
        // SignalLogger.setPath("/media/sdb1/ctre-logs/");

        m_robotContainer = new RobotContainer();
  }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {
        limeLight.updateLimelight();
    }

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        m_autonomousCommand = Autos.getAutoCommand(autoChooser.getSelected());

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
