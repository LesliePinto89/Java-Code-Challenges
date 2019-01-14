package tools;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JProgressBar;

import keyboard.KeyboardInteractions;

public class SeekBar extends JProgressBar implements MouseListener {

	 private int updatedValue = 0; //sharing between different scopes

	 /**
	  * Update SeekBar position
	  * @param progress in microseconds
	  * @param totalVal in seconds
	  */
	 public void updateSeekBar(long progress, float totalVal)
	 {
	  BackgroundExecutor.Get().execute(new UpdatingTask(progress, totalVal)); //Another thread will calculate the relative position
	  setValue(updatedValue);
	 }
	 
	 /**
	  * Task used for updating the seek value in another thread.
	  * @author Pierluigi
	  */
	 private class UpdatingTask implements Runnable {

	  long progress; float totalVal;
	 
	  public UpdatingTask(long progress, float totalVal) {
	   this.progress = progress;
	   this.totalVal = totalVal;
	  }
	  
	  @Override
	  public void run() {
	int lastSeekVal = getValue();
	   int lp = (int) (progress / 1000); //progress comes in microseconds
	   int seekLenght = getMaximum();
	   int n = (int) ((lp/(totalVal*1000))*seekLenght); 
	   updatedValue = lastSeekVal+n; 
	  }
	 }
	 ///////////////////////////////////////////////////////////
	 
	 /**
	  * New Constructor, sets a mouseListener
	  * (extends JProgressBar)
	  */
	 public SeekBar()
	 {
	  super();
	  setMaximum(10000); //it's smoother this way
	  addMouseListener(this);
	  
	 }
	 
	 /**
	  * Informs the player about the relative value selected in the seekbar 
	  * @throws BasicPlayerException 
	  */
	 private void returnValueToPlayer(float val){
	  //TODO inform our player
	 }

	 private void log(String str)
	 {
	  System.out.println("SeekBar] " +str);
	 }
	 
	 public static class BackgroundExecutor {
		 
		  private  static ExecutorService backgroundEx = Executors.newCachedThreadPool(); //UI thread shouldn't do math
		 
		   
		 
		  public BackgroundExecutor(){}
		 
		  
		 
		  public static  ExecutorService Get() { return backgroundEx;}

	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	  public void mousePressed(MouseEvent e) {
	    float val =  ((float)e.getX()/getWidth()) * getMaximum();
	    returnValueToPlayer(val);
	    setValue((int)val);
	    log("SeekBar pressed: " + val + " x: " + e.getX());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
