package tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.sound.midi.InvalidMidiDataException;
import javax.speech.EngineStateError;
import javax.swing.JButton;
import javax.swing.JTextArea;
import keyboard.Note;
import midi.Chord;
import midi.ChordProgression;
import midi.ChordProgressionActions;
import midi.ListOfChords;
import midi.ListOfScales;
import midi.Scale;

public class Quizes implements MouseListener {

	private ArrayList<Chord> allMajorChords = ListOfChords.getInstance().getKeptMajors();
	private ArrayList<Chord> allMinorChords = ListOfChords.getInstance().getKeptMinors();
	private ArrayList<String> tempStoreScore = new ArrayList<String>();

	// Text and text based conditional variables
	private JTextArea text;
	private String handlefeature = "";
	private JButton userChoice = null;

	// Numerical and conditional variables
	private int successCounter = 0;

	// Chord Quiz components
	private Chord majorChordChoice = null;
	private Chord minorChordChoice = null;
	private Chord randomChordChoice = null;

	//Progression quiz components
	private Scale majorProgressionScale;
	private Scale minorProgressionScale;
	private String randomMajorProgressionString = "";
	private String relativeMinorProgression = "";
	private ChordProgression minorProgression;
	private ChordProgression majorProgression;
	private ChordProgression randomProgressionName;

	//Scale quiz components
	private Scale quizMajorScale;
	private Scale quizMinorScale;
	private Scale randomScaleName;

	// Singleton Instances
	private ChordProgressionActions prog = ChordProgressionActions.getInstance();
	private SwingComponents components = SwingComponents.getInstance();
	private ListOfScales scales = ListOfScales.getInstance();

	private static volatile Quizes instance = null;

	public static Quizes getInstance() {
		if (instance == null) {
			synchronized (Quizes.class) {
				if (instance == null) {
					instance = new Quizes();
				}
			}
		}
		return instance;
	}

	private Quizes() {
	}

	public void assignFeatureName(String name) {
		handlefeature = name;
	}

	public JButton addPlayQuiz() {
		if (handlefeature.equals("Chords")) {
			userChoice = components.customJButton(270, 40, "Play chord", this,20,Color.decode("#505050"));
		}
		else if (handlefeature.equals("Scales")) {
			userChoice = components.customJButton(270, 40, "Play scale", this,20,Color.decode("#505050"));
		}
		else if (handlefeature.equals("Progressions")) {
			userChoice = components.customJButton(270, 40, "Play progression", this,20,Color.decode("#505050"));
		}
		return userChoice;
	}

	public void selectQuiz(String choice, String feature, JTextArea contentTextArea) {
		text = contentTextArea;
		handlefeature = feature;
		switch (feature) {
		
		case "Chords":
			if (choice.equals("2")) {
				minOrMajChordQuiz();
				break;
			}

		case "Scales":
			if (choice.equals("2")) {
				minOrMajScaleQuiz();
				break;
			}
		case "Progressions":
			if (choice.equals("2")) {
				minOrMajProgressionsQuiz();
				break;
			}
		}
	}

	public void adjustProgression(String[] bits) {
		relativeMinorProgression ="";
		for (String aString : bits) {
			char[] charArray = aString.toCharArray();
			if (Character.isLowerCase(charArray[0])) {
				relativeMinorProgression += aString.toUpperCase() + " ";
			} else if (Character.isUpperCase(charArray[0])) {
				relativeMinorProgression += aString.toLowerCase() + " ";
			}
		}
	}
	
	public <T> ArrayList<T> mixedValues(T one, T two) {
		ArrayList<T> temp = new ArrayList<T>();
		temp.add(one);
		temp.add(two);
		return temp;
	}
	
	public void minOrMajProgressionsQuiz() {
		String[] getBits = null;
		ArrayList<String> coll = Note.randomNotesForScaleKeys();
		String baseKey = ScreenPrompt.random(coll);
		text.append("Progression Quiz \n\nThe purpose of this quiz is to find\nout if this "
				+ "progression is a major \nor minor progression\n\n");

		randomMajorProgressionString = ScreenPrompt.random(prog.getMajorProgsNames());
		getBits = randomMajorProgressionString.split("\\s+");

		int i = 0;
		for (Scale aScale : scales.getDiatonicMajorScales()) {
			if (aScale.getTonic().getName().equals(baseKey + "3")) {
				majorProgressionScale = aScale;
				minorProgressionScale = scales.getDiatonicMinorScales().get(i);
				break;
			}
			i++;
		}
		adjustProgression(getBits);

		// Created both progressions
		majorProgression = prog.makeChordProgression(randomMajorProgressionString, majorProgressionScale, getBits);
		String[] relativeBits = relativeMinorProgression.split("\\s+");
		minorProgression = prog.makeChordProgression(relativeMinorProgression, minorProgressionScale, relativeBits);

		// Randomise progression played
		ArrayList<ChordProgression> mixProgressions = mixedValues(majorProgression, minorProgression);
		randomProgressionName = ScreenPrompt.random(mixProgressions);
	}

