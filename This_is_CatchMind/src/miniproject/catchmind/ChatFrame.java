package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
class ChatFrame extends JFrame implements ActionListener,Runnable{
	private JTextField chatSend; //�Է�â
	private JTextArea chatPrint; //�޼��� ���â
	private JList<ChatDTO> playerList; //������ ���â
	private JButton exitB,sendB,readyB,startB;//�������ư,���۹�ư,�غ��ư,���۹�ư
	private ObjectInputStream chatClientOis;
	private ObjectOutputStream chatClientOos;
	private Socket chatSocket;
	private DefaultListModel<ChatDTO> chatModel;
	private String chatNickName;
	
	private int x1, y1, x2, y2; //��ǥ
	private JButton[] btn; //���� ��ư
	private JRadioButton thinB, thickB; //�� ����
	private catchmind_Canvas can; //ĵ����
	private ArrayList<catchmind_ShapDTO> sendList; //������ ������ list
	private ArrayList<catchmind_ShapDTO> serverInfoList; //�������� �޾ƿ� list	
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	

	private int colorNum; //���� ����
	
	public ChatFrame(){
		super("�̰��� �׸��ΰ� �����ΰ� �̶����� �̷� ������ ������.");
		setLayout(null);

		chatSend = new JTextField();
		chatPrint= new JTextArea();
		chatModel = new DefaultListModel<ChatDTO>();
		playerList = new JList<ChatDTO>(chatModel);
		exitB=new JButton("������");
		sendB=new JButton("����");
		readyB=new JButton("�غ�");
		startB=new JButton("����");
		
		chatPrint.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(chatPrint);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add("Center",scroll);//
		
		chatSend.setBounds(640,585,300,30);
		scroll.setBounds(640,360,400,220);
		playerList.setBounds(640,50,395,300);
		exitB.setBounds(1050,50,100,30);
		sendB.setBounds(960,585,80,30);
		readyB.setBounds(635,650,200,50);
		startB.setBounds(850,650,200,50);
		
		btn = new JButton[6];
		String[] title = {"����", "����", "�ʷ�", "�Ķ�", "���", "��ȫ"};
		Color[] color = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
		for(int i=0; i<title.length; i++) {
			btn[i] = new JButton(title[i]);
			btn[i].setBackground(color[i]);
		}
		btn[0].setBounds(50, 620, 80, 80);
		btn[1].setBounds(155, 620, 80, 80);
		btn[2].setBounds(245, 620, 80, 80);
		btn[3].setBounds(335, 620, 80, 80);
		btn[4].setBounds(425, 620, 80, 80);
		btn[5].setBounds(515, 620, 80, 80);
		
		//�� ����
		thinB = new JRadioButton("����", true);
		thickB = new JRadioButton("����");
		thinB.setBounds(50, 700, 60, 60);
		thickB.setBounds(150, 700, 60, 60);
		
		//JRadioButton �׷�
		ButtonGroup group = new ButtonGroup();
		group.add(thickB); group.add(thinB);
		
		//ĵ����
		can = new catchmind_Canvas(this);
		can.setBounds(50, 50, 550, 565);
		
		//����Ʈ
		sendList = new ArrayList<catchmind_ShapDTO>();
		serverInfoList = new ArrayList<catchmind_ShapDTO>();
		
		add(can);
		
		for(int i=0; i<title.length; i++) {
			add(btn[i]);
		}
		
		add(thickB);
		add(thinB);
		
		Container containerC =this.getContentPane();
		containerC.add(chatSend);
		containerC.add("Center", scroll);
		containerC.add(playerList);
		containerC.add(exitB);
		containerC.add(sendB);
		containerC.add(readyB);
		containerC.add(startB);
		
		setBounds(700,100, 1200, 800);
		setVisible(true);
		
		//��ư �̺�Ʈ
		for(int i=0; i<title.length; i++) btn[i].addActionListener(this);
		
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){				
				try {
					if(chatClientOos==null||chatClientOis==null) System.exit(0);
					ChatDTO chatdto=new ChatDTO();
					chatdto.setCommand(Info.WAIT);
					chatClientOos.writeObject(chatdto);
					chatClientOos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		can.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				x1 =e.getX();y1 =e.getY();			
			}
			@Override
			public void mouseReleased(MouseEvent e){//       ===================   ���콺 ������ ===========================
				x2 = e.getX();	y2 = e.getY();

				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1);	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);
				
				dto.setColorNum(colorNum);
				
