package midi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import keyboard.Note;
import keyboard.Note.allNotesType;
import midi.Chord.majorBasedChords;
import midi.Chord.minorBasedChords;
import midi.Chord.noneScaleChords;
import midiDevices.MidiReceiver;
import tools.PlaybackFunctions;

public class ListOfChords {

	private Note rootNote; // tonic
	private Note secondNote; // supertonic
	private Note thirdNote; // mediant
	private Note fourthNote; // subdominant
	private Note fifthNote; // dominant
	private Note sixthNote; // submediant
	private Note seventhNote; // subtonic
	private Note newTonicNote; // tonal note / new tonic

	private boolean inverted = false;
	private int scalesWithKeysChordsIndex =0;

	private ArrayList<Chord> allMajorChords;	
	private ArrayList<Chord> allMinorChords;
	private ArrayList<Chord> allHalfDimishedChords;
	private ArrayList<Chord> allFullyDimishedChords;
	private ArrayList<Chord> allDominantDimishedChords;
	
	ArrayList<Chord> allNonScaleChords;
	ArrayList<Chord> allLydianChords;	
	ArrayList<Chord> allMixolydianChords;
	ArrayList<Chord> allDorianChords;
	private Chord currentInversion; // Store any chord inversions
	
	private Chord firstInversion; // Store any chord inversions
	
	private ArrayList <Note> noteNames = new ArrayList<Note>(); 
	private ArrayList <String> noteNamesStrings = new ArrayList<String>(); 
	
	private ArrayList<GivenKeyChords> allKeysChords = new ArrayList<GivenKeyChords>(); //Store all key chords to link back to the name
	private ArrayList<GivenKeyScales> allScalesChords = new ArrayList<GivenKeyScales>();

	private static volatile ListOfChords instance = null;

	private ListOfChords() {
	}

	public static ListOfChords getInstance() {
		if (instance == null) {
			synchronized (ListOfChords.class) {
				if (instance == null) {
					instance = new ListOfChords();
				}
			}
		}

		return instance;
	}
	
	
	public void setAllKeyNotes(){
		allNotesType[] enumsToString = Note.allNotesType.values();
	for (allNotesType enumNote : enumsToString){
		String foundNote = enumNote.getNote();
		Note aNote = Note.getNotes(foundNote+3);
		noteNames.add(aNote);	
		noteNamesStrings.add(aNote.getName());
	}
	}
	
	public ArrayList<Note> getAllKeyNotes (){
		return noteNames;
	}
	
	public ArrayList<String> getAllKeyNotesStrings (){
		return noteNamesStrings;
	}
	
	public void loadMajorChords (ArrayList <Note> noteNames){
		allKeysChords = new ArrayList <GivenKeyChords>();
	Scale ionionScale = null;
	for (Note aNote : noteNames){
		 ionionScale = ListOfScales.getInstance().majorOrIonionScale(aNote);
		ListOfChords.getInstance().setMajorChords(ionionScale);
		ArrayList<Chord> loadedScaleChords = getMajorChords();
		GivenKeyChords givenKeyChords = new GivenKeyChords(aNote.getName(),loadedScaleChords);
		allKeysChords.add(givenKeyChords);
	}
	GivenKeyScales givenKeyScale = new GivenKeyScales(ionionScale.getScaleName(),allKeysChords);
	storeKeyScaleChords(givenKeyScale);
}
	
	public void loadMinorChords (ArrayList <Note> noteNames){
		allKeysChords = new ArrayList <GivenKeyChords>();
		Scale minorScale = null;
		for (Note aNote : noteNames){
			 minorScale = ListOfScales.getInstance().minorOrAeolianScale(aNote);
			ListOfChords.getInstance().setMinorChords(minorScale);
			ArrayList<Chord> loadedScaleChords = getMinorChords();
			GivenKeyChords givenKeyChords = new GivenKeyChords(aNote.getName(),loadedScaleChords);
			allKeysChords.add(givenKeyChords);
		}
		GivenKeyScales givenKeyScale = new GivenKeyScales(minorScale.getScaleName(),allKeysChords);
		storeKeyScaleChords(givenKeyScale);
	}
	
