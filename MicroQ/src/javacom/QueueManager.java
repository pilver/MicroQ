package javacom;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * The class handling queue manipulations.
 * 
 * @version 1.0
 * @author Marcus Enderskog
 *
 */
public class QueueManager {
	/**
	 * The actual queue that all methods will work with.
	 */
	public static ConcurrentLinkedQueue<Student> Queue;
	//public static ArrayList<Thread> runningThreads = new ArrayList<Thread>();
	
	/**
	 * Initializes the queue.
	 */
	public QueueManager() {
		Queue = new ConcurrentLinkedQueue<Student>();
	}
	
	/**
	 * Adds a student object to the queue.
	 * @param s			The Student object that will be added to the queue.
	 */
	public static void addStudent(Student s)
	{
		SimulationMain.simData.setStartTime(System.currentTimeMillis(), s.getStudentNumber());
		Queue.add(s);
		Simulation.queue_sem.release();
	}
	
	/**
	 * Removes the first element in the queue and starts a new Communicator thread with the popped student as 
	 * argument.
	 */
	public void readyCheck()
	{
		System.out.println("Queue size: " + Queue.size());
		Student s = Queue.remove();
		QueueRepresentation.addWaitingQueue(s.getStudentNumber());
		SimulationMain.simData.setEndTime(System.currentTimeMillis(), s.getStudentNumber());
		Thread t = new Thread(new Communicator(s));
		t.start();
		//runningThreads.add(t);
	}
}
