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
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class LoadMIDISupport  {
	
	
	private Synthesizer synth;
	private MidiChannel channel;
	private Instrument [] instruments;
	private Sequencer seq = null;
	private Transmitter seqTrans= null;
	private Receiver synthRcvr= null;
	
	protected void prepareMIDI() throws InvalidMidiDataException, MidiUnavailableException {
	// System.out.print(Arrays.toString(MidiSystem.getMidiDeviceInfo()));
	ArrayList<MidiDevice.Info> synthInfos = new ArrayList<MidiDevice.Info>();
	MidiDevice device = null;
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
		seq = MidiSystem.getSequencer();
		seqTrans = seq.getTransmitter();
		// synth = MidiSystem.getSynthesizer();
		synthRcvr = device.getReceiver();
		seqTrans.setReceiver(synthRcvr);
		seq.open();
	} catch (MidiUnavailableException e) {
		// handle or throw exception
	}
	device.getMaxTransmitters();
	device.getMaxReceivers();
	
	synth = MidiSystem.getSynthesizer();
	synth.open();
	instruments = synth.getDefaultSoundbank().getInstruments();
	//test.loadInstrument(instruments[instrument]);  
	channel = synth.getChannels()[0];
	
}
	//Found selected synthesizer
	protected Synthesizer returnSynth(){	
		return synth;
	}
	
	//Found selected sequencer
	protected Sequencer returnSequencer(){	
		return seq;
	}
	
	//Found MidiChannel from selected synthesizer
	protected MidiChannel getMidiChannel(){	
		return channel;
	}
	
	protected void newMidiChannel(MidiChannel newChannel){	
		this.channel = newChannel;
	}
	
	//Found Instruments from the selected synthesizer's soundbank
	protected Instrument [] getListOfMidiChannels(){	
		return instruments;
	}
	
	 public MidiChannel selectInstrument (String choice){
	    
		 Patch patch =null;
		 int bankNumber = 0;
		 int programNumber = 0;
		 for(int i = 0; i < instruments.length;i++){	 
			 if(instruments[i].getName().contains(choice)){
				 patch = instruments[i].getPatch();
				 bankNumber = patch.getBank();
				 programNumber = patch.getProgram();
				 channel.programChange(bankNumber, programNumber);
				 return channel;
			 } 	 
			 
			 else if (i == instruments.length-1){
				 break; 				 
			 }
		  }
 	     
	     return channel;
	    }
}
