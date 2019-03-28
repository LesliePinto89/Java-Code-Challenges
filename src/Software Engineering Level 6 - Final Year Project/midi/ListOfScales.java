package midi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.InvalidMidiDataException;

import keyboard.Note;

public class ListOfScales {

	private static volatile ListOfScales instance = null;

	
	private ListOfScales() {
	}

	public static ListOfScales getInstance() {
		if (instance == null) {
			synchronized (ListOfScales.class) {
				if (instance == null) {
					instance = new ListOfScales();
				}
			}
		}
		return instance;
	}

	private Map<String, Note> mapOfNotes = Note.getNotesMap();
	private Collection <Integer> currentScaleIntervals;
	private Scale currentDisplayedScaleColor;
	
	private ArrayList<Scale> diatonicMajorScales = new ArrayList<Scale>();
	private ArrayList<Scale> diatonicMinorScales = new ArrayList<Scale>();

	//Store all scales
	public void generateScalesNames (String modelNote){
		modelNote = modelNote +3;
		Note key = mapOfNotes.get(modelNote);

	//Diatonic Scales / Modes
	Scale.storeScales(majorOrIonianScale(key));
	Scale.storeScales(dorianScale(key));
	Scale.storeScales(phrygianScale(key));
	Scale.storeScales(lydianScale(key));
	Scale.storeScales(mixolydianScale(key));
	Scale.storeScales(minorOrAeolianScale(key));
    Scale.storeScales(locrianScale(key));
     ///////////////
	
	Scale.storeScales(majorPentatonicScale(key));
	Scale.storeScales(minorPentatonicScale(key));
	Scale.storeScales(bluesScale(key));
	Scale.storeScales(ascendingChromaticScale(key));
	Scale.storeScales(descendingChromaticScale(key));
	Scale.storeScales(augmentedScale(key));
	Scale.storeScales(wholeToneScale(key));
    
    Scale.storeScales(harmonicMinorScale(key)); //Up to  here it works
    Scale.storeScales(ascendingMelodicMinorScale(key)); //This affects it  (This kind of works here)
    Scale.storeScales(alteredScale(key));
    
    Scale.storeScales(halfDiminishedScale(key));
    Scale.storeScales(dominantDiminishedScale(key));//This affects it too
    Scale.storeScales(fullyDiminishedScale(key));
}
	
	
	
	
	
	//Diatonic majors
	///////////////////////////////////////////////////
	public void keyDiatonicMajorScales(Scale current){
		diatonicMajorScales.add(current);
	}
	
	public ArrayList<Scale> getDiatonicMajorScales(){
		return diatonicMajorScales;
	}
	
	//Diatonic minor
		///////////////////////////////////////////////////
	public void keyDiatonicMinorScales(Scale current){
		diatonicMinorScales.add(current);
	}
	
	public ArrayList<Scale> getDiatonicMinorScales(){
		return diatonicMinorScales;
	}
	
	////////////////////////////////////////////////
	
	public void displayedScaleNotes(Scale currentScale){
		currentDisplayedScaleColor = currentScale;
	}
	
	public Scale getDisplayedScaleNotes(){
		return currentDisplayedScaleColor;
	}

	public void getMajorModes(ListOfChords instance, Scale foundScale) throws InvalidMidiDataException{
		instance.setMajorChords(foundScale);
		instance.findSpecificChordType(instance.getMajorChords());
	}
	
	public void getMinorModes(ListOfChords instance, Scale foundScale) throws InvalidMidiDataException{
		instance.setMajorChords(foundScale);
		instance.findSpecificChordType(instance.getMinorChords());
	}
		
