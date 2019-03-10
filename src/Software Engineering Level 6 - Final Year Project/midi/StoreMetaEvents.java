package midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import midiDevices.MidiReceiver;

public class StoreMetaEvents {
//This was originally not static
	private static Track trk;
	private int trackCounter;               

	public StoreMetaEvents() {
	}

	// Will use to show visualisation with complete recorded sequence
	public static void createFullSequenceMetaData() throws InvalidMidiDataException {
		int i = 1;
		
		Track[] tracks = MidiReceiver.getInstance().getSequence().getTracks();
		//metaBytesEventMessages = new MetaMessage [reciever.getSequence().getTracks()[0].size()];
		
		for (Track track : tracks) {
			 if (i == track.size()-1) {
				 break;}
			generateMetaData(track);
			i++;
		}
	}

	// Used the completion sequence track rather than a per meta event message
	//This method had a final modifier not static originally
	public static void generateMetaData(Track track) throws InvalidMidiDataException {
		
		trk = MidiReceiver.getInstance().getSequence().createTrack();
		for (int ii = 0; ii < track.size(); ii++) {
			MidiEvent me = track.get(ii);
			MidiMessage mm = me.getMessage();
			if (mm instanceof ShortMessage) {
				ShortMessage sm = (ShortMessage) mm;
				int command = sm.getCommand();
				int type = -1;
				if (command == ShortMessage.NOTE_ON) {
					type = 1;
				} else if (command == ShortMessage.NOTE_OFF) {
					type = 2;
				}
				if (type > 0) {
					
					byte[] messageByteArray = sm.getMessage();
					int l = (messageByteArray == null ? 0 : messageByteArray.length);
					
					MetaMessage metaMessage = new MetaMessage(type, messageByteArray, l);
					
					//metaBytesEventMessages[ii] = metaMessage; //Add metaMessage to metaMessage array
					//metaTickEventMessages.add(me.getTick()); //Add to list
					
					MidiEvent me2 = new MidiEvent(metaMessage, me.getTick());
					trk.add(me2);
				}
			}
		}
	}
	
	
	// Attempt at adding meta events on the fly for real time use for visualization
	public final void perEventMetaData(Track track) throws InvalidMidiDataException {
		if (track.size() ==1){
			trackCounter =0;
			trk = MidiReceiver.getInstance().getSequence().createTrack();
		}
		
		else {trackCounter++;}
		
			MidiEvent me = track.get(trackCounter);
			MidiMessage mm = me.getMessage();
			if (mm instanceof ShortMessage) {
				ShortMessage sm = (ShortMessage) mm;
				int command = sm.getCommand();
				int type = -1;
				if (command == ShortMessage.NOTE_ON) {
					type = 1;
				} else if (command == ShortMessage.NOTE_OFF) {
					type = 2;
				}
				if (type > 0) {
					
					byte[] messageByteArray = sm.getMessage();
					int l = (messageByteArray == null ? 0 : messageByteArray.length);
					
					MetaMessage metaMessage = new MetaMessage(type, messageByteArray, l);
					
					//metaBytesEventMessages[ii] = metaMessage; //Add metaMessage to metaMessage array
					//metaTickEventMessages.add(me.getTick()); //Add to list
					
					MidiEvent me2 = new MidiEvent(metaMessage, me.getTick());
					trk.add(me2);
				}
			}
		
	}
	
	public Track getMetaTrack() {
		return trk;
	}
}