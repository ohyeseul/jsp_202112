package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import dto.Board;
import dto.Page;

class BoardJunitTest {
	BoardDAO bdao = new BoardDAOImpl();
	
	@Test
	void testMBConn() {
		SqlSession session = MBConn.getSession();
		System.out.println(session); 
	}
	
	@Test
	void testinsert() {
		Board board = new Board();
		board.setEmail("oracle@email.com");
		board.setSubject("제목");
		board.setContent("내용");
		board.setIp("192.168.2.100");
		int cnt=bdao.insert(board);
		System.out.println(cnt+"건 추가");
	}
	
	@Test
	void testupdate() {
		Board board = new Board();
		board.setEmail("java1@email.com");
		board.setSubject("제목수정");
		board.setContent("내용수정");
		board.setIp("192.168.0.100");
		board.setBnum(2);
		int cnt=bdao.update(board);
		System.out.println(cnt+"건 수정");
	}
	
	@Test
	void testdelete() {
		int cnt=bdao.delete(3);
		System.out.println(cnt+"건 삭제");
	}

	@Test
	void testselectOnt() {
		Board board=bdao.selectOnt(1);
		System.out.println(board);
		//board가 null과 같지 않으면 성공, 둘중하나해도됨
		//assertNotEquals(null, board);
		assertNotNull(board);
	}
	
	@Test
	void testselectList() {
		Page page = new Page(); 
		page.setFindkey("email");
		page.setFindvalue("jsp");
		List<Board> blist = bdao.selectList(page);
		System.out.println(blist);
		
	}
}
