package miniproject.catchmind;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class catchmind_Server {
	private ServerSocket ss;
	private ArrayList<catchmind_Handler> list;
	
	public catchmind_Server() {
		try {
			ss = new ServerSocket(9500);
			System.out.println("서버준비완료..");			
			list = new ArrayList<catchmind_Handler>();
			
			while(true) {
				Socket socket = ss.accept(); //클라이언트 낚아채오기
				catchmind_Handler handler = new catchmind_Handler(socket, list); //쓰래드 생성
				handler.start(); //쓰레드 시작
				
				list.add(handler);
			}//while
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new catchmind_Server();
	}
}
