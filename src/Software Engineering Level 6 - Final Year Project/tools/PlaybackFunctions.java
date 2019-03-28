package tools;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.swing.JButton;
import keyboard.Note;
import keyboard.VirtualKeyboard;
import midi.Chord;
import midi.ListOfScales;
import midi.MidiMessageTypes;
import midi.Scale;
import midiDevices.MidiReceiver;

public class PlaybackFunctions {

	private static ArrayList<Note> storedPreColorChords = new ArrayList<Note>();
	private static int medoldicIndexCounter = 0;
	private static int notesIndex = 0;
	private static int intervalPrevCounter;
	private static int randomIntervalCounter = 0;
	private static Note currentRandomInterval;
	private static Note currentMelodicInterval;
	private static int direction;
	private static boolean displayOrPlayScale;

	public static void colorChordsAndScales(Note aNote, Color aColor) {
		JButton foundButton;
		Collection<JButton> buttonNotes = VirtualKeyboard.getInstance().getButtons();
		for (JButton buttonNote : buttonNotes) {
			if (buttonNote.getText().equals(aNote.getName())) {
				foundButton = buttonNote;
				foundButton.setBackground(aColor);
				break;
			}
		}
	}

	public static void resetChordsColor() {
		ArrayList<Note> getOriginal = getStoredPreNotes();
		for (Note aNote : getOriginal) {
			if (aNote.getType().equals("Sharp")) {
				colorChordsAndScales(aNote, Color.BLACK);
			} else {
				colorChordsAndScales(aNote, Color.WHITE);
			}
		}
	}

	public static void resetScaleDisplayColor() {
		// ArrayList<Note> getOriginal = getStoredPreNotes();
		Scale currentScale = ListOfScales.getInstance().getDisplayedScaleNotes();
		for (Note aNote : currentScale.getScaleNotesList()) {
			if (aNote.getType().equals("Sharp")) {
				colorChordsAndScales(aNote, Color.BLACK);
			} else {
				colorChordsAndScales(aNote, Color.WHITE);
			}
		}
	}

	public static void resetLastNoteColor() {
		Note lastNote = null;
		// boolean test = MidiMessageTypes.getInstance().getRandomState();
		if (MidiMessageTypes.getInstance().getRandomState()) {
			lastNote = storedPreColorChords.get(randomIntervalCounter - 1);
		} else if (MidiMessageTypes.getInstance().getMelodyInterval()) {
			lastNote = storedPreColorChords.get(medoldicIndexCounter - 1);
		}

		// Feature set 3 only
		else {
			lastNote = storedPreColorChords.get(medoldicIndexCounter - 1);
		}

		if (lastNote.getType().equals("Sharp")) {
			colorChordsAndScales(lastNote, Color.BLACK);

		} else {
			colorChordsAndScales(lastNote, Color.WHITE);

		}
	}

	public static void resetNextNoteColor() {
		Note lastNote = null;
		// boolean test = MidiMessageTypes.getInstance().getRandomState();
		if (MidiMessageTypes.getInstance().getRandomState()) {
			lastNote = storedPreColorChords.get(randomIntervalCounter - 1);
		} else if (MidiMessageTypes.getInstance().getMelodyInterval()) {
			lastNote = storedPreColorChords.get(intervalPrevCounter - 1);
		}

		if (lastNote.getType().equals("Sharp")) {
			colorChordsAndScales(lastNote, Color.BLACK);

		} else {
			colorChordsAndScales(lastNote, Color.WHITE);

		}
	}

	public static void storedPreColorNotes(Note aNote) {
		storedPreColorChords.add(aNote);
	}

	public static ArrayList<Note> getStoredPreNotes() {
		return storedPreColorChords;
	}

	public static void emptyNotes() {
		storedPreColorChords.clear();
	}

	/////////////////////////////////////////

	public static void currentRandom(Note cRandom) {
		currentRandomInterval = cRandom;
	}

	public static Note getCurrentRandom() {
		return currentRandomInterval;
	}

	public static void currentMedoldicInterval(Note cMelodic) {
		currentMelodicInterval = cMelodic;
	}

	public static Note getMedoldicInterval() {
		return currentMelodicInterval;
	}

	public static void setRandomIntervalCounter(int rValue) {
		randomIntervalCounter = rValue;
	}

	public static int currentRandomIntervalCounter() {
		return randomIntervalCounter;
	}
	/////////////////////////////

	public static void setMelodicIndexCounter(int iValue) {
		medoldicIndexCounter = iValue;
	}

	public static int getMelodicIndexCounter() {
		return medoldicIndexCounter;
	}
	///////////////////////////////////

