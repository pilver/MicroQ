package javacom;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpErlangPid;

public class StudentTest {
	
	@Test
	public void testStudent() {
		Simulation sim = new Simulation();
		OtpErlangPid pid = new OtpErlangPid("test", 1, 2, 3);
		Student s = new Student(pid, 0);
		assertEquals(0, s.getStudentNumber());
		assertTrue(s.getHeatingTime() <= Simulation.heatingTimeMax);
		assertTrue(s.getHeatingTime() >= Simulation.heatingTimeMin);
	}

	@Test
	public void testGetPID() {
		Simulation sim = new Simulation();
		OtpErlangPid pid = new OtpErlangPid("test", 1, 2, 3);
		Student s = new Student(pid, 0);
		assertEquals(pid, s.getPID());
	}

	@Test
	public void testSetPID() {
		Simulation sim = new Simulation();
		OtpErlangPid pid = new OtpErlangPid("test", 1, 2, 3);
		Student s = new Student(pid, 0);
		assertEquals(pid, s.getPID());
		pid = new OtpErlangPid("test2", 1, 2, 3);
		s.setPID(pid);
		assertEquals(pid, s.getPID());
	}

	@Test
	public void testGetHeatingTime() {
		Simulation sim = new Simulation();
		OtpErlangPid pid = new OtpErlangPid("test", 1, 2, 3);
		Student s = new Student(pid, 0);
		assertTrue(s.getHeatingTime() <= Simulation.heatingTimeMax);
		assertTrue(s.getHeatingTime() >= Simulation.heatingTimeMin);
	}

}
