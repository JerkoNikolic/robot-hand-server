package controller;

public class HandController {
	private float tolerance;
	
	public HandController() {
		this.tolerance = 0.05f;
	}
	
	public synchronized float getTolerance() {
		return this.tolerance;
	}
	
	 public synchronized void setTolerance(float tolerance) {
		 this.tolerance = tolerance;
	 }

	public synchronized void receiveData() {
		
	}
}
