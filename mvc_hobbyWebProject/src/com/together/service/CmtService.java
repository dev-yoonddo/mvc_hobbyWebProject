package com.together.service;

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
		int cmtID = Integer.parseInt(request.getParameter("cmtID"));
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		String userID = request.getParameter("userID");
		String cmtContent = request.getParameter("cmtContent");
		CommentVO vo = new CommentVO(cmtID, boardID, userID, cmtContent);
		cmtdao.regist(mapper, vo);
	}
}
