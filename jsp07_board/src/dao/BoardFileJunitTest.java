package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import dto.BoardFile;

class BoardFileJunitTest {
	BoardFileDAO bfdao = new BoardFileDAOImpl();
	@Test
	void testMBConn() {
		SqlSession session = MBConn.getSession();
		System.out.println(session); 
	}
	
	@Test
	void testinsert() {
		BoardFile boardFile = new BoardFile();
		boardFile.setBnum(1);
		boardFile.setFilename("a.png");
		int cnt = bfdao.insert(boardFile);
		System.out.println(cnt+"건 추가");
	}
	
	@Test
	void testupdate() {
		BoardFile boardFile = new BoardFile();
		boardFile.setFnum(3);
		boardFile.setFilename("c.png");
		int cnt = bfdao.update(boardFile);
		System.out.println(cnt+"건 수정");
	}
	
	@Test
	void testdelete() {
		int cnt =bfdao.delete(2);
		System.out.println(cnt+"건 삭제");
	}

	@Test
	void testselectOne() {
		BoardFile boardFile =  bfdao.selectOne(3);
		System.out.println(boardFile);
	}
	
	@Test
	void testselectList() {
		List<BoardFile> bflist = bfdao.selectList(1);
		System.out.println(bflist);
		
	}
}
