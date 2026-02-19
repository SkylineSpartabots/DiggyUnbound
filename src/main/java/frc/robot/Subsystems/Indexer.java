package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
    private static Indexer instance;

    public static Indexer getInstance() {
        if(instance == null) {
            instance = new Indexer();
        }
        return instance;
    }

    public enum IndexerStates{
        ON(0.5),
        OFF(0),
        REVERSE(-0.5);

        double speed;
        private IndexerStates(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private TalonFX indexerMotor;

    public Indexer() {
        indexerMotor = new TalonFX(Constants.HardwarePorts.intakeID); //get real port
        config(indexerMotor, NeutralModeValue.Coast, InvertedValue.Clockwise_Positive);
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
        indexerMotor.set(speed);
    }

    public Command setIntakeState(IndexerStates state){
        return Commands.runOnce(() -> setSpeed(state.getSpeed()), this);
    }

    @Override
    public void periodic() {

    }
}
