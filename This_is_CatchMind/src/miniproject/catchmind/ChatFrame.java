package miniproject.catchmind;

import java.awt.Color;
import java.awt.Graphics;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import miniproject.membership.dao.MembershipDAO;
import miniproject.roomdao.RoomDAO;




@SuppressWarnings("serial")
class ChatFrame extends JFrame implements ActionListener,Runnable{
	private JTextField chatSend, answerT; //�Է�â, ����â
	private JTextArea chatPrint; //�޼��� ���â
	private JList<GameUserDTO> playerList; //������ ���â
	private JButton exitB,sendB,readyB,startB, answerB, clearB;//�������ư,���۹�ư,�غ��ư,���۹�ư, ���찳��ư
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket chatSocket;
	private DefaultListModel<GameUserDTO> chatModel;
	private String chatNickName;
	private int point;
	private int readyCount=0; //�߰��Ѱ�
	private int startCount=0; //���߰��� ����
	private boolean timestop=true;//�߰��Ѱ�
	private static int ii=1;//�߰��Ѱ�
	private String[] Quiz = {"�����", "��", "�¾�", "��", "��ǻ��"};//���߰��� ����
	int[] list = new int[5];//���߰��� ����
	private JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL,0,15); //�߰��Ѱ�
	private int score;
	//
	private ImageIcon chatImage;//
	private JScrollPane chatScrollPane;//
	//
	private int x1, y1, x2, y2; //��ǥ
	private JButton[] btn; //���� ��ư
	private JRadioButton thinB, thickB; //�� ����
	private catchmind_Canvas can = new catchmind_Canvas(this); //ĵ����
	private ArrayList<catchmind_ShapDTO> sendList; //������ ������ list
	private ArrayList<catchmind_ShapDTO> serverInfoList; //�������� �޾ƿ� list	
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	private ArrayList<catchmind_ShapDTO> cloneList;
	private String answer;
	private WaitingRoomClient waitingRoomClient;
	private int colorNum; //���� ����
	
	//------------������ �������°� ���---------------
	private ArrayList<waitingRoomRCreateDTO> RoomData;
	private String roomName;
	private String roomPass;
	private String owner;
	private int person;
	private int roomNumber, currentPerson, roomSeq;

	
	public ChatFrame(WaitingRoomClient waitingRoomClient) {
		this();
		this.waitingRoomClient = waitingRoomClient;
		
	}
	
	public ChatFrame() {
			//ObjectOutputStream oos, ObjectInputStream ois){
		super("�̰��� �׸��ΰ� �����ΰ� �̶����� �̷� ������ ������.");
		//this.chatSocket=chatSoket;
		//this.oos = oos;
		//this.ois = ois;
		
		//System.out.println("chatFrame"+waitingroomuserDTO.getName());
		
		
		

		
		setLayout(null);
		
		chatSend = new JTextField();
		answerT = new JTextField(); //����TextField
		chatPrint= new JTextArea();
		chatModel = new DefaultListModel<GameUserDTO>();
		playerList = new JList<GameUserDTO>(chatModel);
		exitB=new JButton("������");
		sendB=new JButton("����");
		readyB=new JButton("�غ�");
		startB=new JButton("����");
		answerB=new JButton("����");
		clearB = new JButton("�����");
		
		chatPrint.setEditable(false);
		startB.setEnabled(false);//���� ���߿� flase�� �����������
		
		JScrollPane scroll = new JScrollPane(chatPrint);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add("Center",scroll);//
		
		chatSend.setBounds(640,585,300,30);
		answerT.setBounds(50, 580, 470, 30);
		scroll.setBounds(640,360,400,220);
		playerList.setBounds(640,50,395,300);
		exitB.setBounds(1050,50,100,30);
		sendB.setBounds(960,585,80,30);
		readyB.setBounds(635,650,200,50);
		startB.setBounds(850,650,200,50);
		answerB.setBounds(530, 580, 70, 30);
		clearB.setBounds(220, 710, 110, 50);
		bar.setBounds(1050, 100, 100, 30);
		bar.setVisible(false);
		
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
		thinB.setBounds(50, 700, 60, 30);
		thickB.setBounds(150, 700, 60, 30);
		thinB.setBackground(new Color(230,255,255));
		thickB.setBackground(new Color(230,255,255));
		
		//JRadioButton �׷�
		ButtonGroup group = new ButtonGroup();
		group.add(thickB); group.add(thinB);
		
		//ĵ����
		can.setBounds(50, 50, 550, 525);
		can.setEnabled(false);
		
		//����Ʈ
		sendList = new ArrayList<catchmind_ShapDTO>();
		serverInfoList = new ArrayList<catchmind_ShapDTO>();
		RoomData = new ArrayList<waitingRoomRCreateDTO>();
		
		bar.setStringPainted(true);//�߰��Ѱ�
		bar.setString("0��");//�߰��Ѱ�
		
		//
		chatImage = new ImageIcon("game.jpg");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(chatImage.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        background.setLayout(null);
       
        
        background.add(answerB);
        background.add(clearB);
        background.add(answerT);
        
        background.add(can);
        background.add(thickB);
        background.add(thinB);
        
        background.add(chatSend);
        background.add("Center", scroll);
        background.add(playerList);
        background.add(exitB);
        background.add(sendB);
        background.add(readyB);
        background.add(startB);
        background.add(bar);
        for(int i=0; i<title.length; i++) {
        	background.add(btn[i]);
		}
        chatScrollPane = new JScrollPane(background);
        setContentPane(chatScrollPane);
        setResizable(false);
		//
        
		setBounds(700,100, 1200, 800);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		//��ư �̺�Ʈ
		for(int i=0; i<title.length; i++) btn[i].addActionListener(this);
		
		for(int i=0; i<5; i++) {
			list[i] = (int)(Math.random()*5);
			for(int j=0; j<i; j++) {
				if(list[i] == list[j]) {
					i--;
					break;
				}
			}
		}
		clearB.addActionListener(this);
		answerB.addActionListener(this); //���߰��� ����
		
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				MembershipDAO membershipDAO = MembershipDAO.getInstance();
				membershipDAO.scoreUpdate(chatNickName, score);
				try {
					if(oos==null||ois==null)dispose();
						WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
						waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
						waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
						waitingroomrcreateDTO.setRoomName(roomName);
						ChatDTO chatdto=new ChatDTO();
						chatdto.setCommand(Info.EXIT);
						GameUserDTO gameuserDTO = new GameUserDTO();
						
						oos.writeObject(waitingroomchattingDTO);
						oos.writeObject(waitingroomuserDTO);
						oos.writeObject(waitingroomrcreateDTO);
						oos.writeObject(chatdto);
						oos.writeObject(sendList);
						oos.writeObject(gameuserDTO);
						oos.flush();
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
				ArrayList<catchmind_ShapDTO> cloneList = (ArrayList<catchmind_ShapDTO>) sendList.clone();
				
				try {
					WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingroomchattingDTO.setCommand(Info.WAIT);
					waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
					waitingroomuserDTO.setCommand(Info.WAIT);
					waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO.setCommand(Info.WAIT);
					waitingroomrcreateDTO.setRoomName(roomName);
					ChatDTO chatDTO_send = new ChatDTO();
					chatDTO_send.setCommand(Info.WAIT);
					GameUserDTO gameuserDTO = new GameUserDTO();
					gameuserDTO.setCommand(Info.WAIT);
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO_send);
					oos.writeObject(cloneList);
					oos.writeObject(gameuserDTO);
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		});
		
		can.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				//System.out.print("drag    ");
				x2 = e.getX();	y2 = e.getY();	can.repaint();
				
				//����
				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1);	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);				
				dto.setColorNum(colorNum);				
				if(thinB.isSelected()) 	dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);
				sendList.add(dto);
				cloneList = (ArrayList<catchmind_ShapDTO>) sendList.clone();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
				
				try {
					WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingroomchattingDTO.setCommand(Info.WAIT);
					waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
					waitingroomuserDTO.setCommand(Info.WAIT);
					waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO.setCommand(Info.WAIT);
					waitingroomrcreateDTO.setRoomName(roomName);
					ChatDTO chatDTO_send = new ChatDTO();
					chatDTO_send.setCommand(Info.WAIT);
					GameUserDTO gameuserDTO = new GameUserDTO();
					gameuserDTO.setCommand(Info.WAIT);
					
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO_send);
					oos.writeObject(cloneList);
					oos.writeObject(gameuserDTO);
					oos.flush();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
				
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
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
		waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
		ChatDTO chatDTO =new ChatDTO();
		GameUserDTO gameuserDTO = new GameUserDTO();
		
		if(e.getSource()==chatSend || e.getSource()==sendB) {
			waitingroomchattingDTO.setCommand(Info.WAIT);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setRoomName(roomName);
			chatDTO.setNickName(chatNickName);
			chatDTO.setCommand(Info.SEND);
			chatDTO.setMessage(chatMsg);
			gameuserDTO.setCommand(Info.WAIT);
			
			try {
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chatSend.setText("");
			
		}else if(e.getSource()==exitB||chatMsg.toLowerCase().equals("exit")) {
//			chatDTO.setCommand(Info.EXIT);
//			MembershipDAO membershipDAO = MembershipDAO.getInstance();
//			membershipDAO.scoreUpdate(chatNickName, score);
//			try {
//				chatDTO.setCommand(Info.EXIT);
//				
//				oos.writeObject(waitingroomchattingDTO);
//				oos.writeObject(waitingroomuserDTO);
//				oos.writeObject(waitingroomrcreateDTO);
//				oos.writeObject(chatDTO);
//				oos.writeObject(cloneList);
//				oos.writeObject(gameuserDTO);
//				oos.flush();
//				
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			MembershipDAO membershipDAO = MembershipDAO.getInstance();
			membershipDAO.scoreUpdate(chatNickName, score);
			try {
				if(oos==null||ois==null)dispose();
					waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingroomuserDTO = new waitingRoomUserDTO();
					waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO.setRoomName(roomName);
					ChatDTO chatdto=new ChatDTO();
					chatdto.setCommand(Info.EXIT);
					gameuserDTO = new GameUserDTO();
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatdto);
					oos.writeObject(sendList);
					oos.writeObject(gameuserDTO);
					oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		
		}else if(e.getSource() == answerT || e.getSource() == answerB){//���߰��� ����		
			
			if(answerT.getText().equals(answer)) {
				score += 20;
				waitingroomchattingDTO.setCommand(Info.WAIT);
				waitingroomuserDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setRoomName(roomName);
				chatDTO.setCommand(Info.ANSWER);
				chatDTO.setNickName(chatNickName);
				chatDTO.setMessage("["+chatNickName+"] �� �����Դϴ�");
				gameuserDTO.setCommand(Info.WAIT);
				
				try {
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO);
					oos.writeObject(cloneList);
					oos.writeObject(gameuserDTO);
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			
			answerT.setText("");
		}else if (e.getSource()==readyB) {//���߰��� ����
			chatMsg = "READY";
			waitingroomchattingDTO.setCommand(Info.WAIT);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setRoomName(roomName);
			chatDTO.setCommand(Info.READY);
			chatDTO.setMessage(chatMsg);
			chatDTO.setScore(score);
			gameuserDTO.setCommand(Info.WAIT);

			
			if(readyCount==0) {
				readyCount++;
				chatDTO.setReadyCount(readyCount);
				readyB.setEnabled(false);
				
			}else {
				readyCount++;
				chatDTO.setReadyCount(readyCount);
				readyB.setEnabled(false);
			}
			
			try {
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			if(readyCount==4) {//if(readyCount==wrc.getPersonCB()) { ������ �̰�
				startB.setEnabled(true);
			}
		}
		
		else if(e.getSource()==startB) {//���߰��� ����
			can.setEnabled(true);
			timestop=true;
			answerT.setEditable(false);
			startB.setEnabled(false);
			
			if(startCount==0) {
				startCount++;
				chatDTO.setStartCount(startCount);
			}else {
				startCount++;
				chatDTO.setStartCount(startCount);
			}
			
			
			
			answerT.setText(Quiz[list[startCount-1]]);
			
			waitingroomchattingDTO.setCommand(Info.WAIT);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setRoomName(roomName);
			chatDTO.setCommand(Info.START);
			chatDTO.setMessage(answerT.getText());
			gameuserDTO.setCommand(Info.WAIT);
			
			if(startCount == 5) {
				startB.setEnabled(false);
				System.out.println("������ ����Ǿ����ϴ�");
				list = null;
			}
			
			try {
				oos.writeObject(waitingroomchattingDTO);
				oos.writeObject(waitingroomuserDTO);
				oos.writeObject(waitingroomrcreateDTO);
				oos.writeObject(chatDTO);
				oos.writeObject(cloneList);
				oos.writeObject(gameuserDTO);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Thread start = new Thread(new Runnable() {
				catchmind_Canvas can = ChatFrame.this.can;
				@Override
				public void run() {//
					for(int i=ii;i<=15;i++) {
						if(!timestop) break;
						try {
							Thread.sleep(1000);//������ 1000
								
						}catch(InterruptedException ee) {}
						bar.setValue(i);
						ii=i;
						bar.setString(i+"��");
						if(ii==15) {
							ii=1;
							bar.setString(ii-1+"��");
							startB.setEnabled(true);
						}
					}
					can.setEnabled(false);
				}//
			});
			start.start();
		}else if(e.getSource() == clearB) {
			sendList.clear();
			cloneList.clear();
			can.repaint();
		}
		
		else {

			catchmind_ShapDTO dto = new catchmind_ShapDTO();
			
			if(e.getSource() == btn[0]) { colorNum = 0;
			}else if(e.getSource() == btn[1]) { colorNum = 1;
			}else if(e.getSource() == btn[2]) { colorNum = 2;
			}else if(e.getSource() == btn[3]) { colorNum = 3;
			}else if(e.getSource() == btn[4]) {	colorNum = 4;
			}else if(e.getSource() == btn[5]) {	colorNum = 5;
			}
		}
													//������  ä�� ������
	}//actionPerformed ����

	public void run() {		//������ ���� �޴°� 
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		waitingRoomUserDTO waitingroomuserDTO = null;
		waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		ChatDTO chatDTO = null;
		GameUserDTO gameuserDTO = null;
		
		while(true) {
			try {
				waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
				waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
				waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
				chatDTO=(ChatDTO)ois.readObject();
				serverInfoList = (ArrayList<catchmind_ShapDTO>) ois.readObject();
				gameuserDTO = (GameUserDTO) ois.readObject();
				
				System.out.println("�巹�׽� �޾ƿ�waitingroomrcreateDTO.getname"+ waitingroomrcreateDTO.getRoomName());
				
				if(waitingroomrcreateDTO.getRoomName().equals(roomName)) {
					if(serverInfoList != null) can.repaint();
				}

				
				
				
				//--------------------------------chatDTO -----------------------------------
				if(chatDTO.getCommand()==Info.EXIT){
					
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						
						waitingroomrcreateDTO.setCurrentPerson(currentPerson);
						RoomDAO roomDAO = RoomDAO.getInstance();
						roomDAO.minusPerson(waitingroomrcreateDTO, roomSeq);
						
						int currentPerson = roomDAO.getCurrentPerson(waitingroomrcreateDTO);
						if(currentPerson==0) {
							//�� ��Ͽ��� ����, DB LimitPerson=0 ����
							roomDAO.initLimitPerson(waitingroomrcreateDTO, roomSeq);
						}
						this.setVisible(false);
						dispose();
						waitingRoomClient.setVisible(true);
					}
					
					//System.exit(0);
				}else if(chatDTO.getCommand()==Info.SEND){
					
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						chatPrint.append(chatDTO.getMessage()+"\n");
						int chatPos = chatPrint.getText().length();
						chatPrint.setCaretPosition(chatPos);//��ũ�ѹ� ��ġ ��ӿ�û
					}
	

					
				}else if(chatDTO.getCommand()==Info.READY) {//���߰��� ����
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						readyCount = chatDTO.getReadyCount();
						System.out.println(readyCount);
						
						if(chatDTO.getReadyCount() == 2 && owner.equals("����")) {
							startB.setEnabled(true);
							readyB.setEnabled(false);
						}
						
						System.out.println(chatDTO.getReadyCount());
						chatPrint.append(chatDTO.getMessage()+"\n");
	
						int chatPos = chatPrint.getText().length();
						chatPrint.setCaretPosition(chatPos);
					}
					
				}else if(chatDTO.getCommand()==Info.START) {//���߰��� ����
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						readyB.setEnabled(false);
						answer = chatDTO.getMessage();
						startCount = chatDTO.getStartCount();
						System.out.println(startCount);
						if(chatDTO.getStartCount() == 5) {
							startB.setEnabled(false);
							readyB.setEnabled(true);
						}
					}
				}else if(chatDTO.getCommand() == Info.ANSWER) {
					if(waitingroomrcreateDTO.getRoomName().equals(roomName)){
						chatPrint.append(chatDTO.getMessage()+"\n");
						
							if(chatDTO.getNickName().equals(chatNickName)) {
								score +=20;
								System.out.println("�����ö����ϴ�");
							}	
					}
						if(startCount == 5) {
							startB.setEnabled(false);
							readyB.setEnabled(true);
						}
				}
				
				//--------------���� ���� ���  ------------
				if(waitingroomuserDTO.getCommand()==Info.SEND) {
					chatNickName =waitingroomuserDTO.getName();
					point = waitingroomuserDTO.getScore();
				}
				
				
				//--------------------------�游��� ------------------------
				if(waitingroomrcreateDTO.getCommand()==Info.CREATE) {
					
				}else if(waitingroomrcreateDTO.getCommand()==Info.WAIT) {
					//System.out.println("Info.WAIT������ "+waitingroomrcreateDTO.getRoomName());
					//if(waitingroomrcreateDTO.getRoomName().equals(roomName))chatModel.addElement(gameuserDTO);
					//���̸��� �Ȱ��� �濡�� �߰�

				}
				
				
	
				//-----------------gameuserDTO-------------------------------
				if(gameuserDTO.getCommand()==Info.JOIN) {
					//System.out.println(gameuserDTO.getOwner());
					//System.out.println("waitingroomrcreateDTO  : "+waitingroomrcreateDTO.getRoomName());
					//System.out.println("�����roomName  :"+roomName);
					if(waitingroomrcreateDTO.getRoomName().equals(roomName))chatModel.addElement(gameuserDTO);

					if(owner.equals("����")){
						can.setEnabled(true);
						bar.setVisible(true);
					}

//					
//					if(RoomData == null) {
//						chatModel.addElement(gameuserDTO);
//					}else {
//						for(int i =0;i<RoomData.size();i++) {
//							if(RoomData.get(i).getRoomName().equals(roomName))chatModel.addElement(gameuserDTO);
//						}
//					}
//					
					
				}else if(gameuserDTO.getCommand()==Info.WAIT) {
				}
				

				
				
				
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
		//for(ChatDTO dto : chatList) chatModel.addElement(dto);//list�� �ִ°� ���ʴ�� ���	
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
	public JButton getClearB() {
		return clearB;
	}

	public void setClearB(JButton clearB) {
		this.clearB = clearB;
	}
/*
	public static void main(String[] args) 
	{
		ChatFrame chatframe= new ChatFrame();
		chatframe.service();
	}
*/
	
	public void service(waitingRoomRCreateDTO waitingroomrcreateDTO, waitingRoomUserDTO waitingroomuserDTO_send) {
	//public void service() {
		
		String chatServerIp="192.168.51.79";
		try {
		chatSocket =new Socket(chatServerIp, 9700);
		oos = new ObjectOutputStream(chatSocket.getOutputStream());
		ois = new ObjectInputStream(chatSocket.getInputStream());
		
		//������ �޾ƿ��� 
		roomSeq = waitingroomrcreateDTO.getRoomSeq();
		roomName = waitingroomrcreateDTO.getRoomName();	// ���뿡�� ������ ȹ��
		System.out.println("chat ���� ������roomName"+roomName);
		
		roomPass = waitingroomrcreateDTO.getRoomPass();
		owner = waitingroomrcreateDTO.getOwner();
		person = waitingroomrcreateDTO.getPerson();
		roomNumber = waitingroomrcreateDTO.getRoomNumber();
		currentPerson = waitingroomrcreateDTO.getCurrentPerson();
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingroomchattingDTO.setCommand(Info.WAIT);
		waitingRoomUserDTO waitingroomuserDTO_1 = new waitingRoomUserDTO();
		waitingroomuserDTO_1.setCommand(Info.WAIT);
		
		waitingRoomRCreateDTO waitingroomrcreateDTO_1 = new waitingRoomRCreateDTO();
		waitingroomrcreateDTO_1.setCommand(Info.JOIN);
		waitingroomrcreateDTO_1.setRoomName(waitingroomrcreateDTO.getRoomName());
		waitingroomrcreateDTO_1.setRoomPass(roomPass);
		waitingroomrcreateDTO_1.setOwner(waitingroomrcreateDTO.getOwner());
		waitingroomrcreateDTO_1.setPerson(person);
		waitingroomrcreateDTO_1.setRoomNumber(roomNumber);
		
		ChatDTO chatDTO = new ChatDTO();
		chatDTO.setCommand(Info.JOIN);
		//chatDTO.setCommand(Info.WAIT);
		GameUserDTO gameuserDTO = new GameUserDTO();
		//gameuserDTO.setCommand(Info.WAIT);
		gameuserDTO.setCommand(Info.JOIN);
		gameuserDTO.setName(waitingroomuserDTO_send.getName());
		gameuserDTO.setPoint(waitingroomuserDTO_send.getScore());
		chatNickName = waitingroomuserDTO_send.getName();
		
		oos.writeObject(waitingroomchattingDTO);
		oos.writeObject(waitingroomuserDTO_1);
		oos.writeObject(waitingroomrcreateDTO_1);
		oos.writeObject(chatDTO);
		oos.writeObject(cloneList);
		oos.writeObject(gameuserDTO);
		oos.flush();

		
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		
		
		Thread chatThread =new Thread(this);
		chatThread.start();
	}
}

