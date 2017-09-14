package org.usfirst.frc.team1011.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.AutonDriveStraight;
import frc.team1011.ruben.AutonLine;
import frc.team1011.ruben.AutonStraightMeasurement;
import frc.team1011.ruben.DriveSemiCircle;
import frc.team1011.ruben.DriveTrain;
import frc.team1011.ruben.Resources;
import vison.VisionLoop;

public class ChezyAutos {

	public static Runnable TwoGearSideLeft = new Runnable() {
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
			// new AutonDriveStraight(840, -0.95, 0).run();//840
			new AutonStraightMeasurement(6.5, 0.95, 0, true).run();
			// new AutonDriveForMeasurement(driveTrain, 5.2, 0.75).run();

			driveTrain.stopMotors();

			//

			//
			driveTrain.turnTo(60);// 300
			gch.scoringPosition();

			try {
				Thread.sleep(300); // 400
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
				driveTrain.turnTo(60);
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
			SmartDashboard.putNumber("DB/Slider 3",
					(1.0 / 12.0) * ((double) SmartDashboard.getNumber("DB/Slider 0", 2) * (2 / Math.sqrt(3)))
							- (30 / 12));
			// new AutonDriveStraight(608, 0.85).run();// back out of peg
			SmartDashboard.putNumber("DB/Slider 2", 0.6);
			SmartDashboard.putNumber("DB/Slider 1", SmartDashboard.getNumber("DB/Slider 0", 0.5));
			//

			new AutonStraightMeasurement(
					(1.0 / 12.0) * ((double) SmartDashboard.getNumber("DB/Slider 0", 2) * (2 / Math.sqrt(3)))
							- (30 / 12),
					-0.85, 60, true).run();
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
			driveTrain.turnTo(240);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();
			new AutonStraightMeasurement(1.5, -0.95, 240, false).run();

			driveTrain.turnTo(0); // turn forwards again
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			driveTrain.turning = false;

			gch.toggleAngle(true);
			gca.setAngle(75);
			new AutonStraightMeasurement(5.9, 0.95, 0, true).run(); // 1321

			driveTrain.turnTo(60);
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
			new AutonDriveStraight(750, -0.65, currentAngle > 3 ? currentAngle - 0 : currentAngle + 0).run();// 3,
																												// 357

			//

			gch.collectingPosition();
			// gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			try {
				Thread.sleep(1500);// 1500
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

	public static Runnable TwoGearRightNew = new Runnable() {
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
			double distanceFirst = SmartDashboard.getNumber("DB/Slider 0", 70);

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
			new AutonDriveStraight(800, -0.85).run();
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
			gca.setAngle(90);

			/*
			 * try { Thread.sleep(450); } catch (InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 */
			// new AutonDriveForMeasurement(driveTrain, -1, 0.7).run();;
			driveTrain.stopMotors();
			// angleWanted is angle to back up to go back to peg
/*			double angleWanted = Math.toDegrees(Math.atan(48.0 / distanceFirst));
			// distance to travel to get back to peg
			double distanceToScoringDiagonal = Math.abs((1 / Math.cos(Math.toRadians(angleWanted))))
					* Math.abs(distanceFirst);
			if (Math.abs(distanceToScoringDiagonal) > 80) {
				double delD = Math.abs(distanceToScoringDiagonal) - 70;
				double factor = 20 * (delD / 30);
				distanceToScoringDiagonal = distanceToScoringDiagonal > 0 ? distanceToScoringDiagonal - factor
						: distanceToScoringDiagonal + factor;
			}
			SmartDashboard.putNumber("DB/Slider 3", distanceToScoringDiagonal);
			angleWanted += angleWanted > 0 ? 90 : -90;
			angleWanted = ((angleWanted % 360) + 360) % 360;
			driveTrain.turnTo(angleWanted);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			new AutonLine(-distanceToScoringDiagonal, 0.95, angleWanted).run();
			*/// new AutonDriveStraight(distanceFirst, -0.9, 290).run();
			boolean right = distanceFirst>0; 
			if(right){
				new DriveSemiCircle(driveTrain, 60, 90, false).run(); // semicircle		
			}else{
				new DriveSemiCircle(driveTrain, -60, 90, false).run(); // semicircle			
			}
			new AutonLine(-(Math.abs(distanceFirst)-(30)), 0.95,right?90:270).run();
			
			
			
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
				Thread.sleep(500); // 900
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//driveTrain.turning = false;
			double d = visionLoop.distanceFromTarget() + 15; // 10
			if (d < 150) {
				new DriveToPeg(d, -0.85, 450, true, true).run();
			}
			/*
			 * gch.toggleClamp(false); double currentAngle = ((AHRS)
			 * Resources.getInstance().getSensor("navX")).getFusedHeading();
			 * gca.setAngle(90); driveTrain.turnTo(visionLoop.getAngleToTarget()
			 * + currentAngle); try { Thread.sleep(750); } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } driveTrain.turning = false;
			 * driveTrain.stopMotors();
			 * 
			 * currentAngle = ((AHRS)
			 * Resources.getInstance().getSensor("navX")).getFusedHeading();
			 * gch.toggleClamp(false); new AutonDriveStraight(750, -0.65,
			 * currentAngle > 3 ? currentAngle - 3 : currentAngle + 357).run();
			 */
			//

			gch.collectingPosition();
			// gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// new AutonDriveStraight(400, 0.95).run();
			gca.setAngle(0);
			new DriveSemiCircle(driveTrain, 90, 180, true).run();
			gca.setAngle(90);
			driveTrain.turnTo(180);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AutonDriveStraight(2000, 0.9, 180).run();

		}

	};

	// THREE JEER
	public static Runnable ThreeJEER = new Runnable() {
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

			double distanceFirst = SmartDashboard.getNumber("DB/Slider 0", 70);
			double distanceSecond = SmartDashboard.getNumber("DB/Slider 1", -70);

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
			new AutonDriveStraight(800, -0.85).run();
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

			new DriveSemiCircle(driveTrain, distanceFirst, 180, true).run(); // semicircle
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
			// angleWanted is angle to back up to go back to peg
			double angleWanted = Math.toDegrees(Math.atan(42.0 / distanceFirst));
			// distance to travel to get back to peg
			double distanceToScoringDiagonal = Math.abs((1 / Math.cos(Math.toRadians(angleWanted))))
					* Math.abs(distanceFirst) - 25;

			angleWanted += angleWanted > 0 ? 90 : -90;
			angleWanted = ((angleWanted % 360) + 360) % 360;
			driveTrain.turnTo(angleWanted);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			new AutonStraightMeasurement(distanceToScoringDiagonal / 12, -0.95, angleWanted, true).run();
			// new AutonDriveStraight(distanceFirst, -0.9, 290).run();
			driveTrain.stopMotors();
			driveTrain.turnTo(0);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			gca.setAngle(90);
			gch.toggleAngle(true);
			gch.toggleClamp(true);

			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gch.toggleClamp(false);
			double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			gca.setAngle(90);
			double vision = visionLoop.getAngleToTarget() + currentAngle;
			SmartDashboard.putNumber("DB/Slider 3", vision);
			SmartDashboard.putNumber("DB/Slider 2", currentAngle);

			driveTrain.turnTo(vision);
			driveTrain.turning = true;
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
			new AutonDriveStraight(750, -0.65, vision > 3 ? vision - 3 : vision + 357).run();

			//

			gch.collectingPosition();
			// gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// new AutonDriveStraight(400, 0.95).run();

			// SECOND GEARRR
			// SECOND GEARRR
			// SECOND GEARRR
			// SECOND GEARRR
			// SECOND GEARRR
			// SECOND GEARRR

			gca.setAngle(45);
			new DriveSemiCircle(driveTrain, distanceSecond * 0.95, 180, true).run(); // semicircle
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
			gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
			gch.collectingPosition();// put collector down to grab the gear
			new AutonDriveStraight(600, -0.9, 180).run();// go get the gear boi

			currentTime = System.currentTimeMillis();
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
			// angleWanted is angle to back up to go back to peg
			angleWanted = Math.toDegrees(Math.atan(42.0 / distanceSecond));
			// distance to travel to get back to peg
			distanceToScoringDiagonal = Math.abs((1 / Math.cos(Math.toRadians(angleWanted)))) * Math.abs(distanceSecond)
					- 20;

			angleWanted += angleWanted > 0 ? 90 : -90;
			angleWanted = ((angleWanted % 360) + 360) % 360;
			driveTrain.turnTo(angleWanted);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();

			new AutonStraightMeasurement(distanceToScoringDiagonal / 12, -0.95, angleWanted, true).run();
			// new AutonDriveStraight(distanceFirst, -0.9, 290).run();
			driveTrain.stopMotors();
			driveTrain.turnTo(0);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;
			driveTrain.stopMotors();
			gca.setAngle(90);
			gch.toggleAngle(true);
			gch.toggleClamp(true);

			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gch.toggleClamp(false);
			gca.setAngle(90);
			currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();

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

			// new AutonDriveStraight(400, 0.95).run();

			gca.setAngle(0);
			new DriveSemiCircle(driveTrain, 90, 180, true).run();
			gca.setAngle(90);
			driveTrain.turnTo(180);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AutonDriveStraight(2000, 0.9, 180).run();

		}

	};

}
