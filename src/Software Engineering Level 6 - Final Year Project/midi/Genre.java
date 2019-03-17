package midi;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;

import keyboard.Note;
import tools.PlaybackFunctions;
import tools.SwingComponents;

public class Genre {

	private DefaultListModel<String> genreModel = new DefaultListModel<String>();
	private ArrayList<Chord> chordProgression = new ArrayList<Chord>();
	private ArrayList<Chord> breakProgression = new ArrayList<Chord>();
	private ArrayList<Chord> diffChord = new ArrayList<Chord>();
	
	private ArrayList<Note> licks = new ArrayList<Note>();
	private ChordProgression prog = ChordProgression.getInstance();
	private static volatile boolean playback = false;

	
	private static volatile Genre instance = null;

	private Genre() {
	}

	public static Genre getInstance() {
		if (instance == null) {
			synchronized (Genre.class) {
				if (instance == null) {
					instance = new Genre();
				}
			}
		}

		return instance;
	}
	
	public void createSong (String key,String genre){
		prog.storeAllProgressionInList();
		try {
		switch(genre){
		
		case "Blues" : bluesOneFourFive(key);
			          twelveBarProgression();
		               break;   
		               
		case "Classical" : classicalProgression(key);
		                   playClassical();
                      break;   
		    }
		} catch (InvalidMidiDataException | InterruptedException e) {
			e.printStackTrace();
		}
	
	} 
	
	public void classicalProgression(String key){
		key = key+3;
		Note oneChordRoot = Note.getNotesMap().get(key);
		Chord rootChord = ListOfChords.getInstance().findChord(oneChordRoot.getName(), "maj");
		chordProgression.add(rootChord);
		
		Note secondNote = ListOfScales.getInstance().getKey(oneChordRoot, 6);
		Chord secondChord = ListOfChords.getInstance().findChord(secondNote.getName(), "maj");
		chordProgression.add(secondChord);
		
		Note thirdNote = ListOfScales.getInstance().getKey(secondNote, 2);
		Chord  thirdChord = ListOfChords.getInstance().findChord(thirdNote.getName(), "min");
		chordProgression.add(thirdChord);
		
		Note fourthNote = ListOfScales.getInstance().getKey(thirdNote, 7);
		Chord  fourthChord = ListOfChords.getInstance().findChord(fourthNote.getName(), "min");
		chordProgression.add(fourthChord);
		
		Note fifthNote = ListOfScales.getInstance().getKey(fourthNote, 1);
		Chord  fifthChord = ListOfChords.getInstance().findChord(fifthNote.getName(), "maj");
		chordProgression.add(fifthChord);
		
		chordProgression.add(rootChord);
		chordProgression.add(fifthChord);
	    chordProgression.add(secondChord);
		chordProgression.add(rootChord);
	
		

//		
//		fifthNote = fourthNote;
//		Note sixthNote = ListOfScales.getInstance().getKey(fifthNote, 2);
//		Chord sixthChord = ListOfChords.getInstance().findChord(sixthNote.getName(), "maj");
//		chordProgression.add(sixthChord);
//		
//		
//		Note seventhNote = ListOfScales.getInstance().getKey(sixthNote, 2);
//		Chord  seventhChord = ListOfChords.getInstance().findChord(seventhNote.getName(), "maj");
//		chordProgression.add(seventhChord);
//		
//		Note lastNote = ListOfScales.getInstance().getKey(seventhNote, 1);
//		Chord  lastChord = ListOfChords.getInstance().findChord(lastNote.getName(), "maj");
//		chordProgression.add(lastChord);
	}


	public void  playClassical(){
		for (Chord aChord : chordProgression){
			try {
				PlaybackFunctions.playAnyChordLength(aChord);
				PlaybackFunctions.timeDelay(1000);
			} catch (InvalidMidiDataException  | InterruptedException e) {
				e.printStackTrace();
			} 
		}
		chordProgression.clear();
	}
	
