package midi;

import java.util.ArrayList;
import keyboard.Note;

public class Chord {

	String chordName = "";
	private ArrayList<Note> chordNotes = new ArrayList<Note>();

	public Chord(String chordName, ArrayList<Note> editedChordNotes) {
		this.chordName = chordName;
		this.chordNotes = editedChordNotes;
	}

	// 3 notes chord
	// The arraylist segments the notes from the chord name for
	// easy manipulation of the notes, and to get its size of notes
	public Chord(String chordName, Note note1, Note note2, Note note3) {
		this.chordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);

	}

	// 4 notes chord
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4) {
		this.chordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
	}

	// 5 notes chord
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4, Note note5) {
		this.chordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
		this.chordNotes.add(note5);
	}

	// 6 notes chord
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4, Note note5, Note note6) {
		this.chordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
		this.chordNotes.add(note5);
		this.chordNotes.add(note6);
	}

	public void setChordName(String currentChord) {
		this.chordName = currentChord;
	}

	public String getChordName() {
		return chordName;
	}

	public ArrayList<Note> getChordNotes() {
		return chordNotes;
	}

	// Can use for default list model in List of Chord class
	public enum majorChordNames {
		tetra, maj, sus4, sus2, maj6, dom9, dim7, add9, maj9;
	}

	public enum minorChordNames {
		min, min7, min9;
	}

	///////////////////////////////////////////////////////

	/*
	 * COME BACK TO THIS CODE LATER IF NEEDED public void minorEleventhChord
	 * (int rootKey){ rootNote = rootKey; secondNote = rootNote + 7; thirdNote =
	 * rootNote +10; fourthNote = rootNote +14; fifthNote = rootNote +15;
	 * sixthNote = rootNote +17; }
	 * 
	 * public void dominantEleventhChord (int rootKey){ rootNote = rootKey;
	 * secondNote = rootNote + 7; thirdNote = rootNote +10; fourthNote =
	 * rootNote +14; fifthNote = rootNote +17; }
	 * 
	 * public void dominantThirteenChord (int rootKey){ rootNote = rootKey;
	 * secondNote = rootNote + 7; thirdNote = rootNote +10; fourthNote =
	 * rootNote +14; fifthNote = rootNote +16; sixthNote = rootNote +21; }
	 * 
	 * public void minorThirteenChord (int rootKey){ rootNote = rootKey;
	 * secondNote = rootNote + 7; thirdNote = rootNote +10; fourthNote =
	 * rootNote +14; fifthNote = rootNote +15; sixthNote = rootNote +21; }
	 * 
	 * public void majorThirteenChord (int rootKey){ rootNote = rootKey;
	 * secondNote = rootNote + 7; thirdNote = rootNote +11; fourthNote =
	 * rootNote +14; fifthNote = rootNote +16; sixthNote = rootNote +21; }
	 */
	// //Is not part of any scale but is enharmonically equivalent to the French
	// sixth chord.
	// public void dominantSevenFlatFiveChord (Note [] notes){
	//
	// rootNote = notes [0]; //0
	// secondNote = notes [2]; //4 increase pitch from root
	// thirdNote = Scale.getInstance().getKey(secondNote, 2);
	// fourthNote = Scale.getInstance().getKey(thirdNote, 4);
	//
	// //rootNote = rootKey;
	// //secondNote = rootNote + 4;
	// // thirdNote = rootNote +6;
	// //fourthNote = rootNote +10;
	// }

	/*
	 * COME BACK TO THESE WHEN NEEDED public void sevenPlusFiveDominantChord
	 * (int rootKey){ rootNote = rootKey; secondNote = rootNote + 4; thirdNote =
	 * rootNote +8; fourthNote = rootNote +10; }
	 * 
	 * 
	 * public void majorSevenMinusFiveDominantChord (int rootKey){ rootNote =
	 * rootKey; secondNote = rootNote + 4; thirdNote = rootNote +6; fourthNote =
	 * rootNote +11; }
	 * 
	 * public void majorSevenPlusFiveDominantChord (int rootKey){ rootNote =
	 * rootKey; secondNote = rootNote + 4; thirdNote = rootNote +8; fourthNote =
	 * rootNote +11; }
	 * 
	 * public void minorMajorSevenChord (int rootKey){ rootNote = rootKey;
	 * secondNote = rootNote + 3; thirdNote = rootNote +7; fourthNote = rootNote
	 * +11; }
	 * 
	 * public void sevenMinusFiveMinusNineChord (int rootKey){ rootNote =
	 * rootKey; secondNote = rootNote + 4; thirdNote = rootNote +6; fourthNote =
	 * rootNote +10; fifthNote = rootNote +13; }
	 * 
	 * public void sevenMinusFivePlusNineChord (int rootKey){ rootNote =
	 * rootKey; secondNote = rootNote + 4; thirdNote = rootNote +6; fourthNote =
	 * rootNote +10; fifthNote = rootNote +15; }
	 * 
	 * public void dominantSevenPlusFiveMinusNineChord (int rootKey){ rootNote =
	 * rootKey; secondNote = rootNote + 4; thirdNote = rootNote +8; fourthNote =
	 * rootNote +10; fifthNote = rootNote +13; }
	 * 
	 * public void dominantSevenPlusFivePlusNineChord (int rootKey){ rootNote =
	 * rootKey; secondNote = rootNote + 4; thirdNote = rootNote +8; fourthNote =
	 * rootNote +10; fifthNote = rootNote +15; }
	 * 
	 */

	// Former code that works only with triad chords

	// private boolean isResetColorKeys =false;
	// private int root;
	// private int third;
	// private int fifth;
	// private ArrayList<JButton> KeysList = new ArrayList<JButton>();
	// private DefaultListModel <String> allChordsListModel = new
	// DefaultListModel <String>();
	// private DefaultListModel<String> majorListModel = new DefaultListModel
	// <String>();
	// private DefaultListModel<String> minorListModel = new DefaultListModel
	// <String>();
	// private int JListChordsIndex;
	//
	//
	// //Used to get enum values only from a default list model if needed
	// public void storeAllChordsInList(){
	// allChordNamesList[] allChords = allChordNamesList.values();
	// for (int i =0;i<allChords.length;i++) {
	// allChordsListModel.addElement(allChords[i].name());
	// }
	// }
	//
	// public DefaultListModel <String> getAllChordsInList() {
	// return allChordsListModel;
	// }
	//
	// //Stores edited String of enum to facilate name change while can later
	// refer
	// //to enum values when convert copy of new string name back to enum name
	// public void storeMajorChordsInList(){
	// majorChordNamesList[] majorChords = majorChordNamesList.values();
	// String accidental = "";
	// for (int i =0;i<majorChords.length;i++) {
	// String chordName = majorChords[i].name();
	//
	// if(chordName.charAt(1) == 'S'){
	// accidental = chordName.substring(1, 6);
	// chordName = chordName.replace(accidental, "#");
	// majorListModel.addElement(chordName);
	// }
	//
	// else if (chordName.charAt(1) == 'F'){
	// accidental = chordName.substring(1, 5);
	// chordName = chordName.replace(accidental, "b");
	// majorListModel.addElement(chordName);
	// }
	// else {
	// majorListModel.addElement(majorChords[i].name());
	// }
	// }
	// }
	// public DefaultListModel <String> getMajorChordsInList() {
	// return majorListModel;
	// }
	//
	//
	// //Stores edited String of enum to facilate name change while can later
	// refer
	// //to enum values when convert copy of new string name back to enum name
	// public void storeMinorChordsInList(){
	// minorChordNamesList[] minorChords = minorChordNamesList.values();
	// String accidental = "";
	// for (int i =0;i<minorChords.length;i++) {
	// String chordName = minorChords[i].name();
	//
	// if(chordName.charAt(1) == 'S'){
	// accidental = chordName.substring(1, 6);
	// chordName = chordName.replace(accidental, "#");
	// minorListModel.addElement(chordName);
	// }
	//
	// else if (chordName.charAt(1) == 'F'){
	// accidental = chordName.substring(1, 5);
	// chordName = chordName.replace(accidental, "b");
	// minorListModel.addElement(chordName);
	// }
	// else {
	// minorListModel.addElement(minorChords[i].name());
	// }
	// }
	// }
	//
	// public DefaultListModel <String> getMinorChordsInList() {
	// return minorListModel;
	// }
	//
	//
	// public String[] breakDownChord(String editChord) {
	//
	// String[] chordInBits = new String[3];
	// String segmentNote = "";
	// int arrayIndex = 0;
	// for (int i = 0; i < editChord.length(); i++) {
	//
	// // This is used when the last note has no accidental
	// // as the increased i index takes the match to the editChord length
	// // to ensure its a natural note
	// if (i == editChord.length() - 1) {
	// char singleNote = editChord.charAt(i);
	// segmentNote = "" + singleNote;
	// chordInBits[arrayIndex] = segmentNote;
	// break;
	// }
	//
	// else if (editChord.charAt(i + 1) == '#' || editChord.charAt(i + 1) ==
	// 'b') {
	// char segmentOne = editChord.charAt(i);
	// char segmentTwo = editChord.charAt(i + 1);
	// segmentNote = "" + segmentOne + segmentTwo;
	// chordInBits[arrayIndex] = segmentNote;
	// arrayIndex++;
	// i++;
	//
	// }
	//
	// else if (editChord.charAt(i + 1) != '#' || editChord.charAt(i + 1) !=
	// 'b') {
	// char singleNote = editChord.charAt(i);
	// segmentNote = "" + singleNote;
	// chordInBits[arrayIndex] = segmentNote;
	// arrayIndex++;
	// }
	// }
	// return chordInBits;
	// }
	//
	// public void generateChord(String chordType, String chordNotes) throws
	// InvalidMidiDataException {
	//
	// String[] editedChord = breakDownChord(chordNotes); //Break Chord into
	// elements
	// String specificChordNote = ""; //Concatenate note in array with
	// conditional octave
	// String octaveBase ="4";
	// String octaveNewScale ="5";
	// String conditionalOctave = octaveBase;
	// specificChordNote = editedChord[0] + conditionalOctave;
	// int rootPitch = Note.convertToPitch(specificChordNote);
	// int rootNote = -1;
	// int minorOrMajorThird = -1;
	// int fifth = -1;
	// ArrayList<JButton> retrievedKeysList =
	// VirtualKeyboard.getInstance().getButtons();
	//
	// if (isResetColorKeys ==true){
	// resetKeysColors();
	// }
	//
	//
	// for (JButton button : retrievedKeysList) {
	// if (button.getText().contains(specificChordNote)) {
	// button.setBackground(Color.GREEN);
	// break;
	// }
	// rootNote++;
	// }
	// ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0,
	// rootPitch, 100);
	// MidiReceiver.getInstance().send(noteOne, -1);
	//
	// switch (editedChord[1]){
	// case "C" :conditionalOctave = octaveNewScale;
	// break;
	// case "C#" :conditionalOctave = octaveNewScale;
	// break;
	// case "D" :conditionalOctave = octaveNewScale;
	// break;
	//
	// //This is for BMajor chord as the major third D# is an octave higher than
	// the root B
	// case "D#" : if (editedChord[0].contains("B")){
	// conditionalOctave = octaveNewScale;
	// }
	// //This is for chord C Minor as the major third D# is in the same octave
	// as root C
	// else {conditionalOctave = octaveBase;}
	// break;
	// default:conditionalOctave = octaveBase;
	// break;
	// }
	// specificChordNote = editedChord[1] + conditionalOctave;
	//
	// for (JButton button : retrievedKeysList) {
	// if (button.getText().contains(specificChordNote)) {
	// button.setBackground(Color.GREEN);
	// break;
	// }
	// minorOrMajorThird++;
	// }
	// ShortMessage noteTwo = new ShortMessage();
	// if (chordType.contains("MAJ")) {
	// noteTwo = new ShortMessage(ShortMessage.NOTE_ON, 0, rootPitch + 4, 100);
	//
	// } else if (chordType.contains("MINOR")) {
	// noteTwo = new ShortMessage(ShortMessage.NOTE_ON, 0, rootPitch + 3, 100);
	// }
	//
	// MidiReceiver.getInstance().send(noteTwo, -1);
	//
	//
	// switch (editedChord[2]){
	// //Added break to F# and F, check to make sure no problems happen with
	// chords
	// case "F#" :conditionalOctave = octaveNewScale;break;
	// case "F" :conditionalOctave = octaveNewScale;break;
	// case "G" : if (editedChord[0].contains("C")){
	// conditionalOctave = octaveBase;}
	// break;
	// case "E" : conditionalOctave = octaveNewScale;
	// break;
	// case "C" :conditionalOctave = octaveNewScale;
	// break;
	// case "C#" :conditionalOctave = octaveNewScale;
	// break;
	// case "D" :conditionalOctave = octaveNewScale;
	// break;
	// case "D#" : conditionalOctave = octaveNewScale;
	// break;
	// default:conditionalOctave = octaveBase;
	// break;
	// }
	//
	// specificChordNote = editedChord[2] + conditionalOctave;
	// for (JButton button : retrievedKeysList) {
	// if (button.getText().contains(specificChordNote)) {
	// button.setBackground(Color.GREEN);
	// break;
	// }
	// fifth++;
	// }
	//
	// ShortMessage noteThree = new ShortMessage(ShortMessage.NOTE_ON, 0,
	// rootPitch + 7, 100);
	// MidiReceiver.getInstance().send(noteThree, -1);
	//
	// // Store current chord notes to be reset on new chord play back
	// storeKeysColors(retrievedKeysList,rootNote+1,minorOrMajorThird+1,fifth+1);
	// isResetColorKeys =true;
	//
	//
	// }
	//
	// public void storeKeysColors(ArrayList<JButton> carriedList,int
	// lastRoot,int lastThird, int lastFifth){
	// this.KeysList = carriedList;
	// this.root = lastRoot;
	// this.third = lastThird;
	// this.fifth = lastFifth;
	//
	// }
	//
	// //Reset key colours to natural and accidental assignments
	// public void resetKeysColors(){
	// if(KeysList.get(root).getText().contains("#")){
	// KeysList.get(root).setBackground(Color.BLACK);
	// }
	// else {
	// KeysList.get(root).setBackground(Color.WHITE);}
	//
	// if(KeysList.get(third).getText().contains("#")){
	// KeysList.get(third).setBackground(Color.BLACK);
	// }
	// else {
	// KeysList.get(third).setBackground(Color.WHITE);}
	//
	// if(KeysList.get(fifth).getText().contains("#")){
	// KeysList.get(fifth).setBackground(Color.BLACK);
	// }
	// else {
	// KeysList.get(fifth).setBackground(Color.WHITE);}
	// }
	//
	// //Don't need now, maybe later or remove
	// public void storedJChordListSelectedIndex(int carriedJListIndex) {
	// this.JListChordsIndex = carriedJListIndex;
	// }
	// //Don't need now, maybe later or remove
	// public int getChordsListSelectedIndex() {
	// return JListChordsIndex;
	// }

	public enum allChordNamesList {

		// Major chords - commented enumerations are standard form for chords,
		// but the
		// music application's buttons are designed using natural and sharp
		// names.
		// Pitch is in correct natural and sharp/flat structure however.

		CMAJ(new String[] { "C", "E", "G" }), CSHARPMAJ(new String[] { "C#", "F", "G#" }),
		// CHASHMAJ(new String[] { "C#", "E#", "G#" }),
		DMAJOR(new String[] { "D", "F#", "A" }), EFLATMAJ(new String[] { "D#", "G", "A#" }),
		// EFLATMAJ(new String[] { "Eb", "G", "Bb" }),
		EMAJ(new String[] { "E", "G#", "B" }), FMAJ(new String[] { "F", "A", "C" }), FSHARPMAJ(
				new String[] { "F#", "A#", "C#" }), GMAJ(
						new String[] { "G", "B", "D" }), AFLATMAJ(new String[] { "G#", "C", "D#" }),
		// AFLATMAJ(new String[] { "Ab", "C", "Eb" }),
		AMAJ(new String[] { "A", "C#", "E" }), BFLATMAJ(new String[] { "A#", "D", "F" }),
		// BFLATMAJ(new String[] { "Bb", "D", "F" }),
		BMAJ(new String[] { "B", "D#", "F#" }),

		// Minor chords

		CMINOR(new String[] { "C", "D#", "G" }),
		// CMINOR(new String[] { "C", "Eb", "G" }),
		CSHARPMINOR(new String[] { "C#", "E", "G#" }), DMINOR(new String[] { "D", "F", "A" }), EFLATMINOR(
				new String[] { "D#", "F#", "A#" }),
		// EFLATMINOR(new String[] { "Eb", "Gb", "Bb" }),
		EMINOR(new String[] { "E", "G", "B" }), FMINOR(new String[] { "F", "G#", "C" }),
		// FMINOR(new String[] { "F", "Ab", "C" }),
		FSHARPMINOR(new String[] { "F#", "A", "C#" }), GMINOR(new String[] { "G", "A#", "D" }),
		// GMINOR(new String[] { "G", "Bb", "D" }),
		AFLATMINOR(new String[] { "G#", "B", "D#" }),
		// AFLATMINOR(new String[] { "Ab", "Cb", "Eb" }),
		AMINOR(new String[] { "A", "C", "E" }), BFLATMINOR(new String[] { "A#", "C#", "F" }),
		// BFLATMINOR(new String[] { "Bb", "Db", "F" }),
		BMINOR(new String[] { "B", "D", "F#" });

		public final String[] chord;

		allChordNamesList(String[] chord) {
			this.chord = chord;
		}

		public String[] getChord() {
			return chord;
		}
	}

	// To make individual major chords list
	public enum majorChordNamesList {
		CMAJ(new String[] { "C", "E", "G" }), CSHARPMAJ(new String[] { "C#", "E#", "G#" }), DMAJOR(
				new String[] { "D", "F#", "A" }), EFLATMAJ(new String[] { "Eb", "G", "Bb" }), EMAJ(
						new String[] { "F", "A", "C" }), FMAJ(new String[] { "F#", "A#", "C#" }), GMAJ(
								new String[] { "G", "B", "D" }), AFLATMAJ(new String[] { "Ab", "C", "Eb" }), AMAJ(
										new String[] { "A", "C#", "E" }), BFLATMAJ(
												new String[] { "Bb", "D", "F" }), BMAJ(
														new String[] { "B", "D#", "F#" });

		public final String[] majorChord;

		majorChordNamesList(String[] majorChord) {
			this.majorChord = majorChord;
		}

		public String[] getMajorChord() {
			return majorChord;
		}
	}

	// To make individual minor chords list
	public enum minorChordNamesList {
		CMINOR(new String[] { "C", "Eb", "G" }), CSHARPMINOR(new String[] { "C#", "E", "G#" }), DMINOR(
				new String[] { "D", "F", "A" }), EFLATMINOR(new String[] { "Eb", "Gb", "Bb" }), EMINOR(
						new String[] { "E", "G", "B" }), FMINOR(new String[] { "F", "Ab", "C" }), FSHARPMINOR(
								new String[] { "F#", "A", "C#" }), GMINOR(new String[] { "G", "Bb", "D" }), AFLATMINOR(
										new String[] { "Ab", "Cb", "Eb" }), AMINOR(
												new String[] { "A", "C", "E" }), BFLATMINOR(
														new String[] { "Bb", "Db", "F" }), BMINOR(
																new String[] { "B", "D", "F#" });

		public final String[] minorChord;

		minorChordNamesList(String[] minorChord) {
			this.minorChord = minorChord;
		}

		public String[] getMinorChord() {
			return minorChord;
		}
	}

	public enum allCommonChords {
		tetra, maj, min, sus4, sus2, aug, dim, dom7, maj6, dom9, dim7, add9, maj9, min9;
	}

	public enum allNoneScaleChords {
		min11, dom11, dom13, min13, maj13, dom7Minus5, dom7Plus5, maj7Minus5, maj7Plus5, minmaj7, dom7Minus5Minus9, dom7Minus5Plus9, dom7Plus5Minus9, dom7Plus5Plus9;
	}

}
