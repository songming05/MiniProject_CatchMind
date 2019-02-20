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
		super("ȸ������");
		
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
		passwordRowP.add(new JLabel("5~12�� �Է�"));
		confirmPasswordRowP.add(confirmPasswordT);
		confirmPasswordRowP.add(new JLabel("���̾�α׷�.."));
		nameRowP.add(nameT);
		nameRowP.add(new JLabel("6�� �̳�DB"));
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
		certificateRowP.add(new JLabel("      ������ȣ :"));
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
		IDL = new JLabel("���̵�(ID) *");
		passwordL = new JLabel("��й�ȣ *");
		confirmPasswordL = new JLabel("��й�ȣ Ȯ�� *");
		nameL = new JLabel("�̸� *");
		addressL = new JLabel("�ּ�");
		phoneNumberL = new JLabel("�޴��� ��ȣ");
		emailAddressL = new JLabel("�̸��� *");	
		certificateL = new JLabel("���� ���� �߼� :");
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
		duplicationB = new JButton("�ߺ� Ȯ��");
		cancelB = new JButton("���");
		registerB = new JButton(" �ۼ� �Ϸ� ");
		certificateB = new JButton("�߼�");
	}//constructButton
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("�߼�")) mailSend();
		else if(e.getActionCommand().equals("���")) System.exit(0);
		//�� �κ��� ��ġ�鼭 ���ľ� �Ѵ�. �����ϸ� �ȵȴ�.
		
		else if(e.getActionCommand().equals("�ߺ� Ȯ��")) ;
		//DB���� ID ��ġ���� �˻��ؾ� �Ѵ�. DAO
		else if(e.getSource()==registerB) memberRegister();
		
	}

	private void memberRegister() {
		//��й�ȣ **** ���� ���̰� �ϴ� ��� �����ؾ��Ѵ�.
		
		String userID = IDT.getText().trim();
		if(userID.equals("")) {
			JOptionPane.showMessageDialog(this, "���̵�(ID)�� �ʼ� �Է»����Դϴ�.");
			return;
		}
		String password = passwordT.getText().trim();
		if(password.equals("")) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �ʼ� �Է»����Դϴ�.");
			return;
		}
		String confirm = confirmPasswordT.getText().trim();
		if(!password.equals(confirm)) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			return;
		}
		String userName = nameT.getText().trim();
		if(userName.equals("")) {
			JOptionPane.showMessageDialog(this, "�̸��� �ʼ� �Է»����Դϴ�.");
			return;
		}
		String userAddress = addressT.getText().trim();
		String userPhone = phoneNumberT1.getText().trim()
							+ phoneNumberT2.getText().trim()
							+ phoneNumberT3.getText().trim();
		String userEmail = emailAddressT.getText().trim();
		if(userEmail.equals("")) {
			JOptionPane.showMessageDialog(this, "�̸����� �ʼ� �Է»����Դϴ�.");
			return;
		}
		
		String inputKey = certificateT.getText().trim();		
		if(inputKey.equals(certificationKey)) {
			certificateCheck = true;			
		}
		if(!certificateCheck) JOptionPane.showMessageDialog(this, "�̸��� ������ �ʼ������Դϴ�.");
		else {
			//MembershipDTO �� �߰�
			//DB �� ���
			JOptionPane.showMessageDialog(this, "ȸ�������� �Ϸ� �Ǿ����ϴ�.");
			//�α��� â����
		}
		
	}

	private void mailSend() {
		String userEmail = emailAddressT.getText().trim();
		MailSendManager mailSendManager = new MailSendManager(userEmail);
		
		certificationKey = mailSendManager.getCertificationKey();				
	}
}
