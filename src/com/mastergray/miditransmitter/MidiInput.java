package com.mastergray.miditransmitter;

import java.util.ArrayList;


import android.app.Activity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class MidiInput {

	public static ArrayList<MidiInput> triggers = new ArrayList<MidiInput>();
	public static int playNote;
	public static boolean stopNote = true;
	
	public static void create(String noteName, int baseNote, Button button) {
		
		final MidiInput trigger = new MidiInput(noteName, baseNote);
		
		button.setText(noteName + "1");
		button.setEnabled(false);
		button.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction() & MotionEvent.ACTION_MASK) {

				    case MotionEvent.ACTION_DOWN:
				    	MainActivity.noteView.setText(trigger.getNote() + " -> " + trigger.getNoteValue());
				    	MidiInput.setPlayNote(trigger.getNoteValue());
				    	MidiInput.stopNoteOff();
				    break;
				    case MotionEvent.ACTION_UP:
				    case MotionEvent.ACTION_OUTSIDE:
				    	MainActivity.noteView.setText("");
				    	MidiInput.stopNoteOn();
				    break;
			
				}
				
				return false;
		
			}
				
		});
		
		trigger.setButton(button);
		triggers.add(trigger);
				
	}
	
	public static void update(int octave) {
		
		for (MidiInput trigger : triggers) {
			
			trigger.setNote(octave);
			trigger.setNoteName(octave);
			
		}
	
	}
	
	public static void turnKeysOn() {
		
		for (MidiInput trigger : triggers) {
			
			trigger.turnOn();
			
		}
		
	}
	
	public static void turnKeysOff() {
		
		for (MidiInput trigger : triggers) {
			
			trigger.turnOff();
			
		}
		
	}
	
	
	//	Builds keyboard starting at octave 1:
	public static void init(Activity activity) {
		
		MidiInput.create("C", 24, (Button) activity.findViewById(R.id.c_key));
		MidiInput.create("C#", 25, (Button) activity.findViewById(R.id.c_sharp_key));
		MidiInput.create("D", 26, (Button) activity.findViewById(R.id.d_key));
		MidiInput.create("D#", 27, (Button) activity.findViewById(R.id.d_sharp_key));
		MidiInput.create("E", 28, (Button) activity.findViewById(R.id.e_key));
		MidiInput.create("F", 29, (Button) activity.findViewById(R.id.f_key));
		MidiInput.create("F#", 30, (Button) activity.findViewById(R.id.f_sharp_key));
		MidiInput.create("G", 31, (Button) activity.findViewById(R.id.g_key));
		MidiInput.create("G#", 32, (Button) activity.findViewById(R.id.g_sharp_key));
		MidiInput.create("A", 33, (Button) activity.findViewById(R.id.a_key));
		MidiInput.create("A#", 34, (Button) activity.findViewById(R.id.a_sharp_key));
		MidiInput.create("B", 35, (Button) activity.findViewById(R.id.b_key));
		
	}
	
	public static ArrayList<MidiInput> get() {
		
		return triggers;
		
	}
	
	public static int getPlayNote() {
		
		return playNote;
		
	}
	
	public static boolean getStopNote() {
		
		return stopNote;
		
	}
	
	public static void setPlayNote(int note) {
		
		playNote = note;
		
	}
	
	public static void stopNoteOn() {
		
		stopNote = true;
		
	}
	
	public static void stopNoteOff() {
		
		stopNote = false;
		
	}
	
	private int note;
	private int baseNote;
	private String noteName;
	private Button button;
	
	MidiInput (String noteName, int baseNote) {
		
		this.note   = baseNote;
		this.baseNote = baseNote;
		this.noteName = noteName;
		
			
	}
	
	private void setNoteName(int octave) {
		
		this.button.setText(this.noteName + (octave + 1));
		
	}
	
	private void setNote(int octave) {
		
		this.note = this.baseNote + (octave * 12);
		
	}
	
	private void setButton(Button button) {
		
		this.button = button;
		
	}
	
	
	public String getNote() {
		
		return this.noteName;
		
	}
	
	
	public int getNoteValue() {
		
		return this.note;
		
	}
	
	public void turnOn() {
		
		 this.button.setEnabled(true);
		
	}
	
	public void turnOff() {
		
		this.button.setEnabled(false);
		
	}
	
	
	
	
	
}