	public void loadHalfDimishedChords (ArrayList <Note> noteNames){
		allKeysChords = new ArrayList <GivenKeyChords>();
		Scale halfDiminishedScale = null;
		for (Note aNote : noteNames){
			halfDiminishedScale = ListOfScales.getInstance().halfDiminishedScale(aNote);
			ListOfChords.getInstance().setHalfDimishedChords(halfDiminishedScale);
			ArrayList<Chord> loadedScaleChords = getHalfDimishedChords();
			GivenKeyChords givenKeyChords = new GivenKeyChords(aNote.getName(),loadedScaleChords);
			allKeysChords.add(givenKeyChords);
		}
		GivenKeyScales givenKeyScale = new GivenKeyScales(halfDiminishedScale.getScaleName(),allKeysChords);
		storeKeyScaleChords(givenKeyScale);
	}
	
	public void loadFullyDiminishedScaleChords (ArrayList <Note> noteNames){
		allKeysChords = new ArrayList <GivenKeyChords>();
		Scale fullyDiminishedScaleChords = null;
		for (Note aNote : noteNames){
			fullyDiminishedScaleChords = ListOfScales.getInstance().fullyDiminishedScale(aNote);
			ListOfChords.getInstance().setFullyDimishedScaleChords(fullyDiminishedScaleChords);
			ArrayList<Chord> loadedScaleChords = getFullyDimishedScaleChords();
			GivenKeyChords givenKeyChords = new GivenKeyChords(aNote.getName(),loadedScaleChords);
			allKeysChords.add(givenKeyChords);
		}
		GivenKeyScales givenKeyScale = new GivenKeyScales("No Scale",allKeysChords);
		storeKeyScaleChords(givenKeyScale);
	}
	
	//Adds each full set of chords for each key in all scales 
	public void storeKeyScaleChords(GivenKeyScales givenKeyScale) {
		allScalesChords.add(givenKeyScale);
	}
	
	public ArrayList<GivenKeyScales> getKeyScaleChords() {
		return allScalesChords;
	}
	
	public Chord getChordFromKeyScale(String noteName,String scaleName) {
		noteName +=3;
		int i = 0;
		ArrayList <String> MajorChords= Chord.getMajorEnums();
		ArrayList <String> MinorChords= Chord.getMinorEnums();
		ArrayList <String> HalfDimishedChords= Chord.getHalfDimsEnums();
		ArrayList <String> FullyDimishedChords= Chord.getFullDimsEnums();
		
		
		
				if (MajorChords.contains(scaleName)){
					i =0;
				}
				else if (MinorChords.contains(scaleName)){
					i =1;
				}
				else if (HalfDimishedChords.contains(scaleName)){
					i =2;
				}
				else if (FullyDimishedChords.contains(scaleName)){
					i =3;
				}
				else {return null;}

		GivenKeyChords found = null;
		for (GivenKeyChords test: allScalesChords.get(i).getScaleKeys()){
                   if (test.getKeyName().equals(noteName)){
                	   found = test;
                	   break;
                   }
		}
		Chord foundChord = null;
		for (Chord findChord: found.getKeyChords()){
            if (findChord.getChordName().equals(scaleName)){
            	foundChord = findChord;
         	   break;
            }
        }
		return foundChord;
	}
	
	
	public void setMajorChords(Scale carriedScale) {
		allMajorChords = new ArrayList<Chord>();
		allMajorChords.add(majorTetraChord(carriedScale));
		allMajorChords.add(majorChord(carriedScale));
		allMajorChords.add(majorSixthChord(carriedScale));
		allMajorChords.add(majorSeventhChord(carriedScale));
		allMajorChords.add(majorNinthChord(carriedScale));	
		allMajorChords.add(majorThirteenChord(carriedScale));
		
		allMajorChords.add(suspendedFourthChord(carriedScale));
		allMajorChords.add(suspendedSecondChord(carriedScale));
		
		allMajorChords.add(dominantNinthChord(carriedScale)); 
		allMajorChords.add(dominantThirteenChord(carriedScale));
		allMajorChords.add(dominantEleventhChord(carriedScale));
	
		//Takes part of major scale
		allMajorChords.add(augmentedChord(carriedScale));
		allMajorChords.add(addTwoChord(carriedScale));
		allMajorChords.add(addNineChord(carriedScale));
		allMajorChords.add(dominantNinthChord(carriedScale));
		allMajorChords.add(dominantSeventhChord(carriedScale));
		
		//allMajorChords.add(sevenSharpFiveChord(carriedScale));
		//allMajorChords.add(sevenFlatFiveChord(carriedScale));
	}

