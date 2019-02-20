package miniproject.catchmind;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class ChatFrame extends JFrame
{
	private JTextField chatField; //입력창
	private JTextArea chatArea; //메세지 출력창
	private JList playerList; //참가자 목록창
	private JButton exitC,inputC,readyC,startC;
	
	public ChatFrame(){
		super("채팅창");
		setLayout(null);
		
		chatField = new JTextField();
		chatArea= new JTextArea();
		playerList = new JList();
		exitC=new JButton("나가기");
		inputC=new JButton("입력");
		readyC=new JButton("준비");
		startC=new JButton("시작");
		
		chatField.setBounds(40,550,300,30);
		chatArea.setBounds(40,300,400,220);
		playerList.setBounds(40,50,400,220);
		exitC.setBounds(460,50,100,30);
		inputC.setBounds(360,550,80,30);
		readyC.setBounds(40,600,180,30);
		startC.setBounds(260,600,180,30);
		
		Container containerC =this.getContentPane();
		containerC.add(chatField);
		containerC.add(chatArea);
		containerC.add(playerList);
		containerC.add(exitC);
		containerC.add(inputC);
		containerC.add(readyC);
		containerC.add(startC);
		
		setBounds(700,100,600,700);
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
				}
		});
	}
	public static void main(String[] args) 
	{
		ChatFrame cf= new ChatFrame();
	}
}

