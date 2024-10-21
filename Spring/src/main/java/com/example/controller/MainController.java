package com.example.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Member;
import com.example.service.MemberService;
import com.example.service.MenuService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Value("${kakao.client_id}")
    private String client_id;
	
	@Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MenuService menuService;
    


	/* TODO
	 * 식당 상태를 알려주는 여유, 혼잡 알고리즘 구현
	 *  리뷰 기능 구현
	 *  메뉴 검색, 추천 기능 구현
	 */
    @GetMapping("/")
    public String index(Model model) {
    	model.addAttribute("menus", menuService.getAllMenus());
        return "index";
    }
    
    @GetMapping("/login")
    public String kakaologin() {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        
        return "redirect:" + location;
    }
    
    @GetMapping("/logout")
    public String kakaologout(HttpSession session) throws IOException {
    	// 세션에서 accessToken 가져오기
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            // 카카오 로그아웃 서비스 호출
            try {
				memberService.logoutUser(accessToken);
				session.invalidate();
			} catch (IOException e) {
				log.error("Kakao logout failed: " + e.getMessage(), e); // 예외 로그 추가
			}
        }else {
            log.warn("No accessToken found in session."); // AccessToken이 없을 때 경고 로그 추가
        }
        return "redirect:/"; // 로그아웃 후 리다이렉트
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) throws IOException {
        String accessToken = memberService.getAccessTokenFromKakao(client_id, code);
        Member member = memberService.getUserInfo(accessToken);
        log.info("member id : {} connected", member.getId());
        session.setAttribute("id", member.getId());
         //로그인
        memberService.loginUser(member);
        session.setAttribute("accessToken", accessToken);
        return "redirect:/";
    }
}
