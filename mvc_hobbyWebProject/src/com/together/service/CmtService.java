package com.together.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.together.dao.CommentDAO;
import com.together.session.MySession;
import com.together.vo.CommentVO;

public class CmtService {
	private static CmtService instance = new CmtService();

	private CmtService() {
	}

	public static CmtService getInstance() {
		return instance;
	}

	private CommentDAO cmtdao = CommentDAO.getInstance();

	public int regist(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("CmtService 클래스의 regist() 메소드");
		SqlSession mapper = MySession.getSession();
		String userID = request.getParameter("userID");
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		String cmtContent = request.getParameter("content");
		CommentVO vo = new CommentVO(0, boardID, userID, cmtContent);

		int result = cmtdao.regist(mapper, vo);

		mapper.commit();
		mapper.close();
		return result;
	}

	public void getCmtList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("CmtService 클래스의 getCmtList() 메소드");
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		// mapper.commit();
		ArrayList<CommentVO> cmtlist = cmtdao.selectCmtList(mapper, boardID);
		request.setAttribute("boardID", boardID);
		request.setAttribute("cmtlist", cmtlist);
		System.out.println(boardID);
		System.out.println(cmtlist);
		mapper.close();
	}

	public void getCmtVO(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("CmtService 클래스의 getCmtVO() 메소드");
		SqlSession mapper = MySession.getSession();
		int cmtID = Integer.parseInt(request.getParameter("cmtID"));
		CommentVO vo = cmtdao.getCmtVO(mapper, cmtID);
		request.setAttribute("vo", vo);
		mapper.close();
	}

	// 게시글의 댓글 갯수 가져오기
	public void selectCount(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		int count = cmtdao.selectCount(mapper, boardID);
		request.setAttribute("count", count);
		mapper.close();

	}

	// 삭제하기
	public int cmtDelete(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스의 delete() 메서드");
		SqlSession mapper = MySession.getSession();
		int cmtID = Integer.parseInt(request.getParameter("cmtID"));
		int result = cmtdao.cmtDelete(mapper, cmtID);
		mapper.commit();
		mapper.close();
		return result;
	}
}
