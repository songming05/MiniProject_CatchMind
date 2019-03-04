package miniproject.catchmind;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

public class CommonServer2 {
	private ServerSocket serverSocket, serverSocket2;
	private ArrayList<CommonHandler2>arCHandler2;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	private ArrayList<GameUserDTO> arGameUserList;
	//private Socket socket;
	
	public CommonServer2() {
		try {

			
			arCHandler2 = new ArrayList<CommonHandler2>();
			arUserList = new ArrayList<waitingRoomUserDTO>();
			arRoomList = new ArrayList<waitingRoomRCreateDTO>();
			arGameUserList = new ArrayList<GameUserDTO>();
	
			serverSocket = new ServerSocket(9700);
			System.out.println("����2 �غ� �Ϸ�....");

			
			
			while(true) {
				Socket socket = serverSocket.accept();//�޾Ƽ� ������ 
				if(socket.isBound()) {
					System.out.println("9700�� �����Ǿ���");
	
				}else 
				System.out.println("server�޾ƿ� ������ = " +socket.getLocalPort());

				
				if(9500 == socket.getLocalPort()) {
					System.out.println("9500 port in");
					Socket socket2 = socket;
					System.out.println("9500 accept!!");
					CommonHandler2 commonhandler2= new CommonHandler2(socket2, arCHandler2,arUserList, arRoomList,arGameUserList);
					commonhandler2.start();
					arCHandler2.add(commonhandler2);
				}
				else if(9700 == socket.getLocalPort()) {
					System.out.println("9700 port in");
					Socket socket3 = socket;
					System.out.println("9700 accept!!");
					CommonHandler2 commonhandler2= new CommonHandler2(socket3, arCHandler2,arUserList, arRoomList,arGameUserList);
					commonhandler2.start();
					arCHandler2.add(commonhandler2);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new CommonServer2();
	}
}