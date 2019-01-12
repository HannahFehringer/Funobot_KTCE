package main;

import ch.aplu.ev3.*;

public class Driver extends LegoRobot {
	protected Motor motorA;
	protected Motor motorB;
	protected MediumMotor rotator;

	public static void print(String str) {
		DebugConsole.show(str);
	}

	public Driver(MotorPort a, MotorPort b, MotorPort rot) {
		super();
		motorA = new Motor(a);
		motorB = new Motor(b);
		rotator = new MediumMotor(rot);
		addPart(motorA);
		addPart(motorB);
		addPart(rotator);
		rotator.setSpeed(255);
		motorA.setSpeed(speed);
		motorB.setSpeed(speed);
		motorA.setAcceleration(acceleration);
		motorB.setAcceleration(acceleration);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		motorB.setSpeed(speed);
		motorA.setSpeed(speed);
	}
	public void turnLeft(int speed) {
		this.speed = speed;
		motorA.setSpeed(speed);
		motorB.setSpeed(0);
	}
	
	public void turnRight(int speed) {
		this.speed = speed;
		motorA.setSpeed(0);
		motorB.setSpeed(speed);
	}

	public static int MOVE_DIRECTION_FORWARD = 0;
	public static int MOVE_DIRECTION_BACKWARD = 1;
	public static int MOVE_DIRECTION_STOP = 2;
	private int direction = MOVE_DIRECTION_STOP;
	int acceleration = 10;
	int speed = 100;
	int rotation = 0;

	public void setAcceleration(int acc) {
		acceleration = acc;
		motorA.setAcceleration(acc);
		motorB.setAcceleration(acc);
	}

	public void setDirection(int direction) {
		assert (direction >= 0 && direction <= 2);
		if (direction == MOVE_DIRECTION_BACKWARD) {
			setAcceleration(acceleration);
			System.out.println("yeah forward");
			motorA.forward();
			motorB.forward();
		} 
		else if (direction == MOVE_DIRECTION_FORWARD) {
			setAcceleration(acceleration);
			motorA.backward();
			motorB.backward();
		} else if (direction == MOVE_DIRECTION_STOP) {
			motorA.setAcceleration(255);
			motorB.setAcceleration(255);
			motorA.stop();
			motorB.stop();
		}
	}

	public MediumMotor getRotator() {
		return rotator;
	}

	public void setRotation(int deg) {
		int difference = deg - rotation;
		rotation = deg;
		rotator.rotateTo(difference);
	}

	public Motor getMotor(int type) {
		if (type == 1) {
			return motorA;
		} else {
			return motorB;
		}
	}
}