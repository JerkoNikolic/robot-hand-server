package communication.serial;

import java.io.IOException;

import gnu.io.SerialPortEvent;

public class HandConnection extends SerialConnection {
	protected boolean writeFlag; 	//set to false to block additional data until
									//the movement is performed
	public HandConnection(String portName) {
		super(portName);
		writeFlag = true;
	}
	
	
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				if(inputLine.equals("r")) {
					this.writeFlag=true;
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


	public synchronized boolean checkReady() {
		return writeFlag;
	}

}
