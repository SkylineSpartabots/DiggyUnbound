package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Pivot;

public class SetIntake extends Command {
    Pivot s_intake;
    Timer timer = new Timer();
    
    public SetIntake() {
        s_intake = Pivot.getInstance();
    }

    @Override
    public void initialize() {
        timer.restart();
        s_intake.setSpeed(0.5);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        s_intake.setSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(5);
    }
}
