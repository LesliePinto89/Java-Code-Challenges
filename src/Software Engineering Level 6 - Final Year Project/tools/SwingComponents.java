package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class SwingComponents {

	private static volatile SwingComponents instance = null;

    private SwingComponents() {}

    public static SwingComponents getInstance() {
        if (instance == null) {
            synchronized(SwingComponents.class) {
                if (instance == null) {
                    instance = new SwingComponents();
                    instance.featureTabDimensions();
                }
            }
        }

        return instance;
    }
	
   
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = (int) screenSize.getWidth();
	private int screenHeight = (int) screenSize.getHeight();
	private JTabbedPane tabbedPane;
	
	 public int getScreenWidth (){
		 return screenWidth;
	 }
	 
	 public int getScreenHeight (){
		 return screenHeight;
	 }
	 
	 public void featureTabDimensions(){
		 tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(0, 0, screenWidth / 2, screenHeight / 3);
	 }
	 
	 public JTabbedPane getFeatureTabDimensions (){
		 return tabbedPane;
	 }
	 
    
	public JButton customJButton(int x,int y, int width, int height) {
		JButton addButton = new JButton ();
		addButton.setBounds(x, y, width, height);
		//addButton.setText(buttonText);
		//addButton.setMnemonic(eventNumber);
		return addButton;

	}

	public JPanel customJPanelEditing(int x, int width, int y, int height, Color color, int axis) {
		JPanel carriedJPanel = new JPanel();
		carriedJPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 2, 0, color));
		carriedJPanel.setBounds(x, width, y, height);
		carriedJPanel.setBackground(color);
		//carriedJPanel.setLayout(new BoxLayout(carriedJPanel, axis));
		return carriedJPanel;
	}

	public JLabel customJLabelEditing(JLabel carriedJLabel, String text, int x, int y, int width, int height) {
		carriedJLabel.setText(text);
		carriedJLabel.setBounds(x, y, width, height);
		return carriedJLabel;
	}

	public JPanel createBasePanel(JPanel basePanel, int x, int y, int width, int height, Color color) {
		basePanel.setBounds(x, y, width, height);
		basePanel.setBackground(color);
		return basePanel;

	}
	
	

}
