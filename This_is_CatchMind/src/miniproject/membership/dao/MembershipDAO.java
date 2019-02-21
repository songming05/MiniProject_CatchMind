package miniproject.membership.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import miniproject.membership.dto.MembershipDTO;

public class MembershipDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "java";
	private String password = "itbank";
	
	private static MembershipDAO instance;	
	public static MembershipDAO getInstance() {
		if(instance==null) {
			synchronized (MembershipDAO.class) {
				instance = new MembershipDAO();
			}
		}
		return instance;
	}	
	
	public MembershipDAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}//constr
	
	public Connection getConnection(){
		Connection conn=null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return conn;
	}//getConnection
	
	public int getSeq() {
		int seq = 0;
		Connection conn = null;
		PreparedStatement prps = null;		
		ResultSet rs = null;
		
		String sql = "select seq_membership.nextval from dual";
			
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			rs = prps.executeQuery();
			rs.next();
			seq = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return seq;
	}//getSeq

	public void insertArticle(MembershipDTO membershipDTO) {
		String sql = "insert into membership values(?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement prps = null;
		try {
			conn=getConnection();
			prps = conn.prepareStatement(sql);
			prps.setInt(1, membershipDTO.getSequence());
			prps.setString(2, membershipDTO.getId());
			prps.setString(3, membershipDTO.getPassword());
			prps.setString(4, membershipDTO.getName());
			prps.setString(5, membershipDTO.getAddress());
			prps.setString(6, membershipDTO.getPhone());
			prps.setString(7, membershipDTO.getEmail());
			prps.setInt(8, membershipDTO.getTotalScore());
			
			prps.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(prps!=null) prps.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}//insertArticle
}
