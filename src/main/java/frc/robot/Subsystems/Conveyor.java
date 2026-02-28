package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Conveyor extends SubsystemBase {
    private static Conveyor instance;

    public static Conveyor getInstance() {
        if(instance == null) {
            instance = new Conveyor();
        }
        return instance;
    }

    public enum ConveyorStates{
        ON(0.2),
        OFF(0),
        
        REVERSE(-0.5);
        double speed;
        private ConveyorStates(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private TalonFX conveyorMotor;

    public Conveyor() {
        conveyorMotor = new TalonFX(Constants.HardwarePorts.conveyor, "mechbussy"); //get real port
        config(conveyorMotor, NeutralModeValue.Coast, InvertedValue.Clockwise_Positive);
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
        conveyorMotor.set(speed);
    }

    public Command setState(ConveyorStates state){
        return Commands.runOnce(() -> setSpeed(state.getSpeed()), this);
    }

    @Override
    public void periodic() {

    }
}
