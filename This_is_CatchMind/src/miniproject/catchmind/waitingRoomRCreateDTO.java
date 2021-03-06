package miniproject.catchmind;

import miniproject.catchmind.Info;

import java.io.Serializable;

public class waitingRoomRCreateDTO implements Serializable{
	private int roomSeq;
	private String roomName;
	private String roomPass;
	private String owner;
	private int currentPerson, limitPerson;
	

	private int person;//0 ->2명  // 1->3명 //2->4명 
	private int roomNumber;
	private Info command;
	
	
	public int getRoomSeq() {
		return roomSeq;
	}
	public void setRoomSeq(int roomSeq) {
		this.roomSeq = roomSeq;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public int getCurrentPerson() {
		return currentPerson;
	}
	public void setCurrentPerson(int currentPerson) {
		this.currentPerson = currentPerson;
	}
	public int getLimitPerson() {
		return limitPerson;
	}
	public void setLimitPerson(int limitPerson) {
		this.limitPerson = limitPerson;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public Info getCommand() {
		return command;
	}
	public void setCommand(Info command) {
		this.command = command;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomPass() {
		return roomPass;
	}
	public void setRoomPass(String roomPass) {
		this.roomPass = roomPass;
	}
	public int getPerson() {
		return person;
	}
	public void setPerson(int person) {
		this.person = person;
	}
	
	public String toString() {
		return "["+roomName+"]"+" 인원수 : "+person;
	}
		
}