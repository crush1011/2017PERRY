
package org.usfirst.frc.team1011.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.videoio.VideoCapture;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionThread;
import frc.team1011.ruben.AutonDriveForMeasurement;
import frc.team1011.ruben.AutonDriveStraight;
import frc.team1011.ruben.AutonStraightMeasurement;
import frc.team1011.ruben.DriveSemiCircle;
import frc.team1011.ruben.DriveTrain;
import frc.team1011.ruben.GreybotsAuto;
import frc.team1011.ruben.Resources;
import frc.team1011.ruben.SubSystem;
import frc.team1011.ruben.TeleDriveForMeasurement;
import vison.GripVisionCode;
import vison.VisionLoop;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	String autoSelected;
	SendableChooser chooser;
	Joystick driverJoystick, operatorJoystick;

	Encoder armEncoder;
	Encoder scalingEncoder;
	AHRS navx;
	private PowerDistributionPanel pdp;
	public double leftDistance, rightDistance;
	UsbCamera usbCamera;

	public static Resources resources;
	private Leds leds;
	private GearCollectorHead gch;
	private GearCollectorArm gca;
	private Scalar scalar;
	private DriveTrain driveTrain;
	private boolean manualMode;
	public static boolean driveEnabled = true;
	public Compressor compressor;
	
	//Spark led;
	
	
	//vision instantiations
	VisionLoop visionLoop;	
	CvSink cvSink;
	CvSource cvSource;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		System.out.println("ROBOTSTARTINGO");
		driverJoystick = new Joystick(0);
		operatorJoystick = new Joystick(1);
		Autonomous.setAutosDashboard();
		SmartDashboard.putString("DB/String 4", "Not yet");
		/*
		 * compressor = new Compressor(0);
		 * compressor.setClosedLoopControl(true); compressor.enabled();
		 */

		
		// solenoid init
		HashMap<String, Solenoid> solenoids = new HashMap<>();
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		

		solenoids.put("GearCollectorAngleSolenoid", new Solenoid(7));
		solenoids.put("GearCollectorClampSolenoid", new Solenoid(3)); //0 p3 

		// motor controller inits
		HashMap<String, SpeedController> motorControllers = new HashMap<>();
		CANTalon l1 = new CANTalon(1); //1    p3
		motorControllers.put("l1", l1);
		CANTalon l2 = new CANTalon(3);//3   p4
		motorControllers.put("l2", l2);
		CANTalon l3 = new CANTalon(5);//5   p5
		motorControllers.put("l3", l3);
		CANTalon r1 = new CANTalon(2);//2     p0
		motorControllers.put("r1", r1);
		CANTalon r2 = new CANTalon(4);//4    p1
		motorControllers.put("r2", r2);
		CANTalon r3 = new CANTalon(6);//6    p2
		motorControllers.put("r3", r3);
		CANTalon gearArmVictor = new CANTalon(0);
		//VictorSP gearArmVictor = new VictorSP(0);    
		motorControllers.put("GearCollectorMotor", gearArmVictor);
		Spark winch1 = new Spark(6);
		motorControllers.put("Winch1", winch1);
		Spark winch2 = new Spark(7);
		motorControllers.put("Winch2", winch2);
		
		
		//LEDS
		//led = new Spark(9);
		
		
		// sensor inits
		HashMap<String, Object> sensors = new HashMap();
		
		sensors.put("DriverJoystick", driverJoystick);
		sensors.put("OperatorJoystick", operatorJoystick);
		operatorJoystick.setRumble(RumbleType.kLeftRumble, 0);
		operatorJoystick.setRumble(RumbleType.kRightRumble, 0);
		
		
		navx = new AHRS(SPI.Port.kMXP);
		sensors.put("navX", navx);
		pdp = new PowerDistributionPanel(0);
		sensors.put("pdp", pdp);
		
		scalingEncoder = new Encoder(8,9);//6,7
		sensors.put("ScalingEncoder", scalingEncoder);
		
		armEncoder = new Encoder(4,5);
		//armEncoder = new Encoder(6,7);
		

		sensors.put("GearArmEncoder", armEncoder);
		Encoder encoder1 = new Encoder(2 , 3); //2,3
		sensors.put("LeftEncoder", encoder1);
		Encoder encoder2 = new Encoder(1 , 0); //1,0
		sensors.put("RightEncoder", encoder2);

		
		CameraServer cameraServer = CameraServer.getInstance();
		usbCamera = cameraServer.startAutomaticCapture();
		usbCamera.setBrightness(10);
		usbCamera.setResolution(320, 240);
		usbCamera.setFPS(15);
		usbCamera.setExposureManual(1);
		CameraServer.getInstance().startAutomaticCapture().setFPS(10);
		//usbCamera.setFPS(20);
		
		
		cvSink = cameraServer.getVideo();
		visionLoop = new VisionLoop(cvSink);
		sensors.put("vision", visionLoop);
		
		
		resources = new Resources(motorControllers, solenoids, new HashMap<String, SubSystem>(), sensors);

		// Sub System Instantiations
		GearCollectorHead.instantiate();
		gch = (GearCollectorHead) resources.getSubSystem("GearCollectorHead");
		GearCollectorArm.instantiate();
		gca = (GearCollectorArm) resources.getSubSystem("GearCollectorArm");

		Scalar.instantiate();
		scalar = (Scalar) resources.getSubSystem("Scalar");

		DriveTrain.instantiate();
		driveTrain = (DriveTrain) resources.getSubSystem("DriveTrain");
		manualMode = false;
		gca.setMode("PID");
		
		leds = new Leds(new Solenoid(4));
		
		visionLoop.start();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	
	double autoStartTime;
	AutonomousRoutine ourRoutine;
	public boolean gogogo=false;
	int countAuto;
	public void autonomousInit() {
		autoSelected = SmartDashboard.getString("Auto Selector", "Do Nothing");
		System.out.println("Auto selected: " + autoSelected);
		System.out.println("AUTOSTARTING");
		
	//	gca.displacement= -SmartDashboard.getNumber("DB/Slider 1",0);
		
		SmartDashboard.putString("DB/String 5", "Auto:" + autoSelected);
	
		driveTrain.resetPID();
		List<Runnable> actions = new ArrayList<>();
		actions.add(AutonomousRoutine.gearRight);
		//actions.add(gca.scoringCenter);
		ourRoutine = Autonomous.getAuto(autoSelected);
	//	ourRoutine = Autonomous.getAuto("2 Gear Side Right");
		//ourRoutine = Autonomous.getAuto("dffdgt");
		gogogo=false;
		usbCamera.setBrightness(10);
	//	usbCamera.setResolution(320, 240);
		usbCamera.setExposureManual(1);
		visionLoop.start();
		autoStartTime = System.currentTimeMillis();
		countAuto=0;
		
	}

	/**
	 * This function is called periodically during autonomous
	 */


	double pastAngleToTarget = 0;
	public void autonomousPeriodic( ) {
		String[] args;
		
		String a;
		if(gogogo==false){
			
			ourRoutine.run();
			
	//		new AutonDriveStraight(608, 0.85).run();// back out of peg
			
			//new Thread(new AutonStraightMeasurement(4,-0.75,305)).start();
		/*double distance = SmartDashboard.getNumber("DB/Slider 0", 0);
		double voltage = SmartDashboard.getNumber("DB/Slider 1", 0);
		double timeWanted =1000 * distance;
		timeWanted/= ((-220*voltage) - 89.67);
		SmartDashboard.putString("DB/String 3", ""+timeWanted);
		new Thread(new AutonDriveStraight(timeWanted, voltage)).start();*/
			//	new DriveSemiCircle(driveTrain, SmartDashboard.getNumber("DB/Slider 0", -30), 180).run();
			//new Thread(AutonomousRoutine.test).start();
			gogogo=true;	
		}
		

		SmartDashboard.putString("DB/String 1", "l:"+ driveTrain.leftDistanceTravelled());
		SmartDashboard.putString("DB/String 0", "r:"+ driveTrain.rightDistanceTravelled());

		SmartDashboard.putNumber("Gyro", navx.getFusedHeading());
	}

	/**
	 * This function is called periodically during operator control
	 */
	boolean pastAButton = false;
	boolean pastAOButton = false;
	
	boolean pastBButton = false;
	boolean pastXButton = false;
	boolean pastXOButton = false;
	boolean pastBOButton = false;
	boolean pastYOButton = false;
	boolean pastBackButton = false;
	boolean pastOBackButton = false;
	boolean pastYButton = false;
	boolean pastLTrigger = false;
	boolean pastLTriggerOperator = false;
	boolean pastRTrigger = false;
	boolean pastStartButton = false;
	double pastVelocity = 0;
	double pastDPad=0;

	@Override
	public void teleopInit() {
		gca.getPIDController().reset();
		gca.enable();
		usbCamera.setBrightness(50);
		usbCamera.setExposureAuto();
		visionLoop.stop();
		//led.set(0.05);

		SmartDashboard.putString("DB/String 4", "Not yet");
	}


	int count = 0;
	int stageScaling = 0;
	double encoderScaling = 0;

	public void teleopPeriodic() {
		driveTrain.turning=false;
		visionLoop.stop();
		
		//led.set(SmartDashboard.getNumber("DB/Slider 0", 0));
		
		//SmartDashboard.putNumber("Gyro", navx.getFusedHeading());	
		SmartDashboard.putNumber("Gyro", navx.getAngle());	
		
		boolean current;

			
		current = operatorJoystick.getRawAxis(2) > 0 ? true : false;
		if (current != pastLTrigger && current) {
			gca.setAngle(resources.COLLECTING_POSITION);
			gch.toggleClamp(false);
			gch.toggleAngle(true);
		}
		pastLTrigger = current;
		
		current = operatorJoystick.getRawButton(4);
		if (current != pastYOButton && current) {
			gch.toggleClamp();
		}
		pastYOButton = current;
		
		

		current = operatorJoystick.getRawAxis(3) > 0 ? true : false;
		if (current != pastRTrigger && current) {
			if (operatorJoystick.getRawButton(6) == true
					) {
				new Thread(gca.scoringCenter).start();
			} else {
				gca.setAngle(resources.SCORING_POSITION);
				gch.toggleClamp(true); 
				gch.toggleAngle(false);
			}
		}
		pastRTrigger = current;

		current = driverJoystick.getRawButton(1);
		if (current != pastAButton && current) {
			new Thread(new TeleDriveForMeasurement(-0.09, 0.45 )).start();
		}
		pastAButton = current;
		
		
		
		current = operatorJoystick.getRawButton(1);
		if (current != pastAOButton && current) {
			if(operatorJoystick.getRawButton(6)){
				new Thread(gca.goHome).start();
			}else{
				gca.setAngle(90);
				gch.toggleClamp(true);
				gch.toggleAngle(false);
			}
		}
		pastAOButton = current;

		current = operatorJoystick.getRawButton(3);
		if (current && !driverJoystick.getRawButton(6)) {
			scalar.setWinch(true);
			//compressor.stop();
		}else{
			scalar.setWinch(false);
			//compressor.start();
		}
		pastXOButton = current;
		
		current = operatorJoystick.getRawButton(2);
		if (current != pastBOButton && current) {
			gca.setAngle(90);
			gch.toggleClamp(false);
			gch.toggleAngle(true);
		}
		pastBOButton = current;

		
		double dpad = operatorJoystick.getPOV();
		if (dpad != pastDPad && dpad==0) {
			gca.displacement--;
		}else if(dpad!= pastDPad && dpad==180){
			gca.displacement++;
		}
		pastDPad = dpad;
		

		/*current = driverJoystick.getRawButton(3);
		if (current) {
			scalar.setWinch(true);
			
		}else{
			scalar.setWinch(false);
		}*/
		
		
		
		pastXButton=current;
		/*System.out.println("SCALAR: " + pdp.getCurrent(6));
		if(pdp.getCurrent(5)>65){
			System.out.println("STOPPINGSCALAR: " + pdp.getCurrent(6));
			scalar.setWinch(false);
		}*/
		/*if(scalingEncoder.getDistance() - encoderScaling > 1050 && scalar.scaling){
			scalar.setWinch(false);
		}*/
		//SmartDashboard.putString("DB/String 9", ":" + scalingEncoder.getDistance());
		
		
		
		if(SmartDashboard.getBoolean("DB/Button 2", false)){
			stageScaling=0;
		}
		pastXButton = current;
		
		//if(scalar.scaling = true && (navx.isMoving()))
		//SmartDashboard.putBoolean("DB/LED 1", navx.isMoving());

		/*double currentVelocity = Math.sqrt((Math.pow(navx.getVelocityX(), 2))+ 
				(Math.pow(navx.getVelocityY(), 2) + (Math.pow(navx.getVelocityZ(), 2))));
		SmartDashboard.putString("DB/String 4", ""+ pdp.getCurrent(1));
		SmartDashboard.putBoolean("DB/LED 3", currentVelocity > 0.2);
		current = pdp.getCurrent(1) > 15;
		SmartDashboard.putBoolean("DB/LED 2", current);
		if(current){
			System.out.println("CURRENT:" +System.currentTimeMillis() + ":::" + pdp.getCurrent(1));	
			
		}
		if(operatorJoystick.getRawButton(4)){
			System.out.println("RUBEN:" + System.currentTimeMillis());
		}
		pastVelocity = currentVelocity;*/
		
		
		
		current = driverJoystick.getRawButton(7);
		if (current != pastBackButton && current) {
			manualMode = !manualMode;
			if(manualMode){
				SmartDashboard.putBoolean("DB/LED 3", manualMode);
				
			}
			
		}
		
		
		pastBackButton = current;
		
		current = operatorJoystick.getRawButton(7);
		if (current != pastOBackButton && current) {
			if(gca.PIDEnabled && gca.PotEnabled){
				gca.setMode("Manual");
			}else{
				gca.setMode("PID");

			}
			
		}
		pastOBackButton = current;
		
		current = operatorJoystick.getRawButton(5);
		if (current ) {
			leds.flashLEDS();
			
		}
		pastLTriggerOperator = current;
		
			
		if(gca.PIDEnabled && gca.PotEnabled){
			
			operatorJoystick.setRumble(RumbleType.kRightRumble, 0);

			operatorJoystick.setRumble(RumbleType.kLeftRumble, 0);
		}else{

			operatorJoystick.setRumble(RumbleType.kRightRumble, 0.1);

			operatorJoystick.setRumble(RumbleType.kLeftRumble, 0.1);
		}
		

		
		
		
		current = driverJoystick.getRawButton(8);
		if (current != pastStartButton && current){
			gca.POTSTART= ((Encoder) resources.getSensor("GearArmEncoder")).getDistance();
		}
		pastStartButton = current;
		
		
		
		
		/*a
		 * current = driverJoystick.getRawButton(4); if( current != pastYButton
		 * && current){ if(gca.getAngleSetpoint()==10){ gca.setAngle(90);
		 * SmartDashboard.putString("DB/String 90", "90"); }else
		 * if(gca.getAngleSetpoint() == 90){ gca.setAngle(10);
		 * SmartDashboard.putString("DB/String 0", "0"); } } pastYButton =
		 * current;
		 */


		////////SmartDashboard.putString("DB/String 6",
				//////"SET:" + gca.getPIDController().getSetpoint() + "p" + "CUR:" + Double.toString(gca.getAngle()) + "P");
	//////	SmartDashboard.putString("DB/String 1", "" + resources.getMotorController("GearCollectorMotor").get());

	
		// resources.getMotorController("GearCollectorMotor").set(driverJoystick.getRawAxis(3)
		// - driverJoystick.getRawAxis(2));
		
		gca.manualMotorOutput = -operatorJoystick.getRawAxis(1)/2;

		double ratio;
		if (driverJoystick.getRawButton(5)) {
			ratio = 0.6;
		} else if (driverJoystick.getRawButton(6)) {
			
			ratio = 0.73;
		} else {
			ratio = 1;
		}
		//SmartDashboard.putString("DB/String 9", "crazy");
		
		SmartDashboard.putString("DB/String 1", "l:"+ driveTrain.leftDistanceTravelled());
		SmartDashboard.putString("DB/String 0", "r:"+ driveTrain.rightDistanceTravelled());
		
	/*	System.out.println("LE:" + ((Encoder)
		resources.getSensor("LeftEncoder")).getDistance());*/
		//System.out.println("RI:" + ((Encoder)
		//resources.getSensor("RightEncoder")).getDistance());
		
		//SmartDashboard.putString("DB/String 3",  "" +pdp.getCurrent(10)*0.5 + pdp.getCurrent(11)*0.5);
		double x_axis = driverJoystick.getRawAxis(1), y_axis = driverJoystick.getRawAxis(4);
		if (x_axis < 0.35 && x_axis > -0.35) {
			x_axis = 0;
		}
		if (y_axis < 0.35 && y_axis > -0.35) {
			y_axis = 0;
		}

		//if (resources.getSensor("LeftEncoder") != null && resources.getSensor("RightEncoder") != null && manualMode==false) {
		//	driveTrain.arcadeDrivePID(ratio * x_axis, ratio * y_axis);
		//} else {
		
		
		if(Math.abs(driverJoystick.getRawAxis(1)) >0.4 || Math.abs(driverJoystick.getRawAxis(4)) > 0.4){
			driveEnabled = true;
		}
		if(driveEnabled){
			driveTrain.arcadeDrive(ratio * x_axis, ratio * y_axis);
		}
			
		//}
		/*try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		if(SmartDashboard.getBoolean("DB/Button 3", false)){
			compressor.stop();
			
		}else{
			if(compressor.enabled() == false){
				compressor.start();
					
			}
				}

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}
	
	public void robotPeriodic(){
		autoSelected = SmartDashboard.getString("Auto Selector", "Do Nothing");
		SmartDashboard.putString("DB/String 6", autoSelected);
	}
	
	public void disabledInit(){
		visionLoop.stop();
		driveTrain.stopMotors();
	}

}
