package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "review")
public class Review {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private int rating;
    private String comment;

    
    // 기본 생성자
    public Review() {
    }

    // 모든 필드를 초기화하는 생성자
    public Review(Menu menu, Member member, int rating, String comment) {
        this.menu = menu;
        this.member = member;
        this.rating = rating;
        this.comment = comment;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

    
}
