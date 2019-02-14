package tools;

import java.util.Timer;
import java.util.TimerTask;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.JToggleButton;
import midi.StoreMetaEvents;
import midiDevices.MidiReceiver;

public class MIDIRecord {

	private  int recordClick = 0;
	static Timer timer;
	private int ppqAddedSpeed = 0;
	private boolean endTimer = false;
	
	public void recordReciever(){	
	}
	
	public void recordAction(JToggleButton recordMIDI) throws InvalidMidiDataException{
		recordClick++;
		MidiReceiver.getInstance().endFreePlay(true);
		MidiReceiver.getInstance().endRecording(false);

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
		
		if (MidiReceiver.getInstance().getFirstRecording() == true) {
			
			
			try {
				MidiReceiver.getInstance().setFirstRecording(false);
				Sequence sequence = new Sequence(Sequence.PPQ, 480);
				MidiReceiver.getInstance().storeSeq(sequence);
				Track track = MidiReceiver.getInstance().getSequence().createTrack();
				MidiReceiver.getInstance().storeTrack(track);
				MidiReceiver.getInstance().returnSequencer().setSequence(sequence);
				MidiReceiver.getInstance().returnSequencer().setTempoInBPM(120);			
				MidiReceiver.getInstance().returnSequencer().recordEnable(MidiReceiver.getInstance().getTrack(), 0);
				MidiReceiver.getInstance().returnSequencer().setTickPosition(0);
				
				
				
				
				
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
		else if (MidiReceiver.getInstance().getFirstRecording() == false) {

			try {
				//Removes all track from sequence to make a new sequence
				for (Track aTrack : MidiReceiver.getInstance().getSequence().getTracks() ){
					MidiReceiver.getInstance().getSequence().deleteTrack(aTrack);
				}
				Track newTrack = MidiReceiver.getInstance().getSequence().createTrack();
				MidiReceiver.getInstance().storeTrack(newTrack);
				MidiReceiver.getInstance().returnSequencer().setSequence(MidiReceiver.getInstance().getSequence());
				MidiReceiver.getInstance().returnSequencer().recordEnable(MidiReceiver.getInstance().getTrack(), -1);

				// Fixes note delay when re-recording song similar to
				// memory leaks of scanner.next()
				
				MidiReceiver.getInstance().returnSequencer().setTickPosition(0);
				
				MidiReceiver.getInstance().returnSequencer().startRecording();

			} catch (InvalidMidiDataException error1) {
				error1.printStackTrace();
			}
		}
		
	}
    public void disableRecord(JToggleButton recordMIDI) throws InvalidMidiDataException{
    
    	//Debugging purposes of track ticks times to log of times in console
    	//Track [] tracks = MidiReceiver.getInstance().getSequence().getTracks();
    	
    	//Might remove in case causes problems
    	//MidiReciever.getInstance().storeCumulativeTime(0);
    	//MidiReciever.getInstance().setSustainTimer(null,true);
    	//MidiReciever.getInstance().buildSustain(0);
    	////////////////////////////////////////////
    	
    	MidiReceiver.getInstance().endFreePlay(false);
    	MidiReceiver.getInstance().endRecording(true);
		recordMIDI.setText("Off");
		MidiReceiver.getInstance().returnSequencer().stopRecording();
		MidiReceiver.getInstance().returnSequencer().recordDisable(MidiReceiver.getInstance().getTrack());
		
		//Add complete sequence's meta data - This is really only useful for multi-track sequences
		//As the meta data is stored in a track when written to a .mid file
		StoreMetaEvents newStore = new StoreMetaEvents();
		newStore.createFullSequenceMetaData();
		
		//Debugging purpose which shows new meta track made here but not above track array
		//Track [] tracksEdited = MidiReceiver.getInstance().getSequence().getTracks();
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
