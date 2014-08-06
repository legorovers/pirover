import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.net.*;
import java.text.DecimalFormat;


public class RobotUser extends JFrame implements ActionListener {
	
	public static JTextArea display = new JTextArea(12,45);
	private JButton ultra = new JButton("Get ultrasonic sensor value");
	private JButton followLine = new JButton("Follow line");
	private JButton stop = new JButton("Stop");
	private JButton clear = new JButton("Clear textfield");
	private JPanel ultraPanel = new JPanel();
	private JPanel otherPanel = new JPanel();
	public static ChoosePanelNumber instructionPanel = new ChoosePanelNumber();
	public static ConditionPanel conditionPanel = new ConditionPanel();

	public RobotUser(){
		
		//Sets the layout and adds instance of ChoosePanelNumber and ConditionPanel
		setLayout(new FlowLayout());
		add(instructionPanel);
		add(conditionPanel);
		
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
		//ultraPanel.setLayout(new BoxLayout(ultraPanel, BoxLayout.Y_AXIS));
		addBorder(ultraPanel, "Ultrasonic Sensor");
		add(ultraPanel);
		
		
	}
	
	/*If ultra button is pressed, ultrasonic sensor value is retrieved from raspberry pi. If stop
	  is pressed the robot should stop. If follow line is pressed the robot should follow
	  a line */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == ultra){
			Client.handleNetworkEvent("ultra");
			display.append("Ultrasonic sensor value is " + Client.getValue() + "\n");
		}
		if (e.getSource() == clear)
			display.setText("");
		if (e.getSource() == stop){
			Client.handleNetworkEvent("stop");
			display.append("stop\n");
		}
		if (e.getSource() == followLine){
			Client.handleNetworkEvent("followline");
			display.append("followline\n");
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
		window.setSize(540, 590);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Client.handleNetworkEvent("quit");
				System.exit(0);
			}
		});
		
	}	
}
