package miniproject.catchmind;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class LogInWindow extends JFrame{
	public static void main(String[] args) {
		LogInWindow logInWindow = new LogInWindow();
	}
	
	public LogInWindow() {
		super("ĳġ���ε� �α���");
		//�̹��� ���� �� ������ ����
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 300;
		int frameH = 200;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}

}
