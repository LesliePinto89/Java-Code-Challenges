package midi;

import javax.swing.DefaultListModel;

public class ChordProgression {

	private Chord aChord;
	private int rootNote;
	
	
	private DefaultListModel <String> storeAllChordProgressions = new DefaultListModel <String>();
	private DefaultListModel <String> storeMajorChordProgressions = new DefaultListModel <String>();
	private DefaultListModel <String> storeMinorChordProgressions = new DefaultListModel <String>();
	private static volatile ChordProgression instance = null;
	
	private ChordProgression() {
	}

	public static ChordProgression getInstance() {
		if (instance == null) {
			synchronized (ChordProgression.class) {
				if (instance == null) {
					instance = new ChordProgression();
					instance.storeAllProgressionInList();
					instance.storeMajorProgressionInList();
					instance.storeMinorProgressionInList();
				}
			}
		}

		return instance;
	}

	
	public enum majorChordProgressions {
		//Major scale chord progression
				I_IV_V(new String [] {"I","IV","V"}), //ONE FOUR FIVE
				   I_vi_IV_V(new String []{"I","vi","IV","V"}),	 //ONE_six_FOUR_FIVE
				   ii_V_I(new String [] {"ii","IV","V"}),	//two_FIVE_ONE
				   I_vi_ii_V(new String []{"I","vi","ii","V"}),  //ONE_six_two_FIVE
				   I_V_vi_IV(new String []{"I","VI","vi","IV"}), //ONE_FIVE_six_FOUR
				   I_IV_vi_V(new String []{"I","IV","vi","V"}), // ONE_FOUR_six_FIVE
				   I_iii_IV_V(new String []{"I","iii","IV","V"}), // ONE_three_FOUR_FIVE
				   I_IV_I_V(new String []{"I","IV","I","V"}), //ONE_FOUR_ONE_FIVE
				   I_IV_ii_V(new String []{"I","IV","ii","V"}); //ONE_FOUR_two_FIVE
				   public final String [] chord;
		majorChordProgressions(String []chord) {
	        this.chord = chord;
	    }
	    public String [] getChord() {
	        return chord;
	    }	
	}
	
	public void storeMajorProgressionInList(){
		//int i = 0;
		majorChordProgressions[] majorProgressions = majorChordProgressions.values();
		//String [] progressionsToStrings = new String [allProgressions.length];
		for (int i =0;i<majorProgressions.length;i++) {
			//progressionsToStrings[i] = allProgressions[i].getChord()[i].name
			storeMajorChordProgressions.addElement(majorProgressions[i].name());
		}
	}
	

	public DefaultListModel <String> getMajorChordProgressions() {
		return storeMajorChordProgressions;
	}
	
	
	public enum minorChordProgressions {
		 i_VI_VII(new String [] {"i","VI","VII"}),// one_SIX_SEVEN
		  i_iv_VII(new String [] {"i","iv","VII"}),// one_four_SEVEN
		 i_iv_v(new String [] {"i","iv","v"}), // one_four_five
		 i_IV_III_VII(new String [] {"i","IV","III","VII"}), // one_FOUR_THREE_SEVEN
		 ii_v_i(new String [] {"ii","v","i"}), // two_five_one
		 i_iv_v_i(new String []{"i","iv","v","i"}), //one_four_five_one
		 VI_VII_i_i(new String []{"VI","VII","i","i"}), //SIX_SEVEN_one_one
		 i_VII_VI_VII(new String []{"i","VII","VI","VII"}), //one_SEVEN_SIX_SEVEN
		 i_iv_i(new String []{"i","iv","i"});//one_four_one
				   public final String [] chord;
				   minorChordProgressions(String []chord) {
	        this.chord = chord;
	    }
	    public String [] getChord() {
	        return chord;
	    }	
	}
	
	public void storeMinorProgressionInList(){
		minorChordProgressions[] minorProgressions = minorChordProgressions.values();
		for (int i =0;i<minorProgressions.length;i++) {
			storeMinorChordProgressions.addElement(minorProgressions[i].name());
		}
	}
	

	public DefaultListModel <String> getMinorChordProgressions() {
		return storeMinorChordProgressions;
	}
	
	public enum allChordProgressions {
		
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
		public final String [] chord;
		allChordProgressions(String []chord) {
	        this.chord = chord;
	    }
	    public String [] getChord() {
	        return chord;
	    }	
		
	}
	
	public void storeAllProgressionInList(){
		allChordProgressions[] allProgressions = allChordProgressions.values();
		for (int i =0;i<allProgressions.length;i++) {
			storeAllChordProgressions.addElement(allProgressions[i].name());
		}
	}
	

	public DefaultListModel <String> getAllChordProgressions() {
		return storeAllChordProgressions;
	}
	
	
}
