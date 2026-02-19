package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.HardwarePorts;

public class Shooter extends SubsystemBase {
    private static Shooter instance;

    public static Shooter getInstance() {
        if(instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    private TalonFX L_top_leader, L_bot, R_top, R_bot;

    public Shooter() {
    L_top_leader = new TalonFX(HardwarePorts.shooterLT, "mechinisms");
    L_bot = new TalonFX(HardwarePorts.shooterLB, "mechinisms");
    R_top = new TalonFX(HardwarePorts.shooterRT, "mechinisms");
    R_bot = new TalonFX(HardwarePorts.shooterRB, "mechinisms");

    config(L_top_leader, NeutralModeValue.Coast, InvertedValue.CounterClockwise_Positive);
    config(L_bot, NeutralModeValue.Coast, InvertedValue.CounterClockwise_Positive);
    config(R_top, NeutralModeValue.Coast, InvertedValue.Clockwise_Positive);
    config(R_bot, NeutralModeValue.Coast, InvertedValue.Clockwise_Positive);

    L_bot.setControl(new Follower(L_top_leader.getDeviceID(), MotorAlignmentValue.Aligned));
    R_top.setControl(new Follower(L_top_leader.getDeviceID(), MotorAlignmentValue.Aligned));
    R_bot.setControl(new Follower(L_top_leader.getDeviceID(), MotorAlignmentValue.Aligned));
    }

    private void config(TalonFX motor, NeutralModeValue neutralMode, InvertedValue direction){
    TalonFXConfiguration config = new TalonFXConfiguration();

    config.MotorOutput.NeutralMode = neutralMode;
    config.MotorOutput.Inverted = direction;

    // 20 ms
    motor.getVelocity().setUpdateFrequency(50);
    
    motor.getConfigurator().apply(config);  
    motor.optimizeBusUtilization();
    }

    public void setRPM(int rpm) {

    }

    public void setVelocity(int rpm) {

    }





}