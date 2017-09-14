package org.usfirst.frc.team1011.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Leds {

	Solenoid ledsSolenoid;
	int count=0;
	boolean threadStarted;
	
	boolean flashing;
	public Leds(Solenoid solenoid){
		ledsSolenoid = solenoid;
		ledsSolenoid.set(true);
		
	}
	
	public void flashLEDS(){
		
		if(count==7){
			count=6;
		}else{
			count=7;
		}
		if(!threadStarted){
			threadStarted=true;
			Runnable run = new Runnable(){
				@Override
				public void run() {

					System.out.println("FLASHING");
					while(count>0){
						count--;
						try {
							Thread.sleep(80);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(count%2==0){
							ledsSolenoid.set(false);
						}else{
							ledsSolenoid.set(true);
						}
						
					}
					ledsSolenoid.set(true);
					threadStarted=false;
				}
				
			};
			new Thread(run).start();
		}
	}
	
	
	
}
