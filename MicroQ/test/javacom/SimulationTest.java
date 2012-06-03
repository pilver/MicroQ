package javacom;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpNode;

public class SimulationTest {

	@Test
	public void testSimulation() {
		String testNodeName;
		try {
			testNodeName = "erlcom@" + InetAddress.getLocalHost().getHostName();
			OtpNode testNode = new OtpNode(testNodeName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Simulation sim = new Simulation();
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

}
