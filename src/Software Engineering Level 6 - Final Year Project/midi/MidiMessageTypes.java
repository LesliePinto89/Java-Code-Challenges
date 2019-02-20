package midi;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

import midi.Chord.allChordNamesList;
import midi.MidiMessageTypes.tempoNames;
import midiDevices.MidiReceiver;

public class MidiMessageTypes {

	private LinkedHashMap <String,Float> tempoMarkersMap = new LinkedHashMap<String,Float>();
	private DefaultListModel <String> temposInModel = new DefaultListModel <String>();
	private EnumSet<tempoNames> tempoEnums = null;
	private String rememberedTempo ="AllegroModerato";
	private static MidiChannel channel;
	private boolean tempoChanged =false;
	private static volatile MidiMessageTypes instance = null;
	private boolean tempoSliderChanged =false;


	public static MidiMessageTypes getInstance() {
		if (instance == null) {
			synchronized (Chord.class) {
				if (instance == null) {
					instance = new MidiMessageTypes();
					channel = ((Synthesizer) MidiReceiver.getInstance().returnDevice()).getChannels()[0];
					instance.storedTemposMap();
					instance.storeTemposInModel();
				}
			}
		}

		return instance;
	}
	
	private MidiMessageTypes() {
	}
	
	//public MidiMessageTypes (){
	//	channel = ((Synthesizer) MidiReceiver.getInstance().returnDevice()).getChannels()[0];
	//}
	
	
	
	
	
	
	public enum tempoNames {
		Larghissimo (20), // very, very slow (20 bpm and below)
		Grave (44), //slow and solemn (20–40 bpm)
		Lento (52), //slowly (40–60 bpm)
		Largo (50), //broadly (40–60 bpm)
		Larghetto (60), //rather broadly (60–66 bpm)
		Adagio (70), //slow and stately (literally, "at ease") (66–76 bpm)
		Adagietto (75), //rather slow (70–80 bpm)
		
		//Varies based on source of information
		Andante (90), //at a walking pace (76–108 bpm)
		AndanteModerato (93), // a bit slower than andante
		Andantino (87), //slightly faster than andante
		
		Moderato (110), //moderately (108–120 bpm)
		Allegretto (100), //moderately fast (but less so than allegro)
		AllegroModerato (120), // moderately quick (112–124 bpm)
		Allegro (140), //fast, quickly and bright (120–168 bpm)
		Vivace (150), //lively and fast (≈140 bpm) (quicker than allegro)
		Vivacissimo (160), //very fast and lively
		Allegrissimo (170), // very fast
		Presto (180), //very fast (168–200 bpm)
		Prestissimo (200); //extremely fast (more than 200bpm)
		
		 private final float id;
		 tempoNames(float id) { this.id = id; }
		    public float getValue() { return id; }	    
	}
	
	
	public MidiChannel getMidiChannel() {
		return channel;
	}
	
	 ////////////////////////////////////////////////////////////////////////////
	
		public void storedTemposMap (){
			tempoEnums = EnumSet.allOf(tempoNames.class);
			for (tempoNames tempo : tempoEnums) {
				tempoMarkersMap.put(tempo.toString(), tempo.getValue());
		       }
		}
		
		public LinkedHashMap <String, Float> getTemposMap(){ 
			return tempoMarkersMap;
		}
		
		////////////////Get map names////////////////////////
		public String [] storedTemposMapKeys (){
		    int i = 0;
			Set<String> tempoMarksNames = tempoMarkersMap.keySet();
		    String [] convertTempoKeys = new String [tempoMarksNames.size()];
		    tempoMarksNames.toArray(convertTempoKeys);
		    
		    for (String change : convertTempoKeys){
		    	if(change.equals("AllegroModerato")){
		    		change = convertTempoKeys[i] + ": "+ tempoMarkersMap.get(change).toString()+" BPM - Default" ;
		    	}
		    	else {change = convertTempoKeys[i] + ": "+ tempoMarkersMap.get(change).toString()+" BPM";}
		    	convertTempoKeys[i++] = change;
		    }
			return convertTempoKeys;
		}
		
		
		////////////////////////////////////////////////////////////////////////////
		public void storeTemposInModel(){
			for (String s: storedTemposMapKeys())
			{
				temposInModel.addElement(s);
			}
		}
		
		public DefaultListModel <String> getTemposInModel() {
			return temposInModel;
		}	
		
		////////////////////////////////////////////////////////////////////////////
		
		//Used to keep selected tempo when the start recording functions reset it back to default
		public void saveTempoSeqEnd(String choice) {
			MidiReceiver.getInstance().returnSequencer().setTempoInBPM(tempoMarkersMap.get(choice));
		}
		
		public void selectedTempo(String remembered) {
			rememberedTempo = remembered;
			//rememberedTempo = rememberedTempo.substring(0, rememberedTempo.indexOf(":"));
			tempoChanged = true;
		}

		public String getSelectedTempo(){
			return rememberedTempo;
		}
		
		public void tempoChanged(boolean change){
			tempoChanged = change;
		} 
		
		public boolean checkIfTempoChanged(){
			return tempoChanged;
		} 
		
		public void tempoSliderChanged(boolean change){
			tempoSliderChanged = change;
		} 
		
		public boolean checkIfTempoSliderChanged(){
			return tempoSliderChanged;
		} 
		///////////////////////////////////////////////////////////////////////////
	
}
