package tools;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.awt.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import keyboard.Note;
import keyboard.VirtualKeyboard;
import midi.Chord;
import midi.ListOfChords;
import midi.ListOfScales;
import midi.MidiMessageTypes;
import midi.Scale;
import midi.Scale.ascendingSolfege;
import midi.Scale.descendingSolfege;
import midi.Scale.heptTonicDegree;
import midi.Scale.hexaTonicDegrees;
import midi.Scale.octaTonicDegree;
import midi.Scale.pentatonicDegrees;

public class ScreenPrompt  implements MouseListener {

	private static volatile ScreenPrompt instance = null;
	
	//GUI Swing components
	private JPanel basePanel =null;
	private static JLabel titleLabel;
	private JTextArea contentTextArea;
	//private JTextArea contentChordsText;
	private JScrollPane scrollPane;
	private DefaultListModel<String> conditionalModel = null;
	private JList<String> jListInput = null;
	private int jListTableWidth;
	private int jListYPos = SwingComponents.getJListYPos();
	private int jListTableHeight;
	private int jYAndHeight;
	
	//Dimensions
	private int screenWidth = SwingComponents.getInstance().getScreenWidth();
	private int screenHeight = SwingComponents.getInstance().getScreenHeight();
	private int x = screenWidth / 2 + 20;

	//State conditions
	private int featureState = 0;
	private int pageState = 0;
	private boolean chordFeature = false;
	private boolean inversionFeature = false;
	private boolean scalesFeature = false;
	private boolean relativePitchFeature = false;
	
	private boolean resetReference =false;
	private boolean randomTriggered =false;
	private boolean  randomTriggeredReference = false;
	
	//private boolean intervalInversionFeature = false;
	
	private int inversionCounter = 1;
	private boolean previousInversion =false;
	private boolean reverseOnce =false;
	private int intervalCounter = 0;
	private int previousIntervalCounter;
	
	// Music components
	private String rootNote = ""; //For scale feature
	private String scale = "";
	private Chord foundChord = null;
	private Note foundInterval = null;
	private Scale foundScale = null;
	private ListOfChords chordInstance = ListOfChords.getInstance();
	private ArrayList <Chord> inversionChords = new  ArrayList <Chord>();
	
	//Action buttons
	private JButton prevState = null;
	private JButton playChordState = null;
	private JButton homeState = null;
	private JButton nextInversionState = null;
	private JButton prevInversionState = null;
	private JButton playScaleState = null;
	private JButton playRandomIntervalState = null;
	
	private JButton playNextIntervalState = null;
	private JButton playPrevIntervalState = null;
	
	private JButton speechModeState = null;
	
	private JToggleButton colorModeState = null;
	private JToggleButton scaleRangeColorModeState = null;
	private JPanel actionHolder = new JPanel(new GridBagLayout());
	private GridBagConstraints actionBarConstraints = new GridBagConstraints();
	private MidiMessageTypes messages = MidiMessageTypes.getInstance();
	
	private String scaleOrder ="";
	
	//Instances
	private SwingComponents components = SwingComponents.getInstance();
	
	public static ScreenPrompt getInstance() {
		if (instance == null) {
			synchronized (ScreenPrompt.class) {
				if (instance == null) {
					instance = new ScreenPrompt();
					instance.setJListDimensions();

				}
			}
		}
		return instance;
	}

	private ScreenPrompt() {
	}
	 
	 public JButton addPlayButton(){
		return  playChordState = components.customJButton(0, 0, 70, 40,"Play Chord",this);
	 }
	 
	 public JButton addPreviousButton(){
		 return prevState = components.customJButton(0, 0, 70, 40,"Previous",this);
	 }
	 
	 public JButton addHomeButton(){
		 return homeState = components.customJButton(0, 0, 70, 40,"Home",this);
		}
	 
	 
	 public JButton addNextInversionButton(){
		 return nextInversionState = components.customJButton(0, 0, 70, 40,"Next Inversion",this);
	 }
		 	 
	 
	 public JButton addPrevInversionButton(){
		 return	 prevInversionState = components.customJButton(0, 0, 70, 40,"Prev Inversion",this);
	 }
	 
	 //Interval feature buttons
	 public JButton addRandomIntevalButton(){
		 return playRandomIntervalState = components.customJButton(0, 0, 70, 40,"Random Interval",this);
	 }
	 
	 //Implementing next interval for relative pitch feature
	 public JButton addNextIntervalButton(){
		 return playNextIntervalState = components.customJButton(0, 0, 70, 40,"Next Interval",this);
	 }
	 
	 public JButton addPrevIntervalButton(){
		 return playPrevIntervalState = components.customJButton(0, 0, 70, 40,"Prev Interval",this);
	 }
	 
	 
	 public JButton addScaleButton(){
		 return	 playScaleState = components.customJButton(0, 0, 70, 40,"Play Scale",this);
	 }
	 
	 
	 public JButton speechModeButton(){
		 return	speechModeState = components.customJButton(0, 0, 70, 40,"Speech Mode",this);
	 }
	 
	 public JToggleButton addColorModeButton(){
		 return	 colorModeState = components.customJToggleButton(0, 0, 70, 40,"Color Mode",this);
	 }
	 