	public void minOrMajScaleQuiz() {
		ArrayList<String> coll = Note.randomNotesForScaleKeys();
		String baseKey = ScreenPrompt.random(coll);
		int i = 0;
		for (Scale aScale : scales.getDiatonicMajorScales()) {
			if (aScale.getTonic().getName().equals(baseKey + "3")) {
				quizMajorScale = aScale;
				quizMinorScale = scales.getDiatonicMinorScales().get(i);
				break;
			}
			i++;
		}
		ArrayList<Scale> mixScales = mixedValues(quizMajorScale, quizMinorScale);
		randomScaleName = ScreenPrompt.random(mixScales);
		text.append("Scale Quiz \n\nThe purpose of this quiz is to find\nout if this scale is a major or minor\n\n");
	}

	public void minOrMajChordQuiz() {
		ArrayList<String> allMajorChordNames = Chord.getPureMajorEnums();
		String randomChordName = "";
		ArrayList<String> temp = Note.randomNotesForScaleKeys();
		String baseRoot = ScreenPrompt.random(temp);
		randomChordName = ScreenPrompt.random(allMajorChordNames);
		text.append("Chords Quiz \n\nThe purpose of this quiz is to find\nout if this chord is a major or minor\n\n");
		
		int i = 0;
		for (Chord aChord : allMajorChords) {
			if (aChord.getChordName().equals(randomChordName)) {
				if (aChord.getChordNotes().get(0).getName().equals(baseRoot + "3")) {
					majorChordChoice = aChord;
					minorChordChoice = allMinorChords.get(i);
					break;
				}
			}
			i++;
		}
		ArrayList<Chord> mixChords = mixedValues(majorChordChoice, minorChordChoice);
		randomChordChoice = ScreenPrompt.random(mixChords);
	}

	public void printAnswer(boolean check) {
		String quizFeature = handlefeature;
		String quizTargetName = handlefeature.substring(0, handlefeature.length() - 1);
		if (check) {
			successCounter++;
			text.setText("");
			text.append("You are correct - It was\na " + userChoice.getName() + " " + quizTargetName);
			String correct = quizFeature + "Quiz: Easy Mode | No of success: " + successCounter;
			tempStoreScore.add(correct);
		}
		else {
			text.setText("");
			text.append("You are wrong. It was\na " + userChoice.getName() + " " + quizTargetName);
		}
	}

	public void doesAnswerMatch(String choice) {
		boolean resultOne = false;
		String quizTargetName = handlefeature.substring(0, handlefeature.length() - 1);
		if (!userChoice.getName().equals("Major") && !userChoice.getName().equals("Minor")) {
			text.append("Please listen to the\n" + quizTargetName + " chord before deciding");
		}
		else {
			resultOne = userChoice.getName().equals(choice) ? true : false;
			printAnswer(resultOne);
		}
	}

	public void handleProgressions(Object button) {
		try {
			for (Chord aChordOne : randomProgressionName.getProgressionChords()) {
				PlaybackFunctions.playAnyChordLength(aChordOne);
				PlaybackFunctions.timeDelay(1000);
			}
			if (randomProgressionName.getProgressionName().contains(randomMajorProgressionString)) {
				userChoice.setName("Major");
			}

			else if (randomProgressionName.getProgressionName().contains(relativeMinorProgression)) {
				if (button.equals(userChoice)) {
					userChoice.setName("Minor");
				}
			}
		} catch (InvalidMidiDataException | InterruptedException | EngineStateError e) {
			e.printStackTrace();
		}
	}

	public void handleChords(Object button) {
		try {
			PlaybackFunctions.playAnyChordLength(randomChordChoice);
			if (randomChordChoice.getChordName().contains("maj")) {
				userChoice.setName("Major");
			} else if (randomChordChoice.getChordName().contains("min")) {
				userChoice.setName("Minor");
			}

		} catch (InvalidMidiDataException | InterruptedException | EngineStateError e) {
			e.printStackTrace();
		}
	}

	public void handleScales(Object button) {
		try {
			PlaybackFunctions.playOrDisplay(true);
			PlaybackFunctions.displayOrPlayScale(randomScaleName);
			if (randomScaleName.getScaleName().contains("Ionian")) {
				userChoice.setName("Major");
			} else if (randomScaleName.getScaleName().contains("Aeolian")) {
				userChoice.setName("Minor");
			}
		} catch (InvalidMidiDataException | InterruptedException | EngineStateError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent button) {
		Object obj = button.getSource();
		if (obj.equals(userChoice)) {
			if (handlefeature.equals("Chords")) {
				handleChords(obj);
			} else if (handlefeature.equals("Scales")) {
				handleScales(obj);
			} else if (handlefeature.equals("Progressions")) {
				handleProgressions(obj);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
