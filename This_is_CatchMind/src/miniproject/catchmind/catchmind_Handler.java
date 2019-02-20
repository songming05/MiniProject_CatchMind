package miniproject.catchmind;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class catchmind_Handler extends Thread{
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
	
	@Override
	public void run() {
		catchmind_ShapDTO dto = null;
		
		while(true) {
			try {
				dto = (catchmind_ShapDTO)ois.readObject();
				
				catchmind_ShapDTO sendDTO = new catchmind_ShapDTO();
				sendDTO.setX1(dto.getX1());
				sendDTO.setY1(dto.getY1());
				sendDTO.setX2(dto.getX2());
				sendDTO.setY2(dto.getY2());
				
				sendDTO.setColorNum(dto.getColorNum());
				
				sendDTO.setShape(dto.getShape());
				
				broadcast(sendDTO);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void broadcast(catchmind_ShapDTO sendDTO) {
		for(catchmind_Handler handler : list) {
			try {
				handler.oos.writeObject(sendDTO);
				handler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
