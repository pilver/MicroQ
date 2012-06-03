package javacom;

import com.ericsson.otp.erlang.OtpErlangPid;
/**
 * The Student class which will represent a student. Contains information about heating time, time to wait after
 * a ready-check to erlang.
 * 
 * @version 1.0
 * @author Marcus Enderskog
 *
 */
public class Student {

	/**
	 * The Erlang-pid to this Student's state-process
	 */
	private OtpErlangPid PID;
	
	/**
	 * The ready check time limit in ms
	 */
	private long waitingTime;
	/**
	 * The Student's heating time. The reserved microwave will be occuppied for a maximum time of
	 * waitingTime + heatingtime
	 */
	private long heatingTime;
	
	/**
	 * The individual number of this Student object
	 */
	private int studentNumber;
	
	/**
	 * The Student constructor which sets the @see PID to an Erlang-pid . The waitingTime and heatingTime fields
	 * will be set with the static fields waitingTime respectively between the heatingTimeMin and heatingTimeMax
	 * in the Simulation class. The timer will also start its counting.
	 * @see Simulation
	 *  
	 * @param PID		An Erlang-pid which should correspond to a client_state-process in Erlang		
	 * @param i 
	 */	
	public Student(OtpErlangPid PID, int i) {
		this.PID = PID;
		this.waitingTime = Simulation.waitingTime;
		heatingTime = (int) (Simulation.heatingTimeMin + 
				Math.random()*(Simulation.heatingTimeMax - Simulation.heatingTimeMin));
		studentNumber = i;
	}


	/**
	 * 
	 * @return		<code>PID</code>, the pid to this Student's client_state-process. 
	 */
	public OtpErlangPid getPID() {
		return PID;
	}
	
	/**
	 * @deprecated
	 * Sets PID to a new pid.
	 * 
	 * @param pID2		The new pid which PID will be updated to.
	 */
	public void setPID(OtpErlangPid pID2) {
		PID = pID2;
	}
	
	/**
	 *  Returns the Student's heating time.
	 *  
	 * @return heatingTime
	 */
	public long getHeatingTime() {
		// TODO Auto-generated method stub
		return heatingTime;
	}
	
	/**
	 * Returns the Student's waitingTime.
	 * 
	 * @return waitingTime
	 */
	public long getWaitingTime() {
		// TODO Auto-generated method stub
		return waitingTime;
	}
	
	/**
	 * Returns this student's student number.
	 * 
	 * @return studentNumber
	 */
	public int getStudentNumber() {
		return studentNumber;
	}

}
