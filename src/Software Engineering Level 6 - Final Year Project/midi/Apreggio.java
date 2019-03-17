package midi;

import java.util.ArrayList;

public class Apreggio {

	private String apreggioName;
	private ArrayList<Chord> storeApreggio = new ArrayList<Chord>();
	public Apreggio(String apreggioName, ArrayList<Chord> brokenChordNotes) {
       this.apreggioName = apreggioName;
       this.storeApreggio = brokenChordNotes;
	}
	
    public String getApreggioName() {
    	return apreggioName;
    }
    
    public ArrayList<Chord> getApreggioNotes () {
    	return storeApreggio;
    }
    
    
}
