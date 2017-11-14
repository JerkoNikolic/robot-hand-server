package communication.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public abstract class SerialConnection implements SerialPortEventListener{
	protected SerialPort serialPort;
	protected BufferedReader input;
	protected OutputStream output;
	protected static final int TIME_OUT = 2000;
	protected static final int DATA_RATE = 9600;
	
	public SerialConnection(String portName) {
		CommPortIdentifier portId = null;
		try {
			portId = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e) {
			System.out.println("Could not find COM port.");
			e.printStackTrace();
			return;
		}
		
		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public abstract void serialEvent(SerialPortEvent event);
	
	public abstract void send(byte[] b);
	
	public void send(String s) {
		this.send(s.getBytes(StandardCharsets.UTF_8));
	}

}
