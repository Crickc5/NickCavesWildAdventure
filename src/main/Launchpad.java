package main;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class Launchpad{
	
	private MidiDevice input;
	private MidiDevice output;
	private Receiver rcvr = new MyReceiver();
	private Receiver trans;
	public int status = 0;
	private ArrayList<MidiMessage> buffer = new ArrayList<MidiMessage>();
	
	
	public void setPad(int xPos, int yPos, int redBrightness, int greenBrightness, int flashing){
		ShortMessage msg = new ShortMessage();
		int status = 144;
		int data1 = (16 * yPos) + xPos;
		int data2 = 0;
		data2 = data2 | redBrightness;
		data2 = data2 | (greenBrightness << 4);
		data2 = data2 | (flashing << 2);
		data2 = data2 ^ (1 << 2);
		data2 = data2 | (1 << 3);
		System.out.println(data1 + " " + data2);
		try {
			msg.setMessage(status, data1, data2);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trans.send(msg, -1);
		
	}
	
	public void setRed(int xPos, int yPos, int flashing){
		setPad(xPos, yPos, 3, 0, flashing);
	}
	
	public void setGreen(int xPos, int yPos, int flashing){
		setPad(xPos, yPos, 0, 3, flashing);
	}
	
	public void setGreenLow(int xPos, int yPos, int flashing){
		setPad(xPos, yPos, 0, 1, flashing);
	}
	
	public void setAmber(int xPos, int yPos, int flashing){
		setPad(xPos, yPos, 3, 2, flashing);
	}
	
	public void setOff(int xPos, int yPos){
		setPad(xPos, yPos, 0, 0, 0);
	}
	
	public void drawHorizLine(int yPos){
		for (int i = 0; i < 8; i++){
			setGreen(i, yPos, 0);
		}
	}
	
	public void drawVertLine(int xPos){
		for (int i = 0; i < 8; i++){
			setGreen(xPos, i, 0);
		}
	}
	
	public void checkInputs() throws InvalidMidiDataException{
		//System.out.println(buffer.size());
		if (buffer.size() > 0){
			System.out.println("Processing...");
			for (int i = buffer.size() - 1; i >= 0; i--){
				byte[] msg = buffer.get(i).getMessage();
				if (msg[2] == 127){
					ShortMessage msgOut = new ShortMessage();
					msgOut.setMessage(-112, msg[1], 60);
					trans.send(msgOut, -1);
					
					if (msg[1] == 7){
						status = 1;
						System.out.println("Should be ending the program now");
					}
				} else if (msg[2] == 0){
					ShortMessage msgOut = new ShortMessage();
					msgOut.setMessage(-112, msg[1], 12);
					trans.send(msgOut, -1);
					
				}
				buffer.remove(i);
			}
		}
		
	}
	
	public void testLights(){
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(176, 0, 127);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trans.send(msg, -1);
	}
	
	public void clearPad(){
		resetPad();
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(0xB0, 0x00, 0x28);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resetPad(){
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(176, 0, 0);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trans.send(msg, -1);
		
	}
	
	public void openLaunchpad() throws MidiUnavailableException, InvalidMidiDataException {
		output = getOutputDevice();
		input = getInputDevice();
		if (output == null || input == null){
			System.out.println("Launchpad failed to connect");
		} else {
			output.open();
			input.open();
			trans = output.getReceiver();
			input.getTransmitter().setReceiver(rcvr);
			ShortMessage msg = new ShortMessage();
			msg.setMessage(0xB0, 0x00, 0x28);	//set up double buffering for flashing
			trans.send(msg, -1);
			System.out.println("Launchpad connected successfully");
		}
	}
	
	private MidiDevice getOutputDevice() throws MidiUnavailableException{
		MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo(); 
        for (MidiDevice.Info info : midiDeviceInfo) {
        	System.out.println(info.getName());
            if (info.getName().contains("Launchpad")) { 
                MidiDevice device = MidiSystem.getMidiDevice(info); 
                if (device.getMaxReceivers() == -1) { 
                    return device; 
                } 
            } 
        } 
        System.out.println("We fucked up");
        return null;
	}
	
	private MidiDevice getInputDevice() throws MidiUnavailableException{
		MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info : midiDeviceInfo){
			if (info.getName().contains("Launchpad")){
				MidiDevice device = MidiSystem.getMidiDevice(info);
				if (device.getMaxTransmitters() == -1){
					return device;
				}
			}
		}
		System.out.println("We Fooked up");
		return null;
	}
	
	
	public void closeLaunchpad() {
		if (input.isOpen()){
			input.close();
		}
		if (output.isOpen()){
			resetPad();
			output.close();
		}
		
		rcvr.close();
		
	}

	
	private class MyReceiver implements Receiver{
		
		
		public MyReceiver(){
	         
		}
		
		@Override
		public void close() {
			// TODO Auto-generated method stub
		}

		@Override
		public void send(MidiMessage arg0, long arg1) {
			// TODO Auto-generated method stub
			//status = 1;
			byte[] b = arg0.getMessage();
			buffer.add(arg0);
			System.out.println("Received " + status);			
		}
	}

	
	
}
