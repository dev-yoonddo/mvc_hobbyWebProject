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
		mapper.commit();
		mapper.close();

	}

	// 회원가입
	public int join(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("UserService 클래스의 join() 메소드");
		SqlSession mapper = MySession.getSession();
		UserVO vo = new UserVO();
		// 폼에 입력된 값 가져오기
		String userID = request.getParameter("userID");
		String userName = request.getParameter("userName");
		String userEmail = request.getParameter("userEmail");
		String userBirth = request.getParameter("userBirth");
		String userPhone = request.getParameter("userPhone");
		String userPassword = request.getParameter("userPassword");
		vo = new UserVO(userID, userName, userEmail, userBirth, userPhone, userPassword);
		int exist = userdao.getUserID(mapper, userID);
		if (exist == 0) {
			userdao.join(mapper, vo);
		} else {
			ArrayList<UserVO> emailList = userdao.getEmailList(mapper);
			request.setAttribute("emailList", emailList);
			request.setAttribute("vo", vo);
		}
		mapper.commit();
		mapper.close();
		return exist;
	}

	// 아이디 중복 체크
	public int checkID(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		System.out.println("UserService 클래스의 checkID() 메소드");
		SqlSession mapper = MySession.getSession();
		String userID = request.getParameter("userID");
		return userdao.getUserID(mapper, userID);
	}

	// 로그인
	public int login(HttpServletRequest request, HttpServletResponse response) {
		SqlSession mapper = MySession.getSession();
		HttpSession session = request.getSession(true);
		int result = 0;
		String userID = request.getParameter("userID");
		String userPassword = request.getParameter("userPassword");
		UserVO vo = new UserVO(userID, userPassword);
		String userName = userdao.login(mapper, vo);
		// userName이 정상적으로 리턴되면 session에 userID값을 넣어 로그인해준다.
		if (userName != null) {
			request.setAttribute("userName", userName);
			session.setAttribute("userID", userID);
			session.setMaxInactiveInterval(1800);
			result = 1;
		}
		System.out.println("로그인 유저: " + userID);

		mapper.commit();
		mapper.close();
		return result;
	}

	// 로그아웃
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("로그아웃");
		HttpSession session = request.getSession(false);
		session.invalidate();

	}

	// 회원 정보 가져오기
	public void getUserVO(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("회원 정보 가져오기");
		SqlSession mapper = MySession.getSession();
		HttpSession session = request.getSession(true);
		ArrayList<UserVO> emailList = userdao.getEmailList(mapper);
		// session에 로그인 된 userID의 정보만 가져올 수 있다.
		String userID = (String) session.getAttribute("userID");
		UserVO vo = userdao.getUserVO(mapper, userID);
		request.setAttribute("vo", vo);
		request.setAttribute("emailList", emailList);
		mapper.commit();
		mapper.close();
	}

	// 회원 정보 업데이트
	public void userUpdate(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("회원 정보 업데이트");
		SqlSession mapper = MySession.getSession();
		HttpSession session = request.getSession(true);

		// 폼에 입력된 값 가져오기
		String userID = (String) session.getAttribute("userID");
		String userName = request.getParameter("userName");
		String userEmail = request.getParameter("userEmail");
		String userBirth = request.getParameter("userBirth");
		String userPhone = request.getParameter("userPhone");
		String userPassword = request.getParameter("userPassword");
		UserVO vo = new UserVO(userID, userName, userEmail, userBirth, userPhone, userPassword);
		System.out.println(userID);
		System.out.println(userName);
		userdao.userUpdate(mapper, vo);
		UserVO vo2 = userdao.getUserVO(mapper, userID);
		request.setAttribute("vo", vo2);
		mapper.commit();
		mapper.close();

	}
}
