package midi;

import java.awt.Color;
import java.util.ArrayList;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import keyboard.Note;
import keyboard.VirtualKeyboard;
import midiDevices.MidiReceiver;

public class Chord {

	boolean isResetColorKeys =false;
	int root;
	int third;
	int fifth;
	ArrayList<JButton> KeysList = new ArrayList<JButton>();
	
	Note rootNote;
	Note secondNote;
	Note thirdNote;

	//Used with chords higher than 3 notes
	Note fourthNote;
	Note fifthNote;
	Note sixNote;
	Note seventhNote;
	int pitch;
	String chordType;
	// int rootNote;
	
	
	// A singleton pattern so that only one instance of this class
	// can be accessed and instantiated
	private static volatile Chord instance = null;

	private Chord() {
	}

	public static Chord getInstance() {
		if (instance == null) {
			synchronized (Chord.class) {
				if (instance == null) {
					instance = new Chord();
				}
			}
		}

		return instance;
	}

	private DefaultListModel<String> majorListModel;
	private DefaultListModel<String> minorListModel;
	private int JListChordsIndex;

	public void storeMajorListModel(DefaultListModel<String> carriedMajorListModel) {
		majorListModel = carriedMajorListModel;
	}

	public DefaultListModel<String> getMajorListModel() {
		return majorListModel;
	}

	public void storeMinorListModel(DefaultListModel<String> carriedMinorListModel) {
		minorListModel = carriedMinorListModel;
	}

	public DefaultListModel<String> getMinorListModel() {
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

	public enum allChordNamesList {
		
		//Major chords - commented enumerations are standard form for chords, but the
		// music application's buttons are designed using natural and sharp names.
		// Pitch is in correct natural and sharp/flat structure however.
		
		CMAJ(new String[] { "C", "E", "G" }), 
		CHASHMAJ(new String[] { "C#", "F", "G#" }), 
		//CHASHMAJ(new String[] { "C#", "E#", "G#" }), 
		DMAJOR(new String[] { "D", "F#", "A" }),
		EFLATMAJ(new String[] { "D#", "G", "A#" }), 
		//EFLATMAJ(new String[] { "Eb", "G", "Bb" }), 
		EMAJ(new String[] { "E", "G#", "B" }), 
		FMAJ(new String[] { "F", "A", "C" }), 
		FHASHMAJ(new String[] { "F#", "A#", "C#" }),
		GMAJ(new String[] { "G", "B", "D" }), 
		AFLATMAJ(new String[] { "G#", "C", "D#" }), 
		//AFLATMAJ(new String[] { "Ab", "C", "Eb" }),
		AMAJ(new String[] { "A", "C#", "E" }), 
		BFLATMAJ(new String[] { "A#", "D", "F" }),
		//BFLATMAJ(new String[] { "Bb", "D", "F" }), 
		BMAJ(new String[] { "B", "D#", "F#" }),

		 
		//Minor chords	 
		
		CMINOR(new String[] { "C", "D#", "G" }), 
		//CMINOR(new String[] { "C", "Eb", "G" }), 
		CHASHMINOR(new String[] { "C#", "E", "G#" }), 
		DMINOR(new String[] { "D", "F", "A" }), 
		EFLATMINOR(new String[] { "D#", "F#", "A#" }), 
		//EFLATMINOR(new String[] { "Eb", "Gb", "Bb" }), 
		EMINOR(new String[] { "E", "G", "B" }), 
		FMINOR(new String[] { "F", "G#", "C" }),
		//FMINOR(new String[] { "F", "Ab", "C" }),
		FHASHMINOR(new String[] { "F#", "A", "C#" }),
		GMINOR(new String[] { "G", "A#", "D" }), 
		//GMINOR(new String[] { "G", "Bb", "D" }), 
		AFLATMINOR(new String[] { "G#", "B", "D#" }), 
		//AFLATMINOR(new String[] { "Ab", "Cb", "Eb" }), 
		AMINOR(new String[] { "A", "C", "E" }), 
		BFLATMINOR(new String[] { "A#", "C#", "F" }),
		//BFLATMINOR(new String[] { "Bb", "Db", "F" }),
		BMINOR(new String[] { "B", "D", "F#" });
		
	 

		public final String[] chord;

		allChordNamesList(String[] chord) {
			this.chord = chord;
		}

		public String[] getChord() {
			return chord;
		}
	}

	//To make individual major chords list
	public enum majorChordNamesList {
		CMAJ(new String[] { "C", "E", "G" }), CHASHMAJ(new String[] { "C#", "E#", "G#" }), DMAJOR(
				new String[] { "D", "F#", "A" }), EFLATMAJ(new String[] { "Eb", "G", "Bb" }), EMAJ(
						new String[] { "F", "A", "C" }), FMAJ(new String[] { "F#", "A#", "C#" }),

		GMAJ(new String[] { "G", "B", "D" }), AFLATMAJ(new String[] { "Ab", "C", "Eb" }), AMAJ(
				new String[] { "A", "C#", "E" }), BFLATMAJ(
						new String[] { "Bb", "D", "F" }), BMAJ(new String[] { "B", "D#", "F#" });

		public final String[] majorChord;

		majorChordNamesList(String[] majorChord) {
			this.majorChord = majorChord;
		}

		public String[] getMajorChord() {
			return majorChord;
		}
	}

	//To make individual minor chords list
	public enum minorChordNamesList {
		CMINOR(new String[] { "C", "Eb", "G" }), CHASHMINOR(new String[] { "C#", "E", "G#" }), DMINOR(
				new String[] { "D", "F", "A" }), EFLATMINOR(new String[] { "Eb", "Gb", "Bb" }), EMINOR(
						new String[] { "E", "G", "B" }), FMINOR(new String[] { "F", "Ab", "C" }),

		FHASHMINOR(new String[] { "F#", "A", "C#" }),

		GMINOR(new String[] { "G", "Bb", "D" }), AFLATMINOR(new String[] { "Ab", "Cb", "Eb" }), AMINOR(
				new String[] { "A", "C", "E" }), BFLATMINOR(
						new String[] { "Bb", "Db", "F" }), BMINOR(new String[] { "B", "D", "F#" });

		public final String[] minorChord;

		minorChordNamesList(String[] minorChord) {
			this.minorChord = minorChord;
		}

		public String[] getMinorChord() {
			return minorChord;
		}
	}

	public Chord(String chordType, JButton rootButton, JButton thirdButton, JButton fifthButton, Note noteOne,
			Note noteTwo, Note noteThree) {
		this.chordType = chordType;
		this.rootNote = noteOne;
		this.thirdNote = noteTwo;
		this.fifthNote = noteThree;
	}

	public Chord(String chordType, JButton rootButton, JButton thirdButton, JButton fifthButton, JButton seventhButton,
			Note noteOne, Note noteTwo, Note noteThree, Note noteFour) {

		this.chordType = chordType;
		this.rootNote = noteOne;
		this.thirdNote = noteTwo;
		this.fifthNote = noteThree;
		this.seventhNote = noteFour;
	}

}
