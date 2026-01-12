package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private static Intake instance;

    public static Intake getInstance() {
        if(instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    public enum IntakeStates {
        ON(0.5),
        OFF(0),
        REVERSE(-0.5);

        double speed;
        private IntakeStates(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private TalonFX intakeMotor;

    public Intake() {
        intakeMotor = new TalonFX(20); //get real port
        configureMotor(intakeMotor, InvertedValue.Clockwise_Positive, NeutralModeValue.Coast);
    }

    private void configureMotor(TalonFX motor, InvertedValue direction, NeutralModeValue neutralMode) {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput.Inverted = direction;
        config.MotorOutput.NeutralMode = neutralMode;

        motor.getConfigurator().apply(config);
    }

    public void setSpeed(double speed) {
        intakeMotor.set(speed);
    }

    public Command setIntakeState(IntakeStates state){
        return Commands.runOnce(() -> setSpeed(state.getSpeed()), this);
    }

    @Override
    public void periodic() {

    }
}
