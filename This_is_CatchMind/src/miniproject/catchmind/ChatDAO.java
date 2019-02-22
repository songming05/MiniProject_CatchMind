package miniproject.catchmind;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import miniproject.catchmind.ChatDTO;

public class ChatDAO {
	private String chatDriver = "oracle.jdbc.driver.OracleDriver";
	private String chatUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	private String chatUser = "java";
	private String chatPassword = "itbank";
	
	private static ChatDAO instance; 
	
	public static ChatDAO getInstance() {
		synchronized(ChatDAO.class) {
			if(instance==null) {
				instance = new ChatDAO();
			}
		}
		
		return instance;
	}

	
	public ChatDAO() {
		try {
			Class.forName(chatDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		Connection chatConn = null;

		try {
			chatConn = DriverManager.getConnection(chatUrl, chatUser, chatPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatConn;
	}

	
	public int getSeq() {
		int seq=0;//��������ü���� 0�� ������ ���� �߸��Ȱ���
		Connection chatConn = getConnection();//�̰� 3���� ��Ʈ�ΰŰ���
		PreparedStatement pstmt = null;//�̰� 3���� ��Ʈ�ΰŰ���
		ResultSet rs = null;//�̰� 3���� ��Ʈ�ΰŰ���
		String sql = "select seq_membership.nextval from dual";
		
		try {
			pstmt = chatConn.prepareStatement(sql);//����
			rs = pstmt.executeQuery();//����
			
			rs.next();
			seq = rs.getInt(1);// 1�� �÷��� ���� seq�� �����϶�� ��
			//����Ŭ�� �÷�(������)�� 0���� ���� �ϴ°� �ƴ϶� 1���� �����ϴ� ���̴�
			//���������� ������ 1���� ����
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(chatConn!=null) chatConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return seq;
	}

	public void insert(ChatDTO chatDTO) {
		Connection conn = getConnection();
		
		PreparedStatement pstmt = null;
		String sql = "insert into membership values(?,?,?,?,?)";
		//(seq, id,password,name,email) values(SEQ_MEMBERSHIP.NEXTVAL,'aaa','33333','�￡��','aaa@gmail.com')";
		
		try {
			pstmt = conn.prepareStatement(sql);//����
			pstmt.setInt(1, chatDTO.getSeq());
			pstmt.setString(2, chatDTO.getId());
			pstmt.setString(3, chatDTO.getPassword());
			pstmt.setString(4, chatDTO.getEmail());
			//pstmt.setString(2, chatDTO.getNickName());
			//pstmt.setString(3, chatDTO.get);
			
			pstmt.executeUpdate();//����
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<ChatDTO> getChatList() {
		ArrayList<ChatDTO> list = new ArrayList<ChatDTO>();//DTO�� �����ϱ����� ����
		Connection chatConn = getConnection();//�̰� 3���� ��Ʈ�ΰŰ���
		PreparedStatement pstmt = null;//�̰� 3���� ��Ʈ�ΰŰ���
		ResultSet rs = null;//�̰� 3���� ��Ʈ�ΰŰ���
		String sql = "select * from membership";//friend�� �ִ� ��� ������ �ҷ��´�
		
		try {
			pstmt = chatConn.prepareStatement(sql);//����
			rs = pstmt.executeQuery();//����
			
			while(rs.next()) {//�����Ͱ� ��������� ���� �о����
				ChatDTO chatDTO = new ChatDTO();//�����ϱ����� ��ü ����
				chatDTO.setSeq(rs.getInt("seq"));//DB���� �����ͺҷ� �ͼ� DTO�� ����
				chatDTO.setNickName(rs.getString("name"));//DB���� �����ͺҷ� �ͼ� DTO�� ����
				
				list.add(chatDTO);//list��   DB���� ������ DTO������ �����
			}//while
			
		} catch (SQLException e) {
			e.printStackTrace();
			list = null;//try catch�������� �����߻��� list�� null�� �־ ���̻� ������ �ȵǰ� ����
		}finally {
			try {
				if(rs!=null) rs.close();//�������� ������������ �Ųٷ� �ݾ��ش�
				if(pstmt!=null) pstmt.close();//�������� ������������ �Ųٷ� �ݾ��ش�
				if(chatConn!=null) chatConn.close();//�������� ������������ �Ųٷ� �ݾ��ش�
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;//FriendManager��   ArrayList<FriendDTO> list�� �� ����
	}


	public void delete(int seq) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "delete from membership where seq=?";
		
		try {
			pstmt = conn.prepareStatement(sql);//����
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();//����
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();//�������� ������������ �Ųٷ� �ݾ��ش�
				if(conn!=null) conn.close();//�������� ������������ �Ųٷ� �ݾ��ش�
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

