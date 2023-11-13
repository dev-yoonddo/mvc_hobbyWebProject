package com.together.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

import com.together.dao.HeartDAO;
import com.together.session.MySession;
import com.together.vo.HeartVO;

public class HeartService {
	private static HeartService instance = new HeartService();

	private HeartService() {
	}

	public static HeartService getInstance() {
		return instance;
	}

	private HeartDAO htdao = HeartDAO.getInstance();

	public int checkHeart(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		HttpSession session = request.getSession(true);
		int exist = 0;
		String userID = (String) session.getAttribute("userID");
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		HeartVO vo = new HeartVO(userID, boardID);
		if (userID != null) {
			exist = htdao.checkHeart(mapper, vo);
			if (exist == 1) {
				request.setAttribute("exist", "Y");
			} else {
				request.setAttribute("exist", "N");
			}
		}
		return exist;
	}

	public int heart(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("HeartService 클래스의 heart() 메소드");
		SqlSession mapper = MySession.getSession();
		String userID = request.getParameter("userID");
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		String value = request.getParameter("value");
		System.out.println(value);
		HeartVO vo = new HeartVO(userID, boardID);
		int result = 0;
		if (value.equals("none")) {
			htdao.heart(mapper, vo);
			result = 1;
		} else {
			htdao.deleteHeart(mapper, vo);
			result = 2;
		}
		mapper.commit();
		mapper.close();
		return result;
	}

	public int deleteHeart(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("HeartService 클래스의 deleteHeart() 메소드");
		SqlSession mapper = MySession.getSession();
		String userID = request.getParameter("userID");
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		HeartVO vo = new HeartVO(userID, boardID);
		int result = htdao.deleteHeart(mapper, vo);
		return result;
	}

	// 게시글의 댓글 갯수 가져오기
	public void selectCount(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		int boardID = Integer.parseInt(request.getParameter("boardID"));
		int count = htdao.selectCount(mapper, boardID);
		request.setAttribute("count", count);
		mapper.close();

	}
}
