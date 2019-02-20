package miniproject.catchmind;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import miniproject.certificate.MailSendManager;


public class SignUpMember extends JFrame implements ActionListener{	
	private JLabel IDL, passwordL, confirmPasswordL, nameL, addressL, 
					phoneNumberL, emailAddressL, certificateL;
	private JTextField IDT, passwordT, confirmPasswordT, nameT, addressT, 
						phoneNumberT1, phoneNumberT2, phoneNumberT3, emailAddressT, certificateT;
	private JButton duplicationB, certificateB, cancelB, registerB;
	private JComboBox<String> mailSelect;
	private boolean duplicateCheck, certificateCheck;
	private String certificationKey;
	
	//main
	public static void main(String[] args) {
		SignUpMember signUpMember = new SignUpMember();
	}
	
	public SignUpMember() {
		super("회원가입");
		
		constructLabel();
		constructTextField();
		constructButton();
		mailSelect = new JComboBox<String>();
		mailSelect.addItem("gmail.com");
		event();
		
		Container container = this.getContentPane();
		container.setLayout(null);
		Panel leftP, IDP, passwordP, confirmPasswordP, nameP, addressP, 
				phoneP, emailP, certificateP;
		Panel rightP, IDRowP, passwordRowP, confirmPasswordRowP, nameRowP, addressRowP, 
				phoneRowP, emailRowP, certificateRowP;
		Panel buttonRowP;
		
		leftP = new Panel(new GridLayout(8,1));
		IDP = new Panel(new FlowLayout(FlowLayout.LEFT));
		passwordP = new Panel(new FlowLayout(FlowLayout.LEFT));
		confirmPasswordP = new Panel(new FlowLayout(FlowLayout.LEFT));
		nameP = new Panel(new FlowLayout(FlowLayout.LEFT));
		addressP = new Panel(new FlowLayout(FlowLayout.LEFT));
		phoneP = new Panel(new FlowLayout(FlowLayout.LEFT));
		emailP = new Panel(new FlowLayout(FlowLayout.LEFT));
		certificateP = new Panel(new FlowLayout(FlowLayout.LEFT));
		
		rightP = new Panel(new GridLayout(8, 1));
		IDRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		passwordRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		confirmPasswordRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		nameRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		addressRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		phoneRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		emailRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		certificateRowP = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		
		buttonRowP = new Panel(new GridLayout(1, 2, 20, 10));
		
				
		IDP.add(IDL);
		passwordP.add(passwordL);
		confirmPasswordP.add(confirmPasswordL);
		nameP.add(nameL);
		addressP.add(addressL);
		phoneP.add(phoneNumberL);
		emailP.add(emailAddressL);
		certificateP.add(certificateL);
				
		IDRowP.add(IDT);
		IDRowP.add(duplicationB);
		passwordRowP.add(passwordT);
		passwordRowP.add(new JLabel("5~12자 입력"));
		confirmPasswordRowP.add(confirmPasswordT);
		confirmPasswordRowP.add(new JLabel("다이얼로그로.."));
		nameRowP.add(nameT);
		nameRowP.add(new JLabel("6자 이내DB"));
		addressRowP.add(addressT);
		phoneRowP.add(phoneNumberT1);
		phoneRowP.add(new JLabel(" -  "));
		phoneRowP.add(phoneNumberT2);
		phoneRowP.add(new JLabel(" -  "));
		phoneRowP.add(phoneNumberT3);
		emailRowP.add(emailAddressT);
		emailRowP.add(new JLabel("@"));
		emailRowP.add(mailSelect);
		certificateB.setSize(getPreferredSize());
		certificateRowP.add(certificateB);
		certificateRowP.add(new JLabel("      인증번호 :"));
		certificateRowP.add(certificateT);
		
		
		//leftP.setBackground(Color.WHITE);
		leftP.setBounds(20, 20, 100, 300);		
		//rightP.setBackground(Color.DARK_GRAY);
		rightP.setBounds(120, 20, 250, 300);		
		//buttonRowP.setBackground(Color.cyan);
		buttonRowP.setBounds(20, 330, 350, 50);
		

		leftP.add(IDP);
		leftP.add(passwordP);
		leftP.add(confirmPasswordP);
		leftP.add(nameP);
		leftP.add(addressP);
		leftP.add(phoneP);
		leftP.add(emailP);
		leftP.add(certificateP);
		
		rightP.add(IDRowP);	
		rightP.add(passwordRowP);
		rightP.add(confirmPasswordRowP);
		rightP.add(nameRowP);
		rightP.add(addressRowP);
		rightP.add(phoneRowP);
		rightP.add(emailRowP);
		rightP.add(certificateRowP);		
	
		buttonRowP.add(cancelB);
		buttonRowP.add(registerB);
				
		container.add(leftP);
		container.add(rightP);
		container.add(buttonRowP);
		
		
		

		setBounds(300,300,400,420);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}//constr

