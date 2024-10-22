package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Review;
import com.example.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{menuId}")
    public String getReviews(@PathVariable("menuId") Long menuId, Model model) {
        List<Review> review = reviewService.getMenuReviews(menuId);
        model.addAttribute("reviews", review);
        return "review"; // reviews.html 파일로 이동
    }
    @PostMapping("/submitReview")
    public String submitReview(Long menuId, @RequestParam int rating, @RequestParam String comment, HttpSession session) {
        // 리뷰를 객체로 만들어서 저장하는 로직을 구현합니다.
    	Long id = (Long) session.getAttribute("id");
        reviewService.createReview(menuId, id, rating, comment); // 리뷰 저장 서비스 호출

        return "review"; // 리뷰 제출 후 다시 메인 페이지로 리다이렉트
    }
}
