package midi;

import java.awt.Color;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import keyboard.Note;
import keyboard.VirtualKeyboard;
import midi.Chord.allChordNamesList;
import midi.Chord.majorChordNamesList;
import midi.Chord.minorChordNamesList;
import midiDevices.MidiReceiver;

public class AddChordToFeatureTab {

	private static volatile AddChordToFeatureTab instance = null;

	private AddChordToFeatureTab() {
	}
	
    
	
	public static AddChordToFeatureTab getInstance() {
	if (instance == null) {
		synchronized (AddChordToFeatureTab.class) {
			if (instance == null) {
				instance = new AddChordToFeatureTab();
				//instance.storeAllChordsInList();
				///instance.storeMajorChordsInList();
				//instance.storeMinorChordsInList();
			}
		}
	}

	return instance;
}
	
	
	private boolean isResetColorKeys =false;
	private int root;
	private int third;
	private int fifth;
	private ArrayList<JButton> KeysList = new ArrayList<JButton>();
	private DefaultListModel <String> allChordsListModel = new DefaultListModel <String>();
	private DefaultListModel<String> majorListModel = new DefaultListModel <String>();
	private DefaultListModel<String> minorListModel = new DefaultListModel <String>();
	private int JListChordsIndex;
	
	
	//Used to get enum values only from a default list model if needed
	public void storeAllChordsInList(){
		allChordNamesList[] allChords = allChordNamesList.values();
		for (int i =0;i<allChords.length;i++) {
			allChordsListModel.addElement(allChords[i].name());
		}
	}

	public DefaultListModel <String> getAllChordsInList() {
		return allChordsListModel;
	}
	
	//Stores edited String of enum to facilate name change while can later refer
	//to enum values when convert copy of new string name back to enum name
	public void storeMajorChordsInList(){
		majorChordNamesList[] majorChords = majorChordNamesList.values();
		String accidental = "";
		for (int i =0;i<majorChords.length;i++) {
			String chordName = majorChords[i].name();
			
			 if(chordName.charAt(1) == 'S'){
				accidental = chordName.substring(1, 6);	
				chordName = chordName.replace(accidental, "#");
				majorListModel.addElement(chordName);
			}
			
			else if (chordName.charAt(1) == 'F'){
				accidental = chordName.substring(1, 5);	
				chordName = chordName.replace(accidental, "b");
				majorListModel.addElement(chordName);
			}
			else {
				majorListModel.addElement(majorChords[i].name());
		}
	}
	}
	public DefaultListModel <String> getMajorChordsInList() {
		return majorListModel;
	}
	
	
	//Stores edited String of enum to facilate name change while can later refer
	//to enum values when convert copy of new string name back to enum name
	public void storeMinorChordsInList(){
		minorChordNamesList[] minorChords = minorChordNamesList.values();
		String accidental = "";
		for (int i =0;i<minorChords.length;i++) {
			String chordName = minorChords[i].name();
			
			 if(chordName.charAt(1) == 'S'){
				accidental = chordName.substring(1, 6);	
				chordName = chordName.replace(accidental, "#");
				minorListModel.addElement(chordName);
			}
			
			else if (chordName.charAt(1) == 'F'){
				accidental = chordName.substring(1, 5);	
				chordName = chordName.replace(accidental, "b");
				minorListModel.addElement(chordName);
			}
			else {
				minorListModel.addElement(minorChords[i].name());
		}
	}
	}
	
	public DefaultListModel <String> getMinorChordsInList() {
		return minorListModel;
	}


	public String[] breakDownChord(String editChord) {

		String[] chordInBits = new String[3];
		String segmentNote = "";
		int arrayIndex = 0;
		for (int i = 0; i < editChord.length(); i++) {

			// This is used when the last note has no accidental
			// as the increased i index takes the match to the editChord length
			// to ensure its a natural note
			if (i == editChord.length() - 1) {
				char singleNote = editChord.charAt(i);
				segmentNote = "" + singleNote;
				chordInBits[arrayIndex] = segmentNote;
				break;
			}
			
			else if (editChord.charAt(i + 1) == '#' || editChord.charAt(i + 1) == 'b') {
				char segmentOne = editChord.charAt(i);
				char segmentTwo = editChord.charAt(i + 1);
				segmentNote = "" + segmentOne + segmentTwo;
				chordInBits[arrayIndex] = segmentNote;
				arrayIndex++;
				i++;

			} 
			
			else if (editChord.charAt(i + 1) != '#' || editChord.charAt(i + 1) != 'b') {
				char singleNote = editChord.charAt(i);
				segmentNote = "" + singleNote;
				chordInBits[arrayIndex] = segmentNote;
				arrayIndex++;
			}
		}
		return chordInBits;
	}

