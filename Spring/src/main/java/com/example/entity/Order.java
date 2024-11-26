package com.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

	private LocalDateTime orderDate;

    private String status;
    
	private int total;
	
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
    
    // 기본 생성자 추가
    public Order() {
    	this.orderItems = new ArrayList<>(); // 생성자에서 초기화
    }
    public Order(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
        this.total = calculateTotalPrice(); // total 계산하여 초기화
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

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	
	public int calculateTotalPrice() {
	    int totalPrice = 0;

	    for (OrderItem orderItem : orderItems) {
	        // 메뉴의 가격과 수량을 곱하여 총 가격에 더합니다.
	    	totalPrice += orderItem.getMenu().getPrice() * orderItem.getQuantity();
	    }

	    return totalPrice;
	}
}