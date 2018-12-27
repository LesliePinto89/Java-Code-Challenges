import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InteractionEventsInterface implements ActionListener, ChangeListener {

	private int playedNotePitch;
	private MIDIVirtualKeyboard midiGui;
	private LoadMIDISupport load;
	private int ticksPerSecond;
	private JSlider slider;
	private MidiChannel channel;
	private JButton recordMIDI;
	private JButton pressedNote;
	private JButton playMIDI;
	private int recordClick = 0;
	private JComboBox<String> instrumentList;

	// Construct Record Button
	public InteractionEventsInterface(JButton recordMIDI, MIDIVirtualKeyboard midiGui) {
		this.recordMIDI = recordMIDI;
		this.midiGui = midiGui;
	}

	// Construct Play Button
	public InteractionEventsInterface(MIDIVirtualKeyboard midiGui, JButton playMIDI) {
		this.midiGui = midiGui;
		this.playMIDI = playMIDI;

	}

	// Construct instruments box
	public InteractionEventsInterface(JComboBox<String> instrumentList, MIDIVirtualKeyboard midiGui,
			LoadMIDISupport load) {
		this.instrumentList = instrumentList;
		this.midiGui = midiGui;
		this.load = load;
	}

	// Construct Volume JSlider
	public InteractionEventsInterface(JSlider slider, MIDIVirtualKeyboard midiGui, MidiChannel channel) {
		this.slider = slider;
		this.midiGui = midiGui;
		this.channel = channel;
	}

	// Construct Create MIDI track
	public InteractionEventsInterface(JButton pressedNote, MIDIVirtualKeyboard midiGui, int playedNotePitch,
			int ticksPerSecond) {
		this.pressedNote = pressedNote;
		this.midiGui = midiGui;
		this.playedNotePitch = playedNotePitch;
		this.ticksPerSecond = ticksPerSecond;
	}

	// JSlider volume event
	public void stateChanged(ChangeEvent e) {
		slider = (JSlider) e.getSource();
		if (!slider.getValueIsAdjusting()) {
			int value = slider.getValue();
			channel.controlChange(7, value);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		long startTick = midiGui.getStartTick();

		if (obj.equals(pressedNote)) {

			// Free play - no record mode
			if (midiGui.isFreePlayEnded() == false && midiGui.isRecEnded() == true) {
				try {
					midiGui.holdPlay(playedNotePitch);
					if (!pressedNote.isSelected()) {
						midiGui.stop(playedNotePitch);
					}
				} catch (InvalidMidiDataException | MidiUnavailableException e1) {
					e1.printStackTrace();
				}
			}
			// Record mode
			else if (midiGui.isFreePlayEnded() == true && midiGui.isRecEnded() == false) {
				try {
					ShortMessage turnNoteOn = new ShortMessage();
					ShortMessage turnNoteOff = new ShortMessage();
					turnNoteOn.setMessage(ShortMessage.NOTE_ON, 0, playedNotePitch, 90);
					turnNoteOff.setMessage(ShortMessage.NOTE_OFF, 0, playedNotePitch, 100);
					midiGui.getTrack().add(new MidiEvent(turnNoteOn, startTick));
					midiGui.getTrack().add(new MidiEvent(turnNoteOff, startTick + ticksPerSecond));
					midiGui.getSequencer().startRecording();
					midiGui.setStartTick(startTick += ticksPerSecond);

				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
		}

		else if (obj.equals(recordMIDI)) {
			recordClick++;
			midiGui.endFreePlay(true);
			midiGui.endRecording(false);

			// User clicked record button to start cording
			if (recordClick % 2 == 1) {
				recordMIDI.setText("Recording");

				// User clicked record button after stopping recording to start
				// a new recording
				if (midiGui.getSeq().getTracks()[0].size() >= 2) {
					midiGui.setStartTick(0);
					midiGui.getSeq().deleteTrack(midiGui.getTrack());
					Track newTrack = midiGui.getSeq().createTrack();
					midiGui.storeTrack(newTrack);

					try {
						midiGui.getSequencer().setSequence(midiGui.getSeq());
						midiGui.getSequencer().recordEnable(midiGui.getTrack(), 0);

						// Fixes note delay when re-recording song similar to
						// memory leaks of scanner.next()
						midiGui.getSequencer().startRecording();

					} catch (InvalidMidiDataException error1) {
						error1.printStackTrace();
					}
				}
			}

			//User clicked record button to end recording
			else if (recordClick % 2 == 0) {
				midiGui.endFreePlay(false);
				midiGui.endRecording(true);
				recordMIDI.setText("Record");
				midiGui.getSequencer().stopRecording();
				midiGui.getSequencer().recordDisable(midiGui.getTrack());
			}
		}

		else if (obj.equals(playMIDI)) {
			
			// When sequence tracks are not empty, play sequence.
			int empty = midiGui.getSeq().getTracks()[0].size();
			if (midiGui.isRecEnded() == true || empty >= 2 && midiGui.isRecEnded() == true) {
				midiGui.getSequencer().setTickPosition(0);
				midiGui.getSequencer().start();

				// After song is played, turn of recording mode and turn of
				// freeplay mode
				midiGui.endFreePlay(false);
				midiGui.endRecording(true);
			}
		}

		else if (obj.equals(instrumentList)) {
			String choice = instrumentList.getSelectedItem().toString();
			load.selectInstrument(choice);
		}
	}
}