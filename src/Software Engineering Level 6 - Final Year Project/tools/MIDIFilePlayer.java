package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import midi.MidiMessageTypes;
import midiDevices.PlayBackDevices;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MIDIFilePlayer implements MouseListener, MetaEventListener, ControllerEventListener {

	//Dimensions
	private int screenWidth = SwingComponents.getInstance().getScreenWidth();
	private int screenHeight = SwingComponents.getInstance().getScreenHeight();
	
	//Variables
	private ArrayList<File> storedMIDISavedFiles = new ArrayList<File>();
	private int JlistIndex;
	private int btn_h = 35;
	private int _W = 330;
	private int h_list = 100;
	private File retrieveFile;
	private int startTick = 0;
	private int endTick = 0;
	
	//Components
	private DefaultListModel<String> songList = new DefaultListModel<String>();
	private JList<String> jSongList = new JList<String>(songList);
	private JScrollPane listScroller;
	private JButton btnPlay = new JButton();
	private JButton btnNext = new JButton();
	private JButton btnPrev = new JButton();
	private JButton selectMidiFileButton;
	private JLabel lblplaying = new JLabel();
	private JPanel backgroundPanel;
	private JPanel containsButtons = new JPanel();
	private JPanel playerOptions;
	private JPanel midiVisulizer = new JPanel();
	private JPanel panelNP = new JPanel();

	private SwingComponents components = SwingComponents.getInstance();
	private MIDIFileManager manager = MIDIFileManager.getInstance();

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
		// Content Holder JPanels///////////////////////////////////////
    	backgroundPanel = components.generateEventPanel(screenWidth, screenHeight, null, Color.decode("#F0FFFF"), Color.decode("#F0FFFF"), 1,1,1,1);
		backgroundPanel = new JPanel(new GridBagLayout());                                                 
		playerOptions = components.generateEventPanel(screenWidth/3, 324,null,Color.decode("#6495ED"),Color.decode("#6495ED"),1,1,1,1);
		backgroundPanel.add(playerOptions);                                   
    }
    
    public void designVisualiser(){
    	midiVisulizer = components.generateEventPanel(700, 324,null,Color.BLACK,Color.WHITE,1,1,1,1);
    	backgroundPanel.add(midiVisulizer);
    }
	
    public void drawActionsButtons(){
		btnPrev =components.customTrackJButton(60,40,"<<","prevFile",this);
		btnPrev.setForeground(Color.WHITE);
		btnPrev.setBackground(Color.decode("#33CCFF"));
		btnPrev.setFont(new Font("Cooper Black", Font.BOLD, 18));
		btnPlay =components.customTrackJButton(60,40,">","playFile",this);
		btnPlay.setForeground(Color.WHITE);
		btnPlay.setBackground(Color.decode("#33CCFF"));
		btnPlay.setFont(new Font("Cooper Black", Font.BOLD, 18));
		btnNext =components.customTrackJButton(60,40,">>","nextFile",this);	
		btnNext.setForeground(Color.WHITE);
		btnNext.setBackground(Color.decode("#33CCFF"));
		btnNext.setFont(new Font("Cooper Black", Font.BOLD, 18));
		
		containsButtons = components.generateEventPanel(480, btn_h, null,null,Color.BLACK,0,0,0,0); 
		containsButtons.add(btnPrev);
		containsButtons.add(btnPlay);
		containsButtons.add(btnNext);
		playerOptions.add(containsButtons);
		
    }
    
	public JPanel drawMusicPlayerGUI(){
		designPlayer();
		drawActionsButtons();
		
		panelNP = components.generateEventPanel(_W - 15, 20, null, null,Color.gray,1, 0, 2, 0);
		panelNP.setLayout(new BoxLayout(panelNP, BoxLayout.PAGE_AXIS));
		playerOptions.add(panelNP);
		lblplaying = components.customJLabelEditing("Now Playing: ",100, 4);
		lblplaying.setForeground(Color.WHITE);
		lblplaying.setFont(new Font("Serif", Font.BOLD, 18));
		panelNP.add(lblplaying);
		
		// Add file button and action event
		playerOptions.add(createSelectFileButton());

		// SongList ////////////////////////////////////////
		createJSongList();
		////////////////////////////////////////////////////
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

	/* Store and retrieves index of selected file to play */
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
		return sequence;
	}

	public void playMidiFile() throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(MIDIFileManager.getInstance().selectMIDIFile());
		// Load it into  sequencer start the play back
		PlayBackDevices.getInstance().returnSequencer().setSequence(sequence); 
		PlayBackDevices.getInstance().returnSequencer().start(); 
	}

	public File getStoredFile() {
		return retrieveFile;
	}

	public void createJSongList() {
		songList = manager.buildSongList();
		jSongList = components.customJList(_W, h_list, this);
		jSongList.setModel(songList);
		jSongList.setName("allSongsList");
		jSongList.setForeground(Color.WHITE);
		SwingComponents.getInstance().colourFeatureTab(jSongList, Color.decode("#33CCFF"));
		listScroller = components.customJScrollPane(jSongList,_W - 10, screenHeight/2 - 160);
		playerOptions.add(listScroller);	
	}

	public JButton createSelectFileButton() {
		selectMidiFileButton =components.customTrackJButton(81, 23,"Select File","SelectMidiFile",this);
		selectMidiFileButton.setForeground(Color.WHITE);
		selectMidiFileButton.setBackground(Color.decode("#33CCFF"));
		return selectMidiFileButton;
		
	}

	public void generateMetaFromFile() throws InvalidMidiDataException {
		//If .mid file already has a track for meta messages, this code below is not needed
		//newMetaEvents.createFullSequenceMetaData();
		Track metaTrack = MidiMessageTypes.getInstance().getMetaTrack();
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
				break;
			case ShortMessage.NOTE_OFF:
				 noteOff = true;
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
		    	MIDIVisualPanel testPanel = new MIDIVisualPanel(startTick, notePosition, endTick, 10);
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
			storedFoundFile(desiredFile);
		}

		else if (obj.equals(btnPrev)) {
			int index = MIDIFilePlayer.getInstance().getListSelectedIndex();
			if(index ==0){index = songList.size();}
			storedJListSelectedIndex(--index);
			updateCurrentPlaying(index);
			playFeature();
		}

		else if (obj.equals(btnPlay)) {
			int index = MIDIFilePlayer.getInstance().getListSelectedIndex();
			updateCurrentPlaying(index);
			playFeature();
		}

		else if (obj.equals(btnNext)) {
			int index = MIDIFilePlayer.getInstance().getListSelectedIndex();
			if(index ==songList.size()){index = 0;}
			storedJListSelectedIndex(++index);
			updateCurrentPlaying(index);
			playFeature();
		}
		
	}
	
	public void updateCurrentPlaying(int index){
		String playing = songList.get(index);
		panelNP.removeAll();
		lblplaying = components.customJLabelEditing("Now Playing: "+ playing,100, 4);
		lblplaying.setForeground(Color.WHITE);
		lblplaying.setFont(new Font("Serif", Font.BOLD, 18));
		panelNP.add(lblplaying);
		panelNP.validate();
		panelNP.repaint();
	}
	
	public void playFeature() {
		try {

			if (PlayBackDevices.getInstance().isRunning() == true) {
				PlayBackDevices.getInstance().returnSequencer().stop();
				btnPlay.setText(">");
				PlaybackFunctions.resetChordsColor();
				PlaybackFunctions.emptyNotes();
			}

			else {
				btnPlay.setText("||");
				PlayBackDevices.getInstance().returnSequencer().addMetaEventListener(this);
				
				int[] types = new int[128];
		        for (int ii = 0; ii < 128; ii++) {
		            types[ii] = ii;
		        }
		        PlayBackDevices.getInstance().returnSequencer().addControllerEventListener(this, types);
		        
		         // Replace sequence with file in this feature
		        Sequence original = MIDIFilePlayer.getInstance().playSelectedFile();
		        PlayBackDevices.getInstance().returnSequencer().setSequence(original);
		        Track [] tracks = original.getTracks();
	            Track trk = PlayBackDevices.getInstance().returnSequencer().getSequence().createTrack();
	            for (Track track : tracks) {
	            	MidiMessageTypes.generateMetaData(track, trk);
	            }
	            
	            Sequence updatedSeq = PlayBackDevices.getInstance().returnSequencer().getSequence();
				PlayBackDevices.getInstance().returnSequencer().setSequence(updatedSeq);
			    PlayBackDevices.getInstance().returnSequencer().setTickPosition(0);
			    PlayBackDevices.getInstance().returnSequencer().start();
			}

		} catch (InvalidMidiDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	public void meta(MetaMessage metaPlayer) {
		 MidiMessageTypes.getInstance().metaEventColors(metaPlayer);
		//0x2F in decimal is  47 - value for end MIDI track
		if (metaPlayer.getType() == 0x2F) {
			PlayBackDevices.getInstance().returnSequencer().stop();
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
