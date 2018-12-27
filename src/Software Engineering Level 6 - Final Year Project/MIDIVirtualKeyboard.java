import javax.swing.*;
import javax.imageio.ImageIO;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * This class displays the piano software to the user, and acts as a base to the
 * class that constructs MIDI track files.
 * 
 */
public class MIDIVirtualKeyboard {

	private String[] storeInstrumentsNames;
	private JFrame frame;
	private JLayeredPane layered;
	private JTabbedPane tabbedPane;
	private JLayeredPane pianoLayers;
	private JPanel pianoBacking;
	private JSlider slider;
	private BufferedImage image;
	private static MIDIVirtualKeyboard midiGui;

	// ActionEvent Conditions
	private boolean stopRec = true;
	private boolean stopFreePlay = false;
	private long startTick = 0;
	private JComboBox<String> instrumentList;

	private Sequence sequence;
	private Track track;

	private ArrayList<JButton> naturalKeys = new ArrayList<JButton>();

	private int octave = 2;
	private int wholeNotePositionstart = 22;
	private int sharpNotePositionstart = 42;
	private ArrayList<Byte> storedMidiBytes = new ArrayList<Byte>();
	private LoadMIDISupport loadMIDI;

	/**
	 * Create the application with the MIDI devices loaded in memory
	 * 
	 * @param loadMIDI
	 *            - The MIDI devices needed in the application
	 */
	public MIDIVirtualKeyboard(LoadMIDISupport loadMIDI) {
		this.loadMIDI = loadMIDI;
	}

	public MIDIVirtualKeyboard (){}
	
