package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class catchmind_Handler {
	private Socket socket;
	private ArrayList<catchmind_Handler> list;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public catchmind_Handler(Socket socket, ArrayList<catchmind_Handler> list) throws IOException{
		this.socket = socket;
		this.list = list;	
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}

	public static void main(String[] args) {
		new catchmind_Server();
	}

}
