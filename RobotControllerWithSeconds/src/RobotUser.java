import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.DefaultCaret;



public class RobotUser extends JFrame implements ActionListener{

	public static JTextArea messageDisplay = new JTextArea(22,45);
	public static JTextArea ultraDisplay = new JTextArea(10,45);
	private JScrollPane messageScrollPane = new JScrollPane(messageDisplay);
	private JScrollPane ultraScrollPane = new JScrollPane(ultraDisplay);
	private JButton ultra = new JButton("Receive ultrasonic sensor values");
	private JButton followLine = new JButton("Follow line");
	private JButton stop = new JButton("Stop");
	private JButton clearUltra = new JButton("Clear values");
	private JButton clearMessage = new JButton("Clear messages");
	private JButton servoInit = new JButton(" ");
	private JPanel ultraPanel = new JPanel();
	private JPanel otherPanel = new JPanel();
	private JPanel pantilt = new JPanel();
	private JPanel messagePanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
	private BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
	private BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
	private BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
	private JMenuBar menuBar = new JMenuBar();
	private JMenu raspberryPi = new JMenu("Raspberry Pi");
	private JMenuItem shutdown = new JMenuItem("Shut down");
	private JTabbedPane tabbedPane = new JTabbedPane();
	public static ChoosePanelNumber instructionPanel = new ChoosePanelNumber();
	public static ConditionPanel conditionPanel = new ConditionPanel();

	public RobotUser(){
		
		//Sets the layout and adds menu and button to shut down the Raspberry Pi remotely
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(menuBar);
		menuBar.add(raspberryPi);
		raspberryPi.add(shutdown);
		shutdown.addActionListener(this);

		//Sets layout of mainPanel, for first tab of tabbedPane.
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		//Adds instance of ChoosePanelNumber and ConditionPanel to mainPanel
		mainPanel.add(instructionPanel);
		mainPanel.add(conditionPanel);
		
		//Makes panel with pan/tilt controls and adds to mainPanel
		pantilt.setLayout(new GridLayout(3,3));
		pantilt.add(new JLabel());
		pantilt.add(up);
		up.addActionListener(this);
		pantilt.add(new JLabel());
		pantilt.add(left);
		left.addActionListener(this);
		pantilt.add(servoInit);
		servoInit.addActionListener(this);
		pantilt.add(right);
		right.addActionListener(this);
		pantilt.add(new JLabel());
		pantilt.add(down);
		down.addActionListener(this);
		pantilt.setPreferredSize(new Dimension(510,80));
		addBorder(pantilt, "Pan and tilt controls");
		mainPanel.add(pantilt);
		
		/* Makes panel with buttons to get the robot to perform other functions and adds to
		   mainPanel */
		otherPanel.add(followLine);
		followLine.addActionListener(this);
		otherPanel.add(stop);
		stop.addActionListener(this);
		otherPanel.setPreferredSize(new Dimension(510,60));
		addBorder(otherPanel, "Other functions");
		mainPanel.add(otherPanel);
		
		/* Makes panel with button to get ultrasonic sensor distance and adds to mainPanel */
		ultraPanel.add(ultra);	
		ultra.addActionListener(this);
		ultraPanel.add(clearUltra);
		clearUltra.addActionListener(this);
		ultraPanel.add(ultraScrollPane);
		DefaultCaret caret1 = (DefaultCaret) ultraDisplay.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		ultraPanel.setPreferredSize(new Dimension(510, 220));
		addBorder(ultraPanel, "Ultrasonic Sensor");
		mainPanel.add(ultraPanel);
		
		//ultraPanel.add(clear);
		//clear.addActionListener(this);
		
		//Adds display to messagePanel to display what is being sent to the Raspberry Pi
		messagePanel.add(clearMessage);
		clearMessage.addActionListener(this);
		messagePanel.add(messageScrollPane);
		DefaultCaret caret2 = (DefaultCaret) messageDisplay.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		
		// Sets up tabs for tabbedPane and adds to UI	
		tabbedPane.addTab("Robot Controller", mainPanel);
		tabbedPane.addTab("Sent Messages", messagePanel);
		add(tabbedPane);
		
		
	}

	
	//Handles button presses
	public void actionPerformed(ActionEvent e){
		//If ultra button is pressed, ultrasonic sensor value is retrieved from raspberry pi. 
		if (e.getSource() == ultra){
			Client.handleNetworkEvent("ultra");
			if (UltrasonicGetter.getUltra == false){
				ultra.setText("Stop receiving ultrasonic sensor values");
				UltrasonicGetter.getUltra = true;
			}
			else {
				UltrasonicGetter.getUltra = false;
				ultra.setText("Receive ultrasonic sensor values");
			}
		}
		//If clear is pressed, textfield will be cleared
		if (e.getSource() == clearUltra)
			ultraDisplay.setText("");
		if (e.getSource() == clearMessage)
			messageDisplay.setText("");
		//If stop is pressed the robot should stop.
		if (e.getSource() == stop){
			Client.handleNetworkEvent("stop");
		}
		//If follow line is pressed the robot should follow a line.
		if (e.getSource() == followLine)
			Client.handleNetworkEvent("followline");
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
				System.exit(0);
			}
		// Buttons for controlling pan/tilt mount
		}
		if (e.getSource() == up)
			Client.handleNetworkEvent("tiltup");
		if (e.getSource() == left)
			Client.handleNetworkEvent("panleft");
		if (e.getSource() == right)
			Client.handleNetworkEvent("panright");
		if (e.getSource() == down)
			Client.handleNetworkEvent("tiltdown");
		if (e.getSource() == servoInit)
			Client.handleNetworkEvent("servoinit");
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
		new Thread(new UltrasonicGetter()).start();
		RobotUser window = new RobotUser();
		window.setSize(540, 700);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Client.handleNetworkEvent("quit");
				System.exit(0);
			}
		});
		
	}	
}
