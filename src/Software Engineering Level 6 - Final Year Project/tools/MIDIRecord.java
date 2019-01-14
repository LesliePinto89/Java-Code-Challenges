package tools;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.JToggleButton;

import midi.StoreMetaEvents;
import midiDevices.MidiReciever;
public class MIDIRecord {

	private int recordClick = 0;
	private MidiReciever reciever;
		
	
	public void recordReciever(MidiReciever carriedReciever){
		this.reciever = carriedReciever;
		
	}
	
	public void recordAction(JToggleButton recordMIDI) throws InvalidMidiDataException{
	
	
		recordClick++;
		reciever.endFreePlay(true);
		reciever.endRecording(false);

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
		
		if (reciever.getFirstRecording() == true) {
			
			
			try {
				reciever.setFirstRecording(false);
				Sequence sequence = new Sequence(Sequence.PPQ, 192);
				reciever.storeSeq(sequence);
				Track track = reciever.getSequence().createTrack();
				reciever.storeTrack(track);

				reciever.returnSequencer().setSequence(sequence);
				
				//MIGHT NEED TO CHANGE THIS BACK
				//load.returnSequencer().setTempoInBPM(120);
				
				
				reciever.returnSequencer().recordEnable(reciever.getTrack(), 0);

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
		else if (reciever.getFirstRecording() == false) {

			try {
				//Removes all track from sequence to make a new sequence
				for (Track aTrack : reciever.getSequence().getTracks() ){
					reciever.getSequence().deleteTrack(aTrack);
				}
				Track newTrack = reciever.getSequence().createTrack();
				reciever.storeTrack(newTrack);
				reciever.returnSequencer().setSequence(reciever.getSequence());
				reciever.returnSequencer().recordEnable(reciever.getTrack(), 0);

				// Fixes note delay when re-recording song similar to
				// memory leaks of scanner.next()
				reciever.setStartTick(0);
				reciever.returnSequencer().setTickPosition(0);
				reciever.returnSequencer().startRecording();

			} catch (InvalidMidiDataException error1) {
				error1.printStackTrace();
			}
		}
		
	}
    public void disableRecord(JToggleButton recordMIDI) throws InvalidMidiDataException{
    	reciever.endFreePlay(false);
		reciever.endRecording(true);
		recordMIDI.setText("Off");
		reciever.returnSequencer().stopRecording();
		reciever.returnSequencer().recordDisable(reciever.getTrack());
		
		//Add complete sequence's meta data
		StoreMetaEvents newStore = new StoreMetaEvents(reciever);
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
