package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Convayor.ConvayorStates;
import frc.robot.Subsystems.Convayor;

public class SetClimb extends Command {
    Convayor s_climb;
    Timer timer = new Timer();

    public SetClimb(){
        s_climb = Convayor.getInstance();
    }

    @Override
    public void initialize() {
        timer.restart();
        s_climb.setState(ConvayorStates.ON);
    }

    @Override
    public boolean isFinished(){
        return timer.hasElapsed(5);
    }

    @Override
    public void end (boolean interrupted){
        s_climb.setState(ConvayorStates.OFF);
    }

    
}
