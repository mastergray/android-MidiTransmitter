package com.mastergray.miditransmitter;

import android.content.Context;
import android.widget.Toast;


public class ThreadManager {

	private static ThreadManager_Runnable runnable;
	
	public static void start(Context context) {
		
		// Initializes thread:
		runnable = new ThreadManager_Runnable(context);
		Thread midiThread = new Thread(runnable);
		midiThread.start();
		
	}
	
	public static void stop() {
		
		runnable.kill();
		
	}
	
}


/**
 * 
 *  Runnable Implementation For MIDI task
 *
 */

class ThreadManager_Runnable implements Runnable {

	private volatile boolean isRunning = true;
	private Context context;
	
	public ThreadManager_Runnable(Context context) {
		
		this.context = context;
		
	}
	
	@Override
	public void run()  {
		
		while (isRunning) {
			
				try {
			
					Thread.sleep(10);
					byte[] midiMessage;
					
						if (MidiInput.getStopNote()) {
						
							// Channel 1 Note off = 128
							midiMessage = MidiDevice.setMidiMessage(8, 128, MidiInput.getPlayNote(), 64);
						
						} else {
							
							// Channel 1 Note on = 144
							midiMessage = MidiDevice.setMidiMessage(9, 144, MidiInput.getPlayNote(), 100);
							
						}
					
					byte[] midiResponse = MidiDevice.sendInputRequest(midiMessage); 
					
						if (midiResponse == null) {
						
							Toast.makeText(context, "MIDI Input Could Not Be Sent", Toast.LENGTH_SHORT).show();
						
						} else {
						
							System.out.println(MidiDevice.getMidiMessage(midiResponse));
							
						}
					
					
				} catch (InterruptedException e) {
					
					Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
					
				}
			
			
		}
	
		// Closes connection:
		MidiDevice.closeConnection();
	}
	
	public void kill() {
		
		this.isRunning = false;
		
	}

}
