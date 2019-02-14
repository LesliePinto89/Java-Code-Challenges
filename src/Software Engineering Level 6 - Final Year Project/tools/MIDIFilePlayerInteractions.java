package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JList;

import midi.StoreMetaEvents;
import midiDevices.MidiReceiver;

public class MIDIFilePlayerInteractions implements ActionListener, MouseListener, MetaEventListener {

	private JButton selectFile;
	private Track[] sequenceTracks;
	private Track sequenceMetaTrack;

	private JButton prevFile;
	private JButton playFile;
	private JButton nextFile;

	private MIDIFilePlayer player;
	private JList<String> songList;

	public MIDIFilePlayerInteractions(JList<String> carriedSongList) {
		this.songList = carriedSongList;
	}

	// Select MIDI file from midi player GUI
	//public MIDIFilePlayerInteractions(JButton carriedJButton) {
		//this.selectFile = carriedJButton;
		

	//}

	// Stored Meta event messages
	public MIDIFilePlayerInteractions(Track[] carriedTracks, Track carriedMetaTrack) {
		this.sequenceTracks = carriedTracks;
		this.sequenceMetaTrack = carriedMetaTrack;

	}

	public MIDIFilePlayerInteractions(JButton carriedButton) {
		switch (carriedButton.getName()) {
		case "SelectMidiFile" :   
			this.selectFile = carriedButton;
		break;
		
		case "prevFile":
			this.prevFile = carriedButton;
			break;
		case "playFile":
			this.playFile = carriedButton;
			break;
		case "nextFile":
			this.nextFile = carriedButton;
			break;
		default:
			break;
		}
	
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Object obj = arg0.getSource();

		// User clicks on JList options
		if (obj.equals(songList)) {
			int index = songList.locationToIndex(arg0.getPoint());
			MIDIFilePlayer.getInstance().storedJListSelectedIndex(index);
			// System.out.println("Double clicked on Item " + index);
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object obj = arg0.getSource();

		if (obj.equals(selectFile)) {
			File desiredFile = MIDIFileManager.getInstance().selectMIDIFile();
			MIDIFilePlayer.getInstance().storedFoundFile(desiredFile);
		}

		else if (obj.equals(prevFile)) {

			// System.out.println("Double clicked on Item " + index);

			// File desiredFile = fileManager.selectMIDIFile();
			// player.storedFoundFile(desiredFile);
		}

		else if (obj.equals(playFile)) {
			playFeature();
		}

		else if (obj.equals(nextFile)) {
			int index = MIDIFilePlayer.getInstance().getListSelectedIndex();

			MIDIFilePlayer.getInstance().storedJListSelectedIndex(index++);
			playFeature();
		}
	}

	public void playFeature() {
		try {

			if (MidiReceiver.getInstance().isRunning() == true) {
				MidiReceiver.getInstance().returnSequencer().stop();
				playFile.setText(">");
			}

			else {
				playFile.setText("||");
				// Replace sequence with file in this feature
				MidiReceiver.getInstance().storeSeq(MIDIFilePlayer.getInstance().playSelectedFile());
				MidiReceiver.getInstance().returnSequencer().setSequence(MidiReceiver.getInstance().getSequence());
			    
				//If by some chance the .mid file does not have meta data, or is a MIDI 1 multi-track file
				//running this function below with provide meta data 
				//player.generateMetaFromFile();
			    MidiReceiver.getInstance().returnSequencer().setTickPosition(0);
			    MidiReceiver.getInstance().returnSequencer().start();
			}

		} catch (InvalidMidiDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType() == 0x2F) {
			MidiReceiver.getInstance().returnSequencer().stop();
			playFile.setText(">");
		}
	}
}
