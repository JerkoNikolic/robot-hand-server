package communication.serial;

import java.util.HashMap;

public class SerialData {
	HashMap<Integer, Integer> baseMotorPosition, tipMotorPosition;
	
	public SerialData() {
		baseMotorPosition = new HashMap<Integer, Integer>(5);
		tipMotorPosition = new HashMap<Integer, Integer>(5);
		for(int i = 0; i<5; i++) {
			baseMotorPosition.put(i, 65);
			tipMotorPosition.put(i, 65);
		}
	}
	
	public SerialData(HashMap<Integer, Integer> baseMotorPosition, HashMap<Integer, Integer> tipMotorPosition) {
		this.baseMotorPosition = baseMotorPosition;
		this.tipMotorPosition = tipMotorPosition;
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
	
	public byte[] toBytes() {
		byte[] data = new byte[12];
		data[0]=(byte)255;
		for(int i=0, j=1; i<5; i++) {
			data[j++]=(byte)((int)this.tipMotorPosition.get(i));
			data[j++]=(byte)((int)this.baseMotorPosition.get(i));
		}
		data[11]=(byte) 200;
		return data;
	}
}
