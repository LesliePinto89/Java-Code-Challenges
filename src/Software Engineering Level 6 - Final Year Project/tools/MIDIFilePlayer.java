package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import keyboard.KeyboardInteractions;
import midi.StoreMetaEvents;
import midiDevices.MidiReciever;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MIDIFilePlayer extends JPanel {

	private DefaultListModel<String> songList = new DefaultListModel<String>();
	private JList<String> jSongList = new JList<String>(songList);
	private JScrollPane listScroller;

	private ArrayList<Long> carriedMetaTickEventMessages = new ArrayList<Long>();
	private MetaMessage[] carriedMetaBytesEventMessages = null;

	private ArrayList<File> storedMIDISavedFiles = new ArrayList<File>();

	private int JlistIndex;

	private JButton btnPlay = new JButton();
	// private JButton btnAdd = new JButton();
	private JButton btnNext = new JButton();
	private JButton btnPrev = new JButton();
	private JButton selectMidiFileButton;

	private JButton btnShSt = new JButton();
	private JButton btnShWf = new JButton();
	private JButton btnShDi = new JButton();
	private JButton btnDel = new JButton();
	private JButton btnDelAll = new JButton();
	private JMenuBar topMenu = new JMenuBar();
	private JLabel lblplaying = new JLabel();
	private JLabel lblst = new JLabel();
	private JLabel lblet = new JLabel();

	private int btn_h = 35;
	private int _W = 330;
	private int line1 = 80;
	private int h_list = 100;
	private int line2 = line1 + h_list + 50;

	private JPanel backgroundPanel = new JPanel();
	private JPanel containsButtons = new JPanel();
	private JPanel playerOptions = new JPanel();
	private JPanel midiVisulizer = new JPanel();
	private JFrame secondFrame;
	private JPanel panelNP = new JPanel();
	private JPanel contBtns2 = new JPanel();
	private JLayeredPane playerLayered = new JLayeredPane();
	private JPanel contSlbl = new JPanel();
	private Dimension screenSize;
	private JPanel holdBar;

	private MidiReciever reciever;
	private MIDIFileManager midiFileManager;
	private StoreMetaEvents newMetaEvents;
	private CustomSwingComponents makeComponents;
	private File retrieveFile;

	private int startTick = 0;
	private int endTick = 0;

	public MIDIFilePlayer(MidiReciever carriedReciever, MIDIFileManager carriedMidiFileManager) {
		reciever = carriedReciever;
		this.midiFileManager = carriedMidiFileManager;
		newMetaEvents = new StoreMetaEvents(reciever);
		makeComponents = new CustomSwingComponents();

	}

	public void drawGUIContentHolder() throws InvalidMidiDataException, MidiUnavailableException, IOException {

		// Having the code seperate in this method affects the appearance in
		// windowsbuilder view
		/*
		 * screenSize = Toolkit.getDefaultToolkit().getScreenSize(); secondFrame
		 * = new JFrame("Midi File Player"); secondFrame.setBounds(0, 0, (int)
		 * screenSize.getWidth(), (int) screenSize.getHeight());
		 * secondFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * secondFrame.setContentPane(playerLayered);
		 * secondFrame.setVisible(true);
		 * secondFrame.setLocationRelativeTo(null); // might need to remove
		 * 
		 * // Content Holder JPanels/////////////////////////////////////// //
		 * Background image backgroundPanel.setLayout(null);
		 * backgroundPanel.setBounds(0, 0, (int) screenSize.getWidth(),(int)
		 * screenSize.getHeight());
		 * backgroundPanel.setBackground(Color.decode("#2F4F4F"));
		 * secondFrame.getContentPane().add(backgroundPanel);
		 * 
		 * 
		 * // Player content playerOptions.setBounds(810, 62, 481, 324);
		 * playerOptions.setBackground(Color.decode("#F0FFFF"));
		 * backgroundPanel.add(playerOptions);
		 * 
		 * // Visualiser content midiVisulizer.setBounds(40, 62, 700, 324);
		 * midiVisulizer.setBackground(Color.BLACK);
		 * backgroundPanel.add(midiVisulizer);
		 * 
		 * holdBar.repaint(); midiVisulizer.add(holdBar); drawMusicPlayerGUI();
		 */
		/////////////////////////////////////////////////////////////////////////////
		// Background image
		// backgroundPanel = makeComponents.createBasePanel(backgroundPanel, 0,
		///////////////////////////////////////////////////////////////////////////// 0,
		///////////////////////////////////////////////////////////////////////////// (int)
		///////////////////////////////////////////////////////////////////////////// screenSize.getWidth(),(int)
		///////////////////////////////////////////////////////////////////////////// screenSize.getHeight(),
		///////////////////////////////////////////////////////////////////////////// Color.decode("#2F4F4F"));
		// secondFrame.getContentPane().add(backgroundPanel);

		// Player content
		// backgroundPanel.add(makeComponents.createBasePanel(playerOptions,
		// 810, 62, 481, 324, Color.decode("#F0FFFF")));

		// Visualiser content
		// backgroundPanel.add(makeComponents.createBasePanel(midiVisulizer, 40,
		// 62, 700, 324, Color.BLACK));

		///////////////////////////////////////////////////////////////////////////
		// drawMusicPlayerGUI();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void drawMusicPlayerGUI() throws InvalidMidiDataException, MidiUnavailableException, IOException {

		// Remove this to make code smaller later on
		// ////////////////////////////////////////////////
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		secondFrame = new JFrame("Midi File Player");
		secondFrame.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		secondFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		secondFrame.setContentPane(playerLayered);
		secondFrame.setVisible(true);
		secondFrame.setLocationRelativeTo(null); // might need to remove

		// Content Holder JPanels///////////////////////////////////////
		// Background image
		backgroundPanel.setLayout(null);
		backgroundPanel.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		backgroundPanel.setBackground(Color.decode("#2F4F4F"));
		secondFrame.getContentPane().add(backgroundPanel);

		// Player content
		playerOptions.setBounds(810, 62, 481, 324);
		playerOptions.setBackground(Color.decode("#F0FFFF"));
		backgroundPanel.add(playerOptions);

		// Visualiser content
		midiVisulizer.setBounds(40, 62, 700, 324);
		midiVisulizer.setBackground(Color.BLACK);
		backgroundPanel.add(midiVisulizer);

		// holdBar.repaint();
		// midiVisulizer.add(holdBar);

		////////////////////////////////////////////////////////////////////////////////

		// Buttons///////////////////////////////

		// THIS ONE SHOWS UP IN WINDOWS VIEW BUILDER ONLY
		containsButtons.setBounds(0, line1, 180, btn_h);
		btnPrev.setText("<<");
		btnPrev.setName("prevFile");
		ActionListener prevSelectedFileActionListener = new MIDIFilePlayerInteractions(this, reciever, btnPrev);
		btnPlay.addActionListener(prevSelectedFileActionListener);
		MetaEventListener prevMetaListener = new MIDIFilePlayerInteractions(this, reciever, btnPrev);
		reciever.returnSequencer().addMetaEventListener(prevMetaListener);

		btnPlay.setText(">");
		btnPlay.setName("playFile");
		ActionListener playSelectedFileActionListener = new MIDIFilePlayerInteractions(this, reciever, btnPlay);
		btnPlay.addActionListener(playSelectedFileActionListener);
		MetaEventListener playMetaListener = new MIDIFilePlayerInteractions(this, reciever, btnPlay);
		reciever.returnSequencer().addMetaEventListener(playMetaListener);

		btnNext.setText(">>");
		btnNext.setName("nextFile");
		ActionListener nextSelectedFileActionListener = new MIDIFilePlayerInteractions(this, reciever, btnNext);
		btnPlay.addActionListener(nextSelectedFileActionListener);
		MetaEventListener nextMetaListener = new MIDIFilePlayerInteractions(this, reciever, btnNext);
		reciever.returnSequencer().addMetaEventListener(nextMetaListener);

		containsButtons.add(btnPrev);
		containsButtons.add(btnPlay);
		containsButtons.add(btnNext);
		playerOptions.add(containsButtons);

		///////////////////////////////////////////////////////////////////////////////
		// Optimized code
		/*
		 * containsButtons = makeComponents.createBasePanel(containsButtons, 0,
		 * line1, 180, btn_h, null); containsButtons.add(btnPrev =
		 * makeComponents.customJButton(btnPrev, "<<", 0, 0, 50, btn_h));
		 * containsButtons.add(makeComponents.customJButton(btnPlay, ">", 0, 0,
		 * 50, btn_h));
		 * containsButtons.add(makeComponents.customJButton(btnNext, ">>", 0, 0,
		 * 50, btn_h)); playerOptions.add(containsButtons);
		 */

		///////////////////////////////////////////////////////////////////////////////

		// Now Playing Panel
		panelNP.setBorder(BorderFactory.createMatteBorder(1, 0, 2, 0, Color.gray));
		panelNP.setBounds(5, line1 - 25, _W - 15, 20);
		panelNP.setLayout(new BoxLayout(panelNP, BoxLayout.PAGE_AXIS));

		lblplaying.setText("Now Playing: ");
		lblplaying.setBounds(5, 0, 100, 4);

		panelNP.add(lblplaying);
		playerOptions.add(panelNP);

		///////////////////////////////////////////////////////////////////////////////
		/*
		 * panelNP = makeComponents.customJPanelEditing(panelNP, _W, line1,
		 * BoxLayout.PAGE_AXIS, Color.gray); lblplaying =
		 * makeComponents.customJLabelEditing(lblplaying, "Now Playing: ", 5, 0,
		 * 100, 40); panelNP.add(lblplaying); playerOptions.add(panelNP);
		 */

		/////////////////////////////////////////////////////////////////////////////
		// Add file button and action event
		playerOptions.add(createSelectFileButton());

		// SongList ////////////////////////////////////////
		createJSongList();
		////////////////////////////////////////////////////

		// 2Row Buttons
		contBtns2.setBounds(0, line2, 220, 50);
		btnShSt.setText("STAT");
		btnShWf.setText("ShWf");
		btnShDi.setText("ShDi");

		contBtns2.add(btnShSt);
		contBtns2.add(btnShWf);
		contBtns2.add(btnShDi);
		playerOptions.add(contBtns2);

		///////////////////////////////////////////////////////////////////////////////
		// contBtns2.add(makeComponents.customJButton(btnShSt, "STAT", null,
		/////////////////////////////////////////////////////////////////////////////// null,
		/////////////////////////////////////////////////////////////////////////////// 200,
		/////////////////////////////////////////////////////////////////////////////// 200),-1);
		// contBtns2.add(makeComponents.customJButton(btnShWf, "ShWf", null,
		/////////////////////////////////////////////////////////////////////////////// null,
		/////////////////////////////////////////////////////////////////////////////// 200,
		/////////////////////////////////////////////////////////////////////////////// 200),-1);
		// contBtns2.add(makeComponents.customJButton(btnShDi, "ShDi", null,
		/////////////////////////////////////////////////////////////////////////////// null,
		/////////////////////////////////////////////////////////////////////////////// 200,
		/////////////////////////////////////////////////////////////////////////////// 200),-1);
		// playerOptions.add(contBtns2);
		///////////////////////////////////////////////////////////////////////////////

		// DelBtns
		btnDel.setBounds(_W - 55, line2 + 5, 45, 30);
		btnDel.setText("X");

		playerOptions.add(btnDel);

		// Labels song time
		contSlbl.setBounds(10, 15, _W - 20, 20);
		contSlbl.add(lblst);
		contSlbl.add(lblet);
		lblst.setText("00:00");
		lblst.setBorder(new EmptyBorder(0, 0, 0, 200));
		lblet.setText("00:00");
		playerOptions.add(contSlbl);

		SeekBar seekbar = new SeekBar();

		// SeekBar
		seekbar.setBounds(5, 10, _W - 15, 10);
		playerOptions.add(seekbar);

		/*
		 * int test = playerOptions.getComponentCount(); for (int i = 0; i <
		 * playerOptions.getComponentCount(); i++){
		 * System.out.println(playerOptions.getComponent(i).getClass()); if(i ==
		 * playerOptions.getComponentCount()){
		 * 
		 * } }
		 */
	}

	/*
	 * Display songs in JList panel from both the defined default directory
	 * //and add a new midi file to JList
	 */
	public void addAllMIDIFilesToJList() {
		midiFileManager.getSongList(songList);
	}

	public void storedFoundFile(File selectedFile) {
		this.retrieveFile = selectedFile;
		songList.addElement(retrieveFile.toString());
	}
	//////////////////////////////////////////////////////////////

	/* Store and retrives index of selected file to play */
	public void storedJListSelectedIndex(int carriedJListIndex) {
		this.JlistIndex = carriedJListIndex;
	}

	public int getListSelectedIndex() {
		return JlistIndex;
	}
	///////////////////////////////////////////////////////////

	public Sequence playSelectedFile() throws InvalidMidiDataException, IOException {
		storedMIDISavedFiles = midiFileManager.getFilesSongList();
		File matchingFile = storedMIDISavedFiles.get(JlistIndex);
		Sequence sequence = MidiSystem.getSequence(matchingFile);
		// Track test = sequence.getTracks()[0];
		return sequence;
	}

	public void playMidiFile() throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(midiFileManager.selectMIDIFile());
		reciever.returnSequencer().setSequence(sequence); // load it into //
															// sequencer
		reciever.returnSequencer().start(); // start the playback
	}

	public File getStoredFile() {
		return retrieveFile;
	}

	public void createJSongList() {
		jSongList.setBounds(0, line1 + 50, _W, h_list);
		jSongList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jSongList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jSongList.setVisibleRowCount(-1);

		// To get specifc mouse event only - might not use
		/*
		 * MouseAdapter jListSelectListener = new MouseAdapter (){
		 * 
		 * @Override public void mouseClicked(MouseEvent arg0) { Object obj =
		 * arg0.getSource();
		 * 
		 * // User clicks on JList options if (obj.equals(jSongList)) { int
		 * index = jSongList.locationToIndex(arg0.getPoint());
		 * storedJListSelectedIndex(index); //
		 * System.out.println("Double clicked on Item " + index); } } };
		 * jSongList.addMouseListener(jListSelectListener);
		 */

		MouseListener jListSelectListener = new MIDIFilePlayerInteractions(this, jSongList);
		jSongList.addMouseListener(jListSelectListener);
		//////////////////////////////////

		listScroller = new JScrollPane(jSongList);
		listScroller.setPreferredSize(new Dimension(_W - 10, h_list));
		listScroller.setBounds(0, line1 + 50, _W - 10, h_list);
		addAllMIDIFilesToJList();
		playerOptions.add(listScroller);
	}

	public JButton createSelectFileButton() {
		selectMidiFileButton = new JButton("Select File");
		selectMidiFileButton.setBounds(279, 5, 81, 23);
		selectMidiFileButton.setName("Select Midi File");
		ActionListener selectButtonActionListener = new MIDIFilePlayerInteractions(this, midiFileManager, reciever,
				selectMidiFileButton);
		selectMidiFileButton.addActionListener(selectButtonActionListener);
		return selectMidiFileButton;
	}

	public void generateMetaFromFile() throws InvalidMidiDataException {
		// int i = 0;
		newMetaEvents.createFullSequenceMetaData();
		Track metaTrack = newMetaEvents.getMetaTrack();
		boolean noteOn = false;
		boolean noteOff = false;
		// Messages
		// carriedMetaBytesEventMessages = newMetaEvents.getMetaMessageData();

		// Duration
		// carriedMetaTickEventMessages = newMetaEvents.getMetaTickData();
		// int getMessageNoteStatus =
		// metaTrack.get(0).getMessage().getMessage()[3];
		// int test = metaTrack.get(0).getMessage().getMessage()[0] & 0xFF &
		// 0xF0;

		// .getMessage().gett == 0x2F) {break;}

		for (int i = 0; i < metaTrack.size();) {
			
			/*
			 * Convert to meta message to find end of track meta message and
			 * leave loop when found
			 */
			MetaMessage findTrackEnd = (MetaMessage) metaTrack.get(i).getMessage();

			if (findTrackEnd.getType() == 0x2F) {
				break;
			}
			
			/*The negative value of the command/get status value from message gets changed into
			a negative version as a byte ranges from 1 to 128. The below code converts it to its numerical
			version from the original command/get status The [3] gets the status byte/ channel value of the midi message*/
			int statusByteToInt = metaTrack.get(i).getMessage().getMessage()[3] & 0xFF & 0xF0;

			switch (statusByteToInt) {
			case ShortMessage.NOTE_ON:
				 noteOn = true;
				startTick = (int) metaTrack.get(i++).getTick();
				//endTick = (int) metaTrack.get(++i).getTick();
				break;
			case ShortMessage.NOTE_OFF:
				 noteOff = true;
				//startTick = (int) metaTrack.get(i).getTick();
				endTick = (int) metaTrack.get(i++).getTick();
				break;
			case ShortMessage.PITCH_BEND:
				break;
			case ShortMessage.PROGRAM_CHANGE:
				break;
			case ShortMessage.CONTROL_CHANGE:
				break;
			default:
				break;
			}
			
		    if(noteOn == true && noteOff == true){
		    	//holdBar = new JPanel();
			    //holdBar.setBounds(0, 0, 0, 0);
		    	MIDIVisualPanel testPanel = new MIDIVisualPanel(startTick, 40, endTick, 50);
			    //holdBar.repaint(startTick, 40, endTick, 50);
				midiVisulizer.add(testPanel);
				noteOn = false;
				noteOff = false;
				
		    }
			
		}
	}

	
	/*
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.RED);
		g.fillRect(startTick, 40, endTick, 50);
		g.setColor(Color.BLACK);
		g.drawRect(startTick, 40, endTick, 10);
		//midiVisulizer.add(g);
		
		// g.fillRect(0, 40, 230, 10);
	}*/

}
