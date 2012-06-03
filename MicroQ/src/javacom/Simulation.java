package javacom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Semaphore;

import com.ericsson.otp.erlang.*;

/**
 * 
 */

/**
 * @author Simon
 *
 */
public class Simulation implements ActionListener, Runnable{
	
	
	static String erlNodeName;
	static String javaNodeName;
	static OtpNode javaNode;
	static OtpMbox mbox;
	static OtpErlangPid erlNodePID;

	
	
	public static QueueManager queue;
	public static Thread spawner;	
	public static Communicator com;
	
	public static Semaphore queue_sem;
	public static Semaphore microwave_sem;
	
	static int spawnRate = 50;
	private static int switchRate = 50; 
	public static int studentsCompleted = 0;
	
	static InputBox window;
	static int numberOfStudents;
	static int heatingTimeMin;
	static int heatingTimeMax;
	static int waitingTime;
	
	Simulation() {
		try {
			erlNodeName = "erlcom@" + InetAddress.getLocalHost().getHostName();
			javaNodeName = "javacom@" + InetAddress.getLocalHost().getHostName();
			javaNode = new OtpNode(javaNodeName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mbox = javaNode.createMbox("mbox");
		spawnRate = 50;
		switchRate = 50;
		numberOfStudents = 50;
		heatingTimeMin = 10000;
		heatingTimeMax = 30000;
		waitingTime = 150000;
		queue_sem = new Semaphore(0);
		microwave_sem = new Semaphore(4);
		queue = new QueueManager();
//		spawner = new Thread(new StudentSpawner(spawnRate, switchRate, numberOfStudents, javaNode));
//		spawner.start();
	}
	
	public Simulation(int sR, int nOS, int hTMax, int hTMin, int wT, int mW, int swR){		
		spawnRate = sR;
		switchRate = swR;
		numberOfStudents = nOS;
		heatingTimeMin = hTMin;
		heatingTimeMax = hTMax;
		waitingTime = wT;
		queue_sem = new Semaphore(0);
		microwave_sem = new Semaphore(mW);
		queue = new QueueManager();
		spawner = new Thread(new StudentSpawner(spawnRate, switchRate, numberOfStudents, javaNode));
		// TODO Auto-generated constructor stub
	}


	private static void establishConnection() {
		System.out.println("Establishing connection...");
		try {
			OtpErlangObject objectPID;
			erlNodeName = "erlcom@" + InetAddress.getLocalHost().getHostName();
			javaNodeName = "javacom@" + InetAddress.getLocalHost().getHostName();
			javaNode = new OtpNode(javaNodeName);
			mbox = javaNode.createMbox("mbox");
			
			objectPID = mbox.receive();
			erlNodePID = (OtpErlangPid) objectPID;
			
			Thread t = new Thread(new ConnectionCheck());
			t.start();
			
			System.out.println(erlNodePID.toString());
		
			System.out.println("Connection to erlcom established");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OtpErlangExit e) {
			e.printStackTrace();
		} catch (OtpErlangDecodeException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param numOfMicros 
	 * @param wT 
	 * @param hT 
	 * @param nOS 
	 * @return 
	 * 
	 */
/*	static void init(int sR, int nOS, int hTMin, int hTMax, int wT, int microwaveAmount, int swR) {
		establishConnection();
		spawnRate = sR;
		numberOfStudents = nOS;
		heatingTimeMin = hTMin;
		heatingTimeMax = hTMax;
		waitingTime = wT;
		queue_sem = new Semaphore(0);
		microwave_sem = new Semaphore(microwaveAmount);
		queue = new QueueManager();
		spawner = new Thread(new StudentSpawner(spawnRate, swR));
		spawner.start();
		runSimulation();
	} */

	private static void runSimulation() {
		while(studentsCompleted < numberOfStudents)
		{
			try {
				queue_sem.acquire();
				System.out.println("Students completed in simulation: " + studentsCompleted);
				if(studentsCompleted >= numberOfStudents)
				{
					break;
				}
				microwave_sem.acquire();
				System.out.println("micro is ready");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			queue.readyCheck();
			//System.out.println("Students completed in simulation: " + studentsCompleted);
		}
		
		System.out.println("Outside Simulation loop...");
		
		OtpErlangObject[] message = new OtpErlangObject[2];
		message[0] = new OtpErlangAtom("mbox");
		message[1] = new OtpErlangAtom("terminate");	
		mbox.send(Simulation.erlNodePID, new OtpErlangTuple(message));
		try {
			mbox.receive();
		} catch (OtpErlangExit e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OtpErlangDecodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimulationMain.isRunning = false;
	}
	


	@Override
	public void actionPerformed(ActionEvent e){
		
		//Kör igång
		System.out.println("Här händer det grejer!");
		
		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		establishConnection();
		spawner.start();
		runSimulation();
	}

}
