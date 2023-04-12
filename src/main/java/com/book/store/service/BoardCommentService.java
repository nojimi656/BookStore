package com.book.store.service;

import java.util.List;

import com.book.store.dto.BoardCommentDTO;

public interface BoardCommentService {
	
	public int maxNum() throws Exception;
	
	public void insertData(BoardCommentDTO dto) throws Exception;
	
	public int getDataCount(int boardNum) throws Exception;
	
	public List<BoardCommentDTO> getLists(int start,int end,String searchKey,String searchValue) throws Exception;
	
	public void deleteData(int boardNum) throws Exception;
}
