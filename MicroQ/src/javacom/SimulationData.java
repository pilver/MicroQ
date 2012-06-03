package javacom;

public class SimulationData {
	
	/**
	 * This will create an array which will be used for storing the time of which the students
	 * are waiting in the queue.
	 */
	private double[][] waitingTimeStudents = new double[Simulation.numberOfStudents][3];
	private double totalIdleTime;
	
	public SimulationData(){
		initArray();
	}
	
	private void initArray() {
		for (int i = 0; i < Simulation.numberOfStudents; i++) {
			for (int j = 0; j < 3; j++) {
				waitingTimeStudents[i][j] = 0;
			}
		}
	}
	
	public void setStartTime(long currentTimeMillis, int studNum) {
		waitingTimeStudents[studNum][0] = ((double) currentTimeMillis) / 1000;
	}
	
	public void setEndTime(long currentTimeMillis, int studNum) {
		waitingTimeStudents[studNum][1] = ((double) currentTimeMillis) / 1000;
		waitingTimeStudents[studNum][2] +=  waitingTimeStudents[studNum][1] - waitingTimeStudents[studNum][0];
	}
	
	public void printTimeData() {
		for(int i = 0; i < Simulation.numberOfStudents; i++) {
			System.out.println("Time, student " + i + " " + waitingTimeStudents[i][2]);
		}
	}

	public double calculateAverageTimeInQueue() {
		double average = 0;
		for (int i = 0; i < Simulation.numberOfStudents; i++) {
			average += waitingTimeStudents[i][2];
		}
		average /= Simulation.numberOfStudents;
		return average;
	}
	
	public double calculateLongestTimeInQueue() {
		double longest = 0;
		for (int i = 0; i < Simulation.numberOfStudents; i++) {
			if (waitingTimeStudents[i][2] > longest) {
				longest = waitingTimeStudents[i][2];
			}
		}
		return longest;
	}
	
	public double calculateTotalHeatingTime() {
		double total = 0;
		for (int i = 0; i < SimulationMain.microwaves.length; i++) {
			total += SimulationMain.microwaves[i].getTotalTimeOccupied();
		}
		return total;
	}

	public void increaseIdleTime(double idleTime) {
		// TODO Auto-generated method stub
		totalIdleTime += idleTime;
	}
	
	public double getIdletime() {
		return totalIdleTime;
	}

}
