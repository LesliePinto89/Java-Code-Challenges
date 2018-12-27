import java.util.ArrayList;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class LoadMIDISupport  {
	private MidiDevice device = null;
	private MidiChannel channel;
	private Instrument [] instruments;
	private Sequencer seq = null;
	private Transmitter seqTrans= null;
	private Receiver synthRcvr= null;
	private Receiver seqRcvr= null;
	
	protected void prepareMIDI() throws InvalidMidiDataException, MidiUnavailableException {
	ArrayList<MidiDevice.Info> synthInfos = new ArrayList<MidiDevice.Info>();
	MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
	for (int i = 0; i < infos.length; i++) {
		try {
			device = MidiSystem.getMidiDevice(infos[i]);
		} catch (MidiUnavailableException e) {
			// Handle or throw exception...
		}
		if (device instanceof Synthesizer) {
			synthInfos.add(infos[i]);
		}
	}
	device = MidiSystem.getMidiDevice(synthInfos.get(0));
	if (!(device.isOpen())) {
		try {
			device.open();
		} catch (MidiUnavailableException e) {
			// Handle or throw exception...
		}
	}

	try {		
		synthRcvr = device.getReceiver();
		seq = MidiSystem.getSequencer();
		seqRcvr = seq.getReceiver();
		seqTrans = seq.getTransmitter();
		seqTrans.setReceiver(synthRcvr);
		seq.open();

		
	} catch (MidiUnavailableException e) {
		// handle or throw exception
	}
	device.getMaxTransmitters();
	device.getMaxReceivers();
	
	instruments = ((Synthesizer) device).getDefaultSoundbank().getInstruments();
	channel = ((Synthesizer) device).getChannels()[0];	
}

	
	public void getAudioBytes(){
	AudioInputStream originalInputStream = AudioSystem.getAudioInputStream(inputStream);
	//Format of data read in
	AudioFormat originalFormat = originalInputStream.getFormat();
	//Normalize the data to something we know
	decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
	originalFormat.getSampleRate(), … );
	//Input stream of the normalized format.
	AudioInputStream decodedInputStream = AudioSystem.getAudioInputStream(decodedFormat,
	originalInputStream);
	SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
	line.open(decodedFormat);
	line.start();
	
	}
	
	protected Transmitter returnSeqTransmitter(){
		return seqTrans;
	}
	protected Synthesizer returnSynth(){	
		return (Synthesizer) device;
	}
	protected Sequencer returnSequencer(){	
		return seq;
	}
	
	protected Receiver returnSequencerReceiver(){	
		return seqRcvr;
	}
	
	
	
	protected MidiChannel getMidiChannel(){	
		return channel;
	}
	protected void newMidiChannel(MidiChannel newChannel){	
		this.channel = newChannel;
	}
	protected Instrument [] getListOfMidiChannels(){	
		return instruments;
	}
	
	 public void selectInstrument (String choice){
		 Patch patch =null;
		 int bankNumber = 0;
		 int programNumber = 0;
		 for(int i = 0; i < instruments.length;i++){	 
			 if(instruments[i].getName().contains(choice)){
				 patch = instruments[i].getPatch();
				 bankNumber = patch.getBank();
				 programNumber = patch.getProgram();
				 channel.programChange(bankNumber, programNumber);
				  break;
			 } 	 
			 
			 else if (i == instruments.length-1){
				 break; 				 
			 }
		  }
	    }
}