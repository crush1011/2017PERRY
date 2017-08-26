package frc.team1011.ruben;

import edu.wpi.first.wpilibj.DriverStation;

public class AutonDriveForTime implements Runnable{
	DriveTrain driveTrain;
	double speed;
	long time;

	public AutonDriveForTime(DriveTrain driveTrain, long time, double speed) {
		this.driveTrain = driveTrain;
		this.time = time;
		this.speed = speed;
	}
	
	@Override
	public void run() {
		double initialTime = System.currentTimeMillis();
		while(System.currentTimeMillis()-initialTime <time && DriverStation.getInstance().isAutonomous()){
			driveTrain.arcadeDrive(speed, 0);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		driveTrain.stopMotors();
	}

}
