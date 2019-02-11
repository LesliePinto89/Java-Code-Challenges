package keyboard;
//import java.util.EnumSet;

public class Note {

	private String name;
	private String accidental;
	private int pitch;
	private NoteType type;
	private int velocity;
	private int octave;
	
	
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
	
	
	public Note (){;}
	
	public Note (String name, String accidental, int octave, int pitch,int velocity){
		this.name = name;
		this.accidental = accidental;
		this.octave = octave;
		this.pitch = pitch;
		this.velocity = velocity;
	}
	
	 public static int convertToPitch(String note) {
   	  String symbol = "";
   	  int octave = 0;;
   	  
   	//EnumSet<allNotesNoteType> allNotesEnums = EnumSet.allOf(allNotesNoteType.class);
   			//String [] storeNotes = new String [allNotesEnums.size()];
   			//allNotesEnums.toArray(storeNotes);
   	  
   	String[][] notes = { {"C"}, {"Db", "C#"}, {"D"}, {"Eb", "D#"}, {"E"},
   	    {"F"}, {"Gb", "F#"}, {"G"}, {"Ab", "G#"}, {"A"}, {"Bb", "A#"}, {"B"} };

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
   	  for (int i = 0; i < notes.length; i++)
   	  for (int j = 0; j < notes[i].length; j++) {
   	    if (notes[i][j].equals(symbol)) {
   	        return Character.getNumericValue(octave+1) * 12 + i;
   	    }
   	  }

   	  // If nothing was found, we return -1.
   	  return -1;
   	}
	
	
	

	
	public NoteType getType() {
		return type;
	}

	public void setType(NoteType type) {
		this.type = type;
	}
}
