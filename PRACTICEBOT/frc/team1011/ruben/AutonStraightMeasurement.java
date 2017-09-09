package frc.team1011.ruben;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//MAKE PID TURNING SMOOTHER AFTER CHEZY CHAMPS

public class AutonStraightMeasurement extends PIDSubsystem implements Runnable {

	DriveTrain driveTrain;
	PowerDistributionPanel pdp;
	AHRS navx;
	//VisionLoop visionLoop;
	double rotateOutput = 0;
	double distance;
	double loopCount = 0;
	double speed;
	double angleWanted;
	boolean slow;
	
	public AutonStraightMeasurement(double distance, double speed, boolean slow) {
		super(2.0, 0, 0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(0);
		this.distance = distance;
		this.speed = speed;
		//System.out.println("DAWAY=" + time);
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		pdp = (PowerDistributionPanel) Resources.getInstance().getSensor("pdp");
		navx = (AHRS) Resources.getInstance().getSensor("navX");
		//visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
		loopCount = 0;
		angleWanted = 9999;
		rotateOutput=0;
		this.slow=slow;
	}
	
	public AutonStraightMeasurement(double distance, double speed, double angle, boolean slow) {
		super(1.0, 0, 0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(0);
		this.distance = distance;

		this.speed = speed;
		//System.out.println("DAWAY=" + time);
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		pdp = (PowerDistributionPanel) Resources.getInstance().getSensor("pdp");
		navx = (AHRS) Resources.getInstance().getSensor("navX");
		//visionLoop = (VisionLoop) Resources.getInstance().getSensor("vision");
		loopCount = 0;
		angleWanted = angle%360;
		rotateOutput=0;
		this.slow=slow;
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
		double initialDistanceLeft = driveTrain.leftDistanceTravelled();
		double initialDistanceRight = driveTrain.rightDistanceTravelled();
		double stoppingDistance = Math.abs(speed * 1.6);
		boolean slowed = false;
		if (speed >= 0) {
			while ((driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2 < distance && DriverStation.getInstance().isEnabled()) {
				this.setSetpoint(initialAngle);
				if(loopCount!=pastCount){
					if(rotateOutput>=0){
						rotateOutput+=0.02;
					}else{
						rotateOutput-=0.02;
					}
				}
				if(slow && !slowed && (driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2 > (distance - stoppingDistance)){
					speed = speed * 0.7;
					slowed=true;
				}
				pastCount=loopCount;
				driveTrain.arcadeDrive(-speed, rotateOutput);
				System.out.println("LeftSide:" + driveTrain.leftDistanceTravelled());
				System.out.println("RightSide:" + driveTrain.rightDistanceTravelled());
				SmartDashboard.putString("DB/String 3", ""+ rotateOutput);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			while (Math.abs((driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2) <Math.abs(distance) && DriverStation.getInstance().isEnabled()) {
				this.setSetpoint(initialAngle);
				if(loopCount!=pastCount){
					if(rotateOutput>=0){
						rotateOutput+=0.02;
					}else{
						rotateOutput-=0.02;
					}
				}
				if(slow && !slowed && (Math.abs(driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
						driveTrain.rightDistanceTravelled() - initialDistanceRight)) / 2 > (Math.abs(distance) - stoppingDistance)){
						speed*=0.7;
						slowed=true;
					}
				pastCount=loopCount;
				driveTrain.arcadeDrive(-speed, rotateOutput);
				SmartDashboard.putString("DB/String 3", "BACKWARDS"+ rotateOutput);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		driveTrain.stopMotors();
		
		

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
		rotateOutput = output / 140; //180? p140
		loopCount++;
		//SmartDashboard.putString("DB/String 0",""+rotateOutput);;
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
}
