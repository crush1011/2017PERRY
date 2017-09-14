package org.usfirst.frc.team1011.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.AutonDriveForTime;
import frc.team1011.ruben.AutonDriveStraight;
import frc.team1011.ruben.DriveTrain;
import frc.team1011.ruben.Resources;
import vison.VisionLoop;

public class DriveToPeg extends PIDSubsystem implements Runnable {

	DriveTrain driveTrain;
	PowerDistributionPanel pdp;
	AHRS navx;
	VisionLoop visionLoop;
	double rotateOutput = 0;
	GearCollectorArm gca;
	double distanceAway;
	GearCollectorHead gch;
	double speed, backOutTime;
	boolean clamped;
	double loopCount = 0;
	boolean high = false;
	
	public DriveToPeg(double distanceAway, double speed, double backOutTime, boolean clamped, boolean high) {
		super(2.0, 0, 0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(0);
		this.distanceAway = distanceAway;
		this.backOutTime = backOutTime;
		this.speed = speed;
		this.clamped = clamped;
		System.out.println("DAWAY=" + distanceAway);
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		pdp = (PowerDistributionPanel) Resources.getInstance().getSensor("pdp");
		navx = (AHRS) Resources.getInstance().getSensor("navX");
		visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
		gca = (GearCollectorArm) Resources.getInstance().getSubSystem("GearCollectorArm");
		gch = (GearCollectorHead) Resources.getInstance().getSubSystem("GearCollectorHead");
		loopCount = 0;
		this.high=high;
	}

	public synchronized void run() {
		/*double P = SmartDashboard.getNumber("DB/Slider 0",1);
		double I = SmartDashboard.getNumber("DB/Slider 1",1);
		double D = SmartDashboard.getNumber("DB/Slider 2",1);
		getPIDController().setPID(P, I, D);*/
		getPIDController().setPID(4.5, 0, 3.5);
		
		double currentTime = System.currentTimeMillis();
		this.getPIDController().reset();
		this.enable();
		this.setSetpoint(navx.getFusedHeading());
		double pastVisionAngle = 0;
		gca.setAngle(90);
		//gch.toggleClamp(true);
		if(high){
			gch.toggleAngle(true);	
		}else{
			gch.toggleAngle(false);
			
		}
		
		double initialLeft = driveTrain.leftDistanceTravelled();
		double initialRight = driveTrain.rightDistanceTravelled();
		double currentDistanceTravelled = 0;
		double pastOneTime = System.currentTimeMillis();

		double pastCount = -1;
		double initialTime = System.currentTimeMillis();
		
		double distance = distanceAway-34;
		double voltage = speed;
		double timeWanted =1000 * distance;
		timeWanted/= ((-220*voltage) - 89.67);
		//distanceAway - currentDistanceTravelled > 3 &&  < add to while loop if encoders
		/*while (System.currentTimeMillis() - initialTime < timeWanted && DriverStation.getInstance().isAutonomous()) {
			
			
			double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
			double visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();
			if (visionAngle != pastVisionAngle && System.currentTimeMillis() - pastOneTime > 150 && System.currentTimeMillis() - currentTime > 200) {
				this.setSetpoint(currentAngle + visionAngle);
				SmartDashboard.putString("DB/String 0", "" + this.getSetpoint());
				pastOneTime = System.currentTimeMillis();
			}
			
			pastVisionAngle = visionAngle;

			
			
			driveTrain.arcadeDrive(speed, rotateOutput);
			SmartDashboard.putString("DB/String 1", "" + rotateOutput);
			
			if(loopCount!=pastCount){
				if(rotateOutput>=0){
					rotateOutput+=0.03;
				}else{
					rotateOutput-=0.03;
				}
			}
			pastCount=loopCount;
			
		}*/
		double currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
		double visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();

		SmartDashboard.putString("DB/String 4", "" + visionAngle);
		driveTrain.turnTo(currentAngle + visionAngle);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driveTrain.turning = false;	
		this.setSetpoint(currentAngle + visionAngle);
		SmartDashboard.putString("DB/String 0", "" + this.getSetpoint());
		new AutonDriveStraight(timeWanted,speed, currentAngle+visionAngle).run();

		// near peg
		
		SmartDashboard.putString("DB/String 1", "DONE");
		driveTrain.stopMotors();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		this.getPIDController().disable();
		
		
		// currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
		// visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();
		
	//	SmartDashboard.putString("DB/String 4", "" + visionAngle);
		
		//driveTrain.turnTo(currentAngle + visionAngle );
		/*try {
			Thread.sleep(650);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driveTrain.turning = false;		
		SmartDashboard.putString("DB/String 3", ""+(currentAngle-((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading()));
*/
		 currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
		 visionAngle = ((VisionLoop) Resources.getInstance().getSensor("vision")).getAngleToTarget();
		
		SmartDashboard.putString("DB/String 1", "" + visionAngle);
		
		driveTrain.turnTo(currentAngle + visionAngle );
		if(high){
			gca.setAngle(90);
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			gca.setAngle(Resources.getInstance().SCORING_POSITION);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		driveTrain.turning = false;		
		SmartDashboard.putString("DB/String 0", ""+(currentAngle-((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading()));
		
		/*if(Math.abs(visionAngle)>8){
			driveTrain.turnTo(currentAngle + visionAngle);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driveTrain.turning = false;			
		}*/

		
		//gca.setAngle(-3);
		
	//	old scorng position
		//gca.setAngle(2);
		gch.toggleClamp(true);

		if(!clamped){
			gch.toggleClamp(false);
		}
		
		currentAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();
		new AutonDriveStraight(1000,-0.5, currentAngle>3? currentAngle-3: currentAngle+357).run();
		

		//

		gch.collectingPosition();
		//
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentTime = System.currentTimeMillis();
		gca.setAngle(Resources.getInstance().COLLECTING_POSITION);
		while (System.currentTimeMillis() - currentTime < backOutTime && DriverStation.getInstance().isAutonomous()) {
			driveTrain.arcadeDrive(0.9, 0);
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*while (System.currentTimeMillis() - currentTime < 1500 && DriverStation.getInstance().isAutonomous()) {
			driveTrain.arcadeDrivePID(0.2, 0);
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
		
		driveTrain.stopMotors();

	}

	@Override
	protected double returnPIDInput() {
		return navx.getFusedHeading();
	}

	@Override
	protected void usePIDOutput(double output) {
		rotateOutput = output / 140;
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
}
