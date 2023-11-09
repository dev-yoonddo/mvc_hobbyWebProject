package com.together.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.together.vo.UserVO;

public class UserDAO {

	private static UserDAO instance = new UserDAO();

	private UserDAO() {
	}

	public static UserDAO getInstance() {
		return instance;
	}

//		MvcboardService 클래스에서 호출되는 mapper와 테이블에 저장할 메인글 정보가 저장된 객체를 넘겨받고 메인글을
//		테이블에 저장하는 mvcboard.xml 파일의 insert sql 명령을 실행하는 메소드
	public ArrayList<UserVO> getEmailList(SqlSession mapper) {
		System.out.println("UserDAO 클래스의 getEmailList() 메소드");
		return (ArrayList<UserVO>) mapper.selectList("getEmailList");
	}

	// 가져온 userID와 동일한 데이터가 있으면 1, 없으면 0을 반환한다.
	public int getUserID(SqlSession mapper, String userID) {
		int result = 0;
		if (mapper.selectOne("getUserID", userID) != null) {
			result = 1;
		}
		return result;
	}

	public void join(SqlSession mapper, UserVO vo) {
		System.out.println("UserDAO 클래스의 join() 메소드");
		System.out.println(vo);
		mapper.insert("join", vo);
	}

	public String login(SqlSession mapper, UserVO vo) {
		System.out.println("UserDAO 클래스의 login() 메소드");
		// session.setAttribute("userID", vo.getUserID());
		return (String) mapper.selectOne("login", vo);
	}

	public UserVO getUserVO(SqlSession mapper, String userID) {
		System.out.println("UserDAO 클래스의 getUserVO() 메소드");
		return (UserVO) mapper.selectOne("getUserVO", userID);

	}

	public void userUpdate(SqlSession mapper, UserVO vo) {
		System.out.println("UserDAO 클래스의 update() 메소드");
		mapper.update("userUpdate", vo);
	}
}
