package com.together.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

import com.together.dao.UserDAO;
import com.together.session.MySession;
import com.together.vo.UserVO;

public class UserService {
	private static UserService instance = new UserService();

	private UserService() {
	}

	public static UserService getInstance() {
		return instance;
	}

	private UserDAO userdao = UserDAO.getInstance();

	// 회원들 이메일 가져오기
	public void getEmailList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("UserService 클래스의 getEmailList() 메소드");
		SqlSession mapper = MySession.getSession();
		ArrayList<UserVO> emailList = userdao.getEmailList(mapper);
		System.out.println(emailList);
		request.setAttribute("emailList", emailList);
		mapper.close();

	}

	// 회원가입
	public void join(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("UserService 클래스의 join() 메소드");
		SqlSession mapper = MySession.getSession();
		// 폼에 입력된 값 가져오기
		String userID = request.getParameter("userID");
		String userName = request.getParameter("userName");
		String userEmail = request.getParameter("userEmail");
		String userBirth = request.getParameter("userBirth");
		String userPhone = request.getParameter("userPhone");
		String userPassword = request.getParameter("userPassword");
		UserVO vo = new UserVO(userID, userName, userEmail, userBirth, userPhone, userPassword);
		userdao.join(mapper, vo);
		mapper.commit();
		mapper.close();
	}

	// 로그인
	public void login(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		HttpSession session = request.getSession(true);

		String userID = request.getParameter("userID");
		String userPassword = request.getParameter("userPassword");
		UserVO vo = new UserVO(userID, userPassword);
		String userName = userdao.login(mapper, vo);
		// userName이 정상적으로 리턴되면 session에 userID값을 넣어 로그인해준다.
		if (userName != null) {
			request.setAttribute("userName", userName);
			session.setAttribute("userID", userID);
			session.setMaxInactiveInterval(1800);

		}
		System.out.println("로그인 유저: " + userID);

		mapper.commit();
		mapper.close();
	}

	// 로그아웃
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("로그아웃");
		HttpSession session = request.getSession(false);
		session.invalidate();

	}
}