	public static int convertToPitch(String note) {
		String sym = "";
		int oct = 0;
		String[][] notes = { { "C" }, { "Db", "C#" }, { "D" }, { "Eb", "D#" }, { "E" }, { "F" }, { "Gb", "F#" },
				{ "G" }, { "Ab", "G#" }, { "A" }, { "Bb", "A#" }, { "B" } };

		char[] splitNote = note.toCharArray();

		// If the length is two, then grab the symbol and number.
		// Otherwise, it must be a two-char note.
		if (splitNote.length == 2) {
			sym += splitNote[0];
			oct = splitNote[1];
		} else if (splitNote.length == 3) {
			sym += Character.toString(splitNote[0]);
			sym += Character.toString(splitNote[1]);
			oct = splitNote[2];
		}

		// Find the corresponding note in the array.
		for (int i = 0; i < notes.length; i++)
			for (int j = 0; j < notes[i].length; j++) {
				if (notes[i][j].equals(sym)) {
					return Character.getNumericValue(oct + 1) * 12 + i;
				}
			}

		// If nothing was found, we return -1.
		return -1;
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
		octave = 1;
		EnumSet<Note.SharpNoteType> sharpKeysEnums = EnumSet.allOf(Note.SharpNoteType.class);
		while (octave < 6) {
			for (Note.SharpNoteType getNote : sharpKeysEnums) {
				if (getNote.getSharp().equals("C#")) {
					octave++;
				}
				JButton button = new JButton(getNote.getSharp() + Integer.toString(octave));
				if (button.getText().startsWith("C") && octave >= 3) {
					sharpNotePositionstart += 30;
				}

				else if (button.getText().startsWith("F")) {
					sharpNotePositionstart += 32;
				}
				button.setBounds(sharpNotePositionstart, 145, 20, 126);
				button.setFont(new Font("Arial", Font.PLAIN, 6));
				button.setVerticalAlignment(SwingConstants.BOTTOM);
				button.setMargin(new Insets(1, 1, 1, 1));

				styleKeys(button);
				storeButtons(button);
				sharpNotePositionstart += 37;
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
				JButton button = new JButton(getNote.toString() + Integer.toString(octave));
				button.setBounds(wholeNotePositionstart, 145, 35, 196);
				button.setFont(new Font("Arial", Font.PLAIN, 10));
				button.setVerticalAlignment(SwingConstants.BOTTOM);
				button.setMargin(new Insets(1, 1, 1, 1));

				// Fixed octave number matches 61 notes keyboard
				if (getNote.toString().equals("C") && octave == 7) {
					styleKeys(button);
					storeButtons(button);
					endOctave = true;
					break;
				}
				styleKeys(button);
				storeButtons(button);

				wholeNotePositionstart += 35;
				if (getNote.toString().equals("B")) {
					octave++;
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
	public String[] allInstruments(Instrument[] channels) {
		String[] tempStorage = new String[channels.length];
		for (int i = 0; i < channels.length; i++) {
			tempStorage[i] = channels[i].getName();
		}
		return tempStorage;
	}

	public void storeButtons(JButton noteButton) {
		naturalKeys.add(noteButton);
	}

	public ArrayList<JButton> getButtons() {
		return naturalKeys;
	}

	/**
	 * Adjust all notes volume based on slider value.
	 */
	public void volumeToggle() {
		slider = new JSlider(0, 100, 71);
		slider.setBounds(200, 360, 200, 50);
		slider.setBackground(Color.BLACK);
		InteractionEventsInterface volumeSliderListener = new InteractionEventsInterface(slider, midiGui,loadMIDI.getMidiChannel());
		slider.addChangeListener(volumeSliderListener);
		layered.add(slider);
	}


	public void updateSoundBytes(Byte soundByte) {
		storedMidiBytes.add(soundByte);
	}

	public ArrayList<Byte> getSoundBytes() {
		return storedMidiBytes;
	}

	public void playButton() {

		JButton playMIDI = new JButton("Play");
		playMIDI.setBounds(frame.getWidth() / 3 + 130, frame.getHeight() / 2 - 20, 100, 42);
		layered.add(playMIDI);	
		ActionListener playButtonActionListener = new InteractionEventsInterface(midiGui,playMIDI);
		playMIDI.addActionListener(playButtonActionListener);
	}

	
	public void storeSeq(Sequence sequence){
		this.sequence = sequence;
	}
	
	public Sequence getSeq(){
		return sequence;
	}
	public void recordButton() throws InvalidMidiDataException {
		sequence = new Sequence(Sequence.PPQ, 4);
		storeSeq(sequence);
		track = sequence.createTrack();
		loadMIDI.returnSequencer().setSequence(sequence);
		loadMIDI.returnSequencer().setTempoInBPM(120);
		loadMIDI.returnSequencer().recordEnable(track, 0);

		JButton recordMIDI = new JButton("Record");
		recordMIDI.setBounds(frame.getWidth() / 3, frame.getHeight() / 2 - 20, 100, 42);
		
		layered.add(recordMIDI);
		ActionListener recordButtonActionListener = new InteractionEventsInterface(recordMIDI,midiGui);
		recordMIDI.addActionListener(recordButtonActionListener);
	}

	// update action event play's condition to play when recording ended
	public void endRecording(boolean stopRec) {
		this.stopRec = stopRec;
	}
	
	public boolean isRecEnded(){
		return stopRec;
	}

	public void endFreePlay(boolean stopFreePlay) {
		this.stopFreePlay = stopFreePlay;
	}

	public boolean isFreePlayEnded(){
		return stopFreePlay;
	}
	
	public void storeTrack(Track track){
		this.track = track;
	}
	
	public Track getTrack(){
		return track;
	}
	
	public Sequencer getSequencer(){
		return loadMIDI.returnSequencer();
	}
	
	public void setStartTick(long startTick){
		this.startTick = startTick;
	}
	
	public long getStartTick(){
		return startTick;
	}
	
	public void freePlayOrMakeTrack() throws InvalidMidiDataException, MidiUnavailableException {
		ArrayList<JButton> retrievedList = getButtons();
		int resolution = sequence.getResolution();
		int ticksPerSecond = resolution * (120 / 60);
		//int tickSize = 1 / ticksPerSecond;

		for (int i = 0; i < retrievedList.size(); i++) {
			JButton pressedNote = retrievedList.get(i);
			String noteName = pressedNote.getText();
			int getValue = convertToPitch(noteName);
			ActionListener keyPressedActionListener = new InteractionEventsInterface(pressedNote,midiGui,getValue,ticksPerSecond);
			pressedNote.addActionListener(keyPressedActionListener);
		}

	}

	/**
	 * Combines both keys types and allows the user to change instrument
	 * 
	 * @param pianoKeys
	 *            - The ArrayList of either whole or accidental notes passed to
	 *            the method.
	 */
	
	public void holdPlay(int pitch) throws InvalidMidiDataException, MidiUnavailableException {
		ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, pitch, 100);
		loadMIDI.returnSynth().getReceiver().send(noteOnMessage, -1);
	}

	public void stop(int pitch) throws InvalidMidiDataException, MidiUnavailableException {
		ShortMessage noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, pitch, 20);
		loadMIDI.returnSynth().getReceiver().send(noteOffMessage, -1);
	}
	
	public void instrumentChoices() {
		// Since using one MidiChannel, changes note of any instrument selected
		storeInstrumentsNames = allInstruments(loadMIDI.getListOfMidiChannels());
		instrumentList = new JComboBox<String>(storeInstrumentsNames);
		instrumentList.setEditable(true);
		instrumentList.setBounds(0, 110, 200, 52);
		layered.add(instrumentList);
		InteractionEventsInterface instrumentsBoxActionListener = new InteractionEventsInterface(instrumentList,midiGui,loadMIDI);
		instrumentList.addActionListener(instrumentsBoxActionListener);
	}

	/**
	 * Draws the main GUI layout, used in each Application GUI feature.
	 * 
	 */
	public void drawGUI() throws InvalidMidiDataException, MidiUnavailableException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();

		// Make new Content Pane
		layered = new JLayeredPane();
		frame = new JFrame("6100COMP: Computer Music Education Application");
		frame.setBounds(0, 0, screenWidth, screenHeight);
		frame.setBackground(Color.decode("#2F4F4F"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(layered);
		frame.setVisible(true);

		// Make other functions tabs
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 500, 52);
		layered.add(tabbedPane);

	}

	/**
	 * Main keyboard layout, with method calls to its various components.
	 * 
	 * @throws IOException
	 * 
	 */
	public void virtualKeyboard() throws InvalidMidiDataException, MidiUnavailableException, IOException {
	
		loadMIDI.prepareMIDI();
		instrumentChoices();
		pianoLayers = new JLayeredPane();
		pianoLayers.setBounds(10, 300, frame.getWidth(), 422);
		createWholeKeys();
		createSharpKeys();
		volumeToggle();
		playButton();
		recordButton();
		freePlayOrMakeTrack();
		drawPiano();
	}

	public void drawPiano() throws IOException{
		     
				pianoBacking = new JPanel();
				pianoBacking.setBounds(10, 40, frame.getWidth() - 80, 322);
				pianoBacking.setBackground(Color.black);
				pianoLayers.add(pianoBacking);
				JPanel speakerOne = new JPanel();
				speakerOne.setBounds(15, 40, frame.getWidth() - 90, 92);
				speakerOne.setBackground(Color.black);
				image = ImageIO.read(new File("src/Images/Speaker Grill 2.jpg"));
				JLabel picLabel = new JLabel(new ImageIcon(image));
				speakerOne.add(picLabel);
				pianoLayers.add(speakerOne, 0);
				frame.getContentPane().add(pianoLayers);
		
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoadMIDISupport getSuport = new LoadMIDISupport();
					 midiGui = new MIDIVirtualKeyboard(getSuport);
					midiGui.drawGUI();
					midiGui.virtualKeyboard();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}