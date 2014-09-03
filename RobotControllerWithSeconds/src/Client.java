import java.io.IOException;
import java.net.Socket;


public class Client extends ClientServer{

	protected static Socket socket;
	
	// Client constructor makes socket to connect to pi
	public Client(String IP, int port){
		try {
			socket = new Socket(IP, port);
			System.out.println("CLIENT: connected to " + IP + ": " + port);
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//Closes the socket
	public void closeSocket(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Writes message to socket to be read by raspberry pi
	public static void handleNetworkEvent(String message){
		try {
			writeToSocket(socket, message);
			RobotUser.messageDisplay.insert(message + "\n", 0);
		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Reads message from raspberry pi (e.g. used to get ultrasonic sensor values)	
	public static String getValue(){	
		try {
			return readFromSocket(socket);
		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
