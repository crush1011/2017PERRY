/*
 * Class: Systems
 * Author: Jeremiah Hanson
 * ----------------------------------------------------------------------------
 * Purpose: The purpose of this class is to create a hashmap that stores all of
 * objects that represent the various systems and subsystems on the robot, such
 * as solenoids and speed-controllers 
 * 
 * Responsibilities: Responsible for maintaining the systems so that no class 
 * should have to create their own system objects but instead ask this class 
 * for those objects.
 */

package jerry.code;

import java.util.HashMap;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import vison.VisionLoop;

public class Systems {
	
	private static Systems systems;
	HashMap<SysObj, Object> subsystems;
	
	// Private constructor that is used once to create all the subsystem objects
	private Systems(){
		
		// Solenoids
		subsystems.put(SysObj.Solenoid.COLLECTOR_ANGLE, new Solenoid(7));
		subsystems.put(SysObj.Solenoid.COLLECTOR_CLAMP, new Solenoid(3));
		
		// Left Motor Controllers
		subsystems.put(SysObj.MotorController.LEFT_1, new CANTalon(1));
		subsystems.put(SysObj.MotorController.LEFT_2, new CANTalon(3));
		subsystems.put(SysObj.MotorController.LEFT_3, new CANTalon(5));
		
		// Right Motor Controllers
		subsystems.put(SysObj.MotorController.RIGHT_1, new CANTalon(2));
		subsystems.put(SysObj.MotorController.RIGHT_2, new CANTalon(4));
		subsystems.put(SysObj.MotorController.RIGHT_3, new CANTalon(6));
		
		// Arm Motor Controllers
		subsystems.put(SysObj.MotorController.GEAR_ARM, new CANTalon(0));
		subsystems.put(SysObj.MotorController.WINCH_1, new Spark(6));
		subsystems.put(SysObj.MotorController.WINCH_2, new Spark(7));
		
		// Sensors
		subsystems.put(SysObj.Sensors.DRIVER_STICK, new Joystick(0));
		subsystems.put(SysObj.Sensors.OPERATOR_STICK, new Joystick(1));
		((Joystick)subsystems.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kLeftRumble, 0);
		((Joystick)subsystems.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kRightRumble, 0);
		subsystems.put(SysObj.Sensors.NAVX, new AHRS(SPI.Port.kMXP));
		subsystems.put(SysObj.Sensors.PDP, new PowerDistributionPanel(0));
		
		// Encoders
		subsystems.put(SysObj.Sensors.SCALING_ENCODER, new Encoder(8,9));
		subsystems.put(SysObj.Sensors.ARM_ENCODER, new Encoder(4,5));
		subsystems.put(SysObj.Sensors.LEFT_ENCODER, new Encoder(2,3));
		subsystems.put(SysObj.Sensors.RIGHT_ENCODER, new Encoder(1,0));
		
		// Camera
		UsbCamera usbCamera;
		CameraServer cameraServer = CameraServer.getInstance();
		usbCamera = cameraServer.startAutomaticCapture();
		usbCamera.setBrightness(10);
		usbCamera.setResolution(320, 240);
		usbCamera.setFPS(15);
		usbCamera.setExposureManual(1);
		CameraServer.getInstance().startAutomaticCapture().setFPS(10);
		
		CvSink cvSink;
		cvSink = cameraServer.getVideo();
		subsystems.put(SysObj.Sensors.VISION, new VisionLoop(cvSink));
		
	}
	
	
	// This returns the instance of Systems, or creates the instnace if there is
	// none.
	public Systems getInstance(){
		
		if (systems == null) {
			systems = new Systems();
		}
		return systems;
		
	}
	
	// returns the hashmap containing all the subsystems
	public HashMap<SysObj, Object> getHashMap() {
		return subsystems;
	}
	
	// This returns the instance of Systems, or creates the instnace if there is
	// none.
	public Systems getInstance(){
		
		if (systems == null) {
			systems = new Systems();
		}
		return systems;
		
	}

}



 