	public void bluesOneFourFive (String key){
		key = key +4;
		Note oneChordRoot = Note.getNotesMap().get(key);
		Note fourthChordRoot = ListOfScales.getInstance().getKey(oneChordRoot, 5);
		Note fifthChordRoot = ListOfScales.getInstance().getKey(fourthChordRoot, 2);
		Note [] popularRoots = {oneChordRoot,fourthChordRoot,fifthChordRoot};
		
		
		for(Note aNote : popularRoots){
			Scale bluesScale = ListOfScales.getInstance().bluesScale(aNote);
			ListOfChords.getInstance().setBluesChords(bluesScale);
			ArrayList<Chord> tempChords = ListOfChords.getInstance().getBluesChords();
			chordProgression.addAll(tempChords);
		}
		
		//Used in diffent list
		Note fillerNoteRoot = ListOfScales.getInstance().getKey(oneChordRoot, -7);
		Note fillerNoteNext = ListOfScales.getInstance().getKey(oneChordRoot, -6);
		Note fillerNoteEnd = ListOfScales.getInstance().getKey(oneChordRoot, -5);
		Note [] fillerNotes = {fillerNoteRoot,fillerNoteNext,fillerNoteEnd};
		for(Note aNote : fillerNotes){
		Scale bluesScale = ListOfScales.getInstance().bluesScale(aNote);
		ListOfChords.getInstance().setFillerBluesChord(bluesScale);
		ArrayList<Chord> tempFillerChords = ListOfChords.getInstance().getFillerBluesChord();
		 breakProgression.addAll(tempFillerChords);
		}
		
		//lick(oneChordRoot);
	}
	
	
	public void lick (Note root){
		
		Note lickOne = ListOfScales.getInstance().getKey(root, 6);
		licks.add(lickOne);
		
		Note lickSecond = ListOfScales.getInstance().getKey(lickOne, 1);
		licks.add(lickSecond);
		
		Note lickThirdFlat = ListOfScales.getInstance().getKey(lickSecond, 3);
		licks.add(lickThirdFlat);
		
		Note lickFourthNat = ListOfScales.getInstance().getKey(lickOne, 5);
		licks.add(lickFourthNat);
		
		Note lickFifth = ListOfScales.getInstance().getKey(lickFourthNat, 4);
		licks.add(lickFifth);
		
		licks.add(root);
		
		Note lickNewOne = ListOfScales.getInstance().getKey(root, 3);
		licks.add(lickNewOne);
		
		Note lickNewSecond = ListOfScales.getInstance().getKey(lickNewOne, 1);
		licks.add(lickNewSecond);
		
		Note lickNewThird = ListOfScales.getInstance().getKey(lickNewSecond, 3);
		licks.add(lickNewThird);
		
		Note lickNewFourth = ListOfScales.getInstance().getKey(lickNewSecond, 1);
		licks.add(lickNewFourth);
		
		Note lickNewFifth = ListOfScales.getInstance().getKey(lickNewThird, 2);
		licks.add(lickNewFifth);
		
	
	}
	
