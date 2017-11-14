package controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import communication.glove.GloveConnection;
import communication.leapmotion.LeapMotionConnection;
import communication.leapmotion.LeapMotionData;
import communication.serial.HandConnection;
import communication.serial.SerialConnection;

public class HandController implements Runnable{
	private float tolerance;
	private ArrayBlockingQueue<LeapMotionData> leapMotionInput;
	private HandConnection serial;
	private LeapMotionConnection leapMotion;
	private GloveConnection glove;
	
	public HandController() {
		this.tolerance = 0.05f;
		leapMotionInput = new ArrayBlockingQueue<LeapMotionData>(50);
		leapMotion = new LeapMotionConnection(this);
	}
	
	public synchronized float getTolerance() {
		return this.tolerance;
	}
	
	 public synchronized void setTolerance(float tolerance) {
		 this.tolerance = tolerance;
	 }

	public synchronized void receiveData(LeapMotionData data) {
		try {
			leapMotionInput.offer(data, 50, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		
		while(true) {
			if(!leapMotionInput.isEmpty()) {
				if(serial.checkReady()) {
					serial.send(leapMotionInput.take());
				}
			}
			
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		leapMotion.notify();
	}
}
