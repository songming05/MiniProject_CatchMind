package miniproject.certificate;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthentication extends Authenticator{
	private PasswordAuthentication passwordAuth;
	  
    public MyAuthentication(){  //�����ڸ� ���� ���� ID/PW ����
          
        String id = "���̵�";       // ���� ID
        String pw = "��й�ȣ";       // ���� ��й�ȣ
  
        // ID�� ��й�ȣ�� �Է��Ѵ�.
        passwordAuth = new PasswordAuthentication(id, pw);
    }
  
    // �ý��ۿ��� ����ϴ� ��������
    public PasswordAuthentication getPasswordAuthentication() {
        return passwordAuth;
    }	
}
