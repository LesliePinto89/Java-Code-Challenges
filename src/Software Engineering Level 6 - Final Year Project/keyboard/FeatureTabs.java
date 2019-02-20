package keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import midiDevices.GetInstruments;
import tools.Metronome;
import tools.SwingComponents;
import keyboard.KeyboardInteractions;
import midi.AddChordToFeatureTab;
import midi.Chord;
import midi.ChordProgression;
import midi.MidiMessageTypes;

public class FeatureTabs {

	private JTabbedPane tabbedPane = SwingComponents.getInstance().getFeatureTabDimensions();
	int _W = 330;
	int line1 = 80;
	int h_list = 100;
	int line2 = line1 + h_list + 50;
	//private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	//private int screenWidth = SwingComponents.getInstance().getScreenWidth();
	//;private int screenHeight = SwingComponents.getInstance().getScreenHeight();
	//private JComboBox<String> instrumentList;
	DefaultListModel <String> instrumentsInListModel = new DefaultListModel <String>();
	private static volatile FeatureTabs instance = null;

	private FeatureTabs() {
	}

	public static FeatureTabs getInstance() {
		if (instance == null) {
			synchronized (FeatureTabs.class) {
				if (instance == null) {
					instance = new FeatureTabs();
					//instance.storeInstrumentsList();
				}
			}
		}
		return instance;
	}

	public JPanel leftSideChordNames() {
		//Chord.getInstance();
		JPanel instancePanel = new JPanel();
		
		//Major Chord elements
		DefaultListModel<String> majorChordList = AddChordToFeatureTab.getInstance().getMajorChordsInList();
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
		majorChordListScroller.setPreferredSize(new Dimension(_W - 10, tabbedPane.getHeight() / 4));
		majorChordListScroller.setBounds(0, line1 + 50, _W - 10, h_list);
		
		//Minor Chord elements
		DefaultListModel<String> minorChordList = AddChordToFeatureTab.getInstance().getMinorChordsInList();
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
		minorChordListScroller.setPreferredSize(new Dimension(_W - 10, tabbedPane.getHeight() / 4));
		minorChordListScroller.setBounds(0, line1 + 50, _W - 10, h_list);

		//Finish construction of panel
		
		JLabel majorChordsText = new JLabel();
		majorChordsText.setText("Major Chords");
		instancePanel.add(majorChordsText);
		instancePanel.add(majorChordListScroller);
		JLabel minorChordsText = new JLabel();
		minorChordsText.setText("Minor Chords");
		instancePanel.add(minorChordsText);
		instancePanel.add(minorChordListScroller);
		return instancePanel;
	}

