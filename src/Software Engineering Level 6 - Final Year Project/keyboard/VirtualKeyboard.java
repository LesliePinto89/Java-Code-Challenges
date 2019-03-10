package keyboard;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import keyboard.FeatureTabs;
import tools.ScreenPrompt;
import tools.SwingComponents;


/**
 * This class displays the piano software to the user, and acts as a base to the
 * class that constructs MIDI track files.
 * 
 */
public class VirtualKeyboard {

	private JSlider slider;
	private ArrayList<JButton> naturalKeys = new ArrayList<JButton>();
		private static volatile VirtualKeyboard instance = null;

	    private VirtualKeyboard() {}

	    public static VirtualKeyboard getInstance() {
	        if (instance == null) {
	            synchronized(VirtualKeyboard.class) {
	                if (instance == null) {
	                    instance = new VirtualKeyboard();
	                }
	            }
	        }
	        return instance;
	    }
	
	private int PITCH_OCTAVE = 2;
	private int screenWidth = (int) SwingComponents.getInstance().getScreenWidth();
	private int screenHeight  = (int) SwingComponents.getInstance().getScreenHeight();
	
	private int START_WHOTE_NOTE_POSITION = screenWidth /80;
	private int START_SHARP_NOTE_POSITION = screenWidth /35;

	//This panel will later be updated, utilised with the validate and repaint functions
	private JPanel originalScreenPrompt =null;
	private JPanel contentPane = new JPanel();
	

	
	//needs to be a layered pane to keep sizing 
	private JLayeredPane allPianoKeysLayeredPanel = new JLayeredPane();
	private JPanel panelHoldPianoKeys = new JPanel();
	private JPanel pianoBackingPanel = new JPanel();
	private JFrame secondFrame;
	private JPanel controlPanel = new JPanel();
	private JPanel keysHolder = new JPanel();
	private JPanel buttonHolder = new JPanel (new GridBagLayout());
	
	private JPanel innerButtonHolder = new JPanel (new GridBagLayout());
	
	private GridBagConstraints aConstraint;
	
	//TEST
	private JPanel pane = new JPanel(new GridBagLayout());
	private GridBagConstraints c = new GridBagConstraints();

	////////////////////////
	public void storeButtons(JButton noteButton) {
		naturalKeys.add(noteButton);
	}
	
	public ArrayList<JButton> getButtons() {
		return naturalKeys;
	}
	
	/**
	 * Style whole and accidental keys colour
	 * 
	 * @param button
	 *            - JButton piano key to style
	 */
	public void styleKeys(JButton button) {
		if (button.getText().contains("#")) {
			button.setForeground(Color.WHITE);
			button.setBackground(Color.BLACK);
			allPianoKeysLayeredPanel.add(button, new Integer(1));
		} else {
			button.setBackground(Color.WHITE);
			allPianoKeysLayeredPanel.add(button, new Integer(0));
		}
	}
	
	/**
	 * Creates the piano's accidental (black) keys.
	 * 
	 */
	public void createSharpKeys() throws InvalidMidiDataException, MidiUnavailableException {
		PITCH_OCTAVE = 1;
		EnumSet<Note.SharpNoteType> sharpKeysEnums = EnumSet.allOf(Note.SharpNoteType.class);
		while (PITCH_OCTAVE < 6) {
			for (Note.SharpNoteType getNote : sharpKeysEnums) {
				if (getNote.getSharp().equals("C#")) {
					PITCH_OCTAVE++;
					
				}
				JButton button = new JButton(getNote.getSharp() + Integer.toString(PITCH_OCTAVE));
				if (button.getText().startsWith("C") && PITCH_OCTAVE >= 3) {
					START_SHARP_NOTE_POSITION += 30;
				}

				else if (button.getText().startsWith("F")) {
					START_SHARP_NOTE_POSITION += 32;
				}
				//button.setBounds(START_SHARP_NOTE_POSITION, 145, 20, 126);
				button.setBounds(START_SHARP_NOTE_POSITION, 145, screenWidth /55, 126);
			
				button.setFont(new Font("Arial", Font.PLAIN, 6));
				button.setVerticalAlignment(SwingConstants.BOTTOM);
				button.setMargin(new Insets(1, 1, 1, 1));

				styleKeys(button);
				storeButtons(button);
				
				
				//START_SHARP_NOTE_POSITION += 37;
				START_SHARP_NOTE_POSITION +=screenWidth /35.5;
			}
		}
	}

