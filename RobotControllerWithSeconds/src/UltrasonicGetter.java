import java.text.DecimalFormat;


public class UltrasonicGetter extends Thread{
	
	volatile static boolean getUltra = false;
	
	/* Reads ultrasonic sensor value from the input stream every two seconds when the button
	is pressed. Stops reading if the button is pressed again.*/
	public void run(){
		while (true){
			while (getUltra){
				RobotUser.ultraDisplay.insert("Ultrasonic sensor value is " + 
				new DecimalFormat("0.0").format(Double.parseDouble(Client.getValue())) + "cm\n", 0);
				try {
					Thread.sleep(1900);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
