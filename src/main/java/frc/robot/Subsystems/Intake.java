// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Intake extends SubsystemBase {

    private static Intake instance;
      
    private final Compressor compressor;
    private final Solenoid solenoid;
    
    private TalonFX intakeM, pivotM;

    public IntakeStates intakeState = IntakeStates.RETRACTED;

    public static Intake getInstance() {
        if (instance == null) { 
            instance = new Intake();
        }
        return instance;
    }

    public enum IntakeStates {
        RETRACTED(false, 0),
        ON_DEPLOYED(true, 1),
        OFF_DEPLOYED(true, 0),
        REV_DEPLOYED(true, -0.5);

        public boolean deployed;
        public double direction;

        private IntakeStates(boolean deployed, double direction) {
            this.deployed = deployed;
            this.direction = direction;
        }
    }

    public Intake() {
        compressor = new Compressor(Constants.HardwarePorts.pneumaticHub, PneumaticsModuleType.REVPH);
        solenoid = new Solenoid(
            Constants.HardwarePorts.pneumaticHub, 
            PneumaticsModuleType.REVPH, 
            Constants.HardwarePorts.intakeSolenoidChannel);
        
        intakeM = new TalonFX(0,"idk lmao");
        pivotM = new TalonFX(0, "lol");

        configMotor(intakeM, NeutralModeValue.Brake);

    }

    private void configMotor(TalonFX motor, NeutralModeValue neutralModeValue) {
        motor.setNeutralMode(neutralModeValue);
    }

    public void setState(IntakeStates state) {
        this.intakeState = state;
        solenoid.set(intakeState.deployed);
        // offset value for speed?
        setSpeed(intakeState.direction);
    }

    public void setSpeed(double speed) {
        intakeM.set(speed);
    }

    public boolean isIntakeDeployed() {
        return intakeState.deployed;
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
      // This method will be called once per scheduler run during simulation
    }
}
