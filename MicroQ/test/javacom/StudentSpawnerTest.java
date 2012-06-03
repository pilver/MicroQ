package javacom;

import static org.junit.Assert.*;

import org.junit.Test;

public class StudentSpawnerTest {

	Simulation s = new Simulation();
	
	@Test
	public void testStudentSpawner() {
		StudentSpawner ss = new StudentSpawner(50, 50, 50, Simulation.javaNode);
		assertEquals(50, ss.getSpawnRate());
		assertEquals(50, ss.getSwitchRate());
		assertEquals(50, ss.getNumberOfStudents());
		assertEquals(Simulation.javaNode, ss.getJavaNode());
	}
		

	@Test
	public void testRun() {
		StudentSpawner ss = new StudentSpawner(100, 10, 5, Simulation.javaNode);
		Thread t  = new Thread(ss);
		t.start();
		for(int i = 0; i < 5; i++) {
			StudentSpawner.iter++;
		}
		assertEquals(5, StudentSpawner.iter);
	}

}
