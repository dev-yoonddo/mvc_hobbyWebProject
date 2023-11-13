package com.together.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.ibatis.session.SqlSession;

import com.together.vo.HeartVO;

public class HeartDAO {

	private static HeartDAO instance = new HeartDAO();
	private Connection conn; // 자바와 데이터베이스 연결
	private ResultSet rs; // 결과값 받아오기

	private HeartDAO() {
	}

	public static HeartDAO getInstance() {
		return instance;
	}

	public int checkHeart(SqlSession mapper, HeartVO vo) {
		System.out.println("check heart()");
		HeartVO heart = (HeartVO) mapper.selectOne("checkHeart", vo);
		if (heart != null) {
			return 1;
		} else {
			return 0;
		}
	}

	public int selectCount(SqlSession mapper, int boardID) {
		System.out.println("count heart()");
		return (int) mapper.selectOne("count", boardID);
	}

	public int heart(SqlSession mapper, HeartVO vo) {
		System.out.println("heart()");
		return mapper.insert("heart", vo);
	}

	public int deleteHeart(SqlSession mapper, HeartVO vo) {
		System.out.println("delete heart()");
		return mapper.delete("deleteHeart", vo);
	}
}
