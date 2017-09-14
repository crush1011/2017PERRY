package frc.team1011.ruben;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSemiCircle implements Runnable {

	private DriveTrain dt;
	private double diameter, speed, angle;
	private AHRS navx;
	private boolean stop;

	public DriveSemiCircle(DriveTrain dt, double diameter, double angle, boolean stop){
		this.dt = dt;
		this.navx = (AHRS) Resources.getInstance().getSensor("navX");
		this.diameter=diameter;
		this.angle = angle;
		diameter = diameter*0.85;
		this.stop=stop;
		if(diameter >=0){
			this.speed = - ((1.7174) + (-0.2326* Math.log(Math.abs(diameter))));
		}else{
			this.speed = ((1.7174) + (-0.2326* Math.log(Math.abs(diameter))));
		}

	}
	

	private double initialHeading;
	private double startTime;
	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		initialHeading = navx.getFusedHeading();
		
		while(Math.abs(Math.abs(navx.getFusedHeading() - initialHeading) - angle) > 40 && DriverStation.getInstance().isAutonomous()){
			dt.arcadeDrive(1.0 , speed);
			try{
				Thread.sleep(25);
			}catch(Exception e){
				
			}
			
		}
		while(Math.abs(Math.abs(navx.getFusedHeading() - initialHeading) - angle) > 20 && DriverStation.getInstance().isAutonomous()){
			dt.arcadeDrive(0.7 ,speed * 0.7);
			try{
				Thread.sleep(25);
			}catch(Exception e){
				
			}
			
		}
		double initialTime = System.currentTimeMillis();
		while(Math.abs(Math.abs(navx.getFusedHeading() - initialHeading) - angle) > 0 && System.currentTimeMillis() - initialTime < 200 && DriverStation.getInstance().isAutonomous()){
			if(stop){
				dt.arcadeDrive(-0.5 , 0);
					
			}else{
				dt.arcadeDrive(0.7,speed*0.7);
			}
			
			try{
				Thread.sleep(25);
			}catch(Exception e){
				
			}
			
		}
		/*while(Math.abs(Math.abs(navx.getFusedHeading() - initialHeading) - angle) > 10 && DriverStation.getInstance().isAutonomous()){
			dt.arcadeDrive(0.5 , SmartDashboard.getNumber("DB/Slider 0", -30) /2 );
			try{
				Thread.sleep(25);
			}catch(Exception e){
				
			}
			
		}*/
		if(stop){
			dt.stopMotors();
				
		}
	
	}
	
	
}
