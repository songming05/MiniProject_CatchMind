package miniproject.catchmind;

//�����ʹ� ���ڿ��� �ִ°��̾ƴ϶� ��ü�� �������ϴ°��̴�

import java.io.Serializable;

enum Info{//�������ݰ����� ��
	JOIN,EXIT,SEND//���� ���� ����
}
class InfoDTO implements Serializable//��ü�� ũ�Ⱑ Ŀ�� ���ۺҰ��ϴ� �����͸� �߰��ɰ��� ��������(����ȭ�۾� �ʿ�)
									//�̶� �ʿ��Ѱ� �������̽��� Serializable�̴�
{
	private String nickName; //����������� �˾Ƴ�����
	private String message;
	private Info command;
	//�������ʹ� BufferedReader,PrintWriter���Ұ�,println�����Ұ�,Chatserveer�� �ǵ帱�ʿ�x,io�� �ǵ��
	//��ü����ȭ ���
	//�׷��� �Է��� ObjectInputStream, ����� ObjectOutputStream ����Ѵ�
	//���ڿ� (String)�� �ѱ�� �ް� ���� ���ƶ�
	//��ü=InfoDTO�� �ѱ�� �ް� ��

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