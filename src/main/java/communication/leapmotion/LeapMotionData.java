package communication.leapmotion;

import java.util.ArrayList;
import java.util.HashMap;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Matrix;
import com.leapmotion.leap.Vector;

import communication.InputData;
import communication.serial.SerialData;

public class LeapMotionData implements InputData {
	public ArrayList<Integer> baseAngle, tipAngle;
	private SerialData serialData;

	public static double[][] tipCoefs = { { 0.0004, -0.0377, 2.2852, 1.9508 }, { -0.0005, 0.0629, -3.7834, 155.8975 },
			{ 0.0006, -0.0650, 3.2423, 0.4715 }, { -0.0006, 0.0913, -5.0934, 148.0786 },
			{ 0.0001, -0.0288, 2.9080, -5.3920 } };
	public static double[][] baseCoefs = { { -0.0000, 0.0100, -2.3800, 181.9148 }, { 0.0001, -0.0195, 2.4260, -1.9763 },
			{ -0.0001, 0.0299, -3.3889, 184.2292 }, { -0.0000, -0.0057, 2.3539, 3.1089 },
			{ 0.0015, -0.1523, 4.7852, -4.1423 } };

	public LeapMotionData(Hand hand) {
		Finger finger;
		this.baseAngle = new ArrayList<Integer>(5);
		this.tipAngle = new ArrayList<Integer>(5);
		HashMap<Integer, Integer> baseMotorPosition = new HashMap<>(5);
		HashMap<Integer, Integer> tipMotorPosition = new HashMap<>(5);

		for (int i = 0; i < 5; i++) {
			finger = hand.fingers().get(4 - i);
			Vector base = finger.bone(Bone.Type.TYPE_PROXIMAL).direction();
			Vector dynamic = finger.bone(Bone.Type.TYPE_METACARPAL).direction();
			baseAngle.add(radiansToDegrees(dynamic.angleTo(base)));

			int angle = baseAngle.get(i).intValue();

			angle = (int) (baseCoefs[i][0] * Math.pow(angle, 3) + baseCoefs[i][1] * Math.pow(angle, 2)
					+ baseCoefs[i][2] * angle + baseCoefs[i][3]);

			if (angle > 180)
				angle = 180;
			if (angle < 0)
				angle = 0;
			baseMotorPosition.put(new Integer(i), new Integer(angle));

			dynamic = finger.bone(Bone.Type.TYPE_INTERMEDIATE).direction();
			tipAngle.add(radiansToDegrees(base.angleTo(dynamic)));

			angle = tipAngle.get(i).intValue();

			angle = (int) (tipCoefs[i][0] * Math.pow(angle, 3) + tipCoefs[i][1] * Math.pow(angle, 2)
					+ tipCoefs[i][2] * angle + tipCoefs[i][3]);

			if (angle > 180)
				angle = 180;
			if (angle < 0)
				angle = 0;
			tipMotorPosition.put(new Integer(i), new Integer(angle));
		}
		this.serialData = new SerialData(baseMotorPosition, tipMotorPosition);

	}

	public LeapMotionData() {
		baseAngle = null;
		this.serialData = new SerialData();
	}

	public static Integer radiansToDegrees(float radians) {
		int deg = (int) Math.toDegrees(radians);
		if (deg >= 0 && deg <= 180)
			return new Integer(deg);
		else if (deg > 180)
			return new Integer(deg - 180);
		else if (deg < 0 && deg > -180)
			return new Integer(-deg);
		else if (deg < -180)
			return new Integer(-deg - 180);
		System.out.println("Angle issue" + deg);
		return null;
	}

	/*
	 * If there is a difference between this data and the newData exceeding the
	 * tolerance in any parameter returns true
	 */
	public boolean checkForDifference(LeapMotionData newData, float tolerance) {
		if (baseAngle == null)
			return true;
		boolean flag = false;
		int i = 0;
		for (Integer angle : baseAngle) {
			if (Math.abs((float) newData.baseAngle.get(i++) - angle) / angle > tolerance)
				flag = true;
		}
		for (Integer angle : tipAngle) {
			if (Math.abs((float) newData.tipAngle.get(i++) - angle) / angle > tolerance)
				flag = true;
		}
		return flag;
	}

	public static void main(String[] args) {
		for (Finger.Type t : Finger.Type.values()) {
			System.out.println(t);
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < 5; i++) {
			s = s + "Index" + i + " Base " + baseAngle.get(i) + " Tip " + tipAngle.get(i) + " ";
		}
		return s;
	}

	public SerialData getSerialData() {
		return this.serialData;
	}

}
