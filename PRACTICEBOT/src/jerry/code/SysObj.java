/*
 * Class: SysObj
 * Author: Jeremiah Hanson
 * --------------------------------------------------------
 * Purpose: This is an enum class that is made to act as an
 * easy way to handle keys for the hashmap that is used to 
 * store all of the systems and subsystem in the System 
 * class.
 */

package jerry.code;

// This is an interface to store all the enums for all of 
// the major systems
public interface SysObj {
	
	
	// This is the enum for the Solenoids
	public enum Solenoid implements SysObj{
		
		COLLECTOR_ANGLE("CollectorAngle"),
		COLLECTOR_CLAMP("CollectorSolenoid");
		
		public String solenoid;
		
		// Constructor for the Solenoid Enum
		private Solenoid(String solenoid){
			this.solenoid = solenoid;
		}
	}
	
	// This is the enum for the MotorControllers
	public enum MotorController {
		
		LEFT_1("left1"), LEFT_2("left2"), LEFT_3("left3"),
		RIGHT_1("right1"), RIGHT_2("right2"), RIGHT_3("right3"),
		GEAR_ARM("GearCollectorMotor"),
		WINCH_1("winch1"), WINCH_2("winch2");
		
		public String motor;
		
		// Constructor for the MotorController Enum
		private MotorController(String motor){
			this.motor = motor;
		}
	}
	
	// This is the enum for the sensors
	public enum Sensors {
		
		DRIVER_STICK("DriverJoystick"), OPERATOR_STICK("OpertatorJoystick"),
		NAVX("NavX"), PDP("PowerDistributionPanel"),
		ARM_ENCODER("GearArmEncoder"), LEFT_ENCODER("LeftEncoder"), RIGHT_ENCODER("RightEncoder"),
		VISION("Vision");
		
		public String sensor;
		
		// Constructor the the Sensor Enum
		private Sensors(String sensor){
			this.sensor = sensor;
		}
	}
}
