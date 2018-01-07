package communication.glove;

import java.util.HashMap;

import communication.InputData;
import communication.serial.SerialData;

public class GloveData implements InputData{
	
	private int[] fingers;
	private int[] fingersMax;
	private int[] fingersMin;
	private int[] mag;
	private int[] acc;
	private SerialData serialData;
	
	public GloveData(int[] fingers, int[] mag, int[] acc, int[] fingersMax, int[] fingersMin) {
		this.fingers=fingers;
		this.mag=mag;
		this.acc=acc;
		this.fingersMax=fingersMax;
		this.fingersMin=fingersMin;
		processData();
	}

	private void processData() {
		HashMap<Integer, Integer> baseMotorsPos = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> tipMotorsPos = new HashMap<Integer, Integer>();
		for (int i=0;i<10;i+=2){
			int angle = fingers[i]/(fingersMax[i]-fingersMin[i])*90;
			baseMotorsPos.put(i, angle);
		}
		for (int i=1;i<10;i+=2){
			int angle = fingers[i]/(fingersMax[i]-fingersMin[i])*90;
			tipMotorsPos.put(i, angle);
		}
		this.serialData=new SerialData(baseMotorsPos, tipMotorsPos);
	}

	public SerialData getSerialData() {
		return serialData;
	}

}
