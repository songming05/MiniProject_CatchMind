package miniproject.catchmind;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class catchmind_Canvas extends Canvas{
	private catchmind_MsPaint mp;
	private Image bufferImg;
	private Graphics bufferG;
	
	public catchmind_Canvas(catchmind_MsPaint mp){
		this.mp = mp;
		setBackground(new Color(255, 255 ,255));
	}
	
	@Override
	public void update(Graphics g){

		Dimension d = getSize(); //사이즈 확인

		if(bufferG == null){//Graphics의 값이 아무것도 없는지 확인
			bufferImg = createImage(d.width, d.height); //캔버스의 높이와 길이만큼 그려라
			bufferG = bufferImg.getGraphics(); //만든 이미지를 bufferG에 저장
		}
		
		bufferG.setColor(getBackground()); //캔버스 바탕색
		bufferG.fillRect(0, 0, d.width, d.height); //그색을 캔버스 크기만큼 채워라(그전껄 지움)
		
		for(catchmind_ShapDTO dto : mp.getList()) {
			int x1 = dto.getX1(); //getter로 x1값 가져오기 
			int y1 = dto.getY1(); //getter로 y1값 가져오기 
			int x2 = dto.getX2(); //getter로 x2값 가져오기 
			int y2 = dto.getY2(); //getter로 y2값 가져오기 
			
			switch(dto.getColorNum()) {
				case 0 : bufferG.setColor(Color.BLACK); break;
				case 1 : bufferG.setColor(Color.RED); break;
				case 2 : bufferG.setColor(Color.GREEN); break;
				case 3 : bufferG.setColor(Color.BLUE); break;
				case 4 : bufferG.setColor(Color.YELLOW); break;
				case 5 : bufferG.setColor(Color.MAGENTA); break;
			}
			if(dto.getShape() == Shape.LINE) {
				bufferG.drawLine(x1, y1, x2, y2);
			}else if(dto.getShape() == Shape.RECT) {
				bufferG.fillRect(x1, y1, 10, 10);
			}
		}
		
		int x1 = mp.getX1(); //getter로 x1값 가져오기 
		int y1 = mp.getY1(); //getter로 y1값 가져오기 
		int x2 = mp.getX2(); //getter로 x2값 가져오기 
		int y2 = mp.getY2(); //getter로 y2값 가져오기 
		
		switch(mp.getColorNum()) {
			case 0 : bufferG.setColor(Color.BLACK); break;
			case 1 : bufferG.setColor(Color.RED); break;
			case 2 : bufferG.setColor(Color.GREEN); break;
			case 3 : bufferG.setColor(Color.BLUE); break;
			case 4 : bufferG.setColor(Color.YELLOW); break;
			case 5 : bufferG.setColor(Color.MAGENTA); break;
		}
		if(mp.getThinB().isSelected()) {
			bufferG.drawLine(x1, y1, x2, y2);
		}else if(mp.getThickB().isSelected()) {
			bufferG.fillRect(x1, y1, 10, 10);
		}
		
		paint(g); //그려진 캔버스 불러내기
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(bufferImg, 0, 0, this);
	}

}
