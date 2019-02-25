package miniproject.catchmind;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import miniproject.membership.dao.MembershipDAO;

public class FindIDorPassword extends JFrame implements ActionListener{
	private JLabel nameL, emailL, idL;
	private JTextField nameT, emailT, idT;
	private String inputName, inputEmail, inputID;
	private JButton findB;
	private int checkCase;

	public FindIDorPassword(String findID) {
		checkCase=10;
		constructField();
		event();
		Container container = this.getContentPane();
		idT.setText("���̵� ã�� ��");
		
		container.add(nameL);
		container.add(nameT);
		container.add(emailL);
		container.add(emailT);
		container.add(findB);
	}

	public FindIDorPassword(String findID, String findPassword) {
		checkCase=20;
		constructField();
		event();
		
		Container container = this.getContentPane();
		nameT.setText("��й�ȣ ã�� ��");
		
		container.add(idL);
		container.add(idT);
		container.add(emailL);
		container.add(emailT);
		container.add(findB);
	}
	
	private void constructField() {
		nameL = new JLabel("�̸��� �Է��ϼ��� :     ");
		emailL = new JLabel("�̸����� �Է��ϼ��� :");
		idL = new JLabel("���̵� �Է��ϼ��� :");
		nameT = new JTextField(10);
		emailT = new JTextField(10);
		idT = new JTextField(10);
		findB = new JButton("ã��");
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 300;
		int frameH = 140;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	private void event() {
		nameT.addActionListener(this);
		emailT.addActionListener(this);
		findB.addActionListener(this);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				setVisible(false);
				dispose();
			}
		});
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		inputName = nameT.getText().trim();
		inputEmail = emailT.getText().trim();
		inputID = idT.getText().trim();
		MembershipDAO membershipDAO = MembershipDAO.getInstance();
		//�Է��� �Ǿ����� üũ�ϰ�,
		if(inputName.equals("")) {
			JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���");
			return;
		}
		else if(inputID.equals("")) {
			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���");
			return;
		}
		else if(inputEmail.equals("")) {
			JOptionPane.showMessageDialog(this, "�̸����� �Է��ϼ���");
			return;
		}
		
		//SQL ������ �ε��Ѵ�.
		if(checkCase==10) {			
			String resultID = membershipDAO.findID(inputName, inputEmail);
			if(resultID.equals("")) {
				JOptionPane.showMessageDialog(this, "�������� �ʴ� ȸ�������Դϴ�.");
				return;				
			}
			else {
				JOptionPane.showMessageDialog(this, "ȸ������ ���̵�� <"
						+resultID+">�Դϴ�. \n (Ȯ���� �����ø� �α��� â���� ���ư��ϴ�)");
				setVisible(false);
				dispose();
			}
		}
		else if(checkCase==20) {
			String resultPassword = membershipDAO.findPassword(inputID, inputEmail);
			if(resultPassword.equals("")) {
				JOptionPane.showMessageDialog(this, "�������� �ʴ� ȸ�������Դϴ�.");
				return;				
			}
			else {
				JOptionPane.showMessageDialog(this, "ȸ������ ��й�ȣ�� <"
						+resultPassword+">�Դϴ�. \n (Ȯ���� �����ø� �α��� â���� ���ư��ϴ�)");
				setVisible(false);
				dispose();
			}
			
		}
	}

}
