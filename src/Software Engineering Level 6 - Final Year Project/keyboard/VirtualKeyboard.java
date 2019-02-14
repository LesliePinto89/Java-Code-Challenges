package keyboard;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import midi.Chord;
import midi.MidiMessageTypes;
import midi.StoreMetaEvents;
import midiDevices.MidiReceiver;
import tools.GetInstruments;
import tools.MIDIFileManager;
import tools.MIDIFilePlayerInteractions;
import tools.ProgramMainGUI;


/**
 * This class displays the piano software to the user, and acts as a base to the
 * class that constructs MIDI track files.
 * 
 */
public class VirtualKeyboard {

	private JSlider slider;
	private BufferedImage image;
	private ArrayList<JButton> naturalKeys = new ArrayList<JButton>();
	private JComboBox<String> instrumentList;
	private JComboBox<String> tempoList;
	
	
	//A singleton pattern so that only one instance of this class 
		//can be accessed and instantiated
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
	
	
	
	 int _W = 330;
		int line1 = 80;
	     int h_list = 100;
		int line2 = line1 + h_list + 50;
	
	
	
	
	
	
	private int PITCH_OCTAVE = 2;
	//private int START_WHOTE_NOTE_POSITION = 22;
	//private int START_SHARP_NOTE_POSITION = 42;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = (int) screenSize.getWidth();
	private int screenHeight  = (int) screenSize.getHeight();
	
	private int START_WHOTE_NOTE_POSITION = screenWidth /80;
	private int START_SHARP_NOTE_POSITION = screenWidth /35;
///35.5 defaut for sharp was 42;
	//Will contain Instantiated classes from main menu
	//private static VirtualKeyboard midiGui;

	private MidiMessageTypes midiMessageTypes;
	private GetInstruments loadInstruments;
	private MIDIFileManager fileManager;
	

	private JLayeredPane keyboardLayered;
	private JPanel holdPiano;
	private JTabbedPane tabbedPane;
	private JLayeredPane pianoLayers;
	private JPanel pianoBacking;
	

	private JFrame secondFrame;
	
	
	public void storeButtons(JButton noteButton) {
		naturalKeys.add(noteButton);
		
	}
	
	public ArrayList<JButton> getButtons() {
		return naturalKeys;
	}
	
	/**
	 * Create the application with the MIDI devices loaded in memory
	 * 
	 * @param loadMIDI
	 *            - The MIDI devices needed in the application
	 */
	//public VirtualKeyboard(MIDIFileManager loadedFileManager) {
		//this.fileManager = loadedFileManager;
		
