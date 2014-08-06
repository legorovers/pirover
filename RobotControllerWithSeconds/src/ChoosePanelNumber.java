import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;


public class ChoosePanelNumber extends JPanel implements ActionListener{
	
	private String numbers[] = {"1", "2", "3", "4"};
	public static List<CommandPanel> panelArray = new ArrayList<CommandPanel>();
	private JComboBox numberOfPanels = new JComboBox(numbers);
	private JLabel prompt = new JLabel("How many instructions you would like to send? ");
	private JButton ok = new JButton("Ok");
	private JButton go = new JButton("Go!");
	private JPanel mainPanel = new JPanel();
	private JPanel commandPanels = new JPanel();
	
	public ChoosePanelNumber(){
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Adds first panel containing input to select number of instructions with border
		mainPanel.add(prompt);
		mainPanel.add(numberOfPanels);
		mainPanel.add(ok);
		ok.addActionListener(this);
		add(mainPanel);
		mainPanel.setPreferredSize(new Dimension(510, 60));
		RobotUser.addBorder(mainPanel, "Number of Instructions");
		
		//Sets layout of second panel commandPanels	
		commandPanels.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Adds a CommandPanel to the commandPanels with GridBagLayout
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		panelArray.add(new CommandPanel(1));
		commandPanels.add(panelArray.get(0), c);
		
		//Adds go button to commandPanels
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		go.addActionListener(this);
		commandPanels.add(go, c);
		
		//Adds commandPanels to ChoosePanelNumber and adds a border
		add(commandPanels);
		commandPanels.setPreferredSize( new Dimension(510, 90));
		RobotUser.addBorder(commandPanels, "Instructions");
	}
		
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == ok){
			//Clears commandPanels and panelArray
			RobotUser.instructionPanel.commandPanels.removeAll();
			panelArray.clear();
			
			//Sets up layout for commandPanels			
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 1;
			c.gridwidth = 2;
			
			//Adds instances of CommandPanel to commandPanels depending on user's choice
			for (int i = 0 ; i < numbers.length ; i++){
				if (numberOfPanels.getSelectedItem() == numbers[i]){
					for (int j = 0 ; j <= i ; j++){
						
						c.gridy = j;
						
						panelArray.add(new CommandPanel(j+1));
						RobotUser.instructionPanel.commandPanels.add(panelArray.get(j), c);
					}
				}
				
			}
			//Adds go button at the end of the CommandPanel instances
			c.gridx = 3;
			c.gridy = panelArray.size();
			c.gridwidth = 1;
			RobotUser.instructionPanel.commandPanels.add(go, c);
			
			/* Sets size of commandPanels depending on number of instances of CommandPanel and
			repaints the ChoosePanelNumber instance in RobotUser so the user can see it*/
			commandPanels.setPreferredSize( new Dimension(510, (90 + 34*(panelArray.size()-1))));			
			RobotUser.instructionPanel.repaint();
			RobotUser.instructionPanel.revalidate();
		}
		
		/* When go button is pressed control is passed to event handling methods in 
		   ConditionPanel and CommandPanel*/
		if (e.getSource() == go){
			ConditionPanel.handleConditionEvent(RobotUser.conditionPanel);
			handleEvent();
		}
	}
	
	/* handleEvent() loops through all the instances of CommandPanel in commandPanels
	 and writes commands to the output stream one by one*/
	public static void handleEvent(){
		for (int i = 0 ; i < panelArray.size() ; i++){
			panelArray.get(i).handleUserEvent();
			// Thread must sleep or commands pile up in the output stream eg.forward30right90
			try {
				Thread.sleep(200);
			} catch (InterruptedException e){
				Thread.currentThread().interrupt();
			}
		}
	}
			
}
		

