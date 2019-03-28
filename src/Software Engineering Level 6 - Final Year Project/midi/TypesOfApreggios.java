package midi;

import java.util.ArrayList;
import java.util.Collections;
import javax.sound.midi.InvalidMidiDataException;
import keyboard.Note;
import tools.PlaybackFunctions;

public class TypesOfApreggios {

	private ArrayList<Note> brokenNotes = new ArrayList<Note>();
	private String[] brokenNotesNames;
	private String boxChoice = "";
	private ListOfScales scales = ListOfScales.getInstance();

	private static volatile TypesOfApreggios instance = null;

	private TypesOfApreggios() {
	}

	public static TypesOfApreggios getInstance() {
		if (instance == null) {
			synchronized (TypesOfApreggios.class) {
				if (instance == null) {
					instance = new TypesOfApreggios();
					instance.createBox();
				}
			}
		}

		return instance;
	}

	public void createApreggio(String name, Chord currentProgChord) {
		try {
			switch (name) {
			case "Clean":
				break;
			
			case "Adventure":
				adventureApreggio(currentProgChord);
				playApreggio(200, 62);
				break;
			
			case "Fine":
				fineApreggio(currentProgChord);
				playApreggio(300, 62);
				break;
			
			case "Reflective":
				reflectiveApreggio(currentProgChord);
				playApreggio(400, 62);
				break;

			case "Exciting":
				excitingApreggio(currentProgChord);
				playApreggio(150, 0);
				break;
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public void storeCurrentChoice(String choice) {
		boxChoice = choice;
	}

	public String getCurrentChoice() {
		return boxChoice;
	}

	public void fineApreggio(Chord currentProgChord) {
		brokenNotes = new ArrayList<Note>();
		int size = currentProgChord.getChordNotes().size() - 1;
		Note editNoteEnd = scales.getKey(currentProgChord.getChordNotes().get(size), 5);
		for (Note aNote : currentProgChord.getChordNotes()) {
			brokenNotes.add(aNote);
		}
		brokenNotes.add(editNoteEnd); // octave note
		brokenNotes.add(brokenNotes.get(0));
		brokenNotes.add(editNoteEnd);
		brokenNotes.add(brokenNotes.get(2));
		brokenNotes.add(brokenNotes.get(1));
	}

	public void reflectiveApreggio(Chord currentProgChord) {
		brokenNotes = new ArrayList<Note>();
		int size = currentProgChord.getChordNotes().size() - 1;
		Note editNoteEnd = scales.getKey(currentProgChord.getChordNotes().get(size), 5);
		for (Note aNote : currentProgChord.getChordNotes()) {
			brokenNotes.add(aNote);
		}
		brokenNotes.add(brokenNotes.get(1));
		brokenNotes.add(editNoteEnd); // octave note
		brokenNotes.add(brokenNotes.get(1));
		brokenNotes.add(brokenNotes.get(2));
		brokenNotes.add(brokenNotes.get(1));
	}

	public void excitingApreggio(Chord currentProgChord) {
		brokenNotes = new ArrayList<Note>();
		int size = currentProgChord.getChordNotes().size() - 1;
		Note octaveRoot = scales.getKey(currentProgChord.getChordNotes().get(size), 17);
		Note octaveThird = scales.getKey(currentProgChord.getChordNotes().get(size), 21);
		for (Note aNote : currentProgChord.getChordNotes()) {
			brokenNotes.add(aNote);
		}
		brokenNotes.add(brokenNotes.get(0));
		brokenNotes.add(brokenNotes.get(1));
		brokenNotes.add(brokenNotes.get(2));
		brokenNotes.add(octaveThird); // 7th note
		brokenNotes.add(octaveRoot);
		brokenNotes.add(brokenNotes.get(2));
		brokenNotes.add(brokenNotes.get(1));
	}

	public void adventureApreggio(Chord currentProgChord) {
		brokenNotes = new ArrayList<Note>();
		int i = 0;
		int j = 0;
		for (; i <= 3; i++) {
			// Create list of notes based on progression, and play it
			// sequentially with 4 times, each new progression
			for (Note aNote : currentProgChord.getChordNotes()) {
				brokenNotes.add(aNote);
				// When same notes added 4 times, repeat it
				if (j == currentProgChord.getChordNotes().size() - 1) {
					j = 0;
					break;
				}
				j++;
			}
		}
		Collections.reverse(brokenNotes);
	}

	public void playApreggio(int intervalTime, int restTime) throws InvalidMidiDataException {
		for (Note aNote : brokenNotes) {
			PlaybackFunctions.playIntervalNote(aNote);
			PlaybackFunctions.timeDelay(intervalTime);
		}
		PlaybackFunctions.timeDelay(restTime);
	}

	public ArrayList<Note> getApreggio() {
		return brokenNotes;
	}

	public enum apreggioNames {
		Clear, Adventure, Fine, Reflective, Exciting;
	}

	public void createBox() {
		apreggioNames[] entries = apreggioNames.values();
		brokenNotesNames = new String[entries.length];
		int i = 0;
		for (apreggioNames a : entries) {
			brokenNotesNames[i] = a.toString();
			i++;
		}
	}

	public String[] getNames() {
		return brokenNotesNames;
	}
}