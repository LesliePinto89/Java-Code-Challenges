package midi;

import keyboard.Note;

public class Scale extends Note {

	private Note tonic; // Scale degree 1
	private Note supertonic; // Scale degree2
	private Note mediant; // Scale degree 3
	private Note subdominant; // Scale degree 4
	private Note dominant; // Scale degree 5
	private Note submediant; // Scale degree 6
	private Note subTonic; // Scale degree 7
	private Note endOctaveNote; // Scale degree 1 of new scale
	private Note eigthNote; // Used in 8 note scales
	private String scale = "";

	// Pentatonic
	public Scale(String scaleName, Note note1, Note note2, Note note3, Note note4, Note note5, Note newTonic) {
		this.scale = scaleName;
		this.tonic = note1;
		this.supertonic = note2;
		this.mediant = note3;
		this.subdominant = note4;
		this.dominant = note5;
		this.endOctaveNote = newTonic;
	}

	// Hexatonic
	public Scale(String scaleName, Note note1, Note note2, Note note3, Note note4, Note note5, Note note6,
			Note newTonic) {
		this.scale = scaleName;
		this.tonic = note1;
		this.supertonic = note2;
		this.mediant = note3;
		this.subdominant = note4;
		this.dominant = note5;
		this.submediant = note6;
		this.endOctaveNote = newTonic;
	}

	// Heptatonic
	public Scale(String scaleName, Note note1, Note note2, Note note3, Note note4, Note note5, Note note6, Note note7,
			Note newTonic) {
		this.scale = scaleName;
		this.tonic = note1;
		this.supertonic = note2;
		this.mediant = note3;
		this.subdominant = note4;
		this.dominant = note5;
		this.submediant = note6;
		this.subTonic = note7;
		this.endOctaveNote = newTonic;
	}

	// half-whole diminished scale
	public Scale(String scaleName, Note note1, Note note2, Note note3, Note note4, Note note5, Note note6, Note note7,
			Note note8, Note newTonic) {
		this.scale = scaleName;
		this.tonic = note1;
		this.supertonic = note2;
		this.mediant = note3;
		this.subdominant = note4;
		this.dominant = note5;
		this.submediant = note6;
		this.subTonic = note7;
		this.eigthNote = note8;
		this.endOctaveNote = newTonic;
	}

	public String getScale() {
		return scale;
	}

	public Note getTonic() {
		return tonic;
	}

	public Note getSubTonic() {
		return subTonic;
	}

	public Note getMediant() {
		return mediant;
	}

	public Note getSubDominant() {
		return subdominant;
	}

	public Note getDominant() {
		return dominant;
	}

	public Note getSubMediant() {
		return submediant;
	}

	public Note getSuperTonic() {
		return supertonic;
	}

	// Chromatic scale ommited as there is only one instance of it

	// private static volatile Scale instance = null;
	//
	// private Scale() {
	// }
	//
	// public static Scale getInstance() {
	// if (instance == null) {
	// synchronized (Scale.class) {
	// if (instance == null) {
	// instance = new Scale();
	//
	// }
	// }
	// }
	//
	// return instance;
	// }

