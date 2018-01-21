package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import communication.InputData;
import communication.glove.GloveConnection;
import communication.leapmotion.LeapMotionConnection;
import communication.serial.HandConnection;
import communication.serial.SerialData;

public class HandController implements Runnable {
	private float tolerance;
	private ArrayBlockingQueue<InputData> dataInput;
	private HandConnection serial;
	private Runnable inputConnection;
	private GloveConnection glove;
	private boolean close;
	public Object semafor;

	public HandController() {
		this.tolerance = 0.15f;
		dataInput = new ArrayBlockingQueue<InputData>(50);
		inputConnection = new LeapMotionConnection(this);
		close = false;
		semafor = new Object();
		serial = new HandConnection("COM7", this);
	}
	
	public HandController(Map<String, String> config) {
		tolerance = Float.valueOf(config.get("tolerance"));
		dataInput = new ArrayBlockingQueue<InputData>(50);
		inputConnection = new LeapMotionConnection(this);
		if(config.get("input").equals("leapmotion"))
			inputConnection = new LeapMotionConnection(this);
		else if(config.get("input").equals("glove"))
			inputConnection = new GloveConnection(this, config.get("glovePort"));
		close = false;
		semafor = new Object();
		serial = new HandConnection(config.get("port"), this);
	}

	public synchronized float getTolerance() {
		return this.tolerance;
	}

	public synchronized void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

	public void receiveData(InputData data) {
		synchronized (semafor) {
			try {
				dataInput.offer(data, 50, TimeUnit.MILLISECONDS);
				semafor.notify();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		Thread input = new Thread(inputConnection);
		input.start();
		while (!close) {
			if (!dataInput.isEmpty()) {
				System.out.println(dataInput.peek());
				if (serial.checkReady()) {
					try {
						
						SerialData data = dataInput.take().getSerialData();
						System.out.println(data.toString());
						serial.send(data);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			synchronized (semafor) {
				try {
					semafor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		HashMap<String, String> config = new HashMap<>();
		
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-leapmotion":
				config.put("input", "leapmotion");
				break;
			case "-t":
				try {
					String t = args[i+1];
					config.put("tolerance", t);
					i++;
				}catch(IndexOutOfBoundsException indexEx) {
					System.out.println("Missing tolerance number");
					config.put("tolerance", "0.1");
				}
				break;
			case "-glove":
				config.put("input", "glove");
				try {
					String t = args[i+1];
					config.put("glovePort", t);
					i++;
				}catch(IndexOutOfBoundsException indexEx) {
					System.out.println("Missing glove port identifier");
					config.put("glovePort", "6");
				}
				break;
			case "-c":
				try {
					String c = "COM" + args[i+1];
					config.put("port", c);
					i++;
				}catch(IndexOutOfBoundsException indexEx) {
					System.out.println("Missing port identifier");

					config.put("port", "COM7");
				}
				break;
			default:
			}
		}
		HandController controller = new HandController(config);
		controller.run();
	}
}
