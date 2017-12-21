package communication.leapmotion;

import java.util.ArrayList;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Matrix;
import com.leapmotion.leap.Vector;

import communication.InputData;
import communication.serial.SerialData;

public class LeapMotionData implements InputData {
	public ArrayList<Vector> fingerPosition, fingerDirection;

	public LeapMotionData(Hand hand) {
		Matrix transform = new Matrix(
				hand.palmNormal().cross(hand.direction().normalized()),
				hand.palmNormal().opposite(),
				hand.direction().opposite(),
				hand.palmPosition()
				).rigidInverse();
		Finger finger;
		fingerPosition = new ArrayList<Vector>(5);
		fingerDirection = new ArrayList<Vector>(5);
		for(Finger.Type t:Finger.Type.values()) {
			finger = hand.fingers().fingerType(t).get(0);
			fingerPosition.add(transform.transformPoint(finger.tipPosition()));
			fingerDirection.add(transform.transformDirection(finger.direction()));
		}
	}

	public LeapMotionData() {
		fingerPosition=null;
	}
	
/*
 * If there is a difference between this data and the newData exceeding the tolerance in any parameter returns true
 */
	public boolean checkForDifference(LeapMotionData newData, float tolerance) {
		//TODO
		if(fingerPosition==null)
			return true;
		boolean flag = false;
		int i=0;
		for(Vector v: fingerPosition) {
			if(v.distanceTo(newData.fingerPosition.get(i++))/v.magnitude()>tolerance)
				flag = true;
		}
		return flag;
	}
	
	public static void main(String[] args) {
		for(Finger.Type t : Finger.Type.values()) {
			System.out.println(t);
		}
	}
	
	@Override
	public String toString() {
		String s="";
		for(Vector v: fingerPosition) {
			s+=fingerPosition.indexOf(v) + ": " +v.getX() + " " + v.getY() + " " + v.getZ() + "\n";
		}
		return s;
	}

	public SerialData getSerialData() {
		// TODO Auto-generated method stub
		return null;
	}

}
