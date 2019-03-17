package midi;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;

public class ChordProgression {

	private Chord aChord;
	private int rootNote;
	
	
	//Display and user interaction
	private DefaultListModel <String> storeAllChordProgressions = new DefaultListModel <String>();
	private DefaultListModel <String> storeMajorChordProgressions = new DefaultListModel <String>();
	private DefaultListModel <String> storeMinorChordProgressions = new DefaultListModel <String>();
	
	//Used to get actual chords to be used
	private ArrayList<String> minorAsString = new ArrayList<String>();
	private ArrayList<String> majorAsString = new ArrayList<String>();
	private ArrayList<String> allAsString = new ArrayList<String>();
	
	private ArrayList <String> numbers = new ArrayList <String>();
	private chordSymbol[] symbols = chordSymbol.values();
	
	private static volatile ChordProgression instance = null;
	
	private ChordProgression() {
	}

	public static ChordProgression getInstance() {
		if (instance == null) {
			synchronized (ChordProgression.class) {
				if (instance == null) {
					instance = new ChordProgression();
					instance.storeMajorProgressionInList();
					instance.storeMinorProgressionInList();
					instance.symbolsToNumbers();
				}
			}
		}

		return instance;
	}
	
	
	public enum chordSymbol{
		i(1),ii(2),iii(3),iv(4),v(5),vi(6),vii(7),
		I(1),II(2),III(3),IV(4),V(5),VI(6),VII(7);
		
		public final int prog;
		 chordSymbol(int prog) {
		        this.prog = prog;
		    }
		    public int getProg() {
		        return prog;
		    }		    
	}

	public void symbolsToNumbers(){
		 numbers = new ArrayList <String>();
		for (chordSymbol sym : symbols)
		{   numbers.add(sym.toString());
		}
	}
	
	public int getScaleDegree(String selectedProgChord){
		for (chordSymbol sym : symbols){
			if(selectedProgChord.equals(sym.toString())){
				return sym.getProg();
			}
	}
		return 0;
	}
	
	public String minorOrMajor (String selectedProgChord){
		 char[] charArray = selectedProgChord.toCharArray();
				 if(Character.isLowerCase( charArray[0] )){
					 return "min";
				 }			
				else if(Character.isUpperCase( charArray[0] )){
					return "maj";
				}
			
		
		return null;
	}
	
	public enum majorChordProgressions {
				I_IV_V("I IV V"), //ONE FOUR FIVE
				   I_vi_IV_V("I vi IV V"),	 //ONE_six_FOUR_FIVE
				   ii_V_I("ii IV V"),	//two_FIVE_ONE
				   I_vi_ii_V("I vi ii V"),  //ONE_six_two_FIVE
				   I_V_vi_IV("I V vi IV"), //ONE_FIVE_six_FOUR
				   I_IV_vi_V("I IV vi V"), // ONE_FOUR_six_FIVE
				   I_iii_IV_V("I iii IV V"), // ONE_three_FOUR_FIVE
				   I_IV_I_V("I IV I V"), //ONE_FOUR_ONE_FIVE
				   I_IV_ii_V("I IV ii V"); //ONE_FOUR_two_FIVE
				   public final String chord;
		majorChordProgressions(String chord) {
	        this.chord = chord;
	    }
	    public String getChord() {
	        return chord;
	    }	
	}
	
	public void storeMajorProgressionInList(){
	
		majorChordProgressions[] majorProgressions = majorChordProgressions.values();
		for (majorChordProgressions prog : majorProgressions)
		{   
			String progresion = prog.chord;
			majorAsString.add(progresion);
			storeMajorChordProgressions.addElement(progresion);
		}
	}
	
	public DefaultListModel <String> getMajorChordProgressions() {
		return storeMajorChordProgressions;
	}
	
	public enum minorChordProgressions {
		 i_VI_VII("i VI VII"),// one_SIX_SEVEN
		  i_iv_VII("i iv VII"),// one_four_SEVEN
		 i_iv_v("i iv v"), // one_four_five
		 i_IV_III_VII("i IV III VII"), // one_FOUR_THREE_SEVEN
		 ii_v_i("ii v i"), // two_five_one
		 i_iv_v_i("i iv v i"), //one_four_five_one
		 VI_VII_i("VI VII i"), //SIX_SEVEN_one
		 i_VII_VI_VII("i VII VI VII"), //one_SEVEN_SIX_SEVEN
		 i_iv_i("i iv i");//one_four_one
				   public final String  chord;
				   minorChordProgressions(String chord) {
	        this.chord = chord;
	    }
	    public String getChord() {
	        return chord;
	    }	
	}
	
	public void storeMinorProgressionInList(){
		minorChordProgressions[] minorProgressions = minorChordProgressions.values();
		for (minorChordProgressions prog : minorProgressions)
		{   
			String progresion = prog.chord;
			minorAsString.add(progresion);
			storeMinorChordProgressions.addElement(progresion);
		}
	}
	
	public DefaultListModel <String> getMinorChordProgressions() {
		return storeMinorChordProgressions;
	}
	
	
	public void storeAllProgressionInList(){
		storeMajorProgressionInList();
		storeMinorProgressionInList();
		allAsString.addAll(majorAsString);
		allAsString.addAll(minorAsString);
	}
		
 public DefaultListModel <String> getAllChordProgressions() {
	return storeAllChordProgressions;
}
}