	/**
	 * Creates the piano's whole (white) keys.
	 * 
	 */
	public void createWholeKeys() throws InvalidMidiDataException, MidiUnavailableException {
		
		EnumSet<Note.NoteType> naturalKeysEnums = EnumSet.allOf(Note.NoteType.class);
		boolean endOctave = false;
		while (endOctave == false) {
			for (Note.NoteType getNote : naturalKeysEnums) {
				JButton button = new JButton(getNote.toString() + Integer.toString(PITCH_OCTAVE));
				button.setBounds(START_WHOTE_NOTE_POSITION, 145, screenWidth /37, 196);
				button.setFont(new Font("Arial", Font.PLAIN, 10));
				button.setVerticalAlignment(SwingConstants.BOTTOM);
				button.setMargin(new Insets(1, 1, 1, 1));

				// Fixed octave number matches 61 notes keyboard
				if (getNote.toString().equals("C") && PITCH_OCTAVE == 7) {
					styleKeys(button);
					storeButtons(button);
					endOctave = true;
					break;
				}
				styleKeys(button);
				storeButtons(button);

				START_WHOTE_NOTE_POSITION +=  screenWidth /37;
				if (getNote.toString().equals("B")) {
					PITCH_OCTAVE++;
				}
			}
		}
	}

	
	/**
	 * Adjust all notes volume based on slider value.
	 */
	public void volumeToggle() {
		slider = new JSlider(0, 100, 71);
		slider.setPreferredSize(new Dimension(100, 50));
		slider.setMinimumSize(new Dimension(100, 50));	
		slider.setBackground(Color.decode("#404040"));
		slider.setBorder(new LineBorder(Color.decode("#303030")));
		KeyboardInteractions volumeSliderListener = new KeyboardInteractions(slider);
		slider.addChangeListener(volumeSliderListener);
		
		aConstraint = gridConstraints(0,1,0,1,1);
		aConstraint.anchor = GridBagConstraints.LINE_START;
		innerButtonHolder.add(slider,aConstraint); 
	}
	
	public void debugModeButton() throws InvalidMidiDataException, IOException {
		
		//BufferedImage debug = ImageIO.read(new File("src/Images/DebugImage.png"));
		//ImageIcon debugIcon = new ImageIcon(debug);
		//BufferedImage recOn = ImageIO.read(new File("src/Images/recordOn.png"));
		//ImageIcon recOnIcon = new ImageIcon(recOn);
	    JToggleButton debugMIDI = new JToggleButton();
	    debugMIDI.setBackground(Color.decode("#404040"));
	    debugMIDI.setBorder(new LineBorder(Color.decode("#303030")));
	    debugMIDI.setFocusPainted(false);
	    debugMIDI.setPreferredSize(new Dimension (70, 42));
	    debugMIDI.setMinimumSize(new Dimension (70, 42));
	    
	    int width = debugMIDI.getPreferredSize().width;
	    int height = debugMIDI.getPreferredSize().height;
	    BufferedImage playOff = ImageIO.read(new File("src/Images/DebugImage.png"));
		Image scaledOff = playOff.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon playOffIcon = new ImageIcon(scaledOff);
		debugMIDI.setIcon(playOffIcon);
		debugMIDI.setContentAreaFilled(false);
			
	    
	    debugMIDI.setName("debugButton");

		
		ActionListener debugButtonActionListener = new KeyboardInteractions(debugMIDI);
		debugMIDI.addActionListener(debugButtonActionListener);
		aConstraint = gridConstraints(1,1,0,1,1);
		aConstraint.anchor = GridBagConstraints.LINE_START;
		innerButtonHolder.add(debugMIDI,aConstraint);
	}
	
