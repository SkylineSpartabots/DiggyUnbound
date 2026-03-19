
package frc.robot.Commands.Pivot;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.Pivot;
import frc.robot.Subsystems.Intake.IntakeStates;
import frc.robot.Subsystems.Pivot.PivotStates;;

public class JiggleBallsPivot extends Command {
    private Pivot s_Pivot;
    Timer timer;

    public JiggleBallsPivot() {
        s_Pivot = Pivot.getInstance();

        timer = new Timer();

        addRequirements(s_Pivot);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        s_Pivot.setVoltage(0);

        if ((int)(timer.get() / 0.4) % 2 == 0 ) {
            s_Pivot.setVoltage(-3);
        } else {
            s_Pivot.setRotations(PivotStates.MIDDLE.getPosition());
        }
    }

    @Override
    public void end(boolean interrupted) {
        s_Pivot.setVoltage(0);
        s_Pivot.setRotations(PivotStates.DEPLOYED.getPosition());
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(5);
    }
}