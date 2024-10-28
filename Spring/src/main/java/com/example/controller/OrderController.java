package com.example.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.OrderService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // 생성자 주입
public class OrderController {

    private final OrderService orderservice;

    @PostMapping("/ready")
    public Map<String, String> postReadyKakaoPayment(HttpServletResponse response) throws IOException {
        return orderservice.readyPayment();
    }
    /**
     *  카카오페이 결제 요청 후 사용자가 결제화면에서 비밀번호 까지 입력시, approval_url 로 pg_token(성공토큰)을 파라미터로 해서 보냄
     *  그 성공 요청을 받기위한 API
     *  최종적으로 승인 요청 API 실행
     */
    @GetMapping("/approve")
    public String getReadyKakaPayment(String pg_token, HttpServletRequest httpServletRequest){

        String tid = null; // 쿠키에서 찾을 결제번호
        // 쿠키
        Cookie[] cookies = httpServletRequest.getCookies();
        // 쿠키에서 tid라는 key를 가진 쿠키를 찾아서 tid에 set
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("tid".equals(cookie.getName())){
                    tid = cookie.getValue();
                    break; // tid를 찾은후 루프 종료
                }
            }
        }
        // 카카오 승인요청 API 호출
        String responseBody = orderservice.approvePayment(pg_token, tid);

        return responseBody;
    }
}