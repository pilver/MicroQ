package javacom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** 
 * Main class for the game 
 */ 
public class SimulationMain extends JFrame 
{        
        static boolean isRunning = true; 
        int fps = 30; 
        public JFrame window = this;
        int windowWidth = 500; 
        int windowHeight = 500;
        
        static SimulationData simData;
        
        private QueueRepresentation queueRep;
        static Microwave[] microwaves;
		static Thread simulation;
		public static ConcurrentLinkedQueue<Integer> availableMicros = new ConcurrentLinkedQueue<Integer>();
		static Semaphore setup_done = new Semaphore(0);
		private static boolean connected = true;
		private Thread simulationSetup;
		
        BufferedImage backBuffer; 
        Insets insets; 
        //InputHandler input; 
        
        int x = 0;
		private double totalSimulationTime; 
        
        public static void main(String[] args) 
        { 
            SimulationMain simMain = new SimulationMain(); 
            simMain.run(); 
            System.exit(0); 
        } 
        
        /** 
         * This method starts the game and runs it in a loop 
         */ 
        public void run() 
        {
        	simulationSetup = new Thread(new InputBox());
        	simulationSetup.start();
            try {
				setup_done.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            System.out.println("Setup done...");
            
            initialize(); 
    		System.out.println("Init done...");
            
    		simData = new SimulationData();
    		
    		simulation.start();
    		long simulationStartTime = System.currentTimeMillis();
            
            
            while(isRunning && connected) 
            { 
                    long time = System.currentTimeMillis(); 
                    
                    
                    draw(); 
                    
                    //  delay for each frame  -   time it took for one frame 
                    time = (1000 / fps) - (System.currentTimeMillis() - time); 
                    
                    if (time > 0) 
                    { 
                            try 
                            { 
                                    Thread.sleep(time); 
                            } 
                            catch(Exception e){} 
                    } 
            }
            if(connected) {
            	totalSimulationTime = ((double)System.currentTimeMillis() - simulationStartTime)/1000;
            	//JOptionPane.showMessageDialog(this, "Simulation done!");
            	setVisible(false);
            	displaySimulationData();
            }
            else {
            	JOptionPane.showMessageDialog(this, "Connection lost!");
            }
            setVisible(false); 
        } 
        
        private void displaySimulationData() {
			// TODO Auto-generated method stub
        	Thread t = new Thread(new ResultBox(totalSimulationTime));
        	t.start();
        	try {
				setup_done.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//        	JFrame resultFrame = new JFrame();
//        	
//        	resultFrame.setTitle("Simulation Results");
//    		resultFrame.setVisible(true);
//    		resultFrame.setSize(300,300);
//    		resultFrame.setDefaultCloseOperation(3);
//    		JPanel jp = new JPanel(new GridLayout());
//    		String[] labelStrings = {
//    	            "Average time in queue: ",
//    	            "Longest time in queue: ",
//    	            "Total heating time: ",
//    	            "Total time idle, microwave: ",
//    	            "Average idle time, microwave: "
//    	        };
//    		double totalIdleTime = totalSimulationTime - simData.calculateTotalHeatingTime();
//    		double averageIdleTime = totalIdleTime/microwaves.length;
//    		String[] resultStrings = {
//    	            ""+simData.calculateAverageTimeInQueue(),
//    	            ""+simData.calculateLongestTimeInQueue(),
//    	            ""+simData.calculateTotalHeatingTime(),
//    	            ""+totalIdleTime,
//    	            ""+averageIdleTime
//    	        };
//    		JLabel[] labels = new JLabel[labelStrings.length];
//    		JLabel[] results = new JLabel[labelStrings.length];
//    		for (int i = 0; i < labelStrings.length; i++) {
//                
//    			labels[i] = new JLabel(labelStrings[i],
//                                       JLabel.TRAILING);
//    			results[i] = new JLabel(resultStrings[i],
//                        JLabel.TRAILING);
//                jp.add(labels[i]);
//                jp.add(results[i]);
//    		}
//
//    		jp.add(new JLabel(""));
//    		JButton button = new JButton("Exit");
//			jp.add(button);
//    		
//    		resultFrame.add(jp);
//    		button.addActionListener(new ActionListener(){
//    			public void actionPerformed(ActionEvent e){
//    				//System.exit(0);
//    			}
//    				
//    		});
		}

        
        void initialize() 
        { 
                setTitle("MicroQ"); 
                setSize(windowWidth, windowHeight); 
                setResizable(false); 
                setDefaultCloseOperation(EXIT_ON_CLOSE); 
                setVisible(true);
                
                queueRep = new QueueRepresentation();
                
                insets = getInsets(); 
                setSize(insets.left + windowWidth + insets.right, 
                                insets.top + windowHeight + insets.bottom); 
                
                backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB); 
                //input = new InputHandler(this);
        } 

        
        void draw() 
        {               
                Graphics g = getGraphics();
                Graphics bbg = backBuffer.getGraphics();
                
                bbg.setColor(Color.WHITE); 
                bbg.fillRect(0, 0, windowWidth, windowHeight); 
                
                for(int i = 0; i < microwaves.length; i++) {
                	microwaves[i].draw(bbg);
                }
                
                queueRep.draw(bbg);
                
                g.drawImage(backBuffer, insets.left, insets.top, this);
        }

		public static void setConnected(boolean b) {
			// TODO Auto-generated method stub
			connected = b;
		} 
} 