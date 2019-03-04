package miniproject.catchmind;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import miniproject.membership.dao.MembershipDAO;
import miniproject.roomdao.RoomDAO;


public class WaitingRoomClient extends JFrame implements ActionListener,MouseListener,ListSelectionListener,Runnable{
	private JLabel roomListL,userL,chattingL, idL, pointL;
	private JButton roomB,chattingB , myB, myIB;
	private static JTextArea chattingA;
	private JTextField chattingF, idF, pointF;
	DefaultListModel<waitingRoomRCreateDTO> roomModel;
	JList<waitingRoomRCreateDTO> roomList;
	private DefaultListModel<waitingRoomUserDTO> userModel;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	//ChatFrame�� �ѱ� user DTO ������ 
	private String nickName;
	private int score;
	
	private ImageIcon iconImage;//�ڡڿ��⺯��
	private String iconImg = "cong.png";
	private int state=1;//�����߰�
	private ImageIcon backImage;//
	private JScrollPane roomScrollPane;//
	private ImageIcon backCreateImage;//
	private JScrollPane createScrollPane;//
	
//-----------------�游��� â ---------------------------asdasdasdsadasd
	private JLabel roomNameL,passwordL,personL;
	private JTextField roomNameF,passwordF;
	private JButton roomCreateB,roomCancleB;
	private JComboBox<String> personCB;
//�游�鶧 �ʿ��� ���� ������ ���� ArrayList
	private ArrayList<waitingRoomUserDTO> UserData;
	private ArrayList<waitingRoomRCreateDTO> PersonCheck;

	
	
//-------------ĵ���� ������ �ʿ��� ---------------------------
	private ArrayList<catchmind_ShapDTO> sendList;
	private ChatFrame chatframe;
	
	
	public WaitingRoomClient() {
		setLayout(null);
		//ĵ���� ������ ����Ʈ
		sendList = new ArrayList<catchmind_ShapDTO>();
		//���������� ������ ����Ʈ
		UserData = new ArrayList<waitingRoomUserDTO>();
		//�游���� �ο��� üũ
		PersonCheck = new ArrayList<waitingRoomRCreateDTO>();
		//�� ���
		roomListL = new JLabel("�� ���");
		roomModel = new DefaultListModel<waitingRoomRCreateDTO>();
		roomList = new JList<waitingRoomRCreateDTO>(roomModel);
		
		roomB = new JButton("�� �����");

		//����� ���
		userL = new JLabel("����� ��� ");
		userModel = new DefaultListModel<waitingRoomUserDTO>();
		JList<waitingRoomUserDTO> userListL = new JList<waitingRoomUserDTO>(userModel);
	
		//��ȭâ
		chattingL = new JLabel("��ȭâ");
		chattingA = new JTextArea();
		chattingA.setEditable(false);
		JScrollPane scrollC = new JScrollPane(chattingA);
		scrollC.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chattingF = new JTextField(20);
		chattingB = new JButton("������");

		//�� ����
		idL = new JLabel("ID");
		idF = new JTextField(20);
		idF.setEditable(false);
		pointL = new JLabel("��  ��");
		pointF = new JTextField(20);
		pointF.setEditable(false);
		myB = new JButton("�� ����");
		myIB = new JButton(new ImageIcon("cong.png")); // ���߿� �������� ��ü 
		myIB.setEnabled(true);
		
		//setLatout ��ǥ
		int leftW = 50;//���� �𼭸� ��
		int rightW = 420;//���� ���� ������ �𼭸� �� 

		roomListL.setBounds(leftW,20,50 ,20);
		roomList.setBounds(leftW, 40,350 ,230);
		roomB.setBounds(80+leftW,280,200,30);
		
		//ä��â ��ǥ
		chattingL.setBounds(leftW,340,50,20);
		scrollC.setBounds(leftW,370,350,200);
		chattingF.setBounds(leftW,570,260,30);
		chattingB.setBounds(leftW+260,570,90,30);
		
		//����ڸ�� ��ǥ
		userL.setBounds(rightW,20,80,20);
		userListL.setBounds(rightW,40,200,300);
		
		//������
		idL.setBounds(rightW+100,390,100,20);
		idF.setBounds(rightW+100,410,100,20);
		pointL.setBounds(rightW+100,450,100,20);
		pointF.setBounds(rightW+100,470,100,20);
		myB.setBounds(rightW,570,200,30);
		myIB.setBounds(rightW+10,380,80,150);// ���߿� �������� ��ü 
		
		Container con = this.getContentPane();
		//��
		con.add(roomListL); con.add(roomList);
		con.add(roomB);
		
		//�����
		con.add(userL); con.add(userListL);
		
		//ä�ù�
		con.add(chattingL); con.add(scrollC);
		con.add(chattingF); con.add(chattingB);
		
		//������
		con.add(idL); 	 con.add(idF);
		con.add(pointL); con.add(pointF);
		con.add(myB); 	 con.add(myIB);
		
		
		
		//
		backImage = new ImageIcon("wait.jpg");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(backImage.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }     
        };
        background.setLayout(null);
        
