package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class catchmind_Server {
	private ServerSocket ss;
	private ArrayList<catchmind_Handler> list;
	
	public catchmind_Server() {
		try {
			ss = new ServerSocket(9500);
			System.out.println("서버준비완료..");
			
			list = new ArrayList<catchmind_Handler>();
			
			while(true) {
				Socket socket = ss.accept();
				catchmind_Handler han = new catchmind_Handler(socket, list);
				
				list.add(han);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new catchmind_Server();
	}
}
