package miniproject.catchmind;

import java.awt.Container;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SignUpMember extends JFrame{
	
	private JLabel IDL, passwordL, confirmPasswordL, nameL, addressL, phoneNumberL, emailAddressL;
	private JTextField IDT, passwordT, confirmPasswordT, nameT, addressT, phoneNumberT1, phoneNumberT2, phoneNumberT3, emailAddressT;
	private JButton duplicationB, cancelB, registerB;
	public SignUpMember() {
		super("ȸ������");
		
		constructLabel();
		constructTextField();
		constructButton();
		
		Container container = this.getContentPane();
		Panel leftP, rightP, IDP, passwordP, confirmPasswordP, nameP, adressP, phoneP, emailP, duplicationCheckP, phoneNumberP; 
		
		

		setBounds(300,300,400,400);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void constructLabel() {
		IDL = new JLabel("���̵�");
		passwordL = new JLabel("��й�ȣ");
		confirmPasswordL = new JLabel("��й�ȣ Ȯ��");
		nameL = new JLabel("�̸�");
		addressL = new JLabel("�ּ�");
		phoneNumberL = new JLabel("�޴��� ��ȣ");
		emailAddressL = new JLabel("�̸���");		
	}

	private void constructTextField() {
		IDT = new JTextField(10);
		passwordT = new JTextField(10);
		confirmPasswordT = new JTextField(10);
		nameT = new JTextField(10);
		addressT = new JTextField(10);
		phoneNumberT1 = new JTextField(10);
		phoneNumberT2 = new JTextField(10);
		phoneNumberT3 = new JTextField(10);
		emailAddressT = new JTextField(10);		
	}

	private void constructButton() {
		duplicationB = new JButton("�ߺ� Ȯ��");
		cancelB = new JButton("���");
		registerB = new JButton("�ۼ� �Ϸ�");
		
	}
	//main
	public static void main(String[] args) {
		SignUpMember signUpMember = new SignUpMember();
	}
}
