package midi;

import javax.swing.JButton;

import keyboard.Note;

public class Chord {

	Note rootNote;
	Note secondNote;
	Note thirdNote;
	
	Note fourthNote;
	Note fifthNote;
	Note sixNote;
	Note seventhNote;
	
	int pitch;
	String chordType;
	//int rootNote;
	
	public Chord(String chordType,JButton rootButton, JButton thirdButton, JButton fifthButton, Note noteOne,Note noteTwo, Note noteThree){
	this.chordType = chordType;
	this.rootNote = noteOne;
	this.thirdNote = noteTwo;
	this.fifthNote = noteThree;
	}
	
	public Chord(String chordType,JButton rootButton, JButton thirdButton, JButton fifthButton, JButton seventhButton,
			Note noteOne,Note noteTwo, Note noteThree, Note noteFour){
		
		this.chordType = chordType;
		this.rootNote = noteOne;
		this.thirdNote = noteTwo;
		this.fifthNote = noteThree;
		this.seventhNote = noteFour;
	}
	
	
}
