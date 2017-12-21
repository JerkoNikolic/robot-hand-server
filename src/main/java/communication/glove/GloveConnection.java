package communication.glove;

import controller.HandController;

public class GloveConnection implements Runnable{
	HandController handController;
	
	
	public GloveConnection(HandController handController) {
		super();
		this.handController = handController;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		this.notify();
	}
}
