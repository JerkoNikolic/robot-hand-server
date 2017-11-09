package communication.leapmotion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

import controller.HandController;

public class LeapMotionListener extends Listener {
	private HandController handController;
	private LeapMotionData handData;
	
	public LeapMotionListener(HandController handController) {
		super();
		this.handController = handController;
		handData = new LeapMotionData();
	}
	
	public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {
    	Frame frame = controller.frame();
    	if(!frame.hands().isEmpty()) {
    		LeapMotionData newData = new LeapMotionData(frame.hands().get(0));
    		if(handData.checkForDifference(newData, handController.getTolerance())) {
    			handController.receiveData();
    		}
    	}
    }
}
