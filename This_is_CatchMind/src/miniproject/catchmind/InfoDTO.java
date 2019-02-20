package miniproject.catchmind;

//데이터는 문자열을 넣는것이아니라 객체를 보내야하는것이다

import java.io.Serializable;

enum Info{//프로토콜개념이 들어감
	JOIN,EXIT,SEND//입장 퇴장 전송
}
class InfoDTO implements Serializable//객체는 크기가 커서 전송불가하니 데이터를 잘게쪼개서 보내야함(직렬화작업 필요)
									//이때 필요한게 인터페이스의 Serializable이다
{
	private String nickName; //누가떠들었나 알아내야함
	private String message;
	private Info command;
	//이제부터는 BufferedReader,PrintWriter사용불가,println도사용불가,Chatserveer는 건드릴필요x,io만 건들라
	//객체직렬화 사용
	//그래서 입력은 ObjectInputStream, 출력은 ObjectOutputStream 써야한다
	//문자열 (String)의 넘기고 받고 하지 말아라
	//객체=InfoDTO로 넘기고 받고 함

	public void setNickName(String nickName){
		this.nickName = nickName;
	}
	public void setMessage(String message){
		this.message = message;
	}
	public void setCommand(Info command){
		this.command = command;
	}

	public String getNickName(){
		return nickName;
	}
	public String getMessage(){
		return message;
	}
	public Info getCommand(){
		return command;
	}
	

	
}