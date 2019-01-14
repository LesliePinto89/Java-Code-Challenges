package tools;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomSwingComponents {

	public JButton customJButton(JButton addButton, String buttonText, Integer x,Integer y, Integer width, Integer height) {
		addButton.setText(buttonText);
		//addButton.setMnemonic(eventNumber);
		return addButton;

	}

	public JPanel customJPanelEditing(JPanel carriedJPanel, int _W, int line1, int axis, Color color) {
		carriedJPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 2, 0, color));
		carriedJPanel.setBounds(5, line1 - 25, _W - 15, 20);
		carriedJPanel.setLayout(new BoxLayout(carriedJPanel, axis));
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
