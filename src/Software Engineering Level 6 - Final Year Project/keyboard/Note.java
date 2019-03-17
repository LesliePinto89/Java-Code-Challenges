package keyboard;

import java.util.LinkedHashMap;
import java.util.Map;
import midi.ListOfScales;

public class Note {
	
	private String name;
	private String accidental;
	private int pitch;
	private int velocity;
	private int octave;
	
	protected static Map <String,Note> mapOfNotes = new LinkedHashMap <String,Note>();
	public Note (){;}
	
	public Note (String name, int pitch,int newOctave, int newVelocity, String noteType){
		this.name = name; //includes any accidental and octave number
		this.pitch = pitch; 
		this.octave = newOctave; 
		this.velocity = newVelocity; 
		this.accidental = noteType;
	}
	
	//Add each note created during button creation with string + octave, and other note
	//values stored in note object 
	public static Map <String,Note> getNotesMap (){
	  return mapOfNotes;
	}
	
	public void storeNotes (String noteName, Note aNote){
		mapOfNotes.put(noteName, aNote);
	}
	
	public static Note getNotes (String key){
		return mapOfNotes.get(key);
	}
	
	//Could not get the reference value to stop being overwritten, so left using this method as part of it
//	public void setScaleDegree (String position){
//		this.scaleDegree = position;
//	}

//	public String getScaleDegree (){
//		return scaleDegree;
//	}
	
	public String getType() {
		return accidental;
	}
	
	public int getPitch (){
		return pitch;
	}
	
	public void setPitch (int pitch){
		this.pitch = pitch;
	}
	
	public String getName (){
		return name;
	}
	
	public int getOctave (){
		return octave;
	}
	
	public int getVelocity (){
		return velocity;
	}
	
	////////////////////////////////////////////////////
	
	public enum allNotesType {
		C("C"),
		CSHARP("C#"),
		D("D"),
		DSHARP("D#"),
		E("E"),
		F("F"),
		FSHARP("F#"),
		G("G"),
		GSHARP("G#"),
		A("A"),
		ASHARP("A#"),
		B("B");
		
		public final String note;
		allNotesType(String note) {
	        this.note = note;
	    }
	    public String getNote() {
	        return note;
	    }	    
	}
	
	public enum NoteType {
     C, D, E, F, G,  A, B,
	}

	
	public enum SharpNoteType {
		CSHARP("C#"),
		DSHARP("D#"),
		FSHARP("F#"),
		GSHARP("G#"),
		ASHARP("A#");
		
		public final String noteSharp;
		SharpNoteType(String noteSharp) {
	        this.noteSharp = noteSharp;
	    }
	    public String getSharp() {
	        return noteSharp;
	    }	    
	}
	
	public static int convertToPitch(String note) {
   	String symbol = "";
   	int octave = 0;;
   	char[] splitNote = note.toCharArray();
   	  

   	  // If the length is two, then grab the symbol and number.
   	  // Otherwise, it must be a two-char note.
   	  if (splitNote.length == 2) {
   		symbol += splitNote[0];
   		octave = splitNote[1];
   	  } else if (splitNote.length == 3) {
   		symbol += Character.toString(splitNote[0]);
   		symbol += Character.toString(splitNote[1]);
   		octave = splitNote[2];
   	  }

   	 for (int i = 0; i < ListOfScales.getChromaticScale().length; i++)
      	  for (int j = 0; j < ListOfScales.getChromaticScale()[i].length; j++) {
      	    if (ListOfScales.getChromaticScale()[i][j].equals(symbol)) {
      	        return Character.getNumericValue(octave+1) * 12 + i;
      	    }
      	  }
   	  
   	  // If nothing was found, we return -1.
   	  return -1;
   	}
}
