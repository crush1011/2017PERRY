package frc.team1011.ruben;

import java.util.ArrayList;
import java.util.List;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1011.ruben.SubSystem;

import static frc.team1011.ruben.Resources.*;
import static org.usfirst.frc.team1011.robot.Robot.*;

public class DriveTrain extends PIDSubsystem implements SubSystem {

	public boolean turning;
	private List<SpeedController> leftSide, rightSide;
	private AHRS navx;
	private double heading;
	private boolean leftSideNegative, rightSideNegative, squaredInputs;
	private int onTargetTicks;

	Encoder leftEncoder, rightEncoder;
	PIDDriveSide leftPID, rightPID;

	public DriveTrain(List<SpeedController> leftSide, boolean leftSideNegative, List<SpeedController> rightSide,
			boolean rightSideNegative, boolean squaredInputs, AHRS navx) {
		super("DriveTrain", 0.01, 0.04, 0.02);
		this.setAbsoluteTolerance(1);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-1.0, 1.0);
		this.enable();
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		this.leftSideNegative = leftSideNegative;
		this.rightSideNegative = rightSideNegative;
		this.squaredInputs = squaredInputs;
		this.navx = navx;
		turning = false;
		heading = 0;
		onTargetTicks = 0;
	}

	public static final double tickToFt = ((256 * 12) / (3.65 * Math.PI));

	public DriveTrain(List<SpeedController> leftSide, boolean leftSideNegative, List<SpeedController> rightSide,
			boolean rightSideNegative, boolean squaredInputs, AHRS navx, Encoder leftEncoder, Encoder rightEncoder) {
		super("DriveTrain", 0.05, 0.0, 0.05);
		this.setAbsoluteTolerance(3);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-1.0, 1.0);
		this.enable();
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		this.leftSideNegative = leftSideNegative;
		this.rightSideNegative = rightSideNegative;
		this.squaredInputs = squaredInputs;
		this.navx = navx;
		turning = false;
		heading = 0;
		onTargetTicks = 0;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		leftPID = new PIDDriveSide(leftEncoder, tickToFt, false); //false ptrue
		leftPID.setVelocity(0);
		rightPID = new PIDDriveSide(rightEncoder, tickToFt, false); //false ptrue
		rightPID.setVelocity(0); //true, true
		leftPID.getPIDController().setPID(0.2, 0, 0.8);
		rightPID.getPIDController().setPID(0.2, 0, 0.8);

	}

	int countaa;
	
	public void arcadeDrivePID(double move, double rotate) {
		// rotate = 0.7*rotate;
		double leftMotorSpeed;
		double rightMotorSpeed;
		double moveValue, rotateValue;
		moveValue = limit(move, -1, 1);
		rotateValue = -limit(rotate, -1, 1); // negative p positive

		if (squaredInputs) {
			// square the inputs (while preserving the sign) to increase fine
			// control
			// while permitting full power
			if (moveValue >= 0.0) {
				moveValue = (moveValue * moveValue);
			} else {
				moveValue = -(moveValue * moveValue);
			}
			if (rotateValue >= 0.0) {
				rotateValue = (rotateValue * rotateValue);
			} else {
				rotateValue = -(rotateValue * rotateValue);
			}
		}

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		leftMotorSpeed = leftMotorSpeed * 12.5;
		rightMotorSpeed = rightMotorSpeed * 12.5;
		leftPID.setVelocity(leftMotorSpeed);
		rightPID.setVelocity(rightMotorSpeed);
		leftPID.enable();
		rightPID.enable();
		double leftPIDOutput = leftPID.getMotorOutput();
		double rightPIDOutput = rightPID.getMotorOutput();

		leftSide.forEach(sc -> {
			sc.set(leftSideNegative ? -leftPIDOutput : leftPIDOutput);
			/*if (DriverStation.getInstance().isEnabled()) {
				System.out.println("LEFT:" + sc.get());

			}*/
		});
		rightSide.forEach(sc -> {
			sc.set(rightSideNegative ? -rightPIDOutput : rightPIDOutput);
			/*if (DriverStation.getInstance().isEnabled()) {
				System.out.println("Right:" + sc.get());

			}*/
		});

	}

	public void stopMotors() {
		leftSide.forEach(sc -> {
			sc.set(0);
		});
		rightSide.forEach(sc -> {
			sc.set(0);
		});
		leftPID.setVelocity(0);
		rightPID.setVelocity(0);

	}
	
	public void resetPID(){
		leftPID.getPIDController().reset();
		leftPID.enable();
		rightPID.getPIDController().reset();
		rightPID.enable();
	}

	public void arcadeDrive(double move, double rotate) {
		double leftMotorSpeed;
		double rightMotorSpeed;
		double moveValue, rotateValue;
		moveValue = limit(move, -1, 1); //positive     pnegative
		rotateValue = -limit(rotate, -1, 1); //negative pnegative

		if (squaredInputs) {
			// square the inputs (while preserving the sign) to increase fine
			// control
			// while permitting full power
			if (moveValue >= 0.0) {
				moveValue = (moveValue * moveValue);
			} else {
				moveValue = -(moveValue * moveValue);
			}
			if (rotateValue >= 0.0) {
				rotateValue = (rotateValue * rotateValue);
			} else {
				rotateValue = -(rotateValue * rotateValue);
			}
		}

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}

		leftSide.forEach(sc -> {
			sc.set(leftSideNegative ? -leftMotorSpeed : leftMotorSpeed);
		});
		rightSide.forEach(sc -> {
			sc.set(rightSideNegative ? -rightMotorSpeed : rightMotorSpeed);
		});

	}

	public void tankDrive(double x, double y) {
		leftSide.forEach(sc -> {
			sc.set(leftSideNegative ? -x : x);
		});
		rightSide.forEach(sc -> {
			sc.set(rightSideNegative ? -y : y);
		});
	}

	double turningStartTime;

	public void turnTo(double degree) {
		this.setSetpoint(degree % 360);
		this.getPIDController().setPID(0.015, 0, 0.025);
		//this.getPIDController().setPID(SmartDashboard.getNumber("DB/Slider 0",0), SmartDashboard.getNumber("DB/Slider 1",0), SmartDashboard.getNumber("DB/Slider 2",0));
	//	this.resetPID();
	//	this.enable();
		turning = true;
		onTargetTicks = 0;
		turningStartTime = System.currentTimeMillis();
	}

	@Override
	protected double returnPIDInput() {
		heading = navx.getFusedHeading();
		return heading;
	}

	@Override
	protected void usePIDOutput(double output) {
		if (turning) {
			if (onTargetTicks < 10 && (System.currentTimeMillis() - turningStartTime < 8000) && DriverStation.getInstance().isAutonomous()) {
				System.out.println("TURNOUTPUT:" + output);
				if(output>0){
					this.arcadeDrive(0,Math.sqrt(output+0.12));	
				}else{
					this.arcadeDrive(0,-Math.sqrt(Math.abs(output-0.12)));
				}
				
			} else {
				turning = false;
				this.stopMotors();
			}
		}
		
		if (this.onTarget() && turning) {
			onTargetTicks++;
			System.out.println(this.getSetpoint() + "CURRENT:" + returnPIDInput() + " OnTarget");
		}

	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public static void instantiate() {
		List<SpeedController> leftSide = new ArrayList<>();
		List<SpeedController> rightSide = new ArrayList<>();

		for (int i = 0; i <= 5; i++) {
			if (resources.motorControllers.containsKey("l" + i)) {
				leftSide.add(resources.getMotorController("l" + i));
			}
			if (resources.motorControllers.containsKey("r" + i)) {
				rightSide.add(resources.getMotorController("r" + i));
			}
		}
//changed left to true
		DriveTrain dt = new DriveTrain(leftSide, false, rightSide, true, true, (AHRS) resources.getSensor("navX"),
				(Encoder) resources.getSensor("LeftEncoder"), (Encoder) resources.getSensor("RightEncoder"));
		resources.addSubSystem("DriveTrain", dt); //false, true   ptrue, false
	}
	
	public double leftDistanceTravelled(){
		
		return -leftEncoder.getDistance()/tickToFt;//negative ppositive
	}
	public double rightDistanceTravelled(){
		return -rightEncoder.getDistance()/tickToFt; //negative p positive
	}
	
	
	

}
