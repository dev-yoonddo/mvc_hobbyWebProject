package com.together.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.together.dao.BoardDAO;
import com.together.dao.HeartDAO;
import com.together.session.MySession;
import com.together.vo.BoardVO;

public class BoardService {

	private static BoardService instance = new BoardService();

	private BoardService() {
	}

	public static BoardService getInstance() {
		return instance;
	}

	private BoardDAO dao = BoardDAO.getInstance();
	private HeartDAO htdao = HeartDAO.getInstance();
	public DownloadAction down = new DownloadAction();

//	컨트롤러에 insertOK.nhn 이라는 요청이 들어오면 컨트롤러에서 호출하는 메소드로 테이블에 저장할 메인글이 
//	저장된 request 객체를 넘겨받고 mapper를 얻어온 후 DAO 클래스의 insert sql 명령을 실행하는 메소드를 호출하는 
//	메소드
	public void insert(BoardVO vo, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		System.out.println("MvcboardService 클래스의 insert() 메소드");
		SqlSession mapper = MySession.getSession();

//		insert.jsp에서 입력한 request 객체에 저장되서 넘어온 데이터를 받아서 BoardVO 클래스 객체에 저장한다.

		// String filename = multi.getOriginalFileName("fileupload");
		// BoardVO insert = new BoardVO(boardTitle, boardContent, boardCategory,
		// filename);
//		DAO 클래스의 insert.jsp에서 입력한 데이터를 테이블에 저장하는 insert sql 명령을 실행하는 메소드를
//		호출한다.

		dao.insert(mapper, vo);
		// request.setAttribute("boardCategory", vo.getBoardCategory());
		System.out.println("글작성카테고리: " + vo.getBoardCategory());
		mapper.commit();
		mapper.close();
	}

	public void getBoardList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("내가 작성한 boardList");
		SqlSession mapper = MySession.getSession();
		String category = null;
		if (request.getParameter("category") != null) {
			category = request.getParameter("category");
		} else {
			category = (String) request.getAttribute("category");
		}
		// mapper.commit();
		ArrayList<BoardVO> boardlist = dao.selectBoardList(mapper, category);
		request.setAttribute("category", category);
		request.setAttribute("boardlist", boardlist);
		System.out.println(category);
		System.out.println(boardlist);
		mapper.commit();
		mapper.close();

	}

	/*
	 * 컨트롤러에 list.nhn 이라는 요청이 들어오면 컨트롤러에서 호출하는 메소드로 mapper를 얻어온 후 DAO 클래스의 // 브라우저에
	 * 출력할 1페이지 분량의 글 목록과 페이지 작업에 사용할 8개의 변수가 저장된 클래스 객체를 만들어 // request 영역에 저장하는
	 * 메소드 public void selectList2(HttpServletRequest request, HttpServletResponse
	 * response) { System.out.println("MvcboardService 클래스의 selectList() 메소드");
	 * SqlSession mapper = MySession.getSession();
	 * 
	 * // list.nhn이 요청될 때 넘어오는 브라우저에 표시할 페이지 번호를 받는다. // 브라우저에 표시할 페이지 번호가 정상적으로
	 * 넘어왔으면 넘어온 페이지 번호로, 정상적으로 넘어오지 않았다면 // 1로 브라우저에 표시할 페이지 번호를 설정한다. int
	 * currentPage = 1; try { currentPage =
	 * Integer.parseInt(request.getParameter("currentPage")); } catch (Exception e)
	 * { }
	 * 
	 * // 1페이지에 표시할 글의 개수를 정한다. int pageSize = 10; // 테이블에 저장된 전체 글의 개수를 얻어온다. int
	 * totalCount = dao.selectCount(mapper); // System.out.println(totalCount);
	 * 
	 * // 1페이지 분량의 글과 페이지 작업에 사용되는 8개의 변수를 초기화시키는 클래스(MvcboardList)의 객체를 // 만든다.
	 * MvcboardList mvcboardList = new MvcboardList(pageSize, totalCount,
	 * currentPage);
	 * 
	 * // 1페이지 분량의 시작, 끝 인덱스를 기억하는 HashMap 객체를 만들고 초기화시킨다. HashMap<String, Integer>
	 * hmap = new HashMap<String, Integer>(); hmap.put("startNo",
	 * mvcboardList.getStartNo()); hmap.put("endNo", mvcboardList.getEndNo());
	 * 
	 * /* 1페이지 분량의 글 목록을 얻어와서 mvcboardList의 ArrayList에 넣어준다.
	 * mvcboardList.setList(dao.selectList(mapper, hmap)); // for (BoardVO vo :
	 * mvcboardList.getList()) { // System.out.println(vo); // }
	 * 
	 * 
	 * // MvcboardList 클래스 객체를 request 영역에 저장한다.
	 * request.setAttribute("mvcboardList",mvcboardList);
	 * 
	 * mapper.close();
	 * 
	 * }
	 */

//	컨트롤러에 increment.nhn으로 요청이 들어오면 컨트롤러에서 호출하는 메소드로 조회수를 증가시킬 글번호를
//	넘겨받고 mapper를 얻어온 후 DAO 클래스의 조회수를 증가시키는 메소드를 호출하는 메소드
	public void increment(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스의 increment() 메소드");
		SqlSession mapper = MySession.getSession();

//		request 객체로 넘어오는 조회수를 증가시킬 글번호를 받는다.
		int boardID = Integer.parseInt(request.getParameter("boardID"));
//		조회수를 증가시키는 메소드를 호출한다.
		dao.increment(mapper, boardID);
		BoardVO vo = dao.getBoardVO(mapper, boardID);
		request.setAttribute("vo", vo);
		mapper.commit();
		mapper.close();
	}

	public void getBoardVO(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스의 update() 메서드");
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		BoardVO vo = dao.getBoardVO(mapper, boardID);
		request.setAttribute("vo", vo);
		request.setAttribute("boardID", boardID);
		System.out.println(boardID);
		mapper.commit();
		mapper.close();
	}

	public void update(BoardVO boardVO, HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
//		int boardID = Integer.parseInt(request.getParameter("boardID"));
//		System.out.println("update boardID: " + boardID);
		System.out.println("update vo: " + boardVO);
		dao.update(mapper, boardVO);
		request.setAttribute("boardID", boardVO.getBoardID());
		mapper.commit();
		mapper.close();
	}

	public void delete(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스의 delete() 메서드");
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		dao.delete(mapper, boardID);
		mapper.commit();
		mapper.close();
	}

	public void download(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		System.out.println("MvcboardService 클래스의 delete() 메서드");
		int boardID = Integer.parseInt(request.getParameter("boardID"));
//		down.actionDo(request, response);
		dao.download(mapper, boardID);
		request.setAttribute("boardID", boardID);

		mapper.commit();
		mapper.close();

	}

	public int heartCount(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		int result = dao.heartCount(mapper, boardID);

		mapper.commit();
		mapper.close();
		return result;
	}

	public int heartDelete(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		int result = dao.heartDelete(mapper, boardID);

		mapper.commit();
		mapper.close();
		return result;
	}
}
