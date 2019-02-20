package midiDevices;

import java.util.Collection;

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
	private DefaultListModel<String> pianoInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> percussionInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> organInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> guitarInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> bassInstruments = new DefaultListModel<String>();
	
	private DefaultListModel<String> stringInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> ensembleInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> brassInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> reedInstruments = new DefaultListModel<String>();
	private DefaultListModel<String> pipeInstruments = new DefaultListModel<String>();
	
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
					//instance.storeAllInstruments();
				}
			}
		}

		return instance;
	}

	public void setupInstruments() {
		instruments = ((Synthesizer) MidiReceiver.getInstance().returnDevice()).getDefaultSoundbank().getInstruments();
		channel = ((Synthesizer) MidiReceiver.getInstance().returnDevice()).getChannels()[0];
	}
	
	public Instrument[] getListOfInstruments() {
		return instruments;
	}
	
	
	
	public MidiChannel getChannelSetToInstrument(){
		return channel;
	}

	// experiment
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
			
			if (i <= 7 ){
				pianoInstruments.addElement(instruments[i].getName());
			}	
			else if (i >= 8 && i <=17){
				percussionInstruments.addElement(instruments[i].getName());	
			}
			else if (i >= 18 && i <=25){
				organInstruments.addElement(instruments[i].getName());	
			}
			
			else if (i >= 26 && i <=31){
				guitarInstruments.addElement(instruments[i].getName());	
			}
			else if (i >= 32 && i <=39){
				bassInstruments.addElement(instruments[i].getName());	
			}
			
			/////////////////////////////
			else if (i >= 40 && i <=47){
				stringInstruments.addElement(instruments[i].getName());	
			}
			else if (i >= 48 && i <=55){
				ensembleInstruments.addElement(instruments[i].getName());	
			}
			/////////////////////////////
			
			else if (i >= 56 && i <=63){
				brassInstruments.addElement(instruments[i].getName());	
			}
			else if (i >= 64 && i <=71){
				reedInstruments.addElement(instruments[i].getName());	
			}
			else if (i >= 72 && i <=79){
				pipeInstruments.addElement(instruments[i].getName());	
			}
			allInstruments.addElement(instruments[i].getName());
		}	
	}
	
	public DefaultListModel<String> getAllInstruments (){
		return allInstruments;
	}
	
	
	public DefaultListModel<String> getPianoInstruments (){
		return pianoInstruments;
	}
	
	public DefaultListModel<String> getPercussionInstruments (){
		return percussionInstruments;
	}
	
	public DefaultListModel<String> getOrganInstruments (){
		return organInstruments;
	}
	
	public DefaultListModel<String> getBassInstruments (){
		return bassInstruments;
	}
	
	public DefaultListModel<String> getStringInstruments (){
		return stringInstruments;
	}
	
	public DefaultListModel<String> getEnsembleInstruments (){
		return ensembleInstruments;
	}
	public DefaultListModel<String> getBrassInstruments (){
		return brassInstruments;
	}
	
	public DefaultListModel<String> getReedInstruments (){
		return reedInstruments;
	}
	
	public DefaultListModel<String> getPipeInstruments (){
		return pipeInstruments;
	}
	
//	public String[] allInstruments(Instrument[] channels) {
	//	String[] tempStorage = new String[channels.length];
		///for (int i = 0; i < channels.length; i++) {
		//	tempStorage[i] = channels[i].getName();
		//}
		//return tempStorage;
	//}
}