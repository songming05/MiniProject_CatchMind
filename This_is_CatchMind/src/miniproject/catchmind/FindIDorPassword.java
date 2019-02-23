package miniproject.catchmind;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FindIDorPassword extends JFrame{
	private JLabel nameL, emailL;
	private JTextField nameT, emailT;

	public FindIDorPassword(String findID) {
		constructField();
		event();
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		Container container = this.getContentPane();
		
		container.add(nameL);
		container.add(nameT);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 300;
		int frameH = 220;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
	}

	public FindIDorPassword(String findID, String findPassword) {
		constructField();
	}
	
	private void constructField() {
		nameL = new JLabel("이름을 입력하세요 :");
		emailL = new JLabel("비밀번호를 입력하세요 :");
		nameT = new JTextField(10);
		emailT = new JTextField(10);	
	}
	private void event() {
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				setVisible(false);
				dispose();
			}
		});
		
	}

}
