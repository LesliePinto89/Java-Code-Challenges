package midiDevices;

import java.awt.Color;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import keyboard.Note;
import tools.PlaybackFunctions;

/**
 * This class is used to retrieve the MIDI messages that are transmitted to the
 * default synthesiser, from the MIDI keyboard's input. This process uses the
 * MIDI Wire protocol, which does not use meta messages and does not allow users
 * to access messages send to a receiver's send method (i.e. synthesiser's
 * receiver). While one transmitter sends messages to the synthesiser, another
 * transmitter of the same kind simultaneously sends the same messages to this
 * class, revealing the messages in real-time. I used this data to change the
 * display of each note's colour as the user plays their device in real-time.
 */
public class DummyReceiver implements Receiver {

	private Receiver rcvr;

	public DummyReceiver() throws MidiUnavailableException {
		this.rcvr = MidiSystem.getReceiver();
	}

	/**
	 * Certain MIDI keyboards use a SYSEX (System Exclusive) message called
	 * "active sensing". This is recognised by the MIDI code "254", and is a
	 * part of the active MIDI Keyboard. The option to turn it off depends on the
	 * keyboard's design, and it runs at a constant defined rate, i.e. every 200ms. Similar
	 * to a watch dog cycle, if it is not answered it turns all notes off and 
	 * enters non-sensing mode.
	 * 
	 * @param msg
	 *            - the MidiMessage transmitted by the MIDI Keyboard device's
	 *            MIDI Output port.
	 * @param timeStamp
	 *            - the time the message arrived at the receiver.
	 */
	@Override
	public void send(MidiMessage msg, long timeStamp) {

		// While it runs, this condition isolates the messages to only come from
		// MIDI Keyboard device's MIDI Output port.
		if (msg.getStatus() != 254) {
			byte[] a = msg.getMessage();
			int statusByteToInt = a[0] & 0xFF & 0xF0;
			int notePitch = a[1];
			int velocity = a[2];
			switch (statusByteToInt) {
			// By default, MIDI Keyboard's do not use NOTE ON and OFF messages.
			// It uses NOTE_ON velocity(1=> to <=100) for on, and NOTE_ON (0)
			// as its NOTE OFF equivalent.
			case ShortMessage.NOTE_ON:
				
               // System.out.println(PlayBackDevices.getInstance().returnSynth().getLatency());
				
				// Releasing depressed notes creates a NOTE_ON message with 0
				// velocity.
				if (velocity == 0) {
					PlaybackFunctions.resetLastNotePianoColor(notePitch);
				} else {
					byte bytePitch = a[1];
					Note playNote = null;
					for (Note aNote : Note.getNotesMap().values()) {
						if (aNote.getPitch() == bytePitch) {
							playNote = aNote;
							break;
						}
					}
					PlaybackFunctions.storedPreColorNotes(playNote);
					PlaybackFunctions.colorChordsAndScales(playNote, Color.BLUE);
					break;
				}
			}
		}
	}

	public void close() {
		rcvr.close();
	}
}