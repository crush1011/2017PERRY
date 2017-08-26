package frc.team1011.ruben;

import java.util.Arrays;
import java.util.HashMap;

import java.util.List;



import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class Resources{
	
	private static Resources resources;
	HashMap<String, SpeedController> motorControllers;
	HashMap<String, Solenoid> solenoids;
	HashMap<String, SubSystem> subSystems;
	HashMap<String, Object> sensors;
	
	public final int SCORING_POSITION = -4; //-3  p2
	public final int COLLECTING_POSITION = -20;//-20   p-24
	
	
	
	
	public Resources(HashMap<String,SpeedController> motorControllers, 
		HashMap<String, Solenoid> solenoids, HashMap<String, SubSystem> subSystems,HashMap<String, Object> sensors){
		this.motorControllers = motorControllers;
		this.solenoids=solenoids;
		this.sensors = sensors;
		this.subSystems=subSystems;
		resources=this;

	}
	
	public static Resources getInstance(){
		return resources;
	}
	
	public static double limit(double value, double min, double max) {
		   return Math.min(Math.max(value, min), max);
	}
	
	public SubSystem getSubSystem(String subSystemName){
		return subSystems.get(subSystemName);
	}
	
	public Solenoid getSolenoid(String solenoidName){
		return solenoids.get(solenoidName);
	}
	
	public SpeedController getMotorController(String motorControllerName){
		return motorControllers.get(motorControllerName);
	}
	
	public Object getSensor(String sensorName){
		return sensors.get(sensorName);
	}
	
	public void addSubSystem(String subSystemName, SubSystem ss){
		this.subSystems.put(subSystemName, ss);
	}
	
	public void addSolenoid(String solenoidName, Solenoid s){
		this.solenoids.put(solenoidName, s);
	}
	
	public void addMotorController(String motorControllerName, SpeedController sc){
		this.motorControllers.put(motorControllerName, sc);
	}
	
	public void addSensor(String sensorName, Object sensor){
		this.sensors.put(sensorName, sensor);
	}
	
	
	
}
