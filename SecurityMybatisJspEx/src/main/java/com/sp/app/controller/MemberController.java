package com.sp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/member/*")
public class MemberController {
	// private final MemberService memberService;

	// login 폼은 GET으로 처리하며,
	// login 실패시 loginFailureHandler 에서 /member/login 으로 설정
	//     하여 POST로 다시 이 주소로 이동하므로 GET과 POST를 모두 처리하도록 주소를 매핑	
	@RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
	public String loginForm(@RequestParam(name = "login_error", required = false) String login_error, 
			Model model) {
		
		if(login_error != null) {
			model.addAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
		}
		
		return "member/login";
	}

	@GetMapping("member")
	public String memberForm(Model model) {
		model.addAttribute("mode", "member");
		return "member/member";
	}

	@GetMapping(value = "noAuthorized")
	public String noAuthorized() {
		return "member/noAuthorized";
	}

	@GetMapping(value="expired")
	public String expired() {
		return "member/expired";
	}
	
}