	public void generateChord(String chordType, String chordNotes) throws InvalidMidiDataException {
		
		String[] editedChord = breakDownChord(chordNotes); //Break Chord into elements
		String specificChordNote = ""; //Concatenate note in array with conditional octave
		String octaveBase ="4";
		String octaveNewScale ="5";
		String conditionalOctave = octaveBase;	
		specificChordNote = editedChord[0] + conditionalOctave;
		int rootPitch = Note.convertToPitch(specificChordNote);
		int rootNote = -1;
		int minorOrMajorThird = -1;
		int fifth = -1;
		ArrayList<JButton> retrievedKeysList = VirtualKeyboard.getInstance().getButtons();

		if (isResetColorKeys ==true){
			resetKeysColors();
		}
		
		
		for (JButton button : retrievedKeysList) {
			if (button.getText().contains(specificChordNote)) {
				button.setBackground(Color.GREEN);
				break;
			}
			rootNote++;
		}
		ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0, rootPitch, 100);
		MidiReceiver.getInstance().send(noteOne, -1);

	switch (editedChord[1]){
	    case "C" :conditionalOctave = octaveNewScale;
	    break;
	    case "C#" :conditionalOctave = octaveNewScale;
	    break;
	    case "D" :conditionalOctave = octaveNewScale;
	    break;
	    
	    //This is for BMajor chord as the major third  D# is an octave higher than the root B
	    case "D#" : if (editedChord[0].contains("B")){
			conditionalOctave = octaveNewScale;
			}
	    //This is for chord C Minor as the major third  D# is in the same octave as root C
	    else {conditionalOctave = octaveBase;}   
		break;
		default:conditionalOctave = octaveBase;
		break;
		}
	   specificChordNote = editedChord[1] + conditionalOctave;
		
		for (JButton button : retrievedKeysList) {
			if (button.getText().contains(specificChordNote)) {
				button.setBackground(Color.GREEN);
				break;
			}
			minorOrMajorThird++;
		}
		ShortMessage noteTwo = new ShortMessage();
		if (chordType.contains("MAJ")) {
			noteTwo = new ShortMessage(ShortMessage.NOTE_ON, 0, rootPitch + 4, 100);

		} else if (chordType.contains("MINOR")) {
			noteTwo = new ShortMessage(ShortMessage.NOTE_ON, 0, rootPitch + 3, 100);
		}

		MidiReceiver.getInstance().send(noteTwo, -1);

		
	switch (editedChord[2]){
	//Added break to F# and F, check to make sure no problems happen with chords
	case "F#" :conditionalOctave = octaveNewScale;break;
	case "F" :conditionalOctave = octaveNewScale;break;
	case "G" : if (editedChord[0].contains("C")){
		      conditionalOctave = octaveBase;}
	break;
	case "E" :  conditionalOctave = octaveNewScale;
	break;
	case "C" :conditionalOctave = octaveNewScale;
    break;
    case "C#" :conditionalOctave = octaveNewScale;
    break;
    case "D" :conditionalOctave = octaveNewScale;
    break;
    case "D#" : conditionalOctave = octaveNewScale;
    break;
	default:conditionalOctave = octaveBase;
	break;
	}
		
		specificChordNote = editedChord[2] + conditionalOctave;
		for (JButton button : retrievedKeysList) {
			if (button.getText().contains(specificChordNote)) {
				button.setBackground(Color.GREEN);
				break;
			}
			fifth++;
		}

		ShortMessage noteThree = new ShortMessage(ShortMessage.NOTE_ON, 0, rootPitch + 7, 100);
		MidiReceiver.getInstance().send(noteThree, -1);

		// Store current chord notes to be reset on new chord play back		
		storeKeysColors(retrievedKeysList,rootNote+1,minorOrMajorThird+1,fifth+1);
		isResetColorKeys =true;
		

	}

	public void storeKeysColors(ArrayList<JButton> carriedList,int lastRoot,int lastThird, int lastFifth){
		this.KeysList = carriedList;
		this.root = lastRoot;
		this.third = lastThird;
		this.fifth = lastFifth;
	
	}
	
	//Reset key colours to natural and accidental assignments
	public void resetKeysColors(){
		if(KeysList.get(root).getText().contains("#")){
			KeysList.get(root).setBackground(Color.BLACK);
		}
		else {
		KeysList.get(root).setBackground(Color.WHITE);}
		
		if(KeysList.get(third).getText().contains("#")){
			KeysList.get(third).setBackground(Color.BLACK);
		}
		else {
		KeysList.get(third).setBackground(Color.WHITE);}
		
		if(KeysList.get(fifth).getText().contains("#")){
			KeysList.get(fifth).setBackground(Color.BLACK);
		}
		else {
		KeysList.get(fifth).setBackground(Color.WHITE);}
	}
	
	//Don't need now, maybe later or remove
	public void storedJChordListSelectedIndex(int carriedJListIndex) {
		this.JListChordsIndex = carriedJListIndex;
	}
	//Don't need now, maybe later or remove
	public int getChordsListSelectedIndex() {
		return JListChordsIndex;
	}
}
