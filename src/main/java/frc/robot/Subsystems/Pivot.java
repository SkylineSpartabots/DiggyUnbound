package frc.robot.Subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Subsystems.Intake.IntakeStates;

public class Pivot extends SubsystemBase {
    private static Pivot instance;
    private final VoltageOut voltageRequest = new VoltageOut(0);

    public static Pivot getInstance() {
        if(instance == null) {
            instance = new Pivot();
        }
        return instance;
    }

    public enum PivotStates {
        DEPLOYED(-4.07),
        STOWED(0);

        double position;
        private PivotStates(double position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }   
    }

    private TalonFX pivotMotor;
    private final MotionMagicVoltage mmRequest = new MotionMagicVoltage(0).withSlot(0);

    public Pivot() {
        pivotMotor = new TalonFX(Constants.HardwarePorts.pivot, "mechbussy"); //get real port
        
        configureMotor(pivotMotor, NeutralModeValue.Brake, InvertedValue.Clockwise_Positive);
    }

    private void configureMotor(TalonFX motor, NeutralModeValue neutralMode, InvertedValue direction) {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;

        config.Slot0.kP = 0.01;
        config.Slot0.kG = 0.5;

        config.MotorOutput.Inverted = direction;
        config.MotorOutput.NeutralMode = neutralMode;

        motor.getConfigurator().apply(config);

        motor.setPosition(0);
    }

    public void setRotations(double rotations) {
        pivotMotor.setControl(mmRequest.withPosition(rotations));
    }

    public void setVoltage(double volts) {
        pivotMotor.setControl(voltageRequest.withOutput(volts));
    }

    public void setSpeed(double speed) {
        pivotMotor.set(speed);
    }

    public Command setState(IntakeStates state){
        return Commands.runOnce(() -> setRotations(state.getVoltage()), this);
    }

    public void zeroPivot() {
        pivotMotor.setPosition(0);
    }

    public StatusSignal<Current> getCurrent() {
        return pivotMotor.getStatorCurrent();
    }

    @Override
    public void periodic() {

    }
}
