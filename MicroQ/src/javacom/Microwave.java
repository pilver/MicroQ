package javacom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * The class representing a microwave in the user interface.
 * 
 * @author Björn Berggren
 *
 */
public class Microwave {
	
	/**
	 * The microwave's current time left in seconds. 0 if the microwave is empty.
	 */
	private int time = 0;
	
	/**
	 * The microwave's timer. Used for determining the when to update time.
	 */
	private Timer timer;
	
	private int currentStudentNum = -1;
	
	/**
	 * The x and y coordinates where the microwave will be drawn.
	 */
	private int x, y;
	
	/**
	 * The time left represented as MM:SS
	 */
	private String stringState = "00:00";
	
	/**
	 * The color state. will be Color.GREEN if the microwave is emtpy and Color.RED if it's occupied.
	 */
	private Color colorState = Color.GREEN;
	
	/**
	 * The size of the oval representing a microwave
	 */
	private int ovalSize = 28;
	
	/**
	 * The distance between each microwave
	 */
	private int offset = (500 - ovalSize*10) / 11;

	private int totalTimeOccupied;
	
	//private Image imageState = Toolkit.getDefaultToolkit().createImage("/res/green_circle.jpg");
	
	//private Image greenCircle = Toolkit.getDefaultToolkit().createImage("/res/green_circle.jpg");
	//private Image redCircle = Toolkit.getDefaultToolkit().createImage("/res/red_circle.jpg");
	
	/**
	 * Initializes a microwave, determining the x and y coordinates using microNum, ovalSize and offset. It also
	 * initializes timer. Every second when timer is started timer will decrease time and update colorState to the 
	 * right color. It will also update the correct stringState using time. If time reaches 0, timer will stop.
	 * @param microNum		The microwave number.
	 */
	public Microwave(int microNum) {
		x = offset + (ovalSize+offset)*(microNum%10);
		y = offset + (ovalSize+offset)*(microNum/10);
		timer = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				colorState = Color.RED;
				time--;
				if (time == 0) {
					currentStudentNum = -1;
					timer.stop();
					colorState = Color.GREEN;
				}
				if (time/600 < 1) {
					if (time%60 <= 9) stringState = "0" + time/60 + ":" + "0" + time%60;
					else stringState = "0" + time/60 + ":" + time%60;
				} else {
					if (time%60 <= 9) stringState = time/60 + ":" + "0" + time%60;
					else stringState = time/60 + ":" + time%60;
				}
				
			}
		});
		
		
	}
//	
//	public String getStringState() {
//		return this.stringState;
//	}
//	
//	public Color getColorState() {
//		return this.colorState;
//	}
//	
	/**
	 * Sets time and updates the total time the microwave has been occupied
	 * @param i		the the time which <code>time</code> will be set to.
	 */
	public void setTime(int i) {
		time = i;
		totalTimeOccupied += i;
	}
	
	public int getTotalTimeOccupied() {
		return totalTimeOccupied;
	}
	/**
	 * Returns this microwave's timer.
	 * 
	 * @return timer
	 */
	public Timer getTimer() {
		return timer;
	}
	
	public void setCurrentStudentNum(int num) {
		this.currentStudentNum = num;
	}
	
	/**
	 * Draws the microwave and it's time left to the Graphics object provided.
	 * 
	 * @param bbg		The Graphics object where the microwave and time will be drawn.
	 */
	public void draw(Graphics bbg) {
		
		bbg.setColor(colorState);
		bbg.fillOval(x, y, ovalSize, ovalSize);
		bbg.setColor(Color.BLACK);
		bbg.drawString(stringState, x, y+ovalSize+12);
		if (currentStudentNum != -1) {
			bbg.drawString("" + currentStudentNum, x+7, y+20);
		}
		
	}
	
	/**
	 * @return time
	 */
	public int getTime() {
		return time;
	}
}
