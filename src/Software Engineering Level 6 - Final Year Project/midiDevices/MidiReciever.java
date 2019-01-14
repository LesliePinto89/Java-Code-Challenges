package midiDevices;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

public class MidiReciever implements Receiver {

	protected MidiDevice device;
	private Sequencer sequencer;
	private Transmitter seqTrans;
	private Receiver seqRcvr;
	private Receiver synthRcvr;
	private boolean stopRecording = true;
	private boolean stopPianoFreePlay = false;
	private boolean firstRecording = true;
	protected Sequence sequence;
	private long startTick = 0;	
	private int resolution;
	private int ticksPerSecond;
	private int tickSize;
	private Track track;
	
	public void startConnection() throws InvalidMidiDataException, MidiUnavailableException {
		ArrayList<MidiDevice.Info> synthInfos = new ArrayList<MidiDevice.Info>();
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			device = MidiSystem.getMidiDevice(infos[i]);
			if (device instanceof Synthesizer) {
				synthInfos.add(infos[i]);
			}
		}
		device = MidiSystem.getMidiDevice(synthInfos.get(0));
		loadUp();
	}
	

	public void loadUp() throws MidiUnavailableException {
		if (!(device.isOpen())) {
			device.open();
		}
		synthRcvr = device.getReceiver();
		sequencer = MidiSystem.getSequencer();
		seqRcvr = sequencer.getReceiver();
		seqTrans = sequencer.getTransmitter();
		seqTrans.setReceiver(synthRcvr);
		sequencer.open();
		storeDevice(device);
	}

	public void storeTrack(Track track) {
		this.track = track;
	}

	public Track getTrack() {
		return track;
	}
	
	public int calculateTick(){
		//System.out.print(sequence);
		resolution = sequence.getResolution();
		float tempo = sequencer.getTempoInBPM();
		ticksPerSecond =  (int) (60000 / (tempo * resolution));
		
		//This was needed in bettwe version but not in working other version
		//might or might not be the btter solution
		ticksPerSecond = ticksPerSecond* 60;
		//might need to remote cast as time is not working properly
		//ticksPerSecond = (int) (resolution * (tempo / 60));
		 tickSize = 1 / ticksPerSecond;
		 return ticksPerSecond;
		
	}
	
	
	public void setStartTick(long startTick) {
		this.startTick = startTick;
	}

	public long getStartTick() {
		return startTick;
	}
	public void storeSeq(Sequence sequence) {
		this.sequence = sequence;
	}

	public Sequence getSequence() {
		return sequence;
	}
	
	//test code
	public void createNewSequence () throws InvalidMidiDataException {
     sequence = new Sequence(Sequence.PPQ, 192);
	}
	
	public MidiDevice returnDevice() {
		return device;
	}

	public void storeDevice(MidiDevice aDevice) {
		this.device = aDevice;
	}

	public Sequencer returnSequencer() {
		return sequencer;
	}

	protected Receiver returnSequencerReceiver() {
		return seqRcvr;
	}

	protected Transmitter returnSeqTransmitter() {
		return seqTrans;
	}

	@Override
	public void close() {
	}

	public void freeNotePlay(int pitch) throws InvalidMidiDataException {
		ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, pitch, 100);
		send(noteOnMessage, -1);

	}
	
	public void freeNoteStop(int pitch) throws InvalidMidiDataException {
		ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, pitch, 100);
		send(noteOnMessage, -1);

	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		synthRcvr.send(message, timeStamp);

	}

	//CONDITION VARIABLES/////////////////////////////////
	public void setFirstRecording(boolean firstRec) {
		this.firstRecording = firstRec;
	}

	public boolean getFirstRecording() {
		return firstRecording;
	}

	public void endRecording(boolean stopRec) {
		this.stopRecording = stopRec;
	}

	public boolean isRecEnded() {
		return stopRecording;
	}

	public void endFreePlay(boolean stopFreePlay) {
		this.stopPianoFreePlay = stopFreePlay;
	}

	public boolean isFreePlayEnded() {
		return stopPianoFreePlay;
	}
	
	public boolean isRunning(){
		return sequencer.isRunning();
	}
	/////////////////////////////////////////////////////////
	
	
}