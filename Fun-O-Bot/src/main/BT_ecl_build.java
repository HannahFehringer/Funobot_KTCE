package main;

import ch.aplu.ev3.*;
import lejos.hardware.Sound;
import java.io.*;
import java.util.Random;

import lejos.remote.nxt.*;
import lejos.hardware.*;

public class BT_ecl_build {
	/* Robot */
	private MotorPort defaultMotorPort1 = MotorPort.A;
	private MotorPort defaultMotorPort2 = MotorPort.D;
	private MotorPort defaultRotationPort = MotorPort.C;
	private Driver funobot = new Driver(defaultMotorPort1, defaultMotorPort2, defaultRotationPort);

	/* BlueTooth stuff */
	private BluetoothConnection2 con;
	private BluetoothConnector2 bt;
	private BufferedReader reader;

	/* Sound thread */
	private Thread t;

	/* Direction of car (forward/stop/backwards) */
	private int move_direction = Driver.MOVE_DIRECTION_STOP;
	/* Rotation of car */
	private int rotation = 0;
	/* Speed of car */
	private int speed = 0;
	private int previous_speed = 0;
	/* Is car powered (User choice) */
	private boolean power = true;
	
	/* User connected ? */
	private boolean initiated = false;
	

	/* Tells if beeping is enabled */
	private boolean beep = false;

	/* "Warning" last play time in time-since-epoch */
	private long warning_last = 0L;
	/* "Warning" cooldown */
	private static final long WARNING_COOLDOWN = 1800L; // ms
	boolean wantsToExit = false;
	
