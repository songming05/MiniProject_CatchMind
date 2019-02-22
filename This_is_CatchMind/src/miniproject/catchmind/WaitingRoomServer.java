package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WaitingRoomServer {
	private ServerSocket serverSocket;
	private ArrayList<WaitingRoomHandler>arrayList;
	
	public WaitingRoomServer() {
		try {
			serverSocket = new ServerSocket(9500);
			System.out.println("���� �غ� �Ϸ�....");
			arrayList = new ArrayList<WaitingRoomHandler>();
			
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("Ŭ���̾�Ʈ ����");
				WaitingRoomHandler waitingroomhandler= new WaitingRoomHandler(socket,arrayList);
				
				waitingroomhandler.start();
				arrayList.add(waitingroomhandler);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new WaitingRoomServer();
	}
}