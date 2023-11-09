package com.together.service;

import java.util.ArrayList;

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

	public void regist(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("CmtService 클래스의 regist() 메소드");
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		String userID = request.getParameter("userID");
		String cmtContent = request.getParameter("cmtContent");
		CommentVO vo = new CommentVO(0, boardID, userID, cmtContent);
		System.out.println(vo);
		cmtdao.regist(mapper, vo);
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
	}
}