	public ArrayList<Chord> getMajorChords() {
		return allMajorChords;
	}
	
	
	//NATURAL MINOR SCALE (AOELIAN) AND MINOR MODE SCALE CHORDS
	////////////////////////////////////////////////////////////
	public void setMinorChords(Scale carriedScale) {
		allMinorChords = new ArrayList<Chord>();
		allMinorChords.add(minorTetraChord(carriedScale));
		allMinorChords.add(minorChord(carriedScale));
		allMinorChords.add(minorSeventhChord(carriedScale));
		allMinorChords.add(minorNinthChord(carriedScale));
		allMinorChords.add(minorEleventhChord(carriedScale));
		allMinorChords.add(minorThirteenChord(carriedScale));
		allMinorChords.add(minorSixthChord(carriedScale));
		
		//Based on minor scale
		allMinorChords.add(minorMajorSevenChord(carriedScale));
	}
	
	public ArrayList<Chord> getMinorChords() {
		return allMinorChords;
	}
	
//	//Just like the Ionian scale, but with an augmented fourth
//	public void setLydianChords(Scale carriedScale) {
//		allLydianChords = new ArrayList<Chord>();
//		allLydianChords.add(majorSeventhChord(carriedScale)); 												
//	}
//	
//	public void setAllMixolydianChords(Scale carriedScale) {
//		allMixolydianChords = new ArrayList<Chord>();
//		allMixolydianChords.add(dominantSeventhChord(carriedScale));
//	}

	
	//NATURAL MINOR SCALE (AOELIAN) AND MINOR MODE SCALE CHORDS
	////////////////////////////////////////////////////////////
//	public void setNonScaleChords(Scale carriedScale) {
//		allNonScaleChords = new ArrayList<Chord>();
//		
//		//Kind of based of major scales
//		allNonScaleChords.add(sevenFlatFiveChord(carriedScale));
//		allNonScaleChords.add(sevenSharpFiveChord(carriedScale));
//		
//		//This is not based on half dim, fully dim or dom dim due to the 7th note pitch step
//		//allFullyDimishedChords.add(dimishedMajorSeventh(carriedScale));
//
//	}
	
//	public ArrayList<Chord> getNonScaleChords() {
//		return allNonScaleChords;
//	}
	
	
	    //Fully diminished chords
		////////////////////////////////////////////////////////////
		public void setFullyDimishedScaleChords(Scale carriedScale) {
			allFullyDimishedChords = new ArrayList<Chord>();	
			allFullyDimishedChords.add(dimishedChord(carriedScale));
			allFullyDimishedChords.add(dimishedSeventhChord(carriedScale));
			
			//sevenFlatFive chord
		}
		
		public ArrayList<Chord> getFullyDimishedScaleChords() {
			return allFullyDimishedChords;
		}
		
		
		 //Dominant diminished chords
		////////////////////////////////////////////////////////////
		public void setDominantDimishedScaleChords(Scale carriedScale) {
			allDominantDimishedChords = new ArrayList<Chord>();	
			//C7FLAT9
			
			//13b9,#9,#11 
			
		}
		
		public ArrayList<Chord> getDominantDimishedScaleChords() {
			return allDominantDimishedChords;
		}
	
	
	
