package midi;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

import midiDevices.MidiReciever;

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
					buildDuration(MidiReciever.getInstance().getCurrentSequenceResolution() / 16);
					
					//Original code incase above does not work, but is hard coded to 480 PPQ
					//buildSustain(30);
					
					//System.out.print("Sustain value: " + sustainResolution + ",");
				}
			};

			// create thread to print counter value
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// System.out.println("Value of exit is: "+exit);
					while (!exit) {

						try {
							//MIGHT NEED TO GET RID OF THIS CODE INCASE THE SUSTAIN AFFECT OF SOME
							//NOTES GET RUINED OR THE TIMING AFTER NO SOUND GETS AFFECTED
							if(getCycledDuration() == MidiReciever.getInstance().getCurrentSequenceResolution() * 4){
								System.out.println("Current duration value: "+getCycledDuration());
								System.out.println("Is this a whole note value: "+MidiReciever.getInstance().getCurrentSequenceResolution() * 4);
								timerDuration.cancel();
								stop();
								//break;
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
}
