package frc.team1011.ruben;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDDriveSide extends PIDSubsystem{
	
	private Encoder encoder;
	private Double tickToFT;
	private Double deltaMO = new Double(0), motorOutput= new Double(0), mock = new Double(0);
	private double encoderValue, distanceTraveled = 0, currentTime = 0;
	private double pastOneDistance = 0,pastTwoDistance = 0, pastOneTime = 0, pastTwoTime = 0, deltaT, deltaD;
	private boolean negative;
	
	public PIDDriveSide(Encoder encoder, double tickToFT, boolean negative){
		super(1.5,0,0);
		this.tickToFT=tickToFT;
		this.encoder=encoder;
		this.setSetpoint(0);
		this.getPIDController().setOutputRange(-10, 10);
	//	this.enable();
		this.negative = negative;
		this.disable();
	}
	
	

	
	public void setVelocity(double velocity){
		setSetpoint(velocity);

	}
	
	public Double getMotorOutput(){
		return motorOutput;
	}
	
	private void updateEncoderValues(){
		/*double P = SmartDashboard.getNumber("DB/Slider 0",1);
		double I = SmartDashboard.getNumber("DB/Slider 1",1);
		double D = SmartDashboard.getNumber("DB/Slider 2",1);
		getPIDController().setPID(P, I, D);*/

		/*if(SmartDashboard.getBoolean("DB/Button 1", false)){
			this.getPIDController().reset();
			motorOutput=new Double(0);
			mock=new Double(0);
			this.enable();
		}*/
		
		pastTwoDistance= pastOneDistance;
		pastOneDistance = distanceTraveled;
		pastTwoTime = pastOneTime;
		pastOneTime= currentTime;
		currentTime= System.currentTimeMillis();
		encoderValue=encoder.getDistance();
		
		distanceTraveled = encoderValue/tickToFT;
	}
	
	@Override
	protected double returnPIDInput() {
		updateEncoderValues();
		Double pastVelocity = (pastTwoDistance - pastOneDistance);
		pastVelocity = pastVelocity/(pastTwoTime-pastOneTime);
		Double velocity = (pastOneDistance - distanceTraveled);
		velocity = velocity/(pastOneTime-currentTime);
		Double velocityAverages = (pastVelocity + velocity)/2;
		if(negative){
			velocity= -velocity;
			velocityAverages=-velocityAverages;
		}
		if(this.encoder == Resources.getInstance().getSensor("LeftEncoder")  && DriverStation.getInstance().isEnabled()){
			System.out.println("LEFT VE:"+1000* velocityAverages);	
			System.out.println("Left SETPOINT:" + this.getSetpoint());
		}
		if(this.encoder == Resources.getInstance().getSensor("RightEncoder") && DriverStation.getInstance().isEnabled()){
			System.out.println("RIGHT VE:"+1000* velocityAverages);	
			System.out.println("RIGHT SETPOINT:" + this.getSetpoint());
		}
		return 1000* velocityAverages;
	}
	
	
	@Override
	protected void usePIDOutput(double output) {
		if(DriverStation.getInstance().isEnabled()){
			deltaMO = output/10;

			double triangleMO= deltaMO;
			
				mock+=triangleMO;	
				//System.out.println("DELMO:" + triangleMO);
			/*}else{
				mock-=triangleMO;
				System.out.println("DELMO" + (-triangleMO));
			}*/
			mock = Resources.limit(mock, -1, 1);

			if(mock<0.05 && mock>-0.05){
				mock = 0.0;
			}
			motorOutput=mock;
			if(this.encoder == Resources.getInstance().getSensor("RightEncoder")){
				System.out.println("MOTOROUTPUTRIGHT:" +motorOutput);
			}else{
				System.out.println("MOTOROUTPUTLEFT:" +motorOutput);
			}
		}

	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
}