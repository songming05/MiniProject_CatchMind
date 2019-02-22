package miniproject.catchmind;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class catchmind_Handler extends Thread {
	private Socket socket;
	private ArrayList<catchmind_Handler> list;
	private ArrayList<catchmind_ShapDTO> InfoList;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public catchmind_Handler(Socket socket, ArrayList<catchmind_Handler> list) throws IOException{
		this.socket = socket;
		this.list = list;
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
			while(true) {
				//ct 에서 보낸 데이터를 수신
				try {
					InfoList = (ArrayList<catchmind_ShapDTO>)ois.readObject();
					System.out.println(InfoList);
					System.out.println(InfoList.size()+" - Handler");
					broadcast(InfoList);					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	public void broadcast(ArrayList<catchmind_ShapDTO> sendList) {
		for(catchmind_Handler handler : list) {
			try {
				handler.oos.writeObject(sendList);
				handler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		
	}

}
