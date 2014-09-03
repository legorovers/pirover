import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class ConditionPanel extends JPanel implements ItemListener{
	
	private JCheckBox checkbox = new JCheckBox();
	private JLabel ifLabel = new JLabel("If an obstacle is detected with the");
	private JLabel withLabel = new JLabel("within");
	private JLabel unitLabel = new JLabel("cm, then");
	private JTextField distanceInput = new JTextField(3);
	private String commands[] = {"reverse", "turn left", "turn right", "reverse left", "reverse right", "stop"};
	private JComboBox commandChoice = new JComboBox(commands);
	private String sensors[] = { "ultrasonic sensor", "infrared sensor"};
	private JComboBox sensorChoice = new JComboBox(sensors);
	private JTextField valueInput = new JTextField(3);
	private JLabel labelUse = new JLabel("seconds");
	private JLabel forLabel = new JLabel("for");	
	
	//Constructor makes JPanel with checkbox, drop down menu and textfield for user input
	public ConditionPanel(){
		
		add(checkbox);
		add(ifLabel);
		add(sensorChoice);
		sensorChoice.addItemListener(this);
		add(withLabel);
		add(distanceInput);
		add(unitLabel);
		add(commandChoice);
		commandChoice.addItemListener(this);
		add(forLabel);
		add(valueInput);
		add(labelUse);
		
		this.setPreferredSize(new Dimension(510, 90));		
		RobotUser.addBorder(this, "Conditions");
		
		
	}
	
	//Gets user's chosen distance to trigger the command from the first textfield
	public static String getTriggerDistance(ConditionPanel panel){
		if (panel.distanceInput.getText().isEmpty())
			return "000";
		else
			return new DecimalFormat("000").format(Double.parseDouble(panel.distanceInput.getText()));
	}
	
	// Gets user's chosen command from drop down box and returns appropriate string
	public String getCommand(){
		if (commandChoice.getSelectedItem().equals(commands[0]))
			return "reverse";
		if (commandChoice.getSelectedItem().equals(commands[1]))
			return "left";
		if (commandChoice.getSelectedItem().equals(commands[2]))
			return "right";
		if (commandChoice.getSelectedItem().equals(commands[3]))
			return "reverseleft";
		if (commandChoice.getSelectedItem().equals(commands[4]))
			return "reverseright";
		if (commandChoice.getSelectedItem().equals(commands[5]))
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
			Client.handleNetworkEvent(message);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			
		}
		/*else {
			Client.handleNetworkEvent("n");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1){
				Thread.currentThread().interrupt();
			}
		}*/
	}
	
	/* If user has selected 'stop' the number of seconds input is no longer visible. If user
	 * has selected 'infrared sensor' the input for number of cm is no longer visible. */
	public void itemStateChanged(ItemEvent e){
		if (commandChoice.getSelectedItem().equals(commands[5])){
			valueInput.setText("");
			valueInput.setVisible(false);
			labelUse.setVisible(false);
			forLabel.setVisible(false);
		}
		else {
			valueInput.setVisible(true);
			labelUse.setVisible(true);
			forLabel.setVisible(true);
		}
		if (sensorChoice.getSelectedItem().equals(sensors[1])){
			withLabel.setVisible(false);
			distanceInput.setText("");
			distanceInput.setVisible(false);
			unitLabel.setVisible(false);		
		}
		else {
			withLabel.setVisible(true);
			distanceInput.setVisible(true);
			unitLabel.setVisible(true);			
		}
	}

}
