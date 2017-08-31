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

public enum SysObj {

	SOLENOID("solenoid"), 
	MOTORCONTROLLER("motorController"), 
	SUBSYSTEM("subSystem"),
	SENSOR("sensor");
	public String system;

	// Constructor for the SysObj enum
	private SysObj(String system) {
		this.system = system;
	}
	
	// This is the sub-enums for the SOLENOID enum
	public enum Solenoid {
		
	}
}
