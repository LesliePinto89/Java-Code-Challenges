package midi;

import java.util.ArrayList;
import keyboard.Note;

public class Chord {

	private String aChordName = "";
	private ArrayList<Note> chordNotes = new ArrayList<Note>();
	private static ArrayList <String> chordList = new ArrayList<String>();	
	
	private static String uiSelectedChordName = "";
	private static String uiSelectedChord = "";
	private static String uiSelectedRoot = "";
	

	public static void storeRoot (String uiRoot){
		uiSelectedRoot = uiRoot;
	}
		
	public static String getStoredRoot (){
		return uiSelectedRoot;
	}
	
	public static void storeChordName (String uiChordName){
		uiSelectedChordName = uiChordName;
	}

	public static String getStoredChordName(){
		return uiSelectedChordName;
	}
	
	public static String getStoredChord(){
		uiSelectedChord =uiSelectedRoot +uiSelectedChordName;
		return uiSelectedChord;
	}
	
	public Chord(String chordName, ArrayList<Note> editedChordNotes) {
		this.aChordName = chordName;
		this.chordNotes = editedChordNotes;
	}

	// 3 notes chord
	// The arraylist segments the notes from the chord name for
	// easy manipulation of the notes, and to get its size of notes
	public Chord(String chordName, Note note1, Note note2, Note note3) {
		this.aChordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
	}

	// 4 notes chord - add6, add9, etc
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4) {
		this.aChordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
	}

	// 5 notes chord - dominant seventh, minor Ninth,etc
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4, Note note5) {
		this.aChordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
		this.chordNotes.add(note5);
	}

	// 6 notes chord  - minor Eleventh, dominant Eleventh, etc
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4, Note note5, Note note6) {
		this.aChordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
		this.chordNotes.add(note5);
		this.chordNotes.add(note6);
	}
	
	// 7 notes chord  - minor Thirteen, dominant Thirteen, etc
	public Chord(String chordName, Note note1, Note note2, Note note3, Note note4, Note note5, Note note6, Note note7) {
		this.aChordName = chordName;
		this.chordNotes.add(note1);
		this.chordNotes.add(note2);
		this.chordNotes.add(note3);
		this.chordNotes.add(note4);
		this.chordNotes.add(note5);
		this.chordNotes.add(note6);
		this.chordNotes.add(note7);
	}

	public void setChordName(String currentChord) {
		this.aChordName = currentChord;
	}

	public String getChordName() {
		return aChordName;
	}

	public ArrayList<Note> getChordNotes() {
		return chordNotes;
	}
	//////////////////
	public static ArrayList<String> getAllChordEnums  (){
		chordList = new ArrayList<String>();
		allChords[] allChordsArray = allChords.values();
	for (allChords aValue : allChordsArray){
		chordList.add(aValue.getChord());
	}
	return chordList;
	}
	
	//////////////////
	
	public static ArrayList<String> getMajorEnums  (){
		chordList = new ArrayList<String>();
		majorBasedChords[] majorChordsArray = majorBasedChords.values();
	for (majorBasedChords aValue : majorChordsArray){
		chordList.add(aValue.getChord());
	}
	return chordList;
	}
	//////////////////
	
	public static ArrayList<String> getMinorEnums  (){
		chordList = new ArrayList<String>();
		minorBasedChords[] minorChordsArray = minorBasedChords.values();
	for (minorBasedChords aValue : minorChordsArray){
		chordList.add(aValue.getChord());
	}
	return chordList;
	}
	
	///////////////
	
	public static ArrayList<String> getHalfDimsEnums  (){
		chordList = new ArrayList<String>();
		halfDimishedChords[] halfDimChordsArray = halfDimishedChords.values();
	for (halfDimishedChords aValue : halfDimChordsArray){
		chordList.add(aValue.getChord());
	}
	return chordList;
	}
	
	public static ArrayList<String> getFullDimsEnums  (){
		chordList = new ArrayList<String>();
		fullyDimishedChords[] fullDimChordsArray = fullyDimishedChords.values();
	for (fullyDimishedChords aValue : fullDimChordsArray){
		chordList.add(aValue.getChord());
	}
	return chordList;
	}
	

	
//	public static void chordsStrings(Enum [] values){
//		chordList = new ArrayList<String>();
//		for (Enum aValue : values){
//			chordList.add(aValue.);
//		}
//		//return chordList;
//	}
	

	public static void resetChordsLists(){
		chordList = new ArrayList<String>();
		//listScaleNames = new DefaultListModel<String>();
	}

	
	public enum allChords {
		majTetra("majTetra"), maj("maj"), sus4("sus4"), sus2("sus2"), seven("7"), 
		maj6("maj6"), nine("9"), add9("add9"),add2("add2"), maj9("maj9"),eleven("11"),
		thirteen("13"),maj13("maj13"),aug("aug"),
		
		minMajSeven("minMaj7"),minTetra("minTetra"), min("min"), min7("min7"), min9("min9"), min11("min11"), 
		min13("min13"),

//		sevenFlatFive("7b5"), 
//		sevenSharpFive("maj7#5"), 
//		sevenFlatFiveFlatNine("7b5b9"),
//		sevenSharpFiveFlatNine("7#5b9"), 
//		sevenSharpFiveSharpNine("7#5#9"),
		
		minSevenFlatFive("min7b5"),
		
		dim("dim"),
		dim7("dim7");
		
		public final String chord;
		allChords(String chord) {
			this.chord = chord;
		}
		public String getChord() {
			return chord;
		}	
	}
	
	public enum majorBasedChords {
		majTetra("majTetra"), maj("maj"),maj9("maj9"), sus4("sus4"), sus2("sus2"), seven("7"), 
		maj6("maj6"), nine("9"), add9("add9"), add2("add2"),eleven("11"),
		thirteen("13"),maj13("maj13"),aug("aug");
	
		public final String chord;
		majorBasedChords(String chord) {
			this.chord = chord;
		}
		public String getChord() {
			return chord;
		}	
		
	}
	
	public enum minorBasedChords {	
		minMajSeven("minMaj7"), minTetra("minTetra"), min("min"), min7("min7"), min9("min9"), min11("min11"), 
		min13("min13");
	
		public final String chord;
		minorBasedChords(String chord) {
			this.chord = chord;
		}
		public String getChord() {
			return chord;
		}	
	}

	public enum noneScaleChords {
		sevenFlatFive("7b5"), 
		sevenSharpFive("7#5"), 
		sevenFlatFiveFlatNine("7b5b9"),
		sevenSharpFiveFlatNine("7#5b9"), 
		sevenSharpFiveSharpNine("7#5#9");
		
		public final String chord;
		noneScaleChords(String chord) {
			this.chord = chord;
		}
		public String getChord() {
			return chord;
		}	
	}
	
	public enum halfDimishedChords {
		minSevenFlatFive("min7b5");
		
		public final String chord;
		halfDimishedChords(String chord) {
			this.chord = chord;
		}
		public String getChord() {
			return chord;
		}	
	}
	
	public enum fullyDimishedChords {

		dim("dim"),
		dim7("dim7");
		
		public final String chord;
		fullyDimishedChords(String chord) {
			this.chord = chord;
		}
		public String getChord() {
			return chord;
		}	
	}
	}

class GivenKeyChords {
	String key ="";
	ArrayList <Chord> chords;
	
	public GivenKeyChords(String key, ArrayList <Chord> aChord) {
		this.key = key;
		this.chords = aChord;
	}
	public ArrayList<Chord>  getKeyChords (){
		return chords;
	}	
	
	public String  getKeyName (){
		return key;
	}
}