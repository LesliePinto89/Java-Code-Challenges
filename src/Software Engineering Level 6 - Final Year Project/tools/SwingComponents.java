package tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;

public class SwingComponents implements MouseListener {

	private static volatile SwingComponents instance = null;

	private boolean colorToggleState = false;
	private boolean colorRangeToggleState = false;
	private boolean displayScaleNotesOnly = false;
	
	//used to distinguish playback of scales to relative pitch's show scales only
	public void displayScalesOnlyState(boolean scaleState) {
		displayScaleNotesOnly = scaleState;
	}

	public boolean getDisplayScaleState() {
		return displayScaleNotesOnly;
	}
	///////////////////////////////////////////////////
	
	public void changeColorToggle(boolean state) {
		colorToggleState = state;
	}

	public boolean getColorToggleStatus() {
		return colorToggleState;
	}
	
	public void changeRangeColorToggle(boolean rangeState) {
		colorRangeToggleState = rangeState;
	}

	public boolean getRangeColorToggleStatus() {
		return colorRangeToggleState;
	}

	////////////////////////////////////////////
	private SwingComponents() {
	}

	public static SwingComponents getInstance() {
		if (instance == null) {
			synchronized (SwingComponents.class) {
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
	private static int jListTableWidth;
	private static int jListYPos = 80;
	private static int jListTableHeight = 100;
	private static int jListYAndHeight = jListYPos + jListTableHeight + 50;

	public static int getJListWidth() {
		return jListTableWidth;
	}

	public static int getJListYPos() {
		return jListYPos;
	}

	public static int getJListTableHeight() {
		return jListTableHeight;
	}

	public static int getJListYAndHeight() {
		return jListYAndHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void featureTabDimensions() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 3));
		// tabbedPane.setBackground(Color.decode("#F0F8FF"));
		// This makes all display not change size - its kind of good and bad
		// thing
		// tabbedPane.setMinimumSize(new Dimension (screenWidth / 2,
		// screenHeight / 3));
		jListTableWidth = screenWidth / 2;
		jListTableHeight = screenHeight / 3;
	}

	public JTabbedPane getFeatureTab() {
		return tabbedPane;
	}

	// Intentional null layout frame for debug over features
	public JFrame floatingDebugFrame(boolean visible, boolean resize, Component c, String title, int x, int y,
			int width, int height) {
		JFrame aFrame = new JFrame();
		aFrame.setVisible(visible);
		aFrame.setResizable(resize);
		aFrame.setLocationRelativeTo(c);
		aFrame.setTitle(title);
		aFrame.setBounds(x, y, width, height);
		return aFrame;
	}

	public GridBagConstraints conditionalConstraints(int weightx, int weighty, int gridx, int gridy, int anchor,
			int fill) {
		GridBagConstraints conditionalConstraints = new GridBagConstraints();
		conditionalConstraints.weightx = weightx;
		conditionalConstraints.weighty = weighty;
		conditionalConstraints.gridx = gridx;
		conditionalConstraints.gridy = gridy;
		conditionalConstraints.anchor = anchor;
		conditionalConstraints.fill = fill;
		return conditionalConstraints;
	}

	public void debugFrame(JList<String> currentList, Color backPanel, Color innerPanel) {
		currentList.repaint();
		currentList.setCellRenderer(getRenderer(innerPanel));
		currentList.setBackground(backPanel);
	}

	public void colourMenuPanels(JList<String> currentList, Color backPanel, Color innerPanel) {
		currentList.setCellRenderer(getRenderer(innerPanel));
		currentList.setBackground(backPanel);
	}

	public void colourFeatureTab(JList<String> currentList, Color innerPanel) {
		currentList.setCellRenderer(getRenderer(innerPanel));

	}

	private ListCellRenderer<? super String> getRenderer(Color renderColor) {
		return new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));