	public JPanel leftSideChordProgressionNames() {
		ChordProgression.getInstance();
		JPanel instancePanel = new JPanel();
		
		//Major Chord Progressions elements
		DefaultListModel<String> majorChordProgDefaultList = ChordProgression.getInstance().getMajorChordProgressions();
		JList<String> jListMajorProgressions = new JList<String>(majorChordProgDefaultList);
		jListMajorProgressions.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListMajorProgressions.setBounds(0, line1 + 50, _W, h_list);
		jListMajorProgressions.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListMajorProgressions.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListMajorProgressions.setVisibleRowCount(-1);
		jListMajorProgressions.setBorder(new LineBorder(Color.BLUE));
		jListMajorProgressions.setName("MajorProg");

		MouseListener jListMajorChordProgSelectListener = new KeyboardInteractions(jListMajorProgressions);
		jListMajorProgressions.addMouseListener(jListMajorChordProgSelectListener);

		JScrollPane getChordProgressionsListScroller;
		getChordProgressionsListScroller = new JScrollPane(jListMajorProgressions);
		getChordProgressionsListScroller.setPreferredSize(new Dimension(_W - 10, tabbedPane.getHeight() / 4));
		getChordProgressionsListScroller.setBounds(0, line1 + 50, _W - 10, h_list);

		//Minor Chord Progressions elements
		DefaultListModel<String> minorChordProgDefaultList = ChordProgression.getInstance().getMinorChordProgressions();
		JList<String> jListMinorChordProgs = new JList<String>(minorChordProgDefaultList);
		jListMinorChordProgs.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListMinorChordProgs.setBounds(0, line1 + 50, _W, h_list);
		jListMinorChordProgs.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListMinorChordProgs.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListMinorChordProgs.setVisibleRowCount(-1);
		jListMinorChordProgs.setName("MinorProg");

		MouseListener jListMinorChordProgSelectListener = new KeyboardInteractions(jListMinorChordProgs);
		jListMinorChordProgs.addMouseListener(jListMinorChordProgSelectListener);

		JScrollPane minorChordProgsListScroller;
		minorChordProgsListScroller = new JScrollPane(jListMinorChordProgs);
		minorChordProgsListScroller.setPreferredSize(new Dimension(_W - 10, tabbedPane.getHeight() / 4));
		minorChordProgsListScroller.setBounds(0, line1 + 50, _W - 10, h_list);
		
		//Finish construction of panel
		JLabel majorProgsText = new JLabel();
		majorProgsText.setText("Major Chords Progressions");
		instancePanel.add(majorProgsText);
		instancePanel.add(getChordProgressionsListScroller);
		JLabel minorProgsText = new JLabel();
		minorProgsText.setText("Minor Chords Progressions");
		instancePanel.add(minorProgsText);
		instancePanel.add(minorChordProgsListScroller);
		return instancePanel;

	}

	
	/*public void storeInstrumentsList(){
		GetInstruments loadedInstruments = GetInstruments.getInstance();
		//minorChordProgressions[] minorProgressions = minorChordProgressions.values();
		for (int i =0;i<loadedInstruments.allInstruments().length;i++) {
			instrumentsInListModel.addElement(loadedInstruments.allInstruments()[i]);
		}
		
		
	}
	
	public DefaultListModel<String> getModelInstruments (){
		return instrumentsInListModel;
	}
	*/
	/*public void changeTempo() {
		MidiMessageTypes.getInstance().getTemposMap();
		tempoList = new JComboBox<String>(MidiMessageTypes.getInstance().storedTemposMapKeys());
		tempoList.setName("tempoList");
		tempoList.setEditable(true);
		tempoList.setBounds(screenWidth /2 + 200, screenHeight /16, 250, 52);
		tempoList.setSelectedIndex(12);
		KeyboardInteractions tempoBoxActionListener = new KeyboardInteractions(tempoList);
		tempoList.addActionListener(tempoBoxActionListener);
		keyboardLayered.add(tempoList);
	}*/
	
	public JPanel tempoPanel() {
		//MidiMessageTypes.getInstance().getTemposMap();
		MidiMessageTypes midiMessages = MidiMessageTypes.getInstance();
       JPanel instancePanel = new JPanel();
 
       DefaultListModel<String> tempoList = midiMessages.getTemposInModel();
		JList<String> jListTempos = new JList<String>(tempoList);
		jListTempos.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListTempos.setBounds(0, line1 + 50, _W, h_list);
		jListTempos.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListTempos.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListTempos.setVisibleRowCount(-1);
		jListTempos.setName("Tempos");
		JScrollPane tempoScroll = new JScrollPane(jListTempos);
		tempoScroll.setPreferredSize(new Dimension(tabbedPane.getWidth()/2, tabbedPane.getHeight() -50));
		tempoScroll.setBounds(0, line1 + 50, _W - 10, h_list);
		
	
		jListTempos.addMouseListener(new MouseAdapter(){
	         public void mouseClicked(MouseEvent tempoPressed) {
	        	 MidiMessageTypes messageTypes = MidiMessageTypes.getInstance();
	        	 String selectedTempo ="";
	 			 int index = jListTempos.locationToIndex(tempoPressed.getPoint());
	 			 selectedTempo = messageTypes.getTemposInModel().getElementAt(index);
	 			 selectedTempo = selectedTempo.substring(0, selectedTempo.indexOf(":"));
	 			 
	 			 //Might be needed if can use tempo for other functions
	 			 messageTypes.selectedTempo(selectedTempo);
	 			 messageTypes.saveTempoSeqEnd(selectedTempo);
	 			 messageTypes.tempoChanged(true);
	 			 /*try {
	 				 Metronome.getInstance().chooseTempo();
	 				} catch (InvalidMidiDataException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}*/
	           
	         }                
	      });
		
		
		
		//MouseListener tempoListener = new KeyboardInteractions(jListTempos);
		//jListTempos.addMouseListener(tempoListener);
	       
		instancePanel.add(tempoScroll);
		return instancePanel;
	}
	
