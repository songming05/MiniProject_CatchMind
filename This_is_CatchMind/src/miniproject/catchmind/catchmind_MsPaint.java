package miniproject.catchmind;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Panel;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class catchmind_MsPaint extends JFrame implements ActionListener, Runnable{
	private Socket socket; //소켓
	private ObjectOutputStream oos; //보내기
	private ObjectInputStream ois; //받기
	private int x1, y1, x2, y2; //좌표
	private JButton[] btn; //색깔 버튼
	private JRadioButton thinB, thickB; //선 굵기
	private catchmind_Canvas can; //캔버스
	private ArrayList<catchmind_ShapDTO> sendlist; //서버로 보내는 list
	private ArrayList<catchmind_ShapDTO> serverInfoList; //서버에서 받아온 list
	private ArrayList<catchmind_ShapDTO> serverDrawList; //서버에서 받아온걸 그리는 list
	private int colorNum; //색깔 기준
	
	public catchmind_MsPaint() {		
		String serverIP = "192.168.51.134";
		
		try {
			socket = new Socket(serverIP, 9500);			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		btn = new JButton[6];
		String[] title = {"검정", "빨강", "초록", "파랑", "노랑", "분홍"};
		Color[] color = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
		for(int i=0; i<title.length; i++) {
			btn[i] = new JButton(title[i]);
			btn[i].setBackground(color[i]);
		}
		
		thinB = new JRadioButton("얇은", true);
		thickB = new JRadioButton("굵은");
		
		ButtonGroup group = new ButtonGroup();
		group.add(thickB); group.add(thinB);
		
		Panel p = new Panel();
		p.add(btn[0]); p.add(btn[1]);
		p.add(btn[2]); p.add(btn[3]);
		p.add(btn[4]); p.add(btn[5]);
		p.add(thinB); p.add(thickB);
		
		//캔버스
		can = new catchmind_Canvas(this);
				
		//리스트
		sendlist = new ArrayList<catchmind_ShapDTO>();
		//serverInfoList = new ArrayList<catchmind_ShapDTO>();
		//serverDrawList = new ArrayList<catchmind_ShapDTO>();
		
		Container con = this.getContentPane();
		con.add("Center", can);
		con.add("South", p);
		
		setBounds(500,200,700,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		for(int i=0; i<title.length; i++) {
			btn[i].addActionListener(this);
		}
		Thread t = new Thread(this);
		t.start();
		
		
		
		
		can.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				x1 =e.getX(); 	y1 =e.getY();
			}
			@Override
			public void mouseReleased(MouseEvent e){				
				x2 = e.getX();  y2 = e.getY();
				
				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1); 	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);
				dto.setColorNum(colorNum);
				
				if(thinB.isSelected()) dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);
				
				sendlist.add(dto);
				ArrayList<catchmind_ShapDTO> cloneList = (ArrayList<catchmind_ShapDTO>) sendlist.clone();
				
				try {
					oos.writeObject(cloneList);
					System.out.println("서버로 보낸 send 리스트 수는 = "+sendlist.size());
					System.out.println("서버로 보낸 clone 리스트 수는 = "+cloneList.size());
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		can.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {			
				x2 = e.getX();	y2 = e.getY();
				
				//연필
				catchmind_ShapDTO dto = new catchmind_ShapDTO();
				dto.setX1(x1);	dto.setY1(y1);	dto.setX2(x2);	dto.setY2(y2);
				
				dto.setColorNum(colorNum);
				
				if(thinB.isSelected()) 	dto.setShape(Shape.LINE);
				else if(thickB.isSelected()) dto.setShape(Shape.RECT);
				
				sendlist.add(dto);
				ArrayList<catchmind_ShapDTO> cloneList = (ArrayList<catchmind_ShapDTO>) sendlist.clone();
				
				try {
					oos.writeObject(cloneList);
					//System.out.println("서버로 보낸 send 리스트 수는 = "+sendlist.size());
					//System.out.println("서버로 보낸 clone 리스트 수는 = "+cloneList.size());
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				x1 = x2;
				y1 = y2;
			}
		});
	}//기본생성자
	
	@Override
	public void run() {
		//핸들러가 보내주는 데이터 수신
		while(true) {
			try {
				serverInfoList = (ArrayList<catchmind_ShapDTO>)ois.readObject();
				can.repaint();
				//System.out.println("Handler가 보내줌: "+serverInfoList.size());
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		catchmind_ShapDTO dto = new catchmind_ShapDTO();
		if(e.getSource() == btn[0]) {
			colorNum = 0;
		}else if(e.getSource() == btn[1]) {
			colorNum = 1;
		}else if(e.getSource() == btn[2]) {
			colorNum = 2;
		}else if(e.getSource() == btn[3]) {
			colorNum = 3;
		}else if(e.getSource() == btn[4]) {
			colorNum = 4;
		}else if(e.getSource() == btn[5]) {
			colorNum = 5;
		}
		
	}
	
	public ArrayList<catchmind_ShapDTO> getServerInfoList() {
		return serverInfoList;
	}

	public void setServerInfoList(ArrayList<catchmind_ShapDTO> serverInfoList) {
		this.serverInfoList = serverInfoList;
	}

	public ArrayList<catchmind_ShapDTO> getServerDrawList() {
		return serverDrawList;
	}

	public void setServerDrawList(ArrayList<catchmind_ShapDTO> serverDrawList) {
		this.serverDrawList = serverDrawList;
	}
	
	public int getColorNum() {
		return colorNum;
	}

	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
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
	
	public ArrayList<catchmind_ShapDTO> getSendList() {
		return sendlist;
	}

	public void setSendList(ArrayList<catchmind_ShapDTO> sendlist) {
		this.sendlist = sendlist;
	}

	public static void main(String[] args) {
		new catchmind_MsPaint();
	}


}