	public void recordButton() throws InvalidMidiDataException, IOException {
		BufferedImage recOff = ImageIO.read(new File("src/Images/recordOff.png"));
		ImageIcon recOffIcon = new ImageIcon(recOff);
		BufferedImage recOn = ImageIO.read(new File("src/Images/recordOn.png"));
		ImageIcon recOnIcon = new ImageIcon(recOn);
	    
		JToggleButton recordMIDI = new JToggleButton(recOffIcon);
		recordMIDI.setSelectedIcon(recOnIcon);
		recordMIDI.setBackground(Color.decode("#404040"));
		recordMIDI.setBorder(new LineBorder(Color.decode("#303030")));
		recordMIDI.setFocusPainted(false);
		
		recordMIDI.setPreferredSize(new Dimension (70, 42));
		recordMIDI.setMinimumSize(new Dimension (70, 42));
		recordMIDI.setName("recordButton");
		recordMIDI.setForeground(Color.WHITE);
		recordMIDI.setText("Off");
		recordMIDI.setContentAreaFilled(false);
		
		ActionListener recordButtonActionListener = new KeyboardInteractions(recordMIDI);
		recordMIDI.addActionListener(recordButtonActionListener);
		aConstraint = gridConstraints(2,1,0,1,1);
		aConstraint.anchor = GridBagConstraints.LINE_START;
		innerButtonHolder.add(recordMIDI,aConstraint);
	}
	
	public void playButton() throws IOException {
		JToggleButton playMIDI = new JToggleButton();
		//playMIDI.setBounds(secondFrame.getWidth() / 3 + 130, secondFrame.getHeight() / 2 - 30, 70, 62);
		playMIDI.setPreferredSize(new Dimension (70, 42));
		playMIDI.setMinimumSize(new Dimension (70, 42));
		BufferedImage playOff = ImageIO.read(new File("src/Images/play button off.png"));
		
		int width = playMIDI.getPreferredSize().width;
	    int height = playMIDI.getPreferredSize().height;
		
		Image scaledOff = playOff.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon playOffIcon = new ImageIcon(scaledOff);
		playMIDI.setIcon(playOffIcon);
		
		BufferedImage playOn = ImageIO.read(new File("src/Images/play button.png"));
		Image scaledOn = playOn.getScaledInstance(width,height, Image.SCALE_SMOOTH);
		ImageIcon playOnIcon = new ImageIcon(scaledOn);
		playMIDI.setSelectedIcon(playOnIcon);
		
		playMIDI.setBackground(Color.decode("#404040"));
		playMIDI.setBorder(new LineBorder(Color.decode("#303030")));
		playMIDI.setFocusPainted(false);
		
		playMIDI.setName("playButton");	
		ActionListener playButtonActionListener = new KeyboardInteractions(playMIDI);
		playMIDI.addActionListener(playButtonActionListener);
		
		aConstraint = gridConstraints(3,1,0,1,1);
		aConstraint.anchor = GridBagConstraints.LINE_START;
		innerButtonHolder.add(playMIDI,aConstraint);
	}

	
	public void saveMIDIButton() throws IOException{
		JToggleButton saveMIDI = new JToggleButton();
		//saveMIDI.setBounds(secondFrame.getWidth() / 3 + 260, secondFrame.getHeight() / 2 - 30, 70, 62);
		saveMIDI.setPreferredSize(new Dimension (70, 42));
		saveMIDI.setMinimumSize(new Dimension (70, 42));
		

		int width = saveMIDI.getPreferredSize().width;
	    int height = saveMIDI.getPreferredSize().height;
		
		BufferedImage saveOff = ImageIO.read(new File("src/Images/midi document.png"));
		Image scaledOff = saveOff.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon saveOffIcon = new ImageIcon(scaledOff);
		saveMIDI.setIcon(saveOffIcon);
		BufferedImage saveOn = ImageIO.read(new File("src/Images/midi document - clicked.png"));
		Image scaledOn = saveOn.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon saveOnIcon = new ImageIcon(scaledOn);

		saveMIDI.setBackground(Color.decode("#404040"));
		saveMIDI.setBorder(new LineBorder(Color.decode("#303030")));
		saveMIDI.setSelectedIcon(saveOnIcon);
		saveMIDI.setName("saveButton");
		saveMIDI.setContentAreaFilled(false);
		
		ActionListener saveButtonActionListener = new KeyboardInteractions(saveMIDI);
		saveMIDI.addActionListener(saveButtonActionListener);
		
		aConstraint = gridConstraints(4,1,0,1,1);
		aConstraint.anchor = GridBagConstraints.LINE_START;	
		innerButtonHolder.add(saveMIDI,aConstraint);
	}
	
