package communication.leapmotion;

import com.leapmotion.leap.Controller;

import controller.HandController;

public class LeapMotionConnection implements Runnable {
	HandController handController;
	
	public LeapMotionConnection(HandController handController) {
		super();
		this.handController = handController;
	}

	public void run() {
		LeapMotionListener listener = new LeapMotionListener(this.handController);
		Controller controller = new Controller();
		controller.addListener(listener);
		try {
			this.wait();
		} catch (InterruptedException e) {
			controller.removeListener(listener);
		}
		
	}
}
