package javacom;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResultBox extends JFrame implements Runnable {

	JPanel jp = new JPanel(new GridLayout(7, 2));
	JButton button = new JButton("Exit");
	public ResultBox(double totalSimulationTime) {
    	setTitle("Simulation Results");
		setVisible(true);
		setSize(400,300);
		setDefaultCloseOperation(3);
		
		double totalIdleTime = SimulationMain.simData.getIdletime();
		double averageIdleTime = totalIdleTime/SimulationMain.microwaves.length;
		String[] labelStrings = {
				"Total simulation time: ",
	            "Average time in queue: ",
	            "Longest time in queue: ",
	            "Total heating time: ",
	            "Total time idle, microwave: ",
	            "Average idle time, microwave: "
	        };
		String[] resultStrings = {
				format(totalSimulationTime),
	            format(SimulationMain.simData.calculateAverageTimeInQueue()),
	            format(SimulationMain.simData.calculateLongestTimeInQueue()),
	            format(SimulationMain.simData.calculateTotalHeatingTime()),
	            format(totalIdleTime),
	            format(averageIdleTime)
	        };
		JLabel[] labels = new JLabel[labelStrings.length];
		JLabel[] results = new JLabel[labelStrings.length];
		for (int i = 0; i < labelStrings.length; i++) {
            
			labels[i] = new JLabel(labelStrings[i],
                                   JLabel.TRAILING);
			results[i] = new JLabel(resultStrings[i],
                    JLabel.LEFT);
            jp.add(labels[i]);
            jp.add(results[i]);
		}

		jp.add(new JLabel(""));
		jp.add(button);
		
		add(jp);
	}
	
	private String format(double d) {
		return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SimulationMain.setup_done.release();
				//System.exit(0);
			}
				
		});
		
	}

}
