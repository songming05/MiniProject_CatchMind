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

public class FindIDorPassword extends JFrame implements ActionListener{
	private JLabel nameL, emailL;
	private JTextField nameT, emailT;
	private String inputName, inputEmail;
	private JButton findB;
	private int checkCase;

	public FindIDorPassword(String findID) {
		checkCase=10;
		constructField();
		event();
		emailT.setText("아이디만 찾는 중");
		Container container = this.getContentPane();
		
		container.add(nameL);
		container.add(nameT);	
		container.add(findB);
	}

	public FindIDorPassword(String findID, String findPassword) {
		checkCase=20;
		constructField();
		event();
		
		Container container = this.getContentPane();
		
		container.add(nameL);
		container.add(nameT);
		container.add(emailL);
		container.add(emailT);
		container.add(findB);
	}
	
	private void constructField() {
		nameL = new JLabel("이름을 입력하세요 :         ");
		emailL = new JLabel("이메일을 입력하세요 :");
		nameT = new JTextField(10);
		emailT = new JTextField(10);
		findB = new JButton("찾기");
		
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
		if(inputName.equals("")) JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
		else if(inputEmail.equals("")) JOptionPane.showMessageDialog(this, "이메일을 입력하세요");
		if(checkCase==10);
		else if(checkCase==20);
		//DAO
	}

}
