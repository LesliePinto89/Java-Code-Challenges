package keyboard;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import midi.ListOfScales;
import midi.Scale;

//import java.util.EnumSet;

public class Note {
	
	private String name;
	private String accidental;
	private int pitch;
	private int velocity;
	private int octave;
	
	protected static Map <String,Note> mapOfNotes = new LinkedHashMap <String,Note>();
	public Note (){;}
	
	public String getType() {
		return accidental;
	}
	
	public Note (String name, int pitch,int newOctave, int newVelocity, String noteType){
		this.name = name; //includes any accidental and octave number
		this.pitch = pitch; 
		this.octave = newOctave; 
		this.velocity = newVelocity; 
		this.accidental = noteType;
	}
	
	public Note (String name){
		this.name = name; //includes any accidental and octave number
	}
	
	//Add each note created during button creation with string + octave, and other note
	//values stored in note object 
	public  static Map <String,Note> getNotesMap (){
	  return mapOfNotes;
	}
	
	public void storeNotes (String noteName, Note aNote){
		mapOfNotes.put(noteName, aNote);
	}
	
	protected static Note getNotes (String key){
		return mapOfNotes.get(key);
	}
	
	protected static Collection<Note> getNotesValues (){
		return mapOfNotes.values();
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
	
	//Experiment to make an enum / String array of notes names
	public enum allNotesNoteType {
		 C(new String [] {"C"}),
		 CSHARPORDFLAT(new String []{"Db,C#"}),
		 DSHARPOREFLAT(new String [] {"Eb","D#"}),
		  D(new String []{"D"}),
		 E(new String [] {"E"}),
		 F(new String []{"F"}), 
		 
		 FSHARP(new String [] {"Gb","F#"}),
		 G(new String []{"G"}),
		 GSHARP(new String []{"Ab","G#"}),
		 A(new String []{"A"}), 
		 ASHARP(new String [] {"Bb","A#"}),
		 B(new String []{"B"});
		
		public final String [] note;
		allNotesNoteType(String []note) {
	        this.note = note;
	    }
	    public String [] getSharp() {
	        return note;
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

   	  // Find the corresponding note in the array.
   	 /// for (int i = 0; i < notes.length; i++)
   	  //for (int j = 0; j < notes[i].length; j++) {
   	  //  if (notes[i][j].equals(symbol)) {
   	    ///    return Character.getNumericValue(octave+1) * 12 + i;
   	    //}
   	  //}
   	  
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
