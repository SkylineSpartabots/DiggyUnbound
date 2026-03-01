package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
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

    private TalonFX topL_leader, botL, topR, botR;

    private double airtime; // seconds

    public Shooter() {
        topL_leader = new TalonFX(HardwarePorts.shooterTL, "mechbussy");
        topR = new TalonFX(HardwarePorts.shooterTR, "mechbussy");
        botL = new TalonFX(HardwarePorts.shooterBL, "mechbussy");
        botR = new TalonFX(HardwarePorts.shooterBR, "mechbussy");

        config(topL_leader, NeutralModeValue.Coast, InvertedValue.CounterClockwise_Positive);
        config(botL, NeutralModeValue.Coast, InvertedValue.CounterClockwise_Positive);
        config(topR, NeutralModeValue.Coast, InvertedValue.Clockwise_Positive);
        config(botR, NeutralModeValue.Coast, InvertedValue.Clockwise_Positive);

        botL.setControl(new Follower(topL_leader.getDeviceID(), MotorAlignmentValue.Aligned));
        topR.setControl(new Follower(topL_leader.getDeviceID(), MotorAlignmentValue.Opposed));
        botR.setControl(new Follower(topL_leader.getDeviceID(), MotorAlignmentValue.Opposed));
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
        return topL_leader.getVelocity().getValueAsDouble();
    }
    
    public double[] getAllVelocities() {
        return new double[] {
            topL_leader.getVelocity().getValueAsDouble(),
            botL.getVelocity().getValueAsDouble(),
            topR.getVelocity().getValueAsDouble(),
            botR.getVelocity().getValueAsDouble()
        };
    }
    
    /**
     * set velocity of motors
     * @param velocity in rps
    */
    public void setVelocity(double velocity) {
        topL_leader.setControl(new VelocityVoltage(velocity));
    }

    /**
     * set velocity of motors
     * @param voltage
    */
    public void setVoltage(double voltage) {
        topL_leader.setControl(new VoltageOut(voltage));
    }

    // theoretical
    double compensation = 1.05;
    public void setExitVelocity(double exitVelocity) {
        double rps = exitVelocity / (Constants.shooterRadiusM * Math.PI * 2);
        rps *= compensation;
        setVelocity(rps);
    }

    /**
     * set speed percent output of motors
     * @param percent from -1 to 1
    */
    public void setPercent(double percent) {
        topL_leader.set(percent);
    }

    public void updateAirtime(double airtime) {
        this.airtime = airtime;
    }

    public double getAirtime() {
        return airtime;
    }

}