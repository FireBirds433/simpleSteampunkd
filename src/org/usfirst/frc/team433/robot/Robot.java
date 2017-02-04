package org.usfirst.frc.team433.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	// AutonomousRobot autonomousRobot;
	RobotDrive myRobot;
	Joystick stick;
	Joystick xbox;

	// Drivetrain
	// Solenoid solenoidSpeedshift = new Solenoid(0, 1);
	int NORMSPEED = 100;
	CANTalon leftDrivetrain1 = new CANTalon(4);
	CANTalon leftDrivetrain2 = new CANTalon(5);
	// CANTalon leftDrivetrainSlaveMotor = new CANTalon(6);
	CANTalon rightDrivetrain1 = new CANTalon(1);
	CANTalon rightDrivetrain2 = new CANTalon(2);
	// CANTalon rightDrivetrainSlaveMotor = new CANTalon(3);

	// Loading Station Retrieval
	/*
	 * Talon gearRetrievalPivot = new Talon(6); Talon gearRetrievalAgitator =
	 * new Talon(7); Solenoid solenoidWhaleTailServo = new Solenoid(0, 2);
	 * AnalogInput gearUltrasonicAgitator = new AnalogInput(1); DigitalInput
	 * uprightGearLimitSwitch = new DigitalInput(1); DigitalInput
	 * angledGearLimitSwitch = new DigitalInput(2);
	 */

	// Floor Gear Retrieval
	// Solenoid solenoidGearFloorRetriever = new Solenoid(0, 0);
	// Talon floorRetrievalPivot = new Talon (8);

	// Camera Code
	Solenoid LEDRing;
	int camStream;
	VideoCamera camFront;
	CameraServer server;
	NetworkTable contourReport;

	// Compressor
	Compressor compressor;
	int currCompressor;
	int compressoron;
	int compressoroff;
	int autoLoop;

	// Sensor Flags
	boolean fl_gearBarUp = false;
	boolean flagLeftLimitSensor = false;
	boolean flagRightLimitSensor = false;

	AnalogInput ultrasonic;

	// Autonomous Switches
	DigitalInput autonSwitchA;
	DigitalInput autonSwitchB;
	DigitalInput autonSwitchC;
	int auton;
	int switchAFinal;
	int switchBFinal;
	int switchCFinal;
	int switBinFin;

	// Hanging Mechanism
	CANTalon hangMotor1 = new CANTalon(7);
	CANTalon hangMotor2 = new CANTalon(8);

	DriverStation driverStation = DriverStation.getInstance();
	// SendableChooser<AutonomousRobot> autoChooser;

	public Robot() {
	}

	public void moveRobotForward(double speed) {
		leftDrivetrain1.set(-speed);
		leftDrivetrain2.set(-speed);
		// leftDrivetrain3.set(-speed);
		rightDrivetrain1.set(speed);
		rightDrivetrain2.set(speed);
		// rightDrivetrain3.set(speed);
	}

	public void moveRobotReverse(double speed) {
		leftDrivetrain1.set(speed);
		leftDrivetrain2.set(speed);
		// leftDrivetrain3.set(speed);
		rightDrivetrain1.set(-speed);
		rightDrivetrain2.set(-speed);
		// rightDrivetrain3.set(-speed);
	}

	public void moveRobotTurnLeft(double speedLeft, double speedRight) {
		leftDrivetrain1.set(speedLeft);
		leftDrivetrain2.set(speedLeft);
		// leftDrivetrain3.set(speedLeft);
		rightDrivetrain1.set(speedRight);
		rightDrivetrain2.set(speedRight);
		// rightDrivetrain3.set(speedRight);
	}

	public void moveRobotTurnRight(double speedLeft, double speedRight) {
		leftDrivetrain1.set(-speedLeft);
		leftDrivetrain2.set(-speedLeft);
		// leftDrivetrain3.set(-speedLeft);
		rightDrivetrain1.set(-speedRight);
		rightDrivetrain2.set(-speedRight);
		// rightDrivetrain3.set(-speedRight);
	}

	@Override
	public void robotInit() {
		LEDRing = new Solenoid(0, 7);
		myRobot = new RobotDrive(leftDrivetrain1, leftDrivetrain2, rightDrivetrain1, rightDrivetrain2);

		autonSwitchA = new DigitalInput(0);
		autonSwitchA = new DigitalInput(1);
		autonSwitchA = new DigitalInput(2);

		ultrasonic = new AnalogInput(3);

		compressor = new Compressor(0);
		/*
		 * rightDrivetrainSlaveMotor.changeControlMode(TalonControlMode.Follower
		 * ); rightDrivetrainSlaveMotor.set(3);
		 */
		/*
		 * lefttDrivetrainSlaveMotor.changeControlMode(TalonControlMode.Follower
		 * ); rightDrivetrainSlaveMotor.set(1);
		 */

		stick = new Joystick(0); // joystick
		xbox = new Joystick(1); // xbox controller
		auton = 0;
		autoLoop = 1;
		contourReport = NetworkTable.getTable("GRIP/myContoursReport");
		CameraServer.getInstance().startAutomaticCapture();

		SmartDashboard.putNumber("Autonomous Selector", switBinFin);

		/*
		 * new Thread(() -> { UsbCamera frontLowerFloorCam =
		 * CameraServer.getInstance().startAutomaticCapture(0);
		 * frontLowerFloorCam.setResolution(640, 480); boolean
		 * frontLowerFloorCamButton = xbox.getRawButton(1); CvSink cvSinkGearCam
		 * = CameraServer.getInstance().getVideo(frontLowerFloorCam); CvSource
		 * gearCamOutputStream =
		 * CameraServer.getInstance().putVideo("Front Lower Floor Cam", 640,
		 * 480);
		 * 
		 * UsbCamera hangCam =
		 * CameraServer.getInstance().startAutomaticCapture(1);
		 * hangCam.setResolution(640, 480); boolean hangCamButton =
		 * xbox.getRawButton(2); CvSink cvSinkHangCam =
		 * CameraServer.getInstance().getVideo(frontLowerFloorCam);
		 * 
		 * UsbCamera frontUpperFloorCam =
		 * CameraServer.getInstance().startAutomaticCapture(2);
		 * frontUpperFloorCam.setResolution(640, 480); boolean
		 * frontUpperFloorCamButton = xbox.getRawButton(3); CvSink
		 * cvSinkFrontUpperFloorCam =
		 * CameraServer.getInstance().getVideo(frontLowerFloorCam);
		 * 
		 * UsbCamera backGearPickupCam =
		 * CameraServer.getInstance().startAutomaticCapture(3);
		 * backGearPickupCam.setResolution(640, 480); boolean
		 * backGearPickupCamButton = xbox.getRawButton(4); CvSink
		 * cvSinkBackGearPickupCam =
		 * CameraServer.getInstance().getVideo(frontLowerFloorCam);
		 * 
		 * UsbCamera backOfBotCam =
		 * CameraServer.getInstance().startAutomaticCapture(4);
		 * backOfBotCam.setResolution(640, 480); boolean backOfBotCamButton =
		 * xbox.getRawButton(5); CvSink cvSinkBackOfBotCam =
		 * CameraServer.getInstance().getVideo(frontLowerFloorCam);
		 * 
		 * Mat image = new Mat();
		 * 
		 * while (!Thread.interrupted()) { if (frontLowerFloorCamButton) {
		 * cvSinkGearCam.setEnabled(true); cvSinkHangCam.setEnabled(false);
		 * cvSinkFrontUpperFloorCam.setEnabled(false);
		 * cvSinkBackGearPickupCam.setEnabled(false);
		 * cvSinkBackOfBotCam.setEnabled(false); cvSinkGearCam.grabFrame(image);
		 * } else if (hangCamButton) { cvSinkGearCam.setEnabled(false);
		 * cvSinkHangCam.setEnabled(true);
		 * cvSinkFrontUpperFloorCam.setEnabled(false);
		 * cvSinkBackGearPickupCam.setEnabled(false);
		 * cvSinkBackOfBotCam.setEnabled(false); cvSinkGearCam.grabFrame(image);
		 * } else if (frontUpperFloorCamButton) {
		 * cvSinkGearCam.setEnabled(false); cvSinkHangCam.setEnabled(false);
		 * cvSinkFrontUpperFloorCam.setEnabled(true);
		 * cvSinkBackGearPickupCam.setEnabled(false);
		 * cvSinkBackOfBotCam.setEnabled(false); cvSinkGearCam.grabFrame(image);
		 * } else if (backGearPickupCamButton) {
		 * cvSinkGearCam.setEnabled(false); cvSinkHangCam.setEnabled(false);
		 * cvSinkFrontUpperFloorCam.setEnabled(false);
		 * cvSinkBackGearPickupCam.setEnabled(true);
		 * cvSinkBackOfBotCam.setEnabled(false); cvSinkGearCam.grabFrame(image);
		 * } else if (backOfBotCamButton) { cvSinkGearCam.setEnabled(false);
		 * cvSinkHangCam.setEnabled(false);
		 * cvSinkFrontUpperFloorCam.setEnabled(false);
		 * cvSinkBackGearPickupCam.setEnabled(false);
		 * cvSinkBackOfBotCam.setEnabled(true); cvSinkGearCam.grabFrame(image);
		 * } gearCamOutputStream.putFrame(image); } }).start();
		 */

		/*
		 * autoChooser = new SendableChooser<AutonomousRobot>();
		 * autoChooser.addDefault("My Default", new AutonomousRobot(0));
		 * autoChooser.addObject("Right Peg", new AutonomousRobot(1));
		 * autoChooser.addObject("Center Peg", new AutonomousRobot(2));
		 * autoChooser.addObject("Left Peg", new AutonomousRobot(3));
		 * autoChooser.addObject("Right Hopper", new AutonomousRobot(4));
		 * autoChooser.addObject("Do Nothing", new AutonomousRobot(5));
		 * autoChooser.addObject("Left Hopper", new AutonomousRobot(6));
		 * autoChooser.addObject("Cross Baseline", new AutonomousRobot(7));
		 * SmartDashboard.putData("Autonomous Chooser", autoChooser);
		 */
	}

	public double getUltrasonicInches() {
		// In two locations we measured the ultrasonic voltage and the distance
		// in inches.
		// First calculate slope by dividing the difference in inches by the
		// difference in voltage measurements
		// Use equation of a line to find the offsets
		double result;
		double rawVoltage = ultrasonic.getVoltage();
		result = (rawVoltage * 17.86) + 1.7842;// so the result is the volts
												// converted to inches
		return result;
	}

	/*
	 * public boolean isGearBarUp() { boolean result; double ultrasonicValue =
	 * gearUltrasonicAgitator.getVoltage();// should // be // named // proximity
	 * // sensor if (ultrasonicValue < .1) { result = false; } else { result =
	 * true; } return result; }
	 */

	@Override
	public void autonomousInit() {
		autoLoop = 0;
		auton = 0;
		// autonomousRobot = autoChooser.getSelected();
		// SmartDashboard.putNumber("autoLoop", autoLoop);
		// autonomousRobot.start();
	}

	@Override
	public void autonomousPeriodic() {

		switBinFin = SmartDashboard.getInt("Autonomous Selector", switBinFin);

		/*
		 * boolean switRaw1 = autonSwitchA.get();// analog switch 1 boolean
		 * switRaw2 = autonSwitchB.get();// analog switch 2 boolean switRaw3 =
		 * autonSwitchC.get();// analog switch 3
		 * 
		 * if (switRaw1) { switchAFinal = 1; } else { switchAFinal = 0; } if
		 * (switRaw2) { switchBFinal = 1; } else { switchBFinal = 0; } if
		 * (switRaw3) { switchCFinal = 1; } else { switchCFinal = 0; }
		 * 
		 * int switBinFin = (switchAFinal * 4) + (switchBFinal * 2) +
		 * switchCFinal;
		 */

		switch (switBinFin) {
		case 0:
			DoNothing();
			break;
		case 1:
			rightPeg();
			break;
		case 2:
			centerPeg();
			break;
		case 3:
			rightHopper();
			break;
		case 4:
			leftPeg();
			break;
		case 5:
			centerPegandStay();
			break;
		case 6:
			leftHopper();
			break;
		case 7:
			crossBaseline();
			break;
		}
	}

	public void DoNothing() {// all switch down
		moveRobotForward(0);
	}

	public void rightPeg() {
		LEDRing.set(true);
		if (autoLoop < 120) {
			moveRobotForward(.5);
			autoLoop++;
			SmartDashboard.putNumber("autoLoop", autoLoop);
		}

		else if (autoLoop >= 120) {
			double[] defaultValues = new double[5];
			defaultValues[0] = 1;
			defaultValues[1] = 2;
			defaultValues[2] = 3;
			defaultValues[3] = 4;
			defaultValues[4] = 5;
			double[] centerx = contourReport.getNumberArray("centerx", defaultValues);
			double centerxavg = (centerx[0] + centerx[1]) / 2;
			double centerx0 = centerx[0];
			double[] GRIPheight = contourReport.getNumberArray("height", defaultValues);
			double height = GRIPheight[0];

			SmartDashboard.putNumber("centerxavg", centerxavg);
			SmartDashboard.putNumber("grip height", height);

			if (height < 40) {
				if (centerx[0] == 1 || centerxavg < 60) {
					moveRobotTurnLeft(.25, .25);

				} else if (centerxavg > 120) {
					moveRobotTurnRight(.4, .4);
				}
			} else if (height > 40) {
				moveRobotForward(0);
			} else {
			}
			/*
			 * try { double[] defaultValues = new double[5]; defaultValues[0] =
			 * 1; defaultValues[1] = 2; defaultValues[2] = 3; defaultValues[3] =
			 * 4; defaultValues[4] = 5; defaultValues[5] = 6; double[] centerx =
			 * contourReport.getNumberArray("centerx", defaultValues); double
			 * centerxavg = (centerx[0] + centerx[1]) / 2; double centerx0 =
			 * centerx[0]; double[] GRIPheight =
			 * contourReport.getNumberArray("height", defaultValues); double
			 * height = GRIPheight[0];
			 * 
			 * SmartDashboard.putNumber("centerxavg", centerx0); if (centerx[0]
			 * == 1 || centerxavg < 60) { moveRobotTurnLeft(.4, .4);
			 * 
			 * }else if (centerxavg > 120) { moveRobotTurnRight(.4, .4); } if
			 * (height < 40) { moveRobotForward(.5); } else {
			 * moveRobotReverse(.5); } } catch (ArrayIndexOutOfBoundsException
			 * e) { System.out.println("Array123 is out of Bounds" + e); }
			 */
		} else {
			moveRobotForward(0);
		}

		autoLoop++;

		SmartDashboard.putNumber("Autonomous Loops", autoLoop);
	}

	public void centerPeg() {
		// Dariya and Erin project --- TBD
	}

	public void rightHopper() {
		if (autoLoop < 300) {
			moveRobotForward(.7);
		} else if (autoLoop >= 300) {
			moveRobotTurnRight(.5, .5);
		} else {
			moveRobotForward(0);
		}
	}

	public void leftPeg() {
		if (autoLoop < 300) {
			moveRobotForward(.5);
			autoLoop++;
		}

		if (autoLoop >= 300) {
			try {
				double[] defaultValues = new double[5];
				defaultValues[0] = 1;
				defaultValues[1] = 2;
				defaultValues[2] = 3;
				defaultValues[3] = 4;
				defaultValues[4] = 5;
				defaultValues[5] = 6;
				double[] centerx = contourReport.getNumberArray("centerx", defaultValues);
				double centerxavg = (centerx[0] + centerx[1]) / 2;
				double[] GRIPheight = contourReport.getNumberArray("height", defaultValues);
				double height = GRIPheight[0];
				if (centerxavg < 60) {
					moveRobotTurnRight(.4, .4);
				} else if (centerxavg >= 60 && centerxavg < 120) {
					moveRobotReverse(0);
				}
				if (height < 40) {
					moveRobotForward(.5);
				} else {
					moveRobotReverse(0);
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Array123 is out of Bounds" + e);
			}
		}
	}

	public void centerPegandStay() {
	}

	public void leftHopper() {
		if (autoLoop < 300) {
			moveRobotForward(.7);
		} else if (autoLoop >= 300) {
			moveRobotTurnLeft(.5, .5);
		} else {
			moveRobotForward(0);
		}
	}

	public void crossBaseline() {
		if (autoLoop < 300) {
			moveRobotForward(.7);
		} else {
			moveRobotForward(0);
		}
	}

	@Override
	public void teleopInit() {
		initializeGearAgitator();
	}

	private void initializeGearAgitator() {
		// TODO read gear sensors and initialize gear agitator, set sensor
		// flags.

	}

	@Override
	public void teleopPeriodic() {
		// Drivetrain
		double stickZ = stick.getRawAxis(2);
		double stickY = stick.getRawAxis(1);
		double Z2norm = stickZ * (NORMSPEED / 100.0);
		double y2norm = stickY * (NORMSPEED / 100.0) + Math.signum(stickY) * 0.05;

		myRobot.arcadeDrive(y2norm, Z2norm, true);

		/*
		 * if (currCompressor == compressoron) { if (stick.getRawButton(1)) {
		 * solenoidSpeedshift.set(false); // solenoid set "true" will push //
		 * piston in } else { solenoidSpeedshift.set(true); // solenoid set
		 * "true" will // retract piston } }
		 * 
		 * /* // Loading Station Gear Retrieval
		 * 
		 * // (teleop1) whale tail piston controls if (xbox.getRawButton(8)) {
		 * solenoidWhaleTailServo.set(true); } else if (xbox.getRawButton(9)) {
		 * solenoidWhaleTailServo.set(false); } else {
		 * solenoidWhaleTailServo.set(false); }
		 * 
		 * // (teleop2) gear box pivot if (xbox.getRawAxis(2) != 0) {
		 * gearRetrievalPivot.set(xbox.getRawAxis(2) / 2.5); } else {
		 * gearRetrievalPivot.set(0); }
		 * 
		 * // (teleop3) gear box agitator if ((isGearBarUp()) || (fl_gearBarUp))
		 * { fl_gearBarUp = true; gearRetrievalAgitator.set(.3); }
		 * 
		 * /* else if (!isGearInPosition()) { gearRetrievalAgitator.set(.3); }
		 * else { gearRetrievalAgitator.set(0); }
		 */

		// (teleop4) Floor Gear Retrieval
		/*
		 * if (xbox.getRawAxis(1) != 0) {
		 * floorRetrievalPivot.set(xbox.getRawAxis(1)); } else {
		 * floorRetrievalPivot.set(0); }
		 * 
		 * if (xbox.getRawButton(6)) { solenoidGearFloorRetriever.set(true);//
		 * arms closed } else if (xbox.getRawButton(7)) {
		 * solenoidGearFloorRetriever.set(false);// arms open } else { //
		 * default position: piston IN (arms open)
		 * solenoidGearFloorRetriever.set(false);
		 * 
		 * // need to add camera switch
		 * 
		 * // (teleop5) Hanger
		 * 
		 * }
		 */
		String alexisgreat = "hi kate :)";
	}

	@Override
	public void testPeriodic() {
	}
}