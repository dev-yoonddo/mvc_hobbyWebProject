package com.together.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorMessage {
	private static ErrorMessage instance = new ErrorMessage();

	private ErrorMessage() {
	}

	public static ErrorMessage getInstance() {
		return instance;
	}

	public void errorTest(String error, HttpServletRequest request, HttpServletResponse response) {
		String fail = "fail";
		String sc = "success";
		if (error == "loginN") {
			System.out.println("로그인 실패");
			request.setAttribute("loginError", fail);
		} else if (error == "loginY") {
			System.out.println("로그인 성공");
			request.setAttribute("loginError", sc);
		} else if (error == "joinN") {
			System.out.println("회원가입 실패");
			request.setAttribute("joinError", fail);
		} else if (error == "joinY") {
			System.out.println("회원가입 성공");
			request.setAttribute("joinError", sc);
		}
	}
}
