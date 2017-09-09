package frc.team1011.ruben;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonLine extends PIDSubsystem implements Runnable{
	
	double angle, topSpeed, distance;
	double rotateOutput;
	double loopCount, pastCount;
	
	final double acceleration = 1.7;
	
	DriveTrain driveTrain;
	AHRS navx;
	
	public AutonLine( double distance,double topSpeed,double angle){
		super(2.0,0,0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(0);
		
		this.angle=angle;
		this.topSpeed=topSpeed;
		this.distance=distance;
		
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		navx = (AHRS) Resources.getInstance().getSensor("navX");
		if(Math.abs(distance)<70){
			double delD = 70-Math.abs(distance);
			double factor = 25 * (delD/70);
			this.distance = distance>0? distance+factor:distance-factor;
		}
	}

	double delT = 50;
	public void run(){
		boolean stop = false;
		//300 is accel
		double timeToStop = Math.abs(topSpeed/acceleration);
		
		boolean deAccelerate = false;	
		double initialPosL = driveTrain.leftDistanceTravelled();
		double initialPosR = driveTrain.rightDistanceTravelled();
		double distanceTravelled =0;
		
		boolean backwards = distance<0;
		
		double currentVelocity = 0;
		double lastVelocity =0;
		while(!stop && DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled()){
			
			double distanceNeededToStop = (Math.abs(currentVelocity * 144)/2) * timeToStop;
			
			distanceTravelled=Math.abs(driveTrain.leftDistanceTravelled() - initialPosL + 
					driveTrain.rightDistanceTravelled() - initialPosR) / 2;
			distanceTravelled*=12;
			//remainingdistance
			if(Math.abs(distance) - distanceTravelled<=distanceNeededToStop){
				deAccelerate=true;
			}

			if(deAccelerate? !backwards:backwards){
				currentVelocity-= (delT/1000) * acceleration;
			}else{
				currentVelocity += (delT/1000) * acceleration;
			}
			currentVelocity = Math.max(Math.min(topSpeed, currentVelocity), -topSpeed);
			System.out.println("currentV:" + currentVelocity);
			System.out.println(deAccelerate);
			System.out.println(backwards);
			if(loopCount!=pastCount){
				if(rotateOutput>=0){
					rotateOutput+=0.02;
				}else{
					rotateOutput-=0.02;
				}
			}
			pastCount=loopCount;
			driveTrain.arcadeDrive(-currentVelocity, rotateOutput);
			if(backwards){
				if(currentVelocity>=0 && lastVelocity<0){
					stop=true;
				}
			}else{
				if(currentVelocity<=0 && lastVelocity>0){
					stop=true;
				}
			}
			lastVelocity = currentVelocity;
		}
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