	public JPanel instrumentChoicesPanel() {
		GetInstruments loadedInstruments = GetInstruments.getInstance();
       JPanel instancePanel = new JPanel();
 
       DefaultListModel<String> allInstruments = loadedInstruments.getAllInstruments();
		JList<String> jListInstruments = new JList<String>(allInstruments);
		jListInstruments.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListInstruments.setBounds(0, line1 + 50, _W, h_list);
		jListInstruments.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListInstruments.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListInstruments.setVisibleRowCount(-1);
		jListInstruments.setName("Instruments");
		JScrollPane instrumentsScroll = new JScrollPane(jListInstruments);
		instrumentsScroll.setPreferredSize(new Dimension(tabbedPane.getWidth() - 40, tabbedPane.getHeight() -50));
		instrumentsScroll.setBounds(0, line1 + 50, _W - 10, h_list);
		
	
		MouseListener instrumentsListener = new KeyboardInteractions(jListInstruments);
		jListInstruments.addMouseListener(instrumentsListener);
	       
		instancePanel.add(instrumentsScroll);	
		
	/*	//Piano instruments
       DefaultListModel<String> pianoInstruments = loadedInstruments.getPianoInstruments();
		JList<String> jListPiano = new JList<String>(pianoInstruments);
		jListPiano.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListPiano.setBounds(0, line1 + 50, _W, h_list);
		jListPiano.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListPiano.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListPiano.setVisibleRowCount(-1);
		jListPiano.setName("Piano Instruments");
		JScrollPane pianoScroll = new JScrollPane(jListPiano);
		pianoScroll.setPreferredSize(new Dimension(tabbedPane.getWidth() - 40, 20));
		pianoScroll.setBounds(0, line1 + 50, _W - 10, h_list);
		
	
		MouseListener pianoListener = new KeyboardInteractions(jListPiano);
		jListPiano.addMouseListener(pianoListener);
		
		
		//Piano instruments
	       DefaultListModel<String> percussionInstruments = loadedInstruments.getPercussionInstruments();
			JList<String> jListPercussion = new JList<String>(percussionInstruments);
			jListPercussion.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			jListPercussion.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			jListPercussion.setVisibleRowCount(-1);
			jListPercussion.setName("Percussion Instruments");
			JScrollPane percussionScroll = new JScrollPane(jListPercussion);
			percussionScroll.setPreferredSize(new Dimension(tabbedPane.getWidth() - 40, 20));
			percussionScroll.setBounds(50, line1 + 50, _W - 10, h_list);
			MouseListener percussionListener = new KeyboardInteractions(jListPercussion);
			jListPercussion.addMouseListener(percussionListener);
			
		 	instancePanel.add(pianoScroll);	
		    instancePanel.add(percussionScroll);
			*/
		
	return instancePanel;
	
	//keyboardLayered.add(instrumentList);
}
	
	//Combo list version
	/*public JComboBox<String> instrumentChoices() {
	//Assigned only instance of instrument class to make variable to make 
	//code more eligible
	GetInstruments loadedInstruments = GetInstruments.getInstance();
	loadedInstruments.storeInstrumentsNames = loadedInstruments.allInstruments(loadedInstruments.getListOfMidiChannels());
	instrumentList = new JComboBox<String>(loadedInstruments.storeInstrumentsNames);
	instrumentList.setName("instrumentList");
	instrumentList.setEditable(true);
	instrumentList.setBounds(screenWidth /2, screenHeight /16, 200, 52);
	KeyboardInteractions instrumentsBoxActionListener = new KeyboardInteractions(instrumentList);
	instrumentList.addActionListener(instrumentsBoxActionListener);
	
	return instrumentList;
	
	//keyboardLayered.add(instrumentList);
}
	*/
	
	
	
