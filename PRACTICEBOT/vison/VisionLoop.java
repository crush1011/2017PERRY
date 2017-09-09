package vison;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import edu.wpi.cscore.CvSink;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionLoop implements Runnable {

	VideoCapture videoCapture;
	GripVisionCode tracker;

	static Mat matOriginal;
	public static final double OFFSET_TO_FRONT = 0;
	public static final double CAMERA_WIDTH = 320;
	public static final double DISTANCE_CONSTANT = 2714; //2852
	public static final double WIDTH_BETWEEN_TARGET = 8.5;
	public static boolean shouldRun = true;
	
	double lengthBetweenContours=0;
	double distanceFromTarget=0;
	double lengthError=0;
	double angleToTarget=0;
	double[] centerX;
	
	Mat givenImage;
	
	private Thread visionThread;
	private CvSink cvSink;

	public VisionLoop(CvSink cvSink) {
		this.cvSink = cvSink;
		try {
			System.out.println("TRYING DIS");
			tracker = new GripVisionCode();

		} catch (Exception e) {
			e.printStackTrace();
		}
		givenImage = new Mat();
		centerX = new double[2];
		visionThread = new Thread(this);
	}
	
	private void setImage(Mat img){
		givenImage = img;
	}
	
	
	public boolean isRunning(){
		return running;
	}
	double startTime;
	boolean killThread = false;
	 boolean running = false;
	
	 
	public Runnable killVisionLoop = new Runnable(){
		public void run(){
			stop();
		}
	};
	
	
	@Override
	public void run() {
		while(!killThread){
			System.out.println("kT"  + killThread);
			running =true;
			startTime = System.currentTimeMillis();
			System.out.println("Vision Running");
			Mat mat = new Mat();
			cvSink.grabFrame(mat);
			
			if(givenImage != mat){
				try{
					this.setImage(mat);
					this.processImage();
					
				}catch(Exception e){
					System.err.println("NOT ENOGH MEMORY");
				}
			}

			try{
				Thread.sleep(50);	
			}catch(Exception e){
				
			}
			while(System.currentTimeMillis() - startTime <100){
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		killThread = false;
		running = false;
	}
	

	public void start(){
		killThread=false;
		if(running==false && !visionThread.isAlive()){
			visionThread = new Thread(this);
			visionThread.start();
		}
		
	}
	
	public void stop(){
		killThread = true;
	}
	
	
	public void processImage() {
		if(givenImage!=null){
			System.out.println("Processing Started");
//			matOriginal = new Mat();
			matOriginal = givenImage;
			// only run for the specified time

			// System.out.println("Hey I'm Processing Something!");
		//	videoCapture.read(matOriginal);
			tracker.process(givenImage);
			System.out.println(tracker.filterContoursOutput().size());
			//System.out.println(tracker.hslThresholdOutput().toString());
			updateCenterX();
			updateAngle();
			if(centerX.length == 2){
				System.out.println("Distance = " + distanceFromTarget + "Angle To it:" + angleToTarget );
		//		System.out.println("LengthBetweenCountours: " + lengthBetweenContours);
			//	System.out.println("CenterX1: " + centerX[0] + "  Center X2: " + centerX[1]);
			}
			
			if(givenImage == new Mat()){
				//System.out.println("Forsure not workin");
			}else{
			//	System.out.println("Maybe Working");
			}
			// table.putDouble("distanceFromTarget", distanceFromTarget());
			// table.putDouble("angleFromGoal", getAngle());
			// table.putNumberArray("centerX", centerX);
			//videoCapture.read(matOriginal);
		}else{
			System.out.println("CANT PROCESS");
		}


	}

	public void updateCenterX() {
		double[] defaultValue = new double[0];
		System.out.println("UpdatingCenterX");
		// This is the center value returned by GRIP thank WPI
		if (!tracker.filterContoursOutput().isEmpty() && tracker.filterContoursOutput().size() >= 2) {
			Rect r = Imgproc.boundingRect(tracker.filterContoursOutput().get(1));
			Rect r1 = Imgproc.boundingRect(tracker.filterContoursOutput().get(0));
			centerX = new double[] { r1.x + (r1.width / 2), r.x + (r.width / 2) };
			// Imgcodecs.imwrite("output.png", matOriginal);
			// this again checks for the 2 shapes on the target
			if (tracker.filterContoursOutput().size() >= 2) {
				// subtracts one another to get length in pixels
				lengthBetweenContours = Math.abs(centerX[0] - centerX[1]);
				//SmartDashboard.putString("DB/String 1", ""+lengthBetweenContours);
			}
		}
	}
	
	public double distanceFromTarget(){
	//	System.out.println("Getting Distance from target");
		// distance costant divided by length between centers of contours
		distanceFromTarget = DISTANCE_CONSTANT / lengthBetweenContours;
		return distanceFromTarget - OFFSET_TO_FRONT; 
	}
	
	
	public void updateAngle(){
		System.out.println("UpdatingAngle");
		// 8.5in is for the distance from center to center from goal, then divide by lengthBetweenCenters in pixels to get proportion
		double constant = WIDTH_BETWEEN_TARGET / lengthBetweenContours;
		double angleToGoal = 0;
			//Looking for the 2 blocks to actually start trig
		if(!tracker.filterContoursOutput().isEmpty() && tracker.filterContoursOutput().size() >= 2){

			if(tracker.filterContoursOutput().size() >= 2){
				// this calculates the distance from the center of goal to center of webcam 
				double distanceFromCenterPixels= ((centerX[0] + centerX[1]) / 2) - (CAMERA_WIDTH / 2);
				// Converts pixels to inches using the constant from above.
				double distanceFromCenterInch = distanceFromCenterPixels * constant;
				// math brought to you buy Chris and Jones
				angleToGoal = Math.atan(distanceFromCenterInch / distanceFromTarget());
				angleToGoal = Math.toDegrees(angleToGoal);
				
				angleToTarget=angleToGoal;
				// prints angle
				//System.out.println("Angle: " + angleToGoal);
				}
			}
	}
	
	public double getAngleToTarget(){
		

		double distance =  distanceFromTarget;
		double offset = 2.1; // test tomorrow!!!!!!!!!!!!
		
		
		double cameraAngle = 90-angleToTarget;
		cameraAngle = Math.toRadians(cameraAngle);
		//c^2 = A^2 + b^2 - 2abcos(C)
		double c = (distance*distance) + (offset*offset) - 2 *distance*offset*Math.cos(cameraAngle);
		c = Math.sqrt(c);
		//System.out.println(c);
		double formula = c/(Math.sin(cameraAngle));
		//formula = distance/ sin(theta);
		//sin(theta) = distance/formula
		double theta = distance / formula;
		theta = Math.asin(theta);
		theta = Math.toDegrees(theta);
		theta = 90-theta;
		if(distance <c){
			theta = -theta;
		}
		SmartDashboard.putString("DB/String 9","a:" + theta);
		if(running){
			return theta;	
		}else{
			return 0;
		}
		
	}



}