	private void event() {
		cancelB.addActionListener(this);
		registerB.addActionListener(this);
		duplicationB.addActionListener(this);
		certificateB.addActionListener(this);		
	}

	private void constructLabel() {
		IDL = new JLabel("아이디(ID) *");
		passwordL = new JLabel("비밀번호 *");
		confirmPasswordL = new JLabel("비밀번호 확인 *");
		nameL = new JLabel("이름 *");
		addressL = new JLabel("주소");
		phoneNumberL = new JLabel("휴대폰 번호");
		emailAddressL = new JLabel("이메일 *");	
		certificateL = new JLabel("인증 메일 발송 :");
	}//constructLabel

	private void constructTextField() {
		IDT = new JTextField(12);
		passwordT = new JTextField(10);
		confirmPasswordT = new JTextField(10);
		nameT = new JTextField(10);
		addressT = new JTextField(21);
		phoneNumberT1 = new JTextField(4);
		phoneNumberT2 = new JTextField(5);
		phoneNumberT3 = new JTextField(5);
		emailAddressT = new JTextField(10);
		certificateT = new JTextField(8);
	}//constructTextField

	private void constructButton() {
		duplicationB = new JButton("중복 확인");
		cancelB = new JButton("취소");
		registerB = new JButton(" 작성 완료 ");
		certificateB = new JButton("발송");
	}//constructButton
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("발송")) mailSend();
		else if(e.getActionCommand().equals("취소")) System.exit(0);
		//이 부분은 합치면서 고쳐야 한다. 종료하면 안된다.
		
		else if(e.getActionCommand().equals("중복 확인")) ;
		//DB에서 ID 겹치는지 검색해야 한다. DAO
		else if(e.getSource()==registerB) memberRegister();
		
	}

	private void memberRegister() {
		//비밀번호 **** 으로 보이게 하는 기능 구현해야한다.
		
		String userID = IDT.getText().trim();
		if(userID.equals("")) {
			JOptionPane.showMessageDialog(this, "아이디(ID)는 필수 입력사항입니다.");
			return;
		}
		String password = passwordT.getText().trim();
		if(password.equals("")) {
			JOptionPane.showMessageDialog(this, "비밀번호는 필수 입력사항입니다.");
			return;
		}
		String confirm = confirmPasswordT.getText().trim();
		if(!password.equals(confirm)) {
			JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
			return;
		}
		String userName = nameT.getText().trim();
		if(userName.equals("")) {
			JOptionPane.showMessageDialog(this, "이름은 필수 입력사항입니다.");
			return;
		}
		String userAddress = addressT.getText().trim();
		String userPhone = phoneNumberT1.getText().trim()
							+ phoneNumberT2.getText().trim()
							+ phoneNumberT3.getText().trim();
		String userEmail = emailAddressT.getText().trim();
		if(userEmail.equals("")) {
			JOptionPane.showMessageDialog(this, "이메일은 필수 입력사항입니다.");
			return;
		}
		
		String inputKey = certificateT.getText().trim();		
		if(inputKey.equals(certificationKey)) {
			certificateCheck = true;			
		}
		if(!certificateCheck) JOptionPane.showMessageDialog(this, "이메일 인증은 필수사항입니다.");
		else {
			//MembershipDTO 에 추가
			//DB 에 등록
			JOptionPane.showMessageDialog(this, "회원가입이 완료 되었습니다.");
			//로그인 창으로
		}
		
	}

	private void mailSend() {
		String userEmail = emailAddressT.getText().trim();
		MailSendManager mailSendManager = new MailSendManager(userEmail);
		
		certificationKey = mailSendManager.getCertificationKey();				
	}
}
