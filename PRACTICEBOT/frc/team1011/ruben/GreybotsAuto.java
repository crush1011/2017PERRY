package frc.team1011.ruben;

import com.kauailabs.navx.frc.AHRS;

public class GreybotsAuto {

	public static Runnable GreybotsAuto = new Runnable(){
		DriveTrain driveTrain;
		AHRS navX = (AHRS) Resources.getInstance().getSensor("navX");
		
		@Override
		public void run() {
			driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
			double initialTime = System.currentTimeMillis();
			while(System.currentTimeMillis() - initialTime < 356 ){
				driveTrain.arcadeDrive(-0.9, 0);
			}
			driveTrain.stopMotors();
			initialTime = System.currentTimeMillis();
			while(System.currentTimeMillis() - initialTime < 780 ){
				driveTrain.arcadeDrive(-0.9, 0.55);
			}
			driveTrain.stopMotors();
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			initialTime = System.currentTimeMillis();
			while(System.currentTimeMillis() - initialTime < 600 ){
				driveTrain.arcadeDrive(0.7, 0.2);
			}
			driveTrain.stopMotors();
			driveTrain.turnTo(160);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driveTrain.turning=false;
			driveTrain.stopMotors();
			
			
		}
		
	};
	
}
