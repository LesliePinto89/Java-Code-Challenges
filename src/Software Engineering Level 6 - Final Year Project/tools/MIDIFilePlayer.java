package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.sound.midi.ControllerEventListener;
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
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import keyboard.KeyboardInteractions;
import keyboard.VirtualKeyboard;
import midi.MidiMessageTypes;
import midi.StoreMetaEvents;
import midiDevices.MidiReceiver;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MIDIFilePlayer implements MouseListener, MetaEventListener, ControllerEventListener {

	//Dimensions
	private int screenWidth = SwingComponents.getInstance().getScreenWidth();
	private int screenHeight = SwingComponents.getInstance().getScreenHeight();
	
	private DefaultListModel<String> songList = new DefaultListModel<String>();
	private JList<String> jSongList = new JList<String>(songList);
	private JScrollPane listScroller;

	//private ArrayList<Long> carriedMetaTickEventMessages = new ArrayList<Long>();
	//private MetaMessage[] carriedMetaBytesEventMessages = null;

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

	private JPanel backgroundPanel;
	private JPanel containsButtons = new JPanel();
	private JPanel playerOptions;
	private JPanel midiVisulizer = new JPanel();
	private JFrame secondFrame;
	private JPanel panelNP = new JPanel();
	private JPanel contBtns2 = new JPanel();
	//private JLayeredPane playerLayered = new JLayeredPane();
	private JPanel gridContentPane;
	private JPanel contSlbl = new JPanel();
	private Dimension screenSize;
	private JPanel holdBar;


	//Leave here untul maek classes singlton
	private StoreMetaEvents newMetaEvents;
	private SwingComponents components = SwingComponents.getInstance();
	private MIDIFileManager manager = MIDIFileManager.getInstance();
	private File retrieveFile;

	private int startTick = 0;
	private int endTick = 0;

	
	private static volatile MIDIFilePlayer instance = null;

    private MIDIFilePlayer() {}

    public static MIDIFilePlayer getInstance() {
        if (instance == null) {
            synchronized(MIDIFilePlayer.class) {
                if (instance == null) {
                    instance = new MIDIFilePlayer();
                    
               
                }
            }
        }

        return instance;
    }
    
    public void designPlayer(){
    	//gridContentPane = new JPanel(new GridBagLayout());
		//secondFrame = components.customJFrame("Midi File Player",screenWidth,screenHeight,gridContentPane,null);

		// Content Holder JPanels///////////////////////////////////////
    	backgroundPanel = components.generateEventPanel(screenWidth, screenHeight, null, Color.decode("#F0FFFF"), Color.decode("#F0FFFF"), 1,1,1,1);
		backgroundPanel = new JPanel(new GridBagLayout());
		
		
		//gridContentPane.add(backgroundPanel);

		// Player content
		playerOptions = components.generateEventPanel(screenWidth/3, 324,null,Color.decode("#F0FFFF"),Color.decode("#F0FFFF"),1,1,1,1);
		backgroundPanel.add(playerOptions);

		// Visualiser content
		midiVisulizer = components.generateEventPanel(screenWidth/2, 324,null,Color.BLACK,Color.WHITE,1,1,1,1);
		backgroundPanel.add(midiVisulizer);
	

    }
    
    public void designVisualiser(){
    	midiVisulizer = components.generateEventPanel(700, 324,null,Color.BLACK,Color.WHITE,1,1,1,1);
    	backgroundPanel.add(midiVisulizer);
    }
	
    public void drawActionsButtons(){
		btnPrev =components.customTrackJButton(60,40,"<<","prevFile",this);
		btnPlay =components.customTrackJButton(60,40,">","playFile",this);
		btnNext =components.customTrackJButton(60,40,">>","nextFile",this);	
		containsButtons = components.generateEventPanel(180, btn_h, null,null,Color.BLACK,0,0,0,0); 
		containsButtons.add(btnPrev);
		containsButtons.add(btnPlay);
		containsButtons.add(btnNext);
		playerOptions.add(containsButtons);
		
    }
    
	public JPanel drawMusicPlayerGUI(){
		
		// holdBar.repaint();
		// midiVisulizer.add(holdBar);
		designPlayer();
		drawActionsButtons();
		
		panelNP = components.generateEventPanel(_W - 15, 20, null, null,Color.gray,1, 0, 2, 0);
		panelNP.setLayout(new BoxLayout(panelNP, BoxLayout.PAGE_AXIS));
		playerOptions.add(panelNP);

		lblplaying = components.customJLabelEditing("Now Playing: ",100, 4);
		panelNP.add(lblplaying);
		
		// Add file button and action event
		playerOptions.add(createSelectFileButton());

		// SongList ////////////////////////////////////////
		createJSongList();
		////////////////////////////////////////////////////

		// 2Row Buttons/////////////////////////////////////
		btnShSt.setText("STAT");
		btnShWf.setText("ShWf");
		btnShDi.setText("ShDi");
		
		contBtns2 = components.generateEventPanel(220, 50,null,null,null,0,0,0,0);
		contBtns2.add(btnShSt);
		contBtns2.add(btnShWf);
		contBtns2.add(btnShDi);
		
		playerOptions.add(contBtns2);
		
		// DelBtns
		btnDel = components.customJButton( 45, 30,"X",this);
		playerOptions.add(btnDel);

		// Labels song time
		contBtns2 = components.generateEventPanel(_W - 20, 20,null,null,null,0,0,0,0);
		contSlbl.add(lblst);
		contSlbl.add(lblet);
		lblet = components.customJLabelEditing("00:00",30,30);
		lblst.setBorder(new EmptyBorder(0, 0, 0, 200));
		playerOptions.add(contSlbl);

		SeekBar seekbar = new SeekBar();
		// SeekBar
		//seekbar.setBounds(5, 10, _W - 15, 10);
		seekbar.setPreferredSize(new Dimension(_W - 15, 10));
		seekbar.setMinimumSize(new Dimension(_W - 15, 10));
		playerOptions.add(seekbar);

		return backgroundPanel;
	}

	//When the user clicks "select file" button, its adds the file to the songlist
	public void storedFoundFile(File selectedFile) {
		if (selectedFile !=null) {
		this.retrieveFile = selectedFile;
		songList.addElement(retrieveFile.toString());
		}
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
		storedMIDISavedFiles = MIDIFileManager.getInstance().getFilesSongList();
		File matchingFile = storedMIDISavedFiles.get(getListSelectedIndex());
		Sequence sequence = MidiSystem.getSequence(matchingFile);
		// Track test = sequence.getTracks()[0];
		return sequence;
	}

	public void playMidiFile() throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(MIDIFileManager.getInstance().selectMIDIFile());
		MidiReceiver.getInstance().returnSequencer().setSequence(sequence); // load it into //
															// sequencer
		MidiReceiver.getInstance().returnSequencer().start(); // start the playback
	}

	public File getStoredFile() {
		return retrieveFile;
	}

	public void createJSongList() {
		songList = manager.buildSongList();
		jSongList = components.customJList(_W, h_list, this);
		jSongList.setModel(songList);
		listScroller = components.customJScrollPane(jSongList,_W - 10, h_list);
		playerOptions.add(listScroller);	
	}

	public JButton createSelectFileButton() {
		selectMidiFileButton =components.customTrackJButton(81, 23,"Select File","SelectMidiFile",this);
		return selectMidiFileButton;
	}

	public void generateMetaFromFile() throws InvalidMidiDataException {
		// int i = 0;
		
		//If .mid file already has a track for meta messages, this code below is not needed
		//newMetaEvents.createFullSequenceMetaData();
		Track metaTrack = newMetaEvents.getMetaTrack();
		//////////////////////////////////////////////////////////
		
		
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
			
			/*The value of the command/get status value from message gets changed into
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
		    	int notePosition = (int) metaTrack.get(i++).getMessage().getMessage()[1];
		    	//holdBar = new JPanel();
			    //holdBar.setBounds(0, 0, 0, 0);
		    	MIDIVisualPanel testPanel = new MIDIVisualPanel(startTick, notePosition, endTick, 10);
			    //holdBar.repaint(startTick, 40, endTick, 50);
				midiVisulizer.add(testPanel);
				noteOn = false;
				noteOff = false;
				
		    }
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent music) {
		Object obj = music.getSource();
		if (obj.equals(jSongList)) {
			int index = jSongList.locationToIndex(music.getPoint());
			MIDIFilePlayer.getInstance().storedJListSelectedIndex(index);
		}
		
		else if (obj.equals(selectMidiFileButton)) {
			File desiredFile = MIDIFileManager.getInstance().selectMIDIFile();
			MIDIFilePlayer.getInstance().storedFoundFile(desiredFile);
		}

		else if (obj.equals(btnPrev)) {

			// System.out.println("Double clicked on Item " + index);

			// File desiredFile = fileManager.selectMIDIFile();
			// player.storedFoundFile(desiredFile);
		}

		else if (obj.equals(btnPlay)) {
			playFeature();
		}

		else if (obj.equals(btnNext)) {
			int index = MIDIFilePlayer.getInstance().getListSelectedIndex();

			MIDIFilePlayer.getInstance().storedJListSelectedIndex(index++);
			playFeature();
		}
		
	}
	
	public void playFeature() {
		try {

			if (MidiReceiver.getInstance().isRunning() == true) {
				MidiReceiver.getInstance().returnSequencer().stop();
				btnPlay.setText(">");
				PlaybackFunctions.resetChordsColor();
				PlaybackFunctions.emptyNotes();
			}

			else {
				btnPlay.setText("||");
				// Replace sequence with file in this feature
				
				MidiReceiver.getInstance().returnSequencer().addMetaEventListener(this);
				
				int[] types = new int[128];
		        for (int ii = 0; ii < 128; ii++) {
		            types[ii] = ii;
		        }
		        MidiReceiver.getInstance().returnSequencer().addControllerEventListener(this, types);
		        
		        Sequence original = MIDIFilePlayer.getInstance().playSelectedFile();
		        MidiReceiver.getInstance().returnSequencer().setSequence(original);
		        Track [] tracks = original.getTracks();
	            Track trk = MidiReceiver.getInstance().returnSequencer().getSequence().createTrack();
	            for (Track track : tracks) {
	            	StoreMetaEvents.generateMetaData(track, trk);
	            }
		        
	            Sequence updatedSeq = MidiReceiver.getInstance().returnSequencer().getSequence();
				MidiReceiver.getInstance().returnSequencer().setSequence(updatedSeq);
			    
				//If by some chance the .mid file does not have meta data, or is a MIDI 1 multi-track file
				//running this function below with provide meta data 
				//player.generateMetaFromFile();
			    MidiReceiver.getInstance().returnSequencer().setTickPosition(0);
			    MidiReceiver.getInstance().returnSequencer().start();
			}

		} catch (InvalidMidiDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void meta(MetaMessage metaPlayer) {
		
		
		 MidiMessageTypes.getInstance().metaEventColors(metaPlayer);
		 
		//0x2F in decimal is  47 - value for end MIDI track
		if (metaPlayer.getType() == 0x2F) {
			MidiReceiver.getInstance().returnSequencer().stop();
			btnPlay.setText(">");
		}
	}

	@Override
	public void controlChange(ShortMessage event) {
		 int command = event.getCommand();
       if (command == ShortMessage.NOTE_ON) {
           System.out.println("CEL - note on!");
       } else if (command == ShortMessage.NOTE_OFF) {
           System.out.println("CEL - note off!");
       } else {
    	   
          // System.out.println("CEL - unknown: " + command);
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