	// If user chooses to find chords in diatonic scale using a given rootkey
	public void findMode(String userNoteKey, String chosenScale) throws InvalidMidiDataException {
		ListOfChords listChordsinstance = ListOfChords.getInstance();

		// If user types in on octave number ignore, but if not apply
		// default 3 near middle C on 61 key piano
		Pattern p2 = Pattern.compile("\\d");
		Matcher m2 = p2.matcher(userNoteKey);
		if (!m2.find()) {
			userNoteKey = userNoteKey + "3";
		}

		Note foundNote = mapOfNotes.get(userNoteKey);
		Scale findScale = null;
		switch (chosenScale){
		
//		case "maj": findScale = majorOrIonionScale(foundNote);
//		            getMajorModes(listChordsinstance,findScale);
//					break;
//
//		case "min": findScale = minorOrAeolianScale(foundNote);
//		           getMinorModes(listChordsinstance,findScale);
//					break;
		
		case "lydian": findScale = lydianScale(foundNote);
		getMajorModes(listChordsinstance,findScale);
		break; //variant of major
		
		case "mixolydian": 	findScale = mixolydianScale(foundNote);
		getMajorModes(listChordsinstance,findScale);
		break; //variant of major
		
		case "phrygian": findScale = phrygianScale(foundNote);
		 getMinorModes(listChordsinstance,findScale);
		 break; //variant of minor
		 
		case "dorian": findScale = dorianScale(foundNote);
		 getMinorModes(listChordsinstance,findScale);
		 break; //variant of minor
		 
		case "locrian": findScale = locrianScale(foundNote);
		 getMinorModes(listChordsinstance,findScale);
		 break; //can be a variant of major or minor
		}
	}

	public void currentScalePitchValues(Collection <Integer> intervals){
		currentScaleIntervals = intervals;
	}
	
	public Collection <Integer> getScalePitchValues(){
		return currentScaleIntervals;
	}
	
	
	
	// Find next interval / step based on previous note in scale
	public Note getKey(Note passedNote, int step) {
		
		for (Entry<String, Note> entry : mapOfNotes.entrySet()) {

			// For some reason, the minus arithmetic increases the compared
			// value. e.g: step = 2, To find 48 compared to 46, this makes 48
			// rather than 44.
			if (passedNote.getPitch() == entry.getValue().getPitch() - step)
			//if (passedNote.getPitch() == entry.getValue().getPitch() - step)
			{
				String key = entry.getKey();
				Note stepNote = mapOfNotes.get(key);
				return stepNote;
			}
			
		}
		//Cant remember why this is here
		//ListOfChords.getInstance().getKeyScaleChords();g
		return null;
	}

	// The 5 note pentatonic scale is the relative minor of a major scale -
	// similar to  the normal minor or Aeolian scale. It's root is the 
	// major's scales submediant note. There is no supertonic in this scale
	public Scale majorPentatonicScale(Note rootKey) {
		String scaleName = "Pentatonic Major";
		Note tonic = rootKey; //do
		Note mediant = getKey(tonic, 2); //re
		Note subDominant = getKey(mediant, 2); //mi
		Note dominant = getKey(subDominant, 3); //so
		Note superTonic = getKey(dominant, 2); //so
		Note endOctaveNote = getKey(superTonic, 3);
		Scale aScale = new Scale(scaleName, tonic, mediant, subDominant, dominant, superTonic, endOctaveNote);
		return aScale;
	}
	
