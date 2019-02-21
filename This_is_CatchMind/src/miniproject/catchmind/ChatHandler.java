package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatHandler extends Thread{
	private Socket chatHandlerSocket;
	private ArrayList <ChatHandler> chatHandlerList;
	private ObjectInputStream chatHandlerOis;
	private ObjectOutputStream chatHandlerOos;
	
	public ChatHandler(Socket chatHandlerSocket,ArrayList<ChatHandler> chatHandlerList )throws IOException{
		this.chatHandlerSocket=chatHandlerSocket;
		this.chatHandlerList=chatHandlerList;
		
		chatHandlerOos=new ObjectOutputStream(chatHandlerSocket.getOutputStream());
		chatHandlerOis=new ObjectInputStream(chatHandlerSocket.getInputStream());
	}//생성자 종료
	
	@Override
	public void run() {
		ChatDTO chatHandlerDTO=null;
		String chatNickName=null;
		while(true) {
			try {
				chatHandlerDTO=(ChatDTO)chatHandlerOis.readObject();
				if(chatHandlerDTO.getCommand()==Info.JOIN) {
					chatNickName=chatHandlerDTO.getNickName();
					ChatDTO chatSendDTO= new ChatDTO();
					chatSendDTO.setCommand(Info.SEND);
					chatSendDTO.setMessage("["+chatNickName+"]님이 입장하였습니다,  "+"매너채팅 하시길 바랍니다");
					broadcast(chatSendDTO);
				}
				else if(chatHandlerDTO.getCommand()==Info.EXIT) {
					chatHandlerList.remove(this);
					ChatDTO chatSendDTO=new ChatDTO();
					
					chatSendDTO.setCommand(Info.EXIT);
					chatHandlerOos.writeObject(chatSendDTO);
					chatHandlerOos.flush();
					
					chatSendDTO.setCommand(Info.SEND);
					chatSendDTO.setMessage(chatNickName+"님이 퇴장하였습니다");
					broadcast(chatSendDTO);
					
					chatHandlerOos.close();
					chatHandlerOis.close();
					chatHandlerSocket.close();
					break;
				}else if(chatHandlerDTO.getCommand()==Info.SEND) {
					ChatDTO chatSendDTO=new ChatDTO();
					chatSendDTO.setCommand(Info.SEND);
					chatSendDTO.setMessage("["+chatNickName+"] "+chatHandlerDTO.getMessage());
					broadcast(chatSendDTO);
				}
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//while종료
	}//run종료

	private void broadcast(ChatDTO chatsendDTO) {//뿌려주는 메소드
		// TODO Auto-generated method stub
		for(ChatHandler handler:chatHandlerList) {
			try {
				handler.chatHandlerOos.writeObject(chatsendDTO);
				handler.chatHandlerOos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for문 종료
	}
}
