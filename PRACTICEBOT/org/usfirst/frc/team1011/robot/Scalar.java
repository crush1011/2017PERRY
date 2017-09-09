package org.usfirst.frc.team1011.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team1011.ruben.Resources;
import frc.team1011.ruben.SubSystem;

public class Scalar implements SubSystem {
	
	SpeedController winch1,winch2;
	boolean scaling=false;
	boolean fingersBeingUsed = false;

	
	public Runnable endfingers = new Runnable(){

		Servo leftServo = (Servo) Resources.getInstance().getSensor("LeftFingerServo");
		Servo rightServo = (Servo) Resources.getInstance().getSensor("RightFingerServo");
		@Override
		public void run() {
			if(!fingersBeingUsed){
				fingersBeingUsed = true;

				for(int i = 0;i<=20;i++){
					leftServo.setAngle(120+i*3);
					rightServo.setAngle(60-i*3);
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(i==9){
						winch1.set(0.2);
						winch2.set(0.2);	
					}
					if(i==20){
						winch1.set(0);
						winch2.set(0);	
					}
				}
				fingersBeingUsed=false;
			}
			
		}
		
	};

	
	public Scalar(SpeedController winch1, SpeedController winch2){
		this.winch1=winch1;
		this.winch2=winch2;
	}
	
	public static void instantiate(){
		Scalar scalar = new Scalar(Robot.resources.getMotorController("Winch1"), Robot.resources.getMotorController("Winch2"));
		Robot.resources.addSubSystem("Scalar", scalar);
	}
	
	public void setWinch(boolean b){
		if(b){
			winch1.set(1);
			winch2.set(1);
			scaling=true;
			
		}else{
			winch1.set(0);
			winch2.set(0);
			scaling=false;
		}
		
	}
	
	
	
		}
		
		
