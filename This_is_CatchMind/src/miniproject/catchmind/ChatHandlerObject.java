package miniproject.catchmind;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.net.Socket;

class ChatHandlerObject extends Thread//무조건 run()써주기
{
	private Socket socket;//소켓이있다는건,br,pw가 필요
	private ArrayList <ChatHandlerObject> list;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	

	public ChatHandlerObject(Socket socket,ArrayList<ChatHandlerObject> list)throws IOException{//IOException 에러 2줄만 나는거니 이렇게 써주는게 좋음

			this.socket=socket;
			this.list=list;
			
			oos =new ObjectOutputStream(socket.getOutputStream());
			ois= new ObjectInputStream(socket.getInputStream());

	}
	
	@Override
	public void run(){
		InfoDTO dto= null; //클라이언트로부터 받는 InfoDTO
		String nickName=null;
		while(true){
			try{
				//제일먼저 닉네임을 읽어들여온다
				dto= (InfoDTO) ois.readObject();//Object라 자식클래스로 강제캐스팅

				if(dto.getCommand()==Info.JOIN){//입장
					nickName = dto.getNickName();
					
					//나포함 모든 클라이언트에게 입장 메세지 보내기
					InfoDTO sendDTO= new InfoDTO();//클라이언트로 보내는 InfoDTO
					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage(nickName+"님 입장하였습니다");//나포함 모든 클라이언트에게 입장 메세지 보내기
					broadcast(sendDTO);//뿌려주는것

				}else if(dto.getCommand()==Info.EXIT){//퇴장
					list.remove(this);//list에 담은 ChatHandlerObject제거
					InfoDTO sendDTO= new InfoDTO();

					//나한테는 exit보내기
					sendDTO.setCommand(Info.EXIT);
					oos.writeObject(sendDTO);
					oos.flush();

					//나를 제외한 모든 클라이언트에게 퇴장 메세지 전송
					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage(nickName+"님 퇴장하였습니다");
					broadcast(sendDTO);

					oos.close();//출력먼저 닫아준다
					ois.close();
					socket.close();
					break;


				}else if (dto.getCommand()==Info.SEND){//전송
					InfoDTO sendDTO = new InfoDTO();
					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage("["+nickName+"] "+dto.getMessage());
					broadcast(sendDTO);
	

				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}//while

	}

	public void broadcast(InfoDTO sendDTO){//dto가 넘어오고 있다

		for(ChatHandlerObject handler :list){
			//클라이언트로 현재의msg메세지 보내기
			try{
				handler.oos.writeObject(sendDTO);
				handler.oos.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}