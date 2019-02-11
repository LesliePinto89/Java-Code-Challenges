package tools;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import keyboard.VirtualKeyboard;
import midi.DurationTimer;
import midiDevices.MidiReciever;
import java.awt.BorderLayout;

public class ProgramMainGUI implements MouseListener {

	protected JFrame frame;
	protected JLayeredPane layered;
	protected JPanel backgroundImagePanel = new JPanel();
	private JPanel freePlayPanel = new JPanel();
	private JPanel midiPlayer = new JPanel();
	private JPanel createPiano = new JPanel();
	protected int screenWidth;
	protected int screenHeight;
	protected Dimension screenSize;
	private static VirtualKeyboard midiGui;
	private static ProgramMainGUI loadProgram;
	private static MIDIFilePlayer filePlayer;
	private static MIDIFileManager fileManager;

	public void loadProgramWindowFrameGUI() throws IOException {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();

		layered = new JLayeredPane();
		frame = new JFrame("Midi Keyboard: Welcome Screen");
		frame.setBounds(0, 0, screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(layered, BorderLayout.CENTER);
	}

	public JPanel customizePanel(JPanel carriedJPanel, int x, int y, int width, int height) {
		carriedJPanel.setBounds(x, y, width, height);
		carriedJPanel.addMouseListener(this);
		return carriedJPanel;

	}

	public static void loadApplication() throws IOException, InvalidMidiDataException, MidiUnavailableException {
		loadProgram = new ProgramMainGUI();

		// Instantiated the class once and then call its start connection
		// method.The class uses the singleton design pattern so it can be referenced
		// throughout the application.
		
		MidiReciever.getInstance();
		MidiReciever.getInstance().startConnection();

		
		DurationTimer.getInstance();
		
		fileManager = new MIDIFileManager();
		filePlayer = new MIDIFilePlayer(fileManager);
		midiGui = new VirtualKeyboard(fileManager);
		loadProgram.loadProgramWindowFrameGUI();
		loadProgram.loadProgramOptions();

	}

	public JLabel addPanelImage(BufferedImage carriedBufferedImage, Container carriedObject) {
		Image scaledOff = null;
		if (carriedObject instanceof JFrame) {
			scaledOff = carriedBufferedImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
		} else if (carriedObject instanceof JPanel) {
			scaledOff = carriedBufferedImage.getScaledInstance(carriedObject.getWidth(), carriedObject.getHeight(),
					Image.SCALE_SMOOTH);
		}
		JLabel picLabel = new JLabel(new ImageIcon(scaledOff));
		return picLabel;
	}

	// Main features
	public void loadProgramOptions() throws IOException {

		// Background Image panel to add to layered panel
		backgroundImagePanel.setBounds(0, 0, screenWidth, screenHeight);
		BufferedImage backgroundImage = ImageIO.read(new File("src/Images/Background2.jpg"));
		backgroundImagePanel.add(addPanelImage(backgroundImage, frame));

		freePlayPanel = customizePanel(freePlayPanel, 155, 40, 300, 200);
		BufferedImage bufFreePlayPianoImage = ImageIO.read(new File("src/Images/piano-image.jpg"));
		freePlayPanel.add(addPanelImage(bufFreePlayPianoImage, freePlayPanel));

		// Add piano panel Image to add to layered panel
		createPiano = customizePanel(createPiano, frame.getWidth() / 2, 40, 300, 200);
		BufferedImage bufCreatePianoImage = ImageIO.read(new File("src/Images/Music score.jpg"));
		createPiano.add(addPanelImage(bufCreatePianoImage, createPiano));

		midiPlayer = customizePanel(midiPlayer, 155, frame.getHeight() / 3, 300, 200);
		BufferedImage bufMidiPlayerImage = ImageIO.read(new File("src/Images/MusicStation.jpg"));
		midiPlayer.add(addPanelImage(bufMidiPlayerImage, midiPlayer));

		layered.add(backgroundImagePanel, new Integer(0), 0);
		layered.add(freePlayPanel, new Integer(1), 0);
		layered.add(createPiano, new Integer(2), 0);
		layered.add(midiPlayer, new Integer(3), 0);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loadApplication();
				} catch (IOException | InvalidMidiDataException | MidiUnavailableException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();

		if (obj.equals(freePlayPanel)) {
			try {
				frame.setVisible(false);
				midiGui.drawKeyboardGUI();
				midiGui.createVirtualKeyboard();
			} catch (InvalidMidiDataException | MidiUnavailableException | IOException e1) {
				e1.printStackTrace();
			}
		}
		if (obj.equals(midiPlayer)) {
			try {
				frame.setVisible(false);
				filePlayer.drawMusicPlayerGUI();

			} catch (InvalidMidiDataException | MidiUnavailableException | IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Object obj = e.getSource();
		if (obj.equals(freePlayPanel)) {

			try {
				BufferedImage bufFreePlayPianoTransparentImage = ImageIO
						.read(new File("src/Images/piano-image-transparent.jpg"));
				Image pianoTransparentEdited = bufFreePlayPianoTransparentImage
						.getScaledInstance(freePlayPanel.getWidth(), freePlayPanel.getHeight(), Image.SCALE_SMOOTH);
				JLabel freePlayTransparentOption = new JLabel(new ImageIcon(pianoTransparentEdited));
				// freePlayPanel.remove(0);
				freePlayPanel.add(freePlayTransparentOption, 0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
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
