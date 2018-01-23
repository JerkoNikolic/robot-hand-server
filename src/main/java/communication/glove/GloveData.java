package communication.glove;

import java.util.HashMap;

import communication.InputData;
import communication.serial.SerialData;

public class GloveData implements InputData {
	public static double[][] coefs = { { -0.0000, 0.0100, -2.3800, 181.9148 }, { 0.0004, -0.0377, 2.2852, 1.9508 },
			{ 0.0001, -0.0195, 2.4260, -1.9763 }, { -0.0005, 0.0629, -3.7834, 155.8975 },
			{ -0.0001, 0.0299, -3.3889, 184.2292 }, { 0.0006, -0.0650, 3.2423, 0.4715 },
			{ -0.0000, -0.0057, 2.3539, 3.1089 }, { -0.0006, 0.0913, -5.0934, 148.0786 },
			{ 0.0015, -0.1523, 4.7852, -4.1423 }, { 0.0001, -0.0288, 2.9080, -5.3920 } };
	private int[] fingers;
	private int[] fingersMax;
	private int[] fingersMin;
	private int[] mag;
	private int[] acc;
	private SerialData serialData;

	public GloveData(int[] fingers, int[] mag, int[] acc, int[] fingersMax, int[] fingersMin) {
		this.fingers = fingers;
		this.mag = mag;
		this.acc = acc;
		this.fingersMax = fingersMax;
		this.fingersMin = fingersMin;
		processData();
	}

	private void processData() {
		HashMap<Integer, Integer> baseMotorsPos = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> tipMotorsPos = new HashMap<Integer, Integer>();
		for (int i = 0; i < 5; i += 1) {
			double angle = (fingers[2 * i] * 1.0) / (fingersMax[2 * i] - fingersMin[2 * i]) * 90.0;
			// System.out.println(i+". Kut"+angle);
			angle = coefs[2 * i][0] * Math.pow(angle, 3) + coefs[2 * i][1] * Math.pow(angle, 2)
					+ coefs[2 * i][2] * angle + coefs[2 * i][3];
			if (angle > 180)
				angle = 180;
			if (angle < 0)
				angle = 0;
			// System.out.println("Servo kut:"+angle);
			baseMotorsPos.put(i, (int) angle);
		}
		for (int i = 0; i < 5; i += 1) {
			double angle = (fingers[2 * i + 1] * 1.0) / (fingersMax[2 * i + 1] - fingersMin[2 * i] + 1) * 90.0;
			// System.out.println(i+". Kut:"+angle);
			angle = coefs[2 * i + 1][0] * Math.pow(angle, 3) + coefs[2 * i + 1][1] * Math.pow(angle, 2)
					+ coefs[2 * i + 1][2] * angle + coefs[2 * i + 1][3];
			if (angle > 180)
				angle = 180;
			if (angle < 0)
				angle = 0;
			// System.out.println("Servo kut:"+angle);
			tipMotorsPos.put(i, (int) angle);
		}
		this.serialData = new SerialData(baseMotorsPos, tipMotorsPos);
	}

	public SerialData getSerialData() {
		return serialData;
	}

}