package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class MIDIVisualPanel extends JComponent {

	   private int startTickX = 0;
	   private int midiRecY = 0;
	   private int endTickWidth = 0;
	   private int midiRecHeight = 0;
	   
	    private static final long serialVersionUID = 1L;

	    public MIDIVisualPanel(int x, int y, int width, int height){
	    	this.startTickX = x;
	    	this.midiRecY = y;
	    	this.endTickWidth = width;
	    	this.midiRecHeight = height;
	    }
	    
	    @Override
	    public Dimension getMinimumSize() {
	        return new Dimension(100, 100);
	    }

	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(400, 300);
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	       // int margin = 10;
	      //  Dimension dim = getSize();
	        super.paintComponent(g);
	        g.setColor(Color.red);
	        g.fillRect(startTickX,midiRecY,endTickWidth,midiRecHeight);
	    }
	}
