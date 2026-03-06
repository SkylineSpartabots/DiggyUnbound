package frc.robot.Commands.Factories;

import java.util.Optional;

import choreo.Choreo;
import choreo.trajectory.SwerveSample;
import choreo.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Commands.RampShooterWithDistance;
import frc.robot.Commands.Convayor.SetConveyor;
import frc.robot.Commands.Indexer.SetIndexer;
import frc.robot.Subsystems.Climb;
import frc.robot.Subsystems.Conveyor;
import frc.robot.Subsystems.Conveyor.ConveyorStates;
import frc.robot.Subsystems.Indexer;
import frc.robot.Subsystems.Indexer.IndexerStates;
import frc.robot.Subsystems.Intake.IntakeStates;
import frc.robot.Subsystems.Pivot;
import frc.robot.Subsystems.Shooter;
import frc.robot.Subsystems.Drivetrain.AlignToGoal;
import frc.robot.Subsystems.Drivetrain.CommandSwerveDrivetrain;

public class CommandFactory {

    public static Command AutoAimShoot(){
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                // new AlignToGoal(),
                new WaitCommand(1.5)
            ),
            new SetIndexer(IndexerStates.ON),
            // new SetConveyor(ConveyorStates.ON),
            new WaitCommand(5),
            // new SetConveyor(ConveyorStates.OFF),
            new SetIndexer(IndexerStates.OFF)
        ).alongWith(new RampShooterWithDistance());
    }

    
}
