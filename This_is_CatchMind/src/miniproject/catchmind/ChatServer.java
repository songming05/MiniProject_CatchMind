package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private ServerSocket chatServerSocket;
	private ArrayList<ChatHandler> chatServerList;
	
	public ChatServer() {
		try {
			chatServerSocket=new ServerSocket(9500);//���ϻ���
			System.out.println("���� �غ� �Ϸ�");
			chatServerList= new ArrayList<ChatHandler>();//list����
			
			while(true) {
				Socket socket = chatServerSocket.accept();
				ChatHandler handler = new ChatHandler(socket,chatServerList);
				handler.start();
				chatServerList.add(handler);
			}//while
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new ChatServer();
	}
}
