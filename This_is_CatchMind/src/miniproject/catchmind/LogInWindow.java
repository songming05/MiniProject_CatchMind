package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import miniproject.membership.dao.MembershipDAO;

public class LogInWindow extends JFrame implements ActionListener{
	private JLabel idL, passwordL;
	private JTextField idT;
	private JPasswordField passwordT;
	private JButton cancelB, joinB, loginB;
	private JRadioButton findIDB, findPasswordB;
	
	public static void main(String[] args) {
		LogInWindow logInWindow = new LogInWindow();
		while(true) {
			try {
				logInWindow.startBGM(0);
				Thread.sleep(152000);
				logInWindow.startBGM(1);
				Thread.sleep(190000);
				logInWindow.startBGM(2);
				Thread.sleep(201000);
				logInWindow.startBGM(3);
				Thread.sleep(150000);
				logInWindow.startBGM(4);
				Thread.sleep(118000);
				//break;
			} catch(Exception e) {
				
			}
		}
	}
	
	public LogInWindow() {
		super("ĳġ���ε� �α���");
		
		constructField();
		event();
		
		
		setLayout(null);
		Container container = this.getContentPane();
		Panel labelP, textP, loginP, findID_PasswordP;
		labelP = new Panel(new GridLayout(2,1,5,10));
		textP = new Panel(new GridLayout(2,1,5,20));
		loginP = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		findID_PasswordP =  new Panel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		labelP.setBounds(20, 20, 100, 80);		
		textP.setBounds(120, 20, 150, 80);
		loginP.setBounds(20, 120, 260, 40);
		findID_PasswordP.setBounds(20, 160, 250, 30);
		
		labelP.add(idL);
		labelP.add(passwordL);
		textP.add(idT);
		textP.add(passwordT);
		loginP.add(cancelB);
		loginP.add(joinB);
		loginP.add(loginB);
		findID_PasswordP.add(findIDB);
		findID_PasswordP.add(findPasswordB);
		
		
		//labelP.setBackground(Color.DARK_GRAY);
		//textP.setBackground(Color.WHITE);
		//loginP.setBackground(Color.pink);;
		
		container.add(labelP);
		container.add(textP);
		container.add(loginP);
		container.add(findID_PasswordP);
		
		//�̹��� ���� �� ������ ����
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameW = 300;
		int frameH = 220;
		setBounds((dimension.width/2-frameW/2), (dimension.height/2-frameH/2), frameW, frameH);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
	}

	private void event() {
		cancelB.addActionListener(this);
		joinB.addActionListener(this);
		loginB.addActionListener(this);
		passwordT.addActionListener(this);
		findIDB.addActionListener(this);
		findPasswordB.addActionListener(this);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				int confirm = JOptionPane.showConfirmDialog(LogInWindow.this, "������ �����Ͻðڽ��ϱ�?");
				if(confirm==JOptionPane.YES_OPTION) System.exit(0);
				else return;
			}
		});
	}

	private void constructField() {
		idL = new JLabel("���̵�(ID) �Է� :");
		passwordL = new JLabel("��й�ȣ �Է� :");
		idT = new JTextField(10);
		passwordT = new JPasswordField(10);
		cancelB = new JButton("���");
		joinB = new JButton("ȸ������");
		joinB.setToolTipText("ȸ������ â���� �̵��մϴ�");
		loginB = new JButton("�α���");
		findIDB = new JRadioButton("���̵� /", new ImageIcon());
		findPasswordB = new JRadioButton("��й�ȣ ã��", new ImageIcon());	
		findIDB.setToolTipText("���̵� ã��");
		findPasswordB.setToolTipText("��й�ȣ ã��");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancelB) {
			int confirm = JOptionPane.showConfirmDialog(this, "������ �����Ͻðڽ��ϱ�?");			
			if(confirm==JOptionPane.YES_OPTION) System.exit(0);
			else return;
		} else if(e.getSource()==joinB) {
			SignUpMember signUpMember = new SignUpMember();			
		} else if(e.getSource()==loginB || e.getSource()==passwordT) {
			String userID = idT.getText().trim();
			String userPWD = passwordT.getText().trim();
			
			MembershipDAO membershipDAO = MembershipDAO.getInstance();
			boolean idCheck = membershipDAO.isIDCorrespond(userID);
			boolean passwordCheck = membershipDAO.isPasswordCorrespond(userID, userPWD);
			if(!idCheck) {
				JOptionPane.showMessageDialog(this, "�������� �ʴ� ���̵� �Դϴ�.");
				return;				
			}
			else if(!passwordCheck) {
				JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				return;				
			}
			else {
				JOptionPane.showMessageDialog(this, "�α��� ����!");
				//memebershipDTO�� IdNameScore DTO�� ����!
				
				String userName = membershipDAO.getName(userID);
				int userScore = membershipDAO.getScore(userID);
				IdNameScoreDTO idnamescoreDTO = new IdNameScoreDTO();
				idnamescoreDTO.setId(userID);
				idnamescoreDTO.setName(userName);
				idnamescoreDTO.setScore(userScore);
				
				//���� â ����!
				WaitingRoomClient waitingroomclient = new WaitingRoomClient();
				waitingroomclient.event();
				//waitingroomclient.service(idnamescoreDTO);			
				
				this.setVisible(false);
				this.dispose();
				return;				
			}
		} else if(e.getSource()==findIDB) {
			String findID="";
			FindIDorPassword findIDorPassword = new FindIDorPassword(findID);			
		} else if(e.getSource()==findPasswordB) {
			String findID="";
			String findPassword="";
			FindIDorPassword findIDorPassword = new FindIDorPassword(findID, findPassword);			
		}
	}
	
	public void startBGM(int num) {
		File[] bgmArray = new File[5];
		AudioInputStream audioInputStream;
		AudioFormat audioFormat;
		DataLine.Info info;
		
		bgmArray[0] = new File("Lazy_Rock.wav"); // ���ÿ��� ���� ������ ������ ��
		bgmArray[1] = new File("Lucid_Dreamer.wav");
		bgmArray[2] = new File("Fond_Memories.wav");
		bgmArray[3] = new File("If_I_Had_a_Chicken.wav");
		bgmArray[4] = new File("From_the_Fall.wav");
		
		Clip clip;
		
		try {
			audioInputStream = AudioSystem.getAudioInputStream(bgmArray[num]);
			audioFormat = audioInputStream.getFormat();
			info = new DataLine.Info(Clip.class, audioFormat);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(audioInputStream);
			clip.start();
			
		} catch (Exception e) {
			System.out.println("err : " + e);
		}
		
	}

}
