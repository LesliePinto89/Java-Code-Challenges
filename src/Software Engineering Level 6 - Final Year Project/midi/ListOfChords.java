package midi;

import java.util.ArrayList;
import java.util.Collections;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import keyboard.Note;
import midiDevices.MidiReceiver;

public class ListOfChords {

	private Note rootNote; // tonic
	private Note secondNote; // supertonic
	private Note thirdNote; // mediant
	private Note fourthNote; // subdominant
	private Note fifthNote; // dominant
	private Note sixthNote; // submediant
	private Note seventhNote; // subtonic
	private Note newTonicNote; // tonal note / new tonic

	boolean firstInversionFirst = false;

	ArrayList<Chord> allIonianChords = new ArrayList<Chord>();
	Chord firstIonianInversion; // Store Major first inversions
	Chord[] allLydianChords = new Chord[10];
	Chord[] allMinorsChords = new Chord[10];
	Chord[] allMixolydianChords = new Chord[10];
	Chord[] allDorianChords = new Chord[10];

	private static volatile ListOfChords instance = null;

	private ListOfChords() {
	}

	public static ListOfChords getInstance() {
		if (instance == null) {
			synchronized (ListOfChords.class) {
				if (instance == null) {
					instance = new ListOfChords();
					// instance.storeAllChordsInList();
					/// instance.storeMajorChordsInList();
					// instance.storeMinorChordsInList();
				}
			}
		}

		return instance;
	}

	public void setIonianChords(Scale carriedScale) {
		// int i =0;
		allIonianChords.add(tetraChord(carriedScale));
		allIonianChords.add(majorChord(carriedScale));
		allIonianChords.add(suspendedFourthChord(carriedScale));
		allIonianChords.add(suspendedSecondChord(carriedScale));
		allIonianChords.add(majorSixthChord(carriedScale));
		allIonianChords.add(dominantNinthChord(carriedScale)); // Not sure
		allIonianChords.add(dimishedSeventhChord(carriedScale));
		allIonianChords.add(addNineChord(carriedScale));
		allIonianChords.add(ninthMajorChord(carriedScale));
	}

	public ArrayList<Chord> getIonianChords() {
		return allIonianChords;
	}

	// Generics omitted to allow multiple enum types to be arguments in the
	// function
	// public void findSpecificChordType(ArrayList <Chord> loadedChords, Enum []
	// chordNames) throws InvalidMidiDataException{
	public void findSpecificChordType(ArrayList<Chord> loadedChords) throws InvalidMidiDataException {
		// ADD USER INPUT DETECTION FROM AVAILABLE SCALES
		String chordName = "dom9";
		Chord foundChord = null;
		for (Chord aChord : loadedChords) {
			if (aChord.chordName.equals(chordName)) {
				foundChord = aChord;
				break;
			}
		}
		// Play chord through sequencer
		// Option 1
		// playAnyChordLength(foundChord);

		// Play first inversion - option 2
		firstAndSecondInversion(foundChord);

		// Or play second inversion
		// secondInversion(foundChord);

	}

