package service;

import dao.BoardDAO;
import dao.BoardDAOImpl;
import dto.Board;

public class ReplyServiceImpl implements ReplySevice{
	private BoardDAO bdao = new BoardDAOImpl();
	
	@Override
	public String insert(Board board) {
		//댓글의 글순서와 글레벨 변경
		//1)부모의 글순서(restep)+1
		board.setRestep(board.getRestep()+1);
		//2)부모의 글레벨(relevel)+1
		board.setRelevel(board.getRelevel()+1);
		System.out.println("service: "+board);
		
		//3)기존댓글의 순서 +1
		bdao.restepplus(board);
		
		
		int cnt=bdao.insert(board);
		System.out.println(cnt+"건 댓글 추가");
		if(cnt>0) {
			return "추가 완료";
		}else
			return "추가 실패";
	}

	@Override
	public Board selectOne(int bnum) {
		return bdao.selectOnt(bnum);
		
	}

	@Override
	public String update(Board board) {
		int cnt=bdao.update(board);
		if(cnt>0)
			return "수정 성공";
		else
			return "수정 실패";
	}
	
	

}