	public JTabbedPane createTabbedBar() throws InvalidMidiDataException {
		
		//tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//tabbedPane.setBounds(0, 0, screenWidth / 2, screenHeight / 3);

		JPanel leftPaneTabOne = leftSideChordNames();
		JPanel rightPaneTabOne = new JPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setContinuousLayout(true);
		splitPane.setLeftComponent(leftPaneTabOne);
		splitPane.setRightComponent(rightPaneTabOne);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(tabbedPane.getWidth() / 2);
		tabbedPane.addTab("Chords", splitPane);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JPanel leftPaneTabTwo = leftSideChordProgressionNames();
		JPanel rightPaneTabTwo = new JPanel();
		JSplitPane splitPaneTabTwo =new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneTabTwo.setContinuousLayout(true);
		splitPaneTabTwo.setLeftComponent(leftPaneTabTwo);
		splitPaneTabTwo.setRightComponent(rightPaneTabTwo);
		splitPaneTabTwo.setOneTouchExpandable(true);
		splitPaneTabTwo.setDividerLocation(tabbedPane.getWidth() / 2);
		tabbedPane.addTab("Chord Progressions", splitPaneTabTwo);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		
		JPanel instrumentsPane = new JPanel();
		instrumentsPane.add(instrumentChoicesPanel());	
		tabbedPane.addTab("Instruments", instrumentsPane);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		
		JPanel leftTempoPane = new JPanel();
		leftTempoPane.add(tempoPanel());
		
		JPanel rightTempoPane = new JPanel();
		rightTempoPane.setLayout(null);
		JButton playTempoButton = SwingComponents.getInstance().customJButton(0,0,100,20);
		playTempoButton.setText("Play Tempo");
		JButton stopTempoButton = SwingComponents.getInstance().customJButton(playTempoButton.getWidth() + 10,0,100,20);
		stopTempoButton.setText("Stop Tempo");
		
		
		playTempoButton.addMouseListener(new MouseAdapter(){
	         public void mouseClicked(MouseEvent pressedTempo ) {	 
	        	 try {
	 				 Metronome.getInstance().chooseTempo();
	 				} catch (InvalidMidiDataException e) {
	 					e.printStackTrace();
	 				}
	         }                
	      });
		stopTempoButton.addMouseListener(new MouseAdapter(){
	         public void mouseClicked(MouseEvent stopTempo ) {		      
	        			 Metronome.getInstance().stopLoop();
	         }                
	      });
		
		rightTempoPane.add(playTempoButton);
		rightTempoPane.add(stopTempoButton);
		
		
		
		JPanel tempoSliderPanel = Metronome.getInstance().tempoSlider();
		tempoSliderPanel.setLayout(null);
		
		
		rightTempoPane.add(tempoSliderPanel);
		
		JSplitPane splitPaneTabThree = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneTabThree.setContinuousLayout(true);
		splitPaneTabThree.setLeftComponent(leftTempoPane);
		splitPaneTabThree.setRightComponent(rightTempoPane);
		splitPaneTabThree.setOneTouchExpandable(true);
		splitPaneTabThree.setDividerLocation(tabbedPane.getWidth() / 2);
		tabbedPane.addTab("Metronome", splitPaneTabThree);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		
		JComponent panel4 = makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");
		panel4.setPreferredSize(new Dimension(410, 50));
		tabbedPane.addTab("Tab 4", panel4);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		// Previous arrangement before moving to new class
		// keyboardLayered.add(tabbedPane,new Integer(0), 0);
		return tabbedPane;
	}

	private JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}
}
