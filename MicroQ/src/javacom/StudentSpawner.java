package javacom;

import java.util.concurrent.Semaphore;

import com.ericsson.otp.erlang.OtpNode;



/**
 * A runnable spawner Class. The StudentSpawner will send spawn request to a new Communicator-instance. The amount
 * of students that should be spawned is set in the constructor. 
 * 
 * 
 * @version 1.0
 * @author Mr who?
 * @author Mr you!
 *
 */
public class StudentSpawner implements Runnable{
	
	/**
	 * The StudentSpawner's spawn rate. An integer between 1 and 100
	 */
	private int spawnRate;
	
	/**
	 * 
	 */
	public static Integer iterSync = new Integer(0);
	
	/**
	 * Returns the StudentSpawner's spawn rate. <i>Used for testing</i>.
	 * 
	 * @return spawnRate 
	 */
	public int getSpawnRate() {
		return spawnRate;
	}
	
	/**
	 * Returns the StudentSpawner's switch rate. <i>Used for testing</i>.
	 * 
	 * @return switchRate
	 */
	public int getSwitchRate() {
		return switchRate;
	}
	
	/**
	 * Returns the StudentSpawner's spawn limit. <i>Used for testing</i>.
	 * 
	 * @return numberOfStudents
	 */
	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	/**
	 * Returns the StudentSpawner's current spawn amount. <i>Used for testing</i>.
	 * 
	 * @return iter
	 */
	public static int getIter() {
		return iter;
	}

	/**
	 * Returns the StudentSpawner's Erlang-node. <i>Used for testing</i>.
	 * 
	 * @return javaNode
	 */
	public OtpNode getJavaNode() {
		return javaNode;
	}


	/**
	 * The switch rate which will be sent to the new student's Erlang state_switcher-process.
	 */
	private int switchRate;
	
	/**
	 * The spawn limit. The StudentSpawner will spawn at most <code>numberOfStudents</code> new students.
	 */
	private int numberOfStudents;
	
	/**
	 * The current spawn amount. If <code>iter</code> gets larger than <code>numberOfStudents</code> the 
	 * StudentSpawner will stop spawning new students.
	 */
	public static int iter;
	
	/**
	 * @deprecated
	 * 
	 * The Erlang-node on the java part. No longer used but might be reimplemented in a future build. 
	 */
	private OtpNode javaNode;

	static Semaphore spawn_sem = new Semaphore(0);
	
	
	/**
	 * Constructor which will setup parameters.
	 * 
	 * @param sRate				The spawn rate which spawnRate will be set to.
	 * @param switchRate		The switch rate which switchRate will be set to.
	 * @param numberOfStudents	The spawn limit.
	 * @param jNode				The reference to the Erlang-node on the java part of the program.
	 */
	public StudentSpawner(int sRate, int switchRate, int numberOfStudents, OtpNode jNode){
		iter = 0;
		spawnRate = sRate;
		this.switchRate = switchRate;
		this.numberOfStudents = numberOfStudents;
		javaNode = jNode;
	}
	
	
	/**
	 * The method which will be used to determine if a new student should be spawned or not.
	 * 
	 * @return <code>true</code			If a randomly generated number between 0 and 99 is less than or equal 
	 * to spawnRate. <code>false</code>	Otherwise.
	 */
	public boolean randomSpawn(){
		if((Math.random()*100) < spawnRate){
			return true;
		}
		return false;
	}
	
	/**
	 * The run method fulfilling the Runnable interface. Will try to spawn a new student every second until
	 * <code>iter >= numberOfStudents</code>
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(iter < numberOfStudents)
		{
			if(randomSpawn())
			{
				System.out.println("Number of students " + numberOfStudents);
				System.out.println("Spawning student " + iter);
				Thread t = new Thread(new Communicator(switchRate));
				t.start();
				try {
					spawn_sem .acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
