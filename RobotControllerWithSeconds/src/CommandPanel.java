import java.awt.FlowLayout;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class CommandPanel extends JPanel implements ItemListener {
	
	private String commands[] = {"Go forwards", "Reverse", "Turn left", "Turn right", "Forward left", "Forward right", "Reverse left", "Reverse right"};
	private JComboBox choices = new JComboBox(commands);
	private JTextField input = new JTextField(3);
	private JLabel labelTurn = new JLabel("seconds");
	private JLabel labelMove = new JLabel("seconds");
	private JLabel labelUse = new JLabel("seconds");
	private JLabel timeFor = new JLabel(" for ");
	private int instructionNo;
	private JLabel commandNo = new JLabel("");
	private String number;
	
	// Contructor makes panel with drop down box to choose instruction and textfield for user input
	public CommandPanel (int instructionNo) {
		commandNo.setText("Instruction " + instructionNo + ": ");
		add(commandNo);
		add(choices);
		add(timeFor);
		add(input);
		add(labelUse);
		
		choices.addItemListener(this);
	}
	
	/* If drop down box says foward/back change label to 'units', if box says left/right 
	change label to 'degrees'. */
	public void itemStateChanged(ItemEvent e){
		if (choices.getSelectedItem().equals("Turn left") || choices.getSelectedItem().equals("Turn right"))
			labelUse.setText(labelTurn.getText());
			else
			labelUse.setText(labelMove.getText());
	}
	
	public void handleUserEvent(){
		/*Checks for valid input and makes message with standardised format eg. forward020, 
		left120, displays message in window and passes to method in Client*/
		if (input.getText().isEmpty())
			number = "00.0";
		else {
			number = new DecimalFormat("00.0").format(Double.parseDouble(input.getText())); 
		}
		if (choices.getSelectedItem().equals(commands[0])){
			String message = "forward" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[1])){
			String message = "reverse" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[2])){
			String message = "left" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[3])){
			String message = "right" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[4])){
			String message = "forwardleft" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[5])){
			String message = "forwardright" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[6])){
			String message = "reverseleft" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[7])){
			String message = "reverseright" + number;
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
		}
		input.setText("");
	}
	

}

