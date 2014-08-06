import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class ConditionPanel extends JPanel implements ItemListener{
	
	private JCheckBox checkbox = new JCheckBox();
	private JLabel ifLabel = new JLabel("If an obstacle is detected, then");
	private JLabel unitLabel = new JLabel("cm, then");
	private JTextField distanceInput = new JTextField(3);
	private String commands[] = {"reverse", "turn left", "turn right", "reverse left", "reverse right", "stop"};
	private JComboBox choices = new JComboBox(commands);
	private JTextField valueInput = new JTextField(3);
	private JLabel labelTurn = new JLabel("seconds");
	private JLabel labelMove = new JLabel("seconds");
	private JLabel labelUse = new JLabel("seconds");
	private JLabel timeFor = new JLabel("for");	
	
	//Constructor makes JPanel with checkbox, drop down menu and textfield for user input
	public ConditionPanel(){
		
		add(checkbox);
		add(ifLabel);
		//add(distanceInput);
		//add(unitLabel);
		add(choices);
		add(timeFor);
		add(valueInput);
		add(labelUse);
		
		choices.addItemListener(this);
		
		this.setPreferredSize(new Dimension(510, 60));		
		RobotUser.addBorder(this, "Conditions");
		
		
	}
	
	/*Gets user's chosen distance to trigger the command from the first textfield (not used in
	  this version) */
	public static String getTriggerDistance(ConditionPanel panel){
		if (panel.distanceInput.getText().isEmpty())
			return "000";
		else
			return new DecimalFormat("000").format(Double.parseDouble(panel.distanceInput.getText()));
	}
	
	// Gets user's chosen command from drop down box and returns appropriate string
	public String getCommand(){
		if (choices.getSelectedItem().equals(commands[0]))
			return "reverse";
		if (choices.getSelectedItem().equals(commands[1]))
			return "left";
		if (choices.getSelectedItem().equals(commands[2]))
			return "right";
		if (choices.getSelectedItem().equals(commands[3]))
			return "reverseleft";
		if (choices.getSelectedItem().equals(commands[4]))
			return "reverseright";
		if (choices.getSelectedItem().equals(commands[5]))
			return "stop";
		else return "";
	}
	
	/* Gets user input from the second textfield and puts it in standard format e.g. 05.2 or 
	   10.0. If user has chosen 'stop' as their command there is no input so sets it as 00.0 */
	public String getValue(){
		if(valueInput.getText().isEmpty())
			return "00.0";
		else
			return new DecimalFormat("00.0").format(Double.parseDouble(valueInput.getText()));
	}
	
	/* If the checkbox is ticked when the user presses go then a string is sent to the
	 raspberry pi which assigns the input values to variables that can be used by the robot */
	public static void handleConditionEvent(ConditionPanel panel){
		if (panel.checkbox.isSelected()){
			String message = "y" + getTriggerDistance(panel) + 
					panel.getCommand() + panel.getValue();
			RobotUser.display.append(message + "\n");
			Client.handleNetworkEvent(message);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			
		}
		else {
			Client.handleNetworkEvent("n");
			RobotUser.display.append("n");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1){
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/* If drop down box says forward/back change JLabel to 'units', if box says left/right
	   change JLabel to 'degrees', if box says 'stop' then make textbox and units invisible */
	public void itemStateChanged(ItemEvent e){
		if (choices.getSelectedItem().equals(commands[2]) || choices.getSelectedItem().equals(commands[3]))
			labelUse.setText(labelTurn.getText());
		else
			labelUse.setText(labelMove.getText());
		if (choices.getSelectedItem().equals(commands[5])){
			valueInput.setVisible(false);
			labelUse.setVisible(false);
			timeFor.setVisible(false);
		}
		else {
			valueInput.setVisible(true);
			labelUse.setVisible(true);
			timeFor.setVisible(true);
		}
	}

}
