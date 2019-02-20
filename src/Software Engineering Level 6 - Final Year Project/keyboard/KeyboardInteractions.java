package keyboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Instant;
import java.util.Timer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import midiDevices.MidiReceiver;
import midi.AddChordToFeatureTab;
import midi.Chord;
import midi.DurationTimer;
import midi.MidiMessageTypes;
import tools.MIDIFileManager;
import tools.MIDIRecord;
import tools.Metronome;
import midiDevices.GetInstruments;

public class KeyboardInteractions implements ActionListener, ChangeListener, MouseListener {

	// Swing components
	private JSlider slider;
	private JToggleButton recordMIDI;

	private JButton pressedNote;
	private JToggleButton playMIDI;
	private JToggleButton saveMIDI;
	
	//private JComboBox<String> instrumentList;
	//private JComboBox<String> tempoList;

	// MIDI Timing and message variables
	private int durationValue;
	private long startTick;
	int resolution;
	static Timer timer;
	boolean noteIsOn = true;
	private int playedNotePitch;

	// Included classes
	//private MidiMessageTypes messageTypes;

	private MIDIRecord record;
	//private GetInstruments getInstruments = null;

	// Soon to include class
	// Chords
	// ChordProgression

	public static final int DAMPER_PEDAL = 64;
	public static final int DAMPER_ON = 127;
	public static final int DAMPER_OFF = 0;
	private boolean sustain = false; // is the sustain pedal depressed?
	private boolean endDurationCycle = false;

	private JList<String> minorChordsList;
	private JList<String> majorChordsList;
	
	private JList<String> allInstruments;
	private JList<String> tempoList;
	
	//Attempt to make several lists of instruments
	private JList<String> pianoInstruments;
	private JList<String> percussionInstruments;
	private JList<String> organInstruments;
	private JList<String> guitarInstruments;
	private JList<String> bassInstruments;
	private JList<String> stringInstruments;
	private JList<String> ensembleInstruments;
	private JList<String> brassInstruments;
	private JList<String> reedInstruments;
	private JList<String> pipeInstruments;

	private Metronome metronome = Metronome.getInstance();
	private MidiMessageTypes messageTypes = MidiMessageTypes.getInstance();
	
	public KeyboardInteractions() {
	}

	public KeyboardInteractions(JList<String> carriedChordsList) {

		if (carriedChordsList.getName().equals("Major")) {
			this.majorChordsList = carriedChordsList;
		}

		else if (carriedChordsList.getName().equals("Minor")) {
			this.minorChordsList = carriedChordsList;
		}
		
		else if (carriedChordsList.getName().equals("Instruments")) {
			this.allInstruments = carriedChordsList;
	}
		
		else if (carriedChordsList.getName().equals("Tempos")) {
			this.tempoList = carriedChordsList;
	}
		
		
		
		
		
		/*else if (carriedChordsList.getName().equals("Piano Instruments")) {
				this.pianoInstruments = carriedChordsList;
		}
		else if (carriedChordsList.getName().equals("Percussion Instruments")) {
			this.percussionInstruments = carriedChordsList;
	   }*/
		
		/*
		else if (carriedChordsList.getName().equals("MajorProg")) {
			this.majorChordsList = carriedChordsList;
		}

		else if (carriedChordsList.getName().equals("MinorProg")) {
			this.minorChordsList = carriedChordsList;
		}*/
	}

	/*public KeyboardInteractions(JComboBox<String> list) {
		
		// Construct instruments box
		//if (list.getName().equals("instrumentList")) {
			
		//	this.instrumentList = list;
		//}

		// Construct tempo markings box
		 if (list.getName().equals("tempoList")) {
			this.tempoList = list;
		}

	}*/

	public KeyboardInteractions(JToggleButton optionButton) {

		switch (optionButton.getName()) {

		case "playButton":
			this.playMIDI = optionButton;
			break;
		case "recordButton":
			this.recordMIDI = optionButton;
			record = new MIDIRecord();
			record.recordReciever();
			MidiReceiver.getInstance().storedRecordStart(record);
			break;
		case "saveButton" : 
			this.saveMIDI = optionButton;
			break;
		default:
			break;
		}

	}

	// Construct Save Button
	// Have to bring in load as the sequence is not brought in without it
	//public KeyboardInteractions(JToggleButton saveMIDI) {
		//this.saveMIDI = saveMIDI;
	//}

	// Construct Volume JSlider
	public KeyboardInteractions(JSlider slider) {
		this.slider = slider;
	}

	// Construct Create MIDI track
	public KeyboardInteractions(JButton pressedNote, int playedNotePitch) {

		this.pressedNote = pressedNote;
		this.playedNotePitch = playedNotePitch;
		
	}

