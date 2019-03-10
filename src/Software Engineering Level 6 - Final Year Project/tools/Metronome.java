package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import keyboard.FeatureTabs;
import keyboard.KeyboardInteractions;
import midi.MidiMessageTypes;
import midiDevices.MidiReceiver;

public  class Metronome implements MetaEventListener {

	
	private JSlider tempoSlider;
	private static volatile Metronome instance = null;

    private Metronome() {}

    public static Metronome getInstance() {
        if (instance == null) {
            synchronized(Metronome.class) {
                if (instance == null) {
                    instance = new Metronome();
               
                }
            }
        }

        return instance;
    }
	
	private Track metroTrack;
	private float chosenBPM;
	private float defaultScaleBPM = 100;
	private Sequencer sequencer = MidiReceiver.getInstance().returnSequencer();
	private MidiMessageTypes msgTypes = MidiMessageTypes.getInstance();
	private JLabel currentTempo;
	
	public void chooseTempo() throws InvalidMidiDataException{
		if (MidiMessageTypes.getInstance().checkIfTempoSliderChanged() ==true){
			 chosenBPM = tempoSlider.getValue();
			 MidiMessageTypes.getInstance().tempoSliderChanged(false);
		}
		else {
		String newTempoName = msgTypes.getSelectedTempo();
		 chosenBPM = msgTypes.getTemposMap().get(newTempoName);}
		 setupMetronomeSequence();
		 startSequence();
		 
	}
	
	public void setupMetronomeSequence () throws InvalidMidiDataException {
		Sequence metroSeq = new Sequence(Sequence.PPQ, 1);
		 metroTrack = metroSeq.createTrack();
		 sequencer.setSequence(metroSeq);	
		 addNoteEvent(metroTrack, 0);
         addNoteEvent(metroTrack, 1);
         addNoteEvent(metroTrack, 2);
         addNoteEvent(metroTrack, 3);
         sequencer.addMetaEventListener(this);
	}
	
	private void startSequence() throws InvalidMidiDataException {
        sequencer.setTempoInBPM(chosenBPM);
        sequencer.start();
    }

	
	@Override
    public void meta(MetaMessage message) {
        if (message.getType() != 47) {  // 47 is end of track
            return;
        }
        doLoop();
    }
	
	private void doLoop() {
        if (sequencer == null || !sequencer.isOpen()) {
            return;
        }
        sequencer.setTickPosition(0);
        sequencer.start();
        sequencer.setTempoInBPM(chosenBPM);
    }
	
       public void stopLoop (){
		sequencer.stop();
		//MidiMessageTypes.getInstance().setPlayTempoCounter(0);
	    }

   	public JPanel tempoSlider() {
   		
   		JPanel instancePanel = new JPanel();
   		int featureWidth = SwingComponents.getInstance().getScreenWidth();
  
   		tempoSlider = new JSlider(0, 218, (int)defaultScaleBPM);
   	    //DOES NOT NEED MINIMUM SIZE - RUINS SCALING
   		tempoSlider.setPreferredSize(new Dimension (featureWidth/4 - 20, featureWidth/8));
   		tempoSlider.setForeground(Color.RED);
   		tempoSlider.setBackground(Color.ORANGE);
   		tempoSlider.setPaintTrack(true); 
   		tempoSlider.setPaintTicks(true); 
   		tempoSlider.setPaintLabels(true); 
   		
   		tempoSlider.setMajorTickSpacing(50); 
   		tempoSlider.setMinorTickSpacing(5); 
   	
   		tempoSlider.setOrientation(SwingConstants.HORIZONTAL); 
     
     // set Font for the slider 
   		tempoSlider.setFont(new Font("Serif", Font.ITALIC, 20)); 
   		
   		tempoSlider.addChangeListener(new ChangeListener(){
   		 	public void stateChanged(ChangeEvent e) 
   		    { 
   		   		currentTempo.setText("BPM: "+tempoSlider.getValue());
   		    	MidiMessageTypes.getInstance().tempoSliderChanged(true);
   		    } });
	
       // 
        
        currentTempo = new JLabel();
        currentTempo.setText(tempoSlider.getValue()+" BPM");
        currentTempo.setForeground(Color.BLACK);
        
        //DOES NOT NEED MINIMUM SIZE - RUINS SCALING
        currentTempo.setPreferredSize(new Dimension (100, 40));
        
        
        instancePanel.add(currentTempo);
        instancePanel.add(tempoSlider);
        instancePanel.setPreferredSize(new Dimension(featureWidth/4 - 20, featureWidth/8));
        //instancePanel.setBounds(0, 100, featureWidth/4 - 20, featureWidth/8);
        
		//KeyboardInteractions volumeSliderListener = new KeyboardInteractions(tempoSlider);
		//tempoSlider.addChangeListener(volumeSliderListener);
        
        
		return instancePanel;
		//keyboardLayered.add(slider);
	}
   
  
	 private void addNoteEvent(Track track, long tick) throws InvalidMidiDataException {
	        ShortMessage message = new ShortMessage(ShortMessage.NOTE_ON, 9, 37, 100);
	        MidiEvent event = new MidiEvent(message, tick);
	        metroTrack.add(event);
	    }
	 
	 
	 
	 
	 
}