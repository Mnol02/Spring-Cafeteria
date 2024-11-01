package com.example.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

	@OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;
    
    
    // 기본 생성자 추가
    public Cart() {
    	this.cartItems = new ArrayList<>(); // 생성자에서 초기화
    }
    public Cart(Member member, List<CartItem> cartItems) {
        this.member = member;
        this.cartItems = cartItems;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public void addCartItem(CartItem cartItem) {
	    this.cartItems.add(cartItem);
	}
    
}
