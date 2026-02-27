package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Pivot extends SubsystemBase {
    private static Pivot instance;

    public static Pivot getInstance() {
        if(instance == null) {
            instance = new Pivot();
        }
        return instance;
    }

    public enum PivotStates {
        ON(0.5),
        OFF(0),
        REVERSE(-0.5);

        double speed;
        private PivotStates(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private TalonFX pivotMotor;

    public Pivot() {
        pivotMotor = new TalonFX(Constants.HardwarePorts.intake); //get real port
        
        configureMotor(pivotMotor, NeutralModeValue.Brake, InvertedValue.Clockwise_Positive);
    }

    private void configureMotor(TalonFX motor, NeutralModeValue neutralMode, InvertedValue direction) {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput.Inverted = direction;
        config.MotorOutput.NeutralMode = neutralMode;

        motor.getConfigurator().apply(config);
    }

    public void setSpeed(double speed) {
        pivotMotor.set(speed);
    }

    public Command setState(PivotStates state){
        return Commands.runOnce(() -> setSpeed(state.getSpeed()), this);
    }

    @Override
    public void periodic() {

    }
}
