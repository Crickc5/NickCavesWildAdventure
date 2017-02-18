package main;


import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Main{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Launchpad launchpad = new Launchpad();
		
		try {
			launchpad.openLaunchpad();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (launchpad.status == 0){
			//System.out.println();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				launchpad.checkInputs();
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			
			
			
			if (launchpad.status != 0){
				break;
			}
		}
		
		//launchpad.testLights();
		
		launchpad.closeLaunchpad();
	}

}
