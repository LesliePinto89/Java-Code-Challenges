package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextArea;

import keyboard.VirtualKeyboard;
import midi.Chord;
import midi.ListOfChords;
import midi.ListOfScales;
import midi.Scale;

public class Quizes implements MouseListener {

	private ArrayList<Chord> allMajorChords = ListOfChords.getInstance().getKeptMajors();
	private ArrayList<Chord> allMinorChords = ListOfChords.getInstance().getKeptMinors();

	private Scale quizMajorScale;
	private Scale quizMinorScale;

	private SwingComponents components = SwingComponents.getInstance();
	private ScreenPrompt prompt = ScreenPrompt.getInstance();
	private boolean result;
	private JTextArea text;
	private String handlefeature = "";
	// private String targetFromRandom;
	private String alternativeFromRandom;
	private ArrayList<String> tempStoreScore = new ArrayList<String>();

	private static volatile Quizes instance = null;

	private Chord majorChord = null;
	private Chord minorChord = null;

	// private Scale ionianScale = null;
	// private Chord aoelianScale = null;

	private String guessChordType = "";
	private String guessScaleType = "";

	private String shorted;

	private JButton choiceOne = null;
	private JButton choiceTwo = null;

	// private JButton submit;
	private int randomizeIndex = 0;

	// private boolean choiceOneChosen =false;
	// private boolean choiceTwoChosen =false;

	private String stringFromButton = "";
	private int successCounter = 0;
	private int failureCounter = 0;

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

	public JButton addChordOne() {
		if (handlefeature.equals("Chords")) {
			return choiceOne = components.customJButton(70, 40, "Chord One", this);
		}

		else if (handlefeature.equals("Scales")) {
			return choiceOne = components.customJButton(70, 40, "Scale One", this);
		}

		return choiceOne;
	}

	public JButton addChordTwo() {
		if (handlefeature.equals("Chords")) {
			return choiceTwo = components.customJButton(70, 40, "Chord Two", this);
		} else if (handlefeature.equals("Scales")) {
			return choiceTwo = components.customJButton(70, 40, "Scale Two", this);
		}

		return choiceTwo;
	}

	// public JButton addSubmitButton() {
	// return submit = components.customJButton(70, 40, "Submit", this);
	// }

	public <T> void storeAlternative(String alt) {
		alternativeFromRandom = alt;
	}

	public <T> T ternaryRandom(Collection<T> coll) {
		int num = (int) (Math.random() * coll.size());
		int i = 0;
		for (T t : coll) {
			if (--num < 0) {
				if (i == 0) {
					// T [] stringArray;
					Object[] stringArray = coll.toArray();
					String aString = stringArray[++i].toString();
					storeAlternative(aString);
				}
				return t;
			}

			else {
				Object[] stringArray = coll.toArray();
				String aString = stringArray[i].toString();
				storeAlternative(aString);
			}
			i++;
		}
		throw new AssertionError();
	}

