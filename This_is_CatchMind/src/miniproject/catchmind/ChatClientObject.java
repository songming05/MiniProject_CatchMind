package miniproject.catchmind;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;

//		    pw                     |                      br             
//CharClient --> buffer -->socket -|->socket --> buffer -->ChatHandler(ChatServer)
//CharClient <-- buffer <--socket <|--socket <-- buffer <--ChatHandler(ChatServer)
//         br                      |                       pw


//JFrame 은 동서남북이 기본패널
class ChatClientObject extends JFrame implements ActionListener,Runnable//implements했으니 오버라이드써주고,이벤트 걸어줘야함
{
	private JTextArea output;
	private JTextField input;
	private JButton send;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ChatClientObject(){
		output = new JTextArea();
		output.setEditable(false);//텍스트 아레아창에 입력방지=카톡처럼 남의 대화만 보이게 하기위해

		JScrollPane scroll = new JScrollPane(output);//스크롤바위에 textArea올려줌
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//새로스크롤바 항상 떠있도록
		this.getContentPane().add("Center",scroll);

		input = new JTextField(17);//17은 텍스트창의 크기
		send = new JButton("보내기");

		JPanel p = new JPanel();//기본으로 FlowLayout임 가운데,중앙배치함
		p.setLayout(new BorderLayout()); //패널을 보더로 바꿔서 버튼이랑 텍스트창 이동 
											//창크기 조절에따라 버튼,텍스트필드창도 크기 같이 변경
		p.add("Center",input);
		p.add("East",send);

		Container con = getContentPane();
		con.add("Center", scroll);//output써주면 안붙음, output은 스크롤바에 붙어있기때문
		con.add("South", p);

		setBounds(700, 200, 300, 300);
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//x버튼 누르면 바로꺼짐
		this.addWindowListener(new WindowAdapter()	{
			@Override
			public void windowClosing(WindowEvent b){
				
				try{
					if(oos==null||ois==null)System.exit(0); //io쪽이 끊어진걸 확인하고 끊어야한다 반드시 안그러면 널포인터익셉션뜬다
					
					InfoDTO dto=new InfoDTO();
					dto.setCommand(Info.EXIT);
					oos.writeObject(dto);//서버에 exit 보내기
					oos.flush(); //pw비운다
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
		

		send.addActionListener(this);//버튼에대한 이벤트발생
		input.addActionListener(this);//엔터 누르면 보내기버튼 눌러지게 됨
	}//생성자 종료 
	
	//서버로 메세지를 보내는쪽 
	@Override
	public void actionPerformed(ActionEvent e){//send버튼 눌리면 이쪽으로 넘어옴,//버튼이 1개뿐이니 e.getSource() 이런 설정 안해줘도 된다
		
		String msg=input.getText();//JTextFeild의 값을 얻어오기

		InfoDTO dto =new InfoDTO();
		if(msg.toLowerCase().equals("exit")){//빠져나갈때 EXIT, exit ,Exit 같이 대문자,소문자중 어떤게올지 모르는것이니 
												//모든입력을 소문자로 변경
			dto.setCommand(Info.EXIT);
		}
		else{
			dto.setCommand(Info.SEND);
			dto.setMessage(msg);
		}
		try{
			oos.writeObject(dto);//서버로 객채넣어서 보내기
			oos.flush();
		}catch(IOException io){
			io.printStackTrace();
		}

		input.setText("");//input창은 비워줌
	}
	
	//0.서버=서비스를요청함,서버가하는역할은 오로지 받으면 다시뿌려주는것뿐
	//1.채팅프로그램은 서버가 먼저열려있어야함
	//2.ServerSocket 이 클라이언트가 들어오기를 기다린다
	//3.클라이언트가 들어오면 accept()로 낚아채서 연결해준다
	//4.chatclient가 문을열고 들어간다,  서버에접근할ip와포트번호필요(전화번호) 그리고 소켓(핸드폰)필요
	//5.서버와 대화할 소켓을 만들어준다//소켓과 소켓끼리 대화하도록 길을 터줌
	//6.다중채팅은 chatclient2가 더있다// 이다른 chatclient2도 소켓과 서버,port번호를 들고있어야한다
	//7.chatclient2도 서버와 연결, 서버에서 accept()로 낚아챈다
	//이런식임
	//chatclient이 "안녕"을 보내면 서버는 chatclient,chatclient2 모두(서버에 매달려있는 모든클라이언트에 보내준다,자기자신에게도)에게 보내준다

	//모든chatclient와 쓰레드로 잡아줘야하고, 서버클래스에서 chatclient가 몇개인지 개수를 알고있어야한다,
			//서버는 반드시 1개만있어야한다, 그래서 서버쪽에 chatHandler가 chatClient의 개수만큼 있어야함
			//chatHandler 전부를 ArrayList에 담아준다
			//chatHandler 에는 소켓이있고 소켓에는 br,pw가 존재


	public void service() { //서버 아이피 받기		
			//String serverIP=JOptionPane.showInputDialog(this,"서버IP를 입력하세요","서버IP",JOptionPane.INFORMATION_MESSAGE);//JOptionPane.INFORMATION_MESSAGE 은 느낌표모양 그림넣어주는거
			String serverIP=JOptionPane.showInputDialog(this,"서버IP를 입력하세요","192.168.");//위에말고 이렇게 해도됨//그림나오는곳에 물음표뜬다
			if(serverIP==null || serverIP.length() ==0){//서버에 IP가 안들어올시
				System.out.println("서버 IP가 입력되지 않았습니다.");
				System.exit(0);
			}
			String nickName = JOptionPane.showInputDialog(this,"닉네임을 입력하세요","닉네임",JOptionPane.INFORMATION_MESSAGE);
			if(nickName == null || nickName.length()==0){	
				nickName="guest"; 
			}

			try{
				//소켓 생성
				socket= new Socket(serverIP,9500);//서버아이피,포트번호는9500

				//객체가 먼저 나갈수있도록 출력스트림을을 먼저 써줘야한다
				oos =new ObjectOutputStream(socket.getOutputStream());
				ois= new ObjectInputStream(socket.getInputStream());
				
				//가장먼저 해야할일 닉네임을 서버로 보내기
				InfoDTO dto=new InfoDTO();
				dto.setCommand(Info.JOIN);//가입할거라는 정보를 보내줌
				dto.setNickName(nickName);
				oos.writeObject(dto);
				oos.flush();//flush()는 현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다.

			}catch(UnknownHostException e){//전화번호가 없을때
				System.out.println("서버를 찾을 수 없습니다");
				e.printStackTrace();
				System.exit(0);
			}catch(IOException e){//전원이꺼젓거나,계속통화중이거나
				System.out.println("서버와의 연결이 안되었습니다");
				e.printStackTrace();
				System.exit(0);
			}

			Thread t= new Thread(this);//ChatClient가 쓰레드가 되고싶은거니 this  //스레드생성
				t.start();//스레드시작

	}//service

	//서버로 부터 오는 메세지를 받는쪽
	@Override
	public void run(){
		
		InfoDTO dto =null;
		while(true){//메세지가 한번 만오는게 아니니까 while문사용
			try{
				dto =(InfoDTO)ois.readObject();//세미콜론이 붙을때까지////서버로 부터 받은 메세지를 JTextArea에 추가
							//Object는 최상위클래스라 dto에 못넣어서 자식클래스로 강제형변환필요
				if(dto.getCommand()==Info.EXIT){// exit가 들어올시 종료 
					oos.close();//출력할는것을 꼭 close먼저 써줘야 널포인터 에러 안뜬다
					ois.close();
					socket.close();

					System.exit(0);
				}else if(dto.getCommand()==Info.SEND){

					output.append(dto.getMessage()+"\n"); //append는 엔터값을 안읽어서 \n을 추가해줘야함
					
					//밑에 두줄하면 채팅 계속 다른사람이쳐도 스크롤바가 따라가게된다
					int pos = output.getText().length();
					output.setCaretPosition(pos);//위치값 계속요청하는 메소드
				}

			}catch(IOException io){
				io.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}//while
	
	}

	public static void main(String[] args) {
		new ChatClientObject().service();
	}
}