	 public JToggleButton addScaleRangeColorModeButton(){
		 return	 scaleRangeColorModeState = components.customJToggleButton(0, 0, 70, 40,"Range Color Mode",this);
	 }
	 
	 
	public JPanel createCurrentPromptState() throws InvalidMidiDataException {
		basePanel = new JPanel();
		
		basePanel.setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 2));
		basePanel.setMinimumSize(new Dimension(screenWidth / 2, screenHeight / 2));
		
		switch (featureState){
		case 0 : 	
			welcomePrompt(); break;
		case 1 :	
		if (pageState ==1){
			basePanel.add(addPreviousButton());
			displayKeysPrompt(); 
		}
		
		else if (pageState ==2){
			basePanel.add(addPreviousButton());
			basePanel.add(addHomeButton());
			displayChordOrScalesNamesPrompt(); 
			}
	
		else if (pageState ==3){
			//EXPERIMEMTAL
			basePanel.setLayout(new GridBagLayout());
			
			if (scalesFeature ==true){
				scalesChoiceSummary();
			}
			
			else if (relativePitchFeature ==true){
				//Diverge from scale names from feature 3
				displayScaleOrderPrompt();
			}
			
			else {
			 if (inversionFeature ==true){
					components.colourMenuPanels(jListInput,Color.decode("#FFF8DC"),Color.decode("#FFFAF0"));
				}
			else if (chordFeature ==true){
				components.colourMenuPanels(jListInput,Color.decode("#F0F8FF"),Color.decode("#F0FFFF"));
			}
			findSpecificChord();
			}

			}
		else if (pageState ==4){
			
			if (relativePitchFeature ==true){
				scalesOrder();
			}
			
		}
		
		break;
		
		case 3:
			basePanel.add(addPreviousButton());
		displayKeysPrompt();
		break;
		}
		return basePanel;
	}
	
	public void setJListDimensions(){
		jListTableWidth = screenWidth/2 -20;
		jListTableHeight = screenHeight /2 - 50;
		jYAndHeight = jListYPos +jListTableHeight;
	}
	
	//Initial state with list model values added
	public void welcomePrompt () throws InvalidMidiDataException{
		conditionalModel = new DefaultListModel <String>();
		titleLabel = new JLabel ("List of actions");
        basePanel.add(titleLabel);
        basePanel.setBackground(Color.decode("#803D27"));
        conditionalModel.addElement("1) Create a chord\n");
        conditionalModel.addElement("2) Play inversion of a chord\n");
        conditionalModel.addElement("3) List all scales from a key\n");
       // conditionalModel.addElement("4) Guess the played chord\n");
        conditionalModel.addElement("4) Guess the played relative note\n");
        
        jListInput = new JList<String>(conditionalModel);
		jListInput.setName("Welcome");
        jListInput.setLayoutOrientation(JList.VERTICAL_WRAP);     
        components.colourMenuPanels(jListInput,Color.decode("#A6624C"),Color.decode("#CC907C"));
  
        
        //Adjust all cells width to be width of jList
        jListInput.setFixedCellWidth(jListTableWidth-20);
        jListInput.setFont(new Font("Arial",Font.BOLD,14));
        changePanelState(); //design panel appearance

	}
	
	//Add current state model to be created for each prompt page
	public void changePanelState () throws InvalidMidiDataException{
		scrollPane = retrieveStatePane();
		if(relativePitchFeature ==true && pageState ==3){
			GridBagConstraints adjust;
			adjust = VirtualKeyboard.getInstance().gridConstraints(0, 0, 1, 1,1);
			basePanel.add(scrollPane,adjust);
		}
		else {
		basePanel.add(scrollPane);
		}
		}
	

	//Re-used each time anew Panel is create through user interactions
	public JScrollPane retrieveStatePane() throws InvalidMidiDataException {
	
		jListInput.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListInput.setVisibleRowCount(-1);
		jListInput.setBorder(new LineBorder(Color.BLUE));
		jListInput.addMouseListener(this);
		scrollPane = new JScrollPane(jListInput);
		scrollPane.setPreferredSize(new Dimension(jListTableWidth - 10, jListTableHeight));
		scrollPane.setMinimumSize(new Dimension(jListTableWidth - 10, jListTableHeight));
		return scrollPane;
	}

		//Create key names model list
		public void displayKeysPrompt() throws InvalidMidiDataException{    
        conditionalModel = new DefaultListModel<String>();
		
		jListInput = new JList<String>(conditionalModel);
		
		//Feature set 2 - inversion of chords
		if (inversionFeature ==true){
			components.colourMenuPanels(jListInput,Color.decode("#8FBC8F"),Color.decode("#EEE8AA"));
		}
		
		//Feature set 1 - Create chords
		else if (chordFeature ==true){
			components.colourMenuPanels(jListInput,Color.decode("#F0F8FF"),Color.decode("#F0FFFF"));
			
		}
		
		//Feature set 3 - play scales based on key
		else if (scalesFeature ==true){
			components.colourMenuPanels(jListInput,Color.decode("#F08080"),Color.decode("#FFDAB9"));
		}
		
		else if (relativePitchFeature ==true){
			components.colourMenuPanels(jListInput,Color.decode("#FFE4B5"),Color.decode("#F0E68C"));
		}
		
		jListInput.setName("Key Names");
		jListInput.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		ArrayList <String> stringKeys = chordInstance.getAllKeyNotesStrings();
		for (String noteString : stringKeys){
			noteString = noteString.substring(0,noteString.length() - 1);
			conditionalModel.addElement(noteString);
		}
		
		jListInput.setFont(new Font("Arial",Font.BOLD,65));
		changePanelState();
		}
		
		//Feature
		public void displayScaleOrderPrompt() throws InvalidMidiDataException{
			createCommandButtons("stages",null);    
			conditionalModel = new DefaultListModel<String>();
			jListInput = new JList<String>(conditionalModel);
			components.colourMenuPanels(jListInput,Color.decode("#FFE4B5"),Color.decode("#F0E68C"));
			jListInput.setName("Scale Order");
			jListInput.setLayoutOrientation(JList.HORIZONTAL_WRAP);	
	
							
			jListInput.setFixedCellWidth(jListTableWidth-20);
			conditionalModel.addElement("Ascending Order");
			conditionalModel.addElement("Descending Order");
		jListInput.setFont(new Font("Arial",Font.BOLD,24));
		changePanelState();
		}
		
		
		//Create key names model list
			public void displayChordOrScalesNamesPrompt() throws InvalidMidiDataException{
				conditionalModel = new DefaultListModel<String>();			
				//Feature 3
			      if (scalesFeature ==true || relativePitchFeature ==true){
					
					conditionalModel =Scale.getScales();
					jListInput = new JList<String>(conditionalModel);
					
					if(scalesFeature ==true){
						components.colourMenuPanels(jListInput,Color.decode("#F08080"),Color.decode("#FFDAB9"));
						}
					else if (relativePitchFeature ==true){
						components.colourMenuPanels(jListInput,Color.decode("#FFE4B5"),Color.decode("#F0E68C"));
						}
					
					jListInput.setName("Scale Names");
					jListInput.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				}
	
				else {
					
			   //Feature 2
		       if (inversionFeature ==true){
		    	   
		    	   //The seconds color is not working now
					    components.colourMenuPanels(jListInput,Color.decode("#8FBC8F"),Color.decode("#EEE8AA"));
					
					
				}    //Feature 1
				else if (chordFeature ==true){
					components.colourMenuPanels(jListInput,Color.decode("#F0F8FF"),Color.decode("#F0FFFF"));
				}
				jListInput = new JList<String>(conditionalModel);
				jListInput.setName("Chord Names");
				jListInput.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				
				ArrayList <String> stringChords = Chord.getAllChordEnums();
				for (String noteString : stringChords){
					conditionalModel.addElement(noteString);
				    }
				  
				}
				//Shared settings
				jListInput.setFixedCellWidth(jListTableWidth-20);
				jListInput.setFont(new Font("Arial",Font.BOLD,24));
				changePanelState();
				}


	public boolean checkIfEndInversion() {
		// Get root of current inversion
		Chord aChord = chordInstance.getCurrentInversion();
		if (aChord ==null){
			return false;
		}
		Note invertedRoot = aChord.getChordNotes().get(0);
		// Compare to original to see if it has return to original chord in new octave
		if (foundChord.getChordNotes().get(foundChord.getChordNotes().size()-1).equals(invertedRoot)) {
			//contentChordsText.setText("Reset Inversion");
			return true;
		}
		return false;
	}


	public void findSpecificChord() throws InvalidMidiDataException {
		
		String completeChord = Chord.getStoredChord();
			
		   
		   //cmajTetra
	     if (completeChord.length() == 9 && !completeChord.contains("#") || completeChord.length() == 10 && completeChord.contains("#")) {
			scale = completeChord.contains("#") ? completeChord.substring(2, 10) : completeChord.substring(1,9);
			}
		
		
	     else if (completeChord.length() == 8 && !completeChord.contains("#") || completeChord.length() == 9 && completeChord.contains("#")) {
				scale = completeChord.contains("#") ? completeChord.substring(2, 9) : completeChord.substring(1, 8);
				}
		   
		    //cseven or c#seven
		    else if (completeChord.length() == 7 && !completeChord.contains("#") || completeChord.length() == 8 && completeChord.contains("#")) {
				scale = completeChord.contains("#") ? completeChord.substring(2, 8) : completeChord.substring(1, 7);
				}
			
			// e.g chord is Ctetra or C#min11
			else if (completeChord.length() == 6 && !completeChord.contains("#")|| completeChord.length() == 7 && completeChord.contains("#")) {
			scale = completeChord.contains("#") ? completeChord.substring(2, 7) : completeChord.substring(1, 6);
		     }
			
			// e.g chord is Cmaj6 or C#maj6
			else if (completeChord.length() == 5 && !completeChord.contains("#") || completeChord.length() == 6 && completeChord.contains("#")) {
				scale = completeChord.contains("#") ? completeChord.substring(2, 6) : completeChord.substring(1, 5);
			}

			// e.g chord is Cmaj or C#maj
			else if (completeChord.length() == 4 && !completeChord.contains("#") || completeChord.length() == 5 && completeChord.contains("#")) {
				scale = completeChord.contains("#") ? completeChord.substring(2, 5) : completeChord.substring(1, 4);
			}

			// e.g chord is C11 or C#11
			else if (completeChord.length() == 3 && !completeChord.contains("#") || completeChord.length() == 4 && completeChord.contains("#")) {
				scale = completeChord.contains("#") ? completeChord.substring(2, 4) : completeChord.substring(1, 3);
			}

			// e.g chord is C9 or C#9
			else if (completeChord.length() == 2 && !completeChord.contains("#") || completeChord.length() == 3 && completeChord.contains("#")) {
				scale = completeChord.contains("#") ? completeChord.substring(2, 3) : completeChord.substring(1, 2);
			}
			String key = completeChord.contains("#") ? completeChord.substring(0, 2) : completeChord.substring(0, 1);
			key = key.toUpperCase();
			
			//Current chord to play in button
			foundChord = chordInstance.getChordFromKeyScale(key, scale);
			
			if (foundChord == null) {
				//Debugging
				System.out.println("Chord not found");
			} else {

				//Used when feature 2 has been triggered
				if (inversionFeature == true) {
					
					
					//Get action bar
					createCommandButtons("feature 2",Color.decode("#556B2F"));
				
					
					
					if(checkIfEndInversion()){
						
						//Keep button pressed down only when color mode is on and user
						//reached last inversion when its final next or previous inversion.
						 if (components.getColorToggleStatus()){
								colorModeState.setSelected(true);}
						 
						inversionCounter =1;
						Chord getFirstInversion = chordInstance.getFirstInversion();
						chordInstance.storeCurrentInversion(getFirstInversion);
						foundChord = chordInstance.getCurrentInversion();	
						
						
						actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(4, 1, 0, 1,1);
						actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
						actionHolder.add(addNextInversionButton(),actionBarConstraints);
						
						//basePanel.add(addNextInversionButton());
					}
				
					else{
						//When previous pressed, this triggers resets until previous
						//pressed again. Other code prevents reverse inversion
						if (previousInversion == true){
							
							//Keep button pressed down only when color mode is on. Previous inversion creates new panel layout.
							//Function still works without it, but missing aesthetic is confusing
							 if (components.getColorToggleStatus()){
									colorModeState.setSelected(true);}
							 
							previousInversion =false; 
							foundChord = chordInstance.getCurrentInversion();
						}
						
						else {
							
							chordInstance.firstAndSecondInversion(foundChord);
					foundChord = chordInstance.getCurrentInversion();}
						
					//Store first inversion when reach the end inversion to reset it
					if (inversionCounter ==1){
					chordInstance.storeFirstInversion(foundChord);
					}
					actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(4, 1, 0, 1,1);
					actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
					actionHolder.add(addNextInversionButton(),actionBarConstraints);
					//basePanel.add(addNextInversionButton());
					if (inversionCounter>1){
						
						//Keep button pressed down only when color mode is on. Next inversion creates new panel layout.
						//Function still works without it, but missing aesthetic is confusing
						  if (components.getColorToggleStatus()){
						colorModeState.setSelected(true);
						}
						  
						  actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(5, 1, 0, 1,1);
							actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
							actionHolder.add(addPrevInversionButton(),actionBarConstraints);
						 // basePanel.add(addPrevInversionButton());				
					}
					
					
					}
					JPanel chordPanel = baseSummary("Chord Inversions",Color.decode("#8FBC8F"),Color.decode("#556B2F"));
	                chordSummary(chordPanel);
				}
				else {
				
				createCommandButtons("feature 1",Color.decode("#87CEFA"));
				JPanel chordPanel = baseSummary("Chord Notes",Color.decode("#E0FFFF"),Color.decode("#87CEFA"));
                chordSummary(chordPanel);
			}
			}
	}
		
	public void chromaticIntervalsRelativePitch(){
	
		
	}
	
   public void solfegeIntervalsRelativePitch(){
		
	}
	

	
	public void chordSummary(JPanel innerChordsContent ){
			String removedOctave = foundChord.getChordNotes().get(0).getName();
			removedOctave = removedOctave.substring(0,removedOctave.length()-1);
			
			
			contentTextArea.append("Chord Root: "+removedOctave+"\n");
			contentTextArea.append("Chord Name: "+foundChord.getChordNotes().get(0).getName()+"\n\n");
			contentTextArea.append("Chord Notes: ");
			for (Note aNote : foundChord.getChordNotes()) {
				String noteNoOctave = aNote.getName();
				noteNoOctave = noteNoOctave.substring(0,noteNoOctave.length()-1);
				contentTextArea.append(noteNoOctave + " | ");
				System.out.println(noteNoOctave);
			}
			contentTextArea.append("\n");
			if (inversionFeature==true){
				 contentTextArea.append("Number of inversions: "+inversionCounter);
				}
			//innerChordsContent.add(contentTextArea,instance);
	}
	
	