	public String randomScalesQuestionAnswer() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("Ionian");
		type.add("Aeolian");
		String targetFromRandom = ternaryRandom(type);
		return targetFromRandom;
	}

	public String randomQuestionAnswer() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("Major");
		type.add("Minor");
		String targetFromRandom = ternaryRandom(type);
		return targetFromRandom;
	}

	public void selectQuiz(String choice, String feature, JTextArea contentTextArea) {
		text = contentTextArea;
		handlefeature = feature;
		switch (feature) {
		case "Chords":
			if (choice.equals("1")) {
				minOrMajChordQuiz(false);
				break;
			}

			else if (choice.equals("2")) {
				minOrMajChordQuiz(true);
				break;
			}

		case "Scales":
			if (choice.equals("1")) {
				minOrMajScaleQuiz(false);
				break;
			}

			else if (choice.equals("2")) {
				minOrMajScaleQuiz(true);
				break;
			}
		}
	}

	public void minOrMajScaleQuiz(boolean hard) {
		quizMajorScale = Scale.getScaleFromList(0);
		quizMinorScale = Scale.getScaleFromList(5);

		ArrayList<String> allMajorScalesNames = Scale.getPureMajorScales();

		String baseScaleName = "";
		String randomScaleName = "";

		guessScaleType = randomScalesQuestionAnswer();
		String baseKey = Scale.getScaleKey();

		// ListOfScales.getInstance().displayedScaleNotes(foundScale);

		if (!hard) {
			baseScaleName = Scale.getCurrentScaleName();
			text.append("The purpose of this quiz is to find out which of the two scales" + " is the " + guessScaleType
					+ " scale?\n\n");
			text.append("Try to remember the diffence in sound and find which scale is the " + guessScaleType
					+ " scale?\n");
		}

		else {
			randomScaleName = ScreenPrompt.random(allMajorScalesNames);
			// randomChordName = ScreenPrompt.random(allMajorChordNames);
			text.append("The purpose of this quiz is to find out which of the two scales" + " is the " + guessScaleType
					+ " scale?\n\n");
			text.append("Each scale matching was generated randomly from any of the supported scales.\n");

		}

		// for (Scale aScale : major) {
		//
		// // Compares Major against minor version of initial chord/root
		// if (!hard) {
		// if (aChord.getChordName().equals(baseChordName)) {
		// if (aChord.getChordNotes().get(0).getName().equals(baseRoot + "3")) {
		// majorChord = aChord;
		// break;
		// }
		// }
		// i++;
		// }
		// // Compares random Major against its minor version
		// else if (hard) {
		// if (aChord.getChordName().equals(randomChordName)) {
		//
		// if (aChord.getChordNotes().get(0).getName().equals(baseRoot + "3")) {
		// majorChord = aChord;
		// break;
		// }
		// }
		// i++;
		// }
		// }
		// minorChord = minors.get(i);
		// aoelianScale
		//

	}

	///////////////////////////////

	public void minOrMajChordQuiz(boolean hard) {
		ArrayList<String> allMajorChordNames = Chord.getPureMajorEnums();
		String baseChordName = "";
		String randomChordName = "";
		guessChordType = randomQuestionAnswer();
		shorted = guessChordType.substring(0, 3);
		shorted = shorted.toLowerCase();
		int i = 0;

		String baseRoot = Chord.getStoredRoot();
		if (!hard) {
			baseChordName = Chord.getStoredChordName();
			text.append("The purpose of this quiz is to find out which of the two chords" + " is the " + guessChordType
					+ " chord?\n\n");
			text.append("Try to remember the diffence in sound and find which chord is the " + guessChordType
					+ " chord?\n");
		}

		else {
			randomChordName = ScreenPrompt.random(allMajorChordNames);
			text.append("The purpose of this quiz is to find out which of the two chords" + " is the " + guessChordType
					+ " chord?\n\n");
			text.append("Each chord matching was generated randomly from any of the supported chords.\n");

		}

		for (Chord aChord : allMajorChords) {

			// Compares Major against minor version of initial chord/root
			if (!hard) {
				if (aChord.getChordName().equals(baseChordName)) {
					if (aChord.getChordNotes().get(0).getName().equals(baseRoot + "3")) {
						majorChord = aChord;
						break;
					}
				}
				i++;
			}
			// Compares random Major against its minor version
			else if (hard) {
				if (aChord.getChordName().equals(randomChordName)) {

					if (aChord.getChordNotes().get(0).getName().equals(baseRoot + "3")) {
						majorChord = aChord;
						break;
					}
				}
				i++;
			}
		}
		minorChord = allMinorChords.get(i);
	}

	public void printAnswer(boolean check) {
		// try {
		String quizFeature = handlefeature;
		String quizTargetName = handlefeature.substring(0, handlefeature.length() - 1);
		if (check) {

			// if(successCounter >=1){
			// int updateSuccessCounter = successCounter=1;
			// String updatedPrevious = quizFeature+"Quiz: Easy Mode | No of
			// success: "+ successCounter;
			// tempStoreScore.set(updateSuccessCounter,updatedPrevious);
			// }

			// else {
			successCounter++;
			if (handlefeature.equals("Chords")) {
				text.append("You are correct - That was the " + guessChordType + " " + quizTargetName);
			} else if (handlefeature.equals("Scales")) {
				text.append("You are correct - That was the " + guessScaleType + " " + quizTargetName);
			}

			String correct = quizFeature + "Quiz: Easy Mode | No of success: " + successCounter;
			tempStoreScore.add(correct);
			// }
			// Progress.getInstance().writeScoresToFile(tempStoreScore);
		}

		else {
			if (handlefeature.equals("Chords")) {
				text.append("You are wrong. That was not the " + guessChordType + " " + quizTargetName);
			} else if (handlefeature.equals("Scales")) {
				text.append("You are wrong. That was not the " + guessScaleType + " " + quizTargetName);
			}

		}

		// prompt.decrementPageState();
		// increaseRandomIndex();
		// prompt.resetQuiz();
		// PlaybackFunctions.timeDelay(5000);
		// VirtualKeyboard.getInstance().updateScreenPrompt();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public void doesAnswerMatch(String choice) {
		boolean resultOne = false;
		if (choiceOne.getName().equals("Chord One") || choiceTwo.getName().equals("Chord Two")) {
			text.append("Please listen to both chords before deciding");
		}

		else if (choiceOne.getName().equals("Scale One") || choiceTwo.getName().equals("Scale Two")) {
			text.append("Please listen to both scales before deciding");
		}

		else {
			if (choice.equals("Choice 1")) {

				if (choiceOne.getName().equals(guessChordType)) {
					stringFromButton = guessChordType;
				}

				else if (choiceOne.getName().equals(guessScaleType)) {
					stringFromButton = guessScaleType;
				}

				else {
					stringFromButton = alternativeFromRandom;

				}
			}

			else if (choice.equals("Choice 2")) {
				if (choiceTwo.getName().equals(guessChordType)) {
					stringFromButton = guessChordType;
				}

				else if (choiceTwo.getName().equals(guessScaleType)) {
					stringFromButton = guessScaleType;
				}

				else {
					stringFromButton = alternativeFromRandom;

				}
			}

			if (handlefeature.equals("Chords")) {
				resultOne = stringFromButton.equals(guessChordType) ? true : false;
			}

			else if (handlefeature.equals("Scales")) {
				resultOne = stringFromButton.equals(guessScaleType) ? true : false;
			}
			printAnswer(resultOne);
		}

	}

	public void increaseRandomIndex() {
		randomizeIndex++;
	}

	public void resetRandomIndex() {
		randomizeIndex = 0;
	}

	public void handleChords(Object button, Chord chordOne, Chord chordTwo)
			throws InvalidMidiDataException, InterruptedException {
		if (randomizeIndex % 2 == 0) {
			PlaybackFunctions.playAnyChordLength(chordOne);
			if (chordOne.getChordName().contains(shorted)) {
				if (button.equals(choiceOne)) {
					choiceOne.setName(guessChordType);
				} else {
					choiceTwo.setName(guessChordType);
				}
			}

			else {
				if (button.equals(choiceOne)) {
					choiceOne.setName(alternativeFromRandom);
				} else {
					choiceTwo.setName(alternativeFromRandom);
				}
			}
		}

		else {
			PlaybackFunctions.playAnyChordLength(chordTwo);
			if (chordTwo.getChordName().contains(shorted)) {

				if (button.equals(choiceOne)) {
					choiceOne.setName(guessChordType);
				} else {
					choiceTwo.setName(guessChordType);
				}
			} else {

				if (button.equals(choiceOne)) {
					choiceOne.setName(alternativeFromRandom);
				} else {
					choiceTwo.setName(alternativeFromRandom);
				}
			}
		}
	}

	public void handleScales(Object button, Scale scaleOne, Scale scaleTwo)
			throws InvalidMidiDataException, InterruptedException {
		if (randomizeIndex % 2 == 0) {
			PlaybackFunctions.playOrDisplay(true);
			PlaybackFunctions.displayOrPlayScale(scaleOne);
			if (scaleOne.getScaleName().contains(guessScaleType)) {
				if (button.equals(choiceOne)) {
					choiceOne.setName(guessScaleType);
				} else {
					choiceTwo.setName(guessScaleType);
				}
			}

			else {
				if (button.equals(choiceOne)) {
					choiceOne.setName(alternativeFromRandom);
				}

				else {
					choiceTwo.setName(alternativeFromRandom);
				}
			}
		}

		///////////////////////////////////////////////////////
		else {
			PlaybackFunctions.playOrDisplay(true);
			PlaybackFunctions.displayOrPlayScale(scaleTwo);
			if (scaleTwo.getScaleName().contains(guessScaleType)) {

				if (button.equals(choiceOne)) {
					choiceOne.setName(guessScaleType);
				} else {
					choiceTwo.setName(guessScaleType);
				}
			} else {

				if (button.equals(choiceOne)) {
					choiceOne.setName(alternativeFromRandom);
				} else {
					choiceTwo.setName(alternativeFromRandom);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent button) {
		Object obj = button.getSource();
		try {
			if (obj.equals(choiceOne)) {
				// choiceOneChosen =true;
				if (handlefeature.equals("Chords")) {
					handleChords(obj, majorChord, minorChord);
				}

				else if (handlefeature.equals("Scales")) {
					handleScales(obj, quizMajorScale, quizMinorScale);

				}
			}

			else if (obj.equals(choiceTwo)) {
				// choiceTwoChosen =true;

				if (handlefeature.equals("Chords")) {
					handleChords(obj, minorChord, majorChord);
				}

				else if (handlefeature.equals("Scales")) {
					handleScales(obj, quizMinorScale, quizMajorScale);
				}
			}
		} catch (InvalidMidiDataException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