	// JSlider volume event
	public void stateChanged(ChangeEvent e) {
		slider = (JSlider) e.getSource();
		if (!slider.getValueIsAdjusting()) {
			int value = slider.getValue();
			// channel.controlChange(7, value);

			// experimental
			MidiMessageTypes.getInstance().getMidiChannel().controlChange(7, value);
		}
	}

	public void mousePressed(MouseEvent pressed) {
		Object obj = pressed.getSource();
		String selectedChord = "";
		
		 if (obj.equals(allInstruments)) {
			 String selectedInstrument = "";
			 GetInstruments loadedInstruments = GetInstruments.getInstance();
			 int index = allInstruments.locationToIndex(pressed.getPoint());
			 selectedInstrument = loadedInstruments.getAllInstruments().getElementAt(index);
			 GetInstruments.getInstance().selectInstrument(selectedInstrument);
			 loadedInstruments.instrumentChanged(true);
		}
		 /*
		 else if (obj.equals(tempoList)) {
			 String selectedTempo ="";
			// MidiMessageTypes loadedTypes = MidiMessageTypes.getInstance();
			 //Metronome metro = Metronome.getInstance();
			 int index = tempoList.locationToIndex(pressed.getPoint());
			 selectedTempo = messageTypes.getTemposInModel().getElementAt(index);
			 selectedTempo = selectedTempo.substring(0, selectedTempo.indexOf(":"));
			 
			 //Might be needed if can use tempo for other functions
			 messageTypes.selectedTempo(selectedTempo);
			 messageTypes.saveTempoSeqEnd(selectedTempo);
			 messageTypes.tempoChanged(true);
			 try {
				 metronome.chooseTempo();
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 //GetInstruments.getInstance().selectInstrument(selectedInstrument);
			 
			 
				//String choice = tempoList.getSelectedItem().toString();
				//choice = choice.substring(0, choice.indexOf(":"));
				//MidiMessageTypes.getInstance().selectedTempo(tempoList);
				//MidiMessageTypes.getInstance().saveSelectedTempo(choice);
			}
		 
		 
		 */
		 
		 
		/*if (obj.equals("Piano Instruments")) {
			 GetInstruments loadedInstruments = GetInstruments.getInstance();
			 int index = pianoInstruments.locationToIndex(pressed.getPoint());
			 selectedInstrument = loadedInstruments.getPianoInstruments().getElementAt(index);
			 GetInstruments.getInstance().selectInstrument(selectedInstrument);
		}
		 else if (obj.equals("Percussion Instruments")) {
			 GetInstruments loadedInstruments = GetInstruments.getInstance();
			 int index = percussionInstruments.locationToIndex(pressed.getPoint());
			 selectedInstrument = loadedInstruments.getPianoInstruments().getElementAt(index);
		}
		*/
		//Major and Minor chord JList elements
		 else if (obj.getClass() == JList.class) {
	
			
			//If user not clicked major chords, process minor chords
			 if (majorChordsList == null){
				 if (minorChordsList.getName().equals("Minor")) {
						int index = minorChordsList.locationToIndex(pressed.getPoint());
						minorChordsList.setModel(AddChordToFeatureTab.getInstance().getMinorChordsInList());
						selectedChord = AddChordToFeatureTab.getInstance().getMinorChordsInList().getElementAt(index);
						
						//Added to change a copy of the enum's new string name and change it
						//back to original enum to get its value
						if(selectedChord.contains("b")){
							selectedChord = selectedChord.replaceAll("b", "FLAT");
						}
						else if(selectedChord.contains("#")){
							selectedChord = selectedChord.replaceAll("#", "SHARP");
						}
						///////////////////////////////////////////////////////
				 }
			} 
			//If user not clicked minor chords, process major chords
			else if (minorChordsList == null){
				if (majorChordsList.getName().equals("Major")) {
					int index = majorChordsList.locationToIndex(pressed.getPoint());
					majorChordsList.setModel(AddChordToFeatureTab.getInstance().getMajorChordsInList());
					selectedChord = AddChordToFeatureTab.getInstance().getMajorChordsInList().getElementAt(index);
					
					//Again, a change a copy of the enum's new string name and change it
					//back to original enum to get its value
					if(selectedChord.contains("#")){
						selectedChord = selectedChord.replaceAll("#", "SHARP");
					}
					
					else if(selectedChord.contains("b")){
						selectedChord = selectedChord.replaceAll("b", "FLAT");
					}
					/////////////////////////////////////////////////////
				}
			}
			
			String foundNotes = "";
			Chord.allChordNamesList[] allChordsEnums = Chord.allChordNamesList.values();
			String[] chordsNotes = new String[allChordsEnums.length];
			int i = 0;

			for (Chord.allChordNamesList e : allChordsEnums) {
				//Changed the == symbol to equals(). Strange since before I changed the
				//display of the enum chord names to modified strings, the == symbol worked.
				//The modified version is for display purposes, but I retrieved the modified
				//string as a copy of that variable, changed its value to the enum,
				// and now == wont work.
				if (selectedChord.equals(e.name())) {
					chordsNotes = e.getChord();

					foundNotes = chordsNotes[i] + chordsNotes[i+1] + chordsNotes[i+2];
					break;
				}

			}

			try {
				AddChordToFeatureTab.getInstance().generateChord(selectedChord, foundNotes);

			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// EnumSet<Chord.SharpNoteType> sharpKeysEnums =
			// EnumSet.allOf(Note.SharpNoteType.class);
			// String noteName = item.toString();

			// Chord.getInstance().storedJChordListSelectedIndex(index);
			// System.out.println("Double clicked on Item " + index);
		}

		else if (obj.equals(pressedNote)) {

			// Free play mode
			if (MidiReceiver.getInstance().isFreePlayEnded() == false) {

				try {
					MidiReceiver.getInstance().freeNotePlay(playedNotePitch);
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}

			}

			// Record mode
			else if (MidiReceiver.getInstance().isRecEnded() == false) {

				if (MidiReceiver.getInstance().getTrack().size() == 1) {
					MidiReceiver.getInstance().returnSequencer().startRecording();
					startTick = 0; // MidiReciever.getInstance().getLastTick();
					System.out.println("First Note start tick value is: " + startTick);

				}
				else if (MidiReceiver.getInstance().getTrack().size() >= 3) {

					// Get the current time in milliseconds, and remove the last
					// note's off time from itself.
					// To get absolute time to later user.Storage of the current
					// time in memory is not needed,
					// only each note's off time.
					Instant instantOnTime = Instant.now();
					long timeStampMillisOnTime = instantOnTime.toEpochMilli();

					long diffBetweenRest = timeStampMillisOnTime - DurationTimer.getInstance().getNoteOffTimeStamp();
					System.out.println("\nTime stamp of difference between last note and new note wihtot division: "
							+ diffBetweenRest);

					startTick = DurationTimer.getInstance().getCumulativeTime() + diffBetweenRest;
					System.out.println("\nTick time per second based starttick: " + startTick);
				}

				try {
                  // GetInstruments.getInstance().getChannelSetToInstrument();
					
					if (GetInstruments.getInstance().checkIfinstrumentChanged() == true){
						//int bank = GetInstruments.getInstance().getBank();
						int program = GetInstruments.getInstance().getProgramNumber();
						ShortMessage changeInstrument = new ShortMessage();
						changeInstrument.setMessage(ShortMessage.PROGRAM_CHANGE,0,program,0);
						MidiReceiver.getInstance().getTrack().add(new MidiEvent(changeInstrument, startTick));
						//reset detect instrument change until it occurs again
						GetInstruments.getInstance().instrumentChanged(false);
					}
					
					/*  THIS WAS AN ATTEMP TO CHNAGE TEMPO TO RECORDING SEQUENCE
					if (MidiMessageTypes.getInstance().checkIfTempoChanged() == true){
						String newTempoName = MidiMessageTypes.getInstance().getSelectedTempo();
						float tempoValue = MidiMessageTypes.getInstance().getTemposMap().get(newTempoName);				
						MidiMessageTypes.getInstance().tempoChanged(false);
					}*/
					
					ShortMessage message = new ShortMessage();

					// Allows immediate wire play back while short messages
					// are added to sequence
					MidiReceiver.getInstance().freeNotePlay(playedNotePitch);

					//System.out.println("Instrument choice :"+GetInstruments.getInstance().getChannelSetToInstrument());
					
					message.setMessage(ShortMessage.NOTE_ON, 0, playedNotePitch, 90);
					MidiReceiver.getInstance().getTrack().add(new MidiEvent(message, startTick));

					DurationTimer.getInstance().setDurationTimer(pressedNote, false);

					// ShortMessage sustainMessage = new ShortMessage();
					// sustainMessage.setMessage(ShortMessage.CONTROL_CHANGE, 0,
					// DAMPER_PEDAL, sustain ? DAMPER_ON : DAMPER_OFF);
					// reciever.getTrack().add(new MidiEvent(sustainMessage,
					// startTick));

				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		if (MidiReceiver.getInstance().isFreePlayEnded() == false) {
			try {
				MidiReceiver.getInstance().freeNoteStop(playedNotePitch);
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();

			}
		}
		if (MidiReceiver.getInstance().isRecEnded() == false) {
			resolution = MidiReceiver.getInstance().returnSequencer().getSequence().getResolution();

			try {
				durationValue = DurationTimer.getInstance().getCycledDuration();

				ShortMessage turnNoteOff = new ShortMessage();
				turnNoteOff.setMessage(ShortMessage.NOTE_OFF, 0, playedNotePitch, 0);
				MidiReceiver.getInstance().getTrack().add(new MidiEvent(turnNoteOff, startTick + durationValue - 1));

				// Debug usage
				System.out.println("Duration value of note is: " + durationValue);

				// Uses the epoch to capture time in milliseconds when a button
				// is pressed. This code utilises the absolute time value
				// approach to rest time in the sequence.
				Instant instantOffTime = Instant.now();
				long timeStampMillisOffTime = instantOffTime.toEpochMilli();
				DurationTimer.getInstance().storeNoteOffTimeStamp(timeStampMillisOffTime);
				////////////////////////////////////////////////////////

				// Used when the user has released a button before or after it
				// has decayed
				MidiReceiver.getInstance().freeNoteStop(playedNotePitch);

				// Make the value of the duration timer be 0 at end of note
				// messages construction
				DurationTimer.getInstance().resetDuration();

				// The true boolean value is used to turn the timer off during
				// the construction of a new note
				DurationTimer.getInstance().setDurationTimer(pressedNote, true);

				// Add this note's cumulative time to the time difference
				// between this note off and
				// a new note on (a new button pressed) and assign the value to
				// the start tick variable
				long cumulativeTime = startTick + durationValue;
				DurationTimer.getInstance().storeCumulativeTime(cumulativeTime);
				System.out.println("The cumultive value of start tick and duration is: " + cumulativeTime);

			} catch (InvalidMidiDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public void mouseEntered(MouseEvent e) {

		/*
		 * Point location = MouseInfo.getPointerInfo().getLocation(); Component
		 * button = e.getComponent(); JLayeredPane parent =
		 * (JLayeredPane)button.getParent();
		 * SwingUtilities.convertPointFromScreen(location, parent); Component
		 * mouseOver = parent.findComponentAt( location ); if (mouseOver
		 * instanceof JButton) {
		 * 
		 * try { pressedNote = (JButton) mouseOver;
		 * reciever.freeNotePlay(playedNotePitch); } catch
		 * (InvalidMidiDataException e1) { e1.printStackTrace(); }
		 * 
		 * }
		 */
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj.equals(saveMIDI)) {

			// Go to another class to make a midi file
			MIDIFileManager.getInstance().saveNewMIDIFile(saveMIDI);
		}

		// Go to another class to make enable and disable record feature
		else if (obj.equals(recordMIDI)) {
			// record.recordReciever(reciever,trackDetails);

			try {
				record.recordAction(recordMIDI);
				// reciever.storedStartEpochTime(System.currentTimeMillis());
			} catch (InvalidMidiDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		else if (obj.equals(playMIDI)) {
			// When sequence tracks are not empty, play sequence.

			if (MidiReceiver.getInstance().getSequence() != null) {
				int empty = MidiReceiver.getInstance().getSequence().getTracks()[0].size();
				if (MidiReceiver.getInstance().isRecEnded() == true
						|| empty >= 2 && MidiReceiver.getInstance().isRecEnded() == true) {
					// System.out.println(reciever.returnSequencer().getTickPosition());
					MidiReceiver.getInstance().returnSequencer().setTickPosition(0);
					// System.out.println(reciever.returnSequencer().getTickPosition());
					MidiReceiver.getInstance().returnSequencer().start();
					if (MidiReceiver.getInstance().returnSequencer().isRunning() == true) {
						playMIDI.setSelected(false);
						playMIDI.setEnabled(true);
					}
				}
			}
		}

		//else if (obj.equals(instrumentList)) {
			// System.out.println(instrumentList);
		//	String choice = instrumentList.getSelectedItem().toString();
			// getInstruments.setupInstruments(reciever);
			//GetInstruments.getInstance().selectInstrument(choice);
		//}

		/*else if (obj.equals(tempoList)) {
			String choice = tempoList.getSelectedItem().toString();
			choice = choice.substring(0, choice.indexOf(":"));
			MidiMessageTypes.getInstance().selectedTempo(tempoList);
			MidiMessageTypes.getInstance().saveSelectedTempo(choice);
		}*/
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}