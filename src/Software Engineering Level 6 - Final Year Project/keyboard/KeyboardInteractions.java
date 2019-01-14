package keyboard;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import midiDevices.MidiReciever;
import midi.MidiMessageTypes;
import tools.MIDIFileManager;
import tools.MIDIRecord;
import tools.GetInstruments;

public class KeyboardInteractions extends MidiReciever implements ActionListener, ChangeListener, MouseListener {

	private int playedNotePitch;
	// private LoadMIDISupport load;
	private JSlider slider;
	private JToggleButton recordMIDI;
	private JToggleButton chordProgression;
	//private JButton selectFile;
	private JButton pressedNote;
	private JToggleButton playMIDI;
	private JToggleButton saveMIDI;
	private JComboBox<String> instrumentList;
	private JComboBox<String> tempoList;

	private int carriedSaveNumber;

	// only due to constructor not allowing another two paramater
	private Sequence sequence;

	private MidiMessageTypes messageTypes;
	private MidiReciever reciever;
	private MIDIFileManager fileManager;
	private MIDIRecord record;
	private GetInstruments getInstruments = null;
	
	
	private boolean clicked = false;

	public KeyboardInteractions() {
	}

	public KeyboardInteractions(MidiMessageTypes loadMessageTypes, GetInstruments loadInstruments, MidiReciever loadedReciever,
			JComboBox<String> list) {
		this.reciever = loadedReciever;
		this.messageTypes = loadMessageTypes;

		// Construct instruments box
		if (list.getName().equals("instrumentList")) {
			this.getInstruments = loadInstruments;
			this.instrumentList = list;
		}

		// Construct tempo markings box
		else if (list.getName().equals("tempoList")) {
			this.tempoList = list;
		}

	}

	public KeyboardInteractions(MidiReciever loadedReciever, JToggleButton optionButton) {
		this.reciever = loadedReciever;
		switch (optionButton.getName()) {

		case "playButton":
			this.playMIDI = optionButton;
			break;
		case "recordButton":
			this.recordMIDI = optionButton;
			record = new MIDIRecord();
			record.recordReciever(reciever);
			break;
		case "Chord Progression":
			this.chordProgression = optionButton;
			break;
        default: break;
		}

	}

	// Construct Save Button
	// Have to bring in load as the sequence is not brought in without it
	public KeyboardInteractions(MIDIFileManager loadedFileManager, JToggleButton saveMIDI, int saveNumber) {
		this.fileManager = loadedFileManager;
		this.saveMIDI = saveMIDI;
		this.carriedSaveNumber = saveNumber;

	}


	
	// Construct Volume JSlider
	public KeyboardInteractions(MidiMessageTypes loadMessageTypes, JSlider slider) {
		this.slider = slider;
		this.messageTypes = loadMessageTypes;
	}

	// Construct Create MIDI track
	public KeyboardInteractions(MidiMessageTypes loadMessageTypes, MidiReciever loadedReciever, JButton pressedNote, int playedNotePitch) {
		this.reciever = loadedReciever;
		this.pressedNote = pressedNote;
		this.playedNotePitch = playedNotePitch;
		this.messageTypes = loadMessageTypes;
	}

	// JSlider volume event
	public void stateChanged(ChangeEvent e) {
		slider = (JSlider) e.getSource();
		if (!slider.getValueIsAdjusting()) {
			int value = slider.getValue();
			// channel.controlChange(7, value);

			// experimental
			messageTypes.getMidiChannel().controlChange(7, value);
		}
	}

	
	public void mousePressed(MouseEvent pressed) {
		Object obj = pressed.getSource();
	  
		if (obj.equals(pressedNote)) {

			// Free play mode
			if (reciever.isFreePlayEnded() == false) {
				clicked = true;
				try {
					reciever.freeNotePlay(playedNotePitch);
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}

			}

			// Record mode
			else  if (reciever.isRecEnded() == false) {

				// If condition used if user does not change tempo to make a
				// recording.
				if (messageTypes.isTempoSelected() == true) {

					// Remembers and sets the new tempo change by user after
					// star recording bnelow resets it
					// it to default
					messageTypes.saveSelectedTempo(messageTypes.getSelectedTempo());
				}

				int ticksPerSecond = reciever.calculateTick();
				long startTick = reciever.getStartTick();
				try {
					ShortMessage turnNoteOn = new ShortMessage();
					ShortMessage turnNoteOff = new ShortMessage();
					turnNoteOn.setMessage(ShortMessage.NOTE_ON, 0, playedNotePitch, 90);
					turnNoteOff.setMessage(ShortMessage.NOTE_OFF, 0, playedNotePitch, 100);

					reciever.getTrack().add(new MidiEvent(turnNoteOn, startTick));
					reciever.getTrack().add(new MidiEvent(turnNoteOff, startTick + ticksPerSecond));
					reciever.returnSequencer().startRecording();

					// MIDI cumulative time computation: Previous event start
					// time + delta time
					reciever.setStartTick(startTick += ticksPerSecond);
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}

			}
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		if (reciever.isFreePlayEnded() == false) {
			clicked = false;
			try {
				reciever.freeNoteStop(playedNotePitch);
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
				
			}
		}

	}

	public void mouseEntered(MouseEvent e) {

		/*Point location = MouseInfo.getPointerInfo().getLocation();
		Component button = e.getComponent();
		JLayeredPane parent = (JLayeredPane)button.getParent();
		SwingUtilities.convertPointFromScreen(location, parent);
		Component mouseOver = parent.findComponentAt( location );
		  if (mouseOver instanceof JButton)
		  {
			
			try {
				pressedNote = (JButton) mouseOver;
				reciever.freeNotePlay(playedNotePitch);
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		  
		}*/
		}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
	}

         
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj.equals(saveMIDI)) {

			// Go to another class to make a midi file
			fileManager.saveNewMIDIFile(saveMIDI);
		}

		// Go to another class to make enable and disable record feature
		else if (obj.equals(recordMIDI)) {
			// record.recordReciever(reciever,trackDetails);			
				
					try {
						record.recordAction(recordMIDI);
					} catch (InvalidMidiDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				
		
		}

		else if (obj.equals(playMIDI)) {
			// When sequence tracks are not empty, play sequence.

			if (reciever.getSequence() != null) {
				int empty = reciever.getSequence().getTracks()[0].size();
				if (reciever.isRecEnded() == true || empty >= 2 && reciever.isRecEnded() == true) {
					// System.out.println(reciever.returnSequencer().getTickPosition());
					reciever.returnSequencer().setTickPosition(0);
					// System.out.println(reciever.returnSequencer().getTickPosition());
					reciever.returnSequencer().start();
					if (reciever.returnSequencer().isRunning() == true) {
						playMIDI.setSelected(false);
						playMIDI.setEnabled(true);
					}
				}
			}
		}

		else if (obj.equals(instrumentList)) {
			// System.out.println(instrumentList);
			String choice = instrumentList.getSelectedItem().toString();
			// getInstruments.setupInstruments(reciever);
			getInstruments.selectInstrument(choice);
		}

		else if (obj.equals(tempoList)) {
			String choice = tempoList.getSelectedItem().toString();
			choice = choice.substring(0, choice.indexOf(":"));
			messageTypes.selectedTempo(tempoList);
			messageTypes.saveSelectedTempo(choice);
		}
	}
}