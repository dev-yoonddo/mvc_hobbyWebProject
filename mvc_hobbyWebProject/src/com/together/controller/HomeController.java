package com.together.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.together.service.BoardService;
import com.together.service.FileUploadService;
import com.together.service.UserService;
import com.together.vo.BoardVO;

@WebServlet(urlPatterns = { "/mainPage", "/join", "/joinAction", "/login", "/loginAction", "/logout", "/community",
		"/searchPage", "/view", "/write", "/writeAction", "/boardAction", "/updateOK" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

//	컨트롤러에서 사용할 service 클래스 객체를 생성한다.
	private BoardService bdservice = BoardService.getInstance();
	private UserService urservice = UserService.getInstance();

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

//		요청을 받는다.
		String url = request.getRequestURI();
		String contextPath = request.getContextPath();
		String context = url.substring(contextPath.length());
//		System.out.println(context);
//		요청받은 페이지 경로를 만든다.
		String viewPage = "/WEB-INF/";
		String paramName = null;
		String param = null;
		String redirect = null;
		switch (context) {
		case "/mainPage":
			redirect = "mainPage";
			break;
		case "/join":
			urservice.getEmailList(request, response);
			redirect = "join";
			break;
		case "/joinAction":
			urservice.join(request, response);
			redirect = "login";
			break;
		case "/login":
			redirect = "login";
			break;
		case "/loginAction":
			urservice.login(request, response);
			redirect = "community";
			break;
		case "/logout":
			urservice.logout(request, response);
			redirect = "mainPage";
			break;
		case "/community":
			redirect = "community";
			break;
		case "/searchPage":

//				브라우저에 출력할 1페이지 분량의 글과 페이지 작업에 사용할 8개의 변수가 저장된 클래스 객체를
//				얻어오는 메소드를 호출한다.
			bdservice.getBoardList(request, response);

			redirect = "searchPage";
			break;
		/*
		 * case "/increment.nhn": // 조회수를 증가시키는 메소드를 호출한다. service.increment(request,
		 * response);
		 * 
		 * viewPage += "increment"; break;
		 */
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
//			request.setAttribute("category", vo.getBoardCategory());
//			service.selectAllList(request, response);
//			viewPage = "searchPage";
//			viewMain++;
			redirect = "searchPage";
			paramName = "category";
			param = vo.getBoardCategory();
			break;
		case "/view":
//			조회수를 증가시키는 메소드를 호출한다.
			bdservice.increment(request, response);
//			조회수를 증가시킨 글 1건을 얻어오는 메소드를 호출한다.
			bdservice.getBoardVO(request, response);

			redirect = "view";
			break;
		case "/boardAction":
			String action = request.getParameter("action");
			if (action.equals("update")) {
				System.out.println("업데이트");
				bdservice.getBoardVO(request, response);
				redirect = "update";
			} else {
				System.out.println("삭제");
				bdservice.delete(request, response);
				redirect = "searchPage";
			}
			break;

		case "/updateOK":
			// System.out.println("update success: " + request.getParameter("boardID"));
			BoardVO updateVO = upload.fileupload(request, response);
			System.out.println(updateVO);
			bdservice.update(updateVO, request, response);
			// service.getBoardVO(request, response);
			redirect = "view";
			paramName = "boardID";
			param = Integer.toString(updateVO.getBoardID());
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