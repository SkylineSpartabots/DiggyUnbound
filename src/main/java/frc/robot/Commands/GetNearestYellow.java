package frc.robot.Commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Limelight;

public class GetNearestYellow extends Command {
    //Locates the nearest ball and finds coordinates
    Limelight s_limeLight;
    
    public GetNearestYellow() {
        s_limeLight = Limelight.getInstance();
    }
}
