import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;

import java.net.*;
import java.text.DecimalFormat;


public class RobotUser extends JFrame implements ActionListener {
	
	public static JTextArea display = new JTextArea(12,45);
	private JButton ultra = new JButton("Get ultrasonic sensor value");
	private JButton followLine = new JButton("Follow line");
	private JButton stop = new JButton("Stop");
	private JButton clear = new JButton("Clear textfield");
	private JButton servoinit = new JButton(" ");
	private JPanel ultraPanel = new JPanel();
	private JPanel otherPanel = new JPanel();
	private JPanel pantilt = new JPanel();
	private BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
	private BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
	private BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	private BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	private JMenuBar menuBar = new JMenuBar();
	private JMenu raspberryPi = new JMenu("Raspberry Pi");
	private JMenuItem shutdown = new JMenuItem("Shut down");
	public static ChoosePanelNumber instructionPanel = new ChoosePanelNumber();
	public static ConditionPanel conditionPanel = new ConditionPanel();

	public RobotUser(){
		
		//Sets the layout and adds menu and button to shut down the Raspberry Pi remotely
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(menuBar);
		menuBar.add(raspberryPi);
		raspberryPi.add(shutdown);
		shutdown.addActionListener(this);
		
		//Adds instance of ChoosePanelNumber and ConditionPanel
		add(instructionPanel);
		add(conditionPanel);
		
		//Makes panel with pan/tilt controls
		pantilt.setLayout(new GridLayout(3,3));
		pantilt.add(new JLabel());
		pantilt.add(up);
		up.addActionListener(this);
		pantilt.add(new JLabel());
		pantilt.add(left);
		left.addActionListener(this);
		pantilt.add(servoinit);
		servoinit.addActionListener(this);
		pantilt.add(right);
		right.addActionListener(this);
		pantilt.add(new JLabel());
		pantilt.add(down);
		down.addActionListener(this);
		pantilt.setPreferredSize(new Dimension(510,80));
		addBorder(pantilt, "Pan and tilt controls");
		add(pantilt);
		
		// Makes panel with buttons to get the robot to perform other functions
		otherPanel.add(followLine);
		followLine.addActionListener(this);
		otherPanel.add(stop);
		stop.addActionListener(this);
		otherPanel.setPreferredSize(new Dimension(510,60));
		addBorder(otherPanel, "Other functions");
		add(otherPanel);
		
		
		/* Makes panel with button to get ultrasonic sensor distance and textarea to show what is
		being sent to the raspberry pi */
		ultraPanel.add(ultra);	
		ultra.addActionListener(this);
		ultraPanel.add(clear);
		clear.addActionListener(this);
		ultraPanel.add(display);
		ultraPanel.setPreferredSize(new Dimension(510, 255));
		addBorder(ultraPanel, "Ultrasonic Sensor");
		add(ultraPanel);
		
		
	}
	
	//Handles button presses
	public void actionPerformed(ActionEvent e){
		//If ultra button is pressed, ultrasonic sensor value is retrieved from raspberry pi. 
		if (e.getSource() == ultra){
			Client.handleNetworkEvent("ultra");
			display.append("Ultrasonic sensor value is " + Client.getValue() + "\n");
		}
		//If clear is pressed, textfield will be cleared
		if (e.getSource() == clear)
			display.setText("");
		//If stop is pressed the robot should stop.
		if (e.getSource() == stop){
			Client.handleNetworkEvent("stop");
			display.append("stop\n");
		}
		//If follow line is pressed the robot should follow a line.
		if (e.getSource() == followLine){
			Client.handleNetworkEvent("followline");
			display.append("followline\n");
		}
		/*If RaspberryPi-> Shutdown is chosen a dialogue box will appear asking user to confirm,
		  if 'no' is chosen, nothing will happen. If 'yes' is chosen, the Raspberry Pi will shut
		  down and this program will exit. */
		if (e.getSource() == shutdown){
			Object[] options = {"Yes",
            "No"};
			int n = JOptionPane.showOptionDialog(null, "Are you sure you want to shut down the" +
				" Raspberry Pi?", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
			if (n == 0){
				JOptionPane.showMessageDialog(null, "The Raspberry Pi will shut down and this"
						+ " program will now stop");
				Client.handleNetworkEvent("shutdown");
				display.append("shutdown");
				System.exit(0);
			}
		// Buttons for controlling pan/tilt mount
		}
		if (e.getSource() == up){
			Client.handleNetworkEvent("tiltup");
			display.append("tiltup\n");
		}
		if (e.getSource() == left){
			Client.handleNetworkEvent("panleft");
			display.append("panleft\n");
		}
		if (e.getSource() == right){
			Client.handleNetworkEvent("panright");
			display.append("panright\n");
		}
		if (e.getSource() == down){
			Client.handleNetworkEvent("tiltdown");
			display.append("tiltdown\n");
		}
		if (e.getSource() == servoinit){
			Client.handleNetworkEvent("servoinit");
			display.append("servoinit\n");
		}
		}
	
	//Method for adding a border with title to a JPanel
	public static void addBorder(JPanel panel, String str){
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder title = BorderFactory.createTitledBorder(raisedetched, str);
		panel.setBorder(title);
		
	}
	
	/* Main method creates instance of Client and RobotUser. Window listener causes instructions 
	 to be sent to the raspberry pi to stop the python script and the java program then exits*/
	public static void main(String[] args){
		Client client = new Client("192.168.1.1", 10001);
		RobotUser window = new RobotUser();
		window.setSize(540, 680);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Client.handleNetworkEvent("quit");
				System.exit(0);
			}
		});
		
	}	
}
