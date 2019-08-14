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
	
	//아이디 조회
	public Users selectOne(String id) {
		conn=DAO.getConnect();
		
		Users usr=new Users();
		
		String sql="select id, pw, nick from users where id=?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				usr.setId(rs.getString("id"));
				usr.setPw(rs.getString("pw"));
				usr.setNick(rs.getString("nick"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usr;
		
	}
	
	//닉네임 조회
	
	public Users selectOneNick(String nick) {
		conn=DAO.getConnect();
		
		Users usr=new Users();
		
		String sql="select id, pw, nick from users where nick=?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, nick);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				usr.setId(rs.getString("id"));
				usr.setPw(rs.getString("pw"));
				usr.setNick(rs.getString("nick"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usr;
		
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
