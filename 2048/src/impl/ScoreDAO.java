package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DAO;
import model.Score;
import model.Users;
import view.LogInController;

public class ScoreDAO {

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection conn = null;

	private static ScoreDAO instance = new ScoreDAO();

	public static ScoreDAO getInstance() {
		return instance;
	}

	public void insertScore(int score) {

		conn = DAO.getConnect();

		String sql = "insert into score values(s_id_seq.nextval, ?, ?, sysdate)";
		Users usr = LogInController.usr;
		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, usr.getNick());
			pstmt.setInt(2, score);

			int r = pstmt.executeUpdate();

			System.out.println(r + " has been updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Score> selectOne(String nick) {
		conn = DAO.getConnect();

		Score scr = null;
		ArrayList<Score> list = new ArrayList<Score>();

		String sql = "select s_id, nick, score, g_date from score where nick=?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, nick);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				scr = new Score();

				scr.setS_id(rs.getInt("s_id"));
				scr.setNick(rs.getString("nick"));
				scr.setScore(rs.getInt("score"));
				scr.setG_date(rs.getString("g_date"));

				list.add(scr);

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

	public List<Score> selectAll() {
		conn = DAO.getConnect();

		Score scr = null;
		ArrayList<Score> list = new ArrayList<Score>();

		String sql = "select s_id, nick, score, g_date from score";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				scr = new Score();

				scr.setS_id(rs.getInt("s_id"));
				scr.setNick(rs.getString("nick"));
				scr.setScore(rs.getInt("score"));
				scr.setG_date(rs.getString("g_date"));

				list.add(scr);

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
	
	public List<Score> searchHighScore() {
		conn=DAO.getConnect();
		Users usr=LogInController.usr;
		
		String sql="select max(score), nick from score";
		
		try {
			pstmt=conn.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public void highScore(int score) {
		conn = DAO.getConnect();
		Users usr = LogInController.usr;
		int s = 0;

		String sql = "select highscore from users where nick=?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, usr.getNick());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				s = rs.getInt("highscore");

				if (s < score) {

					sql = "update users set highscore=? where nick=?";
					pstmt=conn.prepareStatement(sql);
					
					pstmt.setInt(1, score);
					pstmt.setString(2, usr.getNick());

					int r = pstmt.executeUpdate();

					System.out.println(r + "건 업데이트");
				}
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
	}

}
