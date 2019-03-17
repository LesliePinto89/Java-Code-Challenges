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


import midiDevices.GetInstruments;
import tools.Metronome;
import tools.SwingComponents;
import keyboard.KeyboardInteractions;
import midi.MidiMessageTypes;

public class FeatureTabs {

	private JTabbedPane tabbedPane = SwingComponents.getInstance().getFeatureTab();
	private int jListTableWidth;
	
	private int jListYPos =  SwingComponents.getJListYPos();
	private int jListTableHeight =  SwingComponents.getJListTableHeight();
	private int jYAndHeight =  SwingComponents.getJListYAndHeight();

	private DefaultListModel <String> instrumentsInListModel = new DefaultListModel <String>();
	private SwingComponents components = SwingComponents.getInstance();
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

	
	public JPanel instrumentChoicesPanel() {
		GetInstruments loadedInstruments = GetInstruments.getInstance();
       JPanel instancePanel = new JPanel();
       //Fizes border color
       instancePanel.setBackground(Color.decode("#D2691E"));
    
       //Color.decode(#F08080)
       
       //instancePanel.setBackground(Color.decode("#F0F8FF"));
       jListTableWidth = SwingComponents.getJListWidth();
 
       DefaultListModel<String> allInstruments = loadedInstruments.getAllInstruments();
       
     
		JList<String> jListInstruments = new JList<String>(allInstruments);
		SwingComponents.getInstance().colourFeatureTab(jListInstruments, Color.decode("#F4A460"));
		jListInstruments.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListInstruments.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListInstruments.setVisibleRowCount(-1);
		jListInstruments.setName("Instruments");
		jListInstruments.setFixedCellHeight(50);
		
		JScrollPane instrumentsScroll = new JScrollPane(jListInstruments);
		//instrumentsScroll.setBackground(Color.decode("#F0F8FF"));
		//Using tabbedpane width wont work because the code defines the preferred size for the layout manager to use
		instrumentsScroll.setPreferredSize(new Dimension(jListTableWidth-70, jListTableHeight));
		instrumentsScroll.setMinimumSize(new Dimension(jListTableWidth-70, jListTableHeight));
		

		MouseListener instrumentsListener = new KeyboardInteractions(jListInstruments);
		jListInstruments.addMouseListener(instrumentsListener); 
		instancePanel.add(instrumentsScroll);	
		
	return instancePanel;
}
	
	public JPanel tempoPanel() {
		MidiMessageTypes midiMessages = MidiMessageTypes.getInstance();
       JPanel instancePanel = new JPanel();
       DefaultListModel<String> tempoList = midiMessages.getTemposInModel();
		JList<String> jListTempos = new JList<String>(tempoList);
		jListTempos.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListTempos.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListTempos.setVisibleRowCount(-1);
		jListTempos.setName("Tempos");
		JScrollPane tempoScroll = new JScrollPane(jListTempos);
		tempoScroll.setPreferredSize(new Dimension(jListTableWidth/2, jListTableHeight));
		tempoScroll.setMinimumSize(new Dimension(jListTableWidth/2, jListTableHeight));
	
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
	         }                
	      });
		instancePanel.add(tempoScroll);
		return instancePanel;
	}
	
	
	
	public JTabbedPane createTabbedBar() throws InvalidMidiDataException {
		components.featureTabDimensions();
		JPanel instrumentsPane = new JPanel();
		
		instrumentsPane.add(instrumentChoicesPanel());
		
		instrumentsPane.setBackground(Color.decode("#D2691E"));

		
		tabbedPane.addTab("Instruments", instrumentsPane);
		
		//tabbedPane.setPreferredSize(new Dimension(components.getScreenWidth()/2,components.getScreenHeight()/2));
		//tabbedPane.setMinimumSize(new Dimension(components.getScreenWidth()/2,components.getScreenHeight()/2));
		
		JPanel leftTempoPane = new JPanel();
		leftTempoPane.add(tempoPanel());
		
		JPanel rightTempoPane = new JPanel();
		//As last null is mouse listener, might need to modify as below seperate listener will replace null
		JButton playTempoButton = SwingComponents.getInstance().customJButton(100,20,null,null);
		playTempoButton.setText("Play Tempo");
		JButton stopTempoButton = SwingComponents.getInstance().customJButton(100,20,null,null);
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
		rightTempoPane.add(tempoSliderPanel);
		
		JSplitPane splitPaneTabThree = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneTabThree.setContinuousLayout(true);
		splitPaneTabThree.setLeftComponent(leftTempoPane);
		splitPaneTabThree.setRightComponent(rightTempoPane);
		splitPaneTabThree.setOneTouchExpandable(true);
		
		//int test = tabbedPane.getWidth() / 2;
		splitPaneTabThree.setDividerLocation(SwingComponents.getJListWidth()/2);
		tabbedPane.addTab("Metronome", splitPaneTabThree);
		
		JComponent panel4 = makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");
		panel4.setPreferredSize(new Dimension(410, 50));
		panel4.setMinimumSize(new Dimension(410, 50));
		tabbedPane.addTab("Tab 4", panel4);

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
