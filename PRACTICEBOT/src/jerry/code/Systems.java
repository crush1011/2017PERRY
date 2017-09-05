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

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import jerry.code.SysObj.Solenoid;

public class Systems {
	
	private static Systems systems;
	HashMap<SysObj, Object> subsystems;
	
	private Systems(){
		subsystems.put(SysObj.Solenoid.COLLECTOR_ANGLE, new Solenoid(7));
		subsystems.put(SysObj.Solenoid.COLLECTOR_CLAMP, new Solenoid(3));
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



 