package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Climb.ClimbStates;
import frc.robot.Subsystems.Climb;

public class SetClimb extends Command {
    Climb s_climb;
    Timer timer = new Timer();

    public SetClimb(){
        s_climb = Climb.getInstance();
    }

    @Override
    public void initialize() {
        timer.restart();
        s_climb.setState(ClimbStates.ON);
    }

    @Override
    public boolean isFinished(){
        return timer.hasElapsed(5);
    }

    @Override
    public void end (boolean interrupted){
        s_climb.setState(ClimbStates.OFF);
    }

    
}
