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

public class Systems {
	
	private static Systems systems;
	HashMap<SysObj, SpeedController> subsystems;
	
	private Systems(){
		
	}

}
