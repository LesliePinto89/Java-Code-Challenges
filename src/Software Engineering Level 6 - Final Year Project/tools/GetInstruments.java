package tools;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Patch;
import javax.sound.midi.Synthesizer;

import midiDevices.MidiReciever;

public class GetInstruments   {

	private Instrument[] instruments;
	private String instrumentName;
	private MidiChannel channel = null;
	public String[] storeInstrumentsNames;
	
	
	//Start of instrument selection
	public void setupInstruments (MidiReciever carriedReciever){
		instruments = ((Synthesizer) carriedReciever.returnDevice()).getDefaultSoundbank().getInstruments();
		channel = ((Synthesizer) carriedReciever.returnDevice()).getChannels()[0];
	}
	

	//public MidiChannel getMidiChannel() {
	//	return channel;
	//}

	//protected void newMidiChannel(MidiChannel newChannel) {
	//	this.channel = newChannel;
	//}
	
	public Instrument[] getListOfMidiChannels() {
		return instruments;
	}
	
	public String [] returnIntrumentNames (){
		return storeInstrumentsNames;
		
	}
	
	//experiment
	protected void storeInstrumentName(String selectedInstrument) {
		this.instrumentName = selectedInstrument;
		
	}
	
	protected String getInstrumentName() {
		return instrumentName;
		
	}
	public void selectInstrument(String choice) {
		
		Patch patch = null;
		int bankNumber = 0;
		int programNumber = 0;
		for (int i = 0; i < instruments.length; i++) {
			if (instruments[i].getName().contains(choice)) {
				patch = instruments[i].getPatch();
				bankNumber = patch.getBank();
				programNumber = patch.getProgram();
				channel.programChange(bankNumber, programNumber);			
				break;
			}

			else if (i == instruments.length - 1) {
				break;
			}
		}
	}
	
	public String[] allInstruments(Instrument[] channels) {
		String[] tempStorage = new String[channels.length];
		for (int i = 0; i < channels.length; i++) {
			tempStorage[i] = channels[i].getName();
		}
		return tempStorage;
	}
	
}