	public void playLick() throws InvalidMidiDataException{
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//int i = 0;
						// End thread if user leaves progressions page
						if (!playback) {
							PlaybackFunctions.timeDelay(300);
							PlaybackFunctions.playIntervalNote(licks.get(0));
							PlaybackFunctions.timeDelay(0);
							
							PlaybackFunctions.playIntervalNote(licks.get(1));
							PlaybackFunctions.playIntervalNote(licks.get(2));
							PlaybackFunctions.timeDelay(200);
							
							PlaybackFunctions.playIntervalNote(licks.get(3));
							PlaybackFunctions.playIntervalNote(licks.get(4));
							PlaybackFunctions.timeDelay(300);
							
							PlaybackFunctions.playIntervalNote(licks.get(5));
							PlaybackFunctions.timeDelay(400);
							
							//PlaybackFunctions.timeDelay(322);
							PlaybackFunctions.playIntervalNote(licks.get(6));
							PlaybackFunctions.timeDelay(100);
							
							PlaybackFunctions.playIntervalNote(licks.get(7));
							PlaybackFunctions.playIntervalNote(licks.get(8));
							PlaybackFunctions.timeDelay(400);
							
							PlaybackFunctions.playIntervalNote(licks.get(9));
							PlaybackFunctions.playIntervalNote(licks.get(10));
							PlaybackFunctions.timeDelay(200);
							} 
						else {
							playback = false;
						}	
					} catch ( InvalidMidiDataException e) {
			
					e.printStackTrace();
				}

			}

		}).start();
		
	}
	
	public void playBar(int pos,boolean endBar,int limit, int index) throws InvalidMidiDataException, InterruptedException{
		if(!endBar){
	     for (int i =1; i<=limit;i++){
			PlaybackFunctions.playAnyChordLength(chordProgression.get(index));
			PlaybackFunctions.timeDelay(400);
			
			
				PlaybackFunctions.playAnyChordLength(chordProgression.get(index));
				PlaybackFunctions.timeDelay(200);
		
				
				Note adjustedNote = ListOfScales.getInstance().getKey(chordProgression.get(index).getChordNotes().get(1), 2);
				ArrayList<Note> tempNotes = new ArrayList<Note>();
				tempNotes.add(chordProgression.get(index).getChordNotes().get(0));
				tempNotes.add(adjustedNote);
				Chord editedChord = new Chord("temp",tempNotes);
				PlaybackFunctions.playAnyChordLength(editedChord);
				
				PlaybackFunctions.timeDelay(400);
				
				PlaybackFunctions.playAnyChordLength(chordProgression.get(index));
				PlaybackFunctions.timeDelay(200);
			
		}
	}
		else if(endBar){
			PlaybackFunctions.timeDelay(300);
			PlaybackFunctions.playAnyChordLength(breakProgression.get(0));
			PlaybackFunctions.timeDelay(400);
			PlaybackFunctions.playAnyChordLength(breakProgression.get(1));
			PlaybackFunctions.timeDelay(400);
			PlaybackFunctions.playAnyChordLength(breakProgression.get(2));
			PlaybackFunctions.timeDelay(500);
		}
	}
	public void twelveBarProgression() throws InvalidMidiDataException, InterruptedException{
		
		playBar(1,false,8,0);//4 bars
		playBar(5, false,4,2);//2 bars
		playBar(1,false,4,0);//2 bars
		playBar(5,false,2,2);//1 bar
		playBar(4,false,2,1);//1 bar
		playBar(1,false,2,0);//1 bar
		playBar(0,true,0,0);//1 bar
	}
	
public ArrayList<Chord> getBluesChords(){
		return chordProgression;
	}
	
	
//	public void TwelveBarBluesChords(Note key){
//		//Common blues progression is 12 bar progression using I-IV-V
//		ArrayList<Note> chordNotes = new ArrayList<Note>();
//		
//        //Start with root
//		chordNotes.add(key);
//		Note perfectFifth = ListOfScales.getInstance().getKey(key, 7);
//		chordNotes.add(perfectFifth);
//		Chord rootChord = new Chord("root Chord",chordNotes);
//		chordProgression.add(rootChord);
//		chordNotes.clear();
//		
//		//5 semitones form key is I -IV: e.g. if G was key, IV is C
//		Note fourthAsNewRoot = ListOfScales.getInstance().getKey(key, 5);
//		chordNotes.add(fourthAsNewRoot);
//		perfectFifth = ListOfScales.getInstance().getKey(fourthAsNewRoot, 7);
//		chordNotes.add(perfectFifth);
//		Chord fourthChord = new Chord("fourth Chord",chordNotes);
//		chordProgression.add(fourthChord);
//		chordNotes.clear();
//
//		//7 semitones form key is I -V: e.g. if G was key, V is D
//				Note fifthAsNewRoot = ListOfScales.getInstance().getKey(key, 6);
//				chordNotes.add(fifthAsNewRoot);
//				perfectFifth = ListOfScales.getInstance().getKey(fifthAsNewRoot, 7);
//				chordNotes.add(perfectFifth);
//				Chord fifthChord = new Chord("fifth Chord",chordNotes);
//				chordProgression.add(fifthChord);
//				chordNotes.clear();
//	}
	
	
	public enum genre {
		Blues("Blues"), Jazz("Jazz"), Pop("Pop"), Folk("Folk"), Classical("Classical");

		public final String gen;

		genre(String gen) {
			this.gen = gen;
		}

		public String getGen() {
			return gen;
		}
	}

	public void storeGenreNames() {
		genre[] genreArray = genre.values();
		for (genre aGenre : genreArray) {
			genreModel.addElement(aGenre.getGen());
		}
	}

	public DefaultListModel<String> getGenreNames() {
		return genreModel;
	}

}