	// private int[] harmonicMinorScale;
	// private int[] melodicScale;
	//
	// //Stores 7 note scales (This includes diatonic scales)
	// private Note[] heptatonicModelsScales;
	//
	// //Stores 6 note scales (This includes whole tone and augmented scales)
	// private Note[] hexatonicScales;
	//
	// //Stores 5 note scales
	// private int[] pentatonicScale;
	//
	//
	// public void createHexatonicScale(String userNoteKey, int modelNumber) {
	// Pattern p2 = Pattern.compile("\\d");
	// Matcher m2 = p2.matcher(userNoteKey);
	// if (!m2.find()) {
	// userNoteKey = userNoteKey + "3";
	// }
	// Note foundNote = mapOfNotes.get(userNoteKey);
	// switch (modelNumber) {
	// case 1:
	// hexatonicScales = augmentedScale(foundNote);
	// break;
	// case 2:
	// hexatonicScales = wholeToneScale(foundNote);
	// break;
	// }
	//
	// }
	// // If user chooses to find chords in diatonic scale using a given rootkey
	// public void createDiatonicScale(String userNoteKey, String chosenScale)
	// throws InvalidMidiDataException {
	// // String regix = "[A-G]{3}|^[^\\d].*|([\\w&&[^b]])*";
	// // Pattern p1 = Pattern.compile("[A-G],\\d");
	// // Matcher m1 = p1.matcher(userNoteKey);
	// int modelNumber = 0;
	// if (chosenScale.equals("major")){
	// modelNumber =1;
	// }
	// if (chosenScale.equals("minor")){
	// modelNumber =2;
	// }
	//
	// // If user types in on octave number ignore, but if note, apply
	// // a default one to assign the octave to start from
	// Pattern p2 = Pattern.compile("\\d");
	// Matcher m2 = p2.matcher(userNoteKey);
	// if (!m2.find()) {
	// userNoteKey = userNoteKey + "3";
	// }
	//
	// Note foundNote = mapOfNotes.get(userNoteKey);
	//
	// switch (modelNumber) {
	// case 1:
	// heptatonicModelsScales = majorOrIonionScale(foundNote);
	// ListOfChords.getInstance().setIonianChords(heptatonicModelsScales);
	//// Note [][] loadedScaleChords = Chord.getInstance().getIonianChords();
	//
	// //Specify a new key?
	// //Option 1
	//
	// ////Find a specific chord for manipulation?
	// //Chord.majorChordNames [] majorChordNames =
	// Chord.majorChordNames.values();
	// //Chord.getInstance().findSpecificChordType(loadedScaleChords,majorChordNames);
	// break;
	// case 2:
	// heptatonicModelsScales = dorianScale(foundNote);
	// break;
	// case 4:
	// heptatonicModelsScales = lydianScale(foundNote);
	// break;
	// case 5:
	// heptatonicModelsScales = mixolydianScale(foundNote);
	// break;
	// case 6:
	// heptatonicModelsScales = minorOrAeolianScale(foundNote);
	// break;
	// case 3:
	// heptatonicModelsScales = phrygianScale(foundNote);
	// break;
	// case 7:
	// heptatonicModelsScales = locrianScale(foundNote);
	// break;
	// }
	// }
	//
	// // Find next interval / step based on previous note in scale
	// public Note getKey(Note passedNote, int step) {
	// for (Entry<String, Note> entry : mapOfNotes.entrySet()) {
	//
	// // For some reason, the minus arithmetic increases the compared value.
	// //e.g: step = 2, To find 48 compared to 46, this makes 48 rather than 44.
	// if (passedNote.getPitch() == entry.getValue().getPitch() - step)
	//
	// {
	// String key = entry.getKey();
	// Note stepNote = mapOfNotes.get(key);
	// return stepNote;
	// }
	// }
	// return null;
	// }
	//
	// // A diatonic scale is an heptatonic scale that is broken into 5 whole
	// // notes and 2 semi-tones. There are 7 modes of the Diatonic Major Scale,
	// each one
	// // being based off one of the notes in a typical octave.
	//
	// public Note[] majorOrIonionScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey; //1
	// supertonic = getKey(tonic, 2); //2
	// mediant = getKey(supertonic, 2); //3
	// subdominant = getKey(mediant, 1); //4
	// dominant = getKey(subdominant, 2); //5
	// submediant = getKey(dominant, 2); //6
	// subTonic = getKey(submediant, 2); //7
	// endOctaveNote = getKey(subTonic, 1); //8
	//
	//
	//
	// // supertonic = notesmMap.
	// /*
	// * supertonic.setPitch(tonic.getPitch()+2); // 2212221
	// * mediant.setPitch(supertonic.getPitch()+2);
	// * subdominant.setPitch(supertonic.getPitch()+1);
	// * dominant.setPitch(subdominant.getPitch()+2);
	// * submediant.setPitch(dominant.getPitch()+2);
	// * subTonic.setPitch(dominant.getPitch()+2);
	// * endOctaveNote.setPitch(dominant.getPitch()+1); //the tonic of the new
	// * octave would be +1
	// */
	//
	// // mediant = supertonic +1;
	// // subdominant = mediant +2;
	// // dominant = subdominant +2;
	// // submediant = subdominant + 2;
	// // subTonic = submediant+1;
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// public Note[] dorianScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 1);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 2);
	// subTonic = getKey(submediant, 1);
	// endOctaveNote = getKey(subTonic, 2);
	//
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// public Note[] phrygianScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey; // Is a semi note
	// supertonic = getKey(tonic, 1);
	// mediant = getKey(supertonic, 2);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 1);
	// subTonic = getKey(submediant, 2);
	// endOctaveNote = getKey(subTonic, 2);
	//
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	//
	// public Note[] lydianScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 2);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 1);
	// submediant = getKey(dominant, 2);
	// subTonic = getKey(submediant, 2);
	// endOctaveNote = getKey(subTonic, 1);
	//
	//
	//
	//
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// public Note[] mixolydianScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 2);
	// subdominant = getKey(mediant, 1);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 2);
	// subTonic = getKey(submediant, 1);
	// endOctaveNote = getKey(subTonic, 2);
	//
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// // The scale "natural minor" is an Aeolian Scale based of the 6th note in
	// a
	// // major scale that is also the same as the Ionian scale
	// public Note[] minorOrAeolianScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 1);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 1);
	// subTonic = getKey(submediant, 2);
	// endOctaveNote = getKey(subTonic, 2);
	//
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// public Note[] locrianScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 1);
	// mediant = getKey(supertonic, 2);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 1);
	// submediant = getKey(dominant, 2);
	// subTonic = getKey(submediant, 2);
	// endOctaveNote = getKey(subTonic, 2);
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// ////////////////////////////////////////////////////////////////
	//
	//
	// // This scale covers all pitches and is made up of only semi-tones.
	// //Because each pitch in the scale is equidistant, there is no tonic,
	// // so it is known as a non-diatonic scale.It uses 12-tone equal
	// temperament
	// // and it starts from C to gain each octave's pitches value.
	// private static String[][] createChromaticScale = {
	// { "C" }, { "Db", "C#" }, { "D" }, { "Eb", "D#" }, { "E" }, { "F" }, {
	// "Gb", "F#" }, { "G" }, { "Ab", "G#" },
	// { "A" }, { "Bb", "A#" }, { "B" } };
	//
	// public static String[][] getChromaticScale() {
	// return createChromaticScale;
	// }
	//
	// // A harmonic scale is just a natural minor scale (Aeolian), but with an
	// 1 and 1/2 step
	// // seventh and a half step from the seventh and eight note.
	// public Note[] harmonicMinorScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 1);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 1);
	// subTonic = getKey(submediant, 3);
	// endOctaveNote = getKey(subTonic, 1);
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// public Note [] ascendingMelodicMinorScale(Note rootKey) {
	//
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 1);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 2);
	// subTonic = getKey(submediant, 2);
	// endOctaveNote = getKey(subTonic, 1);
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// //This scale descends using he natural minor scale, but ascends using
	// ///the melodic scale - steps pattern is TSTTSTT or 2122122
	// public void descendingMelodicMinorScale(Note rootKey) {
	// }
	//
	// //While diatonics scale is based of the heptatonic scale, the Augmented
	// scale
	// //is based on the hexatonic scale. This allows 6 and is made up of two
	// augmented chords.
	// //Due to the existing symmetry in this scale, 3 from its 6 notes from a
	// given key
	// // can be considered the tonic.
	// public Note [] augmentedScale(Note rootKey) {
	//
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 3);
	// mediant = getKey(supertonic, 1);
	// subdominant = getKey(mediant, 3);
	// dominant = getKey(subdominant, 1);
	// submediant = getKey(dominant, 3);
	// //subTonic = getKey(submediant, 1);
	// endOctaveNote = getKey(subTonic, 1); //either 3 or 1
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// //Another hexatonic scale, its 6 notes have a whole step interval between
	// each note.
	// public Note [] wholeToneScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 2);
	// subdominant = getKey(mediant, 2);
	// dominant = getKey(subdominant, 2);
	// submediant = getKey(dominant, 2);
	// //subTonic = getKey(submediant, 1); There is no subtonic in this scale
	// endOctaveNote = getKey(submediant, 2);
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// //The 5 note pentatonic scale is the relative minor of a major scale -
	// similar to
	// // the normal minor or Aeolian scale. It's root is the major's scales
	// submediant
	// // note,
	// public Note [] majorPentatonicScale(Note rootKey) {
	// Note[] tempArray;
	// submediant = rootKey;
	// tonic = getKey(submediant, 2);
	// supertonic = getKey(tonic, 2);
	// mediant = getKey(supertonic, 3);
	// dominant = getKey(mediant, 2);
	//
	// //subTonic = getKey(submediant, 1); There is no subtonic in this scale
	// endOctaveNote = getKey(dominant, 2);
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant,
	// endOctaveNote };
	// return tempArray;
	// }
	//
	// //This is an 8 note scale use for diminished chords. Its step intervals
	// // are the opposite of the augmented scale
	// public Note [] halfWholeDiminishedScale(Note rootKey) {
	// Note[] tempArray;
	// tonic = rootKey;
	// supertonic = getKey(tonic, 1);
	// mediant = getKey(supertonic, 3);
	// subdominant = getKey(mediant, 1);
	// dominant = getKey(subdominant, 3);
	// submediant = getKey(dominant, 1);
	// subTonic = getKey(submediant, 3);
	// eigthNote = getKey(subTonic, 1);
	// //endOctaveNote = getKey(submediant, 1);
	// endOctaveNote = getKey(eigthNote, 3);
	// tempArray = new Note[] { tonic, supertonic, mediant, subdominant,
	// dominant, submediant, subTonic,eigthNote,endOctaveNote };
	// return tempArray;
	// }

}