				if(thinB.isSelected()) dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);				
				sendList.add(dto);		
				
				try {
					ChatDTO chatDTO = new ChatDTO();
					chatDTO.setCommand(Info.WAIT);
					
					chatClientOos.writeObject(chatDTO);
					chatClientOos.writeObject(sendList);
					chatClientOos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		});
		
		can.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				System.out.print("drag    ");
				x2 = e.getX();	y2 = e.getY();	can.repaint();
				
				//����
				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1);	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);				
				dto.setColorNum(colorNum);				
				if(thinB.isSelected()) 	dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);
				sendList.add(dto);
				
				x1 = x2;
				y1 = y2;
				

			}
		});
		
		exitB.addActionListener(this);
		sendB.addActionListener(this);
		readyB.addActionListener(this);
		startB.addActionListener(this);
		chatSend.addActionListener(this);//����Ű�� ���۰���
		
	}//������ ����

	@Override
	public void actionPerformed(ActionEvent e) {
		String chatMsg=chatSend.getText();
		
		if(e.getSource()==chatSend || e.getSource()==sendB) {
			
			ChatDTO chatDTO =new ChatDTO();
			chatDTO.setCommand(Info.SEND);
			chatDTO.setMessage(chatMsg);
			
			try {
				chatClientOos.writeObject(chatDTO);
				chatClientOos.writeObject(sendList);
				chatClientOos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chatSend.setText("");
		}else if(e.getSource()==exitB||chatMsg.toLowerCase().equals("exit")) {
			
		}

		catchmind_ShapDTO dto = new catchmind_ShapDTO();
		
		if(e.getSource() == btn[0]) { colorNum = 0;
		}else if(e.getSource() == btn[1]) { colorNum = 1;
		}else if(e.getSource() == btn[2]) { colorNum = 2;
		}else if(e.getSource() == btn[3]) { colorNum = 3;
		}else if(e.getSource() == btn[4]) {	colorNum = 4;
		}else if(e.getSource() == btn[5]) {	colorNum = 5;
		}
																	//������  ä�� ������
		/*
		if(e.getSource()==exitB || e.getSource()==readyB || e.getSource()==startB) {

			
			if(e.getSource()==exitB) {	chatDTO.setCommand(Info.EXIT);
			}
			else if(e.getSource()==readyB) {
			}
			else if(e.getSource()==startB) {				
			} 
			else{
				
			}
			
			try {
				
				chatClientOos.writeObject(chatDTO);
				chatClientOos.writeObject(sendList);
				chatClientOos.flush();
			
			} catch (IOException io) {
				io.printStackTrace();
			}
			*/
			
		
	}//actionPerformed ����
	
	public void chatIp() {
		String chatServerIp="192.168.53.17"; //�п������� 192.168.51.79 
		
		if(chatServerIp==null|| chatServerIp.length()==0) {
			System.out.println("���� IP�� �Էµ��� �ʾҽ��ϴ�.");
			System.exit(0);
		}
		chatNickName =JOptionPane.showInputDialog(this,"�г����� �Է��ϼ���","�г���",JOptionPane.INFORMATION_MESSAGE);
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
			chatClientOos.writeObject(sendList);
			chatClientOos.flush();
			
			System.out.println("CT�� ���µ�");
			chatClientOos.flush();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		Thread chatThread =new Thread(this);
		chatThread.start();
	}//chatIp ����
	
	public void run() {		//������ ���� �޴°� 
		ChatDTO chatDTO =null;
		
		while(true) {
			try {
				chatDTO=(ChatDTO)chatClientOis.readObject();
				shapeDTOList = (ArrayList<catchmind_ShapDTO>) chatClientOis.readObject();
				can.repaint();
				
				if(chatDTO.getCommand()==Info.EXIT){
					chatClientOos.close();
					chatClientOis.close();
					chatSocket.close();
					System.exit(0);
				}
				else if(chatDTO.getCommand()==Info.SEND){
					
					chatPrint.append(chatDTO.getMessage()+"\n");
					int chatPos = chatPrint.getText().length();
					chatPrint.setCaretPosition(chatPos);//��ũ�ѹ� ��ġ ��ӿ�û
				}
				
				System.out.println("����Ʈ�� �ڵ鷯�� �о���.");
				
			}catch (IOException io) {
					io.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}//while�� ����
	}//run ����
	public void getchatModel() {
		//DB�� ��� �׸��� ������ JList�� �Ѹ���
		ChatDAO chatDAO = ChatDAO.getInstance();//DAO�� �����ϱ����� ����
		ArrayList<ChatDTO> chatList = chatDAO.getChatList();//DAO�� getFriendList()ȣ���ؼ� ��ȯ���� list�� ����	
		for(ChatDTO dto : chatList) chatModel.addElement(dto);//list�� �ִ°� ���ʴ�� ���	
	}
	
	
	
	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public JRadioButton getThinB() {
		return thinB;
	}

	public void setThinB(JRadioButton thinB) {
		this.thinB = thinB;
	}

	public JRadioButton getThickB() {
		return thickB;
	}

	public void setThickB(JRadioButton thickB) {
		this.thickB = thickB;
	}

	public ArrayList<catchmind_ShapDTO> getSendList() {
		return sendList;
	}
	public void setSendList(ArrayList<catchmind_ShapDTO> sendlist) {
		this.sendList = sendlist;
	}
	public ArrayList<catchmind_ShapDTO> getServerInfoList() {
		return serverInfoList;
	}

	public void setServerInfoList(ArrayList<catchmind_ShapDTO> serverInfoList) {
		this.serverInfoList = serverInfoList;
	}
	public int getColorNum() {
		return colorNum;
	}

	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
	}

	public static void main(String[] args) 
	{
		ChatFrame chatframe= new ChatFrame();
		chatframe.chatIp();
	}
}

