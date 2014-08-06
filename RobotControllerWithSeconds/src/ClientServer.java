import java.io.*;
import java.net.*;

public class ClientServer {
	
	//protected InputStream iStream;
	//protected OutputStream oStream;
	
	public static String readFromSocket(Socket sock) throws IOException {
		BufferedReader in = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
        String line = in.readLine();
        //Don't close iStream or recv() on Pi returns empty string -> infinite loop
        //in.close();
        return line;
	}	
	
	public static void writeToSocket(Socket sock, String str) throws IOException {
		BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(sock.getOutputStream()));
		out.write(str);
		//Must flush oStream or can't receive data via iStream -> socket closed error 
		out.flush();
	}
	
}


	
	