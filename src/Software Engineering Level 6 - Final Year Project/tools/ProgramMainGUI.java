package tools;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import midiDevices.GetInstruments;
import keyboard.VirtualKeyboard;
import midi.DurationTimer;
import midi.ListOfChords;
import midi.MidiMessageTypes;
import midiDevices.MidiReceiver;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.ComponentOrientation;

public class ProgramMainGUI implements MouseListener {

	protected JFrame frame;
	protected JPanel backgroundImagePanel = new JPanel();
	private JPanel freePlayPanel = new JPanel();
	private JPanel midiPlayer = new JPanel();
	private JPanel learnMode = new JPanel();
	protected int screenWidth;
	protected int screenHeight;
	protected Dimension screenSize;

	private JPanel contentPane = new JPanel(new GridBagLayout());
	private JPanel centerPane = new JPanel(new GridBagLayout());
	private JPanel topBlockPane = new JPanel();
	private JPanel bottomBlockPane = new JPanel();
	private JPanel leftBlockPane = new JPanel();
	private JPanel rightBlockPane = new JPanel();
	private boolean startup = true;
	private GridBagConstraints outerFrameGUIConstraints = new GridBagConstraints();
	private SwingComponents components = SwingComponents.getInstance();

	private static volatile ProgramMainGUI instance = null;

	private ProgramMainGUI() {
	}

	public static ProgramMainGUI getInstance() {
		if (instance == null) {
			synchronized (ProgramMainGUI.class) {
				if (instance == null) {
					instance = new ProgramMainGUI();
					instance.loadApplication();
				}
			}
		}
		return instance;
	}

	public void loadProgramWindowFrameGUI() throws IOException {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();

		frame = new JFrame("Midi Keyboard: Welcome Screen");
		frame.setPreferredSize(new Dimension(screenWidth, screenHeight));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void loadApplication() {

		try{
		loadProgramWindowFrameGUI();
		loadProgramOptions();
		
		if (startup == true) {

			// Each of these classes use the Singleton pattern as the
			// application only needs one instance of them for reference.
			MidiReceiver.getInstance();
			MidiReceiver.getInstance().startConnection();
			DurationTimer.getInstance();
			MIDIFileManager.getInstance();
			MIDIFilePlayer.getInstance();
			MidiMessageTypes.getInstance();
			GetInstruments.getInstance();
			Metronome.getInstance();
			ScreenPrompt.getInstance();

			// Load all notes for set piano (e.g. 61, 88) on system startup
			VirtualKeyboard.getInstance().createWholeKeys();
			VirtualKeyboard.getInstance().createSharpKeys();
			VirtualKeyboard.getInstance().freePlayOrMakeTrack();
			////////////////////////////////////////////////////////////

			ListOfChords listInstance = ListOfChords.getInstance();
			listInstance.setAllKeyNotes();
			listInstance.loadMajorChords(listInstance.getAllKeyNotes());
			listInstance.loadMinorChords(listInstance.getAllKeyNotes());
			listInstance.loadHalfDimishedChords(listInstance.getAllKeyNotes());
			listInstance.loadFullyDiminishedScaleChords(listInstance.getAllKeyNotes());
			
			startup = false;
			// ListOfChords.getInstance().setAllKeyNotes(); //Load all chords
			// for all keys major/minor scales
		}
		
		} catch (InvalidMidiDataException | MidiUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		

	}

	// Main features
	public void loadProgramOptions() throws IOException {
		createGUIBorder();
		createFeatureImages();

	}


	public void createGUIBorder() throws IOException {
//		topBlockPane = components.generateEventPanel(screenWidth, screenHeight / 6, null, Color.decode("#181818"),
//				Color.decode("#008080"), 0, 0, 2, 0);
//		leftBlockPane = components.generateEventPanel(screenWidth / 12, screenHeight / 2, null, Color.decode("#181818"),
//				Color.decode("#008000"), 0, 0, 2, 0);
//		rightBlockPane = components.generateEventPanel(screenWidth / 12, screenHeight / 2, null,
//				Color.decode("#181818"), Color.decode("#008000"), 0, 0, 2, 0);
//		bottomBlockPane = components.generateEventPanel(screenWidth, screenHeight / 6, null, Color.decode("#181818"),
//				Color.decode("#008080"), 0, 0, 2, 0); 
		BufferedImage topBarImage = ImageIO.read(new File("src/Images/TopBar.png"));
		BufferedImage bottomBarImage = ImageIO.read(new File("src/Images/BottomBar.png"));
		
		topBlockPane = components.guiBorderPanel(topBarImage,screenWidth, screenHeight / 6, 
				Color.decode("#008080"), 0, 0, 2, 0);
		leftBlockPane = components.generateEventPanel(screenWidth / 12, screenHeight / 2,null,
				Color.decode("#181818"),Color.decode("#008000"), 0, 0, 2, 0);
		rightBlockPane = components.generateEventPanel(screenWidth / 12, screenHeight / 2, null,
				Color.decode("#181818"), Color.decode("#008000"), 0, 0, 2, 0);
		
		bottomBlockPane = components.guiBorderPanel(bottomBarImage,screenWidth, screenHeight / 6, Color.decode("#008080"), 0, 0, 2, 0); 
		bottomBlockPane.setBackground(Color.decode("#008000"));
		////////////////////////////////////////////////////////////////////////////////////////////

		centerPane.setPreferredSize(
				new Dimension(screenWidth / 2 + screenWidth / 4, screenHeight / 2 + screenHeight / 3));
		centerPane
				.setMinimumSize(new Dimension(screenWidth / 2 + screenWidth / 4, screenHeight / 2 + screenHeight / 3));
		centerPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		centerPane.setBackground(Color.decode("#696969"));
		centerPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.decode("#404040")));

		//Color c=new Color(1f,0f,0f,.5f );
		//contentPane.setBackground(Color.decode("#181818"));
		//centerPane.setBackground(Color.decode("#181818"));
		
		outerFrameGUIConstraints = components.conditionalConstraints(1, 0, 0, 0, GridBagConstraints.PAGE_START,
				GridBagConstraints.HORIZONTAL);
		contentPane.add(topBlockPane, outerFrameGUIConstraints);

		outerFrameGUIConstraints = components.conditionalConstraints(0, 0, 0, 1, GridBagConstraints.LINE_START,
				GridBagConstraints.VERTICAL);
		//The negative value is used as the panels use images, which removes original aligned  panel
		outerFrameGUIConstraints.insets = new Insets(0, 0, -5, 0);
		contentPane.add(leftBlockPane, outerFrameGUIConstraints);

		// Create feature GUI
		outerFrameGUIConstraints = components.conditionalConstraints(1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.NONE);
		outerFrameGUIConstraints.insets = new Insets(10, 0, 10, 0);
		contentPane.add(centerPane, outerFrameGUIConstraints);

		outerFrameGUIConstraints = components.conditionalConstraints(0, 0, 0, 1, GridBagConstraints.LINE_END,
				GridBagConstraints.VERTICAL);
		outerFrameGUIConstraints.insets = new Insets(0, 0, -5, 0);
		contentPane.add(rightBlockPane, outerFrameGUIConstraints);

		outerFrameGUIConstraints = components.conditionalConstraints(1, 0, 0, 2, GridBagConstraints.PAGE_END,
				GridBagConstraints.HORIZONTAL);
		outerFrameGUIConstraints.insets = new Insets(0, 0, 0, 0);
		contentPane.add(bottomBlockPane, outerFrameGUIConstraints);
	}