	public void freePlayOrMakeTrack() throws InvalidMidiDataException, MidiUnavailableException {
		ArrayList<JButton> retrievedList = getButtons();
		for (int i = 0; i < retrievedList.size(); i++) {
			JButton pressedNote = retrievedList.get(i);
			String noteName = pressedNote.getText();
			String noteOctave = noteName.substring(noteName.length()-1, noteName.length());
			int octaveInNumber = Integer.parseInt(noteOctave);
			int getValue = Note.convertToPitch(noteName);
	
			//Store as notes in a map
			if(noteName.contains("#")){
				Note aSharpNote = new Note(noteName,getValue,octaveInNumber,100,"Sharp");	
				aSharpNote.storeNotes(noteName, aSharpNote);
			}
			else{
				Note aNaturalNote = new Note(noteName,getValue,octaveInNumber,100,"Natural");	
				aNaturalNote.storeNotes(noteName, aNaturalNote);
			}
			
			MouseListener mouseListener = new KeyboardInteractions(pressedNote,getValue);
			pressedNote.addMouseListener(mouseListener);	
		}
	}
	
	public GridBagConstraints gridConstraints (int gridX,int gridWidth,int gridyY,double weightX,
			double weightY){
		GridBagConstraints a = new GridBagConstraints();
		//a.anchor = anchorValue;
		a.gridx = gridX;
		a.gridwidth = gridWidth;
		a.gridy = gridyY;
		a.weightx = weightX;
		a.weighty = weightY;
		return a;
	}
	
	/**
	 * Draws the main GUI layout, used in each Application GUI feature.
	 * @throws IOException 
	 * 
	 */
	public void drawKeyboardGUI(boolean mode) throws InvalidMidiDataException, MidiUnavailableException, IOException {

		contentPane.setLayout(new GridBagLayout());
		if (mode ==false){
			contentPane.setBackground(Color.decode("#4169E1"));
		secondFrame = new JFrame("6100COMP: Computer Music Education Application - Free Play Mode");}
		
		else if (mode ==true){
			contentPane.setBackground(Color.decode("#340D00"));
			secondFrame = new JFrame("6100COMP: Computer Music Education Application - Learn Mode");
		}
		secondFrame.setPreferredSize(new Dimension (screenWidth, screenHeight));
		secondFrame.setMinimumSize(new Dimension (screenWidth, screenHeight));
		secondFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		secondFrame.setContentPane(contentPane);
		secondFrame.setBackground(Color.decode("#4682b4"));	
		secondFrame.pack();
		
		//Make fullscreen
		secondFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//secondFrame.setUndecorated(true);
		
		secondFrame.setVisible(true);
		
		//addFeatureTabs();
    	//learnMode();
		     
		// aConstraint = gridConstraints(GridBagConstraints.PAGE_END,0,2,1,1,1);
		
		
		
		
////		//Black Panel
//		//pianoBackingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//		pianoBackingPanel.setPreferredSize(new Dimension (secondFrame.getWidth() -50, 322));	
//		pianoBackingPanel.setLayout(new GridBagLayout()); //Keeps elements in order, and centred layer of keys
//		pianoBackingPanel.setBackground(Color.black);
//		contentPane.add(pianoBackingPanel,aConstraint);
//	
////		GridBagConstraints b = new GridBagConstraints();
////		b.weighty = 1;  
////		b.weightx = 1;  
////         b.gridx = 0;
////         b.gridy = 0;  //First row
////         b.anchor = GridBagConstraints.PAGE_START;
//         
//         
//		 aConstraint = gridConstraints(GridBagConstraints.PAGE_START,0,1,0,1,1);
//        
//
//		
// 		controlPanel.setPreferredSize(new Dimension(screenWidth -90, 122));
//
//////		controlPanel.setLayout(new GridLayout (6,1,0,0));
//       controlPanel.setBackground(Color.RED);
//         pianoBackingPanel.add(controlPanel,aConstraint);
////		
		
		//Dimensions for panel that holds piano key
		//allPianoKeysLayeredPanel.setPreferredSize(new Dimension(screenWidth, screenHeight/2));
 		//pianoBackingPanel.add(allPianoKeysLayeredPanel,2);

 		//Using parts of this code
 		//layeredContentPane.add(originalScreenPrompt,new Integer(0), 0);
 		
 		//ONLY WORKS THIS WAY FOR NOW
		//allPianoKeysLayeredPanel.setBounds(10, screenHeight/2, screenWidth, screenHeight/2);	
 		//layeredContentPane.add(allPianoKeysLayeredPanel);
 		
 		
 		
 		

	
	
	
	}
	
