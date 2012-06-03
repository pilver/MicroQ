package javacom;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * The input box where simulation parameters are set.
 * 
 * @version 1.0
 * @author Marcus Enderskog
 *
 */
public class InputBox extends JFrame implements Runnable {
	
	/**
	 * The panel which holds all fields.
	 */
	JPanel jp = new JPanel(new GridLayout(8,0));
	
	/**
	 * The spawn rate input field.
	 */
	JTextField srfield = new JTextField("50", 10);
	
	/**
	 * The switch rate input field.
	 */
	JTextField swfield = new JTextField("50", 10);
	
	/**
	 * The student amount input field.
	 */
	JTextField stfield = new JTextField("2", 10);
	
	/**
	 * The minimum heating time input field.
	 */
	JTextField htminfield = new JTextField("3", 10);
	
	/**
	 * The maximum heating time input field.
	 */
	JTextField htmaxfield = new JTextField("4", 10);
	
	/**
	 * The waiting time  input field.
	 */
	JTextField wtfield = new JTextField("15", 10);
	
	/**
	 * The microwave amount input field.
	 */
	JTextField mcfield = new JTextField("4", 10);
	
	/**
	 * The "Start simulation" button
	 */
	JButton button = new JButton("Start simulation");
	
	/**
	 * The input box constructor, displays the input box and adds all input fields to jp and finally adds jp to 
	 * the Jframe.
	 */
	public InputBox(){
		setTitle("Micro-Q Simulator");
		setVisible(true);
		setSize(300,300);
		setDefaultCloseOperation(3);
		
		String[] labelStrings = {
	            "Spawn rate (%): ",
	            "Switch rate (%): ",
	            "No. of students: ",
	            "Min. heating time (s): ",
	            "Max. heating time (s): ",
	            "Waiting time (s): ",
	            "No. of microwaves: "
	        };
		JLabel[] labels = new JLabel[labelStrings.length];
		JTextField[] textFields = new JTextField[labelStrings.length];
		textFields[0] = srfield;
		textFields[1] = swfield;
		textFields[2] = stfield;
		textFields[3] = htminfield;
		textFields[4] = htmaxfield;
		textFields[5] = wtfield;
		textFields[6] = mcfield;
		for (int i = 0; i < labelStrings.length; i++) {
            
			labels[i] = new JLabel(labelStrings[i],
                                   JLabel.TRAILING);
            labels[i].setLabelFor(textFields[i]);
            jp.add(labels[i]);
            jp.add(textFields[i]);
		}
//		jp.add(srfield);
//		jp.add(swfield);
//		jp.add(stfield);
//		jp.add(htminfield);
//		jp.add(htmaxfield);
//		jp.add(wtfield);
//		jp.add(mcfield);
//		
		jp.add(new JLabel(""));
		jp.add(button);
		
		add(jp);
	
	}
	
	/**
	 * Adds an actionListener to button which saves the current values in each input field, hides the input box
	 * create as many Microwave objects that were specified. Finally it constructs a new Simulation object with
	 * given parameters and releases the SimulationMain.setup_done-semaphore.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int sR = Integer.parseInt(srfield.getText());
				int swR = Integer.parseInt(swfield.getText());
				int nOS = Integer.parseInt(stfield.getText());
				int hTMax = Integer.parseInt(htmaxfield.getText())*1000;
				int hTMin = Integer.parseInt(htminfield.getText())*1000;
				int wT = Integer.parseInt(wtfield.getText())*1000;
				int mW = Integer.parseInt(mcfield.getText());
				SimulationMain.microwaves = new Microwave[mW];
				for(int i = 0; i < mW; i++) {
					SimulationMain.microwaves[i] = new Microwave(i);
					SimulationMain.availableMicros.add(i);
				}
				InputBox.this.setVisible(false);
				SimulationMain.simulation = new Thread(new Simulation(sR, nOS, hTMax, hTMin, wT, mW, swR));
				
				SimulationMain.setup_done.release();
			}
				
		});		
	}

}