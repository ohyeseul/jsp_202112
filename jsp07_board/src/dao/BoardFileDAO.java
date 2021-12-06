package dao;

import java.util.List;

import dto.BoardFile;

public interface BoardFileDAO {
	int insert(BoardFile boardfile);
	int update(BoardFile boardfile);
	int delete(int fnum);
	int delete_bnum(int bnum);
	BoardFile selectOne(int fnum);
	List<BoardFile> selectList(int bnum);
}
