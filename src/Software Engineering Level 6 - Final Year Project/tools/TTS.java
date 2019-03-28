package tools;

import java.util.ArrayList;
import java.util.Locale;

import javax.sound.midi.InvalidMidiDataException;
import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer; 
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.text.BadLocationException;

import midi.Chord;
import midi.ChordProgression;
import midi.Scale;


public class TTS {
	
    private  Synthesizer synthesizer;
    
	private static volatile TTS instance = null;

	private TTS() {
	}

	public static TTS getInstance() throws EngineException, AudioException, EngineStateError {
		if (instance == null) {
			synchronized (TTS.class) {
				if (instance == null) {
					instance = new TTS();

				}
			}
		}
		return instance;
	}
	
	
	public ArrayList<String> createProgressionTTS(String text){
		PlaybackFunctions.timeDelay(1000);
		String[] getBits = text.split("\\s+");
		ArrayList<String> convertToNumerals = new ArrayList<String>();
		int i =0;
		int numeralListIndex = 0;
		ArrayList<String> getStoredInnerNumerals = ChordProgression.getNamedNumberOfNumeral();
		ArrayList<String> getStoredOuterNumerals = ChordProgression.getNamedOnlyNumberOfNumeral();
		
		for (int l = 0; l < getStoredOuterNumerals.size();l++){
			if (getBits[i].equals(getStoredOuterNumerals.get(numeralListIndex))){
				convertToNumerals.add(getStoredInnerNumerals.get(numeralListIndex).toString());
				l = -1;
				i++;
				numeralListIndex =0;
			}
			if (i == getBits.length){
				break;
			}
			numeralListIndex++;
		}
		return convertToNumerals;
	}
	
	public String theoryToTTS(Chord foundChord){
		PlaybackFunctions.timeDelay(1000);
		String name = foundChord.getChordNotes().get(0).getName();
		String editedNote = name.substring(0, name.length()-1);
		editedNote = editedNote.contains("#") ? editedNote.replace("#", " SHARP") : editedNote;
			
		String adjust =  "";
		String chordName = foundChord.getChordName();
		for (String aChord: Chord.getAllChordEnums()){
		    if(chordName.equals(aChord) && aChord.contains("min") && aChord.contains("Maj")){						
				adjust = aChord.replaceAll("minMaj", "minor major ");
				editedNote += " "+adjust;
				break;
			}
			
			 else if(chordName.equals(aChord) && aChord.contains("maj")){	
				 if(aChord.contains("Tetra")){
					 adjust = aChord.replaceAll("maj", "major ");
				 }
				 else {
				adjust = aChord.replaceAll("maj", "major");
				}
				 editedNote += " "+adjust;
				break;					
			}
			
			else if(chordName.equals(aChord) && aChord.contains("min")){	
				 if(aChord.contains("Tetra")){
					 adjust = aChord.replaceAll("min", "minor ");
				 }
								 
				 else {
				adjust = aChord.replaceAll("min", "minor ");
				 if(aChord.contains("7")){						
						adjust = adjust.replaceAll("7", "7 ");
					}
				 if(aChord.contains("b")){						
						adjust = adjust.replaceAll("b", "flat ");
					}
				}
				editedNote += " "+adjust;
				break;
			}
			
			else if(chordName.equals(aChord) && aChord.contains("add")){						
				adjust = aChord.replaceAll("add", "added");
				editedNote += " "+adjust;
				break;
			}
			 
			else if(chordName.equals(aChord) && aChord.contains("aug")){						
				adjust = aChord.replaceAll("aug", "augmented");
				editedNote += " "+adjust;
				break;
			}
		    
			else if(chordName.equals(aChord) && aChord.contains("sus")){						
				adjust = aChord.replaceAll("sus", "sustain");
				editedNote += " "+adjust;
				break;
			}
			else if(chordName.equals("11") || chordName.equals("7") || chordName.equals("13") || chordName.equals("9")){						
				editedNote +=chordName;
				break;
			}
			
			else if(chordName.equals(aChord) && aChord.contains("dim")){	
				adjust = aChord.replaceAll("dim", "diminished");
				editedNote += " "+adjust;
				break;
			}
		}
		return editedNote;
	}
	
	public void prepareFunction(String feature, String text) throws EngineException, EngineStateError, 
	IllegalArgumentException, InterruptedException, AudioException {
		 
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
		            //Use shared volative boolean, is triggered to true depending on which feature state uses it.
					//This is because previous and home makes it true and this is reflected here
					if(!ScreenPrompt.getInstance().getShared()) {
		ArrayList <String> conditionalData = new ArrayList <String>();
		switch(feature){
		
		case "Progression" : conditionalData =createProgressionTTS(text);break;
		case "Chord" : conditionalData.add(text);break;
		case "Scale" : conditionalData.add(text);break;
		case "Build" : conditionalData.add(text);break;
		}
		
		  System.setProperty("freetts.voices", 
		             "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");  
		               
		         // Register Engine 
		         Central.registerEngineCentral ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral"); 

		         // Create a Synthesizer 
		          synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));      
		   
		         // Allocate synthesizer 
		         synthesizer.allocate();         
		           
		         // Resume Synthesizer 
		         synthesizer.resume();   
		      
		         for (String aString : conditionalData){

		        	// speaks the given text until queue is empty. 
			         synthesizer.speakPlainText(aString,null); 
			         PlaybackFunctions.timeDelay(500);
			         
		         }
		         
//		         // speaks the given text until queue is empty. 
//		         synthesizer.speakPlainText(text, null); 
		         
		       
		         
		         synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY); 
	         
				}
				}  catch (InterruptedException | AudioException | EngineStateError | EngineException e) {
					e.printStackTrace();
				}
				
			}
		         
		}).start();
	}
		         
                          
	
}

