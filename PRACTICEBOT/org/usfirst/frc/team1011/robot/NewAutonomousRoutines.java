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
import frc.team1011.ruben.AutonStraightMeasurement;
import frc.team1011.ruben.DriveSemiCircle;
import frc.team1011.ruben.DriveTrain;
import frc.team1011.ruben.Resources;
import vison.VisionLoop;

public class NewAutonomousRoutines {

	
	public static Runnable TwoGearSideRight = new Runnable() {
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

			SmartDashboard.putString("DB/String 4", "even better");
			gch.toggleClamp(true);
			// gca.setAngle(Resources.getInstance().SCORING_POSITION );
		//	new AutonDriveStraight(840, -0.95, 0).run();//840
			new AutonStraightMeasurement(6.5,0.95,0, true).run();
			// new AutonDriveForMeasurement(driveTrain, 5.2, 0.75).run();

			driveTrain.stopMotors();

			//

			//
			driveTrain.turnTo(300);// 300
			gch.scoringPosition();
			
			try {
				Thread.sleep(300); //400
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();
				
			try {
				Thread.sleep(400);	
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			gca.setAngle(90);
			
			if (Math.abs(visionLoop.getAngleToTarget()) < 60) {
				driveTrain.turnTo(visionLoop.getAngleToTarget() + currentAngle);
				gca.setAngle(Resources.getInstance().SCORING_POSITION);
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				driveTrain.turning = false;
				driveTrain.stopMotors();
					
			} else {
				driveTrain.turnTo(300);
				gca.setAngle(Resources.getInstance().SCORING_POSITION);
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				driveTrain.turning = false;
				driveTrain.stopMotors();
			}

			new AutonDriveStraight(600, -0.65).run(); // go forwards to score
														// the gear on right
														// side peg
			gch.collectingPosition(); // put collector down to release gear
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
			SmartDashboard.putNumber("DB/Slider 3",(1.0/12.0)*((double) SmartDashboard.getNumber("DB/Slider 0", 2) * (2/Math.sqrt(3))) - (30/12)	 );
	//		new AutonDriveStraight(608, 0.85).run();// back out of peg
			SmartDashboard.putNumber("DB/Slider 2",0.6 );
			SmartDashboard.putNumber("DB/Slider 1",  SmartDashboard.getNumber("DB/Slider 0", 0.5));
			//		
			
			new AutonStraightMeasurement((1.0/12.0)*((double) SmartDashboard.getNumber("DB/Slider 0", 2) * (2/Math.sqrt(3))) - (32/12), -0.85, 300, true).run();
			driveTrain.turnTo(180);// turn towards the wall
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
				
			new AutonDriveStraight(550, -0.95, 180).run(); // first segment of
															// motion towards
															// wall

			/*
			 * driveTrain.turnTo(120); //turn sideways to align better with gear
			 * try { Thread.sleep(500); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 * driveTrain.turning= false;
			 */

			// new AutonDriveStraight(320,-0.95,110).run();
			// second segment

			/*
			 * driveTrain.turnTo(180); //turn towards wall for gear once again
			 * try { Thread.sleep(400); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 * driveTrain.turning= false;
			 */

			new AutonDriveStraight(650, -0.65, 180).run();// get the gear dammit
			gch.scoringPosition();// grasp the gear
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turnTo(120);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();
			new AutonStraightMeasurement(1.5, -0.95, 120, false).run();
			
			  driveTrain.turnTo(0); //turn forwards again 
			  try {
			  Thread.sleep(300); 
			  } catch (InterruptedException e) { e.printStackTrace(); }
			  driveTrain.turning= false;
			 
			gch.toggleAngle(true);
			gca.setAngle(75);
			new AutonStraightMeasurement(5.9, 0.95, 0, true).run(); //1321

			driveTrain.turnTo(300);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			// score a gear on the right side
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * double d = visionLoop.distanceFromTarget()+5;
			 * 
			 * if(d <150){ new DriveToPeg( d, -0.85, 450, false, true).run();
			 * //new DriveToPeg(6, -0.75, 450, false).run(); }
			 */
			currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			gca.setAngle(90);
			driveTrain.turnTo(visionLoop.getAngleToTarget() + currentAngle);
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			gch.toggleClamp(false);
			new AutonDriveStraight(750, -0.65, currentAngle > 3 ? currentAngle - 0 : currentAngle + 0).run();//3, 357

			//

			gch.collectingPosition();
			// gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			try {
				Thread.sleep(1500);//1500
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new AutonDriveStraight(400, 0.95).run();

			driveTrain.turnTo(0);
			gca.setAngle(90);
			gch.toggleClamp(true);
			gch.toggleAngle(false);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
		}

	};

	public static Runnable TwoGearLeftNew = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// initiate everything
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
			AHRS navx = (AHRS) Resources.getInstance().getSensor("navX");

			// new DriveToPeg(6.2, -0.65, 50, true).run();
			// AutonDriveForTime a = new AutonDriveForTime(((DriveTrain)
			// Resources.getInstance().getSubSystem("DriveTrain")), 1000,-0.8);

			double startingAngle = navx.getFusedHeading();

			gca.setAngle(Resources.getInstance().SCORING_POSITION); // set
																	// collector
																	// to score
			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// drive straight to score gear
			new AutonDriveStraight(750, -0.85).run();
			new AutonDriveStraight(450, -0.6, startingAngle).run();
			
			// slow down
																	// so we
																	// dont
																	// crash
																	// that hard

			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);// release
																		// gear
			gch.collectingPosition();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			new DriveSemiCircle(driveTrain, SmartDashboard.getNumber("DB/Slider 0", -30), 180, true).run(); // semicircle
																										// to
																										// the
																										// gear
			driveTrain.stopMotors();
			/*
			 * driveTrain.turnTo(180);
			 * gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			 * gch.collectingPosition();
			 * 
			 * try { Thread.sleep(200); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 * driveTrain.turning = false; driveTrain.stopMotors();
			 */

			gch.collectingPosition();// put collector down to grab the gear
			new AutonDriveStraight(600, -0.9, 180).run();// go get the gear boi

			double currentTime = System.currentTimeMillis();
			gch.collectingPosition();// just making sure
			new AutonDriveStraight(600, -0.45, 180).run(); // 900

			gch.toggleClamp(true); // picking up gear
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gca.setAngle(45);

			/*
			 * try { Thread.sleep(450); } catch (InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 */
			// new AutonDriveForMeasurement(driveTrain, -1, 0.7).run();;
			driveTrain.stopMotors();
			driveTrain.turnTo(250);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();
			
			double distanceFirst = Math.abs( SmartDashboard.getNumber("DB/Slider 0", 10));
			distanceFirst =Math.abs( (1/Math.cos(Math.toRadians(20)))) * distanceFirst - 35;
			
			new AutonStraightMeasurement(distanceFirst/12, -0.95, 250, true).run();
			//new AutonDriveStraight(distanceFirst, -0.9, 290).run();
			driveTrain.stopMotors();
			driveTrain.turnTo(0);
			try {
				Thread.sleep(350);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			gca.setAngle(90);
			gch.toggleAngle(true);
			gch.toggleClamp(true);
			driveTrain.turnTo(0);
			try {
				Thread.sleep(350); // 900
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gch.toggleClamp(false);
			double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			gca.setAngle(90);
			driveTrain.turnTo(visionLoop.getAngleToTarget() + currentAngle);
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			gch.toggleClamp(false);
			new AutonDriveStraight(750, -0.65, currentAngle > 3 ? currentAngle - 3 : currentAngle + 357).run();

			//

			gch.collectingPosition();
			// gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//	new AutonDriveStraight(400, 0.95).run();
			gca.setAngle(0);
			new DriveSemiCircle(driveTrain,90,180, true).run();
			gca.setAngle(90);
			driveTrain.turnTo(180);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AutonDriveStraight(2000,0.9,180).run();
			
			
			
		}

	};
	
	public static Runnable superCool = new Runnable() {
		DriveTrain driveTrain;
		GearCollectorArm gca;
		GearCollectorHead gch;
		VisionLoop visionLoop;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// initiate everything
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
			gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
			visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
			AHRS navx = (AHRS) Resources.getInstance().getSensor("navX");

			new AutonDriveStraight(840, -0.95, 0).run();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new AutonDriveStraight(400,0.9,0).run();
			driveTrain.turnTo(270);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			
			new AutonDriveStraight(200,-0.6,270).run();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new AutonDriveStraight(200,0.6,270).run();
			driveTrain.turnTo(0);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AutonDriveStraight(400,-0.9,0).run();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			new AutonDriveStraight(400,0.9,0).run();
			driveTrain.turnTo(90);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			
			new AutonDriveStraight(200,-0.6,270).run();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new AutonDriveStraight(200,0.6,270).run();
			driveTrain.turnTo(0);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AutonDriveStraight(400,-0.9,0).run();

		}
		
	};
}