        background.add(roomListL);
        background.add(roomList);
        background.add(roomB);
        background.add(userL);
        background.add(userListL);
        background.add(chattingL);
        background.add(scrollC);
        background.add(chattingF);
        background.add(chattingB);
        background.add(idL);
        background.add(idF);
        background.add(pointL);
        background.add(pointF);
        background.add(myB);
        background.add(myIB);
        
        roomScrollPane = new JScrollPane(background);
        setContentPane(roomScrollPane);
		//
		

		setTitle("CatchMind - Robby");
		setResizable(false);
		setBounds(750,300,700,700);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		myIB.addActionListener(new ActionListener()
		 {   
			 @Override
			 public void actionPerformed(ActionEvent e)
			 {
 // TODO Auto-generated method stub
				 if(e.getSource() == myIB)
				 {
  //���¿� ���� �˸°� ��ư�� �׸� �־���
					 if(state == 0)
					 {
						 iconImage = new ImageIcon("cong.png");
						 iconImg="cong.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 1)
					 {
						 iconImage = new ImageIcon("ag.png");
						 iconImg="ag.png";
						 myIB.setIcon(iconImage);      
					 }
					 else if(state == 2)
					 {
						 iconImage = new ImageIcon("cona.png");
						 iconImg="cona.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 3)
					 {
						 iconImage = new ImageIcon("dora.png");
						 iconImg="dora.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 4)
					 {
						 iconImage = new ImageIcon("bo.png");
						 iconImg="bo.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state == 5)
					 {
						 iconImage = new ImageIcon("je.png");
						 iconImg="je.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state ==6)
					 {
						 iconImage = new ImageIcon("ru.png");
						 iconImg="ru.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state ==7)
					 {
						 iconImage = new ImageIcon("jang.png");
						 iconImg="jang.png";
						 myIB.setIcon(iconImage);
					 }
					 else if(state ==8)
					 {
						 iconImage = new ImageIcon("pica.png");
						 iconImg="pica.png";
						 myIB.setIcon(iconImage);
					 }
						 
  
					 state++;
					 if(state > 8) state = 0;
				 }//if�� ����
 
			 }
		 });
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//oos = null;
				//ois = null;
				if(oos == null || ois == null ) ;
				try{
					WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
					waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
					waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
					ChatDTO chatDTO = new ChatDTO();
					GameUserDTO gameuserDTO = new GameUserDTO();
					
					waitingroomchattingDTO.setCommand(Info.EXIT);
					
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO);
					oos.writeObject(sendList);
					oos.writeObject(gameuserDTO);
					oos.flush();
					
				}catch(IOException io){
					io.printStackTrace();
				}
				
				
			}
		});
	}
	
	public void event() {
		
		roomB.addActionListener(this);
		chattingB.addActionListener(this);
		chattingF.addActionListener(this);
		myB.addActionListener(this);
		roomList.addListSelectionListener(this);
		roomList.addMouseListener(this);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == roomB) roomCreate();
		
		else if (e.getSource() == chattingB || 
				e.getSource()==chattingF) roomChatting();
			
		else if (e.getSource() == myB) myInfo();
	}
	
