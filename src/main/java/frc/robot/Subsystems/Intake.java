package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private static Intake instance;

    private final VoltageOut voltageRequest = new VoltageOut(0);

    public static Intake getInstance() {
        if(instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    public enum IntakeStates {
        ON(6),
        OFF(0),
        REVERSE(-3);

        double voltage;
        private IntakeStates(double speed) {
            this.voltage = speed;
        }

        public double getVoltage() {
            return voltage;
        }
    }

    private TalonFX intakeMotor;

    public Intake() {
        intakeMotor = new TalonFX(Constants.HardwarePorts.intake, "mechbussy"); //get real port

        configureMotor(intakeMotor, NeutralModeValue.Brake, InvertedValue.CounterClockwise_Positive);
    }

    private void configureMotor(TalonFX motor, NeutralModeValue neutralMode, InvertedValue direction) {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput.Inverted = direction;
        config.MotorOutput.NeutralMode = neutralMode;

        motor.getConfigurator().apply(config);
    }

    public void setVoltage(double voltage) {
        intakeMotor.setVoltage(voltage);
    }

    public Command setState(IntakeStates state){
        return Commands.runOnce(() -> setVoltage(state.getVoltage()), this);
    }

    @Override
    public void periodic() {

    }
}
