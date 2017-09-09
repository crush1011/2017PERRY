package frc.team1011.ruben;

import org.usfirst.frc.team1011.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class TeleDriveForMeasurement implements Runnable{
		
	DriveTrain driveTrain;
	double inches, speed, initialDistanceLeft, initialDistanceRight;
	Joystick driverJoystick;

	public TeleDriveForMeasurement(double inches, double speed){
		this.driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		driverJoystick = (Joystick) Resources.getInstance().getSensor("DriverJoystick");
		this.inches=inches;
		this.speed=speed;
	}

	@Override
	public void run() {
		initialDistanceLeft = driveTrain.leftDistanceTravelled();
		initialDistanceRight = driveTrain.rightDistanceTravelled();
		if (inches >= 0) {
			while ((driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2 < inches && DriverStation.getInstance().isEnabled()
					&& Math.abs(driverJoystick.getRawAxis(1)) < 0.4 && Math.abs(driverJoystick.getRawAxis(4)) < 0.4 ) {
				Robot.driveEnabled = false;
				driveTrain.arcadeDrive(-speed, 0);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		} else {
			while ((driveTrain.leftDistanceTravelled() - initialDistanceLeft + 
					driveTrain.rightDistanceTravelled() - initialDistanceRight) / 2 > inches && DriverStation.getInstance().isEnabled()
					&& Math.abs(driverJoystick.getRawAxis(1)) < 0.4 && Math.abs(driverJoystick.getRawAxis(4)) < 0.4) {
				Robot.driveEnabled = false;
				driveTrain.arcadeDrive(speed, 0);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		driveTrain.stopMotors();
	}
	
}
