package midi;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import keyboard.Note;
import midi.ChordProgression.chordSymbol;
import midi.ChordProgression.majorChordProgressions;
import midi.ChordProgression.minorChordProgressions;

public class ChordProgressionActions {

	private ChordProgression chordProgression;
	private String chordProgressionString;
	private static int progressionScaleChoice;
	private ArrayList<String> numbers = new ArrayList<String>();
	private chordSymbol[] symbols = chordSymbol.values();

	// Display and user interaction
	private DefaultListModel<String> storeAllChordProgressions = new DefaultListModel<String>();
	private DefaultListModel<String> storeMajorChordProgressions = new DefaultListModel<String>();
	private DefaultListModel<String> storeMinorChordProgressions = new DefaultListModel<String>();

	// Used to get actual chords to be used
	private ArrayList<String> minorAsString = new ArrayList<String>();
	private ArrayList<String> majorAsString = new ArrayList<String>();
	private ArrayList<String> allAsString = new ArrayList<String>();

	private static volatile ChordProgressionActions instance = null;

	private ChordProgressionActions() {
	}

	public static ChordProgressionActions getInstance() {
		if (instance == null) {
			synchronized (ChordProgressionActions.class) {
				if (instance == null) {
					instance = new ChordProgressionActions();
					instance.storeMajorProgressionInList();
					instance.storeMinorProgressionInList();
					instance.symbolsToNumbers();
				}
			}
		}

		return instance;
	}

	public void storeMajorProgressionInList() {

		majorChordProgressions[] majorProgressions = majorChordProgressions.values();
		for (majorChordProgressions prog : majorProgressions) {
			String progresion = prog.chord;
			majorAsString.add(progresion);
			storeMajorChordProgressions.addElement(progresion);
		}
	}
	
	public ArrayList <String> getMajorProgsNames(){
		return majorAsString;
	}

	public DefaultListModel<String> getMajorChordProgressions() {
		return storeMajorChordProgressions;
	}

	public void storeMinorProgressionInList() {
		minorChordProgressions[] minorProgressions = minorChordProgressions.values();
		for (minorChordProgressions prog : minorProgressions) {
			String progresion = prog.chord;
			minorAsString.add(progresion);
			storeMinorChordProgressions.addElement(progresion);
		}
	}

	public DefaultListModel<String> getMinorChordProgressions() {
		return storeMinorChordProgressions;
	}

	public void storeAllProgressionInList() {
		storeMajorProgressionInList();
		storeMinorProgressionInList();
		allAsString.addAll(majorAsString);
		allAsString.addAll(minorAsString);
	}

	public DefaultListModel<String> getAllChordProgressions() {
		return storeAllChordProgressions;
	}

	// Makes a standard progression called Strong, rather than a fractured
	// progression
	public ChordProgression makeChordProgression(String name, Scale currentScale, String[] sections) {

		ArrayList<Chord> tempProgression = new ArrayList<Chord>();
		Chord rootChord = null;
		for (String aString : sections) {
			String type = minorOrMajor(aString);
			int degree = getScaleDegree(aString);

			Note aNote = currentScale.getScaleNote(degree - 1);
			String note = aNote.getName();
			note = note.substring(0, note.length() - 1);
			Chord foundChordsDetails = ListOfChords.getInstance().findChord(aNote.getName(), type);
			Chord editedChord = new Chord(note + foundChordsDetails.getChordName(), foundChordsDetails.getChordNotes());

			if (tempProgression.size() == 0) {
				rootChord = editedChord;
			}
			tempProgression.add(editedChord);
		}
		tempProgression.add(rootChord);
		ChordProgression aProgression = new ChordProgression(name, tempProgression);
		return aProgression;
	}

	public void storeCurrentProgression(ChordProgression current) {
		chordProgression = current;
	}

	public ChordProgression getCurrentProgression() {
		return chordProgression;
	}

	public void storeCurrentProgressionString(String currentProgString) {
		chordProgressionString = currentProgString;
	}

	public String getCurrentProgressionString() {
		return chordProgressionString;
	}

	public static void progressionScale(int choice) {
		progressionScaleChoice = choice;
	}

	public static int getProgressionScale() {
		return progressionScaleChoice;
	}

	public void symbolsToNumbers() {
		numbers = new ArrayList<String>();
		for (chordSymbol sym : symbols) {
			numbers.add(sym.toString());
		}
	}

	public int getScaleDegree(String selectedProgChord) {
		for (chordSymbol sym : symbols) {
			if (selectedProgChord.equals(sym.toString())) {
				return sym.getProg();
			}
		}
		return 0;
	}

	public String minorOrMajor(String selectedProgChord) {
		char[] charArray = selectedProgChord.toCharArray();
		if (Character.isLowerCase(charArray[0])) {
			return "min";
		} else if (Character.isUpperCase(charArray[0])) {
			return "maj";
		}
		return null;
	}
}