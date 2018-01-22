package communication.glove;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;

import controller.HandController;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class GloveConnection implements SerialPortEventListener, Runnable{
	
	HandController handController;
	
	SerialPort serialPort;
    /** The port we're normally going to use. */
	private static String portName = "COM7";
	/**
	 * A BufferedReader which will be fed by a InputStreamReader 
	 * converting the bytes into characters 
	 * making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	/** Number of fingers */
	private static final int JOINTS_NUM = 10;
	/** pinky NCP, PIP, ring NCP, PIP, middle NCP, PIP, index NCP, PIP, and tumb NCP, PIP finger*/
	private static final int[] fingerResistance = new int[JOINTS_NUM];
	/** max values of finger resistance */
	private static final int[] fingersMax = new int[JOINTS_NUM];
	/** min values of finger resistance */
	private static final int[] fingersMin = new int[JOINTS_NUM];
	/** accelerometer data */
	private static final int[] accData = new int[3];
	/** magnetometer data */
	private static final int[] magData = new int[3];
	/** reserved field */
	private static int reserved = 0;

	public GloveConnection(HandController controller, String portName) {
		this.handController=controller;
		if (portName!=null){
			this.portName=portName;
		}
		for (int i=0;i<JOINTS_NUM;i++){
			fingersMin[i]=Integer.MAX_VALUE;
			fingersMax[i]=0;
		}
	}
	
	public void initializeGlovePort() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
			}
			System.out.println("searching...");
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = new PrintStream(serialPort.getOutputStream(), true);
			output.write(("enable\r\n").getBytes());

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		String currentLine = null;
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				currentLine=input.readLine();
				if (currentLine.length()==0) return;
				parseLine(currentLine);
				
				/***
				System.out.println("===========================");
				System.out.println(currentLine);
				for (int value: fingerResistance) System.out.print(value+" ");
				System.out.println("");
				for (int value: accData) System.out.print(value+" ");
				System.out.println("");
				for (int value: magData) System.out.print(value+" ");
				System.out.println("");
				System.out.println("Reserved= "+reserved);
				System.out.println("============================");
				*/
				
				GloveData gloveData = new GloveData(fingerResistance, magData, accData, fingersMax, fingersMin);
				System.out.println(gloveData.getSerialData().toString());
				handController.receiveData(gloveData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	private void  parseLine(String line){
		// skip "DATA[" at the begining and "]" at the end
		line = line.substring(5,line.length()-1);
		// get each input in form index:value
		String[] parts = line.split("\\]\\[");
		if (parts.length!=17){
			System.out.println("Not enough parts!");
			return;
		}
		
		for(String part: parts){
			String [] inputs = part.split(":");
			int index=0;
			int value=0;
			try {
				index = Integer.parseInt(inputs[0]);
				value = Integer.parseInt(inputs[1]);
			} catch (NumberFormatException e) {
				continue;
			}
			
			if (index<10 && index>=0){
				if (value>fingersMax[index]) fingersMax[index]=value;
				if (value<fingersMin[index]) fingersMin[index]=value;
				fingerResistance[index] = value;
			} else if (index<13) {
				accData[index-10] = value;
			} else if (index<16) {
				magData[index-13] = value;
			} else if (index==16) {
				reserved = value;
			} else {
				throw new IndexOutOfBoundsException("Index of input from board is out of bounds. index= "+index);
			}
		}
	}
	
	public static int getReserved() {
		return reserved;
	}
	
	public static int[] getAccData() {
		return accData;
	}
	
	public static int[] getMagData() {
		return magData;
	}
	
	public static int[] getFingerResistance() {
		return fingerResistance;
	}

	public void run() {
		
	}
}
