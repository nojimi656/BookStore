package com.book.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.dto.BookDTO;
import com.book.mapper.BookItemMapper;
import com.book.util.MyUtil;

@Service
public class BookItemServiceImpl implements BookItemService{
	
	@Resource
	private BookItemMapper bookItemMapper;
	
	@Autowired
	private MyUtil myUtil;

	@Override
	public int maxNum() throws Exception {
		
		return bookItemMapper.maxNum();
	}

	@Override
	public void insertData(BookDTO dto) throws Exception {
		
		bookItemMapper.insertData(dto);
		
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		
		return bookItemMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<BookDTO> getLists(int start, int end, String searchKey, String searchValue) throws Exception {
		
		return bookItemMapper.getLists(start, end, searchKey, searchValue);
	}

	@Override
	public BookDTO getReadData(int SEQ_NO) throws Exception {
		
		return bookItemMapper.getReadData(SEQ_NO);
	}

	@Override
	public void updateSal_Count(int SEQ_NO) throws Exception {
		
		bookItemMapper.updateSal_Count(SEQ_NO);
	}

	@Override
	public void updateData(BookDTO dto) throws Exception {
		
		bookItemMapper.updateData(dto);
		
	}

	@Override
	public void deleteData(int SEQ_NO) throws Exception {
		
		bookItemMapper.deleteData(SEQ_NO);
		
	}

}
