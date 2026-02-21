package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
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

        // Slot0Configs slot0Configs = new Slot0Configs();
        // slot0Configs.kS = kS;
        // slot0Configs.kV = kV;


        // 20 ms
        motor.getVelocity().setUpdateFrequency(50);
        
        motor.getConfigurator().apply(config);  
        motor.optimizeBusUtilization();
    }

    // returns rps
    public double getLeaderVelocity() {
        return L_top_leader.getVelocity().getValueAsDouble();
    }
    
    public double[] getAllVelocities() {
        return new double[] {
            L_top_leader.getVelocity().getValueAsDouble(),
            L_bot.getVelocity().getValueAsDouble(),
            R_top.getVelocity().getValueAsDouble(),
            R_bot.getVelocity().getValueAsDouble()
        };
    }
    
    /**
     * set velocity of motors
     * @param velocity in rps
    */
    public void setVelocity(double velocity) {
        L_top_leader.setControl(new VelocityVoltage(velocity));
    }

    public void setRPM(int rpm) {
        double rps = rpm / 60.0;
        setVelocity(rps);
    }

    /**
     * set speed percent output of motors
     * @param percent from -1 to 1
    */
    public void setPercent(double percent) {
        L_top_leader.set(percent);
    }

}