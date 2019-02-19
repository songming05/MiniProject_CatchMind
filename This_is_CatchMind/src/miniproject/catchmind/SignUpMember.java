package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SignUpMember extends JFrame{
	
	private JLabel IDL, passwordL, confirmPasswordL, nameL, addressL, phoneNumberL, emailAddressL, certificateL;
	private JTextField IDT, passwordT, confirmPasswordT, nameT, addressT, phoneNumberT1, phoneNumberT2, phoneNumberT3, emailAddressT, certificateT;
	private JButton duplicationB, cancelB, registerB;
	public SignUpMember() {
		super("회원가입");
		
		constructLabel();
		constructTextField();
		constructButton();
		
		Container container = this.getContentPane();
		container.setLayout(null);
		Panel leftP, IDP, passwordP, confirmPasswordP, nameP, addressP, phoneP, emailP, certificateP;
		Panel rightP, IDRowP, passwordRowP, confirmPasswordRowP;
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
		IDRowP.add(IDT);
		IDRowP.add(duplicationB);
		
		
	
		rightP.setBackground(Color.DARK_GRAY);
		rightP.setBounds(120, 20, 250, 300);
		
		rightP.add(IDRowP);	
		
		
		
		leftP.setBackground(Color.WHITE);
		leftP.setBounds(20, 20, 100, 300);	
		
		IDP.add(IDL);
		passwordP.add(passwordL);
		confirmPasswordP.add(confirmPasswordL);
		nameP.add(nameL);
		addressP.add(addressL);
		phoneP.add(phoneNumberL);
		emailP.add(emailAddressL);
		certificateP.add(certificateL);
		
		leftP.add(IDP);
		leftP.add(passwordP);
		leftP.add(confirmPasswordP);
		leftP.add(nameP);
		leftP.add(addressP);
		leftP.add(phoneP);
		leftP.add(emailP);
		leftP.add(certificateP);
		
		container.add(leftP);
		container.add(rightP);

		
		
		
		

		setBounds(300,300,400,420);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void constructLabel() {
		IDL = new JLabel("아이디");
		passwordL = new JLabel("비밀번호");
		confirmPasswordL = new JLabel("비밀번호 확인");
		nameL = new JLabel("이름");
		addressL = new JLabel("주소");
		phoneNumberL = new JLabel("휴대폰 번호");
		emailAddressL = new JLabel("이메일");	
		certificateL = new JLabel("인증 번호");
	}

	private void constructTextField() {
		IDT = new JTextField(12);
		passwordT = new JTextField(10);
		confirmPasswordT = new JTextField(10);
		nameT = new JTextField(10);
		addressT = new JTextField(10);
		phoneNumberT1 = new JTextField(10);
		phoneNumberT2 = new JTextField(10);
		phoneNumberT3 = new JTextField(10);
		emailAddressT = new JTextField(10);
		certificateT = new JTextField(10);
	}

	private void constructButton() {
		duplicationB = new JButton("중복 확인");
		cancelB = new JButton("취소");
		registerB = new JButton("작성 완료");
		
	}
	//main
	public static void main(String[] args) {
		SignUpMember signUpMember = new SignUpMember();
	}
}
