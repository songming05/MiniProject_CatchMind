package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import miniproject.certificate.MailSendManager;
import miniproject.membership.dao.MembershipDAO;
import miniproject.membership.dto.MembershipDTO;


public class SignUpMember extends JFrame implements ActionListener{	
	private JLabel idL, passwordL, confirmPasswordL, nameL, addressL, 
					phoneNumberL, emailAddressL, certificateL;
	private JTextField idT, nameT;
	private JPasswordField passwordT, confirmPasswordT;
	private JTextField addressT, phoneNumberT1, phoneNumberT2, phoneNumberT3, 
						emailAddressT, certificateT;
	private JButton duplicationB, certificateB, cancelB, registerB;
	private JComboBox<String> mailSelect;
	private boolean duplicateCheck = true,//true: �ߺ�����, false:��밡��
					certificateCheck; //true: ����Ű ��ġ(��밡��), false: ���Ұ�
	private String certificationKey;
	//
	private ImageIcon signImage;//�ڡ�
	private JScrollPane signScrollPane;//�ڡ�
	//
	public SignUpMember() {
		super("ȸ������");
		
		mailSelect = new JComboBox<String>();
		mailSelect.addItem("gmail.com");
		constructLabel();
		constructTextField();
		constructButton();
		event();
		
		
		Container container = this.getContentPane();
		container.setLayout(null);
		JPanel leftP, IDP, passwordP, confirmPasswordP, nameP, addressP, 
				phoneP, emailP, certificateP;
		JPanel rightP, IDRowP, passwordRowP, confirmPasswordRowP, nameRowP, addressRowP, 
				phoneRowP, emailRowP, certificateRowP;
		JPanel buttonRowP;
		
		leftP = new JPanel(new GridLayout(8,1));
		IDP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		passwordP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		confirmPasswordP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addressP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		phoneP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		emailP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		certificateP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		rightP = new JPanel(new GridLayout(8, 1));
		IDRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		passwordRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		confirmPasswordRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		nameRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		addressRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		phoneRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		emailRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		certificateRowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		
		buttonRowP = new JPanel(new GridLayout(1, 2, 20, 10));
		
		//		
		IDP.add(idL);
		IDP.setOpaque(false);
		passwordP.add(passwordL);
		passwordP.setOpaque(false);
		confirmPasswordP.add(confirmPasswordL);
		confirmPasswordP.setOpaque(false);
		nameP.add(nameL);
		nameP.setOpaque(false);
		addressP.add(addressL);
		addressP.setOpaque(false);
		phoneP.add(phoneNumberL);
		phoneP.setOpaque(false);
		emailP.add(emailAddressL);
		emailP.setOpaque(false);
		certificateP.add(certificateL);
		certificateP.setOpaque(false);
				
		IDRowP.add(idT);
		IDRowP.add(duplicationB);
		IDRowP.setOpaque(false);
		passwordRowP.add(passwordT);
		passwordRowP.add(new JLabel("(5~12�� �̳��� �Է�)"));
		passwordRowP.setOpaque(false);
		confirmPasswordRowP.add(confirmPasswordT);
		confirmPasswordRowP.add(new JLabel("(�� �� �� �Է�)"));
		confirmPasswordRowP.setOpaque(false);
		nameRowP.add(nameT);
		nameRowP.add(new JLabel("(�ѱ� �ִ� 6�ڸ� ����)"));
		nameRowP.setOpaque(false);
		addressRowP.add(addressT);
		addressRowP.setOpaque(false);
		phoneRowP.add(phoneNumberT1);
		phoneRowP.add(new JLabel(" -  "));
		phoneRowP.add(phoneNumberT2);
		phoneRowP.add(new JLabel(" -  "));
		phoneRowP.add(phoneNumberT3);
		phoneRowP.setOpaque(false);
		emailRowP.add(emailAddressT);
		emailRowP.add(new JLabel("@"));
		emailRowP.add(mailSelect);
		emailRowP.setOpaque(false);
		certificateB.setSize(getPreferredSize());
		certificateRowP.add(certificateB);
		certificateRowP.add(new JLabel("      ������ȣ :"));
		certificateRowP.add(certificateT);
		certificateRowP.setOpaque(false);
		
		leftP.setBounds(20, 20, 100, 300);	
		rightP.setBounds(120, 20, 250, 300);
		buttonRowP.setBounds(20, 330, 350, 50);
		leftP.setOpaque(false);
		rightP.setOpaque(false);
		buttonRowP.setOpaque(false);
		
		
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
		buttonRowP.setBackground(new Color(230,255,255));
				
		container.add(leftP);
		container.add(rightP);
		container.add(buttonRowP);
		
		
		
		signImage = new ImageIcon("sign.png");
        JPanel signback = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(signImage.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        signback.setLayout(null);
       
        signback.add(leftP);
        signback.add(rightP);
        signback.add(buttonRowP);
       
        signScrollPane = new JScrollPane(signback);
        setContentPane(signScrollPane);

        //
        
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 400;
		int frameH = 420;
		
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}//constr

	private void event() {
		cancelB.addActionListener(this);
		registerB.addActionListener(this);
		duplicationB.addActionListener(this);
		certificateB.addActionListener(this);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				SignUpMember.this.setVisible(false);
				SignUpMember.this.dispose();
			}
		});
	}

	private void constructLabel() {
		idL = new JLabel("���̵�(ID) *");
		passwordL = new JLabel("��й�ȣ *");
		confirmPasswordL = new JLabel("��й�ȣ Ȯ�� *");
		nameL = new JLabel("�̸� *");
		addressL = new JLabel("�ּ�");
		phoneNumberL = new JLabel("�޴��� ��ȣ");
		emailAddressL = new JLabel("�̸��� *");	
		certificateL = new JLabel("���� ���� �߼� :");
	}//constructLabel

	private void constructTextField() {
		idT = new JTextField(12);
		idT.setDocument(new JTextFieldLimit(10));
		passwordT = new JPasswordField(8);
		passwordT.setDocument(new JTextFieldLimit(12));
		confirmPasswordT = new JPasswordField(8);
		confirmPasswordT.setDocument(new JTextFieldLimit(12));
		nameT = new JTextField(8);
		nameT.setDocument(new JTextFieldLimit(12));
		addressT = new JTextField(21);
		phoneNumberT1 = new JTextField(4);
		phoneNumberT1.setDocument(new JTextFieldLimit(3));
		phoneNumberT2 = new JTextField(5);
		phoneNumberT2.setDocument(new JTextFieldLimit(4));
		phoneNumberT3 = new JTextField(5);
		phoneNumberT3.setDocument(new JTextFieldLimit(4));
		emailAddressT = new JTextField(10);
		certificateT = new JTextField(8);
	}//constructTextField

	private void constructButton() {
		duplicationB = new JButton("�ߺ� Ȯ��");
		duplicationB.setBackground(new Color(250,250,190));
		cancelB = new JButton("���");
		cancelB.setBackground(Color.gray);
		registerB = new JButton(" �ۼ� �Ϸ� ");
		certificateB = new JButton("�߼�");
		certificateB.setBackground(new Color(250,250,190));
	}//constructButton
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("�߼�")) mailSend();
		else if(e.getActionCommand().equals("���")) {
			setVisible(false);
			dispose();
		}		
		else if(e.getActionCommand().equals("�ߺ� Ȯ��")) checkID();
		else if(e.getSource()==registerB) memberRegister();		
	}
	
	//ȸ������� DB�� �����Ͽ� �Է��� ID�� �̹� �����ϴ��� �˻��Ѵ�.
	private void checkID() {//true: �ߺ�����, false: ��밡��
		String userID = idT.getText().trim();
		if(userID.equals("")) {
			JOptionPane.showMessageDialog(this, "���̵�(ID)�� �ʼ� �Է»����Դϴ�.");
			return;
		}
		else if(userID.length()<3) {
			JOptionPane.showMessageDialog(this, "���̵�(ID)�� 3���� �̻� �Է��ϼ���.");
			return;
		}
		//DB �ߺ�üũ
		MembershipDAO membershipDAO = MembershipDAO.getInstance();
		duplicateCheck = membershipDAO.isIDExist(userID);
		if(!duplicateCheck) JOptionPane.showMessageDialog(this, "��� ������ ���̵� �Դϴ�.");
		else {
			JOptionPane.showMessageDialog(this, "�̹� �����ϴ� ���̵� �Դϴ�."
												+ "\n�ٸ� ���̵� �Է��� �ּ���.");
		}
	}

	//����ڰ� �Է��� �����ּҷ� ���������� �߼��Ѵ�.(�߽���:songming05)
	private void mailSend() {//true: �ߺ�����, false: ��밡��
		String userEmail = emailAddressT.getText().trim();
		//DB �ߺ�üũ
		boolean emailExist=false;
		MembershipDAO membershipDAO = MembershipDAO.getInstance();
		emailExist = membershipDAO.isEmailExist(userEmail);
		if(emailExist) {
			JOptionPane.showMessageDialog(this, "�̹� �����ϴ� �̸��� �Դϴ�."
									+ "\n�ٸ� �̸��� �ּҸ� �Է��� �ּ���.");
			return;
		}
		
		MailSendManager mailSendManager = new MailSendManager(userEmail);
		
		certificationKey = mailSendManager.getCertificationKey();
		if(certificationKey.length()>4) JOptionPane.showMessageDialog(this, "���� ������ �߼۵Ǿ����ϴ�.");
	}
	
	/*
	 * ȸ��DB�� �ø��� ���� ��� ���ռ� �˻縦 �Ѵ�.
	 * ID�� 10���� �̻� �ԷºҰ�, password�� 12�� �̻� �Ұ�
	 * �̸��� ���� �ִ�12��, �ѱ� �ִ� 6��
	 */
	private void memberRegister() {
		String userID = idT.getText().trim();
		if(userID.equals("")) {
			JOptionPane.showMessageDialog(this, "���̵�(ID)�� �ʼ� �Է»����Դϴ�.");
			return;
		}
		if(duplicateCheck) {
			JOptionPane.showMessageDialog(this, "���̵�(ID) �ߺ�Ȯ���� ���ֽñ� �ٶ��ϴ�.");
			return;
		}
		String userPassword = passwordT.getText().trim();
		if(userPassword.equals("")) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �ʼ� �Է»����Դϴ�.");
			return;
		}
		else if(userPassword.length()<5) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� 5���� �̻� �Է��ϼ���.");
			return;
		}
		String confirm = confirmPasswordT.getText().trim();
		if(!userPassword.equals(confirm)) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			return;
		}
		String userName = nameT.getText().trim();
		if(userName.equals("")) {
			JOptionPane.showMessageDialog(this, "�̸��� �ʼ� �Է»����Դϴ�.");
			return;
		}
		byte[] nameByte = userName.getBytes();
		int engCount=0, korCount=0;
		for(byte data : nameByte) {
			if(data>0)	engCount++;
			else if(data<0) korCount++;
		}
		if(korCount+engCount>12) {
			JOptionPane.showMessageDialog(this, "�̸��� �ѱ� 6��, ���� 12�� ���� �����մϴ�.");
			return;
		}
		String userAddress = addressT.getText().trim();
		String userPhone = phoneNumberT1.getText().trim()
							+ phoneNumberT2.getText().trim()
							+ phoneNumberT3.getText().trim();
		String userEmail = emailAddressT.getText().trim()+"@gmail.com";
		if(userEmail.equals("")) {
			JOptionPane.showMessageDialog(this, "�̸����� �ʼ� �Է»����Դϴ�.");
			return;
		}
		
		String inputKey = certificateT.getText().trim();		
		if(inputKey.equals(certificationKey)) {
			certificateCheck = true;
			JOptionPane.showMessageDialog(this, "�̸��� ������ �Ϸ�Ǿ����ϴ�.");
		}
		if(!certificateCheck) JOptionPane.showMessageDialog(this, "�̸��� ������ ���� �ʾҽ��ϴ�.");
		
		if(certificateCheck && !duplicateCheck) {
			//MembershipDTO �� �߰�
			MembershipDTO membershipDTO = new MembershipDTO();
			MembershipDAO membershipDAO = MembershipDAO.getInstance();
			int sequence = membershipDAO.getSeq();
			
			membershipDTO.setSequence(sequence);
			membershipDTO.setId(userID);
			membershipDTO.setPassword(userPassword);
			membershipDTO.setName(userName);
			membershipDTO.setAddress(userAddress);
			membershipDTO.setPhone(userPhone);
			membershipDTO.setEmail(userEmail);
			
			//DB �� ���
			membershipDAO.insertArticle(membershipDTO);
			
			JOptionPane.showMessageDialog(this, "ȯ���մϴ�!! "+userName+"��"
											+ "\nȸ�������� �Ϸ� �Ǿ����ϴ�."
											+ "\n�α���â���� �̵��մϴ�.");
			//�α��� â����
			setVisible(false);
			dispose();
		}
		
	}
//	public static void main(String[] args) {
//		SignUpMember s = new SignUpMember();
//	}

}
