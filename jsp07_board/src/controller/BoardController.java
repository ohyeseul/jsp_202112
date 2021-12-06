package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dto.Board;
import dto.Page;
import service.BoardService;
import service.BoardServiceImpl;


@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private BoardService bservice = new BoardServiceImpl();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String uri =request.getRequestURI();
		System.out.println(uri);
		//context path구하기
		String path=request.getContextPath();
		
		if(uri.contains("list")) {
			String findkey =request.getParameter("findkey");
			String findvalue =request.getParameter("findvalue");
			String curpage_s = request.getParameter("curpage");
			int curpage=1; //디폴트 페이지
			if (curpage_s != null) //현재 페이지를 넘겼다면
				curpage =Integer.parseInt(curpage_s);
			
			Page page = new Page();
			page.setFindkey(findkey);
			page.setFindvalue(findvalue);
			page.setCurpage(curpage);
			System.out.println("controller:" + page);
			
			List<Board> blist= bservice.selectList(page);
			System.out.println(blist);
			//forward방식,list.jsp
			request.setAttribute("blist",blist);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/views/board/list.jsp")
							.forward(request, response);
		}else if(uri.contains("add")) {
			//등록
			String saveDirectory=getServletContext().getInitParameter("savedir");
			int size = 1024 *1024 * 10; //사진사이즈 10m byte                파일이름 안겹치게 :new DefaultFileRenamePolicy()
			//객체를 생성과 동시에 파일을 saveDirectory에 저장
			MultipartRequest multi= new MultipartRequest(request, saveDirectory, size,"utf-8",new DefaultFileRenamePolicy());
			
			//db에 insert할 정보들
			String email = multi.getParameter("email");
			String subject=multi.getParameter("subject");
			String content=multi.getParameter("content");
			
			//객체생성 (객체:메모리상 독립적인 공간)
			Board board = new Board();
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp(request.getRemoteAddr()); //요청ip주소
			System.out.println(board);
			
			/* 파일처리를 이렇게말고*/
//			String filename1 =multi.getFilesystemName("file1");
//			String filename2 =multi.getFilesystemName("file2");
//			String filename3 =multi.getFilesystemName("file3");
//			System.out.println("파일이름1:"+filename1);
//			System.out.println("파일이름2:"+filename2);
//			System.out.println("파일이름3:"+filename3); 
//			String[] filenames = new String[3];
//			filenames[0] = filename1;
//			filenames[1] = filename2;
//			filenames[2] = filename3;
//			System.out.println(Arrays.toString(filenames));
			
			/* 신규 파일이름 처리       이렇게 해야됨 반복문을 이용해서 */
			List<String> filenames = new ArrayList<>();
			//파일의 이름의 모음
			Enumeration<String> files=multi.getFileNames();
			while(files.hasMoreElements()) { //다음자료가 있으면 true
				String name=files.nextElement();
				System.out.println(name);
				//실제로 저장된 파일이름
				String filename=multi.getFilesystemName(name);
				if(filename !=null) filenames.add(filename);
			}
			
			System.out.println(filenames);
			
			String msg=bservice.insert(board,filenames);
			//redirect방식, /board/list 호출
			response.sendRedirect(path+"/board/list?msg="+URLEncoder.encode(msg,"utf-8"));
			
		}else if(uri.contains("detail")) {
			//상세조회
			int bnum =Integer.parseInt(request.getParameter("bnum"));
			System.out.println(bnum);
			//조회수 +1
			String cntplus=request.getParameter("cntplus");
			System.out.println("cntplus: "+cntplus);
			if(cntplus != null) {
				bservice.cntplus(bnum);
			}
			//게시물조회 + 파일 조회 + 댓글조회
			Map<String, Object> map = bservice.selectOne(bnum);
			
			
			//forward방식, detail.jsp이동
			request.setAttribute("map", map);
			request.getRequestDispatcher("/views/board/detail.jsp")
							.forward(request, response);
			
		}else if(uri.contains("remove")) {
			//삭제
			int bnum =Integer.parseInt(request.getParameter("bnum"));
			String msg=bservice.delete(bnum);
			System.out.println(msg);
			//리스트로 이동
			response.sendRedirect(path+"/board/list?msg="+URLEncoder.encode(msg,"utf-8"));
		}else if(uri.contains("modiform")) {
			//수정폼으로 이동
			int bnum =Integer.parseInt(request.getParameter("bnum"));
			Map<String, Object> map=bservice.selectOne(bnum);
			request.setAttribute("map", map);
			request.getRequestDispatcher("/views/board/modify.jsp")
							.forward(request, response);
		}else if(uri.contains("modify")) {
			//수정
			
			String saveDirectory=getServletContext().getInitParameter("savedir");
			int size = 1024 *1024 * 10; //사진사이즈 10m byte                파일이름 안겹치게 :new DefaultFileRenamePolicy()
			//객체를 생성과 동시에 파일을 saveDirectory에 저장
			MultipartRequest multi= new MultipartRequest(request, saveDirectory, size,"utf-8",new DefaultFileRenamePolicy());
			
			//db에 insert할 정보들
			int bnum=Integer.parseInt(multi.getParameter("bnum"));
			String email = multi.getParameter("email");
			String subject=multi.getParameter("subject");
			String content=multi.getParameter("content");
			Board board = new Board();
			board.setBnum(bnum);
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp(request.getRemoteAddr());
			System.out.println("수정할 board:"+ board);
			
			//checkbox는 배열로
			String[] filedels= multi.getParameterValues("filedel");
			System.out.println("기존파일 삭제리스트:"+Arrays.toString(filedels));

			
			/* 신규 파일이름 처리       이렇게 해야됨 반복문을 이용해서 */
			List<String> filenames = new ArrayList<>();
			//파일의 이름의 모음
			Enumeration<String> files=multi.getFileNames();
			while(files.hasMoreElements()) { //다음자료가 있으면 true
				String name=files.nextElement();
				//System.out.println(name);
				//실제로 저장된 파일이름
				String filename=multi.getFilesystemName(name);
				if(filename !=null) filenames.add(filename);
			}
			System.out.println("신규파일 추가 리스트:"+filenames);
			
			//서비스에 update요청
			String msg=bservice.update(board,filedels,filenames);
			
			response.sendRedirect(path+"/board/detail?bnum="+bnum+"&msg="+URLEncoder.encode(msg,"utf-8"));
			
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
