package com.together.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.together.service.BoardService;
import com.together.service.CmtService;
import com.together.service.ErrorMessage;
import com.together.service.FileUploadService;
import com.together.service.HeartService;
import com.together.service.UserService;
import com.together.vo.BoardVO;

@WebServlet(urlPatterns = { "/mainPage", "/join", "/joinAction", "/login", "/loginAction", "/logout", "/community",
		"/searchPage", "/view", "/view/heart", "/write", "/writeAction", "/update", "/updateAction", "/deleteAction",
		"/userUpdate", "/userUpdateAction", "/checkID", "/cmt", "/cmt/delete" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

//	컨트롤러에서 사용할 service 클래스 객체를 생성한다.
	private UserService urservice = UserService.getInstance();
	private BoardService bdservice = BoardService.getInstance();
	private CmtService cmtservice = CmtService.getInstance();
	private HeartService htservice = HeartService.getInstance();
	private ErrorMessage error = ErrorMessage.getInstance();

	public HomeController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("HomeController 클래스의 doGet() 메소드");
		actionDo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("HomeController 클래스의 doPost() 메소드");
		actionDo(request, response);
	}

	protected void actionDo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("HomeController 클래스의 actionDo() 메소드");
//		한글 깨짐 방지
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
//		System.out.println(request.getParameter("name"));
		FileUploadService upload = new FileUploadService();
		PrintWriter script = response.getWriter();

//		요청을 받는다.
		String url = request.getRequestURI();
		String contextPath = request.getContextPath();
		String context = url.substring(contextPath.length());

//		요청받은 페이지 경로를 만든다.
		String viewPage = "/WEB-INF/";
		String paramName = null;
		String param = null;
		String redirect = null;
		String err = "";

		switch (context) {
		case "/mainPage":
			redirect = "mainPage";
			break;
		case "/join":
			urservice.getEmailList(request, response);
			redirect = "join";
			break;
		case "/joinAction":
			int join = urservice.join(request, response);
			if (join == 0) {
				redirect = "login";
				err = "joinY";
			} else {
				redirect = "join";
				err = "joinN";
			}
			error.errorTest(err, request, response);
			break;
		case "/checkID":
			int result = urservice.checkID(request, response);

			// 사용자가 입력한 아이디와 동일한 아이디가 존재하면 1, 존재하지 않으면 0
			if (result == 0) {
				// ajax에 메시지를 전달한다.
				script.print("success");
			} else {
				script.print("fail");
			}
			script.flush();
			script.close();
			break;
		case "/login":
			redirect = "login";
			break;
		case "/loginAction":
			int login = urservice.login(request, response);
			// 로그인 실패 (login == 0) 는 login페이지로, 로그인 성공은 community 페이지로 이동
			if (login == 0) {
				redirect = "login";
				err = "loginN";
			} else {
				redirect = "community";
				err = "loginY";
			}
			error.errorTest(err, request, response);
			break;
		case "/logout":
			urservice.logout(request, response);
			redirect = "mainPage";
			break;
		case "/userUpdate":
			urservice.getUserVO(request, response);
			redirect = "userUpdate";
			break;
		case "/userUpdateAction":
			urservice.userUpdate(request, response);
			redirect = "userUpdate";
			break;
		case "/community":
			redirect = "community";
			break;
		case "/searchPage":
			bdservice.getBoardList(request, response);
			redirect = "searchPage";
			break;
		case "/write":
			redirect = "write";
			break;
		case "/writeAction":
			BoardVO vo = upload.fileupload(request, response);
//				insert.jsp에서 테이블에 저장할 데이터를 입력하고 submit 버튼을 클릭하면 폼에 입력한 정보가
//				컨트로롤러의 doPost() 메소드의 HttpServletRequest 인터페이스 타입의 객체인 request에 저장된다.
//				doPost() 메소드에서는 request 객체에 저장된 데이터를 가지고 actionDo() 메소드를 실행하므로
//				insert.jsp에서 폼에 입력한 데이터는 actionDo() 메소드의 request 객체에 저장된다.
//				insert.jsp에서 폼에 입력한 데이터를 테이블에 저장하는 메소드를 호출한다.
			bdservice.insert(vo, request, response);
			redirect = "searchPage";
			paramName = "category";
			param = vo.getBoardCategory();
			break;
		case "/view":
//			조회수를 증가시키고 글을 가져오는 메서드를 호출한다.
			bdservice.increment(request, response);
//			사용자가 해당 글에 하트를 눌렀는지 검사하는 메서드를 호출한다.
			htservice.checkHeart(request, response);
//			해당 글의 댓글리스트를 가져오는 메서드를 호출한다.
			cmtservice.getCmtList(request, response);
			redirect = "view";
			break;
		case "/view/heart":
//			사용자가 하트를 클릭하면 하트를 이미 눌렀는지, 누르지 않았는지 확인하고
//			이미 눌렀으면 취소, 누르지 않았으면 하트를 누르는 메서드를 호출한다.
			int heart = htservice.heart(request, response);
			int heartResult = 0;
			if (heart == 1) {
				heartResult = bdservice.heartCount(request, response);
				if (heartResult > 0) {
					script.print("success");
				}
			} else if (heart == 2) {
				heartResult = bdservice.heartDelete(request, response);
				if (heartResult > 0) {
					script.print("success");
				}
			}
			script.flush();
			script.close();
			break;
		case "/update":
			System.out.println("업데이트");
			bdservice.getBoardVO(request, response);
			redirect = "update";
			break;

		case "/updateAction":
			BoardVO vo2 = upload.fileupload(request, response);
			bdservice.update(vo2, request, response);
			// service.getBoardVO(request, response);
			redirect = "view";
			paramName = "boardID";
			param = Integer.toString(vo2.getBoardID());
			break;

		case "/deleteAction":
			bdservice.delete(request, response);
			redirect = "searchPage";
			paramName = "category";
			param = request.getParameter("category");
			break;

		case "/cmt":
			int cmt = cmtservice.regist(request, response);
			System.out.println(cmt);
			if (cmt == 1) {
				script.print("success");
			}
			script.flush();
			script.close();
			break;

		case "/cmt/delete":
			int cmtDel = cmtservice.cmtDelete(request, response);
			if (cmtDel == 1) {
				script.print("success");
			}
			script.flush();
			script.close();
			break;
		}

		// parameter값이 존재하면 sendRedirect로 파라미터에 해당하는 페이지로 이동한다.
		// sendRedirect는 url주소가 변경된다. (글 작성, 수정처럼 action 이후에 사용한다.)
//		if (viewMain > 0) {
		if (param != null) {
			response.sendRedirect(redirect + "?" + paramName + "=" + param);
			return;
		} else {
//			parameter값이 존재하지 않으면 RequestDispatcher로 해당 페이지로 이동한다.
			viewPage += (redirect + ".jsp");
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
//		} else {
//			viewPage += ".jsp";
//			if (param != null) {
//				viewPage += ("?" + paramName + "=" + param);
//			}
//			System.out.println(viewPage);
//			// 요청받은 페이지로 넘긴다.
//			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
//			dispatcher.forward(request, response);
//		}

	}

}
