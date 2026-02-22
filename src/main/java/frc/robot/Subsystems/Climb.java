package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climb extends SubsystemBase {
    private static Climb instance;

    public static Climb getInstance() {
        if(instance == null) {
            instance = new Climb();
        }
        return instance;
    }

    public enum ClimbStates{
        ON(0.5),
        OFF(0),
        REVERSE(-0.5);

        double speed;
        private ClimbStates(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private TalonFX climbMotor;

    public Climb() {
        climbMotor = new TalonFX(Constants.HardwarePorts.climbL); //get real port
        config(climbMotor, NeutralModeValue.Brake, InvertedValue.Clockwise_Positive);
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

    public void setSpeed(double speed) {
        climbMotor.set(speed);
    }

    public Command setState(ClimbStates state){
        return Commands.runOnce(() -> setSpeed(state.getSpeed()), this);
    }

    @Override
    public void periodic() {

    }
}
