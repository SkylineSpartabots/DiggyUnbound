package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Convayor.ConvayorStates;
import frc.robot.Subsystems.Convayor;
import frc.robot.Subsystems.Shooter;

public class SetShooter extends Command {
    Shooter s_shooter;
    Timer timer = new Timer();

    double voltage;

    public SetShooter(double velocity){
        s_shooter = Shooter.getInstance();

        this.voltage = velocity;

        addRequirements(s_shooter);
    }

    @Override
    public void initialize() {
        s_shooter.setVoltage(voltage);
    }

    @Override
    public boolean isFinished(){
        return true;
    }

    @Override
    public void end(boolean interrupted){
    }

    
}
