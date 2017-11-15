package communication.serial;

import java.io.IOException;

import controller.HandController;
import gnu.io.SerialPortEvent;

public class HandConnection extends SerialConnection {
	protected HandController controller;
	protected boolean writeFlag; 	//set to false to block additional data until
									//the movement is performed
	public HandConnection(String portName, HandController controller) {
		super(portName);
		writeFlag = true;
		this.controller = controller;
	}
	
	
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				if(inputLine.equals("r")) {
					this.writeFlag=true;
					this.controller.notify();
				}
			} catch (Exception e) {
				System.out.println("Error receiving serial data");
				e.printStackTrace();
			}
		}
	}
	
	public void send(byte[] b) {
		try {
			this.output.write(b);
			this.output.flush();
			this.writeFlag=false;
		} catch (IOException e) {
			System.out.println("Serial send error");
			e.printStackTrace();
		}
	}
	
	public void send(SerialData data) {
		this.send(data.toString());
	}


	public synchronized boolean checkReady() {
		return writeFlag;
	}

}
