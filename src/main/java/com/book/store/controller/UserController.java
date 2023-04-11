package com.book.store.controller;

import java.io.PrintWriter;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.book.store.service.UserService;
import com.book.store.user.UserCreateForm;
import com.book.store.user.UserData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final HttpSession httpSession;

	//회원가입 페이지로 이동
		@GetMapping("/member")
		public ModelAndView test1() throws Exception {
			
			ModelAndView mav = new ModelAndView();
			
			mav.setViewName("membership");
			
			return mav;
		}
		
		//회원가입페이지에서 버튼누른후 데이터 입력
		@PostMapping("/member")
		public ModelAndView insertdata(UserCreateForm data) throws Exception {
			
			ModelAndView mav = new ModelAndView();
			
			UserData userdata = new UserData();
			
			userdata.setUserId(data.getUserId());
			userdata.setUserPwd(passwordEncoder.encode(data.getPassword1()));
			userdata.setUserName(data.getUserName());
			userdata.setUserAddr(data.getUserAddr() + "" + data.getAddr_detail());
			userdata.setUserEmail(data.getUserEmail());
			userdata.setUserBirth(data.getBirth_year()+"-"+data.getBirth_month()+"-"+data.getBirth_day());
			userdata.setUserTel(data.getUserTel());
			userdata.setRealPwd(data.getPassword1());
			
			userService.insertData(userdata);
			
			mav.setViewName("redirect:/user/hi");
			
			return mav;
			
		}
	
	@GetMapping("/oaumember")
	public ModelAndView oauthlogin() throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		UserData OauthUser = (UserData) httpSession.getAttribute("OauthUser");
		
		mav.addObject("OauthUser", OauthUser);
		
		mav.setViewName("membershipOau");
		
		return mav;
	}
	
	@PostMapping("/oaumember")
	public ModelAndView oauthgo(HttpServletRequest request) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		UserData oauthUser = (UserData) httpSession.getAttribute("OauthUser");
		
		String addr = request.getParameter("userAddr") +" "+ request.getParameter("addr_detail");
		String birth = request.getParameter("birth_year") +"-"+ request.getParameter("birth_month")+"-"+request.getParameter("birth_day");
		
		oauthUser.setUserAddr(addr);
		oauthUser.setUserEmail(request.getParameter("userEmail"));
		oauthUser.setUserTel(request.getParameter("userTel"));
		oauthUser.setUserBirth(birth);
		oauthUser.setRealPwd(request.getParameter("realPwd"));
		
		
		userService.insertData(oauthUser);
		httpSession.setAttribute("OauthUser", oauthUser);
		
		mav.setViewName("redirect:/user/hi");
		
		return mav;
	}
	
//	@GetMapping("/login")
//	public ModelAndView userlogin() {
//		ModelAndView mav = new ModelAndView();
//		
//		mav.setViewName("minsungTest2");
//		
//		return mav;
//		
//	}
	
	@GetMapping("/hi")
	public ModelAndView test() throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("minsungTest3");
		
		UserData user = (UserData) httpSession.getAttribute("user");
		UserData OauthUser = (UserData) httpSession.getAttribute("OauthUser");
		
		
		
		if(user!=null) {
			mav.addObject("userId", user.getUserId());
			mav.addObject("userPwd", user.getUserPwd());
			mav.addObject("userEmail", user.getUserEmail());
			mav.addObject("realPwd", user.getRealPwd());
			
		}

		//OAuth로 첫 로그인시 기타 회원정보 추가를 위해 가입페이지로 이동
		if(OauthUser!=null && OauthUser.getUserAddr()==null) {
		
			mav.setViewName("redirect:/user/oaumember");
			return mav;
		}
		
		
		if(OauthUser!=null) {
			mav.addObject("userId", OauthUser.getUserId());
			mav.addObject("userPwd", OauthUser.getUserPwd());
			mav.addObject("userEmail", OauthUser.getUserEmail());
			
			
		}
		
		return mav;
	}
	
	@GetMapping("/login")
	public ModelAndView mypage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		request.getParameter("name");
		mav.setViewName("login");
		
		
		return mav;
	}
	
	@GetMapping("findId")
	public ModelAndView findid() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("findId");
		
		return mav;
	}
	
	@PostMapping("findId")
	public ModelAndView findid_do(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		String findId = userService.findUserId(request.getParameter("userName"), request.getParameter("userTel"));
		
		if(findId!=null || !findId.equals("")) {
			String alert = "고객님의 아이디는 " + findId + "입니다.";
			mav.addObject("idAlert", alert);
		}
		
		String alert = "회원 정보가 없습니다.";
		mav.addObject("idAlert", alert);
		
		mav.setViewName("redirect:/user/login");
		
		return mav;
	}
	
}
