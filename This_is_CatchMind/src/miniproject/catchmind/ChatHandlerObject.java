package miniproject.catchmind;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.net.Socket;

class ChatHandlerObject extends Thread//������ run()���ֱ�
{
	private Socket socket;//�������ִٴ°�,br,pw�� �ʿ�
	private ArrayList <ChatHandlerObject> list;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	

	public ChatHandlerObject(Socket socket,ArrayList<ChatHandlerObject> list)throws IOException{//IOException ���� 2�ٸ� ���°Ŵ� �̷��� ���ִ°� ����

			this.socket=socket;
			this.list=list;
			
			oos =new ObjectOutputStream(socket.getOutputStream());
			ois= new ObjectInputStream(socket.getInputStream());

	}
	
	@Override
	public void run(){
		InfoDTO dto= null; //Ŭ���̾�Ʈ�κ��� �޴� InfoDTO
		String nickName=null;
		while(true){
			try{
				//���ϸ��� �г����� �о�鿩�´�
				dto= (InfoDTO) ois.readObject();//Object�� �ڽ�Ŭ������ ����ĳ����

				if(dto.getCommand()==Info.JOIN){//����
					nickName = dto.getNickName();
					
					//������ ��� Ŭ���̾�Ʈ���� ���� �޼��� ������
					InfoDTO sendDTO= new InfoDTO();//Ŭ���̾�Ʈ�� ������ InfoDTO
					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage(nickName+"�� �����Ͽ����ϴ�");//������ ��� Ŭ���̾�Ʈ���� ���� �޼��� ������
					broadcast(sendDTO);//�ѷ��ִ°�

				}else if(dto.getCommand()==Info.EXIT){//����
					list.remove(this);//list�� ���� ChatHandlerObject����
					InfoDTO sendDTO= new InfoDTO();

					//�����״� exit������
					sendDTO.setCommand(Info.EXIT);
					oos.writeObject(sendDTO);
					oos.flush();

					//���� ������ ��� Ŭ���̾�Ʈ���� ���� �޼��� ����
					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage(nickName+"�� �����Ͽ����ϴ�");
					broadcast(sendDTO);

					oos.close();//��¸��� �ݾ��ش�
					ois.close();
					socket.close();
					break;


				}else if (dto.getCommand()==Info.SEND){//����
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

	public void broadcast(InfoDTO sendDTO){//dto�� �Ѿ���� �ִ�

		for(ChatHandlerObject handler :list){
			//Ŭ���̾�Ʈ�� ������msg�޼��� ������
			try{
				handler.oos.writeObject(sendDTO);
				handler.oos.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}