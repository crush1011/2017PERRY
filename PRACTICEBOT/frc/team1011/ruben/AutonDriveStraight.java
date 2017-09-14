package frc.team1011.ruben;

import org.usfirst.frc.team1011.robot.GearCollectorHead;
import org.usfirst.frc.team1011.robot.GearCollectorArm;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.DriveTrain;
import frc.team1011.ruben.Resources;
import vison.VisionLoop;

public class AutonDriveStraight extends PIDSubsystem implements Runnable {

	DriveTrain driveTrain;
	PowerDistributionPanel pdp;
	AHRS navx;
	//VisionLoop visionLoop;
	double rotateOutput = 0;
	double time;
	double loopCount = 0;
	double speed;
	double angleWanted;
	
	public AutonDriveStraight(double time, double speed) {
		super(2.0, 0, 0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(0);
		this.time = time;
		this.speed = speed;
		//System.out.println("DAWAY=" + time);
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		pdp = (PowerDistributionPanel) Resources.getInstance().getSensor("pdp");
		navx = (AHRS) Resources.getInstance().getSensor("navX");
		//visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
		loopCount = 0;
		angleWanted = 9999;
	}
	
	public AutonDriveStraight(double time, double speed, double angle) {
		super(1.0, 0, 0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(0);
		this.time = time;

		this.speed = speed;
		//System.out.println("DAWAY=" + time);
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		pdp = (PowerDistributionPanel) Resources.getInstance().getSensor("pdp");
		navx = (AHRS) Resources.getInstance().getSensor("navX");
		//visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
		loopCount = 0;
		angleWanted = angle%360;
	}

	public synchronized void run() {
		/*double P = SmartDashboard.getNumber("DB/Slider 0",1);
		double I = SmartDashboard.getNumber("DB/Slider 1",1);
		double D = SmartDashboard.getNumber("DB/Slider 2",1);
		getPIDController().setPID(P, I, D);*/
		getPIDController().setPID(4.5, 0, 2.5);
		
		double currentTime = System.currentTimeMillis();
		this.getPIDController().reset();
		this.enable();
		this.setSetpoint(navx.getFusedHeading());

		double initialAngle;
		if(angleWanted==9999){
			 initialAngle = ((AHRS) Resources.getInstance().getSensor("navX")).getFusedHeading();	
		}else{
			initialAngle= angleWanted;
		}
		
		double initialTime = System.currentTimeMillis();
		
		double pastCount=-1;
		while (System.currentTimeMillis() - initialTime < time && DriverStation.getInstance().isAutonomous()) {
			this.setSetpoint(initialAngle);
			
			if(loopCount!=pastCount){
				if(rotateOutput>=0){
					rotateOutput+=0.03;
				}else{
					rotateOutput-=0.03;
				}
			}
			pastCount=loopCount;
			driveTrain.arcadeDrive(speed, rotateOutput);
			//SmartDashboard.putString("DB/String 1", "" + rotateOutput);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SmartDashboard.putString("DB/String 1", "DONE");
		driveTrain.stopMotors();

		this.getPIDController().disable();
		
		driveTrain.stopMotors();

	}

	@Override
	protected double returnPIDInput() {
		SmartDashboard.putString("DB/String 1", "" +navx.getFusedHeading());
		return navx.getFusedHeading();
	}

	@Override
	protected void usePIDOutput(double output) {
		rotateOutput = output / 180; //180? p140
		loopCount++;
		SmartDashboard.putString("DB/String 0",""+rotateOutput);;
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
}
