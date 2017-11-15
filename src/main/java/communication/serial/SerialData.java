package communication.serial;

import java.util.HashMap;

import communication.glove.GloveData;
import communication.leapmotion.LeapMotionData;

public class SerialData {
	HashMap<Integer, Integer> baseMotorPosition, tipMotorPosition;
	
	public SerialData() {
		baseMotorPosition = new HashMap<Integer, Integer>(5);
		tipMotorPosition = new HashMap<Integer, Integer>(5);
		for(int i = 0; i<5; i++) {
			baseMotorPosition.put(i, 0);
			tipMotorPosition.put(i, 0);
		}
	}
	
	public SerialData(LeapMotionData data) {
		baseMotorPosition = new HashMap<Integer, Integer>(5);
		tipMotorPosition = new HashMap<Integer, Integer>(5);
		// TODO Auto-generated constructor stub
	}
	
	public SerialData(GloveData data) {
		baseMotorPosition = new HashMap<Integer, Integer>(5);
		tipMotorPosition = new HashMap<Integer, Integer>(5);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		String data = "";
		for(int i = 0; i<5; i++) {
			if(baseMotorPosition.get(i)!=null && tipMotorPosition.get(i)!=null) {
				data += "i" + i + "b" + baseMotorPosition.get(i).toString() + "t" + tipMotorPosition.get(i).toString();
			}
		}
		return data + ";";
	}
}
