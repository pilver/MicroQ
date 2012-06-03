package javacom;

import com.ericsson.otp.erlang.*;

/**
 * This Communicator class is used to communicate with the Erlang communicator-process. It creates new Student objects
 * aswell as telling QueueManager when to add new students to the queue. Every Communicator instance should be run as 
 * a Thread which invokes its start()-method.
 * 
 * @version 1.0
 * @author Simon Evertsson
 *
 */
public class Communicator implements Runnable {
	
	/**
	 * The unique thread-ID for this thread
	 */
	private String tID;
	
	/**
	 * A new mailbox object, with name the name specified in tID, connected to javaNode
	 */
	private OtpMbox mail;
	
	/**
	 * Determines whether a new client_state-process should be created or not. <code>true</code> if such a process 
	 * should be created, <code>false</code> otherwise.
	 */
	private boolean newClient = false;
	
	/**
	 * The switch rate, converted to and Erlang integer.
	 */
	private OtpErlangInt sRate;
	
	/**
	 * The pid to a student's client_state process.
	 */
	private OtpErlangPid pid;
	
//	private OtpErlangAtom tid;
	
	/**
	 * The message which will be sent to the Erlang communicator-process.
	 */
	private OtpErlangAtom atom;
	
	/*
	 * A reference to the student who's about to heat is food 
	 */
	private Student s;
	
	/**
	 * The constructor for creating a new Erlang client_state-process. sRate will be set to the specified 
	 * switch rate. newClient will be set to true and atom will be set to the Erlang atom 'new_client'.
	 * @param switchRate		The switch rate.		
	 */
	public Communicator(int switchRate)
	{
		sRate = new OtpErlangInt(switchRate);
		newClient = true;
		atom = new OtpErlangAtom("new_client");
	}
	
	/**
	 * <i>Used for testing</i>
	 * @return newClient
	 */
	public boolean isNewClient() {
		return newClient;
	}
	
	/**
	 * <i>Used for testing</i>
	 * @return sRate
	 */
	public OtpErlangInt getsRate() {
		return sRate;
	}
	
	/**
	 * Constructor used when a Student is ready to heat the food and its state should be determined.
	 * Sets s to Student, atom to 'ready' and pid to the Student's PID.
	 * @param s			The student who is ready to grab a microwave
	 */
	public Communicator(Student s)
	{
		this.s = s;
		atom = new OtpErlangAtom("ready");
		pid = s.getPID();
	}

	
	/*
	public static void newStudent(int switchRate, ComThread t, long tid)
	{
		tidMap.put("" + tid, t);
		sendMessage(new OtpErlangLong(tid), new OtpErlangAtom("new_client"), new OtpErlangInt(switchRate));
	}
		
		*/

	/**
	 * <i>Used for testing</i>
	 * @return pid
	 */
	public OtpErlangPid getPid() {
		return pid;
	}
	
	/**
	 * <i>Used for testing</i>
	 * @return atom
	 */
	public OtpErlangAtom getAtom() {
		return atom;
	}
	
	/**
	 * <i>Used for testing</i>
	 * @return s
	 */
	public Student getS() {
		return s;
	}
	
