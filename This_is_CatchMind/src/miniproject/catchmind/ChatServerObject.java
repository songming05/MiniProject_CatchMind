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
			ss=new ServerSocket(9500);//포트번호9500을보며 기다린다
			System.out.println("서버준비 완료");

			list= new ArrayList<ChatHandlerObject>();


			while(true){
				Socket socket = ss.accept();//클라이언트를 낚아챈다,그후 소켓을 만들어줌
				ChatHandlerObject handler =new ChatHandlerObject(socket,list);//스래드 생성 //list의 주소값을 보내는것이다
													//chatserver에는 쓰레드로 여러개가 돌기때문에 br,bw불가해서 소켓을 handler로넘겨줘서 스레드생성
				handler.start();//스레드 시작 , this.start()하면안됨 스레드처리는ChatHandler에서 하기때문
					
				list.add(handler);//스레드를 리스트에 담아줌

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
