package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Member;
import com.example.entity.Menu;
import com.example.entity.Review;
import com.example.repository.MemberRepository;
import com.example.repository.MenuRepository;
import com.example.repository.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
    private ReviewRepository reviewRepository;
	@Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MemberRepository memberRepository;

    public void createReview(Long menuId, Long id, int rating, String comment) {
    	
    	Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    	Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menu not found"));
    	Review review = new Review();
    	review.setMenu(menu);
    	review.setMember(member);
    	review.setRating(rating);
    	review.setComment(comment);
    	review.setCreatedAt(null);
    	reviewRepository.save(review);
    }

    public List<Review> getMenuReviews(Long menuId) {
        return reviewRepository.findByMenuId(menuId);
    }
    //추가 로직
}
