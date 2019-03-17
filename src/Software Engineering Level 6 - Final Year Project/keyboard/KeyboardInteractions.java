package keyboard;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Instant;
import java.util.Timer;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import midiDevices.MidiReceiver;
import midi.DurationTimer;
import midi.MidiMessageTypes;
import midi.StoreMetaEvents;
import tools.MIDIFileManager;
import tools.MIDIRecord;
import tools.Metronome;
import tools.PlaybackFunctions;
import tools.SwingComponents;
import midiDevices.GetInstruments;

public class KeyboardInteractions implements ActionListener, ChangeListener, MouseListener,MetaEventListener
{

	// Swing components
	private JSlider slider;
	private JToggleButton recordMIDI;
	private JButton pressedNote;
	private JToggleButton debugButton;
	private JToggleButton playMIDI;
	private JToggleButton saveMIDI;
	private JButton homeButton;
	private JList<String> allInstruments;
	private JList<String> tempoList;
	
	private byte bytePitch;
	
	// MIDI Timing and message variables
	private int durationValue;
	private long startTick;
	int resolution;
	static Timer timer;
	boolean noteIsOn = true;
	private int playedNotePitch;
	private MIDIRecord record;
	private boolean debugNoNotes =false;

	public static final int DAMPER_PEDAL = 64;
	public static final int DAMPER_ON = 127;
	public static final int DAMPER_OFF = 0;
	private boolean sustain = false; // is the sustain pedal depressed?
	private boolean endDurationCycle = false;

	private Metronome metronome = Metronome.getInstance();
	
	private MidiMessageTypes messages = MidiMessageTypes.getInstance();
	private MidiReceiver midiReceiveInstance = MidiReceiver.getInstance();
	private GetInstruments getInstruments = GetInstruments.getInstance();
	//private SwingComponents components = SwingComponents.getInstance();
	
	public KeyboardInteractions() {
	}

	public KeyboardInteractions(JList<String> carriedChordsList) {

	 if (carriedChordsList.getName().equals("Instruments")) {
			this.allInstruments = carriedChordsList;
	}
		else if (carriedChordsList.getName().equals("Tempos")) {
			this.tempoList = carriedChordsList;
	 }
	}

