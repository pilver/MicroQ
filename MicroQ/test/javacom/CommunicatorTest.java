package javacom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangRangeException;
import com.ericsson.otp.erlang.OtpNode;

public class CommunicatorTest {
	 Simulation s = new Simulation();

	 @Test
	 public void testCommunicatorInt() {
	 Communicator c = new Communicator(50);
	 try {
	 assertEquals(50,c.getsRate().intValue());
	 assertEquals(true, c.isNewClient());
	 assertEquals("new_client", c.getAtom().atomValue());
	 } catch (OtpErlangRangeException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 }
	 }

	@Test
	public void testCommunicatorStudent() {
		// Student s = new Student(new OtpErlangPid("test", 7, 3, 5));
		// Communicator c = new Communicator(s);
		// assertEquals(s,c.getS());
		// assertEquals(false, c.isNewClient());
		// assertEquals("ready", c.getAtom().atomValue());
		// assertEquals(s.getPID(), c.getPid());
		//
	}

}
