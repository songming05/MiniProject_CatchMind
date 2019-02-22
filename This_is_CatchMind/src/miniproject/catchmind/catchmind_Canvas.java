package miniproject.catchmind;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class catchmind_Canvas extends Canvas /*implements Runnable*/{
	//private ChatFrame chat;
	private catchmind_MsPaint mp;
	private Image bufferImg;
	private Graphics bufferG;
	
	public catchmind_Canvas(catchmind_MsPaint mp){
		this.mp = mp;
		setBackground(new Color(255, 255 ,255));		
	}
	
	@Override
	public void update(Graphics g){
		Dimension d = getSize(); //������ Ȯ��
		if(bufferG == null){//Graphics�� ���� �ƹ��͵� ������ Ȯ��
			bufferImg = createImage(d.width, d.height); //ĵ������ ���̿� ���̸�ŭ �׷���
			bufferG = bufferImg.getGraphics(); //���� �̹����� bufferG�� ����
		}		
		bufferG.setColor(getBackground()); //ĵ���� ������
		bufferG.fillRect(0, 0, d.width, d.height); //�׻��� ĵ���� ũ�⸸ŭ ä����(������ ����)
		
		
		if(mp.getServerInfoList().size() > 0) {
			for(catchmind_ShapDTO dto : mp.getServerInfoList()) {
				int x1 = dto.getX1(); //getter�� x1�� �������� 
				int y1 = dto.getY1(); //getter�� y1�� �������� 
				int x2 = dto.getX2(); //getter�� x2�� �������� 
				int y2 = dto.getY2(); //getter�� y2�� �������� 				
				switch(dto.getColorNum()) {
					case 0 : bufferG.setColor(Color.BLACK); break; //������
					case 1 : bufferG.setColor(Color.RED); break; //������
					case 2 : bufferG.setColor(Color.GREEN); break; //�ʷϻ�
					case 3 : bufferG.setColor(Color.BLUE); break; //�Ķ���
					case 4 : bufferG.setColor(Color.YELLOW); break; //�����
					case 5 : bufferG.setColor(Color.MAGENTA); break; //��ȫ��
				}
				if(dto.getShape() == Shape.LINE) {
					bufferG.drawLine(x1, y1, x2, y2); //���� ��
				}else if(dto.getShape() == Shape.RECT) {
					bufferG.fillRect(x1, y1, 10, 10); //���� ��
				}
			}
		}else {
			for(catchmind_ShapDTO dto : mp.getServerInfoList()) {//�����κ��� �޾ƿ� ����Ʈ
				int x1 = dto.getX1(); int y1 = dto.getY1();  int x2 = dto.getX2(); 	int y2 = dto.getY2(); 	
				
				switch(dto.getColorNum()) {
					case 0 : bufferG.setColor(Color.BLACK); break; //������
					case 1 : bufferG.setColor(Color.RED); break; //������
					case 2 : bufferG.setColor(Color.GREEN); break; //�ʷϻ�
					case 3 : bufferG.setColor(Color.BLUE); break; //�Ķ���
					case 4 : bufferG.setColor(Color.YELLOW); break; //�����
					case 5 : bufferG.setColor(Color.MAGENTA); break; //��ȫ��
				}
				if(dto.getShape() == Shape.LINE) bufferG.drawLine(x1, y1, x2, y2); //���� ��
				else if(dto.getShape() == Shape.RECT) 	bufferG.fillRect(x1, y1, 10, 10); //���� ��
			}
		}
		
		if(mp.getSendList().size() ==0) {//list�� ������ ���ʸ� �⵿
			int x1 = mp.getX1(); //getter�� x1�� �������� 
			int y1 = mp.getY1(); //getter�� y1�� �������� 
			int x2 = mp.getX2(); //getter�� x2�� �������� 
			int y2 = mp.getY2(); //getter�� y2�� �������� 
			
			switch(mp.getColorNum()) {
				case 0 : bufferG.setColor(Color.BLACK); break;
				case 1 : bufferG.setColor(Color.RED); break;
				case 2 : bufferG.setColor(Color.GREEN); break;
				case 3 : bufferG.setColor(Color.BLUE); break;
				case 4 : bufferG.setColor(Color.YELLOW); break;
				case 5 : bufferG.setColor(Color.MAGENTA); break;
			}
			if(mp.getThinB().isSelected()) 	bufferG.drawLine(x1, y1, x2, y2);
			else if(mp.getThickB().isSelected()) bufferG.fillRect(x1, y1, 10, 10);			
		}
		paint(g); 
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(bufferImg, 0, 0, this);
	}


}
