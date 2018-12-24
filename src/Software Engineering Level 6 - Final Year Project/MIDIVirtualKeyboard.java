import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;

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

	private int octave = 2;
	private int wholeNotePositionstart = 22;
	private int sharpNotePositionstart = 42;
	private LinkedHashMap<String, MidiChannel> buttonToNote = new LinkedHashMap<String, MidiChannel>();
	private LoadMIDISupport loadMIDI;

	/**
	 * Create the application with the MIDI devices loaded in memory
	 * 
	 * @param loadMIDI - The MIDI devices needed in the application
	 */
	public MIDIVirtualKeyboard(LoadMIDISupport loadMIDI) {
		this.loadMIDI = loadMIDI;
	}

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
	 * @param button - JButton piano key to style
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
		ArrayList<JButton> sharpKeys = new ArrayList<JButton>();
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
				sharpKeys.add(button);
				styleKeys(button);
				sharpNotePositionstart += 37;
			}
		}
		playKeys(sharpKeys);
	}

	/**
	 * Creates the piano's whole (white) keys.
	 * 
	 */
	public void createWholeKeys() throws InvalidMidiDataException, MidiUnavailableException {
		EnumSet<Note.NoteType> naturalKeysEnums = EnumSet.allOf(Note.NoteType.class);
		ArrayList<JButton> naturalKeys = new ArrayList<JButton>();
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
					naturalKeys.add(button);
					endOctave = true;
					break;
				}
				styleKeys(button);
				naturalKeys.add(button);
				wholeNotePositionstart += 35;
				if (getNote.toString().equals("B")) {
					octave++;
				}
			}
		}
		playKeys(naturalKeys);
	}

	/**
	 * Gets all supported synthesizer's instruments names.
	 * 
	 * @param Instrument[] channels - Stores instruments names
	 * @return array that stores the instruments names in memory
	 */
	public String[] allInstruments(Instrument[] channels) {
		String[] tempStorage = new String[channels.length];
		for (int i = 0; i < channels.length; i++) {
			tempStorage[i] = channels[i].getName();
		}
		return tempStorage;
	}

	/**
	 * Adjust all notes volume based on slider value.
	 */
	public void volumeToggle() {
		slider = new JSlider(0, 100, 71);
		slider.setBounds(300, 300, 200, 50);
		slider.setBackground(Color.BLACK);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MidiChannel getit = loadMIDI.getMidiChannel();
				JSlider slider = (JSlider) e.getSource();
				if (!slider.getValueIsAdjusting()) {
					int value = slider.getValue();
					getit.controlChange(7, value);
				}
			}
		});
		layered.add(slider);
	}

	/**
	 * Combines both keys types and allows the user to change instrument
	 * @param pianoKeys - The ArrayList of either whole or accidental notes passed to
	 *                    the method.
	 */
	public void playKeys(ArrayList<JButton> pianoKeys) throws InvalidMidiDataException, MidiUnavailableException {
		storeInstrumentsNames = allInstruments(loadMIDI.getListOfMidiChannels());
		MidiChannel getit = loadMIDI.selectInstrument("Piano 1");

		for (int i = 0; i < pianoKeys.size(); i++) {
			JButton temp = pianoKeys.get(i);
			String noteName = temp.getText();
			int getValue = convertToPitch(noteName);
			temp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MidiChannel tempChannel = buttonToNote.get(noteName);
					tempChannel.noteOn(getValue, 71);
				}
			});
			buttonToNote.put(noteName, getit);
		}

		// Since using one MidiChannel, changes note of any instrument selected
		JComboBox<String> instrumentList = new JComboBox<String>(storeInstrumentsNames);
		instrumentList.setEditable(true);
		instrumentList.setBounds(0, 110, 200, 52);
		instrumentList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				String choice = instrumentList.getSelectedItem().toString();
				MidiChannel getit = loadMIDI.selectInstrument(choice);
				loadMIDI.newMidiChannel(getit);
			}
		});

		layered.add(instrumentList);
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
		frame.setBackground(Color.decode("#0099cc"));
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
	 */
	public void virtualKeyboard() throws InvalidMidiDataException, MidiUnavailableException {
		// Needed to load needed MIDI devices
		loadMIDI.prepareMIDI();

		// Create piano keyboard
		pianoLayers = new JLayeredPane();
		pianoLayers.setBounds(10, 240, frame.getWidth(), 422);
		createWholeKeys();
		createSharpKeys();
		volumeToggle();

		// Piano decoration panel
		pianoBacking = new JPanel();
		pianoBacking.setBounds(10, 40, frame.getWidth() - 80, 322);
		pianoBacking.setBackground(Color.black);
		pianoLayers.add(pianoBacking);
		frame.getContentPane().add(pianoLayers);

	}

	/*
	 * public void playNote(int finalNote, int finalInstrument) { try {
	 * Sequencer sequencer = MidiSystem.getSequencer(); sequencer.open();
	 * Sequence sequence = new Sequence(Sequence.PPQ, 4); Track track =
	 * sequence.createTrack();
	 * 
	 * MidiEvent event = null;
	 * 
	 * ShortMessage first = new ShortMessage(); first.setMessage(192, 1,
	 * finalInstrument, 0); MidiEvent changeInstrument = new MidiEvent(first,
	 * 1); track.add(changeInstrument);
	 * 
	 * ShortMessage a = new ShortMessage(); a.setMessage(144, 1, finalNote,
	 * 100); MidiEvent noteOn = new MidiEvent(a, 1); track.add(noteOn);
	 * 
	 * ShortMessage b = new ShortMessage(); b.setMessage(128, 1, finalNote,
	 * 100); MidiEvent noteOff = new MidiEvent(b, 16); track.add(noteOff);
	 * 
	 * sequencer.setSequence(sequence); sequencer.start(); } catch (Exception
	 * ex) { ex.printStackTrace(); }
	 * 
	 * }
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoadMIDISupport getSuport = new LoadMIDISupport();
					MIDIVirtualKeyboard midiGui = new MIDIVirtualKeyboard(getSuport);
					midiGui.drawGUI();
					midiGui.virtualKeyboard();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}