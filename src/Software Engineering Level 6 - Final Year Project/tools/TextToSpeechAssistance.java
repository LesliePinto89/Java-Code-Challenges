package tools;

import java.util.Locale;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer; 
import javax.speech.synthesis.SynthesizerModeDesc;


public class TextToSpeechAssistance {
	
    private Synthesizer synthesizer;
    
	private static volatile TextToSpeechAssistance instance = null;

	private TextToSpeechAssistance() {
	}

	public static TextToSpeechAssistance getInstance() throws EngineException, AudioException, EngineStateError {
		if (instance == null) {
			synchronized (TextToSpeechAssistance.class) {
				if (instance == null) {
					instance = new TextToSpeechAssistance();
					instance.prepareFunction();
				}
			}
		}
		return instance;
	}
	
	public void prepareFunction() throws EngineException, AudioException, EngineStateError{
		 try 
	     { 
		  System.setProperty("freetts.voices", 
		             "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");  
		               
		         // Register Engine 
		         Central.registerEngineCentral 
		             ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral"); 

		         // Create a Synthesizer 
		          synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));      
		   
		         // Allocate synthesizer 
		         synthesizer.allocate();         
		           
		         // Resume Synthesizer 
		         synthesizer.resume();     
		         
		         // speaks the given text until queue is empty. 
		         synthesizer.speakPlainText("test", null); 
		         
		         synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY); 
		           
		         // Deallocate the Synthesizer. 
		         synthesizer.deallocate();   
	     }
		         catch (Exception e)  
		         { 
		             e.printStackTrace(); 
		         }          
	}

}
