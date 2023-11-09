package com.together.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.together.vo.CommentVO;

public class CommentDAO {

	private static CommentDAO instance = new CommentDAO();
	private Connection conn; // 자바와 데이터베이스 연결
	private ResultSet rs; // 결과값 받아오기

	private CommentDAO() {
	}

	public static CommentDAO getInstance() {
		return instance;
	}

//	MvcboardService 클래스에서 호출되는 mapper와 테이블에 저장할 메인글 정보가 저장된 객체를 넘겨받고 메인글을
//	테이블에 저장하는 mvcboard.xml 파일의 insert sql 명령을 실행하는 메소드
	public void regist(SqlSession mapper, CommentVO vo) {
		System.out.println("CommentDAO 클래스의 insert() 메소드");
		mapper.insert("regist", vo);
	}

//	MvcboardService 클래스에서 호출되는 mapper를 넘겨받고 전체 글의 개수를 얻어오는 mvcboard.xml 파일의
//	select sql 명령을 실행하는 메소드
	public int selectCount(SqlSession mapper, int boardID) {
		System.out.println("CommentDAO 클래스의 selectCount() 메소드");
		return (int) mapper.selectOne("selectCount", boardID);
	}

	public ArrayList<CommentVO> selectCmtList(SqlSession mapper, int boardID) {
		System.out.println("CommentDAO 클래스의 selectdList() 메서드");
		ArrayList<CommentVO> cmtlist = (ArrayList<CommentVO>) mapper.selectList("selectCmtList", boardID);
		return cmtlist;
		// return mapper.selectList("selectList");
	}

	public CommentVO getCmtVO(SqlSession mapper, int cmtID) {
		System.out.println("CommentDAO 클래스의 getCmtVO() 메소드");
		return (CommentVO) mapper.selectOne("getCmtVO", cmtID);
	}
}
