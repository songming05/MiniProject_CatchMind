package miniproject.catchmind;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ChatServerObject
{
	private ServerSocket ss;
	private ArrayList<ChatHandlerObject> list;

	public ChatServerObject(){
		try{
			ss=new ServerSocket(9500);//��Ʈ��ȣ9500������ ��ٸ���
			System.out.println("�����غ� �Ϸ�");

			list= new ArrayList<ChatHandlerObject>();


			while(true){
				Socket socket = ss.accept();//Ŭ���̾�Ʈ�� ����æ��,���� ������ �������
				ChatHandlerObject handler =new ChatHandlerObject(socket,list);//������ ���� //list�� �ּҰ��� �����°��̴�
													//chatserver���� ������� �������� ���⶧���� br,bw�Ұ��ؼ� ������ handler�γѰ��༭ ���������
				handler.start();//������ ���� , this.start()�ϸ�ȵ� ������ó����ChatHandler���� �ϱ⶧��
					
				list.add(handler);//�����带 ����Ʈ�� �����

			}//while 
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{
		new ChatServerObject();	
	}
}
