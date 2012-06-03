package javacom;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueRepresentation {
	
	private static ConcurrentLinkedQueue<Integer> waitingQueue;
	private Object[] queueArr;
	private Object[] waitingQueueArr;
	private int studSize = 28;
	private int offset = (500 - studSize*10) / 11;
	private int[] xpos = new int[10];
	
	public QueueRepresentation() {
		initXPos();
		waitingQueue = new ConcurrentLinkedQueue<Integer>();
	}
	
	private void initXPos() {
		for (int i = 0; i < 10; i++) {
			xpos[i] = 500 - offset*(i+1) - studSize*(i+1);
		}
	}
	
	public static void addWaitingQueue(Integer studNum) {
		waitingQueue.add(studNum);
	}
	
	public static void popWaitingQueue(Integer studNum) {
		waitingQueue.remove(studNum);
	}
	
	private void updateArr() {
		queueArr = QueueManager.Queue.toArray();
		waitingQueueArr = waitingQueue.toArray();
	}
	
	private void drawQueue(Graphics bbg) {
		String studNum;
		for (int i = 0; i < queueArr.length && i < 10; i++) {
			bbg.setColor(Color.GRAY);
			bbg.drawRect(xpos[i], 500 - studSize - offset, studSize, studSize);
			studNum = "" + ((Student) queueArr[i]).getStudentNumber();
			bbg.setColor(Color.BLACK);
			bbg.drawString(studNum, xpos[i]+8, 500-studSize-offset+20);
		}
	}
	
	private void drawWaitingQueue(Graphics bbg) {
		String studNum;
		for (int i = 0; i < waitingQueueArr.length && i < 10; i++) {
			bbg.setColor(Color.GRAY);
			bbg.drawRect(xpos[i], 500 - studSize - offset*4, studSize, studSize);
			studNum = "" + waitingQueueArr[i];
			bbg.setColor(Color.BLACK);
			bbg.drawString(studNum, xpos[i]+8, 500-studSize-offset*4+20);
		}
	}
	
	public void draw(Graphics bbg) {
		updateArr();
		drawQueue(bbg);
		drawWaitingQueue(bbg);
	}

}
