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


//JFrame �� ���������� �⺻�г�
class ChatClientObject extends JFrame implements ActionListener,Runnable//implements������ �������̵���ְ�,�̺�Ʈ �ɾ������
{
	private JTextArea output;
	private JTextField input;
	private JButton send;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ChatClientObject(){
		output = new JTextArea();
		output.setEditable(false);//�ؽ�Ʈ �Ʒ���â�� �Է¹���=ī��ó�� ���� ��ȭ�� ���̰� �ϱ�����

		JScrollPane scroll = new JScrollPane(output);//��ũ�ѹ����� textArea�÷���
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//���ν�ũ�ѹ� �׻� ���ֵ���
		this.getContentPane().add("Center",scroll);

		input = new JTextField(17);//17�� �ؽ�Ʈâ�� ũ��
		send = new JButton("������");

		JPanel p = new JPanel();//�⺻���� FlowLayout�� ���,�߾ӹ�ġ��
		p.setLayout(new BorderLayout()); //�г��� ������ �ٲ㼭 ��ư�̶� �ؽ�Ʈâ �̵� 
											//âũ�� ���������� ��ư,�ؽ�Ʈ�ʵ�â�� ũ�� ���� ����
		p.add("Center",input);
		p.add("East",send);

		Container con = getContentPane();
		con.add("Center", scroll);//output���ָ� �Ⱥ���, output�� ��ũ�ѹٿ� �پ��ֱ⶧��
		con.add("South", p);

		setBounds(700, 200, 300, 300);
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//x��ư ������ �ٷβ���
		this.addWindowListener(new WindowAdapter()	{
			@Override
			public void windowClosing(WindowEvent b){
				
				try{
					if(oos==null||ois==null)System.exit(0); //io���� �������� Ȯ���ϰ� ������Ѵ� �ݵ�� �ȱ׷��� ���������ͼ��Ƕ��
					
					InfoDTO dto=new InfoDTO();
					dto.setCommand(Info.EXIT);
					oos.writeObject(dto);//������ exit ������
					oos.flush(); //pw����
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
		

		send.addActionListener(this);//��ư������ �̺�Ʈ�߻�
		input.addActionListener(this);//���� ������ �������ư �������� ��
	}//������ ���� 
	
	//������ �޼����� �������� 
	@Override
	public void actionPerformed(ActionEvent e){//send��ư ������ �������� �Ѿ��,//��ư�� 1�����̴� e.getSource() �̷� ���� �����൵ �ȴ�
		
		String msg=input.getText();//JTextFeild�� ���� ������

		InfoDTO dto =new InfoDTO();
		if(msg.toLowerCase().equals("exit")){//���������� EXIT, exit ,Exit ���� �빮��,�ҹ����� ��Կ��� �𸣴°��̴� 
												//����Է��� �ҹ��ڷ� ����
			dto.setCommand(Info.EXIT);
		}
		else{
			dto.setCommand(Info.SEND);
			dto.setMessage(msg);
		}
		try{
			oos.writeObject(dto);//������ ��ä�־ ������
			oos.flush();
		}catch(IOException io){
			io.printStackTrace();
		}

		input.setText("");//inputâ�� �����
	}
	
	//0.����=���񽺸���û��,�������ϴ¿����� ������ ������ �ٽûѷ��ִ°ͻ�
	//1.ä�����α׷��� ������ ���������־����
	//2.ServerSocket �� Ŭ���̾�Ʈ�� �����⸦ ��ٸ���
	//3.Ŭ���̾�Ʈ�� ������ accept()�� ����ä�� �������ش�
	//4.chatclient�� �������� ����,  ������������ip����Ʈ��ȣ�ʿ�(��ȭ��ȣ) �׸��� ����(�ڵ���)�ʿ�
	//5.������ ��ȭ�� ������ ������ش�//���ϰ� ���ϳ��� ��ȭ�ϵ��� ���� ����
	//6.����ä���� chatclient2�� ���ִ�// �̴ٸ� chatclient2�� ���ϰ� ����,port��ȣ�� ����־���Ѵ�
	//7.chatclient2�� ������ ����, �������� accept()�� ����æ��
	//�̷�����
	//chatclient�� "�ȳ�"�� ������ ������ chatclient,chatclient2 ���(������ �Ŵ޷��ִ� ���Ŭ���̾�Ʈ�� �����ش�,�ڱ��ڽſ��Ե�)���� �����ش�

	//���chatclient�� ������� �������ϰ�, ����Ŭ�������� chatclient�� ����� ������ �˰��־���Ѵ�,
			//������ �ݵ�� 1�����־���Ѵ�, �׷��� �����ʿ� chatHandler�� chatClient�� ������ŭ �־����
			//chatHandler ���θ� ArrayList�� ����ش�
			//chatHandler ���� �������ְ� ���Ͽ��� br,pw�� ����


	public void service() { //���� ������ �ޱ�		
			//String serverIP=JOptionPane.showInputDialog(this,"����IP�� �Է��ϼ���","����IP",JOptionPane.INFORMATION_MESSAGE);//JOptionPane.INFORMATION_MESSAGE �� ����ǥ��� �׸��־��ִ°�
			String serverIP=JOptionPane.showInputDialog(this,"����IP�� �Է��ϼ���","192.168.");//�������� �̷��� �ص���//�׸������°��� ����ǥ���
			if(serverIP==null || serverIP.length() ==0){//������ IP�� �ȵ��ý�
				System.out.println("���� IP�� �Էµ��� �ʾҽ��ϴ�.");
				System.exit(0);
			}
			String nickName = JOptionPane.showInputDialog(this,"�г����� �Է��ϼ���","�г���",JOptionPane.INFORMATION_MESSAGE);
			if(nickName == null || nickName.length()==0){	
				nickName="guest"; 
			}

			try{
				//���� ����
				socket= new Socket(serverIP,9500);//����������,��Ʈ��ȣ��9500

				//��ü�� ���� �������ֵ��� ��½�Ʈ������ ���� ������Ѵ�
				oos =new ObjectOutputStream(socket.getOutputStream());
				ois= new ObjectInputStream(socket.getInputStream());
				
				//������� �ؾ����� �г����� ������ ������
				InfoDTO dto=new InfoDTO();
				dto.setCommand(Info.JOIN);//�����ҰŶ�� ������ ������
				dto.setNickName(nickName);
				oos.writeObject(dto);
				oos.flush();//flush()�� ���� ���ۿ� ����Ǿ� �ִ� ������ Ŭ���̾�Ʈ�� �����ϰ� ���۸� ����.

			}catch(UnknownHostException e){//��ȭ��ȣ�� ������
				System.out.println("������ ã�� �� �����ϴ�");
				e.printStackTrace();
				System.exit(0);
			}catch(IOException e){//�����̲����ų�,�����ȭ���̰ų�
				System.out.println("�������� ������ �ȵǾ����ϴ�");
				e.printStackTrace();
				System.exit(0);
			}

			Thread t= new Thread(this);//ChatClient�� �����尡 �ǰ�����Ŵ� this  //���������
				t.start();//���������

	}//service

	//������ ���� ���� �޼����� �޴���
	@Override
	public void run(){
		
		InfoDTO dto =null;
		while(true){//�޼����� �ѹ� �����°� �ƴϴϱ� while�����
			try{
				dto =(InfoDTO)ois.readObject();//�����ݷ��� ����������////������ ���� ���� �޼����� JTextArea�� �߰�
							//Object�� �ֻ���Ŭ������ dto�� ���־ �ڽ�Ŭ������ ��������ȯ�ʿ�
				if(dto.getCommand()==Info.EXIT){// exit�� ���ý� ���� 
					oos.close();//����Ҵ°��� �� close���� ����� �������� ���� �ȶ��
					ois.close();
					socket.close();

					System.exit(0);
				}else if(dto.getCommand()==Info.SEND){

					output.append(dto.getMessage()+"\n"); //append�� ���Ͱ��� ���о \n�� �߰��������
					
					//�ؿ� �����ϸ� ä�� ��� �ٸ�������ĵ� ��ũ�ѹٰ� ���󰡰Եȴ�
					int pos = output.getText().length();
					output.setCaretPosition(pos);//��ġ�� ��ӿ�û�ϴ� �޼ҵ�
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