	// PLAY CHORDS
	//////////////////////////////
	public void playAnyChordLength(Chord foundChord) throws InvalidMidiDataException {
		ArrayList<Note> notesInChord = foundChord.getChordNotes();
		for (Note aNote : notesInChord) {
			ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0, aNote.getPitch(), 100);
			MidiReceiver.getInstance().send(noteOne, -1);
		}
	}

	// If user types first inversion for chord, then to get second inversion,
	// recall this function with a boolean condition
	public void firstAndSecondInversion(Chord carriedChord) throws InvalidMidiDataException {

		// Repeat using previous stored array for second inversion.
		// Must have done first inversion first though
		if (firstInversionFirst == true) {
			carriedChord = getCurrentFirstInversion();

		}

		ArrayList<Note> notesInChord = carriedChord.getChordNotes();

		// Replace key with one octave higher, swap with last element, and
		// save array in memory
		Note increaseRootOctave = ListOfScales.getInstance().getKey(notesInChord.get(0), 12);

		notesInChord.set(0, increaseRootOctave);
		// List <Note> aList = Arrays.asList(notesInChord);

		switch (notesInChord.size()) {
		case 3:
			Collections.rotate(notesInChord, 2);
			break;
		case 4:
			Collections.rotate(notesInChord, 3);
			break;
		case 5:
			Collections.rotate(notesInChord, 4);
			break;
		case 6:
			Collections.rotate(notesInChord, 5);
			break;
		case 7:
			Collections.rotate(notesInChord, 6);
			break;
		}
		// aList.toArray(notesInChord);

		Chord editedChord = new Chord(carriedChord.getChordName(), notesInChord);

		// sT
		storeCurrentFirstInversion(editedChord);
		firstInversionFirst = true;
		playAnyChordLength(editedChord);

	}

	public void storeCurrentFirstInversion(Chord first) {
		firstIonianInversion = first;
	}

	public Chord getCurrentFirstInversion() {
		return firstIonianInversion;
	}

	// public void storeCurrentFirstInversion (Note [] first){
	// firstIonianInversion = first;
	// }
	//
	// public Note [] getCurrentFirstInversion (){
	// return firstIonianInversion;
	// }

	// This function can be used to jump straight to 2nd inversion, whilst
	// the first inversion can be used twice for 1st to 2nd inversion
	public void secondInversion(Chord carriedChord) throws InvalidMidiDataException {
		// Note [] second = getCurrentFirstInversion();
		ArrayList<Note> notesInChord = carriedChord.getChordNotes();

		// List <Note> aList = Arrays.asList(notesInChord);
		switch (notesInChord.size()) {
		case 3:
			Collections.rotate(notesInChord, 4);
			break;
		case 4:
			Collections.rotate(notesInChord, 6);
			break;
		case 5:
			Collections.rotate(notesInChord, 8);
			break;
		case 6:
			Collections.rotate(notesInChord, 10);
			break;
		case 7:
			Collections.rotate(notesInChord, 12);
			break;
		}
		// aList.toArray(notesInChord);
		Chord editedChord = new Chord(carriedChord.getChordName(), notesInChord);
		playAnyChordLength(editedChord);
	}

	public void setLydianChords(Scale carriedScale) {
		int i = 0;
		allLydianChords[++i] = majorSeventhChord(carriedScale); // A dominant
																// chord could
																// also be in
																// lydian scale
	}

	public void setAllMinorChords(Scale carriedScale) {
		int i = 0;
		allMinorsChords[++i] = minorChord(carriedScale);
		allMinorsChords[++i] = minorSeventhChord(carriedScale);
		allMinorsChords[++i] = minorNinthChord(carriedScale);
	}

	public void setAllMixolydianChords(Scale carriedScale) {
		int i = 0;
		allMixolydianChords[++i] = dominantSeventhChord(carriedScale);
	}

	public void setDorianChords(Scale carriedScale) {
		int i = 0;
		allDorianChords[++i] = minorSixthChord(carriedScale);
		allDorianChords[++i] = minorSeventhChord(carriedScale);
		allDorianChords[++i] = minorNinthChord(carriedScale);
	}

	// Uses Ionian scale's first 4 scale degrees
	public Chord tetraChord(Scale carriedScale) {
		String chordName = "tetra";
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getSubTonic(); // 2
		thirdNote = carriedScale.getMediant(); // 4
		fourthNote = carriedScale.getSubDominant(); // 5
		Chord aChord = new Chord(chordName, rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// Uses Ionian scale
	public Chord majorChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		Chord aChord = new Chord("maj", rootNote, secondNote, thirdNote);
		return aChord;
	}

	// Uses Aeolian scale
	public Chord minorChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 7
		Chord aChord = new Chord("min", rootNote, secondNote, thirdNote);
		return aChord;
	}

	// Could use augmented scale
	public Chord augmentedChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 8
		Chord aChord = new Chord("aug", rootNote, secondNote, thirdNote);
		return aChord;
	}

	// Diminished cords use the Half Whole Diminished Scale
	public Chord dimishedChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 6
		Chord aChord = new Chord("dim", rootNote, secondNote, thirdNote);
		return aChord;
	}

	// This chord is based of the 5th degree of the diatonic scale, and has
	// a root, a major third, a perfect fifth, and a diminished 7th. The
	// dominant chord uses
	// the mixolydian diatonic scale
	public Chord dominantSeventhChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSubTonic(); // 10

		Chord aChord = new Chord("dom7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// A dominant chord could be the ionian or lydian scale
	public Chord majorSeventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSubTonic(); // 11
		Chord aChord = new Chord("maj7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// This chord can use the dorian scale, phrygian scale, and aeolian scale
	public Chord minorSeventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSubTonic(); // 10
		Chord aChord = new Chord("min7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// Can use Ionion scale
	public Chord suspendedFourthChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getSubDominant(); // 5
		thirdNote = carriedScale.getDominant(); // 7
		Chord aChord = new Chord("sus4", rootNote, secondNote, thirdNote);
		return aChord;
	}

	public Chord suspendedSecondChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getSubTonic(); // 2
		thirdNote = carriedScale.getDominant(); // 7
		Chord aChord = new Chord("sus2", rootNote, secondNote, thirdNote);
		return aChord;
	}

	// Can use IonionScale
	public Chord majorSixthChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // [0]; //0
		secondNote = carriedScale.getMediant(); // [2]; //4
		thirdNote = carriedScale.getDominant(); // [4]; //7
		fourthNote = carriedScale.getSubMediant();// [6]; //9
		Chord aChord = new Chord("maj6", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// Can use dorian scale
	public Chord minorSixthChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSubMediant(); // 9
		Chord aChord = new Chord("min6", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	public Chord dominantNinthChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSubMediant(); // 9
		// Not as part of a known scale, but rather an added ninth to the Ionian
		// scale.
		// Uses below function to get next step pitch against former from map of
		// notes
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 5); // 14
		Chord aChord = new Chord("dom9", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	public Chord ninthMajorChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSubTonic(); // 11
		// Same as ninth dominant chord
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 3); // 14
		Chord aChord = new Chord("dom9", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	// This chord can use the dorian scale, phrygian scale, and aeolian scale
	public Chord minorNinthChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // [0]; //0
		secondNote = carriedScale.getMediant(); // [2]; //3
		thirdNote = carriedScale.getDominant(); // [4]; //7
		fourthNote = carriedScale.getSubTonic();// [6]; //10
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		Chord aChord = new Chord("min9", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	// This is enharmonically equivalent to the major sixth. Might not be found
	// using
	// a scale name
	public Chord dimishedSeventhChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = ListOfScales.getInstance().getKey(rootNote, 3); // 3
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 3); // 6
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 9
		Chord aChord = new Chord("dim7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// Ionian scale's 1st,3rd and 5th degree with added ninth
	public Chord addNineChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7

		// Same as ninth dominant chord
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 7); // 14
		Chord aChord = new Chord("add9", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// Is not part of any scale but is enharmonically equivalent to the French
	// sixth chord.
	public Chord dominantSevenFlatFiveChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 2); // 6
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 10
		Chord aChord = new Chord("dom7<5", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	public Chord minorEleventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = ListOfScales.getInstance().getKey(rootNote, 7); // 7
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 3); // 10
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 14
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 1); // 15
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 2); // 17

		Chord aChord = new Chord("dom7<5", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote);
		return aChord;
	}

	public Chord dominantEleventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = ListOfScales.getInstance().getKey(rootNote, 7); // 7
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 3); // 10
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 14
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 3); // 17

		Chord aChord = new Chord("dom7<5", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	public Chord dominantThirteenChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = ListOfScales.getInstance().getKey(rootNote, 7); // 7
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 3); // 10
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 14
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 2); // 16
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 5); // 21
		Chord aChord = new Chord("dom7<5", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote);
		return aChord;

	}

	public Chord minorThirteenChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = ListOfScales.getInstance().getKey(rootNote, 7); // 7
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 3); // 10
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 14
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 1); // 15
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 5); // 21
		Chord aChord = new Chord("dom7<5", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote);
		return aChord;
	}

	public Chord majorThirteenChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = ListOfScales.getInstance().getKey(rootNote, 7); // 7
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 4); // 11
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 14
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 2); // 16
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 5); // 21
		Chord aChord = new Chord("dom7<5", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote);
		return aChord;
	}
}