	public static void setIndexCounter(int index) {
		notesIndex = index;
	}

	public static int getIndexCounter() {
		return notesIndex;
	}

	public static void setPrevIntervalCounter(int iPrevValue) {
		intervalPrevCounter = iPrevValue;
	}

	public static int getPrevIntervalCounter() {
		return intervalPrevCounter;
	}

	public static void currentDirection(int id) {
		direction = id;
	}

	public static int fixDirection() {
		return direction;
	}

	public static void timeDelay(int fixedTime) {
		Instant instantOnTime = Instant.now();
		long startTime = instantOnTime.toEpochMilli();

		Instant newInstantOnTime = Instant.now();
		long endTime = newInstantOnTime.toEpochMilli();
		while (!(endTime - startTime > fixedTime)) {

			newInstantOnTime = Instant.now();
			endTime = newInstantOnTime.toEpochMilli();
		}
	}

	///////////////////////////////////

	public static void playIntervalNote(Note intervalNote) throws InvalidMidiDataException {
		SwingComponents swingComponents = SwingComponents.getInstance();
		MidiMessageTypes messages = MidiMessageTypes.getInstance();

		// Only during color mode on and press random note button
		if (swingComponents.getColorToggleStatus() && messages.getRandomState() && randomIntervalCounter >= 1) {
			resetLastNoteColor();
		}

		// Only applied to relative pitch mode, with next interval
		else if (swingComponents.getColorToggleStatus() && messages.getMelodyInterval() && medoldicIndexCounter >= 1) {
			resetLastNoteColor();
		}

		// Only applied to relative pitch mode, with next interval
		else if (swingComponents.getColorToggleStatus() && messages.getMelodyInterval() && intervalPrevCounter >= 1) {
			resetNextNoteColor();
		}

		// Use for feature set 3 only
		else if (swingComponents.getColorToggleStatus() && medoldicIndexCounter >= 1) {
			resetLastNoteColor();
		}

		if (!swingComponents.getColorToggleStatus()) {
			storedPreColorNotes(intervalNote);
		}

		else if (swingComponents.getColorToggleStatus()) {
			storedPreColorNotes(intervalNote);
			colorChordsAndScales(intervalNote, Color.BLUE);
		}
		ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0, intervalNote.getPitch(), 50);
		MidiReceiver.getInstance().send(noteOne, -1);

		if (messages.getRandomState()) {
			if (messages.getNoColorFirst()) {
				messages.storeRandomState(false);

			}
			randomIntervalCounter++;
		} else if (messages.getIntervalStateID() == 2) {
			medoldicIndexCounter++;

		}

		else if (messages.getIntervalStateID() == 1) {
			PlaybackFunctions.setIndexCounter(PlaybackFunctions.getIndexCounter() - 1);
			intervalPrevCounter++;
		}
		/*
		 * Used for playing scales as opposes to traversal of a scale using
		 * relative pitch feature Might need to remove if causes problems
		 */
		else {
			medoldicIndexCounter++;
		}
	}

	public static void playAnyChordLength(Chord foundChord) throws InvalidMidiDataException, InterruptedException {
		SwingComponents swingComponents = SwingComponents.getInstance();
		ArrayList<Note> notesInChord = foundChord.getChordNotes();
		for (Note aNote : notesInChord) {
			if (swingComponents.getColorToggleStatus()) {
				storedPreColorNotes(aNote);
				colorChordsAndScales(aNote, Color.BLUE);
			}

			ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0, aNote.getPitch(), 50);
			MidiReceiver.getInstance().send(noteOne, -1);
		}
	}

	// This function display scale's notes and plays the indidivuasl notes only,
	// when the
	// first condition is true
	public static void displayOrPlayScale(Scale foundScale) throws InvalidMidiDataException, InterruptedException {

		SwingComponents swingComponents = SwingComponents.getInstance();
		ArrayList<Note> notesInChord = foundScale.getScaleNotesList();
		for (Note aNote : notesInChord) {

			if (isPlayOrDisplay()) {
				ShortMessage noteOne = new ShortMessage(ShortMessage.NOTE_ON, 0, aNote.getPitch(), 50);
				MidiReceiver.getInstance().send(noteOne, -1);
				timeDelay(1000);
			}
			// Add second condition to other methods if needed
			else if (swingComponents.getColorToggleStatus() || swingComponents.getRangeColorToggleStatus()) {
				storedPreColorNotes(aNote);
				colorChordsAndScales(aNote, Color.BLUE);

			}
		}
	}

	public static void playOrDisplay(boolean change) {
		displayOrPlayScale = change;
	}

	public static boolean isPlayOrDisplay() {
		return displayOrPlayScale;
	}
}