	//}

	
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
			pianoLayers.add(button, new Integer(1));
		} else {
			button.setBackground(Color.WHITE);
			pianoLayers.add(button, new Integer(0));
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
		// screenWidth, screenHeight
		boolean endOctave = false;
		while (endOctave == false) {
			for (Note.NoteType getNote : naturalKeysEnums) {
				JButton button = new JButton(getNote.toString() + Integer.toString(PITCH_OCTAVE));
				//button.setBounds(START_WHOTE_NOTE_POSITION, 145, 35, 196);
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
	 * Gets all supported synthesizer's instruments names.
	 * 
	 * @param Instrument[]
	 *            channels - Stores instruments names
	 * @return array that stores the instruments names in memory
	 */

	
	public void addToKeyboardLayout(Component entry){
		keyboardLayered.add(entry);
	}
	
	
	/**
	 * Adjust all notes volume based on slider value.
	 */
	public void volumeToggle() {
		slider = new JSlider(0, 100, 71);
		slider.setBounds(300, 380, 100, 50);
		slider.setBackground(Color.BLACK);
		KeyboardInteractions volumeSliderListener = new KeyboardInteractions(midiMessageTypes,slider);
		slider.addChangeListener(volumeSliderListener);
		keyboardLayered.add(slider);
	}

	public void changeTempo() {
		midiMessageTypes.storedTemposMap();
		tempoList = new JComboBox<String>(midiMessageTypes.storedTemposMapKeys());
		tempoList.setName("tempoList");
		tempoList.setEditable(true);
		tempoList.setBounds(screenWidth /2 + 200, screenHeight /16, 250, 52);
		tempoList.setSelectedIndex(12);
		KeyboardInteractions tempoBoxActionListener = new KeyboardInteractions(midiMessageTypes,null,tempoList);
		tempoList.addActionListener(tempoBoxActionListener);
		keyboardLayered.add(tempoList);
	}
	
	public void instrumentChoices() {
		loadInstruments = new GetInstruments();
		loadInstruments.setupInstruments();
		loadInstruments.storeInstrumentsNames = loadInstruments.allInstruments(loadInstruments.getListOfMidiChannels());
		instrumentList = new JComboBox<String>(loadInstruments.storeInstrumentsNames);
		instrumentList.setName("instrumentList");
		instrumentList.setEditable(true);
		instrumentList.setBounds(screenWidth /2, screenHeight /16, 200, 52);
		KeyboardInteractions instrumentsBoxActionListener = new KeyboardInteractions(null,loadInstruments,instrumentList);
		instrumentList.addActionListener(instrumentsBoxActionListener);
		keyboardLayered.add(instrumentList);
	}
	
	public void playButton() throws IOException {
		JToggleButton playMIDI = new JToggleButton();
		playMIDI.setBounds(secondFrame.getWidth() / 3 + 130, secondFrame.getHeight() / 2 - 30, 70, 62);
		BufferedImage playOff = ImageIO.read(new File("src/Images/play button off.png"));
		Image scaledOff = playOff.getScaledInstance(playMIDI.getWidth(), playMIDI.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon playOffIcon = new ImageIcon(scaledOff);
		playMIDI.setIcon(playOffIcon);
		
		BufferedImage playOn = ImageIO.read(new File("src/Images/play button.png"));
		Image scaledOn = playOn.getScaledInstance(playMIDI.getWidth(), playMIDI.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon playOnIcon = new ImageIcon(scaledOn);
		playMIDI.setSelectedIcon(playOnIcon);
		
		playMIDI.setBackground(Color.black);
		playMIDI.setBorder(new LineBorder(Color.BLACK));
		playMIDI.setFocusPainted(false);
		
		playMIDI.setName("playButton");	
		ActionListener playButtonActionListener = new KeyboardInteractions(playMIDI);
		playMIDI.addActionListener(playButtonActionListener);
		keyboardLayered.add(playMIDI);
		
	}

	public void recordButton() throws InvalidMidiDataException, IOException {
		
		BufferedImage recOff = ImageIO.read(new File("src/Images/recordOff.png"));
		ImageIcon recOffIcon = new ImageIcon(recOff);
		BufferedImage recOn = ImageIO.read(new File("src/Images/recordOn.png"));
		ImageIcon recOnIcon = new ImageIcon(recOn);
		
	    JToggleButton recordMIDI = new JToggleButton(recOffIcon);
		recordMIDI.setSelectedIcon(recOnIcon);
		recordMIDI.setBackground(Color.black);
		recordMIDI.setBorder(new LineBorder(Color.BLACK));
		recordMIDI.setFocusPainted(false);
		recordMIDI.setBounds(secondFrame.getWidth() / 3, secondFrame.getHeight() / 2 - 20, 70, 42);
		recordMIDI.setName("recordButton");
		recordMIDI.setText("Off");
		
		
		ActionListener recordButtonActionListener = new KeyboardInteractions(recordMIDI);
		recordMIDI.addActionListener(recordButtonActionListener);
		keyboardLayered.add(recordMIDI);
		
		
		
	}
	
	public void saveMIDIButton() throws IOException{
		JToggleButton saveMIDI = new JToggleButton();
		saveMIDI.setBounds(secondFrame.getWidth() / 3 + 260, secondFrame.getHeight() / 2 - 30, 70, 62);
		BufferedImage saveOff = ImageIO.read(new File("src/Images/midi document.png"));
		Image scaledOff = saveOff.getScaledInstance(saveMIDI.getWidth(), saveMIDI.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon saveOffIcon = new ImageIcon(scaledOff);
		saveMIDI.setIcon(saveOffIcon);
		
		BufferedImage saveOn = ImageIO.read(new File("src/Images/midi document - clicked.png"));
		Image scaledOn = saveOn.getScaledInstance(saveMIDI.getWidth(), saveMIDI.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon saveOnIcon = new ImageIcon(scaledOn);

		saveMIDI.setBackground(Color.BLACK);
		saveMIDI.setBorder(new LineBorder(Color.BLACK));
		saveMIDI.setSelectedIcon(saveOnIcon);
		saveMIDI.setName("saveButton");
		keyboardLayered.add(saveMIDI);	
		ActionListener saveButtonActionListener = new KeyboardInteractions(saveMIDI);
		saveMIDI.addActionListener(saveButtonActionListener);
	}
	
	public void freePlayOrMakeTrack() throws InvalidMidiDataException, MidiUnavailableException {
		ArrayList<JButton> retrievedList = getButtons();
		for (int i = 0; i < retrievedList.size(); i++) {
			JButton pressedNote = retrievedList.get(i);
			//Note givenNote = new Note();
			String noteName = pressedNote.getText();
			int getValue = Note.convertToPitch(noteName);
			
			
			MouseListener mouseListener = new KeyboardInteractions(midiMessageTypes,pressedNote,getValue);
			pressedNote.addMouseListener(mouseListener);
			
			/*pressedNote.addMouseListener(new MouseAdapter() {
				
				public void mousePressed(MouseEvent bOne) {
					if (loadReciever.isFreePlayEnded() == false) {
						//clicked = true;
						System.out.println("Press Works");
						try {
							loadReciever.freeNotePlay(getValue);
						} catch (InvalidMidiDataException e1) {
							e1.printStackTrace();
						}
					}}
				
				public void mouseEntered(MouseEvent bNew) {
					//MouseEvent getIt = bNew.bon
	            	while(bNew.getButton() == MouseEvent.BUTTON1){
	        			System.out.println("Drag Works");
	        			if (loadReciever.isFreePlayEnded() == false) {
	        				//clicked = true;
	        				try {
	        					loadReciever.freeNotePlay(getValue);
	        				} catch (InvalidMidiDataException e1) {
	        					e1.printStackTrace();
	        				}

	        			}
	        			
	        		}
	            }
				

	        });*/
			
		}
		
	//	loadMIDI.returnSequencer().addMetaEventListener(new MetaEventListener() {
	   //     public void meta(MetaMessage msg) {
	     //       if (msg.getType() == 47) {
	               
	        //        	loadMIDI.returnSequencer().setSequence(loadMIDI.getSeq());             
	        //        	loadMIDI.returnSequencer().setTickPosition(0);
	        //        if (loop) { // End of track                        
	                   // sequencer.start();
	         ////       }
	          //  }
	       /// }
	   // });

	}

	
	public JPanel leftSideChordNames (){
		Chord.getInstance();
		JPanel instancePanel = new JPanel();
		DefaultListModel<String> majorChordList = new DefaultListModel<String>();
		 JList<String> jListMajorChords = new JList<String>(majorChordList);
		 jListMajorChords.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		 jListMajorChords.setBounds(0, line1 + 50, _W, h_list);
		 jListMajorChords.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		 jListMajorChords.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		 jListMajorChords.setVisibleRowCount(-1);
		 jListMajorChords.setBorder(new LineBorder(Color.BLUE));
		 jListMajorChords.setName("Major");
		 
		 MouseListener jListChordSelectListener = new KeyboardInteractions(jListMajorChords);
		 jListMajorChords.addMouseListener(jListChordSelectListener);

		 JScrollPane majorChordListScroller;
		 majorChordListScroller = new JScrollPane(jListMajorChords);
		 majorChordListScroller.setPreferredSize(new Dimension(_W - 10, tabbedPane.getHeight() /4 ));
		 majorChordListScroller.setBounds(0, line1 + 50, _W - 10, h_list);
		    
		 Chord.majorChordNamesList[] majorChordNames = Chord. majorChordNamesList.values();
		    
		 
		 for (int i = 0; i < majorChordNames.length; i++) {
		    	majorChordList.addElement(majorChordNames[i].name());
		    }
		  
			DefaultListModel<String> minorChordList = new DefaultListModel<String>();
			 JList<String> jListMinorChords = new JList<String>(minorChordList);
			 jListMinorChords.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			 jListMinorChords.setBounds(0, line1 + 50, _W, h_list);
			 jListMinorChords.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			 jListMinorChords.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			 jListMinorChords.setVisibleRowCount(-1);
			 jListMinorChords.setName("Minor");
			 
			 MouseListener jMinorListChordSelectListener = new KeyboardInteractions(jListMinorChords);
			 jListMinorChords.addMouseListener(jMinorListChordSelectListener);
			 
			 JScrollPane minorChordListScroller;
			 minorChordListScroller = new JScrollPane(jListMinorChords);
			 minorChordListScroller.setPreferredSize(new Dimension(_W - 10, tabbedPane.getHeight() /4 ));
			 minorChordListScroller.setBounds(0, line1 + 50, _W - 10, h_list);
		    Chord.minorChordNamesList[] minorChordNames = Chord.minorChordNamesList.values();
		   
		    for (int i = 0; i < minorChordNames.length; i++) {
		    	minorChordList.addElement(minorChordNames[i].name());
		    
		    }
		    
		    JLabel majorChords = new JLabel ();
		    majorChords.setText("Major Chords");
		    instancePanel.add(majorChords);
		    instancePanel.add(majorChordListScroller);
		    JLabel minorChords = new JLabel ();
		    minorChords.setText("Minor Chords");
		    instancePanel.add(minorChords);			    
		    instancePanel.add(minorChordListScroller);	
		    
		    //Store to get name from list model in interactions class
		    Chord.getInstance().storeMajorListModel(majorChordList);
		    Chord.getInstance().storeMinorListModel(minorChordList);
		    return instancePanel;
		   
	}
	
	public void createTabbedBar() throws InvalidMidiDataException{
		// Make other functions tabs
				tabbedPane = new JTabbedPane(JTabbedPane.TOP);
				tabbedPane.setBounds(0, 0, screenWidth / 2, screenHeight / 3);
				
	                 JPanel leftPane = leftSideChordNames();
				    JPanel rightPane = new JPanel ();

				
				JSplitPane splitPane = new JSplitPane();
				splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				  splitPane.setContinuousLayout(true);
				  splitPane.setLeftComponent(leftPane);  
				  splitPane.setRightComponent(rightPane);
		         splitPane.setOneTouchExpandable(true);
		          splitPane.setDividerLocation(tabbedPane.getWidth() /2);

				tabbedPane.addTab("Chords", splitPane);
				tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

				
				JComponent panel2 = makeTextPanel("Panel #2");
				tabbedPane.addTab("Tab 2", panel2
				               );
				tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

				JComponent panel3 = makeTextPanel("Panel #3");
				tabbedPane.addTab("Tab 3", panel3);
				tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

				JComponent panel4 = makeTextPanel(
				        "Panel #4 (has a preferred size of 410 x 50).");
				panel4.setPreferredSize(new Dimension(410, 50));
				tabbedPane.addTab("Tab 4",  panel4);
				tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
				keyboardLayered.add(tabbedPane,new Integer(0), 0);
	}
	
	private JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
	
	
	public void createChordProgression () throws InvalidMidiDataException{
		JToggleButton chordProgression = new JToggleButton("Chords");
		chordProgression.setBounds(secondFrame.getWidth() / 3 + 310,secondFrame.getHeight() / 2 - 120, 100, 42);
		chordProgression.setName("Chord Progression");	
		ActionListener chordsButtonActionListener = new KeyboardInteractions(chordProgression);
		chordProgression.addActionListener(chordsButtonActionListener);
		keyboardLayered.add(chordProgression);
		
		
		
		////ChordProgression makeChordProgression = new ChordProgression();
		//makeChordProgression.generateChordProgression();
	}
	
	
	
	/**
	 * Draws the main GUI layout, used in each Application GUI feature.
	 * @throws IOException 
	 * 
	 */
	public void drawKeyboardGUI() throws InvalidMidiDataException, MidiUnavailableException, IOException {

		 //screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 // screenWidth = (int) screenSize.getWidth();
		 // screenHeight = (int) screenSize.getHeight();

		// Make new Content Pane
		 keyboardLayered = new JLayeredPane();
		
		secondFrame = new JFrame("6100COMP: Computer Music Education Application");
		secondFrame.setBounds(0, 0, screenWidth, screenHeight);
		secondFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		secondFrame.setContentPane(keyboardLayered);
		
		//secondFrame.pack();
		
		secondFrame.setVisible(true);
		
	}
	
	/**
	 * Main keyboard layout, with method calls to its various components.
	 * 
	 * @throws IOException
	 * @wbp.parser.entryPoint
	 * 
	 */
	public void createVirtualKeyboard() throws InvalidMidiDataException, MidiUnavailableException, IOException {
	
		midiMessageTypes = new MidiMessageTypes();
		instrumentChoices();
		pianoLayers = new JLayeredPane();
		pianoLayers.setBounds(10, 300, secondFrame.getWidth(), 422);		
		
		//changed this part
		createWholeKeys();
		createSharpKeys();
		volumeToggle();
		playButton();
		recordButton();
		saveMIDIButton();
		freePlayOrMakeTrack();
		
		drawPiano();
		changeTempo();
		createTabbedBar();
		
		//createChordProgression();		
	}

	public void drawPiano() throws IOException{
		     
				pianoBacking = new JPanel();
				pianoBacking.setBounds(10, 40, secondFrame.getWidth() - 80, 322);
				pianoBacking.setBackground(Color.black);
			
				
				JPanel speakers = new JPanel();
				speakers.setBounds(15, 40, secondFrame.getWidth() - 90, 92);
				speakers.setBackground(Color.black);
				
				
				
				BufferedImage image = ImageIO.read(new File("src/Images/Speaker Grill 4.jpg"));
				Image dimg = image.getScaledInstance(speakers.getWidth()/4, speakers.getHeight()/2,
				       Image.SCALE_SMOOTH);
				
				speakers.setLayout(new GridLayout());
				//BufferedImage image =loadMIDI.getSequenceGraph();
				JLabel picLabel = new JLabel(new ImageIcon(dimg));
				picLabel.setHorizontalAlignment(JLabel.LEFT);
				speakers.add(picLabel);
				
				JLabel picLabel2 = new JLabel(new ImageIcon(dimg));
				picLabel2.setHorizontalAlignment(JLabel.RIGHT);
				speakers.add(picLabel2);
				
				
				/*JLabel instrumentName = new JLabel();
				instrumentName.setHorizontalAlignment(JLabel.CENTER);
				instrumentName.setText(loadMIDI.getSelectedInstrument());
				speakers.add(instrumentName);*/
				
				pianoBacking.add(speakers,0);
				pianoLayers.add(pianoBacking);
				secondFrame.getContentPane().add(pianoLayers);
				//pianoLayers.add(pianoBacking);
				//pianoLayers.add(pianoBacking);
				//super.frame.getContentPane().add(pianoLayers);
		
	}
}