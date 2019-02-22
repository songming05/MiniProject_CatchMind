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
			System.out.println("�����غ�Ϸ�..");			
			list = new ArrayList<catchmind_Handler>();
			
			while(true) {
				Socket socket = ss.accept(); //Ŭ���̾�Ʈ ����ä����
				catchmind_Handler handler = new catchmind_Handler(socket, list); //������ ����
				handler.start(); //������ ����
				
				list.add(handler);
			}//while
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new catchmind_Server();
	}
}
