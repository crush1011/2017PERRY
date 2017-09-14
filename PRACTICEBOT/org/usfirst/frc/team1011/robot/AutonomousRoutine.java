package org.usfirst.frc.team1011.robot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.AutonDriveForMeasurement;
import frc.team1011.ruben.AutonDriveForTime;
import frc.team1011.ruben.AutonDriveStraight;
import frc.team1011.ruben.DriveSemiCircle;
import frc.team1011.ruben.DriveTrain;
import frc.team1011.ruben.Resources;
import vison.VisionLoop;

public class AutonomousRoutine {

	public static AutonomousRoutine finalRoutine;
	List<Runnable> actionsByOrder;
	Integer order;
	boolean stop;
	

	public static Runnable gearRight = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
			double currentTime = System.currentTimeMillis();
			gca.setAngle(75);
			gch.toggleClamp(true);
			new AutonDriveStraight(820,-0.95,0).run();
			//new AutonDriveForMeasurement(driveTrain, 5.2, 0.75).run();

			driveTrain.stopMotors();

			driveTrain.turnTo(300);//305
			try {
				Thread.sleep(750);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			// DriveToPeg action = new DriveToPeg();
			// action.run();

			//new DriveToPeg(66 , -0.6, 450, true, false).run();
			
			double d = visionLoop.distanceFromTarget();
			if(d <150){
				new DriveToPeg( d, -0.7, 450, true, true).run();
				//new DriveToPeg(6, -0.75, 450, false).run();	
			}
			driveTrain.turnTo(0);
			gca.setAngle(90);
			gch.toggleClamp(true);
			gch.toggleAngle(false);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
		}

	};

	public static Runnable gearLeft = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
			double currentTime = System.currentTimeMillis();
			gca.setAngle(75);
			gch.toggleClamp(true);
			new AutonDriveStraight(950,-0.8,0).run();
			//new AutonDriveForMeasurement(driveTrain, 5.2, 0.75).run();

			driveTrain.stopMotors();

			driveTrain.turnTo(60);//305
			try {
				Thread.sleep(750);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			// DriveToPeg action = new DriveToPeg();
			// action.run();

			new DriveToPeg(66 , -0.6, 450, true, false).run();
			
			driveTrain.turnTo(0);
			gca.setAngle(90);
			gch.toggleClamp(true);
			gch.toggleAngle(false);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
		}

	};

	public static Runnable test = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		public void run() {
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");

			driveTrain.turnTo(60);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			}
	};

	public static Runnable TwoGearRight = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");

			/*new DriveToPeg(8, -0.7).run();*/
			gca.setAngle(75);
			gch.toggleAngle(false);
			gch.toggleClamp(true);
			new AutonDriveForMeasurement(driveTrain, 5,0.5).run();;
			double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			double visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();
			if(Math.abs(visionAngle)>8){
				driveTrain.turnTo(currentAngle + visionAngle);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				driveTrain.turning = false;			
			}
			
			gca.setAngle(Resources.getInstance().SCORING_POSITION);
			
			try {
				Thread.sleep(800);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			double currentTime = System.currentTimeMillis();
			while (System.currentTimeMillis() - currentTime < 1300 && DriverStation.getInstance().isAutonomous()) {
				driveTrain.arcadeDrive(-0.4, 0);
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			driveTrain.stopMotors();

			//
			gch.toggleAngle(true);
			gch.toggleClamp(false);
			//
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentTime = System.currentTimeMillis();
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			while (System.currentTimeMillis() - currentTime < 450 && DriverStation.getInstance().isAutonomous()) {
				driveTrain.arcadeDrive(0.95, 0);
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			
			
			
			new DriveSemiCircle(driveTrain, SmartDashboard.getNumber("DB/Slider 0", 30), 180, true).run();
			driveTrain.stopMotors();
			driveTrain.turnTo(180);
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			gch.toggleClamp(false);
			gch.toggleAngle(true);
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			new AutonDriveForMeasurement(driveTrain, 2, 0.75).run();

			currentTime = System.currentTimeMillis();
			while (System.currentTimeMillis() - currentTime < 850 && DriverStation.getInstance().isAutonomous()) {
				driveTrain.arcadeDrive(-0.5, 0);
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			driveTrain.stopMotors();
			gca.setAngle(75);
			gch.toggleAngle(false);
			gch.toggleClamp(true);
			try {
				Thread.sleep(450);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new AutonDriveForMeasurement(driveTrain, -1, 0.7);
			driveTrain.stopMotors();
			new DriveSemiCircle(driveTrain, SmartDashboard.getNumber("DB/Slider 0", 30) * 2, 90, true).run();
			gca.setAngle(75);
			gch.toggleAngle(false);
			gch.toggleClamp(true);
			driveTrain.turnTo(0);
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			gch.toggleClamp(true);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	gch.toggleClamp(true);
			
			currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();
			if(Math.abs(visionAngle)>8){
				driveTrain.turnTo(currentAngle + visionAngle);
				//gca.setAngle(Resources.getInstance().SCORING_POSITION);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				driveTrain.turning = false;			
			}
			
			gca.setAngle(Resources.getInstance().SCORING_POSITION);
			
		/*	try {
				Thread.sleep(700);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
			
			currentTime = System.currentTimeMillis();
			while (System.currentTimeMillis() - currentTime < 1300 && DriverStation.getInstance().isAutonomous()) {
				driveTrain.arcadeDrive(-0.4, 0);
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			driveTrain.stopMotors();

			//

			gch.toggleAngle(true);
			gch.toggleClamp(false);
			//
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentTime = System.currentTimeMillis();
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			
			while (System.currentTimeMillis() - currentTime < 450 && DriverStation.getInstance().isAutonomous()) {
				driveTrain.arcadeDrive(0.95, 0);
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//new DriveToPeg(8, -0.7).run();
			
			
			
		}

	};

	public static Runnable TwoGearLeft = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//initiate everything
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
			AHRS navx = (AHRS) Resources.getInstance().getSensor("navX");
			
			//new DriveToPeg(6.2, -0.65, 50, true).run();
			//AutonDriveForTime a = new AutonDriveForTime(((DriveTrain) Resources.getInstance().getSubSystem("DriveTrain")), 1000,-0.8);
			
			double startingAngle = navx.getFusedHeading();
			
			gca.setAngle(Resources.getInstance().SCORING_POSITION); // set collector to score
			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			//drive straight to score gear
			new AutonDriveStraight(800, -0.85).run();
			new AutonDriveStraight(450, -0.6, startingAngle).run(); // slow down so we dont crash that hard
			
			
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);//release gear
			gch.collectingPosition();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			

			
			

			new DriveSemiCircle(driveTrain, SmartDashboard.getNumber("DB/Slider 0", -30), 180, true).run(); // semicircle to the gear
			driveTrain.stopMotors();
			/*driveTrain.turnTo(180);
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			gch.collectingPosition();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();*/

			gch.collectingPosition();//put collector down to grab the gear
			new AutonDriveStraight(600, -0.9,180).run();//go get the gear boi
			
			double currentTime = System.currentTimeMillis();
			gch.collectingPosition();//just making sure
			new AutonDriveStraight(600, -0.45,180).run(); //900
			

			gch.toggleClamp(true);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gca.setAngle(45);

		
			/*try {
				Thread.sleep(450);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			//new AutonDriveForMeasurement(driveTrain, -1, 0.7).run();;
			driveTrain.stopMotors();
			new DriveSemiCircle(driveTrain, SmartDashboard.getNumber("DB/Slider 0", -30) * 1, 180, true).run();
			gca.setAngle(90);
			gch.toggleAngle(true);
			gch.toggleClamp(true);
			driveTrain.turnTo(0);
			try {
				Thread.sleep(350); //900
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			gch.toggleClamp(false);
			
			
			//new DriveToPeg(73, -0.7, 450, true).run();
			//driveTrain.stopMotors();
			double d = visionLoop.distanceFromTarget()+15; //10
			if(d <150){
				new DriveToPeg( d, -0.85, 450, true, true).run();
				//new DriveToPeg(6, -0.75, 450, false).run();	
			}
			
			
			
		}


	};
	
	
	public static Runnable BaseLine = new Runnable() {
		DriveTrain driveTrain;
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			double currentTime = System.currentTimeMillis();
			while(System.currentTimeMillis() - currentTime < 5000){
				driveTrain.arcadeDrive(-0.7, 0);
			}
			driveTrain.stopMotors();
		}
		
	}; 
	
	public AutonomousRoutine(List<Runnable> actionsByOrder) {
		this.actionsByOrder = actionsByOrder;
		stop = false;
		finalRoutine = this;
	}

	public static AutonomousRoutine getAuto() {
		return finalRoutine;
	}

	public void run() {

		for (int count = 0; count < actionsByOrder.size() && DriverStation.getInstance().isAutonomous(); count++) {
			actionsByOrder.get(count).run();
		}

	}

}
