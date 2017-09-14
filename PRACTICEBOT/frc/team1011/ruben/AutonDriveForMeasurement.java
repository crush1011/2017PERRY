package frc.team1011.ruben;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonDriveForMeasurement implements Runnable {
	DriveTrain driveTrain;
	double inches, speed, initialDistanceLeft, initialDistanceRight;

	public AutonDriveForMeasurement(DriveTrain driveTrain, double inches, double speed) {
		this.driveTrain = driveTrain;
		this.inches = inches;
		this.speed = speed;
	}

	@Override
	public void run() {
		initialDistanceLeft = driveTrain.leftDistanceTravelled();
		initialDistanceRight = driveTrain.rightDistanceTravelled();
		if (inches >= 0) {
			while ((driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2 < inches && DriverStation.getInstance().isEnabled()) {
				driveTrain.arcadeDrive(-speed, 0);
				System.out.println("LeftSide:" + driveTrain.leftDistanceTravelled());
				System.out.println("RightSide:" + driveTrain.rightDistanceTravelled());
				SmartDashboard.putString("DB/String 4", ""+ driveTrain.leftDistanceTravelled());
			}
		} else {
			while ((driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2 > inches && DriverStation.getInstance().isEnabled()) {
				driveTrain.arcadeDrive(speed, 0);
			}
		}
		driveTrain.stopMotors();
	}
}
