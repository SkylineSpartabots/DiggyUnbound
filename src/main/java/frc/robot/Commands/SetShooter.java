package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Conveyor.ConveyorStates;
import frc.robot.Subsystems.Indexer.IndexerStates;
import frc.robot.Subsystems.Indexer;
import frc.robot.Subsystems.Conveyor;
import frc.robot.Subsystems.Shooter;

public class SetShooter extends Command {
    Shooter s_shooter;
    Indexer s_indexer;

    double voltage;

    public SetShooter(double velocity){
        s_shooter = Shooter.getInstance();
        s_indexer = Indexer.getInstance();

        this.voltage = velocity;

        addRequirements(s_shooter);
        addRequirements(s_indexer);
    }

    @Override
    public void initialize() {
        s_shooter.setVoltage(voltage);
        s_indexer.setState(IndexerStates.ON);
    }

    @Override
    public boolean isFinished(){
        return true;
    }

    @Override
    public void end(boolean interrupted){
    }

    
}
