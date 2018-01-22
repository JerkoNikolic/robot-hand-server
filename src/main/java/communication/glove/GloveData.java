package communication.glove;

import java.util.HashMap;

import communication.InputData;
import communication.serial.SerialData;

public class GloveData implements InputData{
	public static double[][] coefs = {
			{-0.0000, 0.0100  , -2.3800,  181.9148},
			{0.0004  , -0.0377 ,   2.2852 ,   1.9508},
			{0.0001   ,-0.0195  ,  2.4260  , -1.9763},
			{-0.0005   , 0.0629  , -3.7834  ,155.8975},
			{-0.0001  ,  0.0299  , -3.3889,  184.2292},
			{0.0006  , -0.0650   , 3.2423 ,   0.4715},
			{-0.0000  , -0.0057  ,  2.3539 ,   3.1089},
			{-0.0006   , 0.0913  , -5.0934  ,148.0786},
			{0.0015  , -0.1523   , 4.7852  , -4.1423},
			{0.0001,-0.0288,2.9080,-5.3920}
	};
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
			
			baseMotorsPos.put(i, (int) (coefs[i][0]*Math.pow(angle, 3)+
					coefs[i][1]*Math.pow(angle, 2)+
					coefs[i][2]*angle+
					coefs[i][3]));
		}
		for (int i=1;i<10;i+=2){
			int angle = fingers[i]/(fingersMax[i]-fingersMin[i])*90;
			tipMotorsPos.put(i, (int) (coefs[i][0]*Math.pow(angle, 3)+
					coefs[i][1]*Math.pow(angle, 2)+
					coefs[i][2]*angle+
					coefs[i][3]));
		}
		this.serialData=new SerialData(baseMotorsPos, tipMotorsPos);
	}

	public SerialData getSerialData() {
		return serialData;
	}

}
