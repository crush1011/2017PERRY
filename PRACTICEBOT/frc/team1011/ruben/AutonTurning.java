package frc.team1011.ruben;

public class AutonTurning implements Runnable{

	DriveTrain driveTrain;
	double degree;
	long time;
	
	public AutonTurning(double degree, long time){
		driveTrain = (DriveTrain) Resources.getInstance().getSubSystem("DriveTrain");
		this.degree=degree;
		this.time = time;
	}

	@Override
	public void run() {
		driveTrain.turnTo(degree %360);
		try{
			Thread.sleep(time);
		}catch(Exception e){
			
		}
		driveTrain.turning=false;
		driveTrain.stopMotors();
		
	}
}
