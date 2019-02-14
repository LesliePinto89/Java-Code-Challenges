package midi;

import javax.swing.DefaultListModel;

public class ChordProgression {

	private Chord aChord;
	private int rootNote;
	
	
	private DefaultListModel <String> storeChordProgressions = new DefaultListModel <String>();
	
	
	
	
	public ChordProgression () {
		storeProgressionInList();
	}
	
	public void storeProgressionInList(){
		int i = 0;
		progression[] allProgressions = progression.values();
		String [] progressionsToStrings = new String [allProgressions.length];
		for (progression e: allProgressions){
			progressionsToStrings[i] = allProgressions[i].getChord().toString();
			storeChordProgressions.addElement(progressionsToStrings[i]);
		//storeChordProgressions.addElement(arg0);
		}
		
	}
	

	public DefaultListModel <String> getChordProgressions() {
		return storeChordProgressions;
	}
	
	public enum progression {
		
		//Major scale chord progression
		I_IV_V(new String [] {"I","IV","V"}), //ONE FOUR FIVE
		   I_vi_IV_V(new String []{"I","vi","IV","V"}),	 //ONE_six_FOUR_FIVE
		   ii_V_I(new String [] {"ii","IV","V"}),	//two_FIVE_ONE
		   I_vi_ii_V(new String []{"I","vi","ii","V"}),  //ONE_six_two_FIVE
		   I_V_vi_IV(new String []{"I","VI","vi","IV"}), //ONE_FIVE_six_FOUR
		   I_IV_vi_V(new String []{"I","IV","vi","V"}), // ONE_FOUR_six_FIVE
		   I_iii_IV_V(new String []{"I","iii","IV","V"}), // ONE_three_FOUR_FIVE
		   I_IV_I_V(new String []{"I","IV","I","V"}), //ONE_FOUR_ONE_FIVE
		   I_IV_ii_V(new String []{"I","IV","ii","V"}), //ONE_FOUR_two_FIVE
		
	
		 //minor scale
		   i_VI_VII(new String [] {"i","VI","VII"}),// one_SIX_SEVEN
		  i_iv_VII(new String [] {"i","iv","VII"}),// one_four_SEVEN
		 i_iv_v(new String [] {"i","iv","v"}), // one_four_five
		 i_IV_III_VII(new String [] {"i","IV","III","VII"}), // one_FOUR_THREE_SEVEN
		 ii_v_i(new String [] {"ii","v","i"}), // two_five_one
		 i_iv_v_i(new String []{"i","iv","v","i"}), //one_four_five_one
		 VI_VII_i_i(new String []{"VI","VII","i","i"}), //SIX_SEVEN_one_one
		 
		 i_VII_VI_VII(new String []{"i","VII","VI","VII"}), //one_SEVEN_SIX_SEVEN
		 i_iv_i(new String []{"i","iv","i"});//one_four_one
		 
		 /*
		 DMAJOR(new String [] {"D", "F#", "A"}),
		 EFLATMAJ(new String []{"Eb","G","Bb"}),
		 EMAJ(new String [] {"F", "A", "C"}),
		 FMAJ(new String []{"F#", "A#", "C#"}), 
		 
		 GMAJ(new String [] {"G","B","D"}),
		 AFLATMAJ(new String []{"Ab", "C", "Eb"}),
		 AMAJ(new String []{"A", "C#", "E"}),
		 BFLATMAJ(new String []{"Bb", "D", "F"}), 
		 BMAJ(new String [] {"B", "D#", "F#"}),
		 
		 CMINOR(new String [] {"C","Eb","G"}),
		 CHASHMINOR(new String []{"C#","E","G#"}),
		 DMINOR(new String [] {"D", "F", "A"}),
		 EFLATMINOR(new String []{"Eb","Gb","Bb"}),
		 EMINOR(new String [] {"E", "G", "B"}),
		 FMINOR(new String []{"F", "Ab", "C"}), 
		 
		 FHASHMINOR(new String []{"F#", "A", "C#"}), 
		 
		 GMINOR(new String [] {"G","Bb","D"}),
		 AFLATMINOR(new String []{"Ab", "Cb", "Eb"}),
		 AMINOR(new String []{"A", "C", "E"}),
		 BFLATMINOR(new String []{"Bb", "Db", "F"}), 
		 BMINOR(new String [] {"B", "D", "F#"});
		*/
		public final String [] chord;
		progression(String []chord) {
	        this.chord = chord;
	    }
	    public String [] getChord() {
	        return chord;
	    }	
		
	}
	
}
