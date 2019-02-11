package tools;

import java.util.Timer;
import java.util.TimerTask;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.JToggleButton;
import midi.StoreMetaEvents;
import midiDevices.MidiReciever;

public class MIDIRecord {

	private  int recordClick = 0;
	static Timer timer;
	private int ppqAddedSpeed = 0;
	private boolean endTimer = false;
	
	public void recordReciever(){	
	}
	
	public void recordAction(JToggleButton recordMIDI) throws InvalidMidiDataException{
		recordClick++;
		MidiReciever.getInstance().endFreePlay(true);
		MidiReciever.getInstance().endRecording(false);

		// User clicked record button to start cording
		if (recordClick % 2 == 1) {
			enableRecord(recordMIDI);
		}
		
		// User clicked record button to disable recording
		else if (recordClick % 2 == 0) {
			disableRecord(recordMIDI);
		}
		
	}
	
	
	public void enableRecord(JToggleButton recordMIDI){
		recordMIDI.setText("Rec");
		
		if (MidiReciever.getInstance().getFirstRecording() == true) {
			
			
			try {
				MidiReciever.getInstance().setFirstRecording(false);
				Sequence sequence = new Sequence(Sequence.PPQ, 480);
				MidiReciever.getInstance().storeSeq(sequence);
				Track track = MidiReciever.getInstance().getSequence().createTrack();
				MidiReciever.getInstance().storeTrack(track);
				MidiReciever.getInstance().returnSequencer().setSequence(sequence);
				MidiReciever.getInstance().returnSequencer().setTempoInBPM(120);			
				MidiReciever.getInstance().returnSequencer().recordEnable(MidiReciever.getInstance().getTrack(), 0);
				MidiReciever.getInstance().returnSequencer().setTickPosition(0);
				
				
				
				
				
				//metaAndControl();
				// MetaEventListener test = new
				// InteractionEventsInterface(pressedNote,keyboard,playedNotePitch,load);
				// boolean checkMetaEvent =
				// load.returnSequencer().addMetaEventListener(test);

				// Adds a new track as a side effect to possibly fixing
				// the double note problem at beginning of record
				// sequence
				// load.returnSequencer().startRecording();
			} catch (InvalidMidiDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		// User clicked record button after stopping recording to start
		// a new recording
		else if (MidiReciever.getInstance().getFirstRecording() == false) {

			try {
				//Removes all track from sequence to make a new sequence
				for (Track aTrack : MidiReciever.getInstance().getSequence().getTracks() ){
					MidiReciever.getInstance().getSequence().deleteTrack(aTrack);
				}
				Track newTrack = MidiReciever.getInstance().getSequence().createTrack();
				MidiReciever.getInstance().storeTrack(newTrack);
				MidiReciever.getInstance().returnSequencer().setSequence(MidiReciever.getInstance().getSequence());
				MidiReciever.getInstance().returnSequencer().recordEnable(MidiReciever.getInstance().getTrack(), -1);

				// Fixes note delay when re-recording song similar to
				// memory leaks of scanner.next()
				
				MidiReciever.getInstance().returnSequencer().setTickPosition(0);
				
				MidiReciever.getInstance().returnSequencer().startRecording();

			} catch (InvalidMidiDataException error1) {
				error1.printStackTrace();
			}
		}
		
	}
    public void disableRecord(JToggleButton recordMIDI) throws InvalidMidiDataException{
    
    	//Debugging purposes of track ticks times to log of times in console
    	Track [] tracks = MidiReciever.getInstance().getSequence().getTracks();
    	
    	//Might remove in case causes problems
    	//MidiReciever.getInstance().storeCumulativeTime(0);
    	//MidiReciever.getInstance().setSustainTimer(null,true);
    	//MidiReciever.getInstance().buildSustain(0);
    	////////////////////////////////////////////
    	
    	MidiReciever.getInstance().endFreePlay(false);
    	MidiReciever.getInstance().endRecording(true);
		recordMIDI.setText("Off");
		MidiReciever.getInstance().returnSequencer().stopRecording();
		MidiReciever.getInstance().returnSequencer().recordDisable(MidiReciever.getInstance().getTrack());
		
		//Add complete sequence's meta data
		StoreMetaEvents newStore = new StoreMetaEvents();
		newStore.createFullSequenceMetaData();
	}
    
	
/*
  //Experimental code
  		public void metaAndControl() throws InvalidMidiDataException{  		
  	        MetaEventListener metaListener = new MetaEventListener() { 
  	        	Track trk;  	   
  				@Override
  	            public void meta(MetaMessage meta) {
  					
  					//Track trk = reciever.getSequence().getTracks()[1];
  					if(reciever.getSequence().getTracks().length <2){
  						 trk = reciever.getSequence().createTrack();
  					}
  					Track[] tracks = reciever.getSequence().getTracks();
  		            
  		            for (Track track : tracks) {
  		               if(track.equals(trk)){break;}
  		                try {
  							StoreMetaEvents.generateMetaData(track);
  						} catch (InvalidMidiDataException e) {
  							// TODO Auto-generated catch block
  							e.printStackTrace();
  						}
  		            }
  					
  		            
  		            //for ()
  		            //Track original = loadReciever.getSequence().getTracks()[0];
  		            Track meteVersion = reciever.getSequence().getTracks()[1];
  		          System.out.println(meteVersion.get(0).getMessage().getStatus());
  		          System.out.println(meteVersion.get(1).getMessage());
  		         // System.out.println(meteVersion.get(2));
  		         // System.out.println(meteVersion.get(3));
  		         // System.out.println(meteVersion.get(4));
  		         // System.out.println(meteVersion.get(5));
  		            
  		            //System.out.println(loadMIDI.getSeq().getTracks()[0]);
  		            //System.out.println(loadMIDI.getSeq().getTracks()[1]);
  					//final int type = meta.getType();
  	                //byte b = meta.getData()[1];
  	                //int i = (int) (b & 0xFF);
  	                //handleNote(type, i);
  	            }
  	        }; 
  	      reciever.returnSequencer().addMetaEventListener(metaListener);

  	}*/
}