	public Scale minorPentatonicScale(Note rootKey) {
		String scaleName = "Pentatonic Minor";
		Note tonic = rootKey; //do
		Note mediant = getKey(tonic, 3); //re
		Note subDominant = getKey(mediant, 2); //mi
		Note dominant = getKey(subDominant, 2); //so
		Note superTonic = getKey(dominant, 3); //so
		Note endOctaveNote = getKey(superTonic, 2);
		Scale aScale = new Scale(scaleName, tonic, mediant, subDominant, dominant, superTonic, endOctaveNote);
		return aScale;
	}
	
	
	//HEXATONIC SCALES
    //////////////////////////////////////
		// This allows 6 and is made up of two augmented chords. Due to the 
		// existing symmetry in this scale, 3 from its 6 notes from a
		// given key can be considered the tonic.
		public Scale augmentedScale(Note rootKey) {
			String scaleName = "Augmented";
			Note tonic = rootKey;
			Note subtonic = getKey(tonic, 3);
			Note mediant = getKey(subtonic, 1);
			Note subdominant = getKey(mediant, 3);
			Note dominant = getKey(subdominant, 1);
			Note submediant = getKey(dominant, 3);
			Note endOctaveNote = getKey(submediant, 1); 			
			Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
					endOctaveNote);
			return aScale;
		}
		
		public Scale bluesScale(Note rootKey) {
			String scaleName = "Blues";
			Note tonic = rootKey; //do
			Note subTonic = getKey(tonic, 3); //re
			Note mediant = getKey(subTonic, 2); //re
			Note subDominant = getKey(mediant, 1); //mi
			Note dominant = getKey(subDominant, 1); //so
			Note superTonic = getKey(dominant, 3); //so
			Note endOctaveNote = getKey(superTonic, 2);
			Scale aScale = new Scale(scaleName, tonic, subTonic, mediant, subDominant, dominant, superTonic, endOctaveNote);
			return aScale;
		}

		// Another hexatonic scale, its 6 notes have a whole step interval between
		// each note. There is no supertonic in this scale
		public Scale wholeToneScale(Note rootKey) {
			String scaleName = "Whole Tone";
			Note tonic = rootKey;
			Note subtonic = getKey(tonic, 2);
			Note mediant = getKey(subtonic, 2);
			Note subdominant = getKey(mediant, 2);
			Note dominant = getKey(subdominant, 2);
			Note submediant = getKey(dominant, 2);
			Note endOctaveNote = getKey(submediant, 2);
			Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
					endOctaveNote);
			return aScale;
		}
	
		////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////
		
	// A diatonic scale is an heptatonic scale that is broken into 5 whole
	// notes and 2 semi-tones. There are 7 modes of the Diatonic Major Scale,
	// each one being based off one of the notes in a 7 note octave.
	public Scale majorOrIonianScale(Note rootKey) {
		String scaleName = "Diantonic Ionian";
		
		
		Note tonic = rootKey;     
		Note subtonic = getKey(tonic, 2);     
		Note mediant = getKey(subtonic, 2);     
		Note subdominant = getKey(mediant, 1);    
		Note dominant = getKey(subdominant, 2);     
		Note submediant = getKey(dominant, 2);    
		Note supertonic = getKey(submediant, 2);    
		Note endOctaveNote = getKey(supertonic, 1);     
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	public Scale dorianScale(Note rootKey) {
		String scaleName = "Diantonic Dorian";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 1);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 1);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	public Scale phrygianScale(Note rootKey) {
		String scaleName = "Diatonic Phrygian";
		Note tonic = rootKey; // Is a semi note
		Note subtonic = getKey(tonic, 1);
		Note mediant = getKey(subtonic, 2);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 1);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	public Scale lydianScale(Note rootKey) {
		String scaleName = "Diatonic Lydian";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 2);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 1);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 1);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	public Scale mixolydianScale(Note rootKey) {
		String scaleName = "Diatonic Mixolydian";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 2);
		Note subdominant = getKey(mediant, 1);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 1);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	// The scale "natural minor" is an Aeolian Scale based of the 6th note in a
	// major scale that is also the same as the Ionian scale
	public Scale minorOrAeolianScale(Note rootKey) {
		String scaleName = "Diatonic Aeolian";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 1);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 1);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	public Scale locrianScale(Note rootKey) {
		String scaleName = "Diatonic Locrian";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 1);
		Note mediant = getKey(subtonic, 2);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 1);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	////////////////////////////////////////////////////////////////
	
	// This scale covers all pitches and is made up of only semi-tones.
	// Because each pitch in the scale is equidistant, there is no tonic,
	// so it is known as a non-diatonic scale.It uses 12-tone equal temperament
	// and it starts from C to gain each octave's pitches value.
	private static String[][] createChromaticScale = { { "C" }, { "Db", "C#" }, { "D" }, { "Eb", "D#" }, { "E" },
			{ "F" }, { "Gb", "F#" }, { "G" }, { "Ab", "G#" }, { "A" }, { "Bb", "A#" }, { "B" } };

	public static String[][] getChromaticScale() {
		return createChromaticScale;
	}
	
	public Scale ascendingChromaticScale(Note rootKey) {
		String scaleName = "Ascending Chromatic";
		Note Do = rootKey;   //note name is in capital as java does not allow do keyword as name
		Note di = getKey(Do, 1);
		Note re = getKey(di, 1);
		Note ri = getKey(re, 1);
		Note mi  = getKey(ri, 1);
		Note fa = getKey(mi, 1);
		Note fi = getKey(fa, 1);
		Note sol = getKey(fi, 1);
		Note si = getKey(sol, 1);
		Note la = getKey(si, 1);
		Note li = getKey(la, 1);
		Note ti = getKey(li, 1);
		Scale aScale = new Scale(scaleName, Do, di, re, ri, mi, fa, fi,sol, si,la,li,ti);
		return aScale;

	}
	
	
	public Scale descendingChromaticScale(Note rootKey) {
		String scaleName = "Descending Chromatic";
		Note Do = rootKey;   //note name is in capital as java does not allow do keyword as name
		Note ti = getKey(Do, -1);
		Note te = getKey(ti, -1);
		Note la = getKey(te, -1);
		Note le  = getKey(la, -1);
		Note sol = getKey(le, -1);
		Note se = getKey(sol, -1);
		Note fa = getKey(se, -1);
		Note mi = getKey(fa, -1);
		Note me = getKey(mi, -1);
		Note re = getKey(me, -1);
		Note ra = getKey(re, -1);
		Scale aScale = new Scale(scaleName, Do, ti, te, la, le, sol, se,fa, mi,me,re,ra);
		return aScale;
	}

	// A harmonic scale is just a natural minor scale (Aeolian), but with an 1
	// and 1/2 step seventh and a half step from the seventh and eight note.
	public Scale harmonicMinorScale(Note rootKey) {
		String scaleName = "Harmonic Minor";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 1);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 1);
		Note supertonic = getKey(submediant, 3);
		Note endOctaveNote = getKey(supertonic, 1);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;

	}

	public Scale ascendingMelodicMinorScale(Note rootKey) {
		String scaleName = "Melodic Minor";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 1);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 1);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}

	// This scale ascends using melodic scale but descends using the natural minor scale
	public void descendingMelodicMinorScale(Note rootKey) {
		minorOrAeolianScale(rootKey);
	}

	//Also known as the half-diminished scale, it has  7 notes not 8. 
	public Scale halfDiminishedScale(Note rootKey) {
		String scaleName = "Half-Dimished";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2); // 2nd note is major, not minor
		Note mediant = getKey(subtonic, 1);  
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 1);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant,
				supertonic, endOctaveNote);
		return aScale;
	}
	
	//Altered scale - This is also known as the super locrian or the diminished whole tone
	public Scale alteredScale(Note rootKey) {
		String scaleName = "Altered";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 1);
		Note mediant = getKey(subtonic, 2);
		Note subdominant = getKey(mediant, 1);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 2);
		Note endOctaveNote = getKey(supertonic, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant, dominant, submediant, supertonic,
				endOctaveNote);
		return aScale;
	}
	
	// OCTATONIC SCALES HAVE 8 NOTES
	//Dominant diminished (also called half-whole). Its step intervals alternate half/whole
	public Scale dominantDiminishedScale(Note rootKey) {
		String scaleName = "Dominant Diminished";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 1);
		Note mediant = getKey(subtonic, 2);
		Note subdominant = getKey(mediant, 1);
		Note dominant = getKey(subdominant, 2);
		Note submediant = getKey(dominant, 1);
		Note supertonic = getKey(submediant, 2);
		Note eigthNote = getKey(supertonic, 1);
		Note endOctaveNote = getKey(eigthNote, 2);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant,subdominant, dominant, submediant, supertonic, eigthNote,
				endOctaveNote);
		return aScale;
	}
	
	// This octatonic scale is also called the Whole-half, diminished or
	// diminished 7th scale.Its step intervals alternate whole/half
	public Scale fullyDiminishedScale(Note rootKey) {
		String scaleName = "Fully Diminished";
		Note tonic = rootKey;
		Note subtonic = getKey(tonic, 2);
		Note mediant = getKey(subtonic, 1);
		Note subdominant = getKey(mediant, 2);
		Note dominant = getKey(subdominant, 1);
		Note submediant = getKey(dominant, 2);
		Note supertonic = getKey(submediant, 1);
		Note eigthNote = getKey(supertonic, 2);
		Note endOctaveNote = getKey(eigthNote, 1);
		Scale aScale = new Scale(scaleName, tonic, subtonic, mediant, subdominant,dominant, submediant, supertonic, eigthNote,
				endOctaveNote);
		return aScale;
	}
}


class GivenKeyScales {
	private String scaleName = "";
	private ArrayList<GivenKeyChords> ScaleKeys;

	public GivenKeyScales(String scaleName, ArrayList<GivenKeyChords> aKeyChords) {
		this.scaleName = scaleName;
		this.ScaleKeys = aKeyChords;
	}

	public ArrayList<GivenKeyChords> getScaleKeys() {
		return ScaleKeys;
	}
}