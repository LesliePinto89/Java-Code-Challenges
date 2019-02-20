package tools;

//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Toolkit;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.text.*;


import java.awt.event.*;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.awt.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import keyboard.KeyboardInteractions;
import keyboard.Note;
import midi.Chord;
import midi.ListOfChords;
import midi.ListOfScales;
import midi.Scale; 

public class ScreenPrompt implements ActionListener{
	
	private static volatile ScreenPrompt instance = null;
    static JTextField t; 
    static JPanel p; 
    static JButton b; 
    static JLabel l; 
    static JTextArea textArea;
    String choice = "";
    String key;
    String decideScale;
    int state = 0;
    int lastState;
    
    public static ScreenPrompt getInstance() {
        if (instance == null) {
            synchronized(ScreenPrompt.class) {
                if (instance == null) {
                   instance = new ScreenPrompt();
                   
                }
            }
        }

        return instance;
    }

    private ScreenPrompt () {
    }
    
        public JPanel createPrompt() 
        { 
        
            p = new JPanel (); 
            b = new JButton("Send"); 
            b.addActionListener(this); 

            
            t = new JTextField(16);
           // t.requestFocusInWindow(); //Might not need
            Font fo = new Font("Serif", Font.BOLD, 20); 
            t.setFont(fo); 
                   
            p.add(t); 
            p.add(b);  
            
            int screenWidth = SwingComponents.getInstance().getScreenWidth();
            int screenHeight = SwingComponents.getInstance().getScreenHeight();
        	int x = screenWidth/2 +20;
        	
            p.setBounds(x, 20, screenWidth / 2 - 100, screenHeight / 2 - 100);
            p.setBackground(Color.GREEN);        
            textArea = new JTextArea(14,50);
            
            textArea.append("List of actions\n");
            textArea.append("1) Create a chord\n");
            textArea.append("2) List all scales from a key\n");
            
            JScrollPane scrollPane = new JScrollPane(textArea); 
            textArea.setEditable(false);
            p.add(scrollPane);

      return p;
        } 
        
        public void updateScreen (){
        	textArea.setText(null);
        }
        
        public void getAllMajorScalesInAKey (String key, String scale) throws InvalidMidiDataException{
        	ListOfScales.getInstance().createDiatonicScale(key,scale);
        	
        	/*String [] scalesToStrings = new String [10];
        	Note [][] loadedScaleChords = ChordConstruction.getInstance().getIonianChords();
        	int i =0;
        	for (Note [] aChord : loadedScaleChords){
        		scalesToStrings[i] = aChord.toString();
        		i++;
        	}*/
        	
        	//Major Chord elements
    		/*DefaultListModel<String> chord = Chord.getInstance().getMajorChordsInList();
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
        */
        }

        //Chord.getInstance().setIonianChords(heptatonicModelsScales);
			//Note [][] loadedScaleChords = Chord.getInstance().getIonianChords();
			//Chord.majorChordNames [] majorChordNames = Chord.majorChordNames.values();
			//Chord.getInstance().findSpecificChordType(loadedScaleChords,majorChordNames);
  
        
      
        // if the button is pressed 
        public void actionPerformed(ActionEvent e) 
        { 
            String s = e.getActionCommand(); 
            choice = t.getText();
             if (s.equals("Send") && choice.equals("1") && state == 0) { 
            	t.setText(""); //First option on first page
            	state =1;
            } 
            else if (s.equals("Send") && choice.equals("2") && state == 0) { 
            	t.setText(""); //Second option on first page
            	textArea.append("Please type in a note as the key\n");
            	state =1;
            	
            } 
             
             /////////////Second page
            else if (s.equals("Send") && state == 1 && key ==null) { 
            	 key = t.getText();    //Second page options
            	 decideScale = "isReady";
            	 t.setText("");
            	 textArea.append("Please type in a scale\n");
            }
             
             else if (s.equals("Send") && key !=null && decideScale =="isReady") {
            	 decideScale = t.getText();
            	 t.setText("");
            	 try {
                	 if (decideScale.contains("major")||decideScale.contains("minor")){
                		 getAllMajorScalesInAKey(key,decideScale);
          			  }
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
             }
             
     }
             
            	
                // set the text of the label to the text of the field 
               // l.setText(t.getText()); 
      
                // set the text of field to blank 
               // t.setText("  "); 
            
        } 
    
    
    
    /*
	private int prefixLength;
	private Action deletePrevious;
	
	private SwingComponents makeComps = SwingComponents.getInstance();
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = (int) screenSize.getWidth();
	private int screenHeight  = (int) screenSize.getHeight();
    
	public ScreenPrompt(int prefixLength, JTextComponent component) {
		this.prefixLength = prefixLength;
		deletePrevious = component.getActionMap().get("delete-previous");
		component.getActionMap().put("delete-previous", new BackspaceAction());
		component.setCaretPosition(prefixLength);
	}

	public void setDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
		fb.setDot(Math.max(dot, prefixLength), bias);
	}

	public void moveDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
		fb.moveDot(Math.max(dot, prefixLength), bias);
	}

	class BackspaceAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = (JTextComponent) e.getSource();

			if (component.getCaretPosition() > prefixLength) {
				deletePrevious.actionPerformed(null);
			}
		}
	}

	public  JPanel runCode() {
		int x = screenWidth/2 +20;
		JTextField textField = new JTextField("Prefix_", 20);
		textField.setNavigationFilter(new ScreenPrompt(7, textField));
		JPanel test = SwingComponents.getInstance().customJPanelEditing(x, 20, screenWidth / 2 - 100, screenHeight / 2 - 100,
				Color.BLUE, 100);
		test.add(textField);
		return test;
	}

}*/
