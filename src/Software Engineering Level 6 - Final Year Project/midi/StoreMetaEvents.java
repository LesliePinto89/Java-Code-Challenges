package midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import midiDevices.MidiReciever;

public class StoreMetaEvents {
	private MidiReciever reciever;
	//private static ArrayList<Long> metaTickEventMessages = new ArrayList<Long>();
	//private MetaMessage[] metaBytesEventMessages;
	private Track trk;
	//private static byte[] eachEventBytes = new byte [metaTickEventMessages.size()];
	               
	public StoreMetaEvents(MidiReciever carriedReciever) {
		this.reciever = carriedReciever;

	}

	// Will use to show visualization with complete recorded sequence
	public void createFullSequenceMetaData() throws InvalidMidiDataException {
		int i = 1;
		
		Track[] tracks = reciever.getSequence().getTracks();
		//metaBytesEventMessages = new MetaMessage [reciever.getSequence().getTracks()[0].size()];
		
		for (Track track : tracks) {
		//	track.get(track.size()-1).getMessage();
			//MidiMessage convertToShort = (MidiMessage) track.get(track.size()-1).getMessage();
			
			//MetaMessage findTrackEnd = (MetaMessage) convertToShort;

			 if (i == track.size()-1) {
				 break;}
			generateMetaData(track);
			i++;
		}
	}

	// Used the completion sequence track rather than a per meta event message
	public final void generateMetaData(Track track) throws InvalidMidiDataException {
		 trk = reciever.getSequence().createTrack();
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
	
	public Track getMetaTrack() {
		return trk;
	}

	//public MetaMessage [] getMetaMessageData() {
	//	return metaBytesEventMessages;
	//}
	
	//public ArrayList<Long> getMetaTickData() {
	//	return metaTickEventMessages;
	//}
}