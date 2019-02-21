package miniproject.catchmind;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
class ChatFrame extends JFrame implements ActionListener,Runnable
{
	private JTextField chatField; //입력창
	private JTextArea chatArea; //메세지 출력창
	@SuppressWarnings("rawtypes")
	private JList<ChatDTO> playerList; //참가자 목록창
	private JButton exitC,sendC,readyC,startC;//나가기버튼,전송버튼,준비버튼,시작버튼
	private ObjectInputStream chatClientOis;
	private ObjectOutputStream chatClientOos;
	private Socket chatSocket;
	private DefaultListModel<ChatDTO> chatModel;
	
	@SuppressWarnings("rawtypes")
	public ChatFrame(){
		super("채팅창");
		setLayout(null);
		
		
		chatField = new JTextField();
		chatArea= new JTextArea();
		chatModel = new DefaultListModel<ChatDTO>();
		playerList = new JList<ChatDTO>(chatModel);
		exitC=new JButton("나가기");
		sendC=new JButton("전송");
		readyC=new JButton("준비");
		startC=new JButton("시작");
		
		chatArea.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(chatArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add("Center",scroll);//
		
		chatField.setBounds(40,550,300,30);
		scroll.setBounds(40,300,400,220);
		//chatArea.setBounds(40,300,400,220);
		playerList.setBounds(40,50,400,220);
		exitC.setBounds(460,50,100,30);
		sendC.setBounds(360,550,80,30);
		readyC.setBounds(40,600,180,30);
		startC.setBounds(260,600,180,30);
		
		Container containerC =this.getContentPane();
		containerC.add(chatField);
		containerC.add("Center", scroll);
		//containerC.add(chatArea);
		containerC.add(playerList);
		containerC.add(exitC);
		containerC.add(sendC);
		containerC.add(readyC);
		containerC.add(startC);
		
		setBounds(700,100,600,700);
		setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				
				try {
					if(chatClientOos==null||chatClientOis==null)System.exit(0);

					ChatDTO chatdto=new ChatDTO();
					chatdto.setCommand(Info.EXIT);
					chatClientOos.writeObject(chatdto);
					chatClientOos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		exitC.addActionListener(this);
		sendC.addActionListener(this);
		readyC.addActionListener(this);
		startC.addActionListener(this);
		chatField.addActionListener(this);//엔터키로 전송가능
		
	}//생성자 종료
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String chatMsg=chatField.getText();
		
		ChatDTO chatDTO =new ChatDTO();
		if(e.getSource()==exitC) {
			chatDTO.setCommand(Info.EXIT);
		}
		else if(e.getSource()==readyC) {
			
		}
		else if(e.getSource()==startC) {
			
		}
		else{
			chatDTO.setCommand(Info.SEND);
			chatDTO.setMessage(chatMsg);
		}
		
		try {
			chatClientOos.writeObject(chatDTO);
			chatClientOos.flush();
		} catch (IOException io) {
			// TODO Auto-generated catch block
			io.printStackTrace();
		}
		chatField.setText("");
	}//actionPerformed 종료
	
	public void chatIp() {
		String chatServerIp=JOptionPane.showInputDialog(this,"서버IP를 입력하세요","192.168.");
		
		if(chatServerIp==null|| chatServerIp.length()==0) {
			System.out.println("서버 IP가 입력되지 않았습니다.");
			System.exit(0);
		}
		String chatNickName =JOptionPane.showInputDialog(this,"닉네임을 입력하세요","닉네임",JOptionPane.INFORMATION_MESSAGE);
		if(chatNickName == null || chatNickName.length()==0){
			chatNickName="guest";
		}
		
		try {
			chatSocket =new Socket(chatServerIp,9500);
				
			chatClientOos=new ObjectOutputStream(chatSocket.getOutputStream());
			chatClientOis=new ObjectInputStream(chatSocket.getInputStream());
				
			ChatDTO chatDTO=new ChatDTO();
			chatDTO.setCommand(Info.JOIN);
			chatDTO.setNickName(chatNickName);
			chatClientOos.writeObject(chatDTO);
			chatClientOos.flush();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		Thread chatThread =new Thread(this);
		chatThread.start();
	}//chatIp 종료
	
	public void run() {
		ChatDTO chatDTO =null;
		while(true) {
			try {
				chatDTO=(ChatDTO)chatClientOis.readObject();
				if(chatDTO.getCommand()==Info.EXIT){
					chatClientOos.close();
					chatClientOis.close();
					chatSocket.close();
					System.exit(0);
				}
				else if(chatDTO.getCommand()==Info.SEND){
					chatArea.append(chatDTO.getMessage()+"\n");
					int chatPos = chatArea.getText().length();
					chatArea.setCaretPosition(chatPos);//스크롤바 위치 계속요청
				}
			}catch (IOException io) {
					// TODO Auto-generated catch block
					io.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}//while문 종료
	}//run 종료
	
	
	/*
	private void chatinit() {//데이터베이스의 모든 항목(데이터)을 꺼내서 JList에 뿌리는 메소드
		//DB의 모든 항목을 꺼내서 JList에 뿌리기
		FriendDAO friendDAO = FriendDAO.getInstance();//DAO를 참조하기위해 생성
		ArrayList<FriendDTO> list = friendDAO.getFriendList();//DAO의 getFriendList()호출해서 반환값을 list에 저장
		
		for(FriendDTO friendDTO : list) {
			model.addElement(friendDTO);//list에 있는값 차례대로 출력
		}
	}
	*/
	
	
	
	public static void main(String[] args) 
	{
		ChatFrame chatframe= new ChatFrame();
		chatframe.chatIp();
		//chatframe.chatinit();
	}
}