	//----------------�游��� �޼ҵ� ---------------------
	private void roomCreate() {
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
		waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
		ChatDTO chatDTO=new ChatDTO();
		GameUserDTO gameuserDTO = new GameUserDTO();
		
	
		JFrame roomFrame = new JFrame();
		roomFrame.setLayout(null);
		String[] number= {"2","3","4"};
		
		roomNameL = new JLabel("�� ����");
		roomNameF = new JTextField(30);
		
		passwordL = new JLabel("��й�ȣ");
		passwordF = new JTextField(10);
		
		personL = new JLabel("�����ο�");
		personCB = new JComboBox<String>(number);
		personCB.setSelectedItem(0);
		RoomDAO roomDAO = RoomDAO.getInstance();
		
		roomCreateB = new JButton(" ����� ");
		roomCancleB = new JButton(" ��  �� ");
		
		roomNameL.setBounds(50,30,70,30);
		roomNameF.setBounds(110,30,150,30);
		passwordL.setBounds(50,90,70,30);
		passwordF.setBounds(110,90,70,30);
		personL.setBounds(50,150,70,30);
		personCB.setBounds(110,150,50,30);
		roomCreateB.setBounds(30,210 , 100, 40);
		roomCancleB.setBounds(140,210 , 100, 40);
		
		roomFrame.add(roomNameL); roomFrame.add(roomNameF);
		roomFrame.add(passwordL); roomFrame.add(passwordF);
		roomFrame.add(personL);   roomFrame.add(personCB);
		roomFrame.add(roomCreateB);roomFrame.add(roomCancleB);
		//
		Container back =roomFrame.getContentPane();
		back.setBackground(new Color(230,255,255));
		//
		roomFrame.setTitle("�游���");
		roomFrame.setBounds(750,300,300,300);
		roomFrame.setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void	windowClosing(WindowEvent e){
				roomFrame.dispose();
			}
		});
		//enter �Է½� �۵�
		roomNameF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				waitingroomchattingDTO.setCommand(Info.WAIT);
				waitingroomuserDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setRoomName(roomNameF.getText());
				waitingroomrcreateDTO.setRoomPass(passwordF.getText());
				waitingroomrcreateDTO.setPerson(personCB.getSelectedIndex());
				waitingroomrcreateDTO.setCommand(Info.CREATE);
				waitingroomrcreateDTO.setOwner("����");
				
				
				if(roomNameF.getText().trim()!=null) {
					boolean roomTitleCheck=false;//Default: ���Ұ�
					roomTitleCheck = roomDAO.isRoomExist(roomNameF.getText().trim());
					if(!roomTitleCheck) {
						JOptionPane.showMessageDialog(WaitingRoomClient.this, "�̹� ������� �� �̸��Դϴ�");
						return;
					}
					
					if(waitingroomrcreateDTO.getPerson()== 0) {	
						waitingroomrcreateDTO.setPerson(2);
						waitingroomrcreateDTO.setLimitPerson(2);
					}else if(waitingroomrcreateDTO.getPerson() == 1) {	
						waitingroomrcreateDTO.setPerson(3);
						waitingroomrcreateDTO.setLimitPerson(3);						
					}else if(waitingroomrcreateDTO.getPerson() == 2) {	
						waitingroomrcreateDTO.setPerson(4);
						waitingroomrcreateDTO.setLimitPerson(4);
					}
				} else {
					JOptionPane.showMessageDialog(WaitingRoomClient.this, "�� �̸��� �Է��ϼ���");
					return;					
				}				
				int roomSeq = roomDAO.getSeq();
				waitingroomrcreateDTO.setRoomSeq(roomSeq);
				
				roomDAO.insertArticle(waitingroomrcreateDTO);
				
				roomDAO.plusPerson(waitingroomrcreateDTO);
				int currentPerson = roomDAO.getCurrentPerson(waitingroomrcreateDTO);
				waitingroomrcreateDTO.setCurrentPerson(currentPerson);
				
				
				try {
					//��� ������
					waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
					for(int i = 0; i<UserData.size();i++) {
						if(nickName.equals(UserData.get(i).getName())){
							waitingroomuserDTO_send.setName(UserData.get(i).getName());
							waitingroomuserDTO_send.setScore(UserData.get(i).getScore());
						}
					}
									
					ChatDTO chatDTO = new ChatDTO();
					chatDTO.setCommand(Info.WAIT);
					GameUserDTO gameuserDTO = new GameUserDTO();
					gameuserDTO.setCommand(Info.WAIT);
					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO);
					oos.writeObject(sendList);
					oos.writeObject(gameuserDTO);
					oos.flush();
				
					chatframe = new ChatFrame(WaitingRoomClient.this);//�游��� ��ư
					chatframe.service(waitingroomrcreateDTO, waitingroomuserDTO_send);						
				} catch (IOException ie) {
					ie.printStackTrace();
				}
				setVisible(false);
				roomFrame.dispose();	
			}
		});
		roomCreateB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				waitingroomchattingDTO.setCommand(Info.WAIT);
				waitingroomuserDTO.setCommand(Info.WAIT);
				waitingroomrcreateDTO.setRoomName(roomNameF.getText());
				waitingroomrcreateDTO.setRoomPass(passwordF.getText());
				waitingroomrcreateDTO.setPerson(personCB.getSelectedIndex());
				waitingroomrcreateDTO.setCommand(Info.CREATE);
				waitingroomrcreateDTO.setOwner("����");
				
				if(roomNameF.getText().trim()!=null) {
					boolean roomTitleCheck=false;//Default: ���Ұ�
					roomTitleCheck = roomDAO.isRoomExist(roomNameF.getText().trim());
					if(!roomTitleCheck) {
						JOptionPane.showMessageDialog(WaitingRoomClient.this, "�̹� ������� �� �̸��Դϴ�");
						return;
					}
					
					if(waitingroomrcreateDTO.getPerson()== 0) {	
						waitingroomrcreateDTO.setPerson(2);
						waitingroomrcreateDTO.setLimitPerson(2);
					}else if(waitingroomrcreateDTO.getPerson() == 1) {	
						waitingroomrcreateDTO.setPerson(3);
						waitingroomrcreateDTO.setLimitPerson(3);						
					}else if(waitingroomrcreateDTO.getPerson() == 2) {	
						waitingroomrcreateDTO.setPerson(4);
						waitingroomrcreateDTO.setLimitPerson(4);
					}
				} else {
					JOptionPane.showMessageDialog(WaitingRoomClient.this, "�� �̸��� �Է��ϼ���");
					return;					
				}				
				int roomSeq = roomDAO.getSeq();
				waitingroomrcreateDTO.setRoomSeq(roomSeq);
				
				roomDAO.insertArticle(waitingroomrcreateDTO);
				
				roomDAO.plusPerson(waitingroomrcreateDTO);
				int currentPerson = roomDAO.getCurrentPerson(waitingroomrcreateDTO);
				waitingroomrcreateDTO.setCurrentPerson(currentPerson);
				try {
					//��� ������
					waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
					for(int i = 0; i<UserData.size();i++) {
						if(nickName.equals(UserData.get(i).getName())){
							waitingroomuserDTO_send.setName(UserData.get(i).getName());
							waitingroomuserDTO_send.setScore(UserData.get(i).getScore());
						}
					}
									
					ChatDTO chatDTO = new ChatDTO();
					chatDTO.setCommand(Info.WAIT);
					GameUserDTO gameuserDTO = new GameUserDTO();
					gameuserDTO.setCommand(Info.WAIT);

					
					oos.writeObject(waitingroomchattingDTO);
					oos.writeObject(waitingroomuserDTO);
					oos.writeObject(waitingroomrcreateDTO);
					oos.writeObject(chatDTO);
					oos.writeObject(sendList);
					oos.writeObject(gameuserDTO);
					oos.flush();										
						
					chatframe = new ChatFrame(WaitingRoomClient.this);//�游��� ��ư
					chatframe.service(waitingroomrcreateDTO, waitingroomuserDTO_send);
					
				} catch (IOException ie) {
					ie.printStackTrace();
				}
					
					

				setVisible(false);
				roomFrame.dispose();
				
			}
		});
		roomCancleB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomFrame.dispose();
			}
		});

	}
	
	
	
	//------------------ä�� �޼ҵ� ------------------------------
	private void roomChatting() {				// ä�� �޼��� ���� 
		
		String message = chattingF.getText();
		
		WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
		waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
		waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
		ChatDTO chatDTO = new ChatDTO();
		GameUserDTO gameuserDTO = new GameUserDTO();
		
		if(message.toLowerCase().equals("exit")) {
			waitingroomchattingDTO.setCommand(Info.EXIT);
			waitingroomuserDTO.setCommand(Info.EXIT);
			waitingroomrcreateDTO.setCommand(Info.EXIT);
			chatDTO.setCommand(Info.EXIT);
			gameuserDTO.setCommand(Info.EXIT);
			
		}else {
			waitingroomchattingDTO.setCommand(Info.SEND);
			waitingroomchattingDTO.setMessage(message);
			waitingroomuserDTO.setCommand(Info.WAIT);
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			chatDTO.setCommand(Info.WAIT);
			gameuserDTO.setCommand(Info.WAIT);
			
		}
		
		try {
			oos.writeObject(waitingroomchattingDTO);
			oos.writeObject(waitingroomuserDTO);
			oos.writeObject(waitingroomrcreateDTO);
			oos.writeObject(chatDTO);
			oos.writeObject(sendList);
			oos.writeObject(gameuserDTO);
			oos.flush();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		chattingF.setText("");
		 
	}


	@Override
	public void run() {	//������ ���� �޴���
		
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		waitingRoomUserDTO waitingroomuserDTO = null;
		waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		ChatDTO chatHandlerDTO = null;
		GameUserDTO gameuserDTO = null;
		
		while(true) {
			
			try {
				waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
				waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
				waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
				chatHandlerDTO = (ChatDTO)ois.readObject();
				sendList = (ArrayList<catchmind_ShapDTO>) ois.readObject();
				gameuserDTO = (GameUserDTO)ois.readObject();
		
				if(waitingroomchattingDTO.getCommand()==Info.EXIT){
					
					
					
					//userModel.removeElement(waitingroomuserDTO);
					//roomModel.removeElement(waitingroomrcreateDTO);
					
					oos.close();
					ois.close();
					socket.close();
					//chatframe.
					System.exit(0);
					while(true) {
						if(socket.isClosed()) {
							System.out.println("����!");					
						}
					}
	
					
				}else if(waitingroomchattingDTO.getCommand() == Info.SEND) {
					chattingA.append(waitingroomchattingDTO.getMessage()+"\n");
					int pos = chattingA.getText().length();// ��ũ�ѹٰ� �������� ���󰡰Բ� ���ִ¿�Ȱ
					chattingA.setCaretPosition(pos);
				}

				
				if(waitingroomuserDTO.getCommand() == Info.JOIN) {
					UserData.add(waitingroomuserDTO); // ���� ������ �Է�
					userModel.addElement(waitingroomuserDTO);
					
				}else if (waitingroomuserDTO.getCommand() == Info.WAIT) {
				
				}
				
				if(waitingroomrcreateDTO.getCommand() == Info.CREATE) {

					if(waitingroomrcreateDTO.getOwner().equals("����"))
						roomModel.addElement(waitingroomrcreateDTO);//������ ����ø� ���Ͽ� �߰�
					
					PersonCheck.add(waitingroomrcreateDTO);// �� �ο��� üũ
					
					
				}
		
				if(gameuserDTO.getCommand() == Info.EXIT) {
					
					System.out.println("gameuser = "+gameuserDTO.getName());
					String user = gameuserDTO.getName();
					MembershipDAO membershipDAO = MembershipDAO.getInstance();
					//String user = membershipDAO.getName(userID)
					
					int newScore = membershipDAO.getScore(user);
					//int newScore = gameuserDTO.getPoint();
					idF.setText(user);
					pointF.setText(newScore+"");
					
					//if(nickName.equals(gameuserDTO.getName()))setVisible(true);// WaitingRoomClient 464�� �߰� 
				
				}
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void service(ChatDTO chatDTO) {
	}
	
	
	
	
	public void service(IdNameScoreDTO idnamescoreDTO) {
	//public void service( ) {	
		nickName = idnamescoreDTO.getId();
		//id = getName();
		//score = idnamescoreDTO.getScore();
		//nickName = JOptionPane.showInputDialog("����Ͻ� �г����� �Է��ϼ���");
		//String serverIP = JOptionPane.showInputDialog("����IP�� �Է��ϼ���","192.168.51.71");
		String serverIP ="192.168.51.79";
																		
		if(serverIP==null || serverIP.length()==0){
			System.out.println("���� IP�� �Էµ��� �ʾҽ��ϴ�");
			System.exit(0);
		}
		
		try {
			
			socket = new Socket(serverIP, 9500);			//���� �Է� : ���� ���Է� 
			oos = new ObjectOutputStream(socket.getOutputStream()); 
			ois = new ObjectInputStream(socket.getInputStream());
			
			//ä�� DTO
			WaitingRoomChattingDTO waitingroomchattingDTO = new WaitingRoomChattingDTO();
			waitingroomchattingDTO.setCommand(Info.JOIN);
			waitingroomchattingDTO.setNickName(nickName);	// �α��ν� �г��� �޾ƿ��� : ���� ���Է�
			
			//���� DTO
			waitingRoomUserDTO waitingroomuserDTO = new waitingRoomUserDTO();
			waitingroomuserDTO.setCommand(Info.JOIN);
			waitingroomuserDTO.setName(nickName);
			
			//waitingroomuserDTO.setId();
			//waitingroomuserDTO.setScore();
			
			//�游��� DTO
			waitingRoomRCreateDTO waitingroomrcreateDTO = new waitingRoomRCreateDTO();
			waitingroomrcreateDTO.setCommand(Info.WAIT);
			
			//���� DTO ����
			ChatDTO chatDTO=new ChatDTO();
			chatDTO.setCommand(Info.WAIT);
			
			//���� ���� ����Ʈ DTO
			GameUserDTO gameuserDTO = new GameUserDTO();
			gameuserDTO.setCommand(Info.WAIT);
			
			idF.setText(idnamescoreDTO.getId());
			pointF.setText(idnamescoreDTO.getScore()+"");
			
			//5�� ������ ���� 
			oos.writeObject(waitingroomchattingDTO);
			oos.writeObject(waitingroomuserDTO);
			oos.writeObject(waitingroomrcreateDTO);
			oos.writeObject(chatDTO);
			oos.writeObject(sendList);
			oos.writeObject(gameuserDTO);
			oos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { 		//���콺�� ����� 
		if(arg0.getComponent() == roomList) {
			int answer = 0;
			
			if(arg0.getClickCount()==2) {
				
				if(roomList.getSelectedIndex()>= 0) {
					answer = JOptionPane.showConfirmDialog( this, "�濡 ���� �Ͻðڽ��ϱ�?","�游���",JOptionPane.YES_NO_CANCEL_OPTION); //����ɲ� , �޼��� 
					
					waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
					waitingroomrcreateDTO_send.setCommand(Info.CREATE);
					waitingroomrcreateDTO_send = roomList.getSelectedValue();
	
					if(answer==JOptionPane.YES_OPTION){						
						if( PersonCheck.get(waitingroomrcreateDTO_send.getRoomNumber()-1).getRoomName().equals(waitingroomrcreateDTO_send.getRoomName()) &&
								waitingroomrcreateDTO_send.getPerson() > PersonCheck.size() ) {//������ üũ , �ο��� üũ

							try {
								int roomListNumber = roomList.getSelectedIndex();
								
								WaitingRoomChattingDTO waitingroomchattingDTO_send= new WaitingRoomChattingDTO();
								waitingroomchattingDTO_send.setCommand(Info.WAIT);
								
								waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
								waitingroomuserDTO_send.setCommand(Info.WAIT);
								for(int i = 0; i<UserData.size();i++) {
									if(nickName.equals(UserData.get(i).getName())){
										waitingroomuserDTO_send.setName(UserData.get(i).getName());
										waitingroomuserDTO_send.setScore(UserData.get(i).getScore());
									}
								}
								
								
								waitingroomrcreateDTO_send.setOwner("����");
								waitingroomrcreateDTO_send.setRoomNumber(roomListNumber); //�� ��ȣ �Է�
								
								//System.out.println(roomList.getSelectedIndex());
								
		
								
								ChatDTO chatDTO = new ChatDTO();
								chatDTO.setCommand(Info.WAIT);
								GameUserDTO gameuserDTO = new GameUserDTO();
								gameuserDTO.setCommand(Info.WAIT);
								
								oos.writeObject(waitingroomchattingDTO_send);
								oos.writeObject(waitingroomuserDTO_send);
								oos.writeObject(waitingroomrcreateDTO_send);
								oos.writeObject(chatDTO);
								oos.writeObject(sendList);
								oos.writeObject(gameuserDTO);
								oos.flush();
								
								
								//oos.close();
								//ois.close();
								//socket.close();
								chatframe = new ChatFrame(); //���콺 Ŭ��
								//chatframe.service();
								chatframe.service( waitingroomrcreateDTO_send, waitingroomuserDTO_send);
								
								setVisible(false);
								
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
			                   JOptionPane.showMessageDialog( this, "������ �ʰ��߽��ϴ�");
			               }
					}//YES_OPTION
					
					
				}else if(answer==JOptionPane.NO_OPTION) {
					
				}

			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	//------------------ ������ ----------------------------------
	public void myInfo() {
		waitingRoomMyInfo waitingroommyinfo = new waitingRoomMyInfo();
		waitingroommyinfo.myinfoC(iconImg, nickName);
	}
	//----------------------------------------------------
	
	
	
//	public static void main(String []args) {
//		WaitingRoomClient waitingroomclient = new WaitingRoomClient();
//		waitingroomclient.event();
//		waitingroomclient.service();
//	}
//	 

}

