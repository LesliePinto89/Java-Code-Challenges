package tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.sound.midi.MidiSystem;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import midiDevices.MidiReciever;

public class MIDIFileManager {

	private File createdMIDIFile;
	private ArrayList<File> storedMIDISavedFiles = new ArrayList<File>();
	private JFileChooser fileChooser;
	private File defaultDirectory;
	
	public MIDIFileManager(){
		//this.reciever = carriedReciever;
         fileChooser = new JFileChooser();
		 defaultDirectory = new File (System.getProperty("user.home") + System.getProperty("file.separator")+ "/Desktop/save folder".toString());
		 
		 //Will vary based on default directory
		fileChooser.setCurrentDirectory(defaultDirectory);
	}
	
	public DefaultListModel<String> getSongList (DefaultListModel<String> carriedListModel){
		for (File fileEntry : defaultDirectory.listFiles()){
			if(fileEntry.isDirectory()){
				//matchingFile(addedButton,fileEntry);
			}
			else {
				carriedListModel.addElement(fileEntry.toString());
			}
		}
		return carriedListModel;
	}
	
	public ArrayList<File> getFilesSongList(){
		for (File fileEntry : defaultDirectory.listFiles()){
			storedMIDISavedFiles.add(fileEntry);		
		}
		return storedMIDISavedFiles;
	}
	
	
	public File selectMIDIFile() {
		//JFileChooser selectFile = new JFileChooser();
		
		int sf = fileChooser.showOpenDialog(fileChooser);
		// Store so can get later in memory
		File newFile = fileChooser.getSelectedFile();
		
		if (sf == JFileChooser.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(null, "File save has been canceled");
		}
		return newFile;
		
	     }
		
	public void saveNewMIDIFile(JToggleButton saveMIDI) {
		// Valid when user has made a sequence
		if (MidiReciever.getInstance().getSequence() != null) {
			//JFileChooser saveFile = new JFileChooser();

			int sf = fileChooser.showSaveDialog(fileChooser);
			// Store so can get later in memory
			File newFile = fileChooser.getSelectedFile();
			storeMIDIFileArray(newFile);
			if (sf == JFileChooser.APPROVE_OPTION) {
				int[] allowedMidiTypes = MidiSystem.getMidiFileTypes(MidiReciever.getInstance().getSequence());
				if (allowedMidiTypes.length == 0) {
					System.err.println("No supported MIDI file types.");
				} else {
					try {
						MidiSystem.write(MidiReciever.getInstance().getSequence(), allowedMidiTypes[0], getCurrentMIDIFile());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				JOptionPane.showMessageDialog(null, "File has been saved", "File Saved",
						JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog(null, "Do you want to delele the sequence", "Delete Sequence?",
						JOptionPane.INFORMATION_MESSAGE);

				// true for rewrite, false for override
			} else if (sf == JFileChooser.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "File save has been canceled");
			}
			saveMIDI.setSelected(false);
			saveMIDI.setEnabled(true);
		}
	}

	public void storeMIDIFileArray(File storedMIDIFile) {
		//Add to all midi files in memory
		storedMIDISavedFiles.add(storedMIDIFile);
		
		//Used to add selected MIDI file to save output
		this.createdMIDIFile = storedMIDIFile;
		
	}

	//Used to add custom images based on user action
		public Vector <String> getMIDIFilesNames() throws IOException{	
			String path = storedMIDISavedFiles.get(0).getParentFile().toString();
			File folder = new File(path);
			//String path = file.getAbsolutePath();
			
			Vector <String> imageList = new Vector <String>();
			for (File fileEntry : folder.listFiles() ){
				if(fileEntry.isDirectory()){
					//matchingFile(addedButton,fileEntry);
				}
				else {
					imageList.add(fileEntry.toString());
				}
			}
			return imageList;
			
		}
	// To be used to write created sequence MIDI externally to program
	public File getCurrentMIDIFile() {
		return createdMIDIFile;
	}
}