	/**
	 * Main keyboard layout, with method calls to its various components.
	 * 
	 * @throws IOException
	 * @wbp.parser.entryPoint
	 * 
	 */
	public void createVirtualKeyboard(boolean mode) throws InvalidMidiDataException, MidiUnavailableException, IOException {
		drawKeyboardGUI(mode);
//		volumeToggle();
//		playButton();
//		recordButton();
//		saveMIDIButton();
		if (mode ==true){
			addFeatureTabs();
			learnMode();
		}
		drawPiano();
		debugModeButton();
		volumeToggle();
		recordButton();
		playButton();
		saveMIDIButton();
//		
//        if (mode ==true){
//        	addFeatureTabs();
//        	learnMode();
//		}
	}
	
	public void learnMode() throws InvalidMidiDataException{
		 addScreenPrompt();
		
	}
	
	public JPanel createSpeaker() throws IOException{
		
		JPanel speakerOne = new JPanel(new GridBagLayout());
		speakerOne.setBackground(Color.black);
		
		//speakerOne.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,Color.GRAY));
		speakerOne.setPreferredSize(new Dimension (300,100));
		speakerOne.setMinimumSize(new Dimension (300,100));
		
		//Chosen button holder color
		speakerOne.setBackground(Color.decode("#303030"));
		
		//Removing layout makes the inenr oane;s color show through
		JPanel holdSpeaker = new JPanel(new GridBagLayout());
		holdSpeaker.setPreferredSize(new Dimension (290,90));
		holdSpeaker.setMinimumSize(new Dimension (290,90));
		//holdSpeaker.setBackground(Color.BLUE);
		
		GridBagConstraints speakerConstraints = new GridBagConstraints();
		speakerConstraints.fill = GridBagConstraints.BOTH;
		speakerOne.add(holdSpeaker);
		
		BufferedImage image = ImageIO.read(new File("src/Images/Speaker Grill 4.jpg"));
		
		Image dimg = image.getScaledInstance(screenWidth/4, screenHeight/2,
			       Image.SCALE_SMOOTH);
		JLabel picLabel = new JLabel(new ImageIcon(dimg));
		holdSpeaker.add(picLabel);
		
		return speakerOne;
	}
	
