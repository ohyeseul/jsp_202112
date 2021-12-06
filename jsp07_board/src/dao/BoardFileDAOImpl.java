package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dto.BoardFile;

public class BoardFileDAOImpl implements BoardFileDAO{

	@Override
	public int insert(BoardFile boardfile) {
		try(SqlSession session = MBConn.getSession()){
			return session.insert("BoardFileMapper.insert", boardfile);
		}
	}

	@Override
	public int update(BoardFile boardfile) {
		try(SqlSession session = MBConn.getSession()){
			return session.update("BoardFileMapper.update", boardfile);
		}
	}
	//fnum를 기준으로 삭제(한건삭제)
	@Override
	public int delete(int fnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.delete("BoardFileMapper.delete", fnum);
		}
	}
	//bnum를 기준으로 삭제(여러건 삭제,부모+자식)
	@Override
	public int delete_bnum(int bnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.delete("BoardFileMapper.delete_bnum", bnum);
		}
	}

	@Override
	public BoardFile selectOne(int fnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.selectOne("BoardFileMapper.selectOne", fnum);
		}
	}


	@Override
	public List<BoardFile> selectList(int bnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.selectList("BoardFileMapper.selectList", bnum);
		}
	}

	

}
