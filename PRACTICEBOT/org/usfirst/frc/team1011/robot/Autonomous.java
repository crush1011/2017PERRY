package org.usfirst.frc.team1011.robot;

import java.util.ArrayList;
import java.util.Arrays;

import com.kauailabs.navx.frc.AHRS;
import org.usfirst.frc.team1011.robot.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.*;
import vison.VisionLoop;

public class Autonomous {

	public static void setAutosDashboard(){
		ArrayList<String> autos = new ArrayList<>();
		autos.add("Do Nothing");
		autos.add("2 Gear Left");
		autos.add("2 Gear Right");
		autos.add("1 Gear Right");
		autos.add("1 Gear Left");
		autos.add("Cross Baseline");
		
		//new SmartDashboard().putStringArray("Auto Selector", (String[]) autos.toArray());
		
	}
	public static AutonomousRoutine getAuto(String name){
		AutonomousRoutine finalRoutine;
		switch(name){
		case "1 Gear Left":
			
			finalRoutine=new AutonomousRoutine(Arrays.asList(
					AutonomousRoutine.gearLeft,
					new AutonDriveStraight(2000, -0.9, 0),
					((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop
					));
			break;
		case "2 Gear Side Right":
			SmartDashboard.putString("DB/String 4", "Okay");
			finalRoutine=new AutonomousRoutine(Arrays.asList(
					NewAutonomousRoutines.TwoGearSideRight,
					new AutonDriveStraight(2000, -0.9, 0),
					((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop
					));
			break;
		case "2 Gear Side Left":
			SmartDashboard.putString("DB/String 4", "Okay");
			finalRoutine=new AutonomousRoutine(Arrays.asList(
					ChezyAutos.TwoGearSideLeft,
					new AutonDriveStraight(2000, -0.9, 0),
					((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop
					));
			break;
		case "1 Gear Right":
			 
			finalRoutine = new AutonomousRoutine(Arrays.asList(
					AutonomousRoutine.gearRight
					,new AutonDriveStraight(2000, -0.9, 0),
					((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop));
			break;
		case "1 Gear Middle":
			//finalRoutine = new AutonomousRoutine(Arrays.asList(new DriveToPeg(90, -0.7, 450, true)));
			Runnable a = new AutonDriveStraight(1600, -0.65);
			Runnable collector = new Runnable(){
				public void run(){
					GearCollectorHead gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
					GearCollectorArm gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
					gca.setAngle(Resources.getInstance().SCORING_POSITION);
					gch.toggleAngle(false);
					gch.toggleClamp(true);
					
				}
			};
			Runnable secondCollector = new Runnable(){
				public void run(){
					GearCollectorHead gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
					gch.toggleClamp(false);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gch.toggleClamp(true);
				}
			};
			Runnable tryAgain = new Runnable(){
				public void run(){
					GearCollectorHead gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
					GearCollectorArm gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
					VisionLoop visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
					gca.setAngle(90);
					new AutonDriveForTime((DriveTrain)Resources.getInstance().getSubSystem("DriveTrain"), 800,0.7).run();
					if(visionLoop.distanceFromTarget()<100){
						new DriveToPeg(visionLoop.distanceFromTarget() + 10 , -0.6, 100, true, false).run();

					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gca.setAngle(90);
					gch.scoringPosition();
					
					
					
				}
			};
			
			Runnable driveAway = new Runnable(){
				public void run(){
					
					new DriveSemiCircle((DriveTrain)Resources.getInstance().getSubSystem("DriveTrain"),-95,180, true).run();
					((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")).turnTo(180);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new AutonDriveStraight(2000,0.9,180).run();
				}
			};

			

			Runnable killVision = ((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop; 
			//AutonDriveForTime b = new AutonDriveForTime(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")), 350,0.5);
			
			finalRoutine = new AutonomousRoutine(Arrays.asList(collector,a, secondCollector, tryAgain,killVision, driveAway ));
			
			//	finalRoutine = new AutonomousRoutine(Arrays.asList(AutonomousRoutine.TwoGearRight));
		//	finalRoutine = new AutonomousRoutine(Arrays.asList());
			break;
		case "2 Gear Left":
			//DriveToPeg action1 = new DriveToPeg(0);
			finalRoutine = new AutonomousRoutine(Arrays.asList(AutonomousRoutine.TwoGearLeft,((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop));
			break;
		case "New 2 Gear Left":
			//DriveToPeg action1 = new DriveToPeg(0);
			finalRoutine = new AutonomousRoutine(Arrays.asList(NewAutonomousRoutines.TwoGearLeftNew,((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop));
			break;
		case "New 2 Gear Right":
			//DriveToPeg action1 = new DriveToPeg(0);
			finalRoutine = new AutonomousRoutine(Arrays.asList(ChezyAutos.TwoGearRightNew,((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop));
			break;
		case "3 JEER":
			finalRoutine = new AutonomousRoutine(Arrays.asList(ChezyAutos.ThreeJEER,((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop));
			break;
		case "2 Gear Right":
		
			/*Runnable a = new AutonDriveStraight(1600, -0.65);
			Runnable collector = new Runnable(){
				public void run(){
					GearCollectorHead gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
					GearCollectorArm gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
					gca.setAngle(Resources.getInstance().SCORING_POSITION);
					gch.toggleAngle(false);
					gch.toggleClamp(true);
					
				}
			};
			Runnable secondCollector = new Runnable(){
				public void run(){
					GearCollectorHead gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
					gch.toggleClamp(false);
				}
			};

			Runnable killVision = ((VisionLoop) Resources.getInstance().getSensor("vision")).killVisionLoop; 
			//AutonDriveForTime b = new AutonDriveForTime(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")), 350,0.5);
			
			finalRoutine = new AutonomousRoutine(Arrays.asList(collector,a, secondCollector, killVision));
			
			//	finalRoutine = new AutonomousRoutine(Arrays.asList(AutonomousRoutine.TwoGearRight));
		//	finalRoutine = new AutonomousRoutine(Arrays.asList());
			break;*/
		case "Cross Baseline":
			finalRoutine = new AutonomousRoutine(Arrays.asList(AutonomousRoutine.BaseLine));
			break;
		case "Test":
			Runnable run100 = new Runnable(){
				DriveTrain driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
				public void run(){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					double d = ((VisionLoop) Resources.getInstance().getSensor("vision")).distanceFromTarget() + 15; // 10
					if (d < 150) {
						new DriveToPeg(d, -0.85, 450, true, true).run();
					}
					/*double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
					double visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();

					SmartDashboard.putString("DB/String 4", "" + visionAngle);
					driveTrain.turnTo(currentAngle + visionAngle);
					try {
						Thread.sleep(600);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					driveTrain.turning = false;	
					driveTrain.stopMotors();*/
					//new AutonDriveForMeasurement(driveTrain, 50, 0.9).run();
					//new AutonStraightMeasurement(70/12,-0.95, 0,true).run();
					//new AutonLine(SmartDashboard.getNumber("DB/Slider 0",0), 0.9,0).run();
					/*	driveTrain.turnTo(0);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					driveTrain.turning=false;
					driveTrain.stopMotors();*/
				}
				
			};
			finalRoutine = new AutonomousRoutine(Arrays.asList(run100));
			break;
			
			
		case "1 Gear Left Hopper Long":
			finalRoutine = new AutonomousRoutine(Arrays.asList(
					AutonomousRoutine.gearLeft,
					new AutonDriveForMeasurement(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")), 24, 1),
					new AutonTurning(270, 1500),
					new AutonDriveForTime((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain"), 1000, -0.6))
					);
			break;
		case "1 Gear Right Hopper Long":
			finalRoutine = new AutonomousRoutine(Arrays.asList(
					AutonomousRoutine.gearRight,
					new AutonDriveForMeasurement(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")), 24, 1),
					new AutonTurning(90, 1500),
					new AutonDriveForTime((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain"), 1000, -0.6))
					);
			break;
		case "1 Gear Left Hopper Short":
			finalRoutine = new AutonomousRoutine(Arrays.asList(
					AutonomousRoutine.gearLeft,
					new AutonDriveForMeasurement(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")), 7.5, 1),
					new AutonTurning(270, 1500),
					new AutonDriveForTime((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain"), 1000, -0.6))
					);
			break;
		case "1 Gear Right Hopper Short":
			finalRoutine = new AutonomousRoutine(Arrays.asList(
					AutonomousRoutine.gearLeft,
					new AutonDriveForMeasurement(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")),7.5, 1),
					new AutonTurning(90, 1500),
					new AutonDriveForTime((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain"), 1000, -0.6))
					);
			break;
		default:
			finalRoutine = new AutonomousRoutine(Arrays.asList());
		
		}
		
		return finalRoutine;
	}
	
}