	BT_ecl_build() {
		this.init();
	}
	public void onExit()
	{
		funobot.setRotation(rotation = 0);
		funobot.setSpeed(speed = 0);
		funobot.setDirection(move_direction = Driver.MOVE_DIRECTION_STOP);
		funobot.exit();
	}

	
	void initRobot() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						if (beep) {
							Sound.playTone(800, 500, 10);
							Thread.sleep(400);
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						break;
					}
				}
			}

		});
		t.start();
		funobot.setAcceleration(255);
	}

	void sendMessage(String msg) {
		if (con != null) {
			msg += "\r\n";
			con.write(msg.getBytes(), msg.length());
		}
	}

	void handleMessage(String msg) {
		String packetHeader = msg.trim();
		try {

			if (packetHeader.startsWith("O")) {
				power = Integer.parseInt(packetHeader.substring(1)) != 0 ? true : false;
				if (power) {
					funobot.setSpeed(speed);
					funobot.setDirection(move_direction);
					if (move_direction == Driver.MOVE_DIRECTION_FORWARD) {
						startBeeping();
					}
				} else {
					funobot.setDirection(Driver.MOVE_DIRECTION_STOP);
					if (move_direction == Driver.MOVE_DIRECTION_FORWARD) {
						stopBeeping();
					}
				}
			} else if (packetHeader.startsWith("L")) {
				speed = Integer.parseInt(packetHeader.substring(1));

				if (speed < 0) {
					move_direction = Driver.MOVE_DIRECTION_FORWARD;
				} else if (speed > 0) {
					move_direction = Driver.MOVE_DIRECTION_BACKWARD;
				} else if (speed == 0) {
					// if(move_direction !=
					move_direction = Driver.MOVE_DIRECTION_STOP;
				} // end of if-else

				if (power) {
					funobot.turnLeft(speed);
					funobot.setDirection(move_direction);
					if (previous_speed < 0 && speed >= 0) {
						// Meaning it was beeping
						stopBeeping();
					} else if (speed < 0 && previous_speed >= 0) {
						startBeeping();
					}
					previous_speed = speed;
				}
			} // end of if-else
		 else if (packetHeader.startsWith("R")) {
			speed = Integer.parseInt(packetHeader.substring(1));

			if (speed < 0) {
				move_direction = Driver.MOVE_DIRECTION_FORWARD;
			} else if (speed > 0) {
				move_direction = Driver.MOVE_DIRECTION_BACKWARD;
			} else if (speed == 0) {
				// if(move_direction !=
				move_direction = Driver.MOVE_DIRECTION_STOP;
			} // end of if-else

			if (power) {
				funobot.turnRight(speed);
				funobot.setDirection(move_direction);
				if (previous_speed < 0 && speed >= 0) {
					// Meaning it was beeping
					stopBeeping();
				} else if (speed < 0 && previous_speed >= 0) {
					startBeeping();
				}
				previous_speed = speed;
			}
		} // end of if-else
			else if (packetHeader.startsWith("M")) {
				speed = Integer.parseInt(packetHeader.substring(1));

				if (speed < 0) {
					move_direction = Driver.MOVE_DIRECTION_FORWARD;
				} else if (speed > 0) {
					move_direction = Driver.MOVE_DIRECTION_BACKWARD;
				} else if (speed == 0) {
					// if(move_direction !=
					move_direction = Driver.MOVE_DIRECTION_STOP;
				} // end of if-else

				if (power) {
					if (speed == 0) {
						funobot.getMotor(1).stop(true);
						funobot.getMotor(0).stop(true);
						stopBeeping();
						
					} else {
						
					funobot.setSpeed(speed);
					funobot.setDirection(move_direction);
					if (previous_speed < 0 && speed >= 0) {
						// Meaning it was beeping
						stopBeeping();
					} else if (speed < 0 && previous_speed >= 0) {
						startBeeping();
					}
					previous_speed = speed;
				}}

			} // end of if-else
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String error = errors.toString();
		}
	}

	void initBTConnector() {
		bt = new BluetoothConnector2();
	}
	

	void createConnection() {
		con = (BluetoothConnection2) bt.waitForConnection(10000, NXTConnection.RAW);
		reader = new BufferedReader(new InputStreamReader(con.openInputStream()), 1);
		initiated = true;
	}

	void stop() {
		funobot.getMotor(1).setAcceleration(255);
		funobot.getMotor(2).setAcceleration(255);
		funobot.getMotor(1).stop();
		funobot.getMotor(2).stop();
	}

	void go() {
		funobot.getMotor(1).setAcceleration(50);
		funobot.getMotor(2).setAcceleration(50);
		funobot.getMotor(1).forward();
		funobot.getMotor(2).forward();
	}

	boolean listen() {
		boolean succ = true;
		String message = "";

		try {
			while ((message = reader.readLine()) != null) {
				DebugConsole.show(message);
				handleMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		if (con.eof) {
			succ = false;
		}
		if (wantsToExit)
			succ = false;
		return succ;
	}

	void startBeeping() {
		beep = true;
	}

	void stopBeeping() {
		beep = false;
	}
	void reset()
	{
		funobot.setSpeed(this.speed = 0);
		funobot.setDirection(this.move_direction = Driver.MOVE_DIRECTION_STOP);
		funobot.setRotation(this.rotation = 0);
		this.power = true;
	}
	
	void disconnect()
	{
		try {
			initiated = false;
			con.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error closing socket: " + e.getMessage());
		}
	}
	
	void init() {
		DebugConsole.show("Initiating FUN-O-BOT...");
		initRobot();
		

		DebugConsole.show("Starting Connector, waiting for Bluetooth Client...");
		initBTConnector();
		
		while (wantsToExit == false) {
			createConnection();

			DebugConsole.show("Connection found! ");

			DebugConsole.show("We are now going to listen...");
			while (listen());
			if (!wantsToExit)
			{
				DebugConsole.show("Connection lost, waiting for next Client");
			}
			reset();
			disconnect();
			wantsToExit = true; // 1 round yet because of internal Linux C-Library binding error
		}
		DebugConsole.show("Leaving. Goodbye");
		onExit();
		Tools.delay(3000); //"Standby..." wav
	}

	public static void main(String[] args) {
		BT_ecl_build mainrun = new BT_ecl_build();
		System.exit(0);
	}
}