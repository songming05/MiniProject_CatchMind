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
	private int ready; //���߰��ѳ���
	private int start; //���߰��ѳ���
	
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
				
				if(chatHandlerDTO.getCommand()==Info.JOIN) {
					chatNickName=chatHandlerDTO.getNickName();		
					chatHandlerDTO.setCommand(Info.SEND);
					chatHandlerDTO.setMessage("["+chatNickName+"]���� �����Ͽ����ϴ�,  "+"�ų�ä�� �Ͻñ� �ٶ��ϴ�");
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.EXIT) {
					chatHandlerList.remove(this);
					chatHandlerDTO.setCommand(Info.EXIT);
					chatHandlerOos.writeObject(chatHandlerDTO);
					chatHandlerOos.writeObject(shapeDTOList);
					chatHandlerOos.flush();
					
					chatHandlerDTO.setCommand(Info.SEND);
					chatHandlerDTO.setMessage(chatNickName+"���� �����Ͽ����ϴ�");
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
					
					chatHandlerOos.close();
					chatHandlerOis.close();
					chatHandlerSocket.close();
					break;
					
				}else if(chatHandlerDTO.getCommand()==Info.SEND) {
					chatHandlerDTO.setCommand(Info.SEND);
					chatHandlerDTO.setNickName(chatNickName);
					chatHandlerDTO.setMessage(chatHandlerDTO.getMessage());
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
					
				} else if(chatHandlerDTO.getCommand()==Info.WAIT) {
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.READY) {//���߰��� ����
					start = 0;
					ready = chatHandlerDTO.getReadyCount();
					chatHandlerDTO.setReadyCount(ready);
					chatHandlerDTO.setCommand(Info.READY);
					chatHandlerDTO.setMessage(ready+"���� �غ��Ͽ����ϴ�");
					chatHandlerDTO.setReadyCount(ready);
					if(ready == 4) {
						chatHandlerDTO.setCommand(Info.READY);
						ready = 0;
					}
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}
				else if(chatHandlerDTO.getCommand()==Info.START) {//���߰��� ����
					start = chatHandlerDTO.getStartCount();
					chatHandlerDTO.setStartCount(start);
					chatHandlerDTO.setCommand(Info.START);
					ready = 0;
					broadcast(chatHandlerDTO);
					brodacast(shapeDTOList);
				}else if(chatHandlerDTO.getCommand() == Info.ANSWER) {//���߰��� ����
					chatHandlerDTO.setCommand(Info.ANSWER);
					chatHandlerDTO.setNickName(chatNickName);
					chatHandlerDTO.setMessage(chatHandlerDTO.getMessage());
					broadcast(chatHandlerDTO);
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
