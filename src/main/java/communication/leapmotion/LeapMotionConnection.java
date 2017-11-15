package communication.leapmotion;

import com.leapmotion.leap.Controller;

import controller.HandController;

public class LeapMotionConnection implements Runnable {
	HandController handController;
	
	public LeapMotionConnection(HandController handController) {
		super();
		this.handController = handController;
	}

	public synchronized void run() {
		LeapMotionListener listener = new LeapMotionListener(this.handController);
		Controller controller = new Controller();
		controller.addListener(listener);	
		System.out.println("LeapMotionConnection started");
		try {
			this.wait();
		} catch (InterruptedException e) {
			controller.removeListener(listener);
		}
	}
	
	public void stop() {
		this.notify();
	}
}
