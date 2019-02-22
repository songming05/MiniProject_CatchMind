package miniproject.certificate;

import java.util.Random;

public class MailSendManager {
	private String certificationKey;
	public String getCertificationKey() {
		return certificationKey;
	}

	//������ ���� �ּҸ� param���� �޴´�.
	public MailSendManager(String userMail) {
		certificationKey = makeCertificationKey();
		String text = "ĳġ���ε� ȸ������ ���� �����Դϴ�.\n\n"
						+"������ ���� ��ȣ�� " + certificationKey
						+ "�Դϴ�. \n\n"
						+ "(����� �빮�ڷ� �����ֽñ� �ٶ��ϴ�.)";
		
		GmailSend gmailSend = new GmailSend();
		gmailSend.GmailSet(userMail+"@gmail.com", "ĳġ���ε� �������� �����Դϴ�.", text);
	}

	//5�ڸ� ������ ����+����빮�ڷ� �̷���� Key�� �����Ѵ�.
	private String makeCertificationKey() {
		String key="";
		Random ranndom = new Random();
        int num = 0;
        do {
            num = ranndom.nextInt(75)+48;
            if((num>=48 && num<=57) || (num>=65 && num<=90)) {
                key += ((char)num);
            }else {
                continue;
            }
        } while (key.length() < 6);        
		return key;
	}
}
