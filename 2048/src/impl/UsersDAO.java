package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DAO;
import model.Users;

public class UsersDAO {
	
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	Connection conn=null;
	
	private static UsersDAO instance=new UsersDAO();
	
	public static UsersDAO getInstance() {
		return instance;
	}
	
	//아이디가 있는지 확인
	public boolean checkId(String id) {
		conn=DAO.getConnect();
		
		String sql="select id from users where id=?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs=pstmt.executeQuery();
			
			
			if(rs.next())
				if(id.equals(rs.getString("id")))
					return false;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	//닉네임이 있는지 확인
	
	public boolean checkNick(String nick) {
		conn=DAO.getConnect();
		
		String sql="select nick from users where nick=?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, nick);
			
			rs=pstmt.executeQuery();
			
			
			if(rs.next())
				if(nick.equals(rs.getString("nick")))
					return false;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	//회원가입
	public void signUp(Users usr) {
		conn=DAO.getConnect();
		
		String sql="insert into users(id, pw, nick, highscore)"
				+ "values(?,?,?,null)";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, usr.getId());
			pstmt.setString(2, usr.getPw());
			pstmt.setString(3, usr.getNick());
			
			int rs=pstmt.executeUpdate();
			
			System.out.println(rs+"건이 입력");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//로그인
	
	

}