				if (list.getName().equals("Instruments")) {
					int width = (int) tabbedPane.getPreferredSize().getWidth();
					int height = (int) tabbedPane.getPreferredSize().getHeight();
					listCellRendererComponent.setPreferredSize(new Dimension(width / 7, height / 20));
					listCellRendererComponent.setMinimumSize(new Dimension(width / 7, getScreenHeight() / 20));

					listCellRendererComponent.setBackground(renderColor);

					if (isSelected) {
						listCellRendererComponent.setBackground(Color.decode("#F5DEB3"));
						listCellRendererComponent.setForeground(Color.YELLOW);
					}

				}

				else {
					listCellRendererComponent
							.setPreferredSize(new Dimension(getScreenWidth() / 8, getScreenHeight() / 20));
					listCellRendererComponent
							.setMinimumSize(new Dimension(getScreenWidth() / 8, getScreenHeight() / 20));
					listCellRendererComponent.setBackground(renderColor);
				}
				Font aFont = new Font("Serif", Font.BOLD, 16);
				listCellRendererComponent.setFont(aFont);
				// listCellRendererComponent

				return listCellRendererComponent;
			}
		};
	}

	public JButton customJButton(int x, int y, int width, int height, String text, MouseListener listen) {
		JButton aButton = new JButton();
		aButton.setPreferredSize(new Dimension(width, height));
		aButton.setText(text);
		aButton.setName(text);
		aButton.addMouseListener(listen);
		return aButton;
	}

	public JToggleButton customJToggleButton(int x, int y, int width, int height, String text, MouseListener listen) {
		JToggleButton aJToggleButton = new JToggleButton();
		aJToggleButton.setPreferredSize(new Dimension(width, height));
		aJToggleButton.setText(text);
		aJToggleButton.setName(text);
		aJToggleButton.addMouseListener(listen);
		return aJToggleButton;
	}

	public JPanel generateEventPanel(int width, int height, MouseListener listen, Color panelColor, Color border,
			int top, int left, int bottom, int right) {
		JPanel carriedJPanel = new JPanel();
		carriedJPanel.setBackground(panelColor);
		carriedJPanel.setPreferredSize(new Dimension(width, height));
		carriedJPanel.setMinimumSize(new Dimension(width, height));
		carriedJPanel.addMouseListener(listen);
		carriedJPanel.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, border));
		return carriedJPanel;
	}
	
	public JPanel guiBorderPanel(BufferedImage carriedBufferedImage, int width, int height, Color border,int top, int left, int bottom, int right) {
		JPanel carriedJPanel = new JPanel();
		carriedJPanel.setPreferredSize(new Dimension(width, height));
		carriedJPanel.setMinimumSize(new Dimension(width, height));
		carriedJPanel.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, border));
		JLabel temp = customiseImageAsJLabel(carriedBufferedImage,width,height*3);
		carriedJPanel.add(temp);
		return carriedJPanel;
	}
	
	

	public JLabel customiseImageAsJLabel(BufferedImage carriedBufferedImage, int width, int height) {
		Image scaledOff = null;
		scaledOff = carriedBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		JLabel picLabel = new JLabel(new ImageIcon(scaledOff));
		return picLabel;
	}

	public JPanel customizeFeaturePanel(int width, int height, MouseListener listen,
			BufferedImage carriedBufferedImage) {
		JPanel carriedJPanel = new JPanel();
		carriedJPanel.setBackground(Color.decode("#696969"));
		carriedJPanel.setPreferredSize(new Dimension(width, height));
		carriedJPanel.setMinimumSize(new Dimension(width, height));
		carriedJPanel.addMouseListener(listen);
		Image scaledOff = null;
		scaledOff = carriedBufferedImage.getScaledInstance(screenWidth / 4, screenHeight / 4, Image.SCALE_SMOOTH);
		JLabel picLabel = new JLabel(new ImageIcon(scaledOff));
		carriedJPanel.add(picLabel);
		return carriedJPanel;

	}

	public JLabel customJLabelEditing(JLabel carriedJLabel, String text, int x, int y, int width, int height) {
		carriedJLabel.setText(text);
		carriedJLabel.setPreferredSize(new Dimension(width, height));
		return carriedJLabel;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

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

}