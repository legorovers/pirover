import java.awt.FlowLayout;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class CommandPanel extends JPanel {
	
	private String commands[] = {"Go forwards", "Reverse", "Turn left", "Turn right", "Forward left", "Forward right", "Reverse left", "Reverse right"};
	private JComboBox choices = new JComboBox(commands);
	private JTextField input = new JTextField(3);
	private JLabel labelSeconds = new JLabel("seconds");
	private JLabel forLabel = new JLabel(" for ");
	private int instructionNo;
	private JLabel commandNo = new JLabel("");
	private String number;
	
	// Contructor makes panel with drop down box to choose instruction and textfield for user input
	public CommandPanel (int instructionNo) {
		commandNo.setText("Instruction " + instructionNo + ": ");
		add(commandNo);
		add(choices);
		add(forLabel);
		add(input);
		add(labelSeconds);
		
	}
	
	public void handleUserEvent(){
		/*Checks for valid input and makes message with standardised format eg. forward02.5, 
		left10.2, displays message in window and passes to method in Client*/
		if (input.getText().isEmpty())
			number = "00.0";
		else {
			number = new DecimalFormat("00.0").format(Double.parseDouble(input.getText())); 
		}
		if (choices.getSelectedItem().equals(commands[0])){
			String message = "forward" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[1])){
			String message = "reverse" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[2])){
			String message = "left" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[3])){
			String message = "right" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[4])){
			String message = "forwardleft" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[5])){
			String message = "forwardright" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[6])){
			String message = "reverseleft" + number;
			Client.handleNetworkEvent(message);
		}
		if (choices.getSelectedItem().equals(commands[7])){
			String message = "reverseright" + number;
			Client.handleNetworkEvent(message);
		}
		/*Removes text from fields after 'go' is pressed. Have commented out as sometimes it
		is not desireable*/
		//input.setText("");
	}
	

}

