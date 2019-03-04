package miniproject.catchmind;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import miniproject.membership.dao.MembershipDAO;

public class CommonHandler2 extends Thread {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ArrayList<CommonHandler2>arCHandler;
	private ArrayList<waitingRoomUserDTO> arUserList;
	private ArrayList<waitingRoomRCreateDTO> arRoomList;
	private ArrayList<catchmind_ShapDTO> shapeDTOList;
	private ArrayList<GameUserDTO> arGameUserList;
	private String roomName;
	
	public CommonHandler2(Socket socket,
			ArrayList<CommonHandler2> arCHandler,
			ArrayList<waitingRoomUserDTO>  arUserList, 
			ArrayList<waitingRoomRCreateDTO> arRoomList, 
			ArrayList<GameUserDTO> arGameUserList) {
		
		this.socket = socket;
		this.arCHandler = arCHandler;
		this.arUserList= arUserList;
		this.arRoomList = arRoomList;
		this.arGameUserList = arGameUserList;
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("�ڵ鷯 oos, ois");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		//���â 3�� 
		WaitingRoomChattingDTO waitingroomchattingDTO = null;
		String nickName = null;
		
		waitingRoomUserDTO waitingroomuserDTO = null;
		String id = null;
		String userName = null;
		int score = 0 ;
		int indexNumber = 0;
		
		waitingRoomRCreateDTO waitingroomrcreateDTO = null;
		roomName = null;
		String roomPass = null;
		String owner = null;
		int person = 0;
		int roomNumber = 0;
		
		//���� 2��
		ChatDTO chatHandlerDTO = null;
		String chatNickName = null;
		GameUserDTO gameuserDTO = null;
		String gameUserName =null;
		int point = 0;
		
		while(true) {
					try {

				//5�� ������ ���� �ޱ�
				//���â 3�� �ޱ�
						waitingroomchattingDTO = (WaitingRoomChattingDTO)ois.readObject();
						waitingroomuserDTO = (waitingRoomUserDTO)ois.readObject();
						waitingroomrcreateDTO = (waitingRoomRCreateDTO)ois.readObject();
						
						
				//���� 2�� �ޱ�		
						chatHandlerDTO=(ChatDTO)ois.readObject();
						shapeDTOList = (ArrayList<catchmind_ShapDTO>) ois.readObject();
						gameuserDTO = (GameUserDTO)ois.readObject();
						
				
				//���� �ҷ�����	----------------------------------------------------------------------	
						
						if(waitingroomchattingDTO.getCommand()== Info.JOIN && arUserList !=null) {
								
								for(int i =0; i<arUserList.size();i++) {
									userName = arUserList.get(i).getName();
									WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
									waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
									waitingroomuserDTO_send.setName(userName);
									waitingroomuserDTO_send.setCommand(Info.JOIN);
									waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
									ChatDTO chatDTO_send = new ChatDTO();
									GameUserDTO gameuser_send = new GameUserDTO();
									gameuser_send.setCommand(Info.WAIT);
									oos.writeObject(waitingroomchattingDTO_send);
									oos.writeObject(waitingroomuserDTO_send);
									oos.writeObject(waitingroomrcreateDTO_send);
									oos.writeObject(chatDTO_send);
									oos.writeObject(shapeDTOList);
									oos.writeObject(gameuser_send);
									
								}
								oos.flush();
							
						}
						
				//����� �ҷ�����---------------------------------------------------------------
						
						
						if(waitingroomrcreateDTO.getCommand() == Info.JOIN && arRoomList!=null) {
							
							for(int i =0; i<arRoomList.size();i++) {
								roomName = arRoomList.get(i).getRoomName();
								roomPass = arRoomList.get(i).getRoomPass();
								person = arRoomList.get(i).getPerson();
								WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
								waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
								waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
								waitingroomrcreateDTO_send.setRoomName(roomName);
								waitingroomrcreateDTO_send.setRoomPass(roomPass);
								waitingroomrcreateDTO_send.setPerson(person);
								waitingroomrcreateDTO_send.setCommand(Info.CREATE);
								ChatDTO chatDTO_send = new ChatDTO();
								GameUserDTO gameuser_send = new GameUserDTO();
								gameuser_send.setCommand(Info.WAIT);
								oos.writeObject(waitingroomchattingDTO_send);
								oos.writeObject(waitingroomuserDTO_send);
								oos.writeObject(waitingroomrcreateDTO_send);
								oos.writeObject(chatDTO_send);
								oos.writeObject(shapeDTOList);
								oos.writeObject(gameuser_send);
							
								//broadcast(waitingroomchattingDTO_send,waitingroomuserDTO_send);
							}
							oos.flush();
						}
						
						
						//arGameUserList.add(chatHandlerDTO);	//�������� ����Ʈ �߰�
				//���ӹ濡�� ���� �߰�-----------------------------------------------------------------------
						//System.out.println(arGameUserList.size());
						
						if(gameuserDTO.getCommand()==Info.JOIN && arGameUserList != null) {
							//System.out.println(" ���¼�ġ : "+arGameUserList.size());
							for(int i =0; i<arGameUserList.size();i++) {							
								WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
								waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
								waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
								waitingroomrcreateDTO_send.setRoomName(waitingroomrcreateDTO.getRoomName());
								ChatDTO chatDTO_send = new ChatDTO();
								
								GameUserDTO gameuserDTO_send = new GameUserDTO();
								gameuserDTO_send.setName(arGameUserList.get(i).getName());
								gameuserDTO_send.setPoint(arGameUserList.get(i).getPoint());
								gameuserDTO_send.setOwner(arGameUserList.get(i).getOwner());
								gameuserDTO_send.setCommand(Info.JOIN);
									
								oos.writeObject(waitingroomchattingDTO_send);
								oos.writeObject(waitingroomuserDTO_send);
								oos.writeObject(waitingroomrcreateDTO_send);
								oos.writeObject(chatDTO_send);
								oos.writeObject(shapeDTOList);
								oos.writeObject(gameuserDTO_send);
								
							}
							oos.flush();
						}
						
						
				//------------------------------------------------------------------------------------------
						if(waitingroomchattingDTO.getCommand()== Info.JOIN) {
							nickName = waitingroomchattingDTO.getNickName();
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setNickName(nickName);
							waitingroomchattingDTO_send.setCommand(Info.SEND);
							waitingroomchattingDTO_send.setMessage(nickName+"���� �����Ͽ����ϴ�");
							
							broadcast(waitingroomchattingDTO_send);	//1
							
							
						}else if(waitingroomchattingDTO.getCommand()== Info.EXIT) {
							
							arCHandler.remove(this);
							arUserList.remove(this);
							arRoomList.remove(this);
							arGameUserList.remove(this);
							
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setNickName(nickName);
							waitingroomchattingDTO_send.setCommand(Info.EXIT);
							
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setCommand(Info.EXIT);
							
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.EXIT);
							
							oos.writeObject(waitingroomchattingDTO_send);
							oos.writeObject(waitingroomuserDTO_send);
							oos.writeObject(waitingroomrcreateDTO_send);
							oos.writeObject(chatHandlerDTO);
							oos.writeObject(shapeDTOList);
							oos.writeObject(gameuserDTO);
							oos.flush();
			
							waitingroomchattingDTO_send.setCommand(Info.SEND);
							waitingroomchattingDTO_send.setMessage(nickName+"���� �����Ͽ����ϴ�");
							
							
							broadcast(waitingroomchattingDTO_send);
							broadcast(waitingroomuserDTO_send);
							broadcast(waitingroomrcreateDTO_send);
							broadcast(chatHandlerDTO);
							broadcast(shapeDTOList);
							broadcast(gameuserDTO);
							
							oos.close();
							ois.close();
							socket.close();
							
							break;
								
						}else if(waitingroomchattingDTO.getCommand() == Info.SEND){
		
							
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setCommand(Info.SEND);
							waitingroomchattingDTO_send.setMessage("["+nickName+"] "+waitingroomchattingDTO.getMessage());
							
							
							broadcast(waitingroomchattingDTO_send);
							//broadcast(waitingroomuserDTO);
							//broadcast(waitingroomrcreateDTO);
							
						}else if(waitingroomchattingDTO.getCommand() == Info.WAIT){
							WaitingRoomChattingDTO waitingroomchattingDTO_send = new WaitingRoomChattingDTO();
							waitingroomchattingDTO_send.setCommand(Info.WAIT);
							broadcast(waitingroomchattingDTO_send);
						}
						
						//--------------------------------------------------------
						
						
						if(waitingroomuserDTO.getCommand()== Info.JOIN) {
							arUserList.add(waitingroomuserDTO);				// arrayList �߰� ����� ����Ʈ ī��Ʈ
							indexNumber = arUserList.size(); //������ �ε�����ȣ ��� �ʴ� ���°��
							id = waitingroomuserDTO.getId();
							userName = waitingroomuserDTO.getName();
							score = waitingroomuserDTO.getScore();
							
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setName(userName);
							waitingroomuserDTO_send.setCommand(Info.JOIN);
							
							broadcast(waitingroomuserDTO_send);	
							
			
						}else if (waitingroomuserDTO.getCommand() ==Info.WAIT) {
							waitingRoomUserDTO waitingroomuserDTO_send = new waitingRoomUserDTO();
							waitingroomuserDTO_send.setCommand(Info.WAIT);
							broadcast(waitingroomuserDTO_send);
						}
						
						//--------------------------------------------------------------
						
						
							
				
						if(waitingroomrcreateDTO.getCommand() == Info.CREATE) {		
							arRoomList.add(waitingroomrcreateDTO);				//arrayList �߰�  �� ����� ī��Ʈ 

							roomName = waitingroomrcreateDTO.getRoomName();
							roomPass = waitingroomrcreateDTO.getRoomPass();
							person = waitingroomrcreateDTO.getPerson();
							
							//UserArrayList ���� ����
							//arUserList.remove(indexNumber);
							
							
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.CREATE);
							waitingroomrcreateDTO_send.setRoomName(roomName);
							waitingroomrcreateDTO_send.setRoomPass(roomPass);
							waitingroomrcreateDTO_send.setPerson(person);
							waitingroomrcreateDTO_send.setRoomNumber(arRoomList.size());
							System.out.println("arRoomList  "+arRoomList.size());
							
							broadcast(waitingroomrcreateDTO_send);
							

						}else if(waitingroomrcreateDTO.getCommand() == Info.WAIT) {
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setCommand(Info.WAIT);
							waitingroomrcreateDTO_send.setRoomName(waitingroomrcreateDTO.getRoomName());
							broadcast(waitingroomrcreateDTO_send);
							
						}else if(waitingroomrcreateDTO.getCommand() == Info.JOIN) {
							arRoomList.add(waitingroomrcreateDTO);
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							for(int i = 0; i<arRoomList.size();i++) {
								System.out.println("�ڵ鷯�� ����� ���̸�"+arRoomList.get(i).getRoomName());
								waitingroomrcreateDTO_send.setRoomName(arRoomList.get(i).getRoomName());
							}
							
							roomName = waitingroomrcreateDTO.getRoomName();	// ChatFrame ������ �ڵ鷯2��  ������ ����
							roomPass = waitingroomrcreateDTO.getRoomPass();
							owner = waitingroomrcreateDTO.getOwner();
							person = waitingroomrcreateDTO.getPerson();
							roomNumber = waitingroomrcreateDTO.getRoomNumber();
							System.out.println(roomName);
	

							waitingroomrcreateDTO_send.setRoomPass(waitingroomrcreateDTO.getRoomPass());
							waitingroomrcreateDTO_send.setOwner(waitingroomrcreateDTO.getOwner());
							waitingroomrcreateDTO_send.setPerson(waitingroomrcreateDTO.getPerson());
							waitingroomrcreateDTO_send.setRoomNumber(waitingroomrcreateDTO.getRoomNumber());
							waitingroomrcreateDTO_send.setCommand(Info.WAIT);
							
							broadcast(waitingroomrcreateDTO_send);
							
						}

						
						//-------------------------------------------------------------------
						
						if(chatHandlerDTO.getCommand()==Info.JOIN) {
							chatNickName = gameuserDTO.getName();

							ChatDTO chatSendDTO= new ChatDTO();
							chatSendDTO.setCommand(Info.SEND);
							chatSendDTO.setMessage("["+chatNickName+"]���� �����Ͽ����ϴ�,  "+"�ų�ä�� �Ͻñ� �ٶ��ϴ�");
							
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);

							
						}else if(chatHandlerDTO.getCommand()==Info.EXIT) {
							
							//chatHandlerList.remove(this);
							System.out.println("gameusername = "+gameUserName);
							System.out.println("Point = "+point);
							String user = gameUserName;
							MembershipDAO membershipDAO = MembershipDAO.getInstance();
							//int newScore = membershipDAO.getScore(user);
							
							waitingRoomRCreateDTO waitingroomrcreateDTO_send = new waitingRoomRCreateDTO();
							waitingroomrcreateDTO_send.setRoomName(waitingroomrcreateDTO.getRoomName());
							ChatDTO chatSendDTO= new ChatDTO();
							chatSendDTO.setCommand(Info.EXIT);
							GameUserDTO gameuserDTO_send = new GameUserDTO();
							gameuserDTO_send.setCommand(Info.EXIT);
							gameuserDTO_send.setName(gameUserName);
							gameuserDTO_send.setPoint(point);
							
							oos.writeObject(waitingroomchattingDTO);
							oos.writeObject(waitingroomuserDTO);
							oos.writeObject(waitingroomrcreateDTO_send);
							oos.writeObject(chatSendDTO);
							oos.writeObject(shapeDTOList);
							oos.writeObject(gameuserDTO_send);
							oos.flush();
							
							chatSendDTO.setCommand(Info.SEND);
							chatSendDTO.setMessage(chatNickName+"���� �����Ͽ����ϴ�");
							
							broadcast(waitingroomchattingDTO);
							broadcast(waitingroomuserDTO);
							broadcast(waitingroomrcreateDTO);
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);
							broadcast(gameuserDTO_send);
							
							
						}else if(chatHandlerDTO.getCommand()==Info.SEND) {
							
							ChatDTO chatSendDTO= new ChatDTO();
							System.out.println("arRoomList�� ��"+arRoomList.size());
							
							chatSendDTO.setCommand(Info.SEND);
							chatSendDTO.setMessage("["+chatNickName+"] "+chatHandlerDTO.getMessage());
							
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);

							
						}else if(chatHandlerDTO.getCommand()==Info.WAIT) {
							ChatDTO chatSendDTO= new ChatDTO();
							
							broadcast(chatSendDTO);
							broadcast(shapeDTOList);
							
						}else if(chatHandlerDTO.getCommand()==Info.READY) {
			
							ChatDTO chatDTO_send = new ChatDTO();
							chatDTO_send.setCommand(Info.READY);
							chatDTO_send.setMessage("["+gameUserName+"]��"+"�ڡڡ�"+chatHandlerDTO.getMessage()+"�ڡڡ�");
							chatDTO_send.setReadyCount(chatHandlerDTO.getReadyCount());
							
							broadcast(chatDTO_send);
							broadcast(shapeDTOList);
							
						}else if(chatHandlerDTO.getCommand()==Info.START) {
							ChatDTO chatDTO_send = new ChatDTO();
							
							chatDTO_send.setCommand(Info.START);
							chatDTO_send.setMessage(chatHandlerDTO.getMessage());
							chatDTO_send.setStartCount(chatHandlerDTO.getStartCount());
							
							broadcast(chatDTO_send);
							broadcast(shapeDTOList);
							
						}else if(chatHandlerDTO.getCommand()==Info.ANSWER) {
							ChatDTO chatDTO_send = new ChatDTO();
							chatDTO_send.setCommand(Info.ANSWER);
							chatDTO_send.setNickName(gameUserName);
							chatDTO_send.setMessage(chatHandlerDTO.getMessage());						
							broadcast(chatDTO_send);
							broadcast(shapeDTOList);
							
						}
						
						//-----------------------------------------------------------------------
						
						if(gameuserDTO.getCommand()==Info.JOIN) {
							
							gameUserName = gameuserDTO.getName();			//�̸� ����
							point = gameuserDTO.getPoint();				//���� ����

							//arGameUserList.add(gameuserDTO);	//�������� Array ����Ʈ �߰� ��̸���Ʈ�� �̸��� ���� ���ؼ�
							GameUserDTO gameuserDTO_send = new GameUserDTO();
							gameuserDTO_send.setName(gameUserName);
							gameuserDTO_send.setPoint(point);
							gameuserDTO_send.setOwner(waitingroomrcreateDTO.getOwner());
							
							
							arGameUserList.add(gameuserDTO_send);
							gameuserDTO_send.setCommand(Info.JOIN);
							
							broadcast(gameuserDTO_send);
							
						}else if(gameuserDTO.getCommand()==Info.WAIT) {
						
							GameUserDTO gameuserDTO_send = new GameUserDTO();

							broadcast(gameuserDTO_send);
						}
						
						//------------------------------------------------------------------------------
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
		
		
		}//while��
	}//run


	public void broadcast (WaitingRoomChattingDTO waitingroomchattingDTO_send){
		
		for(CommonHandler2 commonhandler : arCHandler){
			try{
				commonhandler.oos.writeObject(waitingroomchattingDTO_send);
				commonhandler.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();
	
			}
		}
	}
	
	public void broadcast (waitingRoomUserDTO waitingroomuserDTO_send) {
		for(CommonHandler2 commonhandler : arCHandler){
			try{
				commonhandler.oos.writeObject(waitingroomuserDTO_send);
				commonhandler.oos.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
		}		
	}
	public void broadcast (WaitingRoomChattingDTO waitingroomchattingDTO,
							waitingRoomUserDTO waitingroomuserDTO_send){
		for(CommonHandler2 commonhandler : arCHandler){
			try{
				commonhandler.oos.writeObject(waitingroomchattingDTO);
				commonhandler.oos.writeObject(waitingroomuserDTO_send);
				commonhandler.oos.flush();
				
			}catch(IOException e){
				e.printStackTrace();
	
			}
		}
	}
	public void broadcast (waitingRoomRCreateDTO waitingroomrcreateDTO_send) {
		
		for(CommonHandler2 commonhandler : arCHandler) {
			try {
				commonhandler.oos.writeObject(waitingroomrcreateDTO_send);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void broadcast(ArrayList<catchmind_ShapDTO> shapeDTOList) {
		for(CommonHandler2 commonhandler:arCHandler) {
			try {
				commonhandler.oos.writeObject(shapeDTOList);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		
	}

	public void broadcast(ChatDTO chatsendDTO) {
		for(CommonHandler2 commonhandler:arCHandler) {
				try {
					commonhandler.oos.writeObject(chatsendDTO);
					commonhandler.oos.flush();
						
				} catch (IOException e) {
						e.printStackTrace();
				}
		}
	}
	
	public void broadcast(GameUserDTO gameuserDTO_send) {
		for(CommonHandler2 commonhandler:arCHandler) {
			try {
				commonhandler.oos.writeObject(gameuserDTO_send);
				commonhandler.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
}