public JPanel baseSummary (String areaName, Color innerColor, Color areaColor){
	GridBagConstraints instance = VirtualKeyboard.getInstance().gridConstraints(0, 1, 1, 1,1);
	instance.anchor = GridBagConstraints.PAGE_START;
	JPanel innerContent = new JPanel(new GridBagLayout());
	instance.fill = GridBagConstraints.BOTH;
	innerContent.setPreferredSize(new Dimension(screenWidth / 3, screenHeight / 3));
	innerContent.setMinimumSize(new Dimension(screenWidth / 3, screenHeight / 3));
	innerContent.setBackground(innerColor);
	basePanel.add(innerContent, instance);
	
	contentTextArea = new JTextArea(15,40);
	contentTextArea.setName(areaName);
	contentTextArea.setBackground(areaColor);
		
     instance = VirtualKeyboard.getInstance().gridConstraints(0, 1, 0, 1,1);
	instance.anchor = GridBagConstraints.FIRST_LINE_START;
	instance.insets = new Insets (10,10,0,0);
	innerContent.add(contentTextArea,instance);
	return innerContent;
	}
	
	public void scaleSummary (JPanel  innerContent){
		String removedOctave = foundScale.getTonic().getName();
		removedOctave = removedOctave.substring(0,removedOctave.length()-1);
		contentTextArea.append("Scale Key: "+removedOctave+"\n");
		contentTextArea.append("Scale name: "+foundScale.getScaleName()+"\n\n");
		contentTextArea.append("Scale Notes: ");
		for (Note aNote : foundScale.getScaleNotesList()) {
			String noteInDegreeMinusOctave = aNote.getName();
			noteInDegreeMinusOctave = noteInDegreeMinusOctave.substring(0,noteInDegreeMinusOctave.length()-1);
			contentTextArea.append(noteInDegreeMinusOctave + " | ");
		}
		contentTextArea.append("\n");		
	}
	
	public void scalesOrder() throws InvalidMidiDataException {
		JPanel carriedContent;
		if (relativePitchFeature ==true && pageState ==4){
			components.colourMenuPanels(jListInput,Color.decode("#FFE4B5"),Color.decode("#F0E68C"));
			if (scaleOrder.contains("Ascending")){
			createCommandButtons("Ascending feature 4",Color.decode("#F0E68C"));
			}
			else if (scaleOrder.contains("Descending")){
				createCommandButtons("Descending feature 4",Color.decode("#F0E68C"));
				}
			 carriedContent = baseSummary("Ordered Notes",Color.decode("#FFE4B5"),Color.decode("#F0E68C"));
			 relativePitch(carriedContent);
			// pageState =4;
		}
	}
	
	public void scalesChoiceSummary() throws InvalidMidiDataException {
		JPanel carriedContent;
		if (scalesFeature ==true){
		components.colourMenuPanels(jListInput,Color.decode("#F08080"),Color.decode("#FFDAB9"));
		createCommandButtons("feature 3",Color.decode("#A52A2A"));
		 carriedContent = baseSummary("Scales Notes",Color.decode("#A52A2A"),Color.decode("#FFB6C1"));
		scaleSummary(carriedContent);
		generateDegreesText();
		}
	}
	
	//Gathers random value from collection of current scale's notes' pitch value
	public static <T> T random(Collection<T> coll) {
	    int num = (int) (Math.random() * coll.size());
	    for(T t: coll) if (--num < 0) return t;
	    throw new AssertionError();
	}
	public void generateRandomPitch(){
		Collection <Integer> currentScaleIntervels = ListOfScales.getInstance().getScalePitchValues();
	Integer guestInterval = random(currentScaleIntervels);
		
		//Note foundNote = null;
		for (Entry<String, Note> entry : Note.getNotesMap().entrySet()) {
			if (entry.getValue().getPitch() == guestInterval){
				foundInterval =  Note.getNotesMap().get(entry.getKey());
				break;
			}
		}
	}
	
	public void playNextScaleInterval(int index) throws InvalidMidiDataException{
		Note aNote = foundScale.getScaleNotesList().get(index);
		PlaybackFunctions.playIntervalNote(aNote);
	}
	
	
	public void relativePitch(JPanel carriedContent) throws InvalidMidiDataException{
	
		String removedOctave = foundScale.getTonic().getName();
		//String removedOctave = Chord.getStoredRoot();
		removedOctave = removedOctave.substring(0,removedOctave.length()-1);
		contentTextArea.append("Scale Tonic: "+removedOctave+"\n");
		contentTextArea.append("Scale name: "+foundScale.getScaleName()+"\n\n");
		
		String removeOctave = foundScale.getScaleNotesList().get(0).getName();
		 contentTextArea.append("Reference pitch: "+removeOctave);
		
		ArrayList <Integer> givenScaleNotePitches = new ArrayList<Integer>();
		for (Note aNote : foundScale.getScaleNotesList()){
			givenScaleNotePitches.add(aNote.getPitch());

		}
		//previousIntervalCounter = givenScaleNotePitches.size()-1;
		PlaybackFunctions.setIndexCounter(givenScaleNotePitches.size()-1);
		
		
		
		Collection <Integer> givenScaleNotes = givenScaleNotePitches;
		
		//Store for later individual traversal to aid interval recognition
		ListOfScales.getInstance().currentScalePitchValues(givenScaleNotes);
		
		//Save initial random interval
		generateRandomPitch();
		
}

	
	
	public void createCommandButtons(String state, Color buttonsBacking){		
		actionHolder = new JPanel(new GridBagLayout());
		actionHolder.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			actionHolder.setBackground(buttonsBacking);
			actionHolder.setPreferredSize(new Dimension ((int)basePanel.getPreferredSize().getWidth()-50, screenHeight / 10));
			actionHolder.setMinimumSize(new Dimension ((int)basePanel.getMinimumSize().getWidth()-50, screenHeight / 10));
	
			actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(0, 1, 0, 1,1);
			actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;
			actionHolder.add(addPreviousButton(),actionBarConstraints);
	
			actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(1, 1, 0, 1,1);
			actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;
			actionHolder.add(addHomeButton(),actionBarConstraints);
		
			if(pageState ==3 || relativePitchFeature ==true && pageState ==4) {
			actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(2, 1, 0, 1,1);
			actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
			actionHolder.add(addColorModeButton(),actionBarConstraints);
			}
			
			if (state.equals("feature 1") || state.equals("feature 2") ){
				actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(3, 1, 0, 1,1);
				actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
				actionHolder.add(addPlayButton(),actionBarConstraints);
			}
			
			if (state.equals("feature 3")){
				actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(3, 1, 0, 1,1);
				actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
				actionHolder.add(addScaleButton(),actionBarConstraints);
			}
			
			
			//I might need to adjust grid x
			if (state.contains("feature 4")){
				actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(3, 1, 0, 1,1);
				actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
				actionHolder.add(addRandomIntevalButton(),actionBarConstraints);
				
				if (state.contains("Ascending")){actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(4, 1, 0, 1,1);
				actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
				actionHolder.add(addNextIntervalButton(),actionBarConstraints);
				}
				else if (state.contains("Descending")){
					actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(4, 1, 0, 1,1);
					actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
					actionHolder.add(addPrevIntervalButton(),actionBarConstraints);
				}

				actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(5, 1, 0, 1,1);
				actionBarConstraints.fill = GridBagConstraints.HORIZONTAL;		
				actionHolder.add(addScaleRangeColorModeButton(),actionBarConstraints);
			}
			
			actionBarConstraints = VirtualKeyboard.getInstance().gridConstraints(0, 1, 0, 1,1);
			actionBarConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
			actionBarConstraints.gridx = 0;
			actionBarConstraints.weightx =1;
			basePanel.add(actionHolder,actionBarConstraints);
		
	}
	
	public void generateDegreesText(){
		pentatonicDegrees[] penDegrees = pentatonicDegrees.values();
		hexaTonicDegrees[] hexDegrees = hexaTonicDegrees.values();
		heptTonicDegree[] heptDegrees = heptTonicDegree.values();
		octaTonicDegree[] octDegrees = octaTonicDegree.values();
		ascendingSolfege[] ascSolfege = ascendingSolfege.values();
		descendingSolfege[] descSolfege = descendingSolfege.values();
		
		int i =0;
		for (Note aNote : foundScale.getScaleNotesList()) {
			String noteInDegreeMinusOctave = aNote.getName();
			noteInDegreeMinusOctave = noteInDegreeMinusOctave.substring(0,noteInDegreeMinusOctave.length()-1);
			switch (foundScale.getScaleNotesList().size()){
			
			case 6 :	contentTextArea.append("\nDegree: "+penDegrees[i] + " | " +noteInDegreeMinusOctave);
			break;
			case 7 :	contentTextArea.append("\nDegree: "+hexDegrees[i] + " | " +noteInDegreeMinusOctave); 
			break;
			case 8 :	contentTextArea.append("\nDegree: "+heptDegrees[i] + " | " +noteInDegreeMinusOctave); 
			break;
			case 9 :	contentTextArea.append("\nDegree: "+octDegrees[i] + " | " +noteInDegreeMinusOctave); 
			break;
			case 12 :	
				         if (foundScale.getScaleName().contains("Ascending")){	
				        contentTextArea.append("\nSolfege: "+ascSolfege[i] + " | " +noteInDegreeMinusOctave);
				         }
			               else if (foundScale.getScaleName().contains("Descending")){
			             	contentTextArea.append("\nSolfege: "+descSolfege[i] + " | " +noteInDegreeMinusOctave);
			             	break;}	
			             
			}
			i++;
			
			}	
			
		}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public JPanel editPanel (){
		return basePanel;
	}
	
	public void recordInversions (Chord saveInversion){
		if(!inversionChords.contains(saveInversion)){
		inversionChords.add(saveInversion);
		}
	}
	
	public Chord getLastInversionFromList (int index){
		return inversionChords.get(index);
	}
	
	public void accessModelFeatures(MouseEvent optionPressed) {
		
		int index = jListInput.locationToIndex(optionPressed.getPoint());
			try {
			switch (index) {
			case 0: featureState =1;   //If chords option (index 0), trigger feature 1
			pageState =1;
			chordFeature =true;
			VirtualKeyboard.getInstance().updateScreenPrompt();	
				break;
			case 1: 
			//Change to feature 1 to re use it
			featureState =1;
			pageState =1;
			inversionFeature=true;
			VirtualKeyboard.getInstance().updateScreenPrompt();	
					
				break;
			case 2: featureState =1;    //ALSO USES Start of feature 1
			scalesFeature =true;
			pageState =1;
			VirtualKeyboard.getInstance().updateScreenPrompt();	
			break;
			
			
			//Play relative pitch
			case 3: featureState =1;    //ALSO USES Start of feature 1 
			relativePitchFeature =true;
			pageState =1; 
			SwingComponents.getInstance().displayScalesOnlyState(true);
			VirtualKeyboard.getInstance().updateScreenPrompt();	
			break;
			
			
			}
		}catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public void handleHomeStateButtonActions() throws InvalidMidiDataException{
		 //If the user returns to the home screen while the color mode is still enabled
		 
		//ADDED  relativePitchFeature addition
		if ( relativePitchFeature==true  &&components.getColorToggleStatus() ||
				 inversionFeature==true  &&components.getColorToggleStatus()
				 || scalesFeature==true &&components.getColorToggleStatus() ||
				 chordFeature==true &&components.getColorToggleStatus()) {
			 
				PlaybackFunctions.resetChordsColor();
				//Still do below conditions for other conditions below
				inversionFeature =false; 
				scalesFeature =false;
				chordFeature =false;
				relativePitchFeature =false;
				components.changeColorToggle(false); //Turn off color mode
				reverseOnce =false;
				//colorModeState.setEnabled(false); //This greys it out, not removes pressed down look
				
		 }
		 
		 else { //Carry on as if the color mode has been disabled in each feature
		  if (inversionFeature ==true){
			inversionFeature =false; //turn off inversion feature on home return
			PlaybackFunctions.resetChordsColor();
		}
		//ADDED  relativePitchFeature addition - might cause problems
		else if (scalesFeature ==true || relativePitchFeature ==true){
			scalesFeature =false;
			relativePitchFeature =false;
			//Rest scales store when going straight home from 3rd and 2nd page
			Scale.resetScalesLists();
			PlaybackFunctions.resetChordsColor();
		}
		else if (chordFeature ==true){
			chordFeature =false;
			PlaybackFunctions.resetChordsColor();
		}
		 }
		pageState =0;
		featureState =0;
		VirtualKeyboard.getInstance().updateScreenPrompt();		
	}
	
	
	@Override
	public void mousePressed(MouseEvent optionPressed) {
		Object obj = optionPressed.getSource();	
		
		try {		
		if (obj.equals(jListInput)) {
			//First page
			if (jListInput.getName().equals("Welcome")) {
			accessModelFeatures(optionPressed);
			}
			
			//Second page of feature 1
			else if (jListInput.getName().equals("Key Names")) {
				pageState =2;
				int index = jListInput.locationToIndex(optionPressed.getPoint());
				
				//Current key from list model in feature state 1's first page
				String note = conditionalModel.getElementAt(index);
				
				//Only load scales from key on the first page. If the user later clicks previous from the 2nd page,
				//or the home button in page 2 or 3, the list will be deleted and remade.
				if (scalesFeature ==true || relativePitchFeature ==true){
					ListOfScales.getInstance().generateScalesNames(note);
				}
				
				//Used with feature set 1 an 2
				else {
				Chord.storeRoot(note); //concatenate with soon to store chord name to create complete chord		
				}	
				VirtualKeyboard.getInstance().updateScreenPrompt();	
				}
			
			else if (jListInput.getName().equals("Chord Names")) {
				pageState =3;
				int index = jListInput.locationToIndex(optionPressed.getPoint());
				
				//Current chord name  from list model in feature state 1's second page
				String chord = conditionalModel.getElementAt(index);
				
				Chord.storeChordName(chord); //Store to later concatenate with previous stored root note	
				VirtualKeyboard.getInstance().updateScreenPrompt();	
				}	
			
			else if (jListInput.getName().equals("Scale Names")) {
				pageState =3;
				int index = jListInput.locationToIndex(optionPressed.getPoint());
				
				//Might not need these two lines of code
				//String scale = conditionalModel.getElementAt(index);
				//scale = scale.contains("#") ? scale.substring(2,scale.length()) :scale.substring(1,scale.length());
				
				foundScale = Scale.getScaleFromList(index);
				ListOfScales.getInstance().displayedScaleNotes(foundScale);
				
				//Chord.storeChordName(chord); //concatenate with next function call to create chord	
				VirtualKeyboard.getInstance().updateScreenPrompt();	
				}	
			
			
				////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////
			
			else if (jListInput.getName().equals("Scale Order")) {
				pageState =4;
				int index = jListInput.locationToIndex(optionPressed.getPoint());
				
				//If descending order is chosen
				if (index==1 && reverseOnce ==false){	
				Collections.reverse(foundScale.getScaleNotesList());
				reverseOnce =true;
				}
				
				else if (index==0 && reverseOnce ==true){	
					Collections.reverse(foundScale.getScaleNotesList());
					reverseOnce =false;
					}
				
				scaleOrder = conditionalModel.getElementAt(index);
				VirtualKeyboard.getInstance().updateScreenPrompt();	
				}	
			
			
			}		
			
		////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
		
		else  if (obj.equals(prevState)) {
			--pageState;
			if (pageState==0){
				featureState =0;
				inversionFeature =false; //turn off inversion feature when clicked previous until home return
				scalesFeature = false;
				chordFeature =false;
				relativePitchFeature=false;
			}
			
			//Used with feature set 
			else if (pageState==1){    //ADDING SECOND BOOLEAN MIGHT CAUSE PROBLEM
				if (scalesFeature ==true || relativePitchFeature==true){
					//feature 3
				    //For each new key selected, prevents over filling list and model
				    Scale.resetScalesLists();
				}
			}
			
			else if (pageState==2){
				if (inversionFeature ==true){
                  
					//Reset stored inversion for each new root/chord name combination
                    	 inversionChords = new ArrayList<Chord>();
               
					inversionCounter =1; //Might not need this
					chordInstance.resetInversion(); //Take current inversion to original first inversion
				}
				
				else if (pageState==3){
					if (relativePitchFeature ==true){
						reverseOnce =false;
					}	
				}
				
				//DONT ADD RESET SCALES AT THIS POINT BECAUSE THE LOADED SCALES FROM THE KEY CAN BE
				//USED TO ACCESS THE OTHER SCALES. The scales are loaded on page 1, not page 2, which is this stage
				
				//If color mode is on while pressing previous
				if (inversionFeature == true && components.getColorToggleStatus() || 
						chordFeature ==true && components.getColorToggleStatus() || 
								scalesFeature ==true && components.getColorToggleStatus()|| 
										relativePitchFeature ==true && components.getColorToggleStatus()){
					/***TEST FOR COLOR*/
					components.changeColorToggle(false);
					Chord.resetChordsLists(); //Stops list overfilling
					  PlaybackFunctions.resetChordsColor();
				}
//				//A color mode is off is not needed as turning it off does reset
//				else if (inversionFeature == true  || chordFeature ==true ){
//					/***TEST FOR COLOR*/
//					Chord.resetChordsLists();
//				}
				
				//Whether chordFeature or inversion feature is true
				String temp = Chord.getStoredChord();
				temp = temp.replace(Chord.getStoredChordName(), "");
				Chord.storeRoot(temp);
			}
			VirtualKeyboard.getInstance().updateScreenPrompt();	
			}
			
		  else  if (obj.equals(homeState)) {
			  handleHomeStateButtonActions();	
		}
		
		  else  if (obj.equals(playChordState)) {
			  //Feature 2 playback
			if (inversionFeature ==true){
				Chord playCurrentInversion = chordInstance.getCurrentInversion();
				PlaybackFunctions.playAnyChordLength(playCurrentInversion);
			}
			else {
				//Feature 1 playback
			PlaybackFunctions.playAnyChordLength(foundChord);}
		}
		
		  else  if (obj.equals(playScaleState)) { 
			  //Feature 3 playback
				PlaybackFunctions.displayOrPlayScale(foundScale);

		} 
		
		
		////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
		  else  if (obj.equals(playRandomIntervalState)) { 
			  generateRandomPitch();
			  
			  if(messages.getRandomState()==false && PlaybackFunctions.getMelodicIndexCounter()==0){
				  messages.storeRandomState(true);
				  }
			  
			  else if(messages.getRandomState()==false && PlaybackFunctions.getMelodicIndexCounter()>0){
				  PlaybackFunctions.resetLastNoteColor();
				  PlaybackFunctions.setRandomIntervalCounter(PlaybackFunctions.getMelodicIndexCounter());
				  messages.storeRandomState(true);
				  }
			  
			  if(messages.getMelodyInterval()){
				  randomTriggered =true;
				  messages.storeMelodyInterval(false);
				  }
			  
			  if (randomTriggeredReference ==true && !foundInterval.getName().equals(foundScale.getTonic().getName())){
				  int lastIndex =  foundScale.getTonic().getName().length() >2 ? 66: 64;
					  contentTextArea.replaceRange("",45,lastIndex);
					  randomTriggeredReference =false;
			  }
			  
			  /**THIS ADDES THE TEXT BUT MIGHT CAUSE PROBLEMS*/
			  if (foundInterval.getName().equals(foundScale.getTonic().getName())){
				  String removeOctave = foundScale.getScaleNotesList().get(0).getName();
					contentTextArea.append("Reference pitch: "+removeOctave);
					randomTriggeredReference =true;
			  }
			  
			  else if (foundInterval.getName().equals(foundScale.getTonic().getName()) &&
					  intervalCounter ==0){
				  int lastIndex =  foundScale.getTonic().getName().length() >2 ? 66: 64;
				  contentTextArea.replaceRange("",45,lastIndex);
				  randomTriggeredReference =false;
			  }
			  
				PlaybackFunctions.playIntervalNote(foundInterval);
		   }
		////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
		
		  else  if (obj.equals(playNextIntervalState)) {  
			 		  
			  if(messages.getRandomState()){
				  PlaybackFunctions.resetLastNoteColor();
				  PlaybackFunctions.setMelodicIndexCounter(PlaybackFunctions.currentRandomIntervalCounter());
				  intervalCounter =0;
				  
				  /***HERE IS THE FIX
				  */
				  randomTriggered =true;
			       /////////////////////
	
				  messages.storeRandomState(false);
				  messages.storeMelodyInterval(true);
				  }

			  
		         //Partially Solve color timings
				  if(intervalCounter == foundScale.getScaleNotesList().size() && randomTriggered ==true){
					  intervalCounter = 0;
					  resetReference =true;
				  }
			      /////////////////////////////////
			  
				  else if(intervalCounter == foundScale.getScaleNotesList().size()-1&& randomTriggered ==false){
				  PlaybackFunctions.resetLastNoteColor();
				  PlaybackFunctions.setMelodicIndexCounter(0);

				  intervalCounter = PlaybackFunctions.getMelodicIndexCounter();
				  resetReference =true;
			  }
			  
			  

			  
			  
			  //Might involve random triggered
			  if(randomTriggered ==false){
				  intervalCounter = PlaybackFunctions.getMelodicIndexCounter();
			  }
			  
			  //Handle referrence note text
			  if(intervalCounter ==0 || intervalCounter ==1){ 
				  int lastIndex =  foundScale.getTonic().getName().length() >2 ? 66: 64;
					 
//				  if(intervalCounter ==0 && randomTriggered ==true){
//					  contentTextArea.replaceRange("",45,lastIndex);
//					  
//				  }
				     if(intervalCounter ==0 && resetReference ==true){
						 // resetReference =false;
						  String removeOctave = foundScale.getScaleNotesList().get(0).getName();
							contentTextArea.append("Reference pitch: "+removeOctave);
						  
					  }
				      
				      else if(intervalCounter ==1 && resetReference ==true && randomTriggered ==true){
				    	  contentTextArea.replaceRange("",45,lastIndex);
				      }
				      
					  else if (intervalCounter >=0 && intervalCounter <foundScale.getScaleNotesList().size() 
							  && randomTriggered ==true){
						  
						 //For now, a placehold for the above conditions as returning from random to next
						  // leaves no reference show when index becomes 0, if using start to end version.
						  //Is not needed if want to carry next from random
						 // randomTriggered =false;
					  }
					  
					  
					  
					  else if(intervalCounter ==0 && resetReference ==false){
						
						  contentTextArea.replaceRange("",45,lastIndex);

					  }
					  //Done on full cycle and is the note after the key
					  else  if(intervalCounter ==1 && resetReference ==true){
						  resetReference =false;
						  contentTextArea.replaceRange("",45,lastIndex);
					  }
				  
			  }
			  
			  
		
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  //Used to differentiate progressive interval and random singular note
			  MidiMessageTypes.getInstance().storeIntervalStateID(2);
			  
//			   if(PlaybackFunctions.getIntervalCounter() >=1){
//					PlaybackFunctions.resetLastNoteColor();
//					PlaybackFunctions.emptyNotes();
//					
//					PlaybackFunctions.setIntervalCounter(0);
//					messages.storeMelodyInterval(false);
//				}
			  
			  if(messages.getMelodyInterval()==false){
				  messages.storeMelodyInterval(true);
				  }
				
			  
			  playNextScaleInterval(intervalCounter);
			 
			  if(randomTriggered){
			  intervalCounter++;
			  }
			  
		   }
		
        else  if (obj.equals(playPrevIntervalState)) { 
        	
 
        	
        	
			  int scaleSize = foundScale.getScaleNotesList().size()-1;
			  previousIntervalCounter = PlaybackFunctions.getIndexCounter()-1;
			  
//			  if (PlaybackFunctions.fixDirection() == 1){
//				  previousIntervalCounter = intervalCounter-2;
//				  //intervalCounter = previousIntervalCounter;
//				  PlaybackFunctions.currentDirection(0);
//			  }
			  
			  if ( PlaybackFunctions.fixDirection() == 1){
				  previousIntervalCounter = previousIntervalCounter--;
				  PlaybackFunctions.currentDirection(2); 
			  }
			  
//			  else {
//				  PlaybackFunctions.currentDirection(2); 
//			  }
			  
			  //messages.storeMelodyInterval(false);
			  
		
			  
			  //Handle referrence note text
			  if(previousIntervalCounter ==0 || previousIntervalCounter ==scaleSize){
				  
				  if (previousIntervalCounter ==0){
					  String removeOctave = foundScale.getScaleNotesList().get(0).getName();
						 contentTextArea.append("Reference pitch: "+removeOctave);
						 PlaybackFunctions.setIndexCounter(scaleSize+1);
					 
				  }
				  
			      else if(previousIntervalCounter ==scaleSize) {
				  int lastIndex =  foundScale.getTonic().getName().length() >1 ? 134 : 135; 
					  contentTextArea.replaceRange("",115,lastIndex);
				  }
				  
			      else if(previousIntervalCounter ==scaleSize-1) {
					  int lastIndex =  foundScale.getTonic().getName().length() >1 ? 134 : 135; 
						  contentTextArea.replaceRange("",115,lastIndex);
					  }	 
			  }

			  
			  //Used to differentiate progressive interval and random singular note
			  MidiMessageTypes.getInstance().storeIntervalStateID(1);
			  
		
			  
			  if(messages.getMelodyInterval()==false){
				  messages.storeMelodyInterval(true);
				  }
				
			  
			 
			  playNextScaleInterval(previousIntervalCounter);
			  
			  //previousIntervalCounter--;
		   }
		
		
		
		  else if (obj.equals(nextInversionState)) {
			//Stored current inversion as last each next inversion
			
			  
			  
			  if (components.getColorToggleStatus()){
				  PlaybackFunctions.resetChordsColor();
				 //colorModeState.setSelected(false); 
			  }
			  
			  
			++inversionCounter;
			recordInversions(foundChord);
			VirtualKeyboard.getInstance().updateScreenPrompt();			
		}
		
		//Previous Inversion button action
		///////////////////////////////////////////////////////////////////////////
		else if (obj.equals(prevInversionState)) {
			
			if (components.getColorToggleStatus()){
				  PlaybackFunctions.resetChordsColor();
			 }
			
			//Get the last stored inversion
			previousInversion =true;
			--inversionCounter;
			Chord lastInversion = getLastInversionFromList(inversionCounter-1);
			chordInstance.storeCurrentInversion(lastInversion);
			VirtualKeyboard.getInstance().updateScreenPrompt();			
		}
		
		//Color button actions
		///////////////////////////////////////////////////////////////////////////
		else if (obj.equals(colorModeState)) {
			
			
			//Turn on color mode
			if (!components.getColorToggleStatus()){		
				components.changeColorToggle(true);	
				//if(relativePitchFeature){
					//PlaybackFunctions.displayOrPlayScale(foundScale);
					//}
			}
			//Turn off color mode
			else if (components.getColorToggleStatus()){
			
				components.changeColorToggle(false);
			
			//Might need to remove
			if(relativePitchFeature ==true){	
				
				if(PlaybackFunctions.currentRandomIntervalCounter()>=1){
					PlaybackFunctions.resetLastNoteColor();
					intervalCounter =0;

				}		
				else if(PlaybackFunctions.getMelodicIndexCounter() >=1){
					intervalCounter =0;
					PlaybackFunctions.resetLastNoteColor();
				}
				
				
			}
			
			//Chord base features - So far
			else {
			PlaybackFunctions.resetChordsColor(); //Re color original keys colour
			}
			}	
		}
		
		else if (obj.equals(scaleRangeColorModeState)) {
			
			
			//Turn on color mode
			if (!components.getRangeColorToggleStatus()){	
				
				if (components.getColorToggleStatus()){
					PlaybackFunctions.resetLastNoteColor();
					components.changeColorToggle(false);
					colorModeState.setSelected(false);
					}
				
				components.changeRangeColorToggle(true);	
			//	if(relativePitchFeature){
				Scale displayOnly = ListOfScales.getInstance().getDisplayedScaleNotes();
					PlaybackFunctions.displayOrPlayScale(displayOnly);
				//	}
			}
			else if (components.getRangeColorToggleStatus()){	//Turn off color mode
				
				
			components.changeRangeColorToggle(false);
			PlaybackFunctions.resetScaleDisplayColor(); //Re color original keys colour
			}	
		}
	} catch (InvalidMidiDataException e) {	
			e.printStackTrace();
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}