	public void createFeatureImages() throws IOException {
		BufferedImage bufFreePlayPianoImage = ImageIO.read(new File("src/Images/piano-image.jpg"));
		freePlayPanel = components.customizeFeaturePanel(screenWidth / 4, screenHeight / 2, this,
				bufFreePlayPianoImage);
		freePlayPanel.setBackground(Color.decode("#181818"));
		
		BufferedImage bufLearnImage = ImageIO.read(new File("src/Images/Music score.jpg"));
		learnMode = components.customizeFeaturePanel(screenWidth / 4, screenHeight / 2, this, bufLearnImage);
		learnMode.setBackground(Color.decode("#181818"));
		
		BufferedImage bufMidiPlayerImage = ImageIO.read(new File("src/Images/MusicStation.jpg"));
		midiPlayer = components.customizeFeaturePanel(screenWidth / 4, screenHeight / 2, this, bufMidiPlayerImage);
		midiPlayer.setBackground(Color.decode("#181818"));
		
		GridBagConstraints styleGUI = new GridBagConstraints();

		styleGUI.weightx = 1;
		styleGUI.weighty = 1;
		styleGUI.gridx = 0;
		styleGUI.gridy = 0;
		centerPane.add(freePlayPanel, styleGUI);

		styleGUI.gridx = 1;
		centerPane.add(learnMode, styleGUI);

		styleGUI.gridx = 0;
		styleGUI.gridy = 1;
		centerPane.add(midiPlayer, styleGUI);
	}

	public void designCenterGUI() {
		GridBagConstraints styleGUI = new GridBagConstraints();

		styleGUI.weightx = 1;
		styleGUI.weighty = 1;
		styleGUI.gridx = 0;
		styleGUI.gridy = 0;
		centerPane.add(freePlayPanel, styleGUI);

		styleGUI.gridx = 1;

		centerPane.add(learnMode, styleGUI);

		styleGUI.gridx = 0;
		styleGUI.gridy = 1;

		centerPane.add(midiPlayer, styleGUI);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {			
					ProgramMainGUI.getInstance();
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();

		if (obj.equals(freePlayPanel)) {
			try {
				frame.setVisible(false);
				VirtualKeyboard.getInstance().createVirtualKeyboard(false);
			} catch (InvalidMidiDataException | MidiUnavailableException | IOException e1) {
				e1.printStackTrace();
			}
		}

		else if (obj.equals(learnMode)) {
			try {
				frame.setVisible(false);
				VirtualKeyboard.getInstance().createVirtualKeyboard(true);
			} catch (InvalidMidiDataException | MidiUnavailableException | IOException e1) {
				e1.printStackTrace();
			}
		}

		else if (obj.equals(midiPlayer)) {
			try {
				frame.setVisible(false);
				MIDIFilePlayer.getInstance().drawMusicPlayerGUI();

			} catch (InvalidMidiDataException | MidiUnavailableException | IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