	//HALF DIMINISHED SCALE CHORDS ////////////////////////
	public void setHalfDimishedChords(Scale carriedScale) {
		allHalfDimishedChords = new ArrayList<Chord>();
		allHalfDimishedChords.add(minorSevenFlatFiveChord(carriedScale));
		
	}
	public ArrayList<Chord> getHalfDimishedChords() {
		return allHalfDimishedChords;
	}
	//////////////////////////////
	
	
	//Dorian mode is similar to Aoelian model, the only difference is the 6th degree is
	// major 6th above root than a minor 6
//	public void setDorianChords(Scale carriedScale) {
//		allDorianChords = new ArrayList<Chord>();
//		allDorianChords.add(minorSixthChord(carriedScale));
//		allDorianChords.add(minorSeventhChord(carriedScale));
//		allDorianChords.add(minorNinthChord(carriedScale));
//	}
//	
//	//Also like Aoelian, but it has a minor second note, not a major.
//	public void setPhrygianChords(Scale carriedScale) {
//		allDorianChords = new ArrayList<Chord>();
//		allDorianChords.add(minorSixthChord(carriedScale));
//		allDorianChords.add(minorSeventhChord(carriedScale));
//		allDorianChords.add(minorNinthChord(carriedScale));
//	}

//	// Generics omitted to allow multiple enum types to be arguments in the
//	// function public void findSpecificChordType(ArrayList <Chord> loadedChords, 
//	// Enum [] chordNames) throws InvalidMidiDataException{
	public void findSpecificChordType(ArrayList<Chord> loadedChords) throws InvalidMidiDataException {
		// ADD USER INPUT DETECTION FROM AVAILABLE SCALES
		String chordName = "dom9";
		Chord foundChord = null;
		for (Chord aChord : loadedChords) {
			if (aChord.getChordName().equals(chordName)) {
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
//	public void playAnyChordLength(Chord foundChord) throws InvalidMidiDataException {
//		ArrayList<Note> notesInChord = foundChord.getChordNotes();
//		for (Note aNote : notesInChord) {
//			ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0, aNote.getPitch(), 100);
//			MidiReceiver.getInstance().send(noteOne, -1);
//		}
//	}

	// If user types first inversion for chord, then to get second inversion,
	// recall this function with a boolean condition
	public void firstAndSecondInversion(Chord carriedChord) throws InvalidMidiDataException {
		
		// Repeat using previous stored array for second inversion.
		// Must have done first inversion first though
		if (inverted == true) {
			carriedChord = getCurrentInversion();

		}
		ArrayList<Note> notesInChord = new ArrayList<Note>();
		for (Note aNote : carriedChord.getChordNotes())	{
			notesInChord.add(aNote);
		}	
		

		// Replace key with one octave higher, swap with last element, and
		// save array in memory
		Note increaseRootOctave = ListOfScales.getInstance().getKey(notesInChord.get(0), 12);
		
		notesInChord.set(0, increaseRootOctave);

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
		storeCurrentInversion(editedChord);
		inverted = true;

	}

	public void resetInversion (){
		inverted = false;
	}
	
	public void storeFirstInversion(Chord original) {
		firstInversion = original;
	}
	
	public Chord getFirstInversion() {
		return firstInversion;
	}
	
	
	public void storeCurrentInversion(Chord current) {
		currentInversion = current;
	}

	public Chord getCurrentInversion() {
		return currentInversion;
	}

	// This function can be used to jump straight to 2nd inversion, whilst
	// the first inversion can be used twice for 1st to 2nd inversion
	public void secondInversion(Chord carriedChord) throws InvalidMidiDataException, InterruptedException {
		// Note [] second = getCurrentFirstInversion();
		ArrayList<Note> notesInChord = new ArrayList<Note>();
		for (Note aNote : carriedChord.getChordNotes())	{
			notesInChord.add(aNote);
		}	
		

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
		PlaybackFunctions.playAnyChordLength(editedChord);
	}

	// Uses Ionian scale's first 4 scale degrees
	public Chord majorTetraChord(Scale carriedScale) {;
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getSubTonic(); // 2
		thirdNote = carriedScale.getMediant(); // 4
		fourthNote = carriedScale.getSubDominant(); // 5
		Chord aChord = new Chord("majTetra", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}
	
	// Uses Aeolian scale's first 4 scale degrees
	public Chord minorTetraChord(Scale carriedScale) {
	rootNote = carriedScale.getTonic(); // 0
	secondNote = carriedScale.getSubTonic(); // 2
	thirdNote = carriedScale.getMediant(); // 4
	fourthNote = carriedScale.getSubDominant(); // 5
	Chord aChord = new Chord("minTetra", rootNote, secondNote, thirdNote, fourthNote);
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
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 4); //8
		Chord aChord = new Chord("aug", rootNote, secondNote, thirdNote);
		return aChord;
	}

	// Diminished cords use the Half Whole Diminished Scale
	public Chord dimishedChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); //6
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
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 10

		Chord aChord = new Chord("7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// A dominant chord could be the ionian or lydian scale
	public Chord majorSeventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSuperTonic(); // 11
		Chord aChord = new Chord("maj7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}

	// This chord can use the dorian scale, phrygian scale, and aeolian scale
	public Chord minorSeventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSuperTonic(); // 10
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
		rootNote = carriedScale.getTonic();  //0
		secondNote = carriedScale.getMediant();  //4
		thirdNote = carriedScale.getDominant();  //7
		fourthNote = carriedScale.getSubMediant(); //9
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
		
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 10
		//fourthNote = carriedScale.getSubMediant(); // 9
		// Not as part of a known scale, but rather an added ninth to the Ionian
		// scale. Uses below function to get next step pitch against former from map of
		// notes
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		Chord aChord = new Chord("9", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	public Chord majorNinthChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = carriedScale.getSuperTonic(); // 11
		// Same as ninth dominant chord
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 3); // 14
		Chord aChord = new Chord("maj9", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	// This chord can use the dorian scale, phrygian scale, and aeolian scale
	public Chord minorNinthChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // [0]; //0
		secondNote = carriedScale.getMediant(); // [2]; //3
		thirdNote = carriedScale.getDominant(); // [4]; //7
		fourthNote = carriedScale.getSuperTonic();// [6]; //10
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		Chord aChord = new Chord("min9", rootNote, secondNote, thirdNote, fourthNote, fifthNote);
		return aChord;
	}

	// This is enharmonically equivalent to the major sixth. Might not be found
	// using a scale name
	public Chord dimishedSeventhChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant();// 6
		fourthNote = carriedScale.getSuperTonic(); // 9
		//secondNote = ListOfScales.getInstance().getKey(rootNote, 3); // 3
		//thirdNote = ListOfScales.getInstance().getKey(secondNote, 3); // 6
		//fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 9
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

	// Ionian scale's 1st,3rd and 5th degree with added ninth
		public Chord addTwoChord(Scale carriedScale) {
			rootNote = carriedScale.getTonic(); // 0
			secondNote = ListOfScales.getInstance().getKey(rootNote, 2); // 2
			thirdNote = ListOfScales.getInstance().getKey(secondNote, 2); // 4
			fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 7
			Chord aChord = new Chord("add2", rootNote, secondNote, thirdNote, fourthNote);
			return aChord;
		}

	public Chord minorEleventhChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 10
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 3); // 17
		
		Chord aChord = new Chord("min11", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote);
		return aChord;
	}

	public Chord dominantEleventhChord(Scale carriedScale) {

		
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 10
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 3); // 17
		


		Chord aChord = new Chord("11", rootNote, secondNote, thirdNote, fourthNote, fifthNote,sixthNote);
		return aChord;
	}

	public Chord dominantThirteenChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 10
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 3); // 17
		seventhNote = ListOfScales.getInstance().getKey(sixthNote, 4); // 21
			