	public void drawPiano() throws IOException, InvalidMidiDataException{     

		//Making gridY = 0 fixes distortion when in full screen and not mode
		 aConstraint = gridConstraints(0,2,0,1,1);
		 aConstraint.anchor = GridBagConstraints.PAGE_END;
			//Black Panel
			pianoBackingPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			pianoBackingPanel.setPreferredSize(new Dimension (secondFrame.getWidth() -50, 322));	
			pianoBackingPanel.setMinimumSize(new Dimension (secondFrame.getWidth() -50, 322));	
			pianoBackingPanel.setLayout(new GridBagLayout()); //Keeps elements in order, and centred layer of keys
			pianoBackingPanel.setBackground(Color.decode("#0D0707"));
			contentPane.add(pianoBackingPanel,aConstraint);
	
			//Panel that holds all piano buttons
			aConstraint = gridConstraints(0,1,0,1,1);
			aConstraint.anchor =GridBagConstraints.PAGE_START;
			
			controlPanel.setPreferredSize(new Dimension(screenWidth -90, 122));
			controlPanel.setMinimumSize(new Dimension(screenWidth -90, 122));
          controlPanel.setLayout(new GridBagLayout());
	        controlPanel.setBackground(Color.BLACK);
	        pianoBackingPanel.add(controlPanel,aConstraint);
	        
	        //Original with black space above
	        //aConstraint = gridConstraints(GridBagConstraints.CENTER,0,5,0,1,1);
	        
	        GridBagConstraints a = new GridBagConstraints();
	a.anchor = GridBagConstraints.CENTER;
			a.gridx = 0;
			a.gridwidth = 5;
			a.gridheight = 0; //To carry size
			a.fill = GridBagConstraints.VERTICAL;
			a.gridy = 0;
			a.weightx = 1;
			a.weighty = 1;
	        
	        
			//This corrects space between control and keyholder pane
	        //aConstraint = gridConstraints(GridBagConstraints.CENTER,0,5,0,1,1);
			keysHolder.setPreferredSize(new Dimension(screenWidth -90, 122));
			keysHolder.setMinimumSize(new Dimension(screenWidth -90, 122));
			
			//BorderLayout makes keys appear, gridbag layout ruins it in this panel
			keysHolder.setLayout(new BorderLayout());
			keysHolder.setBackground(Color.decode("#8C1400"));
			pianoBackingPanel.add(keysHolder,a);
			
					
			//allPianoKeysLayeredPanel.setPreferredSize(new Dimension(screenWidth, screenHeight/2));
			keysHolder.add(allPianoKeysLayeredPanel,BorderLayout.CENTER);
	
//             //TURN BACK ON LATER
			 aConstraint = gridConstraints(0,1,0,1,1);
			 aConstraint.anchor = GridBagConstraints.LINE_START;
			 JPanel speakerOne = createSpeaker();
			 controlPanel.add(speakerOne,aConstraint);
			 
			 
			 
			 
		        GridBagConstraints m = new GridBagConstraints();
		        m.anchor = GridBagConstraints.LINE_START;
		    			m.gridx = 1;
		    			m.gridwidth = 1;
		    			m.insets = new Insets (0,0,0,0);
		    			//m.gridheight = 0; //To carry size
		    			m.fill = GridBagConstraints.HORIZONTAL;
		    			m.gridy = 0;
		    			m.weightx = 1;
		    			m.weighty = 1;
			
			 //Color.decode("#0D0707")
			 //Add button holder
			// aConstraint = gridConstraints(GridBagConstraints.PAGE_START,2,3,0,1,1);
		 
		    			
		   // buttonHolder = new JPanel(new GridBagLayout());
		    			int panelSize = (int)controlPanel.getPreferredSize().getHeight();		
		    buttonHolder.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.decode("#303030")));
			buttonHolder.setPreferredSize(new Dimension(screenWidth/3+210, panelSize-20));
			buttonHolder.setMinimumSize(new Dimension(screenWidth/3+210, panelSize-20));
			
			
			buttonHolder.setBackground(Color.decode("#383838"));
			controlPanel.add(buttonHolder,m);
			
			
			//m.anchor = GridBagConstraints.CENTER;
 			m.gridx = 0;
 			m.gridwidth = 1;
 			//m.insets = new Insets (0,0,0,0);
 			//m.gridheight = 0; //To carry size
 			m.fill = GridBagConstraints.HORIZONTAL;
 			//m.gridy = 0;
 			m.weightx = 1;
 			m.weighty = 1;
 			
 			int innerPanelSize = (int)buttonHolder.getPreferredSize().getHeight();		
 			
 			//The feature buttons create the space for the innexer box to adjsut it's size to. The
 			//size can be manually created, but for know it will be as big as the height of the feature boxes.
			innerButtonHolder.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.decode("#303030")));
			innerButtonHolder.setPreferredSize(new Dimension(screenWidth/3+210, innerPanelSize/2));
			innerButtonHolder.setMinimumSize(new Dimension(screenWidth/3+210, innerPanelSize/2));
		
			
			innerButtonHolder.setBackground(Color.decode("#404040"));
			buttonHolder.add(innerButtonHolder,m);
			 
			 
			 //gridX is set to 5 to allow components before it from 0 upwards
		     aConstraint = gridConstraints(2,1,0,1,1);
		     aConstraint.anchor = GridBagConstraints.LINE_END;
			 JPanel speakerTwo = createSpeaker();
			 controlPanel.add(speakerTwo,aConstraint);
	}
	
	public void addFeatureTabs() throws InvalidMidiDataException{
		   JTabbedPane getFeatures = FeatureTabs.getInstance().createTabbedBar();    
    	 aConstraint = gridConstraints(0,1,0,1,1);
    	  aConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
    	   
         
         
		contentPane.add(getFeatures,aConstraint);
	}
	
	public void addScreenPrompt() throws InvalidMidiDataException{ 
      aConstraint = gridConstraints(0,1,0,1,1);
      aConstraint.anchor = GridBagConstraints.FIRST_LINE_END;
		originalScreenPrompt = ScreenPrompt.getInstance().createCurrentPromptState();
		contentPane.add(originalScreenPrompt,aConstraint);
	}
	
	public void updateScreenPrompt() throws InvalidMidiDataException{
		  aConstraint = gridConstraints(0,1,0,1,1);
	      aConstraint.anchor = GridBagConstraints.FIRST_LINE_END;
		
		
	    contentPane.remove(originalScreenPrompt);
		originalScreenPrompt = ScreenPrompt.getInstance().createCurrentPromptState();
		contentPane.add(originalScreenPrompt,aConstraint); 
		//Needed to show update of JPanel after remove to update
	    contentPane.validate();
	    contentPane.repaint();
         
         
	}
}