package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DAO;
import model.Users;

public class UsersDAO {

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection conn = null;

	private static UsersDAO instance = new UsersDAO();

	public static UsersDAO getInstance() {
		return instance;
	}

	// �븘�씠�뵒 議고쉶
	public Users selectOne(String id) {
		conn = DAO.getConnect();

		Users usr = new Users();

		String sql = "select id, pw, nick from users where id=?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				usr.setId(rs.getString("id"));
				usr.setPw(rs.getString("pw"));
				usr.setNick(rs.getString("nick"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usr;

	}

	// �땳�꽕�엫 議고쉶

	public Users selectOneNick(String nick) {
		conn = DAO.getConnect();

		Users usr = new Users();

		String sql = "select id, pw, nick from users where nick=?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, nick);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				usr.setId(rs.getString("id"));
				usr.setPw(rs.getString("pw"));
				usr.setNick(rs.getString("nick"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usr;

	}

	// �쉶�썝媛��엯
	public void signUp(Users usr) {
		conn = DAO.getConnect();

		String sql = "insert into users(id, pw, nick, highscore)" + "values(?,?,?,null)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, usr.getId());
			pstmt.setString(2, usr.getPw());
			pstmt.setString(3, usr.getNick());

			int rs = pstmt.executeUpdate();

			System.out.println(rs + "嫄댁씠 �엯�젰");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Users> selectHighScores() {
		ArrayList<Users> list = new ArrayList<Users>();
		Users usr = null;

		String sql = "select nick, highscore from users order by 2 desc";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				usr = new Users();

				usr.setNick(rs.getString("nick"));
				usr.setHiScore(rs.getInt("highscore"));

				list.add(usr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	// 濡쒓렇�씤

}
