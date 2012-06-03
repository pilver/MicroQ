package javacom;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpErlangPid;

public class QueueManagerTest {
	
	Simulation sim = new Simulation();
	
	@Test
	public void testQueueManager() {
		QueueManager qm = new QueueManager();
		assertNotNull(QueueManager.Queue);
	}

	@Test
	public void testAddStudent() {
//		QueueManager qm = new QueueManager();
//		Student s = new Student(new OtpErlangPid("test", 1, 2, 3), 0);
//		int sem_status = Simulation.queue_sem.availablePermits();
//		QueueManager.addStudent(s);
//		assertEquals(s, QueueManager.Queue.peek());
//		assertTrue(sem_status < Simulation.queue_sem.availablePermits());
	}

	@Test
	public void testGrabMicrowave() {
//		QueueManager qm = new QueueManager();
//		Student s1 = new Student(new OtpErlangPid("test1", 1, 2, 3), 0);
//		Student s2 = new Student(new OtpErlangPid("test2", 1, 2, 3), 1);
//		QueueManager.addStudent(s1);
//		QueueManager.addStudent(s2);
//		assertEquals(s1, QueueManager.Queue.peek());
//		qm.readyCheck();
//		assertEquals(s2, QueueManager.Queue.peek());
//		qm.readyCheck();
//		assertNull(QueueManager.Queue.peek());
	}

}
