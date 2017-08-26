package org.usfirst.frc.team1011.robot;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.SubSystem;
import static org.usfirst.frc.team1011.robot.Robot.*;

import com.ctre.CANTalon;

import static frc.team1011.ruben.Resources.*;

public class GearCollectorArm  extends PIDSubsystem implements SubSystem{

	
	
	public enum GearCollectorArmState{
		BEGINNING(90),
		PICKUP_SCORING(0);
		
		private int angle;
		
		GearCollectorArmState(int angle){
			this.angle=angle;
		}
		
		public int getAngle(){
			return angle;
		}
	}
	
	private SpeedController angleMotor;
	private Potentiometer p;
	private Encoder e;
	private boolean invertedMotor;
	private double angle;
	public  double POTSTART,POTTO90, k;
	public boolean PIDEnabled;
	public boolean PotEnabled;
	public double manualMotorOutput=0;
	private boolean beingUsed = false;
	public double displacement = 0;

	public Runnable goHome = () -> {
		if(!beingUsed){
			beingUsed = true;

			GearCollectorHead gch = (GearCollectorHead) resources.getSubSystem("GearCollectorHead");
			gch.toggleClamp(true);
			gch.toggleAngle(true);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("ERROR AFTER FIRST");
			}

			this.setSetpoint(90);
			angle=90;
			//wait till you up
			int count = 0;
			while(Math.abs(getAngle()-90)>20){
				try {
					Thread.sleep(50);

					System.out.println("WAITIMG:" + count++ +"**" +  Boolean.toString(Math.abs(getAngle()-90)<10)  + "::" + Math.abs(getAngle()-90));
				} catch (InterruptedException e) {
					e.printStackTrace();

					System.out.println("ERROR WHILE WAITING");
				}
			}
			//once you up, then
			try {
				Thread.sleep(500);

				System.out.println("IGOTTOTHREE");
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("ERROR AFTER THIRD");
			}
			gch.toggleClamp(false);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				SmartDashboard.putString("DB/String 2" , "error");
			}
			gch.toggleClamp(true);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				SmartDashboard.putString("DB/String 2" , "error");
			}
			gch.toggleAngle(false);
			beingUsed=false;
		}
		
	};
	
	public Runnable scoringCenter = () -> {
		if(!beingUsed){
			beingUsed = true;
			

			GearCollectorHead gch = (GearCollectorHead) resources.getSubSystem("GearCollectorHead");
			gch.toggleClamp(true);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.setSetpoint(-3);
			gch.toggleAngle(false);
			angle=2;
			//wait till you up
			
			//once you up, then
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gch.toggleClamp(false);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				SmartDashboard.putString("DB/String 2" , "error");
			}
			gch.toggleClamp(true);
			
			beingUsed=false;
		}
		
	};
	
	public GearCollectorArm(SpeedController angleMotor, boolean invertedMotor, Encoder e, double POTSTART, double POTTO90, double giveConstant){
		super("GearCollector", 3.0,0,0);
		
		this.angleMotor=angleMotor;
		this.POTSTART=POTSTART;
		this.POTTO90=POTTO90;
		this.k=giveConstant;
		this.invertedMotor=invertedMotor;
		this.e=e;
		this.setOutputRange(-180, 180);
		this.setAbsoluteTolerance(10);
		angle=90;
		PotEnabled=true;
		PIDEnabled=true;
		
	}
	
	public GearCollectorArm(SpeedController angleMotor, boolean invertedMotor){
		super("GearCollector", 0.1,1,0.1);
		this.angleMotor=angleMotor;
		this.invertedMotor=invertedMotor;
		PotEnabled=false;
		PIDEnabled=false;
	}
	
	public GearCollectorArm(){
		super("GearCollector", 0.1,1,0.1);
		k=0;
		POTSTART=0;
		POTTO90=0;
	}
	
	public void setMode(String mode){
		switch(mode){
			case "PID":
				PIDEnabled = true;
				PotEnabled =true;
			//	((CANTalon) angleMotor).enableBrakeMode(false);
			
				break;
			case "Pot":
				PIDEnabled = false;
				PotEnabled = true;
				((CANTalon) angleMotor).enableBrakeMode(false);
				
				break;
			case "Manual":
				PIDEnabled = false;
				PotEnabled = false;
				((CANTalon) angleMotor).enableBrakeMode(true);
				
				break;
				
		}
		
	}
	
	//REMEMBER TO CHANGE THE POT TO 90, AND POTENTIALLY THE POT START TO A DEFAULT VALUE
	
	public static void instantiate() {
		
		//Potentiometer p = (Potentiometer) resources.getSensor("GearCollectorPotentiometer");
		Encoder e = (Encoder) resources.getSensor("GearArmEncoder"); 
		GearCollectorArm gca;
		if(e!=null){
			gca = new GearCollectorArm(resources.getMotorController("GearCollectorMotor"), true, // true    pfalse
					e, e.getDistance(),75*(66/18), 0.03);
				
		}else{
			gca = new GearCollectorArm(resources.getMotorController("GearCollectorMotor"), true); // true   pfalse
		}
		gca.setAngle(90);
		gca.enable();
		resources.addSubSystem("GearCollectorArm", gca);
		GearCollectorHead.instantiate();

	}
	
	public void setAngle(int angle){
		if(!beingUsed){
			this.setSetpoint(angle);
			this.angle=angle;		
		}
	}
	
	public double getAngleSetpoint(){
		return angle;
	}
	
	
	
	
	
	public void setState( GearCollectorArmState gcas){
		this.setSetpoint(gcas.getAngle());
		this.angle = gcas.getAngle();
	}
	
	public double getAngle(){
		double current = 0;
		double potValue = e.getDistance();
		current = potValue/POTTO90;
		current = current*90;
		current = 90+current + displacement *5;
		SmartDashboard.putString("DB/String 2",""+ current);
		return current;
	}
	
	public double getAngleRadians(){
		return (getAngle() * Math.PI)/180;
	}
	
	public boolean used(){
		return beingUsed;
	}
	

	private double calculateGive(double angle){
		return Math.cos(angle) * k;
	}


	@Override
	protected double returnPIDInput() {
		double angle = getAngle();

		if(e!=null){

			
		
			return angle;
				
		}else{
			return 0;
		}
	}

	//CALCULATE OUTPUT
	
	@Override
	protected void usePIDOutput(double output) {
		double motorOutput = output/70;
		double give = calculateGive(getAngleRadians());
		if( this.getAngle()>80 && motorOutput>0.2){
			motorOutput = 0.1;
		}
		if(this.getAngle()<-20 && motorOutput<-0.3){
			motorOutput = -0.2;
		}
		SmartDashboard.putString("DB/String 7", "OUT:"+ motorOutput+ give);
		
		if(PotEnabled && PIDEnabled){
			
			angleMotor.set(invertedMotor? -limit(give + motorOutput, -1,1) :limit(give + motorOutput, -1,1) );

			SmartDashboard.putString("DB/String 8", "PIDON");
		}else if(PotEnabled){
			
			angleMotor.set(invertedMotor? -limit(give, -1,1) :limit(give, -1,1));
		}else{
			angleMotor.set(invertedMotor? -limit(manualMotorOutput + 0.06,-1,1):limit(manualMotorOutput + 0.06,-1,1));
		}
		
	}


	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

	
}
