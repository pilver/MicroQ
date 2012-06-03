package javacom;

/**
 * A thread which ensures that a connection between the javaNode and erlNode is established.
 * 
 * @version 1.0
 * @author Simon Evertsson
 *
 */
public class ConnectionCheck implements Runnable {
	
	/**
	 * Pings the java node every 2 seconds until the answer is not pong and then changes SimulationMain.connected to 
	 * false. 
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(Simulation.javaNode.ping(Simulation.erlNodeName, 2000)) {			
		}
		SimulationMain.setConnected(false);
	}

}
