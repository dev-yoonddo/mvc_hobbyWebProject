package com.together.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.together.vo.BoardVO;

public class BoardDAO {

	private static BoardDAO instance = new BoardDAO();
	private Connection conn; // 자바와 데이터베이스 연결
	private ResultSet rs; // 결과값 받아오기

	protected BoardDAO() {
	}

	public static BoardDAO getInstance() {
		return instance;
	}

//	MvcboardService 클래스에서 호출되는 mapper와 테이블에 저장할 메인글 정보가 저장된 객체를 넘겨받고 메인글을
//	테이블에 저장하는 mvcboard.xml 파일의 insert sql 명령을 실행하는 메소드
	public void insert(SqlSession mapper, BoardVO insert) {
		System.out.println("BoardDAO 클래스의 insert() 메소드");
		mapper.insert("insert", insert);
	}

//	MvcboardService 클래스에서 호출되는 mapper를 넘겨받고 전체 글의 개수를 얻어오는 mvcboard.xml 파일의
//	select sql 명령을 실행하는 메소드
	public int selectCount(SqlSession mapper) {
		System.out.println("BoardDAO 클래스의 selectCount() 메소드");
		return (int) mapper.selectOne("selectCount");
	}

	public ArrayList<BoardVO> selectBoardList(SqlSession mapper, String boardCategory) {
		System.out.println("BoardDAO 클래스의 boardList() 메서드");
		ArrayList<BoardVO> boardlist = (ArrayList<BoardVO>) mapper.selectList("selectBoardList", boardCategory);
		/*
		 * try { PreparedStatement pstmt = conn.prepareStatement(SQL); rs =
		 * pstmt.executeQuery(); while (rs.next()) { BoardVO board = new BoardVO();
		 * board.setIdx(rs.getInt(1)); board.setName(rs.getString(2));
		 * board.setSubject(rs.getString(3)); board.setContent(rs.getString(4));
		 * board.setGup(rs.getInt(5)); board.setLev(rs.getInt(6));
		 * board.setSeq(rs.getInt(7)); board.setHit(rs.getInt(8));
		 * board.setWriteDate(rs.getDate(9)); list.add(board); } } catch (Exception e) {
		 * e.printStackTrace(); } return list;
		 */
		return boardlist;
		// return mapper.selectList("selectList");
	}

	/*
	 * MvcboardService 클래스에서 호출되는 mapper와 1페이지의 시작, 끝 인덱스가 저장된 HashMap 객체를 넘겨받고 //
	 * 1페이지 분량의 글 목록을 얻어오는 mvcboard.xml 파일의 select sql 명령을 실행하는 메소드 public
	 * ArrayList<BoardVO> selectList(SqlSession mapper, HashMap<String, Integer>
	 * hmap) { System.out.println("BoardDAO 클래스의 selectList() 메소드"); return
	 * (ArrayList<BoardVO>) mapper.selectList("selectList", hmap); }
	 */
//	MvcboardService 클래스에서 호출되는 mapper와 조회수를 증가시킬 글번호를 넘겨받고 조회수를 증가시키는
//	mvcboard.xml 파일의 update sql 명령을 실행하는 메소드
	public int increment(SqlSession mapper, int boardID) {
		System.out.println("BoardDAO 클래스의 increment() 메소드");
		return mapper.update("increment", boardID);
	}

//	MvcboardService 클래스에서 호출되는 mapper와 조회수를 증가시킨 글번호를 넘겨받고 조회수를 증가시킨 글 1건을
//	얻어오는 mvcboard.xml 파일의 select sql 명령을 실행하는 메소드
	public BoardVO getBoardVO(SqlSession mapper, int boardID) {
		System.out.println("BoardDAO 클래스의 getBoardVO() 메소드");
		return (BoardVO) mapper.selectOne("getBoardVO", boardID);
	}

	// 게시글 업데이트
	public void update(SqlSession mapper, BoardVO update) {
		mapper.update("update", update);
	}

	public String getFileName(SqlSession mapper, int boardID) {
		return (String) mapper.selectOne("getFileName", boardID);
	}

	public void download(SqlSession mapper, int boardID) {
		System.out.println("BoardDAO클래스의 download() 메서드");
		mapper.update("download", boardID);
	}

	/*
	 * try { Map<String, Object> paramMap = new HashMap<>(); paramMap.put("idx",
	 * idx); paramMap.put("subject", subject); paramMap.put("content", content);
	 * 
	 * // Execute the update statement mapper.update("update", paramMap);
	 * 
	 * // Commit the transaction mapper.commit(); } finally { mapper.close(); }
	 */

	public void delete(SqlSession mapper, int boardID) {
		System.out.println("BoardDAO클래스의 delete() 메서드");
		mapper.delete("delete", boardID);
	}

	public int heartCount(SqlSession mapper, int boardID) {
		return mapper.update("heartCount", boardID);
	}

	public int heartDelete(SqlSession mapper, int boardID) {
		return mapper.update("heartDelete", boardID);
	}
}