		Chord aChord = new Chord("13", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote,seventhNote);
		return aChord;

	}

	public Chord minorThirteenChord(Scale carriedScale) {

		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 3); // 10
		fifthNote = ListOfScales.getInstance().getKey(fourthNote, 4); // 14
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 3); // 17
		seventhNote = ListOfScales.getInstance().getKey(sixthNote, 4); // 21
		
		Chord aChord = new Chord("min13", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote,seventhNote);
		return aChord;
	}

	public Chord majorThirteenChord(Scale carriedScale) {
		
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = carriedScale.getDominant(); // 7
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 11
		fifthNote = ListOfScales.getInstance().getKey(fourthNote,3); // 14
		sixthNote = ListOfScales.getInstance().getKey(fifthNote, 3); // 17
		seventhNote = ListOfScales.getInstance().getKey(sixthNote, 4); // 21
		Chord aChord = new Chord("maj13", rootNote, secondNote, thirdNote, fourthNote, fifthNote, sixthNote,seventhNote);
		return aChord;
	}
	
	//NON SCALE BASED CHORDS
	/////////////////////////////////////
	//Based of major
	// Enharmonically equivalent to the French sixth chord.
	public Chord sevenFlatFiveChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 4
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 2); // 6
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 10
		Chord aChord = new Chord("7b5", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}
	
		public Chord sevenSharpFiveChord(Scale carriedScale) {
			rootNote = carriedScale.getTonic(); // 0
			secondNote = carriedScale.getMediant(); // 4
			thirdNote = ListOfScales.getInstance().getKey(secondNote, 4); // 8
			fourthNote = ListOfScales.getInstance().getKey(thirdNote, 2); // 10
			Chord aChord = new Chord("7#5", rootNote, secondNote, thirdNote, fourthNote);
			return aChord;
		}
	

	//Based of minor scale
	////////////////////////////////////
	public Chord minorMajorSevenChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
		secondNote = carriedScale.getMediant(); // 3
		thirdNote = ListOfScales.getInstance().getKey(secondNote, 4); // 7
		fourthNote = ListOfScales.getInstance().getKey(thirdNote, 4); // 11
		Chord aChord = new Chord("minMaj7", rootNote, secondNote, thirdNote, fourthNote);
		return aChord;
	}
	
	
	//Based on locrian #2 (half-Diminished scale)
		public Chord minorSevenFlatFiveChord(Scale carriedScale) {
		rootNote = carriedScale.getTonic(); // 0
			secondNote = carriedScale.getMediant(); // 4
			thirdNote = carriedScale.getDominant(); // 6
			fourthNote = carriedScale.getSuperTonic(); // 10
			Chord aChord = new Chord("min7b5", rootNote, secondNote, thirdNote, fourthNote);
			return aChord;
		}
}
