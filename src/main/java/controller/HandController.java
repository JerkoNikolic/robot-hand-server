package controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import communication.InputData;
import communication.glove.GloveConnection;
import communication.leapmotion.LeapMotionConnection;
import communication.leapmotion.LeapMotionData;
import communication.serial.HandConnection;
import communication.serial.SerialConnection;
import communication.serial.SerialData;

public class HandController implements Runnable{
	private float tolerance;
	private ArrayBlockingQueue<InputData> dataInput;
	private HandConnection serial;
	private LeapMotionConnection leapMotion;
	private GloveConnection glove;
	private boolean close;
	private Object semafor;
	
	public HandController() {
		this.tolerance = 0.15f;
		dataInput = new ArrayBlockingQueue<InputData>(50);
		leapMotion = new LeapMotionConnection(this);
		close = false;
		semafor = new Object();
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
		Thread leap = new Thread(leapMotion);
		leap.start();
		while(!close) {
			if(!dataInput.isEmpty()) {
				if(true/*serial.checkReady()*/) {
					try {
						System.out.println(dataInput.take());
//						SerialData data = dataInput.take().getSerialData();
//						serial.send(data);
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
		leapMotion.stop();
	}
	
	public static void main(String[] args) {
		HandController controller = new HandController();
		controller.run();
	}
}