	/**
	 * Sends an Erlang tuple to the communicator-process running on the Erlang node containing this Communicator's
	 * mailbox name, the message atom and an Erlang Object.
	 * 
	 * @param tid			The mailbox name for this instance.
	 * @param atom			A message to distinguish the different requests to Erlang
	 * @param obj			An Erlang Object
	 */
	public void sendMessage(OtpErlangAtom tid, OtpErlangAtom atom, OtpErlangObject obj) {
		OtpErlangObject[] message = new OtpErlangObject[3];
		message[0] = tid;
		message[1] = atom;
		message[2] = obj;	
		mail.send(Simulation.erlNodePID, new OtpErlangTuple(message));
	}
	
	
	/**
	 * The run method for fulfilling the Runnable interface. This method will use the parameters which were set in
	 * the constructor to determine what to do. It can either request a new Erlang client_state-pid to create a new
	 * Student and add it to the queue. Or it will send a ready request to an existing Erlang client_state-process
	 * (via the Erlang communicator-process). 
	 * 
	 *  If a new client is requested an Erlang tuple containing {tID, 'new_client', sRate} will be sent. This thread
	 *  will then wait until an answer containing the pid to the newly created Erlang client_state-process is 
	 *  received. The pid will then be used to create a new Student object which will be added to the queue. Finally
	 *  the Simulation.queue_sem will be released.
	 *  
	 *  If a student is ready to grab a microwave an Erlang tuple containing {tID, 'ready', pid} will be sent. This
	 *  thread will then wait until an answer is received or the timeout-limit, defined by Student.heatingTime is 
	 *  reached. If the latter occurs the student who was ready will be re-added to the back of the queue. Otherwise if
	 *  a 'client_ready'-message is received the thread will sleep for the time defined in Student.heatingTime. When 
	 *  the thread wakes up again it will release the Simulation.micro_sem
	 */
	@Override
	public void run() {
		OtpErlangTuple tuple = null;
		tID = ""+Thread.currentThread().getId();
		mail = Simulation.javaNode.createMbox(tID);
		if(newClient)
		{	
			sendMessage(new OtpErlangAtom(tID), atom, sRate);
			System.out.println("new_client message sent...");
			try {
				tuple = (OtpErlangTuple) mail.receive();
				System.out.println("new_client created with PID " + tuple.elementAt(1).toString());
			}
			catch (OtpErlangExit e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (OtpErlangDecodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			int i;
			synchronized(StudentSpawner.iterSync) {
				i = StudentSpawner.getIter();
				s = new Student((OtpErlangPid)tuple.elementAt(1), i);
				StudentSpawner.iter++;
			}
			StudentSpawner.spawn_sem.release();
			QueueManager.addStudent(s);
		}
		else
		{
			sendMessage(new OtpErlangAtom(tID), atom, pid);
			System.out.println("Ready-request sent to PID: " + s.getPID());
			long currTime = System.currentTimeMillis();
			try {
				tuple = (OtpErlangTuple) mail.receive(s.getWaitingTime());
			}
			catch (OtpErlangExit e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (OtpErlangDecodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			double idleTime = ((double)(System.currentTimeMillis() - currTime))/1000;
			SimulationMain.simData.increaseIdleTime(idleTime);
			
			
			if(tuple == null) {
				System.out.println("mail receive timeout...");
				Simulation.microwave_sem.release();
				QueueRepresentation.popWaitingQueue(s.getStudentNumber());
				QueueManager.addStudent(s);
			}
			else {
				QueueRepresentation.popWaitingQueue(s.getStudentNumber());
				System.out.println("student is ready");
				int i = SimulationMain.availableMicros.remove().intValue();
				SimulationMain.microwaves[i].setTime((int) (s.getHeatingTime()/1000));
				SimulationMain.microwaves[i].setCurrentStudentNum(s.getStudentNumber());
				SimulationMain.microwaves[i].getTimer().start();
				try {
					Thread.sleep(s.getHeatingTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendMessage(new OtpErlangAtom(tID), new OtpErlangAtom("kill"), pid);
				while(SimulationMain.microwaves[i].getTime() > 0) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Stuck in loop...");
				}
				SimulationMain.availableMicros.add(i);
				Simulation.studentsCompleted++;
				Simulation.microwave_sem.release();
				if(Simulation.studentsCompleted >= Simulation.numberOfStudents)
				{
					Simulation.queue_sem.release();
				}
				System.out.println("Students completed: " + Simulation.studentsCompleted + ", when queue has " + 
						Simulation.queue_sem.availablePermits() + " permits and microwave sem has " +
						Simulation.microwave_sem.availablePermits() + " permits...");
			}
		}
		// TODO Auto-generated method stub
		
	}

}
