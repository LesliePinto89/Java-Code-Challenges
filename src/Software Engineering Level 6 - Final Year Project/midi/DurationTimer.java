package midi;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

import midiDevices.MidiReceiver;

public class DurationTimer {

	private static int durationResolution = 0;
	static Timer timerDuration;
	private volatile boolean exit = false;
	private long lastNoteOffEpochTime;
	private long allDeltas = 0;
	
	//A singleton pattern so that only one instance of this class 
	//can be accessed and instantiated
	private static volatile DurationTimer instance = null;

    private DurationTimer() {}

    public static DurationTimer getInstance() {
        if (instance == null) {
            synchronized(DurationTimer.class) {
                if (instance == null) {
                    instance = new DurationTimer();
                }
            }
        }

        return instance;
    }
	
	// Stored previous note cumulative value for next note's delta
	public void storeCumulativeTime(long carriedDeltaTicks) {
		allDeltas = carriedDeltaTicks;
	}

	public long getCumulativeTime() {
		return allDeltas;
	}
	
	public void storeNoteOffTimeStamp (long noteOffTimeStamp){
		lastNoteOffEpochTime = noteOffTimeStamp;
	}
	
	public long getNoteOffTimeStamp (){
		return lastNoteOffEpochTime;
	}
    
	public void buildDuration(int newDurationRes) {
		durationResolution = durationResolution + newDurationRes;
	}

	public int getCycledDuration() {
		return durationResolution;
	}

	public void resetDuration() {
		durationResolution = 0;
	}

	public void setDurationTimer(JButton passedButton, boolean endCycleBool) {
         //MIGHT NEED TO REMOVE PASSED BUTTI IF NOT USED
		if (endCycleBool == true) {
			timerDuration.cancel();
			System.out.println("Timer for recording duration has ended");
		}

		else {
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {	
					// Added the smallest value of a 1/64 note of the set resolution used in the 
					// cycle to determine exact note through use of cumulative tick values during sustain.
					buildDuration(MidiReceiver.getInstance().getCurrentSequenceResolution() / 16);
				}
			};

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					
					while (!exit) {

						try {
							//MIGHT NEED TO GET RID OF THIS CODE INCASE THE SUSTAIN AFFECT OF SOME
							//NOTES GET RUINED OR THE TIMING AFTER NO SOUND GETS AFFECTED
							if(getCycledDuration() == MidiReceiver.getInstance().getCurrentSequenceResolution() * 4){
								System.out.println("Current duration value: "+getCycledDuration());
								System.out.println("Is this a whole note value: "+MidiReceiver.getInstance().getCurrentSequenceResolution() * 4);
								timerDuration.cancel();
								stop();
								
							}
          					Thread.sleep(1000);
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}
				}

				public void stop() {
					exit = true;
				}
			});

			timerDuration = new Timer("MyTimer");
			
			//start timer in 63 milliseconds as this is 1/16 of a second (1000ms) as its 
			//allows more accurate accumulation of ticks to decide what note speed is being made.
			timerDuration.scheduleAtFixedRate(timerTask, 0, 63);
			t.start();
			
		}
	}
	
	
	//Turned off now that using epoch difference per rest time
	/*	public void setClockTimer(boolean noteIsOn) {
			
			 if(noteIsOn ==true){
				 System.out.println("Timer stopped properly");
				 timer.cancel();
				// endTimer = true; //Added this messed with code a bit
			 }
	    	
			 else {
	    		
		      TimerTask timerTask = new TimerTask() {

		            @Override
		            public void run() {
		             if(endTimer == true){
		            		 
		            	 MidiReciever.getInstance().storeRestTickCycle(ppqAddedSpeed);
		             }
		               else if(endTimer == false){
		            	 
		            	  // System.out.println("This is the ppqPerQuarter: "+MidiReciever.getInstance().ticksPerQuarterUsingPPQ());
		            	  // ppqAddedSpeed = MidiReciever.getInstance().ticksPerQuarterUsingPPQ();
		            	   
		            	   System.out.println("This is the Clock of ticksPerSixteenthsUsingPPQ: "+MidiReciever.getInstance().getRestTickCycle());
		            	   ppqAddedSpeed = MidiReciever.getInstance().ticksPerSixteenthsUsingPPQ();
		            	 }    
		        }  
		      };
		        
		        //create thread to print counter value
		        Thread t = new Thread(new Runnable() {

		            @Override
		            public void run() {
		                while (true) {
		                    try {
		                        //System.out.println("Thread reading counter is: " + ppqAddedPerSecond);
		                       	                    	
		                    	if (recordClick % 2 == 0) {

		                    		endTimer = true;
		                    		
		                           // ppqAddedPerSecond = 0;
		                            
		                            timer.cancel();//end the timer
		                           // ppqAddedPerSecond = reciever.ppqPerSecond();
		                            MidiReciever.getInstance().storeRestTickCycle(0);
		                            MidiReciever.getInstance().defaultSetStartTick(0);
		                            
		                            break;//end this loop
		                        }
		                    	
		                    	
		                        Thread.sleep(1000);
		                    } catch (InterruptedException ex) {
		                        ex.printStackTrace();
		                    }
		                }
		            }
		            
		        });

		        timer = new Timer("MyTimer");//create a new timer
		        timer.scheduleAtFixedRate(timerTask, 0, 250);//start timer in 250ms for 1/16
		        
		        //Original code before above change
		       //timer.scheduleAtFixedRate(timerTask, 0, 1000);//start timer in 30ms to increment  counter

		        t.start();//start thread to display counter
		        
		     	if (noteIsOn == true) {
		        timer.cancel();//end the timer
		     	}
			 }
		    }
		    */
	
}
