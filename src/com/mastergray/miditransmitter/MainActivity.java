package com.mastergray.miditransmitter;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	Button deviceButton;
	static Switch deviceSwitch;
	LinearLayout inputLayout;
	SeekBar octaveSelect;
	static TextView noteView;
	UsbManager deviceManager;
	
	private static PermissionRequest usbReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize UI:
		deviceButton = (Button) findViewById(R.id.deviceButton);
		deviceSwitch = (Switch) findViewById(R.id.deviceSwitch);
		inputLayout  = (LinearLayout) findViewById(R.id.keyLayout);
		octaveSelect = (SeekBar) findViewById(R.id.octaveSelect);
		noteView = (TextView) findViewById(R.id.noteView);
		
		//	Force disabled UI components:
		inputLayout.setEnabled(false);
		octaveSelect.setEnabled(false);
		
		MidiInput.init(this);
	
		//	Initialize Device:
		deviceButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
            public void onClick(View v) {
			
//				Get devices
				deviceManager = (UsbManager) getSystemService(Context.USB_SERVICE);
				
				//	Find midi device
				UsbDevice device = MidiDevice.getInputDevice(deviceManager.getDeviceList());
				
				if (device != null) {
					
					//	Request permission to use that MIDI device
					usbReceiver = new PermissionRequest(deviceManager);
			 		String usbPermission = usbReceiver.getAction();
			 	
			 		//	Register broadcast for making permission request:
			 		PendingIntent PermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(usbPermission), 0);
			 		IntentFilter filter = new IntentFilter(usbPermission);
			 		registerReceiver(usbReceiver, filter);
			 		
			 		// Launch prompt
			 		deviceManager.requestPermission(device, PermissionIntent);
					
				} else {
					
					Toast.makeText(getApplicationContext(), "No MIDI Device Found", Toast.LENGTH_SHORT).show();
				
				}
				
				
			}
		});
		
		//	Initialize MIDI Input:	
		deviceSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			 
		   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 
			    if(isChecked){
			    
			    	// Update UI:
			    	deviceButton.setEnabled(false);
			    	inputLayout.setEnabled(true);
			    	octaveSelect.setEnabled(true);
			    	MidiInput.turnKeysOn();
			    		    	
					//	Creates connection and claims interface:
	            	MidiDevice.setConnection(deviceManager, MidiDevice.getDevice());
					
					ThreadManager.start(buttonView.getContext());
			    	
			    }else{
			     
			    	//	Update UI:
			    	deviceButton.setEnabled(true);
			    	inputLayout.setEnabled(false);
			    	octaveSelect.setEnabled(false);
			    	octaveSelect.setProgress(0);
			    	MidiInput.turnKeysOff();
			    	
			    	ThreadManager.stop();
			    }
		 
		
		   }
		
		});
		
		octaveSelect.setOnSeekBarChangeListener(new OnSeekBarChangeListener () {

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				
				MidiInput.update(progress);
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onPause(Bundle savedInstanceState) {
		
		 unregisterReceiver(usbReceiver);
		
	}
	
	protected void onDestroy(Bundle savedInstanceState) {
		
		 unregisterReceiver(usbReceiver);
		
	}
	
	protected void onResume(Bundle savedInstanceState) {
		
		IntentFilter filter = new IntentFilter();
	    filter.addAction(usbReceiver.getAction());
	    registerReceiver(usbReceiver, filter);  
		
	}

	protected void onRestart(Bundle savedInstanceState) {
		
		//	Get devices
		deviceManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		
		//	Find midi device
		UsbDevice device = MidiDevice.getInputDevice(deviceManager.getDeviceList());
		
		if (device != null) {
			
			//	Request permission to use that MIDI device
			usbReceiver = new PermissionRequest(deviceManager);
	 		String usbPermission = usbReceiver.getAction();
	 	
	 		//	Register broadcast for making permission request:
	 		PendingIntent PermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(usbPermission), 0);
	 		IntentFilter filter = new IntentFilter(usbPermission);
	 		registerReceiver(usbReceiver, filter);
	 		
	 		// Launch prompt
	 		deviceManager.requestPermission(device, PermissionIntent);
			
		} else {
			
			Toast.makeText(getApplicationContext(), "No MIDI Device Found", Toast.LENGTH_SHORT).show();
		
		}
		
	}
	
}
