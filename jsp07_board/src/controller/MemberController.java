package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dto.Member;
import service.MemberService;
import service.MemberServiceImpl;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService mservice = new MemberServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String uri=request.getRequestURI();
		System.out.println(uri);
		//contextpath구하기
		String path = request.getContextPath();
		if(uri.contains("add")) {
			//회원등록
			//saveDirectory : 파일을 저장할 경로(서버)
			//"C:\\oys\\savedir" (windows 표기방식)와 같음
			
			//String saveDirectory ="C:\\oys\\savedir";
			//web.xml에서 저장경로 읽기
			String saveDirectory =getServletContext().getInitParameter("savedir");
			
			int size = 1024 * 1024 * 10 ; //10m(메가바이트) : 업로드파일사이즈 제한
			//new DefaultFileRenamePolicy() : 같은 이름의 파일이 있을때 파일이름 변경
			MultipartRequest multi=new MultipartRequest(request, saveDirectory,size, "utf-8",new DefaultFileRenamePolicy()); 
			//MultipartRequest 객체를 이용해서 데이터 가져옴
			String email=multi.getParameter("email");
			String passwd=multi.getParameter("passwd");
			String zipcode=multi.getParameter("zipcode");
			String addr=multi.getParameter("addr");
			String addrdetail=multi.getParameter("addrdetail");
			//실제저장된 파일이름가져오기
			String filename=multi.getFilesystemName("file");
			if(filename==null) filename=""; //파일이 없을경우
			//객체 생성
			Member member = new Member();
			member.setEmail(email);
			member.setPasswd(passwd);
			member.setZipcode(zipcode);
			member.setAddr(addr);
			member.setAddrdetail(addrdetail);
			member.setFilename(filename);
			System.out.println(member);
			//저장
			String msg=mservice.insert(member);
			System.out.println(msg);
			
			//response.sendRedirect(path+"/views/home.jsp?msg="+ URLEncoder.encode(msg,"utf-8"));
			
			
			//자동로그인처리
			response.sendRedirect(path + "/member/login?email="+email+"&passwd="+passwd);
			
		}else if(uri.contains("login")) {
			//로그인
			String email = request.getParameter("email");
			String passwd= request.getParameter("passwd");
			System.out.println(email);
			System.out.println(passwd);
			Map<String, String> map=mservice.login(email, passwd);
			//rcode가 0이면 home이동
			//rcode가 0이 아니면 login.jsp이동
			String rcode = map.get("rcode");
			String msg = map.get("msg");
			if(rcode.equals("0")) { //로그인성공
				//세션에 이메일 넣기
				HttpSession session = request.getSession();
				session.setAttribute("email", email);
				session.setMaxInactiveInterval(60*60*3); //3시간
				//쿠기에 이메일 저장
				String idsave=request.getParameter("idsave");
				Cookie email_cookie = new Cookie("email",email);
				email_cookie.setPath(path);
				if(idsave==null) {
					email_cookie.setMaxAge(0); //쿠키삭제
				}
				response.addCookie(email_cookie);
				
				response.sendRedirect(path+"/views/home.jsp?msg="+URLEncoder.encode(msg,"utf-8"));
			}else {
				response.sendRedirect(path+"/views/member/login.jsp?msg="+URLEncoder.encode(msg,"utf-8"));
			}
			
		}else if(uri.contains("logout")) {
			HttpSession session = request.getSession();
			session.invalidate(); //모든 세션변수 삭제
			String msg="로그아웃 되었습니다";
			response.sendRedirect(path+"/views/home.jsp?msg="+URLEncoder.encode(msg,"utf-8"));
		}else if(uri.contains("myinfo")) {
			//내정보
			//이메일을 세션에서 가져오기
			HttpSession session = request.getSession();
			           //다운캐스팅
			String email=(String)session.getAttribute("email");
			Member member=mservice.selectOne(email);
			System.out.println(member);
			//forward방식 myinfo.jsp이동
			request.setAttribute("member", member);
			request.getRequestDispatcher("/views/member/myinfo.jsp").forward(request, response);
		}else if(uri.contains("modify")) {
			//수정
			//saveDirectory : 파일을 저장할 경로(서버)
			//"C:\\oys\\savedir" (windows 표기방식)와 같음
			//String saveDirectory ="C:\\oys\\savedir";
			//web.xml에서 저장경로 읽기
			String saveDirectory =getServletContext().getInitParameter("savedir");
			int size = 1024 * 1024 * 10 ; //10m(메가바이트) : 업로드파일사이즈 제한
			//new DefaultFileRenamePolicy() : 같은 이름의 파일이 있을때 파일이름 변경
			MultipartRequest multi=new MultipartRequest(request, saveDirectory,size, "utf-8",new DefaultFileRenamePolicy()); 
			//MultipartRequest 객체를 이용해서 데이터 가져옴
			String email=multi.getParameter("email");
			String passwd=multi.getParameter("passwd");
			String changepw=multi.getParameter("changepw");
			String zipcode=multi.getParameter("zipcode");
			String addr=multi.getParameter("addr");
			String addrdetail=multi.getParameter("addrdetail");
			String filename=multi.getParameter("filename");//파일이름
			String filedel=multi.getParameter("filedel");// 이미지파일 삭제 여부
			System.out.println(filedel);
			//실제저장된 파일이름가져오기
			String newfilename=multi.getFilesystemName("file"); //파일
			//파일을 변경하겠다는 의미
			if(newfilename!=null) //파일이 없을경우
				filename= newfilename;  
			else if(filedel != null) //삭제체크가 되어있다면 
				filename="";
			//객체 생성
			Member member = new Member();
			member.setEmail(email);
			member.setPasswd(passwd);
			member.setZipcode(zipcode);
			member.setAddr(addr);
			member.setAddrdetail(addrdetail);
			member.setFilename(filename);
			System.out.println(member);
			
			String msg=mservice.update(member, changepw);
			
			//내정보 서블릿 호출
			response.sendRedirect(path + "/member/myinfo?msg="+ URLEncoder.encode(msg, "utf-8"));
			
		} 
		/*	fileController에 filedown가 있기때문에 없애준거임!!!!!!!!
			 * else if(uri.contains("filedown")) { //파일 다운로드 //디렉토리, 파일이름 //String
			 * saveDirectory ="C:/oys/savedir"; //web.xml에서 저장경로 읽기 String saveDirectory
			 * =getServletContext().getInitParameter("savedir"); String filename =
			 * request.getParameter("filename");
			 * 
			 * //마임타입: 파일의 종류 String mimeType=getServletContext().getMimeType(filename);
			 * if(mimeType==null) { mimeType ="application/octet-stream;charset=utf-8"; }
			 * response.setContentType(mimeType);
			 * 
			 * //첨부파일로 파일을 보낼때 //한글파일이름 깨지지 않게 utf-8로 인코딩
			 * response.setHeader("Content-Disposition",
			 * "attachment;filename="+URLEncoder.encode(filename,"utf-8"));
			 * 
			 * //읽어올 파일 경로명 String fileurl = saveDirectory +"/" + filename;
			 * System.out.println(fileurl);
			 * 
			 * //입력스트림 FileInputStream fis=new FileInputStream(fileurl); //출력스트림
			 * ServletOutputStream outs = response.getOutputStream();
			 * 
			 * //할번에 읽어들일 바이트 배열 byte[] b = new byte[4096];//4kbyte 크기의 byte배열 int numRead =
			 * 0; //읽어들은 바이트 수(-1이면 파일의 끝) while ((numRead=fis.read(b, 0, b.length)) != -1)
			 * { outs.write(b,0,numRead); //읽어들인 바이트수만큼 out } outs.flush(); //내보내기,버퍼를 비우기
			 * outs.close(); fis.close(); }
			 */
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
