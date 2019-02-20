package miniproject.certificate;

import java.util.Random;

public class MailSendManager {
	private String certificationKey;
	public String getCertificationKey() {
		return certificationKey;
	}

	public MailSendManager(String userMail) {
		certificationKey = makeCertificationKey();
		String text = "캐치마인드 회원가입 인증 절차입니다.\n\n"
						+"귀하의 인증 번호는 " + certificationKey
						+ "입니다. \n\n"
						+ "(영어는 대문자로 적어주시길 바랍니다.)";
		
		GmailSend gmailSend = new GmailSend();
		gmailSend.GmailSet(userMail+"@gmail.com", "캐치마인드 가입인증 메일입니다.", text);
	}

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
