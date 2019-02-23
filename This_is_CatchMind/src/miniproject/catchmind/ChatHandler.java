package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatHandler extends Thread{
	private Socket chatHandlerSocket;
	private ArrayList <ChatHandler> chatHandlerList;
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	private ObjectInputStream chatHandlerOis;
	private ObjectOutputStream chatHandlerOos;
	
	public ChatHandler(Socket chatHandlerSocket,ArrayList<ChatHandler> chatHandlerList )throws IOException{
		this.chatHandlerSocket=chatHandlerSocket;
		this.chatHandlerList=chatHandlerList;
		
		chatHandlerOos=new ObjectOutputStream(chatHandlerSocket.getOutputStream());
		chatHandlerOis=new ObjectInputStream(chatHandlerSocket.getInputStream());
	}//������ ����
	
	@Override
	public void run() {
		ChatDTO chatHandlerDTO=null;
		String chatNickName=null;
		while(true) {
			try {				
				chatHandlerDTO=(ChatDTO)chatHandlerOis.readObject();
				shapeDTOList = (ArrayList<catchmind_ShapDTO>) chatHandlerOis.readObject();
				//System.out.println("ä������ �ڵ鷯�� �о���.");
				
				//ChatDTO chatSendDTO= new ChatDTO();
				
				if(chatHandlerDTO.getCommand()==Info.JOIN) {
					chatNickName=chatHandlerDTO.getNickName();		
					ChatDTO chatSendDTO= new ChatDTO();
					chatSendDTO.setCommand(Info.SEND);
					chatSendDTO.setMessage("["+chatNickName+"]���� �����Ͽ����ϴ�,  "+"�ų�ä�� �Ͻñ� �ٶ��ϴ�");
					broadcast(chatSendDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.EXIT) {
					chatHandlerList.remove(this);
					ChatDTO chatSendDTO= new ChatDTO();
					chatSendDTO.setCommand(Info.EXIT);
					chatHandlerOos.writeObject(chatSendDTO);
					chatHandlerOos.writeObject(shapeDTOList);
					chatHandlerOos.flush();
					
					chatSendDTO.setCommand(Info.SEND);
					chatSendDTO.setMessage(chatNickName+"���� �����Ͽ����ϴ�");
					broadcast(chatSendDTO);
					brodacast(shapeDTOList);
					
					chatHandlerOos.close();
					chatHandlerOis.close();
					chatHandlerSocket.close();
					break;
					
				}else if(chatHandlerDTO.getCommand()==Info.SEND) {
					ChatDTO chatSendDTO= new ChatDTO();
					chatSendDTO.setCommand(Info.SEND);
					chatSendDTO.setMessage("["+chatNickName+"] "+chatHandlerDTO.getMessage());
					broadcast(chatSendDTO);
					brodacast(shapeDTOList);
					
				} else if(chatHandlerDTO.getCommand()==Info.WAIT) {
					ChatDTO chatSendDTO= new ChatDTO();
					System.out.println("����Ʈ�� �ڵ鷯�� �о���.");
					broadcast(chatSendDTO);
					brodacast(shapeDTOList);
				}
				
			}  catch (IOException e) {
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}//while����
	}//run����

	private void brodacast(ArrayList<catchmind_ShapDTO> shapeDTOList) {
		for(ChatHandler handler:chatHandlerList) {
			try {
				handler.chatHandlerOos.writeObject(shapeDTOList);
				handler.chatHandlerOos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}//for�� ����
		
		
	}

	private void broadcast(ChatDTO chatsendDTO) {//�ѷ��ִ� �޼ҵ�
		for(ChatHandler handler:chatHandlerList) {
			try {
				handler.chatHandlerOos.writeObject(chatsendDTO);
				handler.chatHandlerOos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}//for�� ����
	}
}