	public KeyboardInteractions(JButton aButton) {
		this.homeButton = aButton;
	}
	
	
	public KeyboardInteractions(JToggleButton optionButton) {
		switch (optionButton.getName()) {
		case "Debug" : 
			this.debugButton = optionButton; 
			break;
		case "playButton":
			this.playMIDI = optionButton;
			break;
		case "recordButton":
			this.recordMIDI = optionButton;
			record = new MIDIRecord();
			record.recordReciever();
			midiReceiveInstance.storedRecordStart(record);
			break;
		case "saveButton" : 
			this.saveMIDI = optionButton;
			break;
		default:
			break;
		}
	}

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
			messages.getMidiChannel().controlChange(7, value);
		}
	}

	public void mousePressed(MouseEvent pressed) {
		Object obj = pressed.getSource();	
		 if (obj.equals(allInstruments)) {
			 String selectedInstrument = "";
			 int index = allInstruments.locationToIndex(pressed.getPoint());
			 selectedInstrument = getInstruments.getAllInstruments().getElementAt(index);
			 getInstruments.selectInstrument(selectedInstrument);
			 getInstruments.instrumentChanged(true);
		}
		 
		else if (obj.equals(pressedNote)) {

			// Free play mode
			if (midiReceiveInstance.isFreePlayEnded() == false) {
				try {
					midiReceiveInstance.freeNotePlay(playedNotePitch);
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}

			// Record mode
			else if (midiReceiveInstance.isRecEnded() == false) {
				if (midiReceiveInstance.getTrack().size() == 1) {
					midiReceiveInstance.returnSequencer().startRecording();
					startTick = 0;
					if (messages.getDebugStatus()){
						String startTickString = Long.toString(startTick);
						messages.sequenceTimingMessages(">>>>>Start timing");
						messages.sequenceTimingMessages("First Note start tick value is: "+startTickString);
					    //Console debug
						//System.out.println("First Note start tick value is: " + startTick);
						}

				}
				else if (midiReceiveInstance.getTrack().size() >= 3) {

					// Get the current time in milliseconds, and remove the last
					// note's off time from itself. To get absolute time to later user.
					// Storage of the current time in memory is not needed,
					// only each note's off time.
					
					messages.sequenceTimingMessages(">>>>>START TIME OF NEW NOTE");
					
					Instant instantOnTime = Instant.now();
					long timeStampMillisOnTime = instantOnTime.toEpochMilli();

					long diffBetweenRest = timeStampMillisOnTime - DurationTimer.getInstance().getNoteOffTimeStamp();
					startTick = DurationTimer.getInstance().getCumulativeTime() + diffBetweenRest;
					
					if (messages.getDebugStatus()){
						String rangeRestDiff = Long.toString(diffBetweenRest);
						messages.sequenceTimingMessages(">>>>>CALCULATE TIME DIFFERENCE BETWEEN INTERVALS");
						messages.sequenceTimingMessages("Time stamp of difference between last note and new note: "+rangeRestDiff);
					
						//Console debug
//					System.out.println("\nTime stamp of difference between last note and new note wihtot division: "
//							+ diffBetweenRest);
					
					String startTickString = Long.toString(startTick);
					
					messages.sequenceTimingMessages("Tick time per second based starttick: "+rangeRestDiff);
					//Console debug
//					System.out.println("\nTick time per second based starttick: " + startTickString);
					}
					
				}

				try {				
					if (getInstruments.checkIfinstrumentChanged() == true){
						int program = getInstruments.getProgramNumber();
						ShortMessage changeInstrument = new ShortMessage();
						changeInstrument.setMessage(ShortMessage.PROGRAM_CHANGE,0,program,0);
						midiReceiveInstance.getTrack().add(new MidiEvent(changeInstrument, startTick));
						//reset detect instrument change until it occurs again
						getInstruments.instrumentChanged(false);
					}
	
					ShortMessage message = new ShortMessage();

					// Allows immediate wire play back while short messages
					// are added to sequence
					midiReceiveInstance.freeNotePlay(playedNotePitch);

					//System.out.println("Instrument choice :"+getInstruments.getChannelSetToInstrument());
					
					message.setMessage(ShortMessage.NOTE_ON, 0, playedNotePitch, 90);
					midiReceiveInstance.getTrack().add(new MidiEvent(message, startTick));

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
		try {
		if (midiReceiveInstance.isFreePlayEnded() == false) {
				midiReceiveInstance.freeNoteStop(playedNotePitch);
		}
					
		if (midiReceiveInstance.isRecEnded() == false) {
			resolution = midiReceiveInstance.returnSequencer().getSequence().getResolution();
				durationValue = DurationTimer.getInstance().getCycledDuration();

				ShortMessage turnNoteOff = new ShortMessage();
				turnNoteOff.setMessage(ShortMessage.NOTE_OFF, 0, playedNotePitch, 0);
				midiReceiveInstance.getTrack().add(new MidiEvent(turnNoteOff, startTick + durationValue - 1));

				if (messages.getDebugStatus()){
					String durationValueString = Integer.toString(durationValue);
					messages.sequenceTimingMessages(">>>>>CALCULATE DURATION OF A PLAYED NOTE");
					messages.sequenceTimingMessages("Duration value of note is: "+durationValueString);
					//Console debug
					//System.out.println("Duration value of note is: " + durationValue);
				}
				
				// Uses the epoch to capture time in milliseconds when a button
				// is pressed. This code utilises the absolute time value
				// approach to rest time in the sequence.
				Instant instantOffTime = Instant.now();
				long timeStampMillisOffTime = instantOffTime.toEpochMilli();
				DurationTimer.getInstance().storeNoteOffTimeStamp(timeStampMillisOffTime);
				////////////////////////////////////////////////////////

				// When user released a button before or after it has decayed
				midiReceiveInstance.freeNoteStop(playedNotePitch);

				// Make the value of the duration timer be 0 at end of note
				// messages construction
				DurationTimer.getInstance().resetDuration();

				// The true boolean value is used to turn the timer off during
				// the construction of a new note
				DurationTimer.getInstance().setDurationTimer(pressedNote, true);

				// Add this note's cumulative time to the time difference
				// between this note off and new note on (a new button pressed) 
				// and assign the value to the start tick variable
				long cumulativeTime = startTick + durationValue;
				DurationTimer.getInstance().storeCumulativeTime(cumulativeTime);
				if (messages.getDebugStatus()){
					String cumulativeTimeString = Long.toString(cumulativeTime);
					messages.sequenceTimingMessages(">>>>>CALCULATE CUMULATIVE TIME");
					messages.sequenceTimingMessages("The cumultive value of start tick and duration is: "+cumulativeTimeString);
					
					messages.sequenceTimingMessages(">>>>>END OF NOTE TIMING");
					//Console debug
					//System.out.println("The cumultive value of start tick and duration is: " + cumulativeTime);
					}
			} 
		}catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}	
	}

	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		try {
		
		if (obj.equals(homeButton)){
				//VirtualKeyboard.getInstance().home();
		}
			
		else if (obj.equals(saveMIDI)) {
			// Go to another class to make a midi file
			MIDIFileManager.getInstance().saveNewMIDIFile(saveMIDI);
		}
		
		 else if (obj.equals(debugButton)){
			 
			 //DEBUG Scenerio 1
			 //If user has no started a recording on systen starp up prior to clicling debug mode.
			 //Also if the user has started a recording, but has not ended it and played anything.
			   if (messages.getDebugStatus()==false && messages.isRecordedDebug() ==false)
					   {
				   messages.clearTimingMessages();
				// messages.defaultNoTimingMessages();
				 messages.sequenceTimingMessages(messages.returnDefault());
				 messages.loadDebug();
			 }
			   
			   //DEBUG Scenerio 2
			   else if (messages.getDebugStatus() && messages.isRecordedDebug() ==false &&
						  messages.getTimingMessages().equals("")){
					  // messages.clearTimingMessages();
				  // messages.defaultNoTimingMessages();
					 messages.sequenceTimingMessages(messages.returnDefault());
					   messages.loadDebug();
					   debugNoNotes = true;
				   }
			   
			   
			   //DEBUG Scenerio 3
			   //EXPECT when user clicks button before debug
			   //If user has started the test, not ended recordin but played a note, record the midi time.
			   else if (messages.getDebugStatus() && messages.isRecordedDebug() ==false &&
					  !messages.getTimingMessages().equals("")){
				  
				   //When use does above bute clicks debug before typing a note, and then re clicks debuf
				   if (debugNoNotes==true){
					   messages.editTimingMessages();
				   }
				   messages.loadDebug();
			   }
			   
			   //DEBUG Scenerio 4
			   //User starts rec, clicks debug while rec on, types note, ENDS REC, click on debug
			   else if (messages.getDebugStatus()==false && messages.isRecordedDebug() &&
						  !messages.getTimingMessages().equals("")){
					  
                        //Same as above
					   if (debugNoNotes==true){
						   messages.editTimingMessages();
					   }
					   messages.loadDebug();
				   }
			   
		/////////////////////////////////////////////
			   
			
			 //Not sure if needed as of yet
//		     else if (messages.getDebugStatus() && messages.isRecordedDebug() && !messages.getTimingMessages().equals("") ){
//			    	messages.loadDebug();
//			    }
			 
		 }

		// Go to another class to make enable and disable record feature
		else if (obj.equals(recordMIDI)) {
				record.recordAction(recordMIDI);
		}
		
		else if (obj.equals(homeButton)){
		//Placeholder for now	
		}
		else if (obj.equals(playMIDI)) {
			// When sequence tracks are not empty, play sequence.

			if (midiReceiveInstance.getSequence() != null) {
				int empty = midiReceiveInstance.getSequence().getTracks()[0].size();
				if (midiReceiveInstance.isRecEnded() == true
						|| empty >= 2 && midiReceiveInstance.isRecEnded() == true) {
					
//					int[] types = new int[128];
//			        for (int ii = 0; ii < 128; ii++) {
//			            types[ii] = ii;
//			        }
//					midiReceiveInstance.returnSequencer().addControllerEventListener(this, types);
					
					//Create meta event data
					Track [] tracks = midiReceiveInstance.returnSequencer().getSequence().getTracks();
		            Track trk = midiReceiveInstance.returnSequencer().getSequence().createTrack();
		            for (Track track : tracks) {
		            	StoreMetaEvents.generateMetaData(track, trk);
		            }
					midiReceiveInstance.returnSequencer().addMetaEventListener(this);
					
					//Have to set the new sequence with a track of meta events onto the sequence
					Sequence editSequence = midiReceiveInstance.returnSequencer().getSequence();
					
					midiReceiveInstance.returnSequencer().setSequence(editSequence);
					midiReceiveInstance.returnSequencer().setTickPosition(0);
					midiReceiveInstance.returnSequencer().start();

					if (midiReceiveInstance.returnSequencer().isRunning() == true) {
						playMIDI.setSelected(false);
						playMIDI.setEnabled(true);
					}
				}
			}
		} 
		}
		catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
    public void meta(MetaMessage metaRec) {
		
	 messages.metaEventColors(metaRec);
    }
	

//	 @Override
//     public void controlChange(ShortMessage event) {
//         int command = event.getCommand();
//         if (command == ShortMessage.NOTE_ON) {
//             System.out.println("CEL - note on!");
//         } else if (command == ShortMessage.NOTE_OFF) {
//             System.out.println("CEL - note off!");
//         } else {
//             System.out.println("CEL - unknown: " + command);
//         }
//     }
}