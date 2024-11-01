package com.example.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.OrderService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 생성자 주입
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/ready")
    public void postReadyKakaoPayment(HttpServletResponse response) {
        // 약간의 로깅 추가
        System.out.println("Kakao Payment Ready Request Received");
        
        // 결제 준비를 요청하고 결과를 받음
        Map<String, String> result = orderService.readyPayment();
        
        Cookie tidCookie = new Cookie("tid", result.get("tid"));
        response.addCookie(tidCookie);
        
        // redirect_pc_url로 리다이렉트
        String redirectUrl = result.get("redirect_pc_url");
        if (redirectUrl != null) {
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                e.printStackTrace();
                // 적절한 오류 처리 (예: 전용 오류 페이지로 리다이렉트)
            }
        } else {
            // redirect_pc_url이 없을 때에는 BAD_REQUEST 응답 조치
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @GetMapping("/approve")
    public String getApproveKakaoPayment(@RequestParam("pg_token") String pg_token, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Kakao Payment Approve Request Received - pg_token: " + pg_token);
        String tid = null;

        // 쿠키에서 tid 찾기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("tid".equals(cookie.getName())) {
                    tid = cookie.getValue();
                    break;
                }
            }
        }

        // 카카오 승인 요청 API 호출
        String responseBody = orderService.approvePayment(pg_token, tid);

        // 결제 승인 처리 후 index 페이지로 리다이렉트
        try {
            response.sendRedirect("/"); // 원하는 리다이렉트 경로로 변경
            return null; // 리다이렉트가 완료되면 더 이상의 처리는 필요 없음
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error"; // 오류 시 다른 페이지로 리다이렉션
        }
    }
}