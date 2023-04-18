package com.book.store.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.book.store.dto.BoardCommentDTO;
import com.book.store.service.BoardCommentService;
import com.book.store.service.BoardCommentServiceImpl;
import com.book.store.util.BoardUtil;


@RestController
public class BoardCommentContoller {

	@Resource
	BoardCommentService boardCommentService = new BoardCommentServiceImpl();
	
	@Autowired
	BoardUtil myUtil;
	
	//@RequestMapping("/CommentCreated.action")
	@PostMapping("/CommentCreated.action")
	public ModelAndView commentCreated(BoardCommentDTO dto,HttpServletRequest request) throws Exception{
		
		int boardNum = Integer.parseInt(request.getParameter("boardNum"));
		
		int maxNum = boardCommentService.maxNum();
		
		dto.setCommentNum(maxNum+1);
		dto.setBoardNum(boardNum);
		
		boardCommentService.insertData(dto);
		
		ModelAndView mav = new ModelAndView();
		
		//mav.addObject("dto", dto);
		mav.setViewName("redirect:/CommentList.action");

		return mav;

	}
	
	@RequestMapping("/CommentList.action")
	public ModelAndView CommentList(BoardCommentDTO dto,HttpServletRequest request) throws Exception{
		
		//int boardNum = Integer.parseInt(request.getParameter("boardNum"));
		int boardNum = dto.getBoardNum();

		String pageNum = request.getParameter("pageNum");//문자만 따온건가?
		
		int currentPage = 1;
		int numPerPage = 3;
		
		if(pageNum!=null && !pageNum.equals("")) {
			currentPage = Integer.parseInt(pageNum);
			//1페이지가 아닌 get방식 주소로 받은 pageNum로 변경
		}
		
		int dataCount = boardCommentService.getDataCount(boardNum);
		//DataCount가 boardNum을 받았기 때문에  
		//일련번호는 각 boardNum마다 다르게 출력됨
		
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);

		if(currentPage>totalPage) {
			currentPage=totalPage;
		}
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;	
		
		List<BoardCommentDTO> lists = boardCommentService.getLists(start, end, boardNum);
		
		
		//String pageIndexList = myUtil.pageIndexList(currentPage, totalPage);
		//String Url = "/CommentList.action?boardNum=" + boardNum + "&pageNum=" + currentPage;
		
		String pageIndexList = myUtil.pageIndexList(currentPage, totalPage);
		
		//ModelAndView로 전송
		ModelAndView mav = new ModelAndView();// "jsonView"
		
		
		//포워딩할 데이터
		mav.addObject("lists", lists);
		mav.addObject("pageIndexList", pageIndexList);
		mav.addObject("boardNum", boardNum);
		mav.addObject("pageNum", currentPage);
		
		//mav.addObject("pageNum", currentPage);//3번째 방법시 같이넘겨야함
		//System.out.println(pageNum);
		
		
		mav.setViewName("boardCommentList");

		return mav;
		
	}
	
	@RequestMapping("/CommentDeleted.action")
	public ModelAndView CommentDeleted(BoardCommentDTO dto,HttpServletRequest request) throws Exception{
		
		int commentNum = Integer.parseInt(request.getParameter("commentNum"));
		
		int boardNum = dto.getBoardNum();
		
		boardCommentService.deleteData(commentNum);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("boardNum", boardNum);
		mav.setViewName("redirect:/CommentList.action");
		
		return mav;
		
	}
	
	@GetMapping("/CommentUpdated.action")
	public ModelAndView updated(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		int commentNum = Integer.parseInt(request.getParameter("commentNum"));
		String pageNum = request.getParameter("pageNum");
		
		int boardNum = Integer.parseInt(request.getParameter("num"));
		
	
		BoardCommentDTO dto = boardCommentService.getReadData(commentNum);
		
		if(dto==null) {
			
			mav.setViewName("redirect:BoardList.action?pageNum=" + pageNum + "&num=" + boardNum);
			
			return mav;
		}
		
		String param =  "pageNum=" + pageNum + "&num=" + boardNum ;
		// "boardNum=" + boardNum
	
		mav.addObject("dto", dto);
		mav.addObject("pageNum", pageNum);
		mav.addObject("params", param);
		mav.addObject("boardNum", boardNum);
		
		mav.setViewName("commentUpdated");
		
		return mav;
	}
	
	@RequestMapping(value = "/CommentUpdated_ok.action",method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView updated_ok(BoardCommentDTO dto,HttpServletRequest request) throws Exception{
	
		String pageNum = request.getParameter("pageNum");
		int boardNum = Integer.parseInt(request.getParameter("boardNum"));
		
	
		boardCommentService.updateData(dto);
		
		
		ModelAndView mav = new ModelAndView();
		
		String param =  "pageNum=" + pageNum + "&num=" + boardNum ;
		
		
		mav.setViewName("redirect:/BoardArticle.action?" + param);
							
		return mav;
		
	}
	
	
}
