package org.usfirst.frc.team1011.robot;

import frc.team1011.ruben.Resources;
import frc.team1011.ruben.SubSystem;
import edu.wpi.first.wpilibj.Solenoid;
import static org.usfirst.frc.team1011.robot.Robot.*;



public class GearCollectorHead implements SubSystem {
	
	private Solenoid angleSolenoid, clampSolenoid;
	private boolean angle = false, clamp = false;
	
	public GearCollectorHead(Solenoid angleSolenoid, Solenoid clampSolenoid){
		this.angleSolenoid=angleSolenoid;
		this.clampSolenoid=clampSolenoid;
			
	}
	
	public void collectingPosition(){
		toggleAngle(true);
		toggleClamp(false);
	}
	
	public void scoringPosition(){
		toggleAngle(false);
		toggleClamp(true);
	}
	
	public GearCollectorHead(){
	}
	
	public static void instantiate(){
		GearCollectorHead gch = new GearCollectorHead(Robot.resources.getSolenoid("GearCollectorAngleSolenoid"), Robot.resources.getSolenoid("GearCollectorClampSolenoid") );
		Robot.resources.addSubSystem("GearCollectorHead", gch);
	}
	
	public void toggleAngle(){
		toggleAngle(!angle);
		angle = !angle;
	}

	
	//SWITCH THIS
	public void toggleAngle(boolean state){
		angleSolenoid.set(state);
		angle = state;
	}
	
	public void toggleClamp(){
		toggleClamp(!clamp);
		clamp = !clamp;
	}
	
	public void toggleClamp(boolean state){
		clampSolenoid.set(!state);
		clamp = state;
	}
	
	
	
	
}
