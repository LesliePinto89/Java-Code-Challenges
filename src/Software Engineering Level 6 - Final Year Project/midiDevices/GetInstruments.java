package midiDevices;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Patch;
import javax.sound.midi.Synthesizer;
import javax.swing.DefaultListModel;
import midiDevices.MidiReceiver;

public class GetInstruments {

	private Instrument[] instruments;
	private String instrumentName;
	private MidiChannel channel = null;
	private int bankNumber = 0;
	private int programNumber = 0;
	private boolean instrumentChanged = false;
	private DefaultListModel<String> allInstruments = new DefaultListModel<String>();
    private MidiReceiver midiReceiever = MidiReceiver.getInstance();	
	private static volatile GetInstruments instance = null;
	private GetInstruments() {
	}

	public static GetInstruments getInstance() {
		if (instance == null) {
			synchronized (GetInstruments.class) {
				if (instance == null) {
					instance = new GetInstruments();
					instance.setupInstruments();
					instance.storeInstrumentsList();
				}
			}
		}

		return instance;
	}

	public void setupInstruments() {
		instruments = ((Synthesizer) midiReceiever.returnDevice()).getDefaultSoundbank().getInstruments();
		channel = ((Synthesizer) midiReceiever.returnDevice()).getChannels()[0];
	}
	
	public Instrument[] getListOfInstruments() {
		return instruments;
	}
	
	public MidiChannel getChannelSetToInstrument(){
		return channel;
	}

	protected void storeInstrumentName(String selectedInstrument) {
		this.instrumentName = selectedInstrument;
	}

	protected String getInstrumentName() {
		return instrumentName;
	}
	
	public void instrumentChanged(boolean change){
		instrumentChanged = change;
	} 
	
	public boolean checkIfinstrumentChanged(){
		return instrumentChanged;
	} 
	
	public void selectInstrument(String choice) {
		Patch patch = null;
		int tempBankNumber = 0;
		int tempProgramNumber = 0;
		for (int i = 0; i < instruments.length; i++) {
			if (instruments[i].getName().contains(choice)) {
				patch = instruments[i].getPatch();
				tempBankNumber = patch.getBank();
				storeBank(tempBankNumber);
				tempProgramNumber = patch.getProgram();
				storeProgramNumber(tempProgramNumber);
				channel.programChange(bankNumber, programNumber);
				break;
			}

			else if (i == instruments.length - 1) {
				break;
			}
		}
	}
	
	public void storeBank (int tempBankNumber){
		this.bankNumber = tempBankNumber;
	}
	public void storeProgramNumber (int tempProgramNumber){
		this.programNumber = tempProgramNumber;
	}
	
	public int getBank (){
		return bankNumber;
	}
	public int getProgramNumber (){
		return programNumber;
	}

	public void storeInstrumentsList(){
		for (int i =0;i<instruments.length;i++) {
			allInstruments.addElement(instruments[i].getName());
		}	
	}
	
	public DefaultListModel<String> getAllInstruments (){
		return allInstruments;
	}
	
	public String[] allInstruments(Instrument[] channels) {
		String[] tempStorage = new String[channels.length];
		for (int i = 0; i < channels.length; i++) {
			tempStorage[i] = channels[i].getName();
		}
		return tempStorage;
	}
}